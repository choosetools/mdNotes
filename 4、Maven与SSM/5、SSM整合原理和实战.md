该章节SSM整合的项目，我已经放在了lib目录下： [ssm-integration](..\..\studyCode\ssm-integration) 

其中，part1子项目是第二章的SSM整合配置实战内容；

part2子项目是任务列表案例的项目内容。

建议看着项目代码进行学习。



# 一、SSM整合理解

## 1、什么是SSM整合？

**微观：**将学习的Spring SpringMVC Mybatis框架应用到项目中。

* SpringMVC框架负责控制层
* Spring框架负责整体和业务层的声明式事务管理
* MyBatis框架负责数据库访问层

**宏观**：Spring接管一切（将框架核心组件交给Spring的IOC管理），代码更加简洁

* SpringMVC管理表述层、SpringMVC相关组件
* Spring管理业务层、持久层、以及数据库相关（DataSource,MyBatis）的组件
* 使用IOC的方式管理一切所需组件

**实施**：通过编写配置文件，实现SpringIOC容器接管一切组件。



## 2、SSM整合核心问题明确

### SSM整合需要几个IOC容器？

**`两个容器`**

本质上说，整合就是将三层架构和框架核心API组件交给Spring IOC容器管理。

一个容器可能就够了，但是我们常见的操作是创建两个IOC容器（web容器和root容器），组件分类管理。

这样做法有以下好处和目的：

1. 分离关注点：通过初始化两个容器，可以将各个层次的关注点进行分离。这种分离使得各个层次的组件能够更好地聚焦于各自的责任和功能。
2. 解耦合：各个层次组件分离装配不同的IOC容器，这样可以进行解耦。这种解耦合使得各个模块可以独立操作和测试，提高了代码的可维护性和可测试性。
3. 灵活配置：通过使用两个容器，可以为每个容器提供各自的配置，以满足不同层次和组件的特定需求。每个配置文件也更加清晰和灵活。

总的来说，初始化两个容器在SSM整合中可以实现关注点分离、解耦合、灵活配置等好处。它们各自负责不同的层次和功能，并通过合适的集成方式协同工作，提供一个高效、可维护和可扩展的应用程序架构。



---

### 每个IOC容器对应哪些类型组件？

图解：

![img](.\images\image321312312.png)

总结：

| 容器名   | 盛放的组件                                                   |
| -------- | ------------------------------------------------------------ |
| web容器  | web相关组件（controller、SpringMVC核心组件）                 |
| root容器 | 业务层和持久层相关组件（service、aop、tx、dataSource、mybatis、mapper等） |





---

### IOC容器之间关系和调用方向？

假如说两个IOC容器之间没有任何关系的：当服务端接收到请求后，会被Web IoC容器中的Controller接收，在Controller会去注入Root IOC容器中service层的对象，调用service层中的方法，但是因为两个容器之间没有任何关系，所以无法进行注入Root IOC容器中的组件。

![](.\images\imag212312312e.png)

所以，两个容器之间是有关系的。

**Root容器是父容器，Web容器是子容器。**

**`子容器可以单向的注入父容器IOC容器的组件。`**

![](.\images\image2131231231.png)

* 父容器：
* 子容器

> **子容器可以调用父容器中的组件，所以在controller中可以去调用service层中的方法，但是父容器不可以调用子容器组件，所以在service层与mapper层中无法调用controller层中的组件。**



---

### 具体多少配置类以及对应容器关系？

配置类的数量不是固定的，但是至少要两个，为了方便编写，我们建议三层架构中每一层都对应一个配置类，使用两个容器进行加载。

![img](.\images\image312313143532.png)

建议配置文件：

| 配置类名                | 对应内容                          | 对应容器 |
| ----------------------- | --------------------------------- | -------- |
| **`WebJavaConfig`**     | controller层，springmvc相关       | web容器  |
| **`ServiceJavaConfig`** | service层，aop、tx相关            | root容器 |
| **`MapperJavaConfig`**  | mapper层，dataSource、mybatis相关 | root容器 |







---

### IOC初始化方式和配置位置？

在WEB项目下，我们可以选择使用web.xml或配置类的方式进行IOC配置，推荐使用配置类的方式。

对于使用基于web的Spring配置的应用程序，建议这样做：

```java
public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

  //指定root容器对应的配置类
  //root容器的配置类
  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class<?>[] { ServiceJavaConfig.class,MapperJavaConfig.class };
  }
  
  //指定web容器对应的配置类 webioc容器的配置类
  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class<?>[] { WebJavaConfig.class };
  }
  
  //指定dispatcherServlet处理路径，通常为 / 
  @Override
  protected String[] getServletMappings() {
    return new String[] { "/" };
  }
}
```

图解配置类和容器配置：

![](.\images\imageedaeqaeq.png)





---

# 二、SSM整合配置实战（:star:）

## 1、数据准备与依赖导入

### 1）数据表准备

```sql
CREATE DATABASE `mybatis-example`;

USE `mybatis-example`;

CREATE TABLE `t_emp`(
  emp_id INT AUTO_INCREMENT,
  emp_name CHAR(100),
  emp_salary DOUBLE(10,5),
  PRIMARY KEY(emp_id)
);

INSERT INTO `t_emp`(emp_name,emp_salary) VALUES("tom",200.33);
INSERT INTO `t_emp`(emp_name,emp_salary) VALUES("jerry",666.66);
INSERT INTO `t_emp`(emp_name,emp_salary) VALUES("andy",777.77);
```

![image-20240610171856855](.\images\image-20240610171856855.png) 

表中包含emp_id、emp_name以及emp_salary字段。



### 2）项目准备

创建一个父工程ssm-integration，将父工程中的打包方式修改为pom，并且删除工程下的src目录。

然后将项目所使用的maven仓库，修改成本地的maven：

<img src=".\images\image-20240610175454321.png" alt="image-20240610175454321" style="zoom:50%;" />  

之后去创建一个子工程part01，然后使用插件JBLJavaToWeb将子工程转换成Web工程：

<img src=".\images\image-20240610175256351.png" alt="image-20240610175256351" style="zoom: 80%;" /> 

**给项目配置Tomcat服务器：**

<img src=".\images\image-20240610225328431.png" alt="image-20240610225328431" style="zoom:50%;" /> 



**设置项目使用Tomcat部署：**

<img src=".\images\image-20240610225345657.png" alt="image-20240610225345657" style="zoom:67%;" /> 

<img src=".\images\image-20240610225422428.png" alt="image-20240610225422428" style="zoom: 67%;" /> 

<img src=".\images\image-20240610225452904.png" alt="image-20240610225452904" style="zoom:67%;" /> 

使用war exploded部署模式：

<img src=".\images\image-20240610225518077.png" alt="image-20240610225518077" style="zoom: 80%;" /> 

设置项目的上下文路径为/：

<img src=".\images\image-20240610225546496.png" alt="image-20240610225546496" style="zoom:50%;" /> 







### 3）导入依赖

在父工程的pom.xml，导入工程所需的依赖。

**Spring所需依赖：**

* spring基础依赖：spring-context 6.0.6
* aop依赖：spring-aspects 6.0.6
* 声明式事务所需依赖：spring-jdbc 6.0.6



**SpringMVC所需依赖：**

* webmvc依赖：spring-webmvc 6.0.6
* Servlet的依赖：jakarta.servlet-api（设置为provided）6.0.0
* json格式所需依赖：jackson-databind 2.15.2
* 若是混合开发模式，还需引入jsp依赖：jakarta.servlet.jsp.jstl-api 3.0.0
* 声明式参数验证所需依赖：hibernate-validator与hibernate-validator-annotation-processor 8.0.0.Final



**MyBatis所需依赖：**

* mybatis核心依赖：mybatis 3.5.7
* mysql驱动：mysql-connector-java 8.0.30
* 分页插件依赖：pagehelper 5.2.1



**其他依赖：**

* 加载spring容器：spring-web 6.0.6

* 整合SSM依赖包：mybatis-spring 3.0.2

* log4j2日志依赖：log4j-core与log4j-slf4j2-impl 2.19.0
* 数据库连接池：druid 1.2.15
* lombok：1.18.30
* junit测试依赖：junit-jupiter-api 5.3.1



**依赖导入：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.atguigu</groupId>
    <artifactId>ssm-integration</artifactId>
    <version>1.0</version>
    <packaging>pom</packaging>
    <modules>
        <module>part01</module>
    </modules>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <spring.version>6.0.6</spring.version>
        <jakarta.servlet-api.version>6.0.0</jakarta.servlet-api.version>
        <jackson-databind.version>2.15.2</jackson-databind.version>
        <jakarta.servlet.jsp.jstl-api.version>3.0.0</jakarta.servlet.jsp.jstl-api.version>
        <hibernate-validator.version>8.0.0.Final</hibernate-validator.version>
        <mybatis.version>3.5.7</mybatis.version>
        <mysql.version>8.0.30</mysql.version>
        <pagehelper.version>5.2.1</pagehelper.version>
        <mybatis-spring.version>3.0.2</mybatis-spring.version>
        <log4j2.version>2.19.0</log4j2.version>
        <druid.version>1.2.15</druid.version>
        <lombok.version>1.18.30</lombok.version>
        <junit.version>5.3.1</junit.version>
    </properties>

    
    <dependencies>
        <!--基本依赖-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <!-- aop -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aspects</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <!--spring声明式事务-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <!--spring的webmvc依赖-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <!--Servlet的依赖，Spring6要求使用jakarta命名空间-->
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>${jakarta.servlet-api.version}</version>
            <scope>provided</scope>
        </dependency>

        <!--处理json格式数据所需依赖-->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>${jackson-databind.version}</version>
        </dependency>

        <!-- jsp所需依赖-->
        <dependency>
            <groupId>jakarta.servlet.jsp.jstl</groupId>
            <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
            <version>${jakarta.servlet.jsp.jstl-api.version}</version>
        </dependency>

        <!-- 校验注解实现-->
        <!-- https://mvnrepository.com/artifact/org.hibernate.validator/hibernate-validator -->
        <dependency>
            <groupId>org.hibernate.validator</groupId>
            <artifactId>hibernate-validator</artifactId>
            <version>${hibernate-validator.version}</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.hibernate.validator/hibernate-validator-annotation-processor -->
        <dependency>
            <groupId>org.hibernate.validator</groupId>
            <artifactId>hibernate-validator-annotation-processor</artifactId>
            <version>${hibernate-validator.version}</version>
        </dependency>

        <!-- Mybatis核心 -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>${mybatis.version}</version>
        </dependency>

        <!-- MySQL驱动 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql.version}</version>
        </dependency>

        <!--分页插件-->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <version>${pagehelper.version}</version>
        </dependency>

        <!--加载spring容器-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <!--整合SSM-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>${mybatis-spring.version}</version>
        </dependency>

        <!--log4j2日志依赖-->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>${log4j2.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j2-impl</artifactId>
            <version>${log4j2.version}</version>
        </dependency>

        <!--druid数据库连接池-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>${druid.version}</version>
        </dependency>

        <!--lombok-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
        </dependency>

        <!--junit5测试-->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>${junit.version}</version>
        </dependency>

    </dependencies>

</project>
```



### 4）创建表对应的实体类

创建com.atguigu.pojo包，在该包下，创建一个Employee类：

```java
@Data
public class Employee {
    private Integer empId;
    private String empName;
    private Double empSalary;
}
```





 ### 5）创建日志配置文件

在子工程的resources目录下，创建`log4j2.xml`配置文件（文件名是固定的，且必须存放到resources目录下）：

<img src=".\images\image-20240610175851764.png" alt="image-20240610175851764" style="zoom: 67%;" /> 

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









---

## 2、控制层配置编写

创建`com.atguigu.config`包，使用配置类的方式编写配置，该包就是专门用来存放配置类的。

在该包下创建`WebJavaConfig`类（可随意取名），使用该类配置控制层：

![image-20240610182208249](.\images\image-20240610182208249.png) 

> **控制层的配置需要编写的内容包括：**
>
> 1. controller层包的扫描路径
> 2. handlerMapping、handlerAdapter组件配置
> 3. JSON转化器配置
> 4. 全局异常处理器类配置
> 5. 开启静态资源处理
> 6. jsp视图解析器的配置（适用于混合开发模式）
> 7. 拦截器配置等等

```java
@Configuration
@ComponentScan(value = {"com.atguigu.controller", "com.atguigu.exception"})
@EnableWebMvc
public class WebJavaConfig implements WebMvcConfigurer {
    //jsp视图解析器，配置跳转路径的前缀与后缀
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/",".jsp");
    }

    //开启静态资源扫描
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    //拦截器配置
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new 拦截器类()).addPathPatterns(指定拦截的请求路径);
    }

}
```

其中：

* @ComponentScan("com.atguigu.controller", "com.atguigu.exception")是去指定要扫描的包，包含了controller包以及我们所创建的存放全局异常处理类的包exception。

* @EnableWebMvc能够帮助我们去创建handlerMapping对象与handlerAdapter对象，同时配置JSON转换器。

* 将配置类实现WebMvcConfigurer，我们就能够去重写其中的方法用于配置组件以及开启相关功能：
  * configureViewResolvers()表示配置视图解析器；
  * configureDefaultServletHandling()表示开启静态资源扫描；
  * addInterceptors()表示去添加拦截器的配置。





---

## 3、业务层配置编写

> **业务层的配置需要编写的内容包括：**
>
> 1. 配置service层包的扫描
> 2. 开启aop注解支持
> 3. 创建事务管理器的实现
> 4. 开启声明式事务注解支持

在com.atguigu.config包下，创建`ServiceJavaConfig`配置类，使用该类来配置业务层：

```java
@Configuration
@ComponentScan("com.atguigu.service")
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class ServiceJavaConfig {
    
    @Bean
    public TransactionManager transactionManager(DataSource dataSource){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }
    
}
```

由于事务需要对数据库进行操作，所以我们需要在事务管理器中配置数据库的数据源信息用于连接数据库。

使用@Bean注解修饰的方法，方法的形参会使用IOC中的bean对象注入，返回值对象会放入的IOC容器中。

此时，这里的DataSource对象是报红色异常的，很正常，因为现在还没有把DataSource对象放入IOC容器中，当把DataSource对象放入到IOC容器中以后，IOC容器在创建TransactionManager事务管理器对象时，就会自动将DataSource对象注入到创建该方法的形参中使用。



**解释：**

* @Configuration注解表示当前类是一个配置类
* @ComponentScan注解进行业务组件的扫描
* @EnableAspectJAutoProxy注解，表示开启AOP注解支持
* @EnableTransactionManagement注解，表示开启事务注解支持
* 声明transactionManager(DataSource dataSource)方法，创建事务管理器对象





---

## 4、持久层配置编写

持久层配置主要是去配置mapper的代理类对象，数据库连接池，以及mybatis的核心组件。

### mybatis整合思路

**mybatis核心api的使用回顾：**

```java
//1、读取外部mybatis配置文件
InputStream is = Resources.getResourceAsStream("mybatis-config.xml");

//2、创建SqlSessionFactory对象
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

//3、创建SqlSession
SqlSession sqlSession = sqlSessionFactory.openSession();

//4、获取mapper接口的代理类对象
EmpMapper empMapper = sqlSession.getMapper(EmpMapper.class);

//5、调用数据库的方法
int rows = empMapper.deleteEmpById(1);
```

**核心API的说明：**

* SqlSessionFactoryBuilder

  这个类可以被实例化、使用和丢弃，一旦创建了SqlSessionFactory，就不再需要。因为SqlSessionFactoryBuilder实例的最佳作用域是方法作用域。`无需IOC容器管理`。

* SqlSessionFactory

  一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。使用SqlSessionFactory的最佳实践是在应用运行期间不要重复创建多次，因此SqlSessionFactory的最佳作用域是应用作用域。**`需要IOC容器管理。`**

* SqlSession

  每个线程都应该有它自己的SqlSession实例，SqlSession实例不是线程安全的，是不能被共享的，因此它的最佳作用域是请求或方法作用域。`无需IOC容器管理`。

* Mapper映射器实例

  映射器是一些绑定映射语句的接口。映射器接口的实例是从SqlSession中获得的。虽然从技术层面上来讲，任何映射器实例的虽大作用域与请求它们的SqlSession相同。但方法作用域才是映射器实例的最合适的作用域。

  从作用域的角度来说，映射器实例不应该交给IOC容器管理。

  但是从使用的角度来说，业务层需要注入mapper层的接口，所以**`mapper应该交给IOC容器管理`**。

  ![img](.\images\image2231243123.png)

* **总结：**
  * 将SqlSessionFactory实例存储到IOC容器中
  * 将Mapper实例存储到IOC容器中



**mybatis整合思路理解：**

我们需要自己去实现SqlSessionFactory加入到IOC容器中：

```java
@Bean
public SqlSessionFactory sqlSessionFactory(){
   //1.读取外部配置文件
  InputStream ips = Resources.getResourceAsStream("mybatis-config.xml");

  //2.创建sqlSessionFactory
  SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(ips);

  return sqlSessionFactory;
}
```

这样实现比较繁琐，为了提高整合效率，mybatis给我们提供了封装SqlSessionFactory和Mapper实例化逻辑的FactoryBean组件：**`SqlSessionFactoryBean`**，我们只需要声明少量的代码，将SqlSessionFactoryBean加入到IOC容器中即可。

这个类是一个FactoryBean类型，实际加入到IOC容器中的，并不是这个类，而是将类中的`getObject()`返回的对象加入到IOC容器中。

FactoryBean类型是为了解决复杂的Bean对象创建而出现的类型。



**mybatis整合思路总结**

* 需要将SqlSessionFactory和Mapper实例加入到IOC容器中
* 使用mybatis整合包提供的SqlSessionFactoryBean进行快速整合





### 准备外部配置文件

在resources目录下，创建`jdbc.properties`，该配置文件保存的是数据库连接信息

```properties
jdbc.user=root
jdbc.password=061535asd
jdbc.url=jdbc:mysql://localhost:3306/mybatis-example
jdbc.driver=com.mysql.cj.jdbc.Driver
```





### 整合方式一：保留xml配置文件（不推荐）

#### 配置说明

这种方式是去保留mybatis的核心配置文件，在mybatis的核心配置相关的组件，但是数据库连接信息是要放入到IOC容器中的，因为SqlSessionFactoryBean与Spring的声明式注解中都需要使用。

<img src=".\images\image12312534263456.png" alt="img" style="zoom: 80%;" />

缺点：依然需要xml文件，进行xml文件解析效率较低。



#### xml配置文件的配置

在resources目录下，创建mybatis-config.xml文件，在该文件中不再去配置数据库连接信息和mapper扫描包配置，而mybatis的其他功能（包括别名、settings、插件等信息）依然在mybatis-config.xml中配置。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//MyBatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <settings>
        <!--将表中字段的下划线自动转换为驼峰-->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <!--开启延迟加载-->
        <setting name="lazyLoadingEnabled" value="true"/>
    </settings>

    <typeAliases>
        <!--给类取别名-->
        <package name="com.atguigu.pojo"/>
    </typeAliases>
    

    <!--分页插件-->
    <plugins>
        <plugin interceptor="com.github.pagehelper.PageInterceptor">
            <!--
                helperDialect：分页插件会自动检测当前的数据库链接，自动选择合适的分页方式。
                你可以配置helperDialect属性来指定分页插件使用哪种方言。配置时，可以使用下面的缩写值：
                oracle,mysql,mariadb,sqlite,hsqldb,postgresql,db2,sqlserver,informix,h2,sqlserver2012,derby
             -->
            <property name="helperDialect" value="mysql"/>
        </plugin>
    </plugins>

</configuration>
```



#### 持久层配置类的配置

在持久层配置类配置：数据库连接信息、创建SqlSessionFactoryBean对象并放入IOC容器中，以及将Mapper代理对象加入到IOC容器中。

```java
@Configuration
@PropertySource("classpath:jdbc.properties") //导入外部配置文件
public class MapperJavaConfig {
    @Value("${jdbc.user}")
    private String user;

    @Value("${jdbc.password}")
    private String password;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.driver}")
    private String driver;

    //配置数据源对象
    @Bean
    public DataSource dataSource(){
        //创建的是Druid数据库连接池
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        return dataSource;
    }

    
    
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        //指定数据库连接池对象
        sqlSessionFactoryBean.setDataSource(dataSource);

        //指定外部的mybatis配置文件
        Resource resource = new ClassPathResource("mybatis-config.xml");
        sqlSessionFactoryBean.setConfigLocation(resource);

        return sqlSessionFactoryBean;
    }

   
    
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //指定mapper包路径
        mapperScannerConfigurer.setBasePackage("com.atguigu.mapper");
        return mapperScannerConfigurer;
    }

}
```

**说明：**

* 在类上，使用`@PropertySource`注解导入外部配置文件。
* `@Value`注解表示使用外部配置文件中的属性给当前属性赋值。
* `dataSource()`表示去创建数据源对象，创建的是Druid数据库连接池对象。
* `sqlSessionFactoryBean()`表示去创建SqlSessionFactoryBean对象，该类是一个工厂Bean，使用IOC容器创建时实际调用的是类中的getObject()方法，该类实际上是去创建SqlSessionFactory对象，在其中要去指定数据库连接池信息，以及指定外部的配置文件。
* `mapperScannerConfigure`r也是个工厂Bean，它是Mapper代理实现的FactoryBean。它是去指定一个包，将该包下所有的Mapper接口全部转换成Mapper代理对象，并将Mapper对象放入到IOC容器中。在其底层实际上也是使用SqlSession对象，使用getMapper()的方式获取代理类对象，所以我们需要将SqlSessionFactory放入到IOC容器中。



**注意**：

* @Value注解是org.springframework.beans.factory.annotation包下的，而不是lombok包中的。
* Resource类是org.springframework.core.io包下的。
* 要求Mapper接口与Mapper映射文件要放在同一个包下，这样才能在mapperScannerConfigurer()方法中通过指定包路径的方式获取Mapper的代理类对象。





##### 存在的问题

上述的配置可能会存在sqlSessionFactoryBean()方法中，读取到的dataSource对象中设置的属性都为null的问题。

这是因为`SqlSessionFactoryBean`和`MapperScannerConfigurer`在配置类中通常是用来配置MyBatis相关的Bean，例如数据源、事务管理器、Mapper扫描等。这些配置类通常在`@Configuration`注解下定义，并且使用`@Value`注解来注入属性值。

当配置类被加载时，Spring容器会首先处理Bean的定义和初始化，其中包括`sqlSessionFactoryBean`和`mapperScannerConfigurer`的初始化。在这个过程中，如果`@Value`注解所在的Bean还没有被完全初始化，可能会导致注入的属性值为null。

即：

**sqlSessionFactoryBean与mapperScannerConfigurer会被先初始化，此时@Value注解修饰的属性还没有被初始化，所以dataSource对象中的属性就是默认值null。**





##### 解决方案

**`分成两个配置类独立配置类`**，互不影响，数据库提取一个配置类，mybatis提取一个配置类。

**数据库配置类（DataSourceJavaConfig）**

```java
@Configuration
@PropertySource("classpath:jdbc.properties") //导入外部配置文件
public class DataSourceJavaConfig {
    @Value("${jdbc.user}")
    private String user;

    @Value("${jdbc.password}")
    private String password;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.driver}")
    private String driver;

    //配置数据源对象
    @Bean
    public DataSource dataSource() {
        //创建的是Druid数据库连接池
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        return dataSource;
    }
}
```

**mybatis配置类（MapperJavaConfig）**

```java
@Configuration
public class MapperJavaConfig {

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        //指定数据库连接池对象
        sqlSessionFactoryBean.setDataSource(dataSource);

        //指定外部的mybatis配置文件
        Resource resource = new ClassPathResource("mybatis-config.xml");
        sqlSessionFactoryBean.setConfigLocation(resource);

        return sqlSessionFactoryBean;
    }

    
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.atguigu.mapper");
        return mapperScannerConfigurer;
    }

}
```



此时，我们也可以发现，service层的配置类中，在transactionManager()方法中导入的dataSource形参也不会报红了，因为此时已经将dataSource对象加入到了IOC容器中。

<img src=".\images\image-20240610222009191.png" alt="image-20240610222009191" style="zoom:80%;" /> 





---

### 整合方式二：完全配置类（推荐）

#### 配置说明

这种方式不再保留mybatis的外部配置文件（xml），所有配置信息（setting、插件、别名等）全部在声明SqlSessionFactoryBean的代码中指定，数据库信息依然使用DruidDataSource实例代替。

<img src=".\images\image12313123.png" alt="img" style="zoom:80%;" />



**优势**：全部配置类，避免了XML文件解析效率低的问题。



#### 持久层配置类的配置

在SqlSessionFactoryBean对象方法中，配置mybatis的组件。

* 使用SqlSessionFactoryBean对象的`setConfiguration()`方法，来配置mybatis的settings属性，该方法可以传入一个Configuration对象，该对象就是用于设置settings属性的。注意，该Configuration类是org.apache.ibatis.session包下的类。

* 使用SqlSessionFactoryBean对象的`setTypeAliasesPackage()`方法设置指定包下的类取别名。
* 使用SqlSessionFactoryBean对象的`addPlugins()`方法给mybatis添加插件，可以添加PageInterceptor分页插件。

```java
@Configuration
public class MapperJavaConfig {

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        //指定数据库连接池对象
        sqlSessionFactoryBean.setDataSource(dataSource);
//<settings>
//        <!--将表中字段的下划线自动转换为驼峰-->
//        <setting name="mapUnderscoreToCamelCase" value="true"/>
//        <!--开启延迟加载-->
//        <setting name="lazyLoadingEnabled" value="true"/>
//    </settings>
//
//    <typeAliases>
//        <!--给类取别名-->
//        <package name="com.atguigu.pojo"/>
//    </typeAliases>
//
//
//    <!--分页插件-->
//    <plugins>
//        <plugin interceptor="com.github.pagehelper.PageInterceptor">
//            <!--
//                helperDialect：分页插件会自动检测当前的数据库链接，自动选择合适的分页方式。
//        你可以配置helperDialect属性来指定分页插件使用哪种方言。配置时，可以使用下面的缩写值：
//        oracle,mysql,mariadb,sqlite,hsqldb,postgresql,db2,sqlserver,informix,h2,sqlserver2012,derby
//                -->
//            <property name="helperDialect" value="mysql"/>
//        </plugin>
//    </plugins>
        //settings
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLazyLoadingEnabled(true);
        sqlSessionFactoryBean.setConfiguration(configuration);
        

        //typeAliases
        sqlSessionFactoryBean.setTypeAliasesPackage("com.atguigu.pojo");
        

        //分页插件配置
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        pageInterceptor.setProperties(properties);
        sqlSessionFactoryBean.addPlugins(pageInterceptor);

        return sqlSessionFactoryBean;
    }


    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.atguigu.mapper");
        return mapperScannerConfigurer;
    }

}
```





别忘了**数据库连接信息的配置类**：

```java
@Configuration
@PropertySource("classpath:jdbc.properties") //导入外部配置文件
public class DataSourceJavaConfig {
    @Value("${jdbc.user}")
    private String user;

    @Value("${jdbc.password}")
    private String password;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.driver}")
    private String driver;

    //配置数据源对象
    @Bean
    public DataSource dataSource() {
        //创建的是Druid数据库连接池
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        return dataSource;
    }
}
```





这样一来，三个层级的配置类就全部配置完毕了：

![image-20240610223526779](.\images\image-20240610223526779.png) 

* DataSourceJavaConfig表示数据库连接配置类

* MapperJavaConfig表示mapper层配置类
* ServiceJavaConfig表示service层配置类
* WebJavaConfig表示controller配置类

然后，我们就需要将这四个配置类，放入到容器初始化配置类中。





---

## 5、容器初始化配置类

在com.atguigu.config包下，创建一个类用于容器的初始化，如SpringIocInit类：

```java
public class SpringIocInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    //指定root容器对应的配置类
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{MapperJavaConfig.class, ServiceJavaConfig.class, DataSourceJavaConfig.class};
    }

    //指定web容器对应的配置类
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebJavaConfig.class};
    }

    //指定DispatcherServlet的处理路径，一般为/
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
```

在Root容器中，添加数据库连接配置类、mapper层配置类、以及service层配置类。

在web容器中，添加controller层配置类。





---

## 整合测试

案例：查询所有员工信息，返回对应json数据

**controller层**

```java
@Controller
@Slf4j
@RequestMapping("/emp")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Employee> queryList(){
        List<Employee> employees = employeeService.queryAllEmps();
        log.info(employees.toString());
        return employees;
    }
}
```

**service层**

service接口

```java
public interface EmployeeService {
    public List<Employee> queryAllEmps();
}
```

service实现类

```java
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<Employee> queryAllEmps() {
        return employeeMapper.queryAll();
    }
}
```



**mapper层**

mapper接口

```java
public interface EmployeeMapper {
    List<Employee> queryAll();
}
```

mapper映射文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.atguigu.mapper.EmployeeMapper">


    <select id="queryAll" resultType="com.atguigu.pojo.Employee">
        select *
        from t_emp
    </select>
</mapper>
```



**测试**

<img src=".\images\image-20240610233816491.png" alt="image-20240610233816491" style="zoom:67%;" /> 





---



# 三、《任务列表案例》程序案例——前后端分离项目

## 1、案例介绍与接口分析

### 案例功能预览

![img](.\images\imag21312312e.png)

### 接口分析

1. 学习计划分页查询

```java
/* 
需求说明
    查询全部数据页数据
请求uri
    schedule/{pageSize}/{currentPage}
请求方式 
    get   
响应的json
    {
        "code":200,
        "flag":true,
        "data":{
            //本页数据
            data:
            [
            {id:1,title:'学习java',completed:true},
            {id:2,title:'学习html',completed:true},
            {id:3,title:'学习css',completed:true},
            {id:4,title:'学习js',completed:true},
            {id:5,title:'学习vue',completed:true}
            ], 
            //分页参数
            pageSize:5, // 每页数据条数 页大小
            total:0 ,   // 总记录数
            currentPage:1 // 当前页码
        }
    }
*/
```

2. 学习计划删除

```java
/* 
需求说明
    根据id删除日程
请求uri
    schedule/{id}
请求方式 
    delete
响应的json
    {
        "code":200,
        "flag":true,
        "data":null
    }
*/
```

3. 学习计划保存

```java
/* 
需求说明
    增加日程
请求uri
    schedule
请求方式 
    post
请求体中的JSON
    {
        title: '',
        completed: false
    }
响应的json
    {
        "code":200,
        "flag":true,
        "data":null
    }
*/
```

4. 学习计划修改

```java
/* 
需求说明
    根据id修改数据
请求uri
    schedule
请求方式 
    put
请求体中的JSON
    {
        id: 1,
        title: '',
        completed: false
    }
响应的json
    {
        "code":200,
        "flag":true,
        "data":null
    }
*/
```







---

## 2、前端工程导入

> Node.js是前端程序运行的服务器，类似于Java程序运行的服务器Tomcat
>
> Npm是前端依赖包管理工具，类似于maven依赖管理工具软件

对于前后端分离的项目来说，与后端一样，前端也需要运行在自己的服务器上，这里的Node.js就是前端的服务器。

1. **node的安装**

这里使用的node版本是16.16.0，下载地址：

[Index of /download/release/v16.16.0/ (nodejs.org)](https://nodejs.org/download/release/v16.16.0/)

我已经下载好放在了笔记的lib目录下了，直接安装即可： [node-v16.16.0-x64.msi](lib\node-v16.16.0-x64.msi) ，安装比较傻瓜，一直点next下一步即可。

安装完成后，可以在命令行终端输入`node -v`和`npm -v`来验证是否安装成功：

<img src=".\images\image-20240611003409417.png" alt="image-20240611003409417" style="zoom:80%;" /> 

出现版本号即说明安装成功。



2. **npm的使用**

> npm全称Node Package Manager，是Node.js包管理工具，是全球最大的模块生态系统，里面所有的模块都是开源免费的；也是Node.js的包管理工具，相当于后端的maven。

npm类似于maven，在使用maven仓库时，我们去配置了阿里的镜像仓库，对于npm来说，我们也去配置阿里镜像。

在命令行中执行下面的命令，去配置阿里镜像：

```shell
npm config set registry https://registry.npmjs.org/
```

然后我们还要去更新npm的版本，node16.16.0对应的npm版本过低，需要升级。

在命令行输入下面命令升级npm版本：

```shell
npm install -g npm@9.6.6
```





3. **导入前端程序**

前端程序已经写好了，在lib目录下： [vue3-demo2](lib\vue3-demo2) 

我们使用VS code打开这个目录即可：

![img](.\images\image2231312.png) 

打开后：

![image-20240611004706065](.\images\image-20240611004706065.png) 



4. **引入依赖**

在项目目录中，有一个package.json文件：

![image-20240611004837589](.\images\image-20240611004837589.png) 

<img src=".\images\image-20240611004955878.png" alt="image-20240611004955878" style="zoom:50%;" /> 

这个文件就类似于Java项目中maven的pom.xml文件，是存放依赖信息的，

当我们导入项目时，这些依赖还没有引入，所以我们需要先去引入依赖信息。

点击Vs code右上角的按钮：

![image-20240611005226793](.\images\image-20240611005226793.png) 

它会在下方打开一个控制台，在控制台中输入：

```shell
npm install
```

它就会去帮助我们下载项目中package.json中声明的依赖：

<img src=".\images\image-20240611005339879.png" alt="image-20240611005339879" style="zoom:67%;" /> 

下载完毕后，会在项目下创建一个node_modules目录，类似于maven项目中的target目录。

![image-20240611005533599](.\images\image-20240611005533599.png) 





5. **启动测试**

在控制台中输入：

```shell
npm run dev
```

启动项目：

![image-20240611005716335](.\images\image-20240611005716335.png) 

此时会显示项目的访问地址，访问一下：

<img src=".\images\image-20240611005832735.png" alt="image-20240611005832735" style="zoom: 67%;" /> 

这里的数据都是假数据，实际的数据是要去数据库中获取的。







---

## 3、后端程序实现与测试

### 3.1、准备工作

1. **准备框架**

首先去创建一个子项目part2，用于去开发《任务列表案例》，然后将该项目转换成web项目。

然后，去将原本的SSM整合移植到这个项目中，具体的步骤我就不再阐述了，不知道可以看上面的SSM整合配置实战。

移植后：

<img src=".\images\image-20240611133229696.png" alt="image-20240611133229696" style="zoom:80%;" />  

2. **准备数据库脚本**

创建数据表schedule：

```sql
CREATE TABLE schedule (
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  completed BOOLEAN NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO schedule (title, completed)
VALUES
    ('学习java', true),
    ('学习Python', false),
    ('学习C++', true),
    ('学习JavaScript', false),
    ('学习HTML5', true),
    ('学习CSS3', false),
    ('学习Vue.js', true),
    ('学习React', false),
    ('学习Angular', true),
    ('学习Node.js', false),
    ('学习Express', true),
    ('学习Koa', false),
    ('学习MongoDB', true),
    ('学习MySQL', false),
    ('学习Redis', true),
    ('学习Git', false),
    ('学习Docker', true),
    ('学习Kubernetes', false),
    ('学习AWS', true),
    ('学习Azure', false);

```

<img src=".\images\image-20240611133408834.png" alt="image-20240611133408834" style="zoom: 80%;" /> 



3. **准备实体类**

创建com.atguigu.pojo包，在该包下，创建`Schedule`类：

```java
@Data
public class Schedule {
    private Integer id;
    private String title;
    private Boolean completed;
}
```



4. **准备返回结果类**

在实际开发中，一般返回给前端的数据对象类是固定的，有一个专门的类用于返回，而不是后端想返回什么对象就返回什么对象。

我们要去定义一个类，专门用于响应JOSN数据，在这个类中，一般包含着响应状态码，提示信息，以及实际返回的数据。

在案例项目中，我们来看看返回的信息中包括哪些数据：

<img src=".\images\image-20240611134332491.png" alt="image-20240611134332491" style="zoom: 80%;" /> 

可以看到，响应的信息包括code、flag以及data。其中code表示的是响应码、flag表示的返回的状态、data则表示具体的数据。

对于查询响应的数据来说，响应的data对象中包含data属性、pageSize属性、total、currentPage属性，这些属性同样也是固定的，我们需要去创建一个类，专门用来返回查询到数据。

**创建返回结果类**

创建com.atguigu.utils包，在该包下创建返回结果类，如CommonResult，在该类中对data属性使用泛型表示。

当执行成功时，返回一个code为200、flag为true以及具体执行结果的CommonResult对象；

当执行失败时，返回一个code为500、flag为false以及null的CommonResult对象。

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> {
    private String code;

    private Boolean flag;

    private T data;

    public static <T> CommonResult<T> success(T data){
        return new CommonResult<>("200", true, data);
    }

    public static <T> CommonResult<T> fail(){
        return new CommonResult<>("500", false, null);
    }
}
```





**创建查询结果类**

若查询的结果是一个List集合类型，那么，还需要考虑分页的问题，因为页面会去将多个数据进行分页。

分页的信息包括当前页码、每页数量、总条数以及查询的结果。

所以，我们还需要去创建一个类，用来返回分页信息。

在com.atguigu.config包中创建`CommonPage`类，用于存放查询分页数据：

```java
@Data
public class CommonPage<T> {
    //查询的结果
    private List<T> data;

    //每页大小
    private Integer pageSize;

    //总记录数
    private Integer total;

    //当前页码
    private Integer currentPage;
}
```

我们还需要将PageHelper分页后的List转为CommonPage对象，所以，我们需要在该类中声明一个static方法，用于将List转为CommomPage：

```java
public static <T> CommonPage<T> restPage(List<T> list) {
    CommonPage<T> result = new CommonPage<>();
    PageInfo<T> pageInfo = new PageInfo<>(list);
    result.setCurrentPage(pageInfo.getPageNum());
    result.setData(list);
    result.setPageSize(pageInfo.getPageSize());
    result.setTotal(pageInfo.getTotal());
    return result;
}
```

这样数据就准备好了。





### 3.2、功能实现

#### controller层

创建com.atguigu.controller包，在包下创建ScheduleController类：

```java
@Controller
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    /**
     * 分页查询
     * @param pageSize
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/schedule/{pageSize}/{currenPage}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<Schedule>> getSchedule(
            @PathVariable("pageSize") Integer pageSize,
            @PathVariable("currenPage") Integer currentPage){
        PageHelper.startPage(currentPage, pageSize);
        List<Schedule> schedules = scheduleService.queryList();
        return CommonResult.success(CommonPage.restPage(schedules));
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult deleteSchedule(@PathVariable("id") Integer id){
        scheduleService.deleteById(id);
        return CommonResult.success(null);
    }


    /**
     * 增加日程
     * @param schedule
     * @return
     */
    @RequestMapping(value = "/schedule", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult postSchedule(@RequestBody Schedule schedule){
        scheduleService.saveSchedule(schedule);
        return CommonResult.success(null);
    }

    @RequestMapping(value = "/schedule", method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult putSchedule(@RequestBody Schedule schedule){
        scheduleService.updateSchedule(schedule);
        return CommonResult.success(null);
    }
}
```







#### service层

创建com.atguigu.service层。

ScheduleService接口：

```java
public interface ScheduleService {
    List<Schedule> queryList();

    void deleteById(Integer id);

    void saveSchedule(Schedule schedule);

    void updateSchedule(Schedule schedule);
}
```



创建com.atguigu.service.impl包，在该包下创建实现类。

ScheduleServiceImpl实现类：

```java
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleMapper scheduleMapper;

    @Override
    public List<Schedule> queryList() {
        return scheduleMapper.queryAll();
    }

    @Override
    public void deleteById(Integer id) {
        scheduleMapper.deleteById(id);
    }

    /**
     * 保存对象
     * @param schedule
     */
    @Override
    public void saveSchedule(Schedule schedule) {
        scheduleMapper.saveSchedule(schedule);
    }

    /**
     * 修改对象
     * @param schedule
     */
    @Override
    public void updateSchedule(Schedule schedule) {
        scheduleMapper.updateById(schedule);
    }

}
```



#### mapper层

创建com.atguigu.mapper包，在该包下，创建ScheduleMapper接口

```java
public interface ScheduleMapper {
    List<Schedule> queryAll();

    void deleteById(@Param("id") Integer id);

    void saveSchedule(@Param("schedule") Schedule schedule);

    void updateById(@Param("schedule") Schedule schedule);
}
```

在resources目录下，创建com.atguigu.mapper包，在该包下创建ScheduleMapper映射文件：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.atguigu.mapper.ScheduleMapper">
    
    <insert id="saveSchedule">
        insert into schedule(title, completed)
        values (#{schedule.title}, #{schedule.completed})
    </insert>
    
    <update id="updateById">
        update schedule
        set title = #{schedule.title}, completed = #{schedule.completed}
        where id = #{schedule.id}
    </update>

    <delete id="deleteById">
        delete from schedule
        where id = #{id}
    </delete>


    <select id="queryAll" resultType="com.atguigu.pojo.Schedule">
        select *
        from schedule
    </select>
</mapper>
```



### 实现跨域访问

当我们要去使用前端的服务器，访问后端的服务器时，由于前端node的地址是

![image-20240611155736300](.\images\image-20240611155736300.png) 

http://127.0.0.1:5173/

而后端的Tomcat地址是http://localhost:8080/

要想使用http://127.0.0.1:5173/访问http://localhost:8080/，这实际上是跨域访问，两个地址不同。

对于跨域访问来说，后端的服务器默认会禁止跨域访问，那么我们就需要在Controller类中，使用**`@CrossOrigin`**注解修饰，表示当前Controller类中所设置的地址，可以跨域访问：

![image-20240611160025606](.\images\image-20240611160025606.png) 

此时，就可以使用前端的服务器去发送请求访问后端了。

那么，我们就可以去访问http://127.0.0.1:5173/，使用前端页面的方式与后端实现联调了。
