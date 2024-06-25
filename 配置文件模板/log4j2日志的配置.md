### Log4j2日志概述

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



### 整合Log4j2

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

<img src="C:/Users/14036/Desktop/笔记/3、Maven、SSM、MyBatisPlus、SpringBoot/images/image-20240511181902821.png" alt="image-20240511181902821" style="zoom:67%;" /> 

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



#### 测试

我们将之前创建的User测试类中的代码运行一下：

```java
@Test
public void testUserObject(){
    //加载spring配置文件，对象创建
    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    //获取创建的对象
    User user = (User) context.getBean("user");
    System.out.println(user);

    //使用对象调用方法进行测试
    user.add();
}
```

运行结果为：

![image-20240511183435135](C:/Users/14036/Desktop/笔记/3、Maven、SSM、MyBatisPlus、SpringBoot/images/image-20240511183435135.png)

我们可以看到，控制台上多出了三行内容。

让我们来仔细看看其中一行的信息：

![image-20240511183507885](C:/Users/14036/Desktop/笔记/3、Maven、SSM、MyBatisPlus、SpringBoot/images/image-20240511183507885.png)

这其中的每一行内容就是在log4j2.xml配置文件中，输出到控制台的内容信息：

![image-20240511183024816](C:/Users/14036/Desktop/笔记/3、Maven、SSM、MyBatisPlus、SpringBoot/images/image-20240511183024816.png)

我们通过输出的结果，差不多可以了解到控制台输出的日志格式为：

`年月日 时分秒 毫秒 [执行线程] 日志级别 日志执行的类 具体执行的信息`

这是在控制台上的输出，我们来看看文件里面，由log4j2.xml中所配置的那样，日志信息保存在d:/spring6_log/app.log与d:/spring6_log/test.log中：

![image-20240511183828584](C:/Users/14036/Desktop/笔记/3、Maven、SSM、MyBatisPlus、SpringBoot/images/image-20240511183828584.png)

比如其中的test.log：

![image-20240511183846645](C:/Users/14036/Desktop/笔记/3、Maven、SSM、MyBatisPlus、SpringBoot/images/image-20240511183846645.png)

这里面存储了在程序运行过程中所产生的日志信息。

以上是在程序运行过程中，Log4j2的运行日志。

那假如我们想要去手动地输出日志信息，该如何实现呢？





### 手动使用Log4j2日志

手动地使用日志，可以使用`org.slf4j.Logger`类的对象来实现，该类中包含了许多输出日志信息的方法，包括：trace(String)、debug(String)、info(String)、warn(String)等等，这些都是用于输出不同级别的日志信息。

使用`org.slf4j.LoggerFactory`工厂类去获取Logger类的对象信息。

手动使用日志打印信息案例如下：

```java
package com.atguigu.spring6;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestUser {

    private Logger logger = LoggerFactory.getLogger(TestUser.class);

    @Test
    public void testUserObject(){
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        User user = (User) context.getBean("user");
        System.out.println(user);

        user.add();

        //手动写日志
        logger.info("执行调用成功");
    }
}
```

执行结果：

![image-20240511185520019](C:/Users/14036/Desktop/笔记/3、Maven、SSM、MyBatisPlus、SpringBoot/images/image-20240511185520019.png)

我们可以看到，最后一段信息的"执行调用成功"也就是info()方法中写的日志信息。

以上是在控制台输出的日志内容，同时在文件中，也会去输出日志的内容：

![image-20240511185738627](C:/Users/14036/Desktop/笔记/3、Maven、SSM、MyBatisPlus、SpringBoot/images/image-20240511185738627.png)

