## 包装类的面试题

### 自动装箱或valueOf()方法创建包装类的特点

使用自动装箱的方式去创建包装类，实际上在底层还是会去调用valueOf()方法创建。

![image-20231130215144910](../JavaSE/images/image-20231130215144910.png)

当满足i >= IntegerCache.low && i <= IntegerCache.high时，获得的Integer对象从现有的cache数组中获取的。其中Integer包装类中low的值是-128，high的值是127。

让我们来看看cache数组：

![image-20231130215726147](../JavaSE/images/image-20231130215726147.png)

以上这段代码的含义就是，cache数组实际上就是一个Integer数组，保存的是-128 ~127区间以内的Integer对象，在Integer类加载的时候就会被创建出来。

> **当我们使用自动装箱（或者valueOf()方法)的方式去创建Integer对象时，如果数值满足在-128~127区间以内，是直接从cache数组中获取已创建的Integer对象。**
>
> **当值不在上述区间内时，自动装箱（valueOf()方法）获取到的包装类对象，则是通过new一个新的包装类对象的方式。**

这么做的好处是有利于节省内存空间。

以上也是一种设计模式：享元设计模式。

那么，根据以上的情况，有可能出现以下这些面试题：

```java
Integer a = 1;
Integer b = 1;
System.out.println(a == b);

Integer i = 128;
Integer j = 128;
System.out.println(i == j);

Integer m = new Integer(1);
Integer n = 1;
System.out.println(m == n);

Integer x = new Integer(1);
Integer y = new Integer(1);
System.out.println(x == y);
```

按照一般的想法来说，以上每一个Integer对象都属于新的对象，按理应该都是false才对。

但是，由于享元的设计，实际上输出的结果为：

```
true
false
false
false
```

原因就在于使用自动装箱或者valueOf()的方式创建Integer包装类对象时，若数值范围在-128~127内，则会从数组中获取Integer对象，而不是去新建一个。那么第一个a对象和b对象都是从数组中获取到的同一个对象，所以二者相等。

而i和j均超过了127，那么底层实际是去new一个对象的，所以二者不同。

对于m来说，它是new出来的，肯定和n不是同一个对象。

x和y更不用说了。

包装类缓存数值的范围都是多少呢？

#### **包装类缓存对象**

| 包装类    | 缓存对象    |
| --------- | ----------- |
| Byte      | -128~127    |
| Short     | -128~127    |
| Integer   | -128~127    |
| Long      | -128~127    |
| Float     | 没有        |
| Double    | 没有        |
| Character | 0~127       |
| Boolean   | true和false |

注意，对于Float和Double来说，没有缓存对象，每一个使用自动装箱方式创建的包装类都是新new的。

```java
Double d1 = 1.0;
Double d2 = 1.0;
System.out.println(d1==d2);//false
```



### 包装类与包装类之间，以及与基本数据类型运算的特点

我们先来说结论：

包装类与基本数据类型进行运算时，**`默认会对包装类进行拆箱`**，之后再进行运算。 

同理，如果是包装类与包装类之间进行运算，**`也会对两个包装类先进行拆箱转换成基本数据类型进行`**。

这是一种设定，在进行运算设定为会对包装类进行拆箱，而不是对基本数据类型进行装箱，我觉得是拆箱的成本比较低，包箱还需要去创建一个包装类对象。

案例：

```java
Integer x = 1000;
int y = 1000;
System.out.println(x == y);//true

Integer m = 1000;
double n = 1000;
System.out.println(m == n);//true

Integer a = 10;
Double b = 20.0;
System.out.println(a + b);//30.0
```

以上结果原因就在于，包装类与基本数据类型进行运算时，会对包装类进行拆箱，拆箱之后再使用其对应的基本数据类型进行运算。

同理，如果是包装类与包装类之间进行运算时，也会对包装类先进行拆箱，然后再进行运算。（当然，如果是==运算，比较的是类对象的地址值，而不会进行拆箱）



---



## 三元运算符的兼容性——类型必须一致

### 分析

当使用三元运算符，两边的操作数类型不一致时，这时候就会涉及到三元运算符的转换规则：

> 1. 若两个操作数不可转换，则不做转换，返回值为Object类型
> 2. 若两个操作数是明确类型的表达式（比如变量），则按照正常的二进制数字来转换。int转换为long类型，long类型转换成float类型。
> 3. 若两个操作数中有一个是数字S，另一个是表达式，且其类型为T，那么若数字S在T范围内，则转换为T类型；如S超过了T的范围，则T转换为S类型。
> 4. 若两个操作数都是直接数字，则返回值类型为数值类型范围较大者。

### 场景

```
System.out.println(true ? 90 : 100.0);

System.out.println(true ? new Integer(90) : new Double(100.0));
```

输出结果：

<img src="..\images\image-20231130230059253.png" align="left">

分析：

两个三元运算符条件都为真，那么返回的都是第一个值，所得的返回结果都是90.0，原因就在于90和100.0这两个数上面。

看第四个结论：`若两个操作数都是直接数字，则返回值类型为数值类型范围较大者。`

90属于int数据类型，100.0属于double数据类型，double数据类型范围较大，所以若第一个操作数是90，第二个操作数是100.0的话，第一个操作数就会被自动类型提升成double类型，故返回结果为90.0。

同理，对于Integer类型和Double类型来说，在进行运算时会先进行拆箱处理，然后进行三元运算符运算，此时二者的类型不同，则返回值类型为数值类型范围较大者，即double类型。



### 建议

保证三元运算符中的两个操作数类型一致，减少错误的发生。



---



# final、finally、finalize的区别

1. **`final修饰符（关键字）`**：被final修饰的**类**，就意味着不能再派生出新的子类，**不能**作为父类而**被子类继承**。被final声明的**方法**也同样只能使用，**不能进行重写**。被final修饰的**属性**则表示该属性为**常量**。

   将变量或方法声明为final，可以保证它在使用的过程中不被修改。被声明为final的变量必须在声明的时候就要给出变量的初始值了，而在后续的引用过程中只能被访问，不可被修改。

   故类、方法不能既被abstract修饰，又被final修饰，原因在于被abstract修饰表明该方法、类是抽象的，应当被继承/重写的，而被final修饰表示该类、方法是最终的，不可被修改了，二者逻辑矛盾。

   

2. **`finally`**：在异常处理时，提供finally块来**执行必须要执行的语句**，如关闭流资源等。不管有没有异常被抛出、捕获，finally块都会被执行。try块中的内容是在无异常时执行到结束。catch块中规定内容，是在try块内容发生catch所能捕获的异常时，跳转到catch块中执行。finally块则是无论异常是否发生，无论是否执行了return语句，无论在catch块中是否发生了异常，都会被执行。所以，在逻辑代码上有需要无论发生什么都必须执行的代码，就可以放在finally块中。



3. **`finalize`**：finalize是一个方法名。当某个对象没有任何引用时，JVM就会认为该对象是垃圾对象，就会在之后的不确定时间使用垃圾回收机制对该对象进行销毁，在销毁该对象之前，就会先调用对象的finalize()方法。

   这个方法的作用在于：**对象在被清理前，使用该方法执行必要的清理操作**。这个方法来源于Object类，因此所有的类都继承了这个方法，我们可以在类中对该方法进行重写，从而达到想要的清理操作。

   **永远不要手动地去调用对象的finalize()方法，应当让JVM自己去调用。**





# synchronized vs Lock

1. Lock显式锁（手动开启和关闭），synchronized是隐式锁，出了作用域、遇到异常等自动解锁。
2. Lock只有代码块锁，synchronized有代码块锁和方法锁
3. 使用Lock锁，JVM将花费较少的时间来调度线程，性能更好。并且具有更好的扩展性（提供更多的子类），更体现面向对象。
4. Lock锁可以对读不加锁，对写加锁，synchronized不可以
5. Lock锁可以有多种获取锁的方式，可以从sleep的线程中抢到锁，synchronized不可以。





# sleep vs wait

### 相同点

二个方法一旦执行，都会使得当前线程进入到阻塞状态。

* **`sleep(long millis)`与`wait(int timeout)`方法会使线程进入到`TIMED_WAITING`状态。**

* **`wait()`方法使线程进入到`WAITING`状态。**

### 不同点

1. **`sleep()`方法与`wait()`方法最大的区别在于对`CPU的占用`。**

调用sleep()方法后，线程虽然进入了休眠，但是实际上还是占用着CPU，os认为该线程正在工作；

调用wait()方法后，线程会让出系统资源，线程进入等待池，其他线程可以占用CPU。

例如：线程调用sleep(100)后，虽然线程要休眠100毫秒，但是该线程也要在CPU中执行100毫秒，这100毫秒依旧是在执行该线程，并未释放CPU的执行权。



2. **`sleep()`方法声明在Thread类中，是静态方法；而`wait()`方法声明在Object类中，是非静态方法**

故只有当该类继承Thread类时，才可以直接调用sleep()方法，否则都是通过Thread类进行调用；

由于wait()方法声明在Object类中，故所有类中都包含wait()方法，所以可以直接使用。



3. 使用的范围不同：**`sleep()`可以在任何需要的位置被调用；`wait()`方法必须使用在同步代码块或同步方法中，否则报：`java.lang.IllegalMonitorStateException`异常。**



4. **`sleep()`方法不会释放同步监视器，`wait()`方法会释放同步监视器**。



5. **`sleep()`方法一到时间就会从`TIMED_WAITING`状态转为`RUNNABLE`状态；`wait()`方法包含带参数的与不带参数的，若是带参数的`wait()`方法，则调用后直到被唤醒之前，都是处于`TIME_WAITING`状态，唤醒后若获取到同步监视器，则转变为`RUNNABLE`状态，否则因未获取到同步监视器而被阻塞，变为`BLOCKED`状态；不带参数的则是被唤醒之前处于`WAITING`状态。**







# HashMap与Hashtable的区别

```
HashMap：底层是一个哈希表（JDK7及之前：数组+链表；JDK8及之后：数组+链表+红黑树），是一个线程不安全的集合，执行效率高。
Hashtable：底层也是一个哈希表（数组+链表），是一个线程安全的集合，执行效率低。

HashMap集合：可以存储null的键、null的值。
Hashtable集合：不能存储null的键、null的值。

Hashtable和Vector集合一样，在jdk1.2版本之后就被更先进的集合HashMap和ArrayList取代了。所以HashMap是Map的主要实现类，Hashtable是Map的古老实现类。

Hashtable的子类Properties（配置文件）类依然活跃，Properties集合是唯一一个和IO流相结合的集合。
```





# HashMap与LinkedHashMap的区别

LinkedHashMap和HashMap是Java中两种常用的集合类，并且LinkedHashMap是HashMap的子类。它们的区别主要体现在：

```
LinkedHashMap使用了双向链表来维护插入顺序或者访问顺序，而HashMap没有保持任何顺序。在LinkedHashMap中，每一个entry都包含一个before和after引用，这样就可以形成一个双向链表。这个链表可以按照插入顺序进行遍历。而HashMap没有双向链表，无法根据插入的顺序进行遍历。

如果在遍历时需要保持元素的插入顺序，那么可以选择LinkedHashMap。但如果不关心元素的顺序，则建议使用HashMap。
```



# HashMap的底层实现







# 区分Collection与Collections

**Collection**

Collection是集合类的上层接口。本身是一个接口，里面包含了一些集合的基本操作。Collection接口是Set接口和List接口的父接口。

**Collections**

Collections是一个集合框架的工具类，卡里面包含了一些对集合的排序、搜索以及序列化的方法。

最根本在于Collections是一个类，而Collection是一个接口。

