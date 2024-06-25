[TOC]



# 一、介绍与Maven的安装配置

## 1、 Maven介绍

Maven是一款为Java项目构建管理、依赖管理的工具（软件），使用Maven可以自动化构建、测试、打包和发布项目，大大提高了开发效率和质量。

总结：Maven就是一个软件，掌握软件安装、配置、以及基本功能（**`项目构建、依赖管理`**）使用就是本课程的主要目标。

## 2、主要作用理解

**1、场景概念**

* 场景1：例如我们项目需要第三方库（依赖），如Druid连接池、MySQL数据库驱动和Jackson等。那么我们可以将需要的依赖项信息编写到Maven工程的配置文件，Maven软件就会自动下载并复制这些依赖项到项目中，也会自动下载依赖需要的依赖，确保依赖版本正确无冲突和依赖完整。
* 场景2：项目开发完成后，想要将项目打成.war文件，并部署到服务器中运行，使用Maven软件，我们可以通过一行构建命令（mvn package）快速项目构建和打包，节省大量时间。

**2、依赖管理**

Maven可以管理项目的依赖，包括自动下载所需依赖库、自动下载依赖需要的依赖并且保证版本没有冲突、依赖版本管理等。通过Maven，我们可以方便地维护项目所依赖的外部库，而我们仅仅需要编写配置即可。

**3、构建管理**

`项目构建是指将源代码、配置文件、资源文件等转化为能够运行或部署的应用程序或库的过程。`

Maven可以管理项目的编译、测试、打包、部署等构建过程。通过实现标准的构建生命周期，Maven可以确保每一个构建过程都遵循同样的规则和最佳实践。同时，Maven的插件机制也使得开发者可以对构建过程进行扩展和定制。主动触发构建，只需要简单的命令操作即可。

![img](.\images\image.png)



## 3、GAVP属性

**`GAV指的是项目的标识信息，P表示项目打包使用的类型（有jar、war等）`**

Maven工程相对之前的工程，多出一组gavp属性，gav需要我们在创建项目时指定，p有默认值，后期通过配置文件修改。

MAVEN中的GAVP是指GroupId、ArtifactId、Version、Packaging等四个属性的缩写，其中前三个是必要的，而Packaging属性为可选项。这四个属性主要为每个项目在maven仓库中做一个表示，类似于人的姓名。有了具体的标识，方便maven软件对项目进行管理和互相引用。

**GAV遵循以下规则：**

1. **GroupId格式：**com.{公司}.业务线.[子业务线]，一般最多四级，表示的是maven工程所属的组织标识。例如：com.atguigu.java、com.taobao.tddl

2. **ArtifactId格式**：产品线名-模块名。表示的是maven工程下的模块标识。例如：bookstore、tair-tool

3. **Version版本号**，指的是项目迭代的版本，推荐使用格式为：`主版本号.此版本号.修订号` 例如：1.0.1

   1）主版本号：当做了不兼容的API修改，或者增加了能改变产品方向的新功能

   2）此版本号：当做了向下兼容的功能性新增（新增类、接口等）

   3）修订号：修复BUG，没有修改方法签名的功能增强，保持API兼容性

   例如：初始：1.0.0 修改BUG-> 1.0.1 功能新增 -> 1.1.1等

**Packing定义规则：**

指示将项目打包成什么类型的文件，idea根据packing值，识别maven项目类型。

* packing属性为`jar`（默认值），代表普通的java工程，打包以后是.jar结尾的文件。

* packing属性是`war`，代表的是Java的web工程，打包以后可以部署到Tomcat服务器，打包以后.war结尾的文件
* packing属性为`pom`，代表不会打包，用来做继承的父工程。



## 4、项目结构说明

maven是一个强大的构建工具，它提供一种标准化的项目结构，可以帮助开发者更容易地管理项目的依赖、构建、测试和发布等任务。

以下就是maven项目结构中，各种文件存放的位置：

```xml
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





## 5、Maven的安装与配置

选用的版本：3.6.3

| 发布时间   | maven版本 | jdk最低版本 |
| ---------- | --------- | ----------- |
| 2019-11-25 | 3.6.3     | Java 7      |

软件位于笔记的目录下。

1. **安装**

**安装条件**：maven需要本机安装java环境，必须包含java_home环境变量

<img src=".\images\image-20240506085700636.png" alt="image-20240506085700636" style="zoom: 67%;" />

**软件安装**：右键解压即可

**软件结构：**

![image-20240506085816818](.\images\image-20240506085816818.png)

* bin：含有maven的运行脚本
* boot：含有plexus-classworlds类加载器框架
* conf：包含maven的配置文件settings.xml
* lib：含有Maven运行时所需要的jave类库



2. **环境变量配置**

配置`maven_home`和`path`

maven_home配置的是maven软件的根路径

path配置的是maven软件中bin目录的地址

<img src=".\images\image-20240506091405309.png" alt="image-20240506091405309" style="zoom:67%;" />

<img src=".\images\image-20240506091538657.png" alt="image-20240506091538657" style="zoom:67%;" />

通过引用MAVEN_HOME的方式，去配置path。

配置完成之后，我们去测试一下，是否配置正确。

3. **命令测试**

```shell
mvn -v
```

<img src=".\images\image-20240506091731277.png" alt="image-20240506091731277" style="zoom:67%;" />

以上就表示配置成功。



4. **修改配置文件**

进入到maven文件中的conf目录下，在该目录中找到settings.xml文件，该文件就是一些maven的配置信息，我们需要去修改maven的一些默认配置。

> 我们需要修改的有三个配置：
>
> 1、依赖本地缓存位置（本地仓库地址），默认使用的是c盘下的当前用户目录下的.m2/repository的位置。
>
> 2、maven下载的镜像地址，maven默认是选择国外的镜像地址，该镜像地址下载速度比较慢，所以我们可以改成使用阿里镜像
>
> 3、选择编译项目的jdk版本，这里我们选择的版本是jdk17



a.修改本地仓库地址

原地址信息：（大约位于配置文件的55行处）

![image-20240506092749802](.\images\image-20240506092749802.png)

即，这里没有去显式地配置，是使用默认的地址，默认存放在当前用户文件夹下的.m2目录下的repository目录中

这里我们将其修改成D盘下的repo目录中

```xml
<localRepository>D:\repo</localRepository>
```



b.配置国内阿里镜像

```xml
<!--在mirrors节点(标签)下添加中央仓库镜像 160行附近-->
<mirror>
    <id>alimaven</id>
    <name>aliyun maven</name>
    <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    <mirrorOf>central</mirrorOf>
</mirror>
```

这是一个mirror标签，mirror标签要放在mirros标签中，大概位于160行左右，找到<mirrors>标签，将上面配置阿里镜像的mirror标签放在mirrors标签中。

![image-20240506093835549](.\images\image-20240506093835549.png)







c.配置jdk17版本项目构建

```xml
<!--在profiles节点(标签)下添加jdk编译版本 188行附近-->
<profile>
    <id>jdk-17</id>
    <activation>
      <activeByDefault>true</activeByDefault>
      <jdk>17</jdk>
    </activation>
    <properties>
      <maven.compiler.source>17</maven.compiler.source>
      <maven.compiler.target>17</maven.compiler.target>
      <maven.compiler.compilerVersion>17</maven.compiler.compilerVersion>
    </properties>
</profile>
```

profile标签需要放在profiles标签中，找到profiles标签，大概在188行左右，将上述的profile配置放在profiles标签内，就配置完毕了。

![image-20240506093809359](.\images\image-20240506093809359.png)





5. **idea配置本地maven**

我们进入到idea的设置中，找到maven：

<img src=".\images\image-20240506094348273.png" alt="image-20240506094348273" style="zoom: 50%;" />

IDEA默认使用的是自带的maven，我们将其改成我们我们自己配置的maven：

<img src=".\images\image-20240506094502200.png" alt="image-20240506094502200" style="zoom:50%;" />

选择我们下载的maven软件根目录。

<img src=".\images\image-20240506094539488.png" alt="image-20240506094539488" style="zoom:50%;" />

选择后，我们可以看到，这里的Local repository就变成了我们刚刚在settings.xml中配置的仓库地址。（如果没有改变，说明配置有误，需要重新去配置）

然后选择OK，idea配置本地maven结束。

需要注意的是，我们每次去创建新的项目，都需要去检查是否是使用本地的maven，因为在当前设置idea使用本地maven，有可能只对当前项目有效，对其他项目无效，所以当我们每次去创建新的项目，都去检查是否是本地的maven。





---

# 二、基于IDEA的Maven工程创建



## 1、创建JavaSE工程

在项目中去create module：

<img src=".\images\image-20240506110043576.png" alt="image-20240506110043576" style="zoom:67%;" />

选择Java，然后选择Maven。这里的Maven表示使用Maven去构建Java工程，下方有Maven属性的设置，包含GroupId和ArtifactId信息。

此处省略的version，vision在创建项目时会给一个默认值`<version>1.0-SNAPSHOT</version>`，在创建完项目后，可以在pom.xml中对其进行修改。

GroupId是组织名（包含公司名，也可包含业务名）

ArtifactId默认等于模块名，也可以不相等。

点击CREATE，这样我们的JavaSE工程就创建好了：

![image-20240506110832148](.\images\image-20240506110832148.png)

依赖的信息都存放在pom.xml文件中，我们打开pom.xml文件：

<img src=".\images\image-20240506110925504.png" alt="image-20240506110925504" style="zoom:67%;" />

就可以看到Maven的GAV属性，这里少了一个Packaging属性，不写默认是jar包，我们也可以写上：

<img src=".\images\image-20240506111110852.png" alt="image-20240506111110852" style="zoom: 67%;" />





注意：每次创建完项目之后，我们都要去检查项目是否使用了本地的Maven：

<img src=".\images\image-20240506110729050.png" alt="image-20240506110729050" style="zoom: 50%;" />

不是的话，需要将其修改成本地的Maven。



## 2、创建JavaEE工程

1. **手动方式**

   a.创建一个JavaSE的工程

   <img src=".\images\image-20240506111659575.png" alt="image-20240506111659575" style="zoom: 50%;" />

   ​	创建完成之后，再去手动地添加web项目结构文件。

   b.手动添加web项目结构文件

   ​	在创建的项目中，main目录下创建webapp目录，并在webapp目录下再创建WEB-INF目录，在WEB-INF目录下创建web.xml文件，web.xml文件中保存的信息是web项目的配置文件信息

   ![image-20240506112824376](.\images\image-20240506112824376.png)

   在web.xml中，先写入初始的模板信息：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
            version="4.0">
       
   </web-app>
   ```

   然后，我们需要将打包方式修改成war，在pom.xml中去修改

   <img src=".\images\image-20240506113037929.png" alt="image-20240506113037929" style="zoom:67%;" />

   最后，我们需要去刷新maven：

   ![image-20240506113220293](.\images\image-20240506113220293.png)

   刷新完毕之后，我们再去查看目录结构，就会发现webapp目录的图标中间多出了一个蓝点，此时就说明该项目已经是一个web项目了，即此时就是JavaEE工程。

   ![image-20240506113318389](.\images\image-20240506113318389.png)

   



2. **使用插件方式创建（推荐使用）**

   a.安装插件`JBLJavaToWeb`

   <img src=".\images\image-20240506113631192.png" alt="image-20240506113631192" style="zoom:50%;" />

   b. 创建一个JavaSE工程

   c. 使用插件，将JavaSE工程转变成JavaEE工程

   右键该module，选择JBLJavaToWeb：

   <img src=".\images\image-20240506113722863.png" alt="image-20240506113722863" style="zoom:67%;" />

   点击OK

   ![image-20240506113814676](.\images\image-20240506113814676.png)

   该插件就自动将JavaSE工程转变成了JavaEE工程了：

   <img src=".\images\image-20240506113855445.png" alt="image-20240506113855445" style="zoom:67%;" />

   打包方式也变成了war包：

   <img src=".\images\image-20240506114018193.png" alt="image-20240506114018193" style="zoom:67%;" />









---

# 三、核心功能：依赖管理和构建管理（:star2:）

## 1、依赖管理

### 1.1、依赖配置

Maven依赖管理是Maven软件中最重要的功能之一。Maven的依赖管理能够帮助开发人员自动解决软件包依赖问题，使得开发人员能够轻松地将其他开发人员开发的模块或第三方框架集成到自己的应用程序或模块中，避免出现版本冲突和依赖缺失等问题。

我们通过定义POM文件，Maven能够自动解析项目的依赖关系，并通过Maven仓库自动下载和管理依赖，从而避免了手动下载和管理依赖的繁琐工作和可能引起的版本冲突问题。

**`重点：编写pom.xml文件。`**

让我们来看一下这个pom.xml文件，当我们刚刚初始化好JavaEE项目，pom.xml文件是这样的：

![image-20240506122105422](.\images\image-20240506122105422.png)

其中的四个属性表示的是Maven工程的GAVP属性。

groupId和artifactId在创建完毕之后，就不可修改。

version表示版本信息，在我们进行部署的时候进行修改。

packaging表示maven工程的打包方式，普通java工程使用jar，web工程使用war，不打包则使用pom。



#### 依赖版本管理和添加

以下就是引入依赖的标准模板：

```xml
<dependencies>
    
    <dependency>
        <groupId></groupId>
        <artifactId></artifactId>
        <version></version>
    </dependency>
    
</dependencies>
```

**引入的依赖，全部都要放在`<dependencies>`标签中，dependencies表示依赖的集合。具体的依赖信息，存放在`<dependency>`标签中，一个dependency表示一个依赖。**

每个依赖中，又必须包含三个必要的属性gav。通过编写依赖jar包的gav必要属性，引入第三方依赖。即，**依赖中必须包含：**

* **`<groupId></groupId>`**
* **`<atrifactId></groupId>`**
* **`<version></version>`**

依赖中`<scope>`标签是可选的，表示指定依赖生效的范围，可以填写如下的值：

* compile

  默认值，如果没有指定scope值，该元素的默认值为compile，被依赖项目需要参与当前项目的编译、测试、打包、运行等阶段。

* provided

  被依赖项目可以参与编译、测试、运行等阶段，但是不参与打包阶段。

  例如开发一个web应用，编译时需要依赖Servlet-api.jar，但是在打包然后放在应用服务器运行的时候，我们不需要jar包，因为这个jar包已由应用服务器提供，如果打包的时候引入了这个jar包，就可能产生冲突，此时需要使用provided进行范围修饰。

  ```xml
  <dependency>                                 
      <groupId>org.projectlombok</groupId>     
      <artifactId>lombok</artifactId>          
      <version>1.18.24</version>               
      <scope>provided</scope>                  
  </dependency> 
  ```

* runtime

  表示被依赖的项目无需参与项目的编译，但是会参与到项目的测试和运行。

  例如在编译的时候我们不需要JDBC API的jar包，而在运行的时候我们才需要JDBC驱动包。

* test

  被依赖的项目仅仅参与测试工作，包括代码的编译、执行。

  test表示只能在src下的test文件夹下面才可以使用，如果在A项目中引入了这个依赖，在B项目引入了A项目作为依赖，在B项目中这个注解不会生效，因为scope为test时无法传递依赖。

  例如，最经典scope属性使用test的就是juint包：

  ```xml
  <dependency>                                   
      <groupId>org.junit.jupiter</groupId>       
      <artifactId>junit-jupiter-api</artifactId> 
      <version>5.9.1</version>                   
      <scope>test</scope>                        
  </dependency> 
  ```

* system

  system与provide元素类似，但是被依赖项不会从maven仓库中寻找，而是从本地系统中获取，SystemPath元素用于指定本地系统中Jar文件的路径。也就是说，在编译时使用SystemPath元素指定的Jar包，在打包的时候会排除这个Jar包，在运行时由应用服务器提供jar包。

如果不知道scope该使用哪一种属性，就直接使用默认值，全部生效就行。



完整的案例：

```xml
<dependencies>

    <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>1.2.17</version>
        <scope>runtime</scope>
    </dependency>

</dependencies>
```



我们知道了如何引入依赖，那我们该如何去查询想要的依赖呢？比如说，我们想要Spring框架的依赖，我们可以去maven仓库中去搜索，也可以使用插件搜索。

maven仓库官方地址：https://mvnrepository.com/

去maven仓库中搜索，比如我们去查找spring core的jar包，可以在该网站下搜索spring core

<img src=".\images\image-20240506123307192.png" alt="image-20240506123307192" style="zoom:50%;" />

然后进入第一个网站，在其中看一看哪个版本比较热门：

![image-20240506123402205](.\images\image-20240506123402205.png)

选择比较热门的版本，进入，里面就会有maven的配置信息，告诉你该如何引入：

![image-20240506123428537](.\images\image-20240506123428537.png)

此时，我们就可以将其复制到pom.xml文件中，引入该jar包。



我们也可以使用maven search插件：

<img src=".\images\image-20240506124004916.png" alt="image-20240506124004916" style="zoom: 50%;" />

下载好插件，然后在Tools中打开这个插件：

![image-20240506124042764](.\images\image-20240506124042764.png)

点击，然后搜搜我们想要的jar包：

<img src=".\images\image-20240506124144949.png" alt="image-20240506124144949" style="zoom:50%;" />

其中也有各个版本的热度，选择我们想要的版本，复制之后，粘贴到pom.xml中即可。



#### 依赖版本提取和维护

当我们引入很多很多依赖，对于版本号来说，如果全部放在每个依赖的`<dependency>`标签中的话，就比如对于spring框架来说，我们有spring-core依赖，spring-web依赖等等，对于这些依赖来说，如果我们想要去管理这些依赖的版本，比如全部都设置成6.0.6，此时如果要去dependency标签中修改的话，就特别麻烦，此时我们可以对版本进行统一的管理。



我们可以将版本号声明在`<properties>`标签中，在这个标签中，我们可以随意地定义版本标签名称，比如对于jackson的版本，我们可以定义为<jackson.version>，这里相当于声明了一个jackson.version变量，然后我们在`<dependency>`标签中，就可以引用刚刚声明的变量。

引用的方式为：**`${变量名}`**

注意：声明的标签建议两层以上命名

命名方式建议为：**`技术名.version`**

比如对于spring的版本，可以使用spring.version命名

例如：

```xml
<properties>
    <spring.version>6.0.6</spring.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-core</artifactId>
        <version>${spring.version}</version>
    </dependency>
</dependencies>
```

以上就是在properties标签中，声明了一个spring.version的变量，该变量用于设置spring的版本，之后，我们在给spring下的依赖设置版本时，就可以使用这个变量，这样我们就能够统一地管理和维护spring的版本。







### 1.2、依赖传递和依赖冲突

**依赖传递**指的是当一个模块或库A依赖于另一个模块或库B，而B又依赖于模块或库C，那么A会间接依赖于C。这种依赖传递结构可以形成一个依赖树。

上面这个定义比较抽象，举一个例子：

比如我们依赖于A，我们也依赖于B，也依赖于C，A依赖于B、依赖于C，此时，我们就直接只导入A，这样的话，由于依赖传递，在使用A的同时，A会去获取B和C，所以导入A就相当于导入了A、B和C，这样就不需要我们再去导入B和C了。

即：**`当我们引入一个库或框架时，构建工具会自动解析和加载其所有的直接和间接依赖，确保这些依赖都可用。`**

**作用：**

1. 减少重复依赖：当多个项目依赖于同一个库时，maven可以自动下载并且只下载一次该库。这样可以减少项目的构建时间和磁盘空间。
2. 自动管理依赖：maven可以自动管理依赖项，使用依赖传递，简化了依赖项的管理，使项目构建更加可靠和一致。
3. 确保依赖版本的正确性：通过传递依赖的管理，之间都不会存在版本兼容性问题，确保依赖的版本的正确性。



**依赖传递演示：**

比如，我们现在需要导入jackson的相关依赖，jsckson需要导入三个依赖，分别为：Jackson Databind、Jackson Core和Jackson Annotations：

<img src=".\images\image-20240506152021342.png" alt="image-20240506152021342" style="zoom:67%;" />

但是，我们可以去看看maven仓库中，jackson databind的相关信息。我们发现，jackson databind这个依赖，依赖于其他两个依赖（点开jackson databind的某一个版本，下划）：

![img](.\images\qwheriohoqiwehroirqhwe.png)

（注意，只有Compile Dependencies才是表示当前包的依赖信息，Test Dependencies并不是）

我们可以看到，jackson databind依赖于jackson-annotations和jackson-core，此时，当我们在项目中导入jackson-databind依赖时，相当于导入了三个依赖：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>
```

此时，我们去查看项目的依赖信息：

![image-20240506153521198](.\images\image-20240506153521198.png)

就可以看到三个依赖信息。下面的两个jackson-annotations包和jackson-core就是依赖传递过来的依赖jar包。



**那什么是依赖冲突呢？**

和依赖传递一样，依赖冲突是Maven的一种特性。

依赖冲突并不是一个问题，相反，依赖冲突能够帮助我们避免依赖重复的问题。

当直接引用或间接引用出现了相同的jar包，这时，一个项目就会出现重复的jar包，这算作冲突。依赖冲突避免出现重复依赖，并且终止依赖传递。

依赖冲突指的是：**`当已经存在依赖时，即此时有重复的依赖，会终止依赖传递，避免循环依赖和重复依赖的问题`**

即，由于maven存在依赖冲突的特性，所以当我们在项目中不用担心依赖重复问题。但是，对于重复的依赖，项目实际上使用的是哪个版本呢？

比如：A 依赖于 B的1.0版本

​	 C 依赖于 B的2.0版本

那么，导入A和C，此时，项目使用的是B的1.0版本还是2.0版本呢？

**依赖冲突的解决原则是：**

**第一原则：`谁短谁优先`**

这里的短指的是引用路径短

例如：

A -> C -> B 1.0（A依赖于C，C依赖于B的1.0版本）

F -> B 2.0（F依赖于B的2.0版本）

项目中导入A、F两个依赖，此时项目中的B就是2.0版本，因为F依赖于B 2.0的路径比较短，而如果使用第一个路径的话，还需要导入C，所以选择的是第二个路径。

但是，如果两条路径一样长怎么办，还有第二个原则：

**第二原则：`谁上谁优先`**

这里的上，指的是在`<dependencies>`标签的声明顺序

例如：

A -> B 1.0

F -> B 2.0

项目中导入A、F两个依赖，此时A依赖的声明放在F依赖的上面，那么此时得到的B是1.0的版本，因为这两个传递依赖的路径长度一样，但是A依赖的声明放在上面，所以此时选择的B版本就是1.0的版本。



**案例：**

```
A 1.1 -> B 1.1 -> C 1.1
F 2.2 -> B 2.2
```

依赖传递关系如上所示，此时项目中导入A1.1和F2.2，那么，此时项目中的依赖有：F 2.2、 A 1.1、 B 2.2

那么问：此时有依赖C 1.1吗？

答案是没有的。上述依赖关系存在依赖冲突，由于F 2.2 -> B 2.2这段依赖传递更短，所以此时导入的B版本是2.2版本，第一段依赖传递发生了依赖冲突，**只要发生了冲突，后续的依赖传递都会终止**。所以，C 1.1不会传递。







### 1.3、依赖导入失败的场景和解决方案

在使用Maven构建项目时，可能会发生依赖项下载错误的情况，主要原因有以下几种：

1. 下载依赖时出现网络故障或仓库服务器宕机等原因，导致无法连接至Maven仓库，从而无法下载依赖。
2. 依赖项的版本号或配置文件中的版本号错误，或者依赖项没有正确定义，导致Maven下载的依赖项与实际需要的不一致，从而引发错误。
3. 本地Maven仓库或缓存被污染或损坏，导致Maven无法正确地使用现有的依赖项，并且也无法重新下载。

解决方案：

1. 检查网络连接和maven仓库服务器状态。

2. 确保依赖项的版本号与项目对应的版本号匹配，并检查POM文件中的依赖项是否正确。比如pom文件是否标红，依赖是否存在红色波浪形，存在这些问题说明是导入的信息有误，需要重新去检查。

   <img src=".\images\image-20240506161943312.png" alt="image-20240506161943312" style="zoom: 25%;" />

3. 我们对这种问题进行一下描述：我们访问jar包的正确形式是这样的，我们先去访问本地仓库，当本地仓库中含有，就返回；如果本地仓库中没有，就去远程仓库中下载到本地仓库，然后再从本地仓库中返回。

   当本地仓库中没有该依赖，并且也不去访问远程仓库下载时，此时就出现了本地仓库被污染的问题。这种问题出现的大部分原因，都是因为网络问题，依赖下载了一半没有下载完。

   此时，pom.xml文件中依赖的信息是正确的，但是就是不去下载依赖，依赖就是没有用。

   此时，可以去清除本地Maven仓库缓存（`.lastUpdated` 文件），因为只要存在lastupdated缓存文件，刷新也不会重新下载。本地仓库中，根据依赖的gav属性依次向下查找文件夹，最终删除内部的文件，刷新重新下载即可！

   例如：

   ```xml
   <dependency>
       <groupId>com.fasterxml.jackson.core</groupId>
       <artifactId>jackson-databind</artifactId>
       <version>2.15.2</version>
   </dependency>
   ```

   对于这个依赖来说，存放的地址就在本地仓库下的com/fasterxml/jackson/core/jackson-databind/2.15.2目录下，即：D:\repo\com\fasterxml\jackson\core\jackson-databind\2.15.2，这里的D:\repo是本地仓库地址，可以在settings配置文件中配置（之前讲过）

   我们找到这个目录下：

   <img src=".\images\image-20240506163105615.png" alt="image-20240506163105615" style="zoom: 50%;" />

   如果在这个目录下，存在.lastUpdate文件，删除这个文件，然后重新刷新maven，重新下载这个依赖，这样就可以解决问题。

   当然，如果你觉得这种一个一个地去将本地仓库中所有依赖的.lastUpdate文件删除太麻烦了，这里提供一个脚本，将本地仓库中所有的.lastUpdate文件删除。

   脚本文件是`清理maven错误缓存.bat`，已经放在当前目录下了，我们右键进行编辑，然后将其中的SET REPOSITORY_PATH修改成自己本地仓库地址即可，点击运行，就会自动去本地仓库中删除.lastUpdate文件。



## 2、构建管理

**构建的概念：**

项目构建是指将**`源代码、依赖库和资源文件等转换成可执行或可部署的应用程序`**的过程，在这个过程中包括**编译源代码**、**链接依赖库**、**打包**和**部署**等多个步骤。

即：将java源代码转变成可执行的应用程序的过程。

项目构建是软件开发过程中至关重要的一部分，它能够大大提高软件开发效率，使得开发人员能够更加专注于应用程序的开发和维护，而不必关心应用程序的构建细节。

![img](.\images\quiwoeruoiqwer.image)



### 两种构建方式

#### 命令方式构建（推荐）

**语法**：**`mvn 构建命令 构建命令...`**

| 命令              | 描述                                                  |
| ----------------- | ----------------------------------------------------- |
| **`mvn clean`**   | 清理编译或打包后的项目结构，删除target文件夹          |
| **`mvn compile`** | 编译项目，生成target目录，将.java文件编译成.class文件 |
| **`mvn test`**    | 执行测试源码（测试）                                  |
| **`mvn site`**    | 生成一个项目依赖信息的展示页面                        |
| **`mvn package`** | 打包项目，生成.jar或.war文件                          |
| **`mvn install`** | 打包后上传到maven本地仓库（本地部署）                 |
| **`mvn deploy`**  | 只打包，上传到maven私服仓库（私服部署）               |

* 清理，对应着mvn clean命令；

* 编译，对应着mvn compile命令；

* 测试，对应着mvn test命令；

* 报告，对应着mvn site命令；

* 打包，对应着mvn package命令；

* 部署，对应着mvn install、mvn deploy命令。

对于上述的mvn test以及mvn site命令来说，第一个命令是去执行test目录下的测试方法，这其实挺鸡肋的，我们可以自己直接去执行测试方法即可。第二个命令是去展示项目的依赖信息，我们也可以直接打开maven查看。

> 注意：
>
> 1. **命令执行，需要在项目的根路径下进行**，即与pom.xml平级的路径下，因为需要命令的执行需要使用到pom.xml文件。
>
> 2. 项目的**打包方式**必须是**`jar包`**的形式才能使用部署命令，即只有打包方式是jar包，项目是java普通工程才能部署。
>
>    如果打包方式是war包，说明项目是web工程，此时应该部署到web服务器中，而不是放到本地仓库。



使用命令方式进行构建，需要到项目的目录下，使用命令行的方式运行命令。

比如，我们在项目中创建一个java文件：

![image-20240506180839359](.\images\image-20240506180839359.png)

那我们使用的命令行位置就需要在该module的根目录下，我们可以右键项目

<img src=".\images\image-20240506180930663.png" alt="image-20240506180930663" style="zoom:67%;" />

选择Open Terminal at Current Maven Module Path，表示在该路径下打开命令行。

![image-20240506181505743](.\images\image-20240506181505743.png)

此时，就表示在指定的项目下进行命令方式构建。这里的项目是maven-javaee-project-3。

也可以直接打开IDEA下面的Terminal：

![image-20240509092846863](.\images\image-20240509092846863.png)

此时进入到的是项目最外层的根目录，此时，可以使用cd命令，进入我们想要进行构建的module项目的根目录中：

![image-20240509093044802](.\images\image-20240509093044802.png)



接下来，我们就来分别使用一下这些命令

* **`mvn clean`** 与 **`mvn compile`** 命令

  `mvn clean`表示将之前编译的结果清理掉，当我们去执行这个指令时，会将存放编译结果的target目录直接删除。

  比如，在命令中去执行`mvn clean`：

  ![image-20240506193410496](.\images\image-20240506193410496.png)

  可以发现，在该项目下，原本使用mvn compile创建的target目录就被删除了。

  ![image-20240506181418503](.\images\image-20240506181418503.png)

  `mvn compile`表示编译，是将源代码转换成可执行代码的过程。对于java来说，编译会将.java文件转换成.class文件。

  我们在该项目下执行该`mvn compile`命令：

  ![image-20240506193547110](.\images\image-20240506193547110.png)

  会将User.java文件编译成.class文件，并且生成一个target目录，编译后的文件存放在该目录下

  <img src=".\images\image-20240506181209625.png" alt="image-20240506181209625" style="zoom:67%;" />

这两个命令实际上一般会一起使用，即：

```shell
mvn clean compile
```

表示：将原本的编译信息清理之后，再进行编译。

这样就能够确保，之前编译过的信息不会影响到当前要进行的编译操作。

mvn的编译操作实际上是使用插件完成的：

![image-20240509093810485](.\images\image-20240509093810485.png)

即mvn clean命令使用的是maven-clean-plugin插件；mvn compile命令使用的是maven-resource-plugin与maven-compiler-plugin插件。





* **`mvn test`**

  该指令用于执行项目中的测试。

  此时，我们就可以去调用`mvn test`方法，去执行测试方法。

  由于我们需要先进行编译，然后才能执行，所以在mvn test命令执行中，实际上也会去调用mvn compile执行使用的插件（命令的执行实际上是使用插件来是实现的）先进行编译操作，然后再执行：

  ![image-20240509103819381](.\images\image-20240509103819381.png)

  在test目录下创建一个MavenTest类，MavenTest类中的信息如下所示：

  ```java
  public class MavenTest {
      @Test
      public void testAssert(){
          int a = 10;
          int b = 20;
          Assert.assertEquals(a+b, 20);
      }
  }
  ```
  
  这是一个断言类，当a+b != 20时，就会报AssertionError异常。
  
  先将该类进行编译mvn test-compile（该类存放在test目录下），然后再去执行mvn test，执行结果如下所示：
  
  ![image-20240506200149758](.\images\image-20240506200149758.png)
  
  这里显示了执行该测试类中的测试方法时的结果。
  
  这表示的是去执行了com.atguigu.maven.test.MavenTest文件，执行是失败的（filures），也就是报了一个AssertionError的错误

  

  > **注意：**
  >
  > 当我们使用`mvn test`对test目录下的测试类进行测试时，要求**测试类`必须以Test开头或者以Test结尾`，测试方法建议使用test开头，并且类名与方法名都做到`见名知意`。**

  例如：我们在test目录下创建了三个测试类，分别是：
  
  <img src=".\images\image-20240509103957889.png" alt="image-20240509103957889" style="zoom: 50%;" /><img src=".\images\image-20240509104019133.png" alt="image-20240509104019133" style="zoom: 67%;" /><img src=".\images\image-20240509104052494.png" alt="image-20240509104052494" style="zoom:50%;" />
  
  然后我们去执行mvn test命令，执行结果为：
  ![image-20240509104158934](.\images\image-20240509104158934.png)
  
  发现结果中只包含两个测试类的结果，另一个测试类hhTesth中的结果并没有显示。
  
  为什么？
  
  实际上就是因为测试类的命名是有误的。
  
  **要求`：测试类必须以Test开头或者以Test结尾。`**
  
  这样这样，test命令才能够识别该类为测试类，才能够在mvn test结果中输出该类的执行结果。





* **`mvn package`**

  打包操作，打包成jar包还是war包依据的是pom.xml文件中的packaging属性为jar还是war：

  ![image-20240509112814509](.\images\image-20240509112814509.png)

  我们将maven-javaee-project-3项目进行打包：

  ![image-20240509095023582](.\images\image-20240509095023582.png)

  打包完成后，就会在根目录下的target目录中，生成一个jar包，该jar包使用了**`项目名-版本号.jar`**的方式进行命名：

  ![image-20240509095131563](.\images\image-20240509095131563.png)

  打包后，我们就可以进行部署了。

  以上是将项目打包成jar包，也可以将项目打包成war包，此时pom.xml文件中的packaging属性需要设置成war：

  <img src=".\images\image-20240509115140478.png" alt="image-20240509115140478" style="zoom:67%;" />

  打包成war包，有可能会因为maven中war插件版本过低，导致与JDK不兼容的问题，此时在执行mvn package命令时会报错。此时需要去修改war插件的版本，引入兼容的war插件，具体内容请查看本章内容中的《插件配置》部分。

  <img src=".\images\image-20240509115209341.png" alt="image-20240509115209341" style="zoom:67%;" />





* **`mvn install`**

  使用mvn install，可以将指定的项目部署到maven本地仓库中，这样一来，其他的项目就可以通过部署项目的GAV信息，引用部署的项目。

  **注意：**如果要想让其他项目引用部署的项目，需要将项目打包成jar包的形式再进行部署。war包一般用于部署到服务器中。

  <img src=".\images\image-20240509114804620.png" alt="image-20240509114804620" style="zoom:67%;" />

  执行mvn install命令：

  ![image-20240509100229816](.\images\image-20240509100229816.png)

  这样，就是将maven-javaee-project-3项目部署到本地仓库中了。该项目的GAV属性：

  <img src=".\images\image-20240509100320686.png" alt="image-20240509100320686" style="zoom:67%;" />

  即，该项目应该位于`本地仓库根目录/com/atguigu/maven-javaee-project-3`目录下：

  ![image-20240509100457795](.\images\image-20240509100457795.png)

  在本地仓库中果真找到了该项目。





#### 可视化方式构建

Maven给我们提供了可视化的方式进行构建，在IDEA右侧的maven中，我们可以打开项目下的Lifecycle，这里有很多的命令，直接点击命令就可以使用：

<img src=".\images\image-20240509101218791.png" alt="image-20240509101218791" style="zoom:80%;" />





### 构建生命周期

现在来思考一个问题，当我们要将maven工程部署到本地仓库，那要经历几个过程？首先，数据部署，肯定要先对数据进行打包，打包之前就需要对数据进行编译，编译之后应该对数据进行测试，编译之前肯定是要去清理之前编译的数据，防止数据影响之后的编译。

所以，要将maven工程部署到本地仓库，应该经历：

清理 -> 编译 -> 测试 -> 打包 -> 部署

这五个过程

那是不是在部署的时候，应该使用到的命令是：

```shell
mvn clean compile test package install
```

这样是不是太麻烦了？5个步骤，就需要5个命令，这样太麻烦了。

好的，现在我们来看一看一个现象：

执行mvn compile命令：

<img src=".\images\image-20240509104945952.png" alt="image-20240509104945952" style="zoom:67%;" />

**构建命令实际上是通过插件执行来实现的**。mvn compile通过上述的resources、compile插件实现。

我们来执行一下mvn test命令：

<img src=".\images\image-20240509105111753.png" alt="image-20240509105111753" style="zoom: 67%;" />

可以发现，在mvn test命令中，实际上也调用了mvn compile命令中的两个插件，然后再去调用test命令自身的两个插件。

所以我们可以知道的是，在mvn test命令中，实际上是先去执行了mvn compile命令，然后再去执行测试命令。

我们也可以执行一下mvn package以及mvn install命令：

<img src=".\images\image-20240509105256523.png" alt="image-20240509105256523" style="zoom:67%;" />

<img src=".\images\image-20240509105321667.png" alt="image-20240509105321667" style="zoom:67%;" />

由上述可知，当我们执行构建的后续命令时，会将之前的命令都执行一遍，这样一来，就不需要我们显式地声明命令去执行了。

构建过程所有步骤包含命令：compile -> test -> package -> install/deploy

当我们执行上述的命令时，会自动触发之前的命令。

即这就是构建命令周期。

**构建命令周期：**

构建命令周期可以理解成是一组固定构建命令的有序集合，**`触发周期后的命令，会自动触发周期前的命令`**，也是一种简化构建思路。

* **清理周期**：主要是对项目编译生成文件进行清理

  包含命令：clean

* **默认周期**：定义了真正构建时所需要执行的所有步骤，它是生命周期中最核心的部分

  包含命令：compile - test - package - install/deploy

* **报告周期**

  包含命令：site

所以，由上述可知：

命令mvn test实际上执行的命令是mvn compile test；

命令mvn package实际上执行的命令是mvn compile test package；

命令mvn install实际上执行的命令是mvn compile test package install。

但是，由于上述的命令中都不包含清理，所以，构建的最佳命令前面还需要加一个clean，先去清理原先编译的信息，然后再去打包部署。



### 构建命令最佳使用方案

```
编译：mvn clean compile
打包：mvn clean package
本地部署：mvn clean install
```



### 周期、命令与插件的关系

我们根据之前的学习，我们可以知道：

一个周期可能使用到一个或多个命令，比如默认周期就包含了5个命令；一个命令中会使用到多个插件，实际上做事的是插件，插件去执行操作，真正构建的工具是插件。

那，周期、命令和插件的关系大致如下所示：

![image-20240509111249875](.\images\image-20240509111249875.png)





### 插件配置

maven都给我们提供了构建所需的插件，根据maven软件的不同，提供插件不同的版本：

<img src=".\images\image-20240509111646238.png" alt="image-20240509111646238" style="zoom:80%;" />

我们一般不会主动地去执行某一个插件，因为执行某一个插件是不充分的，执行时有可能会报错，因为一个命令有可能需要执行多个插件，并且触发插件之前可能还需要进行编译测试等等。触发插件一般都是通过命令触发的。

现在有一个场景：当我们要将项目打包成war包，此时，pom.xml文件中的packaging属性为war：

<img src=".\images\image-20240509113022577.png" alt="image-20240509113022577" style="zoom:67%;" />

然后我们使用mvn clean package命令进行打包：

```shell
mvn clean package
```

此时报了一个错误：

<img src=".\images\image-20240509113120343.png" alt="image-20240509113120343" style="zoom:67%;" />

为什么会出现这种情况？

原因在于maven自带的打成war包的插件：war，版本是2.2，版本太低，无法兼容JDK17版本：

![image-20240509113203261](.\images\image-20240509113203261.png)

此时，我们就需要配置一个高版本的war插件。

**该如何进行配置？**

进入到项目下的pom.xml文件，这里我们进入到的是maven-javaee-project-3项目（module）下的pom.xml文件中。

在`<dependencies>`标签下，创建一个`<build>`标签，并在`<build>`标签中创建`<plugins>`标签，在这个标签中就是用来存放所有插件配置的信息。

`<plugins>`标签中创建`<plugin>`标签，每个`<plugin>`标签，就表示一个插件的配置。

<img src=".\images\image-20240509113745137.png" alt="image-20240509113745137" style="zoom:67%;" /> 

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

<img src=".\images\image-20240509114125145.png" alt="image-20240509114125145" style="zoom:67%;" />

复制之后，可能会标红，因为还没有去下载，当我们进行打包操作后，会自动进行下载，打包操作之后也就不再标红。

然后，再使用war的方式进行打包：

```shell
mvn clean package
```

此时执行就不会出现错误了，将项目打包成war包存放到了target目录下：

<img src=".\images\image-20240509114213962.png" alt="image-20240509114213962" style="zoom: 67%;" />







---

# 四、Maven工程的继承和聚合特性

## 1、继承

> **一般来说，我们会将子工程中用到的所有依赖，全部先在父工程中进行声明，然后再到子工程中引入，这样方便我们对所有依赖的版本进行管理。**

**1.继承概念**

Maven继承是指在Maven的项目中，让一个项目从另一个项目中继承配置信息的机制。继承可以让给我们在多个项目中共享同一配置信息，简化项目的管理和维护工作。

![img](.\images\2390479847892137.png)

**2.继承作用**

**`作用：在父工程中统一管理项目中的依赖信息，进行统一版本管理。`**

它的背景是：

* 对一个比较大型的项目进行模块拆分。
* 一个project下面，创建了很多个module。
* 每一个module都需要配置自己的依赖信息。

它背后的需求是：

* 多个模块要使用同一个框架，它们应该是同一个版本，所以整个项目中使用的框架版本需要统一管理。
* 使用框架时所需要的jar包组合（或者说依赖信息组合）需要经过长期摸索和反复调试，最终确定一个可用组合。这个耗费很大精力总结出来的方案不应该在新的项目中重新摸索。

通过在父工程中为整个项目维护依赖信息的组合既保证了整个项目使用规范、准确的jar包；又能够将以往的经验沉淀下来，节约时间和精力。

即：

Maven的继承特性，能够让我们统一地管理依赖的版本，防止出现同一个父工程下多个子工程的依赖不一致问题，我们在父工程中去设置依赖的版本，在子工程中直接引用依赖，而不需要再设置版本号。

**如何去实现maven的继承特性呢？以下就是步骤：**

1. **首先，我们要去创建父工程：**

在项目下，创建一个新的module，这个module就是我们设置的父工程：

<img src=".\images\image-20240509131138738.png" alt="image-20240509131138738" style="zoom:50%;" />

我们把这个父工程命名为maven-pom-parent-04：

<img src=".\images\image-20240509131227021.png" alt="image-20240509131227021" style="zoom: 50%;" />

在这个父工程中，主要是用来做依赖继承的配置信息，这个父工程是不用来写代码的，此时，我们需要将这个父工程的打包方式，修改为**`pom`**类型。

在父工程的pom.xml增加packaging属性：

<img src=".\images\image-20240509131439860.png" alt="image-20240509131439860" style="zoom:67%;" />

**（一般情况下，父工程的打包方式都是pom，父工程不打包也不写代码，只做配置）**



2. **创建子工程**

在父工程的基础上，创建子工程。

<img src=".\images\image-20240509131714281.png" alt="image-20240509131714281" style="zoom:67%;" />

给子工程命名，例如show-user，我们可以看到，子工程的groupId默认是继承于父工程的，并且在parent栏会显示父工程的名字：

<img src=".\images\image-20240509131809960.png" alt="image-20240509131809960" style="zoom:50%;" />

创建出来的子工程，在其pom.xml配置文件中，会去声明其父工程的信息：

![image-20240509143059060](.\images\image-20240509143059060.png)

**`<parent>`标签就是用来指定继承父工程的gav属性。**

即`<parent>`标签就是用来指定父子关系的。

并且，在子工程中，没有groupId和version属性，说明**子工程中的`GV`属性是继承于父工程的**。当然，也可以自己去显式地定义，定义后会将父工程中的属性进行覆盖，但是一般都是继承于父工程的。

这样一来，父工程和子工程就创建好了，创建完成后，该怎么在父工程中，统一地对依赖版本进行维护呢？

3. **在父工程中声明版本信息**

使用**`dependencyManagement`标签**，该标签表示声明依赖，不会去下载依赖信息，可以被子工程继承版本号。

例如，声明jackson-core依赖和mysql的版本信息：

```xml
<dependencyManagement>
    <dependencies>
        
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
            <version>2.15.2</version>
        </dependency>

        
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.33</version>
        </dependency>
        
    </dependencies>
</dependencyManagement>
```

这样我们就在父工程的pom.xml中，声明了依赖的版本信息。

此时实际上是去声明版本信息，并不会去下载依赖，具体下载的依赖还是在子工程中进行下载。

所以，此时的父工程的maven中，没有导入相关的依赖：

<img src=".\images\image-20240509144608277.png" alt="image-20240509144608277" style="zoom: 67%;" />



4. **在子工程中引入依赖**

子工程中，实际上才是真正的引入依赖，会去下载依赖信息，而在父工程中，只是声明依赖，目的是声明依赖的版本。

我们在子工程中，引入依赖时，就不需要再去声明依赖的版本，因为依赖的版本已经在父工程中声明，我们所使用的所有依赖一般都需要先放在父工程中进行声明版本，然后再去子工程中引入。

比如，对于上述的两种依赖jackson-core以及mysql，在子工程中是这样引入的：

```xml
<dependencies>
    
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-core</artifactId>
    </dependency>

    
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    
</dependencies>
```

这里引入的两个**依赖就不需要声明`version`**了，这两个依赖的版本是**继承于父类中声明的依赖版本**。

此时子工程中，才会导入相关的依赖：

<img src=".\images\image-20240509144641632.png" alt="image-20240509144641632" style="zoom:67%;" />

这样一来，就使用到了maven继承的特性，对工程中的依赖版本在子父关系中进行了统一的管理。



如果在父工程中依赖有相应的版本，但是在子工程中引入的版本声明中，还去显式地声明了另一个版本，此时，子工程中声明的依赖版本，就会覆盖父工程声明的依赖版本。

比如，在父工程中mysql依赖版本是8.0.33，在子工程中声明的依赖版本是8.0.25，此时子工程实际引入的是8.0.25版本：

![image-20240509145127767](.\images\image-20240509145127767.png)

![image-20240509145110636](.\images\image-20240509145110636.png)

此时也会有虚线进行提醒。

但是，建议版本统一在父工程中进行管理，并且子工程都使用父工程中所声明的版本。



> 如果只在父工程中引入依赖，而不在子工程中引入，由于子工程也处于父工程中，所以在父工程中引入的依赖，子工程中也是可以使用的。
>
> 比如，我在父工程的pom.xml中引入spring-context的依赖，但是子工程中不包含该依赖，在子工程中也是可以使用该依赖下中的类信息的。原因就在于，在父工程中引入的依赖，在父工程下都可以使用。



## 2、聚合

**1、聚合概念**

Maven聚合是指将多个项目组织到一个父级项目中，通过触发父工程的构建，统一按顺序触发子工程构建的过程。

**2.聚合作用**

* 统一管理子项目构建：通过聚合，可以将多个子项目组织在一起，方便管理和维护。
* 优化构建顺序：通过聚合，可以对多个项目进行顺序控制，避免出现构建依赖混乱导致构建失败的情况。



一个项目下有很多的子项目，如果我们要去构建这个项目，并且对其中的子项目都进行构建，如果一个一个地去项目中进行构建，实在太过麻烦。所以，我们需要当我们对父项目进行构建时，能够帮助我们对其子项目也能进行构建，此时就需要使用到maven的聚合特性。

> 执行聚合项目的构建周期时，maven会首先解析模块的POM、分析要构建的模块、并计算出一个反应堆的构建顺序，然后根据这个顺序依次构建各个模块。反应堆是所有模块组成的一个构建结构。

当我们创建子工程时，在父工程的pom.xml中，会去声明`modules`标签，这个标签用于表示该工程的所有子工程，类似于子工程的pom.xml中的`parent`标签，就是parent标签是用于继承特性的，modules标签是用于聚合特性的：

<img src=".\images\image-20240509152331218.png" alt="image-20240509152331218" style="zoom:67%;" />

有了这个标签，当我们对父工程进行构建时，同时也会去构建子工程。

比如，在父工程中执行mvn clean命令：

```shell
mvn clean
```

![image-20240509152509902](.\images\image-20240509152509902.png)

我们可以看到，执行构建命令时，会先去构建父工程，然后再按照modules标签中声明的顺序，构建子工程。

因为子工程中的shop-user与shop-commodity之间还没有依赖的关系，所以构建时则按照其在modules标签中声明的顺序进行构建的。

比如，shop-user要使用shop-commodity模块，在shop-user中引用了shop-commodity模块，就必须先将shop-commodity部署到本地仓库，然后再引用，则此时就会先去构建shop-commodity模块，再去构建shop-user模块。

此时，如果我们自己一个一个地去构建工程，就需要考虑到工程中的引用问题，需要考虑构建的顺序问题。

但是，如果我们使用了maven聚合的特性，它会自动地为我们分析模块，按照合理的顺序进行构建。

所以，**maven聚合的特性，能够解析工程之间的关系，帮助我们快速地构建项目**。这是maven自带的功能。



> 由上述两种特性可知，继承和聚合的关系在子父工程中是存在的，继承特性是通过子工程中的**`parent`**标签来指定要继承的父工程，聚合特性是通过父工程中的**`modules`**标签来指定的要聚合的子工程。这两种关系是两个方面的补充和完善。
>
> * **继承是用来统一管理依赖的版本**
>
> * **聚合是用来快速地构建项目**









---

# 五、Maven实战：搭建微服务Maven工程架构

## 1、项目的需求与结构分析

需求：搭建一个电商平台项目，该平台包括用户服务、订单服务、通用工具模块等。

![img](.\images\qwerqwrqwerqwer.png)

子工程user-service与order-service都是web工程，使用war包

子工程common-service是普通工程，使用jar包

项目架构：

1.用户服务，负责处理用户相关的逻辑，例如用户信息的管理、用户注册、登录等。

2.订单服务：负责处理订单相关的逻辑，例如订单的创建、订单支付、退货、订单查看等。

3.通用模块：负责存储其他服务需要通用工具类，其他服务依赖此模块。

服务依赖：

1.用户服务（1.0.1）

* spring-context 6.0.6
* spring-core 6.0.6
* spring-beans 6.0.6
* jackson-databind / jackson-core / jackson-annotations 2.15.0

2.订单服务（1.0.1）

* shiro-core 1.10.1
* spring-context 6.0.6
* spring-core 6.0.6
* spring-beans 6.0.6

3.通用模块（1.0.1）

* commons-io 2.11.0



## 2、项目的搭建与统一构建

以下就是该项目的搭建过程：

1. **创建父工程与所有子工程**

创建父工程micro-shop：

<img src=".\images\12341234412easfasfdasd.png" alt="img" style="zoom:67%;" />

创建完成之后，我们来考虑一下：由于父工程并不用来运行程序，仅仅只是一个统领子工程的作用，所以，我们将父工程中的packaging属性修改成pom，并且可以删除其src目录（不运行程序）：

<img src=".\images\image-20240509164758395.png" alt="image-20240509164758395" style="zoom:67%;" />



之后在父工程下，创建三个子工程：user-service、order-service、common-service：

<img src=".\images\image2342134.png" alt="img" style="zoom: 80%;" />

<img src=".\images\imageqerqwerqwer.png" alt="img" style="zoom: 80%;" />

创建完成之后，我们需要将order-service子工程和user-service子工程修改为web工程，这两个工程不是普通工程。可以使用之前所学习的手动修改方式以及插件修改方式（前面有介绍），这里使用插件修改的方式：

<img src=".\images\image-20240509165112543.png" alt="image-20240509165112543" style="zoom:67%;" />

修改之后，这两个工程中的main目录下会自动添加webapp目录，webapp目录下是WEB-INF目录，WEB-INF目录下是web.xml文件，这个文件就是区分该工程是普通工程还是web工程的凭证。

并且，这两个工程中的packaging属性也会自动变为war，打包方式为war，即该工程打包后需要部署到WEB服务器中。

对于common-service工程来说，其是普通工程，所以无需修改为web工程。

在上面的需求中，各个子工程的版本都是1.0.1。由于子工程中的version是继承自父工程中的版本的，并且子工程的版本全部都一样，都是1.0.1，所以我们可以直接修改父工程的version：

<img src=".\images\image-20240509164211489.png" alt="image-20240509164211489" style="zoom: 50%;" />

并且，在每个子工程中，都使用了parent标签表示继承关系，并且parent标签中还包含了父工程中的GAV属性，所以我们还需要去每个子工程中的pom.xml中，修改parent标签的V属性，改成1.0.1：

<img src=".\images\image-20240509164442561.png" alt="image-20240509164442561" style="zoom: 67%;" />

当然，我们也可以在子工程中，单独设置version属性，让子工程中的版本覆盖父工程中的版本信息。



2. **在父工程中声明依赖**

在子工程中，要使用的依赖有：spring-context、spring-core、spring-beans、jackson-databind、jackson-core、jackson-annotations、shiro-core、commons-io这些依赖。

我们查看依赖仓库，可以得知：

spring-context依赖于：spring-aop、spring-beans、spring-core以及spring-expression；

jackson-databind依赖于：jackson-core、jackson-annotations：

所以，依据依赖传递，我们只需要依赖于spring-context，就同时依赖于spring-beans以及spring-core；依赖于jackson-databind就同时依赖于jackson-core以及jackson-annotations。

由此可知，对于上述的依赖，我们只需要声明其中的四个即可：

spring-context

jackson-databind

shiro-core

commons-io

**在父工程中声明依赖：**

```xml
<dependencyManagement>
    <dependencies>
        
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>6.0.6</version>
        </dependency>

        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.15.0</version>
        </dependency>

        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-core</artifactId>
            <version>1.10.1</version>
        </dependency>

        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.11.0</version>
        </dependency>
        
    </dependencies>
</dependencyManagement>
```

这只是声明依赖版本，并不会下载依赖，所以若此时maven仓库中不包含该依赖，则会报红；当后面在子工程中下载依赖后就会正常。



3. **在各个子工程中引入依赖**

**用户服务部分：**

* spring-context 6.0.6
* spring-core 6.0.6
* spring-beans 6.0.6
* jackson-databind / jackson-core / jackson-annotations 2.15.0

```xml
<dependencies>
  <dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
  </dependency>
</dependencies>
```

**订单服务部分：**

* shiro-core 1.10.1
* spring-context 6.0.6
* spring-core 6.0.6
* spring-beans 6.0.6

```xml
<dependencies>
  <dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-core</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
  </dependency>
</dependencies>
```

**通用模块：**

* commons-io 2.11.0

```xml
<dependencies>
    <dependency>
        <groupId>commons-io</groupId>
        <artifactId>commons-io</artifactId>
    </dependency>
</dependencies>
```

然后去刷新maven，去下载依赖。

下载完毕后，我们去查看一下依赖的信息：

<img src=".\images\image-20240509173658280.png" alt="image-20240509173658280" style="zoom:67%;" /><img src=".\images\image-20240509173709790.png" alt="image-20240509173709790" style="zoom:67%;" /><img src=".\images\image-20240509173719613.png" alt="image-20240509173719613" style="zoom:67%;" />

依赖信息无误。



4. **部署common-service，并在另两个子项目中引用该项目**

user-service和order-service两个项目中，还需要依赖于common-service，所以，我们需要将common-service项目部署到本地仓库中，并在另外两个项目中引入这个项目的依赖。

将common-service进行部署：

在common-service子项目的根路径下调用`mvn clean install`命令：

![image-20240509174109938](.\images\image-20240509174109938.png)

这样一来，该项目就以jar包的形式部署到本地仓库中了。

然后我们要在另外两个项目中引入这个依赖，引入依赖的方式与引入spring依赖以及其他依赖的方式一样，都是通过引入该依赖的GAV属性的方式。

common-service的GAV属性参考该项目中pom.xml中的设置：

<img src=".\images\image-20240509174257710.png" alt="image-20240509174257710" style="zoom:80%;" />

即：groupId为com.atguigu

artifactId为common-service

version为1.0.1

所以，在order-service、user-service中引入该依赖：

```xml
<dependency>
  <groupId>com.atguigu</groupId>
  <artifactId>common-service</artifactId>
  <version>1.0.1</version>
</dependency>
```

<img src=".\images\image-20240509174447332.png" alt="image-20240509174447332" style="zoom: 33%;" />

<img src=".\images\image-20240509174508757.png" alt="image-20240509174508757" style="zoom: 33%;" />

这样一来，maven项目的结构就搭建完毕了。





# 六、Maven核心掌握总结

| 核心点     | 掌握目标                                           |
| ---------- | -------------------------------------------------- |
| 安装       | maven安装、环境变量、maven配置文件修改             |
| 工程创建   | gavp属性理解、JavaSE/EE工程创建、项目结构          |
| `依赖管理` | `依赖添加、依赖传递、版本提取、导入依赖错误解决`   |
| 构建管理   | 构建过程、构建场景、构建周期等                     |
| 继承和聚合 | 理解继承和聚合作用、继承语法和实践、聚合语法和实践 |

