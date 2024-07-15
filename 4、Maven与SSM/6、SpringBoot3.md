[TOC]

**课程使用的SpringBoot版本：3.0.5**



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









---

# 一、SpringBoot3 介绍

## 1、简介

到目前为止，已经学习了多种配置Spring程序的方式，但是无论使用XML、注解、Java配置类还是它们混合使用，都会觉得配置文件过于复杂和繁琐。

SpringBoot的作用就是帮我们简单、快速地建立一个独立的、生产级别的Spring应用（说明：SpringBoot底层是Spring），大多数SpringBoot应用只需要编写少量配置即可快速整合Spring平台及第三方技术。

**SpringBoot的只要目标是：**

* 为所有Spring开发提供更快速、更广泛访问的入门体验。
* 开箱即用，设置合理的默认值，但是也可以根据需求进行适当调整。
* 提供一系列大型项目距通用的非功能性程序（如嵌入式服务器、安全性、指标、运行检查等）
* **约定大于配置**，基本不需要主动编写配置类、也不需要XML配置文件。

**`总结：简化开发、简化配置、简化整合、简化部署、简化监控、简化运维。`**



## 2、系统要求

本课程所使用到的SpringBoot版本是3.0.5，SpringBoot3要求技术或工具的版本如下所示：

| 技术&工具 | 版本（or later） |
| --------- | ---------------- |
| maven     | 3.6.3+           |
| Tomcat    | 10.0+            |
| Servlet   | 9.0+             |
| JDK       | 17+              |







## 3、快速入门（:star:）

需求：浏览器发送/hello请求，返回："Hello SpringBoot3!"。

**步骤说明：**

1. 创建Maven工程
2. 添加依赖（springboot父工程依赖，web启动器依赖）
3. 创建启动引导类（springboot项目运行的入口）
4. 编写处理器Controller
5. 启动项目

**实现步骤：**

1. **创建maven项目**

创建一个maven项目，就叫做springboot项目了。

然后给该项目指定自己的maven仓库：

<img src=".\images\image-20240612094602751.png" alt="image-20240612094602751" style="zoom: 40%;" /> 



2. **添加依赖**

在maven项目的pom.xml，声明parent标签，指定当前工程的父工程。

**只要使用springboot工程作为父工程，那么当前工程就是一个springboot工程了。**

使用spring-boot-starter-parent的3.0.5版本jar包作为当前项目的父工程：

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.0.5</version>
    <relativePath />
</parent>
```

然后，我们需要去引入springboot的web启动器依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

注意，我们不需要指定依赖的版本，因为在父工程中已经将依赖的版本声明好了。

引入了依赖后，我们就可以使用springboot的方式，启动web项目，不需要我们自己去创建IOC容器，也不再需要将工程转变为web工程，使用Tomcat的方式启动，springboot中嵌入了Tomcat，我们只需要启动springboot的启动类，即可启动Web工程。



4. **创建启动类**

在包下随便创建一个类，都可以作为启动类

比如，我在com.atguigu包下，创建了一个Main类作为启动类：

```java
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        //固定格式，它会去自动创建IOC容器，启动Tomcat服务器
        //只要在启动类相同的包或子包下，使用IOC容器的注解创建Bean对象
        //就会自动扫描并加入到IOC容器中
        SpringApplication.run(Main.class, args);
    }
}
```

启动类中的格式是固定的，使用`@SpringBootApplication`注解修饰，在类的main方法中，使用`SpringApplication.run(类.class, main方法参数);`的方式启动。

那么，此时会去自动扫描该类所属包及其子包，将使用了Bean注解的类加入到IOC容器，就不需要我们自己手动指定包进行扫描。



5. **编写处理器controller实现**

在启动类所属包及其子包下，使用@Controller注解修饰的controller，会被自动扫描加入到IOC容器中。

所以，我在com.atguigu.controller包下，创建一个Controller：

```java
@Controller
public class SpringBootTestController {
    @RequestMapping("/hello")
    @ResponseBody
    public String springBootTest(){
        return "hello springboot3!";
    }
}
```

此时，该类就会被加入到IOC容器中。



6. **测试**

直接启动Main类：

![image-20240612095928130](.\images\image-20240612095928130.png) 

此时会启动SpringBoot内嵌的Tomcat，默认设定的项目上下文路径为：/。

那么我们使用浏览器访问http://localhost:8080/hello结果为：

<img src=".\images\image-20240612100049528.png" alt="image-20240612100049528" style="zoom:80%;" /> 





## 4、入门总结

### 问题1：为什么依赖不需要写版本？

我们所引入的父工程spring-boot-starter-parent：

<img src=".\images\image-20240612100528808.png" alt="image-20240612100528808" style="zoom: 67%;" />

它有一个父工程：spring-boot-dependencies：

<img src=".\images\image-20240612100618639.png" alt="image-20240612100618639" style="zoom:67%;" /> 

在这个工程里，将我们可能所需的依赖的版本全部定义好了。

这样一来，我们在使用springboot工程时，只需要引入依赖的信息，而不需要引入依赖的版本。就比如我们引入的spring-boot-starter-web依赖：

<img src=".\images\image-20240612100801055.png" alt="image-20240612100801055" style="zoom:67%;" /> 

这样做的好处是：**不会出现依赖版本冲突问题。**

而若遇到仅引入依赖信息，但是不引入依赖版本出现错误的情况，说明在springboot父工程中没有设定依赖的版本，此时再引入依赖版本即可。

假如我们要引入mysql数据库，我们可以看看在spring-boot-dependencies中所设定的mysql数据库版本：8.0.32

![image-20240612101037274](.\images\image-20240612101037274.png) 

那我们直接引入mysql依赖的信息即可，无需引入mysql的版本，这样不会造成依赖冲突问题：

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>${mysql.version}</version>
</dependency>
```



### 问题2：启动器（Starter）是什么？

也就是问：我们所引入的spring-boot-starter-web依赖是什么

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

原本，我们去启动web项目时，对于web层来说，至少需要引入spring-webmvc、servlet以及jackson等依赖。

但是S**pringBoot给我们提供了一种叫做Starter的概念，它是一种预定义的依赖项集合，旨在简化Spring应用程序的配置和构建过程。**

也就是说，**`Starter是一组相关的依赖与配置，`以便在启动应用程序时自动引入所需的库、配置和功能**

对于web项目来说，starter引入了关于spring-webmvc、serlvet、jackson等依赖，还引入了Tomcat服务器，以及服务器的配置等数据。所以，我们只需要引入一个web应用的Starter就能够帮助我们完成引入web依赖、配置Tomcat服务器等功能。

可以看看这个spring-boot-starter-web的依赖：

<img src=".\images\image-20240612102347961.png" alt="image-20240612102347961" style="zoom:80%;" /> 

可以看到，spring-boot-starter-web包括json依赖、tomcat依赖、spring-webmvc依赖等等。也就是说，web的starter包含web项目所需的所有依赖。

**主要作用如下：**

* 简化依赖管理：Spring Boot Starter通过捆绑和管理一组相关的依赖项，减少了手动解析和配置依赖项的工作。只需引入一个相关的Starter依赖，即可获取应用程序所需的全部依赖。
* 自动配置：Spring Boot Starter在应用程序启动时自动配置所需的组件和功能。通过根据类路径和其他设置的自动检测，Starter可以自动配置Spring Bean、数据源、消息传递等常见组件，从而使应用程序的配置变得简单和维护成本减低。

* 提供约定优于配置：SpringBoot Startert遵循"约定优于配置"的原则，通过提供一组默认设置和约定，减少了手动配置的需要。它定义了标准的配置文件命名约定、默认属性值、日志配置等，使得开发者可以更专注于业务逻辑而不是繁琐的配置细节。

* 快速启动和开发应用程序：Spring Boot Starter使得从零开始构建一个完整的Spring Boot应用程序变得容易。它提供了主要领域（如Web开发、数据访问、安全性、消息传递等）的Starter，帮助开发者快速构建一个具备特定功能的应用程序原型。

* 模块化和可扩展性：Spring Boot Starter的组织结构使得应用程序的不同模块可以进行分离和解耦。每个模块都有自己的Starter和依赖项，使得应用程序的不同部分可以按需进行开发和扩展。

  ![img](.\images\imag12312312e.png)

SpringBoot提供了许多预定义的Starter，例如spring-boot-starter-web用于构建Web应用程序，spring-boot-starter-data-jpa用于使用JPA进行数据库访问，spring-boot-starter-security用于安全认证和授权等等。

使用Starter非常简单，只需要在项目的构建文件（例如Maven的pom.xml）中添加所需的Starter依赖，SpringBoot会自动处理依赖管理和配置。

通过使用Starter，开发人员可以方便地引入和配置应用程序所需的功能，避免了手动添加大量的依赖项和编写冗长的配置文件的繁琐过程。同时，Starter也提供了一致的依赖项版本管理，确保依赖项之间的兼容性和稳定性。

Spring boot提供的全部启动器地址：

[Build Systems :: Spring Boot](https://docs.spring.io/spring-boot/reference/using/build-systems.html#using.build-systems.starters)



### 问题3：@SpringBootApplication注解的作用？

这个注解是一个整合注解，它有三个子注解实现：

```java
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
public @interface SpringBootApplication {}
```

@SpringBootApplication注解是Spring Boot框架中的核心注解，它的主要作用是简化和加速Spring Boot应用程序的配置和启动过程。

具体而言，@SpringBootApplication注解启动以下几个作用：

1. **声明配置类**：@SpringBootConfiguration表明`该类本身是一个配置类，也就是说假如我们要往IOC容器中添加一个对象，就可以在该类中使用@Bean的方式，创建一个方法，将方法的返回值加入到IOC容器`。

   配置类可以包含Spring框架相关的配置、Bean定义，以及其他的自定义配置。通过@SpringBootApplication注解，开发者可以将配置类与启动类合并在一起，使得配置和启动可以同时发生。

2. **组件扫描**：@SpringBootApplication注解包含了@ComponentScan注解，用于自动扫描并加载应用程序中的组件。它会`默认扫描@SpringBootApplication注解修饰的启动类所在的包及其子包中的组件，并将它们存入IOC容器中以供使用。`

3. **自动加载其他配置类**：@SpringBootApplication注解中包含了@EnableAutoConfiguration注解，该注解用于启动SpringBoot的自动配置机制，会去自动地配置Spring的功能，减少开发者的手动配置工作。

总的来说，@SpringBootApplication注解的主要作用是简化Spring Boot应用程序的配置和启动过程。它自动配置应用程序、扫描并加载组件，并将配置和启动类合二为一，简化了开发者的工作量，提高了开发效率。



对于启动main方法来说，该方法格式是**固定**的，就是：

```java
public static void main(String[] args) {
    SpringApplication.run(启动类.class, args);
}
```

它的作用是：

1. 创建IOC容器，加载配置
2. 启动内置的web服务器









---

# 二、SpringBoot3 配置文件

## 1、统一配置管理概述

**配置文件说明**

SpringBoot工程下，进行统一的配置管理，想设置的任何参数（包括端口号、项目根路径、数据库连接信息等）都集中放在一个固定位置和名称的配置文件中。（`application.properties` 或 `application.yml`）

而不是像之前SSM一样，一种配置放在一个配置文件，**SpringBoot将所有的配置都放在一个文件中**。

配置文件应该放置在Spring Boot工程的**`src/main/resources`**目录下。这时因为src/main/resources目录是Spring Boot默认的类路径（classpath），配置文件会被自动加载并可供应用程序访问。

![img](.\images\imag12121e.png)

我们来看看spring-boot-starter-parent这个SpringBoot父工程中的配置信息：

<img src=".\images\image-20240612141631155.png" alt="image-20240612141631155" style="zoom: 80%;" /> 

上图信息，表示的含义是，SpringBoot的配置文件，是放在src/main/resources目录下的以application名字开头的yml、yaml、properties文件。

也就是说，满足：

1. `文件放在src/main/resources目录下`
2. `文件名以application名称开头的`
3. `文件类型是yml、yaml或properties`

三个条件的文件，会被自动认为是SpringBoot的配置文件，其中的配置会被自动扫描。







**配置文件参数说明**

在SpringBoot中，它提供了一些固定的key参数，我们在配置参数时，就需要使用这些固定的key来进行配置。

比如，数据库连接的用户名来说，它固定使用spring.datasource.username这个参数来配置，那么我们在springboot的配置文件中，要想去设置登录数据库用户名，就要使用这个固定的参数配置。

在springboot的配置文件中，我们还可以自定义参数，比如设置user.name=cjw，那么，我们在实际使用时，就可以使用`@Value(${key})`的方式给属性赋上参数值使用。

配置文件参数说明网站：

[Common Application Properties :: Spring Boot](https://docs.spring.io/spring-boot/appendix/application-properties/index.html#appendix.application-properties)

在这个网站中，就说明了如何在SpringBoot配置文件中，使用固定的参数进行配置。

就比如对于数据库的连接信息配置，若使用properties的配置文件方式配置：

![image-20240612141224067](.\images\image-20240612141224067.png)

若是使用yaml格式的配置方式进行配置：

![image-20240612141251254](.\images\image-20240612141251254.png)



**注意**：要想在SpringBoot配置文件中修改项目参数的配置，**`参数名是固定的，不可变更`**。

比如，我想去修改服务器的端口号，就必须使用server.port进行修改，使用其他参数无法奏效。

我们也可以在SpringBoot配置文件中，自己去定义一些参数，在项目中通过使用`@Value(${key})`的方式，注入这些参数以供使用。





> **总结：**
>
> * 集中式管理配置。统一在一个文件完成程序功能参数设置和自定义参数声明。
> * 位置：resources文件夹下，必须命名application后缀，必须是.properties 或 .yaml 或 .yml文件。
> * 如果同时存在application.properties 与 application.yml(yaml)文件，则properties文件的优先级更高。
> * 因为约定大于配置，配置都有默认值。







---

## 2、properties配置文件的使用

**系统参数的设置**

在项目的resources目录下，创建`application.properties`文件：

![image-20240612144450667](.\images\image-20240612144450667.png) 

在resources目录下，创建application*.properties文件，会被SpringBoot自动扫描成为SpringBoot项目的配置文件。所有的配置信息，均可放在一个配置文件中，包括数据库连接信息、mybatis配置、SpringMVC配置等等，均可放在SpringBoot的配置文件中。

使用SpringBoot配置文件，其中的项目参数是固定的，我们若想去修改项目参数，必须指定对应的参数名进行修改。具体的参数名请参考：

[Common Application Properties :: Spring Boot](https://docs.spring.io/spring-boot/appendix/application-properties/index.html#appendix.application-properties)

**修改项目参数案例：**

比如，我使用properties格式去修改服务器的端口号和项目的上下文路径：

```properties
#使用springboot提供的配置，修改程序的参数，key是固定的
server.port=80
server.servlet.context-path=/ssm
```

此时，去启动SpringBoot项目，那么此时，Tomcat的端口号就变成了80，项目的上下文路径也就变成了/springboot，那么访问/hello路径的handler方法：

<img src=".\images\image-20240612142627439.png" alt="image-20240612142627439" style="zoom: 80%;" /> 





**自定义参数案例：**

在resources目录下，创建application.properties文件，在该文件中定义两条参数：

```properties
#自定义key
login_user.name=cjw
login_user.age=24
```

然后，在代码中，使用`@Value`注解，使用这两个参数。

比如，在con.atguigu.controller包下，创建一个TestController：

```java
@Controller
public class TestController {

    @Value("${login_user.name}")
    String username;

    @Value("${login_user.age}")
    Integer age;

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        System.out.println("用户名：" + username + "，年龄：" + age);
        return "hello springboot!";
    }
}
```

然后，在com.atguigu包下，创建一个Main方法，用于启动SpringBoot项目：

```java
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
```

此时，com.atguigu.controller包下的组件都会被自动扫描加入到IOC容器中。

启动项目，浏览器访问http://localhost:8080/hello，控制台打印结果：

![image-20240612144325826](.\images\image-20240612144325826.png) 

此时，就将自定义的参数注入到了项目代码中，以供使用。







---

## 3、YAML（yml）配置文件的使用（推荐）

在application的配置文件中，我们要去为所有的框架参数进行配置，那么这些参数名就不能重复，不然不知道为哪一个框架的参数配置。

如何保证参数不能重复呢？

采用的是**`多层命名方式`**。

比如，对于服务器的上下文路径参数，参数名是server.servlet.context-path，服务器的端口号：server.port。

如果使用properties格式的文件，那么对于每一个参数来说，我们不仅需要去声明参数名，还需要将参数名前面的每一个参数都进行声明，这样就比较麻烦。

所以，我们推荐使用`yaml`格式的文件。

yaml格式文件有两种后缀名，一种是.yaml，一种是yml，这两种都可以。

**使用yaml格式有什么好处？**

比如，对于服务器上下文路径参数与服务器端口号参数的设置，使用properties文件：

```properties
server.port=80
server.servlet.context-path=/boot
```

若使用yaml格式文件：

```yaml
server:
  port: 80
  servlet:
    context-path: /boot
```

这样不仅看起来比较清晰，代码也比较少，减少了冗余的配置。



1. **yaml格式介绍**

YAML是一种基于层次结构的数据序列化结构，旨在提供一种易读、人类友好的数据表示方式。

与`.properties`文件相比，YAML格式有以下优势：

* 层次结构：YAML文件使用缩进和冒号来表示层次结构，使得数据之间的关系更加清晰和直观。这样可以更容易理解和维护复杂的配置，特别适用于深层次嵌套的配置情况。
* 自我描述性：YAML文件具有自我描述性，字段和值之间使用冒号分隔，并使用缩进表示层次关系。这使得配置文件更易于阅读和理解，并且可以减少冗余的标点符号和引号。
* 注释支持：YAML格式支持注释，可以在配置文件中添加说明性注释，使配置更具可读性和可维护性。相比之下，`.properties`文件不支持注释，无法提供类似的解释和说明。
* 多行文本：YAML格式支持多行文本的表示，可以更方便地表示长文本或数据块。相比之下，`.properties`文件需要使用转义符或将长文本拆分成多行。
* 类型支持：YAML格式天然支持复杂的数据格式，如列表、映射等。这使得在配置文件中表示嵌套结构或数据集合更加容易，而不需要进行额外的解析或转换。
* 更好的可读性：由于YAML格式的特点，它更容易被人类读懂和解释。它减少了配置文件中需要的特殊字符和语法，让配置更加清晰明了，从而减少了错误和歧义。

综上所述，YAML格式相对于.properties文件具有更好的层次结构表示、自我描述性、注释支持、多行文本表示、复杂数据类型支持和更好的可读性。这些特点使YAML成为一种有力的配置文件格式，尤其适用于复杂的配置需求和人类可读的场景。然而，选择使用YAML还是.properties取决于实际需求和团队偏好。



2. **yaml语法说明**

> * 数据结构使用树形结构呈现，通过缩进来表示层级；
> * 连续的项目（集合）通过减号"-"来表示；
> * 键值结构里面的key/value对用冒号":"来分隔；
> * YAML配置文件的扩展名是yaml或yml。



**注意**：yaml结构中，key与value之间使用冒号":"分隔，并且value与冒号":"之间有一个**`空格`**。如：age: 10，要加一个空格再写值。

并且若参数是一个集合类型，则"-"后面也有一个**空格**，再跟上参数值。

例如：

```yaml
app_name: 我的应用程序
version: 1.0.0
author: cjw
hobbies:
  - 骑自行车
  - 打羽毛球
  - 动漫
  - 游戏


spring:
  datasource:
    username: root
    password: 061535asd
    url: jdbc:mysql://localhost:3306/springboot
```



3. **读取配置文件中的自定义参数**

读取方式与properties一致，都是使用`@Value(${key})`的方式引入。

案例：

在`application.yaml`中定义参数：

```yaml
jwt:
  username: cjw
  age: 24
  email: 1403642083@qq.com
  hobbies:
    - game
    - play
```

创建一个pojo类，引入这些参数：

```java
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class CjwUser {
 
    private String username;

    private Integer age;

    private String email;

    private List<String> hobbies;

}
```

@ConfigurationProperties(prefix = "jwt")目的是为了引入配置文件中，以jwt下的参数，会将jwt下的参数与类中的属性名相同的属性进行匹配，一般用于引入批量数据与集合类型的参数。

原本在yaml文件中，我们自定义的参数会有黄线标注，但是在该参数有地方注入成功后，原本yaml文件中参数的黄线也会消失。我们**可以根据配置文件中自定义参数是否有黄线来判断参数是否注入成功**。

然后，在controller类中打印该对象信息：

```java
@Controller
public class UserController {
    @Autowired
    private CjwUser user;

    @RequestMapping("/user")
    @ResponseBody
    public CjwUser yamlTest(){
        System.out.println(user);
        return user;
    }
}
```

此时，访问/user路径，控制台打印结果：

![image-20240612163421589](.\images\image-20240612163421589.png) 

其他具体引入yaml文件中参数的方式请查看：

[Spring Boot 优雅配置yml配置文件定义集合、数组和Map_yml map配置-CSDN博客](https://blog.csdn.net/zouliping123456/article/details/134080774)





**可能出现的问题：**

在使用yaml配置文件时，可能在启动项目后会出现：

![image-20240612163638793](.\images\image-20240612163638793.png)

报错。

原因是在yaml文件中，使用了中文，并且yaml的编码格式与Java程序中所使用的编码格式不一致。

解决方法：将项目中的所有编码格式都改成使用UTF-8的形式

<img src=".\images\image-20240612163756739.png" alt="image-20240612163756739" style="zoom:67%;" />

然后要去使用clean插件清除编译后的项目，然后再去启动SpringBoot。

此时项目就能够正常启动了。





---

## 4、获取yml配置文件中的参数

#### 方式一：使用@Value注解获取

我们可以使用`@Value`注解的方式，**引入yml配置文件中所定义的`基本数据类型`与`数组类型的参数`（数组也是基本数据类型）**

使用@Value方式注入IOC容器中的Bean对象作为类中的属性，那么当前类也要在IOC容器中，所以需要使用`@Component`将当前类使用IOC容器创建对象。



**案例：**

yml配置文件：

```yaml
user:
  parameters:
    appId: ChengJiaWei
    password: 061535asd
    includes: api/v1,api/v2
```

其中，includes是一个数组类型的数据，数组中每个元素之间使用,逗号分隔，那么上例中的includes数组中有两个元素。

参数引入：

```java
@Component
@Data
public class ConfigBean {
    @Value("${user.parameters.appId}")
    private String appId;

    @Value("${user.parameters.password}")
    private String password;

    @Value("${user.parameters.includes}")
    private String[] includes;
}
```

测试：

```java
@Controller
public class TestController {
    @Autowired
    private ConfigBean configBean;

    @RequestMapping("/test")
    @ResponseBody
    public ConfigBean testValue(){
        System.out.println(configBean);
        return configBean;
    }
}
```

打印结果：

![image-20240612173226187](.\images\image-20240612173226187.png) 



但是使用这种方式有两个缺点：

1. 每次使用@Value注解都需要在其中使用`${参数名}`来指定对应的参数。
2. @Value注解只能读取单个值，**集合类型的数据无法读取。**









---

#### 方式二：使用@ConfigurationProperties注解获取（推荐）

对于@Value注解的缺点，可以使用@ConfigurationProperties注解来代替。

**`@ConfigurationProperties`**注解是SpringBoot提供的重要注解，它可以将一些配置属性批量注入到Bean对象中。在类上通过@ConfigurationProperties注解声明该类读取属性配置，其中的prefix属性读取文件中指定前缀的参数值。前缀和属性名称与配置文件中的中的key保持一致，才可以注入成功。



**案例1：注入基本数据类型参数**

yaml配置文件：

```yaml
user:
  parameters:
    appId: ChengJiaWei
    password: 061535asd
    includes: api/v1,api/v2
```

引入参数：

```java
@Component
@Data
@ConfigurationProperties(prefix = "user.parameters")
public class ConfigBean {
    private String appId;

    private String password;

    private String[] includes;
}
```

使用@ComfigurationProperties注解，引入以user.parameters为前缀的参数，引入时会将类中的属性名与参数名key进行匹配映射，成功匹配的属性会被赋上值，然后将类作为IOC中的对象进行创建。

测试：

```java
@Controller
public class TestController {
    @Autowired
    private ConfigBean configBean;

    @RequestMapping("/test")
    @ResponseBody
    public ConfigBean testValue(){
        System.out.println(configBean);
        return configBean;
    }
}
```

访问/test路径，控制台打印：

![image-20240612174408178](.\images\image-20240612174408178.png) 







**案例2：注入List集合类型参数**

创建一个User类：

```java
@Data
public class User {
    private String name;
    private Integer age;
}
```

**需求**：在yaml配置文件中配置User类型的集合参数，然后在ConfigBean中，引入该参数。

yaml配置文件写法如下：

```yaml
user:
  parameters:
    appId: ChengJiaWei
    password: 061535asd
    includes: api/v1,api/v2
    users:
      - name: tom
        age: 22
      - name: jerry
        age: 18
```

在ConfigBean类中，使用@ConfigurationProperties注解获取对象集合值：

```java
@Component
@Data
@ConfigurationProperties(prefix = "user.parameters")
public class ConfigBean {
    private String appId;

    private String password;

    private String[] includes;

    private List<User> users;
}
```



测试：

```java
@Controller
public class TestController {
    @Autowired
    private ConfigBean configBean;

    @RequestMapping("/test")
    @ResponseBody
    public ConfigBean testValue(){
        System.out.println(configBean);
        return configBean;
    }
}
```

访问/test路径后，页面的结果：

![image-20240612174815006](.\images\image-20240612174815006.png) 







**案例3：注入Map集合类型参数**

对于User类型：

```java
@Data
public class User {
    private String name;
    private Integer age;
}
```

对于User对象，比如user{name="tom", age=18}来说，我们可以转换成使用Map<String, String>类型，即此时key1="name"，value1="tom"；key2="age"，value2="18"。

同理，我们也可以在yaml文件中，存放map集合类型的参数。

**yaml格式1：**

```yaml
maps: {name: tom,age: 18}
```

**yaml格式2：**

```yaml
maps:
	name: tom
	age: 18
```

注意与List集合的区别，使用Map集合格式的参数，每个元素前面没有减号"-"。

如，yaml文件：

```yaml
user:
  parameters:
    appId: ChengJiaWei
    password: 061535asd
    includes: api/v1,api/v2
    users:
      - name: tom
        age: 22
      - name: jerry
        age: 18
    userMessage:
      name: lucy
      age: 24
      email: 1403642083@qq.com
```

在配置类Bean中，使用@ConfigurationProperties注解获取：

```java
@Component
@Data
@ConfigurationProperties(prefix = "user.parameters")
public class ConfigBean {

    private String appId;

    private String password;

    private String[] includes;

    private List<User> users;

    private Map<String, String> userMessage;
}
```

测试：

```java
@Controller
public class TestController {
    @Autowired
    private ConfigBean configBean;

    @RequestMapping("/test")
    @ResponseBody
    public ConfigBean testValue(){
        System.out.println(configBean);
        return configBean;
    }
}
```

访问/test，页面访问结果：

<img src=".\images\image-20240612175752945.png" alt="image-20240612175752945" style="zoom:80%;" /> 





使用@ConfigurationProperties方式引入配置文件中的参数有**两个优势**：

1. 方便，不需要一个一个地进行设置读取。
2. **可以给集合类型的参数赋值**。







---

## 5、多环境配置和使用

**需求：**我们在实际开发应用中，可能会使用到多个环境，比如开发的时候使用开发环境，测试的时候使用测试的环境，生产的时候使用生产的环境。主要是可能存在数据库不同，服务器地址不同等等。这个时候，application中的配置信息就会不同。如果我们每到一个不同的环境，就去修改application中的配置，这样就会有点麻烦。所以，我们就想去配置多个环境，在不同时期使用不同的环境，只需要更改一点点配置就可以切换。

在SpringBoot中，可以使用多环境配置配置来根据不同的运行环境（如开发、测试、生产）加载不同的配置。SpringBoot支持环境配置让应用程序在不同的环境中使用不同的配置参数，例如数据库连接信息、日志级别、缓存配置等。

**多环境配置的方法：**

将配置参数分离到不同的YAML文件中，每个环境对应一个文件。例如，可以创建`application-dev.yml`、`application-prod.yml`和`application-test.yml`等文件。在这些文件中，可以使用YAML语法定义各自环境的配置参数。



> **通过在application.yml文件中，使用`spring.profiles.active`参数指定当前所使用的环境**

例如，在项目中创建了开发环境application-dev.yml：

<img src=".\images\image-20240612194704556.png" alt="image-20240612194704556" style="zoom:80%;" /> 

创建了测试环境application-test.yml：

<img src=".\images\image-20240612194714208.png" alt="image-20240612194714208" style="zoom:80%;" /> 

创建了生产环境application-prod.yml：

<img src=".\images\image-20240612194725125.png" alt="image-20240612194725125" style="zoom:80%;" /> 

我们就可以就可以在application.yml中，使用：

```yaml
spring:
	profiles:
		active: dev
```

的方式指定开发环境，同理也可以将参数值修改成test或prod指定其他环境。







**引入多个配置文件**

如果在项目中只有一种环境，那么此时就不需要使用该参数来切换环境。但是，只有一种环境时，所有的参数全部都放在application.yml文件中，这个时候该文件中的参数就会很多。

此时，我们可以依据参数类型的不同，创建多个配置文件，比如application-mybatis.yml文件就是专门用来配置mybatis的，application-druid.yml文件就是专门用来配置druid数据库连接池的。

这个时候，我们也可以**使用`spring.profiles.active`参数用于引入参数**：

```yaml
spring:
	profiles:
		active: mybatis,druid
```

这表示引入`application-mybatis.yml`与`application-druid.yml`配置文件中的配置。

此时，我们就可以把不同类型的配置分散在不同的配置文件中，使用参数引入，这样就可以减少application.yml中参数的数量，使配置更加清晰。



> **使用总结：**
>
> SpringBoot项目可以配置多个yml配置文件，配置多个配置文件有两种方案：
>
> 1. **根据不同环境进行多环境参数配置**。项目根据不同的环境进行多个文件配置，开发环境使用一种配置文件，测试环境使用一种配置文件等等。在application.yml中，仅配置多环境共同值的参数，在其他配置文件中，如application-dev.yml中，配置开发环境与其他环境不同的参数（如数据库参数、服务器参数等），然后在application.yml中，通过`spring.profiles.active`引入，即可使用该文件中所配置的参数。
> 2. **根据不同类型进行多类型参数配置。**若项目中只有一个环境，那么不需要配置多个环境，仅需要一个application.yml即可。但是此时，在application.yml中配置所有的参数，参数数量比较多，查看比较麻烦。所以此时我们可以根据参数的类型来进行配置，将不同类型的参数放在不同的yml文件中，比如mybatis的参数放在application-mybatis.yml文件中。那么，我们就可以在application.yml中，通过`spring.profiles.active`引入多个yml文件，从而引入这些不同类型的参数。
>
> 这些配置文件必须以**`application-`**开头。
>
> 在实际的开发中，一般都是使用方案一，即根据不同环境进行多环境参数配置。



**注意：**

1. 这些不同环境的配置文件，必须以**`application-`**开头，然后再接上不同的名称标识不同的参数key。
2. **如果在引入的配置文件中，有和application.yml重叠的属性，则`以引入文件中的设置优先`。**



案例：

application.yml文件

```yaml
spring:
  profiles:
    active: dev

user:
  parameters:
    appId: Tom
    password: 061535asd
```

在该配置中，引入了application-dev.yml文件。

application-dev.yml文件

```yaml
user:
  parameters:
    appId: Jerry
    password: 123456
```

此时，我们在类中引入这些参数：

```java
@Component
@Data
@ConfigurationProperties(prefix = "user.parameters")
public class ConfigBean {
    private String appId;

    private String password;
}
```

此时，在该类中，引入的appId和password属性是application-dev.yml中所设置的参数值。

测试：

```java
@Controller
public class TestController {
    @Autowired
    private ConfigBean configBean;

    @RequestMapping("/test")
    @ResponseBody
    public ConfigBean testValue(){
        System.out.println(configBean);
        return configBean;
    }
}
```

执行结果：

<img src=".\images\image-20240612201808857.png" alt="image-20240612201808857" style="zoom:80%;" /> 





---

# 三、整合SpringMVC

> **对于Java的Web层来说，我们需要配置的内容包括：**
>
> 1. `controller组件扫描的路径`
> 2. `HandlerMapping、HandlerAdapter组件的创建`
> 3. `JSON类型的转换器配置`
> 4. `视图解析器的配置`
> 5. `开启静态资源处理`
> 6. `拦截器配置`
> 7. `DispatcherServlet接收的路径设置`

在SpringBoot中，对于第1、2、3、7点，SpringBoot都会给我们进行默认的配置。

第一点：会去自动扫描启动类所属包及其子包下所有的组件，所以我们只需要将controller包声明在启动类所属包下。

第二点、第三点：在我们引入web开发的启动器时，就会自动为我们在环境中创建HanderMapping、HandlerAdapter组件，并且在启动器的依赖中，包含了jackson依赖，会自动取HandlerAdapter组件中配置JSON格式的转换器。

第四点：SpringBoot默认接收：/，即所有请求信息。

那么，我们可能需要进行配置的内容是：视图解析器的前后缀、开启静态资源处理以及拦截器的配置。视图解析器在application.yml中进行配置，静态资源处理SpringBoot有默认的配置，拦截器配置类似于SpringMVC中。



## 1、实现过程

整合SpringMVC的实现过程，已经在《快速入门》章节中讲解过了，这里我就再写一遍。

1. **创建程序**

创建一个子项目，就取名springboot-springmvc-part03。

![image-20240612204302065](.\images\image-20240612204302065.png) 

2. **引入依赖**

若要使用springboot项目，则需要将spring-boot-starter-parent作为父级项目。

因为该子项目的父项目springboot中已经将spring-boot-starter-parent作为父项===========目了，所以就不需要在当前子项目中再去设置。

如果在父项目中没有引入springboot，则需要引入，引入方式：

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.0.5</version>
    <relativePath />
</parent>
```

对于SpringMVC来说，我们还需要引入springboot中的web项目启动器依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

该依赖包含了web项目所需的spring-webmvc依赖、jackson依赖以及Servlet依赖等，同时还包含了配置信息以及程序，包括Tomcat服务器及其配置信息等。





3. **创建启动类**

创建一个包，如com.atguigu包，在该包下，创建一个启动类，比如SpringWebApplication。

使用@SpringBootApplication修饰该类，则该类就是一个配置类，同时也是一个启动类，该类所属的包及其子包都会去自动扫描其中的Spring组件，加入到IOC容器中。

然后，在该类中创建一个main方法，使用SpringApplication.run()方法启动容器。启动的格式是固定的。

```java
@SpringBootApplication
public class SpringWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringWebApplication.class, args);
    }
}
```





4. **编写Controller**

```java
@Controller
public class TestController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello springboot!";
    }
}
```





---

## 2、web相关配置

与web相关的配置，一般都是以server这个key为开头的。

位置：`application.yml`

案例：配置服务器的端口号为80，项目的上下文路径为/boot

```yaml
# web相关的配置
# https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#appendix.application-properties.server
server:
  # 端口号设置
  port: 80
  # 项目根路径
  servlet:
    context-path: /boot
```

当涉及Spring Boot的Web应用程序配置时，以下是五个重要的配置参数：

* **`server.port`**：指定应用程序的HTTP服务器端口号。默认情况下，Spring Boot使用8080作为默认端口号。
* **`server.servlet.context-path`**：设置应用程序的上下文路径。这是应用程序在URL中的基本路径。默认情况下，上下文路径为空，即/。
* **`spring.mvc.view.prefix`**和**`spring.mvc.view.suffix`**：这两个属性用于配置视图解析器的前缀和后缀。视图解析器用于解析控制器返回的视图名称，并将其映射到实际的视图页面。spring.mvc.view.prefix定义视图的前缀，spring.mvc.view.suffix定义视图的后缀。
* **`spring.resources.static-locations`**：配置静态资源的位置。静态资源可以是CSS、JavaScript、图片等。在默认情况下，SpringBoot会将静态资源放在`classpath:/static`目录下。可以通过在配置文件中设置spring.resources.static-locations属性来自定义静态资源的位置。
* **`spring.http.encoding.charset`**和**`spring.http.encoding.enabled`**：这两个属性用于配置Http请求和响应的字符编码。sprig.http.encoding.charset定义字符编码的名称（例如UTF-8），spring.http.encoding.enabled用于启动或禁用字符编码的自动配置。

这些是在SpringBoot的配置文件中与Web应用程序的一些重要配置参数。根据需要，可以在配置文件中设置这些参数来定制和配置Web应用程序。







---

## 3、静态资源处理

在WEB开发中，我们需要引入一些静态资源，例如：HTML，CSS，JS，图片等，如果是普通的项目静态资源可以放在项目的webapp下。现在使用SpringBoot开发，项目中没有webapp目录，并且我们的项目是一个jar工程，本身就没有webapp，那么我们的静态资源应放在哪呢？

> **默认静态资源路径为：**
>
> * **classpath:/META-INF/resources/**
> * **classpath:/resources/**
> * **`classpath:/static/`**
> * **classpath:/public/**

SpringBoot让我们将静态资源可以放在上述四种路径下，我们只要把静态资源放在这些目录中的任何一个，SpringMVC都会帮助我们处理。

我们一般将静态资源放在class:/static/目录下，因为没有webapp目录，也就是在**`resources/static/`目录中**。

例如，我们在resources目录下，创建一个static目录，在该目录下创建一个index.html文件：

![image-20240612222912360](.\images\image-20240612222912360.png) 

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
hello springboot!
</body>
</html>
```

然后去启动项目，我们就可以通过访问：

http://localhost:8080/index.html

的方式进行访问。

![image-20240612223016142](.\images\image-20240612223016142.png) 

也就是说：

> **`访问静态资源文件夹中的内容时，不需要在路径中写静态资源文件夹名称。`**

例如：

我们在resources目录下，创建了一个public目录，在public目录中又创建了一个html目录，在html目录下创建了一个index.html文件。

也就是说，静态文件的路径是：/resources/public/html/index.html：

![image-20240612223529432](.\images\image-20240612223529432.png) 

此时，若想去访问这个index.html文件的话，URL地址是：

http://localhost:8080/html/index.html

这个public目录，是SpringBoot中默认的静态资源目录，在路径中是不需要写的，所以我们只需要写静态资源目录中的目录与文件地址即可。

我们也可以在handler方法中，通过使用**请求转发**或者**响应重定向**的方式对静态资源进行访问，如：

```java
@Controller
public class TestController {

    @RequestMapping("/test")
    public String test(){
        return "forward:/html/index.html";
    }
}
```

此时，就可以通过/test去访问到/resources/public/html/index.html文件，因为public目录属于默认的静态资源目录，所以在路径中不需要填写。一般使用static目录作为默认的静态资源目录。



**静态资源目录的修改**

在application.yml文件中，使用**`spring.web.resources.static-locations`**参数，对默认的静态资源地址进行修改。

例如，将静态资源目录修改为resources/webapp：

```yaml
spring:
  web:
    resources:
      static-locations: classpath:/webapp
```

此时，默认的四种静态资源目录全部都会失效，此时默认的静态资源目录是resources/webapp/，只有在该目录下的才有效。

此时，我们在项目中创建静态资源信息：
![image-20240612224712126](.\images\image-20240612224712126.png)

static目录中的index.html:

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
static目录中的index.html
</body>
</html>
```

webapp目录中的index.html：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
webapp目录中的index.html
</body>
</html>
```

此时，我们去访问http://localhost:8080/index.html结果为：

![image-20240612224833236](.\images\image-20240612224833236.png) 

可以发现，此时去访问的就是webapp目录下的index.html

因为我们修改了静态资源的访问目录，当我们去访问静态资源时，就会默认在路径前面加上我们所指定的路径，寻找对应的静态资源。

由于在路径中，不需要写静态资源的路径，所以我们只需要访问index.html，就可以访问到resources/webapp/目录中的index.html。







---

## 4、自定义拦截器

在SpringBoot中自定义拦截器的方式与在SSM中配置方式是一样的，也是要去创建拦截器类，也是要去创建配置类。也就是说，在SpringBoot中，需要配置类的时候可以加，不需要时直接用即可。

原本，我们在SpringMVC中，是去创建一个拦截器类后，然后在配置类中采用new对象的方式，将拦截器类的对象加入到配置类中。在SpringBoot中，由于会去自动扫描启动类所属的包及其子包，不需要我们手动地指定扫描路径，所以我们在SpringBoot中，直接将拦截器作为组件放入到IOC容器中，直接将对象使用@Autowired注入到配置类中，就不用我们自己手动创建了。

**实现过程：**

1. **`创建拦截器类`**

在com.atguigu.interceptor包下，创建一个拦截器类，叫做MyInterceptor：

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

这里使用了@Component注解，将该类放入IOC容器中。





2. **`创建配置类，添加拦截器`**

在`com.atguigu.config`包下，创建一个配置类，叫做MvcConfig。

**注意**：该包必须在SpringBoot的扫描下，也就是需要在启动类所在包及其子包下创建，这样配置类才能生效。

使用`@Configuration`注解修饰该类，表示该类是一个配置类。

使用`@Autowired`注解，自动注入我们所创建的拦截器对象。

将该类去实现`WebMvcConfigurer`接口，该接口在SpringMVC中学习过，该接口中声明了给Web项目进行配置的方法，其中的`addInterceptors()`方法就是用来添加拦截器信息的。

重写`addIneterceptors()`方法，在该方法中使用形参对象的addInterceptor()添加拦截器对象。

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

此时就不需要使用`@EnableWebMvc`注解与`@ComponentScan`注解，因为这两个注解的功能，SpringBoot都默认实现。

这样一来，拦截器类的配置就完毕了。



**测试：**

```java
@Controller
public class TestController {
    @RequestMapping("/test")
    @ResponseBody
    public void test(){
        System.out.println("test方法正在执行！");
    }
}
```

访问/test，控制台打印结果：

![image-20240612232138689](.\images\image-20240612232138689.png) 







---

## 5、全局异常处理机制

对于全局异常处理机制来说，其实这里没有使用到SprinMVC配置类中的内容，只需要去扫描该全局异常处理类的包即可。

所以，我们只需要保证全局异常处理类的包，在启动类的包及其子包下即可。

如：

在启动类Main所属包com.atguigu下创建一个子包exception，在该包下创建一个GlobalExceptionHandler，使用该类作为全局异常处理类：

![image-20240612212052168](.\images\image-20240612212052168.png) 

该GlobalExceptionHandler使用@ControllerAdvice注解修饰，表明该类是一个全局异常处理类，并且会将该类作为组件加入到IOC容器中。

在该类中，声明异常处理的方法，使用@ExceptionHandler指明映射的异常，例如：

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public String handlerNullPointerException(NullPointerException e){
        return e.getMessage();
    }
}
```

当项目中发生异常时，就会到该类中来寻找是否有匹配的异常。

比如，发生了NullPointerException时，就会被该类中的handler方法handlerNullPointerException()所映射，从而来执行该方法，向前端响应message信息。

也就是说，在SpringBoot中，对于全局异常处理类的使用方式与SSM中一致。











---

# 四、整合Druid数据源

## 1、创建程序

创建一个项目springboot-druid-part04，用于整合Druid数据源。



## 2、导入依赖

**首先，我们需要让当前项目变成一个SpringBoot项目。**

在pom.xml中，添加parent标签，在parent标签中引入spring-boot-starter-parent的jar包：

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.0.5</version>
    <relativePath />
</parent>
```

在pom.xml中声明上述信息，就表示引入springboot，当前的项目就是一个springboot项目。

如果在当前工程的父工程中已经引入过了，就无需引入。

**之后，便是引入依赖了。**

我们要去启动这个项目，首先需要引入**web开发的启动器**：

```xml
<!--  web开发的场景启动器 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

其次，我们需要去使用JdbcTemplate来测试数据库是否连接成功，并且若要使用事务，就需要引入**数据库相关配置的启动器**：

```xml
<!-- 数据库相关配置启动器 jdbcTemplate 事务相关-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

这两个启动器都是不需要设置version版本的，启动器的版本默认使用引入的springboot版本。

之后，我们需要引入**druid数据库连接池启动器**的依赖以及**mysql的驱动**：

```xml
<!-- druid启动器的依赖  -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-3-starter</artifactId>
    <version>1.2.22</version>
</dependency>

<!-- mysql驱动-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>${mysql.version}</version>
</dependency>
```

最后，引入lombok这个工具jar包：

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>${lombok.version}</version>
</dependency>
```

对于mysql与lombok这两个依赖，我们使用了${}的方式引入了版本。因为在项目的父工程spring-boot-starter-parent的父工程spring-boot-dependencies中，对于一些常见的依赖指定了版本，依据继承的传递性，我们在使用时可以直接使用${}的方式使用其指定的版本，也可以直接去掉，使用的是父工程中`<dependencyManagement>`标签中设定的版本。

这样一来，就不会出现版本冲突问题。

这样，所有所需的依赖都准备好了。



## 3、配置文件编写

数据库的配置全部都在`spring.datasource`参数下。

首先，我们使用`spring.datasource.type`参数配置使用哪一种数据源信息，我们使用的是Druid数据库连接池来进行连接，所以参数配置为：

```yaml
spring:
  datasource:
    #使用druid连接池
    type: com.alibaba.druid.pool.DruidDataSource
```

然后，我们要去配置数据库连接等信息。

在SpringBoot2及之前，我们所配置的username、password等信息是直接配置在spring.datasource参数下的。

但是在SpringBoot3中，配置的数据库连接信息，必须放在`spring.datasource.druid`属性下：

```yaml
spring:
  datasource:
    #使用druid连接池
    type: com.alibaba.druid.pool.DruidDataSource

    druid:
      url: jdbc:mysql://localhost:3306/springboot
      username: root
      password: 061535asd
      driver-class-name: com.mysql.cj.jdbc.Driver
```

**以上就是Druid数据库连接的基础配置**

以下是Druid数据库连接池的常用配置：

```yaml
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





## 4、创建启动类

在com.atguigu包下，创建启动类DruidApplication：

```java
@SpringBootApplication
public class DruidApplication {
    public static void main(String[] args) {
        SpringApplication.run(DruidApplication.class,args);
    }
}
```



## 5、编写Controller

在com.atguigu.controller包下，创建一个Controller类进行测试，如DruidController。

在类中，使用@Autowired引入JdbcTemplate对象（当我们引入spring-boot-starter-jdbc这个数据库相关配置启动器时，会自动将JdbcTemplate对象放入到IOC容器中以供使用），我们使用JdbcTemplate对象去查询数据库进行测试：

```java
@Controller
public class DruidController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public List<User> druidTest(){
        String sql = "select * from user";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        System.out.println(users);
        return users;
    }
}
```

User数据表：

![image-20240613011054186](.\images\image-20240613011054186.png) 

## 6、测试及可能出现的问题

启动SpringBoot，访问/user路径，页面显示结果：

<img src=".\images\image-20240613011122670.png" alt="image-20240613011122670" style="zoom:80%;" /> 

**可能出现的问题**：

当我们在启动SpringBoot项目，出现：

![image-20240613011356560](.\images\image-20240613011356560.png) 

的报错时，此时是因为Druid版本与SpringBoot3的适配问题。

此时的Druid版本比较低，我们需要将其修改成1.2.20及以后的版本即可：

![image-20240613011508404](.\images\image-20240613011508404.png)









---

# 五、整合Mybatis

## 整合步骤说明

1. **导入依赖**：创建项目。然后在pom.xml文件中，将当前项目构建成SpringBoot项目，并添加MyBatis、数据库驱动、连接池以及事务等依赖。
2. **配置参数**：在application.yml中配置数据的连接信息，包括url、用户名以及密码，然后还要配置mybatis的参数，包括mapper映射文件位置、分页插件信息等。
3. **创建实体类**：创建与数据库表对应的实体类。
4. **创建Mapper接口**：创建与数据库表交互的Mapper接口。
5. **创建Mapper接口SQL实现**：使用Mapper映射文件实现SQL。
6. **创建程序启动类**
7. **注解扫描**：在Spring Boot的主应用类上添加@MapperScan注解，用于扫描和注册Mapper接口
8. **使用Mapper接口**





## 具体整合步骤

### 1、创建项目

### 2、导入依赖

在项目的pom.xml中，使用spring-boot-starter-parent作为父工程：

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.0.5</version>
    <relativePath/>
</parent>
```

之后便是去导入依赖。

要导入的依赖有：

spring-boot-starter-web，这是web项目启动器依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

mybatis-spring-boot-starter，这是整合mybatis启动器依赖：

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>3.0.1</version>
</dependency>
```

以及数据库相关配置的启动器依赖：

```xml
<!-- 数据库相关配置启动器 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

druid启动器依赖以及mysql驱动：

```xml
<!-- druid启动器的依赖  -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-3-starter</artifactId>
    <version>1.2.22</version>
</dependency>

<!-- 驱动类-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>${mysql.version}</version>
</dependency>
```

最后是分页插件的启动器依赖以及lombok：

```xml
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
```

这样，整合MyBatis的所有依赖都导入完毕了



### 3、配置参数

参数包括数据库的参数、mybatis的参数：

```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      url: jdbc:mysql://localhost:3306/springboot
      username: root
      password: 061535asd
      driver-class-name: com.mysql.cj.jdbc.Driver

mybatis:
  configuration:
    map-underscore-to-camel-case: true #驼峰
  mapper-locations: classpath:/mapper/*.xml #mapper映射文件
  type-aliases-package: com.atguigu.pojo #起别名

pagehelper:
  helper-dialect: mysql #分页插件
```

这是最常用的参数信息。

其中，**`mybatis.mapper-locations`**参数是去配置Mapper映射文件的位置。在SSM中，我们在配置Mapper映射文件和Mapper接口时，要求这两种文件放在同一文件目录结构下；但是在SpringBoot中，Mapper映射文件和Mapper接口的位置是分开配置的，更加灵活，不需要放在同一目录结构下。Mapper接口的配置在后面。



### 4、实体类准备

创建com.atguigu.pojo包，在该包下创建User类：

```java
@Data
public class User {
    private Integer id;
    private String userName;
    private Integer age;
}
```





### 5、创建三层架构内容

**Controller层**

创建com.atguigu.controller包，在该包下，创建UserController类：

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
}
```



**Service层**

创建com.atguigu.service包，在该包下创建UserService接口：

```java
@Service
public interface UserService {
     List<User> queryList();
}
```

创建com.atguigu.service.impl包，在该包下创建UserServiceImpl类，实现UserServiceImpl接口：

```java
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> queryList() {
        return userMapper.queryAll();
    }
}
```



**Mapper层**

创建com.atguigu.mapper包，在该包下创建UserMapper接口：

```java
public interface UserMapper {
    List<User> queryAll();
}
```

在resources目录下，创建mapper文件夹，在该文件夹下，创建UserMapper.xml映射文件：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.atguigu.mapper.UserMapper">

    <select id="queryAll" resultType="com.atguigu.pojo.User">
        select *
        from user
    </select>
    
</mapper>
```

**注意！注意！注意！**

在Mapper接口中，使用@Repository这个注解是没有作用的！之前都搞错了！

只有在类上，添加Bean的注解才能生效，才能将类的对象作为bean放入到IOC容器中，而对于接口类型，我们使用Bean的注解是无效的。

真正起作用的，实际上是SSM中配置的Mapper扫描路径：

```java
@Bean
public MapperScannerConfigurer mapperScannerConfigurer(){
    MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
    mapperScannerConfigurer.setBasePackage("com.atguigu.mapper");
    return mapperScannerConfigurer;
}
```

所以，我们才要求Mapper接口与Mapper映射文件放在同一个目录下，这样才能将Mapper接口与Mapper映射文件都进行扫描。





### 6、创建启动类

创建启动类，在启动类中，我们需要去指定Mapper接口所在的位置，创建Mapper接口的代理类对象，并将该代理类对象放入到IOC容器中。

实现这一效果的注解是**`@MapperScan`**，使用该注解的`value`属性指定mapper接口的包：

```java
@SpringBootApplication
@MapperScan("com.atguigu.mapper")
public class MybatisApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisApplication.class, args);
    }
}
```

`@MapperScan("com.atguigu.mapper")`去扫描com.atguigu.mapper包下的Mapper接口，去创建这些Mapper接口的代理类对象，并将代理类对象放入到IOC容器中。

这样，我们在service层就可以使用@Autowired注解自动注入这些对象，然后，Mapper映射文件也能够找到对应的mapper接口。



我们也可以采用`@Mapper`注解的方式，修饰Mapper接口，表示当前的接口是一个Mapper接口，也会去创建该接口的代理类对象。这样也能够实现@MapperScan注解的扫描功能：

```java
@Mapper
public interface UserMapper {
    List<User> queryAll();
}
```



在SpringBoot中，能够实现Mapper接口与Mapper映射文件分开配置，更具有灵活性，就不需要在resources目录下，创建一个目录结构与Mapper接口包一样的目录了。



### 7、测试

user表：

![image-20240613114205498](.\images\image-20240613114205498.png) 

访问/user/2/3：

<img src=".\images\image-20240613114242889.png" alt="image-20240613114242889" style="zoom:67%;" /> 





### 配置Mapper接口的三种方式

**方式一：在启动类中使用@MapperScan注解**

在SpringBoot工程的启动类上，使用@MapperScan注解配置mapper的扫描路径，该路径下的接口将创建代理类对象，并放入到IOC容器中。

```java
@SpringBootApplication
@MapperScan("com.atguigu.mapper")
public class MybatisApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisApplication.class, args);
    }
}
```





**方式二：给每个Mapper接口使用@Mapper注解**

这样就不需要扫描指定包

```java
@Mapper
public interface UserMapper {
    List<User> queryAll();

    void updateExampleById(@Param("user") User user);
}
```

使用@Mapper表示该接口就是一个Mapper类型的接口。





**`方式三：创建Mybatis的配置类，在配置类中使用@MapperScan注解（推荐）`**

创建一个配置类，在配置类上使用@MapperScan注解指定扫描的包，同时在配置类中，还可以配置MyBatis的其他参数：

```java
@Configuration
@MapperScan("com.atguigu.mapper")
public class MybatisConfig {
}
```







---



# 注意！Mapper接口使用@Repository注解没有作用

> **`注意！注意！注意！`**
>
> 我原本以为，需要给Mapper接口添加一个@Repository注解，将其放入到IOC容器中就会生效。
>
> 但是，实际上这个注解去修饰接口是不会起作用的！
>
> 因为@Component、@Repository、@Service、@Controller这四个注解的作用是给类创建Bean对象，并放入到IOC容器中，给接口是创建不了对象的，所以给接口设置Bean的注解没有用。
>
> 所以，实际上在学习SSM时，MyBatis的Mapper接口起作用是因为在mybatis-config.xml中的配置：
>
> ```xml
> <mappers>
>     <package name="com.atguigu.mybatis.mapper"/>
> </mappers>
> ```
>
> 使用package批量导入映射的要求是：将mapper接口与mapper映射文件放在了同一目录下Mapper才能起作用，而不是因为@Repository注解！
>
> 在实际开发中，我们使用SpringBoot项目，在SpringBoot项目中，Mapper接口与Mapper映射文件不会放在同一个目录结构下。此时，我们可以使用两种方式让Mapper接口起作用：
>
> 1. **在启动类中扫描Mapper包**
>
>    在启动类中，使用`@MapperScan`注解去扫描指定的mapper包，该包下的mapper接口就能够生成代理类对象，放入到IOC容器中，并能与mapper映射文件产生映射关系
>
>    ```java
>    @SpringBootApplication
>    @MapperScan("com.atguigu.mapper")
>    public class MybatisApplication {
>        public static void main(String[] args) {
>            SpringApplication.run(MybatisApplication.class, args);
>        }
>    }
>    ```
>
> 2. 在Mapper接口中，使用`@Mapper`注解表示当前接口是一个Mapper接口，此时也能够起作用：
>
>    ```java
>    @Mapper
>    public interface UserMapper {
>        List<User> queryAll();
>    }
>    ```
>
>    以上这两种方式都可以使用，也可以一起使用。
>
> 那么此时，我们可以将Mapper接口与Mapper映射文件放在不同的包下，在application.yml中配置的是Mapper映射文件所存放的位置：
>
> ![image-20240613110545200](.\images\image-20240613110545200.png) 
>
> 该位置是在resources/mapper/下。
>
> 此时，mapper接口与mapper映射文件分开指定，就不需要放在同一个结构下了，这样比较灵活。





---

# 六、声明式事务整合配置

**依赖导入：**

```xml
 <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

注：SpringBoot项目会自动配置一个DataSourceTransactionManager，所以我们只需要在方法（或者类）加上@Transactional注解，就自动纳入Spring的事务管理中了。

```java
@Transactional
public void update(){
    User user = new User();
    user.setId(1);
    user.setPassword("test2");
    user.setAccount("test2");
    userMapper.update(user);
}
```



> **`当我们在一个service方法中，需要去两次或两次以上地操作数据库时，就需要添加事务。`**

（如果仅操作一次数据库，可能就不需要添加事务，因为报错只会影响一个操作，一个操作本身就是一个事务，不可再分割）





---

# 七、AOP整合配置

**依赖导入：**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```



直接使用AOP注解即可：

```java
@Component
@Aspect
public class LogAdvice {

    @Before("execution(* com..service.*.*(..))")
    public void before(JoinPoint joinPoint){
        System.out.println("LogAdvice.before");
        System.out.println("joinPoint = " + joinPoint);
    }

}
```





---

# 八、项目打包和运行

SpringBoot项目的打包方式，和之前普通的Web项目不同。

我们先来回顾一下原本Web项目是如何打包的：

我们是将Web项目打包成war包，然后将war包放在Tomcat服务器软件的webapps文件夹下即可，当Tomcat启动时，war包就会自动解压，外部就可以访问该项目了。

SpringBoot就不同了，因为SpringBoot项目内嵌了服务器，就不需要找个服务器进行部署了，本身就可以独立执行了。

SpringBoot项目也要进行打包，只不过打成的是jar包，其内部包含了一个服务器软件，打成jar包后，使用命令执行去执行即可，不需要放到服务器软件中触发服务器执行。



## 1、添加打包插件

> 在SpringBoot项目中添加spring-boot-maven-plugin插件是为了支持将项目打包成可执行的可运行的jar包。
>
> 如果不添加spring-boot-maven-plugin插件配置，使用常规的java -jar命令来运行打包后的SpringBoot项目是无法找到应用程序的入口点，因此导致无法运行。

在项目的pom.xml中，放入下面的打包插件：

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



## 2、执行打包

引入打包插件后，刷新maven，然后在maven页的项目页，点击package进行打包即可：

![image-20240613165227175](.\images\image-20240613165227175.png) 

这个时候，就会在项目的target目录下，生成一个jar包，该jar包就是当前项目的。

![image-20240613165243689](.\images\image-20240613165243689.png) 





## 3、命令启动和参数说明

那打成jar包后，该如何运行呢？
可以使用命令行的方式运行这个项目。

**`java -jar`**命令用于在Java环境中执行可执行的JAR文件。下面是关于java -jar命令的说明：

```
命令格式：java -jar  [选项] [参数] jar文件名
```

jar -jar jar包文件名这三个是固定的格式

其中还可以设置一些参数信息：

1. 使用`-D`开头来设置一些参数值：**`-D<name>=<value>`**。

   例如：`jar -jar -Dserver.port=8080 myapp.jar`，这是去设置服务器的端口号为8080.

   这些设置的参数与SpringBoot项目的配置文件application.yml中的参数是一样的。

   再比如：`-Dspring.profiles.active=<profile>`是指定SpringBoot的激活配置文件，在不同的环境下，指定不同的配置文件。例如：`java -jar -Dspring.profiles.active=dev myapp.jar`，表示当前的环境是开发环境，引入appliation-dev.yml文件中的配置，也就是类似于下面的设置：

   <img src=".\images\image-20240613170521667.png" alt="image-20240613170521667" style="zoom:80%;" /> 

2. 还可以使用**`-X`**开头的参数，来设置JVM参数。例如内存大小、垃圾回收策略等。常用的选项包括：

   * `-Xmx<size>`：设置JVM的最大堆内存大小，例如-Xmx512m表示设置最大堆内存为512MB。
   * `-Xms<size>`：设置JVM的初始堆内存大小，例如-Xms256m表示设置初始堆内存为256MB。

运行时设置参数，能够避免我们因一些参数的修改而再次打包，可以进行临时地修改，更加方便。



## 打包案例

我们对上面整合的SpringBoot项目进行打包，首先，引入打包插件：

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

刷新maven，下载打包插件，下载好后，在maven页中，先对项目编译的数据进行清空，点击clean，然后再点击package进行打包：

<img src=".\images\image-20240613171348694.png" alt="image-20240613171348694" style="zoom:80%;" /> 

此时，在项目下的target目录中，就会生成一个jar包：

<img src=".\images\image-20240613171455009.png" alt="image-20240613171455009" style="zoom: 80%;" /> 

这个jar包就打包完毕了，之后，我们在哪里需要用，就复制到哪里即可。



然后，我们再来说说如何通过jar包来运行项目。

比如，我们将jar包复制出来放在了文件夹内，然后打开命令行终端，可以通过在文件夹目录页使用cmd运行：

![image-20240613171627229](.\images\image-20240613171627229.png)

然后，使用命令`java -jar jar`包文件名的方式即可运行：

<img src=".\images\image-20240613171745463.png" alt="image-20240613171745463" style="zoom:67%;" />

此时就运行成功了。

那么此时，我们去访问/user：

<img src=".\images\image-20240613171835580.png" alt="image-20240613171835580" style="zoom: 67%;" /> 



也能够去访问了。

假如，此时，我们想把服务器的端口号改成8888，通过访问8888端口访问服务器，此时如果去重新修改原项目，再打包就特别麻烦了。

我们可以通过在运行jar包时，来修改参数，比如端口号改成8888：

```shell
java -jar -Dserver.port=8888 jar包文件名
```

此时，服务器的端口号就改成了8888:

![image-20240613172216295](.\images\image-20240613172216295.png) 

<img src=".\images\image-20240613172238663.png" alt="image-20240613172238663" style="zoom:80%;" /> 

所以，我们可以在运行jar包时，临时地修改系统的参数。
