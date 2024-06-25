[TOC]



# 一、XML

## 1、XML介绍

<img src=".\images\1681452257379.png" alt="1681452257379" style="zoom:50%;" />

**`XML`**是Extensible Markup Language的缩写，翻译过来就是可扩展标记语言。所以很明显，XML和HTML一样都是标记语言，也就是说它们的基本语法都是标签。

* **可扩展** 三个字意思是XML允许自定义格式。
* 在XML基本语法的规范上，使用的第三方应用程序、框架会通过XML约束的方式强制规定配置文件中可以怎么写，应该怎么写。

#### 常见配置文件类型

1. properties文件，例如druid连接池就是使用properties文件作为配置文件
2. XML文件，例如Tomcat就是使用XML文件作为配置文件
3. YAML文件，例如SpringBoot就是使用YAML作为配置文件
4. json文件，通常用来做文件传输，也可以用来做前端或移动端的配置文件
5. ...



#### properties配置文件示例

```properties
jdbc.url=jdbc:mysql://localhost:3306/atguigu
jdbc.driverClassName=com.mysql.cj.jdbc.Driver
jdbc.username=root
jdbc.password=061535asd
```

**语法规范：**

* 由键值对组成
* 键和值之间的符号是等号
* 每一行都必须顶格写，前面不能有空格之类的其他符号



#### xml配置文件

示例

```xml
<?xml version="1.0" encoding="UTF-8"?>
<students>
    <student>
        <name>张三</name>
        <age>18</age>
    </student>
    <student>
        <name>李四</name>
        <age>20</age>
    </student>
</students>
```



**XML的基本语法**

* XML的基本语法和HTML的基本语法简直如出一辙。其实这不是偶然，XML基本语法 + HTML约束 = HTML语法。在逻辑上HTML确实是XML的子集。

* XML文档声明，这部分基本上就是固定格式，要注意的是文档声明一定要从第一行第一列开始写

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  ```

* 根标签

  * 根标签有且只能有一个

* 标签关闭

  * 双标签：开始标签和结束标签必须成对出现。
  * 单标签：单标签在标签内关闭。

* 标签嵌套

  * 可以嵌套，但是不能交叉嵌套

* 注释不能嵌套

* 标签名、属性名建议使用小写字母

* 属性

  * 属性必须有值
  * 属性值必须加引号，单双都行



**XML的约束**

XML约束，就是限制我们在XML文件中，能够编写的内容。

将来我们主要就是根据XML约束中的规定来编写XML配置文件，而且会在我们编写XML的时候根据约束来提示我们编写，而XML约束主要包括DTD和Schema两种。

* DTD
* Schema

DTD约束比较简单，上手快，没有Schema约束细致。

Schema约束比较复杂，上手慢，约束要比DTD约束细致。

Schema约束要求我们一个XML文档中，所有标签，所有属性都必须在约束中有明确的定义。

下面我们以web.xml约束为例来做一个说明：

```xml
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
```

那也就是说，使用xml注解的方式实现Spring，其中的xml配置文件就是使用了schema约束。当我们在xml配置文件中，引入相关命名空间时，例如aop命名空间，context命名空间，我们还需要引入相关的约束才能使用。



## 2、DOM4J解析XML

这里的DOM4J，实际上采用的原理和JS中的document原理一样，都是去读取XML文件，将其中的标签转换成树形结构，通过这个树形结构，去获取其中的元素结点。

### DOM4J的使用步骤

> 1. **引入DOM4J的依赖**
> 2. **创建解析器对象（`SAXReader`）**
> 3. **解析xml获得`Document`对象**
> 4. **获取根节点`RootElement`**
> 5. **获取根节点下的子节点**



### DOM4J的API介绍

解析XML文件，需要使用SAXReader对象。**创建SAXReader对象：**

```java
SAXReader saxReader = new SAXReader();
```



**创建Document对象：**调用`SAXReader`对象的**`read()`**方法，参数传入的是要解析的XML信息，可以传入Reader类型、Url类型、File类型、InputStream类型以及String类型，即可以是xml文件的地址信息，也可以是xml文件的输入流。

这里以字节流参数为例：

```java
Document document = saxReader.read(inputStream);
```



**获取文档根标签**：使用`document`对象的**`getRootElement()`**方法，获取XML文件的根标签：

```java
Element rootElement = document.getRootElement();
```



**获取标签的子标签：**

```java
//获取所有子标签
List<Element> sonElementList = rootElement.elements();
//获取指定标签名的子标签
List<Element> sonElementList = rootElement.elements("标签名");
```



**获取标签的文本或属性：**

```java
String text = element.getText();
String value = element.attributeValue("属性名");
```



### DOM4J实现案例

**引入DOM4J依赖**

```xml
<dependency>
   <groupId>org.dom4j</groupId>
   <artifactId>dom4j</artifactId>
   <version>2.1.3</version>
</dependency>
```





创建一个测试方法，去创建解析器对象，解析xml文件。这里我们就去解析maven的pom.xml文件，将pom.xml文件copy到resources目录下。

然后去解析该pom.xml文件，并去打印该文件中各个标签的信息：

```java
@Test
public void testDOM4J() throws DocumentException {
    SAXReader saxReader = new SAXReader();

    Document document = saxReader.read("pom.xml");

    Element rootElement = document.getRootElement();

    for (Element element : rootElement.elements()) {
        System.out.println(element);
    }

    System.out.println("*******************************");
    Element modelVersion = rootElement.element("modelVersion");
    System.out.println(modelVersion.getText());

}
```

打印结果：

![image-20240529180226022](.\images\image-20240529180226022.png) 







---

# 二、Tomcat 10

## 服务器环境准备

需要注意：在Tomcat 10版本及以后，原本Tomcat实现的api包名从`javax`变为了`jakarta`，所以原本使用Tomcat 8或者Tomcat 9开发的项目，因为包名改变，所以不能使用Tomcat 10作为应用服务器。

并且，由于Tomcat是JavaWeb服务器，所以Tomcat的安装也需要JDK环境。不同的Tomcat版本，需要的JDK版本环境也不同，我们来看一个对照表：

![image-20240529214232987](.\images\image-20240529214232987.png)

可以看到，Tomcat 10所需的Java版本需要11或者以后。我在学习时，电脑装的是JDK17，所以可以使用Tomcat 10.

### Tomcat服务器的安装

**下载Tomcat 10**

可以去官网**下载**，也可以直接使用笔记中的lib目录下已经下载好的安装包。这里我们介绍去官网下载Tomcat 10：

到官网下载：[Apache Tomcat® - Welcome!](https://tomcat.apache.org/)

在官网的左侧，Download位置，选择Tomcat 10：

<img src=".\images\image-20240529214603304.png" alt="image-20240529214603304" style="zoom:67%;" /> 

然后滑到最下面，选择64-bit Windows zip选项，下载64位的windows版本，zip压缩包形式的文件：

<img src=".\images\image-20240529214725943.png" alt="image-20240529214725943" style="zoom:67%;" /> 

下载完成后就可以进行安装。



**安装Tomcat 10**

安装之前，需要确保电脑中已经安装了Java环境，因为Tomcat在安装时，会自动取找环境变量中的JAVA_HOME。只有JAVA_HOME配置好了，在Tomcat安装完毕之后，才能正常启动Tomcat。验证JAVA_HOME是否配置成功：

<img src=".\images\image-20240529215441171.png" alt="image-20240529215441171" style="zoom:67%;" />

当出现上面的版本信息时，就说明JAVA_HOME配置成功了。之后，就可以解压Tomcat 10的压缩包了。



将Tomcat 10**压缩包解压**，得到一个apache-tomcat-10.1.24的目录。

安装非常简单，这里就已经将Tomcat安装完毕了。

接下来，就可以启动Tomcat服务器。



---

### 手动启动Tomcat服务器

在该目录的bin`目录中，找到startup文件，该文件是Tomcat的启动命令：

![image-20240529215815943](.\images\image-20240529215815943.png) 

其中.bat文件是在Windows系统中运行的，.sh文件是在Linux系统中运行的。

**双击startup.bat文件**

此时会弹出一个终端，显示一些信息：

<img src=".\images\image-20240529220533700.png" alt="image-20240529220533700" style="zoom:67%;" /> 

信息最终会停止不动，此时终端上显示这些信息是一些中文的乱码，并且还有一个http-nio-8080。

这里实际上Tomcat服务器就已经启动了，我们可以通过本机的浏览器，访问`localhost:8080`去访问本地的Tomcat服务器：

<img src=".\images\image-20240529220932618.png" alt="image-20240529220932618" style="zoom: 33%;" />  

当出现上面的页面时，就说明Tomcat已经安装并且启动成功。

**Tomcat服务器的端口号是`8080`**





---

### 配置环境变量，通过命令行方式启动Tomcat服务器

如果每次都需要跑到Tomcat的根目录下，然后进入到bin目录中，去点击startup.bat文件来启动Tomcat服务器，有点过于麻烦了。

所以，我们不如将Tomcat根目录下的bin目录，配置到环境变量中，让我们要启动Tomcat服务器时，就通过命令行的方式运行，这样会方便很多。

**配置过程：**

1、复制Tomcat根目录路径：我这里是D:\apache-tomcat-10.1.24

2、打开高级系统设置中的环境变量：

<img src=".\images\image-20240529223936881.png" alt="image-20240529223936881" style="zoom:50%;" /> 

3、在系统变量中，新建一个变量，名叫：**`CATALINA_HOME`**，然后将根目录的路径作为value创建。

<img src=".\images\image-20240529224246244.png" alt="image-20240529224246244" style="zoom: 67%;" /> 

4、在环境变量的Path中，再去新建一个数据，值为：`%CATALINA_HOME%\bin`。（就有点类似于JAVA_HOME的配置）点击确定，退出环境变量的配置中。

<img src=".\images\image-20240529224637330.png" alt="image-20240529224637330" style="zoom:67%;" /> 



5、测试

win+r，输入cmd打开命令行。在命令行中，输入startup.bat，然后回车：

此时，弹出了Tomcat的Doc窗口，显示了Tomcat的日志信息，就说明配置成功了。

<img src=".\images\image-20240529224748284.png" alt="image-20240529224748284" style="zoom:50%;" /> 



**注意：**

以上的配置并不是必须的，配置之后就无需我们每次都要去找到Tomcat根目录中的bin目录下的startup.bat文件启动Tomcat，通过命令行方式启动即可。

但是，后续我们一般都会使用IDEA整合Tomcat，在IDEA中来控制启动和关闭Tomcat服务器，就不需要我们自己去启动和关闭了，上述操作可以进行配置，也可以不进行配置。





---

### 手动关闭Tomcat服务器

我们可以通过直接关闭`startup`命令所弹出的终端，或者启动`bin`目录下的`shutdown`命令来关闭Tomcat服务器（.bat是Windows系统使用，.sh是Linux系统使用）：

![image-20240529221301772](.\images\image-20240529221301772.png)



后续，在IDEA中会去关联Tomcat，我们就不需要再手动地开启与关闭Tomcat服务器。 



---

### 解决Tomcat日志乱码问题

我们在双击startup.bat文件，启动Tomcat时，会弹出一个Dos窗口，用于打印Tomcat日志信息：

<img src=".\images\image-20240529221747964.png" alt="image-20240529221747964" style="zoom:50%;" /> 

但是这些日志信息全是中文乱码，为什么会出现这样的问题？

因为Doc窗口，默认使用的是系统所设置的默认字符集，也就是GBK。

但是在Tomcat中，日志默认使用的字符集是utf-8，所以我们需要去Tomcat的配置中，将日志的字符集修改成GBK。

打开tomcat根目录下的conf目录，找到logging.properties：

![image-20240529222050463](.\images\image-20240529222050463.png)

使用文本编辑软件打开，找到`java.util.logging.ConsoleHandler.encoding = UTF-8`这个值（在51行），将UTF-8修改成GBK并且保存。

此时，我们再去双击startup.bat，运行Tomcat服务器，就可以看到弹出的Dos窗口中，日志信息不再是乱码的了：

<img src=".\images\image-20240529222443720.png" alt="image-20240529222443720" style="zoom: 50%;" /> 







---

## 乱码问题

### 解决日志乱码问题

我们在双击startup.bat文件，启动Tomcat时，会弹出一个Dos窗口，用于打印Tomcat日志信息：

<img src=".\images\image-20240529221747964.png" alt="image-20240529221747964" style="zoom:50%;" /> 

但是这些日志信息全是中文乱码，为什么会出现这样的问题？

因为Doc窗口，默认使用的是系统所设置的默认字符集，也就是GBK。

但是在Tomcat中，日志默认使用的字符集是utf-8，所以我们需要去Tomcat的配置中，将日志的字符集修改成GBK。

打开tomcat根目录下的conf目录，找到logging.properties：

![image-20240529222050463](.\images\image-20240529222050463.png)

使用文本编辑软件打开，找到`java.util.logging.ConsoleHandler.encoding = UTF-8`这个值（在51行），将UTF-8修改成GBK并且保存。

此时，我们再去双击startup.bat，运行Tomcat服务器，就可以看到弹出的Dos窗口中，日志信息不再是乱码的了：

<img src=".\images\image-20240529222443720.png" alt="image-20240529222443720" style="zoom: 50%;" /> 











---



## Tomcat目录说明

<img src=".\images\image-20240529235131753.png" alt="image-20240529235131753" style="zoom: 50%;" /> 

* `bin`：该目录下存放的是二进制可执行文件。如果是安装版，那么这个目录下会有两个exe文件：tomcat10.exe、tomecat10w.exe，前者是在控制台下启动Tomcat，后者是弹出GUI窗口启动Tomcat；如果是解压版，那么会有startup.bat和shutdown.bat文件，startup.bat用来启动Tomcat，但需要先配置JAVA_HOME环境变量才能启动，shutdown.bat用来停止Tomcat。



* `conf`：这是一个非常重要的目录，这个目录下有四个最为重要的文件：

  * **server.xml：配置整个服务器信息。例如修改端口号。默认HTTP请求的端口号是8080。**

  * tomcat-users.xml：存储tomcat用户的文件，这里保存的是tomcat的用户名及密码，以及用户的角色信息。可以按着以下文件中的注释信息添加tomcat用户，然后就可以在Tomcat主页中进入Tomcat Manager页面了。

    ```xml
    <tomcat-users xmlns="http://tomcat.apache.org/xml"
                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                  xsi:schemaLocation="http://tomcat.apache.org/xml tomcat-users.xsd"
                  version="1.0">	
    	<role rolename="admin-gui"/>
    	<role rolename="admin-script"/>
    	<role rolename="manager-gui"/>
    	<role rolename="manager-script"/>
    	<role rolename="manager-jmx"/>
    	<role rolename="manager-status"/>
    	<user 	username="admin" 
    			password="admin" 
    			roles="admin-gui,admin-script,manager-gui,manager-script,manager-jmx,manager-status"
    	/>
    </tomcat-users>
    ```

    之后，我们就可以通过admin用户与admin的密码进入到http://localhost:8080/manager/html路径访问Tomcat自带的管理工具。

  * web.xml：部署描述符文件，这个文件中注册了很多MIME类型，即文档类型。这些MIME类型是客户端与服务器之间说明文档类型的，如用户请求一个html网页，那么服务器还会告诉客户端浏览器相应的文档是text/html类型的，这就是一个MIME类型。客户端浏览器通过这个MIME类型就知道如何处理它了。

  * context.xml：对所有应用的统一配置，通常我们不会去配置它。



* `lib`：Tomcat的类库，里面是一大堆jar文件。如果需要添加Tomcat依赖的jar文件，可以把它放到这个目录中，当然也可以把应用依赖的jar文件放到这个目录中，这个目录中的jar所有项目都可以共享之，但这样你的应用放到其他Tomcat下时就不能再共享这个目录下的jar包下，所以建议只把Tomcat需要的jar包放到这个目录下。



* `logs`：这个目录中都是日志文件，记录了Tomcat启动和关闭的信息，如果启动Tomcat时有错误，那么异常也会记录在日志文件中。



* `temp`：存放Tomcat的临时文件，这个目录下的东西可以在停止Tomcat后删除。



* **`webapps`：存放web项目的目录，其中每个文件夹都是一个项目**；

  <img src=".\images\image-20240529234850026.png" alt="image-20240529234850026" style="zoom: 67%;" /> 

  我们将项目打包成war包的形式，存放到该目录下，在启动Tomcat服务器后就会解析该war包，解析成一个部署目录，然后就可以使用Tomcat服务器进行访问。比如，打成的war包就叫做project1.war，之后就可以通过本地Tomcat服务器使用http://loacalhost:8080/project1/的路径对该项目进行访问。

  如果这个目录下已经存在了目录，那么都是Tomcat自带的项目。其中ROOT是一个特殊的项目，在地址栏中访问：http://127.0.0.1:8080，没有给出项目目录时，对应的就是ROOT项目。

  http://localhost:8080/examples，进入示例项目。其中examples就是项目名，即文件夹的名字。



* `work`：运行时生成的文件，最终运行的文件都在这里。通过webapps中的项目生成。可以把这个目录下的内容删除，再次运行时会再次生成work目录。当客户端用户访问一个JSP文件时，Tomcat会通过JSP生成Java文件，然后再编译Java文件生成class文件，生成的java和class文件都会存放到这个目录下。（目前JSP技术已经过时，不再使用，所以work目录也就不再关注了）
* LICENSE：许可证。
* NOTICE：说明文件。



## Web应用结构说明

在IDEA中，一个Java Web应用的项目中，通常我们喜欢将各种文件存放在如下结构的位置中：

```
|-- pom.xml                          # Maven 项目管理文件 
|-- src
    |-- main                         # 项目主要代码
    |   |-- java                     # Java 源代码目录
    |   |   |-- com/example/myapp    # 开发者代码主目录
    |   |       |-- controller       # 存放 Controller 层代码的目录
    |   |       |-- service          # 存放 Service 层代码的目录
    |   |       |-- dao              # 存放 DAO 层代码的目录
    |   |       |-- model            # 存放数据模型的目录
    |   |-- resources                # 资源目录，存放配置文件、静态资源等
    |   |   |-- static               # 存放静态资源的目录
    |   |       |-- css              # 存放 CSS 文件的目录
    |   |       |-- js               # 存放 JavaScript 文件的目录
    |   |       |-- images           # 存放图片资源的目录
    |   |-- webapp                   # 存放 WEB 相关配置和资源
    |       |-- WEB-INF              # 存放 WEB 应用配置文件
    |       |   |-- web.xml          # Web 应用的部署描述文件
    |       |   |-- classes          # 存放编译后的 class 文件
    |       |-- index.html           # Web 应用入口页面
    |-- test                         # 项目测试代码
        |-- java                     # 单元测试目录
        |-- resources                # 测试资源目录
```

将上述项目结构对应的Web应用，打成war包，放入到Tomcat的webapps中，解析得到的结构是：

![1681453620343](.\images\1681453620343.png)

**对文件结构的说明：**

* app 应用根目录
  * `static`  非必要目录，约定俗成的名字，一般在该目录中存放静态资源（html、css、js、img）
  * `WEB-INF`  必要目录，必须要叫WEB-INF。该目录是受保护的资源目录，浏览器不可通过url直接访问目录。
    * `classes`   必要目录，src下源代码、resources配置文件目录经编译后会存放在该目录下，web项目中如果没有源码，则该目录不会出现。
    * `lib`       必要目录，项目所依赖的jar包经编译后会出现在该目录下，web项目要是没有依赖任何jar，该目录不会出现
    * `web.xml`    必要文件，web项目的基本配置文件。
  * index.html   非必要文件，为默认的首页。

![image-20240530005100581](.\images\image-20240530005100581.png)

对于.html、.css、.js以及图片文件来说，可以放在static目录下的各种对应目录中，也可以直接放在项目目录下，只不过我们为了方便管理，一般会使用目录存放这些静态资源。

**WEB-INF是受保护的资源目录，不可以通过浏览器直接访问的资源目录。**



> **url的组成部分和项目中资源的对应关系**

![1681456161723](.\images\1681456161723.png)







---

## 配置Tomcat（:star2:）

> **新建project进行配置演示**

我们去新建一个Project，来进行配置。创建的Project名称就叫做JavaWeb，使用maven进行构建的：

<img src=".\images\image-20240530103258027.png" alt="image-20240530103258027" style="zoom:67%;" /> 





> **关联本地mavenc仓库**

然后，我们去settings中，为项目关联本地的maven仓库：
<img src=".\images\image-20240530103352166.png" alt="image-20240530103352166" style="zoom: 33%;" />

在Build,Execution,Deployment中的Build Tools中，选择Maven，然后在Maven home path选项中，选择本地下载的maven仓库，点击OK。这样就为项目关联上的本地的maven。







> **关联本地Tomcat服务器**

在IDEA中配置本地的Tomcat，首先我们需要去让IDEA的项目关联本地Tomcat。进入到settings设置中的Build,Execution,Deployment中的Application Servers中，该位置用于配置应用服务器，该处如果是新创建的项目是没有任何服务的：

<img src=".\images\image-20240530103805313.png" alt="image-20240530103805313" style="zoom: 33%;" /> 

点击+号，选择Tomcat Server:

<img src=".\images\image-20240530103829161.png" alt="image-20240530103829161" style="zoom: 67%;" /> 

选择本地已经下载好的Tomcat服务器，点击OK：

<img src=".\images\image-20240530103919956.png" alt="image-20240530103919956" style="zoom:50%;" /> 

这样一来，当前项目就关联上了本地的Tomcat服务器。

但是，仅仅只是关联上本地的Tomcat服务器，还需要部署Tomcat服务器到Web项目中。（由于该项目是新创建的，所以我们再去创建一个Web子工程，用于进行配置测试）



> **创建Web子工程**

创建子工程，比如叫做TomcatTest

<img src=".\images\image-20240530104337995.png" alt="image-20240530104337995" style="zoom:50%;" /> 

在子工程的pom.xml配置文件中，使用`<packaging>war</packaging>`配置，将子工程的打包方式修改成war包：

<img src=".\images\image-20240530104523219.png" alt="image-20240530104523219" style="zoom:67%;" /> 

 然后，将该子工程修改成Web工程。可以使用两种方式，一种是手动地在项目的main目录下创建webapp目录，然后在其中创建WEB-INF目录，在WEB-INF目录下创建web.xml文件，并在该文件中放入配置信息；还有一种是直接使用JBLJavaToWeb插件，将普通的Java工程修改成Web工程。

我这里就是使用JBLJavaToWeb插件的方式（具体内容请查看maven中的创建JavaEE工程部分），使用插件后，在main目录下就会创建好Web应用所需的目录与文件了：

<img src=".\images\image-20240530105019872.png" alt="image-20240530105019872" style="zoom:80%;" /> 

此时，一个Web项目就算搭建完毕了。





> **Web项目配置Tomcat服务器**

最后，我们就可以在Web项目中配置Tomcat服务器。从而实现开发中使用Tomcat服务器。

在IDEA的右上角，点击Edit Configuartions：

<img src=".\images\image-20240530105136880.png" alt="image-20240530105136880" style="zoom: 33%;" /> 

该处表示的是Run/Debug时所需要的配置。



点击+号，选择Tomcat Server中的Local，即选择本地的Tomcat服务器：

<img src=".\images\image-20240530105501363.png" alt="image-20240530105501363" style="zoom:50%;" />



配置中，红框框出来的内容从上到下依次是：

为该Tomcat服务取的名字；

本地的Tomcat服务；

所使用的JRE版本；

所使用的HTTP端口号。

<img src=".\images\image-20240530105627087.png" alt="image-20240530105627087" style="zoom: 50%;" /> 

该位置配置完毕后，选择Deployment页，去配置项目的部署方式：

点击+号，选择Artifact..

![image-20240530110019350](.\images\image-20240530110019350.png)



会弹出一个框，让开发者选择该项目使用何种方式进行部署。

由于该项目是一个JavaEE项目，在pom.xml中已经配置了项目的打包方式为war，所以在这里可以选择使用war方式进行部署，或者使用war exploded方式进行部署。（这两种方式的不同后面有说明）

我们这里选择使用war的方式进行部署，点击OK。

**推荐使用`war exploded`的方式进行部署，开发中也一般都使用这种方式**

<img src=".\images\image-20240530110100627.png" alt="image-20240530110100627" style="zoom: 80%;" /> 

此时，应用程序上下文就是`/项目名称_war`，也就是该项目的访问路径。可以进行更改，我这里就不更改了，点击OK。

<img src=".\images\image-20240530110447789.png" alt="image-20240530110447789" style="zoom:50%;" /> 



这样一来，当前项目的Tomcat就已经配置完毕了。





**测试**

刚刚配置好的Tomcat，我们来测试一下，看看是否有用。

在TomcatTest项目下（刚刚配置Tomcat的项目）的main目录下的webapp目录中，创建一个index.html文件作为首页测试，在该html页面中输入一段文字，比如就叫hello tomcat!：

<img src=".\images\image-20240530110916539.png" alt="image-20240530110916539" style="zoom:50%;" /> 

然后，我们去运行Tomcat服务器：

![image-20240530111036789](.\images\image-20240530111036789.png) 

Tomcat正常运行。

其中，访问路径中端口号后面的部分，就是项目名称_war，也就是在配置中配置的应用程序上下文。





---

### Tomcat中部署Web项目

上面的内容，是如何在IDEA中部署Tomcat服务器以便于开发。以下内容，讲述的是如何将开发好的Java Web项目，打包部署到服务器中。（这里就以本地的Tomcat服务器为例）

在上述中，如果在项目的Tomcat配置中，Deployment所设置的是以war方式进行打包部署，则在运行Tomcat时，会自动将当前项目打包成war并部署到Tomcat目录的webapps目录中。

<img src=".\images\image-20240530112317861.png" alt="image-20240530112317861" style="zoom:50%;" /> 

接下来介绍的方式，主要内容是如何将项目打成war包的过程。





> **1、首先，我们需要使用maven的方式管理项目的构建**。

对于maven的引入以及构建管理这里就省略了，可以直接查看maven笔记。





> **2、然后，我们需要确保Java项目是Web项目**。

即项目中要有WEB-INF目录、web.xml配置文件，我们可以通过JBLJavaToWeb插件将普通Java项目转变为Web项目。

安装好JBLJavaToWeb插件，右击项目，点击JBLJavaToWeb，即可将普通项目转换成Web项目：

<img src=".\images\image-20240530020803345.png" alt="image-20240530020803345" style="zoom:50%;" /> 





> **3、其次，我们需要将打包方式修改成war**。

在项目的pom.xml文件中，将packaging属性修改成war：

```xml
<packaging>war</packaging> 
```



> **4、当项目完成后，进行打包。**

通过maven的插件进行打包操作:

<img src=".\images\image-20240530020352482.png" alt="image-20240530020352482" style="zoom: 50%;" /> 

双击package，使用插件实现项目的打包操作。

打包完成后，会在项目的target目录中，生成一个xxxx.war文件，该文件就是打包好的war包。



如果这里的打包操作报错：

<img src="C:/Users/14036/Desktop/笔记/4、Maven、SSM、MyBatisPlus、SpringBoot/images/image-20240509113120343.png" alt="image-20240509113120343" style="zoom: 50%;" /> 

为什么会出现这种情况？

原因在于maven自带的打成war包的插件：war，版本是2.2，版本太低，无法兼容JDK17版本：

<img src=".\images\image-20240509113203261.png" alt="image-20240509113203261" style="zoom: 67%;" /> 

此时，我们就需要配置一个高版本的war插件。

**该如何进行配置？**

进入到项目下的pom.xml文件，这里我们进入到的是maven-javaee-project-3项目（module）下的pom.xml文件中。

在`<dependencies>`标签下，创建一个`<build>`标签，并在`<build>`标签中创建`<plugins>`标签，在这个标签中就是用来存放所有插件配置的信息。

`<plugins>`标签中创建`<plugin>`标签，每个`<plugin>`标签，就表示一个插件的配置。

<img src="C:/Users/14036/Desktop/笔记/4、Maven、SSM、MyBatisPlus、SpringBoot/images/image-20240509113745137.png" alt="image-20240509113745137" style="zoom:67%;" /> 

解决jdk17和war包版本插件不匹配的配置信息是：

```xml
<build>
   <!-- jdk17 和 war包版本插件不匹配 -->
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-war-plugin</artifactId>
            <version>3.2.2</version>
        </plugin>
    </plugins>
</build>
```

将上述信息复制到pom.xml中：

<img src=".\images\image-20240509114125145.png" alt="image-20240509114125145" style="zoom: 50%;" /> 

复制之后，可能会标红，因为还没有去下载，当我们进行打包操作后，会自动进行下载，打包操作之后也就不再标红。

然后，再使用war的方式进行打包：

```shell
mvn clean package
```

此时执行就不会出现错误了，将项目打包成war包存放到了target目录下：

<img src=".\images\image-20240509114213962.png" alt="image-20240509114213962" style="zoom: 67%;" /> 



> **5、将该war包复制到Tomcat服务器应用根目录下的webapps目录中**

<img src=".\images\image-20240530021015140.png" alt="image-20240530021015140" style="zoom:50%;" /> 



这样就部署完毕了。

之后，我们就可以通过`http://localhost:8080/项目名` 的方式访问部署好的项目了。





**问题**：在Tomcat中，部署Web项目，必须要放在webapps目录中吗？可以放在其他目录下吗？

答案是可以的，但是前提是你得告诉Tomcat：项目部署在哪了。那么如何告诉Tomcat呢？

例如：

我要去部署一个名叫pro1的项目，我要将其部署在D:\mywebapps目录下，配置过程：

+ 在tomcat的conf下创建Catalina/localhost目录,并在该目录下准备一个pro1.xml文件

``` xml
<!-- 
	path: 项目的访问路径,也是项目的上下文路径,就是在浏览器中,输入的项目名称
    docBase: 项目在磁盘中的实际路径
 -->
<Context path="/pro1" docBase="D:\mywebapps\pro1" />
```

此时，我们就能够在D:\mywebapps目录下部署pro1的项目，并且可以通过localhost:8080/pro1路径进行访问。

但是，推荐大家还是将项目都放在webapps目录中。





## war和war exploded的区别以及Tomcat热部署的说明（:star:）

### war和war exploded的区别

* **`war模式`：将web工程以war包的形式上传到服务器；**

* **`war exploded模式`：将web工程以当前文件夹的位置关系上传到服务器。**

通俗地说，war模式先打成war包，然后再发布到Tomcat的webapps下，所以选择这个模式后，运行Tomcat可以看到在Tomcat应用的webapps目录下，会存在项目目录。war exploded模式只编译生成target目录，然后把当前的target目录的位置关系上传到服务器，没有打包到Tomcat的webapps下。

因此，**`war exploded`模式支持热部署**，一般在开发的时候也是使用这种方式。



### 对于Tomcat重启弹框的说明

项目使用不同的模式，运行Tomcat后更新数据时，弹框也是不一致的：

当模式是war时，更新的弹框：

<img src=".\images\image-20240530131429180.png" alt="image-20240530131429180" style="zoom:67%;" /> 

当模式是war exploded时，更新的弹框：

<img src=".\images\image-20240530113137850.png" alt="image-20240530113137850" style="zoom:67%;" /> 

这也是因为war exploded能够实现热部署，所以出现的选项才不一样。

对于选项的说明：

* `Update resources`：表示更新资源文件（.xml、.css、.js、.html等）
* `Update classes and resources`：如果使用的是debug调试方式启动的Tomcat，则会去更新java文件和资源文件；如果使用的是run运行方式启动的Tomcat，则只会更新resources资源文件。
* `Redeploy`：重新部署应用程序。此时，更改的任何文件都会生效。
* `Restart server`：重启Tomcat服务器。当我们修改了Tomcat服务器配置时，重启Tomcat服务器生效。

所以，我们一般在开发中，当只修改了resource资源文件时，要想将其更新到页面上，则一般使用Update resources的方式刷新服务器；当修改了Java文件，则一般使用Update classes and resources的方式刷新服务器（Debug方式运行）



### 什么是热部署？

**热部署**指的就是：在不需要重启应用的情况下，在更新文件后，被更新的数据会被同步地更新到正在运行的应用中。

Tomcat支持热部署的，前提是项目使用的是`war exploded`模式：

<img src=".\images\image-20240530133809381.png" alt="image-20240530133809381" style="zoom:50%;" /> 

当我们更新Java文件或者资源文件如.html后，可以在不重启Tomcat服务器的情况下，将修改的数据同步地更新到正在运行的页面中。



### 如何实现Tomcat热部署？

Tomcat支持我们手动去更新资源到服务器中，也支持自动刷新，自动地将资源更新到服务器。

**手动方式：**

当我们修改数据后，点击Tomcat的刷新按钮：

<img src=".\images\image-20240530133906671.png" alt="image-20240530133906671" style="zoom: 25%;" /> 

此时会弹出一个弹框，选择刷新的方式：（如果在Tomcat中进行配置过，就不会弹框）

<img src=".\images\image-20240530134049078.png" alt="image-20240530134049078" style="zoom:50%;" /> 

这弹框信息在上面说明过，第一个和第二个就是实现热部署的。



**自动方式：**

在Tomcat中进行配置，进入到Tomcat服务器配置中：

<img src=".\images\image-20240530134156619.png" alt="image-20240530134156619" style="zoom: 30%;" />

这里有两个选项：

<img src=".\images\image-20240530134305490.png" alt="image-20240530134305490" style="zoom:50%;" />

第一个选项：`On Update action`，指的就是当我们手动点击更新按钮后，默认使用何种方式刷新Tomcat，这里我们选择的是Update classes and resources，即更新classes文件与资源文件。

第二个选项：`On frame deactivation`，指的是去监听整个页面，当修改数据后该如何刷新Tomcat。可以选择三个选项：Do nothing、Update resources以及Update classes and resources，默认是选择Do nothing，即更新数据后不做任何操作。

<img src=".\images\image-20240530134642014.png" alt="image-20240530134642014" style="zoom:50%;" /> 

可以将其修改成Update resources或者Update classes and resources，当我们修改文件数据后，就不需要手动地点击Tomcat的刷新按钮，直接就会刷新到Tomcat，这样我们刷新页面就能够获得更新后的数据。

例如，下面的过程就是使用了自动刷新的方式实现的热部署：

![动画](.\images\39173129.gif)



以上两种操作都是热部署的实现，各有好处，第一种方式需要手动点击，第二种方式自动刷新，但是第二种方式更加占用内存一点。





### 不同的模式可能存在的相对路径不同问题

由于war和war exploded存在着部署方式的区别，所以当使用不同模式时，可能存在着相对路径有所不同的问题：

使用war模式开发的时候遇到的坑
一、项目代码的位置如下：

![这里写图片描述](.\images\SouthEasteqwew.png) 

上述项目为SSM项目。

二、部署使用的Tomcat位置：

![这里写图片描述](.\images\SouthEast14313241) 

三、用于获取上下文环境绝对路径的代码：

```java
String contextPath = getServletContext().getRealPath("/");
```


四、两种方式的实验过程和结果：

（1）在使用war模式开发的时候，通过下边这段代码获取项目的绝对路径：

```java
String contextPath = getServletContext().getRealPath("/");
```


war模式始终是获取到的路径如下：

![这里写图片描述](.\images\SouthEast) 

其中C:\Software\apache-tomcat-8.0.32 是我Tomcat的所在位置。

可以看出通过war模式是最终打包部署到Tomcat的位置。

（2）然后再看war exploded模式,同样进行设置，运行同一段代码，运行结果如下：

![这里写图片描述](.\images\2312341234132) 

可以看出最终得到的是我这个项目的位置，其实就是这个项目target的位置。

**五、总结**

不同的部署模式，实际上项目部署的位置是不同的。

当使用war的模式进行部署时，会将当前项目打包到Tomcat的webapps目录下进行部署，当项目进行了修改之后，还需要将项目重新打包部署到Tomcat的webapps目录下，所以不支持热部署。

当使用war exploded模式进行部署时，会将当前项目编译后的位置信息，传给Tomcat服务器，项目在当前位置进行部署，Tomcat访问当前位置的项目，所以支持热部署。









---

# 三、HTTP协议

## 简介

### 什么是HTTP？

<img src=".\images\1681522638617.png" alt="1681522638617" style="zoom: 67%;" />

<img src=".\images\1681522600239.png" alt="1681522600239" style="zoom:67%;" />

> **HTTP超文本协议**（HTTP-Hyper Text transfer protocol），是一个属于应用层的面向对象的协议，由于其简捷、快速的方式，适用于分布式超媒体信息系统。它于1990年提出，经过十几年的使用与发展，得到不断地完善和扩展。**它是一种详细规定了浏览器和万维网服务器之间互相通信的规则**，通过因特网传送万维网文档的数据传送协议。客户端与服务端通信时传输的内容我们称之为**报文**。**HTTP协议就是规定报文的格式。**HTTP就是一个通信规则，这个规则规定了客户端发送给服务器的报文格式，也规定了服务器发送给客户端的报文格式。实际我们要学习的就是这两个报文。**客户端发送给服务器的称为"请求报文"，服务器发送给客户端的称为"响应报文"。**



即，**HTTP的主要作用：**

* **`规定客户端和服务端交互的方式`**

* **`规定客户端和服务端交互数据的格式`**

交互方式分为请求和响应。**请求**，是客户端向服务端发送；**响应**，是服务端向客户端发送。

客户端和服务端交互的数据称之为报文，请求时交互的数据称为**请求报文**；响应时返回的数据称之为**响应报文**。





### HTTP的各个版本发展历程

> HTTP/0.9 

+ 蒂姆伯纳斯李是一位英国计算机科学家，也是万维网的发明者。他在 1989 年创建了单行 HTTP 协议。它只是返回一个网页。这个协议在 1991 年被命名为 HTTP/0.9。 

>  HTTP/1.0

+  1996 年，HTTP/1.0 发布。该规范是显著扩大，并且支持三种请求方法：GET，Head，和POST。 
+  HTTP/1.0 相对于 HTTP/0.9 的改进如下：
   + 每个请求都附加了 HTTP 版本。
   + 在响应开始时发送状态代码。
   + 请求和响应都包含 HTTP 报文头。
   + 内容类型能够传输 HTML 文件以外的文档。
+  但是，HTTP/1.0 不是官方标准。

> HTTP/1.1

+ HTTP 的第一个标准化版本 HTTP/1.1 ( RFC 2068 ) 于 1997 年初发布，支持七种请求方法：OPTIONS，GET，HEAD，POST，PUT，DELETE，和TRACE 

+ HTTP/1.1 是 HTTP 1.0 的增强：

  + 虚拟主机允许从单个 IP 地址提供多个域。

  + 持久连接（长连接）和流水线连接允许 Web 浏览器通过单个持久连接发送多个请求。

  + 缓存支持节省了带宽并使响应速度更快。

+ HTTP/1.1 在接下来的 15 年左右将非常稳定。 

+ 在此期间，出现了 HTTPS（安全超文本传输协议）。它是使用 SSL/TLS 进行安全加密通信的 HTTP 的安全版本。 

> HTTP/2

+  由IETF在2015年发布。HTTP/2旨在提高Web性能，减少延迟，增加安全性，使Web应用更加快速、高效和可靠。 

- 多路复用：HTTP/2 允许同时发送多个请求和响应，而不是像 HTTP/1.1 一样只能一个一个地处理。这样可以减少延迟，提高效率，提高网络吞吐量。
- 二进制传输：HTTP/2 使用二进制协议，与 HTTP/1.1 使用的文本协议不同。二进制协议可以更快地解析，更有效地传输数据，减少了传输过程中的开销和延迟。
- 头部压缩：HTTP/2 使用 HPACK 算法对 HTTP 头部进行压缩，减少了头部传输的数据量，从而减少了网络延迟。
- 服务器推送：HTTP/2 支持服务器推送，允许服务器在客户端请求之前推送资源，以提高性能。
- 改进的安全性：HTTP/2 默认使用 TLS（Transport Layer Security）加密传输数据，提高了安全性。
- 兼容 HTTP/1.1：HTTP/2 可以与 HTTP/1.1 共存，服务器可以同时支持 HTTP/1.1 和 HTTP/2。如果客户端不支持 HTTP/2，服务器可以回退到 HTTP/1.1。

> HTTP/3

+ 于 2021 年 5 月 27 日发布 , HTTP/3 是一种新的、快速、可靠且安全的协议，适用于所有形式的设备。 HTTP/3 没有使用 TCP，而是使用谷歌在 2012 年开发的新协议 QUIC 
+ HTTP/3 是继 HTTP/1.1 和 HTTP/2之后的第三次重大修订。 

+ HTTP/3 带来了革命性的变化，以提高 Web 性能和安全性。设置 HTTP/3 网站需要服务器和浏览器支持。

+ 目前，谷歌云、Cloudflare和Fastly支持 HTTP/3。Chrome、Firefox、Edge、Opera 和一些移动浏览器支持 HTTP/3。



目前使用的HTTP协议，一般都是HTTP/1.1版本。

我们可以在Tomcat的server.xml配置文件中，查看当前的Tomcat所使用的HTTP协议：

<img src=".\images\image-20240531050952675.png" alt="image-20240531050952675" style="zoom:50%;" /> 

连最新的Tomcat10都是使用HTTP/1.1的版本，所以其实在未来很长时间，都是使用这个HTTP/1.1版本的。



### HTTP的协议的会话方式

浏览器与服务器之间的通信要经历四个步骤：

![](.\images\1557672342250_1H8nt17MNz.png)

* 浏览器与WEB服务器的连接过程是短暂的，每次连接只处理一个请求和响应。对每一个页面的访问，浏览器与WEB服务器都要建立一次单独的连接。
* 浏览器与WEB服务器之间的所有通讯都是完全独立分开的请求和响应对。



### HTTP的长连接与短连接

HTTP是使用TCP协议来让客户端与服务器之间建立连接的。每次客户端与服务端之间建立连接，都需要经过TCP连接所需的三次握手过程，同时在断开连接时，还需进行四次挥手的过程。

浏览器与Web服务器的每次连接，只能处理一个请求和响应。当我们去访问一个页面时，假如在该页面中，需要引入其他文件资源，比如，在login.html中，还需要引入三个资源文件：btn.css、btn.js以及logo.png：

<img src=".\images\image-20240531055116366.png" alt="image-20240531055116366" style="zoom:67%;" /> 

此时，我们去访问该页面，客户端和服务端之间就会建立一个连接，访问该login.html文件。客户端响应该页面给服务端显示，但是在响应之后，发现该页面需要使用另外三个文件，此时，客户端就会向服务端发送请求，让服务端将所需的其他文件也响应给客户端：

<img src=".\images\image-20240531055408633.png" alt="image-20240531055408633" style="zoom:80%;" /> 

如果此时，每次客户端请求一次数据，服务端响应一次数据，都让它们建立一次连接，就共需要建立四次连接。

但是，在之前也讲过，HTTP使用TCP协议建立连接，每次TCP建立连接都需要耗费比较大的资源，这样肯定是不行的。

所以，就需要引入长连接的概念。**`长连接`**，就是指**在一个连接上可以连续发送多个数据包，在资源全部加载完毕之后才断开连接。**

而**`短连接`**就是与之对应的，指的是**只在需要发送请求数据时才去建立一个连接，数据发送完成后就断开此连接，每次连接只完成一次数据的发送与接收工作。**

所以，对于上例中，就会使用长连接的方式，让四次的数据传输工作在一次连接中完成，这样就不需要经历多次连接从而造成资源的浪费。

**在HTTP1.0版本中，只支持短连接**，当浏览器请求一个带有图片的网页，会由于下载图片而与服务器之间开启一个新的连接。但是**在HTTP1.1版本后，默认是长连接的**，允许浏览器在拿到当前请求对应的全部资源后再断开连接，提高了效率。使用长连接后，就能够减少TCP连接创建的次数，减少了资源的消耗，并减少了延迟。

![](.\images\1557672415271_EgyN-GdbWY.png)

并且在HTTP1.1版本后，还支持缓存。

那什么是**缓存**呢？

比如，浏览器第一次去访问页面，加载了一个资源，比如一张图片，那么服务器就将该资源响应给客户端，客户端在展示完毕关闭后先不进行删除，先将该图片存着。当客户端后面再去加载图片时，就会先去查找之前是否存过该资源，如果有的话，就直接加载之前保存的资源，就不需要重新去请求加载了，这样就节省了带宽，让响应的速度更快。其实就类似于Mybatis中的缓存，就是将原本已经有的资源存起来，下次直接使用。









---

## 请求报文和响应报文

客户端和服务端交互的数据称之为报文，请求时交互的数据称为**请求报文**；响应时返回的数据称之为**响应报文**。

报文是有规定格式的，报文由下面的结构组成，分别是`行、头以及报文主体`。又因为报文分为请求报文和响应报文，所以这两种报文的格式名分别为：

|          | 请求报文 | 响应报文 |
| -------- | -------- | -------- |
| 行       | 请求行   | 响应行   |
| 头       | 请求头   | 响应头   |
| 报文主体 | 请求体   | 响应体   |



### 报文格式

报文主题上分为报文首部和报文主体，中间空行隔开。

<img src=".\images\1681522962846.png" alt="1681522962846" style="zoom: 62%;" />

![1681522998417](.\images\1681522998417.png)



报文部首则可以继续分为"行"和"头"。

通过在浏览器中，打开F12，在网络部分，点开其中一个请求：

<img src=".\images\image-20240531111327756.png" alt="image-20240531111327756" style="zoom:67%;" />



 

### 请求报文

请求报文是客户端发送给服务器的报文。

> **请求报文格式**：
>
> * **`请求行`：GET/POST 资源路径?参数 HTTP/1.1**
> * **`请求头`**
> * **`空行`**
> * **`请求体`（POST请求才有请求体）**

我们通过F12，来查看一下一个请求报文的信息：

![image-20240531113033283](.\images\image-20240531113033283.png)

其中，POST /TomcatTest_war_exploded/login.html HTTP/1.1就是**请求行**；

接下来部分，全部都是**请求头**的信息。我们可以看到，请求头中的数据都是以参数名: 参数值的形式进行传输的，即以key: value的形式传输。

那对于POST请求来说，对应的请求体并不在上面的表头位置，而是在负载中，负载中就是POST请求的**请求体**内容：

![image-20240531113150084](.\images\image-20240531113150084.png) 

然后，我们接下来，就分别叙述一下这个组成部分的内容。



**请求行的组成**

* 请求方式  `GET/POST`
* `访问服务器的资源路径?参数1=值1&参数2=值2...`（如果是GET则将参数放在路径上）
* 协议及版本 `HTTP/1.1`

案例：

```http
GET /05_web_tomcat/login_success.html?username=admin&password=123213 HTTP/1.1
```



**常见请求头内容的说明**

```http
-主机虚拟地址
Host: localhost:8080   
-长连接
Connection: keep-alive 
-请求协议的自动升级（发送请求时，客户端可能使用的是http协议，服务器却是https的，那么浏览器会自动将请求协议升级为https）
Upgrade-Insecure-Requests: 1  
- 用户系统信息
User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36
- 浏览器支持的文件类型
Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
- 当前页面的上一个页面的路径[当前页面通过哪个页面跳转过来的]，可以通过此路径跳转回上一个页面
Referer: http://localhost:8080/05_web_tomcat/login.html
- 浏览器支持的压缩格式
Accept-Encoding: gzip, deflate, br
- 浏览器支持的语言
Accept-Language: zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7
```



**请求体的说明**

* **POST请求有请求体，而GET请求没有请求体。**

* POST请求数据在请求体中携带，请求体数据没有大小限制，可以用来上传所有内容，包括文件、文本等；GET请求参数拼接在请求行中的url地址中，没有请求体，所以GET请求参数有大小限制，并且只能携带纯文本。

请求体案例：

![image-20240531114655022](.\images\image-20240531114655022.png) 

以上就是一个请求体的内容，请求参数的格式依旧是参数名1=值1&参数名2=值2...





**form表单发送GET请求的特点**

1. 由于请求参数在请求行中已经携带了，所以没有请求体，也没有请求空行
2. 请求参数拼接在url地址中，地址栏可见，不安全
3. 由于参数在地址栏中携带，所以有大小限制，并且只能携带纯文本
4. get请求只能上传文本数据，不可上传文件数据
5. 没有请求体，所以封装和解析块，效率高，浏览器默认提交的请求都是get请求。



**form表单发送POST请求的特点**

1. POST请求有请求体，而GET请求没有请求体。
2. POST请求数据在请求体中携带，请求体数据没有大小限制，可以用来上传所有内容。
3. 只能使用POST请求上传文件，GET请求不可上传文件。
4. POST请求的请求头中，多了请求体的相关配置。
5. 地址栏参数不可见，相对安全。
6. POST效率略比GET低（但可以忽略）
7. POST请求要求将form表单中的method属性设置为POST.







---

### 响应报文

> **响应报文格式：**
>
> * **`响应行`：协议/版本 状态码 状态码描述**
> * **`响应头`**
> * **`空行`**
> * **`响应体`**

我们通过F12，来查看一下响应报文的信息：

<img src=".\images\image-20240531120935846.png" alt="image-20240531120935846" style="zoom:80%;" /> 

以上的内容，是响应报文中的响应行与响应头信息。

其中，HTTP/1.1 200就是**响应行**，其中HTTP/1.1是协议/版本，304指的是响应状态码。

接下来的部分，全部都是**响应头**的信息，响应头使用参数名: 参数值的形式传输，即key: value。

对于**响应体**部分，并不会存在响应标头中，而是将其放在了响应页签中：

<img src=".\images\image-20240531120137908.png" alt="image-20240531120137908" style="zoom:67%;" /> 

也就是服务器具体响应体的内容，该内容是没有经过浏览器解析的，在预览中则是经过浏览器解析后的响应体内容。



**响应行组成部分**

* `协议及版本` HTTP/1.1
* `响应状态码` 如200
* 状态描述 如OK（缺省）

例如：

```http
HTTP/1.1 200 OK
说明：响应协议为HTTP1.1，响应状态码为200，表示请求成功； 
```

从Tomcat 10开始，会将响应状态码的描述文字给省略了，因为该描述文字是给人看的，而不是给机器看的。人，一般只有程序员来看这个响应报文，所以对于比较常见的响应码的描述文字，就省略了，比如当响应码为200时，描述文字为OK，这里的OK就省略了。对于Tomcat 10之前的版本，就没有将描述文字缺省。



**响应头内容的说明**

```http
Accept-Ranges: bytes
ETag: W/"3818-1717125748708"
Last-Modified: Fri, 31 May 2024 03:22:28 GMT
--表示MIME的类型，该参数是告诉客户端响应体中的数据是什么类型，方便客户端进行解析。text/html就表示响应体中的数据是HTML文档
Content-Type: text/html
--表示响应体的长度，为了方便客户端对响应回来的响应体进行校验
Content-Length: 3818
Date: Fri, 31 May 2024 04:09:15 GMT
--长连接的超时时间，当长连接超过指定时间后还没有发送请求，那么该长连接就会被关闭
Keep-Alive: timeout=20
--当前连接属于长连接
Connection: keep-alive
```



响应体就是服务器返回给我们的页面数据。





### 常见响应状态码

响应状态码对浏览器来说很重要，它告诉了浏览器响应的结果。

响应状态码在响应报文的第一行：

<img src=".\images\image-20240531122635539.png" alt="image-20240531122635539" style="zoom:80%;" /> 

**比较有代表性的响应码如下：**

* **`200`**：请求成功，浏览器会把响应体内容（通常是html）显示在浏览器中；
* **`302`**：重定向，当响应码为302时，表示服务器要求浏览器重新再发一个请求，服务器会发送一个响应头Location指定新请求的URL地址；
* **`304`**：使用了本地缓存；
* **`404`**：请求的资源没有找到，说明客户端错误的请求了不存在的资源；
* **`405`**：请求的方式不允许；
* **`500`**：请求资源找到了，但服务器内部出现了问题。



更多的状态码：

| 状态码 | 状态码英文描述                  | 中文含义                                                     |
| :----- | :------------------------------ | :----------------------------------------------------------- |
| 1**    |                                 |                                                              |
| 100    | Continue                        | 继续。客户端应继续其请求                                     |
| 101    | Switching Protocols             | 切换协议。服务器根据客户端的请求切换协议。只能切换到更高级的协议，例如，切换到HTTP的新版本协议 |
| 2**    |                                 |                                                              |
| 200    | OK                              | 请求成功。一般用于GET与POST请求                              |
| 201    | Created                         | 已创建。成功请求并创建了新的资源                             |
| 202    | Accepted                        | 已接受。已经接受请求，但未处理完成                           |
| 203    | Non-Authoritative Information   | 非授权信息。请求成功。但返回的meta信息不在原始的服务器，而是一个副本 |
| 204    | No Content                      | 无内容。服务器成功处理，但未返回内容。在未更新网页的情况下，可确保浏览器继续显示当前文档 |
| 205    | Reset Content                   | 重置内容。服务器处理成功，用户终端（例如：浏览器）应重置文档视图。可通过此返回码清除浏览器的表单域 |
| 206    | Partial Content                 | 部分内容。服务器成功处理了部分GET请求                        |
| 3**    |                                 |                                                              |
| 300    | Multiple Choices                | 多种选择。请求的资源可包括多个位置，相应可返回一个资源特征与地址的列表用于用户终端（例如：浏览器）选择 |
| 301    | Moved Permanently               | 永久移动。请求的资源已被永久的移动到新URI，返回信息会包括新的URI，浏览器会自动定向到新URI。今后任何新的请求都应使用新的URI代替 |
| 302    | Found                           | 临时移动。与301类似。但资源只是临时被移动。客户端应继续使用原有URI |
| 303    | See Other                       | 查看其它地址。与301类似。使用GET和POST请求查看               |
| 304    | Not Modified                    | 未修改。所请求的资源未修改，服务器返回此状态码时，不会返回任何资源。客户端通常会缓存访问过的资源，通过提供一个头信息指出客户端希望只返回在指定日期之后修改的资源 |
| 305    | Use Proxy                       | 使用代理。所请求的资源必须通过代理访问                       |
| 306    | Unused                          | 已经被废弃的HTTP状态码                                       |
| 307    | Temporary Redirect              | 临时重定向。与302类似。使用GET请求重定向                     |
| 4**    |                                 |                                                              |
| 400    | Bad Request                     | 客户端请求的语法错误，服务器无法理解                         |
| 401    | Unauthorized                    | 请求要求用户的身份认证                                       |
| 402    | Payment Required                | 保留，将来使用                                               |
| 403    | Forbidden                       | 服务器理解请求客户端的请求，但是拒绝执行此请求               |
| 404    | Not Found                       | 服务器无法根据客户端的请求找到资源（网页）。通过此代码，网站设计人员可设置"您所请求的资源无法找到"的个性页面 |
| 405    | Method Not Allowed              | 客户端请求中的方法被禁止                                     |
| 406    | Not Acceptable                  | 服务器无法根据客户端请求的内容特性完成请求                   |
| 407    | Proxy Authentication Required   | 请求要求代理的身份认证，与401类似，但请求者应当使用代理进行授权 |
| 408    | Request Time-out                | 服务器等待客户端发送的请求时间过长，超时                     |
| 409    | Conflict                        | 服务器完成客户端的 PUT 请求时可能返回此代码，服务器处理请求时发生了冲突 |
| 410    | Gone                            | 客户端请求的资源已经不存在。410不同于404，如果资源以前有现在被永久删除了可使用410代码，网站设计人员可通过301代码指定资源的新位置 |
| 411    | Length Required                 | 服务器无法处理客户端发送的不带Content-Length的请求信息       |
| 412    | Precondition Failed             | 客户端请求信息的先决条件错误                                 |
| 413    | Request Entity Too Large        | 由于请求的实体过大，服务器无法处理，因此拒绝请求。为防止客户端的连续请求，服务器可能会关闭连接。如果只是服务器暂时无法处理，则会包含一个Retry-After的响应信息 |
| 414    | Request-URI Too Large           | 请求的URI过长（URI通常为网址），服务器无法处理               |
| 415    | Unsupported Media Type          | 服务器无法处理请求附带的媒体格式                             |
| 416    | Requested range not satisfiable | 客户端请求的范围无效                                         |
| 417    | Expectation Failed              | 服务器无法满足Expect的请求头信息                             |
| 5**    |                                 |                                                              |
| 500    | Internal Server Error           | 服务器内部错误，无法完成请求                                 |
| 501    | Not Implemented                 | 服务器不支持请求的功能，无法完成请求                         |
| 502    | Bad Gateway                     | 作为网关或者代理工作的服务器尝试执行请求时，从远程服务器接收到了一个无效的响应 |
| 503    | Service Unavailable             | 由于超载或系统维护，服务器暂时的无法处理客户端的请求。延时的长度可包含在服务器的Retry-After头信息中 |
| 504    | Gateway Time-out                | 充当网关或代理的服务器，未及时从远端服务器获取请求           |
| 505    | HTTP Version not supported      | 服务器不支持请求的HTTP协议的版本，无法完成处理               |







## Content-type属性的说明

### 1、什么是Content-Type？

Content-Type，即是Internet Media Type，互联网媒体类型，也叫作`MIME类型`。在互联网中有成百上千种不同的数据类型，HTTP在传输数据对象时会为它们打上MIME的数据格式标签，用于区分数据类型。最初MIME是用于电子邮件系统的，后来HTTP也采用了这一方案。

在HTTP协议头中，使用Content-Type来表示请求和响应中的媒体类型消息。**它用来告诉服务端如何处理请求的数据，以及告诉客户端如何解析响应的数据**。比如如何去显示图片，解析并展示html等。

<img src=".\images\image-20240601170406245.png" alt="image-20240601170406245" style="zoom:50%;" /> 





### 注意：get请求中的headers没有content-type

get请求的headers中是没有content-type这个字段的。content-type是用来指定消息体格式的，get请求一般没有新消息体，所以get请求一般不用设置content-type。





### 2、Content-Type的格式

**`type/subtype;parameter`**

* type：主类型，任意的字符串，如text，如果是*代表所有。
* subtype：子类型，任意的字符串，如html，如果是*代表所有，使用"/"与主类型隔开；
* parameter：可选类型，如charset, boundary等。

例如：

Content-Type: text/html;

Content-Type: application/json;charset:uft-8s



### 3、常见的Content-Type类型

* text/plain：纯文本格式；

* text/html：HTML格式；
* text/css：Cascading Style Sheets；
* text/javascript：JavaScript代码；
* application/json：JSON格式数据；
* application/xml：XML格式数据；
* application/octet-stream：二进制流数据；
* image/jpeg：JPEG格式图片；
  image/gif：GIF格式图片；
* image/png：PNG格式图片；
* audio/mpeg：MP3格式音频；
* video/mp4：MP4格式视频；
* multipart/form-data：表单数据；
* application/x-www-form-urlencoded：URL编码表单数据；

还有许多其他的 Content-type 类型，具体使用取决于需要传输的数据格式。



### 4、四种需要记住的Content-Type

* **`application/x-www-form-urlencoded`**

HTTP会将请求参数用key1=val1&key2=val2的方式进行组织，并放到请求实体里面，注意如果是中文或特殊字符如`"/"、","、“:" `等会自动进行URL转码。**不支持文件，一般用于表单提交。**

![在这里插入图片描述](.\images\20190624231322976.png)



* **`multpart/form-data`**

与application/x-www-form-urlencoded不同，这是一个多部分多媒体类型。首先生成了一个 boundary 用于分割不同的字段，在请求实体里每个参数以------boundary开始，然后是附加信息和参数名，然后是空行，最后是参数内容。多个参数将会有多个boundary块。如果参数是文件会有特别的文件域。最后以------boundary–为结束标识。**multipart/form-data支持文件上传的格式，一般需要上传文件的表单则用该类型。**
<img src=".\images\20190624231435322.png" alt="在这里插入图片描述" style="zoom:67%;" />



* **`application/json`**

`JSON `是一种轻量级的数据格式，以“键-值”对的方式组织的数据。这个使用这个类型，需要参数本身就是`json`格式的数据，参数会被直接放到请求实体里，不进行任何处理。服务端/客户端会按`json`格式解析数据（约定好的情况下）。

![在这里插入图片描述](.\images\20190624231555162.png) 



* **`application/xml` 和 `text/xml`**

与application/json类似，这里使用的是xml格式的数据，text/xml的话。将忽略xml数据里的编码格式。





### 5、Content-Type的使用

#### 1）request中的Content-Type

一般我们在开发的过程中现需要注意客户端发送请求（Request）的`Content-Type`设置。比如在spring中，如果接口使用了`@RequestBody`，spring强大的自动解析能力，会将请求实体的内容自动转换为`Bean`，但前提是请求的`Content-Type`必须设置为`application/json`，否则就会返回`415`错误。

> **建议：**
>
> * 如果是一个restful接口（json格式），一般将Content-Type设置为`application/json,charset=utf-8`。
> * 如果是文件上传，一般Content-Type设置为`multipart/form-data`。
> * 如果是普通表单提交，一般Content-Type设置为`application/x-www-form-unlencoded`。





#### 2）response的Content-Type

服务器响应（Response）的Content-Type最好也保持准确。虽然一般web开发中，前端解析响应的数据不会根据Content-Type，并且服务端一般能自动设置准确的Content-Type，但是如果乱设置在某些情况下会出现问题，比如导出文件时，如果在spring项目里使用@ResponseBody，spring会将响应的Content-Type设置为application/json;charset=UTF-8，可能导致文件无法导出，需要注意下。

> **建议**：
>
> * 一般情况下，都是使用application/json,charset=utf-8，一般后端一般返回的都是json格式的数据‘
> * 如果是文件导出，`Content-Type` 设置为 `multipart/form-data`，并且添加一个`Content-Disposition`设置为`attachment;fileName=文件.后缀`。



**注：** `Content-Disposition`是`Content-Type`的扩展，告诉浏览器弹窗下载框，而不是直接在浏览器里展示文件。因为一般浏览器对于它能够处理的文件类型，如`txt，pdf` 等，它都是直接打开展示，而不是弹窗下载框。
