## @Override

@Override注解用于子类中重写的方法上，用来检测该方法是否满足重写方法的要求，即检测父类中是否有该被重写的方法，检测方法的返回值、权限修饰符、参数列表是否满足要求，要求如下：

1. 子类重写的方法必须和父类被重写的方法具有**`相同的方法名称、参数列表`**。

> ​	如果参数列表不同的话，会被判定与继承来的方法形成重载，而不是重写。所以参数列表一定要相同。

2. 子类重写的方法的返回值类型**`不能大于`**父类被重写的方法的返回值类型。（例如：Student < Person）

   > 注意：**如果返回值类型是基本数据类型或void，那么必须是相同的。**

3. 子类重写的方法使用的权限**`不能小于`**父类被重写的方法的范围权限。（public > protected > 缺省 > private）

   > 注意：①父类私有方法不能重写 ②跨包的父类缺省的方法也不能重写

4. 子类方法抛出的异常**`不能大于`**父类被重写方法的异常范围。

此外，子类与父类中同名同参数的方法必须同时声明为非static（即为重写），或者同时声明为static的（不是重写）。因为static方法是属于类的，子类无法覆盖父类的方法。



这个注解就算不写，只要满足要求，也是正确的方法覆盖重写。建议保留，这样编译器可以帮助我们检测格式，另外也可以让阅读源代码的程序员清晰地知道这是一个重写的方法。

使用案例：

比如新的手机增加来电显示头像的功能，代码如下：

```java
package com.atguigu.inherited.method;

public class Phone {
    public void sendMessage(){
        System.out.println("发短信");
    }
    public void call(){
        System.out.println("打电话");
    }
    public void showNum(){
        System.out.println("来电显示号码");
    }
}
```

```java
package com.atguigu.inherited.method;

//SmartPhone：智能手机
public class SmartPhone extends Phone{
    //重写父类的来电显示功能的方法
	@Override
    public void showNum(){
        //来电显示姓名和图片功能
        System.out.println("显示来电姓名");
        System.out.println("显示头像");
    }
    //重写父类的通话功能的方法
    @Override
    public void call() {
        System.out.println("语音通话 或 视频通话");
    }
}
```

