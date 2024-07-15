# 一、概述

MyBatis-Plus（简称 MP）使用一个MyBatis的增强工具，在MyBatis的基础上只做增强，不做改变，为简化开发、提高效率而生。其突出的特性如下：

* **无侵入**：只做增强不做改变，引入它不会对现有工程产生影响，如丝般顺滑。
* **强大的CRUD操作**：内置通用Mapper、通用Service，提供了大量的通用CRUD方法，因此可以省去大量手写sql语句的工作。
* **条件构造器**：提供了强大的条件构造器，可以构造各种复杂的查询条件，以应对各种复杂查询。
* **内置分页插件**：配置好插件之后，写分页等同于普通List查询，无需关注分页逻辑。

下面通过一个案例快速熟悉MyBatis Plus的基本使用。



# 二、数据准备

### 数据库准备

**创建数据库**

在MySQL中创建一个数据库hello_mp

```sql
CREATE DATABASE hello_mp CHARACTER;
```

**创建表**

在hello-mp库中创建一个表user

```sql
use hello_mp;
DROP TABLE IF EXISTS user;
CREATE TABLE user
(
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age INT(11) NULL DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    del_flag INT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id)
);
```

**插入数据**

```sql
INSERT INTO user (id, name, age, email) VALUES
(1, 'Jone', 18, 'test1@baomidou.com'),
(2, 'Jack', 20, 'test2@baomidou.com'),
(3, 'Tom', 28, 'test3@baomidou.com'),
(4, 'Sandy', 21, 'test4@baomidou.com'),
(5, 'Billie', 24, 'test5@baomidou.com');
```



### SpringBoot集成

MyBatis-Plus与SpirngBoot的集成十分简单，具体操作如下：

**1、创建项目**

创建一个项目，就叫做mybatis-plus。

创建完毕后，我们首先先去给该项目配置maven仓库：

<img src=".\images\image-20240617163758138.png" alt="image-20240617163758138" style="zoom: 50%;" /> 

使用本地的maven仓库来给其进行配置。

然后，我们需要将该项目变为SpringBoot项目，在项目的pom.xml中，使用parent标签，将其项目的父工程设置为spring-boot-parent-starter：

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.0.5</version>
    <relativePath />
</parent>
```

刷新maven，这样项目就变成了一个SpringBoot项目。

然后创建一个子工程hello-mybatis-plus，用来学习mybatis-plus。



**2、引入Maven依赖**

引入mybatis-plus的依赖：

```XML
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.3.1</version>
</dependency>
```

然后，引入其他依赖：

```xml
<dependencies>
    <!--web工程所需依赖-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!--整合aop-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>

    <!--整合mybatis-->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>3.0.1</version>
    </dependency>

    <!--mybatis-plus的依赖-->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.5.3.1</version>
    </dependency>

    <!-- 数据库相关配置启动器 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>

    <!-- druid启动器的依赖  -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-3-starter</artifactId>
        <version>1.2.22</version>
    </dependency>

    <!-- mysql驱动类-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>${mysql.version}</version>
    </dependency>

    <!--lombok-->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```





**3、配置application.yml文件**

在子项目的resources目录下，创建application.yml文件：

```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      url: jdbc:mysql://192.168.200.101/hello_mp?useUnicode=true&characterEncoding=utf-8
      username: root
      password: 061535asd
      driver-class-name: com.mysql.cj.jdbc.Driver

mybatis:
  configuration:  # setting配置
    map-underscore-to-camel-case: true #驼峰
  type-aliases-package: com.atguigu.pojo # 配置别名
  mapper-locations: classpath:/mapper/*.xml # mapperxml位置
```

这里我们还配置了mybatis的信息，为了保证在项目中也能够使用mybatis。





**创建启动类**

在子项目hello-mybatis-plush中，创建com.atguigu包，在该包下，创建Application类：

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```





# 三、创建实体类

创建与user表对应的实体类，如下：

```java
@Data
@TableName("user")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("age")
    private Integer age;

    @TableField("email")
    private String email;

    @TableLogic(value = "0", delval = "1")
    @TableField("del_flag")
    private Integer delFlag;
}
```

**注解的说明：**

1. **@TableName注解**:

   当我们要使用BaseMapper或ServiceImpl中的方法来执行SQL操作时，我们需要知道去操作的是哪一个数据表，那么@TableName注解就是用来确定数据表的。在不使用@TableName注解的情况下，默认操作的表名和实体类的类名一致。

   所以，可知**`@TableName`注解用来将Java类与数据库表建立一一对应的关系。**

   这样的话，我们才能通过BaseMapper或者ServiceImpl的泛型，找到实际要操作的表。



2. **@TableId注解：**

   `@TableId`注解是用于标识表中的主键字段所对应的实体类属性的。当表中的主键字段名与类中所对应的属性名不一致时，比如主键名使用id，表中对应的字段采用uId，那么此时我们使用MyBatis-Plus中的方法时，就无法识别该属性与字段的关系，此时会报错。所以我们需要使用一个注解用于给表中的字段和类中的属性建立映射关系，其中@TableId就是用于给主键建立映射关系的。

   所以，**`@TableId`是用于将类中的属性与数据表中的字段建立一一对应的映射关系。**

   在不使用@TableId的情况下，MyBatis-Plus默认使用驼峰命名法将实体类中的属性与数据表中的字段寻找一一对应关系；但如果想让名字不同的属性与字段之间建立映射关系，则需要添加注解。

   * **`type`属性说明：**

     @TableId的value属性建立映射关系，**type属性设置`主键的生成策略`**。

     如果没有使用@TableId来设置主键的生成策略，则默认使用IdType.NONE作为MyBatis-Plus的主键生成策略，此时和MyBatis主键生成策略一致：如果数据库配置了主键递增，使用主键递增；如果实体类中设置了值，使用属性值；如果都没有设置，则报错。

     但是，如果使用MyBatis-Plus来实现插入操作，就需要设置主键生成策略。

     type属性有五种类型：

     1. **`AUTO(0)`**

        策略：数据库自增

        使用：数据库ID自增，数据库需要设置主键自增。

     2. `NONE(1)`

        无策略，默认使用NONE。

        使用：如果数据库主键设置了自增，则使用数据库自增；如果Java类的主键属性设置了值，则默认使用该值；否则报错。

     3. `INPUT(2)`

        策略：用户自己输入。

        适用：特殊业务场景，需要特定主键。

     4. **`ASSIGN_ID(3)`**

        策略：全局唯一id，采用“雪花算法”生成全局唯一64为主键，主键类型可以是数值类型（bigint）或字符串类型。

        适用：当前大部分分布式应用，需要生成全局唯一id。

     5. `ASSIGN_UUID`

        策略：32位UUID

        适用：严格全局唯一ID

        缺点：占用数据库存储空间大，排序不方便。

     **比较常用的是ASSIGN_ID与AUTO。**



3. **@TableField注解**

   当我们使用实体类接收查询的结果，或者传入实体类类型的参数时，MyBatis-Plus默认会使用**驼峰命名法**，会将表字段与实体类进行一一映射。如表字段user_name，则对应的实体类属性为userName。但是当表字段与实体类属性名不满足这种映射关系时，此时我们就需要或使用@TableField来将实体类与表中字段关联起来。

   所以，**`@TableField`注解可以将实体类属性与数据表字段建立对应的映射关系。**



4. **@TableLogic注解**

   在实际项目中，我们一般会在数据表中添加一个`del_flag`字段，这个字段来表示该行数据是否已被逻辑删除。我们在实际开发中，删除数据不会真正地使用delete操作去删除，而是使用update操作去修改要删除行的del_flag字段，将其标识为已删除。然后，我们在查询时，就不会查询出已经逻辑删除的行数据。

   那么，在MyBatis-Plus中，给我们提供了这个@TableLogic注解，用于**标识类中属性所对应的字段是`逻辑删除字段`**。当我们使用MyBatis-Plus进行删除时，实际上进行的是逻辑删除，也就是将该字段设置为1。

   @TableLogic注解的`value`属性表示字段未被逻辑删除时的默认值，`delval`属性表示字段被逻辑删除时的值。







# 四、BaseMapper

在BaseMapper中，给我们提供了通用的CRUD方法，能够让我们不用自己编写大量简单重复的SQL工具即可直接使用。

> **注意：**Mapper层的方法是使用在service层的类上的。

## 用法说明

1. **创建Mapper接口**

   在mapper层，创建一个Mapper接口，比如我就在com.atguigu.mapper包下，创建一个UserMapper接口，并让该接口，去继承MyBatis-Plus所提供的`BaseMapper<T>`接口，其**泛型的类型是表所对应的实体类。**

   ```java
   @Mapper
   public interface UserMapper extends BaseMapper<User> {
   }
   ```

   在一般情况下，我们也会去创建UserMapper所对应的映射文件，当出现比较复杂的SQL，而BaseMapper中没有对应的方法时，我们就会去手写SQL来实现。

2. **在servie层使用**

   我们使用UserMapper接口去继承的BaseMapper，但是在UserMapper接口中无法使用，一般都是在service层使用。

   使用@Autowire注解注入Mapper接口使用：

   ```java
   @Service
   public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
       
       @Autowired
       private UserMapper userMapper;
       
   }
   ```

   

## BaseMapper中的方法

<img src=".\images\image-20240617200423003.png" alt="image-20240617200423003"  /> 

我们来看看BaseMapper中给我们定义了哪些方法：

我们可以看到，都是一些最基本的增删改查方法。



## 测试

在一般情况下，我们是在service层下，使用baseMapper这些方法，但是下面我们就在Test测试类中进行测试：

```java
@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectList() {
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    @Test
    public void testSelectById() {
        User user = userMapper.selectById(1);
        System.out.println(user);
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setName("zhangsan");
        user.setAge(11);
        user.setEmail("test@test.com");
        userMapper.insert(user);
    }

    @Test
    public void testUpdateById() {
        User user = userMapper.selectById(1);
        user.setName("xiaoming");
        userMapper.updateById(user);
    }
    
    @Test
    public void testDeleteById() {
        userMapper.deleteById(1);
    }
}
```







# 五、通用Service

通用Service进一步封装了通用Mapper的CRUD方法，并提供了例如`saveOrUpdate`、`saveBatch`等高级方法。

1. **创建Service接口**

   创建`UserService`，内容如下

   ```java
   public interface UserService extends IService<User> {
   }
   ```

2. **创建Service实现类**

   创建`UserServiceImpl`，内容如下

   ```java
   @Service
   public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
   }
   ```

3. **测试通用Service**

   创建`UserServiceImplTest`测试类，内容如下

   ```java
   @SpringBootTest
   class UserServiceImplTest {
   
   
       @Autowired
       private UserService userService;
   
       @Test
       public void testSaveOrUpdate() {
           User user1 = userService.getById(2);
           user1.setName("xiaohu");
   
           User user2 = new User();
           user2.setName("lisi");
           user2.setAge(27);
           user2.setEmail("lisi@email.com");
           userService.saveOrUpdate(user1);
           userService.saveOrUpdate(user2);
       }
   
   
       @Test
       public void testSaveBatch() {
           User user1 = new User();
           user1.setName("dongdong");
           user1.setAge(49);
           user1.setEmail("dongdong@email.com");
   
           User user2 = new User();
           user2.setName("nannan");
           user2.setAge(29);
           user2.setEmail("nannan@email.com");
   
           List<User> users = List.of(user1, user2);
           userService.saveBatch(users);
       }
   }
   ```





# 六、条件构造器

条件构造器用于构造复杂的查询条件，例如获取`name='zhangsan'`的用户。MyBatis Plus共提供了两类构造器，分别是`QueryWrapper`和`UpdateWrapper`。其中`QueryWrapper`主要用于查询、删除操作，`UpdateWrapper`主要用于更新操作，下面通过几个案例学习条件构造器的基础用法。

1. 创建`WrapperTest`测试类，内容如下

   ```java
   @SpringBootTest
   public class WrapperTest {
   
       @Autowired
       private UserService userService;
   
       @Test
       public void testQueryWrapper() {
   
           //查询name=Tom的所有用户
           QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
           queryWrapper1.eq("name", "Tom");
   
           //查询邮箱域名为baomidou.com的所有用户
           QueryWrapper<User> queryWrapper2 = new QueryWrapper<>();
           queryWrapper2.like("email", "baomidou.com");
   
           //查询所有用户信息并按照age字段降序排序
           QueryWrapper<User> queryWrapper3 = new QueryWrapper<>();
           queryWrapper3.orderByDesc("age");
           
           //查询age介于[20,30]的所有用户
           QueryWrapper<User> queryWrapper4 = new QueryWrapper<>();
           queryWrapper4.between("age", 20, 30);
           
           //查询age小于20或大于30的用户
           QueryWrapper<User> queryWrapper5 = new QueryWrapper<>();
           queryWrapper5.lt("age", 20).or().gt("age", 30);
   
           //邮箱域名为baomidou.com且年龄小于30或大于40且的用户
           QueryWrapper<User> queryWrapper6 = new QueryWrapper<>();
           queryWrapper6.like("email", "baomidou.com").and(wrapper -> wrapper.lt("age", 30).or().gt("age", 40));
           
           List<User> list = userService.list(queryWrapper6);
           list.forEach(System.out::println);
       }
   
       @Test
       public void testUpdateWrapper() {
   
           //将name=Tom的用户的email改为Tom@baobidou.com
           UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
           updateWrapper.eq("name", "Tom");
           updateWrapper.set("email", "Tom@baobidou.com");
   
           userService.update(updateWrapper);
       }
   }
   ```

2. 创建`LambdaWrapperTest`测试类，内容如下

   上述的`QueryWrapper`和`UpdateWrapper`均有一个`Lambda`版本，也就是`LambdaQueryWrapper`和`LambdaUpdateWrapper`，`Lambda`版本的优势在于，可以省去字段名的硬编码，具体案例如下：

   ```java
   @SpringBootTest
   public class LambdaWrapperTest {
   
       @Autowired
       private UserService userService;
   
       @Test
       public void testLambdaQueryWrapper() {
           //查询name=Tom的所有用户
           LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
           lambdaQueryWrapper.eq(User::getName, "Tom");
   
           List<User> list = userService.list(lambdaQueryWrapper);
           list.forEach(System.out::println);
   
       }
   
       @Test
       public void testLambdaUpdateWrapper() {
           //将name=Tom的用户的邮箱改为Tom@tom.com
           LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
           lambdaUpdateWrapper.eq(User::getName, "Tom");
           lambdaUpdateWrapper.set(User::getEmail, "Tom@Tom.com");
   
           userService.update(lambdaUpdateWrapper);
       }
   }
   ```





# 七、分页插件

MyBatis-Plus提供了一个分页插件，它能够让我们在使用MyBatis-Plus时使用分页查询。

## 1、配置分页插件

在MyBatis-Plus中使用分页插件，需要先去配置分页插件：

在com.atguigu.config包下，创建一个配置类，我们就叫做MPConfiguration：

```java
@Configuration
public class MPConfiguration {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //这里的DbType.MYSQL表示的是数据库的类型，使用不同的数据库需要更改
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```



## 2、构造分页对象

分页对象包含了各项信息，其核心属性如下：

* `records`：List类型属性，表示查询到的数据集合。
* `total`：Long类型属性，表示查询列表的总记录数。
* `size`：Long类型属性，表示每页显示条数，默认值是10。
* `current`：Long类型属性，表示当前是第几页，默认值是1。

分页对象既作为分页查询的参数，也作为分页查询的返回结果，当作为查询参数时，通常只需提供current和size属性，如下：

```java
IPage<T> page = new Page<>(current, size);
```

注：IPage为分页接口，Page为IPage接口的一个实现类。

在实际工作中，IPage一般这样创建：

```java
@RequestMapping(value = "/page1/{pageNu}/{pageSize}")
@ResponseBody
public IPage<User> pageQuery1(@PathVariable("pageNu") Integer pageNu,
                             @PathVariable("pageSize") Integer pageSize){
    IPage<User> page = new Page<>(pageNu, pageSize);
    return userService.page(page);
}
```

current也就是PageNumber，表示第几页；size也就是PageSize，表示每页有几条数据。





## 3、分页查询的实现

在MyBatis-Plus中，我们既可以使用BaseMapper与ServiceImpl中已经定义好的分页查询方法，也可以在MyBatis中的自定义SQL中使用分页。



### 使用已有的分页查询方法

在MyBatis-Plus的`BaseMapper`和`ServiceImpl`中，均提供了常用的分页查询方法，例如：

#### BaseMapper中的分页查询

```java
<P extends IPage<T>> P selectPage(P page, @Param("ew") Wrapper<T> queryWrapper);


<P extends IPage<Map<String, Object>>> P selectMapsPage(P page, @Param("ew") Wrapper<T> queryWrapper);
```

其中：

第一个方法：`selectPage()`，传入的一般是`Page<实体类>`类型的对象，泛型是实体类类型，用于根据条件查询表中的所有字段数据，并将查询的结果封装为Page分页数据返回。

第二个方法：`selectMapsPage()`，要求传入一个Page<Map<String, Object>>类型的分页对象，用于根据条件，查询数据表中指定字段的数据，并封装成Page<Map<String, Object>>类型的分页数据返回，即返回的对象中的records属性是List<Map<String, Object>>类型的。



**实现案例1：**

查询user表中的name和email字段，并且分页显示，每页显示2个数据，当前页为第一页：

```java
@RequestMapping(value = "/page1")
@ResponseBody
public IPage<Map<String, Object>> pageQuery1(){
    IPage<Map<String, Object>> page = new Page(1, 2);
    
    LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.select(User::getName, User::getEmail);
    
    return userMapper.selectMapsPage(page, lambdaQueryWrapper);
}
```

我这里将mapper中的方法放在了controller中，是方便来测试，实际开发中不能在controller中使用mapper。

访问的结果：

<img src=".\images\image-20240617223728488.png" alt="image-20240617223728488" style="zoom: 80%;" /> 





#### ServiceImpl中的分页查询

```java
//无条件分页查询
IPage<T> page(IPage<T> page);

//条件分页查询
IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper);
```

其中：

第一个方法`page(IPage<T> page)`，表示去查询表中的所有数据，并将查询结果封装成IPage对象。

第二个方法`page(IPage<T> page, Wrapper<T> queryWrapper)`，表示根据条件查询表中的所有数据，并将查询结果封装成IPage对象。



**实现案例：**

```java
//通用Service分页查询
@Test
public void testPageService() {
    Page<User> page = new Page<>(2, 3);
    Page<User> userPage = userService.page(page);
    userPage.getRecords().forEach(System.out::println);
}
```





### 自定义SQL中使用分页查询

对于自定义SQL，也可以十分方便的完成分页查询，如下

`Mapper`接口：

```java
IPage<UserVo> selectPageVo(IPage<?> page, Integer state);
```

`Mapper.xml`：

```java
<select id="selectPageVo" resultType="xxx.xxx.xxx.UserVo">
    SELECT id,name FROM user WHERE state=#{state}
</select>
```

**注意**：`Mapper.xml`中的SQL只需实现查询`list`的逻辑即可，无需关注分页的逻辑。

在自定义SQL中实现分页查询非常简单，在Mapper接口中，传入IPage的参数，并且返回值使用IPage类型，那么查询就会自动采用分页查询的方式。

需要注意的是：**如果Mapper接口的方法需要传入多个参数，那么IPage分页对象的参数一定是放在第一的位置。**



**实现案例**

例如：分页查询user表中id小于指定值的对象，每页2个数据，当前页是第一页。

```java
@RequestMapping(value = "/page3")
@ResponseBody
public IPage<User> pageQuery3(@RequestParam("id") Integer id){
    IPage<User> page = new Page<>(1, 2);
    return userMapper.selectPageVo(page, id);
}
```

这里是在controller里面去调用，实际上不可在controller层调用mapper层的方法。

Mapper接口

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {
    IPage<User> selectPageVo(IPage<User> page, @Param("id") Integer id);
}
```

我们只需要在Mapper接口中传入IPage对象，并且使用IPage对象接收，就会自动使用分页查询。



Mapper.xml映射文件

```java
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.atguigu.mapper.UserMapper">

    <select id="selectPageVo" resultType="com.atguigu.pojo.User">
        select *
        from user
        where id &lt; #{id}
    </select>
</mapper>
```

注意，这里的<小于号要使用转义字符：`&lt;`，否则会报错。

查询结果：

<img src=".\images\image-20240617235124716.png" alt="image-20240617235124716" style="zoom:80%;" /> 





# 八、MyBatisX插件

MyBatis Plus给我们提供了一个IDEA插件——MyBatisX，使用它可以根据数据库快速生成Entity、Mapper、Mapper.xml、Service、ServiceImpl等代码，使用户更专注于业务。

下面演示具体用法

1. **安装插件**

在IDEA插件市场中搜索MyBatisX，进行在线安装：

<img src=".\images\MyBatisX插件.png" style="zoom:67%;" />



2. **配置数据库连接**

在IDEA中配置数据库连接：

![image-20240617235806755](.\images\image-20240617235806755.png) 

<img src=".\images\image-20240618000048096.png" alt="image-20240618000048096" style="zoom:67%;" />

这里我去连接的是自己Linux虚拟机上的mysql。

点击OK，就在IDEA中连接上的mysql。



3. **生成代码**

首先将之前所编写的User、UserMapper接口、Mapper映射文件、UserService以及UserServiceImpl类全部删除。

然后按照下图过程使用插件生成代码：

<img src=".\images\生成代码.png" style="zoom: 67%;" /> 

配置实体类相关信息

![image-20240618001716192](.\images\image-20240618001716192.png)

这里分别要配置三个内容：

第一个，是选择要去配置的module，我们这里选择的是mybatis-plus这个父工程下的hello-mybatis-plus项目，注意，这里可能会出现选择包但是无效的现象，我们可能需要手动地输入子包名。

第二个，是填写要配置的基础包，由于我是将所有的包，都以com.atguigu作为父包，所以这里配置的是com.atguigu。

第三个，是去配置表所对应实体类的包名，我这里使用的是next。



下一步：配置代码模版信息

![image-20240618002028117](.\images\image-20240618002028117.png)

这里要去勾选三个选项：

第一个选项：MyBatis-Plus3，表示的是实体类选择的是哪一个版本的注解，由于我们使用的MyBatis-Plus版本是3，所以这里选择MyBatis-Plus3.

第二个选项：Lombok，这一部分选择的是实体类所包含的其他信息，这里包括了Comment也就是说明内容，第二个选项是toString、hashCode与equals()方法，我们勾上了Lombok选项，表示给类添加@Data注解。

第三个选项，是去创建service、mapper这两层结构的代码，我们选择使用mybatis-plus3版本创建。



点击Finish然后查看生成的代码。



**生成的代码需要修改的地方：**

1. 首先说明一下实体类：

<img src=".\images\image-20240618002601336.png" alt="image-20240618002601336" style="zoom:67%;" /> 

可以看到实体类中的属性，并没有使用上@TableField注解（当然如果属性名与字段名一致，可以不使用）

 ![image-20240618002647866](.\images\image-20240618002647866.png) 

但是del_flag表示逻辑删除的字段，需要使用@TableLogic注解，用于注明该字段表示逻辑删除。



2.Mapper接口

![image-20240618002821143](.\images\image-20240618002821143.png) 

如果没有使用@MapperScan去批量扫描Mapper接口，我们还需要去给Mapper接口使用@Mapper注解。



其他没有什么问题，和实际开发中一模一样，都是为我们继承了MyBatis-Plus的声明的类，并且建立了映射关系。

只不过，就是在类中，没有实际的方法，逆向工程中有生成相关方法。