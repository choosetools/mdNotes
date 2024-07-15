[TOC]

> **在mybatis中，mapper接口没有去创建相应的实现类，我们获取到的mapper接口对象是通过`JDK动态代理`，给我们去创建了一个代理实现类对象，从而可以去执行接口中定义的方法。**

# MyBatis可能所需的依赖

```xml
<!-- Mybatis核心 -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.7</version>
</dependency>


<!-- MySQL驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.30</version>
</dependency>

<!--分页插件-->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>5.2.1</version>
</dependency>
```





---

# 一、搭建MyBatis环境

## 开发环境

* IDE：idea 2023.2.3
* 构建工具：maven 3.6.3
* MySQL版本：MySQL 8.0.36
* JAVA：JDK 17
* MyBatis版本：MyBatis 3.5.7

* 数据库管理工具：Navicat 16



## 1、搭建项目

创建一个新的项目比如叫mybatis去学习mybatis中的内容，JDK选择的是JDK 17版本。

<img src=".\images\image-20240519182447330.png" alt="image-20240519182447330" style="zoom: 33%;" /> 

创建完成之后，去配置项目的maven：

在settings设置中，选择maven进行配置，在这里选择的是我们自己去下载的maven 3.6.3，具体的maven下载信息以及相关的配置请查看maven章节。

<img src=".\images\image-20240519182704573.png" alt="image-20240519182704573" style="zoom: 33%;" />  

创建出来的项目mybatis这个大工程中，一般不会去编写代码，大工程一般只是起到一个统领的作用。我们可以将mybatis中的src目录删除，并在其中创建一个子项目mybatis-demo1，用于进行具体的实现。



## 2、引入相关依赖

引入依赖包括MyBatis核心jar包，以及MySQL驱动，由于我的MySQL版本是8.0.36，所以我这里引入的MySQL依赖至少是8.0以上的。最后，再引入一个junit5的依赖来进行测试。

```xml
<dependencies>
    <!-- Mybatis核心 -->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.7</version>
    </dependency>

    <!-- MySQL驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.30</version>
    </dependency>

    <!--junit5测试-->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.3.1</version>
    </dependency>
</dependencies>
```

引入依赖，并刷新maven之后，我们就可以看到项目下的依赖信息：

![image-20240519183619367](.\images\image-20240519183619367.png) 





## 3、创建MyBatis的核心配置文件

MyBatis核心配置文件习惯上命名为`mybatis-config.xml`。

在之后整合SSM时，会去使用Spring给我们去进行配置，到时该配置文件就不再需要。

核心配置文件主要是用来配置连接数据库的环境以及MyBatis的全局配置信息

该配置文件需要存放在resources目录下。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>
    <!--设置连接数据库的环境-->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis"/>
                <property name="username" value="root"/>
                <property name="password" value="061535asd"/>
            </dataSource>
        </environment>
    </environments>

    <!--引入映射文件-->
    <mappers>
        <mapper resource="mappers/UserMapper.xml"/>
    </mappers>
</configuration>
```

上述文件中，配置了连接数据库的环境以及映射文件的信息，之后整合了SSM后，这些信息都可以存放在Spring的配置文件中。

上述的事务管理方式使用的是JDBC，所以事务的提交与回滚都需要手动进行管理。（整合了Spring之后，事务全部都交给Spring来进行管理，之前学习过，也就是@Transactional注解）



## 4、创建表与相应的实体类

在mybatis的数据库下，创建t_user表：

```mysql
create database mybatis;

use mybatis;

create table t_user(
	id INT PRIMARY KEY auto_increment,
	username VARCHAR(20),
	password VARCHAR(20),
	age INT,
	sex CHAR,
	email VARCHAR(20)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4;
```



在`com.atguigu.mybatis.pojo`包下，创建表相应的实体类User，在该类中，去创建相关属性，以及一些基本的方法。

在User类中我们需要去保证类中的属性名与表的字段名保持一致。

```java
public class User {
    private int id;
    private String username;
    private String password;
    private int age;
    private char sex;
    private String email;
    
   	//省略相应的get()、set()方法以及toString()方法
}
```

> **实体类存放在pojo包下**
>
> ![image-20240519200611981](.\images\image-20240519200611981.png) 



## 5、创建mapper接口

MyBatis中的mapper接口相当于以前的dao，对于dao来说，我们需要去创建一个dao接口，还需要去创建其实现类，通过在实现类中对数据库进行操作。

但是mapper接口与之的区别在于，mapper仅仅是接口，我们不需要提供实现类。

> **mapper接口存放在`mapper`包下**
>
> <img src=".\images\image-20240519200521513.png" alt="image-20240519200521513" style="zoom:67%;" /> 

在`com.atguigu.mybatis.mapper`包下，创建UserMapper接口，并创建一个添加用户信息的方法：

```java
public interface UserMapper {
    /**
     * 添加用户信息
     * @return
     */
    public int insertUser();
}
```

当我们操作数据库时，实际上是去操作Mapper接口中的方法。





## 6、创建映射文件

> **MyBatis映射文件相关规则**
>
> * 映射文件的命名：`实体类的类型 + Mapper.xml`
>
>   例如：表t_user，映射的实体类为User，则所对应的映射文件为UserMapper.xml
>
>   因此一个映射文件文件对应一个实体类，对应一张表的操作
>
> * MyBatis映射文件用于编写SQL，访问以及操作表中的数据
>
> * MyBatis映射文件存放的位置是`src/main/resources/mappers`目录下
>
>   <img src=".\images\image-20240519200540082.png" alt="image-20240519200540082" style="zoom: 80%;" /> 
>
> * MyBatis中可以面向接口操作数据，要保证两个一致：
>
>   * mapper接口的全类名和映射文件的命名空间（namespace)保持一致
>   * mapper接口中方法的方法名和映射文件中编写SQL的标签的id属性保持一致



在resources配置文件目录下，去创建一个mappers目录，专门用来存放MyBatis的映射文件。

在该mappers目录下，创建UserMapper接口所对应的映射文件UserMapper.xml，在其中的mapper标签中的namespace属性，去添加UserMapper接口的全路径，从而与该接口形成映射关系：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.atguigu.mybatis.mapper.UserMapper">
    
    <!--int insertUser();-->
    <insert id="insertUser">
        insert into t_user values(null,'张三','123',23,'女',"123@qq.com")
    </insert>
    
</mapper>
```



在创建完mappers目录，让所有Mapper映射文件都存放在该目录下后，我们需要在mybatis的核心配置文件中，引入映射文件的信息：

<img src=".\images\image-20240519210033198.png" alt="image-20240519210033198" style="zoom:67%;" />  



## 7、修改操作案例测试

在test目录下的java目录下，创建com.atguigu.mybatis.test包，在该包下创建MyBatisTest类进行测试：

那么我们首先需要去加载mybatis的核心配置文件，加载核心配置文件的第一步是去获取配置文件的字节输入流：

MyBatis给我们提供了一个`Resources`类（全类名为org.apache.ibatis.io.Resources），通过调用该类中的静态方法getResourceAsStream()，传入配置文件名，获取该配置文件的字节输入流：

```java
InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
```

然后，我们要根据这个字节输入流，去获取SqlSession的对象，获取该对象的步骤是：

1、生成SqlSessionFactoryBuilder对象

2、通过SqlSessionFactoryBuilder的build()方法，传入配置文件的字节输入流，获取SqlSessionFactory对象

3、调用SqlSessionFactory的openSession方法，获取SqlSession对象

```java
//创建SqlSessionFactoryBuilder对象
SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

//通过核心配置文件所对应的字节流创建工厂类，生产SqlSession对象
SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);

//创建SqlSession对象，这里传入参数true，表明通过SqlSession对象所操作的Sql都会自动提交
SqlSession sqlSession = sqlSessionFactory.openSession(true);
```

最后，我们通过代理模式，创建Mapper接口的代理实现类对象，从而去调用指定的方法：

```java
//通过代理模式创建UserMapper接口的代理实现类对象
UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

//调用方法操作数据库
userMapper.insertUser();
```

在默认的情况下，我们需要去手动提交事务，如果要自动提交事务，则在获取SqlSession对象时，使用SqlSessionFactory.openSession(true)，传入一个true，表示自动提交。

（当然，当我们整合Spring之后，直接使用@Transactional表示给指定的方法添加事务，在方法开始时开启事务，在方法结束后自动提交事务）

> **在mybatis中，mapper接口没有去创建相应的实现类，我们获取到的mapper接口对象是通过`JDK动态代理`，给我们去创建了一个代理实现类对象，从而可以去执行接口中定义的方法。**



**最终测试方法**

```java
@Test
public void testMyBatis() throws IOException {
    //1、加载核心配置文件
    InputStream is = Resources.getResourceAsStream("mybatis-config.xml");

    //2、创建SqlSession对象
    SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
    //传入true，表示事务自动提交
    SqlSession sqlSession = sqlSessionFactory.openSession(true);

    //3、创建Mapper接口的代理类对象，并执行数据库操作方法
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    int result = userMapper.insertUser();
    System.out.println(result);
}
```

**执行结果**

![image-20240519210426085](.\images\image-20240519210426085.png) 

方法返回的结果是1，表示执行操作数据库中受到影响的行数是1。

在学习MySQL时，我们知道，在增删改操作返回结果，就是受到影响的行数。

那么我们打开t_user表查看一下：

![image-20240519210150492](.\images\image-20240519210150492.png) 

成功往t_user表中插入一条数据。

> **SqlSession默认不自动提交数据。**
>
> 如果我们在创建SqlSession对象时，默认是不会自动提交数据的，当我们对数据表进行修改后，想要提交事务，就需要调用`sqlSession.commit()`手动提交事务。
>
> 如果我们在创建SqlSession时，传入一个true参数，即：sqlSessionFactory.openSession(`true`)，此时创建的SqlSession对象会去自动提交数据。



Spring整合MyBatis时，Spring是去使用了FactoryBean机制帮助我们去创建一个SqlSessionFactory对象，从而在Spring配置文件中，对mybatis进行整合，不需要mybatis的配置映射文件，我们现在可以先来看看：

```xml
<!-- spring和MyBatis整合，不需要mybatis的配置映射文件 -->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <!-- 自动扫描xml文件 -->
    <property name="mapperLocations" value="classpath:mapper/*.xml"></property>
</bean>
```







## 8、查询操作案例测试

**案例：查询t_user表中指定id的记录信息**

先去在UserMapper接口中创建根据id获取用户的方法：getUserById()，使用User对象接收查询的结果：

```java
/**
 * 根据id去获取用户信息
 * @param id
 * @return
 */
public User getUserById(Integer id);
```

然后，我们需要去UserMapper接口所对应的映射文件UserMapper.xml中，创建相应的SQL进行映射：

使用select标签表明当前的SQL是SELECT查询操作，标签中使用`resultType`属性表明接收查询信息的类型。在SQL中使用**`#{参数名}`**来将指定的方法参数传入到SQL中：

```xml
<select id="getUserById" resultType="com.atguigu.mybatis.pojo.User">
    SELECT *
    FROM t_user
    WHERE id = #{id}
</select>
```

> **查询的标签select必须设置属性resultType或resultMap，用于设置实体类和数据库表的映射关系。**
>
> 这两个属性是去指定将该如何去存查询出来的结果。
>
> * `resultType`：自动映射，用于属性名和表中字段名一致的情况；
> * `resultMap`：自定义映射，用于一对多或多对一或字段名和属性名不一致的情况。
>
> 当查询的数据为多条时，不能使用实体类作为返回值，只能使用集合，否则会抛出异常；但是若查询的数据只有一条，可以使用实体类或集合作为返回值。

测试：

```java
@Test
public void testMyBatis() throws IOException {
    //1、加载核心配置文件
    InputStream is = Resources.getResourceAsStream("mybatis-config.xml");

    //2、创建SqlSession对象
    SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
    SqlSession sqlSession = sqlSessionFactory.openSession(true);

    //3、创建Mapper接口的代理类对象，并执行数据库操作方法
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    User user = userMapper.getUserById(1);
    System.out.println(user);
}
```

执行结果：

![image-20240519223824673](.\images\image-20240519223824673.png) 





## 9、封装SqlSession对象获取过程

通过SqlSession对象去获取到Mapper接口的代理实现类对象，这一过程比较麻烦，需要先将核心配置文件转换成字节输入流的形式，然后创建SqlSessionFactoryBuilder对象，该对象通过build()方法使用字节输入流来创建SqlSessionFactory对象，通过该工厂去创建SqlSession对象。

```java
InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
SqlSession sqlSession = sqlSessionFactory.openSession(true);
```

这些过程我们可以封装在一个工具类里面，通过该工具类，传入mybatis的核心配置文件，从而直接去获取到SqlSession对象，就不需要我们每次都手动去创建了。

**过程如下：**

1、在工程中创建utils包，在utils包下创建SqlSessionUtils工具类：

<img src=".\images\image-20240520122021880.png" alt="image-20240520122021880" style="zoom:67%;" /> 

2、在SqlSessionUtils工具类中，创建一个静态方法，返回SqlSession对象，传入核心配置文件的文件名：

```java
public class SqlSessionUtils {
    public static SqlSession getSqlSession(String fileName){
        
        SqlSession sqlSession = null;
        try {
            InputStream is = Resources.getResourceAsStream(fileName);
            
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            
            //传入参数为true，表示事务自动开启与提交
            sqlSession = sqlSessionFactory.openSession(true);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sqlSession;
    }
}
```

这样一来，我们就可以直接通过该工具类获取到SqlSession的对象，测试：

```java
@Test
public void test01() throws IOException {
    SqlSession sqlSession = SqlSessionUtils.getSqlSession("mybatis-config.xml");

    ParameterMapper parameterMapper = sqlSession.getMapper(ParameterMapper.class);

    //获取User表中所有数据的方法
    for (User user : parameterMapper.getAllUsers()) {
        System.out.println(user);
    }
}
```

执行成功。







## 10、整合log4j2日志功能

#### Log4j2日志概述

在项目开发中，日志十分重要，不管是记录运行情况还是定位线上问题，都离不开对日志的分析。日志记录了系统行为的时间、地点、状态等相关信息，能够帮助我们了解并监控系统状态，在发生错误或者接近某种危险状态时能够及时提醒我们处理，同时在系统产生问题时，能够帮助我们快速的定位、诊断并解决问题。

**Apache Log4j2**是一个开源的日志记录组件，使用非常的广泛。在工程中以易用、方便代替了System.out等打印语句，它是Java下流行的日志输入工具。

**Log4j2主要由几个重要的组件构成：**

* **日志信息的优先级**。日志细腻的优先级由低到高有：**`TRACE < DEBUG < INFO < WARN < ERROR < FATAL`**

  * TRACE：追踪，是最低的日志级别，相当于追踪程序的执行
  * DEBUG：调试，一般在开发中，都将其设置为最低的日志级别
  * INFO：信息，输出重要的信息，使用较多
  * WARN：警告：输出警告的信息
  * ERROR：错误，输出错误信息
  * FATAL：严重错误

  这些级别分别用来指定这条日志信息的重要程度；级别高的会自动屏蔽级别低的日志，也就是说，设置了WARN的日志，则INFO、DEBUG的日志级别的日志不会显示。

* **日志信息的输出目的地**，日志信息的输出目的地指定了日志将打印到控制台还是文件中；

* **日志信息的输出格式**，输出格式控制了日志信息的显示内容。



#### 引入Log4j2依赖

在pom.xml中，引入Log4j2的依赖，以下是引入依赖的信息：

```xml
<!--log4j2的依赖-->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.19.0</version>
</dependency>

<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-slf4j2-impl</artifactId>
    <version>2.19.0</version>
</dependency>
```





#### 创建日志配置文件

在resources目录下，创建`log4j2.xml`配置文件（文件名是固定的，且必须存放到resources目录下）：

![image-20240519211832236](.\images\image-20240519211832236.png) 

然后，将下面的内容直接复制到配置文件中，需要注意的是，log4j2.xml配置文件中的配置信息是固定的，不需要我们学会怎么敲，只需要搞懂每行的含义，知道该如何修改即可。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <loggers>
        <!--
        level指定日志级别，从低到高的优先级：
        TRACE < DEBUG < INFO < WARN < ERROR < FATAL
        	trace：追踪，是最低的日志级别，相当于追踪程序的执行
            debug：调试，一般在开发中，都将其设置为最低的日志级别
            info：信息，输出重要的信息，使用较多
            warn：警告，输出警告的信息
            error：错误，输出错误信息
            fatal：严重错误
        -->
        <root level="DEBUG">
            <appender-ref ref="spring6log"/>
            <appender-ref ref="RollingFile"/>
            <appender-ref ref="log"/>
        </root>
    </loggers>

    
    <appenders>
        <!--输出日志信息到控制台-->
        <console name="spring6log" target="SYSTEM_OUT">
            <!--控制日志输出的格式-->
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss SSS} [%t] %-3level %logger{1024} - %msg%n"/>
        </console>

        
        <!--
		表示在文件中输出
		文件会打印出所有信息，这个log每次运行程序会自动清空，由append属性决定，适合临时测试用-->
        <File name="log" fileName="d:/spring6_log/test.log" append="false">
            <!--输出的格式-->
            <PatternLayout pattern="%d{HH:mm:ss.SSS} %-5level %class{36} %L %M - %msg%xEx%n"/>
        </File>

        
        <!-- 
			这也是表示在文件中输出
			这个会打印出所有的信息，
            每次大小超过size，
            则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档
		-->
        <RollingFile name="RollingFile" fileName="d:/spring6_log/app.log"
                     filePattern="log/$${date:yyyy-MM}/app-%d{MM-dd-yyyy}-%i.log.gz">
            <PatternLayout pattern="%d{yyyy-MM-dd 'at' HH:mm:ss z} %-5level %class{36} %L %M - %msg%xEx%n"/>
            <SizeBasedTriggeringPolicy size="50MB"/>
            <!-- DefaultRolloverStrategy属性如不设置，
            则默认为最多同一文件夹下7个文件，这里设置了20 -->
            <DefaultRolloverStrategy max="20"/>
        </RollingFile>
    </appenders>
</configuration>
```

设置了两个日志打印的文件：test.log与app.log。

test.log与app.log的区别在于：test.log每次去执行都会清空日志文件中的内容，其只会保存最近一次程序执行的日志；而app.log则会保存每一次程序执行时的日志信息。



**测试**

我们再去调用一下测试方法：

![image-20240519211928232](.\images\image-20240519211928232.png) 

此时就有相应的日志信息打印出来。









---

# 二、核心配置文件内容（了解）

核心配置文件都是固定的内容，我们使用的时候只需要复制一遍，然后将其中的内容修改一下就好了。

由于在后面我们去整合SSM时，mybatis中的核心配置文件内容都可以在spring配置文件中完成，所以这里核心配置文件的内容我们只要进行了解一下即可。

> **核心配置文件中的标签必须按照固定的顺序进行编写**（有点标签可以不写，但顺序一定不能乱）：
>
> `properties `
>
> `settings `
>
> `typeAliases `
>
> `typeHandlers `
>
> `objectFactory `
>
> `objectWrapperFactory `
>
> `reflectorFactory `
>
> `plugins `
>
> `environments `
>
> `databaseIdProvider `
>
> `mappers`

对于上述这些标签，我们在学到具体的内容时再来说，这里我们主要了解一下其中的四个标签：environment、properties、typeAliases与mappers。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//MyBatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--引入properties文件，此时就可以${属性名}的方式访问属性值-->
    <properties resource="jdbc.properties"></properties>
    
    
    <settings>
        <!--将表中字段的下划线自动转换为驼峰-->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <!--开启延迟加载-->
        <setting name="lazyLoadingEnabled" value="true"/>
    </settings>
    
    
    <typeAliases>
        <!--
        typeAlias：设置某个具体的类型的别名
        属性：
        type：需要设置别名的类型的全类名
        alias：设置此类型的别名，且别名不区分大小写。若不设置此属性，该类型拥有默认的别名，即类名
        -->
        <!--<typeAlias type="com.atguigu.mybatis.bean.User"></typeAlias>-->
        <!--<typeAlias type="com.atguigu.mybatis.bean.User" alias="user">
        </typeAlias>-->
        <!--以包为单位，设置改、该包下所有的类型都拥有默认的别名，即类名且不区分大小写-->
        <package name="com.atguigu.mybatis.bean"/>
    </typeAliases>
    
    
    <!--
    environments：设置多个连接数据库的环境
    属性：
	    default：设置默认使用的环境的id
    -->
    <environments default="mysql_test">
        <!--
        environment：设置具体的连接数据库的环境信息
        属性：
	        id：设置环境的唯一标识，可通过environments标签中的default设置某一个环境的id，表示默认使用的环境
        -->
        <environment id="mysql_test">
            <!--
            transactionManager：设置事务管理方式
            属性：
	            type：设置事务管理方式，type="JDBC|MANAGED"
	            type="JDBC"：设置当前环境的事务管理都必须手动处理
	            type="MANAGED"：设置事务被管理，例如spring中的AOP
            -->
            <transactionManager type="JDBC"/>
            <!--
            dataSource：设置数据源
            属性：
	            type：设置数据源的类型，type="POOLED|UNPOOLED|JNDI"
	            type="POOLED"：使用数据库连接池，即会将创建的连接进行缓存，下次使用可以从缓存中直接获取，不需要重新创建
	            type="UNPOOLED"：不使用，即每次使用连接都需要重新创建
	            type="JNDI"：调用上下文中的数据源
            -->
            <dataSource type="POOLED">
                <!--设置驱动类的全类名-->
                <property name="driver" value="${jdbc.driver}"/>
                <!--设置连接数据库的连接地址-->
                <property name="url" value="${jdbc.url}"/>
                <!--设置连接数据库的用户名-->
                <property name="username" value="${jdbc.username}"/>
                <!--设置连接数据库的密码-->
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>
    
    
    
    <!--引入映射文件-->
    <mappers>
        <!-- <mapper resource="UserMapper.xml"/> -->
        <!--
        以包为单位，将包下所有的映射文件引入核心配置文件
        注意：
			1. 此方式必须保证mapper接口和mapper映射文件必须在相同的包下
			2. mapper接口要和mapper映射文件的名字一致
        -->
        <package name="com.atguigu.mybatis.mapper"/>
    </mappers>
</configuration>
```

在SSM整合之后，mybatis就不再需要提供数据源了，因为在spring中会有数据源。上述的environments标签和mappers标签就不再需要提供了，直接在spring配置文件中完成配置，我们只需要在该配置文件中，完成mapper层需要进行的配置。



### 细节问题

**1、typeAliases标签设置表名的作用是什么？**

查询数据库中的数据，我们在映射文件中，需要使用resultType与resultMap将查询出来的结果映射成类对象进行接收，此时就需要在resultType与resultMap中使用类的全类名来指定，如：

```xml
<select id="getUserById" resultType="com.atguigu.mybatis.pojo.User">
    SELECT *
    FROM t_user
    WHERE id = #{id}
</select>
```

这里的resultType属性就是去声明了User的全类名来接收查询到的结果。

但是，如果每次都去声明一个全类名就有些太麻烦了，typeAliases标签就是提供给我们，让我们去给指定的类取一个别名（默认是类名，不区分大小写），让我们在resultType与resultMap中使用时直接使用类的别名即可。

例如：

```java
<typeAliases>
	<!--以包为单位，设置改、该包下所有的类型都拥有默认的别名，即类名且不区分大小写-->
	<package name="com.atguigu.mybatis.pojo"/>
</typeAliases>
```

这就是表示将com.atguigu.mybatis.pojo包下所有的类都使用自身的类名作为别名，供映射文件使用。此时，映射文件中的查询操作就可以修改成：

```xml
<select id="getUserById" resultType="User">
    SELECT *
    FROM t_user
    WHERE id = #{id}
</select>
```





**2、在mappers标签中，使用package批量引入映射文件的要求是什么？**

> * **`1.要求mapper接口和mapper映射文件必须在相同的包下`**
>
> * **`2.mapper接口要和mapper映射文件的名字一致。`**

在resources目录下，就需要去根据Mapper接口所在的包，一层一层地去创建目录，然后将mapper映射文件存放在包对应的目录下，才可以去使用package属性批量引入映射文件。

注意，在resources目录中右键，是没有包选项去创建的，我们只能通过选择创建Directory文件夹的方式，来确保mapper映射文件存放在与Mapper接口相同的目录下（因为包实际就是目录），并且使用这种方式创建不能使用.的方式来表示一层目录（包可以），但是可以使用/斜线来表示一层，因为创建的是文件目录：

<img src=".\images\image-20240520014830228.png" alt="image-20240520014830228" style="zoom: 50%;" /> 

<img src=".\images\image-20240520015328720.png" alt="image-20240520015328720" style="zoom:50%;" /> 

然后再在其中创建mapper映射文件，此时Mapper接口就与对应的mapper映射文件在同一个目录下了：

<img src=".\images\mapper接口和mapper映射文件在同一包下.png" style="zoom:67%;" /> 

---

# 注意：不能在Mapper接口中声明重载的方法！

由于在mapper映射文件中，我们是根据SQL标签的id属性与Mapper接口中的方法产生映射关系，如：

```java
public User checkLogin(@Param("username") String username, @Param("password") String password);
```

```xml
<select id="checkLogin" resultType="com.atguigu.mybatis.pojo.User">
    select * from t_user where username = #{username} and password = #{password}
</select>
```

是根据select标签中的id属性，与Mapper接口中的方法产生映射关系的。

如果在Mapper接口中去创建了多个重载的方法，比如有两个checkLogin()方法，次数是在mapper映射文件中，一个SQL就会有两个方法与之关联，那么去调用其中任意一个方法都会去调用同一个SQL标签，这样就可能会造成问题。





---

# 三、获取参数值的两种方式

## 1、#{}和${}

获取参数值的方式，也就是在映射文件中，mapper接口中传入的参数该如何获取的方式。

**MyBatis获取参数值的两种方式**：**`${}`**和**`#{}`**

* **`${}`的本质就是字符串拼接**

* **`#{}`的本质就是占位符赋值。**

当我们使用${}时，需要注意可能会造成SQL注入，若为字符串类型或日期类型的字段进行赋值时，需要手动加单引号。

使用#{}是占位符赋值的方式，不会产生SQL注入问题，并且为字符串或日期类型的字段进行赋值时，可以自动添加单引号。

> 所以，我们在实际开发中，**${}一般用于去拼接SQL**，当SQL语句不是固定的，而是根据实际业务情况改编时，我们就可以使用${}的方式拼接SQL语句；
>
> **#{}一般用于参数的传递**，当我们要根据某个参数查询或去设置指定参数的值时，就会使用#{}去传入指定的参数。





---

## 2、使用参数的情况

### 单个参数

若mapper接口中的方法参数为单个的字面量类型，此时可以使用${}和#{}使用参数名称的方式获取参数的值，注意${}需要手动加单引号。

```xml
<!--public User getUserbyUsername(String username);-->
<select id="getUserByUsername" resultType="com.atguigu.mybatis.pojo.User">
	select * from t_user where username = #{username}
</select>
```

```xml
<!--public User getUserbyUsername(String username);-->
<select id="getUserByUsername" resultType="com.atguigu.mybatis.pojo.User">
	select * from t_user where username = '${username}'
</select>
```



### 多个参数（易错点）

注意！这里是一个易错点，我们先来演示一下错误的写法：

创建一个验证用户登录的方法，也就是去根据用户的username和password去获取用户的方法，先去创建接口方法：

```java
public interface ParameterMapper {
    /**
     * 验证登录的方法
     */
    public User checkLogin(String username, String password);
}
```

然后去创建对应的Mapper映射文件SQL：

```xml
<select id="checkLogin" resultType="com.atguigu.mybatis.pojo.User">
    select * from t_user where username = #{username} and password = #{password}
</select>
```

咋一看，没有任何问题，我们去测试一下，获取ParameterMapper的代理实现类对象，并且执行checkLogin()：

```java
SqlSession sqlSession = SqlSessionUtils.getSqlSession("mybatis-config.xml");

ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);

User user = mapper.checkLogin("cheng", "061535");

System.out.println(user);
```

执行结果：

![image-20240520133020609](.\images\image-20240520133020609.png)

报错了！报的结果是username这个参数找不到，也就是在映射文件中，我们所使用的参数#{username}与#{password}这个参数是不存在的。

这里也就是与@Param注解产生了记忆上的冲突，我们在工作中，对于这种场景所直接使用的参数名，是因为使用了@Param注解，而对于多个参数的调用，正确写法是：



**正确写法**

当mapper接口中的方法有多个时，此时mybatis会自动将这些参数放在map集合中：

```
1.以arg0,arg1...为键，以参数为值
2.以param1,param2...为键，以参数为值
```

使用arg或者param都可以，需要注意的是，arg以0开始，param以1开始。（注意${}需要手动加单引号）

```xml
<!--User checkLogin(String username,String password);-->
<select id="checkLogin" resultType="User">  
	select * from t_user where username = #{arg0} and password = #{arg1}  
</select>
```

```xml
<!--User checkLogin(String username,String password);-->
<select id="checkLogin" resultType="User">
	select * from t_user where username = '${param1}' and password = '${param2}'
</select>
```

```xml
<!--User checkLogin(String username,String password);-->
<select id="checkLogin" resultType="User">
	select * from t_user where username = #{arg0} and password = '${param2}'
</select>
```

但是这种写法实在不能起到见名知意，所以建议：

> **无论是什么样的情况，都直接使用`@Param`注解来给参数取别名的方式使用。**





### map参数

若mapper接口中的方法需要的参数为多个时，此时可以手动创建map集合，将做这些数据放在map中，当然原本的参数就是map也是适用的。

此时只需要通过${}和#{}访问map集合中的键就可以获取相对应的值，注意${}需要手动加单引号。

```java
<!--User checkLoginByMap(Map<String,Object> map);-->
<select id="checkLoginByMap" resultType="User">
	select * from t_user where username = #{username} and password = #{password}
</select>
```

```java
@Test
public void checkLoginByMap() {
	SqlSession sqlSession = SqlSessionUtils.getSqlSession();
	ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
	Map<String,Object> map = new HashMap<>();
	map.put("usermane","admin");
	map.put("password","123456");
	User user = mapper.checkLoginByMap(map);
	System.out.println(user);
}
```



### 实体类参数

若mapper接口中的方法参数为实体类对象时此时可以使用${}和#{}，通过访问实体类对象中的属性名获取属性值，注意${}需要手动加单引号。

```xml
<!--int insertUser(User user);-->
<insert id="insertUser">
	insert into t_user values(null,#{username},#{password},#{age},#{sex},#{email})
</insert>
```

```java
@Test
public void insertUser() {
	SqlSession sqlSession = SqlSessionUtils.getSqlSession();
	ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
	User user = new User(null,"Tom","123456",12,"男","123@321.com");
	mapper.insertUser(user);
}
```



### @Param注解（推荐）

可以通过@Param注解标识mapper接口中的方法参数，此时，会将这些参数放在一个map集合中，就类似于mapper接口的方法中有多个参数的情况，此时map集合中key和value的值：

```
1. 以@Param注解的value属性值为键，以参数为值；
2. 以param1,param2...为键，以参数为值；
```

也就是说，原本的arg0、arg1...使用@Param注解中的value进行代替。

只需要通过${}和#{}访问map集合中的键就可以获取相对应的值，注意${}需要手动加单引号。

```xml
<!--User CheckLoginByParam(@Param("username") String username, @Param("password") String password);-->
    <select id="CheckLoginByParam" resultType="User">
        select * from t_user where username = #{username} and password = #{password}
    </select>
```

```java
@Test
public void checkLoginByParam() {
	SqlSession sqlSession = SqlSessionUtils.getSqlSession();
	ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
	mapper.CheckLoginByParam("admin","123456");
}
```

@Param注解会将这些修饰的参数存放到map集合中，我们去使用#{参数名}的方式去调用参数，实际上就是去调用map集合中的key键值，从而获取到对应的value值。这实际上是与没有使用@Param的多参数mapper方法是一样的，它也会将参数存入到map集合中，只不过此时使用的key是arg0、arg1...，而使用了@Param注解的话，此时使用的key就是@Param注解中的value属性值。



结论：建议所有的参数，都去使用`@Param`注解进行标识，@Param注解可以解决任何情况。









---

# 四、SQL详解（:star2:）

## 1、各种查询

> **注意：**
>
> * 当查询到的结果使用`List`集合来接收时，resultType中的是集合的`泛型类型`；
>
> * 当查询到的结果使用`Map`集合来接收时，resultType中的是Map的全类名，即`java.util.Map`。

### 查询实体类对象

MyBatis查询的数据只有一条，可以通过实体类对象来接收结果。

```java
/**
 * 根据用户id查询用户信息
 * @param id
 * @return
 */
User getUserById(@Param("id") int id);
```

```java
<!--User getUserById(@Param("id") int id);-->
<select id="getUserById" resultType="com.atguigu.mybatis.pojo.User">
	select * from t_user where id = #{id}
</select>
```

此时，查询出来的结果就会使用一个User对象进行接收。





---

### 查询List集合

MyBatis查询的数据有多条，一定不能通过实体类对象接收，我们可以使用List集合进行接收。

```java
/**
 * 查询所有用户信息
 * @return
 */
List<User> getUserList();
```

```java
<!--List<User> getUserList();-->
<select id="getUserList" resultType="User">
	select * from t_user
</select>
```

此时，查询出来的结果就会去使用一个`List<User>`集合进行接收，t_user数据表中的每一行记录都会使用List集合中的User类型元素来进行接收，字段名会与类中的属性名匹配，将与属性名匹配的字段值赋给该属性。

> **注意：**虽然使用List集合来接收数据，但是`resultType`中使用的是**泛型的类型**，也就是User类型，而不是List类的全类名。



---

### 查询单个数据

例如，我们要去查询t_user表中的总记录数：

创建Mapper接口中的方法：

```java
/**
 * 查询t_user表中的总记录数
 */
public int getCount();
```

创建对应的mapper映射文件的SQL标签：

```xml
<select id="getCount" resultType="java.lang.Integer">
    select count(*)
    from t_user
</select>
```

这里的resultType使用的是基本数据类型的包装类的全类名，包括返回值是String以及其他基本数据类型一样，都是使用其包装类的全类名进行接收。

虽然MyBatis也给我们的基本数据类型的包装类提供了简化的别名，但是还是直接使用全类名比较好。



---

### 查询一条map

当我们去查询数据表中的数据，想要使用map集合来接收时，例如：

```java
Map<String, Object> getUserToMapById();
```

对应的mapper映射文件：

```java
<select id="getUserToMapById" resultType="java.util.Map">
    select *
    from t_user
    where id = #{id}
</select>
```

注意，此时的resultType的resultType类型中使用的是Map的全类名。

然后，我们去测试一下看一下结果：

```java
SqlSession sqlSession = SqlSessionUtils.getSqlSession("mybatis-config.xml");
SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);

Map<String, Object> map = mapper.getUserToMapById(1);
System.out.println(map.toString());
```

执行结果：

![image-20240520182107977](.\images\image-20240520182107977.png) 

从执行结果我们可以得知：

使用Map集合对象去接收查询的数据，是将查询得到的列名作为Map集合元素的key，值作为Map集合元素的value。

> **要求：**
>
> 1、使用Map集合去接收查询到数据，要求泛型为**`<String, Object>`**，由于是将列名作为Map集合的key，所以key的泛型是String类型；列中的值作为Map集合的value，value可能是各种类型，所以使用Object进行接收。
>
> 2、使用Map集合接收数据，查询结果只能是一条数据，如果查询结果是多条数据，需要在外面套一层List集合才能接收，即List<Map<String, Object>>。

**应用场景：**

在实际开发中，当我们查询出来的数据，没有任何一个实体类与之对应时，那么我们就可以使用Map集合数据去接收，这样的话，我们就不用专门去创建一个实体类进行接收。



---

### 查询多条map

假如查询结果有多个记录时，此时若想使用map集合去接收，例如，查询所有用户信息并使用map集合接收：

**方式一：使用泛型是Map集合类型的List集合接收（推荐）**

这种方法，是将查询到的多条记录使用多个map进行存储。

查询的结果的每一条数据都对应一个map，数据的列名对应map中的key，列值对应map中的value，那么多个map放在一个List集合中获取。

即，Mapper接口中的方法：

```java
List<Map<String, Object>> getAllUserToMap();
```

对应的mapper映射文件：

```xml
<select id="getAllUserToMap" resultType="java.util.Map">
    select *
    from t_user
</select>
```

此时，我们去执行该操作，得到的结果为：

<img src=".\images\image-20240520184605494.png" alt="image-20240520184605494" style="zoom:67%;" /> 

此时是将得到的结果中的每一条记录，都作为List集合中的map集合元素，并且每一条记录中的列名作为map元素的key，列值作为map元素的value进行存储。

注意，这里的resultType属性是Map的全类名，即返回的结果是List集合时，使用List集合中的泛型类型作为resultType属性的值。





**方式二：使用@MapKey()注解指定Map集合的key元素**

这种方式，是将查询到的多条记录，使用一个Map来进行存储。

我们也可以将查询的多条记录，放到一个Map集合中进行存储，此时我们需要使用到`@MapKey()`注解，该注解的作用是去指明选择结果集中的哪一列作为Map集合的key（一般都选择的是主键列，因为主键列不可重复，而Map集合中的key元素也是不可重复的），而整个元素都会作为集合中的value元素。

例如：

```java
@MapKey("id")
public Map<Integer, User> getUsersReturnMap();
```

这里表示的含义就是，选择t_user表中的id列作为Map的key，而整条记录都会使用User对象类型进行接收。

```xml
<select id="getUsersReturnMap" resultType="java.util.Map">
    select *
    from t_user
</select>
```

注意，这里的resultType属性是Map的全类名，即当使用Map集合进行接收查询结果时，resultType使用的是Map集合的全类名，这点与List集合不同。

测试一下：

```java
SqlSession sqlSession = SqlSessionUtils.getSqlSession("mybatis-config.xml");
SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);

Map<Integer, User> usersMap = mapper.getUsersReturnMap();
System.out.println(usersMap.toString());
```

执行结果：

![image-20240520185927998](.\images\image-20240520185927998.png)

由执行的结果可以得出，此时Map集合中使用了查询结果中的id列作为了集合元素的key键，然后将每一条数据都使用了User类型对象接收，并作为了集合元素的value值。







---

## 2、特殊的SQL

### 模糊查询

**方式一：手动添加"%"通配符（不推荐）**

例如：

xml配置为：

```xml
<!--模糊查询-->
<select id="fuzzyQuery" resultType="com.bin.pojo.Book">
    select * from mybatis.book where bookName like #{info};
</select>
```

在使用该方法时，手动地添加%进行模糊查询：

```java
@Test
public void fuzzyQuery(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    BookMapper mapper = sqlSession.getMapper(BookMapper.class);
    List<Book> books = mapper.fuzzyQuery("%萨%");
    for (Book book : books) {
        System.out.println(book);
    }
    sqlSession.close();
}
```

这样也能够实现模糊查询。

但是这种方式容易出错，忘记添加通配符就会变成普通的查询语句，匹配所有的字符进行查询。



**方式二：使用'%${}%'拼接字符串（不推荐）**

例如：

```xml
<select id="fuzzyQuery" resultType="com.bin.pojo.Book">
    select * from mybatis.book where bookName like '%${info}%';
</select>
```

这种方式也是可以实现模糊查询的，${info}直接拼接字符串，将参数直接拼接到该字符串中，进行模糊查询。

那可不可以使用这种写法：'%#{}%'

```
<select id="fuzzyQuery" resultType="com.bin.pojo.Book">
    select * from mybatis.book where bookName like 
    '%#{info}%';
</select>
```

**答案是不行的**，因为使用''单引号包裹住#{}，会被认为是''字符串中的内容，而不是一个占位符，此时就无法被识别了，此时就会报错。

对于使用${}拼接字符串的方式，可能会造成SQL注入的问题，不安全，所以不推荐这样使用。



**方式三：使用`concat()`函数（推荐）**

使用concat()函数的方式，拼接字符串，这种方式简单且不易出错。

例如，查询用户表中username字段包含c字符的记录

Mapper接口中的方法：

```java
public List<User> getUserByLike(@Param("username")) String username;
```

对应的mapper映射文件：

```xml
<select id="getUserByLike" resultType="com.atguigu.mybatis.pojo.User">
    select *
    FROM t_user
    where username like concat('%', #{username}, '%');
</select>
```

测试一下：

```java
SqlSession sqlSession = SqlSessionUtils.getSqlSession("mybatis-config.xml");
SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);

for (User user : mapper.getUserByLike("c")) {
    System.out.println(user);
}
```

执行结果：

![image-20240520194618510](.\images\image-20240520194618510.png) 

此时，就能够使用模糊查询的方式，查询出username中包含c字符的记录。





**方式四：使用`"%"#{参数}"#"`的形式（不推荐）**

也可以去使用"%"#{参数名}"%"实现字符串的拼接操作，注意，这里的%通配符使用的是""双引号引起来，而不是''单引号，若使用的是单引号则不能实现模糊查询的效果。

案例：查询User表email字符包含12字符的记录

Mapper接口方法：

```java
public List<User> getUserByLike2(@Param("email") String email);
```

对应的映射文件：

```xml
<select id="getUserByLike2" resultType="com.atguigu.mybatis.pojo.User">
    select *
    from t_user
    where email like "%"#{email}"%"
</select>
```

测试：

```JAVA
@Test
public void test02(){
    SqlSession sqlSession = SqlSessionUtils.getSqlSession("mybatis-config.xml");
    SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);
    for (User user : mapper.getUserByLike2("12")) {
        System.out.println(user);
    }
}
```

执行结果：

![image-20240520195136668](.\images\image-20240520195136668.png) 

可见，此时，成功实现了模糊查询的功能。





**问题**：

为什么"%"#{username}"%"使用双引号可以使用，但'%'#{username}'%'使用单引号却无法实现模糊查询呢？

因为在SQL中，''被认为是一个字符串，而'%'#{username}'%'则会被认为是三个字符串，并不会将它们给拼接起来；而""在SQL中并不会认作是字符串，所以当使用"%"#{username}"%"则只有中间的#{username}是一个字符串，此时会将它们拼接起来使用。



**在MySQL中，拼接字符串使用的是`concat()`函数实现，但是在PgSQL中，模糊查询我直接使用||双下划线的方式进行拼接。**

---

### 动态设置表名、列名

如果想要去创建一个方法，对不同的表或者表中不同的列进行操作时，由于此时的表名、列名是由参数来决定的，而不是固定的，所以要去拼接SQL。

此时就只能使用${}，而不能使用#{}，因为表名、列名不能加单引号。

例如：

```java
/**
 * 查询指定表中的数据
 * @param tableName 
 * @return java.util.List<com.atguigu.mybatis.pojo.User>
 */
List<User> getUserByTable(@Param("tableName") String tableName);
```

```xml
<!--List<User> getUserByTable(@Param("tableName") String tableName);-->
<select id="getUserByTable" resultType="User">
	select * from ${tableName}
</select>
```





### 获取自增主键的值

**现在有一个场景：**

假如有两个表，一个表是班级表class，一个表是学生表student，二者是一对多的关系，在学生表中有一个class_id与班级表进行关联。

当去新建一个班级数据时，此时我们就想去给这个班级表中安排学生，也就是在插入一条班级表的数据后，去获取该插入的班级表id，从而让学生表中的一些数据的class_id设置成该id，达到给该班级安排学生的目的。

由于class表中的id列我们设置成为了自增`auto_increment`类型，所以在插入数据时，我们一般会去将id列设置成null，从而让数据库自己去递增id的值，我们在插入数据后，是不知道刚才所插入的class信息中的id值的。

此时，如果我们去查询一遍刚刚插入数据的话，从而去获取id，这样的话既比较麻烦，而且两次操作数据库对效率也不友好。

**mybatis给我们提供了获取自增主键值的方式：**

在执行插入语句的标签中，使用**`useGeneratedKeys`**和**`keyProperty`**属性来实现。

* `useGeneratedKeys`：设置当前标签中的sql使用了自增的主键
* `keyProperties`：将自增的主键的值赋值给传输到映射文件中参数的某个属性

当我们在insert标签中，将useGeneratedKeys属性设置为true，并将keyProperties设置为id，此时插入一条数据，会去获取数据库中自动生成的主键并将该值赋值给insert方法传入的对象参数中的id属性。

> useGeneratedKeys设置为true后，mybatis会使用JDBC的getGeneratedkeys()方法获取由数据库内部自动生成的主键，并将该值赋值给keyProperty指定的字段。

也许看不懂，没事，我们看一个案例就懂了。

案例：往t_user表中插入一条数据，并获取刚刚插入数据的id值

Mapper接口方法

```java
public int insertUser(@Param("user") User user);
```

映射文件

```xml
<insert id="insertUser">
    insert into t_user values(null, #{user.username}, #{user.password}, #{user.age}, #{user.sex}, #{user.password})
</insert>
```

此时我们去执行插入操作：

```java
SqlSession sqlSession = SqlSessionUtils.getSqlSession("mybatis-config.xml");
SelectMapper mapper = sqlSession.getMapper(SelectMapper.class);
User user = new User();
user.setAge(22);
user.setPassword("heloda");
user.setUsername("qweqwe");
user.setSex('男');
user.setEmail("123asd@qq.com");

mapper.insertUser(user);
System.out.println(user);
```

执行结果：

<img src=".\images\image-20240520225131857.png" alt="image-20240520225131857" style="zoom:80%;" /> 

此时并不能去获取到刚才所插入的数据id值。



当我们去使用useGeneratedKeys与keyProeprties属性：

```xml
<insert id="insertUser" useGeneratedKeys="true" keyProperty="id">
    insert into t_user values(null, #{user.username}, #{user.password}, #{user.age}, #{user.sex}, #{user.password})
</insert>
```

此时表示的含义就是：使用JDBC的`getGeneratedkeys()`方法获取刚才插入数据所创建的主键，并将该值赋值给keyProperty指定的字段，也就是User对象中的id属性。

那么此时再去调用测试方法，执行的结果为：
![image-20240520225419422](.\images\image-20240520225419422.png) 

可以看到，刚才所创建的对象的id是10。



---

## 3、解决映射关系的多种方式（:star:）

之前，我们学习过，resultType可以将查询到的结果中的列，转换成对象中同名的属性值。但是当列名与属性名不一致，或者存在一对多或多对一的情况时，此时我们就需要使用到resultMap来进行处理。

### 数据准备

**搭建子项目**

创建子项目mybatis-demo3，专门用于对resultMap的学习



**创建mybatis配置文件**

在项目下的resources目录中，创建jdbc.properties数据源信息文件，在该文件中配置连接数据库的信息：

```properties
jdbc.username=root
jdbc.password=061535asd
jdbc.url=jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf8&useSSL=false
jdbc.driverClassName=com.mysql.cj.jdbc.Driver
```

创建mybatis-config.xml核心配置文件，在文件中引入jdbc.properties，并设置连接数据库的环境：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//MyBatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <properties resource="jdbc.properties"></properties>

    <environments default="mysql_test">
        <environment id="mysql_test">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driverClassName}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>

</configuration>
```





**创建表与相应的实体类**

创建员工表t_emp与部门表t_dept：

员工表中包含员工id（eid），员工name（emp_name），员工年龄（age），员工性别（sex），员工邮箱（email）与所属部门的id（did）的信息。

部门表中包含部门id（did）与部门名称（dept_name）的信息。

```mysql
create table t_emp(
	eid int PRIMARY key auto_increment,
	emp_name varchar(20),
	age INT,
	sex char,
	email varchar(20),
	did INT
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

create table t_dept(
	did int PRIMARY key auto_increment,
	dept_name VARCHAR(20)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
```

向这两个表中添加一些数据，最终得到的两个表的信息为：

员工表

![image-20240521110026961](.\images\image-20240521110026961.png) 



部门表

![image-20240521110102520](.\images\image-20240521110102520.png) 

然后去创建员工表与部门表所对应的实体类：

创建com.atguigu.mybatis.pojo包下，创建Emp类与Dept类。

Emp类

```java
public class Emp {
    private Integer eid;

    private String empName;

    private Integer age;

    private String sex;
    
    private String email;
    
    //属性对应的getter、setter方法以及构造器和toString()
}
```

Dept类

```java
public class Dept {
    private Integer did;

    private String deptName;
    
    //省略属性对应的getter、setter以及构造器和toString()方法
}
```

这样，我们用于测试的表以及实体类就创建好了。



**创建Mapper接口与映射文件**

创建com.atguigu.mybatis.mapper包，在mapper包下创建EmpMapper接口与DeptMapper接口：

<img src=".\images\image-20240521111209437.png" alt="image-20240521111209437" style="zoom:80%;" /> 

再去resources目录下，创建com/atguigu/mybatis/mapper目录：

<img src=".\images\image-20240521111256197.png" alt="image-20240521111256197" style="zoom:50%;" /> 

 ![image-20240521111350522](.\images\image-20240521111350522.png) 

为了能够在mybatis配置文件中，在映入映射文件时能够使用package批量引入，必须要保证mapper映射文件与Mapper接口放在相同包下。

在该目录下，创建EmpMapper.xml与DeptMapper.xml映射文件，与Mapper接口建立映射关系：

EmpMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.atguigu.mybatis.mapper.EmpMapper">

</mapper>
```

DeptMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.atguigu.mybatis.mapper.DeptMapper">

</mapper>
```



**配置文件中引入映射文件**

创建完Mapper接口以及对应的mapper映射文件后，我们就可以去核心配置文件中，引入映射文件。

由于映射文件与Mapper接口放在同一包下，并且mapper接口名与mapper映射文件名一致，所以我们就可以去使用package标签批量引入映射文件：

```xml
<mappers>
     <!-- 
     以包为单位，将包下所有的映射文件引入核心配置文件
     注意：
1. 此方式必须保证mapper接口和mapper映射文件必须在相同的包下
2. mapper接口要和mapper映射文件的名字一致
     -->
     <package name="com.atguigu.mybatis.mapper"/>
</mappers>
```

映射文件与Mapper接口均在com.atguigu.mybatis.mapper包下。



**创建工具类SqlSessionUtils，封装获取SqlSession的过程**

最后，我们来创建一个工具类SqlSessionUtils，封装获取SqlSession的过程。我们每次去使用mybatis时，都需要先通过SqlSession，去获取Mapper接口的代理实现类对象，通过这个代理实现类对象去执行相关的sql。

但是，每次去获取SqlSession时，都需要通过SqlSessionFactoryBuilder类，去创建SqlSessionFactory对象，再通过该对象去获取SqlSession对象，这一过程比较麻烦，我们这里就去封装一个静态方法，每次直接通过调用该方法去获取SqlSession对象就行。

创建com.atguigu.mybatis.utils包，在该包下创建SqlSessionUtils工具类：

```java
public class SqlSessionUtils {
    public static SqlSession getSqlSession(){
        SqlSession sqlSession = null;
        try {
            //读取指定配置文件去创建输入流
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            //创建SqlSessionFactory对象
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            //调用openSession()方法，传入true，表示自动提交事务
            sqlSession = sqlSessionFactory.openSession(true);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sqlSession;
    }
}
```

至此，数据准备工作就完成了。

最终该项目下的文件信息为：

<img src=".\images\image-20240521112719514.png" alt="image-20240521112719514" style="zoom:67%;" /> 





---

### 方式一：使用字段别名

使用`resultType`映射，当我们使用类对象的方式去接收查询的结果时，类中的**属性名**要与结果集中的**列名保持一致**，才会将列所对应的值映射给属性。

如果实体类的属性名与表中的列名不一致，该如何将查询到的结果映射给指定的属性呢？

方式之一就是使用字段别名的方式。

**我们先来看一个例子：**

假设我们要去查询t_emp员工表中的所有信息，那么首先去创建Mapper接口的方法：

```java
public interface EmpMapper {
    /**
     * 获取所有的员工信息
     */
    public List<Emp> getAllEmps();
}
```

然后去创建对应的SQL映射：

```java
<select id="getAllEmps" resultType="com.atguigu.mybatis.pojo.Emp">
    SELECT *
    FROM t_emp
</select>
```

测试：

```java
SqlSession sqlSession = SqlSessionUtils.getSqlSession();
EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
for (Emp emp : mapper.getAllEmps()) {
    System.out.println(emp);
}
```

执行结果：

![image-20240521134518704](.\images\image-20240521134518704.png) 

**结果分析：**

为什么empName查询出来的结果是null呢？原因就在于查询结果集中，没有列的列名是empName的。

在数据库中，列名或表名使用多个单词构成的话，中间使用的是下划线_进行分割，表示员工名称的列名为emp_name；在Java类中，属性名、方法名若由多个单词构成，则使用的是第二个单词开始首字母大写的方式，所以表示员工名称的属性名为empName。

若使用resultType映射，使用类对象接收查询到的结果，要求属性名与列名要匹配，这里名称不同，所以无法将结果集中的emp_name列值赋值给对象中的empName属性。

所以也就造成了查询到的结果中empName为null。

**那该如何解决这一问题？**

我们之前知道，只要结果集中的列名与属性名一致就可以进行映射，那我们是不是可以使用给列取别名的方式，让结果集中的列名采取我们想要的名称，这样不就可以进行映射了吗？

**修改：**

给查询到的列取一个别名，让其与接收对象中的属性名保持一致：

```xml
<select id="getAllEmps" resultType="com.atguigu.mybatis.pojo.Emp">
    SELECT eid, emp_name as empName, age, sex, email
    FROM t_emp
</select>
```

此时，我们再去查询一下t_emp表中所有信息：

<img src=".\images\image-20240521135743946.png" alt="image-20240521135743946" style="zoom: 67%;" /> 

可以看到，此时就能够将emp_name列中的值查询出来了，即此时就将emp_name列与empName属性建立了映射关系。





---

### 方式二：开启驼峰命名法

mysql的命名方式与Java中的命名方式是不一样的，如果每次遇到由多个单词组成的列，都需要去给列取一个与属性名一致的别名，也过于麻烦。

所以，mybatis给我们提供了一种配置方式，让我们去开启驼峰命名法，让查询出来的结果集中列名形式aaa_bbb，自动转换成Java属性名的命名方式：aaaBbb，这样就不需要我们每次都去取一个别名，而会自动实现自动映射了。

**开启方式**

在mybatis核心配置文件中，创建`settings`标签，该标签是用来进行全局配置的。**（settings标签在配置文件中，处于第二的位置，放在properties标签的后面。）**

在该标签中，使用`setting`标签进行各种配置：

* `name`属性：配置的类型名
* `value`属性：配置的值

而我们开启驼峰命名法的配置名为**`mapUnderscoreToCamelCase`**。故开启驼峰命名法的配置为：

```xml
<settings>
    <!--将表中字段的下划线自动转换为驼峰-->
    <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
```

在配置文件中开启配置之后，SQL查询的结果集中字段名就会自动改成使用驼峰命名法的名称，例如emp_name，使用驼峰命名法命名为empName。此时，该列名就可以使用empName的属性进行接收。

**测试：**

```java
<select id="getAllEmps" resultType="com.atguigu.mybatis.pojo.Emp">
    SELECT *
    FROM t_emp
</select>
```

执行结果：

<img src=".\images\image-20240521140930384.png" alt="image-20240521140930384" style="zoom:67%;" /> 

此时就将emp_name的列，使用了Emp类中的empName属性接收成功。 



**小结**

若字段名和实体类中的属性名不一致，但是字段名符合数据库的规则（使用_），实体类中的属性名符合Java的规则（驼峰命名）。此时也可通过以下两种方式处理字段名和实体类中的属性的映射关系。

```xml
1. 可以通过为字段起别名的方式，保证和实体类中的属性名保持一致  

	<!--List<Emp> getAllEmp();-->
<select id="getAllEmp" resultType="Emp">
	select eid,emp_name as "empName",age,sex,email from t_emp
</select>



2. 可以在MyBatis的核心配置文件中的`setting`标签中，设置一个全局配置信息mapUnderscoreToCamelCase，可以在查询表中数据时，自动将_类型的字段名转换为驼峰，例如：字段名user_name，设置了mapUnderscoreToCamelCase，此时字段名就会转换为userName。[核心配置文件详解](#核心配置文件详解)

<settings>
    <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
```



---

### 方式三：使用resultMap自定义映射(:star2:)

#### 基本使用

当使用实体类接收查询的数据库结果集时，数据名和结果集中的列名不一致，并且列名与数据名之间也不符合驼峰命名规则映射时，那么此时，如果都使用给列取别名的方式有些麻烦，并且该SQL标签仅适用于当前的实体类，当我们去使用其他不同属性名的实体类，使用该SQL查询并接收时，就需要重新再去创建一个新的方法，重新创建一个SQL查询，在新的SQL查询中取一个符合当前实体类属性名的列别名才能够实现，这样既麻烦，又代码冗余了。

此时，就可以使用到resultMap来自定义结果映射功能实现。

> **实现过程**
>
> 1.在mapper映射文件中创建一个标签`<resultMap>`，该标签就是来自定义结果集映射规则的。在该标签中，有两个属性，`id`和`type`。
>
> * `id`属性：映射规则的唯一标识
> * `type`属性：Java实体类类型，即：使用该映射规则所进行接收的实体类类型。
>
> 2.在resultMap标签中创建两种子标签：`<id>`和`<result>`，id标签用来给结果集中的主键列建立映射关系，result标签用来给结果集中的普通列建立映射关系。
>
> 3.在这两种子标签中，通过`column`属性和`property`属性分别指定结果集的列以及实体类的属性，从而建立映射关系。
>
> 4.在SQL标签中，调用resultMap标签的id属性，去给当前查询使用指定的映射关系。



**实现案例：**获取t_emp表中的所有数据

创建Mapper接口方法

```java
public interface EmpMapper {
    /**
     * 获取所有的员工信息
     */
    public List<Emp> getAllEmps();
}
```

创建映射文件信息：

```xml
<select id="getAllEmps" resultMap="">
    SELECT *
    FROM t_emp
</select>
```

t_emp表如下所示：

<img src=".\images\image-20240521150934751.png" alt="image-20240521150934751" style="zoom: 80%;" /> 

我们要使用Emp类进行接收，该类中的属性如下所示：
<img src=".\images\image-20240521151343263.png" alt="image-20240521151343263" style="zoom: 80%;" /> 

可以看到，是存在着属性名与列名不一致情况的。

此时，我们想通过resultMap来实现自定义映射，可以在mapper映射文件中，**去创建resultMap标签：**

```xml
<resultMap id="myEmp" type="com.atguigu.mybatis.pojo.Emp">
    <id column="eid" property="id" />
    <result column="emp_name" property="name" />
    <result column="age" property="age" />
    <result column="sex" property="sex" />
    <result column="email" property="email" />
</resultMap>
```

之后，让SQL标签去使用resultMap属性调用我们创建的resultMap：

```xml
<select id="getAllEmps" resultMap="myEmp">
    SELECT *
    FROM t_emp
</select>
```

测试一下：

```java
SqlSession sqlSession = SqlSessionUtils.getSqlSession();
EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
for (Emp emp : mapper.getAllEmps()) {
    System.out.println(emp);
}
```

执行结果：

<img src=".\images\image-20240521151729667.png" alt="image-20240521151729667" style="zoom: 67%;" /> 

可以看到，通过resultMap自定义映射，成功将结果集中的eid列映射给了Emp类中的id属性，将结果集中的emp_name列映射给了Emp类中的name属性。 



> resultMap可以配合association与collection标签使用：
>
> * **`association用于映射对象类型属性`**
>
> * **`collection用于映射集合类型属性`**



---

#### 什么是多对一、一对多映射关系？

例如：对于员工与部门来说，一个员工只属于一个部门，但一个部门里面可以有多个员工，所以员工与部门之间的关系就是一对多的关系。

而员工表所对应的员工类中，就可以去给其创建一个部门类型的属性，说明当前员工属于哪个部门：

```java
public class Emp {
    //当前员工所属的部门
    private Dept dept;
    
    //其他属性...
}
```

我们通过员工表中的部门id信息，去查找部门表中对应的部门信息，并将该信息存放在员工类型对象中的Dept属性中，就是**多对一的映射关系**。



同理在部门表所对应的实体类部门类中，也可以去创建一个List集合类型的属性，用于去存储当前部门中所有的员工信息：

```java
public class Dept {
    //当前部门下的所有员工信息
    private List<Emp> empList;
    
    //其他属性...
}
```

我们通过部门表中的部门id，去员工表中查询所有部门id为该值的员工信息，并将查询到的所有员工信息存放在部门类型对象中的List集合中，就是**一对多的映射关系**。

其实，只要类中包含对象类型属性，并且想在一次查询中给类中对象类型属性赋值时，都适用于多对一映射关系。

只要类中包含集合类型属性，并且在一次查询将查询的多个结果给类中的集合属性赋值都适用于一对多映射关系。



#### 多对一映射关系的解决（属性是对象类型）

##### 数据准备

员工类：

```java
public class Emp {
    private Integer eid;
    private String empName;
    private Integer age;
    private String sex;
    private String email;
    private Dept dept;
	//必须的方法...
}
```

部门类：

```java
public class Dept {
    private Integer did;
    private String deptName;
	//必须的方法...
}
```

员工表

![image-20240521162926089](.\images\image-20240521162926089.png) 

部门表

![image-20240521162937764](.\images\image-20240521162937764.png) 

**案例**：想要去查询员工表中指定员工以及其对应部门的信息

创建Mapper接口方法

```java
/**
 * 查询员工以及员工所对应的部门信息
 */
public Emp getEmpAndDept(@Param("eid") int eid);
```

创建对应映射文件信息

```xml
<select id="getEmpAndDept" resultMap="">
    select *
    from t_emp e left join t_dept d
    on e.eid = d.did
    where eid = #{eid}
</select>
```

接下来主要的重心就是，如何去将查询结果给Emp类中的Dept类型属性赋值。



##### 1）通过级联属性解决多对一映射关系

在result标签中，property属性使用**`对象类型属性.属性名`**的方式，进行级联属性映射。

也就是说，在resultMap自定义映射关系中，通过对象类型属性.属性名的方式，将查询到的结果集中指定的列，给接收实体类中的对象类型属性中的属性进行赋值。

比如说，使用这种方式解决案例问题的resultMap标签：

```xml
<resultMap id="empAndDeptResultMap" type="com.atguigu.mybatis.pojo.Emp">
    <id property="eid" column="eid" />
    <result column="emp_name" property="empName" />
    <result column="email" property="email" />
    <result property="age" column="age" />
    <result property="sex" column="sex" />
    <result property="dept.deptName" column="dept_name" />
    <result property="dept.did" column="did" />
</resultMap>
```

可以看到，使用了`dept.deptName`与`dept.did`的方式，指明了将结果集中的dept_name与did列赋值给实体类中的dept对象属性中的deptName属性与did属性。

此时，我们去SQL查询标签中，配置该映射关系：

```xml
<select id="getEmpAndDept" resultMap="empAndDeptResultMap">
    select *
    from t_emp e left join t_dept d
    on e.eid = d.did
    where eid = #{eid}
</select>
```

然后进行测试：

```java
SqlSession sqlSession = SqlSessionUtils.getSqlSession();
EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
Emp emp = mapper.getEmpAndDept(1);
System.out.println(emp);
```

执行结果：

![image-20240521165104357](.\images\image-20240521165104357.png) 

即此时，成功将结果集中的部门信息赋值给了Emp类中的dept类型属性。





---

##### 2）通过association解决多对一映射关系（推荐）

association标签，用来映射类中对象类型属性。

在resultMap标签中，有一个`association`标签，该标签**专门用来处理接收类中对象类型属性的映射关系的。**

在association标签中，有两个属性：

* `property`属性：用于指定接收类中映射的属性
* `javaType`属性：指定该属性所属的对象类型

在association标签内，与resultMap标签一样，也是使用id标签和result标签，指定对象类型属性中的属性映射关系。

比如，使用这种方式解决员工类中部门属性的映射：

```xml
<resultMap id="empAndDeptResultMapTwo" type="com.atguigu.mybatis.pojo.Emp">
    <id property="eid" column="eid" />
    <result column="emp_name" property="empName" />
    <result column="email" property="email" />
    <result property="age" column="age" />
    <result property="sex" column="sex" />
    
    <association property="dept" javaType="com.atguigu.mybatis.pojo.Dept">
        <id column="did" property="did" />
        <result column="dept_name" property="deptName" />
    </association>

</resultMap>
```

此时，在SQL标签中，使用该resultMap，然后去测试一下，执行的结果：

![image-20240521171115872](.\images\image-20240521171115872.png) 

此时也实现了对象类型属性的映射关系。





---

##### 3）使用association实现分步查询解决多对一映射关系

查询员工表中的员工信息，并且将员工所对应的部门信息查询出来赋值给员工类中的Dept属性。

上述的这一过程，我们在SQL标签中，使用的是关联查询的方式来完成，实际上也可以分为两步查询：

1、查询员工表信息

2、使用员工表中的did，查询部门表的信息

即，我们将员工表中部门属性的映射操作，使用了两步查询去完成：

```
select *
from t_emp
where eid = #{eid}

#查出员工表中的部门id，然后根据部门id查询部门信息

select *
from t_dept
where did = #{did}
```

使用第二步的查询结果，我们就能够给第一步所查询出来的Emp类对象中的Dept部门属性赋值，从而实现多对一的映射。

**在mybatis中，使用`association`与`collection`标签实现分步查询。**

分步查询的前提是，在接口方法中，有这两种查询的实现，现在我们就去实现一下：

**1、首先去创建对应的查询抽象方法：**

查询员工表信息的方法应该放在EmpMapper接口中，查询部门表信息的方法应当放在DeptMapper接口中。

```java
//与员工表有关的Mapper接口
public interface EmpMapper {
    /**
     * 根据id查询员工信息
     */
    public Emp getEmpByEid(@Param("eid") int eid);
}


//与部门表有关的Mapper接口
public interface DeptMapper {
    /**
     * 根据部门id查询部门的信息
     */
    public Dept getDeptByDid(@Param("did") Integer did);
}
```

**2、然后就是去创建对应的mapper映射文件**

在员工的Mapper接口对应的映射文件中，以及部门的Mapper接口对应的映射文件中，分别去创建SQL映射：

EmpMapper.xml

```xml
<select id="getEmpByEid" resultMap="">
    select *
    from t_emp
    where eid = #{eid}
</select>
```

DeptMapper.xml

```xml
<select id="getDeptByDid" resultType="com.atguigu.mybatis.pojo.Dept">
    select *
    from t_dept
    where did = #{did}
</select>
```

由于我们需要在查询中，将第二步查询出来的Dept对象映射给第一步查询出来的Emp对象中的dept属性，所以第一步所使用的映射属性是resultMap，而第二次所使用的映射属性是resultType，因为第一步还需要进一步地进行设置。

**3、创建resultMap标签，在resultMap标签中配置分步查询**

```xml
<resultMap id="empStepQuery" type="com.atguigu.mybatis.pojo.Emp">
    <id property="eid" column="eid" />
    <result column="emp_name" property="empName" />
    <result column="email" property="email" />
    <result property="age" column="age" />
    <result property="sex" column="sex" />

    <association property="dept" select="com.atguigu.mybatis.mapper.DeptMapper.getDeptByDid" column="did">
    </association>
</resultMap>
```

这里的association标签就是去执行查询操作，并映射的作用。其中包含三个属性：

* `property`属性，用于指定当前调用查询的结果映射给当前实体类中的哪一个属性；

* `select`属性，表示指定的方法进行查询；

* `column`属性，指定传给查询方法的参数是当前查询的结果集中的哪一列。



**4、最后，在当前的查询中使用resultMap指定该映射**

```xml
<select id="getEmpByEid" resultMap="empStepQuery">
    select *
    from t_emp
    where eid = #{eid}
</select>
```

这样一来，我们在去调用getEmpByEid()方法时，就会使用到该映射方式，从而实现分步查询，将第二步查询的结果映射给类中的dept属性。

**测试**

![image-20240521183030302](.\images\image-20240521183030302.png) 

此时成功实现了Emp类中Dept属性的映射。



**分步查询传递多列值**

将多列的值封装成map传递：

**`column="{参数1=column1，参数2=clomn2,...}"`**

例如：

```xml
<association property="employees" select="com.rudywork.dao.EmployeeMapper2.getEmpByDepId"
column="{depId=id, empTime=time}">
</association>
```



> **注意点：**
>
> 1. 分步查询的每一次查询都需要去创建相对应的接口方法以及映射SQL；
>
> 2. **接收类中对象类型属性内部的映射关系，实际上是在第二次查询，依据第二次查询的所设置的映射关系来实现的。**使用association标签实现的分步查询，仅仅只是将指定查询的查询结果拿过来，赋值给当前这个查询中接收类的指定属性。
>
>    比如说，在上例中接收类的Emp类中，Dept类型对象属性中的属性映射关系，如did和deptName的映射关系，实际上是在第二步查询中使用了resultType来实现，即：resultType映射是结果集中列名与相同属性名进行映射。
>
>    <img src=".\images\image-20240521182215157.png" alt="image-20240521182215157" style="zoom:67%;" /> 
>
>    我们同样可以在第二步查询中，使用resultMap实现自定义映射，即也有可能会出现属性名和列名不一致的情况我们需要注意。 





---

#### 一对多映射关系的解决（属性是集合类型）

##### 数据准备

员工类：

```java
public class Emp {
    private Integer eid;
    private String empName;
    private Integer age;
    private String sex;
    private String email;
	//必须的方法...
}
```

部门类：

```java
public class Dept {
    private Integer did;
    private String deptName;
    private List<Emp> empList;
	//必须的方法...
}
```

员工表

![image-20240521162926089](.\images\image-20240521162926089.png) 

部门表

![image-20240521162937764](.\images\image-20240521162937764.png) 

**案例**：查询指定部门的信息以及该部门的所有员工信息

在DeptMapper接口中创建方法

```java
/**
 * 查询指定部门的信息以及该部门的所有员工信息
 */
public Dept getDeptAndEmps(@Param("did") int did);
```

创建对应映射文件信息

```xml
<select id="getDeptAndEmps" resultMap="">
    select *
    from t_dept d left join t_emp e 
    on d.did = e.did
    where d.did = #{did}
</select>
```

接下来主要的重心就是，如何去将查询结果给接收类Dept中的List<Emp>属性赋值。





---

##### 1）通过collection解决一对多映射关系（推荐）

collection标签，可以用来映射类中的集合类型属性。

collection标签的使用和association标签的使用差不多，只不过该标签中的属性与association标签中有所不同。该标签中包含两个属性，分别是`property`和`ofType`：

* `property`属性：表示类中的哪个属性使用该标签映射。
* `ofType`属性：表示集合元素的类型。

然后，在collection标签中，也是和association一样，使用`id`、`result`标签来指定集合中类对象中的属性映射。



**案例：**查询指定部门的部门信息以及该部门的所有员工信息

部门类Dept

```java
public class Dept {
    private Integer did;
    private String deptName;
    private List<Emp> empList;
	//必须的方法...
}
```

实际上就是将查询到的员工信息，存入到其中的List属性中属性中。

resultMap映射

```xml
<resultMap id="getDeptAndEmpList" type="com.atguigu.mybatis.pojo.Dept">
    <id property="did" column="did" />
    <result property="deptName" column="dept_name" />
    
    <collection property="empList" ofType="com.atguigu.mybatis.pojo.Emp">
        <id column="eid" property="eid" />
        <result column="emp_name" property="empName" />
        <result column="age" property="age" />
        <result column="sex" property="sex" />
        <result column="email" property="email" />
    </collection>
    
</resultMap>
```

以上，就实现了将查询到的结果映射给类中List集合类型属性的功能。

然后，我们就在SQL标签中，使用该resultMap映射：

```xml
<select id="getDeptAndEmps" resultMap="getDeptAndEmpList">
    select *
    from t_dept d left join t_emp e
    on d.did = e.did
    where d.did = #{did}
</select>
```

测试一下：

```java
SqlSession sqlSession = SqlSessionUtils.getSqlSession();

DeptMapper mapper = sqlSession.getMapper(DeptMapper.class);

Dept dept = mapper.getDeptAndEmps(1);
System.out.println(dept);
```

执行结果：

<img src=".\images\image-20240521203820242.png" alt="image-20240521203820242" style="zoom:80%;" /> 

即此时，成功实现一对多的映射。





---

##### 2）使用collection实现分步查询解决一对多映射关系

同样地，一对多映射关系，也能够使用分步查询的方式进行映射。

案例：查询部门表中id为1的部门信息，及其该部门下的所有员工信息。

对于上述这个例子，若使用分步查询，可以分成两步：

1、查询部门表中指定id的部门信息

2、查询员工表中，指定部门id的员工信息

这里每一步所对应的查询为：

```mysql
#查询1：
select *
from t_dept
where did = #{did}

#查询2：
select *
from t_emp
where did = #{did}
```

对于查询结果映射为集合的分步查询，mybatis让我们使用`collection`标签来实现，以下是实现的步骤：

**1、创建分步查询的抽象方法**

如果要使用分步查询来实现集合类型属性的映射，我们需要为每一步的查询都创建对应的方法实现。

DeptMapper接口中创建依据部门id获取部门的方法：

```java
/**
 * 根据部门id获取部门信息
 * @param did
 * @return
 */
public Dept queryStepByDid(@Param("did") int did);
```

EmpMapper接口中创建依据部门id获取员工集合的方法：

```java
/**
 * 根据部门id获取员工集合的方法
 * @param did
 * @return
 */
public List<Emp> getEmpListByDid(@Param("did") int did);
```



**2、创建对应的映射文件标签**

为每一步的查询都创建对应的映射文件信息。

在DeptMapper接口对应的映射文件中，创建第一步查询所对应的SQL标签：

```xml
<select id="queryStepByDid" resultMap="">
    select *
    from t_dept
    where did = #{did}
</select>
```

由于我们是使用分步查询的方式，给Dept类中的List<Emp>属性映射赋值，所以这里使用的是resultMap标签。

在EmpMapper接口对应的映射文件，创建第二步查询所对应的SQL标签：

```xml
<select id="getEmpListByDid" resultType="com.atguigu.mybatis.pojo.Emp">
    select *
    from t_emp
    where did = #{did}
</select>
```

**3、创建resultMap标签，在该标签中实现分步查询**

```xml
<resultMap id="getDeptStepQuery" type="com.atguigu.mybatis.pojo.Dept">
    <id property="did" column="did" />
    <result property="deptName" column="dept_name" />

    <collection property="empList" 
select="com.atguigu.mybatis.mapper.EmpMapper.getEmpListByDid" column="did" />
</resultMap>
```

这里的collection标签中，使用了`select`属性，该属性就是将去引用指定的查询方法，将`column`属性作为该查询方法的参数传入，并将该查询方法的结果集赋值给当前类中指定的`property`属性，

那么在这里，就是去调用了EmpMapper接口中的getEmpListByDid()方法，将当前结果集中的did列作为参数传入，并将执行的getEmpListByDid()结果映射给类中的empList属性。



**4、最后，在映射文件的标签中，指定使用该resultMap映射**

```xml
<select id="queryStepByDid" resultMap="getDeptStepQuery">
    select *
    from t_dept
    where did = #{did}
</select>
```



**测试**

```java
SqlSession sqlSession = SqlSessionUtils.getSqlSession();
DeptMapper mapper = sqlSession.getMapper(DeptMapper.class);

Dept dept = mapper.queryStepByDid(1);

System.out.println(dept);
```

执行结果：

<img src=".\images\image-20240521214701436.png" alt="image-20240521214701436" style="zoom: 80%;" /> 

此时就成功去获取了部门id为1的员工信息，并映射给了Dept类中的List<Emp>属性。

> 接收类中的集合属性的映射关系，取决于第二步查询的映射关系。



---

### 分步查询延迟加载

在MySQL学习中，我们知道要尽量去减少IO的次数，IO的次数越少，效率就会越高。

那我们为什么要去使用分步查询呢？明明使用联合查询或者子查询，只去查询一次，为什么非要去分成两次查询操作呢？

这其实是和**`延迟加载`**有关的。

**什么是延迟加载？**

举个例子：员工信息中关联着部门的信息。如果要求只去查询员工信息，那么此时的部门信息就不会被加载出来，等到什么时候要使用到部门信息时再去使用部门中的查询方法，再加载出来。**延迟加载就是先进行单表查询，等到需要第二张表的信息时再进行第二次查询。**这样能够大大提升数据库的性能，因为只查询一张表的效率要比联表查询的效率高。



**如何实现延迟加载**

延迟加载默认是关闭的，首先我们需要去全局配置文件中，开启延迟加载：

延迟加载的配置名称为`lazyLoadingEnabled`，将该属性设置为true。

同时，也需要保证`aggressiveLazyLoading`属性设置的值为false（由于默认就是false，所以无需进行设置），该属性开启时，任何方法的调用都会去加载该对象的所有属性，否则，每个属性会按需加载。

```xml
<settings>
	<!--开启延迟加载-->
	<setting name="lazyLoadingEnabled" value="true"/>
</settings>
```

上述配置会让所有的分步查询都使用延迟加载。



我们也可以自己去给某一个分步查询设置延迟加载，在association或collection标签中，使用`fetchType`属性来手动地控制当前分步查询是否使用延迟记载：

**当fetchType为`lazy`，表示延迟加载；为eager，表示立即加载**。如：

```xml
<association property="dept" select="com.atguigu.mybatis.mapper.DeptMapper.getEmpAndDeptByStepTwo" column="did" fetchType="lazy">
</association>
```

上述表示的含义就是，对于第二步查询getEmpAndDeptByStepTwo使用了延迟加载，当使用到dept属性时才进行查询操作。

使用了fetchType标签来修饰的分步查询，会将当前的是否延迟加载的设置覆盖全局配置。（和版本有关，有的版本是要求全局配置中要进行设置，fetchType属性也需要设置为lazy才会进行延迟加载；但是我目前使用的这个版本fetchType属性的设置会覆盖掉全局配置）



**实现案例**：查询t_emp表中指定id的员工信息与对应的部门信息，使用分步查询来实现

员工表的Mapper映射文件中SQL

```xml
<!--public Emp getEmpByEid(@Param("eid") int eid);-->
<select id="getEmpByEid" resultMap="empStepQuery">
    select *
    from t_emp
    where eid = #{eid}
</select>


<resultMap id="empStepQuery" type="com.atguigu.mybatis.pojo.Emp">
        <id property="eid" column="eid" />
        <result column="emp_name" property="empName" />
        <result column="email" property="email" />
        <result property="age" column="age" />
        <result property="sex" column="sex" />

        <association property="dept" select="com.atguigu.mybatis.mapper.DeptMapper.getDeptByDid" column="did" fetchType="lazy">
        </association>
/resultMap>
```

第二步查询所关联的查询SQL标签：

```xml
<select id="getDeptByDid" resultType="com.atguigu.mybatis.pojo.Dept">
        select *
        from t_dept
        where did = #{did}
</select>
```

测试方法：

```java
SqlSession sqlSession = SqlSessionUtils.getSqlSession();
EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
Emp emp = mapper.getEmpByEid(1);

System.out.println("员工的姓名：" + emp.getEmpName());

System.out.println("员工所属的部门名称：" + emp.getDept().getDeptName());
```

**开启延迟加载前：**

![image-20240521194729279](.\images\image-20240521194729279.png)

此时会将分步查询中的所有查询都一次性完成

**开启延迟加载后：**

![image-20240521194647234](.\images\image-20240521194647234.png)

可以看到，只有使用到Emp类中的Dept属性时，才会进行分步查询中的第二次查询。



---

## 4、动态SQL（:star:）

MyBatis框架的动态SQL技术是一种根据特定条件动态拼装SQL语句的功能，它存在的意义是为了解决拼接SQL语句字符串时的痛点问题。

### if标签与where标签

if标签实际上就是类似于Java中的if判断语句。

> if标签通过`test`属性（即传递过来的数据）的表达式进行判断，若表达式的结果为true，则标签中的内容会执行；反之标签中的内容不会执行。

直接来看一个案例：

查询t_emp表中的员工信息，方法传入Emp对象，依据该对象中所包含的属性对t_emp表的数据进行筛选，比如传入Emp对象参数中包含id和名称，那么就让t_emp表中满足这两个条件的信息返回。

```mysql
select *
from t_emp
where eid = #{eid}
and emp_name = #{empName}
and age = #{age}
and sex = #{sex}
and email = #{email}
```

若Emp对象中所有属性的值都不为空，则实际执行的SQL语句如上所示。

但是，此时我们去根据条件进行查询，当属性不为空时才会将该条件加入到where子句中，此时我们就需要进行判断了，此时就可以使用if标签，判断传入进来的参数是否符合某些条件。

Mapper接口的方法

```java
/**
 * 根据条件查询员工信息
 */
public List<Emp> getEmpsByCondition(@Param("emp") Emp emp);
```

mapper映射

```xml
<select id="getEmpsByCondition" resultType="com.atguigu.mybatis.pojo.Emp">
    select *
    from t_emp
    where
    <if test="emp.eid != null and emp.eid != 0">
        eid = #{emp.eid}
    </if>
    <if test="emp.empName != null and emp.empName != ''">
        and emp_name = #{emp.empName}
    </if>
    <if test="emp.age != null and emp.age != 0">
        and age = #{emp.age}
    </if>
    <if test="emp.sex != null and emp.sex !=''">
        and sex = #{emp.sex}
    </if>
    <if test="emp.email != null and emp.email !=''">
        and email != #{emp.email}
    </if>
</select>
```

此时，我们就建立了根据传入的Emp类型对象来进行条件查询，如果对象中的属性有值，那么就会使用该属性的等值条件作为查询的条件。

测试

```java
SqlSession sqlSession = SqlSessionUtils.getSqlSession();
EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);

Emp emp = new Emp();
emp.setEid(1);
emp.setEmpName("tom");
emp.setAge(20);

List<Emp> emps = mapper.getEmpsByCondition(emp);
System.out.println(emps);
```

我们来看看这个查询的执行日志：

![image-20240521232413172](.\images\image-20240521232413172.png)

可以看到，由于传入的Emp对象中，eid、empName以及age三个属性是有值的，所以在查询时，使用这三个属性的条件就会拼接到查询SQL的where子句中。





假设，对于上例中，我们的测试是这样的：

```java
SqlSession sqlSession = SqlSessionUtils.getSqlSession();
EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);

Emp emp = new Emp();
emp.setEmpName("tom");
emp.setAge(20);

List<Emp> emps = mapper.getEmpsByCondition(emp);
System.out.println(emps);
```

此时去执行一下：

![image-20240521232643003](.\images\image-20240521232643003.png)

发现，此时执行报错了。

为什么会报错？

我们来看看执行SQL的日志：

![image-20240521232725334](.\images\image-20240521232725334.png)

此时的执行SQL是：

`SELECT * FROM t_emp WHERE AND emp_name = ? AND age = ?`

这句SQL的语法是错误的，为什么会这样？

原因在于：我们在条件查询时，if标签进行拼接SQL，由于传入的Emp对象中id值为null，所以没有将`eid = #{emp.eid}`这一部分拼接在WHERE后面，而是将满足条件的`and emp_name = #{emp.empName}`拼接在了WHERE后面，造成了SQL的语法错误。

这就有一点麻烦了，如果我们不使用AND连接符，后续的判断条件也会去造成语法错误，如果使用AND连接符，那么也可能造成语法错误。

那有没有什么办法，能够让满足条件的SQL使用AND连接符进行拼接，也能让多余的AND连接符去除呢？

**方式一：添加恒成立1=1条件**（不推荐）

这一种方式，是在WHERE子句的开头，添加一个1=1恒成立的条件，让后续所有的if条件判断的SQL语句都加上AND连接符。

例如：

```xml
<!--List<Emp> getEmpByCondition(Emp emp);-->
<select id="getEmpByCondition" resultType="Emp">
	select * from t_emp where 1=1
	<if test="empName != null and empName !=''">
		and emp_name = #{empName}
	</if>
	<if test="age != null and age !=''">
		and age = #{age}
	</if>
	<if test="sex != null and sex !=''">
		and sex = #{sex}
	</if>
	<if test="email != null and email !=''">
		and email = #{email}
	</if>
</select>
```

如上例，在where子句中后面加一个1=1的恒成立式子，在if标签中所填写的SQL均添加AND。



**方式二：使用where标签（推荐）**

where标签一般和if标签配合使用。

where标签会自动添加where关键字，并将条件最前方多余的AND或OR连接符去掉。

例如：

```xml
select *
from t_emp
<where>
    <if test="emp.eid != null and emp.eid != 0">
        eid = #{emp.eid}
    </if>
    <if test="emp.empName != null and emp.empName != ''">
        and emp_name = #{emp.empName}
    </if>
    <if test="emp.age != null and emp.age != 0">
        and age = #{emp.age}
    </if>
    <if test="emp.sex != null and emp.sex !=''">
        and sex = #{emp.sex}
    </if>
    <if test="emp.email != null and emp.email !=''">
        and email != #{emp.email}
    </if>
</where>
```

> **注意：`AND或OR连接符需要放在if条件SQL语句的开头`**，若放在其他地方where标签无法起作用。

测试：

```java
SqlSession sqlSession = SqlSessionUtils.getSqlSession();
EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);

Emp emp = new Emp();
emp.setEmpName("tom");
emp.setAge(20);

List<Emp> emps = mapper.getEmpsByCondition(emp);
System.out.println(emps);
```

此时，我们去看一看执行的SQL：

![image-20240521234659551](.\images\image-20240521234659551.png) 

可以看到，此时将原本where and emp_name = ?中前面的多余的and去除了。







---

### trim标签：自定义字符串截取

trim标签用于添加或去掉属性中所指定的内容。

其中可以定义四个属性：

* **`prefix`**：**前缀**，给拼接后的整个字符串添加一个字符串前缀。
* **`prefixOverrides`**：**前缀覆盖**，去掉拼接后字符串前面指定的多余子字符串。（有则去除，无则没有影响）
* **`suffix`**：**后缀**，给拼接后的整个字符串添加一个字符串后缀。
* **`suffixOverrides`**：**后缀覆盖**，去掉拼接后字符串后面指定的多余子字符串。（有则去除，无则没有影响）

使用案例：

假设在where子句中，我们所使用if标签来进行判断时，将AND连接符放在了if标签体的最后时，如：

```xml
<select id="getEmpsByConditionIf" resultType="employee">
    select * from employee
   <where>
        <if test="id != null">
             id = #{id} AND
        </if>
        <if test="lastName != null and lastName != ''">
             last_name = #{lastName} AND
        </if>
        <if test="email!=null and email!=''">
             email = #{email}
        </if>
   </where>
</select>
```

由于where标签，只会去自动删除前面多余出来的AND或OR连接符，将连接符放在if标签体的后面时，where标签不会起作用。

此时，就可以使用trim标签来截取，使用trim标签中的suffixOverrides属性，指定去除字符串中，最后多余出来的AND连接符，此时执行时SQL的语法就不会出现问题了：

```xml
<select id="getEmpsByConditionIf" resultType="employee">
    select * from employee
    <!--
        where可以放在prefix属性中，该属性用于给整个trim标签体中拼接后的字符串加上一个前缀
        因为有可能最后会多出一个AND连接符，比如last_name有数据，但email没有数据的情况下，此时可以使用suffixOverrides属性，给字符串中最后的and标签覆盖掉（有则去除，无则没有影响）
    -->
    <trim prefix="where" suffixOverrides="AND">
        <if test="id != null">
             id = #{id} AND
        </if>
        <if test="lastName != null and lastName != ''">
            last_name = #{lastName} AND
        </if>
        <if test="email!=null and email!=''">
             email = #{email}
        </if>
    </trim>
</select>
```

那么，此时就不会出现因最后多出一个AND连接符而造成的SQL语法错误。







---

### choose标签：类似于swicth-case

**需求案例**：依据条件查询t_emp表中的数据，传入Emp类型对象，如果该Emp对象中eid属性有值，就用该eid进行查询；如果该Emp对象中empName属性有值，就根据该empName进行查询；如果该Emp对象中email属性有值，就根据该email进行查询；否则，根据sex进行查询。

**实现：**

```xml
<!--public List<Emp> getEmpsByConditionChoose(@Param("emp") Emp emp);-->
<select id="getEmpsByConditionChoose" resultType="com.atguigu.mybatis.pojo.Emp">
    select *
    from t_emp
    <where>
        <choose>
            <when test="emp.id != null and emp.id != 0">
                eid = #{emp.eid}
            </when>
            <when test="emp.empName != null and emp.empName != ''">
                emp_name = #{emp.empName}
            </when>
            <when test="emp.email != null and emp.email != ''">
                email = #{emp.email}
            </when>
            <otherwise>
                sex = #{emp.sex}
            </otherwise>
        </choose>
    </where>
</select>
```

`<choose>`标签类似于Java中switch-case结构中的switch结构。

`<when>`标签类似于switch-case结构中的case结构，用于判断。

`<otherwise>`标签类似于switch-case结构中的default结构，即当以上条件均不满足时，就会去拼接otherwise标签体。

在when标签中，也是与if标签类似，在test属性中进行条件判断，判断的语句也是和if标签类似，其中是一个boolean类型的语句。

> **注意：该分支结构中每一层分支，都会自动加上`break`语句，即：`只要满足其中的一个条件，SQL拼接上对应的语句后就会退出choose标签体，而不会继续判断执行`。**







---

### foreach标签：循环

**foreach标签的属性：**

* **`collection`**：设置要循环的数组或集合
* **`item`**： 表示集合或数组中每一次迭代的元素。若collection为list、set或数组，则表示其中的元素；若collection为map，则表示key-value中的value。该参数是必填参数。
* `index`：若collection为list、set或数组，index表示当前所迭代的位置；若collection为map，idnex表示的是元素的key。该参数是可选的。
* `open`：表示循环语句以什么开始，最常用的是左括号(。该参数是可选的。
* `close`：表示循环语句以什么结束，最常用的是右括号)。该参数是可选的。
* `separator`：在每次迭代后，都会给sql语句添加上separator指定的字符串，最常用的是,逗号。该参数是可选的。

假设接口传递的list如下所示：

```java
list<String> list = new ArrayList<>();
list.add("zhangsan");
list.add("lisi");
list.add("wangwu");
```

将该list传入到SQL方法中，则在foreach标签使用该list的配置为：

```xml
<!--public List<Emp> getEmpsByNames(@Param("nameList") List<String> nameList) -->
<foreach collection="nameList" item="name" open="(" close=")" separator=",">
	#{name}
</foreach>
```

此时，mybatis解析foreach标签的内容，并且拼接SQL过程是：

```
(
('zhangsan'
('zhangsan',
('zhangsan','lisi'
('zhangsan','lisi',
('zhangsan','lisi','wangwu'
('zhangsan','lisi','wangwu')
```

最终得到的结果是：

```
('zhangsan','lisi','wangwu')
```

此时在foreach标签的前面加上SQL语句，如：

```mysql
SELECT *
FROM t_emp
WHERE emp_name IN ('zhangsan','lisi','wangwu')
```



**SQL批量执行案例：数据库批量插入案例**

**方式一：使用foreach对VALUES后的数据项进行循环**

```xml
<!--public int employeeSave(@Param("saveEmps") List<Emp> empList);-->
<insert id="employeeSave">
    INSERT INTO t_emp(emp_name, sex, email) VALUES
    <foreach collection="saveEmps" item="emp" separator=",">
        (#{emp.empName},#{emp.gender},#{emp.email})
    </foreach>
</insert>
```

**方式二：循环使用多个完整的SQL语句连续执行插入操作**

MySQL数据库允许一次性执行多个SQL，这样我们就可以使用foreach标签将整个SQL都作为循环体，循环地执行SQL来执行插入操作。

在执行操作时，我们需要将Mysql数据库的**`allowMultiQueries`**属性设置为true，表示允许多个SQL语句一起执行。多个SQL之间，使用;分号分隔开。

可以通过数据源配置的url中设置：

![image-20240522135154409](.\images\image-20240522135154409.png)

之后，我们就可以使用foreach标签将整个SQL语句全部包起来，作为标签体，每个SQL语句之间使用;进行分隔，所以separator属性设置为;。

实现：

Mapper接口方法

```java
/**
 * 批量执行完整的SQL插入数据
 */
public int insertByLoopSQL(@Param("emps") List<Emp> empList);
```

映射文件

```xml
<insert id="insertByLoopSQL">
    <foreach collection="emps" item="emp" separator=";">
        INSERT INTO t_emp
        VALUES(null, #{emp.empName}, #{emp.age}, #{emp.sex}, #{emp.email}, #{emp.did})
    </foreach>
</insert>
```

测试：

```java
SqlSession sqlSession = SqlSessionUtils.getSqlSession();
EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);

List<Emp> empList = new ArrayList<>();
empList.add(new Emp(null, "java", 22, "男", "123@456.com", 2));
empList.add(new Emp(null, "python", 12, "女", "789@qwer.com", 1));
empList.add(new Emp(null, "world", 10, "男", "101212@asd.com", 3));

int i = mapper.insertByLoopSQL(empList);
System.out.println(i);
```

设置allowMultiQueries属性之前：

![image-20240522140414622](.\images\image-20240522140414622.png) 

执行报错。

设置allowMultiQueries属性之后：

![image-20240522140548818](.\images\image-20240522140548818.png)

执行成功。可以从结果日志看到，执行了三次INSERT语句。

这里为什么返回值是1，也就是说结果只影响了数据表中的1行吗？

这里实际上，返回的是最后一次执行SQL所影响的行数，也就是说，这里分成了三个SQL执行，返回值只是最后一次SQL所影响的行数。









**Map集合参数的循环**

在foreach标签中，map集合的key，使用的是标签中的index属性来表示；map集合的value，使用的是标签中的item属性来表示。

来看一个例子：

```xml
<update id="modify" parameterType="map">
	update t_emp set 
    
    <foreach collection="hashMap" index="key" item="value" separator=",">
    ${key} = #{value}
    </foreach>
    
    where id = 2
</update>
```

我们来看看这一个例子。

这是一个update操作，根据传入map的key去决定要修改的列，传入map的value决定修改的值。

这里的key使用的是${}去获取参数，因为这表示的是列名，直接拼接SQL，而不能使用#{}，因为它是占位符，当拼接SQL后会自动添加''单引号。（表名、列名都使用${}）



---

### sql片段

sql片段的作用就是能够让我们去重复地使用sql，避免代码冗余。

就比如去查询t_emp表中的字段，我们知道，在查询时，是业务上需要哪些字段就查哪些，而不是去select *，所以我们就可以将要经常查询的字段给放在sql片段中，什么时候要使用就去调用这个sql片段。

**声明sql片段**：`<sql>`标签

在该标签中使用`id`属性来指定该sql片段的唯一标识。

例如：

```xml
<sql id="Emp_Column">
    eid, emp_name, age, sex, email, did
</sql>
```

**引用sql标签**：`<include>`标签

在该标签中，使用`refid`属性，引用指定id的sql片段。

例如：

```xml
<insert id="insertEmp">
	insert into t_emp(
    	<include refid="Emp_Column"></include>
    )
    values(#{emp.eid}, #{emp.empName}, #{emp.age}, #{emp.sex}, #{emp.email}, #{emp.did})
</insert>
```





---

# 五、缓存（面试必考）

## 1、缓存概述

缓存（即cache）的作用是为了减轻数据库的压力，提高数据库的性能。缓存实现的原理是从数据库中查询出来的对象在使用完后不销毁，而是存储在缓存中，当再次需要获取该对象，直接从缓存中获取，不再去数据库执行相关语句，减少数据库的查询次数，提高数据库的性能。缓存还未到数据库层面，所以事务是与缓存无关的，管不了缓存。

> **缓存底层使用`Map`集合存储数据。**

那么也就是说，有些类似于MySQL的数据库缓冲池，MyBatis也给我们提供了在内存中去获取重复数据的方式，该方式就是缓存。

缓存让我们，可以不去通过访问数据库的方式（访问数据库缓冲池也是访问数据库），直接在内存中去获取曾经查询过的数据。

MyBatis有`一级缓存`和`二级缓存`之分。

**一级缓存的作用域是同一个SqlSession。**在同一个SqlSession中两次执行相同的Sql语句，第一次执行完毕会将数据库查询的数据写到缓存，第二次会从缓存中获取数据而不进行数据库查询，大大提高了查询效率。当一个SqlSession结束后该SqlSession中的一级缓存也就不存在了。**`MyBatis默认启动一级缓存。`**

**二级缓存是多个SqlSession共享的，其作用域是Mapper的同一个namespace**，不同的sqlSession两次执行相同namespace下的sql语句并且向sql中传递的参数也相同时，第一次执行完毕会将数据库中查询到的数据写到缓存，第二次查询直接从缓存中获取，从而提高了查询效率。MyBatis默认不开启二级缓存，需要在Mybatis全局配置文件中进行setting配置开启二级缓存。



## 2、一级缓存

### 一级缓存的说明

每当我们使用MyBatis开启一次和数据库的会话，MyBatis会创建**一个`SqlSession`对象表示一次数据库会话**。

在对数据库的一次会话中，我们有可能会反复地执行完全相同的查询语句，如果不采取一些措施的话，每一次查询都会查询一次数据库，而我们在极短的时间内做了完全相同的查询，那么它们的结果极有可能完全相同，由于查询一次数据库的代价很大，这有可能造成很大的资源浪费。

为了解决这一问题，减少资源的浪费，**MyBatis会在表示会话的`SqlSession`对象中建立一个简单的缓存，将每次查询到的结果缓存起来，当下次查询的时候，如果判断先前有个完全一样的查询，会直接从缓存中直接将结果取出，返回给用户，不需要再进行一次数据库查询了。**

如下图所示，MyBatis一级缓存的简单示意图：

![img](.\images\20141121213425390.png)

**一级缓存查询的过程：**

一个SqlSession对象中创建一个本地缓存（`local cache`），对于每一次查询，都会尝试根据查询的条件去本地缓存中查找是否在缓存中，如果在缓存中，就直接从缓存中取出，然后返回给用户；否则，从数据库读取数据，将查询结果存入缓存并返回给用户。

**`对于会话（Session）级别的数据缓存，我们称之为一级数据缓存，简称一级缓存。`**







---

### 底层实现原理

由于MyBatis使用`SqlSession`对象表示一个数据库的会话，那么，对于会话级别的一级缓存也应该是在SqlSession中控制的。

**`SqlSession`对数据库的各种操作是通过`Executor`执行器这个角色来完成的。**当创建一个SqlSession对象时，MyBatis会为这个SqlSession对象创建一个Executor执行器，而缓存信息就维护在这个Executor执行器中，MyBatis将缓存和缓存相关的操作封装成了Cache接口中。

SqlSession、Executor、Cache之间的关系如下列类图所示：

<img src=".\images\20141120100824184.png" alt="img" style="zoom: 80%;" />

如上述的类图所示，Executor接口的实现类BaseExecutor中拥有一个Cache接口的实现类**`PerpetualCache`**，则对于BaseExecutor对象而言，它将使用PerpetualCache对象维护缓存。

综上，SqlSession对象、Executor对象、Cache对象之间的关系如下图所示：

<img src=".\images\20141119164906640.png" alt="img" style="zoom: 80%;" />

**由于Session级别的一级缓存实际上就是使用`PerpetualCache`维护的。**

那PerpetualCache是怎样实现的呢？

PerpetualCache实现原理其实很简单，其内部就是通过一个简单的**`HashMap<Object, Object>`**来实现的，没有任何其他的限制。

![image-20240524171848490](.\images\image-20240524171848490.png)



**总结**

> 1、**`SqlSession`**使用**`Executor`**执行器来执行数据库的操作。
>
> 2、在**`Executor`**执行器中，有一个Cache的实现类**`PerpetualCache`**对象属性，该类中有一个HashMap类型的属性来维护缓存信息。







---

### 生命周期

> * MyBatis在开启一个数据库会话时，会创建一个新的SqlSession对象，SqlSession对象中会有一个新的Executor对象，Executor对象中持有一个新的PerpetualCache对象；当会话结束时，`SqlSession`对象及其内部的`Executor`对象还有`PerpetualCache`对象也一并释放掉，那么此时一级缓存就会失效。
> * 如果SqlSession调用了`close()`方法，会释放掉一级缓存PerpetualCache对象，一级缓存将不可用；
> * 如果SqlSession调用了`clearCache()`方法，会清空PerpetualCache对象中的数据，但是该对象仍可用。
> * SqlSession中执行了任何一个`update`操作（update()、delete()、insert()）`会清空PerpetualCache对象的数据`，但是该对象仍然可用。

![img](.\images\20141120104257906.png)







---

### 一级缓存的工作流程（:star:）

#### 整体工作流程

> 1. 对于某个查询，根据`statementId`,`params`,`rowBounds`来构建一个key值，根据这个key值去缓存Cache中取出对应的key值存储的缓存结果；
> 2. 判断从Cache中根据特定的key值对应是否有value值，即是否命中。
> 3. 如果命中，则直接将缓存结果返回；
> 4. 如果没命中：
>    1. 去数据库中查询数据，得到查询结果；
>    2. 将key和查询得到的结果分别作为key，value存储到Cache中；
>    3. 将查询结果返回；
> 5. 结束。

这里的statementId表示的是sql的id、params表示参数、rowBounds表示分页数据。

![img](.\images\20141120133247125)

具体key值的构建，在下面重点讨论。







#### 判断是否命中缓存的条件（重要）

如下图所示，MyBatis定义了一个org.apache.ibatis.cache.Cache接口作为其Cache提供者的SPI（Service Provider Interface），所有的Mybatis内部的Cache缓存，都应该实现这一接口。**MyBatis定义了一个`PerpetualCache`实现类实现了`Cache`接口，实际上，在SqlSession对象里的Executor对象内维护的Cache类型实例对象，就是`PerpetualCache`子类创建的。**

（mybatis内部还有很多Cache接口的实现，一级缓存只涉及到这一个PerpetualCache子类，Cache的其他实现将会放在二级缓存中介绍）

<img src=".\images\20141120134402285" alt="img" style="zoom:80%;" />

> **我们知道，`Cache`最核心的实现其实就是一个`Map`，将本次查询使用的特征作为`key`，将查询结果作为`value`存储到`Map`中。**

那么，现在的问题就是：怎样去生成Map集合的key，去判断两次查询是否相等呢？

MyBatis认为，对于两次查询，如果以下条件都完全一样，那么就认为它们是完全相同的两次查询：

> 1. **传入的`statementId`（sql的id）**
> 2. **查询时要求的结果集中的结果范围（结果的范围通过`rowBouds.offset`和`rowBounds.limit`表示，即分页信息）**
> 3. **这次查询最终传递给JDBC的Preparedstatement的`sql语句字符串`**
> 4. **`参数值`**

来分别解释一下上面的四个条件：

1、statementId，就是sql标签所对应的id；

2、mybatis的分页功能是通过RowBounds来实现的，它通过rowBounds.offset和rowBounds.limit来过滤查询出来的结果集，也就是说，条件之一是结果集的分页范围是否相同。

3、MyBatis底层是使用JDBC来实现的，MyBatis会使用SQL字符串创建JDBC的PreparedStatement对象，那么，这里就要求使用的SQL字符串是一致的，并且创建PreparedStatement对象后，还需要去设置参数，那么要求对JDBC的PreparedStatement的参数值也要一致。

也就是说，第3个条件，是要求传递给JDBC的SQL语句完全一致；第4条是要求传递给JDBC的参数也完全一致。

对于3、4条件，我们来举一个例子：

```xml
  <select id="selectByCritiera" parameterType="java.util.Map" resultMap="BaseResultMap">
        select employee_id,first_name,last_name,email,salary
        from louis.employees
        where  employee_id = #{employeeId}
        and first_name= #{firstName}
        and last_name = #{lastName}
        and email = #{email}
  </select>
```

如果使用上述的selectByCritiera进行查询，那么MyBatis会将上述的SQL中的#{}都替换成?，如下：

```mysql
select employee_id,first_name,last_name,email,salary
from louis.employees
where employee_id = ?
and first_name= ?
and last_name = ?
and email = ?
```

MyBatis最终会使用上述的SQL字符串创建JDBC的PreparedStatement对象，对于这个PreparedStatement对象，还需要对它设置参数，调用setXXX()来完成设值，第4的条件，就是要求对设值JDBC的PreparedStatement的参数值也要完全一样。



**疑问**：statementId（也就是sql标签的id）相同，那SQL字符串也就相同，为什么这里还要要求传给JDBC的SQL字符串要相同呢？

其实，statementId相同，对应的SQL语句不一定相同，因为在sql标签中，有可能使用一些动态SQL进行查询操作，比如使用了if标签，有的查询满足if标签，有的查询不满足if标签，此时二者虽然是同一个statementId，属于同一个方法，但是因if动态sql标签的存在，实际的查询SQL语句是不一样的，所以在要求statementId相同的基础上，还需要要求传递给JDBC的SQL语句相同。

**总结：**

CacheKey由以下条件决定：

> **`statementId + rowBounds + 传递给JDBC的SQL + 传递给JDBC的参数值`**





---

### 性能分析

举例：看下面的例子，下面的例子使用了同一个SqlSession指令了两次完全一样的查询，将两次查询所消耗的时间打印出来：

```java
public class SelectDemo1 {
 
	private static final Logger loger = Logger.getLogger(SelectDemo1.class);
	
	public static void main(String[] args) throws Exception {
		SqlSession sqlSession = SqlSessionUtils.getSqlSession();

		Map<String,Object> params = new HashMap<String,Object>();
        //查询工资低于10000的员工
		params.put("min_salary",10000);
        
		
		Date first = new Date();
		//第一次查询
		List<Employee> result = sqlSession.selectList("com.louis.mybatis.dao.EmployeesMapper.selectByMinSalary",params);
		loger.info("first quest costs:"+ (new Date().getTime()-first.getTime()) +" ms");
        
		Date second = new Date();
        //第二次查询
		result = sqlSession.selectList("com.louis.mybatis.dao.EmployeesMapper.selectByMinSalary",params);
        
		loger.info("second quest costs:"+ (new Date().getTime()-second.getTime()) +" ms");
	}
 
}
```

运行结果：

![img](.\images\20141119161312532)

由上面的结果你可以看到，第一次查询耗时464ms，而第二次查询耗时不足1ms。这是因为第一次查询后，MyBatis会将查询结果存储到SqlSession对象的缓存中，当后来有完全相同的查询时，直接从缓存中将结果取出。

我们对上例做一个更改：在第二次查询前，往HashMap类型的参数中添加一些无关值，然后再去执行，看查询结果：

![img](.\images\20141120171810031)

由上述的结果，我们应该知道：

**MyBatis所认为的参数完全相同，并不是指使用sqlSession查询时的传递的所有参数值完全相同，只需要去保证SQL语句的占位符所使用的参数值相同即可**。





---

### 问题的说明

由于缓存的存在，当在SqlSession两次执行相同的查询操作之间，有其他的SqlSession修改了其中的数据，但是读取的结果依旧是前后一样的。这是不是一种"脏读"，算不算缓存存在的问题？

我觉得这不算是一个问题。我们知道，在数据库层面，使用Repeatable Read的隔离级别是为了解决不可重复读的问题。不可重复读，即事务前后读取相同的数据，结果不一致。上述缓存的特征，不就解决了这一问题吗？所以，我觉得，这反而是缓存存在的好处，并不是一个问题。

所以，当我们想要在后续的查询中，查询修改后的数据，可以去清除缓存，或关闭缓存来达到。



---

### 一级缓存失效的四种情况

```
1. 不同的SqlSession对应不同的一级缓存
2. 同一个SqlSession但是查询条件不同
3. 同一个SqlSession两次查询期间执行了任何的增删改操作
4. 同一个SqlSession两次查询期间手动清空了缓存clearCache()
```



## 3、二级缓存

### 二级缓存的使用

二级缓存是用来解决一级缓存不能跨会话共享的问题，**范围是`namespace`级别的，可以被多个`SqlSession`共享，要求`sqlSessionFactory`是同一个**（只要同一个接口里面有相同方法，都可以共享），生命周期和应用同步。

**二级缓存开启的条件**

> 1. **在核心配置中，设置全局配置属性`cacheEnabled=true`，默认是开启的状态，所以无需进行设置。**
> 2. **在映射文件中设置标签`<cache />`**（每个Mapper分配一个Cache缓存），或者**`<cache-ref>`**标签（多个Mapper共用一个Cache缓存）。
> 3. **二级缓存必须在`SqlSession`关闭或提交之后生效。**
> 4. **查询的数据所转换的实体类必须要实现`序列化接口`。**

那么，也就是说，当我们没有去关闭SqlSession，或者没有提交SqlSession时，此时查询的数据会保存到一级缓存中；当我们关闭或者提交SqlSession时，此时查询的数据才会保存到二级缓存中。



**二级缓存实现案例**

案例：去查询t_emp表中id为3的数据，第一次正常查询，第二次使用二级缓存。

1、首先，我们需要先去设置全局数据，将cacheEnable设置为true（默认就是true，也可不设置）

<img src=".\images\image-20240524225539408.png" alt="image-20240524225539408" style="zoom:67%;" /> 

2、其次，我们需要SQL映射文件中，使用cache标签开启二级缓存

<img src=".\images\image-20240524224926534.png" alt="image-20240524224926534" style="zoom:67%;" /> 

3、然后，我们需要让查询接收的实体类（这里是Emp类）去实现序列化的接口Serializable

<img src=".\images\image-20240524225029099.png" alt="image-20240524225029099" style="zoom:67%;" /> 

这样一来，使用二级缓存的配置就完成了，现在，我们就需要通过同一个SqlSessionFactory去创建两个不同的SqlSession，从而来测试二级缓存。

由于二级缓存要求使用的SqlSession是由同一个SqlSessionFactory创建的，所以我们不能使用SqlSessionUtils工具类去创建SqlSession对象，因为每次调用都会去生成一个新的SqlSessionFactory。

测试：

```java
@Test
public void test01() throws IOException {
    InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    //创建两个SqlSession对象
    SqlSession sqlSession1 = sqlSessionFactory.openSession(true);
    SqlSession sqlSession2 = sqlSessionFactory.openSession(true);
    
    //分别使用这两个SqlSession对象去查询
    EmpMapper mapper1 = sqlSession1.getMapper(EmpMapper.class);
    Emp emp1 = mapper1.getEmpById(3);
    System.out.println(emp1);

    EmpMapper mapper2 = sqlSession2.getMapper(EmpMapper.class);
    Emp emp2 = mapper2.getEmpById(3);
    System.out.println(emp2);
}
```

此时，我们来看看查询中的日志信息：

![image-20240524225757439](.\images\image-20240524225757439.png)

从结果中我们可以得知，第二次查询并没有去使用二级缓存，而还是去数据库中进行了查询。

为什么这样？

因为在测试中，还缺少了二级缓存中使用的条件之一，也就是SqlSession必须要关闭或提交之后才会生效。

即，当SqlSession1查询之后，必须去调用`close()`方法或者`commit()`方法，将SqlSession1关闭或提交之后，其查询到的数据才会放到二级缓存中。

所以，需要将上例测试中的SqlSession1进行关闭或提交：

```java
@Test
public void test01() throws IOException {
    InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    //创建两个SqlSession对象
    SqlSession sqlSession1 = sqlSessionFactory.openSession(true);
    SqlSession sqlSession2 = sqlSessionFactory.openSession(true);
    
    //分别使用这两个SqlSession对象去查询
    EmpMapper mapper1 = sqlSession1.getMapper(EmpMapper.class);
    Emp emp1 = mapper1.getEmpById(3);
    System.out.println(emp1);
    
    //关闭SqlSession1
    sqlSession1.close();

    EmpMapper mapper2 = sqlSession2.getMapper(EmpMapper.class);
    Emp emp2 = mapper2.getEmpById(3);
    System.out.println(emp2);
}
```

此时，我们再来看看查询的结果：

![image-20240524230151231](.\images\image-20240524230151231.png)

可以看到，第一次是去数据库中查询了数据，但是第二次的信息是：Cache Hit Ratio，表示命中了缓存，后面的0.5是命中缓存的概率，这里是50%。



**二级缓存失效的情况**

两次查询之间执行了任意的增删改，会使一级和二级缓存同时失效。



**如果某些查询方法对数据的实时性要求很高，不需要二级缓存怎么办？**

我们可以在单个statement上显式地关闭二级缓存，使用useCache标签，将其设置为false（默认是true，即使用二级缓存）

```xml
<select id="selectBlog" resultType="employee" useCache="false">
```





### cache标签属性的配置

| **属性**      | **含义**                           | **取值**                                                     |
| ------------- | ---------------------------------- | ------------------------------------------------------------ |
| type          | 缓存实现类                         | 指定自定义缓存类型，需要使用Cache接口，默认是PrepetualCache  |
| size          | 最多缓存对象个数                   | 默认是1024                                                   |
| eviction      | 回收策略                           | 多出的缓存对象回收策略LRU-最近最少使用的：移除最长时间不被使用的对象（默认）FIFO-先进先出：按对象进入缓存的顺序移除他们SOFT-软引用：移除基于垃圾回收器状态和软引用规则的对象WEAK-弱引用：更积极地移除基于垃圾回收器状态和弱引用规则的对象 |
| flushInterval | 定时自动清空缓存间隔               | 默认情况下，不自动情况缓存。该参数用于设置自动清空缓存间隔，单位是ms |
| readOnly      | 是否只读                           | true：只读。mybatis认为所有从缓存中获得数据的操作都是只读操作，不会修改数据。mybatis为了加快速度，直接就会将数据在缓存中的引用交给客户。不安全，速度快。false（默认）：非只读。mybatis觉得获取的数据可能会被修改。mybatis会利用序列化&反序列化的技术克隆一份新的数据给你。安全，速度慢。 |
| blocking      | 是否使用可重入锁实现缓存的并发控制 | true:会使用BlockingCache对Cache进行装饰。默认为false。       |



### 二级缓存的机制

 <img src=".\images\20141123125616381" alt="img" style="zoom:67%;" />

如上图所示，当开一个会话时，一个SqlSession对象会使用一个Executor对象来完成会话操作，MyBatis的二级缓存机制的关键就是对这个Executor对象做文章。

如果用户配置了`cacheEnabled=true`（默认），那么MyBatis在为SqlSession对象创建Executor对象时，会对Executor对象加上一个装饰者**`CachingExecutor`**，这时SqlSession使用CachingExecutor对象来完成操作请求。

**`CachingExecutor`对于查询请求，会先判断该查询请求在`Application`级别的二级缓存中是否有缓存结果，如果有查询结果，则直接返回缓存结果；如果缓存中没有，再交给真正的`Executor`对象来完成查询操作**，再去一级缓存中查询，都没有再去数据库中查询。之后，CachingExecutor会将真正的Executor返回的查询结果放置到缓存中，然后返回给用户。

![img](.\images\20141123125640998)

CachingExecutor是Executor的装饰者，以增强Executor的功能，使其具有缓存的功能，这里使用到了设计模式中的**装饰者模式**。





### MyBatis缓存查询的顺序

请注意，如果你的MyBatis使用了二级缓存，并且你的Mapper中也配置了二级缓存，那么在select查询的时候，会先从二级缓存中取输入，其次才是一级缓存，即MyBatis查询数据的顺序是：

**`二级缓存 -> 一级缓存 -> 数据库`**

并且当二级缓存与一级缓存中均没有对应的数据时，会去从数据库中查询数据，并将查询到的数据存入到一级缓存中。

> **`当SqlSession关闭或提交后，SqlSession中保存的一级缓存会放到二级缓存中，自身的一级缓存会被释放`。**

别的SqlSession去查询时，会去查询Application级别的二级缓存，没有再去自己的一级缓存中查询。









---

# 六、逆向工程

正向工程：先创建Java实体类，由框架负责根据实体类生成数据库表。

逆向工程：先创建数据库表，由框架负责根据数据库表，反向生成如下资源：

* Java实体类
* Mapper接口
* Mapper映射文件

也就是说，我们可以根据数据表去创建相关的实体类以及Mapper文件，从而让程序员将更多的精力放在繁杂的业务逻辑上。



### 构建过程

**1、引入相关依赖**

首先，引入mybatis的核心依赖信息以及MySQL驱动等，这里还没有涉及到逆向工程。

```xml
 <dependencies>
	<!-- MyBatis核心依赖包 -->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.7</version>
    </dependency>
    <!-- junit测试 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.3.1</version>
    </dependency>
    <!-- MySQL驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.30</version>
    </dependency>
     
     
     <!--log4j2的依赖-->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.19.0</version>
        </dependency>

        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j2-impl</artifactId>
            <version>2.19.0</version>
        </dependency>
</dependencies>
```

**2、创建逆向工程的插件信息，并且配置插件所需的依赖**

在pom.xml中，在build标签里，使用plugins标签中的`pulgin`标签表示来给工程创建插件。

逆向工程的插件是mybatis-generator-maven-plugin，然后，我们还需要引入该插件所需的依赖信息

```xml
<!-- 控制Maven在构建过程中相关配置 -->
<build>
    <!-- 构建过程中用到的插件 -->
    <plugins>
        <!-- 具体插件，逆向工程的操作是以构建过程中插件形式出现的 -->
        <plugin>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-maven-plugin</artifactId>
            <version>1.3.0</version>

            <!-- 插件的依赖 -->
            <dependencies>
                <!-- 逆向工程的核心依赖 -->
                <dependency>
                    <groupId>org.mybatis.generator</groupId>
                    <artifactId>mybatis-generator-core</artifactId>
                    <version>1.3.2</version>
                </dependency>
                <!-- 数据库连接池 -->
                <dependency>
                    <groupId>com.alibaba</groupId>
                    <artifactId>druid</artifactId>
                    <version>1.2.15</version>
                </dependency>
                <!-- MySQL驱动 -->
                <dependency>
                    <groupId>mysql</groupId>
                    <artifactId>mysql-connector-java</artifactId>
                    <version>8.0.30</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```

刷新maven，此时在工程的maven下，就有了该逆向工程的插件，之后，如果我们想要去通过逆向工程的方式构建项目，直接使用该插件即可。

<img src=".\images\image-20240525123434827.png" alt="image-20240525123434827" style="zoom: 50%;" /> 



**3、创建mybatis的核心配置文件以及其他配置文件信息**

逆向工程会帮助我们根据表创建实体类、Mapper接口以及Mapper映射文件信息，但是对于mybatis的核心配置文件，以及其他的配置信息，比如数据源信息、日志文件等还是都需要自己去配置。

创建jdbc.properties，配置数据源信息

```properties
jdbc.username=root
jdbc.password=061535asd
jdbc.url=jdbc:mysql://localhost:3306/mybatis
jdbc.driverClassName=com.mysql.cj.jdbc.Driver
```

创建mybatis核心配置文件mybatis-config.xml，引入数据源，并配置mybatis的相关配置；

创建log4j2.xml文件，配置日志信息

上述三个创建配置文件的过程这里就不再详细阐述了，前面都有。



**4、创建逆向工程的配置文件**

在resources目录下，创建一个名叫generatorConfig.xml的文件（固定的名字），该文件就是逆向工程的配置文件信息。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!--
    targetRuntime: 执行生成的逆向工程的版本
    MyBatis3Simple: 生成基本的CRUD
    MyBatis3: 生成带条件的CRUD
    -->
    <context id="DB2Tables" targetRuntime="MyBatis3Simple">
        
        <!--是否去除自动生成的注释-->
    	<commentGenerator>
        	<property name="suppressAllComments" value="true" />
    	</commentGenerator>
        
        <!-- 数据库的连接信息 -->
        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/mybatis"
                        userId="root"
                        password="061535asd">
            <property name="nullCatalogMeansCurrent" value="true" />
        </jdbcConnection>
        
        <!-- javaBean的生成策略-->
        <javaModelGenerator targetPackage="com.atguigu.mybatis.pojo" targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
        
        <!-- SQL映射文件的生成策略 -->
        <sqlMapGenerator targetPackage="com.atguigu.mybatis.mapper"
                         targetProject=".\src\main\resources">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>
        
        <!-- Mapper接口的生成策略 -->
        <javaClientGenerator type="XMLMAPPER"
                             targetPackage="com.atguigu.mybatis.mapper" targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>
        <!-- 逆向分析的表 -->
        <!-- tableName设置为*号，可以对应所有表，此时不写domainObjectName -->
        <!-- domainObjectName属性指定生成出来的实体类的类名 -->
        <table tableName="t_emp" domainObjectName="Emp"/>
        <table tableName="t_dept" domainObjectName="Dept"/>
    </context>
</generatorConfiguration>
```

对上面标签的解释：

1、`context`标签中，存储所有逆向工程的配置信息，其中的targetRuntime属性，指明该逆向工程是何种类型，比较常用的有两种，一种是MyBatis3Simple，还有一种是MyBatis3。MyBatis3Simple，生成的Mapper接口中只包含基本的CRUD方法；MyBatis3中，包含着带有条件的CRUD。



2、`jdbcConnection`标签，用来配置数据库的连接信息。

当使用的mysql数据库版本是8时，生成实体类会在数据库中全面搜索表名，如果存在多个相同表名，那么生成的实体类可能出现问题：

<img src=".\images\image-20240525134549145.png" alt="image-20240525134549145" style="zoom:50%;" /> 

这里生成了一些多余的类。

解决方案：

在generatorConfig.xml文件中数据库的连接信息配置中添加一句代码

`<property name="nullCatalogMeansCurrent" value="true"/>`



3、`javaModelGenerator`标签，是用来配置JavaBean的生成策略的，也就是数据表所对应的实体类的生成策略。其中配置两个属性：

* targetPackage属性指明生成实体类放在哪个包下；
* targetProject指明生成的位置；

在javaModelGenerator标签中，还配置了两个property标签，这两个标签的含义：

* enableSubPackages：表示是否使用子包。也就是在targetPackage中的.点是否表示一层包的含义，如果该属性设置为false，com.atguigu的含义就是这个包的名称是com.atguigu，而不是表示com包下的子包atguigu.（默认就是true，可以无需设置）
* trimStrings：表示是否去除空格。将字段名转换成属性时，字段名前后可能存在着空格，当trimStrings为true时，字段名转换成属性时就将空格去掉。（默认也是true，无需设置）



4、`sqlMapGenerator`标签，用来配置SQL映射文件的生成策略，属性与javaModelGenerator标签一致。



5、`javaClientGenerator`标签，用来配置Mapper接口的生成策略，其中有一个type属性，该属性的值为XMLMAPPER。



6、`table`标签，就是设置我们用来逆向分析的表，其中`tableName`属性用于指定表名，`domainObjectName`属性用来指定生成出来的实体类的类名。同时，Mapper接口名以及Mapper映射文件名，就会根据实体类的类名，自动生成以`实体类名Mapper`作为这两个文件的名称。



这样，逆向工程的配置文件就配置好了。

接下来，我们就可以通过运行插件的方式，构建逆向工程。

**5、运行插件，构建逆向工程**

在maven中的该工程下，双击逆向工程插件：

<img src=".\images\image-20240525133356447.png" alt="image-20240525133356447" style="zoom:50%;" /> 

原工程的信息：工程中只有一些配置文件的信息

<img src=".\images\image-20240525133420018.png" alt="image-20240525133420018" style="zoom:50%;" /> 

运行逆向工程插件后：

<img src=".\images\image-20240525133954142.png" alt="image-20240525133954142" style="zoom:50%;" /> 

这样，通过逆向工程去构建项目的方式就已经完成了。







### MyBatis3方式构建的类说明

通过MyBatis3Simple方式逆向工程构建项目所生成的Mapper接口中，仅包含最基本的CRUD操作：

<img src=".\images\image-20240525135411081.png" alt="image-20240525135411081" style="zoom:80%;" /> 

其中包含5个方法，分别表示：

1、根据主键删除

2、插入

3、根据主键查询

4、查询所有

5、根据主键修改

生成的实体类中，也只包含列名所对应的属性，以及属性的get()、set()方法：

<img src=".\images\image-20240525135553592.png" alt="image-20240525135553592" style="zoom:67%;" /> 

MyBatis3Simple方式所生成的Mapper接口以及实体类中，其中的方法还是比较简单的，现在，我们使用逆向工程MyBatis3的方式去生成表所对应的各种类。

1、首先，去将原本MyBatis3Simple所生成的类删除，防止因为重复执行逆向工程，出现往生成类中追加数据的方式构建。

2、然后，去将generatorConfig.xml中，context标签下的targetRuntime属性设置为MyBatis3：

![image-20240525140054219](.\images\image-20240525140054219.png) 

3、最后，双击逆向工程的插件，构建MyBatis3类型的逆向工程。

构建出来的项目：

<img src=".\images\image-20240525140317123.png" alt="image-20240525140317123" style="zoom: 67%;" /> 

与MyBatis3Simple方式不同的是，使用MyBatis3构建出来的逆向工程，多出了DeptExample与EmpExample类。这两个类是用来实现条件查询的。



#### 实体类

我们来看看生成的Emp与Dept实体类

<img src=".\images\image-20240525140624615.png" alt="image-20240525140624615" style="zoom: 67%;" /> 

根据类生成的实体类，和使用MyBatis3Simple方式所生成的实体类一样，都是列所对应的属性，以及对应的get()、set()方法，其中不包含构造方法（默认空参）。

为了方便测试，在实体类中生成相应的无参构造器、有参构造器以及toString()方法。



#### Mapper接口

在Mapper接口中，就比使用MyBatis3Simple方式多了一些方法：

<img src=".\images\image-20240525141000055.png" alt="image-20240525141000055" style="zoom: 80%;" /> 

其中的xxxByExample()方法，就表示根据条件进行操作，这个根据条件，在后面会有详细的例子。

对于上面的方法说明：

**删除**

* `deleteByExample`()，表示根据条件进行删除。
* `deleteByPrimaryKey`()，表示根据主键信息进行删除。

**查询**

* `countByExample`()，表示根据条件，获取数据表中的记录数。

* `selectByExample`()：根据条件进行查询。
* `selectByPrimaryKey`()：根据主键进行查询。

**添加**

* `insert`()：普通的添加。

* `insertSelective`()：选择性添加。

**什么是普通添加，什么是选择性添加？**

当传输过来的对象参数中，某属性为null，普通添加就会将该null作为属性所对应的新记录的列值，进行添加；而选择性添加则不会将null属性所对应的列作为添加的字段。

**修改**

* `updateByExampleSelective`():根据条件选择性修改
* `updateByExample`()：根据条件修改
* `updateByPrimaryKeySelective`()：根据条件选择性修改
* `updateByPrimaryKey`()：根据主键修改

**什么是选择性修改，什么是普通修改呢？**

一样地，当传输过来的对象参数中，某属性为null，普通修改就是将该属性所对应的列的值也修改成null；而选择性修改则是不会去修改该属性所对应的列。



使用逆向工程生成，所生成的具体是如何实现的，我们不需要去了解，只需要知道该怎么用就可以了。逆向工程所生成的方法名，以及其中的格式，都可以学习学习，作为自己创建类的标准。





#### 方法的使用

**案例1：查询t_emp表中所有的数据**

生成的查询数据的方法有两个：selectByExample()与selectByPrimaryKey()，分别是根据条件进行查询以及根据主键进行查询。

这里使用的应该是selectByExample()，查询所有的数据，没有条件，那么此时传入的Example就是null：

```java
@Test
public void testMBG() throws IOException {
    InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    SqlSession sqlSession = sqlSessionFactory.openSession(true);
    EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
    
    for (Emp emp : mapper.selectByExample(null)) {
        System.out.println(emp);
    }
}
```

执行结果：

![image-20240525144523438](.\images\image-20240525144523438.png)

此时，就将t_emp表中的数据全部查询出来了。



**案例2：根据条件进行查询**

根据条件进行查询，也就是使用逆向工程给我们创建好的selectByExample()方法，xxxByExample()方法就是根据指定条件来进行查询的，这里的根据条件，实际上就是语句后面的where子句。

xxxByExample()方法，需要传入xxxExample类型，该类型在逆向工程构建时就会给我们创建好：

<img src=".\images\image-20240525145105220.png" alt="image-20240525145105220" style="zoom: 67%;" /> 

![image-20240525145206825](.\images\image-20240525145206825.png) 

在该类中，会给我们去声明一个`Criteria`内部类，该类表示条件的意思。通过调用Example类的**`createCriteria()`方法**，创建条件，该方法给我们返回Criteria类型对象。

然后，我们通过该对象，去调用**`and属性名条件()`方法**，添加条件。并且条件方法也是返回Criteria属性，所以我们可以通过链式结构一直在后面添加条件。

`and属性名条件()`默认是使用and连接符进行连接；如果我们想使用or连接符，添加或者的条件，此时，我们可以使用Example类型对象调用**`or()`方法**，此时，之前设置的条件与这里的条件之间就是使用or连接符进行连接的。

例如：查询t_emp表中emp_name为"tom"或者email包含'@qq'字符的数据

```java
@Test
public void testMBG() throws IOException {
    InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    SqlSession sqlSession = sqlSessionFactory.openSession(true);
    EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);

    //创建Example类
    EmpExample example = new EmpExample();

    //调用createCriteria()方法，创建条件，通过and属性条件()的方法添加条件
    example.createCriteria().andEmpNameEqualTo("tom");
    //使用or()方法，将之前的条件与当前条件之间使用or连接符连接
    example.or().andEmailLike("%@qq%");

    for (Emp emp : mapper.selectByExample(example)) {
        System.out.println(emp);
    }

}
```

查看一下SQL日志：

![image-20240525150845885](.\images\image-20240525150845885.png) 

可以看到，SQL语句的where子句中有两个条件，条件之间使用了or进行连接。

查询结果：

![image-20240525150920821](.\images\image-20240525150920821.png) 







**案例3：普通操作与选择性操作区别的演示**

我们知道，逆向工程给我们提供了四种修改，分别是：

1、根据主键进行修改

2、根据主键选择性修改

3、根据条件进行修改

4、根据条件选择性修改

条件，我们已经知道该怎么配了。

现在主要来测试一下根据普通的修改与选择性修改有什么区别。

我们来看看t_emp表中id为1的数据：

![image-20240525151904382](.\images\image-20240525151904382.png)

如果我们使用普通修改，并且将sex属性设置为null：

```java
@Test
public void testMBG2(){
    InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    SqlSession sqlSession = sqlSessionFactory.openSession(true);
    EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);


    //创建Emp类，这里我们将email
    Emp emp = new Emp(1, "hello", 22, null, "nihao@qq.com", 2);
    mapper.updateByPrimaryKey(emp);
}
```

修改后的结果：

![image-20240525152855936](.\images\image-20240525152855936.png)

发现，此时表中的记录的sex被设置成了null。



如果我们使用选择性修改，并且将sex属性设置为null：

```java
@Test
public void testMBG2() throws IOException {
    InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    SqlSession sqlSession = sqlSessionFactory.openSession(true);
    EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);


    //创建Emp类，这里我们将email
    Emp emp = new Emp(1, "hello", 22, null, "nihao@qq.com", 2);
    mapper.updateByPrimaryKeySelective(emp);
}
```

修改后的结果：

![image-20240525153105016](.\images\image-20240525153105016.png)

此时，就没有去将sex值进行修改。



**由这两个结果，我们可以得知：**

普通修改，传入的属性是什么值，就修改成什么值，是null就修改成null。

选择性修改，在修改字段时，会进行一个判断操作，如果传过来的属性是null，则不去修改该属性所对应的字段。





---

# 七、分页Page

在mysql中，分页操作是通过limit语句来完成的。如果我们自己去写语句，那么分页肯定是没有任何问题的。

但是，当model多起来，表多起来，复杂起来之后，我们使用逆向工程来生成相应的pojo和mapper，方法与SQL就不需要我们来写了，但是也会带来弊端，比如分页问题就不方便解决了。

那么，我们就可以使用分页插件的方式实现分页操作。



### 配置步骤

**1、引入相关依赖**

```xml
<dependency>
	<groupId>com.github.pagehelper</groupId>
	<artifactId>pagehelper</artifactId>
	<version>5.2.1</version>
</dependency>
```

**2、配置分页插件**

在MyBatis的核心配置文件中配置插件

```xml
<plugins>
    <!--设置分页插件-->
    <plugin interceptor="com.github.pagehelper.PageInterceptor"></plugin>
</plugins>
```

在之前学习MyBatis核心配置文件内容中学习过，核心配置文件中的各个标签是有顺序的，该插件应该放在environments标签之前：

<img src=".\images\image-20240525154544613.png" alt="image-20240525154544613" style="zoom: 50%;" /> 

这样一来，分页插件的配置就完成了，接下来我们就可以使用了。



### 分页插件的使用

在使用之前，我们来回顾一下分页limit的两个参数：

`LIMIT index, pageSize`

* index表示当前页的起始索引

* pageSize表示当前页的页码

比如：查询第21条到第30条的记录：SELECT * FROM 表名 LIMIT 20, 10

还有另一个参数：`pageNum`表示当前页的页数

那么，LIMIT index, pageSize也可以改成使用pageNum表示：

`LIMIT (pageNum-1)*pageSize, pageSize`





**那么，该如何使用分页插件呢？**

**在查询语句之前，调用`PageHelper`类中的静态方法`startPage(int pageNum, int pageSize)`开启分页功能**

这样一来，后续的查询操作就会自动使用分页功能。

其中，`pageNum`就是页码；`pageSize`就是一页有多少数据。

使用页码的方式比使用index的方式，更加的清晰易懂，也更符合业务的需求。

案例：分页查询t_emp表中的数据，访问第2页，每页4条数据

```java
@Test
public void testPageHelper() throws IOException {
    InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    SqlSession sqlSession = sqlSessionFactory.openSession(true);
    EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);

    //在查询操作之前，调用PageHelper的startPage()方法
    PageHelper.startPage(2, 4);
    List<Emp> emps = mapper.selectByExample(null);
    emps.forEach(System.out::println);

}
```

我们来看看打印的日志信息：

![image-20240525161513770](.\images\image-20240525161513770.png)

可以看到，查询方法使用了limit分页，分页语句是LIMIT 4,4。

第二页，则pageNum是2，index=(pageNum - 1) * pageSize = 1 * 4 = 4。





### Page信息（PageInfo类:star:）

startPage()方法，返回的是一个`Page`类型的对象，我们将该Page对象输出一下查看一下信息：

![image-20240525170346350](.\images\image-20240525170346350.png) 

输出结果：

![image-20240525170406426](.\images\image-20240525170406426.png)

上述的Page对象中，包含了分页的一些信息：

pageNum：当前页的页码

pageSize：每页显示的条数

startRow：从第几行开始

endRow：到第几行结束

total：总行数

pages：总页数

以及查询后得到的分页结果。







如果上面的信息不够，使用**`PageInfo`**类来输出分页的完整信息：

在查询方法后，创建PageInfo对象，在构造器中可以传入两种参数：

![image-20240525172759408](.\images\image-20240525172759408.png) 

第一个参数是查询分页后的结果，第二种类型的第二个参数，指的是导航分页的页码数。

什么是导航分页的页码数？

我们知道，在浏览器的页面中，假如数据很多就会有很多页，数据的底部就会显示页码：

![image-20240525173227726](.\images\image-20240525173227726.png) 

这就是导航分页的页码。

一般来说，我们会将当前页的页码放在最中间，其余两侧分别会使用等长的页码以供选择，比如，当前页在第3页，那么导航页就可能为：

1 2 3 4 5

可以让我们选择1-5的页。

那么，在创建PageInfo对象时，所传入的第二个参数，就是去设置导航页的页码数量的，比如传入5，就表示导航页使用5个页码来显示。

```java
InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
SqlSession sqlSession = sqlSessionFactory.openSession(true);
EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);

//在查询操作之前，调用PageHelper的startPage()方法
Page<Object> page = PageHelper.startPage(3, 2);
List<Emp> emps = mapper.selectByExample(null);
//创建PageInfo对象，传入查询的结果集与导航页页码数
PageInfo<Emp> pageInfo = new PageInfo<>(emps, 5);
System.out.println(pageInfo);
```

导航页会给当前页码左右等量地进行分配，上例中当前页码为3，导航页码数为5，包括中间的页码左右各为两个，导航页码为：

1 2 3 4 5

执行结果得到的结果：

 <img src=".\images\image-20240525173728887.png" alt="image-20240525173728887" style="zoom:80%;" /> 

可以看到，内容非常多，其中的list属性，实际就是Page对象，我们将这部分内容去除掉，单独讲讲除了Page对象以外的内容信息：

```json
PageInfo{pageNum=3, pageSize=2, size=2, startRow=5, endRow=6, total=12, pages=6, prePage=2, nextPage=4, isFirstPage=false, isLastPage=false, hasPreviousPage=true, hasNextPage=true, navigatePages=5, navigateFirstPage=1, navigateLastPage=5, navigatepageNums=[1, 2, 3, 4, 5]}
```

* `pageNum`：当前页的页码
* `pageSize`：每页显示的条数
* `size`：当前页显示的真实条数
* `startRow`：从总行数中的第几行开始
* `endRow`：从总行数中的第几行结束
* `total`：总记录数
* `pages`：总页数
* `prePage`：上一页的页码
* `nextPage`：下一页的页码
* `isFirstPage`/`isLastPage`：是否是第一页/最后一页
* `hasPreviousPage`/`hasNextPage`：是否存在上一页/下一页
* `navigatePages`：导航页的页码数
* `navigateFirstPage`：导航页中第一个页码
* `navigateLastPage`：导航页中最后一个页码
* `navigatepageNums`：导航页的页码信息

如果我们去修改一下，将要当前查询的页数修改成5：

```java
Page<Object> page = PageHelper.startPage(5, 3);
List<Emp> emps = mapper.selectByExample(null);
PageInfo<Emp> pageInfo = new PageInfo<>(emps, 5);
System.out.println(pageInfo);
```

那么，此时的导航页码就应该是[3, 4, 5, 6, 7]

<img src=".\images\image-20240525181510584.png" alt="image-20240525181510584" style="zoom:80%;" /> 

中间是5，总共页码数是5





### 总结

那么，由此我们可以知道，**分页插件的使用步骤是**：

> 1. 在查询之前，调用`PageHelper.startPage(pageNum, pageSize)`方法，表明下一个查询使用分页；
>
> 2. 在查询之后，创建PageInfo对象：`PageInfo<> pageInfo = new PageInfo<>(list, navigatePages);`，获取分页的相关信息。
>
>    其中list是使用分页查询得到的结果，navigatePages是去设置导航分页的页码数。
