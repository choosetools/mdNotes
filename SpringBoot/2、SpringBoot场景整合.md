#  整合SSM

# 整合SSM步骤总结

## 目录结构

<img src=".\images\image-20240613151704516.png" alt="image-20240613151704516"  /> 

## 引入依赖

创建SpringBoot项目，首先需要让项目作为SpringBoot-parent项目的子项目（保证父工程或者当前工程使用spring-boot-starter-parent作为父工程），在pom.xml中，引入：

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.0.5</version>
    <relativePath/>
</parent>
```

然后就是引入各种依赖：

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
    
    <!--分页插件-->
    <dependency>
        <groupId>com.github.pagehelper</groupId>
        <artifactId>pagehelper-spring-boot-starter</artifactId>
        <version>1.4.7</version>
    </dependency>
    
    <!--lombok-->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
    </dependency>
    
</dependencies>
```

这些依赖中，包含starter的依赖都是springboot提供给我们的，它是一个启动器的依赖。例如spring-boot-starter-web是Web工程所需的基本所有依赖的整合包，并且还包括了所需的服务器以及配置信息等，所以，这就是SpringBoot的好处，约定大于配置，我们只需要引入依赖，默认的项目配置都已经配置好了，我们只需要拿来用即可。

对于mysql以及lombok这类比较常见的依赖，在其父工程的spring-boot-dependencies中已经为我们定义了版本，我们使用${}进行使用，这样就不会出现版本冲突问题。

最后，我们还**需要引入打包的插件**：

在SpringBoot项目中，如果使用maven自带的package功能，那么打成的jar包无法运行，找不到运行的入口。我们需要使用springboot的打包插件，这样打出来的jar包才能够运行：

```xml
<!--    SpringBoot应用打包插件-->
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

这样，在项目完成后，使用package打包，打成的jar包才能够使用java -jar命令运行。（具体内容，请查看本笔记的最后一章）



## 配置参数

有一些参数，是SpringBoot没法进行默认配置的，比如数据库的账号、密码，还有mapper映射文件放在哪，所以，这些配置还需要我们自己来进行配置。

在项目下的resources目录下，创建application.yml文件，在该文件中进行配置。

**基本配置：**

```yml
server:
  port: 8080
  servlet:
    context-path: /
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      url: jdbc:mysql://localhost:3306/springboot
      username: root
      password: 061535asd
      driver-class-name: com.mysql.cj.jdbc.Driver
      
mybatis:
  configuration:  # setting配置
    map-underscore-to-camel-case: true #驼峰
  type-aliases-package: com.atguigu.pojo # 配置别名
  mapper-locations: classpath:/mapper/*.xml # mapperxml位置
```



druid数据库连接池还可以配很多东西，配置信息如下：

```yml
spring:
  datasource:
    # 连接池类型 
    type: com.alibaba.druid.pool.DruidDataSource

    # Druid的其他属性配置 springboot3整合情况下,数据库连接信息必须在Druid属性下!
    druid:
      url: jdbc:mysql://localhost:3306/day01
      username: root
      password: root
      driver-class-name: com.mysql.cj.jdbc.Driver
      # 初始化时建立物理连接的个数
      initial-size: 5
      # 连接池的最小空闲数量
      min-idle: 5
      # 连接池最大连接数量
      max-active: 20
      # 获取连接时最大等待时间，单位毫秒
      max-wait: 60000
      # 申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
      test-while-idle: true
      # 既作为检测的间隔时间又作为testWhileIdel执行的依据
      time-between-eviction-runs-millis: 60000
      # 销毁线程时检测当前连接的最后活动时间和当前时间差大于该值时，关闭当前连接(配置连接在池中的最小生存时间)
      min-evictable-idle-time-millis: 30000
      # 用来检测数据库连接是否有效的sql 必须是一个查询语句(oracle中为 select 1 from dual)
      validation-query: select 1
      # 申请连接时会执行validationQuery检测连接是否有效,开启会降低性能,默认为true
      test-on-borrow: false
      # 归还连接时会执行validationQuery检测连接是否有效,开启会降低性能,默认为true
      test-on-return: false
      # 是否缓存preparedStatement, 也就是PSCache,PSCache对支持游标的数据库性能提升巨大，比如说oracle,在mysql下建议关闭。
      pool-prepared-statements: false
      # 要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。在Druid中，不会存在Oracle下PSCache占用内存过多的问题，可以把这个数值配置大一些，比如说100
      max-pool-prepared-statement-per-connection-size: -1
      # 合并多个DruidDataSource的监控数据
      use-global-data-source-stat: true
```









## 创建启动类

然后在一个包下，创建SpringBoot项目的启动类，比如我们就在com.atguigu下创建一个启动类，如：`MyApplication`。

启动类的格式是固定的：

```java
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

启动类要使用@SpringBootApplication注解修饰，并且在其中创建一个main方法，使用SpringApplication的run()方法启动项目。



## 创建配置类

虽然SpringBoot已经给我们配置了很多的参数，但是有一些参数，我们不得不使用自己去定义一个配置类，让配置类帮助我们完成配置。

比如，SpringMVC的拦截器，比如MyBatis的Mapper接口扫描路径。这些都需要自己去创建一个配置类来完成配置。

**SpringMVC的拦截器配置**

对于SpringMVC来说，其所需配置的视图解析器可以在application.yml中配置，默认已经开启静态资源（在resources/static/目录下即可自动检测），全局异常处理类只需要放在启动类所在包及其子包下即可自动配置。

但是对于拦截器来说，还需要我们自己去创建一个配置类进行配置。

首先，我们先去创建一个拦截器的类，使用该类实现

```java
@Component
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("MyInterceptor拦截器的preHandle方法执行....");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("MyInterceptor拦截器的postHandle方法执行....");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("MyInterceptor拦截器的afterCompletion方法执行....");
    }
}
```

使用该类去实现HandlerInterceptor接口，实现其中的三个方法，用于分别拦截不同的位置的请求。

注意，我们使用@Component注解将该类对象放入到IOC容器中，所以需要将该类放在启动类所在的包及其子包下（启动类会去自动扫描其所在包及其子包下所有的组件）

然后，我们去创建一个配置类，就叫WebConfig类吧，使用@Configuration修饰，表示该类是一个配置类。

**`在SpringBoot中，只要将配置类放在启动类所在包及其子包下，就会自动去扫描这些配置类，并进行相关配置。`**

```java
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    private MyInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //设置了：/**表示拦截所有路径的请求
        registry.addInterceptor(myInterceptor).addPathPatterns("/**");
    }
}
```



**MyBatis扫描路径的配置**

在MyBatis中，我们使用

```xml
<mappers>
    <package name="com.atguigu.mybatis.mapper"/>
</mappers>
```

的方式对Mapper接口与Mapper映射文件进行扫描，要求Mapper接口与Mapper映射文件要放在同一个目录结构路径下。

因为对于Mapper接口来说，我们无法使用Bean的注解来将其放入到IOC容器中，只能通过Mapper扫描的方式，因为Bean的注解只能去将去创建类的代理对象，然后将其放入到IOC容器。

在SpringBoot中，能够实现Mapper接口与Mapper映射文件分开配置路径，可以放在不同的路径下。

**在application.yml中配置Mapper映射文件的位置：**

![image-20240613151156452](.\images\image-20240613151156452.png)

这表示放在resources目录下的mapper目录中。

然后，我们在项目中，创建一个配置类，使用`@MapperScan`注解来配置Mapper接口的映射路径，同时在该配置类中，还可以配置MyBais的其他参数：

```java
@Configuration
@MapperScan("com.atguigu.mapper")
public class MybatisConfig {
}
```

注意，该类需要放在启动类所在包及其子包下，才会被扫描成配置类。





## 创建三层包结构

例：

**controller层**

com.atguigu.controller.UserController

```java
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/{pageNum}/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    public PageInfo<User> userList(
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("pageSize") Integer pageSize
    ){
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userService.queryList();
        PageInfo<User> pageInfo = new PageInfo<>(users);
        System.out.println(pageInfo);
        return pageInfo;
    }

    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    @ResponseBody
    public void updateUser(){
        userService.update();
    }
}
```





**service层**

com.atguigu.service

```java
@Service
public interface UserService {
     List<User> queryList();

     void update();
}
```

com.atguigu.service.impl

```java
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> queryList() {
        return userMapper.queryAll();
    }

    @Override
    @Transactional
    public void update() {
        //将user表id为1的姓名改成hello
        User user = new User();
        user.setId(1);
        user.setUserName("hello");
        userMapper.updateExampleById(user);

        //将user表中id为1的年龄改成1
        user.setAge(1);
        userMapper.updateExampleById(user);
    }
}
```





**mapper层**

com.atguigu.mapper.UserMapper

```java
public interface UserMapper {
    List<User> queryAll();

    void updateExampleById(@Param("user") User user);
}
```



resources/mapper/UserMaper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.atguigu.mapper.UserMapper">

    <update id="updateExampleById">
        update user
        set
        <trim prefixOverrides=",">
            <if test="user.userName != null and user.userName != ''">
                user_name = #{user.userName}
            </if>
            <if test="user.age != null and user.age != 0">
                , age = #{user.age}
            </if>
        </trim>
        where id = #{user.id}
    </update>


    <select id="queryAll" resultType="com.atguigu.pojo.User">
        select *
        from user
    </select>
</mapper>
```





# 整合log4j2

对spirngboot的依赖进行修改，并且加入log4j2依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    
    <!-- 去掉springboot默认配置 -->
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
    </exclusions>
    
</dependency>

<!-- 引入log4j2依赖 -->
<dependency> 
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
```

然后，我们在项目的resources目录下，创建log4j2.xml文件：

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

这样，在SpringBoot中，就成功整合log4j2了。







# 整合MyBatis-Plus

我们只需要在SpringBoot中，引入MyBatis-Plus的依赖即可：

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.3.1</version>
</dependency>
```

然后，我们在使用MyBatis-Plus的分页插件时，需要先去配置分页插件：

在config的包下，创建一个配置类，比如叫做MPConfiguration，在该配置类中，需要去获取MyBatisPlusInterceptor的对象，并将该对象放入到IOC容器中：

```java
@Configuration
public class MPConfiguration {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

这样一来，分页插件就已经配置完毕了，我们就可以在项目中，使用：

```java
IPage<T> page = new Page<>(pageNum, pageSize);
```

的方式创建IPage对象，并使用该分页插件了。
