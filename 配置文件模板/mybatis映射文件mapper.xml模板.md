**映射文件的命名规则**

 - 表所对应的实体类的类名+Mapper.xml

   - 例如：表t_user，映射的实体类为User，所对应的映射文件为UserMapper.xml 

     因此一个映射文件对应一个实体类，对应一张表的操作

   - MyBatis映射文件用于编写SQL，访问以及操作表中的数据

   - MyBatis映射文件存放的位置是src/main/resources/mappers目录下

   

**MyBatis中可以面向接口操作数据，要保证两个一致：**

 - mapper接口的全类名和映射文件的命名空间（namespace）保持一致
 - mapper接口中方法的方法名和映射文件中编写SQL的标签的id属性保持一致

```xml
<?xml version="1.0" encoding="UTF-8" ?>  
<!DOCTYPE mapper  
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"  
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">  
<mapper namespace="com.atguigu.mybatis.mapper.UserMapper">
    
	<!--int insertUser();-->  
	<insert id="insertUser">  
		insert into t_user values(null,'张三','123',23,'女') 
	</insert>  
    
</mapper>
```

对应的mapper接口是在com.atguigu.mybatis.mapper包下的UserMapper接口：

```java
public interface UserMapper {
    /**
     * 添加用户信息
     * @return
     */
    public int insertUser();
}
```

