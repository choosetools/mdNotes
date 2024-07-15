[TOC]

# 所有可能需要的依赖

```xml
<!--spring的webmvc依赖-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>6.0.6</version>
</dependency>


<!--Servlet的依赖，Spring6要求使用jakarta命名空间-->
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.0.0</version>
    <scope>provided</scope>
</dependency>


<!--处理json格式数据所需依赖-->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>


<!-- jsp所需依赖-->
<dependency>
    <groupId>jakarta.servlet.jsp.jstl</groupId>
    <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
    <version>3.0.0</version>
</dependency>


<!-- 校验注解实现-->
<!-- https://mvnrepository.com/artifact/org.hibernate.validator/hibernate-validator -->
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>8.0.0.Final</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.hibernate.validator/hibernate-validator-annotation-processor -->
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator-annotation-processor</artifactId>
    <version>8.0.0.Final</version>
</dependency>
```





# 一、SpringMVC简介和体验

## 1、介绍

**`SpringMVC底层就是封装的Servlet`。**

Spring Web MVC是基于Servlet API构建的原始Web框架，从一开始就包含在Spring Framework中。正式名称"Spring Web MVC"来自其原模块的名称（`spring-webmvc`），但它通常被称为"Spring MVC"。

在控制层框架历经Strust、WebWork、Strust2等诸多产品的历代更迭之后，目前业界普遍选择了SpringMVC作为JavaEE**表述层开发的首选方案**。之所以能做到这一点，是因为SpringMVC具备如下的显著优势：

* Spring家族原生产品，与IOC容器等基础设施无缝对接；
* 表述层各细分领域需要解决的问题全方位覆盖，提供全面解决方案；
* 代码清晰简洁，大幅度提升开发效率；
* 内部组件化程度高，可插拔式组件即插即用，想要什么功能配置相应组件即可；
* 性能卓越，尤其适合现代大型、超大型互联网项目要求。

**原生Servlet API开发代码片段**

```java
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
    String userName = request.getParameter("userName");
    
    System.out.println("userName="+userName);
}
```

**基于SpringMVC开发代码片段**

```
@RequestMapping("/user/login")
public String login(@RequestParam("username") String username, String password){
	login.debug("userName=" + userName);
	//调用相关业务
	
	return "result";
}
```



## 2、主要作用

![img](.\images\image213123123.png)

SSM框架构建起单体项目的技术栈要求！其中的SpringMVC负责表述层（控制层）实现简化！

SpringMVC的作用主要是覆盖的表述层，例如：

* 请求映射
* 数据输入
* 视图界面
* 请求分发
* 表单回显
* 会话控制
* 过滤拦截
* 异步交互
* 文件上传
* 文件下载
* 数据校验
* 类型转换等等

即主要作用是：

1. `简化前端参数接收（形参列表）`
2. `简化后端数据响应（返回值）`
3. 其他......





## 3、核心组件和执行流程（:star:）

**SpringMVC中的核心组件**

* **`DispactherServlet`：前端控制器。**用于接收请求、响应结果，相当于转发器，它是SpringMVC框架中最核心的组件，有了它就能够减少其他组件之间的耦合度。
* **`HandlerMapping`：处理器映射器**。它内部缓存了handler方法和handler访问路径，根据请求的URL，找到与路径匹配的handler处理器方法，并将方法信息返回给前端控制器。需要将对象加入到IOC容器中才可生效。
* **`HandlerAdapter`：处理器适配器。**它的作用是去执行具体的handler处理器方法，并且简化请求参数，将其转换成handler所需的参数，并且简化响应数据，将handler返回的数据封装到response中返回给前端控制器。需要将该对象加入到IOC容器中才可生效。
* **`hander`：处理器方法**。是Controller类内部的方法简称，由我们自己定义，用来接收参数，调用后端的业务，最终返回响应结果。
* **`ViewResolver`：视图解析器**。若我们要去响应一个视图页面给客户端时，比如，要去返回/WEB-INF/html/index.html页面，我们会通过视图解析器帮助查找对应的页面，也许我们只返回一个index字符串，视图解析器会通过我们的配置找到具体的页面数据响应给用户。但是注意，前后端分离项目，后端只返回JSON数据，不返回页面，就不需要视图解析器。所以，视图解析器并不是必须的。需要将该对象加入到IOC容器中才可生效。

对于DispatcherServlet前端处理器、HandlerMapping处理器映射器、HandlerAdapter处理器适配器以及ViewResovler视图解析器都是由SpringMVC给我们提供的，我们只需要进行IOC配置，将它们加入到IOC容器中即可生效。

对于handler处理器方法来说，则是需要我们自己定义，它是真正干活的。

介绍了SpringMVC中的核心组件，那么就来看看这些核心组件的具体执行流程是怎样的：

**SpringMVC的执行流程**

![](.\images\imagedsasda.png)

1. 用户通过客户端发起一个请求，请求由前端控制器`DispatcherServlet`接收；
2. DispatcherServlet前端控制器收到请求后，调用处理映射器`HandlerMapping`，（HandlerMapping中存放的是在Controller中所配置的handler方法名，以及方法所对应的映射路径）HandlerMapping根据请求的URL找到对应的handler方法，并将其返回给DispatcherServlet；
3. DispatcherServlet前端控制器调用`HandlerAdapter`，将获得到的handler方法信息传递给处理器适配器；
4. 处理器适配器根据会根据`handler`，调用真正的handler方法处理请求，并处理相关的业务逻辑；
5. 当处理器处理完业务后，返回响应数据给`HandlerAdapter`，HandlerAdapter再将数据返回给前端控制器；
6. 若需要去查找视图，则前端控制器将响应的数据传给`ViewResolver`视图解析器，视图解析器根据配置以及响应数据查找对应的视图，并将视图信息返回给前端控制器；前端控制器将具体的视图进行渲染，渲染完成后响应给用户。
7. 若不查找视图，则前端控制器直接将数据响应给用户。







---

## 4、快速体验（全注解启动:star2:）

### 需求

创建一个SpringMVC程序，其中定义一个hanlder()，当我们去访问http://localhost:8080/springmvc/hello时，响应hello springmvc!（不是视图，只是一个字符串）。

![img](.\images\imageeq231231233.png)

### 配置分析

我们若要使用SpringMVC来实现上述的要求，按照SpringMVC核心组件和执行流程中的内容，我们需要去配置的信息：

1. 首先，我们需要去配置`DispatcherServlet`，设置处理所有请求；
2. 配置`HandlerMapping`、`HandlerAdapter`、`Handler`，需要将它们加入到IOC容器中，供DispatcherServlet调用；
3. Handler自己声明需要配置到HandlerMapping中，供DS查找。



### 创建项目

创建父工程ssm-springmvc-part，删除项目下的src目录，并在父工程的pom.xml文件中，将该工程的打包方式设置为pom：

```xml
<packaging>pom</packaging>
```

然后，为父工程绑定maven仓库以及Tomcat服务器：

<img src=".\images\image-20240606070806784.png" alt="image-20240606070806784" style="zoom:50%;" /> 

<img src=".\images\image-20240606070849594.png" alt="image-20240606070849594" style="zoom:50%;" /> 







### 引入依赖

这里所使用的Spring是6.0.6版本，**Spring6要求JDK最低版本是`JDK17`**。所以，我们需要准备的Java环境至少是在JDK17以上。

这里所使用的Tomcat版本是Tomcat10：Tomcat10.0 ————> Spring Framework 6.x，要求**Spring最低版本是`Spring6`。**

* 首先，在SpringMVC中，需要使用到Spring的IOC容器，所以需要引入Spring的基本依赖**`spring-context`**；

* 然后，SpringMVC实际是Servlet API的框架，所以还需要引入**`Servlet`**，当前我使用的Tomcat版本是Tomcat 10，Tomcat 10开始，将原本的`javax.*`包都改名为`jakarta.*`，从Tomcat 10以后，需要使用的Servlet包都是以jakarta开头；

* 最后，SpringMVC属于Spring中的web-mvc部分，还需要引入**Spring中的webmvc**依赖。

```xml
<dependencies>
    <!--spring的webmvc依赖-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>6.0.6</version>
    </dependency>

    <!--Servlet的依赖，Spring6要求使用jakarta命名空间-->
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

由于在spring-webmvc依赖中，包含了spring的基本依赖spring-context，所以只需要引入spring-webmvc依赖即可。

在Tomcat中，已经包含了Servlet的api，如果在部署项目时也在项目中引入当前所使用的Servlet API，则使用Tomcat运行项目，就会发生冲突，为了避免出现冲突，我们需要将Servlet的依赖作用范围设置为provided，即只在编译与测试时生效，在运行时不会被包含在应用程序中。



### 创建子工程用于体验

创建一个子工程：spring-base-quick-01，

![image-20240606072119029](.\images\image-20240606072119029.png) 

将该普通的Java工程转换成Web工程（使用JBLJavaToWeb插件或者手动更改）：

<img src=".\images\image-20240606072159330.png" alt="image-20240606072159330" style="zoom: 80%;" /> 

这样一来，该工程的打包方式就变成了war。

然后，我们将工程配置Tomcat服务器：





### 为项目配置Tomcat服务器

<img src=".\images\image-20240606072546992.png" alt="image-20240606072546992" style="zoom:80%;" /> 

<img src=".\images\image-20240606072621634.png" alt="image-20240606072621634" style="zoom:50%;" /> 

<img src=".\images\image-20240606072651441.png" alt="image-20240606072651441" style="zoom: 33%;" /> 

选择war explded部署方式：（至于两种部署方式有什么区别，请查看Tomcat）

<img src=".\images\image-20240606072709846.png" alt="image-20240606072709846" style="zoom:67%;" /> 

将项目的上下文路径修改为/：

<img src=".\images\image-20240606072802894.png" alt="image-20240606072802894" style="zoom:67%;" />

将项目的上下文路径修改为/，直接使用ip地址与port端口号来表示相应的项目，这样一来，当我们使用绝对路径时，直接/就可以表示当前项目下。（使用绝对路径的请求转发就和其他绝对路径是一样的）

如果不使用/表示项目的上下文路径的话，则我们在访问@RequestMapping中所指定的路径时，还需要在指定路径前面添加项目的上下文路径，这样比较麻烦。不如直接在不同的类中，使用不同的路径，从而达到一样的效果。



### 声明Controller

创建com.atguigu.controller包，在该包下创建HelloController类。

项目的需求是访问http://localhost:8080/springmvc/hello，返回响应体内容是hello springmvc!。

如果使用Servlet的方式，实现为：

```java
@WebServlet("/springmvc/hello")
public class HelloServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.getWriter().write("hello springmvc!");
    }
}
```

若使用SpringMVC的方式：

1. 首先，需要将该类添加进IOC容器中，使用@Controller注解实现
2. @WebServlet注解使用@RequestMapping注解对应，用于映射地址，并将该注解所修饰的方法与地址信息放入到handlerMapping中
3. 直接在方法中返回响应体内容，并使用@ResponseBody表示返回的信息是响应体内容，不是视图。

```java
@Controller
public class HelloController {
    //handler -> springmvc/hello return "hello springmvc!"
    @RequestMapping("/springmvc/hello")//映射地址，到handlerMapping注册的注解
    @ResponseBody //表示返回的不是视图，不去视图解析器中找视图
    public String hello(){
        return "hello springmvc!";
    }
}
```





### 使用配置类的方式配置核心组件

我们这里使用配置类的方式，代替xml方式配置SpringMVC的核心组件。

创建com.atguigu.config包，该包下就是存放配置类的信息，在该包下，创建`MvcConfig`类。

该类相当于Spring中的xml配置信息。在Spring中的XML配置中，我们需要去开启注解扫描，并且创建Bean标签，同理，在该配置类中，我们也需要去开启注解驱动，并且创建Bean对象，将Bean对象加入到IOC容器中。

在之前的学习中，我们知道了需要将`HandlerMapping`处理器映射器、`HandlerAdapter`处理器适配器与`ViewResovler`视图解析器加入到IOC容器中才可生效。在该案例中，不需要使用ViewResolver，所以我们只需要将HandlerMapping与HandlerAdapter加入到IOC容器中。

HandlerMapping与HandlerAdapter都是接口，我们实际创建的对象是实现类`RequestMappingHandlerMapping`对象与实现类`RequestMappingHandlerAdapter`对象。

**使用`@Configuration`注解修饰类，表明该类是配置类。**

**使用`@ComponentScan`注解指明开启注解驱动时扫描的包。**

**使用`@Bean`注解实现在配置类中，将该注解修饰的方法返回的对象加入到IOC容器。**

```java
@Configuration
@ComponentScan("com.atguigu.*")
public class MvcConfig {
    @Bean
    public HandlerMapping handlerMapping(){
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public HandlerAdapter handlerAdapter(){
        return new RequestMappingHandlerAdapter();
    }
}
```





### 配置DispatcherServlet

原本，我们在Servlet中，是将Servlet的信息配置在web.xml中的，原本的web.xml是必不可少的，但是在SpringBoot中，web.xml是会被替换，直接使用配置类的方式取代web.xml。

在SpringMVC中，给我们提供了一个`AbstractAnnotationConfigDispatcherServletInitializer`抽象类，使用该接口来替代web.xml配置文件，我们只需要去继承这个抽象类，并且实现其中的抽象方法。

通过重写AbstractAnnotationConfigDispatcherServletInitializer抽象类中的方法，来指定SpringMVC的配置类，指定service、mapper的配置类，以及当前DispactherServlet的处理映射地址（与servlet中的url-pattern标签作用一样，都是为当前的Servlet指定处理映射地址）。

这样一来，内部就会帮助我们去实例化IOC容器，并且指定的url地址的请求就会被DispatcherServlet所接收，从而进行业务处理与响应。

所以，**`AbstractAnnotationConfigDispatcherServletInitializer`抽象类的作用是：**

1. 可以被web项目加载
2. 会去初始化IOC容器
3. 会去设置DisptcherServlet的地址，让Servlet生效

**总结：用于配置容器，包括web容器与IOC容器。**

我们通过继承该抽象类的方式，来替代web.xml配置文件，让指定的配置生效。

继承该抽象类，需要去实现其中的三个抽象方法，分别表示的含义是：

1. **`getRootConfigClasses()`**：返回的带有@Configuration注解的类来配置ContextLoaderListener，即**指定service / mapper层的配置类**。
2. **`getServletConfigClasses()`**：返回的带有@Configuration注解的类来配置DispatcherServlet，即，**指定SpringMVC的配置类**。
3. **`getServletMappings()`：设置DispatcherServlet的映射路径，一般情况下为`/`**，表示处理所有请求。

只要继承了AbstractAnnotationConfigDispatcherServletInitializer类，在项目启动时，该类中所配置的信息就会被加载，不需要我们使用手动创建ApplicationContext对象的方式，对IOC容器进行加载，同时配置其中的web容器信息也会被Tomcat加载。

在com.atguigu.config包下创建SpringMvcInit类：

```java
/**
 * @Description 可以被web项目加载，初始化IOC容器，会设置DispatcherServlet地址，让Servlet生效
 */
public class SpringMvcInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    /**
     * 指定service / mapper层的配置类
     * @return
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    /**
     * 制定springmvc的配置类
     * @return
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{MvcConfig.class};
    }

    /**
     * 设置DispatcherServlet的处理路径
     * 一般情况下为 / ，代表处理所有请求
     * @return
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
```

对于其中的getRootConfigClasses()方法，在后续的SSM整合中再说。该方法是用于指定service / mapper层的路径的。





### 启动Tomcat测试

使用Debug方式启动Tomcat，然后访问http://localhost:8080/springmvc/hello：

![image-20240606091503969](.\images\image-20240606091503969.png) 







## 5、web容器初始化原理

原本传统的、基于xml方式构建web应用程序，都需要在WEB-INF/web.xml中，注册Spring的DispatcherServlet。一般情况下都会这样做：

```xml
<servlet>
<servlet-name>dispatcher</servlet-name>
<servlet-class>
  org.springframework.web.servlet.DispatcherServlet
</servlet-class>
<init-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>/WEB-INF/spring/dispatcher-config.xml</param-value>
</init-param>
<load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
<servlet-name>dispatcher</servlet-name>
<url-pattern>/</url-pattern>
</servlet-mapping>
```

以上方式就是传统注册DispatcherServlet的方式。

现在推荐都是使用Java代码代替xml配置方式，来配置DispatcherServlet。

SpringMVC给我们提供了一个`WebApplicationInitializer`接口，来代替web.xml，该接口中有一个`onStartup()`方法，在Web服务器启动时，该方法会被执行。

这样一来，上面配置DispatcherServlet的方式，就可以改成使用实现该接口的方式来进行配置，并且在onStartUp()方法中，同样也可以去使用XmlWebApplicationContext，去加载指定的Spring配置类，从而创建IOC容器。那我们来看看，onStartUp()方法是具体如何实现的。

**WebApplicationInitializer类家族**

<img src=".\images\image-20240606095015773.png" alt="image-20240606095015773" style="zoom: 80%;" /> 

可以看到，我们实际继承的AbstractAnnotationConfigDispatcherServletInitializer就是WebApplicationInitalizer接口的实现类之一。

在AbstractAnnotationConfigDispatcherServletInitializer的父类`AbstractDispatcherServletInitilizer`中，最后实现了onStartUp()方法：

![image-20240606100725800](.\images\image-20240606100725800.png)

第一个是去调用父类中的onStartup()方法，第二个则是去注册DispatcherServlet。

我们来看看registerDispatcherServlet()方法：

```java
protected void registerDispatcherServlet(ServletContext servletContext) {
    String servletName = getServletName();
    Assert.state(StringUtils.hasLength(servletName), "getServletName() must not return null or empty");

    //创建IOC容器
    WebApplicationContext servletAppContext = createServletApplicationContext();
    Assert.state(servletAppContext != null, "createServletApplicationContext() must not return null");
    
	//创建DispatcherServlet对象
    FrameworkServlet dispatcherServlet = createDispatcherServlet(servletAppContext);
    Assert.state(dispatcherServlet != null, "createDispatcherServlet(WebApplicationContext) must not return null");
    dispatcherServlet.setContextInitializers(getServletApplicationContextInitializers());

    ServletRegistration.Dynamic registration = servletContext.addServlet(servletName, dispatcherServlet);
    if (registration == null) {
       throw new IllegalStateException("Failed to register servlet with name '" + servletName + "'. " +
             "Check if there is another servlet registered under the same name.");
    }

    registration.setLoadOnStartup(1);
    registration.addMapping(getServletMappings());
    registration.setAsyncSupported(isAsyncSupported());

    Filter[] filters = getServletFilters();
    if (!ObjectUtils.isEmpty(filters)) {
       for (Filter filter : filters) {
          registerServletFilter(servletContext, filter);
       }
    }

    customizeRegistration(registration);
}
```

其中，`createServletApplicationContext()`方法是去创建IOC容器的，我们来看看该方法的实现，该方法就是在AbstractAnnotationConfigDispatcherServletInitializer类中进行实现的：

![image-20240606101707167](.\images\image-20240606101707167.png) 

可以看到，在createServletApplicationContext()方法中，调用的getServletConfigClasses()方法，来创建IOC容器，而getServletConfigClasses()就是我们自己所重写的方法，在方法中添加类的Class对象类型，从而去指定SpringMVC的配置类，加载指定的包，创建IOC容器。

在registerDispatcherServlet()方法中，通过getServletMappings()方法来给DispatcherServlet指定映射路径，这也是我们进行重写的方法。

<img src=".\images\image-20240606102004121.png" alt="image-20240606102004121" style="zoom:80%;" /> 

这样一来，在启动Tomcat容器时，就会去调用WebApplicationInitializer中的onStartup()方法，在onStartup()方法中，会去调用我们所重写的getRootConfigClasses()、getServletConfigClasses()以及getServletMappings()方法，从而创建IOC容器，将HandlerMapping以及HandlerAdapter假如到IOC容器中，并且指定DispatcherServlet的映射路径。





---

# 二、SpringMVC接收数据

**handler方法可以设置的形参类型：**

| 控制器方法参数           | 说明                                                         |
| ------------------------ | ------------------------------------------------------------ |
| HttpServletRequest       | 请求对象                                                     |
| HttpServletResponse      | 响应对象                                                     |
| HttpSession              | 会话对象                                                     |
| InputStream \| Reader    | 用于访问由Servlet API公开的原始请求正文                      |
| OutputStream \| Writer   | 用于访问由Servlet API公开的原始响应正文                      |
| @PathVariable            | 接收路径参数注解                                             |
| @RequestParam            | 用于访问Servlet请求参数，包括多部分文件。参数值将转换为声明的方法参数类型。 |
| @RequestHeader           | 用于访问请求头。请求头值将转换为声明的方法参数类型。         |
| @CookieValue             | 用于访问Cookie，Cookie值将转换为声明的方法参数类型。         |
| @RequestBody             | 用于访问HTTP请求正文。正文内容通过使用HttpMessageConverter实现转换为声明的方法参数类型。 |
| Map \| Model \| ModelMap | 共享域对象，并在视图呈现过程中向模板公开。                   |
| Errors \| BindingResult  | 验证和数据绑定中的错误信息获取对象。                         |



## 1、访问路径设置

路径设置通过**`@RequestMapping`**注解来实现。

该注解的作用就类似于Servlet中的`@WebServlet`，表示与路径建立映射关系。但是，@WebServlet必须使用/开头，而@RequestMapping既可以使用/开头，也可以不使用/开头，都会在路径前面自动加上一个/。

同时，@RequestMapping注解还表示将其所修饰的方法加入到HandlerMappingg中，以供DispatcherSerlvet调用。

### 两种路径匹配

在@RequestMapping中，路径的写法有两种：

1. **精确匹配**

在@ReuqestMapping注解中指定URL地址时，不使用任何通配符，按照请求地址进行精确匹配。

案例：

```java
@Controller
public class UserController {
    @RequestMapping("/user/login")
    @ResponseBody
    public String login(){
        return "login success!";
    }


    //可以在@RequestMapping中，给handler方法设置多个映射路径
    @RequestMapping({"/user/register1", "/user/register2"})
    @ResponseBody
    public String register(){
        return "register success!";
    }
}
```

@RequestMapping中的value属性用于表示当前handler方法所对应的映射路径，该属性是String[]类型的，说明可以为当前方法映射多个路径。（与Servlet一样）

![image-20240606113211024](.\images\image-20240606113211024.png) 

也可以使用path属性来代替value属性，表示映射路径（也和Servlet中的@WebServlet注解设置一样）



2. **模糊匹配**

在@RequestMapping注解中指定URL地址时，通过使用通配符*来匹配多个类似的地址。

单层匹配和多层匹配：

* **`/*`**，表示模糊匹配URL地址单层任意字符串。
* **`/**`**，表示模糊匹配URL地址多层任意字符串。

所谓的一层或多层匹配是指一个URL地址字符串被 / 划分出来的各个层次。

比如：/a/*，则可以匹配/a/a，但是不能匹配/a/a/a

/a/**则是可以匹配/a/a/a。

案例：

```java
@Controller
public class ProductController {

    /**
     *  路径设置为 /product/*  
     *    /* 为单层任意字符串  /product/a  /product/aaa 可以访问此handler  
     *    /product/a/a 不可以
     *  路径设置为 /product/** 
     *   /** 为任意层任意字符串  /product/a  /product/aaa 可以访问此handler  
     *   /product/a/a 也可以访问
     */
    @RequestMapping("/product/*")
    @ResponseBody
    public String show(){
        System.out.println("ProductController.show");
        return "product show!";
    }
}
```





### 类和方法级别区别

`@RequestMapping`注解可以修饰在类上、方法上。它们之间的区别如下：

* 设置到类级别：@RequestMapping注解可用于设置在控制器上，用于映射整个控制器的通用请求路径。这样，如果控制器中的多个方法都需要映射同一请求路径，就不需要在每个方法上都添加映射路径。
* 设置到方法级别：@RequestMapping注解可以单独设置在控制器方法上，用于更细粒度地映射请求路径和处理方法。当多个方法处理同一路径的不同操作时，可以使用方法级别的@RequestMapping注解进行更细粒度的映射。

要进行访问时，会先去找修饰类的@RequestMapping，然后再去找方法上的@ReuqestMapping，二者联合起来的路径，才是handler方法的真正映射路径。

即，**访问路径变为了：`类地址 + 方法地址`**

那么，对于同一个类中的不同handler方法而言，它们肯定是处理同一业务的，对于同一业务来说，我们一般会将这些handler方法放在同一映射路径下，此时，我们就可以在类上去使用@RequestMapping指定该类下的所有handler方法，放在哪个路径下，然后在具体的方法中，再设置该路径下的路径。

案例：

```java
@Controller
public class UserController {
    @RequestMapping("/user/login")
    @ResponseBody
    public String login(){
        return "login success!";
    }


    //可以在@RequestMapping中，给handler方法设置多个映射路径
    @RequestMapping({"/user/register1", "/user/register2"})
    @ResponseBody
    public String register(){
        return "register success!";
    }
    
}
```

对于上例来说，两个方法login()与register()都是user业务下的，所以，handler映射路径中的/user可以修改成放在类中：

```java
@Controller
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/login")
    @ResponseBody
    public String login(){
        return "login success!";
    }


    //可以在@RequestMapping中，给handler方法设置多个映射路径
    @RequestMapping({"/register1", "/register2"})
    @ResponseBody
    public String register(){
        return "register success!";
    }

}
```

实际还是通过/user/login与/user/register去访问，只不过在类中声明/user，表明当前类都是属于user业务。





### 请求方式的限制

客户端是使用Http协议，来访问服务器的，在Http协议中，有不同的请求方式，包括GET | POST | PUT | DELETE等等。

对于不同的请求，我们有时候会要求使用指定的请求方式，比如登录操作，我们想让其使用POST请求发送，这样的话，会比较安全一点。

**在默认情况下，即在没有指定请求方式的情况下，任何请求方式都可以访问**。

使用@RequestMapping中的**`method`属性**来指定请求的方式：

![image-20240606124953352](.\images\image-20240606124953352.png) 

该属性是一个RequestMethod类型的数组

RequestMethod是一个枚举类型，在其中定义了八种请求方式，分别是`GET、HEAD、POST、PUT、PATCH、DELETE、OPTIONS、TRACE`：

![image-20240606125023469](.\images\image-20240606125023469.png) 

例如，我要将login()方法设置为只能POST方式进行访问，将register()方法设置为只能GET或POST方式进行访问：

```java
@Controller
@RequestMapping("/user")
public class UserController {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(){
        return "login success!";
    }

    //可以在@RequestMapping中，给handler方法设置多个映射路径
    @RequestMapping(value = {"/register1", "/register2"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String register(){
        return "register success!";
    }
}
```

method属性是一个数组类型，所以可以给某一个方法设置使用指定的多个访问方式进行访问。

> **若不符合指定的访问方式进行访问，比如我设定成只能POST方式访问的路径，使用了GET方式进行访问，此时就会报`405异常`。**





### 其他表示映射关系的注解

@RequestMapping的HTTP方法特定快捷方式变体：

* `@GetMapping`
* `@PostMapping`
* `@PutMapping`
* `@DeleteMapping`
* `@PatchMapping`

@GetMapping，就相当于@RequestMapping中，设置了method属性为RequestMethod.GET。

同理，@PostMapping，就相当于@RequestMapping中，设置了method属性为RequestMethod.POST。

```java
@RequestMapping(value = "/login", method = RequestMethod.GET)
//等于
@GetMapping("/login")
```

**注意**：上述的这些注解只能使用在方法上，不能使用在类上。因为在类上，不能设定请求方式，只能指定不同的handler方法，使用不同的请求方式。





## 2、请求参数的接收（:star:）

### 对于接收参数类型的说明

在handler方法中，使用方法形参来接收请求的参数，方法形参类型不同，会因传入的请求参数不同而达到不同的效果。

1. **基本数据类型**

这里的基本数据类型是除了boolean以外的基本数据类型。

行为解释：

* 若不传或传入的参数名和形参变量名不匹配，则直接抛出异常，这些基本数据类型不能初始化为null。
* 若传入正确的参数名和值，则初始化为对应的数值
* 若传入正确的参数名，但是值不匹配，则抛出异常MethodArgumentTypeMismatchException。

上面的行为是什么意思？我们来看个例子：

```java
@Controller
@RequestMapping("/param")
public class ParamController {
    
    @RequestMapping("/test")
    public void test(int id, String name){
        System.out.println(id + "-->" + name);
    }
    
}
```

此时发送一个请求：

http://localhost:8080/param/test?name=cheng

此时的执行结果：

<img src=".\images\image-20240607085052678.png" alt="image-20240607085052678" style="zoom:67%;" /> 

为什么会出现这种情况？

因为对于int或者其他基本数据类型的handler方法形参，必须有对应请求参数接收。



2. **引用数据类型**

对于引用数据类型的方法形参而言，若请求参数中没有对应的请求参数，则默认初始化为null，使用null来给这些方法形参赋值。若方法形参类型为boolean类型，则默认初始化为false。（如果使用了@RequestParam或者@RequestBody注解，则默认必须传入对应的参数名）

行为解释：

* 若不传入或传入的参数名和形参变量名不匹配，则初始化为null。
* 若传入正确的参数名和值，则初始化为对应的值。
* 若传入正确的参数名，但参数值不匹配，则抛出异常：MethodArgumentTypeMismatchException。

上述是什么情况，看一个例子：

```java
@Controller
@RequestMapping("/param")
public class ParamController {
   
    @RequestMapping("/test")
    @ResponseBody
    public void test(Integer id, String name){
        System.out.println(id + "-->" + name);
    }
}
```

发送一个请求：

http://localhost:8080/param/test

此时控制台打印的信息：

![image-20240607090341109](.\images\image-20240607090341109.png) 

由于id与name此时都是引用数据类型，当传入参数中没有与方法形参对应的参数名时，此时会使用null来进行初始化赋值。

当然，如果这些引用数据类型的参数使用了@RequestParam或者@RequestBody注解修饰，则在默认情况下，必须传入与之匹配的请求参数名。

由上述的结果可知，如果用基本类型来接收参数，若不传入或者传入的参数名和handler方法的形参变量名不匹配会直接抛出异常。

> 因此，我们尽量**`使用包装类来代替基本数据类型接收参数`**。





---

### param和json参数比较

前端传输的参数一般有两种形式，分别是`param`与`json`类型。两种类型参数的格式分别如下所示：

* **`param`**：key=vlaue&key=value

* **`json`**：{"key":"value", "key":"vlaue"}

下面对这两种参数类型进行区别和对比：

1. 参数编码：

   param类型的参数会被编码为ASCII码。例如，假设name=john doe，则会编码为name=john%20doe。而JSON类型的参数会被编码为UTF-8。

2. 参数顺序：

   param类型的参数没有顺序限制。但是JSON类型的参数是有序的。JSON采用键值对的形式进行传递，其中键值对是有序排列的。

3. 数据类型：

   param类型的参数仅支持字符串类型、数值类型和布尔类型等简单数据类型。而JSON类型的参数则支持更加复杂的数据类型，如数组、对象等。

4. 嵌套性：

   param类型的参数不支持嵌套。但是JSON类型的参数可以嵌套，可以传递更为复杂的数据结构。

5. 可读性：

   param类型的参数格式比JSOM类型的参数更加简答、易读。但是，JSON格式在传递嵌套数据结构时更加清晰易懂。



**在实际开发中，对于param类型的参数，使用GET方式的请求；对于JSON类型的参数，使用POST方式的去请求。**因为GET方式的请求只能将请求放在url中，所以适合简单的参数；而POST方式的请求可以将请求参数放在请求体中，适合比较复杂的参数。

POST方式的请求也可以发送`param`类型的参数，只不过在一般情况下，像这种比较简单的请求参数都是使用GET的请求方式。

即：

> 若请求参数是`JSON`类型，则需要使用`POST`方式请求；
>
> 若请求参数是`param`类型，则使用`GET`或`POST`均可，一般情况下使用GET，因为GET请求效率高一点。







----

### 2.1、使用param类型接收参数

#### 直接接收

**当handler方法的形参`参数名和类型`与传递过来的param参数的参数名相同时，即可自动接收**。

这是通过HandlerAdapter来实现的。

例如，前端请求：http://localhost:8080/param/value?name=cheng&age=18

此时，使用了param类型的请求参数，两个参数名分别是name与age，我们可以在Handler方法中，声明相同名称的形参，使用该形参自动接收请求参数。

**声明的形参要求**：

* `形参名 = 请求参数名`
* `类型与请求参数相同`

那么，将上述的请求参数使用直接接收的方式声明的方法为：

```java
@Controller
@RequestMapping("/param")
public class ParamController{
    
    //由于是将请求参数放在URL地址中，所以是GET请求方式
    //将param参数放在请求体中，则是POST请求方式
    @GetMapping("/value")
    @ResponseBody
    public String setupForm(String name, int age){
        System.out.println("name = " + name + ", age = " + age);
        return name + age;
    }
}
```

此时，当我们去访问http://localhost:8080/param/value?name=cheng&age=18地址时，setupForm()方法中的name与age形参就能够接收到对应的参数值：

<img src=".\images\image-20240606160155818.png" alt="image-20240606160155818" style="zoom:67%;" /> 













---

#### 使用@RequestParam注解（推荐）

当请求中的参数与handler方法的参数名不同时，可以使用`@RequestParam`注解，指定方法参数绑定的请求参数名。

**@RequestParam注解使用场景：**

* `指定绑定的请求参数名`
* `要求请求参数必须传递`
* `为请求参数提供默认值`

使用@RequestParam注解，能够实现让方法形参接收指定的请求参数，以及设置方法形参必须接收请求参数，以及方法形参若未接收请求参数时的默认值。

**用法：**

* @RequestParam中`value`属性用于指定请求参数名，如果形参名和请求参数名一致，则可以省略。

* `required`属性值用于表示前端是否必须传递此参数，默认是true，即必须传递，不传则报400异常。

* `defaultValue`属性表示默认值，当required属性值为false，且未传入对应的属性值时，使用defaultValue属性值来给形参默认初始化。



**案例：**

请求路径为/param/data1?account=root&page=1，要求account请求参数使用username形参接收，account参数必须传递，page参数可以不传递，如果不传递默认值是1。则handler方法为：

```java
@Controller
@RequestMapping("/param")
public class ParamController {

    //注解指定
    // /param/data1?account=root&page=1
    @RequestMapping("/data1")
    @ResponseBody
    public void data1(@RequestParam("account") String username, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page){
        System.out.println(username + "-->" + page);
    }
}
```



**@RequestParam中的required属性说明：**

当形参没有使用@RequestParam注解修饰，若形参是基本数据类型，则该形参必须在请求参数中有对应的值，否则会报错；若形参是引用数据类型，则当请求参数中没有对应的值时，使用null作为其初始值，不会报错。

但是当形参使用了@RequestParam注解，则其中的`required`属性默认是true，表示形参必须被赋值，此时就算形参是引用数据类型，在请求参数中也要有值对应。







---

#### 特殊场景

##### 1）使用集合接收同名参数

当请求参数中有多个同名参数时，例如：

http://localhost:8080/param/mul?hbs=吃&hbs=喝

此时，我们可以使用集合类型形参进行接收。

当然，这个集合类型形参名若与请求参数名不一致时，使用@RequestParam注解来指定形参。

案例：

```java
@Controller
@RequestMapping("/param")
public class ParamController {
    @RequestMapping("/data2")
    @ResponseBody
    public void data2(List<String> hbs){
        System.out.println("hbs = " + hbs);
    }
}
```







---

##### 2）实体类接收

SpringMVC是Spring框架提供的Web框架，它允许开发者使用实体对象来接收HTTP请求中的参数。通过这种方式，可以在方法内部直接使用对象的属性来访问请求参数，而不需要每个参数都写一遍。

其实和Mybatis中，查询方法的接收实体类相似。对于查询的方法，如果使用实体类进行接收，则要求实体类中的属性名与查询结果集中的列名一致，这样才能产生映射关系。

同理在SpringMVC的handler方法形参中，也是一样的，要求请求参数名与实体类中属性名保持一致，这样才能够产生映射。

对于这种前端传给后端的实体类，我们一般称之为**DTO**。



**案例：**

URL路径：

http://localhost:8080/param/user?name=cheng&age=24

创建一个User实体类接收，要求实体类中的属性名请求参数名一一对应，这样才能够实现映射。

```java
public class User {
    private String name;
    private Integer age;

    //省略get()、set()方法
}
```

handler方法：

```java
@Controller
@RequestMapping("/param")
public class ParamController {
 
    @RequestMapping("/user")
    @ResponseBody
    public String addUser(User user){
        System.out.println(user);
        return "success";
    }
}
```

此时执行后的打印结果：

![image-20240607104826871](.\images\image-20240607104826871.png) 

在上例中，虽然使用的是实体类进行接收数据，但是请求方式依旧是GET，因为请求参数类型是param类型，并且放在URL地址中。只不过在handler方法的形参中，使用了实体类进行接收，类中的属性名与参数名一一对应。









---

### 2.2、使用JSON类型接收参数

前段传递JSON数据时，Spring MVC框架可以使用`@RequestBody`注解来将JSON数据转换为Java对象。

**`@RequestBody`**注解表示当前方法参数的值应该从请求体中获取，并且需要指定value属性来指示请求体应该映射到哪个参数上。

@RequestBody注解中只有一个required属性，表示是否必填：

<img src=".\images\image-20240607113411476.png" alt="image-20240607113411476" style="zoom:67%;" /> 

因为对于@RequestBody注解来说，其表示的含义是将请求体数据全部映射成一个实体类，所以在该注解中，就不需要设置value属性来指明当前形参所对应的请求参数，因为整个请求体都是请求参数。



其使用方式和示例代码如下：

1. 前端发送JSON数据的示例：

   ```json
   {
     "name": "张三",
     "age": 18,
     "gender": "男"
   }
   ```

2. 定义一个用于接收JSON数据的Java类，例如：

   ```java
   public class Person {
     private String name;
     private Integer age;
     private Character gender;
     // getter 和 setter 略
   }
   ```

3. 在控制器中，创建一个Handler方法，使用@RequestBody注解来表示接收JSON数据，并将其转换为Java对象，例如：

   ```java
   @Controller
   @RequestMapping("/param")
   public class ParamController {
   
       @RequestMapping(value = "/person", method = RequestMethod.POST)
       @ResponseBody
       public Object addPerson(@RequestBody Person person){
           System.out.println(person);
           return person;
       }
   }
   ```

   在上述代码中，@RequestBody注解将请求体中的JSON数据映射到注解所修饰的形参person上，并将其作为一个对象来传递给addPerson()方法进行处理。

4. 测试

   因为在浏览器中，无法通过在URL地址上进行修改来发送带有请求体的POST请求。

   这里我们使用的是Postman工具，Postman就是用来模拟发送请求的工具。

   <img src=".\images\image-20240607114738621.png" alt="image-20240607114738621" style="zoom: 50%;" /> 

   选择POST方式请求，输入地址信息，选择raw中的JSON，表示发送的是JSON类型的请求体，在输入框中输入要发送的JSON数据。

   点击Send，发送请求，此时的执行结果为：

   <img src=".\images\image-20240607114859459.png" alt="image-20240607114859459" style="zoom:50%;" /> 

   即，发生了415错误，表示不支持JSON类型格式的数据。

   

#### 问题解决方案

原因：Java原生的API，只支持路径和param类型的请求参数，对于JSON类型的请求参数则不支持。

在Servlet中学习的request.getParameter()方法获取请求参数，这是param类型的，不支持JSON。因为JSON本身就是前端的数据格式。

**解决方案：**

1. 导入JSON处理的依赖
2. 给handlerAdapter配置JSON转化器

**解决过程：**

1. 导入jackson的依赖

```xml
<dependency>
   <groupId>com.fasterxml.jackson.core</groupId>
   <artifactId>jackson-databind</artifactId>
   <version>2.15.2</version>
</dependency>
```

2. 给SpringMVC的handlerAdapter配置JSON转化器

在SpringMVC的配置类上，添加一个@EnableWebMvc，该注解就相当于给handlerAdapter配置JSON转化器

```java
@Configuration
@ComponentScan("com.atguigu.controller")
@EnableWebMvc
public class MvcConfig {
    @Bean
    public HandlerMapping handlerMapping(){
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public HandlerAdapter handlerAdapter(){
        return new RequestMappingHandlerAdapter();
    }
}
```

并且在添加了@EnableWebMvc注解后，都不需要我们手动地去将HandlerMapping与HandlerAdapter对象添加到IOC容器中，它自己会去创建这两个对象并添加进IOC容器。

也就是说，**`@EnableWebMvc`注解的作用是：**

1. 创建HandlerMapping与HandlerAdapter对象，并将该bean对象添加进IOC容器中；
2. 为HandlerAdapter对象创建JSON处理器，让我们能够去接收JSON类型的请求体信息。

这样一来，在SpringMVC配置类中，就不需要再去手动地创建HandlerMapping与HandlerAdapter对象并添加进IOC容器：

```java
@Configuration
@ComponentScan("com.atguigu.controller")
@EnableWebMvc
public class MvcConfig {
}
```



此时，我们再去发送JSON类型格式的数据：

<img src=".\images\image-20240607124116681.png" alt="image-20240607124116681" style="zoom:67%;" /> 

此时的SpringMVC中，就能够正常地使用实体类对象进行接收。





---

### 2.3、使用路径接收参数

**说明**

原本之前学习的参数，都是放在请求体或者URL路径上，比如使用param方式传参：`/user/login?account=root&password=123456`

而路径传参，则并不是上面param的格式，而是将参数放在URL路径中的一层结构中，类似于将参数值作为目录结构的形式进行传递，如将上面的路径改成使用路径传参：`/user/login/root/123456`

这种方式，相对于使用param参数传参来说，路径更短一点，效率也会更高一点。

路径传递参数是一种在URL路径中传递参数的方式。在RESTful的Web应用程序中，经常使用路径传递参数来表示资源的唯一标识符或更复杂的表示方式。而SpringMVC框架提供了@PathVariable注解来处理路径传递参数。**`@PathVariable`**注解允许将URL中的占位符映射到控制器方法中的参数。

例如，如果我们想将/user/{id}路径下的{id}映射到控制器方法的一个参数中，则可以使用@PathVariable注解来实现。



**案例：**

```java
@Controller
@RequestMapping("/path")
public class PathController {
    //路径为：/path/账号/密码
    //使用{key}的形式，则我们可以通过使用@PathVariable注解来在方法的形参中使用
    @RequestMapping("/{account}/{password}")
    @ResponseBody
    public String login(@PathVariable("account") String account, @PathVariable("password") String password){
        return "账号：" + account + "，密码：" + password;
    }
}
```

Postman测试：

<img src=".\images\image-20240607150232835.png" alt="image-20240607150232835" style="zoom:67%;" /> 

成功获取数据。

上面的???实际上是中文乱码，原因在于当前的服务器所使用编码格式是UTF-8，那么服务端返回给客户端响应体就是使用UTF-8进行编码的，但是在客户端接收响应数据时，是使用客户端所在系统的默认字符集进行解码。当前在中文环境下，也就是使用GBK字符集进行解码。当响应体中包含中文字符时，由于字符集的不同，自然会出现乱码。

在Servlet中，我们是去设置response响应体的字符编码方式，来保证客户端使用UTF-8字符集的方式解码：

```java
response.setContentType("text/html;charset=UTF-8");
```

而在SpringMVC中，解决乱码问题的方式在后面阐述。



**注意**：如果在控制器方法中，未使用@PathVariable注解，将URL中占位符的值映射到方法形参中的话，则形参默认是使用param类型的请求参数进行映射，而不是路径类型参数。也就是说，使用路径类型参数映射，必须要使用@`PathVariable`注解。





---

## 3、接收Cookie数据

可以使用**`@CookieValue`**注释将HTTP Cookie的值绑定到控制器方法的形参中，从而获取HTTP中的cookie数据。

那么，我们要去接收Cookie，首先浏览器中得有Cookie数据。

在SpringMVC的handler方法中，也可以去传入Servlet API中的service()方法传入的HttpServletRequest和HttpServletResponse对象，从而去获取请求信息并设置响应信息。

我们可以使用`HttpServletResponse`对象中的`addCookie()`方法，手动给浏览器添加Cookie数据。

```java
@RequestMapping("/save")
@ResponseBody
public String saveCookie(HttpServletResponse response){
    Cookie cookie = new Cookie("JSESSIONID","415A4AC178C59DACE0B2C9CA727CDD84");
    response.addCookie(cookie);
    return "success";
}
```

在Postman中访问该路径，就会将该创建的Cookie信息存入到host本机中：

<img src=".\images\image-20240607154907209.png" alt="image-20240607154907209" style="zoom: 67%;" /> 

查看Cookie信息：

<img src=".\images\image-20240607154946092.png" alt="image-20240607154946092" style="zoom:67%;" />

此时，我们就可以去获取Cookie的信息：

```java
@RequestMapping("/get")
@ResponseBody
public String getCookie(@CookieValue("JSESSIONID") String cookie){
    System.out.println(cookie);
    return "success";
}
```

将Cookie信息赋值给handler方法中的cookie对象中。

访问该路径，控制台的打印结果：

![image-20240607155101706](.\images\image-20240607155101706.png) 





---

## 4、接收请求头数据

使用**`@RequestHeader`**注解，将指定的请求头属性绑定到控制器方法的形参中。

**主要请求头内容**

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

下面的示例代码获取`Accept-Encoding`和`Accept-Language`请求头的值：

```java
@Controller
@RequestMapping("/header")
public class HeaderController {
    
    @RequestMapping("/demo")
    @ResponseBody
    public String demo(@RequestHeader("Accept-Encoding") String encoding,
                       @RequestHeader("Accept-Language") String language){
        return "请求头Accept-Encoding值：" + encoding + "，Accept-Language值：" + language;
    } 
}
```





---

## 5、接收原生Api对象

**handler方法可以设置的形参类型：**

| 控制器方法参数           | 说明                                                         |
| ------------------------ | ------------------------------------------------------------ |
| HttpServletRequest       | 请求对象                                                     |
| HttpServletResponse      | 响应对象                                                     |
| HttpSession              | 会话对象                                                     |
| InputStream \| Reader    | 用于访问由Servlet API公开的原始请求正文                      |
| OutputStream \| Writer   | 用于访问由Servlet API公开的原始响应正文                      |
| @PathVariable            | 接收路径参数注解                                             |
| @RequestParam            | 用于访问Servlet请求参数，包括多部分文件。参数值将转换为声明的方法参数类型。 |
| @RequestHeader           | 用于访问请求头。请求头值将转换为声明的方法参数类型。         |
| @CookieValue             | 用于访问Cookie，Cookie值将转换为声明的方法参数类型。         |
| @RequestBody             | 用于访问HTTP请求正文。正文内容通过使用HttpMessageConverter实现转换为声明的方法参数类型。 |
| Map \| Model \| ModelMap | 共享域对象，并在视图呈现过程中向模板公开。                   |
| Errors \| BindingResult  | 验证和数据绑定中的错误信息获取对象。                         |

获取原生对象示例：

```java
/**
 * 如果想要获取请求或者响应对象,或者会话等,可以直接在形参列表传入,并且不分先后顺序!
 * 注意: 接收原生对象,并不影响参数接收!
 */
@GetMapping("api")
@ResponseBody
public String api(HttpSession session , HttpServletRequest request, HttpServletResponse response){
    String method = request.getMethod();
    System.out.println("method = " + method);
    return "api";
}
```

我们之前学习过ServletContext对象，这个对象是Servlet获取配置信息的对象，同时也是最大的共享域——应用域对象，作用范围是整个容器，在容器关闭后销毁。

之前，我们所学习的获取ServletContext对象的方式是通过request.getServletContext()方式获得。

**在整合Spring容器之后，容器启动时，`ServletContext`对象会被装入到IOC容器中，所以，此时我们可以通过@Autowired注解的方式，自动注入ServletContext对象属性到我们所需使用的类中。**

例如：

```java
@Controller
@RequestMapping("/api")
public class HeaderController {
    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value = "/demo")
    @ResponseBody
    public String demo() {
        //设置域对象
        servletContext.setAttribute("username","123456");

        //获取当前项目部署后的绝对路径
        return servletContext.getRealPath("/");
    }
}
```



---

## 6、操作共享域对象

当我们去访问一个Servlet，然后由这个Servlet使用请求转发或者响应重定向的方式跳转到另一个Servlet或者页面时，我们要想在这之间进行数据的传递，就可以使用共享域空间来存储数据。（在SpringMVC中通过**handler方法的返回值跳转页面**，实际上是**`请求转发`**的形式进行跳转）

在发送方，先将数据存储到共享空间，然后在接收方，取出共享域空间的数据，这样就实现了数据的传递。

<img src=".\images\image-20240608103348784.png" alt="image-20240608103348784" style="zoom:50%;" /> 

在JavaWeb中，共享域指的是在Servlet中存储数据，以便在同一Web应用程序的多个组件中进行共享和访问。常见的共享域：**`request`请求域**、**`session`会话域**与**`application`应用域**。

1. `request`请求域：`HttpServletRequest`对象可以在同一请求的多个处理器方法之间共享数据。比如，可以将请求的参数和属性存储在HttpServletRequest中，让处理器方法之间可以访问这些数据。
1. `session`会话域：`HttpSession`对象可以在同一用户发出的多个请求之间共享数据，但只能在同一个会话中使用。比如，可以将用户登录状态保存在HttpSession中，让用户在多个页面间保持登录状态。

3. `application`应用域：`ServletContext`对象可以在整个Web应用程序中共享数据，是最大的共享域。一般可以用于保存整个Web应用程序的全局配置信息，以及所有用户都共享的数据。在ServletContext中保存的数据是线程安全的。

### Request请求域

在同一个请求中，请求域中的数据是共享的。所以，对于同一个请求中的不同控制器方法之间，数据是共享的。

当控制器方法使用了请求转发的方式跳转到其他控制器方法中时，在原方法中所设置的请求域数据，就可以在另一个方法中获取。

**实现请求域数据共享的几种方式**

1. **使用Servlet原生API：`HttpServletRequest`对象实现（推荐）**

使用Servlet原生API：HttpServletRequest对象来实现请求域中数据的共享，既能够去将数据存储到请求域中，也可以去获取请求域中已有的数据信息。这种方式是比较推荐的。

在整合了Spring框架之后，Spring也会将HttpServletRequest对象放入到IOC容器中，我们可以在类中使用自动注入的方式，注入HttpServletRequest对象，这样的话就不需要将该对象声明在handler方法的形参中获取。

**案例：**

```java
@Controller
public class RequestController{

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 方式一：使用Servlet原生API
     * @return
     */
    @RequestMapping("/request/servletApi/demo1")
    public String requestDemo1(){
        httpServletRequest.setAttribute("username","cheng");
        return "forward:/request/servletApi/demo2";
    }

    @RequestMapping("/request/servletApi/demo2")
    @ResponseBody
    public String requestDemo2(){
        String username = (String)httpServletRequest.getAttribute("username");
        return "username ==> " + username;
    }
}
```

在上例中，当我们去访问http://localhost:8080/request/selectApi/demo1路径时，就会将username=cheng数据存入到请求域中，之后，就会通过请求转发的方式，跳转到http://localhost:8080/request/selectApi/demo2中，在该handler方法中获取请求域中的数据，并且作为响应体返回。





2. **使用ModelAndView/Model/ModelMap或Map对象**

将上述四种类型作为handler方法的参数，能够去使用这些参数设置请求域的数据，但是不能去获取请求域中已有的数据，只能通过HttpServletRequest对象去获取。

案例：

```java
//model类型
@RequestMapping("/attr/request/model")
@ResponseBody
public String testAttrRequestModel(Model model) {
    model.addAttribute("Model","[model value]");
    return "target";
}


//modelMap类型
@RequestMapping("/attr/request/model/map")
@ResponseBody
public String testAttrRequestModelMap(ModelMap modelMap) { 
    modelMap.addAttribute("ModelMap","[modelMap value]");
    return "target";
}


//map类型
@RequestMapping("/attr/request/map")
@ResponseBody
public String testAttrRequestMap(Map<String, Object> map) {
    map.put("Map", "[map value]");
    return "target";
}


//modelAndView类型
@RequestMapping("/attr/request/mav")
public ModelAndView testAttrByModelAndView() {
    
    // 1.创建ModelAndView对象
    ModelAndView modelAndView = new ModelAndView();
    // 2.存入模型数据
    modelAndView.addObject("requestScopeMessageMAV", "i am very happy[mav]");
    // 3.设置视图名称
    modelAndView.setViewName("target");
    //实现跳转
    return modelAndView;
}
```

以上这些方式，仅仅只能去设置Request域中的数据，无法通过getAttribute()方法去获取，如果上述的handler使用请求转发的方式跳转到其他handler方法中，在跳转后的handler方法中若想获取原Request域中的数据，则必须通过HttpServletRequest对象获取。

当然，如果跳转到JSP视图页面中，则可以通过thymeleaf来获取。



---

### Session会话域

会话域可以实现在同一个会话中，多次请求之间实现数据的共享。

在SpringMVC的控制器方法中，可以`传入HttpSession原生Servlet的API`，从而去实现Session会话域数据的共享。

例如：

```java
@RequestMapping("/session/demo1")
@ResponseBody
public String sessionDemo1(HttpSession session){
    session.setAttribute("username", "cheng");
    return "success";
}

@RequestMapping("/session/demo2")
@ResponseBody
public String sessionDemo2(HttpSession session){
    Object username = session.getAttribute("username");
    return "username => " + username;
}
```

这样一来，在同一个会话中，第一个Servlet与第二个Servlet之间能够使用Session共享数据。

在整合了Spring框架以后，HttpSession对象会加入到IOC容器中，让IOC容器给我们创建。当我们需要去使用Session会话域时，可以使用`@Autowired`注解的方式，直接将该对象作为要使用类中的属性注入到类中，直接调用即可，这种方式更加推荐。

例如：

```java
@Controller
public class RequestController{
    @Autowired
    private HttpSession httpSession;


    @RequestMapping("/session/demo1")
    @ResponseBody
    public String sessionDemo1(){
        httpSession.setAttribute("username", "cheng");
        return "success";
    }

    @RequestMapping("/session/demo2")
    @ResponseBody
    public String sessionDemo2(){
        Object username = httpSession.getAttribute("username");
        return "username => " + username;
    }
}
```

使用@Autowired注解，自动注入HttpSession对象到类中，就不需要在handler方法的形参中，获取Servlet原生API。这种方式更加清晰方便。







---

### Application应用域

Application应用域也就是**`ServletContext`**对象。

在Servlet中，我们学习过，通过HttpServletRequest对象来获取ServletContext对象，从而去获取Servlet的配置信息以及应用域对象。

在整合了Spring与SpringMVC之后，Spring会去帮助我们将ServletContext对象存入IOC容器中，让IOC容器给我们创建，我们在使用时，直接使用自动注入依赖`@Autowired`的方式，将ServletContext对象注入到对应的类中，从而实现应用域数据的共享。

例如：

```java
@Controller
@RequestMapping("/api")
public class HeaderController {
    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value = "/demo")
    @ResponseBody
    public String demo() {
        //设置域对象
        servletContext.setAttribute("username","123456");

        //获取当前项目部署后的绝对路径
        return servletContext.getRealPath("/");
    }
}
```

这样一来，只要容器未关闭，加入到ServletContext共享域中的数据就不会被销毁，其他人都可以获取。



---

## 总结

1. 路径设置

   * @RequestMapping(value="地址", method="请求方式") 类 | 方法
   * @GetMapping | @PostMapping 方法

2. 接收参数【重点】

   * param
     * 直接接收 handler(类型 形参名) 形参名 = 请求参数名
     * 注解指定 handler(@RequestParam(name="请求参数名", required=true, defaultValue="默认值"))
     * 一名多值 handler(@RequestParam List key)
     * 实体接收 handler(实体 对象) 对象的属性名 = 请求参数名
   * 路径参数
     * 设置动态路径和标识   /{key}/info/{key}
     * 接收路径 handler(@PathVariable(动态路径key) 类型 形参名)
   * json
     * 数据接收 handler(@RequestBody 实体类 对象)
     * 准备工作：
       * 1. 导入jackson依赖
       * 2. 使用@EnableWebMvc注解修饰配置类（加入handlerMapping 加入handlerAdapter 给handlerAdapter配置json处理器）

3. 接收cookie

   * handler(@CookieValue("cookie的名字"))

4. 接收请求头

   * handler(@RequestHeader("请求头属性名"))

5. 原生api获取

   * headler(httpServletRequest, response, session)

     Servlet(Context -> ioc -> 全局变量 @Autowired)

6. 共享域获取

   * 原生api方式即可



---

# 三、SpringMVC响应数据

## handler方法说明

一个controller的方法是控制层的一个处理器，我们称为handler。

在handler方法中，使用方法形参的方式来接收请求参数，简化了请求参数的获取方式。使用方法的返回值方式，响应数据给前端，从而简化了响应数据的方式。

理解handler方法的作用和组成：

```java
/**
 * 一个controller的方法是控制层的一个处理器，我们称为handler
 * handler需要使用@RequestMapping注解，声明路径，在
 * HandlerMapping中注册，供DispatcherServlet查找。
 *
 * handler作用总结：
 * 	1.接收请求参数（param, json, pathVariable, 共享域等）
 * 	2.调用业务逻辑
 * 	3.响应前端数据（页面，json，转发和重定向等）
 * 
 * handler如何处理？
 * 	1.接收参数：handler(形参列表：主要的作用就是用来接收参数)
 * 	2.调用业务: {方法体 可以向后调用业务方法 service.xx()}
 * 	3.响应数据：return 返回结果，可以快速响应前端数据
 */
@GetMapping
public Object handler(){
    调用业务方法
    返回的结果（页面跳转，返回数据（json））
    return 简化响应前端数据;
}
```

总结：请求数据接收，都是通过handler的形参列表

前端数据响应，通过handler的return关键字快速处理





## 开发模式说明

在Web开发中，有两种主要的开发模式：`前后端分离`和`混合开发`。

**混合开发模式**（Web开发）

在混合开发模式中，将前端的视图页面和后端的逻辑代码集成在同一个项目中，比如JavaWeb项目，共享相同的技术栈和框架。这种模式在小型项目中比较常见，可以减少学习成本和部署难度。但是，在大型项目中，这种模式会导致代码耦合度很高，维护和升级难度较大。

浏览器发送URL请求给服务端，由controller接收数据，然后对数据进行逻辑处理，将处理好的数据响应给集成在当前项目中的页面，实现页面的跳转，共享的数据存放在共享域中，在页面中去共享域中获取数据（比如Request共享域）。

但是，混合开发模式只能去访问html页面，也就是只能使用浏览器打开。但是，如果我们想要使用客户端的方式，比如使用安卓客户端，或者ios客户端打开，那么此时就不能使用混合开发模式，只能使用前后端分离的开发模式。

混合开发模式的优势在于成本较低。

![image-20240608140425557](.\images\image-20240608140425557.png)





**前后端分离模式**

指将前端的界面和后端的业务逻辑通过接口分离开发的一种方式。开发人员使用不同的技术栈和框架，前段开发人员主要负责页面的呈现和用户交互，后端开发人员主要负责业务逻辑和数据存储。

对于前后端分离开发模式来说，都是使用JSON数据进行交互的，handler方法可以使用实体类类型接收JSON数据，同样也可以将实体类对象类型作为handler方法的返回值响应给前端数据，响应体中同样也是JSON类型格式。

前后端分离模式可以提高开发效率，同时也有助于代码重用和维护。

![image-20240608142039716](.\images\image-20240608142039716.png)





---

## 快速返回模板视图（适用于混合开发模式）

对于**混合开发模式**来说，在handler方法中，就需要使用请求转发的方式跳转到模板页面，然后在模板页面中去获取共享域中的数据。



**JSP技术说明**

thymeleaf技术和jsp差不多。

JSP（JavaServer Pages）是一种动态网页开发技术，它是由Sun公司提出的一种基于Java技术的Web页面制作技术，可以在HTML文件中嵌入Java代码，使得生成动态内容的编写更加简单。

JSP最主要的作用是生成动态页面。它允许将Java代码嵌入到HTML页面中，以便使用Java进行数据库查询、处理表单数据和生成HTML等动态内容。另外，JSP还可以与Servlet结合使用，实现更加复杂的Web应用程序开发。

JSP的主要特点包括：

1. 简单：JSP通过将Java代码嵌入到HTML页面中，使得生成动态内容的编写更加简单。
2. 高效：JPS首次运行时会被转换为Servlet，然后编译为字节码，从而可以启用Just-in-Time（JIT）编译器，实现更高效的运行。
3. 多样化：JSP支持多种标准标签库，包括JSTL（JavaServer Pages标准标签库）、EL（表达式语言）等，可以帮助开发人员更加方便的处理常见的Web开发需求。

总之，JSP是一种简单高效、多样化的动态网页开发技术，它可以方便地生成动态页面和与Servlet结合使用，是Java Web开发中常用的技术之一。





**需求：**

在handler方法中，往request共享域中存入数据，比如：data=jsp，然后，要求在jsp页面中，动态地展示request共享域中data的数据。





**JSP的使用**

**1、引入jsp依赖**

由于当前所使用的Spring版本是6，其要求JakartaEE 9+，Tomcat版本10，所以我们所引入的jsp是jakarta包下的版本。

在pom.xml中，引入jsp依赖：

```xml
<!-- jsp所需依赖-->
<dependency>
    <groupId>jakarta.servlet.jsp.jstl</groupId>
    <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
    <version>3.0.0</version>
</dependency>
```



**2、创建jsp页面**

jsp允许将Java代码嵌入到html页面，所以jsp文件中的大纲依然是html。

**jsp文件不推荐在webapp目录下直接创建**，我们所需的jsp页面，需要经过handler处理器解析后，再访问，直接访问是访问不到的，因为jsp页面中需要一些代码处理后，进行动态的展示，直接访问没有经过处理的数据应该是静态资源（如html、css等）。所以，我们**需要将jsp文件放在`WEB-INF`目录下，让客户端无法直接访问，只能通过请求转发的方式进行访问**（handler方法返回的方式访问页面就是请求转发）。

在WEB-INF目录下，创建一个文件夹views，专门用来存放动态jsp页面。在views文件夹中，创建index.jsp文件：

<img src=".\images\image-20240608144337532.png" alt="image-20240608144337532" style="zoom: 80%;" /> 

<img src=".\images\image-20240608144242305.png" alt="image-20240608144242305" style="zoom: 50%;" /> 

可以看到，jsp文件页面大纲和html页面大致一样，都是包含head标签和body标签。



3. **使用${}获取共享域的数据**

在jsp页面中，是使用**`${属性名}`**的方式，获取后端往共享域中存入的数据。

在jsp页面中，同样也可以使用html中的相关标签。

那么，在index.jsp中，动态地展示共享域中data属性的数据是：

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
${data}
</body>
</html>
```





4. **配置视图解析器**

在之前学习SpringMVC的执行流程时，其中有一个视图解析器ViewResolver的过程。对于混合开发模式来说，需要ViewResolver解析handler方法跳转的视图页面，将跳转后的视图页面解析并响应给浏览器。

ViewResolver可以去定义一个`前缀`和`后缀`，比如前缀是/WEB-INF/views/，后缀是.jsp，当我们handler方法返回index字符串时（不要使用@ResponseBody注解，@ResponseBody注解是将返回数据看作是响应体），此时实际解析后得到一个路径：`/WEB-INF/views/index.jsp`

**ViewResolver需要添加到IOC容器中才可使用。**

SpringMVC防止我们每次添加Bean到IOC容器中，都需要自己手动添加，它给我们提供了一个接口：**`WebMvcConfigurer`**，这个接口内部有一系列方法，这些方法的作用就是给我们快速地配置SpringMVC的组件，这样一来，我们就可以通过重写方法的方式快速地配置组件。

<img src=".\images\image-20240608151618091.png" alt="image-20240608151618091" style="zoom: 50%;" /> 

其中，配置视图解析器的方法是`configureViewResolver()`，调用该方法形参registry的`jsp(String prefix, String suffix)`方法，在方法中设置跳转页面的前缀与后缀，这样一来，当handler方法返回字符串时，就会使用这个前缀与后缀，以及返回的字符串值，拼接一个路径进行跳转：

```java
@Configuration
@ComponentScan("com.atguigu.controller")
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        //配置返回值跳转到的文件夹位置
        registry.jsp("/WEB-INF/views/",".jsp");
    }
}
```





5. **创建handler方式实现跳转**

```java
@Controller
@RequestMapping("/jsp")
public class JspController {
    
    @RequestMapping("/jump")
    public String jumpJsp(ModelMap modelMap){
        modelMap.addAttribute("data","hello Jsp!");
        return "index";
    }
    
}
```

在handler方法中，使用ModelMap，往Request共享域中存入了数据，之后返回index字符串。视图解析器会去解析index字符串，将其与视图解析器中配置的前缀与后缀拼接，使用请求转发的形式跳转到对应的页面中。

**注意**：使用handler实现跳转时，不能使用@ResponseBody注解，使用该注解表示返回的值是响应体数据。

**快速查找视图要求：**

1. 方法的返回值是字符串类型
2. 不能添加@ResponseBody，直接返回给字符串给浏览器
3. 返回值对应中间值的视图名称即可



**测试**

在浏览器中输入http://localhost:8080/jsp/jump，查看网页：

![image-20240608154545324](.\images\image-20240608154545324.png) 

由此可知，handler方法实现了页面的跳转，跳转到了index.jsp页面中，并且将request请求域中的data数据进行了显示。







---

## 请求转发和重定向

当我们将视图解析器的组件加入到SpringMVC中后，此时若在handler中未使用`@ResponseBody`注解，并且返回的是一个普通的字符串时，就会去使用视图解析器，按照视图解析器所定义的路径去跳转到指定的视图返回。

例如：

```java
@Controller
public class RequestController{
    
    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping("/demo1")
    public String demo1(){
        httpServletRequest.addAttribute("username", "cheng");
        return "/demo2";
    }

    @RequestMapping("/demo2")
    @ResponseBody
    public String demo2(){
        String username = (String)httpServletRequest.getAttribute("username");
        return "username -> " + username;
    }
}
```

若SpringMVC未在配置类中配置视图解析器的话，此时是可以通过访问/demo1请求转发到/demo2的。

但是，若在SpringMVC中配置了视图解析器，此时去访问/demo1时，跳转的就不是/demo2的路径了，而是去使用视图解析器，跳转到视图解析器中指定的路径，例如/WEB-INF/views/demo2.jsp。



那如果我们想要去跳转到非视图解析器指定路径，比如我就想从一个handler方法路径跳转到另一个handler方法路径，那此时该如何做？**使用请求转发或响应重定向的方式。**

**请求转发：**

1、方法的返回值写成字符串形式

2、不能使用@RequestBody注解

3、返回的字符串使用**`forward:/转发地址`**的形式

**响应重定向：**

1、方法的返回值写成字符串形式

2、不能使用@RequestBody注解

3、返回的字符串使用**`redirect:/重定向地址`**的形式。



**案例：**

```java
@RequestMapping("/redirect-demo")
public String redirectDemo() {
    // 重定向到 /demo 路径 
    return "redirect:/demo";
}

@RequestMapping("/forward-demo")
public String forwardDemo() {
    // 转发到 /demo 路径
    return "forward:/demo";
}

注意： 转发和重定向到项目下资源路径都是相同，都不需要添加项目根路径，填写项目下路径即可。
```



**注意点：**

1. **解决请求转发中使用绝对路径时的特殊情况。**

   在Servlet学习请求转发时，我们学习过，若请求转发使用的是绝对路径（以/开头的路径），则/开头的斜线表示包含项目上下文路径的URL地址。

   比如项目的上下文路径为demo01，则请求转发中斜杆/表示：http://ip:port/demo01。但是在其他情况下，比如标签中的src属性使用绝对路径，或者响应重定向使用绝对路径，/斜杆表示的含义均只是：http://ip:port/

   即请求转发使用绝对路径的形式是一个特例，故，我们为了避免特殊情况，所以一般会将项目的上下文路径就设置为/，这样一来，无论使用何种方式去使用绝对路径，/斜杆均只表示http://ip:port/，这样一来就不会出错了。

   <img src=".\images\image-20240608170412559.png" alt="image-20240608170412559" style="zoom:40%;" /> 

   在SpringMVC中，给我们做了一个优化：

   在handler方法中，无论是使用请求转发还是响应重定向的方式去跳转，使用绝对路径时，/斜杆均将项目的上下文路径包含了，我们只需要在return返回值中，直接使用/，不需要考虑是否包含项目路径上下文。例如：

   项目路径上下文为application

   <img src=".\images\image-20240608171650242.png" alt="image-20240608171650242" style="zoom:40%;" /> 

   若是在Servlet中，使用重定向的方式跳转到http://lcoalhost:8080/application/demo2中的话，使用的绝对路径应该是：/application/demo2

   但是，在SpringMVC的handler方法中，使用响应重定向的方式跳转所使用的路径是：

   ```java
   return "redirect:/demo2";
   ```

   SpringMVC会自动为我们添加项目的上下文路径。

   

2. 注意在使用请求转发或响应重定向时，**forward: redirect:与地址之间不能有空格**。否则会出现异常的跳转地址。

   比如：forward: /user，此时去跳转的地址是http://localhost:8080/ /user，这是错误的。所以，在forward:、redirect:与地址之间，千万不能有空格出现。



**请求转发与响应重定向特点回顾**

**请求转发特点：**

1. 请求转发时，请求和响应对象会继续传递给下一个资源。
2. 请求中的参数可以继续向下传递。
3. 请求转发是服务器内部的行为，对客户端是屏蔽的。
4. 客户端只产生了一次请求，客户端地址栏不变。
5. 请求转发通过`forward:/路径`方式实现。
6. 请求转发可以转发给其他Servlet动态资源，也可以转发给一些静态资源实现页面跳转。
7. 请求转发可以转发给WEB-INF下受保护的资源。该方式是WEB-INF下受保护资源的唯一访问方式，只有通过请求转发才能进行访问。
8. 请求转发不能转发到外部资源。



**响应重定向特点：**

1. 响应重定向通过`redirect:/路径`方式实现。
2. 响应重定向是服务端通过302响应码和路径，告诉客户端自己去找其他资源，是在服务端提示下，客户端的行为。
3. 客户端至少发送了两次请求，客户端地址是变化的。
4. 服务端产生了多次请求和响应对象，且请求和响应对象不会传递给下一个资源。
5. 因为全程发送了多次请求，所以产生了多个HttpServletRequest对象，请求参数不可传递，请求域中的数据也不可传递。
6. 重定向可以是其他Servlet动态资源，也可以是一些静态资源以实现页面跳转。
7. 重定向不可以去访问WEB-INF下受保护的资源，因为重定向实际上就是多次进行访问。
8. 重定向可以访问本项目外的外部资源，







---

## 响应JSON数据（适用于前后端分离开发模式:star:）

对于前后端分离开发模式来说，都是使用JSON数据进行交互的，handler方法可以使用实体类类型接收JSON数据，同样也可以将实体类对象类型作为handler方法的返回值响应给前端数据，响应体中同样也是JSON类型格式。



### 前置准备

**使用JSON数据格式的准备**

在之前学习过，JSON类型格式的数据是使用在前端的，若在后端中要使用JSON类型的数据格式，则需要引入JSON的相关依赖并进行配置。

导入jackson依赖

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>
```

添加json数据转化器

这个在之前也讲过，就是在SpringMVC的配置类中，添加`@EnableWebMvc`注解。该注解的用处是将HandlerMapping与HandlerAdapter加入到IOC容器中，并且为HandlerAdapter添加JSON转化器。

```java
@Configuration
@ComponentScan("com.atguigu.controller")
@EnableWebMvc
public class MvcConfig{
}
```





---

### @ResponseBody注解

可以在方法上使用@ResponseBody注解，用于将方法返回的对象序列化为JSON或XML格式的数据，并发送给客户端。在前后端分离的项目中使用。

**`@ResponseBody`注解表示的含义是返回的数据并不是要去跳转的路径，而是作为响应的响应体数据，返回给前端。**

当使用了@ResponseBody注解之后，数据直接放入到响应体中返回，不会再走视图解析器，也不会进行转发和重定向了。

若返回的是String字符串类型或其他基本数据类型，则直接作为响应体内容返回；若返回的是引用数据类型的话，则会转换成JSON字符串，放在响应体中返回。

**案例：**

```java
@Controller
@RequestMapping("/person")
public class PersonController {
    
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    @ResponseBody
    public Person getPerson(@RequestBody Person personParam){
        Person person = new Person();
        person.setAge(personParam.getAge());
        person.setName(personParam.getName());
        person.setGender(personParam.getGender());
        return person;
    }
    
}
```

这里我们实际上就是将请求体转换成Person对象，然后将这个Person对象响应给前端。在实际的开发中，一般都是去调用持久层的方法，获取数据库中的数据转换成对象响应给前端。



**测试结果**

<img src=".\images\image-20240608212955066.png" alt="image-20240608212955066" style="zoom:67%;" /> 



如果在类中的每个方法上都要标记@ResponseBody注解，则这些注解可以提取到类上，表示类中的所有方法都使用@ResponseBody注解修饰。

```java
@ResponseBody  //responseBody可以添加到类上,代表默认类中的所有方法都生效!
@Controller
@RequestMapping("/param")
public class ParamController {}
```





---

### @RestController注解

类上的@ResponseBody注解可以和@Controller注解合并为@RestController注解。所以使用了@RestController注解就相当于给类添加了@Controller注解，并且给类中的每个方法都加了@ResponseBody注解。

**@RestController源码：**

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@ResponseBody
public @interface RestController {
 
  /**
   * The value may indicate a suggestion for a logical component name,
   * to be turned into a Spring bean in case of an autodetected component.
   * @return the suggested component name, if any (or empty String otherwise)
   * @since 4.0.1
   */
  @AliasFor(annotation = Controller.class)
  String value() default "";
 
}
```





---

## 静态资源处理

静态资源本身已经是可以直接拿到浏览器上使用的程度，不需要在服务器端做任何运算、处理。而对于动态资源来说，就可能还需要对其解析、处理，比如JSP文件就是动态资源，因为在JSP相当于在html页面插入了Java代码，所以在拿到JSP文件时，还需要对其中的Java代码进行解析。

所以，静态资源和动态资源的处理是不一样的：

* 静态资源浏览器可以直接解析，我们只需要跳转到相应的静态资源，然后服务端就会将该静态资源作为响应体响应给浏览器，浏览器自己去解析；
* 动态资源浏览器无法直接解析，我们需要使用视图解析器，解析

**典型的静态资源包括：**

* 纯HTML文件
* 图片
* CSS文件
* JS文件
* ...



**案例说明：**

在webapp目录下，创建一个images目录，用于存放图片，在该目录下，引入一个bi.jpg图片。

然后，去启动容器，使用路径的方式访问该图片：

http://localhost:8080/images/bi.jpg

访问结果：

<img src=".\images\image-20240608221056892.png" alt="image-20240608221056892" style="zoom:67%;" /> 

可以发现，在SpringMVC容器中，直接去访问静态资源是访问不到的。

但是，如果上述案例使用的是Servlet原生API的方式作为请求与响应处理的工具时，则是可以直接访问到webapp下的静态文件的。

**为什么会这样呢？**

原因还是与SpringMVC的整体流程有关：在SpringMVC中，我们将DispatcherServlet的映射路径设置成了/，这样一来，浏览器所发送的所有路径的请求，都会被DispatcherServlet所接收，而DispatcherServlet会去HandlerMapping中寻找路径所对应的handler方法。而我们访问的是一个静态路径地址，不是handler方法路径，所以也就找不到了。

而在Servlet原生API中，有一个DefaultServlet，它的映射路径设置成了/，也就是接收所有的请求。当客户端发送一个路径请求时，Servlet原生的API就会拿路径与所有Servlet进行比对，如果没有定义的Servlet映射路径与路径匹配时，此时就会交给DefaultServlet处理，DefaultServlet会根据请求的路径，查找相应的静态资源。

由此，我们可以得知：Servlet原生API可以访问静态资源是因为当没有路径与现有的映射路径匹配时，Servlet原生的API就会去项目路径中去查找是否有资源路径匹配；而SpringMVC则不会去查找，所以SpringMVC无法直接访问。



**解决方案**

> **在配置类中开启静态资源处理，就可以通过`URL地址`的方式访问静态资源。**

```java
@Configuration
@ComponentScan("com.atguigu.controller")
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
```

上述的配置，相当于在XML配置文件中配置`<mvc:default-servlet-handler />`

我们来看看实际作用的代码：

![image-20240608222848274](.\images\image-20240608222848274.png)

该方法位于DefaultServletHandler类中，我们可以看到，实际上跳转到静态资源是使用forward请求转发的方式进行访问的。



**开启静态资源处理后，SpringMVC处理静态资源过程说明**

DispatcherServlet在接收到客户端的请求后，先让HandlerMapping根据请求的URL路径地址，查找对应的handler方法，若查找到，则让HandlerAdapter去执行；若未查找到，则让`DefaultServletHandler`根据URL地址，查找项目路径下对应的静态资源，在`DefaultServletHandler`中，使用**请求转发**的方式，到项目资源路径下进行查找，并将查找到的静态资源数据作为响应体返回；若没有，则报404的异常。



开启静态资源处理后，我们就可以通过URL路径的方式，访问静态资源了：

<img src=".\images\image-20240609000048268.png" alt="image-20240609000048268" style="zoom:67%;" /> 

我们知道，在DefaultServletHandler中，实际上是通过请求转发的方式来访问静态资源的。

> 同理，**如果我们不去开启静态资源，也可以手动地使用`forward:`关键字**，获取项目下的静态资源并响应给前端。

如：

```java
@Controller
public class StaticController {
    @RequestMapping("/jump/image")
    public String jumpToImage(){
        return "forward:/images/bi.jpg";
    }
}
```

此时，我们不去开启静态资源处理，通过访问/jump/image路径，也能够获取到images目录下的bi.jpg图片。





## 对于路径访问的说明总结（:star2:）

在进行说明之前，我们首先需要弄明白SpringMVC的执行流程：

![](.\images\imagedsasda.png)

### 动态资源访问说明

动态资源指的是在可以使用Java代码来动态地展示页面的文件，如JSP。

我们**`可以使用URL地址的方式进行直接访问动态资源`**，比如在webapp下有一个views目录，views目录中有index.jsp文件：

<img src=".\images\image-20240608234511975.png" alt="image-20240608234511975" style="zoom:67%;" /> 

<img src=".\images\image-20240608234618410.png" alt="image-20240608234618410" style="zoom:67%;" /> 

然后通过URL地址进行访问：

<img src=".\images\image-20240608234642469.png" alt="image-20240608234642469" style="zoom:67%;" /> 

可以看到，我们是可以直接通过URL地址进行访问动态资源的，只不过通过这种方式访问到的动态资源，在动态资源中所使用的Java代码逻辑，或者去获取共享域中的数据无法显示，只能显示浏览器中能够看懂的语言，如html、css等。**通过这种方式访问的动态资源，与静态资源无异。**



动态资源也可以通过在handler方法中，**`使用请求转发的方式来访问`**（请求转发能够访问项目下的任何资源），当然也可以通过响应转发的方式来访问（响应转发与发送URL请求一样）

```java
@Controller
public class StaticController {

    @RequestMapping("/jump/jsp")
    public String jumpToJsp(HttpServletRequest httpServletRequest){
        httpServletRequest.setAttribute("data","hello jsp!");
        return "forward:/WEB-INF/views/index.jsp";
    }
}
```

当我们去访问/jump/jsp路径时，会去跳转到项目中的/WEB-INF/views/index.jsp文件，并响应给客户端。

这种方式也不推荐，但是**这种方式可以往请求域中存入数据，以供jsp文件中获取。**



上述访问的两种方式都不推荐，因为动态资源中往往有着Java代码，或者要获取共享域中的数据，我们需要对动态资源中的数据进行解析，一般要经过ViewResolver视图解析器解析之后，再响应给客户端显示。

所以，对于动态资源的访问，**`推荐使用ViewResolver视图解析器解析后访问`**。

先去配置视图解析器，指定返回字符串跳转路径的前缀与后缀

```java
@Configuration
@ComponentScan("com.atguigu.controller")
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        //配置返回值跳转到的文件夹位置
        registry.jsp("/WEB-INF/views/",".jsp");
    }
}
```

然后，在handler方法中，返回字符串，注意这里的字符串不能使用forward:与redirect:关键字，否则会使用请求转发与响应重定向的方式进行跳转。

比如，当返回的是index字符：

```java
@RequestMapping("/jump/jsp")
public String jumpToJsp(HttpServletRequest httpServletRequest){
    httpServletRequest.setAttribute("data","hello jsp!");
    return "index";
}
```

那么就会去将index字符串拼接前缀与后缀，得到/WEB-INF/views/index.jsp，这也就是实际跳转的路径。

视图解析器会去解析这个路径的动态文件，将其转换成浏览器能够读懂的语言，返回给客户端。那么，我们就可以访问/jump/jsp路径来获得对应的jsp动态文件。



> **总结：**
>
> 1. 可以直接通过URL地址的方式，直接访问项目下的动态资源。
> 2. 也可以通过请求转发forward:或者响应重定向redirect:的方式，访问动态资源。
> 3. 在一般情况下，都会将动态资源放在WEB-INF目录下，然后配置视图解析器，然后在handler方法中，通过返回字符串的形式（不包含forward与redirect，不使用请求转发与响应重定向）通过视图解析器，解析并响应对应的动态资源给前端显示。





### 静态资源访问说明

**`直接在URL地址上访问静态资源是访问不到的`**。

如，访问webapp/views/index.html：

<img src=".\images\image-20240608230032018.png" alt="image-20240608230032018" style="zoom: 50%;" /> 

原因：

当我们使用客户端发送一个请求时，SpringMVC是通过DispatcherServlet接收的，DS接收到请求后，调用HandlerMapping来根据URL地址获取对应的handler方法，若能够获取对应的handler方法，则进行执行并返回响应数据；若没有对应的handler方法，则报404异常。

上述出现404的原因，就是因为DS只去查找对应的handler方法了，并没有去项目下查找静态资源。



**如果我们想要在URL地址上直接对静态资源进行访问，则需要开启静态资源处理：**

在SpringMVC的配置类中，实现WebMvcConfigurer接口，并重写configureDefaultServletHandling方法：

```java
@Configuration
@ComponentScan("com.atguigu.controller")
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
```

此时，就可以通过URL地址的方式进行访问：

<img src=".\images\image-20240608231506846.png" alt="image-20240608231506846" style="zoom: 25%;" /> 

当我们使用客户端发送一个请求，DS接收到请求调用HandlerMapping来根据URL地址获取对应的handler方法，若没有对应的handler方法，则不会报404异常，而是去调用`DefaultServletHandler`，使用**请求转发**的方式去查找静态资源，若存在，则返回静态资源的数据；若不存在，则报404异常。

配置开启静态处理，SpringMVC底层实际上是使用请求转发的方式进行访问，那么同理，如果我们不去配置开启静态处理，而是自己在handler方法中，使用请求转发的方式进行访问是否可以实现呢？

答案是可以的。当我们使用请求转发的方式，就能够访问项目下的任意资源，也包括其他的handler方法。

如：

```java
@Controller
public class StaticController {
    @RequestMapping("/jump/image")
    public String jumpToImage(){
        return "forward:/images/bi.jpg";
    }
}
```

此时，我们可以通过访问/jump/image路径的方式，去访问项目路径下/images/bi.jpg静态文件。



> **总结：**
>
> 1. 若想通过URL路径直接访问项目下的静态资源，则需要先开启静态资源处理；
>
> 2. 若不开启静态资源处理，我们也可以在handler方法中，通过forward:请求转发的方式，访问项目中的静态资源。
>
>    使用响应重定向则无法访问，因为响应重定向实际上就是通过再发一次URL路径请求进行访问。











---



# 四、RESTFul风格设计和实战

这一章其实学不学都一样，每个公司都有自己的编写风格，PUT和DELETE请求方式一般都不用，只要做到URL地址要见名知意，然后根据是否有请求体来选择是使用GET还是POST请求方式即可。

## 1、什么是RESTFul？

客户端与服务端之间进行数据传输，一般使用的是Http协议。

使用Http协议时需要去规定三个内容：

1. 请求的URL地址
2. 请求方式（GET、POST、DELETE、PUT）
3. 传递参数的类型（param、json、path）

比如说，针对user表，我们要去创建三个功能，分别是用户添加、用户删除以及根据id查询用户，对于每个功能，就要分别去规定每个功能所对应的请求URL地址、请求方式以及传递的参数类型。

那么，到底该如何去规定这三部分内容呢？

这就可以去使用RESTFul了。

**RESTFul是Http协议的标准使用方案和风格。**

**RESTFul的作用：**

* `RESTFul会教你如何设计路径`

* `教你如何设计参数传递`

* `教你如何选择请求方式`

<img src=".\images\image-20240609144315550.png" alt="image-20240609144315550" style="zoom:80%;" /> 



## 2、RESTFul风格概述

### 2.1、RESTFul风格简介

![img](.\images\image2132131.png)

RESTFul是一种软件架构风格，用于设计网络应用程序和服务之间的通信。它是一种基于标准HTTP方法的简单和轻量级的通信协议，广泛应用于现代的Web服务开发。

通过遵循RESTful架构的设计原则，可以构建出易于理解、可扩展、松耦合和可重用的Web服务。RESTFul API的特点是简单、清晰，并且易于使用和理解，它们使用准备的HTTP方法和状态码进行通信，不需要额外的协议和中间件。

总而言之，RESTFul是一种基于HTTP和标准化的设计原则的软件架构风格，用于设计和实现可靠、可扩展和易于集成的Web服务和应用程序。

![img](.\images\image313131212.png)

学习RESTFul设计原则可以帮助我们更好地设计HTTP协议的API接口。



### 2.2、RESTFul风格特点



## 3、RESTFul实战



## 对于PUT、DELETE请求方式的说明

基于RESTFul风格，我们可以直接从请求方式就看出来，这个请求发送出去后服务器会怎么处理数据。RESTFul使用不同的请求方式，来区别资源的处理方式。

对于PUT和DELETE来说，实际和GET、POST请求没有什么区别，完全都可以使用GET、POST请求替换。PUT和DELETE之所以被设计出来，完全是基于RESTFul风格的考虑，是一种编码规范而已。所以如果不管restful的话，完全可以不用PUT和DELETE请求方式，而且就算使用了PUT和DELETE，后端的逻辑依旧需要自己写。

所以，在实际的项目开发中，有可能直接不使用PUT和DELETE请求方式。

但是，虽然不使用PUT|DELETE请求方式，但是在请求路径上，我们一般需要做到见名知意，从路径大概知道该请求发送后，服务器会做的操作。





---

# 五、SpringMVC其他扩展

## 1、声明式异常处理机制

### 异常处理的两种方式

开发过程中是不可避免地出现各种异常情况的，例如网络连接异常、数据格式异常、空指针异常等等。异常的出现可能导致程序的运行出现问题，甚至直接导致程序崩溃。因此，在开发过程中，合理处理异常、避免异常发生、以及对异常进行有效的调试是非常重要的。

对于异常的处理，一般有两种方式：

* `编程式异常处理`：是指在代码中显式地编写处理异常的逻辑。它通常涉及到对异常类型的检测及其处理，例如使用try-catch快来捕获异常，然后在catch块中编写待定的处理代码，或者在finally快中执行一些清理操作。在编程式异常处理中，开发人员需要显式地进行异常处理，异常处理代码混杂在业务代码中，导致代码可读性较差。
* **`声明式异常处理`**：则是将异常处理的逻辑从具体的业务逻辑中分离出来，通过配置等方式进行统一的管理和处理。在声明式异常处理中，开发人员只需要为方法或类标注相应的注解（如`@Throws`或`@ExceptionHandler`），就可以处理特定类型的异常。相较于编程式异常处理，声明式异常处理可以使diamante更加简洁、易于维护和扩展。

**简单的描述一下**：当我们在开发过程中出现异常，有两种异常处理方式，一种是编程式异常处理，一种是声明式异常处理。编程式异常处理，就是当出现异常时，我们手动地使用try-catch语句来处理异常，在catch中编写对异常的操作；而声明式异常则是去在一个类里面，专门编写某异常出现时该如何处理，当执行过程中出现了异常时，就会去自动使用该类中编写的异常处理方式处理异常。

**声明式异常的好处：**

整个项目从架构这个层面设计的异常处理的统一机制和规范。

一个项目中包含很多个模块，各个模块需要分工完成。如果张三负责的模块按照A方案处理异常，李四负责的模块按照B方案处理异常......各个异常处理异常的思路、代码、命名细节都不一样，那么就会让整个项目非常混乱。

使用声明式异常处理，可以统一项目处理异常思路，项目更加清晰明了。

> **声明式异常实际上是使用`AOP面向切面编程实现`的。**



**声明式异常处理的过程**

当发生异常，就会进入到加了`@ControllerAdvice`注解的类中，然后去判定当前的异常与全局异常处理类中handler方法上的`@ExceptionHandler`注解中所设置的异常是否向相等，若相等则执行对应的handler方法；若没有相等的异常，则会去查找handler映射的异常中，是否有当前异常的父异常，若有则去执行父异常。若父异常依旧没有匹配，那就会直接报错，不会去使用异常处理。







---

### 基于注解的声明式异常处理

**实现步骤**

1. **声明一个全局异常处理类**

创建com.atguigu.exception包，该包下就是去存放异常处理类的：

![image-20240609171046210](.\images\image-20240609171046210.png) 

在该包中创建一个全局异常处理类，如GlobalExceptionHandler类，使用**`@ControllerAdvice`**注解或**`@RestControllerAdvice`**注解修饰该类。

@RestControllerAdvice = @ControllerAdvice + @ResponseBody。



**其中，`@CotrollerAdvice`注解表示该类是一个全局异常处理类，当异常发生时，就会走此类中的handler方法来进行处理，不会再继续执行原有的方法。**

它是去定义一个handler类，其中的所有方法都是handler方法，即此时就不会再走原本的handler方法，而在全局异常处理类中的handler方法，也类似于Controller中的handler方法，**异常处理方法可以响应数据或者使用转发、重定向实现页面的跳转。**

那么，我们就可以使用@ResponseBody注解来表示异常处理方法返回的数据属于响应体数据，当全局异常处理类中的所有方法都使用@ResponseBody注解，那么我们就可以使用@RestControllerAdvice来代替@ControllerAdvice与全部方法的@ResponseBody。

```java
@ControllerAdvice
public class GlobalExceptionHandler {
}
```







2. **声明异常处理handler方法**

在GlobalExceptionHandler类中，去创建两个处理异常的handler方法，分别用于处理空指针异常NullPointerException与异常Exception。假设这两个方法分别叫做handlerNullPointerException与handlerException。

在handler方法中，需要传入所处理的异常类型对象，在handler方法中，对该异常进行处理。在handler方法上，使用**`@ExceptionHandler`**注解修饰，这个注解表示的含义是标明当前的方法是全局异常处理方法：

<img src=".\images\image-20240609180511451.png" alt="image-20240609180511451" style="zoom: 80%;" /> 

使用该注解需要去指定`value`属性，该value属性用于指定handler方法所映射的异常类，即指明，当出现哪些异常时，会被当前的handler方法所捕获。

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    //空指针异常处理的handler方法
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public String handlerNullPointerException(NullPointerException e){
        String message = e.getMessage();
        System.out.println("message = " + message);
        return message;
    }

    //异常处理的handler方法
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handlerException(Exception e){
        String message = e.getMessage();
        System.out.println("message = " + message);
        return message;
    }
}
```

由于异常处理的handler方法类似于普通的handler方法，也会实现数据的响应与页面的跳转，所以我们需要使用@ResponseBody注解表明当前的handler返回的方法是响应体内容，不是页面跳转。



3. **配置文件扫描全局异常处理类**

确保异常处理控制器类被扫描到，在SpringMVC配置类的@ComponentScan注解中，添加异常处理的包，去扫描全局异常处理类。

```java
@EnableWebMvc  //json数据处理,必须使用此注解,因为他会加入json处理器
@Configuration
@ComponentScan(basePackages = {"com.atguigu.controller", "com.atguigu.exception"})
//WebMvcConfigurer springMvc进行组件配置的规范,配置组件,提供各种方法
public class SpringMvcConfig{}
```





**测试：**

在类中，创建两个方法，分别用于抛出空指针异常与算术异常：

```java
@Controller
@RequestMapping("/user")
public class UserController {

    //空指针异常
    @RequestMapping("/data")
    @ResponseBody
    public String data(){
        String name = null;
        name.toString();
        return "success";
    }


    //算术异常
    @RequestMapping("/data1")
    @ResponseBody
    public String data1(){
        int i = 1/0;
        return "success";
    }
}

```

当我们去访问/user/data路径时，此时的浏览器的显示结果为：

![image-20240609194321938](.\images\image-20240609194321938.png) 

在去访问/user/data路径时，会去对应映射的handler方法data()，在data()方法中，由于操作了null对象的方法，所以会报空指针异常。

此时就会去全局异常处理类中，寻找匹配异常的异常处理方法，从而被全局异常处理类中的处理空指针异常的handlerNullPointerException()方法所捕获，从而去执行异常处理的handler方法，该方法向前端响应报错信息。



当我们去访问/user/data1路径是，浏览器的执行结果为：

![image-20240609194647902](.\images\image-20240609194647902.png) 

当去访问/user/data1路径时，会去执行对应的handler方法data1()，在data1()中，由于去除以0了，所以会报算术异常ArithmeticException。

那么此时，就会去全局异常处理类中，寻找是否有处理ArithmeticException异常的handler方法，找了一遍发现没有；那么此时就会去寻找ArithmeticException异常的父异常，发现存在处理Exception异常的handler方法，此时就会去执行处理Exception异常的handler方法，即向前端响应异常的信息。





**总结：**

异常处理handler方法与控制器handler方法类似：

普通的handler方法需要使用@RequestMapping注解映射路径，异常处理的handler方法使用@ExceptionHandler注解映射异常。

普通的handler方法可以响应数据，也可以使用转发或重定向进行页面的跳转；异常处理的handler方法同样也可以响应数据或者进行页面的跳转。

所以，我们将异常处理的handler方法看作是一种特殊的handler方法，只不过这种handler方法是出现异常时去执行，而不是根据URL路径映射。





---

## 2、拦截器

### 2.1、拦截器的概念

**什么是拦截器？**

在学习Servlet时，我们学习过`Filter过滤器`。在SpringMVC的框架中，假如我们使用Filter过滤器拦截web资源进行处理的话，那么过滤器实际的作用位置是在请求发送到DispatcherServlet之前，以及请求处理完毕DipatcherServlet响应数据之后：

<img src=".\images\image-20240609215322230.png" alt="image-20240609215322230" style="zoom:50%;" />

Filter过滤器只能去处理SpringMVC最外层的流程，对于SpringMVC执行的内部流程Filter过滤器无法进行拦截处理，所以Filter过滤器不能满足SpringMVC框架结构中的执行拦截。

在SpringMVC中，给我们提供了一个类似于Filter过滤器作用的类：**`拦截器HandlerInterceptor`**。它的好处是可以在SpringMVC执行内部对资源进行拦截操作，`可以在handler方法执行之前`、`handler方法执行之后`，以及最终`操作完毕响应数据给用户之前`进行拦截，从而对拦截的数据进行操作。

拦截器只能在SpringMVC框架的基础上进行使用，可以在SpringMVC流程内部进行拦截；过滤器适用于所有的JavaWeb项目，范围更大，可以在服务端接收请求之前以及响应数据之后进行拦截、

**拦截器方法拦截的位置：**

![](.\images\image13123123123.png)

**拦截器与过滤器的对比：**

* **相似点：**
  * 拦截：必须先把请求拦住，才能执行后续操作
  * 过滤：拦截器或过滤器存在的意义就是对请求进行统一的处理
  * 放行：对请求执行了必要操作后，放请求过去，让它访问原本想要访问的资源。

* **不同点：**
  * 容器支持：
    * 拦截器只能使用在SpringMVC框架的基础上
    * 过滤器因为是使用在Servlet容器中，所以过滤器在JavaWeb项目中都可以使用。
  * 拦截的范围：
    * 过滤器：
    * 拦截器：
  * 拦截的位置：
    * 过滤器是在request到达Servlet程序之前，以及response离开Servlet程序之后进行处理的。
    * 拦截器，则是在SpringMVC执行流程的内部来处理的。

<img src=".\images\imag222211e.png" style="zoom:67%;" /> 







---

### 2.2、拦截器的使用

#### 拦截的三个方法

**通过实现`HandlerInterceptor`接口的方式，创建拦截器类**

例如，在com.atguigu.interceptor包下，创建了一个MyInterceptor类实现HandlerInterceptor接口

```java
public class MyInterceptor implements HandlerInterceptor {
}
```

在HandlerInterceptor接口中，声明了三个默认方法，分别是`preHandle()`、`postHandle()`以及`afterCompletion()`：

```java
public interface HandlerInterceptor {
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
```

这些方法声明成了default默认方法，那么我们在自定义的拦截器类中，就可以根据拦截位置的需要，重写不同的拦截方法。

**拦截器方法拦截的位置：**

<img src=".\images\image13123123123.png" style="zoom:67%;" />

> **对于三种拦截方法的说明：**
>
> * **`preHandle()`：前置处理方法，在执行handler方法之前调用的拦截方法**。
>   * 用来对编码格式进行设置，登录保护，以及权限的处理。
>   * 方法有三个参数，第一个参数request是请求对象，第二个参数response是响应对象，第三个参数handler就是要调用的handler方法对象。
>   * preHandle()方法根据返回值设置对请求是放行还是拦截，返回true是放行，返回false是拦截。
> * **`postHandle()`：后置处理方法，在handler方法执行完毕后执行的方法。**
>   * 用来对执行结果的处理，比如进行敏感词汇的检查。
>   * 方法有四个参数，分别是request、response、handler以及modelAndView，分别表示请求对象、响应对象、handler方法对象以及返回的视图和请求域数据对象。
>   * 由于在执行到此方法时，handler方法已经执行完毕了，请求也已处理完毕了，所以不会再去拦截请求不让继续执行，所以该方法返回值为void。
> * **`afterCompletion()`：完成后处理方法，在请求处理完毕，响应数据给用户之前执行的方法。**
>   * 方法有四个参数，分别是request、 response、handler以及ex，分别表示请求对象、响应对象、handler方法对象以及当handler方法报错时的异常对象。
>
> 其中最常用的是preHandle()方法，用于进行登录验证以及权限验证等等。





#### 实现过程

1. **创建拦截器类**

**通过实现`HandlerInterceptor`接口的方式，创建拦截器类**

例如，在com.atguigu.interceptor包下，创建了一个MyInterceptor类实现HandlerInterceptor接口

```java
public class MyInterceptor implements HandlerInterceptor {
}
```

在HandlerInterceptor接口中，声明了三个默认方法，分别是`preHandle()`、`postHandle()`以及`afterCompletion()`：

```java
public interface HandlerInterceptor {
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
```

这些方法声明成了default默认方法，那么我们在自定义的拦截器类中，就可以根据拦截位置的需要，重写不同的拦截方法。

在了解了这些方法后，在拦截器类中重写这些拦截方法进行测试：

```java
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle()方法");
        return true;
    }
    

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle()方法");
    }
    

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion()方法");
    }
}
```

注意，preHandle()的方法返回值一定是true，表示对请求进行放行，如果为false则在执行完preHandle()后就不再继续执行handler方法。





2. **修改配置类添加拦截器**

在创建完拦截器类之后，要想使用拦截器，就需要将拦截器类注册到SpringMVC的配置类中。

**配置方式：**让配置类去实现`WebMvcConfigurer`接口（若要配置视图解析器或开启静态资源时，也需要去实现该接口），然后重写**`addInterceptors()`**方法，在该方法中，调用形参对象的`addInterceptor()`方法，将拦截器方法对象传入，表示将指定的拦截器添加到springmvc环境。

```java
@EnableWebMvc  //json数据处理,必须使用此注解,因为他会加入json处理器
@Configuration
@ComponentScan(basePackages = {"com.atguigu.controller", "com.atguigu.exception"})
//WebMvcConfigurer springMvc进行组件配置的规范,配置组件,提供各种方法
public class SpringMvcConfig implements WebMvcConfigurer {

    //配置jsp对应的视图解析器
    //只适用于混合开发模式，由于前后端分离项目，handler只需要返回JSON字符串，所以无需实现页面跳转
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        //快速配置jsp模板语言对应的
        registry.jsp("/WEB-INF/views/",".jsp");
    }

    //开启静态资源处理 <mvc:default-servlet-handler/>
    //只适用于混合开发模式
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //将拦截器添加到springmvc环境，在默认情况下会去拦截所有springmvc分发的请求
        registry.addInterceptor(new MyInterceptor());
    }
}
```

将拦截器添加到springmvc环境的addInterceptor()方法，实际上在底层，是去往List集合中添加拦截器对象：

![image-20240609225413414](.\images\image-20240609225413414.png)

其中registrations是一个InterceptorRegistration对象的List集合。所以可以多次调用addInterceptor()方法，那么就是往springmvc环境中添加多个拦截器。

> **使用`registry.addInterceptor(拦截器类对象);`的方式将拦截器配置到springmvc环境中，`默认是拦截所有springmvc分发的请求`。**
>
> 即此时springmvc分发的所有请求，都会被拦截器类中所配置的方法拦截，我们也可以去配置拦截指定的路径，或者对指定路径不拦截。（在下面）





**测试**

```java
@Controller
public class DemoController {
    @RequestMapping("/demo")
    @ResponseBody
    public String demo1(){
        return "success";
    }
}
```

浏览器访问/demo路径，此时Tomcat控制台的打印结果为：

![image-20240609232014390](.\images\image-20240609232014390.png) 

可以看到，配置的三个拦截器方法就执行了。







#### 拦截路径配置

* **默认拦截全部**

  在之前学习中，使用`registry.addInterceptor(拦截器对象);`方式去将拦截器添加到springmvc环境的方式，默认是去拦截所有springmvc分发的请求。

  如：

  ```java
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(new MyInterceptor());
  }
  ```

  

* **精确拦截**

  registry.addInterceptor(拦截器对象)方法返回一个InterceptorRegistration类型的对象，在该对象中，调用`addPathPatterns()`方法来添加路径进行精准的拦截。

  该方法是一个**链式方法**，即，在InterceptorRegistration对象调用的addPathPatterns()方法返回的也是InterceptorRegistration对象，可以往该方法后面继续添加方法进行执行。

  ![image-20240610011325772](.\images\image-20240610011325772.png)

  该方法传入的是路径的集合，表示只去拦截指定的路径。

  比如，拦截器只去拦截/user/data与/user/test路径，可以写成：

  ```java
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(new MyInterceptor()).addPathPatterns("/user/data","/user/test");
  }
  ```

  也可以写成：

  ```java
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(new MyInterceptor()).addPathPatterns("/user/data").addPathPatterns("/user/test");
  }
  ```

  因为该方法是一个链式方法。

  在addPathPatterns()方法传入的路径中，也支持通配符，/*表示一层任意字符串路径，/**表示任意层任意字符串路径。



* **排除路径**

  排除路径，表示在选择要进行拦截的路径中，排除指定的路径，当请求的路径是指定的路径时，拦截器不对其进行拦截。

  使用`excludePathPatterns()`方法实现，表示去排除指定的路径。该方法也是一个**链式方法**，使用InterceptorRegistry对象进行调用，返回的依旧是一个InterceptorRegistry对象。

  比如，当我们要去拦截/user/*的请求时，去排除/user/test路径的请求：

  ```java
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(new MyInterceptor()).addPathPatterns("/user/*").excludePathPatterns("/user/test");
  }
  ```

  注意：排除路径，排除应该在匹配拦截路径的范围内进行排除。





#### 多个拦截器执行顺序

假如在SpringMVC中配置了多个拦截器，如MyInterceptor1和MyInterceptor2：

**MyInterceptor1拦截器类**

```java
public class MyInterceptor1 implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Interceptor1中的preHandle()方法");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("Interceptor1中的postHandle()方法");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("Interceptor1中的afterCompletion()方法");
    }
}
```

**MyInterceptor2拦截器类**

```java
public class MyInterceptor2 implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Interceptor2中的preHandle()方法");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("Interceptor2中的postHandle()方法");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("Interceptor2中的afterCompletion()方法");
    }
}
```

然后，在SpringMVC中，先去配置MyInterceptor1拦截器，然后再去配置MyInterceptor2拦截器：

```java
@EnableWebMvc  //json数据处理,必须使用此注解,因为他会加入json处理器
@Configuration
@ComponentScan(basePackages = {"com.atguigu.controller", "com.atguigu.exception"})
//WebMvcConfigurer springMvc进行组件配置的规范,配置组件,提供各种方法
public class SpringMvcConfig implements WebMvcConfigurer {

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //将拦截器添加到springmvc环境，在默认情况下会去拦截所有springmvc分发的请求
        registry.addInterceptor(new MyInterceptor1());
        registry.addInterceptor(new MyInterceptor2());
    }
}
```

然后去任意访问一个地址，Tomcat控制台打印的结果为：

![image-20240610110400357](.\images\image-20240610110400357.png) 

其中preHandle()方法是在handler控制器方法执行之前执行的；

postHandle()方法是在handler控制器方法执行之后执行的；

afterCompletion()方法是在整体结束，DS响应数据到客户端之前执行的。

**由此，我们可以得知，多个拦截器执行顺序是：**

* **`preHandle()方法`**：SpringMVC会把所有拦截器收集到一起，然后按照**配置顺序**调用各个preHandle()方法。
* **`postHandle()方法`**：SpringMVC会把所有拦截器收集到一起，然后按照**配置相反的顺序**调用各个postHandle()方法。
* **`afterCompletion()方法`**：SpringMVC会把所有拦截器收集到一起，然后按照**配置相反的顺序**调用各个afterCompletion()方法。









---

## 3、参数校验

在Web应用三层架构体系中，表述层负责接受浏览器提交的数据，业务逻辑层负责数据的处理。为了能够让业务逻辑层基于正确的数据进行处理，我们需要在表述层对数据进行检查，将错误的数据隔绝在业务逻辑层之前。

假如我们要求，前端发送的请求参数有要求，比如某某参数不能为空。如，当用户进行登录时，我们就要求前端发送的请求参数中，用户名与密码不能为空，此时，我们如果在service业务代码中，去进行一个一个的判断，比较的麻烦。

那么，此时我们可以使用参数校验功能，来对前端发送的请求参数进行校验。



**校验概述**

JSR 303是Java为Bean数据合法性校验提供的标准框架，它已经包含在JavaEE 6.0标准中。JSR 303通过在Bean属性上标注类似于@NotNull、@Max等标准的注解指定校验规则，并通过标准的验证接口对Bean进行验证。

| 注解                         | 规则                                       |
| ---------------------------- | ------------------------------------------ |
| `@Null`                      | 标注值必须为null                           |
| `@NotNull`                   | 标注值不可为null                           |
| `@AssertTrue`                | 标注值必须为true                           |
| `@AssertFalse`               | 标注值必须为false                          |
| `@Min(value)`                | 标注值必须大于或等于value                  |
| `@Max(value)`                | 标注值必须小于或等于value                  |
| `@DecimalMin(value)`         | 标注值必须大于或等于value                  |
| `@DecimalMax(value)`         | 标注值必须小于或等于value                  |
| `@Size(max, min)`            | 标注值大小必须在max和min的限定范围内       |
| `@Digits(integer,fratction)` | 标注值必须是一个数字，且必须在可接受范围内 |
| `@Past`                      | 标注值只能用于日期型，且必须是过去的日期   |
| `@Future`                    | 标注值只能用于日期型，且必须是将来的日期   |
| `@Pattern(value)`            | 标注值必须符合指定的正则表达式             |

JSR 303只是一套标准，需要提供其实现才可以使用。Hibernate Validator是JSR 303的一个参考实现，除支持所有标准的校验注解外，它还支持以下的扩展注解：

| 注解        | 规则                               |
| ----------- | ---------------------------------- |
| `@Email`    | 标注值必须是格式正确的Email地址    |
| `@Length`   | 标注值字符串大小必须在指定的范围内 |
| `@NotEmpty` | 标注值字符串不能是空字符串         |
| `@Range`    | 标注值必须在指定的范围内           |

Spring 4.0版本已经拥有自己独立的数据校验框架，同时支持JSR 303标准的校验框架。Spring在进行数据绑定时，可同时调用校验框架完成数据校验工作。在SpringMVC中，可直接通过注解驱动`@EnableWebMvc`的方式进行数据校验。

Spring的LocalValidatorFactoryBean既实现了Spring的Validator接口，也实现了JSR 303的Validator接口。只要在Spring容器中定义了一个LocalValidatorFactoryBean，即可将其注入到需要数据校验的Bean中。Spring本身并没有提供JSR 303的实现，所以必须将JSR 303的实现者的jar包放到类路径下。

**配置@EnableWebMvc注解后，SpringMVC会默认装配好一个LocalValidatorFactoryBean，通过在处理方法的形参上标注`@Validated`注解即可让SpringMVC在完成数据绑定后执行数据校验工作。**





**操作演示**

1. **引入依赖**

```java
<!-- 校验注解实现-->
<!-- https://mvnrepository.com/artifact/org.hibernate.validator/hibernate-validator -->
<dependency>
  <groupId>org.hibernate.validator</groupId>
  <artifactId>hibernate-validator</artifactId>
  <version>8.0.0.Final</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.hibernate.validator/hibernate-validator-annotation-processor -->
<dependency>
  <groupId>org.hibernate.validator</groupId>
  <artifactId>hibernate-validator-annotation-processor</artifactId>
  <version>8.0.0.Final</version>
</dependency>
```





2. **创建实体类，声明校验注解**

创建一个实体类，如User，给类中的属性使用注解进行限制，比如要去新建一个用户时，我们要去保证传入的name属性不能为空字符串，password长度至少为6，age属性值至少为1，email要符合邮箱的格式，birthday是过去的日期格式。

```java
@Data
public class User {
    //name不能为null与空字符串
    @NotBlank
    private String name;

    //长度大于等于6
    @Length(min = 6)
    private String password;

    //大于等于1
    @Min(1)
    private Integer age;

    //符合email邮箱格式
    @Email
    private String email;

    //生日，必须是过去时间
    @Past
    private Date birthday;
}
```



3. **在handler方法的形参上使用@Validated注解**

在handler方法中，使用对象类型接收请求参数（这里的请求参数可以是param类型，也可以是json类型），我们需要使用`@Validated`注解去修饰这个对象，表示当对象接收到参数并转换成属性后，要对属性进行校验，校验是否符合在类中属性所设置的限制。

```java
@Controller
@RequestMapping("/user")
public class UserController {
    
    //添加用户数据
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public User addUser(@Validated @RequestBody User user){
        System.out.println(user);
        return user;
    }
    
}
```





**测试**

访问/user/add路径，若传入的参数均符合User类中属性的设定的要求时，就会正常执行：

<img src=".\images\image-20240610125641931.png" alt="image-20240610125641931" style="zoom:67%;" /> 

当传入的参数不符合设定的要求时，比如我这里将email属性设置成了非邮箱格式时，就会报错给前端：

<img src=".\images\image-20240610125740041.png" alt="image-20240610125740041" style="zoom:67%;" />   





**问题**

这里存在一个问题，当前端发送的请求并不能满足我们所设置的参数需求时，就去报错，但是这个报错是直接响应给前端的，直接在页面上显示出来，这样是有问题的。我们的需求是，我们对报错进行处理，返回我们设置的信息给前端，而不是仅仅错误的信息，比如我们想要返回错误码，或者一些错误提示，此时就需要对异常进行处理。



**优化**

将异常使用**全局异常处理机制**接收，让原本给前端报的错误使用异常处理方法进行接收后，返回我们想要返回的错误信息，而不是仅仅是一个错误。

在全局异常处理类中，声明一个方法，如handleValidException()，该方法就是去映射请求传参异常的处理方法。

在该方法上，使用@ExceptionHandler()方法，映射指定的异常：**`MethodArgumentNotValidException`**。

此时，当请求参数不符合我们所设定的要求时，会报MethodArgumentNotValidException异常，此时会去全局异常处理类中寻找是否有匹配的异常处理方法，就能够找到该handleValidException()方法，从而实际执行该方法中的逻辑，不会再直接报一个异常给前端。

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public String handleValidException(MethodArgumentNotValidException e){
        return "request param is wrong!";
    }
}
```

注意，该类需要Springmvc中的扫描配置扫描到。

此时，我们再去发送不满足请求参数要求的请求：

<img src=".\images\image-20240610133644214.png" alt="image-20240610133644214" style="zoom:67%;" /> 

此时报错会映射到异常处理类中的处理方法，不会再去直接报错给前端，而是去执行异常处理方法。





---

# 六、乱码问题

## 1、响应数据中文乱码问题

**问题重现：**

我们要去响应给页面一些中文字符，如：

```java
@Controller
public class InterceptorController {
    @RequestMapping(value = "/test")
    @ResponseBody
    public String test(){
        return "你好，springmvc!";
    }
}
```

去访问/test路径，页面显示结果：

![image-20240609232240192](.\images\image-20240609232240192.png) 

此时出现了中文乱码问题。



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

此时，浏览器就会根据响应头中的Content-Type属性，使用指定的MIME类型与字符集对响应体进行解码解析。此时响应体就是正常编码的。

但是在SpringMVC中，handler方法如果每次都要去获取response对象就过于麻烦，而且SpringMVC的响应体内容是放在方法的返回值中的，不是通过response对象手动设置的，所以在SpringMVC中，不能通过Servlet的方式调用response的方法设置。

在SpringMVC中，在handler方法的@RequestMapping注解中，使用`prodeces`属性设置响应体的Content-Type类型，如：

```java
@Controller
public class InterceptorController {
    @RequestMapping(value = "/test", produces="text/html;charset=utf-8")
    @ResponseBody
    public String test(){
        return "你好，springmvc!";
    }
}
```

此时，就将响应头中的Content-Type属性设置为了text/html;charset=utf-8，那么浏览器在接收到响应信息后，就知道服务端返回给我的响应体是html文本，使用的字符集是utf-8类型，会根据上述的类型进行解析，就不会出现乱码问题了。

访问该路径，显示结果：

![image-20240609233259210](.\images\image-20240609233259210.png) 





## 2、Tomcat控制台中文乱码

### 日志乱码

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

此时Tomcat的控制台的日志信息就不会是乱码了。





### 打印乱码

**问题重现：**

解决日志信息乱码问题后，控制台打印的乱码却无法解决。若我们使用sout语句，往控制台中打印中文字符，依旧会是乱码，如：

```java
@Controller
public class InterceptorController {
    @RequestMapping(value = "/test")
    @ResponseBody
    public void test(){
        System.out.println("你好！springmvc！");
    }
}
```

发送请求，控制台打印结果：

![image-20240609235928716](.\images\image-20240609235928716.png) 

为什么此时，控制台打印的结果依然是乱码，但是控制台上的其他日志信息却不是乱码呢？

这其实因为sout打印数据，是基于GBK进行编码的，出现了乱码问题我们就需要将其设置为UTF-8。





**问题解决：**

修改项目Tomcat容器的配置，在Tomcat的VM options框中，设置：

```properties
-Dfile.encoding=utf-8
```

<img src=".\images\image-20240610001329750.png" alt="image-20240610001329750" style="zoom:50%;" /> 

这样，JVM去编译项目时，所使用的编码就是UTF-8。

这样就能够将使用UTF-8编码sout打印的中文字符。

那么重新启动Tomcat容器，发送请求，此时控制台的打印结果为：

<img src=".\images\image-20240610001538845.png" alt="image-20240610001538845" style="zoom: 80%;" /> 

此时使用sout往控制台中打印的数据就不会出现中文乱码了。





---

# 七、总结

| 核心点          | 掌握目标                                     |
| --------------- | -------------------------------------------- |
| springmvc框架   | 主要作用、核心组件、调用流程                 |
| `简化参数接收`  | `路径设计、参数接收、请求头接收、cookie接收` |
| `简化数据响应`  | `模板页面、转发和重定向、JSON数据、静态资源` |
| restful风格设计 | 主要作用、具体规范、请求方式和请求参数选择   |
| 功能扩展        | 全局异常处理、拦截器、参数校验注解           |











