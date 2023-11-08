# Java权限修饰符详解

首先要理解：类中元素的权限修饰符范围一定是小于类本身的，因为连类本身都看不见，如何能够看见其中的元素呢？

对于Java权限修饰符，需要分开进行解释，对于除protected之外的权限修饰符很好理解。

## private、缺省与public

### private与public

private与public很好理解，private只有在本类中才可以进行访问

而public则是在所有类中均可以进行访问。

### 缺省

对于缺省而言，需要考虑的东西会多一些：

> 1. 如果是访问创建类本身的属性或方法，则只需要考虑创建类与当前类之间的位置关系：**如果是在同一个包下则可以访问。**
>
> 2. 如果是访问创建对象所属类的父类中的属性或方法，则还需要考虑父类、子类与当前类之间的位置关系：**只有当前类与父类、子类均在同一个包下，才可以通过子类对象去调用父类中的缺省属性或方法**

即此时还要去考虑子类中是否可以访问到父类中的属性或方法，当属性为缺省，子类与父类不在同一个包下，就算创建的子类实例所在的类与父类或者子类在同一个包中，也因为子类对象中无法访问到父类属性，而造成无法访问的情况。

案例：

```java
@Test
public void test3(){
	SubClass2 subClass2 = new SubClass2();
	//当前类与父类和子类均在同一个包下，且当前类与其在同一个包中，那么两个类中的缺省属性均可以访问
	System.out.println(subClass2.supDefault);
    

	//父类与子类均在同一个包下，但当前类与其不在同一个包中，则无法去访问子类或父类中的缺省属性
	SubClass4 subClass4 = new SubClass4();
//        System.out.println(subClass4.supDefault);报错，无法访问
    

	//当前类与父类在同一个包下，但是与子类不在同一个包下，无法通过子类去调用父类中的缺省属性
	SubClass subClass = new SubClass();
//        System.out.println(subClass.supDefault);报错，无法访问
    

	//当前类与子类在同一个包下，但是与父类不在同一个包下，无法通过子类去调用父类中的缺省属性
	SubClass3 subClass3 = new SubClass3();
//        System.out.println(subClass3.supDefault);报错，无法访问

}
```



## projected

在Java中，访问权限修饰符属于最最最基础的知识，protected修饰符只是其中一个，protected比public、default、private更值得深入思考。

在《Thinking in Java》中，protected的名称是`“继承访问权限”`，这也就是我们记忆中的protected：protected必须要有继承关系菜可以访问。但是你真的理解正确了吗？

先思考几个问题：

1. 同一个包中，子类对象能访问父类的protected方法吗？
2. 不同包下，在子类中创建该子类对象能够访问父类的protected方法吗？
3. 不同包下，在子类中创建父类对象能访问父类的protected方法吗？
4. 不同包下，在子类中创建另一个子类的对象能访问公共父类的protected方法吗？
5. 父类protected方法加上static修饰符又会如何？

《Thinking in Java》中有一句话：
“protected也提供包访问权限，也就是说，相同包内的其他类可以访问protected元素”，其实就是protected修饰符包含了default默认修饰符的权限，所以第一个问题有了答案，在同一个包中，普通类或者子类都可以访问基类的protected方法。



#### 父类为非静态protected修饰类

在非静态情况下，也要分为两种情况考虑：

1. <u>**直接通过创建父类对象的方式去调用父类中的protected实例：**</u>

   > **可以直接将父类中的protected实例看作是缺省实例，只有在同包中才可以进行调用。**

2. <u>**如果是通过子类对象的方式去调用父类中的protected实例：**</u>

   此时需要考虑三方因素：当前类、子类以及父类之间的关系。

   > 1. **在子类中通过该子类引用可以访问其父类的protected方法。**
   > 2. **在非子类或子类中通过其他子类的引用，需当前类与父类在同一个包中，才可访问其父类的protected方法。**

代码案例：

父类代码：

```java
package com.protectedaccess.parentpackage;

public class Parent {

    protected String protect = "protect field";
    
    protected void getMessage(){
        System.out.println("i am parent");
    }

}
```

**不同包下，在子类中通过父类引用不可以访问其protected方法**

无论是创建Parent对象还是通过多态创建Son1对象，只要Parent引用，则不可访问，编译器报错。

```java
package com.protectedaccess.parentpackage.sonpackage1;

import com.protectedaccess.parentpackage.Parent;

public class Son1 extends Parent{
    public static void main(String[] args) {
        Parent parent1 = new Parent();
        // parent1.getMessage();   错误

        Parent parent2 = new Son1();
        // parent2.getMessage();  错误
    }
}
```

**不同包下，在子类中通过该子类引用可以访问其protected方法**

子类中实际上把父类的方法继承下来了，可以通过该子类对象访问，也可以在子类方法中直接访问，还可以通过super关键字调用父类中的该方法。

```java
package com.protectedaccess.parentpackage.sonpackage1;

import com.protectedaccess.parentpackage.Parent;

public class Son1 extends Parent{
    public static void main(String[] args) {
        Son1 son1 = new Son1();
        son1.getMessage(); // 输出：i am parent,
    }
    private void message(){
        getMessage();  // 如果子类重写了该方法， 则输出重写方法中的内容
        super.getMessage(); // 输出父类该方法中的内容
    }
}
```

**不同包下，在子类中不能通过另一个子类引用访问共同基类的protected方法**

```java
package com.protectedaccess.parentpackage.sonpackage2;

import com.protectedaccess.parentpackage.Parent;

public class Son2 extends Parent {

}
```

注意Son2是另一个类，在Son1中创建Son2对象是无法访问protected方法的

```java
package com.protectedaccess.parentpackage.sonpackage1;

import com.protectedaccess.parentpackage.Parent;
import com.protectedaccess.parentpackage.sonpackage2.Son2;

public class Son1 extends Parent{
    public static void main(String[] args) {
        Son2 son2 = new Son2();
        // son2.getMessage(); 错误
    }
}
```



---

#### 父类为静态protected修饰类

因为对于父类静态实例是直接通过类进行访问的。

> **对于静态变量或静态方法而言，子类中可以直接访问，在不同包的非子类则不可访问。**

```java
package com.protectedaccess.parentpackage;

public class Parent {

    protected String protect = "protect field";

    protected static void getMessage(){
        System.out.println("i am parent");
    }
}
```

静态方法直接通过类名访问

**无论是否同一个包，在子类中均可直接访问**

```java
package com.protectedaccess.parentpackage.sonpackage1;

import com.protectedaccess.parentpackage.Parent;

public class Son3 extends Parent{
    public static void main(String[] args) {
        Parent.getMessage(); // 输出： i am parent
    }
}
```

**在不同包下，非子类不可访问**

```
package com.protectedaccess.parentpackage.sonpackage1;

import com.protectedaccess.parentpackage.Parent;

public class Son4{
    public static void main(String[] args) {
       // Parent.getMessage(); 错误
    }
}
```





## 子父类中有同名的属性特殊情况

1. 方法的重写意味着覆盖，在子类中就不会再有被重写的方法能够被调用，<font color="red">**被调用的同名方法一定是重写后的方法**</font>。

2. 对于同名属性来说则不同：

   属性不会被重写，就算有同名的属性也不会出现覆盖的情况，父类中的同名属性依旧是存在的，<font color="red">**使用父类中的方法访问的同名属性一定是父类中的属性**</font>。

   如果父类中的方法访问了父类中的属性，并且子类中没有对该方法重写，使用子类对象去调用的时候，访问的依旧是父类中的属性，而不是子类中同名的属性。

**案例1：**

父子类：

```java
class Parent{
    private String name = "Parent";
    public String getName() {
        return name;
    }
}
class Son extends Parent{
	private String name = "Son";
	public void test(){
        System.out.println(this.getName());
        System.out.println(super.getName());
    }
}
```

测试类：

```java
public class SuperTest {
    @Test
    public void test(){
        Son son  = new Son();
        son.test();
    }
}
```

输出结果：

```
Parent
Parent
```

这里的this.或者super.并不会影响结果，原因是子类中并没有去声明getName()方法，那么无论this.还是super.都会去Parent类中寻找getName()方法。

并且由于属性不会被覆盖，在父类中使用的getName()方法中访问的name属性根据就近原则实际上是父类中的name属性。



**案例2：**

父子类：

```java
class Parent{
    public void method(){
        System.out.println("父类中的method方法");
    }

    public void print(){
        method();
    }
}

class Son extends Parent{
    public void method(){
        System.out.println("子类中的method方法");
    }
}
```

测试类：

```java
public class SuperTest {
    @Test
    public void test(){
        Son son  = new Son();
        son.print();
    }
}
```

运行结果：

```
子类中的method方法
```

这里使用子类引用调用了继承于父类的print()方法，但是为什么这里实际上调用的method()方法却是子类中的呢？

原因就在于方法的重写，会覆盖掉原本父类中的被重写方法，子类中就找到了继承来的method()方法，所以使用子类对象调用的print()方法实际上调用的是重写的method()方法。
