**Jackson的三个核心模块：**

* jackson-core：定义了低级的流式API，包括了JSON处理细节。
* jackson-annotations：包含了Jackson的注解。
* jackson-databind：实现了对象和JSON之间的转换，该包依赖上面两个包。



**使用步骤**

1. **引入jackson的依赖：**

```XML
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.0</version>
</dependency>
```

刷新后来看看这个依赖信息：

![image-20240528152727443](.\images\image-20240528152727443.png) 

可以看到，jackson-databind其实依赖于jackson-annotations和jackson-core两个jar包，所以我们引入这一个jar包就相当于引入了jackson的三个jar包。



2. **准备数据**

准备一个Employee类与Department类，在Employee类中，声明一个Department对象属性。

Department类

```java
public class Department {
    private String id;
    private String departmentName;

    //get()、set()、toString()方法与有参构造器
}
```

Employee类

```java
public class Employee {
    private String id;

    private String name;

    private Integer age;

    private double salary;

    private String email;

    private Department department;

    //有参构造器与get()、set()、toString()方法
}
```





3. **使用测试**

创建一个测试类，并且创建一个Employee对象

**将对象转换成json字符串**：创建`ObjectMapper`对象，调用ObjectMapper对象的**`writeValueAsString()`**方法，传入Employee对象数据

案例：

```java
public class JSONTest {
    @Test
    public void conventObjectToJson(){
        //创建Employee对象
        Department department = new Department("1", "技术部");
        Employee employee = new Employee("1","tom",24, 16000, "123@qq.com", department);


        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonString = objectMapper.writeValueAsString(employee);

            System.out.println(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
```

输出结果：

{"id":"1","name":"tom","age":24,"salary":16000.0,"email":"123@qq.com","department":{"id":"1","departmentName":"技术部"}}





**将json字符串转换成对象**：创建`ObjectMapper`对象，调用ObjectMapper对象的**`readValue()`**方法，传入两个参数，第一个参数是json字符串，第二个参数是要转换成的对象的Class类型

案例：

```java
public class JSONTest {

    @Test
    public void conventJsonToObject(){
        //json字符串
        String jsonStr = "{\"id\":\"1\",\"name\":\"tom\",\"age\":24,\"salary\":16000.0,\"email\":\"123@qq.com\",\"department\":{\"id\":\"1\",\"departmentName\":\"技术部\"}}";

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //将json字符串转换成对象
            Employee employee = objectMapper.readValue(jsonStr, Employee.class);
            
            System.out.println(employee);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
```

打印结果：

![image-20240528154410278](.\images\image-20240528154410278.png)

发现报错了，报错原因是转换成的类中没有空参构造器。

这里的Employee类与Department类中都没有空参构造器，都是使用里面的有参构造器创建对象的。

**`当我们将JSON字符串转换成对象时，需要确保对象所对应的类中包含空参构造器，并且对象中的属性如果也有引用数据类型的，也需要保证属性所对应的类中包含空参构造器`。**

当我们给Employee类与Department类中均添加空参构造器后，再去执行：

![image-20240528154826897](.\images\image-20240528154826897.png)

此时转换成功。