[TOC]



# 所有可能所需的依赖

```xml
<dependencies>
    <!--基本依赖-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>6.0.6</version>
    </dependency>

    <!-- aop -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-aspects</artifactId>
        <version>6.0.6</version>
    </dependency>

    <!--junit5测试-->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.3.1</version>
    </dependency>

    <!--spring jdbc  Spring 持久化层支持jar包-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>6.0.6</version>
    </dependency>

    <!--spring用于整合junit的支持-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId>
        <version>6.0.6</version>
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



---

# 整体性说明

Spring的知识，相当于是去学习MVC三层架构中的service层的学习。

我们主要是去学习：

1. IOC容器该如何创建

2. AOP该如何使用
3. 声明式事务该如何创建



---

# 一、概述

## 1、Spring是什么？

Spring是一款主流的JavaEE轻量级开源框架，Spring由"Spring之父"Rod Johnson提出并创立，其目的是用于简化Java企业级应用的开发难度和开发周期。

Spring的用途不仅限于服务器端的开发。从简单性、可测试性和松耦合的角度而言，任何Java应用都可以从Spring中受益。Spring框架除了自己提供功能外，还提供整合其他技术和框架的能力。

Spring自诞生依赖备受青睐，一直被广大开发人员作为Java企业级应用程序开发的首选。时至今日，Spring俨然成为了Java EE代名词，成为了构建Java EE应用的事实标准。

**`本课程采用的是Spring的正式版本6.0.6`**

![image-20221216223135162](.\images\image-20221201102513199.png)



## 2、Spring的狭义和广义

在不同的语境中，Spring所代表的含义是不同的。下面我们就分别从"广义"和"狭义"两个角度，对Spring进行介绍。

**广义的 Spring：Spring 技术栈**

广义上的Spring泛指以Spring Framework为核心的 Spring 技术栈。

经过十多年的发展，Spring 已经不再是一个单纯的应用框架，而是逐渐发展成为一个由多个不同子项目（模块）组成的成熟技术，例如 Spring Framework、Spring MVC、SpringBoot、SpringCloud、Spring Data、Spring Security等，其中Spring Framework是其他子项目的基础。

这些子项目涵盖了从企业级应用开发到云计算等各方面的内容，能够帮助开发人员解决软件发展过程中不断产生的各种实际问题，给开发人员带来了更好的开发体验。

**狭义的 Spring：Spring Framework**

狭义的Spring特指Spring Framework，通常我们将它称为Spring 框架。

Spring 框架是一个分层的、面向切面的Java应用程序的一站式轻量级解决方案，它是Spring技术栈的核心和基础，是为了解决企业级应用开发的复杂性而创建的。

**`Spring有两个最核心的模块：IOC 和 AOP.`**

**IOC**：Inverse Of Control的简写，译为"控制反转"，指把创建对象过程交给Spring进行管理。

**AOP**：Aspect Oriented Programming的简写，译为"面向切面编程"。AOP用来封装多个类的公共行为，将那些与业务无关，却为业务模块所共同调用的逻辑封装起来，减少系统的重复代码，降低模块间的耦合度。另外，AOP还解决一些系统层面上的问题，比如日志、事务、权限等。



## 3、Spring Framework特点

* 非侵入式：使用Spring Framework开发应用程序时，Spring对应用程序本身的结构影响非常小。对领域模型可以做到零污染；对功能性组件也只需要使用几个简单的注解进行标记，完全不会破坏原有结构，反而能将组件结构进一步简化。这就使得基于Spring Framework开发应用程序时结构清晰、简洁优雅。
* 控制反转：IOC，翻转资源获取方向。把自己创建资源、向环境索取资源变成环境将资源准备好，我们享受资源注入。
* 面向切面编程：AOP，在不修改源代码的基础上增强代码功能。
* 容器：Spring IOC 是一个容器，因为它包含并且管理组件对象的生命周期。组件享受到了容器化的管理，替程序员屏蔽了组件创建过程中的大量细节，极大的降低了使用门槛，大幅度提高了开发效率。
* 组件化：Spring 实现了使用简单的组件配置组合成一个复杂的应用。在Spring 中可以使用 XML 和 Java注解 组合这些对象。这使得我们可以基于一个个功能明确、边界清晰的组件有条不紊的搭建超大型复杂应用系统。
* 一站式：在IOC 和AOP 的基础上可以整合各种企业应用的开源框架和优秀的第三方类库。而且Spring 旗下的项目已经覆盖了广泛的领域，很多方面的功能性要求可以在Spring Framework 的基础上全部使用 Spring 来实现。





## 4、Spring模块组成

![image-20221207142746771](.\images\image-20221207142746771.png)

![image-2097896352](.\images\2097896352.png)

上图中包含了Spring框架的所有模块，这些模块可以满足一切企业级应用开发的需求，在开发过程中可以根据需求有选择性的使用所需要的模块。下面分别对这些模块的作用进行简单的介绍。

**①Spring Core（核心容器）**

spring core提供了IOC、DI、Bean配置装载创建的核心实现。核心概念： Beans、BeanFactory、BeanDefinitions、ApplicationContext。

- spring-core ：IOC和DI的基本实现

- spring-beans：BeanFactory和Bean的装配管理(BeanFactory)
- spring-context：Spring context上下文，即IOC容器(AppliactionContext)
- spring-expression：spring表达式语言

**②Spring AOP**

- spring-aop：面向切面编程的应用模块，整合ASM，CGLib，JDK Proxy
- spring-aspects：集成AspectJ，AOP应用框架
- spring-instrument：动态Class Loading模块

**③Spring Data Access**

- spring-jdbc：spring对JDBC的封装，用于简化jdbc操作
- spring-orm：java对象与数据库数据的映射框架
- spring-oxm：对象与xml文件的映射框架
- spring-jms： Spring对Java Message Service(java消息服务)的封装，用于服务之间相互通信
- spring-tx：spring jdbc事务管理

**④Spring Web**

- spring-web：最基础的web支持，建立于spring-context之上，通过servlet或listener来初始化IOC容器
- spring-webmvc：实现web mvc
- spring-websocket：与前端的全双工通信协议
- spring-webflux：Spring 5.0提供的，用于取代传统java servlet，非阻塞式Reactive Web框架，异步，非阻塞，事件驱动的服务

**⑤Spring Message**

- Spring-messaging：spring 4.0提供的，为Spring集成一些基础的报文传送服务

**⑥Spring test**

- spring-test：集成测试支持，主要是对junit的封装



## 版本要求

> **`Spring6要求JDK最低版本是JDK17`**

本课程的软件版本：

JDK：Java17

Spring：6.0.6







# 二、引入Spring的案例

**入门案例开发步骤：**

1. 我们先去搭建一个父工程，比如这个工程就叫做Spring6。然后再在父工程下创建多个子模块（子工程），在这些子工程中去实现Spring的入门案例。
2. 引入Spring的相关依赖。这里我们引入的是spring-context依赖，这个spring-context依赖中依赖传递了Spring的基础依赖。然后还引入了juint的依赖。
3. 创建类，用于测试，定义类的属性和方法。
4. 按照Spring的要求创建配置文件（xml格式），在配置文件中配置相关的信息。比如创建的类叫做User类，我们在配置文件中，对User类的对象进行配置。
5. 进行最终的测试



## 环境要求

* JDK：Java17+（Spring6要求JDK最低版本是Java17）
* Maven：3.6+
* IDEA 2023.2.2
* Spring：6+（这里使用的是Spring 6.0.6）

### 准备工作

1. 下载IDEA以及JDK17，并配置相关的环境（略）
2. 下载Maven 3.6.3，并设置相关配置文件（请查看maven章）
3. IDEA中配置maven



## 入门案例

### 1、搭建模块

1. **搭建父工程**

创建父工程，把这个工程命名为Spring6，该工程使用的是Maven的方式进行构建的，JDK选择的是17：

<img src=".\images\image-20240511150406931.png" alt="image-20240511150406931" style="zoom: 50%;" /> 

由于在父工程中无需写代码，父工程仅仅只是用于统一管理的作用，所以，将父工程目录下用于编写代码的目录src删除：

![image-20240511150627681](.\images\image-20240511150627681.png) 

2. **搭建子模块**

在父工程的目录下，去创建子模块（module），我们这里将子模块命名为spring-first：

<img src=".\images\image-20240511150732472.png" alt="image-20240511150732472" style="zoom:67%;" /> 

<img src=".\images\image-20240511150830203.png" alt="image-20240511150830203" style="zoom:50%;" /> 

我们需要确认，在创建子模块时，其Parent这一栏是父工程Spring6，表示当前创建的模块的父工程是Spring6，其是在Spring6的基础上进行创建的。

这样一来，父工程和子工程的搭建就完成了：

<img src=".\images\image-20240511151040652.png" alt="image-20240511151040652" style="zoom: 67%;" /> 

**注意**：搭建完项目后，需要检查当前项目是否是自己所配置的maven（IDEA自己会自动带一个maven），如果不是需要将maven设置为之前所配置的maven:

<img src=".\images\image-20240511161928252.png" alt="image-20240511161928252" style="zoom: 50%;" /> 



### 2、引入依赖

对于这种包含父子工程的项目来说，我们一般使用Maven的继承特性，让父工程中去声明依赖的版本，在子工程中直接声明依赖的GA属性，而不需要在声明依赖的version，这样能够让依赖的版本进行统一的管理。

所以，我们在父工程的pom.xml中，去声明依赖，包含依赖的GAV属性；子啊子工程的pom.xml中，引入依赖，只包含依赖的GA属性。这样的话，依赖的版本就是依据其父工程中依赖的version。

**父工程中声明依赖：**

<img src=".\images\image-20240511151744663.png" alt="image-20240511151744663" style="zoom: 67%;" /> 

声明spring-context依赖以及junit依赖（junit方便后续的测试）：

```xml
<dependencyManagement>
    <dependencies>
    	<!--spring context依赖-->
    	<!--当你引入Spring Context依赖之后，表示将Spring的基础依赖引入了-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>6.0.6</version>
        </dependency>

        <!--junit5测试-->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.3.1</version>
        </dependency>
        
    </dependencies>
</dependencyManagement>
```

此时可能会报红，刷新maven也不会得到解决，原因在于父工程中使用了`dependencyManagement`标签，这个标签实际表示的含义是父工程中声明的这些依赖，仅仅是用于声明依赖的版本，用于统一管理父工程下所有子工程的依赖版本的，并不会实际引入依赖，maven仓库中此时可能不包含这些依赖信息，所以maven会报红。当我们在后续子工程中引入依赖后，报红就会得到解决。

**子工程中引入依赖：**

<img src=".\images\image-20240511152308919.png" alt="image-20240511152308919" style="zoom:67%;" /> 

引入spring-context依赖以及junit依赖:

```XML
<dependencies>
    
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
    </dependency>

    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
    </dependency>
    
</dependencies>
```

由于我们使用了maven继承特性，在父工程的pom.xml中统一声明了依赖的版本，所以当在子工程中不去声明引入依赖的version属性时，就会自动地使用父工程中所依赖的版本。

然后，刷新maven，第一次引入时，会去联网下载。下载完毕之后，这时，父工程下的pom.xml中就不再有报红了。

此时，我们去查看一下子工程中的依赖信息：

<img src=".\images\image-20240511152919453.png" alt="image-20240511152919453" style="zoom: 80%;" /> 

可以看到，spring-context中依赖传递了四个依赖：spring-aop、spring-beans、spring-core以及spring-expression。



### 3、创建Java类

先创建com.atguigu.spring6包，然后在包下创建User类：

<img src=".\images\image-20240511154058049.png" alt="image-20240511154058049" style="zoom: 80%;" /> 

```java
package com.atguigu.spring6;

public class User {
    public void add(){
        System.out.println("add...........");
    }
}
```



### 4、创建配置文件

在resources目录中（该目录专门用于存放配置文件），创建一个Spring的配置文件beans.xml（配置文件可随意取名）：

<img src=".\images\image-20240511154340012.png" alt="image-20240511154340012" style="zoom: 50%;" />

创建完成如图所示：

![image-20240511154621231](.\images\image-20240511154621231.png)

其中，我们可以看到，beans标签中包含以下的属性：

```xml
xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd"
```

这一部分内容，表明该beans标签的约束，也就是表示该beans标签中可以写哪些标签，可以有哪些属性。

原本，我们去创建User类对象，可以通过new User()的方式进行，自己去手动地创建一个对象。在Spring中，我们可以在其配置文件中去完成对象的创建。

在Spring的配置文件中，使用**`bean`**标签来完成类的创建，bean标签中包含两个属性：

* id属性：表示bean标签的唯一标识
* class属性：表示要创建对象所在类的全路径（包名+类名）

此时，我们就可以在Spring配置文件中，完成类对象的创建：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!--完成对象的创建：使用bean标签
        bean标签中包含两个属性：id和class
            id属性：表示bean标签的唯一标识
            class属性：要创建对象所在类的全路径（包名+类名）
    -->
    <bean id="user" class="com.atguigu.spring6.User"></bean>
    
</beans>
```





### 5、创建测试类测试

创建一个测试类TestUser，用于测试Spring：

```java
public class TestUser {
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
}
```

这里面通过ApplicationContext类，加载beans.xml配置文件，通过这种方式去创建User对象。

测试结果：

![image-20240511162522706](.\images\image-20240511162522706.png) 

以上就是Spring入门案例的整个过程。



### 可能出现的问题

maven引入的依赖的scope属性默认是compile属性，即该依赖全局生效。

但是引入的junit单元测试依赖，可能设置了scope属性为test，表示的含义是仅在src的test目录下生效：

<img src=".\images\image-20240511161058556.png" alt="image-20240511161058556" style="zoom: 80%;" /> 

由于我们这里的测试类是放在main目录下的，而不是放在test目录下。对于引入的junit依赖中，scope属性不能为test，如果为test就表示该依赖只作用于test目录下。建议直接将scope属性删除。（在父工程中所声明的依赖版本中，如果也有scope属性，也删除）

子工程中：

<img src=".\images\image-20240511160412738.png" alt="image-20240511160412738" style="zoom:67%;" /> 

父工程中：

<img src=".\images\image-20240511161150968.png" alt="image-20240511161150968" style="zoom:67%;" /> 

这样一来，junit就能够在全局都生效了。





## 入门案例分析

**分析1：**

我们来分析一个问题，我们之前创建对象，都是通过new的方式去创建的，例如new User()，这是去调用类中的无参构造器去创建的对象。

那我们通过Spring配置文件的方式创建的对象，是否还会去调用User类中的无参构造器呢？

我们来测试一下：

在User类中创建无参构造器：

```java
public class User {
    public User(){
        System.out.println("User类中的无参构造器........");
    }

    public void add(){
        System.out.println("add...........");
    }

    public static void main(String[] args) {
        User user = new User();
    }
}
```

此时，再去执行测试类，结果为：

![image-20240511164752855](.\images\image-20240511164752855.png) 

由测试得知：

> **使用上述方式，通过Spring配置文件的方式创建对象，实际上也调用了类中的无参构造器创建。**



---

**分析2：**

上述通过Spring配置文件的方式创建对象，不是自己去new的，而实际上是通过**`反射`**来实现的。

**首先，让我们回顾一下，如何通过反射来创建运行时类对象？**

**方式一**：使用Class类的newProxyInstance()方法（不推荐）

例如：

```java
Class<Person> clazz = Person.class;
//创建Person类的实例
Person p1 = clazz.newProxyInstance();
System.out.println(p1);
```

使用这种方式要求：

* 运行时类中必须有一个无参构造器
* 当前位置的访问权限足够访问到该无参构造器

**方式二**：通过获取构造器对象来进行实例化（推荐）

例如：

```java
Class<Person> clazz = Person.class;
Constructor<Person> constructor = clazz.getDeclaredConstructor(String.class, int.class);
constructor.setAccessible(true);
Person person = constructor.newProxyInstance("cheng", 24);
```

使用这种方式，可以使用运行时类中的任意一个构造器创建对象，并且没有权限限制（通过setAccessible的设置解决）。

那我们是如何使用Spring配置文件来创建对象的呢？

> **Spring配置文件创建对象的步骤：**
>
> 1、加载Spring的xml配置文件
>
> 2、对xml文件进行解析操作
>
> 3、获取xml文件bean标签属性值（id与class）
>
> 4、使用反射根据类的全路径创建对象
>
> ```java
> //获取类Class对象，传入
> Class<?> clazz = Class.forName("com.atguigu.spring6.User");
> 
> //调用方法创建对象
> User user = (User) clazz.getDeclaredConstructor().newProxyInstance();
> ```
>
> 5、创建完的对象，存放到Spring容器中。
>
> 以上就是通过Spring配置文件，创建对象的大致过程。





---

分析3：

让我们来执行以下的代码：

```java
@Test
public void testUserObject(){
    System.out.println("------------------------");

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

    System.out.println("*********************");

    User user = (User) context.getBean("user");
    System.out.println(user);

    user.add();
}
```

执行结果：

![image-20240511172315812](.\images\image-20240511172315812.png) 

这个执行结果，能够让我们知道的是：User类对象的创建是在我们去加载配置文件时就进行的，而不是在我们getBean()也就是获取该对象时进行的。

所以：

> **当我们去加载Spring配置文件时，就会通过反射创建Spring配置文件中所配置类的对象。而不是在获取类对象时才去创建。**

那创建出来的类对象存放在哪呢？

这就设置到Spring的容器了。

让我们进入到源码中进行查看：

> 在spring源码底层，**存储bean对象的spring容器实际上是一个map集合，存放在`DefaultListableBeanFactory`类**中。

![image-20240511172839168](.\images\image-20240511172839168.png)

![image-20240511172901965](.\images\image-20240511172901965.png)

创建的对象全都存储在map集合中：

map集合的key，也就是bean标签的唯一标识id，通过这个key来获取对象；value，是类的描述信息，是BeanDefinition类型的。



## 整合Log4j2日志框架

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

<img src=".\images\image-20240511181902821.png" alt="image-20240511181902821" style="zoom:67%;" /> 

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

![image-20240511183435135](.\images\image-20240511183435135.png)

我们可以看到，控制台上多出了三行内容。

让我们来仔细看看其中一行的信息：

![image-20240511183507885](.\images\image-20240511183507885.png)

这其中的每一行内容就是在log4j2.xml配置文件中，输出到控制台的内容信息：

![image-20240511183024816](.\images\image-20240511183024816.png)

我们通过输出的结果，差不多可以了解到控制台输出的日志格式为：

`年月日 时分秒 毫秒 [执行线程] 日志级别 日志执行的类 具体执行的信息`

这是在控制台上的输出，我们来看看文件里面，由log4j2.xml中所配置的那样，日志信息保存在d:/spring6_log/app.log与d:/spring6_log/test.log中：

![image-20240511183828584](.\images\image-20240511183828584.png)

比如其中的test.log：

![image-20240511183846645](.\images\image-20240511183846645.png)

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

![image-20240511185520019](.\images\image-20240511185520019.png)

我们可以看到，最后一段信息的"执行调用成功"也就是info()方法中写的日志信息。

以上是在控制台输出的日志内容，同时在文件中，也会去输出日志的内容：

![image-20240511185738627](.\images\image-20240511185738627.png)





# 三、容器 IOC

## 1、概述

### 什么是IOC容器？

IOC是Inversion of Control的简写，译为"控制反转"，它不是一种技术，而是一种**设计思想**，是一个重要的面向对象编程法则，能够指导我们如何设计出松耦合、更优良的程序。

> **Spring通过IOC容器来管理所有Java对象的实例化和初始化，控制对象与对象之间的依赖关系。**

我们将由 IoC 容器管理的Java对象称为Spring Bean，它与使用关键字 new 创建的Java对象没有任何区别。

IoC容器是Spring框架中最重要的核心组件之一，它贯穿了 Spring 从诞生到成长的整个过程。

**`容器里面存放的是bean对象，底层使用的是map集合进行存储的。`**

容器负责创建、配置和管理bean，也就是它管理着bean的生命，控制着bean的依赖注入。

通俗点讲，因为项目中每次创建对象是很麻烦的，所以我们需要使用Spring IoC来管理这些对象，需要的时候直接使用，不需要管它是怎么来的、什么时候要销毁，直接使用就好。

那bean是什么呢？

**bean其实就是包装了的Object**，无论是控制反转还是依赖注入，主语都是Object，而bean就是由第三方包装好了的object。



### 什么是控制反转？

控制，是指对于bean的创建、管理的权限。

反转，意味着将控制的权利交给了Spring容器，而不是自己去控制。由之前自己主动创建对象，变为现在被动接收别人给我们的对象的过程，这就是反转。

控制反转是一种**思想**，为了降低程序耦合度，提高程序扩展力。

控制反转使用**DI依赖注入**的方式来实现的。



### 什么是依赖注入

`DI（Dependency Injection）`：依赖注入。依赖注入实现了控制反转的思想。

**在Spring创建过程中，可以将对象的依赖资源通过配置的方式进行注入。**

那也就是说，配置文件把资源从外部注入到内部，容器加载了外部的文件、对象、数据，然后把这些资源注入给程序内的对象，维护了程序内外对象之间的依赖关系。

就比如：User类中有两个属性，分别为基本数据类型的age以及引用数据类型的parent，这两个属性的值我们都可以在xml配置文件中进行配置，然后在spring创建过程中，注入这两个属性的值到bean中

```java
public class User {
	private int age;
	private Parent parent;
}
```

依赖注入常见的实现包括两种：

* 第一种：set注入
* 第二种：构造注入

**`IOC是一种控制反转的思想，而DI是对IOC的一种具体实现。`**



### 使用IOC容器创建对象的大致过程

大致过程如下所示：

![image-20240512010243177](.\images\image-20240512010243177.png)![image-20240512010333497](.\images\image-20240512010333497.png)

首先，我们需要将类的信息定义在spring配置文件中；

然后，我们会使用到BeanDefinitionReader接口的实现类，去读取配置文件中的信息；

之后，将这些读取到的信息（Bean定义信息）使用BeanFactory工厂与反射对对象进行实例化；

然后，再对对象进行初始化；

最后，调用getBean()方法，获取到类的对象。



### 使用IOC容器的主要类

Spring的IoC容器就是IOC思想的一个落地产品实现。IOC容器中管理的组件也叫作bean。在创建bean之前，首先需要创建IOC容器。

![iamges](.\images\img005.png)

Spring提供了IOC容器的两种实现方式：

1. **BeanFactory接口**

这是IoC容器的基本实现，是Spring内部使用的接口。该接口只面向Spring本身，我们使用的是该接口的实现类，不会直接使用到该接口。

2. **ApplicationContext接口**

BeanFactory的子接口，提供了更多高级特性。面向Spring的使用者，几乎所有场合都使用ApplicationContext，而不是底层的BeanFactory。

**ApplicationContext的主要实现：**

| `类型名`                          | 简介                                                         |
| --------------------------------- | ------------------------------------------------------------ |
| `ConfigurableApplicationContext`  | ApplicationContext的子接口，包含一些扩展的方法refresh()和close()，让ApplicationContext具有启动、关闭和刷新上下文的能力 |
| `ClassPathXmlApplicationContext`  | 通过读取类路径下的XML格式的配置文件创建IOC容器对象           |
| `FileSystemXmlApplicationContext` | 通过文件系统路径读取XML格式的配置文件创建IOC容器对象         |
| `WebApplicationContext`           | 专门为Web应用准备，基于Web环境创建IOC容器对象，并将对象引入存入ServletContext域中 |





## 2、基于XML管理bean

### 环境搭建

原本已经存在一个父工程Spring6和一个子工程spring-first，这两个工程的搭建是在之前入门案例中进行搭建的：

<img src=".\images\image-20240512095501339.png" alt="image-20240512095501339" style="zoom:67%;" /> 

在父工程的pom.xml文件中，引入了四种依赖，其中，第一个依赖是spring的基础依赖，第二个依赖是junit5的依赖，后两个依赖是log4j2日志的依赖：

```xml
<dependencies>
    
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>6.0.6</version>
    </dependency>

    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.3.1</version>
    </dependency>


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

由于这些依赖是在父工程中引入的，所以在父工程下的所有子工程中都可以使用。

**1、在spring6工程下创建子模块spring6-ioc-xml：**

<img src=".\images\image-20240512095757477.png" alt="image-20240512095757477" style="zoom: 50%;" />

<img src=".\images\image-20240512095835841.png" alt="image-20240512095835841" style="zoom: 50%;" />

创建完成之后，我们可以看到，刚刚创建的子工程拥有父工程中引入的四个依赖：

<img src=".\images\image-20240512100031149.png" alt="image-20240512100031149" style="zoom:67%;" /> 

**2、为子工程spring-ioc-xml创建spring配置文件**

在子工程的resources目录下，创建spring的配置文件bean.xml（这里的bean.xml不是固定的名字，可以随意取名）：

<img src=".\images\image-20240512100222805.png" alt="image-20240512100222805" style="zoom:50%;" />

![image-20240512100250285](.\images\image-20240512100250285.png)

**3、在java目录下，创建包com.atguigu.spring6.ioc.xml，并在包下创建User类：**

```java
package com.atguigu.spring6.ioc.xml;

public class User {
    private String name;
    private Integer age;

    public void run(){
        System.out.println("run...........");
    }
}
```

**4、在resources目录下创建log4j2.xml文件（文件名称是固定的，不可更改），并在文件中输入以下内容，以下是log4j2日志文件的模板：**

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

        <!--文件会打印出所有信息，这个log每次运行程序会自动清空，由append属性决定，适合临时测试用-->
        <File name="log" fileName="d:/spring6_log/test.log" append="false">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} %-5level %class{36} %L %M - %msg%xEx%n"/>
        </File>

        <!-- 这个会打印出所有的信息，
            每次大小超过size，
            则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，
            作为存档-->
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

这样一来，基本的准备工作就完成了。



---

### 获取bean对象的三种方式

一个bean，就是一个Java对象。bean标签中有两个主要的属性，分别是id和class，一个用于唯一标识bean，一个表明类的全类名。

Spring容器根据bean标签创建对象，尽管可能存在class属性相同的bean标签，只要是id值不同，spring容器就会创建该class对象。



#### 方式一：根据id获取

这里的id指的是，在spring配置文件中所配置的bean标签的id属性。

由于id属性指定了bean的唯一标识，所以根据bean标签的id属性可以精确获取到一个组件对象。

比如：

在spring的配置文件bean.xml中，声明了User类的bean标签：

```xml
<!--user对象创建-->
<bean id="user" class="com.atguigu.spring6.ioc.xml.User">
```

我们在使用时，就可以通过这个id属性获取User类的bean对象：

```java
//获取ApplicationContext对象
ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");

//根据id获取bean
User user = (User) context.getBean("user");

System.out.println("根据id获取bean：" + user);
```

输出结果：

![image-20240512102339094](.\images\image-20240512102339094.png)

红框前面的输出结果，是打印的日志信息；红框中的信息，就是我们打印在控制台上的输出结果。

在日志结果中，最后有这样一句话：`Creating shared instance of singleton bean 'user'`，这句话的含义是创建出来的是单例bean的共享实例。具体内容请查看《bean的生命周期》部分



#### 方式二：根据类型获取

根据类型获取的含义，就是直接通过类的来获取，而不通过id获取bean。如：

```java
//获取ApplicationContext对象
ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");

User user2 = context.getBean(User.class);

System.out.println("根据类型获取bean：" + user2);
```

注意：虽然不通过spring配置文件中bean标签的id属性获取，但是依然需要在spring配置文件中配置该类的bean标签的。

即我们是通过spring配置文件声明的bean标签来创建bean对象存入到ioc容器中的，如果配置文件中没有进行配置，那么不会创建bean对象，从而也无法获取。



#### 方式三：根据id和类型获取（推荐）

通过id和类型获取更好理解了，就是将配置文件中bean标签的id属性以及该bean标签所对应的类的Class对象都传入到getBean()方法中，获取bean对象：

```java
//获取ApplicationContext对象
ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");

//根据id和类型获取
User user3 = context.getBean("user", User.class);

System.out.println("根据id和类型获取bean：" + user3);
```

使用这种方式有好处：既融合了使用id获取bean对象，来保证使用唯一的bean标签，从而避免了因不知道使用哪一个bean标签获取bean对象而报错的问题；又融合了使用全类名获取bean对象，来确保编译器知道自己创建的bean对象属于哪一个类，从而避免进行强转而可能产生的错误。







**可能发生的错误**

当spring配置文件中，有相同class类型属性的bean标签，此时如果使用根据类型的方式获取bean对象，会报错。

例如：

在spring配置文件中，配置了两个User类的bean标签：

```xml
<bean id="user" class="com.atguigu.spring6.ioc.xml.User"></bean>
<bean id="user1" class="com.atguigu.spring6.ioc.xml.User"></bean>
```

此时，如果我们通过根据类型的方式获取bean对象：

```java
User user2 = context.getBean(User.class);
System.out.println("根据类型获取bean：" + user2);
```

就会报错：

![image-20240512104234640](.\images\image-20240512104234640.png)

报错的原因就在于：我们通过类型去获取bean，但是此时spring配置文件中，有两个bean对象是符合条件的，此时就不知道通过哪个bean标签获取bean对象，所以会报错。

解决方案：在spring配置文件中，有业务需求需要我们创建多个相同类型的bean标签时，此时我们去获取bean对象，则需要通过id或者id和class的方式获取。



#### 扩展

**问题1**：假设我们要使用IOC容器去获取接口的实现类对象，`接口类 对象名 = new 实现类()`是采用向上转型的方式获取的，那么我们是否可以采用IOC容器的方式，通过接口的类型获取实现类的bean对象呢？

**问题2**：对问题1的扩展，假如说接口的实现类有很多，比如UserDao中有多个实现类，那么我们是否还可以通过接口类来获取其实现类对象？

**测试：**

首先我们去创建一个接口，例如UserDao：

```java
package com.atguigu.spring6.ioc.xml.bean;

public interface UserDao {
    public void run();
}
```

然后创建其实现类对象，例如UserDaoImpl：

```java
package com.atguigu.spring6.ioc.xml.bean;

public class UserDaoImpl implements UserDao{
    @Override
    public void run() {
        System.out.println("run.............");
    }
}
```

然后，我们去Spring的配置文件中，对其进行配置。由于我们需要获取到的是实现类对象，所以我们配置的就是实现类的bean标签：

```xml
<bean id="userDaoImpl" class="com.atguigu.spring6.ioc.xml.bean.UserDaoImpl"></bean>
```

然后，我们去创建一个测试类，用于测试，是否可以通过接口类型去获取到实现类对象的bean，所以我们这里是通过类型去获取bean的方式：

```java
ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");

//根据类型获取接口对应bean
UserDao userDao = context.getBean(UserDao.class);
userDao.run();
```

运行结果：

![image-20240512113623481](.\images\image-20240512113623481.png) 

该运行结果，就说明了，此时我们获取到的UserDao是UserDaoImpl的对象，通过多态去执行了运行时类对象中的run()方法。

即，我们**可以通过接口的类型去获取运行时类的bean对象。**

现在我们来验证一下第二个问题：

我们再去给UserDao创建一个实现类，例如PersonDaoImpl：

```java
package com.atguigu.spring6.ioc.xml.bean;

public class PersonDaoImpl implements UserDao{
    @Override
    public void run() {
        System.out.println("Person run..........");
    }
}
```

然后我们去Spring配置文件中，去给该实现类在spring配置文件中，配置相应的bean标签：

```xml
<bean id="personDaoImpl" class="com.atguigu.spring6.ioc.xml.bean.PersonDaoImpl"></bean>
```

此时，我们再去通过接口类型获取实现类的bean对象：

```java
ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");

//根据类型获取接口对应bean
UserDao userDao = context.getBean(UserDao.class);
userDao.run();
```

此时就会报错：

![image-20240512115430131](.\images\image-20240512115430131.png)

由于此时，一个接口存在两个实现类对象，如果此时我们通过接口类型去找bean对象，此时就不知道是去找userDaoImpl还是personDaoImpl。



**结论**

根据类型来获取bean时，在满足bean唯一性的前提下，其实只是看："`对象 instanceof 指定的类型`"返回结果，只要返回的是true就可以认定为和类型匹配，能够获取到。

java中，instanceof运算符用于判断前面的对象是否是后面的类，或者子类、实现类的实例。如果是返回true，否则返回false。也就是说，用instanceof关键字做判断时，instanceof操作符的左右操作有继承或实现关系。



---

### 依赖注入

#### 1、基本注入方式

在之前的案例中，使用getBean()方式创建对象，**默认使用的是类中的`无参构造器`来创建对象的**，使用对象的无参构造器创建对象，对象的属性必定为初始值，例如int类型为0，boolean类型的属性为false。

依赖注入，可以简单地理解为：设置类对象的属性值。

**依赖注入的原生方法：**

```java
public static void main(String[] args) {
    //通过set()方法注入
    Book book = new Book();
    book.setBname("java");
    book.setAuthor("程嘉伟");

    //通过构造器注入
    Book book1 = new Book("mysql","尚硅谷");
}
```

上面就是两种属性注入的方式。一种是使用set()方法去给对象中的属性设值；一种是使用构造器的方式，在构造对象的时候就给对象中的属性设值。

这两种方式，都是自己主动地去new一个对象，然后主动地去给对象中的属性设值的方式。

那有没有办法让IoC容器帮助我们完成对象属性的赋值，让我们直接就能够从容器中拿到有属性值的对象呢？答案是肯定的，以下就是两种类型的属性赋值方式。

##### 方式一：setter注入（使用较多）

setter注入，也就类似于使用set()方法去给创建的类对象设置对象的属性值。

所以，使用setter注入需要使用到类中相应属性的set()方法。

> **使用setter注入的前提是：`Java类中拥有属性对应的set()方法。`**
>
> 在spring配置文件中，使用bean标签下的**`property`标签**，来通过组件类的`setXxx()`方法给组件对象设置属性。

property标签中包含两个属性：`name`和`value`。

* name属性用来指定注入的属性名；

* value属性指定注入的属性值。

注意：对于property中的name属性来说，name属性所指定的类中的属性名，是由类中的setXxx()方法定义的，和成员变量无关。也就是说，类中无论是否有某个属性，例如name，只要类中包含setName()方法，就可以在property标签的name属性中定义。

**案例：**

创建一个类Book，在类中定义两个属性bname和author：

```java
package com.atguigu.spring6.ioc.xml.di;

public class Book {
    private String bname;
    private String author;

    //生成set方法
    public void setBname(String bname) {
        this.bname = bname;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
    //这里省略了get()方法，因为setter注入最重要的还是set()方法，所以我这里就只将set()方法写出来
}
```

然后，我们去xml文件中创建对象所对应的bean标签，这里我们去新建了一个新的xml文件：bean-di.xml。（名字可以随意取，只需要在ApplicationContext对象的创建时，使用这个文件进行创建即可）

在bean标签中，创建property标签，该property标签就表示类中的属性，property标签包含两个属性：name和value。name表示的是类的属性名，value表示创建出来的bean对象中属性的值。

```xml
<!--使用set方法注入-->
<bean id="book" class="com.atguigu.spring6.ioc.xml.di.Book">
    
    <property name="bname" value="MySQL由浅入深"></property>
    <property name="author" value="崔振华"></property>
    
</bean>
```

这样一来，类中属性的赋值也交给了IoC容器进行管理。

创建一个测试方法：

```java
@Test
public void test(){
    ApplicationContext context = new ClassPathXmlApplicationContext("bean-di.xml");
    
    Book book = context.getBean("book", Book.class);
    
    System.out.println("书的名称为：" + book.getBname());
    System.out.println("书的作者为：" + book.getAuthor());
}
```

执行结果：

![image-20240512140705128](.\images\image-20240512140705128.png) 





##### 方式二：构造注入

默认情况下，创建bean对象调用的是类中的无参构造器。

我们也可以使用类中的有参构造器创建bean对象，当然，如果使用构造注入的方式（使用有参构造器创建bean对象），**前提是在类中有对应的`有参构造器`**。

使用**`constructor-arg`**标签实现属性的注入。

`constructor-arg`标签中有三个主要属性，分别是`index`、`name`和`value`：

* index属性：表示有参构造器中的第几个参数（索引值）
* name属性：表示有参构造器中参数名
* value属性：表示给对应参数赋的值

在constructor-arg标签中，使用了index属性就可以不使用name属性，因为index属性本身就可以唯一标识出给构造器中哪个形参进行赋值；同理使用name属性就可以不使用index属性，name属性也可以唯一标识出构造器中的形参。（推荐使用name属性，不使用index属性来标识构造器中的形参，因为name可以做到见名知意）

**案例：**

创建类Book用于测试，类中创建两种构造器：

```java
package com.atguigu.spring6.ioc.xml.di;

public class Book {
    private String bname;
    private String author;

    public Book(String bname, String author) {
        System.out.println("有参构造器执行了.......");
        this.bname = bname;
        this.author = author;
    }

    public Book() {
        System.out.println("无参构造器执行了.......");
    }
    
    //省略getter与setter方法
}
```

为该类创建对应的bean标签，在bean标签中使用constructor-arg标签，表明在创建bean对象时，使用对应的有参构造器去创建（无constructor-arg标签表示使用默认的无参构造器创建bean对象）：

```xml
<bean id="bookCon" class="com.atguigu.spring6.ioc.xml.di.Book">
    
    <!--在constructor-arg标签中，可以使用name属性表示当前对应的是有参构造器中的哪一个形参，同时也能够使用index属性，用索引值的方式表示当前对应的是哪一个形参。但是推荐使用name属性，因为name属性可以做到见名知意-->
    <constructor-arg name="bname" value="Spring Boot实战"></constructor-arg>
    
    <constructor-arg index="1" value="Craig Walls"></constructor-arg>
</bean>
```

这样一来，使用构造器注入的方式就已经配置完毕了。

我们进行一下测试：

```java
@Test
public void testConstructor(){
    
    ApplicationContext context = new ClassPathXmlApplicationContext("bean-di.xml");
    
    Book book = context.getBean("bookCon", Book.class);
    
    System.out.println("书的名称为：" + book.getBname());
    System.out.println("书的作者为：" + book.getAuthor());
}
```

输出结果：

![image-20240512143544294](.\images\image-20240512143544294.png) 

由输出结果可知，以上是通过类中的有参构造器去创建的bean对象。





#### 2、特殊类型属性注入

##### 特殊值的处理

1. **`null值`**

当我们要给创建出来的bean对象中，某个属性设置为null时，可以使用`<null />`标签来进行设置。

这个`<null />`标签既可以使用在property标签中（setter注入），也可以使用在constructor-arg标签中（构造注入）。

例如：

```xml
<property name="name">
	<null />
</property>
```



案例：有一个Book类：

```java
package com.atguigu.spring6.ioc.xml.di;

public class Book {
    private String bname;
    private String author;
    private String others;
	
    //省略了getter、setter方法以及有参构造器
}
```

创建Book类的bean对象，其中others属性是null值。分别使用两种注入方式：

```xml
<!--使用set方法注入-->
<bean id="book" class="com.atguigu.spring6.ioc.xml.di.Book">
    <property name="bname" value="MySQL由浅入深"></property>
    <property name="author" value="崔振华"></property>
    <property name="others">
        <null />
    </property>
</bean>


<!--2、构造器注入-->
<bean id="bookCon" class="com.atguigu.spring6.ioc.xml.di.Book">
    <constructor-arg name="bname" value="Spring Boot实战"></constructor-arg>
    <constructor-arg index="1" value="Craig Walls"></constructor-arg>
    <constructor-arg name="others">
        <null />
    </constructor-arg>
</bean>
```



注意：

以下的写法是错误的：

```xml
<property name="name" value="null"></property>
```

这种写法，实际上是将name属性赋值成了"null"字符串的形式。



2. **`带有<>符号的值`**

在xml配置文件中，对于<>这两个符号来说，是用来定义标签的，所以如果在其中使用这两个符号去给属性进行赋值时，会报错。

比如：

<img src=".\images\image-20240512163528236.png" alt="image-20240512163528236" style="zoom:67%;" />

此时，可以使用转义字符来代替这两个符号。

* **`&lt;`**表示<小于号

* **`&gt;`**表示>大于号

例如：

```xml
<bean id="book" class="com.atguigu.spring6.ioc.xml.di.Book">
    
    <property name="bname" value="MySQL由浅入深"></property>
    <property name="author" value="崔振华"></property>
    <property name="others" value="&lt;ab&gt;"></property>
    
</bean>
```

此时，创建出来的Book的bean对象中，属性值打印结果为：

![image-20240512163855147](.\images\image-20240512163855147.png) 





3. **`使用CDATA节注入任意符号文本`**

CDATA节是XML文件中一种特有的写法。CDATA中的C代表Character，是文本、字符的含义，CDATA就表示纯文本数据。

XML解析器看到CDATA节就知道这里是纯文本，不会将其中的任意内容看作是XML标签或者属性来进行解析，所以在CDATA节中写什么符号都随意。主要是为了解决XML文件中，对于大于号>与小于号<解析错误问题。

**CDATA节的格式为：`<![CDATA[文本内容]]>`**

使用CDATA节，需要将CDATA节放入到标签体内，而不能放在标签的行属性中，否则会报错。

例如：

```xml
<property name="expression">
    <value><![CDATA[a你好 <> b]]></value>
</property>
```

此时，这里表示的value值就是："a你好 <> b"。



---

##### 对象类型属性注入

之前所学习的，都是一些基本数据的注入方式，假如在类中包含对象类型的属性时，该如何注入呢？接下来我们来介绍一下。

接下来，我们通过部门和员工两者之间的关系，来演示三种对象类型属性注入的方式。（这里我们使用的都是setter注入的方式，因为setter注入使用的比较多，也可以替换成使用构造注入）

**准备工作**

1、创建两个类，部门类与员工类：

部门与员工的关系是一对多，我们在员工类中，创建一个全局的部门对象属性，并调用该部门对象的info()方法：

部门类Dept：

```java
package com.atguigu.spring6.ioc.xml.ditest;

public class Dept {
    private String dname;

    public void info(){
        System.out.println("部门名称：" + dname);
    }
    //省略getter、setter方法
}
```

员工类Emp：

```java
package com.atguigu.spring6.ioc.xml.ditest;

public class Emp {
    //员工属于的部门
    private Dept dept;
    private String ename;
    private Integer age;

    public void work(){
        System.out.println(ename + "emp work......." + age);
        
        //调用全局属性的部门类对象info()方法
        dept.info();
    }
    //这里省略getter与setter方法
}
```



2、编写Spring配置文件

创建一个新的xml文件，bean-ditest.xml

<img src=".\images\image-20240512194613578.png" alt="image-20240512194613578" style="zoom: 50%;" /> 



###### 方式一：引用外部bean

**步骤：**

1. 创建所有类对应的bean标签
2. 在引用类的bean标签里，使用property标签声明对象类型的属性
3. 使用property标签的`ref`属性引入被引用类的bean

使用这种方式解决员工类中注入部门类对象：

```xml
<bean id="dept" class="com.atguigu.spring6.ioc.xml.ditest.Dept">
    <property name="dname" value="安保部"></property>
</bean>


<bean id="emp" class="com.atguigu.spring6.ioc.xml.ditest.Emp">
    <!--普通属性注入-->
    <property name="ename" value="lucy"></property>
    <property name="age" value="50"></property>
    <!--注入对象类型属性-->
    <property name="dept" ref="dept"></property>
</bean>
```

注意：这里引入外部的bean，使用的属性是ref，ref表示的是引入，传入的是**bean的id值**。

测试：

```java
@Test
public void testImportBean(){
    ApplicationContext context = new ClassPathXmlApplicationContext("bean-ditest.xml");
    
    Emp emp = context.getBean("emp", Emp.class);
    emp.work();
}
```

输出结果：

![image-20240512200710313](.\images\image-20240512200710313.png) 

由输出结果我们可以看到，Emp类中的ename和age属性成功注入，并且Emp类中Dept类的属性也是成功注入。



注意：**这里使用的是ref，而不是value**。如果错把ref属性写成了value属性，比如：`<property name="dept" value="dept"></property>`

此时在运行时会抛出异常：

Cannot convert value of type 'java.lang.String' to required type 'com.atguigu.spring6.ioc.xml.ditest.Dept' for property 'dept': no matching editors or conversion strategy found

意思是不能把String类型转换成我们想要的Dept属性，说明我们使用value属性时，Spring只会把这个属性看作是一个普通的字符串，不会认为这是一个bean的id，



###### 方式二：使用内部bean

内部bean的意思是，直接将要注入类的类型所对应的bean标签，声明在被注入类的bean标签中，而不是声明在外面通过ref属性引入。

内部bean的**说明：**

* 在一个bean中再声明一个bean，就是内部bean。

* 内部bean只能用于给属性赋值，不能在外部通过IOC容器获取，因此可以省略id属性。

**使用内部bean的方式注入部门类型属性：**

```xml
<bean id="emp" class="com.atguigu.spring6.ioc.xml.ditest.Emp">
    <!--普通属性注入-->
    <property name="ename" value="jerry"></property>
    <property name="age" value="25"></property>
    
    <!--使用内部bean的方式注入类类型属性-->
    <property name="dept">
        <bean class="com.atguigu.spring6.ioc.xml.ditest.Dept">
            <property name="dname" value="销售部"></property>
        </bean>
    </property>
    
</bean>
```

这种内部bean的方式，所创建的bean标签，只能用于当前的bean中给属性赋值，不能被其他bean使用，也不能通过IOC容器创建对象，所以这里的内部bean的id属性可以省略。

测试：

```java
ApplicationContext context = new ClassPathXmlApplicationContext("bean-ditest.xml");

Emp emp = context.getBean("emp", Emp.class);
emp.work();
```

执行结果：

![image-20240512201913767](.\images\image-20240512201913767.png) 

可以知道，三种属性都成功注入了。





###### 方式三：级联属性赋值

级联属性赋值，有点类似于引入外部bean，只不过对于这个外部bean注入的属性来说，可能不符合我们的要求。

比如，外部bean中dname属性值为安保部，但是我们现在要想创建的Emp对象中，Dept类对象属性的dname属性为HR部，此时如果直接引入外部bean，是不符合业务要求的，那么此时我们就可以使用级联属性的方式，对引入的外部bean中的属性进行修改。

**引入外部依赖后，使用`外部bean.属性名`的方式，实现对外部bean属性的注入。**

**案例**：使用级联属性完成对Emp类型中Dept类型属性的注入

```xml
<bean id="dept" class="com.atguigu.spring6.ioc.xml.ditest.Dept">
        <property name="dname" value="安保部"></property>
</bean>

<bean id="emp" class="com.atguigu.spring6.ioc.xml.ditest.Emp">
    <!--普通属性注入-->
    <property name="ename" value="谢娜"></property>
    <property name="age" value="29"></property>
    
    <!--使用级联赋值的方式注入对象类型属性-->
    <!--引入外部bean-->
    <property name="dept" ref="dept"></property>
    <property name="dept.dname" value="HR部"></property>
</bean>
```

测试：

```java
@Test
public void testImportBean(){
    ApplicationContext context = new ClassPathXmlApplicationContext("bean-ditest.xml");
    
    Emp emp = context.getBean("emp", Emp.class);
    emp.work();
}
```

执行结果：

![image-20240512203527192](.\images\image-20240512203527192.png) 

由执行结果可知：

级联属性赋值，将引入外部bean，并且继续对外部bean所包含的属性进行修改，会去覆盖原本外部bean中所声明的相关属性值。这样能够帮助我们解决一些由于外部bean中属性的值不符合要求的业务场景，能够让我们自由地对bean的属性进行扩展。

以上就是三种对象类型属性注入的方式。



---

##### 数组类型属性注入

可以直接看总结。

**准备工作**

为Emp员工类新增一个属性hobbies，该属性是String[]数组类型，表示的含义是员工的兴趣爱好。（其他的属性都是原本的）

```java
public class Emp {
    private Dept dept;
    private String ename;
    private Integer age;

    private String[] hobbies;
    
    public void work(){
        System.out.println(ename + "emp work......." + age);
        dept.info();
    }
    
    //...省略get、set以及构造器
}
```

还有一个部门类：

```java
public class Dept {
    private String dname;
    
    public void info(){
        System.out.println("部门名称：" + dname);
    }
    //...省略get、set以及构造器
}
```

然后我们去为其创建一个新的Spring配置文件用于数组类型属性注入的学习，就叫做bean-diarray.xml，在这个文件中，创建员工类与部门类的bean标签，使用引入外部bean的方式将部门的bean作为员工的属性dept注入（在对象类型属性注入中讲过）

```xml
<bean id="dept" class="com.atguigu.spring6.ioc.xml.ditest.Dept">
    <property name="dname" value="安保部"></property>
</bean>


<bean id="emp" class="com.atguigu.spring6.ioc.xml.ditest.Emp">
    <property name="ename" value="lucy"></property>
    <property name="age" value="20"></property>
    <!--对象类型的属性-->
    <property name="dept" ref="dept"></property>
</bean>
```

现在，对于Emp类来说，我们还剩下最后一个数组类型的属性hobbies没有注入，我们来说说数组类型的属性如何注入。

对于数组类型的属性，我们在xml文件中，使用`array`标签来表示，使用array标签就表示当前的数据是数组类型。

由于数组类型可能包含多条数据，所以，我们还需要在array标签中，使用`value`标签，来表示数组中的一个值，有多少个值，就使用多少个value标签。

所以，**数组类型属性注入的表示方式为：**

```xml
<array>
	<value></value>
    <value></value>
    <value></value>
</array>
```

那么，对于上例中的Emp员工类来说，将其hobbies属性使用IoC容器注入后，其bean标签例为：

```xml
<bean id="emp" class="com.atguigu.spring6.ioc.xml.ditest.Emp">
    <property name="ename" value="lucy"></property>
    <property name="age" value="20"></property>
    <!--对象类型的属性-->
    <property name="dept" ref="dept"></property>

    <!--数组类型的属性-->
    <property name="hobbies">
        <array>
            <value>吃饭</value>
            <value>睡觉</value>
            <value>打豆豆</value>
        </array>
    </property>
</bean>
```

测试：

```java
@Test
public void testArrayProperty(){
    ApplicationContext context = new ClassPathXmlApplicationContext("bean-diarray.xml");
    Emp emp = context.getBean("emp", Emp.class);
    emp.work(); 
    System.out.println(Arrays.toString(emp.getHobbies()));

}
```

输出结果：

![image-20240512231510790](.\images\image-20240512231510790.png) 





以上的数组类型，是String[]类型，可以直接使用字符串值来表示其中的元素。如果是引用数据类型，比如说User类，那么我们就不能使用value标签来表示其值，就需要使用引入外部bean的方式，引入外部的类对象，注入到数组的元素中。

例如：

在Dept类中，声明一个User[]数组类型的属性：

```java
public class Dept {
    private User[] users;
    //省略get()、set()方法
}
```

那么，在xml中，使用依赖注入的方式，将User[]数组属性值注入到bean对象中。

首先肯定是先去声明注入到对象数组中的元素的bean对象，然后才能使用ref标签，指明数组的元素来自于哪一个bean标签。xml配置如下所示：

```xml
<bean id="user1" class="com.atguigu.spring6.ioc.xml.User">
    <property name="name" value="tom"></property>
    <property name="age" value="24"></property>
</bean>
<bean id="user2" class="com.atguigu.spring6.ioc.xml.User">
    <property name="name" value="jerry"></property>
    <property name="age" value="25"></property>
</bean>
<bean id="user3" class="com.atguigu.spring6.ioc.xml.User">
    <property name="name" value="john"></property>
    <property name="age" value="26"></property>
</bean>


<bean id="dept2" class="com.atguigu.spring6.ioc.xml.ditest.Dept">
    <property name="users">
        <array>
            <ref bean="user1"></ref>
            <ref bean="user2"></ref>
            <ref bean="user3"></ref>
        </array>
    </property>
</bean>
```

此时，我们通过IoC容器所创建的dept对象中，就注入了User的数组属性。

测试：

```java
@Test
public void testArrayProperty(){
    ApplicationContext context = new ClassPathXmlApplicationContext("bean-diarray.xml");
    Dept dept = context.getBean("dept2", Dept.class);

    for (User user : dept.getUsers()) {
        System.out.println(user);
    }
}
```

输出结果：

![image-20240513011658146](.\images\image-20240513011658146.png) 



**总结**

对于数组类型属性的注入，如果是数组的类型是**普通数据类型**，比如基本数据类型或String，此时使用`value`标签来直接设置元素的值：

```xml
<bean id="emp" class="com.atguigu.spring6.ioc.xml.ditest.Emp">
    <!--普通类型的数组，比如这里是String[]-->
    <property name="hobbies">
        <array>
            <value>吃饭</value>
            <value>睡觉</value>
            <value>打豆豆</value>
        </array>
    </property>
</bean>
```

如果数组类型是除String之外的**引用数据类型**，比如User，此时使用`ref`标签，通过**引入外部bean标签**的方式设置元素的值：

```xml
<!--引入的是外部的bean，所以外部bean需要存在，这里我们去声明三个User类的bean对象-->
<bean id="user1" class="com.atguigu.spring6.ioc.xml.User">
    <property name="name" value="tom"></property>
    <property name="age" value="24"></property>
</bean>
<bean id="user2" class="com.atguigu.spring6.ioc.xml.User">
    <property name="name" value="jerry"></property>
    <property name="age" value="25"></property>
</bean>
<bean id="user3" class="com.atguigu.spring6.ioc.xml.User">
    <property name="name" value="john"></property>
    <property name="age" value="26"></property>
</bean>


<bean id="dept2" class="com.atguigu.spring6.ioc.xml.ditest.Dept">
    <property name="users">
        <array>
            <ref bean="user1"></ref>
            <ref bean="user2"></ref>
            <ref bean="user3"></ref>
        </array>
    </property>
</bean>
```







---

##### 集合类型属性注入

###### 1）List（Set）属性注入

**准备工作**

部门与员工之间的关系是一对多的关系，我们在员工类中，声明了一个部门类型的属性，表明当前员工属于哪一个部门的，同时也可以在部门类中声明一个List类型的属性empList，用来存储该部门下有哪些员工。

部门类：

```java
public class Dept {
    private String dname;
    
    private List<Emp> empList;
    
    public void info(){
        System.out.println("部门名称：" + dname);
        for (Emp emp : empList) {
            System.out.println(emp.getEname());
        }
    }
    //...省略get、set以及构造器
}
```

员工类：

```java
public class Emp {
    private String ename;
    private Integer age;
    //省略get()、set()方法
}
```

然后，创建相关配置文件bean-dilist.xml文件。



**List属性注入的说明**

**对于List属性的注入，类似于数组属性，也有类似于array标签的`标签list`，使用在property标签中，表示该属性为List属性。**

**由于`list`集合也可能有多条元素，所以也是类似于array标签，在其中使用`value`标签表示元素的值，或者使用`ref`标签引入外部bean的方式作为集合的元素**。即：

```xml
<list>
    <value></value>
    ...
</list>
或者
<list>
	<ref bean=""></ref>
    ...
</list>
```



**注入List属性的案例实现**

所以，对于上例部门类中的员工集合属性，使用依赖注入的方式是这样配置的：

```xml
<bean id="dept" class="com.atguigu.spring6.ioc.xml.ditest.Dept">
    <property name="dname" value="技术部"></property>
    <property name="empList">
        <list>
            <ref bean="emp1"></ref>
            <ref bean="emp2"></ref>
        </list>
    </property>
</bean>


<bean id="emp1" class="com.atguigu.spring6.ioc.xml.ditest.Emp">
    <property name="ename" value="lucy"></property>
    <property name="age" value="24"></property>
</bean>

<bean id="emp2" class="com.atguigu.spring6.ioc.xml.ditest.Emp">
   <property name="ename" value="jerry"></property>
    <property name="age" value="26"></property>
</bean>
```

由于部门类Dept中的List属性是Emp类型的，即该类型是一个引用类型。所以，我们需要使用bean标签首先对List中的元素初始化，然后再将初始化的引用类型元素注入到List属性中。所以，这里需要使用ref标签引入外部bean，而不是使用value标签。

测试：

```java
public static void main(String[] args) {
    ApplicationContext context = new ClassPathXmlApplicationContext("bean-dilist.xml");

    Dept dept = context.getBean("dept", Dept.class);
    dept.info();
}
```

执行结果：

![image-20240513111346196](.\images\image-20240513111346196.png) 

可以看见，成功将Emp的bean对象注入到了Dept类的empList集合属性中了。

如果Dept类中的List集合是String类型，比如：

```java
List<String> empNames;
```

此时在配置文件中，使用的就是value标签来注入集合中的元素。

```xml
<property name="empNames">
    <list>
        <value>tom</value>
        <value>john</value>
    </list>
</property>
```



> 若为Set集合类型属性赋值，只需要将其中的list标签修改为set标签即可，其他的都一样。







---

###### 2）map属性注入

**数据准备**

准备两个类：学生类Student与教师类Teacher，学生与教师之间的关系是多对多的关系，一个学生有多个老师，一个老师也有多个学生。

在学生类中，创建三个属性，分别是sid：表示学生的id（String类型），sname：表示学生的姓名（String类型）以及teacherMap：表示该学生的所有老师（Map类型），然后再创建一个run()方法输出学生类中的属性：：

```java
public class Student {
    private Map<String, Teacher> teacherMap;
    private String sid;
    private String sname;
    
    public void run(){
        System.out.println("学生编号：" + sid + "，学生名称：" + sname);
        System.out.println(teacherMap);
    }
    
    //get()、set()方法
}
```

在老师类中，创建两个属性teacherId与teacherName，分别表示老师的id与老师的名称：

```java
public class Teacher {
    private String teacherId;
    private String teacherName;
    //省略toString()与get()、set()方法
}
```

然后，再去创建spring的配置文件：bean-dimap.xml，用于配置bean对象。

这样的话，数据就准备完毕了。



**map属性注入说明**

map属性的注入使用`map`标签来实现

map标签中，每个元素都使用`entry`标签表示

entry标签中，包含key、value属性，如果key、value是普通的数据，则直接使用`key`、`value`标签即可；如果key、value是引用数据类型，则使用`key-ref`与`value-ref`标签。

如：

```xml
<map>
	<entry key="" value=""></entry>
    <entry key-ref="" value-ref=""></entry>
    或者：
    <entry>
    	<key>
            <value></value>
        </key>
        <ref bean=""></ref>
    </entry>
    ...
</map>
```



**map属性注入的案例实现**

那么，对于上述的例子，由于在Student类中的map属性是<String, Teacher>类型的，所以，对于value属性来说，我们需要通过导入外部bean的方式注入Teacher属性作为map的value值。

```xml
<bean id="teacher1" class="com.atguigu.spring6.ioc.xml.dimap.Teacher">
    <property name="teacherId" value="1"></property>
    <property name="teacherName" value="Mr.Cheng"></property>
</bean>
<bean id="teacher2" class="com.atguigu.spring6.ioc.xml.dimap.Teacher">
    <property name="teacherId" value="2"></property>
    <property name="teacherName" value="李老师"></property>
</bean>



<bean id="student" class="com.atguigu.spring6.ioc.xml.dimap.Student">
    <property name="sid" value="1"></property>
    <property name="sname" value="程嘉伟"></property>
    
    <!--注入map集合属性-->
    <property name="teacherMap">
        <map>
            <entry>
                <key>
                    <value>1</value>
                </key>
                <!--value是引入外部bean的方式-->
                <ref bean="teacher1"></ref>
            </entry>
            <entry key="2" value-ref="teacher2"></entry>
        </map>
    </property>
</bean>
```

测试：

```java
ApplicationContext context = new ClassPathXmlApplicationContext("bean-dimap.xml");

Student student = context.getBean("student", Student.class);
student.run();
```

执行结果：

![image-20240513122054858](.\images\image-20240513122054858.png) 

这样就将成功注入了map集合属性。

###### 小结

由上述两种方式可知，对于数组、集合来说，如果要将其注入到对象中，需要在`property`标签中再创建一个自身类型所对应的标签，并在其中使用`value`标签表示数字或集合中的一个元素。

**数组属性注入格式：**

```xml
<array>
	<value></value>
    ...
</array>
```

**List集合属性注入格式：**

```xml
<list>
	<value></value>
    ...
</list>
```

**map集合属性注入格式：**

```xml
<map>
	<entry key="" value=""></entry>
</map>
```

**set集合属性注入格式：**

```xml
<set>
	<value></value>
    ...
</set>
```

以上数组以及集合中，注入的都是普通的数据类型，比如基本数据类型或者String类型。

如果数组或集合中，需要注入的是类的类型对象时，则需要将value修改成ref，通过使用引入外部bean的方式注入即可。

如：

```xml
<array>
	<ref bean=""></ref>
    ...
</array>
```





###### 3）util命名空间的使用（引入外部数组集合）

有时候，对于数组、集合的属性，需要使用在多个地方。

在Java中，经常需要编译某一段相同的代码，那么我们就考虑将这一部分代码整合成一个方法，当我们使用时直接调用方法即可。

同理，在xml文件中，如果某一段数组或集合需要被多个bean注入，那么我们就考虑将property标签中所配置的数组或集合标签抽取出来，单独放在一块，供多个bean标签使用。

这个时候，就需要使用**`命名空间util`**来实现。



**使用util命名空间的集合格式：**

```xml
<util:集合类型>
    <value>(或<entry>)</value>
</util:集合类型>
```



**实现案例如下所示：**

```xml
<util:list id="nameList">
    <value>cheng</value>
    <value>jia</value>
    <value>wei</value>
</util:list>


<bean class="com.rudywork.IOCTest3.Person" id="person">
    <property name="names" ref="nameList" />
</bean>
```

如上，使用`util:`标签，让list集合声明在外部，而不是在property标签内。

然后我们在使用时，可以通过property标签中的`ref`属性，引入外部定义的list标签来实现属性的注入。

**`在使用util:标签之前，需要引入该命名空间`**，引入命名空间的方式请在案例中进行查看。



**案例数据准备**

创建课程类Lesson，给该类创建一个属性lessonName，用于表示课程的名称。

```java
package com.atguigu.spring6.ioc.xml.dimap;

public class Lesson {
    private String lessonName;
    //这里省略属性的get()、set()以及toString()方法
}
```

在学生类Student中，有一个全局属性lessonList，是List<Lesson>类型的，该属性用于表示该学生的所有课程；还有另外三个属性，分别是sid、sname以及tearcherMap，其中sid和sname为String类型，表示的是学生的id和姓名；teacherMap是Map<String, Teacher>类型，用于表示学生的所有老师。其中还有一个run()方法，该方法用于打印学生的信息。

```java
public class Student {
    //课程
    private List<Lesson> lessonList;
    //所有老师
    private Map<String, Teacher> teacherMap;
    private String sid;
    private String sname;

    public void run(){
        System.out.println("学生编号：" + sid + "，学生名称：" + sname);
        System.out.println(teacherMap);
        System.out.println(lessonList);
    }
    
	//省略get()、set()方法
}
```

最后还有一个教师类Teacher，该类中有两个属性，分别是teacherId和teacherName：

```java
public class Teacher {
    private String teacherId;
    private String teacherName;
    //省略了get()、set()以及toString()方法
}
```

创建完三个类之后，我们再去创建一个Spring的xml文件`bean-diref.xml`。在xml文件中，我们去将这三个类的bean对象先创建出来，然后注入普通类型的属性：（由于Student类中需要注入List类型的Lesson，以及Map类型的teacher，所以lesson类和teacher类的bean对象我就多创建几个）

```xml
<!--学生bean-->
<bean id="student" class="com.atguigu.spring6.ioc.xml.dimap.Student">
    <property name="sid" value="1"></property>
    <property name="sname" value="lucy"></property>
</bean>

<!--教师bean-->
<bean id="teacherOne" class="com.atguigu.spring6.ioc.xml.dimap.Teacher">
    <property name="teacherId" value="10001"></property>
    <property name="teacherName" value="李老师"></property>
</bean>
<bean id="teacherTwo" class="com.atguigu.spring6.ioc.xml.dimap.Teacher">
    <property name="teacherId" value="10002"></property>
    <property name="teacherName" value="王老师"></property>
</bean>


<!--课程bean-->
<bean id="lessonOne" class="com.atguigu.spring6.ioc.xml.dimap.Lesson">
    <property name="lessonName" value="java"></property>
</bean>
<bean id="lessonTwo" class="com.atguigu.spring6.ioc.xml.dimap.Lesson">
    <property name="lessonName" value="vue"></property>
</bean>
```

这样，案例的数据就准备好了。



**实现步骤案例：**

1. **`引入命名空间util，并且声明util命令空间遵循的标准`**

我们要使用命名空间util，就需要先引入命名空间。

```xml
xmlns:util="http://www.springframework.org/schema/util"
```

在xml文件中，引入命令空间的命令如上所示，将上述代码添加到beans标签中。

引入了命令空间，我们还需要去声明两段代码，规定util命名空间所遵循的标准：

```xml
http://www.springframework.org/schema/util
http://www.springframework.org/schema/util/spring-util.xsd
```

将这两段代码添加到beans标签中的xsi:schemaLocation属性上。

最终得到的xml文件中beans标签结构是：

![image-20240513134743062](.\images\image-20240513134743062.png)

2. **`写外部集合代码`**

将要注入属性的集合，使用`<util:集合>`标签的方式，声明在bean标签的外部。

在这些标签中，可以通过使用ref标签引入外部的bean作为集合中的元素，同时也可以使用value标签来手动设置元素值：

```xml
<util:list id="lessonList">
    <ref bean="lessonOne"></ref>
    <ref bean="lessonTwo"></ref>
</util:list>

<util:map id="teacherMap">
    <entry>
        <!--以下是entry标签的两种写法-->
        <key>
            <value>10001</value>
        </key>
        <ref bean="teacherOne"></ref>
    </entry>
    <entry key="10002" value-ref="teacherTwo"></entry>
</util:map>
```



3. **`在bean中的property标签中，使用ref属性引入外部集合`**

声明完外部的数组或集合标签后，通过使用ref属性，填写标签中的id属性，从而引入外部所设置的集合，达到注入集合属性的目的。

所以，Student的bean标签引入上述两个集合的配置是：

```xml
<bean id="student" class="com.atguigu.spring6.ioc.xml.dimap.Student">
    <property name="sid" value="1"></property>
    <property name="sname" value="lucy"></property>
    <property name="teacherMap" ref="teacherMap"></property>
    <property name="lessonList" ref="lessonList"></property>
</bean>
```

这样一来，就将外部的list注入到属性中了。

测试：

```java
@Test
public void testDiRef(){
    ApplicationContext context = new ClassPathXmlApplicationContext("bean-diref.xml");
    Student student = context.getBean("student", Student.class);
    student.run();
}
```

执行结果：

![image-20240513145501474](.\images\image-20240513145501474.png)

此时，就完成了通过外部标签的方式完成了集合属性的注入。





---

#### 3、p命名空间的使用

**引入p命名空间**

在spring的配置xml文件中，在beans标签中，添加下面的属性，表示引入p命名空间：

```xml
xmlns:p="http://www.springframework.org/schema/p"
```

即：

![image-20240513150118230](.\images\image-20240513150118230.png)

此时就引入了p命名空间。



**p命名空间的作用**

p命名空间，能够让我们在bean标签中，通过**`p:属性名`**的方式来完成属性的注入。

例如：

```xml
<bean id="studentp" class="com.atguigu.spring6.ioc.xml.dimap.Student"
p:sid="100" p:sname="marry" p:lessonList-ref="lessonList" p:teacherMap-ref="teacherMap"></bean>
```

如果是直接使用数值进行赋值，直接使用 `p:属性名` 即可；

如果是使用外部的标签来对属性进行赋值时，使用 `p:属性名-ref` 的方式引入外部标签。

测试一下：

```java
@Test
public void testDiRef(){
    ApplicationContext context = new ClassPathXmlApplicationContext("bean-diref.xml");
    Student student = context.getBean("studentp", Student.class);
    student.run();
}
```

执行结果：

![image-20240513150742427](.\images\image-20240513150742427.png) 

结果是正确的。







---

### XML中引入外部配置文件

**为什么要在XML中引入外部配置文件？**

在开发中，我们在一个XML配置文件里，可能会配置很多类、很多东西，有一些东西是写死的，是固定的，需要在多个配置文件中重复地书写与使用。比如说JDBC操作数据库的数据库驱动连接。其中，数据库地址、账号密码、端口号等这些基本上都是写死的，我们可以将这些东西放在一个配置文件里，让XML文件去读取这个配置文件，这样的话就能够减少冗余代码。

同时，如果要去修改某个配置的配置信息，那么我们就直接去配置文件中进行修改，这样修改后所有引用该配置的xml文件都能够得到修改，而不需要再进入到每个xml文件中对配置进行修改。

这就是XML中要引入外部配置文件的需求所在。



**实现案例（这里引入的是德鲁伊数据库连接池配置文件）**

原本，我们在xml配置文件中，配置druid数据库连接池是这样的：

```xml
<!-- 直接配置连接池-->
<bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
    <property name="driverClassName" value="com.mysql.cj.jdbc.Driver" />
    <property name="url" value="jdbc:mysql://localhost:3306/ssm" />
    <property name="user" value="root" />
    <property name="password" value="061535asd" />
</bean>
```

接下来，我们就将druid数据库连接池的配置存放在单独的properties文件中，通过在xml文件中引入该properties文件的方式，实现连接池的配置。

**引入外部配置文件的步骤：**

1. 首先，我们需要先引入数据库相关依赖

```xml
 <!-- MySQL驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.30</version>
</dependency>

<!-- 数据源 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.2.15</version>
</dependency>
```



2. 创建外部属性文件，properties格式，定义数据信息：用户名、密码、地址等。

创建外部属性文件：jdbc.properties

![images](.\images\img010.png) 

然后，在配置文件中，配置数据库的相关信息：

```properties
jdbc.user=root
jdbc.password=061535asd
jdbc.url=jdbc:mysql://localhost:3306/ssm
jdbc.driverClassName=com.mysql.cj.jdbc.Driver
```



3. 创建xml配置文件，引入context命名空间，引入外部属性文件，使用表达式完成注入

创建一个Spring的xml配置文件，进行测试，例如测试文件名为bean-import.xml。

<img src=".\images\image-20240513170710241.png" alt="image-20240513170710241" style="zoom: 50%;" /> 

然后在配置文件中，引入context名称空间。

引入context名称空间的方式：在beans标签中添加xmlns:context属性：

```xml
xmlns:context="http://www.springframework.org/schema/context"
```

并在xsi:schemaLocation属性中声明context空间的约束：

```xml
http://www.springframework.org/schema/context
http://www.springframework.org/schema/context/spring-context.xsd
```

此时，Spring的xml文件的beans标签如下所示：

<img src=".\images\image-20240513171036743.png" alt="image-20240513171036743" style="zoom: 67%;" />

然后，通过**`<context:property-placeholder location="classpath:文件名" />`**的方式去引入外部配置文件。

这里我们引入外部的druid数据库连接池的配置文件：

```xml
<context:property-placeholder location="classpath:jdbc.properties" />
```

这样一来，我们在xml配置文件中，就可以使用指定表达式的方式，引入文件中的属性了。

表达式：**`${val}`**

```xml
<bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
    <property name="driverClassName" value="${jdbc.driverClassName}" />
    <property name="url" value="${jdbc.url}" />
    <property name="username" value="${jdbc.user}" />
    <property name="password" value="${jdbc.password}" />
</bean>
```

那么，完整的xml配置文件就如下所示：

<img src=".\images\image-20240513173319099.png" alt="image-20240513173319099" style="zoom:67%;" />

以上，通过引入外部配置文件的方式注入属性就演示完毕了，我们接下来测试一下。

测试：

```java
@Test
public void testJdbc(){
    ApplicationContext context = new ClassPathXmlApplicationContext("bean-import.xml");

    DruidDataSource druidDataSource = context.getBean("druidDataSource", DruidDataSource.class);
    
    System.out.println(druidDataSource.getUrl());
    
}
```

输出结果：

![image-20240513173348598](.\images\image-20240513173348598.png) 

由此，我们就可以知道，引入外部文件的方式成功注入属性。



---

### bean的作用域与生命周期

#### bean的作用域

**概念**

在Spring中可以通过配置bean标签的**`scope属性`**来指定bean的作用域范围，各取值含义如下表：

| 取值                | 含义                        | 创建对象的时机  |
| ------------------- | --------------------------- | --------------- |
| `singleton`（默认） | 该bean的对象始终为单实例    | IOC容器初始化时 |
| `prototype`         | 该bean在IOC容器中有多个实例 | 获取bean时      |

如果是在WebApplicationContext环境下还会有几个作用域（但不常用）：

| 取值    | 含义                 |
| ------- | -------------------- |
| request | 在一个请求范围内有效 |
| session | 在一个会话范围内有效 |



**单实例与多实例作用域的演示**

**数据准备**

创建一个Orders类用于测试

```java
package com.atguigu.spring6.ioc.xml.scope;

public class Orders {}
```

创建Spring的xml文件bean-scope.xml，为Orders类创建bean对象：

```xml
<!--通过scope属性配置单实例还是多实例-->
<bean id="orders" class="com.atguigu.spring6.ioc.xml.scope.Orders">
</bean>
```

创建测试类，使用IoC容器创建两个Orders类的对象进行测试。

```java
public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean-scope.xml");

        Orders orders1 = context.getBean("orders", Orders.class);
        System.out.println(orders1);
        
        Orders orders2 = context.getBean("orders", Orders.class);
        System.out.println(orders2);
    
        System.out.println(orders1 == orders2);
    }
```

**测试1**

当bean的scope属性为singleton时（也可以不去显式的生命scope，默认scope属性就是singleton）：

```xml
<bean id="orders"
class="com.atguigu.spring6.ioc.xml.scope.Orders"
scope="singleton">
</bean>
```

此时测试的输出结果为：

![image-20240514115530307](.\images\image-20240514115530307.png)

我们可以看到，创建的两个对象地址值是一样的，即通过两次getBean()获取到的Orders对象是同一个。

在日志信息中，有这样一句话：Creating shared instance of singleton bean 'orders'，表示创建单例bean的orders共享实例，即创建出来的是单例对象。

创建单例对象的日志信息，是在调用getBean()方法之前的，也就是在

ApplicationContext context = new ClassPathXmlApplicationContext("bean-scope.xml");

打印的日志信息，由此我们可以得知该单例bean对象是在初始化IoC容器时进行创建的。

即：

**scope属性为`singleton`时，创建的bean对象是单例对象，并且在IOC容器初始化时进行创建。**



**测试2**

当scope属性为prototype时：

```xml
<bean id="orders"
class="com.atguigu.spring6.ioc.xml.scope.Orders"
scope="prototype">
</bean>
```

此时，我们再去执行测试方法，所得到的结果是：

![image-20240514115621417](.\images\image-20240514115621417.png)

创建出来的两个对象的地址值是不同的，所以这两个对象并不是同一个对象。

并且，在上述结果中，不包含使用singleton时的日志信息：Creating shared instance of singleton bean 'orders'，也就是说，这些对象的创建并不是在IoC初始化时进行创建的，而是在获取bean对象时进行创建。

即：

**scope属性为`prototype`时，创建的bean对象是多实例对象，并且在获取bean时进行创建。**











---

#### bean的生命周期

> **bean的具体生命周期**
>
> 1. bean对象创建（调用无参构造）
> 2. 给bean对象设置相关属性（依赖注入）
>
> 3. 调用bean前置处理器（初始化前）
>
> 4. bean对象初始化（调用指定初始化方法）
> 5. 调用bean后置处理器（初始化后）
> 6. bean对象创建完成，可以使用
> 7. bean对象销毁（配置指定销毁的方法）
> 8. IoC容器关闭

**初始化指的是为数据对象或变量赋初值的做法**。我们调用setXxx()方法去设置属性，这个值实际上是修改的属性值；对于某些属性来说，有一个初始的值，比如age年龄，我们可以设置初始值为10；email，我们可以设置初始值是"xxx@qq.com"。而将这些属性设值初始值的过程，就叫做初始化。

所以，在去创建bean对象，并注入属性值后，我们还需要去进行初始化的操作，因为注入属性值的操作并不是给属性进行初始化的操作，而是修改操作，有些属性还需要一些默认初始值。



**bean生命周期的验证**

创建User类，User类中声明name属性，并声明无参构造器，以及initMethod()和destroyMethod()方法，这两个方法分别表示初始化与销毁的方法：

```java
package com.atguigu.spring6.ioc.xml.life;

public class User {
    private String name;

    //无参构造
    public User(){
        System.out.println("1 调用无参构造创建bean对象");
    }


    public void setName(String name) {
        System.out.println("2 给bean对象设置属性值");
        this.name = name;
    }

    //初始化的方法
    public void initMethod(){
        System.out.println("4 调用指定的方法进行bean初始化");
    }

    //销毁的方法
    public void destroyMethod(){
        System.out.println("7 bean对象的销毁，调用指定的销毁方法");
    }
}
```

创建Spring的配置文件:bean-life.xml，并在其中配置bean：

```xml
<bean id="user"
      class="com.atguigu.spring6.ioc.xml.life.User"
      init-method="initMethod"
      destroy-method="destroyMethod">
    <property name="name" value="lucy"></property>
</bean>
```

在bean的标签中，包含`init-method`和`destroy-method`两个属性，这两个属性分别表示初始化bean对象以及销毁bean对象时，所调用的类中的哪个方法。

测试：

```java
@Test
public void testBeanLife(){
    //注意这里使用的是ClassPathXmlApplicationContext类接收，因为ClassPathXmlApplicationContext类中有close()方法，去关闭IoC容器，销毁bean对象，但ApplicationContext类中没有close()方法
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("bean-life.xml");

    User user = context.getBean("user", User.class);

    System.out.println("6 bean对象创建完成，可以使用bean对象");
    System.out.println(user);
    
    context.close();
}
```

输出结果：

<img src=".\images\image-20240514133201909.png" alt="image-20240514133201909" style="zoom:67%;" />

以上这些过程中，是bean生命周期的基本过程，但是缺少了bean对象创建时，前置处理器与后置处理器的执行。接下来，我们介绍一下，在bean创建时，前置处理器与后置处理器的执行。

**bean的前置处理器与后置处理器**

bean的前置处理器与后置处理器会在生命周期的初始化前后添加额外的操作，需要**实现`BeanPostProcessor`接口，并且配置到IOC容器中**。

通过实现BeanPostProcessor接口，并重写其中的`postProcessBeforeInitialization()`（前置处理器）与`psotProcessAfterInitialization()`（后置处理器）来实现bean创建的前置处理器与后置处理器。

**需要注意的是，bean的前后置处理器并不是单独针对某一个bean生效的，而是针对IOC容器中所有bean都会执行。**

案例：

创建bean的前后置处理器：

```java
public class MyBeanProcessor implements BeanPostProcessor {
   
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("3 调用bean的前置处理器");
        
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("5 调用bean后置处理器");
        
        return bean;
    }
}
```

然后，我们需要将该前后置处理器的类配置到IoC容器中：

```xml
<bean id="processor" class="com.atguigu.spring6.ioc.xml.life.MyBeanProcessor">
</bean>
```

这样一来，每次去加载该前后置处理器的bean所在的xml文件时，都会将该bean进行加载，从而在创建其他bean时，都会去自动执行该前后置处理器类中重写的两个表示前置与后置处理器的方法。

测试：

```java
@Test
public void testBeanLife(){
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("bean-life.xml");

    User user = context.getBean("user", User.class);

    System.out.println("6 bean对象创建完成，可以使用bean对象");
    System.out.println(user);
    context.close();

}
```

输出结果：

<img src=".\images\image-20240514134351068.png" alt="image-20240514134351068" style="zoom:67%;" />









---

### FactoryBean

Spring中有两种Bean，一种是普通Bean，另一种是工厂Bean，即FactoryBean。FactoryBean跟普通Bean不同，其返回的对象不是指定类的一个实例，而是该FactoryBean的`getObject()`方法所返回的对象。

**为什么要使用FactoryBean？即FactoryBean的作用是什么？**

一般情况下，Spring通过反射机制利用bean标签中的class属性指定实现类实例化Bean。但是在某些情况下，实例化Bean的过程比较复杂，如果按照传统的方式，则需要在bean标签中提供大量的配置信息，配置方式的灵活性是受限的。如果此时采用编码的方式可能会得到一个简单的方案。Spring为此提供了org.springframework.beans.factory.FactoryBean的工厂类接口，用户可以通过实现该接口定制实例化Bean的逻辑。FactoryBean接口对于Spring框架来说占用重要的地位，Spring自身就提供了70多个FactoryBean的实现。它们隐藏了实例化一些复杂Bean的细节，给上层应用带来了便利。从Spring3.0开始，FactoryBean开始支持泛型，即接口声明改为FactoryBean<T>的形式。

以Bean结尾，表示它是一个Bean，不同于普通Bean的是，它是实现了FactoryBean<T>接口的Bean，根据该Bean的ID从BeanFactory中获取的时机上是FactoryBean的getObject()返回的对象。

**FactoryBean接口定义**

```java
package org.springframework.beans.factory;

import org.springframework.lang.Nullable;

public interface FactoryBean<T> {

    @Nullable
    T getObject() throws Exception;

    @Nullable
    Class<?> getObjectType();

    default boolean isSingleton() {
        return true;
    }
}
```

**应用场景**

FactoryBean通常是用来创建比较复杂的bean，一般的bean直接用xml配置即可，但如果一个bean创建过程中涉及到很多其他的bean和复杂的逻辑，用xml配置比较困难，这时可以考虑用FactoryBean。

我们整合MyBatis时，Spring就是通过FactoryBean机制来帮助我们创建SqlSessionFactory对象的。

```xml
<!-- spring和MyBatis整合，不需要mybatis的配置映射文件 -->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <!-- 自动扫描xml文件 -->
    <property name="mapperLocations" value="classpath:mapper/*.xml"></property>
</bean>
```

在SqlSessionFactoryBean中，实际上获取bean的方法是：

```java
public class SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean, ApplicationListener<ApplicationEvent> {
    private static final Log LOGGER = LogFactory.getLog(SqlSessionFactoryBean.class);
...
public SqlSessionFactory getObject() throws Exception {
        if (this.sqlSessionFactory == null) {
            this.afterPropertiesSet();
        }
 
        return this.sqlSessionFactory;
    }
...
}
```

这个SqlSessionFactory对象在该类中经过了很多复杂的操作，所以使用FactoryBean的方式来创建比使用xml配置更加方便。

**使用案例**

创建User类

```java
public class User{}
```

创建MyFactoryBean类，该类实现FactoryBean接口，FactoryBean接口的泛型是User，表示创建出来的bean对象是User类型的。

实现FactoryBean接口，需要重写getObject()与getObjectType()方法，由于通过FactoryBean创建的对象是User类型的，所以getObject()返回的是User对象，getObjectType()方法返回的Class类型也是User.class。（实际通过FactoryBean创建的对象逻辑肯定更加复杂，这里我们简化进行测试）

```java
public class MyFactoryBean implements FactoryBean<User> {
    @Override
    public User getObject() throws Exception {
        return new User();
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }
}
```

然后，在xml配置文件中，对FactoryBean进行配置：

```xml
<bean id="user" class="com.atguigu.spring6.ioc.xml.factorybean.MyFactoryBean"></bean>
```

测试：

```java
ApplicationContext context = new ClassPathXmlApplicationContext("bean-factorybean.xml");

User user = (User)context.getBean("user");

System.out.println(user);
```

注意，这里使用的getBean(id)方法，使用的是仅通过bean标签的id获取，而不能使用getBean(Class)或者getBean(id, Class)的方式获取bean对象，否则就只能使用Class所表示的类进行接收从而报错。

执行结果：

![image-20240514144111647](.\images\image-20240514144111647.png) 

即成功通过FactoryBean的方式，获取getObject()方法中所设置的类。



---

### 基于xml自动装配

之前，我们在向bean对象中，使用property标签（set注入）或constructor-arg标签（构造注入）的方式，为类对象注入属性值，这种方式叫做**手动装配**。

> 那什么是**自动装配**呢？就是根据指定的策略，在IOC容器中匹配某一个bean，自动为指定的bean中所依赖的类类型或接口类型属性赋值。

**实现方式：**

使用bean属性**`autowire`**实现

autowire属性有两种值：

* `byName`：根据属性名称注入
* `byType`：根据属性类型注入

> **注意：**
>
> 这里虽然是使用了autowire属性实现了自动装配，但是，我们还是需要在类中给自动装配的属性创建**`set()方法`**，因为使用xml的方式实现自动装配，实际上还是调用set()方法实现的设置。

#### 场景模拟

我们开发中有下面几个部分：

![image-20240514145602143](.\images\image-20240514145602143.png)

我们需要在controller层中注入service层，在service层注入dao层。我们使用自动装配的方式来进行实现。那么我们先去准备一下数据。

**数据准备**

创建三个包，分别是controller、service以及dao包。

在controller包中创建UserController类。

在service包中创建UserService接口、UserImpl类。

在Dao包中创建UserDao接口、UserDaoImp类。

在Controller层需要使用Service层中的对象，并调用Service层中的方法；在Service层需要使用dao层的对象，并调用Dao层中的方法。我们一般是将Service层对象作为Controller类中的属性，将Dao层对象作为Service类中的属性，方便进行调用。

现在，我们通过Spring的配置文件来实现，并且通过自动装配将类对象作为属性注入bean中。

所以，我们需要在Controller类与ServiceImpl类中，分别去创建UserService与UserDao的对象属性，并让其使用自动装配的方式（实际底层调用的也是set()方法），注入到Controller与ServiceImpl类的bean对象中进行调用。然后，我们就可以在Controller中使用Service层中的方法，在Service层使用Dao层中的方法。

故最终，这几个接口或类的代码为：

UserController

```java
public class UserController {

    private UserService userService;
    
    public void addUser(){
        System.out.println("controller方法执行了");
        //调用service层的方法
        userService.addUserService();
    }

    //需要set()方法
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
```

UserService

```java
public interface UserService {
    public void addUserService();
}
```

UserServiceImpl

```java
public class UserServiceImpl implements UserService{
    private UserDao userDao;
    
    @Override
    public void addUserService() {
        System.out.println("userService方法执行了");
        //调用dao层中的方法
        userDao.addUserDao();
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
```

UserDao

```java
public interface UserDao {
    public void addUserDao();
}
```

UserDaoImpl

```java
public class UserDaoImpl implements UserDao{
    @Override
    public void addUserDao() {
        System.out.println("userDao方法执行了...");
    }
}
```

创建完成之后，我们再继续创建xml文件，创建spring的xml文件，并在xml文件中声明三个bean标签，分别是UserController类、UserServiceImpl类以及UserDaoImpl类的bean标签。

（Service层、Dao层实际上起作用都是Impl实现类对象，调用的实际也是实现类对象，比如UserController中的UserService属性其实也是实现类对象，只不过是用UserService接收）

```xml
<bean id="userController" class="com.atguigu.spring6.ioc.xml.auto.controller.UserController"></bean>

<bean id="userService" class="com.atguigu.spring6.ioc.xml.auto.service.UserServiceImpl"></bean>

<bean id="userDao" class="com.atguigu.spring6.ioc.xml.auto.dao.UserDaoImpl"></bean>
```

这样的话，我们数据就准备完毕了。

**实现自动装配**

> 使用bean标签的属性：**`autowire`**设置自动装配效果
>
> autowire有两种类型的取值，分别为byType和byName。
>
> 自动装配方式1：`byType`
>
> 表示根据类型匹配IOC容器中某个兼容类型的bean，为属性自动赋值。
>
> 注意这里的byType并不是要求bean的class属性必须要和自动装配类中的属性保持一致才可以，只需要兼容。兼容指的是也可以向下兼容，比如属性是A类型，兼容A类型的bean以及A的子类型bean。
>
> 若在IOC中，没有任何一个兼容类型的bean能够为属性赋值，则该类型不匹配，则值为默认值null
>
> 若在IOC中，有多个兼容类型的bean能够为属性赋值，则抛出异常NoUniqueBeanDefinitionException。

实现：

```xml
<bean id="userController" class="com.atguigu.spring6.ioc.xml.auto.controller.UserController" autowire="byType"></bean>

<bean id="userService" class="com.atguigu.spring6.ioc.xml.auto.service.UserServiceImpl" autowire="byType"></bean>

<bean id="userDao" class="com.atguigu.spring6.ioc.xml.auto.dao.UserDaoImpl"></bean>
```

此时，就实现了将UserDaoImpl类型的bean自动为UserServiceImpl中的UserDao属性赋值；将UserServiceImpl类型的bean自动为UserController中的UserService属性赋值。接口与其实现类之间是兼容的。

测试：

``` java
@Test
public void test(){
    ApplicationContext context = new ClassPathXmlApplicationContext("bean-auto.xml");

    UserController userController = context.getBean("userController", UserController.class);
    
    userController.addUser();
}
```

![image-20240514155424449](.\images\image-20240514155424449.png) 



> 自动装配方式二：`byName`
>
> byName要求进行自动装配的属性名，与在IOC容器中匹配bean的id进行相应的自动赋值。
>
> 即通过byName进行自动装配，会将bean中的id值与类中属性值进行自动匹配赋值。

测试：

```xml
<bean id="userController" class="com.atguigu.spring6.ioc.xml.auto.controller.UserController" autowire="byName"></bean>

<bean id="userServiceImpl" class="com.atguigu.spring6.ioc.xml.auto.service.UserServiceImpl" autowire="byName"></bean>

<bean id="userDaoImpl" class="com.atguigu.spring6.ioc.xml.auto.dao.UserDaoImpl"></bean>
```

```java
@Test
public void test(){
    ApplicationContext context = new ClassPathXmlApplicationContext("bean-auto.xml");

    UserController userController = context.getBean("userController", UserController.class);
    
    userController.addUser();
}
```

执行结果：

![image-20240514160155315](.\images\image-20240514160155315.png)

此时会报错，因为在UserController中，属性名为userService，但是实际在xml中所配置的UserServiceImpl的bean对象id为userServiceImpl，id与属性名不匹配，使用byName自动装配无法找到对应的属性，故报错；同时，UserServiceImpl中UserDao的属性名为userDao，而xml文件中UserDaoImpl类的bean对象id为userDaoImpl，与属性名不匹配，所以也无法使用byName的方式进行自动装配。

我们需要将xml中id值进行修改：

```xml
<bean id="userController" class="com.atguigu.spring6.ioc.xml.auto.controller.UserController" autowire="byName"></bean>

<bean id="userService" class="com.atguigu.spring6.ioc.xml.auto.service.UserServiceImpl" autowire="byName"></bean>

<bean id="userDao" class="com.atguigu.spring6.ioc.xml.auto.dao.UserDaoImpl"></bean>
```

此时就能够实现自动装配了：

![image-20240514160630824](.\images\image-20240514160630824.png) 

以上就是xml自动装配的全部过程。





---

## 3、基于注解管理bean（:star:）

从Java 5开始，Java增加了对注解（Annotation）的支持，它是代码中的一种特殊标记，可以在编译、类加载和运行时类读取，执行相应的处理。开发人员可以通过注解在不改变原有代码和逻辑的情况下，子啊源代码中嵌入补充信息。

Spring从 2.5 版本开始提供了对注解技术的全面支持，我们可以使用注解来实现自动装配，简化Spring的XML配置。

> Spring通过注解实现自动装配的步骤如下：
>
> 1. 引入依赖
> 2. 开启组件扫描
> 3. 使用注解定义Bean
> 4. 依赖注入

在实际开发中，我们都是使用注解来进行实现的。

### 环境搭建

#### 创建模块并引入依赖

创建模块spring6-ioc-annotation：

<img src=".\images\image-20240514163513123.png" alt="image-20240514163513123" style="zoom: 50%;" /> 

![image-20240514163806439](.\images\image-20240514163806439.png) 

引入相关依赖，这里我们引入的依赖包括spring-context、junit以及log4j2的相关依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>6.0.6</version>
    </dependency>

    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.3.1</version>
    </dependency>


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

其中，log4j2是日志，这个日志有无对spring注解开发没有什么影响，只不过一般在项目里面都有这个日志，所以我引入了。

log4j2日志的使用还需要再在resources目录下，创建一个log4j2.xml的配置文件（文件名称是固定的），配置信息如下所示，直接复制即可

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

        <!--文件会打印出所有信息，这个log每次运行程序会自动清空，由append属性决定，适合临时测试用-->
        <File name="log" fileName="d:/spring6_log/test.log" append="false">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} %-5level %class{36} %L %M - %msg%xEx%n"/>
        </File>

        <!-- 这个会打印出所有的信息，
            每次大小超过size，
            则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，
            作为存档-->
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





### 开启组件扫描功能

Spring默认不使用注解装配Bean，因此我们需要在Spring的XML配置中，通过**`context:component-scan`**元素开启Spring Beans的自动扫描功能。

开启此功能后，Spring会自动从扫描指定包及其子包下所有类，如果类上使用的相关的注解，就将该类装配到IoC容器中。

**开启组件扫描功能步骤：**

**1、引入context命名空间**

创建Spring的xml配置文件

<img src=".\images\image-20240514175511017.png" alt="image-20240514175511017" style="zoom: 50%;" /> 

在beans标签中添加属性：

```xml
xmlns:context="http://www.springframework.org/schema/context"
```

**2、添加context命名空间的约束**

在beans标签中的xsi:schemaLocation属性中添加两条context命名空间的约束：

```
http://www.springframework.org/schema/context
http://www.springframework.org/schema/context/spring-context.xsd
```

这样一来，context命名空间的引入就算完成了。

<img src=".\images\image-20240514165733038.png" alt="image-20240514165733038" style="zoom: 50%;" />

**3、开启组件扫描**

在Spring的xml文件中，创建`context:component:scan`标签，在标签的`base-package`属性中，指定要扫描的包。指定的包以及其子包中，所有配置了相关注解的类都将装配到IoC容器中。

比如，指定com.atguigu包及其子包下，所有的使用相关注解的类都注入到IoC容器中：

```xml
<context:component-scan base-package="com.atguigu" />
```

此时，当我们去给该xml文件进行IoC容器初始化时，就会去自动扫描com.atguigu包及其子包下所有的类，将声明了相关注解的类装配到IoC容器中。



就比如，我在com.atguigu.dao中创建了一个类User，在类上声明了注解@Component：

```java
@Component
public class User {
}
```

此时在配置文件中使用组件扫描，扫描了这个包下，在初始化IoC容器时，就会将该类装配到IOC容器中管理。

如果在该类中还包含属性，比如Dept属性，并且该属性使用了@Autowired：

```java
@Component
public class User {
	@Autowired
	private Dept dept;
}
```

就会使用自动装配的方式，自动为该类注入Dept的属性。



**注解扫描的配置有三种情况**

**情况一：最基本的扫描方式**

这种情况也就是上面所说的方式，扫描指定包及其子包。

```xml
<context:component-scan base-package="com.atguigu.spring6">
</context:component-scan>
```

**情况二：指定要排除的组件**

这种表示的含义是，在原本扫描的基础上，排除一些指定的类或指定注解修改的类。

```xml
<context:component-scan base-package="com.atguigu.spring6">
    
    <!-- context:exclude-filter标签：指定排除规则 -->
    <!-- 
 		type：设置排除的依据
		type="annotation"，根据注解排除，expression中设置要排除的注解的全类名
		type="assignable"，根据类型排除，expression中设置要排除的类型的全类名
	-->
    
    <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
        <!--<context:exclude-filter type="assignable" expression="com.atguigu.spring6.controller.UserController"/>-->
</context:component-scan>
```



**情况三：仅扫描指定组件**

```xml
<context:component-scan base-package="com.atguigu" use-default-filters="false">
    <!-- context:include-filter标签：指定在原有扫描规则的基础上追加的规则 -->
    <!-- use-default-filters属性：取值false表示关闭默认扫描规则 -->
    <!-- 此时必须设置use-default-filters="false"，因为默认规则即扫描指定包下所有类 -->
    <!-- 
 		type：设置包含的依据
		type="annotation"，根据注解包含，expression中设置要包含的注解的全类名
		type="assignable"，根据类型包含，expression中设置要包含的类型的全类名
	-->
    
    <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
	<!--<context:include-filter type="assignable" expression="com.atguigu.spring6.controller.UserController"/>-->
</context:component-scan>
```







---

### 使用注解定义Bean

Spring提供了以下多个注解，这些注解可以直接标注在Java类上，将它们定义为Spring Bean。

| 注解              | 说明                                                         |
| ----------------- | ------------------------------------------------------------ |
| **`@Component`**  | 该注解用于描述Spring中的Bean，它是一个泛化的概念，仅仅表示容器中的一个组件（Bean），并且可以作用在应用的任何层次，例如Service层、Dao层等。使用时只需要将该注解标注在相应类上即可。 |
| **`@Repository`** | 该注解用于将数据访问层（Dao层）的类标识为Spring中的Bean，功能与@Component相同。 |
| **`@Service`**    | 该注解通常作用在业务层（Service层），用于将业务层的类标识为Spring中的Bean，其功能与@Component相同。 |
| **`@Controller`** | 该注解通常作用在控制层（Controller层），用于将控制层的类标识为Spring中的Bean，其功能与@Component相同。 |

**使用案例**

在com.atguigu.dao包下创建User类，并使用@Component注解修饰（也可以使用其他三个注解）：

```java
@Component(value = "user")
public class User {
}
```

在注解中有一个`属性value`，该属性实际上就是bean标签中的`id属性`，用于对bean对象进行唯一的表示。

value属性可以不写，默认是当前类名称的**首字母小写**，如User类默认的value就是user。

在配置文件中，配置组件扫描：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="com.atguigu" />
</beans>
```

创建测试方法测试：

```java
@Test
public void test(){
    ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
    User user = context.getBean("user", User.class);
    System.out.println(user);
}
```

输出结果：

![image-20240514180022399](.\images\image-20240514180022399.png) 

即成功创建了User类对象。





---

### 使用注解注入属性

#### 1.@Autowired属性注入

单独使用@Autowired注解，**默认根据类型装配**。（即默认情况下，使用的是`byType`）

该注解表示的含义，就是对属性进行自动装配。

我们来看看@Autowired源码：

```java
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    boolean required() default true;
}
```

源码中，@Target元注解表示当前注解可以使用的位置，该@Autowired注解可以标注在：

* 构造方法上
* 普通方法上
* 形参上
* 属性上
* 注解上

该注解中包含了一个required属性，默认值是true，表示在注入的时候要求被注入的bean必须是存在的，若不存在则报错；若required属性设值为false，表示注入时bean存在不存在都无关系，存在则注入，不存在也不报错。

接下来，我们根据@Autowired注解可以使用的位置，来一一讲解。

首先，我们先来准备一下数据：

**数据准备**

创建三个包，分别是controller、service以及dao包。

在controller包中创建UserController类；在service包中创建UserService接口、UserImpl类；在Dao包中创建UserDao接口、UserDaoImp类。

在Controller层需要使用Service层中的对象，并调用Service层中的方法；在Service层需要使用dao层的对象，并调用Dao层中的方法。

给UserController类、UserServiceImpl类以及UserDaoImpl类添加bean对象的注解，让其装配到IoC容器中。

故最终，这几个接口或类的代码为：

UserController

```java
@Controller
public class UserController {

    private UserService userService;
    
    public void add(){
      System.out.println("controller............");
      userService.add();
    }
}
```

UserService

```java
public interface UserService {
    public void add();
}
```

UserServiceImpl

```java
@Service
public class UserServiceImpl implements UserService{
    private UserDao userDao;
    
    @Override
    public void add() {
        System.out.println("service............");
        userDao.add();
    }
}
```

UserDao

```java
public interface UserDao {
    public void add();
}
```

UserDaoImpl

```java
@Repository
public class UserDaoImpl implements UserDao{
    @Override
    public void add() {
        System.out.println("dao...........");
    }
}
```





##### 声明的四种位置

###### 1、修饰在属性上

在Controller层中，需要调用Service层的对象；在Service层中，需要调用Dao层的对象。所以，我们要在Controller类中，注入Service层的对象，在Service实现类中，注入Dao层的对象。

使用注解的方式，实现属性的注入，使用的是`@Autowired`注解。

该注解实现属性的自动注入。

> **与使用xml方式不同的是，使用注解的方式实现自动属性自动注入（@Autowired）不需要在类中声明属性的`set()`方法**，直接声明即可。

将@Autowired声明在属性上方，当IoC容器初始化时，就会自动将容器中声明的bean自动装配到相应的属性中。

所以，给Controller、ServiceImpl类中的属性，添加@Autowired注解，这样在初始化IoC容器时，就会将IoC中匹配的bean对象注入到属性中。

```java
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    public void add(){
        System.out.println("controller............");
        userService.add();
    }
}
```

```java
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;

    @Override
    public void add() {
        System.out.println("service............");
        userDao.add();
    }
}
```

测试：

```java
@Test
public void test(){
    ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
    
    UserController userController = context.getBean("userController", UserController.class);
    
    userController.add();
}
```

输出结果：

![image-20240514185743315](.\images\image-20240514185743315.png) 

即，成功将Service类型的对象注入到Controller属性中；成功将Dao类型的对象注入到ServiceImpl的属性中。



###### 2、声明在set()方法上

这种方式，是将原本写在属性上的@Autowired注解，改成写在set()方法上。这样也能够实现将bean对象注入到属性中。

比如，使用这种方式实现上述案例的属性注入：

Controller层

```java
@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void add(){
        System.out.println("controller............");
        userService.add();
    }
}
```

Service层：

```java
@Service
public class UserServiceImpl implements UserService{

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void add() {
        System.out.println("service............");
        userDao.add();
    }
}
```

测试：

```java
@Test
public void test(){
    ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
    
    UserController userController = context.getBean("userController", UserController.class);
    
    userController.add();
}
```

执行结果：

![image-20240514190428008](.\images\image-20240514190428008.png) 

即，此时也成功实现了属性的注入。

但是这种方式使用的不多





###### 3、声明在构造方法上

UserController

```java
@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void add(){
        System.out.println("controller............");
        userService.add();
    }

}
```

UserServiceImpl

```java
@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void add() {
        System.out.println("service............");
        userDao.add();
    }
}
```

测试：成功注入





###### 4、声明在形参上

UserController

```java
@Controller
public class UserController {

    private UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    public void add(){
        System.out.println("controller............");
        userService.add();
    }

}
```

UserServiceImpl

```java
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void add() {
        System.out.println("service............");
        userDao.add();
    }
}
```

测试：成功注入





---

##### 特殊情况：只有一个构造函数，省略注解

**`当类中只有一个包含该属性的有参构造时，@Autowired注解可以省略，属性会被自动注入相应的bean对象`**

**实现案例**

UserController

```java
@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void add(){
        System.out.println("controller............");
        userService.add();
    }
}
```

UserServiceImpl

```java
@Service
public class UserServiceImpl implements UserService{

    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public void add() {
        System.out.println("service............");
        userDao.add();
    }
}
```

可以看到，在这两个类中，并没有使用@Autowired去给属性注入依赖。

我们此时去初始化IoC容器，执行Controller中的add()方法测试一下：

```java
@Test
public void test(){
    ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
    
    UserController userController = context.getBean("userController", UserController.class);
    
    userController.add();
}
```

执行结果：

![image-20240514194521888](.\images\image-20240514194521888.png) 

可以发现，此时就算没有使用@Autowired注解来注入属性，IoC容器也给我们自动地将ServiceImpl对象和DaoImpl对象，注入到Controller类与ServiceImpl类中了。

但是注意：当类中并不是只有一个构造器时，无法使用这种方式实现属性的注入。例如，在Controller类中还包含一个空参构造器时，此时再去执行测试方法：

<img src=".\images\image-20240514194746904.png" alt="image-20240514194746904" style="zoom: 67%;" />

此时，就会因为Controller类中的userService属性是null，此时调用add()方法造成的空指针异常。此时的userService并没有bean对象注入。因为只有在类中有且只有一个属性初始化的有参构造器时，才会进行属性的注入。

但是，最好还是使用@Autowired注解进行显式地注入属性，这种方式太隐晦了。





---

##### 与@Qualifier联合使用

现在有这样一个场景，UserDao中有很多个实现类：UserDaoImpl1、UserDaoImpl2、UserDaoImpl3......并且这些实现类都作为bean使用IOC容器进行管理。在UserServiceImpl中有一个属性是UserDao类型的，假如我们需要将UserDaoImpl2类的bean对象注入到UserServiceImpl类的UserDao属性中时，如果我们仅仅只使用@Autowired注解，就不清楚要将UserDao中的哪一个实现类对象注入，此时就会报错。

因为@Autowired默认是`byType`，即**根据类型进行装配的**。

此时，就可以使用到**`@Qualifier`注解**

该注解表示，**通过设置名称，来选择注入的类对象**。

该注解需要搭配@Autowired一起使用，其中有一个`value`属性，用于指定具体要注入bean对象的id值。（也就是使用bean注解的value值，如@Controller(value="user")，这个user值）



**案例测试**

创建多个实现UserDao接口的实现类，例如UserDaoImpl1、UserDaoImpl2与UserDaoImpl3，并都使用@Repository注解进行修饰，将这些实现类加入到IoC容器中，在这些类都均创建add()方法，打印当前的类名：

UserDaoImpl1类

```java
@Repository
public class UserDaoImpl1 implements UserDao{
    @Override
    public void add() {
        System.out.println("UserDaoImpl1.........");
    }
}
```

UserDaoImpl2类

```java
@Repository
public class UserDaoImpl2 implements UserDao{
    @Override
    public void add() {
        System.out.println("UserDaoImpl2.........");
    }
}
```

UserDaoImpl3类

```java
@Repository
public class UserDaoImpl3 implements UserDao{
    @Override
    public void add() {
        System.out.println("UserDaoImpl3.........");
    }
}
```

在UserServiceImpl，使用了注解的方式自动注入UserDao类型的bean：

```java
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Override
    public void add() {
        System.out.println("service............");
        userDao.add();
    }
}
```

如果此时，进行测试的话：

```java
ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");

UserController userController = context.getBean("userController", UserController.class);

userController.add();
```

执行结果为：

<img src=".\images\image-20240514211629911.png" alt="image-20240514211629911" style="zoom:67%;" /> 

可以看到，由于在IoC容器中，UserDao接口的实现类对象有三个均可以作为UserDao类型对象注入，所以此时就不知道往其中注入哪一个对象，此时就会报错。

若使用xml配置文件的方式，我们可以将autowired属性设值成byName的方式，指定注入的bean的id，从而避免冲突。

同理，若使用注解的方式，也给我们提供了一个**`@Qualifier注解`**。

**`@Qualifier`注解的value属性，就是与bean的id值进行匹配，用于指定具体是哪一个bean对象（使用注解方式创建bean对象，bean的id值在声明bean的四个注解中使用value指明，例如@Service(value="userService")，其bean的id值就为userService，若未显式指明，默认就是类名的首字母小写）**

假如，要将UserDaoImpl2类的bean对象作为ServiceImpl类中的UserDao属性注入，此时UserServiceImpl类为：

```java
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    @Qualifier(value = "userDaoImpl2")
    private UserDao userDao;

    @Override
    public void add() {
        System.out.println("service............");
        userDao.add();
    }
}
```

此时再去测试，执行结果为：

![image-20240514210729449](.\images\image-20240514210729449.png) 

可以发现，此时就能够成功注入属性了。





---

##### 总结

* @Autowired注解可以出现在：属性上、构造方法上、构造方法的参数上、setter方法上。
* 当带参数的构造方法只有一个，@Autowired注解可以省略。
* @Autowired注解默认根据类型注入。此时需要根据名称注入，需要@Autowired与@Qualifier注解一起使用。



---

#### 2.@Resource注入

> **@Resource注解默认是根据bean的id与属性名进行匹配，使用`byName`的方式进行匹配。**
>
> @Resource注解有一个`name`属性，将与name属性值匹配的bean对象注入到当前所修饰的属性中，若不指定name，则默认使用属性名与bean的id进行匹配。
>
> **只有当通过属性名找不到对应的bean对象id时，才会自动启动`byType`进行注入。**

@Resource注解也可以完成属性注入。那么它和@Autowired注解有什么区别：

* @Resource注解是JDK扩展包中的，也就是说属于JDK的一部分。所以该注解是标准注解，更有通用性。
* @Autowired注解是Spring框架自己的。
* **@Resource注解默认根据名称装配byName，未指定name时，使用属性名作为name。通过name找不到的话会自动启动通过类型byType装配。**

* **@Autowired注解默认根据类型装配byType，如果向根据名称装配，需要配合@Qualifier注解一起使用。**
* @Resource注解用在属性上、setter方法上。
* @Autowired注解用在属性上、setter方法上、构造方法上、构造方法参数上。

@Resource注解属于JDK扩展包，所以不在JDK当中，需要额外引入以下依赖：**【如果是JDK8的话不需要额外引入依赖。高于JDK11或低于JDK8需要引入以下依赖】**

```xml
<dependency>
    <groupId>jakarta.annotation</groupId>
    <artifactId>jakarta.annotation-api</artifactId>
    <version>2.1.1</version>
</dependency>
```



**环境搭建**

创建三个包，分别是controller、service以及dao包。

在controller包中创建UserController类；在service包中创建UserService接口、UserImpl类；在Dao包中创建UserDao接口、UserDaoImp类。

在Controller层需要使用Service层中的对象，并调用Service层中的方法；在Service层需要使用dao层的对象，并调用Dao层中的方法。

给UserController类、UserServiceImpl类以及UserDaoImpl类添加bean对象的注解，让其装配到IoC容器中。

故最终，这几个接口或类的代码为：

UserController

```java
@Controller
public class UserController {

    private UserService userService;
    
    public void add(){
      System.out.println("controller............");
      userService.add();
    }
}
```

UserService

```java
public interface UserService {
    public void add();
}
```

UserServiceImpl

```java
@Service
public class UserServiceImpl implements UserService{
    private UserDao userDao;
    
    @Override
    public void add() {
        System.out.println("service............");
        userDao.add();
    }
}
```

UserDao

```java
public interface UserDao {
    public void add();
}
```

UserDaoImpl

```java
@Repository
public class UserDaoImpl implements UserDao{
    @Override
    public void add() {
        System.out.println("dao...........");
    }
}
```

创建bean.xml文件，开启注解扫描（后续将不再使用XML文件）

```xml
<context:component-scan base-package="com.atguigu" />
```





**1、场景一：根据name注入**

在默认情况下，@Resource自动装配属性的方式是`byName`。

根据@Resource注解的name属性，与注入到IoC容器中bean对象的value值（默认是类名的第一个字母小写）进行匹配。

故使用@Resource实现上述案例的属性注入：

UserController类

```java
@Controller
public class UserController {

    @Resource(name = "myUserServiceImpl")
    private UserService userService;

    public void add(){
      System.out.println("controller............");
      userService.add();
    }
}
```

UserServiceImpl类

```java
//注解中如果只有一个属性，并且该属性为value，则前面的value=可以省略
@Service("myUserServiceImpl")
public class UserServiceImpl implements UserService{

    @Resource(name = "userDaoImpl")
    private UserDao userDao;

    @Override
    public void add() {
        System.out.println("service............");
        userDao.add();
    }
}
```

UserDaoImpl

```java
//若不设置value值，默认使用类名第一个字母小写
@Repository
public class UserDaoImpl implements UserDao{
    @Override
    public void add() {
        System.out.println("dao...........");
    }
}
```

测试：

```java
ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");

UserController userController = context.getBean("userController", UserController.class);

userController.add();
```

输出结果：

![image-20240514232317530](.\images\image-20240514232317530.png) 

即此时注入成功。





**2、场景二：name未知注入**

当未给@Resource的name属性值设值时，默认使用要注入属性的属性名，与bean的value值进行匹配。

这种场景下，使用@Resource实现属性注入：

UserController类

```java
@Controller
public class UserController {

    @Resource
    private UserService myUserServiceImpl;

    public void add(){
        System.out.println("controller..........");
        myUserServiceImpl.add();
    }
}
```

UserServiceImpl类

```java
@Service("myUserServiceImpl")
public class UserServiceImpl implements UserService{

    @Resource
    private UserDao myUserDaoImpl;

    @Override
    public void add() {
        System.out.println("service............");
        myUserDaoImpl.add();
    }
}
```

UserDaoImpl类

```java
@Repository("myUserDaoImpl")
public class UserDaoImpl implements UserDao{
    @Override
    public void add() {
        System.out.println("dao...........");
    }
}
```

测试结果：

![image-20240514233036602](.\images\image-20240514233036602.png) 





**3、场景三：根据类型匹配**

如果在@Resource注解中，既没有指定name值；也没有让属性名与bean的value值保持一致时，此时@Resource注解就会去使用byType进行匹配，即根据属性的类型与bean的类型进行匹配。

案例：

去给UserDao中，创建多个实现类，并将这些实现类使用Bean注解修饰，让它们加入到IoC容器中。

修改UserController类与UserServiceImpl类

UserController类

```java
@Controller
public class UserController {

    @Resource
    private UserService userService;

    public void add(){
        System.out.println("controller..........");
        userService.add();
    }
}
```

UserServiceImpl类

```java
@Service("myUserServiceImpl")
public class UserServiceImpl implements UserService{

    @Resource
    private UserDao userDao;

    @Override
    public void add() {
        System.out.println("service............");
        userDao.add();
    }
}
```

此时再去执行测试：

![image-20240514233507057](.\images\image-20240514233507057.png)

根据异常信息得知：显然当通过name找不到的时候，自然会启动byType进行注入，以上的错误是因为UserDao接口下有多个实现类导致的。所以根据类型注入就会报错。

报错原因具体分析：

在UserController类中，使用@Resource注解注入属性，由于@Resource未设置name值，则默认使用属性名userService与IoC容器中的bean对象的id值，也就是设置Bean注解的value值进行匹配，发现没有匹配的值，此时就会根据type类型进行匹配。在IoC容器中，只有UserServiceImpl类型满足，所以就会将UserServiceImpl类型的对象注入到UserController类的UserService属性中。这里是注入成功的。

接下来还需要给UserServiceImpl类中的UserDao注入属性。

在IoC容器中，没有bean的id值与userDao属性名匹配（@Resource未设置name值），故会使用类型进行匹配。又因为在IoC容器中，包含多个UserDao类的实现类对象，所以，此时不知道该使用哪一个类型与属性进行匹配注入，所以此时注入失败，报错。



**总结**

@Resource注解：默认byName注入，没有指定name时把属性名当做name，根据name找不到时，才会byType注入。

byType注入，匹配的类型bean只能有一个，否则会报错。



---

### Spring全注解开发

在之前的学习中，我们使用注解进行开发，但是还是需要编写一个xml的配置文件，该配置文件用于开启组件扫描。

如果我们想要在注解开发中，全部都使用注解来实现，包括组件的扫描，不使用xml配置文件。这就是全注解开发。

**方式：**使用配置类来代替配置文件

使用`@Configuration`修饰类，表示该类是一个配置类

使用`@ComponentScan`注解来指定要扫描的包，IoC会去扫描value属性表示的包及其子包下的所有的组件。

**创建配置类**

```java
@Configuration
@ComponentScan("com.atguigu.spring6")
public class Spring6Config {
}
```

**加载配置类**

使用的类是`AnnotationConfigApplicationContext`

```java
@Test
public void testAllAnnotation(){
    ApplicationContext context = new AnnotationConfigApplicationContext(Spring6Config.class);
    
    UserController userController = context.getBean("userController", UserController.class);
    
    userController.add();
}
```

成功执行。



---

## 4、手写代码实现IoC功能

IOC是通过`Java反射 + 注解`来实现的。

我们知道，IoC（控制反转）和DI（依赖注入）是Spring核心的东西，那么，我们如何自己手写出这样的代码呢？

下面我们就一步一步写出Spring框架最核心的部分。

原本，我们是使用@Component、@Controller、@Service、@Repository注解将类装载到IoC容器中，并使用@Autowired与@Resource实现属性注入的功能，那么我们在手写IOC功能时，也需要去声明相关注解实现。

**大致实现过程**

第一步：搭建环境

第二步：创建两个注解，第一个注解@Bean来完成创建bean对象，第二个注解@Di实现属性的注入

第三步：实现IoC容器



### 实现过程

##### 第一步：环境搭建

创建一个子模块atguigu-spring，该模块就是用于手写代码实现IoC功能的

<img src=".\images\image-20240515142305205.png" alt="image-20240515142305205" style="zoom:67%;" /> 

<img src=".\images\image-20240515142402542.png" alt="image-20240515142402542" style="zoom: 50%;" /> 

然后，在工程下创建几个包与几个类用于测试：

创建com.atguigu.service包，在该包下创建子包impl与UserService接口，并在子包impl下创建UserServiceImpl类，让该类去实现UserService接口；

创建com.atguigu.dao包，在该包下创建子包impl与UserDao接口，并在子包impl下创建UserDaoImpl类，让该类去实现UserDao接口。

这样一来，相当于在该子项目下，创建了两层结构：service与dao层，同时在这两层中均声明了两个类进行测试。创建完毕，最终的项目结构为：

<img src=".\images\image-20240515143104538.png" alt="image-20240515143104538" style="zoom: 80%;" /> 

如果我们使用原先Spring给我们提供的注解，则我们会在UserServiceImpl类与UserDaoImpl类上，添加@Service与@Repository注解，将这两个类加入到IoC容器中。并且在UserServiceImpl类中会去声明UserDao类型的属性，通过@Autowired注解，根据类型匹配自动注入bean属性。

现在我们自己去手写实现IOC容器，那么我们也要去定义几个注解，来实现上述的功能。



##### 第二步：创建注解

我们去创建注解，来代替Spring给我们提供的注解。

* 创建注解`@Bean`，代替定义Bean的注解；

* 创建注解`@Di`，代替注入属性的注解。

创建com.atguigu.anno包，在该包下，创建两个注解，分别是@Bean与@Di：

![image-20240515144008147](.\images\image-20240515144008147.png) 

然后，我们给这两个注解添加元注解：

@Bean注解是用于bean对象的创建，修饰在类上面，所以我们使用@Target注解限制其使用在类上；然后使用@Retention注解让其只在运行时生效：

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Bean {
}
```

@Di注解是用于属性注入，修饰在属性上，所以我们使用@Target注解限制其只能修饰在属性上，然后使用@Retention注解表明其只在运行时生效：

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Di {
}
```

这样一来，两个注解就创建完毕了。（但是暂时还没有功能）

此时，我们可以使用这两个注解来代替原本的Spring IoC的注解，使用我们自己的注解实现功能：

UserServiceImpl类

```java
@Bean
public class UserServiceImpl implements UserService {
    @Di
    private UserDao userDao;
}
```

UserDaoImpl类

```java
@Bean
public class UserDaoImpl implements UserDao {
}
```





##### 第三步：实现IOC容器

在Spring中，IOC容器是通过ApplicationContext接口及其子类来实现的，我们在Spring中，去加载IoC容器，都是先去创建ApplicationContext实现类对象的方式进行的。

那么，我们去实现IOC容器，也通过这种方式。

那么，我们就先去创建一个类似于ApplicationContext的接口，例如MyApplicationContext，在该接口中，提供了getBean()方法，用于获取bean对象。

###### 1）创建MyApplicationContext接口

```java
public interface MyApplicationContext {
    Object getBean(Class clazz);
}
```

这里提供getBean()的方法是根据类的Class对象获取。

该接口类似于大多数接口，起到一个`规范`的作用，表示所有实现IoC容器的类都要去实现该接口，和Spring给我们提供的ApplicationContext接口一样。

我们再去创建一个MyApplicationContext的实现类，用于具体地实现。由于我们是使用注解来实现的，实现类就叫做`AnnotationApplicationContext`。

###### 2）创建MyApplicationContext的实现类

```java
public class AnnotationApplicationContext implements MyApplicationContext{
    @Override
    public Object getBean(Class clazz) {
        return null;
    }
}
```

在Spring中，使用的是一个Map集合来存放创建的bean对象，所以，我们也去创建一个Map集合，存储创建出来的bean对象，该Map集合的key是Class属性。（虽然在Spring的IoC容器中，存储bean对象的Map集合的泛型并不是这样，我这里就简单化了，方便学习）

getBean()方法是根据类去获取类的bean对象，所以，我们可以在getBean()方法中，去根据Map集合的key，获取Map集合中对应的Object对象返回即可：

```java
public class AnnotationApplicationContext implements MyApplicationContext{
    //创建Map集合，存放bean对象，key为beanName，value为bean对象
    private Map<Class, Object> beanFactory = new HashMap<>();

    @Override
    public Object getBean(Class clazz) {
        return beanFactory.get(clazz);
    }
}
```

我们还需要去设置包的扫描规则，去扫描指定包及其子包下，哪个类中使用了@Bean注解修饰，就要把这个类需要通过反射进行实例化。

在Spring中，我们在新建ApplicationContext的对象时，需要传入一个配置文件，比如：`ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");`

此时就会去bean.xml文件中，根据其中的配置开启扫描组件或者创建bean标签的对象，那么我们去这样做：将原本传入xml文件的方式修改成直接传入包路径，表示去扫描指定包及其子包下的类。



**创建有参构造，传入包路径**

```java
//设置包的扫描规则
//当前包及其子包，哪个类有@Bean注解，把这个类通过反射实例化
public AnnotationApplicationContext(String basePackage){
}
```

然后我们来分析一下，该如何实现包的扫描功能。

比如，此时我们传入的是com.atguigu这个包，但是在com.atguigu包下，可能有很多层级，比如可能有com.atguigu.service包、有com.atguigu.dao包，我们需要将com.atguigu包及其所有子包下的类。

我们知道包，实际上都是一个一个的文件夹，那么我们可以去找到传入的basePackage包所对应的文件夹，然后去文件夹里面进行递归操作，进入到所有子文件夹下，查找每一个类是否使用了@Bean修饰，如果有就加入到Map集合中就可以了。

**该如何去找到包对应的文件夹呢？**

1、需要把包路径中的.点替换成/斜杆，这才是文件夹式的路径

2、获取包的绝对路径，去磁盘中进行查找。

> **注意这里的绝对路径，是项目编译之后的路径，存放在`target`目录中**
>
> 例如：C:\Users\14036\Desktop\mall\Spring6\atguigu-spring\target\classes\com\atguigu\anno\Bean.class
>
> 这是去获取com.atguigu.anno包下的Bean类的绝对路径。
>
> 获取Java项目下某个文件的绝对路径，实际上获取到的都是该项目编译后存放的地址，因为项目只有编译，才能运行，项目中的文件进行编译后都会放在target目录中。

对上述的逻辑进行实现：

```java
public AnnotationApplicationContext(String basePackage){
    try {
        //1、把.替换成\
        String packagePath = basePackage.replaceAll("\\.", "\\\\");

        //2、获取包的绝对路径
        //得到的是一个枚举类型
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packagePath);
        //根据这个枚举类型，去获取绝对路径
        while (urls.hasMoreElements()){
            URL url = urls.nextElement();
            String filePath = URLDecoder.decode(url.getFile(), "utf-8");
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
```

这样一来，我们就可以获取到包所对应的绝对路径filePath了。

接下来，我们就需要根据这个绝对路径，进行组件的扫描了：

对该路径下（包括子目录中），所有使用了@Bean注解的类进行扫描，将其存入到Map集合中。



###### 3）组件扫描功能实现（主要）

单独创建一个方法，比如`loadBean()`，传入一个File类型的对象，在该方法中，完成bean对象的扫描。

> 分析一下该如何扫描：
>
> 1. **我们在类中声明一个属性`rootPath`，这个属性去记录一下传入包比如com.atguigu所对应的包之前的绝对路径，方便后续使用。**
>
>    比如com.atguigu包所对应的绝对路径是：C:\Users\14036\Desktop\mall\Spring6\atguigu-spring\target\classes\com\atguigu，那么我们就需要将其中的C:\Users\14036\Desktop\mall\Spring6\atguigu-spring\target\classes这一部分使用rootPath记录一下。
>
>    实现也比较简单：
>
>    `rootPath = 包绝对路径.substring(0, 包绝对路径.length()-包.length())`
>
> 2. loadBean()方法使用递归地方式，一层一层地进入文件夹中进行操作。首先，在loadBean()方法中进行判断，判断传入的File是否是文件夹，如果不是，则直接返回；
>
> 3. 如果是文件夹，则去判断文件夹中是否为空，如果为空，也直接返回；
>
> 4. 如果文件夹内有内容，则对文件夹下的所有内容进行循环遍历：
>
>    1. 对于遍历得到的所有subFile，也进行一个判断，如果subFile为文件夹，则递归地执行loadBean()方法，传入subFile对象参数；
>
>    2. 如果遍历的subFile为文件，则我们需要判断该文件是否为.class文件：
>       1. 如果不是，则跳出当前循环，继续下一次循环。
>       2. 如果当前循环的subFile是.class文件，就需要去获取该文件所对应的全类名（去除绝对路径中rootPath部分，并将\替换成.，.class文件后缀去除）。然后我们使用反射的方式，判断类是否使用了@Bean注解进行修饰，如果有，则将对象进行实例化，并将实例化后的对象存入Map集合中。

这就是扫描的核心功能。

这里还有两个细节：

1、接口无法实例化，所以当前文件所对应的是接口时（反射判断），跳过当前文件；

2、在实际开发中，我们一般在controller类中声明的service属性以及在service层声明的dao属性都是接口类型，而我们手写的IoC容器中，是通过Class对象获取到对应的bean对象，所以我们进行匹配时，也是根据类型进行匹配的。所以，当遇到有父接口的实现类时，我们使用其父级接口作为Map集合中的key。

扫描功能实现：

```java
public void loadBean(File file) {
    //判断传入的File是否存在，或者是否为文件夹，如果不存在或者不为文件夹，则直接返回
    if (!file.exists() || !file.isDirectory()){
        return;
    }

    //如果是文件夹，去获取该文件夹下的内容
    File[] subFiles = file.listFiles();
    //如果该文件夹下没有内容，则直接返回
    if (subFiles == null){
        return;
    }

    //如果有内容，进行遍历：
    // 如果subFile是文件夹，则进行递归操作；
    //如果subFile是文件，则
    for (File subFile : subFiles) {
        if (subFile.isDirectory()){
            //如果是文件夹，则进行递归扫描
            loadBean(subFile);
        }else {
            //如果是文件，则需要去获取该subFile所表示的类对应的全路径
            //然后使用Class.forName()方法去获取类的Class对象
            //再使用反射去获取注解信息

            //获取类全路径的方式实际也是通过字符串的拼接进行的
            //需要将类的绝对路径（编译后）修改成类的全路径
            String absolutePath = subFile.getAbsolutePath();
            //将绝对路径中，包前面部分去掉
            String pathWithClass = absolutePath.substring(rootPath.length() - 1);
            //然后判断文件是否是.class文件，如果不是，则直接退出循环
            if (!pathWithClass.endsWith(".class")){
                continue;
            }
            //如果是.class文件，则需要将.class去掉，并且将/使用.替换，此时获得的就是类的全路径
            String fullName = pathWithClass.replaceAll("\\\\", ".").replace(".class", "");
            //使用Class.forName()创建Class对象
            try {
                Class<?> clazz = Class.forName(fullName);
                //如果当前类是接口类型，接口无法创建对象，所以跳过
                if(clazz.isInterface()){
                    continue;
                }
                //判断当前类是否被@Bean注解修
                Bean annotation = clazz.getAnnotation(Bean.class);
                if (annotation != null){
                    //如果annotation不为null，就表示该类被@Bean注解修饰
                    //初始化类的实例对象
                    Object instance = clazz.getDeclaredConstructor().newProxyInstance();

                    if (clazz.getInterfaces().length > 0){
                        //如果该类有父接口，则将该类的第一个父接口.class作为key，对象作为value传入到map中
                        beanFactory.put(clazz.getInterfaces()[0], instance);
                    }else {
                        //如果该类没有父接口，则将类.class作为key，对象作为value传入到map中
                        beanFactory.put(clazz, instance);
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
```

此时，我们就实现了对指定包下进行组件扫描的功能。

目前完整的AnnotationApplicationContext类如下所示：

```java
public class AnnotationApplicationContext implements MyApplicationContext{
    //创建Map集合，存放bean对象，key为beanName，value为bean对象
    private Map<Class<?>, Object> beanFactory = new HashMap<>();

    //记录一下不包含包路径的绝对路径，例如：C:\Users\14036\Desktop\mall\Spring6\atguigu-spring\target\classes
    private String rootPath;

    @Override
    public Object getBean(Class clazz) {
        return beanFactory.get(clazz);
    }

    
    //设置包的扫描规则
    //当前包及其子包，哪个类有@Bean注解，把这个类通过反射实例化
    public AnnotationApplicationContext(String basePackage){
        try {
            //1、把.替换成\
            String packagePath = basePackage.replaceAll("\\.", "\\\\");

            //2、获取包的绝对路径
            //得到的是一个枚举类型
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packagePath);
            //根据这个枚举类型，去获取绝对路径
            while (urls.hasMoreElements()){
                URL url = urls.nextElement();
                String filePath = URLDecoder.decode(url.getFile(), "utf-8");
                rootPath = filePath.substring(0, filePath.length() - packagePath.length());
                //组件的扫描
                loadBean(new File(filePath));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //包扫描过程
    public void loadBean(File file) {
        //判断传入的File是否存在，或者是否为文件夹，如果不存在或者不为文件夹，则直接返回
        if (!file.exists() || !file.isDirectory()){
            return;
        }

        //如果是文件夹，去获取该文件夹下的内容
        File[] subFiles = file.listFiles();
        //如果该文件夹下没有内容，则直接返回
        if (subFiles == null){
            return;
        }

        //如果有内容，进行遍历：
        // 如果subFile是文件夹，则进行递归操作；
        //如果subFile是文件，则
        for (File subFile : subFiles) {
            if (subFile.isDirectory()){
                //如果是文件夹，则进行递归扫描
                loadBean(subFile);
            }else {
                //如果是文件，则需要去获取该subFile所表示的类对应的全路径
                //然后使用Class.forName()方法去获取类的Class对象
                //再使用反射去获取注解信息

                //获取类全路径的方式实际也是通过字符串的拼接进行的
                //需要将类的绝对路径（编译后）修改成类的全路径
                String absolutePath = subFile.getAbsolutePath();
                //将绝对路径中，包前面部分去掉
                String pathWithClass = absolutePath.substring(rootPath.length() - 1);
                //然后判断文件是否是.class文件，如果不是，则直接退出循环
                if (!pathWithClass.endsWith(".class")){
                    continue;
                }
                //如果是.class文件，则需要将.class去掉，并且将/使用.替换，此时获得的就是类的全路径
                String fullName = pathWithClass.replaceAll("\\\\", ".").replace(".class", "");
                
                try {
                    //使用Class.forName()创建Class对象
                    Class<?> clazz = Class.forName(fullName);
                    //如果当前类是接口类型，接口无法创建对象，所以跳过
                    if(clazz.isInterface()){
                        continue;
                    }
                    //判断当前类是否被@Bean注解修
                    Bean annotation = clazz.getAnnotation(Bean.class);
                    if (annotation != null){
                        //如果annotation不为null，就表示该类被@Bean注解修饰
                        //初始化类的实例对象
                        Object instance = clazz.getDeclaredConstructor().newProxyInstance();

                        if (clazz.getInterfaces().length > 0){
                            //如果该类有父接口，则将该类的第一个父接口.class作为key，对象作为value传入到map中
                            beanFactory.put(clazz.getInterfaces()[0], instance);
                        }else {
                            //如果该类没有父接口，则将类.class作为key，对象作为value传入到map中
                            beanFactory.put(clazz, instance);
                        }
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
```



测试：

在AnnotationApplicationContext中创建getBeanFactory()方法，方便我们进行测试：

```java
AnnotationApplicationContext context = new AnnotationApplicationContext("com.atguigu");

Map<Class<?>, Object> map = context.getBeanFactory();

for (Class<?> clazz : map.keySet()) {
    Object o = map.get(clazz);
    System.out.println(clazz + "---------->" + o);
}
```

创建AnnotationApplicationContext对象时，传入的包是com.atguigu。

在com.atguigu包下，有两个子包：dao与service，在dao包下，创建了impl子包，在该包下给UserDao接口的实现类UserDaoImpl使用了@Bean注解实现；在service包下，创建了impl子包，在该包下给UserService接口的实现类UserServiceImpl使用了@Bean注解实现：

![image-20240516011101665](.\images\image-20240516011101665.png) 

执行结果为：

![image-20240516011239372](.\images\image-20240516011239372.png) 

可以看到，我们所创建的Bean容器中，有两个元素，均使用了其父接口作为key，其自身对象作为value，符合我们想要创建的IoC容器要求。

自此，扫描Bean组件的功能就完成了，还差IoC容器的另一个主要功能DI依赖注入。



###### 4）依赖注入功能实现（主要）

我们已经完成了对指定包下进行扫描，将使用了@Bean修饰的类实例化并存入到Map集合中，现在我们还需要对存入到Map集合中的实例化对象进行属性的注入，如果这些对象的类的属性中使用了@Di依赖，我们需要将类型匹配的bean对象注入。

创建依赖注入方法：`loadDi()`

> 该方法的大致过程：
>
> 1. 遍历beanFactory，beanFactory这个Map集合中存放的都是使用了@Bean注解的类的实例对象；
>
> 2. 获取每个集合元素中value对应的Class实例。也就是实例对象其实际对应的Class实例（注意这里的Class与集合中对应的key有区别，key有可能是value类的父接口的Class）。
>
> 3. 使用反射，去获取类中所有的属性集合Fields；
>
> 4. 遍历集合Fields，使用反射判断元素Field是否被@Di注解修饰；
>    1. 若未被@Di注解修饰，则跳过当前循环，继续遍历下一个属性；
>    2. 若被@Di注解修饰，则根据当前Field属性的Class，以此为key，去获取beanFactory这个bean对象的Map集合中所对应的value实例，并给当前实例的Field赋值。

这样一来，依赖注入的简单实现就完成了。

代码具体实现：

```java
private void loadDi(){
    //如果当前的beanFactory不为空
    if (!this.beanFactory.isEmpty()){
        for (Map.Entry<Class<?>, Object> entry : this.beanFactory.entrySet()) {
            
            //获取bean的实例对象所实际对应的class（因为key的class有可能是其父接口的Class）
            Object bean = entry.getValue();
            Class<?> aClass = bean.getClass();
            
            //使用反射，去获取其中所有属性，判断是否有属性使用了@Di注解
            for (Field field : aClass.getDeclaredFields()) {
                Di annotation = field.getAnnotation(Di.class);
                
                //如果annotation不为null，说明当前属性使用了@Di注解
                if (annotation != null){
                    //我们就去给对象的属性赋值
                    field.setAccessible(true);
                    try {
                        field.set(bean, beanFactory.get(field.getType()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
```

然后，在构造器中，在执行完组件扫描功能后，去调用依赖注入的方法loadDi()。

那么，自此，IoC容器的简单实现就全部完成了，我们来测试一下：

```java
AnnotationApplicationContext context = new AnnotationApplicationContext("com.atguigu");

UserService userService = (UserService)context.getBean(UserService.class);

System.out.println(userService);
```

在com.atguigu下有两个包，分别是dao和service，在service中有一个类UserServiceImpl，该类使用了@Bean注解，在该类中包含有一个属性是UserDao接口类型的，该属性使用了@Di注解修饰；在dao包下有一个类UserDaoImpl，该类是UserDao的实现类，并且该类也使用了@Bean注解修饰。

测试结果：

![image-20240516102909049](.\images\image-20240516102909049.png) 

可以看到，成功创建UserServiceImpl实例，并且在UserServiceImpl的实例对象中，UserDaoImpl成功注入到其属性userDao中。

即，此时，我们已经将IoC容器的简单功能使用手写代码的方式实现。





### 实现代码

```java
public class AnnotationApplicationContext implements MyApplicationContext{
    //创建Map集合，存放bean对象，key为beanName，value为bean对象
    private Map<Class<?>, Object> beanFactory = new HashMap<>();

    //记录一下不包含包路径的绝对路径，例如：C:\Users\14036\Desktop\mall\Spring6\atguigu-spring\target\classes
    private String rootPath;

    
    @Override
    public Object getBean(Class<?> clazz) {
        return beanFactory.get(clazz);
    }

    
    //设置包的扫描规则
    //当前包及其子包，哪个类有@Bean注解，把这个类通过反射实例化
    public AnnotationApplicationContext(String basePackage){
        try {
            //1、把.替换成\
            String packagePath = basePackage.replaceAll("\\.", "\\\\");

            //2、获取包的绝对路径
            //得到的是一个枚举类型
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packagePath);
            //根据这个枚举类型，去获取绝对路径
            while (urls.hasMoreElements()){
                URL url = urls.nextElement();
                String filePath = URLDecoder.decode(url.getFile(), "utf-8");
                rootPath = filePath.substring(0, filePath.length() - packagePath.length());
                
                //组件扫描
                loadBean(new File(filePath));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //依赖注入
        loadDi();
    }

    
    /**
     * 组件扫描功能
     * @param file
     */
    public void loadBean(File file) {
        //判断传入的File是否存在，或者是否为文件夹，如果不存在或者不为文件夹，则直接返回
        if (!file.exists() || !file.isDirectory()){
            return;
        }
        //如果是文件夹，去获取该文件夹下的内容
        File[] subFiles = file.listFiles();
        //如果该文件夹下没有内容，则直接返回
        if (subFiles == null){
            return;
        }
        //如果有内容，进行遍历：
        // 如果subFile是文件夹，则进行递归操作；
        //如果subFile是文件，则
        for (File subFile : subFiles) {
            if (subFile.isDirectory()){
                //如果是文件夹，则进行递归扫描
                loadBean(subFile);
            }else {
                //如果是文件，则需要去获取该subFile所表示的类对应的全路径
                //然后使用Class.forName()方法去获取类的Class对象
                //再使用反射去获取注解信息
                //获取类全路径的方式实际也是通过字符串的拼接进行的
                //需要将类的绝对路径（编译后）修改成类的全路径
                String absolutePath = subFile.getAbsolutePath();
                //将绝对路径中，包前面部分去掉
                String pathWithClass = absolutePath.substring(rootPath.length() - 1);
                //然后判断文件是否是.class文件，如果不是，则直接退出循环
                if (!pathWithClass.endsWith(".class")){
                    continue;
                }
                //如果是.class文件，则需要将.class去掉，并且将/使用.替换，此时获得的就是类的全路径
                String fullName = pathWithClass.replaceAll("\\\\", ".").replace(".class", "");
                //使用Class.forName()创建Class对象
                try {
                    Class<?> clazz = Class.forName(fullName);
                    //如果当前类是接口类型，接口无法创建对象，所以跳过
                    if(clazz.isInterface()){
                        continue;
                    }
                    //判断当前类是否被@Bean注解修
                    Bean annotation = clazz.getAnnotation(Bean.class);
                    if (annotation != null){
                        //如果annotation不为null，就表示该类被@Bean注解修饰
                        //初始化类的实例对象
                        Object instance = clazz.getDeclaredConstructor().newProxyInstance();

                        if (clazz.getInterfaces().length > 0){
                            //如果该类有父接口，则将该类的第一个父接口.class作为key，对象作为value传入到map中
                            beanFactory.put(clazz.getInterfaces()[0], instance);
                        }else {
                            //如果该类没有父接口，则将类.class作为key，对象作为value传入到map中
                            beanFactory.put(clazz, instance);
                        }
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * 依赖注入功能
     */
    private void loadDi(){
        if (!this.beanFactory.isEmpty()){
            for (Map.Entry<Class<?>, Object> entry : this.beanFactory.entrySet()) {
                //获取bean的实例对象所实际对应的class（因为key的class有可能是其父接口的Class）
                Object bean = entry.getValue();
                Class<?> aClass = bean.getClass();
                //使用反射，去获取其中所有属性，判断是否有属性使用了@Di注解
                for (Field field : aClass.getDeclaredFields()) {
                    Di annotation = field.getAnnotation(Di.class);
                    //如果annotation不为null，说明当前属性使用了@Di注解
                    if (annotation != null){
                        //我们就去给对象的属性赋值
                        field.setAccessible(true);
                        try {
                            field.set(bean, beanFactory.get(field.getType()));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }
}
```









---

# 四、面向切面：AOP

## 1、问题模拟

搭建子模块：spring6-aop

在子模块中，声明计算器接口Calculator，在该接口中，创建包含加减乘除的抽象方法：

```java
public interface Calculator {
    
    int add(int i, int j);
    
    int sub(int i, int j);
    
    int mul(int i, int j);
    
    int div(int i, int j);
    
}
```

创建一个计算器的实现类CalculatorImpl，让实现类具体去实现计算器的接口：

```java
public class CalculatorImpl implements Calculator {

    @Override
    public int add(int i, int j) {

        int result = i + j;

        System.out.println("方法内部 result = " + result);

        return result;
    }

    @Override
    public int sub(int i, int j) {

        int result = i - j;

        System.out.println("方法内部 result = " + result);

        return result;
    }

    @Override
    public int mul(int i, int j) {

        int result = i * j;

        System.out.println("方法内部 result = " + result);

        return result;
    }

    @Override
    public int div(int i, int j) {

        int result = i / j;

        System.out.println("方法内部 result = " + result);

        return result;
    }
}
```

假如说此时，我想去给计算器的实现类添加一个日志功能，在方法开始执行之前打印一下，说明什么方法开始执行了，并将参数打印；在方法执行结束再打印一下，说明执行的结果是什么。

此时计算器的实现类如下所示：

![images](.\images\img015.png)

```java
public class CalculatorImpl implements Calculator {

    @Override
    public int add(int i, int j) {

        System.out.println("[日志] add 方法开始了，参数是：" + i + "," + j);

        int result = i + j;

        System.out.println("方法内部 result = " + result);

        System.out.println("[日志] add 方法结束了，结果是：" + result);

        return result;
    }

    @Override
    public int sub(int i, int j) {

        System.out.println("[日志] sub 方法开始了，参数是：" + i + "," + j);

        int result = i - j;

        System.out.println("方法内部 result = " + result);

        System.out.println("[日志] sub 方法结束了，结果是：" + result);

        return result;
    }

    @Override
    public int mul(int i, int j) {

        System.out.println("[日志] mul 方法开始了，参数是：" + i + "," + j);

        int result = i * j;

        System.out.println("方法内部 result = " + result);

        System.out.println("[日志] mul 方法结束了，结果是：" + result);

        return result;
    }

    @Override
    public int div(int i, int j) {

        System.out.println("[日志] div 方法开始了，参数是：" + i + "," + j);

        int result = i / j;

        System.out.println("方法内部 result = " + result);

        System.out.println("[日志] div 方法结束了，结果是：" + result);

        return result;
    }
}
```

但是，此时就有一个问题了：

对于add()方法为例：

`System.out.println("[日志] add 方法开始了，参数是：" + i + "," + j);`

`System.out.println("[日志] add 方法结束了，结果是：" + result);`

上述的两段代码是日志功能

`int result = i + j;`

`System.out.println("方法内部 result = " + result);`

上述两段代码是核心业务逻辑

**存在的问题**

* 将附加功能和核心业务放在一起，维护起来十分不便；

* 将日志与核心功能放在一块，会对和核心业务产生干扰，导致程序员在开发核心业务功能时分散了精力。

所以，我们就想要把附加功能从核心业务代码中抽取出来，放在一起进行统一的维护。

对于上述的例子来说，如果我们使用普通的方式：创建一个工具类，在工具类中创建带有日志功能的方法，这样是无法实现上述的功能的。因为在上例中，各个方法所打印的日志信息，包含了方法自身的信息，包括方法的名称，方法的参数，以及方法内部属性result的执行结果，这些要打印的信息存在于方法内部，要抽取出来仅仅靠创建工具类方法的方式，无法实现。

所以，我们需要引入新的技术，这个技术就是代理模式。

## 2、代理模式

### 2.1、概念

二十三种设计模式的一种，属于结构型模式。

它的作用就是提供一个代理类，让我们在调用目标方法的时候，不再是直接对目标方法进行调用，而是**通过代理类间接**调用。让不属于目标方法核心逻辑的代码从目标方法中剥离出来——**解耦**。

调用目标方法时先调用代理对象的方法，减少对目标方法的调用和打扰，同时让附加功能能够集中在一起也有利于维护。

使用代理前：

<img src=".\images\img016.png" alt="images" style="zoom: 67%;" />

使用代理后：

<img src=".\images\img017.png" alt="images" style="zoom:67%;" />

比如我们要去调用add()方法：

没有代理时，我们会去直接调用add()方法，并获得返回值；有代理时，则会去调用代理，由代理去给我们调用add()方法，然后由代理将返回值返回给我们。

**相关术语**

* 代理：将非核心逻辑剥离出来以后，封装这些非核心逻辑的类、对象、方法。
* 目标：被代理套用了的非核心逻辑代码的类、对象、方法。

代理分为两种：一种是静态代理、一种是动态代理。



### 2.2、静态代理

**`静态代理关键在于代理对象和目标对象实现共同的接口，并且代理对象持有目标对象的引用。`**

我们来针对上述的计算器实现类，来使用静态代理的方式解决：

创建一个类CalculatorStaticProxy，也需要去实现Calculator接口；在该类中，声明Claculator类型的属性，用于注入CalculatorImpl对象，去调用相应的业务逻辑方法。

我们通过调用CalculatorStaticProxy中的add()方法，从而实现对ClaculatorImpl中add()方法的调用，并且还会去打印相关的日志信息。

CalculatorStaticProxy类如下所示，这里以add()方法为例：

```java
public class CalculatorStaticProxy implements Calculator{
    //要把被代理的目标对象传递进来
    private Calculator calculator;


    public CalculatorStaticProxy(Calculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public int add(int i, int j) {
        //输出日志
        System.out.println("[日志] add 方法开始了，参数是：" + i + "," + j);

        //调用目标对象的方法实现核心业务逻辑
        int addResult = calculator.add(i, j);

        System.out.println("[日志] add 方法结束了，结果是：" + addResult);
        return addResult;
    }
}
```

我们可以看到，这里实现了代理。

我们可以通过创建CalculatorStaticProxy类型对象，在创建时传入CalculatorImpl对象，这样我们在调用CalculatorStaticProxy对象的add()方法时，在该add()方法中会去调用CalculatorImpl中的add()方法。

并且在CalculatorStaticProxy方法中，将附加日志功能与核心业务功能分离开了，核心业务逻辑代码存放在具体的Calculator实现类中，而这个CalculatorStaticProxy仅仅只是用于代理，帮助我们去维护附加功能，并且代理调用核心业务逻辑。

但是，这里也存在着问题。

> **问题**
>
> 1. 代码写死了，不具备灵活性。比如如果在其他类或其他方法中也需要附加日志，这里的`System.out.println("[日志] add 方法开始了，参数是：" + i + "," + j);`完全就是为Calculator接口实现类的add()方法准备的，没有通用性，其他类或其他方法中要想附加日志，还得再去声明静态代理类，再去声明相关的日志信息，此时就会产生大量的代码。
> 2. 日志功能分散，没有进行统一的管理。
> 3. 在静态代理中，我们需要在代理类中，将原始类的所有的方法，都重新实现一遍，并且为每个方法都附加相似的代码逻辑。如果要添加的附加功能的类不止一个，我们需要针对每一个类都创建一个代理类。
>
> 
>
> 针对上述的问题，我们现在进一步提出**需求**：
>
> 将日志功能集中统一到一个代理类中，将来有任何日志需求，都可以通过这一个代理类来实现。

这就需要使用动态代理技术了。



### 2.3、动态代理

上述的静态代理，特征是代理类和目标对象的类都是在**编译期间**确定下来，不利于程序的扩展。并且，**每一个代理类只能为一个接口服务**，因为需要静态类与目标类实现共同的接口，这样一来程序开发中必然产生很多的代理。最好可以通过一个代理类完成全部的代理功能。

动态代理是指客户通过代理类来调用其他对象的方法，并且是在程序**运行时**根据需要动态创建目标类的代理对象。

Java对代理模式提供了支持，在java.lang.reflect包下，提供了一个**`Proxy`**类和一个**`InvocationHandler`**的接口。

我们实现动态代理，先要去实现InvocationHandler的接口，并重写其中的invoke()方法，在invoke()方法中，就是具体要执行的逻辑。

##### 使用前提

> **`被代理类必须要有父接口，并且被代理的方法必须在父接口中声明`**。

比如，我们要去给Student类中的run()方法进行代理，给执行业务逻辑代码之前与之后加上相关日志信息：

```java
public class Student{
	public void run(){
		System.out.println("run..........");	
	}
}
```

那么，我们就必须要去给Student创建一个接口，比如说是Person接口，在Person接口里面要去相同的声明run()方法，让Student类去实现该接口并重写run()。

此时，Student类才能够使用动态代理，动态代理其run()方法。

```java
public interface Person{
	public void run();
}

public class Student implements Person{
    @Override
	public void run(){
		System.out.println("run..........");	
	}
}
```



##### 实现过程

**1、实现InvocationHandler接口**

创建一个类，例如MyInvocationHandler类，去实现InvocationHandler接口，并重写其中的invoke()方法：

```java
public class MyInvocationHandler implements InvocationHandler {
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
```

我们来看看这个invoke()方法：

* 第一个参数proxy，表示的是代理类的真实代理对象（是Proxy对象，一般不使用）

* 第二个参数method，是我们实际上要调用的某个对象的方法的Method对象

* 第三个参数args，即我们要调用方法的参数。

在该invoke()方法中，使用反射的方式去调用方法，即：`method.invoke(obj, args)`。

这里的obj，就是实际调用方法的对象，我们可以通过在InvocationHandler的实现类中创建相应的属性，通过构造器的方式传入目标对象，也就是要被代理的对象。即：

```java
public class MyInvocationHandler implements InvocationHandler {
    private Object target;

    public MyInvocationHandler(Object target){
        this.target = target
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //...前置代码
        
        Object result = method.invoke(target, args);
   
		//...后置代码
        
        return result;
    }
}
```

在该invoke()方法中，我们只需要执行Method类的反射相关方法：`method.invoke(target, args)`，该方法实际就是去执行相关的业务逻辑代码，就比如Student类中的run()。可以在该操作之前以及之后，添加我们所需要的相关附加功能，比如日志功能。

这里，我使用的是Object类型的属性，是为了提高该动态代理的通用性，让任何类型的对象都可以去使用该MyInvocationHandler类，使用其中的invoke()方法执行相关的前置与后置操作。



**2、调用Proxy.newProxyInstance()方法创建代理对象**

Proxy是`java.lang.reflect`包下的类，该类中的newProxyInstance()静态方法，用于创建代理对象。

返回的结果就是实际的代理对象，该代理对象使用的是父接口类型去接收。比如对于之前说明的Student与Person例子，在Student类的run()方法中，是实际要执行的业务逻辑；Person是Student的父接口，其中也声明了run()方法。此时，我们就需要使用Person父接口类型去接收Proxy.newProxyInstance()方法，然后调用父接口对象的run()方法，完成代理。

该方法参数如下所示：

![image-20240517133401826](.\images\image-20240517133401826.png)

newProxyInstance()方法中包含三个参数，分别是：

* ClassLoader loader：加载动态代理生成代理类的类加载器
* Class<?>[] interfaces：目标对象所实现的所有接口的Class类型数组
* InvocationHandler h：设置代理对象实现目标对象方法的过程

前两个参数很好理解，分别是**加载目标类的类加载器**以及**目标类所有接口的Class对象集合**。这两个参数可以通过**`目标类的Class对象.getClassLoader()`**和

**`目标类的Class对象.getInterfaces()`**方式去获取。

最后这个InvocationHandler参数，由于InvocationHandler是一个接口，所以我们可以现场去new一个其匿名实现类的对象（无法重用）：

`new InvocationHandler(){`
    `@Override`
    `public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {`
        `return null;`
    `}`
`}`

这样的话，就只对当前位置有效，其他位置就无法使用该代理逻辑。

也可以事先创建其实现类对象再使用（可以重用）。

例如：

```java
MyInvocationHandler myInvocationHandler = new MyInvocationHandler(new CalculatorImpl());

Calculator calculator = (Calculator) Proxy.newProxyInstance(CalculatorImpl.class.getClassLoader(), CalculatorImpl.class.getInterfaces(), myInvocationHandler);
```

在上例中，实际的业务逻辑是在CalculatorImpl类的方法中的，该类是Calculator接口的实现类。我们使用Proxy.newProxyInstance()方法所创建的代理对象，需要使用CalculatorImpl的父接口Calculator来接收，实际执行代理方法也是使用Calculator去执行的，所以被代理的方法也需要在Calculator接口中进行声明才能调用。



**3、调用代理方法**

创建完代理对象后，就可以去通过该接收的接口对象，去调用实际的方法了。

实际上执行的方法，是InvocationHandler实现类中，所声明的invoke()方法，使用反射去调用业务逻辑方法。





**案例：通过动态代理的方式实现计算器类的代理**

比如，我们想通过动态代理，实现计算器的加法操作，想要去实现下面的add()方法：

```java
public int add(int i, int j) {

    System.out.println("[日志] add 方法开始了，参数是：" + i + "," + j);

    int result = i + j;

    System.out.println("方法内部 result = " + result);

    System.out.println("[日志] add 方法结束了，结果是：" + result);

    return result;
}
```

其中：

int result = i + j;System.out.println("方法内部 result = " + result);这两段是核心业务代码

System.out.println("[日志] add 方法开始了，参数是：" + i + "," + j);System.out.println("[日志] add 方法结束了，结果是：" + result);这两段代码是附加日志功能

如果我们想使用动态代理的方式去实现上述的功能，需要将核心业务逻辑和附加日志功能分开，可以这样来完成：

首先，先去声明核心业务逻辑的类，如就叫CalculatorImpl类，在该类中创建add()方法，声明核心业务逻辑；使用动态代理，需要类拥有一个父接口，并且在父接口中需要声明核心业务逻辑的方法。那么，我们就给该类创建一个父接口Calculator，在该类中声明add()方法：

```java
public interface Calculator {
    int add(int i, int j);
}


public class CalculatorImpl implements Calculator {

    @Override
    public int add(int i, int j) {

        //只包含核心业务逻辑代码
        int result = i + j;
        System.out.println("方法内部 result = " + result);

        return result;
    }
}
```

然后，我们需要去创建InvocationHandler接口的实现类，比如叫MyInvocationHandler，将相关的附加功能声明在该类中的invoke()方法中，并使用反射的方式，调用Method参数的invoke()方法，传入实际执行的对象，执行核心业务逻辑。

```java
public class MyInvocationHandler implements InvocationHandler {
    private Object obj;

    public MyInvocationHandler(Object obj){
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //...前置代码
        System.out.println("[日志] " + method.getName() + " 方法开始了，参数是：" + Arrays.toString(args));

        //核心业务逻辑执行
        Object result = method.invoke(obj, args);

        //...后置代码
        System.out.println("[日志] " + method.getName() + " 方法结束了，结果是：" + result);
        return result;
    }
}
```

此时，InvocationHandler接口的实现类就创建好了。

注意其中几个细节，其中invoke()方法中，我们还需要调用method.invoke()方法，该方法使用了反射的方式，帮助我们实际去调用obj对象中的相关方法，这里的obj对象，是在创建MyInvocationHandler对象时指定的，调用哪一种方法，是我们在实际使用代理对象调用指定方式时确定的。

之后，我们再去调用Proxy.newProxyInstance()方法，获取到代理对象，从而执行代理方法：

```java
//创建被代理对象
CalculatorImpl calculatorImpl = new CalculatorImpl();

//创建InvocationHandler实现类对象
MyInvocationHandler myInvocationHandler = new MyInvocationHandler(calculatorImpl);

//调用Proxy.newProxyInstance()方法，获取代理对象，使用被代理类的父接口接收
Calculator calculator = (Calculator) Proxy.newProxyInstance(CalculatorImpl.class.getClassLoader(), CalculatorImpl.class.getInterfaces(), myInvocationHandler);

//调用指定代理方法
calculator.add(1, 2);
```

这里创建了CalculatorImpl类对象作为MyInvocationHandler构造器的参数传入到MyInvocationHandler对象中，那么在MyInvocationHandler中，实际上执行的核心业务逻辑方法，也就是CalculatorImpl对象中的方法。

之后使用代理对象调用add()代理方法，那么在MyInvocationHandler类的invoke()方法中，实际调用的核心业务逻辑method.invoke()方法，就是CalculatorImpl类中的add()方法。

执行结果：

<img src=".\images\image-20240517164727447.png" alt="image-20240517164727447" style="zoom:80%;" /> 





**存在的局限性**

JDK动态代理虽然功能强大，但也存在一些局限性。首先，它只能代理实现了接口的类，而不能代理没有实现接口的类。其次，JDK动态代理生成的代理类是final类，因此无法被继承。最后，JDK动态代理只能代理方法调用，而不能代理字段访问。



## 3、AOP概念及相关术语

### 概述

AOP是一种设计思想，是软件设计领域中的面向切面编程，它是面向对象编程的一种补充和完善，它以预编译方式和运行期动态代理方式实现，在不修改源代码的情况下，给程序动态统一添加额外功能的一种技术。利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。

### 相关术语

#### ①横切关注点

分散在各个模块中解决同样的问题，如用户验证、日志管理、事务管理、数据缓存都属于横切关注点。

从每个方法中抽取出来的同一类非核心业务。在同一个项目中，我们可以使用多个横切关注点对相关方法进行多个不同方面的增强。

这个概念不是语法层面的，而是根据附加功能的逻辑上的需要：有十个附加功能，就有十个横切关注点。

#### ②通知（增强）

通知，也叫作增强，通俗地说，就是你想要增强的功能，比如安全、事务、日志等。

每一个横切关注点上要做的事情都需要写一个方法来实现，这样的方法就叫通知方法。

通知有五种类型：

* 前置通知：在被代理的目标方法前执行
* 返回通知：在被代理的目标方法成功结束后执行
* 异常通知：在被代理的目标方法异常结束后执行
* 后置通知：在被代理的目标方法最终结束后执行
* 环绕通知：使用try...catch...finally结构围绕整个被代理的目标方法，包括上面四种通知对应的所有位置

![images](.\images\img020.png)

#### ③切面

封装通知方法的类。

![images](.\images\img021.png)

#### ④目标

被代理的目标对象。

#### ⑤代理

向目标对象应用通知之后创建的代理对象。

#### ⑥连接点

这是一个纯逻辑概念，不是语法定义的。

**通俗地说，就是Spring允许你使用通知的地方**。比如在方法前，方法后，这些地方都是连接点。

#### ⑦切入点

**实际上要去增强的方法，叫做切入点。**



### 作用

* 简化代码：把方法中固定位置的重复代码抽取出来，让被抽取的方法更专注于自己的核心功能，提高内聚性。
* 代码增强：把特定的功能封装到切面类中，看哪里有需要，就往上套，被套用了切面逻辑的方法就被切面给增强了。



## 4、基于注解的AOP（:star:)

### 技术说明

![images](.\images\img023.png)

动态代理分为：`JDK动态代理`与`cglib动态代理`

![image-20221216132844066](.\images\image-20221216132844066.png)

当目标类有接口的情况使用JDK动态代理和cglib动态代理，没有接口时只能使用cglib动态代理。

JDK动态代理动态生成的代理类会在com.sum.proxy包下，类名为$proxy1，和目标类实现相同的接口。

cglib动态代理生成的代理类会和目标在相同的包下，会继承目标类

JDK的动态代理，需要被代理的目标类必须实现接口。因为这个技术要求**代理对象和目标对象实现同样的接口**。

cglig：通过**继承被代理的目标类**实现代理，所以不需要目标类实现接口。

AspectJ：是AOP思想的一种思想。本质上是静态代理，将代理逻辑“织入”被代理的目标类编译得到的字节码文件，所以最终效果是动态的。Spring只是借用了AspectJ中的注解。







---

### 数据准备

**引入相关依赖**

注意，如果你已经在IOC部分中引入过，则下面的spring-context依赖与junit依赖则不需再引入。

```xml
<dependencies>
    
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>6.0.6</version>
    </dependency>
    
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-aspects</artifactId>
        <version>6.0.6</version>
    </dependency>

    <!--junit5测试-->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.3.1</version>
    </dependency>
    
</dependencies>
```

**准备被代理的目标资源**

创建一个计算器的接口Calculator，用于计算加减乘除：

```java
public interface Calculator {
    int add(int i, int j);

    int sub(int i, int j);

    int mul(int i, int j);

    int div(int i, int j);
}
```

创建Calculator的实现类CalculatorImpl，在该类中对方法具体实现：

```java
public class CalculatorImpl implements Calculator {

    @Override
    public int add(int i, int j) {

        int result = i + j;

        System.out.println("方法内部 result = " + result);

        return result;
    }

    @Override
    public int sub(int i, int j) {

        int result = i - j;

        System.out.println("方法内部 result = " + result);

        return result;
    }

    @Override
    public int mul(int i, int j) {

        int result = i * j;

        System.out.println("方法内部 result = " + result);

        return result;
    }

    @Override
    public int div(int i, int j) {

        int result = i / j;

        System.out.println("方法内部 result = " + result);

        return result;
    }
}
```

这样一来，准备工作就已完成。







---

### 开启AspectJ动态代理

在配置切面类之前，我们需要去创建一个配置文件，用于配置组件扫描规则，以及配置AspectJ的动态代理功能。比如，该配置文件就叫做`bean.xml`。

![image-20240517200326852](.\images\image-20240517200326852.png) 

首先，我们先去配置包的扫描规则，我们之前都学习过，我们需要引入context命名空间，然后使用context:component-scan标签，指定扫描哪些包及其所有子包：

![image-20240517200313642](.\images\image-20240517200313642.png)

对于开启AspectJ的动态代理功能，为目标对象自动生成代理对象，通俗点说，也就是让Spring认识动态代理所使用的注解，比如@Aspect。在开启AspectJ动态代理功能前，我们需要在配置文件中引入aop命名空间。

**引入aop命名空间**

与引入context命名空间一样，我们需要在Spring的xml配置文件的beans标签中，声明xmlns:aop类型的属性：

```xml
xmlns:aop="http://www.springframework.org/schema/aop"
```

并在xsi:schemaLocation属性中，添加下面的代码，用于引入aop命名空间的约束：

```xml
http://www.springframework.org/schema/aop 
http://www.springframework.org/schema/aop/spring-aop.xsd
```

然后，再去开启动态代理。

**开启动态代理的配置**

```xml
<!--开启aspectj自动代理，为目标对象生成代理-->
<aop:aspect-autoproxy />
```

至此，AspectJ的动态代理功能就开启了。

这样一来，Spring的xml配置文件最终如下所示：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns:context="http://www.springframework.org/schema/context"
xmlns:aop="http://www.springframework.org/schema/aop"
xsi:schemaLocation="http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd
http://www.springframework.org/schema/context
http://www.springframework.org/schema/context/spring-context.xsd
http://www.springframework.org/schema/aop
http://www.springframework.org/schema/aop/spring-aop.xsd">

    <!--组件扫描-->
    <context:component-scan base-package="com.atguigu.spring6.aop.anoaop" />

    <!--开启动态代理-->
    <aop:aspectj-autoproxy />
</beans>
```









---

### 创建切面类

**`切面类，即封装增强方法的类。`**

创建切面类，切面类就是去存储增强方法的类，比如在方法执行之前去打印的日志信息，方法执行完成之后执行的日志信息等等都是存放在切面类中。

对于切面类来说，需要使用`@Aspect`与`@Component`注解进行修饰。其中，@Aspect注解表示当前类是一个切面类，@Component注解用于将这个切面类放入IOC容器中，让IOC容器帮助我们去创建。

比如，这个切面类用于打印日志信息，那我们将这个类命名为LogAspect：

```java
@Aspect//切面类
@Component//IOC容器
public class LogAspect {
}
```

之后，就是具体的通知方法。









---

### 创建通知方法并演示（重点）

#### 通知的类型

我们在之前学习过，通知类型包括：`前置通知`、`返回通知`、`异常通知`、`后置通知`以及`环绕通知`。每一个通知都是使用一个方法来实现的，我们在方法上使用相应的注解，通过这个注解来表示当前方法的通知类型，也就是方法执行的时机。

* **`@Before`：前置通知**。在被代理的目标方法前执行。

* **`@AfterReturning`：返回通知**。在被代理的目标方法成功结束后执行。

* **`@AfterThrowing`：异常通知**，在被代理的目标方法异常结束后执行。

* **`@After`：后置通知**，在被代理的目标方法最终结束后执行。

* **`@Around`：环绕通知**，使用try...catch...finally结构围绕整个被代理的目标方法，包括上面四种通知对应的所有位置。

> 各个通知的执行顺序：
>
> * 前置通知
> * 目标操作
> * 返回通知或异常通知
> * 后置通知





#### 切入点表达式

在使用通知注解时，我们需要去指明对哪些方法使用通知。在这些注解中，包含`value`属性，该value属性需要输入一个**`切入点表达式`**，使用切入点表达式来配置切入点（也就是要去增强的方法）。

![images](.\images\img025.png)

上图就是一个完整的切入点表达式。

切入点表达式是用于精准定位方法的，所以表达式中包含了方法的权限修饰符、方法返回值、方法所在类的全类名、方法名以及参数列表。

**语法：**

**`execution([权限修饰符][方法返回值] 包名.类名.方法名(参数列表))`**

* 权限修饰符
  * 可以使用public，表示仅限于公共方法；
  * 一般省略，表示任意方法。

* 返回类型，不可省略。

  * 可以使用void，表示没有返回值的方法；

  * String，表示返回字符串类型的方法；

  * *，表示任意方法。

若一开始使用*，则表示方法的权限修饰符和返回值任意。

* 包名、类名与方法名都可以使用*来表示，表示任意包、任意类与任意方法。
  * 包使用`*..`表示包名任意同时包的层次深度任意
  * 类的部分使用`*`代替，例如*Service，表示匹配以Service结尾的类或接口
  * 方法名部分用`*`代替，例如get*，表示匹配以get开头有的方法
* 参数列表使用(..)表示任意参数。

例如：

将指定包com.atguigu下所有类的所有方法进行增强，其切入点表达式例为：

```java
execution(* com.atguigu.*.*(..))
```

或者对指定包com.atguigu.service.impl下的指定类UserServiceImpl中所有方法进行增强，其切入点表达式例为：

```
execution(* com.atguigu.service.impl.UserServiceImpl.*(..))
```

对任意包下以Service为结尾的类中的任意以int为最后参数的方法匹配：

```
execution(* *..*Service.*(.., int))
```









---

#### 五种通知的创建

##### ①前置通知

给CalculatorImpl类（全类名为：com.atguigu.spring6.aop.anoaop.CalculatorImpl）中的所有方法添加前置通知。

在前置通知方法中，可以声明一个**`JoinPoint`**类型的参数，该类型用于表示切入点（也就是被增强的方法），使用该参数可以获取到一些通知的相关信息，比如：

`joinPoint.getSignature().getName()`：获取切入方法名

`joinPoint.getArgs()`：获取切入方法的参数

**创建前置通知方法**

```java
@Aspect
@Component
public class LogAspect {
    @Before("execution(* com.atguigu.spring6.aop.anoaop.CalculatorImpl.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        
        //获取方法的参数
        Object[] args = joinPoint.getArgs();
        
        System.out.println("Logger-->前置通知，方法名称：" + methodName + "，参数：" + Arrays.toString(args));
    }
}
```

**测试**

创建一个测试方法testAdd()，在测试方法中，也是先去加载配置文件，然后通过IoC容器的方式去获取bean对象，通过bean对象调用方法查看是否有前置通知：

```java
ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");

Calculator calculator = context.getBean(Calculator.class);

calculator.add(1, 2);
```

注意，在使用时，需要将CalculatorImpl类加入到IoC容器中，即需要将该类使用@Component进行修饰：

<img src=".\images\image-20240517212404273.png" alt="image-20240517212404273" style="zoom:67%;" /> 

执行结果：

![image-20240517215255933](C:\Users\14036\AppData\Roaming\Typora\typora-user-images\image-20240517215255933.png) 

我们可以看到，不仅去执行了CalculatorImpl类中的add()方法，在执行该业务逻辑代码执行，还执行了前置通知beforeMethod()方法，此时就实现了动态代理的功能。







---

##### ②后置通知

后置通知的创建与前置通知类似，后置通知使用的是**`@After`**注解来表示。

在该通知方法中也包含`JoinPoint`参数，用于去表示切入点方法，可以去获取切入点方法的相关信息。

> **注意：**
>
> 后置通知在目标方法结束后执行。
>
> 这里的结束既可以是正常结束，即执行完整个方法后正常返回结束；
>
> 也可以是异常退出后结束，即目标方法中出现异常后返回结束。
>
> 上述两种情况都会去执行后置通知，即**`后置通知一定会执行`**，无论是否出现异常。就有些类似于try-catch-finally中的finally。

**创建后置通知方法**

给CalculatorImpl类中的所有方法添加后置通知：

```java
@After("execution(* com.atguigu.spring6.aop.anoaop.CalculatorImpl.*(..))")
public void afterMethod(JoinPoint joinPoint){
    //获取方法名
    String methodName = joinPoint.getSignature().getName();
    
    System.out.println("Logger-->后置通知，方法名称：" + methodName);
}
```



**测试**

```java
ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");

Calculator calculator = context.getBean(Calculator.class);

calculator.add(1, 2);
```

执行结果：

![image-20240517220931616](.\images\image-20240517220931616.png) 

红框所示就是后置通知所打印的信息。





---

##### ③返回通知

返回通知的方法使用**`@AfterReturning`**注解来修饰

返回通知在后置通知之前执行。

返回通知的特殊之处在于：返回通知可以去**获取目标方法的返回值**：

> 通过在@AfterReturning注解中使用**`returning`**属性去给目标方法的返回参数设置参数名例如result，在通知方法的参数中使用同名的参数来表示从而获取到目标方法的返回结果，一般来说，表示目标方法返回结果的形参使用**`Object`**类型接收。
>
> **`返回通知在后置通知之前执行，并且返回通知只有在目标方法正常执行返回后才会执行`**

在返回通知中，同样也能传入`JoinPoint`参数，使用该参数获取目标方法的信息。

**创建返回通知方法**

```java
@AfterReturning(value = "execution(* com.atguigu.spring6.aop.anoaop.CalculatorImpl.*(..))", returning = "result")
public void afterReturningMethod(JoinPoint joinPoint, Object result){
    //获取方法名
    String methodName = joinPoint.getSignature().getName();

    System.out.println("Logger-->返回通知，方法名称：" + methodName + "，返回结果：" + result);
}
```

在上例中，在@AfterReturning注解中使用了returning属性，该属性用于指明在afterReturningMethod通知方法中，使用result参数来表示目标方法的返回值。为了目标方法返回值类型的灵活性，使用Object类型进行声明。



**测试**

```java
ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
Calculator calculator = context.getBean(Calculator.class);
calculator.add(1, 2);
```

执行结果：

![image-20240517221205421](.\images\image-20240517221205421.png) 

红框所示就是返回通知打印的结果。

可以看到，返回通知，在后置通知之前，在业务方法执行之后，并且可以获取到目标方法所返回的结果。





---

##### ④异常通知

该通知方法使用**`@AfterThrowing`**注解实现。

表示当目标方法出现异常时，该通知会去执行。也就是说，当未出现异常时，该通知方法就不会执行。

异常通知特殊点在于，我们可以在通知方法中**获取目标方法所出现的异常的信息**：

> 通过@AfterThrowing注解中的属性**`throwing`**，该属性用于设置通知方法的参数名，用来将通知方法的某个**`Throwable`**类型的参数，接收成目标方法所出现的异常。

在返回通知中，同样也能传入`JoinPoint`参数，使用该参数获取目标方法的信息。

**创建异常通知方法**

```java
@AfterThrowing(value = "execution(* com.atguigu.spring6.aop.anoaop.CalculatorImpl.*(..))", throwing = "ex")
public void afterThrowingMethod(JoinPoint joinPoint, Throwable ex){
    //获取方法名
    String methodName = joinPoint.getSignature().getName();
    
    System.out.println("Logger-->异常通知，方法名称：" + methodName + "，异常信息：" + ex);
}
```



**测试**

由于异常通知只有在目标方法出现异常时才会执行，所以我们故意在目标方法中添加一个异常进行测试：

<img src=".\images\image-20240517223953033.png" alt="image-20240517223953033" style="zoom: 50%;" /> 

测试代码：

```java
ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");

Calculator calculator = context.getBean(Calculator.class);

calculator.add(1, 2);
```

执行结果：

![image-20240517224059041](.\images\image-20240517224059041.png)

我们可以看到，出现了异常，异常通知执行了，可以打印出异常的信息；并且就算出现了异常，后置通知依然可以执行，因为后置通知是在方法结束之后执行，这个结束既可以是正常返回结束，也可以是出现异常结束执行。后置通知类似于Java中的try-catch-finally中的finally，即**后置通知一定会被执行**。

但是这里没有出现返回通知，因为返回通知是在目标方法正常执行返回后执行的，所以这里不会去执行返回通知。即**返回通知只有在目标方法正常执行返回后才会执行**。





---

##### ⑤环绕通知

环绕通知，使用注解**`@Around`**实现

使用环绕通知能够实现上述的全部四种通知的效果。

> 与之前四种通知不同的是，环绕通知中只传入一个参数：**`ProceedingJoinPoint`**类型参数。ProceedingJoinPoint类型是JoinPoint类型的子类，所以我们同样也可以使用ProceedingJoinPoint类来获取目标方法的信息。
>
> 该对象在环绕通知方法中调用**`proceed()`**方法，用于表明目标方法的执行，该proceed()方法有一个Object类型的返回值，用于接收目标方法的返回值。
>
> 在环绕通知中，我们通常会使用**`try-catch-finally`**语句：
>
> * **try语句：**
>
>   存入proceed()方法执行，用于表示目标方法的执行；该方法执行前的执行语句类似于前置通知，在该方法后的执行语句类似于返回通知。
>
> * **catch语句：**
>
>   catch语句中的执行语句类似于异常通知。
>
> * **finally语句：**
>
>   finally语句中的执行语句类似于后置通知。



**创建环绕通知方法**

```java
@Around(value = "execution(* com.atguigu.spring6.aop.anoaop.CalculatorImpl.*(..))")
public Object aroundMethod(ProceedingJoinPoint joinPoint){
    //获取目标方法名
    String methodName = joinPoint.getSignature().getName();
    //获取目标方法的参数
    Object[] args = joinPoint.getArgs();
	
    //目标方法的返回值
    Object result = null;
    try {
        System.out.println("环绕通知-->目标对象方法执行之前");
        //目标方法执行
        result = joinPoint.proceed();
        System.out.println("环绕通知-->目标对象方法返回值之后");
    } catch (Throwable throwable) {
        throwable.printStackTrace();
        System.out.println("环绕通知-->目标对象方法出现异常时");
    } finally {
        System.out.println("环绕通知-->目标对象方法执行完毕");
    }
    //可以返回给调用者，也可以不返回
    return result;
}
```



**测试**

将之前四个通知先注释一下，然后再进行测试：

```java
ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");

Calculator calculator = context.getBean(Calculator.class);

calculator.add(1, 2);
```

执行结果：

![image-20240517232702260](.\images\image-20240517232702260.png) 



##### 切面方法最终实现

```java
@Aspect//切面类
@Component//IOC容器
public class LogAspect {
    @Before("execution(* com.atguigu.spring6.aop.anoaop.CalculatorImpl.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        //获取方法的参数
        Object[] args = joinPoint.getArgs();
        System.out.println("Logger-->前置通知，方法名称：" + methodName + "，参数：" + Arrays.toString(args));
    }


    @After("execution(* com.atguigu.spring6.aop.anoaop.CalculatorImpl.*(..))")
    public void afterMethod(JoinPoint joinPoint){
        //获取方法名
        String methodName = joinPoint.getSignature().getName();

        System.out.println("Logger-->后置通知，方法名称：" + methodName);
    }


    @AfterReturning(value = "execution(* com.atguigu.spring6.aop.anoaop.CalculatorImpl.*(..))", returning = "result")
    public void afterReturningMethod(JoinPoint joinPoint, Object result){
        //获取方法名
        String methodName = joinPoint.getSignature().getName();

        System.out.println("Logger-->返回通知，方法名称：" + methodName + "，返回结果：" + result);
    }


    @AfterThrowing(value = "execution(* com.atguigu.spring6.aop.anoaop.CalculatorImpl.*(..))", throwing = "ex")
    public void afterThrowingMethod(JoinPoint joinPoint, Throwable ex){
        //获取方法名
        String methodName = joinPoint.getSignature().getName();

        System.out.println("Logger-->异常通知，方法名称：" + methodName + "，异常信息：" + ex);
    }


    @Around(value = "pointcut()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint){
        //获取目标方法名
        String methodName = joinPoint.getSignature().getName();
        //获取目标方法的参数
        Object[] args = joinPoint.getArgs();

        Object result = null;
        try {
            System.out.println("环绕通知-->目标对象方法执行之前");
            //目标方法执行，目标方法的返回值一定要返回给外界调用者
            result = joinPoint.proceed();
            System.out.println("环绕通知-->目标对象方法返回值之后");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("环绕通知-->目标对象方法出现异常时");
        } finally {
            System.out.println("环绕通知-->目标对象方法执行完毕");
        }
        return result;
    }
}
```





---

#### 切入点的重用

当我们创建一个类作为增强类，并且在其中创建了很多的增强方法，若这些方法有很多都是为同一个包下的某一个方法服务时，此时如果在这些注解中都一个一个地将包名、方法名每次都声明一次，会很麻烦。这个时候，我们可以在这个类中定义切入点，直接使用切入点对应的方法来代替，这样会比较轻松。

**切入点方法的格式：**

```java
@Pointcut("execution(* 包名.类名.方法(...))")
public void 方法名(){}
```

这个时候，我们就可以直接在通知注解中，使用该方法代替原本的切入点表达式。

**使用案例**

```java
@Controller
@Aspect
public class PersonProxy {

    @Pointcut("execution(* com.rudywork.AOPTest2.*.*(..))")
    public void pointcut(){}

    @Before("pointcut()")
    public void before(){
        System.out.println("before。。。。");
    }

    @After("pointcut()")
    public void after(){
        System.out.println("after.....");
    }

    @AfterReturning("pointcut()")
    public void afterReturning(){
        System.out.println("afterReturning.....");
    }


    @AfterThrowing("pointcut()")
    public void afterThrowing(){
        System.out.println("afterThrowing.........");
    }

    @Around("pointcut()")
    public void around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("环绕方法之前.....");
        proceedingJoinPoint.proceed();
        System.out.println("环绕方法之后。。。。");
    }
}
```

如在上例中定义了一个切入点方法，我们只需要在要使用该切入点方法所表示的切入点表达式时，使用该方法名代替原本的切入点表达式即可。





---

### 切面的优先级

相同目标方法上同时存在多个切面时，切面的优先级控制切面的**内外嵌套**顺序。

* 优先级高的切面：外面
* 优先级低的切面：里面

使用@Order注解可以控制切面的优先级：

* @Order(较小的数)：优先级高
* @Order(较大的数)：优先级低

![images](.\images\img026.png)





## 5、基于XML的AOP（了解即可）

### 准备工作

参考基于注解实现AOP，只是在配置文件中，就不需要再去开启AspectJ动态代理功能，即此时不需要使用到AspectJ中的相关注解。

**引入相关依赖**

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>6.0.6</version>
    </dependency>
    
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-aspects</artifactId>
        <version>6.0.6</version>
    </dependency>

    <!--junit5测试-->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.3.1</version>
    </dependency>

</dependencies>
```

创建Spring的xml配置文件，开启组件扫描：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <!--组件扫描-->
    <context:component-scan base-package="com.atguigu.spring6.aop.xmlaop" />
   
</beans>
```

**创建用于测试的类**

创建计算器接口Calculator，在该接口中声明加减乘除的方法，并创建其实现类CalculatorImpl，在实现类的方法中写核心业务代码，并使用@Component注解将该实现类加入到IoC容器中：

```java
public interface Calculator {
    int add(int i, int j);

    int sub(int i, int j);

    int mul(int i, int j);

    int div(int i, int j);
}



@Component
public class CalculatorImpl implements Calculator {

    @Override
    public int add(int i, int j) {

        int result = i + j;

        System.out.println("方法内部 result = " + result);

        return result;
    }

    @Override
    public int sub(int i, int j) {

        int result = i - j;

        System.out.println("方法内部 result = " + result);

        return result;
    }

    @Override
    public int mul(int i, int j) {

        int result = i * j;

        System.out.println("方法内部 result = " + result);

        return result;
    }

    @Override
    public int div(int i, int j) {

        int result = i / j;

        System.out.println("方法内部 result = " + result);

        return result;
    }
}
```





### 实现

**创建切入面类**

我们需要先去创建一下切面类，在这个切面类中，不再使用@Aspect与通知的相关注解@Before等，直接创建方法即可。

下面就是一个切面类的示例：

```java
@Component//IOC容器
public class LogAspect {
    
    //切入点方法
    public void pointcut(){}

    //前置通知方法
    public void beforeMethod(JoinPoint joinPoint){
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        //获取方法的参数
        Object[] args = joinPoint.getArgs();
        System.out.println("Logger-->前置通知，方法名称：" + methodName + "，参数：" + Arrays.toString(args));
    }


    //后置通知方法
    public void afterMethod(JoinPoint joinPoint){
        //获取方法名
        String methodName = joinPoint.getSignature().getName();

        System.out.println("Logger-->后置通知，方法名称：" + methodName);
    }


    //返回通知方法
    public void afterReturningMethod(JoinPoint joinPoint, Object result){
        //获取方法名
        String methodName = joinPoint.getSignature().getName();

        System.out.println("Logger-->返回通知，方法名称：" + methodName + "，返回结果：" + result);
    }


    //异常通知方法
    public void afterThrowingMethod(JoinPoint joinPoint, Throwable ex){
        //获取方法名
        String methodName = joinPoint.getSignature().getName();

        System.out.println("Logger-->异常通知，方法名称：" + methodName + "，异常信息：" + ex);
    }


    //环绕通知方法
    public Object aroundMethod(ProceedingJoinPoint joinPoint){
        //获取目标方法名
        String methodName = joinPoint.getSignature().getName();
        //获取目标方法的参数
        Object[] args = joinPoint.getArgs();

        Object result = null;
        try {
            System.out.println("环绕通知-->目标对象方法执行之前");
            //目标方法执行，目标方法的返回值一定要返回给外界调用者
            result = joinPoint.proceed();
            System.out.println("环绕通知-->目标对象方法返回值之后");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("环绕通知-->目标对象方法出现异常时");
        } finally {
            System.out.println("环绕通知-->目标对象方法执行完毕");
        }
        return result;
    }
}

```



创建好方法后，我们需要在XML的配置文件中，对这些通知方法进行配置。

**在配置文件中配置通知方法**

在配置通知方法之前，我们需要引入aop命名空间，即在xml的beans属性中，添加：

```xml
xmlns:aop="http://www.springframework.org/schema/aop"
```

然后在xsi:schemaLocation属性中添加：

```xml
http://www.springframework.org/schema/aop
http://www.springframework.org/schema/aop/spring-aop.xsd
```

即可引入。

引入完aop命名空间后，我们去使用aop:config标签，配置aop的相关信息：

```xml
<aop:config>
</aop:config>
```

在其中使用aop:aspect标签，去配置切面类的相关信息。其中有一个ref属性，用于引入切面类的bean对象，填写的值是bean对象的id值，由于我们这里的切面类是LogAspect类型，在其@Component注解中没有显式地使用value属性去设置bean对象的id值，所以该id值默认是logAspect。

```xml
<aop:config>
    <aop:aspect ref="logAspect">
    
    </aop:aspect>
</aop:config>
```

在aop:aspect标签中，就是去编写切面类中的各种方法的，其中可以编写如下的标签：

* `aop:pointcut`：配置切入点方法，重用切入点表达式。使用id属性来指定该切入点表达式的唯一标识，使用expression属性指定切入点表达式。

* `aop:before`：配置前置通知方法，使用method属性指定通知方法名，使用pointcut-ref来指定使用的切入点表达式方法，值填写的是切入点表达式方法的id值。
* `aop:after`：配置后置通知方法，使用method属性指定通知方法名，使用pointcut-ref来指定使用的切入点表达式方法，值填写的是切入点表达式方法的id值。
* `aop:after-returning`：配置返回通知方法，使用method属性指定通知方法名，使用pointcut-ref来指定使用的切入点表达式方法，值填写的是切入点表达式方法的id值。使用returning属性指定方法参数的哪一个参数用于接收目标方法的返回值。
* `aop:after-throwing`：配置异常通知方法，使用method属性指定通知方法名，使用pointcut-ref来指定使用的切入点表达式方法，值填写的是切入点表达式方法的id值。使用throwing属性指定方法参数的那一个参数用于获取异常的信息。
* `aop:around`：配置环绕通知方法，使用method属性指定通知方法名，使用pointcut-ref来指定使用的切入点表达式方法，值填写的是切入点表达式方法的id值。

根据上述的信息，将切面方法配置到xml文件中：

```xml
<aop:config>
    <!--配置切面类-->
    <aop:aspect ref="logAspect">
        <aop:pointcut id="pointCut" expression="execution(* com.atguigu.spring6.aop.xmlaop.CalculatorImpl.*(..))"/>

        <aop:before method="beforeMethod" pointcut-ref="pointCut"></aop:before>

        <aop:after method="afterMethod" pointcut-ref="pointCut"></aop:after>

        <aop:after-returning method="afterReturningMethod" returning="result" pointcut-ref="pointCut"></aop:after-returning>

        <aop:after-throwing method="afterThrowingMethod" throwing="ex" pointcut-ref="pointCut"></aop:after-throwing>

        <aop:around method="aroundMethod" pointcut-ref="pointCut"></aop:around>

    </aop:aspect>
</aop:config>
```

此时，通知方法就已经配置完毕了。

**配置文件所有信息**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns:context="http://www.springframework.org/schema/context"
xmlns:aop="http://www.springframework.org/schema/aop"
xsi:schemaLocation="http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd
http://www.springframework.org/schema/context
http://www.springframework.org/schema/context/spring-context.xsd
http://www.springframework.org/schema/aop
http://www.springframework.org/schema/aop/spring-aop.xsd">

    <!--组件扫描-->
    <context:component-scan base-package="com.atguigu.spring6.aop.xmlaop" />

    
    
    <aop:config>
        <!--配置切面类-->
        <aop:aspect ref="logAspect">
            <aop:pointcut id="pointCut" expression="execution(* com.atguigu.spring6.aop.xmlaop.CalculatorImpl.*(..))"/>

            <aop:before method="beforeMethod" pointcut-ref="pointCut"></aop:before>

            <aop:after method="afterMethod" pointcut-ref="pointCut"></aop:after>

            <aop:after-returning method="afterReturningMethod" returning="result" pointcut-ref="pointCut"></aop:after-returning>

            <aop:after-throwing method="afterThrowingMethod" throwing="ex" pointcut-ref="pointCut"></aop:after-throwing>

            <aop:around method="aroundMethod" pointcut-ref="pointCut"></aop:around>

        </aop:aspect>
    </aop:config>
</beans>
```

**测试**

```java
ApplicationContext context = new ClassPathXmlApplicationContext("xmlaop.xml");

Calculator calculator = context.getBean(Calculator.class);

calculator.add(12, 23);
```

执行结果：

![image-20240518104215729](.\images\image-20240518104215729.png) 









---

# 五、Spring整合Junit

在之前的测试方法中，几乎都能看到以下的两行代码：

```java
ApplicationContext context = new ClassPathXmlApplicationContext("xxx.xml");

Xxx xxx = context.getBean(Xxx.class);
```

这两行代码的作用是创建Spring容器，并获取指定对象。

但是每次测试都需要重复编写。针对上述问题，我们需要的是程序能够自动帮助我们创建容器。Junit单元测试拥有这个功能。

Junit无法知晓我们是否使用了Spring容器，但是Spring提供了一个运行器，可以读取配置文件（或注解）来创建容器，我们只需要告诉它配置文件的位置即可。这样一来，我们就可以通过Spring整合Junit使程序创建spring容器，而不需要每次都去手动地创建了。

> **Spring整合Junit，实际上就是：**
>
> **通过注解来代替原本使用代码的方式读取配置文件信息创建IoC容器；**
>
> **使用@Autowired注解自动注入测试类属性的方式，代替使用getBean()方法获取bean对象。**



## 数据准备

**引入依赖**

引入的依赖包括spring-context，这是spring的基础依赖；spring-test，这是spring对junit的支持相关依赖。

```xml
<dependencies>
    <!--spring基础依赖-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>6.0.6</version>
    </dependency>

    <!--spring对junit的相关支持-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId>
        <version>6.0.6</version>
    </dependency>
</dependencies>
```

**配置XML配置文件**

创建XML配置文件，开启组件扫描，将要进行测试的类所在的包放入进行组件扫描的包路径下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <!--组件扫描-->
    <context:component-scan base-package="com.atguigu.spring6.junit.junit5" />
</beans>
```

**创建类用于测试**

在com.atguigu.spring6.junit.junit5包下，创建一个User类，使用该类进行测试，在类中创建一个run()属性，并对该类使用@Component注解将类装载到IoC容器中：

```java
@Component
public class User {
    public void run(){
        System.out.println("run.........");
    }
}
```

这样一来，数据就准备完毕了。

对于之前的测试，我们想要通过IoC去获取User类型的对象，并调用其run()方法时，是这样做的：

```java
public class AopTest {
    @Test
    public void testRun(){
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        
        User user = context.getBean(User.class);
        user.run();
    }
}
```

即，每次都是去new一个ApplicationContext的实现类对象，加载Spring的配置文件，创建IOC容器，然后再去获取Bean对象实例。

Spring给我们提供了整合Junit的方式，让我们不需要手动地去加载配置文件，通过自动注入的方式获取bean对象实例。







---

## 整合Junit5

引入Junit5的相关依赖：

```xml
<!--junit5测试-->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.3.1</version>
</dependency>
```

然后，我们就将上述的普通方式修改成Spring整合Junit5的方式：

**1、使用`@ExtendWith`与`@ContextConfiguration`注解**

```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:bean.xml")
public class UserTest1 {
    @Autowired
    private User user;

    @Test
    public void testRun(){
        user.run();
    }
}
```

如上图所示，使用@ExtendWith注解与@ContextConfiguration注解实现。

其中@ExtendWith(SpringExtension.class)是固定格式，@ContextConfiguration中的value属性用于指定XML配置文件加载。

使用@Autowired注解的方式，将原本使用getBean()获取bean对象改成了使用自动注入的方式，注入想要的bean对象。

我们只需要告诉它配置文件的位置，在运行时就会去自动加载配置文件，并注入对象信息。



**2、使用`@SpringJUnitConfig`注解（推荐使用）**

上述的两种注解也可以修改成使用@SpringJUnitConfig注解的方式来完成，在该注解中，使用value属性来指定要加载配置文件的名称。

```java
@SpringJUnitConfig(locations = "classpath:bean.xml")
public class UserTest2 {
    @Autowired
    private User user;

    @Test
    public void testRun(){
        user.run();
    }
}
```

将上述两种注解，修改成使用一种注解就可以完成。

使用这种方式比较多。

运行测试结果：

![image-20240518120101851](.\images\image-20240518120101851.png) 

执行成功。

注意：上述两种导入的@Test注解应当是Junit5中的@Test注解，全类名是`org.junit.jupiter.api.Test`





---

## 整合Junit4

整合Junit4，添加的是Junit4的依赖信息：

```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13.2</version>
</dependency>
```

Spring整合Junit4，使用的是`@RunWith`注解与`@ContextConfiguration`注解来实现的。

其中，在@RunWith注解中，填写的是固定内容SpringJUnit4ClassRunner.class；在@ContextConfiguration注解中，需要在value属性中使用classpth去填写xml配置文件名，用于指定加载的配置文件：

* **`@RunWith(SpringJUnit4ClassRunner.class)`**

* **`@ContextConfiguration("classpath:文件名.xml");`**

那么，对于上述案例，使用Junit4进行整合是：

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:bean.xml")
public class UserTest3 {
    @Autowired
    private User user;

    @Test
    public void testRun(){
        user.run();
    }
}
```

注意，这里导入的@Test注解的全路径是`org.junit.Test`

执行结果：

![image-20240518120936406](.\images\image-20240518120936406.png) 

即此时，成功整合Junit4。







---

# 六、声明式事务

## 1、JdbcTemplate

Spring框架对JDBC进行了封装，使用jdbcTemplate方便实现对数据库操作。

### 准备工作

#### 1、引入依赖

首先需要引入Spring的基础框架：spring-context，只有引入该依赖，才能使用Spring的IoC容器来加载bean。

其次，对于JdbcTemplate，我们需要引入spring-jdbc的持久层支持，还需要引入数据库的相关依赖以及druid数据库连接池信息。

最后，我们可以引入junit测试：

```xml
<!--Spring的基础依赖-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>6.0.6</version>
</dependency>

<!--spring jdbc  Spring 持久化层支持jar包-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>6.0.6</version>
</dependency>

<!-- MySQL驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.30</version>
</dependency>
<!-- 数据源 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.2.15</version>
</dependency>

<!--junit5测试-->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.3.1</version>
</dependency>
```

下面三种依赖可引入也可不引入：

如果还需要Spring整合Junit进行测试，可以引入spring-test依赖；

如果需要AOP实现切面编程，给方法添加通知方法，可以引入spring-aspects依赖；

如果需要log4j2日志信息打印，可以引入log4j2的依赖信息。

```xml
<!--spring整合Junit依赖-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>6.0.6</version>
</dependency>

<!--spring的aop依赖信息-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
    <version>6.0.6</version>
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
```



#### 2、创建jdbc.properties

在工程的resources目录下创建jdbc.properties配置文件，用于配置连接MySQL数据库的信息，连接MySQL数据库下指定的数据库。

```properties
jdbc.username=root
jdbc.password=061535asd
jdbc.url=jdbc:mysql://localhost:3306/spring?characterEncoding=utf8&useSSL=false
jdbc.driverClassName=com.mysql.cj.jdbc.Driver
```

上述表示的含义就是使用druid数据库连接池的方式，连接MySQL数据库中的spring数据库，也可以去连接自己想要的数据库，这里的spring数据库会在后面初始化用于测试。

其中在url中：

characterEncoding用于指定所处理字符的解码和编码的格式，为utf8

useSSL指的是客户端和服务器之间通信的协议，当`useSSL=false`，意味着不使用SSL进行连接，数据传输是不加密的，这种连接一般适用于内部网络或可信环境。在内部网络或测试环境中，数据传输不需要加密，此时可以关闭SSL来提高连接的效率。然后在生产环境或涉及敏感信息的网络中，启用SSL可以防止敏感信息被窃取或篡改，保护数据的安全性和完整性。



#### 3、配置Spring的xml文件

创建Spring的配置文件，比如beans.xml。

由于我们需要使用到jdbc.properties文件中的数据库连接的属性信息，所以我们需要引入外部文件jdbc.properties。

##### 引入外部配置文件

引入外部配置文件的过程是：

1、引入context命名空间，并设定相关约束

2、使用context:property-placeholder属性，引入外部文件

结果如下所示：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 导入外部属性文件 -->
    <context:property-placeholder location="classpath:jdbc.properties" />

</beans>
```

引入完配置文件后，我们就需要在Spring的配置文件中，配置数据库的连接信息。



##### 配置数据库连接信息

创建DruidDataSource类型的bean对象，在其中使用外部文件jdbc.properties中的属性，配置该bean对象的属性，从而创建数据源：

```xml
<!--配置数据源-->
<bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
    <property name="username" value="${jdbc.username}" />
    <property name="password" value="${jdbc.password}" />
    <property name="url" value="${jdbc.url}" />
    <property name="driverClassName" value="${jdbc.driverClassName}" />
</bean>
```





##### 配置JdbcTemplate

创建完数据源信息后，我们还需要去配置JdbcTemplate对象，并在其中装配数据源属性：

```xml
<!--配置JdbcTemplate-->
<bean class="org.springframework.jdbc.core.JdbcTemplate" id="jdbcTemplate">
    <!--装配数据源-->
    <property name="dataSource" ref="druidDataSource" />
</bean>
```

此时，Spring的配置文件信息就全部配置完成了。

那么，Spring配置文件最终信息为：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 导入外部属性文件 -->
    <context:property-placeholder location="classpath:jdbc.properties" />

    <!--配置数据源-->
    <bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="username" value="${jdbc.username}" />
        <property name="password" value="${jdbc.password}" />
        <property name="url" value="${jdbc.url}" />
        <property name="driverClassName" value="${jdbc.driverClassName}" />
    </bean>

    <!--配置JdbcTemplate-->
    <bean class="org.springframework.jdbc.core.JdbcTemplate" id="jdbcTemplate">
        <!--装配数据源-->
        <property name="dataSource" ref="druidDataSource" />
    </bean>
</beans>
```



#### 4、准备数据库与数据表

```mysql
CREATE DATABASE `spring`;

use `spring`;

CREATE TABLE `t_emp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `sex` varchar(2) DEFAULT NULL COMMENT '性别',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

这样我们想要的数据库与数据表就创建完毕了：

<img src=".\images\image-20240518132621973.png" alt="image-20240518132621973" style="zoom: 80%;" />





#### 5、创建测试类进行测试

创建包com.atguigu.spring6.jdbc，在包下创建一个测试类JdbcTemplateTest。

在该类中整合Junit5，使用@SpringJUnitConfig注解指明加载的配置文件是beans.xml，然后使用@Autowired注解，注入配置文件中所创建的JdbcTemplate的bean对象。然后再在其中创建测试方法进行测试：

```java
@SpringJUnitConfig(locations = "classpath:beans.xml")
public class JdbcTemplateTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
}
```

自此，数据的准备工作就完成了。接下来，就是去具体使用JdbcTemplate实现数据库的CRUD操作。



---

### JdbcTemplate实现CRUD

使用JdbcTemplate实现CRUD，都是先去编写SQL语句，然后再去调用JdbcTemplate对象的相关方法，传入SQL语句和相关的参数，从而执行相关的操作。

#### 实现增删改功能

使用JdbcTemplate实现增删改功能，都是使用其**`update()`**方法来实现的，update()重载的方法有很多，目前使用的最多的是：

<img src=".\images\image-20240518144422739.png" alt="image-20240518144422739" style="zoom:50%;" /> 

表示传入sql以及sql语句需要的参数信息。

**添加操作**

例如，t_emp表如下所示：

![image-20240518144704094](.\images\image-20240518144704094.png) 

往t_emp表中插入一条数据：

```java
@SpringJUnitConfig(locations = "classpath:beans.xml")
public class JdbcTemplateTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testUpdate(){
        //第一步：编写sql语句
        String sql = "insert into t_emp values(null, ?, ?, ?)";
        //第二步：调用jdbcTemplate的方法，传入相关参数
        int result = jdbcTemplate.update(sql, "tom", "24", "男");
        
        System.out.println(result);
    }
}
```

其中?表示的是占位符，用于填入所需的参数信息，在SQL中有三个?，所以我们在使用update()方法时也需要传入三个参数。

执行完成后，t_emp表如下所示：

![image-20240518144736470](.\images\image-20240518144736470.png) 

可以看到，成功插入了一条数据。

控制台打印信息为：

![image-20240518144806603](.\images\image-20240518144806603.png) 

返回值为1，表示成功执行update()方法。

**为什么要使用?占位符？**

当要批量地添加或修改表中的数据，或进行多次类似操作时，此时使用?占位符，就可以无需多次创建sql语句，只需要将sql语句中的参数进行修改即可。

并且**只能用来为加引号''的参数（如参数值）用?占位，不可用于表名、字段名**等，否则无法生成预编译的语句对象。







**修改操作**

修改操作也是使用update()方法完成的，例如将原本插入的id为1的记录中，name属性值修改成jerry：

```java
@SpringJUnitConfig(locations = "classpath:beans.xml")
public class JdbcTemplateTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testUpdate(){
        //第一步：编写sql语句
        String sql = "update t_emp set name = ? where id = ?";
        //第二步：调用jdbcTemplate的方法，传入相关参数
        int result = jdbcTemplate.update(sql, "jerry", "1");
        System.out.println(result);
    }
}
```

打印结果为1，表示的是操作影响的数据表行数。

执行完上述方法后，我们去数据库中查看一下：

![image-20240518145423868](.\images\image-20240518145423868.png) 

可以看到，name值已经被修改成了jerry。



**删除操作**

例如，我们去删除id为1的记录：

```java
@SpringJUnitConfig(locations = "classpath:beans.xml")
public class JdbcTemplateTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testUpdate(){
        //第一步：编写sql语句
        String sql = "delete from t_emp where id = ?";
        //第二步：调用jdbcTemplate的方法，传入相关参数
        int result = jdbcTemplate.update(sql,  "1");

        System.out.println(result);
    }
}
```

打印结果：

![image-20240518145801258](.\images\image-20240518145801258.png) 

这样一来，数据库中id = 1的记录就被删除了：

![image-20240518145839402](.\images\image-20240518145839402.png) 



对上述三种操作**总结一下**：

使用JdbcTemplate实现数据库的增删改操作，都是使用update()方法来实现的，当执行成功时，update()方法会返回1；

update()方法中需要传入实际执行的sql语句以及相关的参数信息，我们主要关注其中的sql语句。





---

#### 实现查询功能

我们先去创建一个实体类，比如Emp类，该类就是去接收查询的结果。

在类中定义几个属性，分别是id、name、age与sex，并去给这些属性生成get()与set()方法，并且为了方便查看，我们再去生成一个toString()方法：

```java
public class Emp {
    private Integer id;
    private String name;
    private Integer age;
    private String sex;

    //省略get()、set()、toString()方法
}
```

> **要求：`类中的属性名必须与数据表中的列名一致，并且在类中声明了这些属性的set()方法`**





**返回对象类型**

如果需要将查询到的结果使用对象进行接收，此时需要使用到**`queryForObject()`**方法来执行查询操作。

该方法有很多重载方法，最常使用的是：

![image-20240518153252909](.\images\image-20240518153252909.png)  

第一个参数传入的是查询语句的SQL；

第二个参数传入的是RowMapper类型的参数；

第三个参数传入的是查询语句SQL占位符所对应的参数。

第一个参数和第三个参数很好理解，主要是这第二个参数，RowMapper类型。

RowMapper有一个实现类：BeanPropertyRowMapper，我们可以通过创建BeanPropertyRowMapper实现类的对象，并在创建时传入我们用来接收类的Class类型：

**`new BeanPropertyRowMapper<>(接收类.class)`**

这样的话，就能够指定我们使用哪一个类进行接收，从而将查询的各个列转换成该类中的各个属性（属性名要与列名保持一致），从而通过获取到类对象的方式得到查询的结果。

例如：

t_emp表中数据如下所示：

![image-20240518153847959](.\images\image-20240518153847959.png) 

我们去获取id为2的数据：

```java
@SpringJUnitConfig(locations = "classpath:beans.xml")
public class JdbcTemplateTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testQuery(){
        
        String sql = "select * from t_emp where id = ?";

        Emp emp = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Emp.class), 2);

        System.out.println(emp);
    }
}
```

执行结果：

![image-20240518154214365](.\images\image-20240518154214365.png) 

由结果我们可以看到，我们成功将数据库中id为2的数据查询出来，并且使用了Emp类接收，列中的值均由类中的属性接收。





**返回List集合类型**

查询数据表中的多条记录，并将其作为List集合的数据返回与查询返回对象类型差不多，方法使用的是**`query()`**。

该方法也有很多重载方法，最常用的是下面两种：

![image-20240518155455471](.\images\image-20240518155455471.png)

![image-20240518155520558](.\images\image-20240518155520558.png)

一种是不带参数的查询，一种是带参的查询。

同样地，也是在其中要传入一个RowMapper接口类型，即BeanPropertyRowMapper对象，用于指定List集合中元素的类型。

案例：查询表中的所有数据，并使用List集合接收

```java
@SpringJUnitConfig(locations = "classpath:beans.xml")
public class JdbcTemplateTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testQueryList(){
        
        String sql = "select * from t_emp";

        List<Emp> empList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Emp.class));

        for (Emp emp : empList) {
            System.out.println(emp);
        }
    }
}
```

查询结果如下所示：

![image-20240518155814151](.\images\image-20240518155814151.png) 

即成功将表中数据查询出，并将每条记录都使用了Emp类型对象接收，作为元素存入到List集合中。





**返回单个值**

```java
@Test
//查询单行单列的值
public void selectCount(){
    String sql = "select count(id) from t_emp";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
    System.out.println(count);
}
```





---

## 2、声明式事务概述

### 事务基本概念

**什么是事务？**

数据库事务是访问并可能操作各种数据项的一个数据库操作序列，这些操作要么全部执行，要么全部不执行，是一个不可分割的工作单位。事务由事务开始与事务结束之间执行的全部数据库操作组成。

**事务的特性（ACID）**

**`A：原子性（Atomicity）`**

一个事务中的所有操作，要么全部完成，要么全部不完成，不会结束在中间某个环节。事务在执行过程中发生错误，会被回滚（Rollback）到事务开始前的状态，就像这个事务从来没有执行过一样。

**`C：一致性（Consistency）`**

事务的一致性指的是一个事务执行之前和执行之后数据库都必须处于一致性状态。

如果事务成功地完成，那么系统中所有变化都将正确地应用，系统处于有效状态。

如果在事务中出现错误，那么系统中的所有变化都将自动地回滚，系统返回到原始状态。

**`I：隔离性（Isolation）`**

指的是在并发环境中，当不同的事务同时操纵相同的数据时，每个事务都有各自的完整数据空间。由并发事务所做的修改必须与任何其他并发事务所做的修改隔离。事务查看数据更新时，数据所处的状态有么是另一个修改它之前的状态，要么是另一个事务修改它之后的状态，事务不会查看到中间状态的数据。

**`D：持久性（Durability）`**

指的是只要事务成功结束，它对数据库所做的更新就必须保存下来。即使发生系统崩溃，重新启动数据库系统后，数据库还能恢复到事务成功结束时的状态。



### 编程式事务

指的就是我们自己通过代码的方式，实现事务的开启、事务的提交以及事务的回滚等功能。即使用代码的方式，实现事务的整个生命周期。

```java
Connection conn = ...;
    
try {
    
    // 开启事务：关闭事务的自动提交
    conn.setAutoCommit(false);
    
    // 核心操作
    
    // 提交事务
    conn.commit();
    
}catch(Exception e){
    
    // 回滚事务
    conn.rollBack();
    
}finally{
    
    // 释放数据库连接
    conn.close();
}
```

编程式的实现方式存在缺陷：

* 细节没有被屏蔽：具体操作过程中，所有细节都需要程序员自己来完成，比较繁琐。
* 代码复用性不高：如果没有有效抽取出来，每次实现功能都需要自己编写代码，代码就没有得到复用。



### 声明式事务

既然事务控制的代码有规律可循，代码的结果基本是确定的，所以框架就可以将固定模式的代码抽取出来，进行相关的封装。

封装起来后，我们只需要在配置文件中进行简单的配置即可完成操作。

* 好处1：提高开发效率
* 好处2：消除了冗余的代码
* 好处3：框架会综合考虑相关领域中在实际开发环境下有可能遇到的各种问题，进行了健壮性、性能等各个方面的优化

所以，我们可以总结下面两个概念：

* 编程式事务：自己写代码实现功能
* 声明式事务：通过配置让框架实现功能







---

## 3、基于注解的声明式事务（:star:）

妈的，直接看实现方式就行了，学习MySQL高级部分都把事务学的透透的了。

### 准备工作

#### 实现需求

假如说我们要去买一本书，过程应该是：

1、查询图书的价格

2、图书表库存 - 1

3、用户表余额 - 图书的价格

那么，这三个过程应该构成一个事务，保证这三个过程要么全部成功，要么全部失败。

通过上述的案例来学习基于注解的声明式事务。

#### 引入依赖

由于注解需要去连接MySQL数据库，那么我们就需要引入MySQL的相关依赖以及druid数据库连接池的依赖。

以及我们这里要使用的JdbcTemplate去操作数据库，那么我们还需要引入spring-jdbc的依赖，以及Spring的基础依赖信息spring-context。

最后，由于需要进行单元测试，所以junit5的依赖junit-jupiter-api也必不可少。后面可能还需要使用Spring去整合Junit，所以还需要引入spring-test用于支持。

所以，所有的依赖信息如下所示：

```xml
<dependencies>
    <!--spring基础依赖-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>6.0.6</version>
    </dependency>
    
    <!--spring jdbc  Spring 持久化层支持jar包-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>6.0.6</version>
    </dependency>
    
    <!-- MySQL驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.30</version>
    </dependency>
    <!-- 数据源 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.2.15</version>
    </dependency>

    <!--junit5测试-->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.3.1</version>
    </dependency>
        
    <!--spring用于整合junit的支持-->
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-test</artifactId>
 		<version>6.0.6</version>
    </dependency>

</dependencies>
```

引入了相关依赖后，我们就要开始考虑，创建一些数据用于测试了。

#### 配置文件的准备

引入依赖后，对于创建MySQL数据库连接池信息jdbc.properties以及在Spring的配置文件中引入jdbc.properties文件并配置连接信息，然后创建JdbcTemplate对象请参考JdbcTemplate中的准备工作。这里就不再阐述了。

**jdbc.properties数据库配置文件**

```properties
jdbc.username=root
jdbc.password=061535asd
jdbc.url=jdbc:mysql://localhost:3306/spring?characterEncoding=utf8&useSSL=false
jdbc.driverClassName=com.mysql.cj.jdbc.Driver
```

在Spring的配置文件beans.xml中，导入外部的数据库属性文件jdbc.properties，并且配置数据源，配置JdbcTemplate并开启组件扫描功能：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="com.atguigu.spring6.tx" />

    <!-- 导入外部属性文件 -->
    <context:property-placeholder location="classpath:jdbc.properties" />

    <!--配置数据源-->
    <bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="username" value="${jdbc.username}" />
        <property name="password" value="${jdbc.password}" />
        <property name="url" value="${jdbc.url}" />
        <property name="driverClassName" value="${jdbc.driverClassName}" />
    </bean>

    <!--配置JdbcTemplate-->
    <bean class="org.springframework.jdbc.core.JdbcTemplate" id="jdbcTemplate">
        <!--装配数据源-->
        <property name="dataSource" ref="druidDataSource" />
    </bean>
</beans>
```



#### 准备数据表

我们现在去使用用户买书的过程来演示事务操作。

我们去创建两个表，一个表是用户表，用户表包含id、name以及余额balance三个字段；

另一个表是图书表，图书表包含id、name、price以及库存stock四个字段。

```mysql
CREATE TABLE t_book (
  book_id int NOT NULL AUTO_INCREMENT COMMENT '主键',
  book_name varchar(20) DEFAULT NULL COMMENT '图书名称',
  price int DEFAULT NULL COMMENT '价格',
  stock int unsigned DEFAULT NULL COMMENT '库存（无符号）',
  PRIMARY KEY (book_id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

insert into t_book(book_id, book_name, price, stock) 
values 
(1,'斗破苍穹',80,100),
(2,'斗罗大陆',50,100);

CREATE TABLE t_user (
  user_id int NOT NULL AUTO_INCREMENT COMMENT '主键',
  username varchar(20) DEFAULT NULL COMMENT '用户名',
  balance int unsigned DEFAULT NULL COMMENT '余额（无符号）',
  PRIMARY KEY (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

insert into t_user(user_id, username, balance)
values (1,'lucy',500);
```

这样一来，用户表与图书表就已经创建完成了，并往其中分别添加了一条与两条数据：

t_user表：

![image-20240518201646120](.\images\image-20240518201646120.png) 

t_book表：

<img src=".\images\image-20240518201305182.png" alt="image-20240518201305182" style="zoom:67%;" /> 



#### 结构的搭建

在com.atguigu.spring6.tx包下，分别创建三个包：controller、service以及dao包。

在controller包下创建BookController类；

在service包下创建BookService接口，以及其实现类BookServiceImpl；

在dao包下创建BookDao接口以及其实现类BookDaoImpl。

并且，我们需要给BookController类、BookServiceImpl与BookDaoImpl加入到Spring的IoC容器中，那么我们需要给它们分别添加@Controller、@Service以及@Repository注解，并且需要在Controller类中注入Service类型的属性进行调用，在Service类中注入Dao类型的属性。

所以，最终这些类的结构与实现是这样的：

![image-20240518211943425](.\images\image-20240518211943425.png) 

BookController类

```java
package com.atguigu.spring6.tx.controller;

@Controller
public class BookController {
    @Autowired
    private BookService bookService;
}

```

BookService接口

```java
package com.atguigu.spring6.tx.service;

public interface BookService {
}
```

BookServiceImpl类

```java
package com.atguigu.spring6.tx.service;

@Service
public class BookServiceImpl implements BookService{
    @Autowired
    private BookDao bookDao;
}
```

BookDao接口

```java
package com.atguigu.spring6.tx.dao;

public interface BookDao {
}
```

BookDaoImpl类

```java
package com.atguigu.spring6.tx.dao;

@Repository
public class BookDaoImpl implements BookDao {
}
```









---

### 问题引入

#### 购买图书方法的实现

为了实现购买图书的功能，我们就需要去创建买书的相关方法了，比如说就叫做buyBook()方法，需要传入book的id与user的id。

在实际开发中，我们知道，controller层是控制层，是去调用service层的方法；service层是业务层，是实际的业务操作的；而dao层则是数据访问层，则是实际访问数据库的。

所以，我们需要在service层中创建buyBook()方法，实际编写业务逻辑操作；然后在controller层进行调用，对于访问数据库的操作，则是在dao层完成。

所以，对于BookController层来说，我们只需要在其中调用service的buyBook()方法即可：

**BookController类**

```java
@Controller
public class BookController {
    @Autowired
    private BookService bookService;

    public void buyBook(Integer bookId, Integer userId){
        bookService.buyBook(bookId, userId);
    }
}
```

对于BookService接口来说，也只是去声明一个buyBook()的抽象方法即可：

**BookService接口**

```java
public interface BookService {
    /**
     * 购买图书的方法
     * @param bookId
     * @param userId
     */
    public void buyBook(Integer bookId, Integer userId);
}
```

对于BookServiceImpl类来说，其是真正的去编写业务逻辑的类，所以，我们需要在该类的buyBook()方法中，去编写购买图书的业务逻辑：

购买图书的业务逻辑核心部分有三步：

1、根据图书的id查询图书的价格

2、更新图书表的库存量 - 1

3、更新用户表用户的余额 - 图书价格

这三步都是去操作数据库的，该实际操作是在dao中完成，我们需要去dao层中创建三个方法用于执行上述的操作，在ServiceImpl类中，就是去调用这三个方法。

**BookServiceImpl类**

```java
@Service
public class BookServiceImpl implements BookService{
    @Autowired
    private BookDao bookDao;

    @Override
    public void buyBook(Integer bookId, Integer userId) {
        //根据图书id查询图书价格
        Integer bookPrice = bookDao.getBookPriceByBookId(bookId);

        //更新图书表中的库存量 - 1
        bookDao.updateStock(bookId);

        //更新用户表的用户余额 - 图书价格
        bookDao.updateUserBalance(userId, bookPrice);
    }
}
```

**BookDao接口**

```java
public interface BookDao {
    /**
     * 根据图书id获取图书的价格
     */
    public Integer getBookPriceByBookId(Integer bookId);

    /**
     * 更新图书库存-1
     */
    void updateStock(Integer bookId);

    /**
     * 更新用户余额 - 图书价格
     */
    void updateUserBalance(Integer userId, Integer bookPrice);
}
```

然后，我们在BookDaoImpl类中，具体实现对数据库表的操作。

向BookDaoImpl中注入JdbcTemplate类型对象，并在该类中调用JdbcTemplate对象去具体实现对数据库操作的三个方法：

**BookDaoImpl类**

```java
@Repository
public class BookDaoImpl implements BookDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //根据图书id查询图书的价格
    @Override
    public Integer getBookPriceByBookId(Integer bookId) {
        String sql = "select price from t_book where book_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, bookId);
    }

    //更新图书库存量减一
    @Override
    public void updateStock(Integer bookId) {
        String sql = "update t_book set stock = stock - 1 where book_id = ?";
        jdbcTemplate.update(sql, bookId);
    }

    //更新用户余额减图书价格
    @Override
    public void updateUserBalance(Integer userId, Integer bookPrice) {
        String sql = "update t_user set balance = balance - ? where user_id = ?";
        jdbcTemplate.update(sql, bookPrice, userId);
    }
}
```

那么自此，购买图书的方法就创建完成了，我们就可以通过去调用controller层的方法从而去购买图书信息。



**测试**

创建一个测试类BookTxTest，使用Spring整合Junit的方式进行测试。

在该测试类中，我们只需要去注入一个BookController，然后调用其中的buyBook()方法，传入图书id与用户id，就能够实现买书的操作：

BookTxTest类

```java
@SpringJUnitConfig(locations = "classpath:beans.xml")
public class BookTxTest {
    @Autowired
    private BookController bookController;

    @Test
    public void testByBook(){
        bookController.buyBook(1, 1);
    }
}
```

上述所表示的含义就是用户表中id为1的用户购买了一本id为1的书。

图书表信息为：

![image-20240518215838167](.\images\image-20240518215838167.png) 

用户表信息为：

![image-20240518215853852](.\images\image-20240518215853852.png) 

执行一次上述的测试后：

图书表的信息为：

![image-20240518220118074](.\images\image-20240518220118074.png) 

可以发现其中的id为1的库存字段stock减去了1，变成了99

用户表信息为：

![image-20240518220157253](.\images\image-20240518220157253.png) 

可以发现其中的余额减去了图书的价格，变为了420

上述是正常的过程，没有出现任何问题。

现在，我们去演示一个异常的过程，引入事务的相关内容。



#### 问题演示

将t_user表中用户余额字段设置成50：

![image-20240518221051650](.\images\image-20240518221051650.png) 

此时，再去t_book表中购买id为1的书时：

![image-20240518221122040](.\images\image-20240518221122040.png) 

由于其价格price为80，此时用户余额就不够。

并且，由于我们在创建t_user表时，将balance字段设置成无符号类型的（unsigned），即其值不能为负数，所以，此时当我们去调用购买书的方法时就会报错：

![image-20240518221424257](.\images\image-20240518221424257.png)

由于执行报错，那么在我们看来，数据表中应该还是原本的样子不变。

但是，此时我们去查看一下数据表中的信息：

![image-20240518221539845](.\images\image-20240518221539845.png) 

发现t_book表中book_id为1的书的库存又减去了1，但是实际上我们并没有去购买，为什么会出现这样的问题？

购买图书业务执行的顺序是：

1、先去查询图书的价格

2、再去更新图书库存

3、最后再去更新用户的余额

出现问题的是在第三步，但是在第二步的时候就已经去修改图书库存了，在第三步出现异常时没有去将第二步操作进行回滚，这就造成了数据的非一致性问题。

所以，我们需要引入事务，将这三个操作看作一个整体，其中出现了错误，整个整体都需要进行回滚，回滚到执行之前的样子。

这就是为什么需要引入事务的原因。





---

### 添加事务的方式（重要）

> **Spring事务会关闭事务的自动提交功能，即`autocommit=OFF`。**
>
> **相当于在MySQL中使用`start transaction`的方式显式地开启事务，这样一来，其中的DML语句就不会去自动提交数据**

#### ①添加事务配置

在Spring配置文件中，引入tx命名空间并设置相关约束：

```xml
xmlns:tx="http://www.springframework.org/schema/tx"
```

```xml
http://www.springframework.org/schema/tx
http://www.springframework.org/schema/tx/spring-tx.xsd
```

<img src=".\images\image-20240518223610987.png" alt="image-20240518223610987" style="zoom:67%;" />



在Spring的配置文件中，创建`事务管理器`的bean对象，并注入druid数据库数据源：

```xml
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="druidDataSource" />
</bean>
```

之后，使用`tx:annotation-dirven`标签开启事务的注解驱动：

使用事务管理器的bean对象的id值作为其transaction-manager属性的值传入，如果事务管理器bean的id正好是`transactionManager`这个默认值，就可以省略该属性：

```xml
<tx:annotation-driven transaction-manager="transactionManager" />
```

自此，事务注解的配置就完成了。



#### ②添加事务注解

**`@Transactional`**注解

方法上添加了该注解，就表示该方法是一整个事务。

该注解可以添加在方法上，表示给该方法添加事务；

也可以添加在类上，表示给该类中的所有public方法都添加事务信息。

> **@Transactional注解推荐配置在方法上。**
>
> **`@Transactional注解一般作用在service层。`**因为service层是业务层，编写的是业务的信息，而一个方法就代表了一整个业务操作，也就是一个整体，所以推荐把@Transactional注解用在service层，把service层的方法看作一个整体来使用。



**添加事务后问题案例演示**

那么，有了@Transactional注解，我们再来实现一下问题案例：

首先，我们去给service层中的buyBook()方法添加@Transactional注解：

```java
@Service
public class BookServiceImpl implements BookService{
    @Autowired
    private BookDao bookDao;

    @Transactional
    @Override
    public void buyBook(Integer bookId, Integer userId) {
        //根据图书id查询图书价格
        Integer bookPrice = bookDao.getBookPriceByBookId(bookId);

        //更新图书表中的库存量 - 1
        bookDao.updateStock(bookId);

        //更新用户表的用户余额 - 图书价格
        bookDao.updateUserBalance(userId, bookPrice);
    }
}
```

然后我们去调用测试方法，购买图书：

```java
@SpringJUnitConfig(locations = "classpath:beans.xml")
public class BookTxTest {
    @Autowired
    private BookController bookController;

    @Test
    public void testByBook(){
        bookController.buyBook(1, 1);
    }
}
```

原用户表中的信息：

![image-20240518230744336](.\images\image-20240518230744336.png) 

原图书表中的信息：

![image-20240518230755463](.\images\image-20240518230755463.png) 

由于用户表中用户的余额不足以购买图书，并且balance属性设置成了unsigned，即一定是非负数，所以此时执行报错：

![image-20240518230842396](.\images\image-20240518230842396.png) 

执行之后图书表中的信息：

![image-20240518230857307](.\images\image-20240518230857307.png) 

发现此时图书的库存并没有减少。

即，此时方法使用了事务，当方法中出现异常时，方法的执行会进行回滚，回滚到执行之前的样子，所以在方法中对图书表库存进行减一的操作回滚了。

以上就是@Transactional注解的基本用法。







---

### 事务属性（重要）

在@Transactional注解中可以填入很多属性：

![image-20240518231836550](.\images\image-20240518231836550.png)

通过这些属性，我们可以对事务进行更多的管理。

事务的属性包括了五个方面，如图所示：

![Image](.\images\1231214321234.png) 

---

#### ①readOnly：只读

> 在默认情况下，readOnly属性是`false`，非只读。

如果我们使用`@Transactional(readOnly = true)`去修饰方法，就是告诉数据库，这个方法不涉及到写操作，该方法中**只查询**。

如果在使用了@Transactional(readOnly = true)的方法对数据库进行了修改，就会抛出异常，所以该属性也可以用来去给方法进行约束，约束其只能进行读取操作。

**使用案例**

```java
@Service
public class BookServiceImpl implements BookService{
    @Autowired
    private BookDao bookDao;

    @Transactional(readOnly = true)
    @Override
    public void buyBook(Integer bookId, Integer userId) {
        //根据图书id查询图书价格
        Integer bookPrice = bookDao.getBookPriceByBookId(bookId);

        //更新图书表中的库存量 - 1
        bookDao.updateStock(bookId);

        //更新用户表的用户余额 - 图书价格
        bookDao.updateUserBalance(userId, bookPrice);
    }
}
```

执行抛出异常：Caused by: java.sql.SQLException: Connection is read-only. Queries leading to data modification are not allowed。





---

#### ②timeout：超时

timeout属性是用来设置超时时间，单位是`秒`。

如果在使用该属性的注解所修饰的方法中，执行的时间超过了timeout所设置的时间，则**抛出异常并且回滚**。

> **`默认是-1`，即不设置超时的时间。**

**使用案例**

```java
//超时时间单位秒
@Transactional(timeout = 3)
public void buyBook(Integer bookId, Integer userId) {
    try {
        TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    //查询图书的价格
    Integer price = bookDao.getPriceByBookId(bookId);
    //更新图书的库存
    bookDao.updateStock(bookId);
    //更新用户的余额
    bookDao.updateBalance(userId, price);
    //System.out.println(1/0);
}
```

执行过程中抛出异常：

org.springframework.transaction.**TransactionTimedOutException**: Transaction timed out: deadline was Fri Jun 04 16:25:39 CST 2022







---

#### ③回滚策略

在@Transactional注解中，表示回滚策略的属性一共有四个：

* `rollbackFor`：需要设置一个Class类型对象，表示出现指定异常回滚；
* `rollbackForClassName`：需要设置一个字符串类型的全类名，表示出现指定异常回滚；
* `noRollbackFor`：需要设置一个Class类型对象，表示出现指定异常不回滚；
* `noRollbackForClassName`：需要设置一个字符串类型的全类名，表示出现指定异常不回滚。

> **声明式事务默认只针对`运行时异常`（RuntimeException类及其子类），编译时异常不回滚。**

**使用案例**

```java
@Transactional(noRollbackFor = ArithmeticException.class)
//或者可以使用：@Transactional(noRollbackForClassName = "java.lang.ArithmeticException")
public void buyBook(Integer bookId, Integer userId) {
    //查询图书的价格
    Integer price = bookDao.getPriceByBookId(bookId);
    //更新图书的库存
    bookDao.updateStock(bookId);
    //更新用户的余额
    bookDao.updateBalance(userId, price);
    System.out.println(1/0);
}
```

执行结果

虽然购买图书功能中出现了数学运算异常（ArithmeticException），但是我们设置的回滚策略是，当出现ArithmeticException不发生回滚，因此购买图书的业务逻辑操作正常执行，没有进行回滚操作。





---

#### ④isolation：隔离级别

具体的隔离级别，请查看MySQL高级中的事务管理部分。

SQL中规定了多种事务隔离级别，不同隔离级别对应不同的干扰程度，隔离级别越高，数据一致性就越好，但并发性越弱。

隔离级别一共有四种：

* `READ UNCOMMITTED`：读未提交

  允许Transaction01读取Transaction02未提交的修改。

* `READ COMMITTED`：读已提交

  要求Transaction01只能读取Transaction02已提交的修改。

* `REPEATABLE READ`：可重复读

  确保Transaction01可以多次从一个字段中读取到相同的值，即Transaction01执行期间禁止其他事务对这个字段进行更新。

* `SERIALIZABLE`：串行化

  确保Transaction01可以多次从一个表中读取到相同的行，在Transaction01执行期间，禁止其他事务对这个表进行添加、更新、删除操作。可以避免任何并发问题，但性能十分低下。

各个隔离级别解决并发问题的能力见下表：

| 隔离级别           | 脏读 | 不可重复读 | 幻读 |
| ------------------ | ---- | ---------- | ---- |
| `READ UNCOMMITTED` | 有   | 有         | 有   |
| `READ COMMITTED`   | 无   | 有         | 有   |
| `REPEATABLE READ`  | 无   | 无         | 有   |
| `SERIALIZABLE`     | 无   | 无         | 无   |

**各种数据库产品对事务隔离级别的支持程度**

| 隔离级别         | Oracle  | MySQL   |
| ---------------- | ------- | ------- |
| READ UNCOMMITTED | ×       | √       |
| READ COMMITTED   | √(默认) | √       |
| REPEATABLE READ  | ×       | √(默认) |
| SERIALIZABLE     | √       | √       |

**使用方式**

```java
@Transactional(isolation = Isolation.DEFAULT)//使用数据库默认的隔离级别

@Transactional(isolation = Isolation.READ_UNCOMMITTED)//读未提交

@Transactional(isolation = Isolation.READ_COMMITTED)//读已提交

@Transactional(isolation = Isolation.REPEATABLE_READ)//可重复读

@Transactional(isolation = Isolation.SERIALIZABLE)//串行化
```





---

#### ⑤propagation：传播行为

事务的传播行为是在Spring中新增加的概念，在数据库中不存在。

**什么是事务的传播行为？**

它描述的内容是多个事务存在调用关系时，事务是如何在这些方法之间进行传播的。

例如：在service类中有a()方法和b()方法，a()方法上有事务，b()方法上也有事务，当a()方法执行过程中调用了b()方法，事务该如何传递？合并到一个事务里？还是开启一个新的事务？这就是事务传播行为。



假设方法A调用方法B，在方法B上设置不同的事务传播级别程序会做不同的处理。事务传播机制的设置是通过@Transactional注解的propagation属性来设置的，属性值如下：

* **`Propagation.REQUIRED`**：（默认属性）如果方法A存在事务，则方法B加入该事务；如果方法A没有事务，则方法B创建一个新的事务。

* **`Propagation.REQUIRES_NEW`**：如果事务A存在事务，则把当前事务挂起；如果方法A不存在事务，方法B就创建新事务。也就是说不管外部方法是否开启事务，方法B都会新开启自己的事务，且开启的事务相互独立，互不干扰。
* `Propagation.SUPPORTS`：如果方法A存在事务，则方法B加入该事务；如果方法A没有事务，则方法B以非事务的方式运行。
* `Propagation.MANDATORY`：如果方法A存在事务，则方法B加入该事务；如果方法A没有事务，则抛出异常。
* `Propagation.NOT_SUPPORTED`：如果方法A存在事务，则把当前事务挂起，方法B以非事务的方式运行；如果方法A不存在事务，方法B以非事务的方式运行。也就是说不管外部方法是否开启事务，方法B都会以非事务的方式运行。
* `Propagation.NEVER`：以非事务的方式运行，如果当前存在事务，则抛出异常。
* `Propagation.NESTED`：如果方法A存在事务，则方法B创建一个事务作为当前事务的嵌套事务来运行；如果方法A没有事务，则取值等价于Propagation.REQUIRED。

这些传播行为，是设置在**被调用方法**中的，即上述的B()方法，在该方法被调用时，才会去显示传播行为的特征。

上述传播行为，使用的最多的是`REQUIRED`（默认）与`REQUIRES_NEW`。我们只需要了解这两个传播机制即可。

现在，我们来对这两种传播行为来做一个演示。





**演示**

创建CheckoutService接口，并创建其实现类CheckoutServiceImpl，在其中定义一个方法checkout()，该方法去调用BookService实现类中的buyBook()方法，实现一次性购买多个图书的逻辑：

CheckoutService接口

```java
public interface CheckoutService {
    void checkout(Integer[] bookIds, Integer userId);
}
```

CheckoutServiceImpl类

```java
@Service
public class CheckoutServiceImpl implements CheckoutService{

    @Autowired
    private BookService bookService;

    @Transactional
    @Override
    public void checkout(Integer[] bookIds, Integer userId) {
        for (Integer bookId : bookIds) {
            bookService.buyBook(bookId, userId);
        }
    }
}
```

checkout()方法根据传入的bookIds，多次去购买指定的图书

该方法使用了@Transactional注解进行修饰，并且在其中调用的buyBook()方法也使用了@Transactional注解进行修饰。

然后，我们在BookController中，导入CheckoutServiceImpl类型对象，去调用的是CheckoutService中的checkout()方法。

BookController类

```java
@Autowired
private CheckoutService checkoutService;

public void checkout(Integer[] bookIds, Integer userId){
    checkoutService.checkout(bookIds, userId);
}
```

然后，我们去将数据表进行修改：

将用户表t_user中的用户余额balance修改成100：

![image-20240519022344489](.\images\image-20240519022344489.png) 

图书表t_book中的信息为：

![image-20240519022421392](.\images\image-20240519022421392.png) 

此时，用户lucy想各买id为1与id为2的书，余额肯定是不够的。

但是，在buyBook()的@Transactional注解中传播行为所设置的不同，会得到不同的结果。

**传播行为是REQUIRED的测试**

当被调用的事务方法中（也就是buyBook()方法），@Transactional注解的propagation属性为REQUIRED（或不写，默认为REQUIRED）：

```java
@Service
public class BookServiceImpl implements BookService{
    @Autowired
    private BookDao bookDao;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void buyBook(Integer bookId, Integer userId) {
        //根据图书id查询图书价格
        Integer bookPrice = bookDao.getBookPriceByBookId(bookId);

        //更新图书表中的库存量 - 1
        bookDao.updateStock(bookId);

        //更新用户表的用户余额 - 图书价格
        bookDao.updateUserBalance(userId, bookPrice);
    }
}
```

此时，去调用BookController中的方法，去购买图书表中的id为1与2的两本图书：

```java
@SpringJUnitConfig(locations = "classpath:beans.xml")
public class BookTxTest {
    @Autowired
    private BookController bookController;

    @Test
    public void testByBook(){
        bookController.checkout(new Integer[]{1, 2}, 1);
    }
}
```

此时去执行，肯定会报错：

![image-20240519023224010](.\images\image-20240519023224010.png) 

执行完后，用户表中的信息：

![image-20240519023304154](.\images\image-20240519023304154.png) 

图书表中的信息：

![image-20240519023319543](.\images\image-20240519023319543.png) 

**结果说明**

@Transactional(propagation = Propagation.REQUIRED)，默认情况下表示如果当前线程上有已经开启的事务可用，那么就在这个事务中运行。经过观察，购买图书的方法buyBook()在checkout()中被调用，checkout()方法上有事注解，因此直接在此事务中执行。所购买的两本图书的价格为80和50，而用户的余额为100，因此在购买第二本图书时余额不足失败，导致整个checkout()回滚，即只要有一本书买不了，就都买不了。





**传播行为是REQUIRES_NEW的测试**

当被调用的方法使用了@Transactional注解修饰，并且所设置的传播行为是REQUIRES_NEW时：

```java
@Service
public class BookServiceImpl implements BookService{
    @Autowired
    private BookDao bookDao;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void buyBook(Integer bookId, Integer userId) {
        //根据图书id查询图书价格
        Integer bookPrice = bookDao.getBookPriceByBookId(bookId);

        //更新图书表中的库存量 - 1
        bookDao.updateStock(bookId);

        //更新用户表的用户余额 - 图书价格
        bookDao.updateUserBalance(userId, bookPrice);
    }
}
```

此时去调用BookController类中的checkout方法，该checkout方法会去调用CheckoutServiceImpl中的checkout方法，通过该方法去批量调用BookServiceImpl中的buyBook()方法，该方法也声明了@Transactional注解。

```java
@SpringJUnitConfig(locations = "classpath:beans.xml")
public class BookTxTest {
    @Autowired
    private BookController bookController;

    @Test
    public void testByBook(){
        bookController.checkout(new Integer[]{1, 2}, 1);
    }
}

```

执行结果:

![image-20240519024056839](.\images\image-20240519024056839.png)

执行完后，用户表信息：

![image-20240519024132415](.\images\image-20240519024132415.png) 

图书表信息：

![image-20240519024145665](.\images\image-20240519024145665.png) 

**结果说明**

@Transactional(propagation = Propagation.REQUIRES_NEW)，表示不管当前线程上是否有已经开启的事务，都要开启新事务。同样的场景，每次购买图书都是在buyBook()的事务中执行，因此第一本图书购买成功，事务结束，第二本图书购买失败，只在第二次的buyBook()中回滚，购买第一本图书不受影响。即每次去buyBook()都会去创建一个新的事务。







---

### 全注解配置事务

也就是通过配置类去代替原本使用XML配置文件的方式。

原本的Springxml配置文件中的配置信息：

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd">

    <context:component-scan base-package="com.atguigu.spring6.tx" />

        
    <!-- 导入外部属性文件 -->
    <context:property-placeholder location="classpath:jdbc.properties" />

        
    <!--配置数据源-->
    <bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="username" value="${jdbc.username}" />
        <property name="password" value="${jdbc.password}" />
        <property name="url" value="${jdbc.url}" />
        <property name="driverClassName" value="${jdbc.driverClassName}" />
    </bean>

        
    <!--配置JdbcTemplate-->
    <bean class="org.springframework.jdbc.core.JdbcTemplate" id="jdbcTemplate">
        <!--装配数据源-->
        <property name="dataSource" ref="druidDataSource" />
    </bean>

    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="druidDataSource" />
    </bean>
S
	<!--开启事务的注解驱动-->        
    <tx:annotation-driven transaction-manager="transactionManager" />
</beans>
```

其中包括组件扫描功能、数据库数据源的配置、JdbcTemplate对象的配置以及事务管理器的配置以及开启事务管理功能。

将上述的配置信息转换成配置类的方式：

我们要去创建一个配置类，例如SpringConfig类，在该类中，使用：

* `@Configuration`注解，用于表示当前类是一个配置类；

* `@ComponentScan`注解，用于指定组件扫描的包路径；

* `@EnableTransactionManagement`注解，用于开启事务的注解驱动

那么也就是说，<context:component-scan base-package="com.atguigu.spring6.tx" />使用了@ComponentScan注解进行替代；

<tx:annotation-driven transaction-manager="transactionManager" />使用了@EnableTransactionManagement注解进行替代。

即：

```java
@ContextConfiguration
@ComponentScan("com.atguigu.spring6.tx")
@EnableTransactionManagement
public class SpringConfig {
}
```

之后，我们要在配置类中，去配置数据源信息，以及使用数据源的信息去创建JdbcTemplate对象和事务管理器对象。

**使用`@Bean`注解，通过定义方法的方式去获取到配置的bean对象信息，这些方法中需要返回对应的对象，将`@Bean`修饰方法返回的对象加入到IOC容器中**

例如，配置数据源信息：

```java
@Bean
public DataSource getDataSource(){
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql://localhost:3306/spring?characterEncoding=utf8&useSSL=false");
    dataSource.setUsername("root");
    dataSource.setPassword("061535asd");
    return dataSource;
}
```



配置JdbcTemplate对象信息，在方法中，通过setXxx()的方式，设置属性.

这里`@Bean`注解中的name属性，就类似于bean标签的id属性，用于设置bean对象的唯一标识。

```java
@Bean(name = "jdbcTemplate")
public JdbcTemplate getJdbcTemplate(){
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    jdbcTemplate.setDataSource(getDataSource());
    return jdbcTemplate;
}
```



配置事务管理器：

```java
@Bean
public DataSourceTransactionManager getDataSourceTransactionManager(){
    DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
    transactionManager.setDataSource(getDataSource());
    return transactionManager;
}
```

这样一来，bean对象就配置完毕了。

最终，上面的配置文件信息，**改成全部使用注解方式实现为：**

```java
@ContextConfiguration
@ComponentScan("com.atguigu.spring6.tx")
@EnableTransactionManagement
public class SpringConfig {
    @Bean
    public DataSource getDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/spring?characterEncoding=utf8&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("061535asd");
        return dataSource;
    }

    
    @Bean
    public JdbcTemplate getJdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(getDataSource());
        return jdbcTemplate;
    }

    
    @Bean
    public DataSourceTransactionManager getDataSourceTransactionManager(){
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(getDataSource());
        return transactionManager;
    }
}
```



**测试：**

```java
@Test
public void test(){
    ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
    BookController book = context.getBean(BookController.class);
    book.buyBook(2,1);
}
```

使用`AnnotationConfigApplicationContext`类型去读取类配置文件。

执行成功。



---

## 4、基于XML的声明式事务（了解一下）

在之前基于注解的配置文件中，我们做了如下的事情：

> 1、开启组件扫描
>
> 2、引入外部配置文件，创建数据源对象
>
> 3、创建JdbcTemplate对象，注入数据源
>
> 4、创建事务管理器对象，注入数据源
>
> 5、开启事务的注解驱动

这些是在基于注解的事务管理配置文件中，我们所配置的信息。

现在，我们要将基于注解的声明式事务修改成基于XML文件的方式。

将基于注解的声明式事务修改成基于XML的形式，需要将原本的声明式事务，将原本开启事务注解驱动的过程去除，改成：**使用`事务通知`的方式，利用`AOP切面编程`，配置`切入点表达式`，把事务通知添加到具体的目标方法上从而实现给目标方法添加事务。**

由于该方式需要使用到AOP切面编程，所以我们需要在原依赖的基础上引入aspectJ的依赖：

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
    <version>6.0.6</version>
</dependency>
```

在配置时，我们首先去将开启组件扫描、创建数据源、创建JdbcTemplate对象以及创建事务管理器对象先进行完善，配置信息与之前基于注解的方式完全一致，这里我就不再过多阐述。

**创建beans-xml.xml文件**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd">

    <context:component-scan base-package="com.atguigu.spring6.xmltx" />

    <!-- 导入外部属性文件 -->
    <context:property-placeholder location="classpath:jdbc.properties" />

    <!--配置数据源-->
    <bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="username" value="${jdbc.username}" />
        <property name="password" value="${jdbc.password}" />
        <property name="url" value="${jdbc.url}" />
        <property name="driverClassName" value="${jdbc.driverClassName}" />
    </bean>

    <!--配置JdbcTemplate-->
    <bean class="org.springframework.jdbc.core.JdbcTemplate" id="jdbcTemplate">
      <!--装配数据源-->
      <property name="dataSource" ref="druidDataSource" />
    </bean>

    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
      <property name="dataSource" ref="druidDataSource" />
    </bean>
    
</beans>
```

然后，我们需要去**配置事务增强信息**。

使用`tx:advice`标签，来配置事务的增强信息，使用id属性给该事务通知设置唯一标识，便于引用，以及使用transaction-manager属性去关联事务管理器。

在tx:advice标签内，会有一个`tx:attributes`标签，用于管理所有的配置具体事务的方法。

在tx:attributes中配置`tx:method`标签，该标签就是具体配置事务的方法，其中的name属性用于指定方法名，可以使用*号来表示任意多个字符。

（注意，在使用tx:标签之前，我们需要先引入tx命名空间）

即：

```xml
<tx:advice id="" transaction-manager="transactionManager">
    <tx:attributes>
    	<tx:method name="" />
    </tx:attributes>
</tx:advice>
```

在tx:method标签中，可以使用多个属性来给事务赋上各种属性：

* read-only属性：设置只读属性
* rollback-for属性：设置回滚的异常
* no-rollback-for：设置不回滚的异常
* isolation属性：设置事务的隔离级别
* timeout属性：设置事务的超时属性
* propagation属性：设置事务的传播行为

例如：

```xml
<tx:advice id="txAdvice" transaction-manager="transactionManager">
    <tx:attributes>
    	<tx:method name="get*" read-only="true" />
        <tx:method name="update*" read-only="false" propagation="REQUIRED" />
    </tx:attributes>
</tx:advice>
```

表示给get开头的方法添加事务，其事务的read-only属性为true；

给update开头的方法添加事务，其事务的read-only属性为false，propagation属性为REQUIRED。



最后，我们需要去**配置切入点和通知使用的方法**

创建`aop:config`标签，该标签就是用来配置aop切面的。

在该标签中，先去配置一个`aop:pointcut`标签，用于配置切入点表达式；

然后，我们使用**`aop:advisor`**去配置事务通知，第一个属性`advice-ref`用于传入事务通知的id，第二个属性`pointcut-ref`就是用来传入切入点方法的。

（注意，我们在使用aop标签之前，首先要去先引入aop命名空间-->

配置如下：

```xml
<!--配置切入点表达式和事务通知使用的方法-->
<aop:config>
    <aop:pointcut id="pointcut" expression="execution(* com.atguigu.spring6.xmltx.service.*.*(..))" />
	<aop:advisor advice-ref="txAdvice" pointcut-ref="pointcut" />
</aop:config>
```

以上就是基于XML配置文件的方式，配置声明式事务的过程。

**beans-xml.xml配置文件配置完毕后的全部内容：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd
       http://www.springframework.org/schema/aop 
       http://www.springframework.org/schema/aop/spring-aop.xsd">

    <context:component-scan base-package="com.atguigu.spring6.xmltx" />

    <!-- 导入外部属性文件 -->
    <context:property-placeholder location="classpath:jdbc.properties" />

    <!--配置数据源-->
    <bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="username" value="${jdbc.username}" />
        <property name="password" value="${jdbc.password}" />
        <property name="url" value="${jdbc.url}" />
        <property name="driverClassName" value="${jdbc.driverClassName}" />
    </bean>

    
    
    <!--配置JdbcTemplate-->
    <bean class="org.springframework.jdbc.core.JdbcTemplate" id="jdbcTemplate">
        <!--装配数据源-->
        <property name="dataSource" ref="druidDataSource" />
    </bean>
    
   
	<!--创建事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="druidDataSource" />
    </bean>
    
    
    
    

    <!--表示配置哪些方法使用事务通知以及事务的属性-->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <tx:method name="get*" read-only="true" />
            <tx:method name="update*" read-only="false" propagation="REQUIRED" />
            <tx:method name="buyBook" />
        </tx:attributes>
    </tx:advice>
    

    
    <!--使用AOP切面编程的方式，将事务使用通知的方式添加到方法中-->
    <aop:config>
        <aop:pointcut id="pointcut" expression="execution(* com.atguigu.spring6.xmltx.service.*.*(..))" />
        <aop:advisor advice-ref="txAdvice" pointcut-ref="pointcut" />
    </aop:config>

</beans>
```

这样一来，我们就将com.atguigu.spring6.xmltx.service包下所有的类中的以get、update开头的方法添加上了事务，以及buyBook方法也添加上了事务。

我们来测试一下：

使用Spring整合Juint5的方式，加载beans-xml.xml配置文件，创建IoC容器，并往其中注入BookController类型对象，调用其中的buyBook()方法；该buyBook()方法会去service包下调用BookServiceImpl类中的buyBook()方法，该方法就会去实际执行业务逻辑，操作数据库：

```java
@SpringJUnitConfig(locations = "classpath:beans-xml.xml")
public class BookTxTest {
    @Autowired
    private BookController bookController;

    @Test
    public void testByBook(){
        bookController.buyBook(1,1);
    }
}
```

此时执行，出现异常，原因在于数据表中字段我设置了unsigned，即非负数，所以才会报异常：

![image-20240519122332741](.\images\image-20240519122332741.png)

但是，数据表回到了原本的状态，即支持了事务。

所以，该配置文件生效了。



---

# 七、资源操作：Resources

下面这些内容好像并不重要，用到再学。















---

# 八、国际化：i18n













---

# 九、数据校验：Validation















---

# 十、提前编译：AOT

