## 浅析Java项目架构体系

### Controller层：

本层定义接口并调用service层接口方法完成业务逻辑。

功能：

接受前端请求，调用service，接受service返回的数据，之后响应给客户端。

### Service层：

service层为业务服务，调用mapper层并提供给controller层使用，间接和数据库打交道。

项目结构包括两部分，接口文件和接口实现类文件，接口文件中定义在controller层中调用的service层方法；接口实现类文件中完成service层接口中定义的方法的实现。

*注意：这里接口实现类中方法的实现是指业务逻辑的实现，可能有些方法并不能在实现类里完成真正意义上的实现，还需要在mapper层文件完成其真正意义上的实现（主要是和数据库交互）。*

### Mapper层：

mapper层为操作数据库的一层。

mapper层分为两部分，mapper接口层和mapper.xml层。

mapper接口层：

定义在service接口中没有完成真正实现，需要通过书写SQL语句才能完成其功能的方法。

mapper.xml层：

（1）配置通用查询映射结果：

> ```xml
> <?xml version="1.0" encoding="UTF-8"?>
> <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
> <mapper namespace="com.nayun.cultourism.mapper.CultureTourismSituationMapper">
> 
>     <!-- 通用查询映射结果-->
>     <resultMap id="cultureTourismResultMap" type="com.nayun.cultourism.entity.CultureTourismSituation">
>         <result column="id" property="id"/>
>         <result column="name" property="name"/>
>         <result column="annual_flow" property="annualFlow"/>
>         <result column="position" property="position"/>
>         <result column="illustration" property="illustration"/>
>         <result column="pic_url" property="picUrl"/>
>         <result column="creat_user" property="createUser"/>
>         <result column="creat_dept" property="creatDept"/>
>         <result column="creat_time" property="creatTime"/>
>         <result column="update_user" property="updateUser"/>
>         <result column="update_time" property="updateTime"/>
>         <result column="status" property="status"/>
>         <result column="is_deleted" property="isDeleted"/>
>         <result column="address" property="address"/>
>     </resultMap>
> ```

*注意：*

- *namespace 和 resultMap 的 type 要指向正确的地址，namespace指向mapper文件，type指向实体类。*
- *column为数据库中的表字段，property为实体类中属性名（一一对应）。*

*关于column以及property命名：*

- *column为数据库表字段名，小写，各单词之间用下划线分割。*
- *property为实体类中属性名，命名一般符合驼峰形。*

（2）完成mapper接口中方法的SQL语句实现：

*在这个模块中就是书写具体的SQL语句了，将查询结果映射在方法中的resultMap中，id为在mapper接口中定义的方法名。*

```xml-dtd
<!--查询所有景点数据-->
    <select id="findAll" resultMap="cultureTourismResultMap">
        select * from cockpit_tourist_spots where is_deleted = 0
    </select>

    <select id="getList" resultMap="cultureTourismResultMap">
        select * from cockpit_tourist_spots where is_deleted = 0
        <if test="cultureTourismSituation.name != null and cultureTourismSituation.name != ''">
            and name LIKE CONCAT('%',#{cultureTourismSituation.name},'%')
        </if>
    </select>
```

### Entity层：

也就是所谓的model层，也称为pojo层，是数据库在项目中的类，该文件包含实体类的属性和对应属性的set、get方法。

实体类中属性同数据库表字段一一对应，对于相应的set、get方法一般不需要书写，实体类上引入@Data注解，会自动为实体类注入get、set以及toString方法，减少代码量。

### VO层：

视图对象，用于展示层，把某个指定页面的所有数据封装起来，方便前端获取数据，后端将前端需要 的数据做整合，打包成一个类。

使用场景：

如果在前端页面需要展示经过某些数据库操作才能展示的特定数据，一般在vo层中把操作过程中涉及的数据进行封装，方便前端获取。并在controller层定义对应接口时把返回类型规定为vo类。

### DTO层：

数据对象传输层，负责屏蔽后端实体层，将UI要的数据进行重新定义和封装。因为后端在实际业务场景中需要[存储](https://auth.huaweicloud.com/authui/saml/login?xAccountType=csdndev_IDP&isFirstLogin=false&service=https%3A%2F%2Factivity.huaweicloud.com%2Ffree_test%2Findex.html%3Futm_source%3Dhwc-csdn%26utm_medium%3Dshare-op%26utm_campaign%3D%26utm_content%3D%26utm_term%3D%26utm_adplace%3DAdPlace070851)大量数据，而用户需要的数据只是一部分，为了快速获取用户需要的数据，应该把用户经常用到的数据在dto层中进行封装，在调用服务层时，只需要调用一次便可完成所有的逻辑操作。

### 运行流程：

1. 控制层接收前端请求，调用对应的业务层接口方法
2. 业务层实现类去实现业务层接口
3. 业务层实现类的方法内调用数据层的接口
4. 数据层实现文件（mapper.xml）实现数据层接口
5. 然后处理结果层层返回

总的来说这样每层做什么的分类只是为了使业务逻辑更加清晰，写代码更加方便，所以有时候也需要根据具体情况来，但是大体的都是这样处理的，因为它其实就是提供一种规则，让你把相同类型的代码放在一起，这样就形成了层次，从而达到分层解耦、复用、便于测试和维护的目的。

---

## 关于PO、BO、VO、DTO、DAO、POJO等概念的理解

随着编程工业化水平的不断加深，各种编程模型层出不穷（比如MVC，MVP等等），伴随着这些编程模型，又有一大批新的概念蜂拥而至，什么VO，BO，PO，DO，DTO等，这些新概念一直一来都是云里雾里，网上虽然也有不少文章来区分这些概念，但很多都是互相矛盾。

接下来看一下一个企业级java项目的大致框架：

<img src=".\images\v2-24e3ed681c02b6434681719753c53b40_r.jpg" style="zoom:80%;">

### 一、POJO(Plain Ordinary Java Object 普通Java对象)

* **POJO是一个大的范围概念，包含了PO/DTO/BO/VO。**

实际上就是普通的JavaBeans，是为了避免和`EJB`（Enterprise Java Beans企业级JavaBeans）混淆所创造的简称，也称为（ Plain Old Java Object又普通又老的对象）。

相比于`EJB`来说，的确是老的对象，因为`ORM`中间件的日趋流行，POJO又重新焕发了光彩。

**什么是ORM？**

> ORM，全称Object Relational Mapping，中文叫做对象关系映射，通过ORM我们可以通过类的方式去操作数据库，而不用再写原生的SQL语句。通过把表映射成类，把行作实例，把字段作为属性，ORM在执行对象操作的时候最终还是会把对应的操作转换为数据库原生语句。

**POJO的内在含义是指？**

> **那些没有继承任何类、也没有实现任何接口，更没有被其他框架侵入的java对象。**

它**仅包含自身的属性以及自身属性的getter和setter方法**，这意味着POJO可以方便的从一个框架迁移到另一个框架中，或者框架升级也会对代码毫无影响，因而可以得到复用。

```java
//例如在该实体EJB中，实体包含业务逻辑，同时也包含自身的持久化逻辑
//当更换数据源，或改变中间件框架时，则需要修改大量代码
public class Customer {
	private Long id;
	private String name;
	private Set<Order> orders = new HashSet();
	//省略业务逻辑
	//数据库访问方法
	public void remove(){
		//通过不同方式访问数据库，例如JDBC，Mybaits，JPA
	}
	public Customer load(){...}
	public Customer create(){...}
}

//当改为POJO时，则可以运行在任一JAVA环境中
//以下这个类就是POJO类，不包含任何的业务逻辑代码，仅仅只含有属性以及属性的getter和setter方法
public class Customer {
	private Long id;
	private String name;
	private Set<Order> orders = new HashSet();
	//省略getter和setter
}
```

一般，当需要持久化对象时，人们喜欢将该对象放在名为xxxPOJO的目录中，当然，不建议这样命名，因为POJO实际上是包括BO/VO/PO/DTO/DO等一系列对象的总称。有的团队规定禁止命名为xxxPOJO。



### 二、DAO（data access object 数据访问对象）

包含对数据的访问，负责持久层的操作。通常需要结合PO来访问数据库，主要用来封装对数据的访问，并不转化成其他对象。

在基于“事务脚本”的业务设计时，它包含业务逻辑。否则，一般只包含持久化的封装。

```java
public interface Dao{
    int insert(User user);
    User selectById(long id);
}
```





### 三、PO（persistence object持久层对象）

`PO`实在`ORM`(对象关系映射)中与数据表的一条记录相匹配，自身属性与数据表字段一一对应。可以将数据表中的一条记录作为对象处理，并可以转换为其他对象。

> **在《阿里巴巴开发手册》中，`PO`也叫`DO`（Data Object)数据对象，与数据库结构一一对应，通过DAO层向上传输数据源对象。**

面对不同的数据源时，比如文档型数据库，对象型数据库等时，顾名思义PO是DAO层为进行持久化操作而准备的对象。

* 包含getter、setter方法。
* 不包含业务逻辑与数据库的访问方法，因为数据库本身不包含业务逻辑。
* PO平常不一定需要实现序列化，只是当采用分布式存储或者作为前端输出及远程调用使用时，应该实现序列化。

```java
//示例代码
public class User implements Serializable {
    //序列化版本，通过IDEA自动生成
    private static final long serialiVersionUID = 1L；
	private Long id;
    private String username;
    private String password;    
    //省略getter和setter方法
}
```

> **一般将PO与DO、Entity看作是同一种。**





### 四、DTO（Data Transfer Object）数据传输对象

这个传输通常指的是前后端之间的传输

#### **什么是DTO？**

**从输入来看**，在进行请求时，应用在接口接收传入对象，然后又转换成实体进行持久化。在此过程中，传入的对象就是DTO。

它的命名方式可能是Param、Query、Command等。Param为查询参数对象，适用于各层，一般用作接收前端参数对象。Param和Query的出现是为了不使用Map作为接收参数的对象。（因为Json可以转换成Map进行使用）

**从输出来看**，在进行返回响应时，若数据表有100个字段，那么PO中就有100个属性，而界面可能只需要其中10个属性，那么查询数据库后，对象就需要由PO转化成DTO。

DTO可能还需要组合多个表查询到的对象成为一个大对象，以便减少网络的调用（通过SQL进行联表，查询出来的数据封装到DTO中），或者给前端传输一些不在数据库中查到的属性，所以需要添加属性，此时就不能使用PO来存储。

```java
//示例代码，继承实体类，从查询到的PO中添加所需要的属性，可以用于获取前端传输的数据，也可以传输数据给前端
public class UserDTO extends User {
    //序列化版本
    private static final long serialiVersionUID = 2L;
    
    //用户标识
    private String username;
    
    //添加额外属性
    private HashMap<String, Object> extProperties;   
    
   //省略getter、setter方法
}
```

DTO也是只包含自身数据的存储，而不包含业务逻辑。

#### DTO怎么使用？

对于不同的客户端展现，可以选择一次封装所有可能的数据组成DTO，也可以为每种展现创建不同的DTO。

* 单个大DTO，减少调用次数，只用创建一次，但是难以掌握传输的数据。
* 不同的小DTO，要创建很多DTO，传输数据很清晰。

对于输入方和输出方，可以共用一个DTO，也可以各准备一个DTO。





### 五、VO(View Object 显示层对象)

View Object的含义是通常是Web向模板渲染引擎层传输的对象。《阿里巴巴开发手册》中建议把输出的显示层对象命名为VO。

```java
//Controller层
public HttpResult list(@RequestBody XXParam param){
    HttpResult vo=HttpResultUtils.convert(XXDTO);
    return vo;
}
```

由于很多页面需要额外的数据，比如提示信息、分页信息、错误码等等，查询的DTO之后需要再封装成View Object显示层对象再显示出来。

```json
{
	errcode: "00000",
	errmsg: "ok",
	data: {
		pageNum: 1, 
		pageSize: 10, 
		totalPage: 1, 
		total: 4, 
		list: [...]
	}
}
```



### 六、BO（bussiness object 业务对象）

**BO就是PO的组合。**

BO是一个业务对象，一类业务就会对应一个BO，数量上没有限制，而且BO会有很多业务操作，也就是说除了get、set方法以外，BO会有很多针对自身数据进行计算的方法。

比如一个简历，有教育经历、工作经历、社会关系等等。 我们可以把教育经历对应一个 PO ，工作经历对应一个 PO ，社会关系对应一个 PO 。 建立一个完整简历的 BO 对象处理简历，每个 BO 包含上面的这些 PO 。 这样处理业务逻辑时，我们就可以使用 BO 去处理。

BO与Service不同的是，它只包含基本的业务操作；而Service负责整个流程，一个业务流程可能会调用到多个BO。





## **总结：**

> * **一般来说，在web应用中前端传输给后端的是dto，后端返回给前端是VO，或者直接返回不封装的DTO。**
>
> * **VO和DTO比较类似，主要的区别是在设计思想方面；**
>   * **数据是为了在微服务之间传输时，我们使用DTO进行传输；**
>   * **为了返回给web、ID或者android进行展示时，我们使用VO返回。**
>
> * **VO是可以第一个优化掉的，展示业务不复杂可以不使用VO，可以直接用DTO返回。**
> * **PO类无法省略，不管是entity、DO还是PO，都是同一个概念，表示的是数据表中的字段所映射的类**。
> * **各种类所存放的位置：**
>   * **PO类一般存放在entity包或者model包下；**
>   * **DTO类一般存放在dto包下；**
>   * **VO有时存放在vo包下，也可能存放在dto包下（主要看项目是否对这方面特别严谨）；**
>   * **DAO放在dao包下；BO类放在bo包下。**
>
> 
