[TOC]



# 一、Servlet简介

## 1、动态资源和静态资源

**静态资源**：无需在程序运行时通过代码生成的资源，在程序运行之前就写好的资源。例如html、js、css、img，音频和视频文件。

**动态资源**：需要在程序运行时通过代码生成的资源，在程序运行前无法确定的数据，运行时动态生成。例如Servlet，Thymeleaf...。

动态资源不是指动态生成的视图或者动画。

动态资源，是指根据传入的参数，动态生成的资源；但是静态资源是已经确定了的资源，不会动态生成。





## 2、Servlet的说明

**获取静态资源的流程**：

假如浏览器要去获取服务器中的图片资源，会发送一个请求报文给服务器。服务器接收到请求报文后，返回一个响应报文给浏览器，响应报文包括响应行、响应头和响应体。响应头中的Content-Type参数就会指明当前响应体的数据是什么类型，响应体中则是图片数据。

![image-20240601094518282](.\images\image-20240601094518282.png)

**获取动态资源的流程：**

浏览器要去获取动态资源，也是先去发送一个请求给服务端，服务端接收到请求后，会去运行内部的Java代码，根据请求的数据，运行Java代码得到要返回的数据，并将Java代码执行的结果放在响应体中返回给客户端。

![image-20240601095327136](.\images\image-20240601095327136.png)

以上就是获取动态资源的流程。

> **不是所有的JAVA类都能用于处理客户端请求，在服务端中处理客户端请求并做出响应的一套技术标准就是`Servlet`。**

Servlet是运行在服务端的，所以Servlet必须在Web项目中开发并且在Tomcat这样的服务容器中运行。

**Servlet说明**

Servlet是运行在服务端的Java小程序，是sun公司提供的一套定义动态资源规范；从代码层面上来讲Servlet就是一个接口。

Servlet用来接收、处理客户端请求、响应给浏览器的动态资源。在整个Web应用中，Servlet主要负责接收处理请求、协同调度功能以及响应数据。我们可以把Servlet称为Web应用中的**`控制器`**。





## 3、Servlet流程说明

浏览器只会发送报文和接收报文，浏览器不知道服务端是静态资源还是动态资源。

浏览器发送请求报文给Tomcat，Tomcat接收到请求后，会将请求报文的信息转换为一个**`HttpServletRequest`**对象，该对象中包含了请求中的所有信息，包括请求行、请求头以及请求体。

Tomcat同时创建一个**`HttpServletResponse`**对象，该对象用于承装要响应给客户端的信息，后面该对象会被转换成响应的报文，即包括响应行、响应头以及响应体。

Tomcat会根据请求中的资源路径找到对应的servlet，将servlet实例化，调用其中的**`service()`**方法，并将HttpServletRequest和HttpServletResponse对象传入。

比如说，我们将servlet中的路径设置为/aaa，同时在浏览器发送的请求体中的请求行中资源路径也是/aaa，那么Tomcat就能够将请求体中的资源路径与servlet中的路径对应，从而找到相应的servlet。

![image-20240601101520480](.\images\image-20240601101520480.png)

我们需要自己去开发Java代码，拿到请求报文信息以及设置响应报文数据，对它们进行操作。那么，我们需要自己去创建一个实现Servlet接口的类，并且实现service()方法，在该方法中有两个参数，第一个参数是HttpServletRequest，也就是请求报文对象；第二个参数是HttpServletResponse，也就是响应报文对象。

其中request对象相当于是service()方法传进来，一开始就有数据的对象；

response对象相当于是service()方法要传出去的，一开始没有数据的对象。



在service()方法中的一系列代码，就是程序员自己开发的，在**service()**中我们可以做的事情：

1. 获取请求的所有信息（一般是获取请求中的参数）
2. 根据参数生成要响应给客户端的数据
3. 将响应的数据放入response对象，该对象会被转成响应行、响应头以及响应体。



<img src=".\images\1681544428055.png" alt="1681544428055" style="zoom:50%;" />

比如用户要去显示员工的列表，就会发送请求给服务端，服务端使用Servlet接收请求报文数据并且转换成request对象，Java就根据request对象使用JDBC技术读取数据库，查询员工列表信息，并获取，Java对象使用集合来接收。将接收到的集合对象，放入到response对象中，然后使用Servlet技术转换成响应报文发送给客户端。

也是就说，Servlet的作用就是去接收请求报文，转换成request对象；并将创建的response对象转换成响应报文，发送给客户端。





---

# 二、Servlet开发案例与说明

**目标**：检验注册时，用户名是否被占用。通过客户端向Servlet发送请求，携带username，如果用户名被占用，则向客户端响应NO；如果未被占用，则响应YES。

其中，用户信息表user，该表位于java_web数据库下：

<img src=".\images\image-20240601104627977.png" alt="image-20240601104627977" style="zoom:67%;" /> 

## 案例流程（:star:）

### 1、数据准备

#### 1）创建子项目并转换成Web项目

创建一个子项目，就叫做demo02-servlet01：

![image-20240601105512314](.\images\image-20240601105512314.png) 

使用JBLJavaToWeb插件，将该项目转成Java Web项目（也可以手动改，具体内容请查看maven）：

<img src=".\images\image-20240601110826064.png" alt="image-20240601110826064" style="zoom:67%;" /> 



#### 2）确保项目关联maven仓库并部署Tomcat

需要确保当前项目关联了本地的Tomcat服务器以及maven仓库：

<img src=".\images\image-20240601110941505.png" alt="image-20240601110941505" style="zoom: 33%;" /> 

<img src=".\images\image-20240601110914249.png" alt="image-20240601110914249" style="zoom:33%;" /> 

然后，需要让当前项目的运行使用Tomcat：

<img src=".\images\image-20240601132834396.png" alt="image-20240601132834396" style="zoom: 33%;" />

<img src=".\images\image-20240601132946486.png" alt="image-20240601132946486" style="zoom: 33%;" /> 

在Tomcat的Deployment中，添加当前项目：

<img src="C:\Users\14036\AppData\Roaming\Typora\typora-user-images\image-20240601132643861.png" alt="image-20240601132643861" style="zoom: 33%;" /> 

![image-20240601133118334](.\images\image-20240601133118334.png) 

选择war exploded版本（将当前项目的本地路径位置传给Tomcat，可以实现热部署）：

<img src=".\images\image-20240601133136317.png" alt="image-20240601133136317" style="zoom:50%;" /> 

将上下文路径修改成demo02，点击OK：

![image-20240601133327304](.\images\image-20240601133327304.png) 

然后，将当前项目添加热部署功能（具体请查看Tomcat内容）：

<img src=".\images\image-20240601133536073.png" alt="image-20240601133536073" style="zoom: 50%;" /> 









#### 3）使用逆向工程创建表对应的各种类

由于Servlet侧重于客户端与服务端之间数据的请求与响应，所以对于数据库的访问我这里就比较简单的省略了：
创建com.atguigu.servlet.mapper包与com.atguigu.servlet.pojo包，使用逆向工程，生成user表所对应的实体类以及Mapper接口和mapper映射文件：

 <img src=".\images\image-20240601111517201.png" alt="image-20240601111517201" style="zoom: 67%;" />

创建jdbc.properties数据源配置文件以及创建mybatis核心配置文件：mybatis-config.xml：

![image-20240601111542414](.\images\image-20240601111542414.png) 

jdbc.properties数据源配置文件

```properties
jdbc.username=root
jdbc.password=061535asd
jdbc.url=jdbc:mysql://localhost:3306/java_web
jdbc.driverClassName=com.mysql.cj.jdbc.Driver
```

mybatis核心配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//MyBatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--引入properties文件-->
    <properties resource="jdbc.properties"></properties>


    <settings>
        <!--开启驼峰命名法-->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>


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


    <!--引入映射文件-->
    <mappers>
        <package name="com.atguigu.servlet.mapper"/>
    </mappers>
</configuration>
```





最后，我们去创建一个SqlSession对象创建的工具类，用于获取SqlSession对象，省的每次获取都要执行很多步骤。创建com.atguigu.servlet.utils包，在该包下创建SqlSessionUtils类用于获取SqlSession对象：

```java
public class SqlSessionUtils {
    public static SqlSession getSqlSession(){
        try {
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactoryBuilder sessionFactoryBuilder = new SqlSessionFactoryBuilder();
            SqlSessionFactory sqlSessionFactory = sessionFactoryBuilder.build(is);
            return sqlSessionFactory.openSession(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

此时，对于数据库的访问配置算是完成了。

<img src=".\images\image-20240601115423677.png" alt="image-20240601115423677" style="zoom: 45%;" /> 





#### 4）引入Servlet依赖

**方式一：引入Tomcat10的依赖（不推荐）**

由于当前项目已经关联了本地的Tomcat10，所以我们可以通过引入Tomcat10的依赖，从而引入Servlet依赖。

选择Project Structure：

<img src=".\images\image-20240601112921567.png" alt="image-20240601112921567" style="zoom: 33%;" /> 

选择Modules，选择当前的工程，在Dependencies中，点击+添加依赖：

<img src=".\images\image-20240601113047407.png" alt="image-20240601113047407" style="zoom: 33%;" /> 

选择Library...：

![image-20240601113151440](.\images\image-20240601113151440.png) 

在弹出的choose libraries窗口中，选择已经与IDEA关联的本地Tomcat 10.1.24，点击添加，然后点击OK。

<img src=".\images\image-20240601113230408.png" alt="image-20240601113230408" style="zoom:50%;" /> 

这样一来，就引入了本地的Tomcat10的依赖。

该依赖中包含Servlet的依赖以及jsp的依赖：

<img src=".\images\image-20240601113616074.png" alt="image-20240601113616074" style="zoom: 33%;" /> 





**方式二（推荐）：手动引入Servlet依赖**

还是使用maven仓库的方式引入Servlet依赖，方便我们查看依赖的版本以及调用依赖，如果直接引入Tomcat10的话不方便查看版本信息。

**引入依赖：**

```xml
<dependencies>
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

注意，这里引入的servlet依赖是以jakarta开头的，因为这里的Tomcat版本是10，而Tomcat10之前的版本，servlet的版本则是javax开头的。

并且，我们还需要将该依赖的生命周期设置为**`provided`**，即**只在编译和测试中有效**，其余不生效。这是为什么呢？在这一章的问题说明中讲解。

Tomcat10和历史版本的不同之处：Oracle将Java EE（Java SE还自己保留）交给开源组织Eclipse基金会接手。但Oracle不允许开源组织使用Java名号，所以Jakarta EE名称于2018.02.26应运而生，所以Tomcat 10将原本的`javax.*`包都改名为`jakarta.*`。

**对于Tomcat 10之前的版本，需要引入的Servlet包是以`javax`开头的，而从Tomcat 10以后，需要使用的Servlet包则都是以`jakarta`开头的。包与版本之间要对应，不能搞混，否则运行时就会报错。**





#### 5）创建校验页面

在webapp目录下，创建一个校验页面index.html，去发送username给服务端，让服务端校验数据。

当Tomcat启动时，默认是去访问指定项目路径下，如http://localhost:8080/项目名/，当我们创建index.html放在项目的webapp目录下，则该路径会去自动访问index.html。

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<form action="" method="get">
    用户名：<input type="text" name="username" id=""><br/>
    <input type="submit" value="校验">
</form>
</body>
</html>
```

这是一个很简单的功能，发送只有一个表单项的表单数据给后端，让后端对传过来的username参数进行校验。

其中action这里暂时还是空，因为现在暂时还未在Servlet设置路径。



 





### 2、创建Serlet实现类并重写service()

我们要去创建一个Servlet接口的实现类UserServlet，去实现其中的service()方法，该方法就是主要用于实现处理客户端请求以及处理返回服务端响应的方法：

```java
public class UserServlet implements Servlet{
}
```

实现该接口，发现需要实现其中的五个抽象方法：

<img src=".\images\image-20240601115621082.png" alt="image-20240601115621082" style="zoom: 50%;" /> 

但是目前我们主要是需要实现其中的service()方法，要是直接实现该接口还需要实现另外的四个方法。

此时，可以去修改成**通过继承Servlet实现类`HttpServlet`的方式，间接实现Servlet接口。**

抽象HttpServlet类继承于抽象类GenericServlet类，GenericServlet类实现了Servlet接口。

使用Show Diagram查看类的实现与继承关系：

<img src=".\images\image-20240601120536827.png" alt="image-20240601120536827" style="zoom: 40%;" /> 

![image-20240601120216666](.\images\image-20240601120216666.png)

那么，我们自定义类UserServlet，继承了HttpServlet，就相当于间接实现了Servlet接口：

使用UserServlet重写service()方法，注意这里重写的方法是以HttpServletRequest与HttpServletResponse为参数的service()方法，不是其他的service()方法：

使用Alt + insert快捷键实现快速重写方法：

<img src=".\images\image-20240601120907788.png" alt="image-20240601120907788" style="zoom:40%;" /> 

```java
public class UserServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }
}
```

* HttpServletRequest：代表请求对象，是由请求报文经过Tomcat转换过来的，通过该对象可以获取请求中的信息。
* HttpServletResponse：代表响应对象，该对象会被Tomcat转换为响应报文，通过该对象可以设置响应中的信息。
* Servlet对象的生命周期（创建、初始化、处理服务、销毁）是由Tomcat管理的，无需我们自己去new。

* HttpServletRequest、HttpServletResponse两个对象也是由Tomcat负责转换，在调用service()方法传入我们用的。









### 3、在service()中处理相关业务

重写了service()方法，那么我们就需要在service()方法中，去获取请求报文中的数据，处理相关的业务，并将处理好的业务数据放入到响应体中，发送给服务端。

请求报文放在HttpServletRequest参数中，响应报文放在HttpServletResponse参数中。

案例中，客户端使用的是get方式发送请求，请求参数是username，那么url是：请求路径?username=值，即请求参数是`键=值`的格式；

如果使用post方式发送请求，则请求参数放在请求体中，格式也是键=值的格式。

那么，我们在service()方法中，可以**使用`request.getParameter(参数名)`的方式，根据参数名获取参数值**。无论参数是在url中，还是请求体中，都是使用这种方式去获取参数值。

之后，便是一些业务的处理，将获取到的请求体的操作进行业务操作。

业务操作完毕后，我们要响应数据给客户端，**使用`response.getWriter()`方法，获取一个PritWriter对象，该对象是一个向响应体中打印字符流的打印流。**使用这个流去打印数据时，该流就会将打印的数据放到response响应体中。

之后，使用PritWriter对象的`wirte()`方法或者`print()`方法，打印数据到响应体中。

实现：

```java
public class UserServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1、从request对象中获取请求中的任何信息
        String username = req.getParameter("username");//根据

        //2、处理业务的代码，根据username，去查询是否有对应的数据
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        //创建Example条件类
        UserExample example = new UserExample();
        //添加username的等于条件
        example.createCriteria().andUserNameEqualTo(username);
        List<User> users = userMapper.selectByExample(example);


        //3、将要响应的数据放入response中
        if (users != null && !users.isEmpty()){
            resp.getWriter().write("NO");
        }else {
            resp.getWriter().write("YES");
        }

    }
}
```







### 4、在web.xml中配置Servlet对应的请求映射路径

在web.xml中使用配置文件的方式，配置Serlvet所对应的请求映射。（在开发中一般使用注解的方式进行配置）

创建Servlct标签，在该标签中创建`<servlet-name>`标签与`<servlet-class>`标签，其中servlet-name是给创建Servlet取一个别名，并且去关联请求的映射路径；servlet-class则是指明需要实例化的Servlet所对应的类。

```xml
<servlet>
    <servlet-name>userServlet</servlet-name>
    <servlet-class>com.atguigu.servlet.UserServlet</servlet-class>
</servlet>   
```

之后，创建`<servlet-mapping>`标签，该标签是用于指明Servlet别名与请求映射路径的对应关系的。在该标签中创建两个子标签：`servlet-name`与`url-pattern`，其中的servlet-name是刚才所创建Servlet的别名，url-pattern则是设置的请求路径，并且url-pattern中的参数需要以/开头，表明请求的路径。

```xml
<servlet-mapping>
    <servlet-name>userServlet</servlet-name>
    <url-pattern>/userServlet</url-pattern>
</servlet-mapping>
```

这样一来，刚才所创建的Servlet就在web.xml中配置完毕了：

<img src=".\images\image-20240601131910077.png" alt="image-20240601131910077" style="zoom:50%;" />







### 5、在表单中添加Servlet路径

<img src=".\images\image-20240601134501434.png" alt="image-20240601134501434" style="zoom:50%;" /> 

在表单的action属性中，添加刚才所创建的Servlet路径。

注意，这里的userServlet前面暂时不要加/，加/还需要做其他的处理，将在后面讲述。







### 6、运行测试

给Servlet的service()方法中添加断点：

<img src=".\images\image-20240601134656965.png" alt="image-20240601134656965" style="zoom: 50%;" /> 

使用DEBUG方式，运行Tomcat（建议全部都使用Debug方式运行）。

此时，浏览器页面显示：

![image-20240601134746631](.\images\image-20240601134746631.png) 

随便输入一个111，点击校验。此时请求发出，程序就会进入到service()方法中，并且停在了获取参数的那一行中：

![image-20240601134922850](.\images\image-20240601134922850.png)

此时的username就是111:

<img src=".\images\image-20240601134941508.png" alt="image-20240601134941508" style="zoom:67%;" /> 

继续执行，就会去MySQL中数据库中查询是否有username=111的数据，执行查询完毕，数据库中并没有相应的数据，此时所查询到的userList的长度就应该为0：

![image-20240601135124742](.\images\image-20240601135124742.png) 

那么此时，if判断语句不成立，会打印一个YES放在响应体中给客户端：

![image-20240601135332737](.\images\image-20240601135332737.png)

我们来看看这个请求的请求行和请求头：

![image-20240601135507525](.\images\image-20240601135507525.png)

请求方式是GET，请求路径是/demo02/userServlet?username=111

请求体是：

<img src=".\images\image-20240601135636334.png" alt="image-20240601135636334" style="zoom:80%;" /> 







响应的响应行和响应头：

![image-20240601135616066](.\images\image-20240601135616066.png) 

Content-Length是3，也就是响应体就是3个字符：YES。

响应体：

<img src=".\images\image-20240601135713518.png" alt="image-20240601135713518" style="zoom:50%;" /> 









---

## Servlet依赖作用周期的问题

我们需要在maven中，将引入的Servlet依赖的scope属性设置为**`provided`**值。

```xml
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.0.0</version>
    <scope>provided</scope>
</dependency>
```

provided属性值表示该依赖在编译与测试时是生效的，但是在运行时不生效。

这是为什么？

因为我们通常会将Web应用程序部署到Servlet容器（比如Tomcat）中，而这些容器已经包含了Servlet-api的实现。

比如，对于Tomcat10中，已经包含了该Servlet的api：

<img src=".\images\image-20240601142413131.png" alt="image-20240601142413131" style="zoom: 43%;" /> 

如果我们使用引入maven依赖的方式引入servlet的api，那么在我们部署项目时，创建出来的项目中就会包含servlet的jar包，此时，通过Tomcat去运行项目，就会发生冲突。

那么，为了避免与Tomcat中的servlet-api包发生冲突，我们需要将Servlet的坐标中的scope设置为provided，这也就意味着该包只在编译和测试时生效，在运行时不会被包含在应用程序中，而是直接使用Servlet容器提供的实现。











---

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



### 3、常见的Content-Type

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



### 4、四种重要的Content-Type

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





### 6、给案例响应头添加Content-Type属性

我们来查看一下刚才的Servlet例子中的请求与响应数据：

请求

<img src=".\images\image-20240601164857422.png" alt="image-20240601164857422" style="zoom: 67%;" /> 

响应

<img src=".\images\image-20240601164801195.png" alt="image-20240601164801195" style="zoom:67%;" /> 

发现在请求和响应中，都没有Content-Type数据。

该请求是get请求，所以没有请求体信息，也就没有Content-Type数据。

而对于响应来说，我们并没有在Servlet的service()方法中，去设置response响应的Content-Type类型数据，所以在响应中也就没有Content-Type数据。

**当响应中，没有Content-Type数据时，浏览器就会将响应体当成是`HTML`数据进行处理。**

服务器响应（Response）的Content-Type最好也保持准确，那如何设置response的Content-Type呢？

使用`HttpServletResponse`对象的`setHeader()`或者`setContentType()`方法进行设置。

`setHeader()`方法，是去设置响应头信息的，传入两个参数，第一个参数表示属性名，第二个参数表示属性值。

那么，使用setHeader()的方式为：

```java
//希望客户端将响应体按照html文件来解析
response.setHeader("Content-Type", "text/html")
```



也可以直接使用`setContentType()`方法，该方法是专门用于设置头中的Content-Type属性的：

```java
response.setContentType("text/html");
```



那么，设置了响应头中Content-Type属性后，service()方法为：

```java
public class UserServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1、从request对象中获取请求中的任何信息
        String username = req.getParameter("username");//根据

        //2、处理业务的代码，根据username，去查询是否有对应的数据
        SqlSession sqlSession = SqlSessionUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        //创建Example条件类
        UserExample example = new UserExample();
        //添加username的等于条件
        example.createCriteria().andUserNameEqualTo(username);
        List<User> users = userMapper.selectByExample(example);


        //3、将要响应的数据放入response中
        //设置response中的content-type
        resp.setContentType("text/html");
        if (users != null && !users.isEmpty()){
            resp.getWriter().write("NO");
        }else {
            resp.getWriter().write("YES");
        }
    }
}
```

此时，我们再去发送表单数据给服务端，请求中的响应头信息为：

![image-20240601174933427](.\images\image-20240601174933427.png) 

此时，在响应头中就有了Content-Type属性，该属性告诉客户端使用html文件的方式解析响应体内容。









---

## url-pattern写法说明

url-partten的写法分为两种：

1. **精确匹配**

例如：/servlet1，表示匹配/项目名/servlet1路径。

2. **模糊匹配**

模糊匹配是以`*`作为通配符，`*`在哪里，哪里就是模糊的。

* **`/`**           表示通配所有资源，不包括jsp文件
* **`/*`**          表示通配所有资源，包括jsp文件
* **`/a/*`**        表示匹配所有以/a/为前缀的映射路径
* **`*.action`**    匹配后缀，匹配所有以action为后缀的映射路径



**注意**：假如url-partten使用的写法是`/xx*`的方式，例如`/aaa*`，则表示的精确匹配，路径必须是/aaa*才可以匹配。



大多数情况都是使用精确写法。

**url-pattern是用来匹配路径的，一定是以/开头，并不是绝对路径，和相对路径、绝对路径无关**。





---

# 三、Servlet注解配置（:star:）

使用**`@WebServlet`**注解替换Servlet的配置文件配置

例如，创建一个Serlvet1类，给该类配置的路径为s1：

```java
@WebServlet("/s1")
public class Servlet1 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }
}
```

这样我们就可以通过localhost:8080/项目名/s1的方式访问该Servlet了。

学习了SpringMVC后，我们会使用`@RequestMapping`注解来设置请求的映射路径。



**对于该注解的属性说明**

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebServlet {
    String name() default "";

    String[] value() default {};

    String[] urlPatterns() default {};

    int loadOnStartup() default -1;

    WebInitParam[] initParams() default {};

    boolean asyncSupported() default false;

    String smallIcon() default "";

    String largeIcon() default "";

    String description() default "";

    String displayName() default "";
}
```

* `name`属性：用于给Servlet起别名。相当于xml配置中的<servlet-name>标签。

* `value`属性与`urlPatterns`属性：这两个属性表示的含义是一样的，都是设置映射路径的含义。在@WebService注解中，可以任意使用其中的一个属性去表示映射路径。（一般都是使用value）

  因为当注解中只使用其中的value属性，就可以使用字符串的方式设置映射路径，不需要显式地使用value属性。@WebService注解最重要的就是设置映射路径，所以设置value属性。同时，urlPatterns属性也可以设置映射路径。

  并且value属性与urlPatterns属性是String[]数组类型，所以可以给Servlet设置多个映射路径，通过多个路径均可以访问到该Servlet。

* `loadOnStartUp`属性：与Servlet生命周期有关，在下面讲解。

* `initParams`属性：与ServletConfig对象有关，在下面讲解。









---

# 四、Servlet生命周期

## 1、生命周期简介

**什么是Servlet的生命周期？**

* 应用程序中的对象不仅在空间上有层次结构的关系，在时间上也会因为处于程序运行过程中的不同阶段而表现出不同状态和不同行为——这就是对象的生命周期。
* 所谓的生命周期，就是对象在容器中从开始创建到销毁的过程。



**Servlet主要的生命周期以及执行特点**

Servlet对象是Servlet容器创建的，生命周期方法都是由容器（目前我们使用的是Tomcat）调用的。



> **Servlet生命周期**：**`实例化 -> 初始化 -> 处理服务 -> 销毁`**

* 实例化所对应的方法就是Servlet的构造器

* 初始化对应的方法是Servlet接口中的`init()`方法

* 处理服务，对应的方法是Servlet接口中的`service()`方法

* 销毁对应的方法是Servlet接口中的`destory()`方法

| 生命周期     | 对应方法    | 执行时机                 | 执行次数 |
| ------------ | ----------- | ------------------------ | -------- |
| **构造对象** | `构造器`    | 第一次请求时或容器启动时 | 1        |
| **初始化**   | `init()`    | 构造完毕后               | 1        |
| **处理服务** | `service()` | 每次请求时               | 多次     |
| **销毁**     | `destory()` | 容器关闭                 | 1        |



**执行流程：**

1. 第一次发送请求，构造Servlet对象
2. 构造完毕，调用init()方法初始化Servlet对象
3. 初始化完毕，调用service()方法，处理服务
4. 每次访问，都调用service()处理服务
5. 关闭Tomcat服务，调用destory()方法销毁Servlet对象

大致的Servlet生命周期的流程如上所示，对于Servlet的实例化时机，可以修改成在容器启动时进行。具体的方式请查看Servlet实例化时机的修改。



**注意**：

* 这里的service()方法，是参数为HttpServletRequest和HttpServletResponse的方法。

* init()和destory()方法都是指**空参**的方法（注意其他重载的方法）。





当我们使用继承HttpServlet的方式实现Servlet接口，在HttpServlet中，已经对init()方法进行了重写，并且在HttpServlet的父类GenericServlet中，对destory()方法进行了重写：

HttpServlet中的init()方法：

![image-20240601205728911](.\images\image-20240601205728911.png)

GenericServlet中的destory()方法：

<img src=".\images\image-20240601205810737.png" alt="image-20240601205810737" style="zoom:67%;" /> 







## 2、测试

#### 数据准备

创建一个Servlet的实现类ServletLifeCycle，在该Servlet类中，创建一个空参的构造器，并且重写init()、service()以及destory()方法：

```java
@WebServlet("/servletLifeCycle")
public class ServletLifeCycle extends HttpServlet {
    public ServletLifeCycle(){
        System.out.println("ServletLifeCycle类中的构造器");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("ServletLifeCycle类中的init()方法");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ServletLifeCycle类中的service()方法");
    }

    @Override
    public void destroy() {
        System.out.println("ServletLifeCycle类中的destroy()方法");
    }
}
```

#### 测试过程

给其中的每一个方法都使用一个断点：

<img src="C:\Users\14036\AppData\Roaming\Typora\typora-user-images\image-20240601214221621.png" alt="image-20240601214221621" style="zoom:50%;" /> 

然后，我们使用DEBUG方式启动Tomcat服务器，浏览器自动去访问部署Tomcat的项目路径：

<img src=".\images\image-20240601214044896.png" alt="image-20240601214044896" style="zoom:67%;" /> 

此时，在Tomcat服务日志中，并没有信息打印。

然后，我们去访问/servletLifeCycle路径：

![image-20240601214254156](.\images\image-20240601214254156.png) 

此时，程序会先去运行到空参构造器的断点中，执行构造方法，创建Servlet的对象：

<img src=".\images\image-20240601214356242.png" alt="image-20240601214356242" style="zoom:67%;" /> 

debug继续运行，则此时控制台中会打印出空参构造器中的数据：

<img src=".\images\image-20240601214505526.png" alt="image-20240601214505526" style="zoom:50%;" /> 

并且程序跳转到init()方法中：

<img src=".\images\image-20240601214801216.png" alt="image-20240601214801216" style="zoom:67%;" /> 

继续执行，打印init()方法到控制台，并且程序停止在server()方法的断点中：

![image-20240601214821743](.\images\image-20240601214821743.png) 

<img src=".\images\image-20240601214841611.png" alt="image-20240601214841611" style="zoom:50%;" /> 

继续执行，打印server()方法中的sout数据到控制台：

![image-20240601214907486](.\images\image-20240601214907486.png) 

此时，程序就不在ServletLifeCycle中了。

多次访问该Servlet的路径地址，程序都只执行service()方法，Servlet的构造器与init()不再执行：

<img src=".\images\image-20240601215024350.png" alt="image-20240601215024350" style="zoom:67%;" /> 

关闭Tomcat服务器，此时会去执行Servlet中的destory()方法，销毁Servlet对象，打印断点中的sout信息：

<img src=".\images\image-20240601215249435.png" alt="image-20240601215249435" style="zoom:67%;" /> 





## 3、Servlet可能存在的问题

我们由上述的知识可以知道：

Servlet对象是在第一次发送请求时去创建的，初始化操作则是在创建完毕之后调用init()方法进行的初始化。那么，也就是说，Servlet对象在第一次发送请求之后，就已经会建出来，后面无论访问多少次相同的页面，使用的都是同一个Servlet对象。

> 即：**Servlet对象是`单例对象`，只在第一次发送请求时创建。**

但是，Servlet单例对象，就可能存在着一个问题：

假如有多个用户去访问Tomcat服务器，就会在Tomcat服务器中创建多个栈空间。

service()方法是在各个栈中单独执行的，Servlet对象则是存放在堆空间中。

因为Servlet对象是单例对象，所以多个用户访问的实际上是同一个Servlet对象，即该对象是共享的。

假设此时在Servlet对象中有一个成员变量i，用户A去操作了成员变量i，用户B也去操作了成员变量i，假设都是i++。此时，就可能出现线程安全问题，得到的成员变量i不是用户想要的结果。

![image-20240601220724170](.\images\image-20240601220724170.png) 

那么也就是说，Servlet的成员变量在多个线程栈中是共享的，所以，**不建议在service方法中修改Servlet的成员变量**，在并发请求时，会引发线程安全问题，或者可以将Servlet中定义的成员变量设置成`final`类型，不可更改。









---

## 4、Servlet实例化的时机修改

Servlet实例化的时机可以修改成在容器启动时进行，在默认情况下，Servlet是在第一次请求时，即只有在客户端发送了Servlet对象所设置的映射路径时，Tomcat才去创建对应的Servlet对象。

**修改方式（注解方式）：**

在`@WebServlet`注解中，将**`loadOnStartup`**属性修改为正整数。

在默认情况下，@WebServlet的loadOnStartup属性是-1，也就是不在容器启动时创建：

<img src=".\images\image-20240601222503115.png" alt="image-20240601222503115" style="zoom:67%;" /> 

loadOnStartup正整数的值，代表了实例化Servlet对象的顺序。比如loadOnStartup为3，就表示该Servlet对象是第三个进行实例化的；先要去实例化loadOnStartup为1和2的Servlet对象。数字越小就越先实例化，数字越大就越后实例化。当然，如果你设置了Servlet的loadOnStartup为3，但是前面没有其他的Servlet对象，也是可以的。

即，只要将loadOnStartup设置成了正整数，就能够在Tomcat容器启动时就实例化Servlet对象。

案例：

```java
@WebServlet(value = "/servletLifeCycle", loadOnStartup = 2)
public class ServletLifeCycle extends HttpServlet {
    public ServletLifeCycle(){
        System.out.println("ServletLifeCycle类中的构造器");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("ServletLifeCycle类中的init()方法");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ServletLifeCycle类中的service()方法");
    }

    @Override
    public void destroy() {
        System.out.println("ServletLifeCycle类中的destroy()方法");
    }
}
```

当我们启动Tomcat服务器时：

![image-20240601223457769](.\images\image-20240601223457769.png) 

此时就已经去执行了Servlet对象的构造器与init()方法。





**修改方式（xml配置方式）：**

如果使用的是xml配置方式配置的Servlet对象，则在web.xml的servlet标签中，添加一个load-on-startup标签，在该标签中填写正整数，效果和注解中一样。

案例：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
         
    <servlet>
        <servlet-name>userServlet</servlet-name>
        <servlet-class>com.atguigu.servlet.UserServlet</servlet-class>
        <load-on-startup>2</load-on-startup>
    </servlet>
    
    <servlet-mapping>
        <servlet-name>userServlet</servlet-name>
        <url-pattern>/userServlet</url-pattern>
    </servlet-mapping>

</web-app>
```





**loadOnStartup推荐值**

推荐loadOnStartup的值尽量不要占用1-5，最好从6开始。

为什么？

我们打开Tomcat的配置文件目录conf，在该目录下有一个web.xml文件，该文件是所有部署的项目通用的web.xml配置，该配置会应用于所有的web应用程序。

在该文件中，我们可以发现，其中loadOnStartup值为1、3、4、5已经存在了Servlet对象所配置，所以，为了避免发生冲突，建议loadOnStartup值从6开始。

<img src=".\images\image-20240601224248888.png" alt="image-20240601224248888" style="zoom: 50%;" /> 

<img src=".\images\image-20240601224355839.png" alt="image-20240601224355839" style="zoom:50%;" /> 

<img src=".\images\image-20240601224411579.png" alt="image-20240601224411579" style="zoom:50%;" /> 

<img src=".\images\image-20240601224456653.png" alt="image-20240601224456653" style="zoom:50%;" /> 





## 5、总结

1. Servlet对象在容器中是单例的
2. 容器可以处理并发的用户请求，每个请求在容器中都会开启一个线程
3. **多个线程可能会使用相同的Servlet对象，所以在Servlet中，不要去修改成员变量，否则可能造成线程安全问题**
4. load-on-startup值如果是正整数则表示当前的Servlet对象在容器启动时进行初始化，正整数的大小表示实例化顺序，如果数字重复了，容器会自动解决顺序问题，但是应当避免重复
5. Tomcat容器中，已经定义了一些随系统启动实例化的servelt，我们自定义的servlet对象的load-on-startup避免使用数字1-5，若使用正整数，建议从6开始。











---

# default-servlet的说明

在Tomcat自身的配置文件web.xml中，有一个default-servlet的配置信息：

```xml
<servlet>
    <servlet-name>default</servlet-name>
    <servlet-class>org.apache.catalina.servlets.DefaultServlet</servlet-class>
    <init-param>
        <param-name>debug</param-name>
        <param-value>0</param-value>
    </init-param>
    <init-param>
        <param-name>listings</param-name>
        <param-value>false</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>


<servlet-mapping>
    <servlet-name>default</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

我们可以看到，该Servlet的load-on-startup设置为了1，也就是要求该Servlet在容器启动时第一个进行初始化。

并且该Servlet的url-pattern的路径设置成了/，也就是匹配除了jsp外所有的访问路径。

那这个DefaultServlet到底是干啥的呢？

当客户端请求的是静态资源时，比如请求的是aaa.html、a.png或者a.css这些静态资源时，Tomcat会拿路径和定义的所有Servlet路径进行对比，如果发现任何定义的Servlet路径都没有和请求的路径匹配时，此时Tomcat就会将请求交给DefaultServlet来处理。

DefaultServlet会根据请求的路径，去查看对应的资源，比如请求的是aaa.html，那么DefaultServlet就会去寻找aaa.html资源。当找到了对应的资源时，DefaultServlet就会使用io流的方式，读取文件中的数据并且将读取到的数据放入到response的响应体中；并获取文件的大小，设置response中的响应头的Content-Length大小；判断文件的类型，设置response的响应头中的Content-Type参数。最后，将reponse转换成响应的报文返回给客户端。

也就是说，**所有的静态资源都是由`DefaultServlet`对象来加载的**。

![image-20240601232128298](.\images\image-20240601232128298.png) 

> **总结：**
>
> * DefaultServlet为默认的Servlet，当客户端请求不能匹配其他所有Servlet时，将由该Servlet来处理。
>
> * 所有的静态资源都由DefaultServlet来处理，如HTML、图片、CSS、JS文件等。



在SpringMVC中，会提供一个Servlet，该Servlet会造成DefaultServlet对象的丢失，这样一来，当我们去访问静态资源时就会出现404的错误。所以，在后面学习SpringMVC时，如果项目不是前后端分离，在项目中有一些静态资源需要进行访问时，需要重新配置DefaultServlet对象，使得我们能够访问静态资源。











---

# 五、与Servlet有关的继承结构中各种API说明

我们使用继承HttpServlet类的方式来实现Servlet接口，HttpServlet类的继承结构如下所示：

<img src=".\images\image-20240601232927424.png" alt="image-20240601232927424" style="zoom:80%;" />

抽象类HttpServlet继承于抽象类GenericServlet，GenericServlet类实现了Servlet接口。我们来分别讲解一下它的父类、父接口的结构。

**继承关系图解：**

![1682299663047](.\images\1682299663047.png)



## Servlet接口

<img src=".\images\image-20240601234126138.png" alt="image-20240601234126138" style="zoom:80%;" /> 

Servlet是规范接口，所有的Servlet对象都必须实现。

* public void init(ServletConfig config) throws ServletException;   
  + 初始化方法,容器在构造servlet对象后,自动调用的方法,容器负责实例化一个ServletConfig对象,并在调用该方法时传入
  + ServletConfig对象可以为Servlet 提供初始化参数
* public ServletConfig getServletConfig();
  + 获取ServletConfig对象的方法,后续可以通过该对象获取Servlet初始化参数
* public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException;
  + 处理请求并做出响应的服务方法,每次请求产生时由容器调用
  + 容器创建一个ServletRequest对象和ServletResponse对象,容器在调用service方法时,传入这两个对象
* public String getServletInfo();
  + 获取ServletInfo信息的方法
* public void destroy();
  + Servlet实例在销毁之前调用的方法





## GenericServlet抽象类

<img src=".\images\image-20240602000511455.png" alt="image-20240602000511455" style="zoom:67%;" />  

GenericServlet抽象类是对Servlet接口的一些固定功能的粗糙实现，以及对service方法的再次抽象声明，并定义了一些其他相关功能方法。

* private transient ServletConfig config; 
  + 初始化配置对象作为属性
* public GenericServlet() { } 
  + 构造器,为了满足继承而准备
* public void destroy() { } 
  + 销毁方法的平庸实现
* public String getInitParameter(String name) 
  + 获取初始参数的快捷方法
* public Enumeration<String> getInitParameterNames() 
  + 返回所有初始化参数名的方法
* public ServletConfig getServletConfig()
  +  获取初始Servlet初始配置对象ServletConfig的方法
* public ServletContext getServletContext()
  +  获取上下文对象ServletContext的方法
* public String getServletInfo() 
  + 获取Servlet信息的平庸实现
* public void init(ServletConfig config) throws ServletException() 
  + 初始化方法的实现,在调用该方法时，会读取配置信息进入一个ServletConfig对象并将该对象传入到init()方法中，给当前类中的ServletConfig对象属性赋值，ServletConfig是初始的配置信息。而后在此方法中调用了init()的重载方法
* public void init() throws ServletException 
  + 重载init方法,为了让我们自己定义初始化功能的方法
* public void log(String msg) 
* public void log(String message, Throwable t)
  +  打印日志的方法及重载
* public abstract void service(ServletRequest req, ServletResponse res) throws ServletException, IOException; 
  + 服务方法再次声明
* public String getServletName() 
  + 获取ServletName的方法



## HttpServlet抽象类

abstract class HttpServlet extends GenericServlet  HttpServlet抽象类,除了基本的实现以外,增加了更多的基础功能

+ private static final String METHOD_DELETE = "DELETE";
+ private static final String METHOD_HEAD = "HEAD";
+ private static final String METHOD_GET = "GET";
+ private static final String METHOD_OPTIONS = "OPTIONS";
+ private static final String METHOD_POST = "POST";
+ private static final String METHOD_PUT = "PUT";
+ private static final String METHOD_TRACE = "TRACE";
  + 上述属性用于定义常见请求方式名常量值
+ public HttpServlet() {}
  + 构造器,用于处理继承
+ public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
  + 对服务方法的实现
  + 在该方法中,将请求和响应对象转换成对应HTTP协议的HttpServletRequest HttpServletResponse对象，将父类型的参数转换成子类型的参数，因为子类型的参数能够调用的api更加丰富
  + 调用重载的service方法
+ public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
  + 重载的service方法，被重写的service方法所调用
  + 在该方法中,通过请求方式判断,调用具体的doXxx()方法（如doGet()）完成请求的处理
+ protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
+ protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
+ protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
+ protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
+ protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
+ protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
+ protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  + 对应不同请求方式的处理方法
  + 除了doOptions和doTrace方法,其他的doXxx() 方法都在故意响应错误信息



那么，也就是说实际上的init()实例化方法，是GenericServlet类中的init(ServletConfig)方法，我们所重写的init()方法，会被该方法调用，从而实现自身想要的实例化操作；

Tomcat所执行的service()方法，实际上是HttpServlet中的service(ServletRequest, ServletResponse)方法，在该方法中，调用了我们所重写的service(HttpServletRequest, HttpServletResponse)方法，我们进行了重写，所以能够自己所想要的业务需求；

如果我们不对service()方法进行重写，则Tomcat调用的service()方法是HttpServlet类中的service()方法，在该方法中根据method类型调用了doGet()或者doPost()方法，这些方法故意响应了错误的信息，所以不重写就会报错。

我们除了可以通过重写service()方法来实现自定义Servlet外，还可以通过重写doGet()或doPost()方法来实现自定义Servlet。这样一来，也就不会因为在调用service()时，去调用HttpServlet类中自带的doGet或doPost而响应错误了。

即，这是故意通过响应错误的方式，提醒我们要对方法进行重写。







---

# 自定义Servlet的两种方式（:star:）

我们知道，实际上Servlet所运行的service()方法，是HttpServlet中的service(ServletRequest, ServletResponse)方法：

<img src=".\images\image-20240602092314372.png" alt="image-20240602092314372" style="zoom: 50%;" /> 

而在该方法中，调用的是我们所重写的service(HttpServletRequest, HttpServletResponse)方法，在HttpServlet中该方法如下所示：

```java
protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String method = req.getMethod();

    if (method.equals(METHOD_GET)) {
        long lastModified = getLastModified(req);
        if (lastModified == -1) {
            doGet(req, resp);
        } else {
            long ifModifiedSince = req.getDateHeader(HEADER_IFMODSINCE);
            if (ifModifiedSince < lastModified) {
                maybeSetLastModified(resp, lastModified);
                doGet(req, resp);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            }
        }

    } else if (method.equals(METHOD_HEAD)) {
        long lastModified = getLastModified(req);
        maybeSetLastModified(resp, lastModified);
        doHead(req, resp);

    } else if (method.equals(METHOD_POST)) {
        doPost(req, resp);

    } else if (method.equals(METHOD_PUT)) {
        doPut(req, resp);

    } else if (method.equals(METHOD_DELETE)) {
        doDelete(req, resp);

    } else if (method.equals(METHOD_OPTIONS)) {
        doOptions(req, resp);

    } else if (method.equals(METHOD_TRACE)) {
        doTrace(req, resp);

    } else {
        String errMsg = lStrings.getString("http.method_not_implemented");
        Object[] errArgs = new Object[1];
        errArgs[0] = method;
        errMsg = MessageFormat.format(errMsg, errArgs);

        resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, errMsg);
    }
}
```

在未被重写的service()方法中，实际上就是根据当前请求的method类型去调用各种doXxx()方法，这些doXxx()方法案例如下所示：

doGet()：

![image-20240602092608858](.\images\image-20240602092608858.png)

doPost()：

![image-20240602092623601](.\images\image-20240602092623601.png)

可以发现，这些方法实际上就是响应错误的信息给客户端。即，在默认的情况下，service()方法中调用的doXxx()方法是响应错误给客户端，所以我们必须要去重写service()，防止在客户端出现错误，同时也能够放入自己想要的业务操作。

那，假如我们去根据请求的method类型，直接去重写的是doXxx()方法可以吗？

答案是可以的。

由于响应错误信息是在doXxx()方法中进行的，直接重写doXxx()方法也能够停止响应错误，同时将业务的信息放在这些方法中。



## 方式一：重写service()方法

这里就不用再说了，前面都已经讲过。还是需要注意的是，这里重写的service()方法，是父类HttpServlet中的service(HttpServletRequest, HttpServletResponse)方法。





## 方式二：重写doGet()或doPost()方法

在父类HttpServlet的service()方法中，调用了doXxx()方法，这些方法响应错误信息给客户端，所以我们可以直接重写这些doGet()或doPost()方法，根据不同的请求类型调用不同的doXxx()方法。

案例：

```java
@WebServlet("/s2")
public class Servlet2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.getWriter().write("get请求已接收");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.getWriter().write("post请求已接收");
    }
}
```

表单信息：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<form action="./s2" method="get">
    用户名：<input type="text" name="username"><br/>
    <input type="submit" value="校验">
</form>
<hr>
    
<!--./表示当前路径下，/表示当前项目下，../表示上一层目录下，其中./可以省略-->
<form action="./s2" method="post">
    用户名：<input type="text" name="username"><br/>
    <input type="submit" value="校验">
</form>
</body>
</html>
```

显示：

<img src=".\images\13131312312" alt="动画" style="zoom:67%;" /> 

如果我们要使用doGet()与doPost()的方式处理请求，并且这两个方法中要写的代码是一样的，那么我们可以在一个方法内部调用另一个方法即可。

如：

```java
@WebServlet("/s2")
public class Servlet2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //业务处理
    }
}
```

这样就不会造成代码的冗余了。



需要注意的是：

在前端中，这里的action，包括href这些要输入路径的，需要注意和Servlet中的url-pattern有区别，url-pattern和相对路径、绝对路径无关，都是使用/开头，去匹配项目下的路径。

但是action、href是使用相对路径或绝对路径表示的。

**相对路径**：以./或../开头

* ./    表示当前目录下的资源，./可以省略，可直接使用资源名表示
* ../   表示上一层目录下的资源

例如./s1，使用路径表示就是http://localhost:8080/当前项目/s1



**绝对路径**：以/开头

* /     表示当前最外层project下的资源（或者当前工作空间下的资源）

例如/s1，使用路径表示就是http://localhost:8080/s1



在后面我们学习了SpringMVC后，就不需要继承HttpServlet了，也不需要重写service()还是doGet()与doPost()方法了。









---

# 六、获取初始化参数的两个对象

## 1、ServletConfig

### 是什么？

* ServletConfig是**`为Servlet提供初始配置参数的对象`**，**每个Servlet都有自己独立唯一的ServletConfig对象**。





**ServletConfig的创建过程**

Tomcat在创建Servlet对象之前，会先去读取Servlet的初始参数，也就是在web.xml配置文件中，我们在`<servlet>`标签中所配置的**`<init-param>`**中的信息，如：

<img src=".\images\image-20240602105706510.png" alt="image-20240602105706510" style="zoom:50%;" /> 

读取完Tomcat会去创建ServletConfig对象，然后将这些`<inin-param>`的信息存放入ServletConfig对象中。

在实例化Servlet对象时，会去调用实例化方法init(ServletConfig)，该方法将ServletConfig对象作为参数传入，并将该对象作为Servlet中的一个属性。

之后，我们通过自定义类继承HttpServlet类的方式实现Servlet，该自定义类中就会存在ServletConfig属性，通过该属性就可以获得Servlet的初始参数。





**GenericServlet中的init(ServletConfig)方法**

![image-20240602105113036](.\images\image-20240602105113036.png)

该init(ServletConfig)方法中会去调用重载的init()方法，该空参init()方法我们可以进行重写





**大致流程**

<img src=".\images\image-20240602105215422.png" alt="image-20240602105215422" style="zoom:67%;" />



**注意**：

每个Servlet都有自己的ServletConfig，每个Servlet的ServletConfig不是共用的。

<img src=".\images\1682302307081.png" alt="1682302307081" style="zoom:50%;" /> 





---

### 怎么用？

首先，我们需要知道，ServletConfig是一个**接口**，它是HttpServlet父类GenericServlet的父接口：

<img src=".\images\image-20240602102907048.png" alt="image-20240602102907048" style="zoom:50%;" /> 

在该接口中定义了四个方法：

```java
public interface ServletConfig {

    public String getServletName();
    public ServletContext getServletContext();
    public String getInitParameter(String name);
    public Enumeration<String> getInitParameterNames();

}
```

那么，凡是通过继承于HttpServlet类的方式创建Servlet对象，我们都可以调用其中的ServletConfigh接口中的方法。

**方法说明**

| 方法名                    | 作用                                                  |
| ------------------------- | ----------------------------------------------------- |
| `getServletName()`        | 获取Servlet的别名，也就是servlet-name标签中定义的名称 |
| `getServletContext()`     | 获取ServletContext对象                                |
| `getInitParameter()`      | 根据名字获取初始化参数的值                            |
| `getInitParameterNames()` | 获取所有初始化参数名组成的`Enumeration`对象           |

`Enumeration`对象是一个迭代器对象，类似于`Iterator`。在该对象中，有两个方法，分别是`hasMoreElements()`以及`nextElement()`。

* hasMoreElements()方法类似于Iterator中的hasNext()方法，表示如果有下一个元素，则返回true；

* nextElement()方法类似于Iterator中的next()方法，表示将游标下移一位，并返回游标所执行的元素。

一般使用while循环的方式，迭代游标，循环地返回迭代器中的元素。



**使用案例1：使用XML配置方式**

Servlet的配置信息

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    
    <servlet>
        <servlet-name>servlet1</servlet-name>
        <servlet-class>com.atguigu.servlet.servletconfig.Servlet1</servlet-class>
        <!--配置servlet的初始参数-->
        <init-param>
            <param-name>keya</param-name>
            <param-value>valuea</param-value>
        </init-param>
        
        <init-param>
            <param-name>keyb</param-name>
            <param-value>valueb</param-value>
        </init-param>
    </servlet>

    <servlet-mapping>
        <servlet-name>servlet1</servlet-name>
        <url-pattern>/s1</url-pattern>
    </servlet-mapping>
</web-app>
```



定义Servlet，获取初始化参数

```java
public class Servlet1 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取ServletConfig对象
        ServletConfig servletConfig = getServletConfig();

        //根据参数名获取单个参数
        String value = servletConfig.getInitParameter("keya");
        System.out.println("keya:" + value);

        //获取所有参数名
        Enumeration<String> parameterNames = servletConfig.getInitParameterNames();
        //迭代获取参数名
        while(parameterNames.hasMoreElements()){
            String parameterName = parameterNames.nextElement();

            System.out.println(parameterName + ":" + servletConfig.getInitParameter(parameterName));
        }
    }
}
```

当去访问s1路径时，控制台打印：

<img src=".\images\image-20240602111930627.png" alt="image-20240602111930627" style="zoom:80%;" /> 







**使用案例2：通过注解方式配置**

在@WebServlet注解中，有一个initParams()属性，它是一个WebInitParam[]数组类型。

<img src=".\images\image-20240602112220269.png" alt="image-20240602112220269" style="zoom:67%;" /> 

该WebInitParam又是一个注解类型：

<img src=".\images\image-20240602112408837.png" alt="image-20240602112408837" style="zoom:50%;" /> 

所以，如果要使用注解的方式配置Servlet的初始化参数，格式应该为：

```java
@WebServlet(
		value = "/s2",
        initParams = {@WebInitParam(name = "keya",value = "valuea"), @WebInitParam(name = "keyb", value = "valueb")}
)
```

案例：

```java
@WebServlet(
        value = "/s2",
        initParams = {@WebInitParam(name = "encoding", value = "UTF-8"), @WebInitParam(name = "keyb", value = "valueb")}
)
public class Servlet2 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取ServletConfig对象
        ServletConfig servletConfig = getServletConfig();

        //根据参数名获取单个参数
        String value = servletConfig.getInitParameter("encoding");
        System.out.println("encoding:" + value);

        //获取所有参数名
        Enumeration<String> parameterNames = servletConfig.getInitParameterNames();
        //迭代获取参数名
        while(parameterNames.hasMoreElements()){
            String parameterName = parameterNames.nextElement();

            System.out.println(parameterName + ":" + servletConfig.getInitParameter(parameterName));
        }
    }
}
```

当我们访问s2路径时，控制台打印结果：

<img src=".\images\image-20240602113236088.png" alt="image-20240602113236088" style="zoom:80%;" /> 







---

## 2、ServletContext

### 是什么？

* ServletContext对象有称呼为上下文对象，或者叫做应用域对象。
* 容器会为每个项目都创建一个独立的唯一的ServletContext对象。也就是说，**ServletContext是单例的**。
* ServletContext对象为所有的Servlet所共享。
* ServletContext可以为所有的Servlet提供初始配置参数。

> **与ServletConfig最大的区别在于**：**`ServletContext是所有Servlet所共享的，而ServletConfig则是每个Servlet所独有的。`**

![1682303205351](.\images\1682303205351.png)





### 怎么用？

在web.xml中，配置context-param标签，在容器启动时，会去创建一个ServletContext对象，并将context-param标签中所设置的初始化参数放在该对象中。

之后，我们就可以去获取ServletContext对象，从而获取到初始化参数值。

**配置ServletContext参数**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <context-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </context-param>

    <context-param>
        <param-name>key1</param-name>
        <param-value>value1</param-value>
    </context-param>
    
</web-app>
```



**在Servlet中获取ServletContext对象并获取参数**

```java
@WebServlet("/s3")
public class Servlet3 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取ServletContext对象
        ServletContext servletContext = getServletContext();

        //获取单个参数
        String value1 = servletContext.getInitParameter("encoding");
        System.out.println("encoding:" + value1);

        String value2 = servletContext.getInitParameter("key2");
        System.out.println("key2:" + value2);

  
        //获取所有参数
        Enumeration<String> parameterNames = servletContext.getInitParameterNames();
        while (parameterNames.hasMoreElements()){
            String key = parameterNames.nextElement();
            System.out.println(key + ":" + servletContext.getInitParameter(key));
        }
    }
}
```

执行路径/s3，控制台打印结果：

<img src=".\images\image-20240602125244058.png" alt="image-20240602125244058" style="zoom:80%;" /> 





---

### ServletContext的其他API

#### 获取绝对路径和上下文路径（:star2:）

> **获取资源部署后的绝对路径：`getRealPath()`**

假如，我们要去获取Web项目中某个静态资源的路径，如果我们直接拷贝我们电脑中的完整路径的话，是有问题的。因为如果将项目以后部署到其他的服务器中的话，路径肯定是会发生改变的，所以，我们需要使**用代码动态地获取资源的路径，这样无论项目部署在哪里，都是动态进行获取实际路径**，那么就不会由于写死了路径而导致项目部署位置概念而引发路径错误的问题。

**`getRealPath("/")`得到的结果是项目部署后的绝对路径。**



**案例：**

假如我们要去webapp下的html目录中的hello.html：

<img src=".\images\image-20240602132756569.png" alt="image-20240602132756569" style="zoom: 80%;" /> 

项目部署后，hello.html存放的位置在项目的html目录下：

<img src=".\images\image-20240602132957695.png" alt="image-20240602132957695" style="zoom:80%;" /> 

我们使用getRealPath("/")是去获取工程部署后的绝对路径，工程下的html目录中的hello.html，则是getRealPath("/html/hello.html")：

```java
@WebServlet("/s4")
public class Servlet4 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        System.out.println(servletContext.getRealPath("/html/hello.html"));
    }
}
```

此时就能够获取到该文件部署后真实的文件路径。

![image-20240602133137768](.\images\image-20240602133137768.png)

* getRealPath("/")：获取工程部署后的绝对路径。

* getRealPath("/css")：获取工程部署后，工程下css目录的绝对路径。

* getRealPath("/imgs/1.jpg")：获取工程部署后，工程下imgs目录中1.jpg的绝对路径。







**不同部署模式，使用getRealPath()获得的路径不同**

当Tomcat的部署模式使用的是**war exploded**时，部署后的绝对位置，也就是在当前项目的target目录中的Web项目名称下，比如：

<img src=".\images\image-20240602132112475.png" alt="image-20240602132112475" style="zoom: 67%;" /> 

在demo03中调用getRealPath("/")，此时获取到的结果就是demo03项目部署后的绝对路径，如：
`C:\Users\14036\Desktop\代码\JavaWeb\demo03-servletConfig-servletContext\target\demo03\`

如果使用的是**war**的部署模式，使用getRealPath("/")去获取项目部署后的绝对位置，也就是将当前项目部署到Tomcat服务器中的webapp目录下的绝对位置，如：

`D:\apache-tomcat-10.1.24\webapps\demo03\`

我们可以通过getRealPath()方法，来获取各种资源所在的路径位置，并且不会因为主机的不同，路径的不同而造成路径错误问题。







> **获取项目的上下文路径：`getContextPath()`**

项目的上下文路径，也就是当前项目所使用的访问路径，使用getContextPath()获取到的结果一般为：`/工程名`

可以在IDEA关联的Tomcat中修改当前项目的上下文路径。

<img src=".\images\image-20240602134152358.png" alt="image-20240602134152358" style="zoom: 33%;" /> 

获取项目的上下文路径，可以**帮助我们解决一些后端页面渲染技术或者请求转发和响应重定向中的路径问题。**

案例：

```java
@WebServlet("/s4")
public class Servlet4 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        System.out.println(servletContext.getContextPath());
    }
}
```

访问后的打印结果：

![image-20240602134443766](.\images\image-20240602134443766.png) 



---

#### 域对象相关API

* **域对象**：一些用于存储数据和传递数据的对象，传递数据不同的范围，我们称之为不同的域，不同的域对象代表不同的域，共享数据的范围也不同。

* ServletContext代表应用，所以ServletContext域也叫作应用域，是webapp中最大的域，可以在本应用内实现数据的共享和传递。

* webapp中的三大域对象，分别是应用域、会话域、请求域。

* **域对象会像`Map`集合一样存取数据，即存取数据的方式是key=value。**

* 后续我们将三大域对象统一进行讲解和演示，三大域对象都具有的API如下：

  > * `setAttribute(String key, Object value)`：根据指定的key，存入value值。
  > * `getAttribute(String key)`：根据指定的key，获取其对应的值。
  > * `removeAttribute(String key)`：根据指定的key，删除其存放在ServletContext中的值。

**使用案例：**

创建两个Servlet，一个使用/s4的路径，一个使用/s5的路径。

在第一个Servlet中，向应用域中存入key1:value1的数据，在第二个Servlet中，取出key1的数据：

```java
@WebServlet("/s4")
public class Servlet4 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取ServletContext对象
        ServletContext servletContext = getServletContext();

        //输出ServletContext的地址值
        System.out.println(servletContext);

        //往应用域中存入数据
        servletContext.setAttribute("key1", "value1");

        //获取应用域中存入的key1数据
        System.out.println(servletContext.getAttribute("key1"));
    }
}
```

```java
@WebServlet("/s5")
public class Servlet5 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取ServletContext对象
        ServletContext servletContext = getServletContext();
        //输出其地址值
        System.out.println(servletContext);

        //获取其他Servlet对象所存入应用域中的数据
        System.out.println(servletContext.getAttribute("key1"));
    }
}
```

分别去访问/s4与/s5，控制台打印的结果：

<img src=".\images\image-20240602141909319.png" alt="image-20240602141909319" style="zoom:67%;" /> 

由结果我们可以得知：

ServletContext对象是单例的，所有的Servlet共用同一个ServletContext对象；

ServletContext应用域的范围是整个项目，在该项目中，任意一个Servlet往其中存入的数据都可以在其他Servlet中获得。

由于ServletContext是应用域对象，所以在开发中，我们一般喜欢将该对象取名为application，意为应用。



---

# 七、HttpServletRequest与HttpServletResponse类（了解）

## HttpServletRequest

> **HttpServletRequest是什么**

+ HttpServletRequest是一个接口,其父接口是ServletRequest
+ HttpServletRequest是Tomcat将请求报文转换封装而来的对象,在Tomcat调用service方法时传入
+ HttpServletRequest代表客户端发来的请求,所有请求中的信息都可以通过该对象获得

![1681699577344](.\images\1681699577344.png)





> **HttpServletRequest怎么用**

+ **获取请求行信息相关(方式,请求的url,协议及版本)**

| API                           | 功能解释                       |
| ----------------------------- | ------------------------------ |
| StringBuffer getRequestURL(); | 获取客户端请求的url            |
| String getRequestURI();       | 获取客户端请求项目中的具体资源 |
| int getServerPort();          | 获取客户端发送请求时的端口     |
| int getLocalPort();           | 获取本应用在所在容器的端口     |
| int getRemotePort();          | 获取客户端程序的端口           |
| String getScheme();           | 获取请求协议                   |
| String getProtocol();         | 获取请求协议及版本号           |
| String getMethod();           | 获取请求方式                   |

**扩展：URI与URL的关系**

* URI：统一资源标识符。interface URI{}，它是资源定位的要求和规范。
* URL：统一资源定位符。class URL implements URI{}，它是网络中，一个具体资源路径

URI案例：/demo03/a.html，这是资源路径。

URL案例：http://ip:port/demo03/a.html，包含了资源路径以及前面的协议、端口号、ip地址。

由于在不同的服务器中，URL中的http://ip:port/是会变化的，所以我们就单独将后面的资源路径拿出来，作为URI，后面的资源路径是不会改变的。



+ **获得请求头信息相关**

| API                                   | 功能解释               |
| ------------------------------------- | ---------------------- |
| String getHeader(String headerName);  | 根据头名称获取请求头   |
| Enumeration<String> getHeaderNames(); | 获取所有的请求头名字   |
| String getContentType();              | 获取content-type请求头 |

+ **获得请求参数相关**

| API                                                     | 功能解释                             |
| ------------------------------------------------------- | ------------------------------------ |
| String getParameter(String parameterName);              | 根据请求参数名获取请求单个参数值     |
| String[] getParameterValues(String parameterName);      | 根据请求参数名获取请求多个参数值数组 |
| Enumeration<String> getParameterNames();                | 获取所有请求参数名                   |
| Map<String, String[]> getParameterMap();                | 获取所有请求参数的键值对集合         |
| BufferedReader getReader() throws IOException;          | 获取读取请求体的字符输入流           |
| ServletInputStream getInputStream() throws IOException; | 获取读取请求体的字节输入流           |
| int getContentLength();                                 | 获得请求体长度的字节数               |

+ **其他API**

| API                                          | 功能解释                    |
| -------------------------------------------- | --------------------------- |
| String getServletPath();                     | 获取请求的Servlet的映射路径 |
| ServletContext getServletContext();          | 获取ServletContext对象      |
| Cookie[] getCookies();                       | 获取请求中的所有cookie      |
| HttpSession getSession();                    | 获取Session对象             |
| void setCharacterEncoding(String encoding) ; | 设置请求体字符集            |





## HttpServletResponse

> **HttpServletResponse是什么**

+ HttpServletResponse是一个接口,其父接口是ServletResponse
+ HttpServletResponse是Tomcat预先创建的,在Tomcat调用service方法时传入
+ HttpServletResponse代表对客户端的响应,该对象会被转换成响应的报文发送给客户端,通过该对象我们可以设置响应信息

![1681699577344](.\images\16816995sss77344.png)



> **HttpServletRequest怎么用**

+ **设置响应行相关**

| API                        | 功能解释       |
| -------------------------- | -------------- |
| void setStatus(int  code); | 设置响应状态码 |


+ **设置响应头相关**

| API                                                    | 功能解释                                         |
| ------------------------------------------------------ | ------------------------------------------------ |
| void setHeader(String headerName, String headerValue); | 设置/修改响应头键值对                            |
| void setContentType(String contentType);               | 设置content-type响应头及响应字符集(设置MIME类型) |

+ **设置响应体相关**

| API                                                       | 功能解释                                                |
| --------------------------------------------------------- | ------------------------------------------------------- |
| PrintWriter getWriter() throws IOException;               | 获得向响应体放入信息的字符输出流                        |
| ServletOutputStream getOutputStream() throws IOException; | 获得向响应体放入信息的字节输出流                        |
| void setContentLength(int length);                        | 设置响应体的字节长度,其实就是在设置content-length响应头 |

+ **其他API**

| API                                                          | 功能解释                                            |
| ------------------------------------------------------------ | --------------------------------------------------- |
| void sendError(int code, String message) throws IOException; | 向客户端响应错误信息的方法,需要指定响应码和响应信息 |
| void addCookie(Cookie cookie);                               | 向响应体中增加cookie                                |
| void setCharacterEncoding(String encoding);                  | 设置响应体字符集                                    |









---

# 八、间接访问资源的两种方式（:star:）

## 1、概述

**什么是请求转发和响应重定向？**

请求转发和响应重定向是web应用中，间接访问项目资源的两种手段，也是Servlet控制页面跳转的两种手段。

请求转发通过`HttpServletRequest`实现，响应重定向通过`HttpServletResponse`实现。

生活中的案例：

请求转发：张三找李四借钱，李四没有，李四找王五，让王五借钱给张三。

响应重定向：张三找李四借钱，李四没有，李四让张三去找王五，张三自己去找王五借钱。





## 2、请求转发

**请求转发**：服务器收到请求后，从一个资源跳转到另一个资源的操作。

**图示：**

<img src=".\images\1682321228643.png" alt="1682321228643" style="zoom: 50%;" /> 

客户端向服务器发送请求，假如原本在服务端是使用ServletA来接收的，在ServletA中进行了请求转发，那么就将原本的请求发送给了另一个ServletB，让ServletB进行了处理，然后由SerlvetB将response再响应给客户端。这就是请求转发。

> **请求转发的特点：**
>
> 1. 请求转发时，请求和响应对象会继续传递给下一个资源。
> 2. 请求中的参数可以继续向下传递。
> 3. 请求转发是服务器内部的行为，对客户端是屏蔽的。
> 4. 客户端只产生了一次请求，客户端地址栏不变。
> 5. 请求转发通过**`HttpServletRequest`**对象获取请求转发器实现。
> 6. 请求转发可以转发给其他Servlet动态资源，也可以转发给一些静态资源以实现页面跳转。
> 7. 请求转发可以转发给WEB-INF下受保护的资源。**`该方式是WEB-INF下受保护资源的唯一访问方式，只有通过请求转发才能进行访问。`**
> 8. **请求转发不能转发到外部资源。**



**请求转发的实现**

1. 调用request对象的`getRequestDispatcher()`方法，传入要转发到的路径，去获取RequestDispatcher对象。该类型是请求转发器类型。
2. 调用请求转发器对象的`forward()`方法，做出转发动作，传入request与response对象，进行跳转。



**测试：**

* **案例1：转发给其他Servlet动态资源**

在项目的webapp下创建index.html，在index.html中创建一个表单，表单提交到项目路径下的servletA路径下：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<form action="./servletA">
    用户名：<input type="text" name="username"><br/>
    密码：<input type="password" name="pwd" ><br/>
    <input type="submit" value="提交">
</form>
</body>
</html>
```

然后去创建两个Servlet，分别是ServletA与ServletB，在ServletA中实现请求转发到ServletB，并且在这两个Servlet中都去获取请求的参数信息并打印：

**ServletA**

```java
@WebServlet("/servletA")
public class ServletA extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ServletA执行了");

        String username = req.getParameter("username");
        System.out.println(username);

        //请求转发
        //1、获取请求转发器
        //这里的路径是url路径，也就是http://ip:port/类型，这里使用了相对路径，也就是与当前Servlet相同路径下的servletB
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("./servletB");


        //2、让请求转发器做出转发动作，将request与response传入
        requestDispatcher.forward(req, resp);

    }
}
```

**ServletB**

```java
@WebServlet("/servletB")
public class ServletB extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ServletB执行了");

        String username = req.getParameter("username");
        System.out.println(username);
    }
}
```

index.html的表单提交到/servletA对应的ServletA，然后在ServletA中请求转发给了/servletB对应的ServletB中。

**显示：**

进入到页面中，给表单随便输入数据：

<img src=".\images\image-20240602185414705.png" alt="image-20240602185414705" style="zoom:67%;" /> 

点击提交，查看控制台打印结果：

<img src=".\images\image-20240602185447995.png" alt="image-20240602185447995" style="zoom:67%;" /> 

由结果可知，ServletA请求转发跳转到了ServletB中，并且请求中的参数可以继续向下传递。





* **案例2：请求转发可以实现WEB-INF下受保护资源的跳转**

在webapp的WEB-INF目录下，创建一个a.html文件，我们可以使用请求转发的方式，跳转到该受保护的静态资源中，去展示一个页面。

<img src=".\images\image-20240602191433323.png" alt="image-20240602191433323" style="zoom:67%;" /> 

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    我是WEB-INF目录下的a.html
</body>
</html>
```

然后，去创建一个ServletC，在Servlet中实现请求转发跳转到该静态文件：

```java
@WebServlet("/servletC")
public class ServletC extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("跳转到WEB-INF下的a.html中");
        //获取RequestDispatcher对象
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("./WEB-INF/a.html");
        //实现跳转
        requestDispatcher.forward(req, resp);
    }
}
```

该a.html是放在WEB-INF目录下的，当项目部署后，a.html则是放在项目下的WEB-INF目录下，所以路径是./WEB-INF/a.html，./表示的是当前路径中。

由于该Servlet的访问路径是http://ip:port/项目名/servletC，所以在该Servlet中使用相对路径./WEB-INF/a.html表示的就是http://ip:port/项目名/WEB-INF/a.html，也就可以正确地访问到该a.html文件。

<img src=".\images\image-20240602191800758.png" alt="image-20240602191800758" style="zoom:67%;" /> 

运行后，去访问http://localhost:8080/demo04/servletC，则页面显示结果：

<img src=".\images\image-20240602192223078.png" alt="image-20240602192223078" style="zoom:67%;" /> 

控制台打印结果：

<img src=".\images\image-20240602192244268.png" alt="image-20240602192244268" style="zoom:80%;" /> 

即，此时成功发生了请求转发，从ServletC中跳转到了受保护的文件a.html中。

直接访问是无法访问到的。







## 3、响应重定向

**响应重定向**，是指客户端给服务器发送请求访问，服务器响应给客户端一个新的地址，让客户端重新访问新地址。（原地址可能废弃）



**图示**：

![image-20240602194907852](.\images\image-20240602194907852.png)

客户端发送请求，去访问ServletA，在ServletA中，将响应码302和目标资源路径放到response中响应给客户端；

客户端在拿到响应数据，就会重新发送一个请求，去访问新的资源如ServletB，然后经过ServletB的业务处理后返回一个新的response给客户端。

这就是响应重定向的过程。



**响应重定向的两种方式：**

**第一种：**通过手动设置response的响应码域和响应头中的Location参数的方式

```java
//设置响应码302，表示重定向
response.setStatus(302);
//设置响应头，说明新的地址在哪里
response.setHeader("Location", "新路径");
```



**第二种（推荐）：**使用**`setRedirect()`**的方式

```java
response.setRedirect("新路径");
```

这一行代码就进行了两步操作：设置响应状态码为302，同时设置Location响应头的值为新路径的值。



> **响应重定向特点：**
>
> * 响应重定向通过HttpServletResponse对象的**`sendRedirect()`**方法实现。
> * 响应重定向是服务端通过302响应码和路径，告诉客户端自己去找其他资源，是在服务端提示下，**客户端的行为**。
> * 客户端至少发送了两次请求，**客户端地址栏是变化的**。
> * 服务端产生了多次请求和响应对象，且请求和响应对象不会传递给下一个资源。
> * 因为全程产生了多个HttpServletRequest对象，所以请求参数不可传递，请求域中的数据也不可传递。
> * 重定向可以是其他Servlet动态资源，也可以是一些静态资源以实现页面跳转。
> * **重定向不可以去访问WEB-INF下受保护的资源**，因为重定向实际上就是多次进行访问。
> * **重定向可以访问本项目外的外部资源。**
>
> **`实际上响应重定向就是因服务端的信息提示，客户端多次进行请求访问的过程。`**



**测试案例**

创建两个Servlet，一个Servlet1，一个Servlet2，在Servlet1中使用响应重定向的方式，让客户端重新去访问Servlet2。

**Servlet1**

```java
@WebServlet("/servlet1")
public class Servlet1 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("servlet1执行了");

        //响应重定向
        resp.sendRedirect("./servlet2");
    }
}
```

**Servlet2**

```java
@WebServlet("/servlet2")
public class Servlet2 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Servlet2执行了");
    }
}
```

访问Servlet1的项目路径：

<img src=".\images\image-20240602214710466.png" alt="image-20240602214710466" style="zoom:80%;" /> 

此时，使用F12，可以查看到两个请求信息：

<img src=".\images\image-20240602214803190.png" alt="image-20240602214803190" style="zoom:80%;" /> 

查看servlet1请求的信息：

<img src=".\images\image-20240602214837327.png" alt="image-20240602214837327" style="zoom:80%;" /> 

可以看到，响应行的状态码为302，响应头中Location值为./servlet2，也就是响应重定向的新地址。

查看servlet2请求信息：

<img src=".\images\image-20240602215106548.png" alt="image-20240602215106548" style="zoom:67%;" /> 

该请求就是使用重定向后，客户端第二次发送的请求信息。

在重定向后，url地址栏也变成了重定向的地址：

<img src=".\images\image-20240602215200940.png" alt="image-20240602215200940" style="zoom:80%;" /> 

控制台打印信息：

<img src=".\images\image-20240602215236684.png" alt="image-20240602215236684" style="zoom:80%;" /> 

可以看到，两个Servlet都执行了。







> **开发中这两种方式如何使用？**
>
> 两种方式都能够实现页面的跳转，在实际开发中，**优先使用响应重定向**的方式实现页面跳转。当重定向的方式**无法满足业务需求**时，比如要求参数要继续传递，或者访问WEB-INF内的资源时，那此时就只能使用**请求转发**。





---

# 九、web乱码问题和路径问题总结

## 乱码问题

> **乱码问题产生的根本原因**

1. 数据的编码和解码使用的表示同一个字符集
2. 使用了不支持某个语言文字的字符集



当我们将字符使用UTF-8进行编码存入到计算机时，而解码时却使用GBK去解码，去根据计算机中10101这样的数据查找对应的中文字符，此时就会出现乱码。



> **各个字符集的兼容性**

<img src=".\images\1682326867396.png" alt="1682326867396" style="zoom:80%;" />



字符集都兼容了ASCII。

ASCII中有什么？英文字母和一些通常使用的符号，所以这些东西无论使用什么字符集都不会乱码。



### 1、HTML页面乱码问题

**问题演示：**

创建一个html文件，使用记事本的方式打开：

 <img src=".\images\image-20240602224023117.png" alt="image-20240602224023117" style="zoom:50%;" />

在html文件，使用的是UTF-8的方式进行编码的，编码方式在右下角。

而浏览器对html文件进行解码时，是通过其中的`<meta charset="">`中charset属性判断该html使用的是何种方式编码，那么浏览器就会使用同样的字符集进行解码。

当我们去给该meta标签的charset属性设置为GBK，但是实际所使用的编码方式是UTF-8时，此时使用浏览器打开该html页面，显示的结果为：

![image-20240602224241590](.\images\image-20240602224241590.png) 

此时就会出现乱码现象。

但是，这种现象不会出现在IDEA中，因为在IDEA中，html文件的编码方式会根据meta标签中设置的编码方式而发生改变。比如，我们将meta标签中的charset属性设置为GBK，此时，该文件的编码方式就会自动变为GBK；当我们将charset属性设置为UTF-8，则该html文件的编码方式也就会自动变成UTF-8：

<img src=".\images\image-20240602224711198.png" alt="image-20240602224711198" style="zoom: 43%;" /> 

所以，当我们使用IDEA去开发JavaWeb项目时，就不需要担心html文件的乱码问题。



**如何将在IDEA中开发的项目文件，全都设置统一的字符集编码呢？**

<img src=".\images\image-20240602225126369.png" alt="image-20240602225126369" style="zoom: 40%;" /> 

Global Encoding表示的是全局字符集编码配置

Project Encoding表示的是项目的字符集编码配置

还有一个Default encoding for properties files，指的是对.properties文件字符集的设置

我这里全部都设置成了UTF-8。

经过上述修改后，在IDEA中开发的项目文件，字符集默认都是UTF-8了。







### 2、Tomcat控制台乱码问题

**问题说明**

在Tomcat控制台中，打印的日志信息都是使用`UTF-8`字符集进行编码的，但是，由于当前系统所使用的是中文环境，dos控制台使用`GBK`字符集进行解码，所以Tomcat控制台打印的日志信息会出现乱码问题。

例如：

<img src=".\images\image-20240602225658155.png" alt="image-20240602225658155" style="zoom:40%;" /> 



**问题解决：**

在Tomcat启动时，IDEA中会弹出三个窗口，分别是server、Tomcat Localhost Log以及Tomcat Catalina Log：

<img src=".\images\image-20240602230232969.png" alt="image-20240602230232969" style="zoom:80%;" /> 

这三个窗口都是打印日志信息的。

解决乱码问题，只需要保证这三个窗口打印出来的日志信息不是乱码即可，也就是保证这三个窗口打印的日志信息是使用GBK进行编码的。

在Tomcat应用目录下的conf/logging.properties文件中，设置着日志的编码字符集：

<img src=".\images\image-20240602230459753.png" alt="image-20240602230459753" style="zoom: 50%;" /> 

其中，server控制台的字符集对应的配置是：

<img src=".\images\image-20240602230718244.png" alt="image-20240602230718244" style="zoom:50%;" /> 

Tomcat Localhost Log控制台的编码字符集对应的配置是：

<img src=".\images\image-20240602230814495.png" alt="image-20240602230814495" style="zoom:40%;" /> 

Tomcat Catalina Log对应的编码字符集配置是：

<img src=".\images\image-20240602230852528.png" alt="image-20240602230852528" style="zoom:40%;" /> 

所以，我们只需要将这三个配置的字符集修改成GBK即可。

<img src=".\images\image-20240602230953273.png" alt="image-20240602230953273" style="zoom:40%;" /> 



修改后：

<img src=".\images\image-20240602231038231.png" alt="image-20240602231038231" style="zoom:50%;" /> 

此时Tomcat的控制台就不再有乱码的情况了。









### 3、请求乱码问题

**问题演示**

创建一个index.html，在该html中有一个表单，去提交数据到指定的Servlet中：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="GBK">
    <title>Title</title>
</head>
<body>
<form action="./getTest">
    <input type="text" name="username"><br/>
    <input type="submit" value="提交">
</form>
</body>
</html>
```

注意，这里html使用的字符集是GBK，也就是说该html是使用GBK进行编码的，提示浏览器也是使用GBK进行解码。

然后，创建一个Servlet接收数据，并且打印username请求参数的value值：

```java
@WebServlet("/getTest")
public class GetFormTest extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        System.out.println(username);
    }
}

```

运行Tomcat，打开index页面，输入中文字符并提交，例如：

<img src=".\images\image-20240602233457123.png" alt="image-20240602233457123" style="zoom:80%;" /> 

提交数据后，跳转到对应的Servlet，执行相应的service()方法，执行完毕后查看控制台输出：

![image-20240602233550516](.\images\image-20240602233550516.png) 

发现出现了乱码，并且在跳转后的url也显示了乱码：

![image-20240602233617223](.\images\image-20240602233617223.png) 

由于请求方式是使用GET，所以请求参数会放在url中，请求参数中的乱码也就出现在了url中。



**问题说明**

为什么会出现上面的乱码问题？

在html中，对于`<meta charset="GBK">`标签来说，charset字符集的设置，影响到了IDEA对于该html编码的字符集，也影响到了浏览器去解析该html页面时所使用到解码字符集。

同时也能够影响form表单提交时，使用什么字符集对请求参数值进行编码。

我们来看看上例中，请求报文信息：

<img src=".\images\image-20240602234039716.png" alt="image-20240602234039716" style="zoom: 67%;" /> 

表单提交的数据是"你好"，在请求行的url中，使用GBK编码转换成了%C4%E3%BA%C3数据。

然后Tomcat接收了请求报文，对请求报文进行解码，此时默认使用的是UTF-8字符集对请求行进行解码，由于编码方式与解码方式不一致，自然就出现了乱码问题。



**问题解决**

在HTML文件中，设置字符集为UTF-8

<img src=".\images\image-20240602234758770.png" alt="image-20240602234758770" style="zoom:50%;" /> 





### 4、响应乱码问题

**问题再现：**

创建一个Servlet，在Servlet中响应一些中文字符给客户端：

```java
@WebServlet("/test")
public class ServletTest extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("你好！Servlet!");
    }
}
```

去访问test路径，响应体内容是：

![image-20240603000234118](.\images\image-20240603000234118.png) 





**问题说明：**

在Tomcat10中，响应体默认的编码字符集是UTF-8，那么响应给客户端的响应体默认使用UTF-8进行编码。

Tomcat10将响应体使用UTF-8字符集编码成机器数据，然后传输给客户端。

但是客户端在拿到响应体的机器数据时，由于它不知道服务端是使用什么字符集进行编码的，它就会使用系统默认的字符集进行解码。由于系统处于中文环境中，所以客户端就会去使用GBK进行解码，此时就会报错。



**问题解决：**

在Servlet中，使用response的`setContentType()`方法，告诉浏览器以指定的MIME类型和字符集解析响应体。这个时候，浏览器在拿到响应体的机器数据时，就会根据响应头中指定的字符集进行解码。

如：

```java
@WebServlet("/test")
public class ServletTest extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        resp.getWriter().write("你好！Servlet!");
    }
}

```

响应信息：

<img src=".\images\image-20240603000809687.png" alt="image-20240603000809687" style="zoom:67%;" /> 

此时，浏览器就会根据响应头中的Content-Type属性，使用指定的MIME类型与字符集对响应体进行解码解析。

此时响应体就是正常编码的：

![image-20240603000931794](.\images\image-20240603000931794.png) 







---

## 路径问题（:star:）

### 文件准备

在项目中，创建如下结构：

<img src=".\images\image-20240603132623667.png" alt="image-20240603132623667" style="zoom:80%;" />  



### 相对路径与绝对路径问题

#### 相对路径情况分析

**相对路径：`以当前资源的所在路径为出发点，去找目标资源。`**

**相对路径语法：**

* 不以/开头
* ./表示当前资源路径，./可以省略
* ../表示上一层资源路径

> **`相对路径的规则就是当前资源的所在路径后，拼接目标资源的路径，然后发送请求找目标资源。`**



请查看具体的案例解释。



##### 情况1

假设，我们要在index.html中使用img标签引入webapp/static/img目录中的图片人物.jpg。

<img src=".\images\image-20240603130052150.png" alt="image-20240603130052150" style="zoom: 80%;" /> 

那么index.html是在webapp下的，而人物.jpg是在webapp下的static目录下的img目录中，所以在index.html中，使用img引入图片的写法是：

```html
<img src="./static/img/人物.jpg">
```

那么，此时去访问index.html页面，页面的呈现：

![image-20240603122817245](.\images\image-20240603122817245.png) 

就能够将图片显示出来。



**那具体访问该图片的过程是怎么样的呢？**

首先，我们去访问这个index.html，假设该index.html在demo05项目下，并且部署在Tomcat服务器中，那么这个资源的url就是http://localhost:8080/domo05/index.html。

通过这个url去访问index.html，服务端将index.html文件的数据响应给客户端，客户端就会去解析html文件数据。在解析代码过程中，客户端解析到了`<img src="./static/img/人物.jpg">`这段代码，然后客户端就会根据img中的路径去发送请求给服务端，获取图片。

也就是说，解析的请求是客户端发送的，客户端是不知道服务端中的目录结构，只会根据响应得到的路径信息发送请求。

客户端不知道服务端的目录结构，但是知道当前资源的路径：http://localhost:8080/domo05/index.html，客户端会根据当前资源路径与相对路径路径的关系得到要请求资源的路径。

**当前资源的请求路径**：http://localhost:8080/demo05/index.html

**当前资源是**：`index.html`

**当前资源的所在路径**：http://localhost:8080/demo05

**目标路径**：`./static/img/人物.jpg`

> **`相对路径的规则就是当前资源的所在路径后，拼接目标资源的路径，然后发送请求找目标资源。`**

那么，当解析index.html响应数据后，要去查找指定的图片数据，就会在当前资源的所在路径后，拼接图片目标资源的路径，得到的路径是：`http://localhost:8080/demo05/static/img/人物.jpg`，此时，浏览器就根据这个路径去发送请求访问资源，如果该路径能够正确地匹配上对应的文件时，此时服务端就会将对应的文件数据响应回来，此时客户端就能够拿到数据。





##### 情况2

假设在webapp中的a/b/c目录下的test.html中，也想使用img标签引入webapp中的static/img目录下的人物.jpg图片：

<img src=".\images\image-20240603130220342.png" alt="image-20240603130220342" style="zoom:67%;" /> 

此时，在test.html中，该如何使用相对路径去引入该图片呢？

**分析：**

test.html资源的访问路径应该是http://localhost:8080/a/b/c/test.html，浏览器访问该文件，服务端响应数据给浏览器，浏览器解析响应体数据时发现其中有一个img标签，是使用相对路径的方式引入人物.jpg。

然后浏览器就会发送第二次请求给服务器，第二次请求的目的就是去访问图片资源。

由于在src中使用的是相对路径，相对路径是根据当前资源的所在路径，拼接相对路径的方式获取完整的路径。

**当前资源（也就是test.html）的所在路径是**：http://localhost:8080/demo05/a/b/c/

而我们**要去获取目前资源（也就是图片）的路径是**：http://localhost:8080/demo05/static/img/人物.jpg

我们可以看到，当前资源所在的路径和目标资源路径前面相同的路径是：http://localhost:8080/demo05/，所以我们若想要让相对路径拼接上当前资源路径，就需要在相对路径中，将/a/b/c/这三个目录抵消，也就是往目录前面移动。

在相对路径中，使用../的方式，将目录抵消，那么我们需要抵消三个目录，就是使用`../../../`的方式，然后再往当前资源的所在路径后面拼接/static/img/人物.jpg，所以最终在test.html中，引入图片的路径是：`../../../static/img/人物.jpg`

```html
<img src="../../../static/img/人物.jpg">
```

当开test.html的显示结果：

<img src=".\images\image-20240603131715258.png" alt="image-20240603131715258" style="zoom: 80%;" /> 





##### 情况3

以上两种情况，磁盘结构与url结构是一致的，所以可以直接通过磁盘结构使用相对路径来进行访问。但是有一些情况下，url的路径结构和磁盘结构实际上是不一致的，此时就不能通过磁盘结构，来对项目中的资源进行访问。

假如：要在webapp/WEB-INF/views/view1.html中，获取webapp/static/img/人物.jpg：

<img src=".\images\image-20240603175833195.png" alt="image-20240603175833195" style="zoom: 80%;" /> 

那么，此时我们使用相对路径在view1.html中对图片进行访问：

```html
<img src="../../static/img/人物.jpg">
```

那此时，是否可以通过使用请求转发的方式，跳转到view1.html，然后在view1.html中访问到该图片呢？

答案是不行的。

我们来测试一下：
首先，由于view1.html文件放在WEB-INF中，所以我们使用请求转发来进行访问。

在View1Servlet中，使用请求转发的方式来访问该view1.html：

```java
@WebServlet("/view1Servlet")
public class View1Servlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取RequestDispatcher对象
        // 由于当前Servlet的路径是http://localhost:8080/demo05/view1Servlet
        //所以当前资源的当前路径是http://localhost:8080/demo05/
        //使用相对路径，那么实际访问的路径是http://localhost:8080/demo05/WEB-INF/views/view1.html
        //由于使用的是请求转发，所以可以访问到WEB-INF中的资源
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("./WEB-INF/views/view1.html");
        requestDispatcher.forward(req, resp);
    }
}
```

那么，此时就使用了请求转发的方式访问到了webapp/WEB-INF/views/view1.html文件，但是在view1.html却无法对图片进行访问。显示结果： 

![image-20240603134425600](.\images\image-20240603134425600.png) 

![image-20240603134445929](.\images\image-20240603134445929.png) 

<img src=".\images\image-20240603134456485.png" alt="image-20240603134456485" style="zoom:50%;" /> 

**为什么会出现这种情况呢？**

这就和请求转发有关了。

当我们使用http://localhost:8080/demo05/view1Servlet去访问Servlet，Servlet给我们请求转发了，将去访问WEB-INF/view/view1.html文件，然后将view1.html中的数据响应给了客户端，客户端在拿到数据后解析响应体数据，其中的：

```xml
<img src="../../static/img/人物.jpg">
```

是根据当前资源所在路径，使用其相对路径来引入图片文件。

在view1.html中，所设置的是相对于view1.html的路径。

但是我们知道，请求转发时，url地址是不变的。

我们通过Servlet的请求转发来访问的view1.html，所以实际的url地址是：http://localhost:8080/demo05/view1Servlet

浏览器所认为的**当前资源是**：`viewServlet`，而不是view1.html

此时的**资源的当前路径是**：http://localhost:8080/demo05

那么将该路径拼接上相对路径../../static/img/人物.jpg，得到的**实际访问路径是**：http://localhost:8080/static/img/人物.jpg（相对路径中的../不会将前面的ip地址和端口号也去除）

![image-20240603140231448](.\images\image-20240603140231448.png) 

而我们在view1.html中，实际上是根据view1.html所在的当前路径，来考虑拼接相对路径。

**实际图片的路径是**：http://localhost:8080/demo05/static/img/人物.jpg

所以，此时无法去引入图片资源。

此时，我们可以将相对路径修改成绝对路径的方式：

```xml
<img src="/demo05/static/img/人物.jpg">
```

此时，去访问图片的路径就绝对是：http://localhost:8080/demo05/static/img/人物.jpg





##### 相对路径存在的问题及解决方案

**缺点**：目标资源路径受到当前资源路径的影响，不同的位置相对路径写法不同。

**解决方案：**使用绝对路径。

当使用相对路径时，只考虑文件和文件之间的目录结构是不靠谱的，我们要站在客户端的角度，以url结构基础来分析相对路径和绝对路径。



---

#### 绝对路径情况分析

**绝对路径：`始终以固定的路径作为出发点去找目标资源，和当前资源的所在路径没有关系。`**

**语法格式**：

* **以`/`开头**。不同的项目中，固定的路径出现点可能不一致。可以测试一下。

例如，在html中，使用绝对路径去访问资源：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>

</head>
<body>
<a href="/a/b/c">跳转</a>
</body>
</html>
```

访问index.html，点击超链接访问资源，可以看到超链接的路径为：

![image-20240603152059060](.\images\image-20240603152059060.png) 

所以，我们可以知道，在绝对路径中，开头的/所表示路径是：

http://localhost:8080

那我们要使用绝对路径的访问访问目标资源，还需要在该路径后面添加上项目的上下文路径Application Context，以及目标资源在当前项目下的路径信息。

即，使用**绝对路径的格式应该为**：

**`/项目路径上下文/目标资源所在项目下的路径`**

绝对路径与当前资源所在的路径无关，只与目标资源所在路径有关。不会因为当前资源路径的改变而影响目标资源的路径。



##### 案例

将相对路径中的情况三，我们使用绝对路径来实现。

在Servlet中，使用请求转发的方式访问WEB-INF/views/view.html文件，在该view.html中，使用绝对路径去访问/static/img/人物.jpg图片。

<img src=".\images\image-20240603152855511.png" alt="image-20240603152855511" style="zoom:67%;" /> 

Servlet

```java
@WebServlet("/view1Servlet")
public class View1Servlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("./WEB-INF/views/view1.html");
        requestDispatcher.forward(req, resp);
    }
}
```

view.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<img src="/demo05/static/img/人物.jpg">
</body>
</html>
```

此时，去访问Servlet，就能够因请求转发跳转到view.html中，并且在view.html中，就不会因为相对路径的缘故，请求转发时url不变而获取不到图片。绝对路径不会受到当前资源的路径的影响。

此时访问图片的路径：

![image-20240603153902043](.\images\image-20240603153902043.png) 





##### 绝对路径存在的问题及解决方案

**绝对路径的优点**：目标资源路径的写法不会受到当前资源路径的影响，不同的位置绝对路径写法一致。

**绝对路径的缺点**：绝对路径要补充项目的上下文路径，项目的上下文路径是可以改变的。此时，设定的绝对路径都要改变。



**解决方案：使用base标签**

在head标签中，定义一个base标签，用于定义当前页面中相对路径的公共前缀。

该base标签，是给当前页面中所有不加任何修饰的相对前缀前，自动补充href中的内容。

如：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <!--定义相对路径的公共前缀,将相对路径转化成了绝对路径-->
    <base href="/web03_war_exploded/">
</head>
<body>
    <img src="static/img/logo.png">
</body>
</html>
```

则在访问img路径时，就会自动给该相对路径前添加href的内容，得到/web03_war_exploded/static/img/log.png。无论该html中有多少个相对路径访问，都会在前面添加/web03_war_exploded/。

这样一来，当项目的上下文路径发生改变后，我们只需要该文件中的base标签内容即可，不需要去改文件中所有的绝对路径地址信息。

注意：base标签定义的公共前缀只在相对路径上生效，绝对路径中无效。如果相对路径开头有./或者../修饰，则base标签对该路径同样无效。即若需要使用该标签，不能使用./开头以及../开头，也不能以/开头。







> #### 总结
>
> * **`相对路径`**
>   * 相对路径的规则是：`以当前资源所在的路径为出发点去寻找目标资源`。
>   * 相对路径不以 / 开头
>   * 在file协议下，使用的是磁盘路径
>   * 在http协议下，使用的是url路径
>   * 相对路径中可以使用./表示当前资源所在路径，可以省略不写
>   * 相对路径中可以使用../表示当前资源所在路径的上一层路径，需要时手动添加。
>
> **缺点**：目标资源路径受到当前资源路径的影响，不同的位置相对路径写法不同。
>
> * **`绝对路径`**
>   * 绝对路径的规则是：`使用以一个固定的路径去做出发点去寻找目标资源，和当前资源所在的路径没有关系`
>   * 绝对路径要以 / 开头
>   * 绝对路径的写法中，不以当前资源的所在路径为出发点，所以不会出现 ./ 和 ../
>   * 不同的项目和不同的协议下，绝对路径的基础位置可能不同，要通过测试确定
>   * 绝对路径的好处就是：无论当前资源在哪，寻找目标资源路径的写法都一样。
>
> **优点**：目标资源路径的写法不会受到当前资源路径的影响，不同的位置绝对路径写法一致。
>
> **缺点**：绝对路径要补充项目的上下文路径，项目的上下文路径是可以改变的。此时，设定的绝对路径都要改变。
>
> * 应用场景：
>   1. 前端代码中，href、src、action等属性
>   2. 请求转发和重定向中的路径







---

### 请求转发与响应重定向中的路径问题

对于响应重定向来说：其跳转路径中，相对路径与就绝对路径的写法与前端是一样的。

但是，**对于请求转发而言：跳转路径若使用相对路径来实现，与去前端的写法是一样的；但是若使用的是绝对路径，则绝对路径中不需要在/斜杆后面添加项目的上下文路径，/默认已经包含了上下文路径了。即/就等于http://localhost:8080/demo05，我们就只需要考虑项目上下文路径之后的路径。**

我们直接根据案例来具体地考虑。



**目的**

在ServletA中，使用响应重定向与请求转发跳转到ServletB。

**ServletA**

```java
@WebServlet("/a/b/c/servletA")
public class ServletA extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //使用重定向跳转到ServletB
    }
}
```

**ServletB**

```java
@WebServlet("/x/y/servletB")
public class ServletB extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write("我是ServletB");
    }
}
```





#### 案例1：使用相对路径实现

**响应重定向**

我们来看看ServletA，ServletA的映射地址是/a/b/c/ServletA，那么访问ServletA的url为：http://localhost:8080/demo05/a/b/c/ServletA

ServletA的当前资源路径是：http://localhost:8080/demo05/a/b/c

要去跳转的ServletB目标资源为：http://localhost:8080/demo05/x/y/servletB

那么此时，若想要去使用相对路径的方式跳转，则相对路径应该为：`../../../x/y/servletB`。

```java
@WebServlet("/a/b/c/servletA")
public class ServletA extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //使用重定向跳转到ServletB
        resp.sendRedirect("../../../x/y/servletB");
    }
}
```

此时，去访问http://localhost:8080/demo05/a/b/c/servletA，此时服务端响应的信息：

<img src=".\images\image-20240603171749123.png" alt="image-20240603171749123" style="zoom: 80%;" /> 

响应码为302，此时客户端就知道要重新发送请求跳转。

Location为../../../x/y/servletB，该信息为相对路径，那么客户单就会将该值拼接到当前资源的所在路径中，也就是往http://localhost:8080/demo05/a/b/c/。

../抵消一层目录，一共有三层../，就抵消了三层路径，然后将/x/y/servletB进行拼接，获得最终要重新发送请求的url：

http://localhost:8080/demo05/x/y/servletB。

<img src=".\images\image-20240603172119804.png" alt="image-20240603172119804" style="zoom:80%;" /> 





**请求转发**

请求转发的相对路径使用方式与前端一致：

```java
@WebServlet("/a/b/c/servletA")
public class ServletA extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //使用请求转发的方式访问ServletB
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("../../../x/y/servletB");
        requestDispatcher.forward(req, resp);
    }
}
```









---

#### 案例2：使用绝对路径实现

**响应重定向**

响应重定向使用绝对路径考虑的方式与前端路径考虑方式一样。

绝对路径与当前资源所在的路径无关，只与目标资源的路径有关。

目标资源ServletB的url为：http://localhost:8080/demo05/x/y/servletB。

绝对路径以/开头，/表示的是项目上下文路径前面的部分：http://localhost:8080/。

所以，若使用绝对路径的方式，则在servletA中所设置的重定向地址应该为：**`/demo05/x/y/servletB`**

```java
@WebServlet("/a/b/c/servletA")
public class ServletA extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //使用重定向跳转到ServletB
        resp.sendRedirect("/demo05/x/y/servletB");
    }
}
```

此时，去访问ServletA，就会跳转到http://localhost:8080/demo05/x/y/servletB

<img src=".\images\image-20240603173122620.png" alt="image-20240603173122620" style="zoom:67%;" /> 

访问ServletA，响应信息：

![image-20240603173220357](.\images\image-20240603173220357.png) 

响应码是302，浏览器会重新发送请求访问Location地址。

Location地址以/开头表示该地址是一个绝对地址，将Location中的信息拼接上http://lcoalhost:8080就是请求的地址。

我们也可以使用ServletContext对象中的getContextPath()方法，获取项目的上下文路径，然后将获得的值拼接到跳转路径中，如：

```java
@WebServlet("/a/b/c/servletA")
public class ServletA extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = req.getServletContext();
        String contextPath = servletContext.getContextPath();

        resp.sendRedirect(contextPath + "/x/y/servletB");
    }
}

```

这样就能够实现动态获取项目的上下文路径，防止因上下文路径的改变而导致需要手动更改路径。





**请求转发**

请求转发的跳转路径，若使用的是绝对路径的方式，与前端绝对路径有些不一样，**/斜杆表示的内容不仅仅是http://localhost:8080，还包含了项目的上下文路径**，即：`http://localhost:8080/项目上下文路径`。

这是唯一不同的地方。

实现ServletB的跳转：

```java
@WebServlet("/a/b/c/servletA")
public class ServletA extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //使用请求转发的方式访问ServletB
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/x/y/servletB");
        
        requestDispatcher.forward(req, resp);
    }
}
```

此时，使用的是绝对路径，并且因使用的方式是请求转发，/能够将项目的上下文路径都包含，所以在后续不再填写上下文路径。





---

#### 请求转发目标资源内相对路径处理

假如：要在webapp/WEB-INF/views/view1.html中，获取webapp/static/img/人物.jpg：

<img src=".\images\image-20240603175833195.png" alt="image-20240603175833195" style="zoom: 80%;" /> 

那么，此时我们使用相对路径在view1.html中对图片进行访问：

```html
<img src="../../static/img/人物.jpg">
```

那此时，是否可以通过使用请求转发的方式，跳转到view1.html，然后在view1.html中访问到该图片呢？

答案是不行的。

我们来测试一下：
首先，由于view1.html文件放在WEB-INF中，所以我们使用请求转发来进行访问。

在View1Servlet中，使用请求转发的方式来访问该view1.html：

```java
@WebServlet("/view1Servlet")
public class View1Servlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取RequestDispatcher对象
        // 由于当前Servlet的路径是http://localhost:8080/demo05/view1Servlet
        //所以当前资源的当前路径是http://localhost:8080/demo05/
        //使用相对路径，那么实际访问的路径是http://localhost:8080/demo05/WEB-INF/views/view1.html
        //由于使用的是请求转发，所以可以访问到WEB-INF中的资源
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("./WEB-INF/views/view1.html");
        requestDispatcher.forward(req, resp);
    }
}
```

那么，此时就使用了请求转发的方式访问到了webapp/WEB-INF/views/view1.html文件，但是在view1.html却无法对图片进行访问。显示结果： 

![image-20240603134425600](.\images\image-20240603134425600.png) 

![image-20240603134445929](.\images\image-20240603134445929.png) 

<img src=".\images\image-20240603134456485.png" alt="image-20240603134456485" style="zoom:50%;" /> 

**为什么会出现这种情况呢？**

这就和请求转发有关了。

当我们使用http://localhost:8080/demo05/view1Servlet去访问Servlet，Servlet给我们请求转发了，将去访问WEB-INF/view/view1.html文件，然后将view1.html中的数据响应给了客户端，客户端在拿到数据后解析响应体数据，其中的：

```xml
<img src="../../static/img/人物.jpg">
```

是根据当前资源所在路径，使用其相对路径来引入图片文件。

在view1.html中，所设置的是相对于view1.html的路径。

但是我们知道，请求转发时，url地址是不变的。

我们通过Servlet的请求转发来访问的view1.html，所以实际的url地址是：http://localhost:8080/demo05/view1Servlet

浏览器所认为的**当前资源是**：`viewServlet`，而不是view1.html

此时的**资源的当前路径是**：http://localhost:8080/demo05

那么将该路径拼接上相对路径../../static/img/人物.jpg，得到的**实际访问路径是**：http://localhost:8080/static/img/人物.jpg（相对路径中的../不会将前面的ip地址和端口号也去除）

![image-20240603140231448](.\images\image-20240603140231448.png) 

而我们在view1.html中，实际上是根据view1.html所在的当前路径，来考虑拼接相对路径。

**实际图片的路径是**：http://localhost:8080/demo05/static/img/人物.jpg

所以，此时无法去引入图片资源。

此时，我们可以将相对路径修改成绝对路径的方式：

```xml
<img src="/demo05/static/img/人物.jpg">
```

此时，去访问图片的路径就绝对是：http://localhost:8080/demo05/static/img/人物.jpg





> #### 总结
>
> * 若使用相对路径的方式实现请求转发与响应重定向，
>   * 使用的方式与前端一致，即./表示当前路径下，../表示上一层路径下。
> * 若使用绝对路径的方式实现请求转发与响应重定向，
>   * 响应重定向使用方式与前端一致，即/开头，/表示的含义是http://ip:port/，不包含项目上下文路径。
>   * 请求转发与前端有些不同，使用/开头，/表示的含义是http://ip:port/项目上下文路径，是包含项目上下文路径的。
>
> **注意：**
>
> 请求转发到目标资源后，若目标资源使用的`相对路径`的方式去获取其他资源，此时`相对的路径是请求转发跳转之前的路径`，因为`请求转发不会改变url地址`。所以，在请求转发的目标资源中，若要引入其他资源，则一般使用绝对路径的方式。



---

### 解决路径问题的方案：不设置项目上下文路径

在之前的学习中，我们可以发现路径存在几个问题：

1. 使用相对路径，url路径可能与项目目录结构不同：请求转发跳转的目标资源中，若使用相对路径去访问了其他资源，则相对的是当前的资源路径。
2. 若项目的上下文路径改变，使用的绝对路径也需要去改变项目的上下文路径。
3. 若请求转发使用的是绝对路径，则该绝对路径特殊，/中包含了项目的上下文路径。

对于上述这些问题，有没有什么解决方案能够全部解决？

有。

**解决方案**：

将项目的上下文路径缺省，设置为/，我们将所有需要使用路径的位置全部都使用绝对路径，所有绝对路径中都不必填写项目的上下文了，开头都使用/即可。

<img src=".\images\image-20240603194408225.png" alt="image-20240603194408225" style="zoom:50%;" /> 

例如：

当我们将项目的上下文路径设置为/，此时使用绝对路径的方式，无论使用响应重定向还是请求转发的方式，实际上跳转到的路径都是一样的：

```java
@WebServlet("/a/b/c/servletA")
public class ServletA extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //使用请求转发的方式访问ServletB
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/x/y/servletB");
        
        requestDispatcher.forward(req, resp);
    }
}
```

```java
@WebServlet("/a/b/c/servletA")
public class ServletA extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //使用重定向跳转到ServletB
        resp.sendRedirect("/x/y/servletB");
    }
}
```

在上面请求转发与响应重定向跳转的地址都是一样的：

http://localhost:8080/x/y/servletB



在实际的开发中，我们就是将项目的上下文设置成/，只需要根据ip地址和端口号，就能够找到对应的项目，没有必要给项目设置上下文路径。





---

# 十、MVC架构模式

> **MVC（Model View Controller）**是软件工程中的一种软**`件架构模式`**，它把软件系统分为**`模型`**、**`视图`**和**`控制器`**三个基本部分。用一种业务逻辑、数据、界面显示分离的方法组织代码，将业务逻辑聚集到一个部件里面，在改进和个性化定制界面及用户交互的同时，不需要重新编写业务逻辑。
>
> 实现MVC架构模式，有助于实现高内聚、低耦合；更加符合开闭原则。

* **M**：Model 模型层，具体功能如下

  1. 存放和数据库对象的实体类以及一些用于存储非数据库表完整相关的VO对象

     （比如进行联表查询时，数据库对应的实体类就无法存储，此时就可以去创建一个VO对象用于存放多个表格查询返回的结果集）

  2. 存放一些对数据进行逻辑运算操作的一些业务处理代码

* **V**：View 视图层，具体功能如下

  1. 存放一些视图文件相关的代码 html css js等
  2. 在前后端分离的项目中，后端已经没有视图文件，该层次已经衍化成独立的前端项目

* C：Controller 控制层，具体功能如下：

  1. 接收客户端请求，获得请求数据
  2. 将准备好的数据响应给客户端



**MVC模式下，项目中的常见包：**

* **`M`**：
  1. 实体类包（pojo / entity / bean）：专门存放和数据库对应的实体类和一些VO对象
  2. 数据库访问包（dao / mapper）：专门存放对数据库不同表格CRUD方法封装的一些类
  3. 服务包（service）：专门存放对数据进行业务逻辑运算的一些类
* **`C`**：
  * 控制层包（controller）
* **`V`**：
  1. web目录下的视图资源：html css js img等
  2. 前端后端分离后，在后端项目中已经不存在了

 

如下图所示，一个项目中MVC架构模式的案例：

![image-20240603202506132](.\images\image-20240603202506132.png)



**非前后端分离的MVC**

![1690349913931](.\images\1690349913931.png)



**前后端分离的MVC**

![1683363039636](.\images\1683363039636-1690349401673.png)
