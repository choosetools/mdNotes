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

子父类同名属性和方法之间处理有些区别：

若子父类中有同名的属性，在访问时，如何确定访问的是哪一个类中的属性？

1. > <font color="red">**通过`对象.属性`的方式访问的属性，由该引用变量所属的类决定。**</font>属于哪一个类，依据就近原则访问的就是哪一个类中的属性。

案例：

```
public class Person{
    public int age = 1;
}

public class Student extends Person{
    public int age = 2;
}

//测试类：
public class PolymorphismTest {
   @Test
    public void test(){
        Person person = new Student();
        System.out.println(person.age);
        
        Student student = (Student) person;
        System.out.println(student.age);
    }
}

```

运行结果：

```
1
2
```

Person类与Student类中有同名的属性age，使用对象.属性的方式访问类中的属性，与对象所属的类有关。

本例中，先使用Person类的引用指向了Student类对象，之后通过向下转型，让person强转转换成了Student类。

所以，先获取到的是Person类中的age属性，后由于向下转型，转换成了Student类，获取到的就是Student类中的age属性。



2. > <font color="red">**通过`对象.方法名`的方式执行类中的方法，方法去访问的属性，由执行的方法所属的类决定。**</font>方法属于哪一个类，依据就近原则，访问的就是哪一个类中的属性。

案例1：

```java
public class Person{
    public int age = 1;
    
    public int getAge() {
        return age;
    }
}
public class Student extends Person{
    public int age = 2;

    @Override
    public int getAge() {
        return age;
    }
}

//测试类：
public class PolymorphismTest {
	@Test
    public void test5(){
        Person person = new Student();
        System.out.println(person.getAge());
    }
}
```

运行结果：

```
2
```

Person类的引用指向了Student类的对象，在Student类中重写了Person中的getAge()方法，所以使用person.getAge()实际上调用的是Student类中的getAge()方法，根据就近原则，此时在方法中访问的age属性那实际上就是Student类中的age属性了。



案例2：

```
public class Animal {
    public int age = 3;
}

public class Person extends Animal{
    public int getAge() {
        return age;
    }
}

public class Student extends Person{
    public int age = 2;
}
```

测试类：

```
public class PolymorphismTest {
    @Test
    public void test(){
        Person person = new Student();
        System.out.println(person.getAge());
    }
}
```

运行结果：

```
3
```

这里的运行结果为什么是3呢？

原因：

在Student类中没有对Person类中的getAge()方法进行重写，实际调用的就是Person类中的getAge()方法，即依据就近原则，getAge()方法访问的age属性即为Person类中的age属性，但是Person类中没有声明的age属性，其age属性来源于Animal类中继承而来的，所以访问即是继承而来的age属性。



故：

**不要在子父类中声明同名的属性！**

---

## 问：子类能够继承父类的private属性或方法吗？

> **答：子类继承父类，子类拥有了父类的所有属性和方法。**

程序验证：父类的私有属性和方法子类是无法直接访问的。当然私有属性可以通过public修饰的getter和setter方法访问达到的，但是私有方法不行。

假设：子类不能够继承父类的私有属性和方法

那么：分析内存后，会发现:

当一个子类被实例化的时候，***默认会先调用父类的构造方法对父类进行初始化***，即在内存中创建一个父类对象，然后再在父类对象的外部放上子类独有的属性，两者合起来成为一个子类的对象。

所以：子类继承了父类的所有属性和方法或子类拥有父类的所有属性和方法是对的，只不过父类的私有属性和方法，子类是无法直接访问到的。**<font color="red">即只是拥有，但是无法使用</font>**。

子类中无法访问到父类中private类型的属性与方法（如果子类在其他包中，同样无法访问父类中缺省类型的属性与方法），但是可以通过其他可以查询到的方法（例如public类型的）去访问private类型的属性与方法。

同样地，**因为无法访问到父类中的private类型的方法，那么子类中也无法对其进行重写**。
