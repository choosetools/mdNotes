# 浅析Java中dto、dao、service、controller的四层结构

一般开发大型项目时会将所有的功能细分为许多小模块，每个模块都有dto、dao、service和controller层，有些模块还会加入validate层。

先来看一个小模块的目录结构：

<img src=".\images\20180724194228397.png">

首先，最底层的就是**dto层**，dto就是所谓的model，dto中定义的是实体类，也就是.class文件，改文件中包含实体类的属性和对应属性的get、set方法；

 其次，是**dao层**（dao层的文件习惯以*Mapper命名），dao层会调用dto层，dao层中会定义实际使用到的方法，比如增删改查。一般在dao层下还会有个叫做sqlmap的包，该包下有xml文件，文件内容正是根据之前定义的方法而写的SQL语句；

之后，到了**service层**，service层会调用dao层和dto层，service层也会对数据进行一定的处理，比如条件判断和数据筛选等等；

最后，是**controller层**，controller层会调用前面三层，controller层一般会和前台的js文件进行数据的交互，controller层是前台数据的接收器，后台处理好的数据也是同controller层传递到前台显示的。



# 关于PO、BO、VO、DTO、DAO、POJO等概念的理解



