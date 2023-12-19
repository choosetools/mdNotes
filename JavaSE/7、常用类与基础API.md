# String类 

## 一、String类大致介绍

* `java.lang.String` 类代表字符串。Java程序中所有的字符串文字都可以看做是实现此类的实例。
* String对象的字符内容是存储在一个字符数组value[]中的，`"hello"`等效于`char[] data = {'h','e','l','l','o'};`



**jdk8中的String源码：**

![image-20231218174513953](.\images\image-20231218174513953.png)



**jdk8之后String的源码：**

![image-20231218174700201](.\images\image-20231218174700201.png)

> * 在`jdk8`中，字符串存储在`char`类型的数组中；在`jdk8`以后，字符串存储在**`byte`**类型的数组中。
>   为什么会改变？
>
>   原因是为了节省内存进行了优化。在于大多数的字符串都是拉丁字符，比如abcd这些都属于拉丁字符体系，这些拉丁字符使用数字表示范围在1~127之间，只需要一个字节的存储空间，完全可以使用占一个字节的byte来存储（byte存储大小范围在-128~127之间）。
>
>   char占用两个字节，在jdk8即之前时，String字符串使用char[]数组来存储，在大多数情况下有一半的内存空间都是未被使用的。
>
>   但若是其他语言，比如中文，对应的编码数字一定要占两个字节，这种情况该怎么办呢？
>
>   jdk8之后，新的String类将根据字符串不同的内容，有着不同的存储方式，分为ISO-8859-1（每个字符一个字节）和UTF-16（每个字符两个字节）。编码标志将指示使用哪种编码。
>
>   即当我们String存储汉字时，就是使用UTF-16编码集进行编码来存储，即：使用两个字节存储一个字符。
>
> * `String`类声明为`final`意味着`String`**不可被继承**。
> * `Serializable`接口，可序列化接口。凡是实现此接口的对象就可以通过网络或者本地进行数据的传输。
> * `Comparable`接口：凡是实现此接口的类，其对象可以进行比较大小。
> * 在JDK8之前：**`private final char value[]`**，声明为`final`类型的，意味着数组一旦初始化，其地址就不可变，即**数据创建后不可再被修改**。（无论是jdk8之前还是以后）



其实不仅仅是String是不可变的，**`包装类也是不可变的`**。Integer等包装类，一旦创建，就不可被修改了，实际上修改是创建了一个新的对象，而不是在原有对象的基础上进行修改。

例如：

```java
public class StringTest {
    public static void main(String[] args) {
        int i = 1;
        Integer j = Integer.valueOf(2);
        Circle c = new Circle();
        change(i,j,c);
        
        //这里为1很好理解，因为int属于基本数据类型，值传递而不是引用传递
        System.out.println("i = " + i);//1
        //这里为什么为2呢？明明Integer属于引用数据类型，为什么不是12？
        //原因就在于Integer具有不变性，当我们去修改Integer的值时，实际上是去创建一个新的Integer对象
        //故没有在原对象的基础上进行修改
        System.out.println("j = " + j);//2
        System.out.println("c.radius = " + c.radius);//10.0
    }

    public static void change(int a ,Integer b,Circle c ){
        a += 10;
        //等价于  b = new Integer(b+10);即不是在原对象上进行修改，而是新建了一个包装类的对象
       b += 10;
        c.radius += 10;
    }
}
class Circle{
    double radius = 0;
}
```



## 二、String的内存结构及存储方式

由于字符串对象设计成不可变的（final），所以字符串存放在字符串常量池中。

在`JDK6`及之前，字符串常量池放在`方法区`内。

从`JDK7`及以后，常量池移到`堆空间`中。

String对象创建过程内存结构案例：

![image-20220405160036240](.\images\1562945799274.png)

由上图可知，String不同实例化的方式，存储的方式也不同：

* 当我们使用直接赋值的方式创建String对象时，**String对象保存的是常量池String对象的地址值，String对象中的value属性再指向常量池中的数组数据**。

  比如String s1 = "hello"，s1指向的是位于常量池中的"hello"。

* 当我们使用new的方式创建String对象时，**String对象保存的是堆中String对象的地址值，String对象中的属性value再指向常量池中的数组数据**。

  比如String s2 = new String("hello")，s2指向的是堆中的String对象。

> 总结：
>
> **不同实例化的方式，String对象的存储位置不同：**
>
> **`直接赋值存放在常量池中；new存放在堆中`。**

内存案例分析（注意，以下使用的是JDK6之前的内存图）：

![image-20231218200643270](.\images\image-20231218200643270.png)



> **`在字符串常量池中，不允许存放两个相同的字符串常量的。`**

所以，

当使用直接创建的方式创建字符串对象，当常量池中含有一样的字符串常量时，会直接将常量池中String对象的地址值赋值给该对象变量，而对象中的value属性则指向了数组的地址；

若使用new的方式创建字符串对象，当常量池中含有一样的字符串常量时，是在堆空间中重新创建了一个String对象，并将常量池中存放数据的数组地址赋值给对象中的value属性。



案例：

```
String s1 = "hello";
String s2 = "hello";
String s3 = new String("hello");

//对于引用数据类型来说，==判断的是地址值
System.out.println(s1 == s2);//true，说明二者的地址值相同
System.out.println(s1.equals(s2));//true，说明值相同

System.out.println(s1 == s3);//false，说明地址值不同
System.out.println(s1.equals(s3));//true
```

为什么s1 == s2是true，而s1 == s3为false呢？

原因就在于，使用直接赋值的方式创建的String对象，是将常量池中的地址值赋值给String对象变量；而使用new的方式创建的String对象，则是重新在堆中创建一个String对象，然后再将常量池中的数组地址值赋值给堆中String对象的value属性。

并且，由于常量池中不允许存放两个相同的常量，所以当创建的s2字符串与s1相同时，是将s1常量地址值直接赋给s2，所以s1 == s2为true。



并且由于String的不可变性，当给String对象赋一个新的值时，实际上是去常量池中创建一个新的数组，并赋值给这个对象引用。



但是**无论实例化String的方式如何，最终都会指向常量池中的value数组。**



## 三、String的特性

注意！以下的内存结构都是JDK6及之前的，JDK7及以后常量池均存放在堆空间内。

并且以下的内存均是简化情况，均未将常量池中的String对象表示出来。

### 1、对String进行修改

1. 当对字符串重新赋值时，需要重新指定一个字符串常量的位置进行赋值，不能在原有的位置修改



![image-20231218194421653](.\images\image-20231218194421653.png)

2. 当对现有的字符串进行拼接操作时，需要重新开辟空间保存拼接以后的字符串，不能在原有的位置修改。

![image-20231218195102037](/\images\image-20231218195102037.png)

3. 当调用字符串的replace()替换现有的某个字符时，需要重新开辟空间保存修改以后的字符串，不能在原有的基础上修改。

![image-20231218195207224](.\images\image-20231218195207224.png)



### 2、对String拼接的一些情况

#### 2.1、测试String的连接符：+

```java
String s1 = "hello";
String s2 = "world";

String s3 = "helloworld";
String s4 = "hello" + "world";
String s5 = s1 + "world";
String s6 = "hello" + s2;
String s7 = s1 + s2;

System.out.println(s3 == s4);//true
System.out.println(s3 == s5);//false
System.out.println(s3 == s6);//false
System.out.println(s3 == s7);//false
System.out.println(s5 == s6);//false
System.out.println(s5 == s7);//false
```

为什么会出现这种情况？

**原因：**

* 使用`常量 + 常量`的方式进行拼接，实际上是在字符串常量池中创建了一个新的字面量，若常量池中已有该字面量，则返回字面量的地址值。

* 若使用`常量 + 变量`或`变量 + 变量`的方式进行拼接，实际上是通过new的方式创建了一个新的对象，返回的是对象的地址值。

所以，在上述情况中，若使用"hello" + "world"的方式进行拼接，则直接将常量池中的字面量地址值返回；若使用到了变量s1或s2，则会去堆空间中创建一个新的对象，将对象的地址值返回。

我把情况改一改：将s1设置成final类型的：

```
        final String s1 = "hello";
        String s2 = "world";

        String s3 = "helloworld";
        String s5 = s1 + "world";

        System.out.println(s3 == s5);//true
```

这个时候为什么变为true了呢？

原因在于s1加了一个final，这表示s1从变量变成了常量了，那么s5就变成了常量拼接常量，s5就是直接指向了常量池中的字面量，自然也与s3相等了。



#### 2.2、concat()方法进行拼接

使用`concat()`进行拼接，不管是常量调用，还是变量调用，实际上都是`new`一个新的`String`对象出来。

案例：

```java
String s1 = "hello";
String s2 = "world";

String s3 = "helloworld";
String s4 = s1.concat("world");
String s5 = s1.concat(s2);
String s6 = "hello".concat("world");
String s7 = "hello".concat(s2);

System.out.println(s3 == s4);//false
System.out.println(s3 == s5);//false
System.out.println(s3 == s6);//false
System.out.println(s3 == s7);//false
```

#### 2.3、String的方法`intern()`

> String的`intern()`方法返回的是**字符串常量池中字面量的地址值**。

就是将常量池中该字符串数组的地址值直接返回，此方法可以用于从new对象的方式创建的String对象中获取到字面量的地址值，即使用直接赋值的方式创建String对象。

例如：

```java
String s1 = new String("hello");

String s2 = s1.intern();

//那么s2的创建就相当于String s2 = "hello";
```



## 四、String常用的构造器与方法

### 1、构造器

* **`public String()`**：初始化新建的String对象，以使其表示空字符序列。
* **`public String(String original)`**：初始化一个新创建的String对象，使其表示一个与参数相同的字符序列。
* **`public String(char[] value)`**：通过当前参数中的字符数组来构造新的String。
* **`public String(char[] value, int offset, int count)`**：通过字符数组的一部分来构造新的String。
* **`public String(byte[] bytes)`**：通过使用平台的**默认字符集**解码当前参数中的字节数组来构造新的String。
* **`public String(byte[] bytes, String charsetName)`**：通过使用指定的字符集解码当前参数中的字节数组来构造新的String。

举例：

```java
//字面量定义方式：字符串常量对象
String str = "hello";

//构造器定义方式：无参构造
String str1 = new String();

//构造器定义方式：创建"hello"字符串常量的副本
String str2 = new String("hello");

//构造器定义方式：通过字符数组构造
char chars[] = {'a', 'b', 'c','d','e'};     
String str3 = new String(chars);
String str4 = new String(chars,0,3);

//构造器定义方式：通过字节数组构造
byte bytes[] = {97, 98, 99 };     
String str5 = new String(bytes);
String str6 = new String(bytes,"GBK");
```

### 2、方法

#### 2.1、常用方法

* **`boolean isEmpty()`**：判断当前字符串是否为空。
* **`int length()`**：返回当前字符串的长度。
* **`String concat(String s)`**：返回拼接后的字符串。这里是去使用new的方式新建了一个字符串返回的。
* **`boolean equals(Object obj)`**：比较字符串是否相等，区分大小写。
* **`boolean equalsIgnoreCase(Object obj)`**：比较两个字符串是否相等，不区分大小写。
* **`int compareTo(String other)`**：比较字符串的大小，区分大小写，按照其Unicode编码值比较大小。
* **`int compareToIgnoreCase(String other)`**：比较字符串的大小，不区分大小写。
* **`String toLowerCase()`**：将字符串转换成小写。
* **`String toUpperCase()`**：将字符串转换成大写。
* **`String trim()`**：去掉字符串前后空白符。
* **`String intern()`**：将字符串的在常量池中的字面量地址值返回，即返回的String对象与使用直接赋值的方式创建的String对象指向方式一致。

#### 2.2、与查找有关的方法

* **`boolean contains(String s)`**：是否包含xxx。
* **`int indexOf(String str)`**：在从前往后查找字符串str，即如果有返回第一次出现的下标（从0开始），要是没有返回-1。
* **`int indexOf(String str, int fromIndex)`**：返回指定子字符串在此字符串中第一次出现处的索引，从指定的索引开始。
* **`int lastIndexOf(String str)`**：在当前字符串中从后往前查找str字符串，如果有则返回第一次查找到的位置下标（相对于全局字符串而言），如果没有返回-1。
* **`int lastIndexOf(String str, int fromIndex)`**：在当前字符串中从指定位置开始从后往前查找str字符串，如果有则返回第一次查找到的位置下标，如果没有返回-1。

案例：

```java
//contains()，是否包含
String s1 = "helloworldhelloworldhelloworld";
System.out.println(s1.contains("elo"));//false

//索引从0开始
System.out.println(s1.indexOf("e"));//1
//从指定索引开始检索，返回的值是该字符串位于全局字符串的索引
System.out.println(s1.indexOf("e", 5));//11

//从指定的索引从后往前反向搜索
System.out.println(s1.lastIndexOf("e"));//21
//从指定的索引从后往前反向搜索，返回指定字符串第一次出现处的索引，注意是相对于全部字符串从左至右的索引值
System.out.println(s1.lastIndexOf("e",19));//11
```

#### 2.3、与截取有关的方法

* **`String substring(int beginIndex)`**：返回一个新的字符串，它是此字符串的从beginIndex开始（包括beginIndex位置的字符）截取到最后一个子字符串。
* **`String substring(int beginIndex, int endIndex)`**：返回一个新的字符串，它是此字符串从beginIndex开始截取到endIndex索引位置（包括beginIndex，不包括endIndex）的一个子字符串。

案例：

```java
String s1 = "helloworldhelloworldhelloworld";
//截取后的字符串包含beginIndex索引位置的字符
System.out.println(s1.substring(17));//rldhelloworld

//从beginIndex开始截取到endIndex（不包含endIndex位置的字符）
//相当于是beginIndex截取到endIndex-1.
System.out.println(s1.substring(6, 11));//orldh
```

在Java中，凡是涉及到一个区间的，全是**左闭右开**区间，即包含左边，但不包含右边。比如Math.random()随机数，其区间就是[0, 1)区间，左闭右开区间。

#### 2.4、与char字符有关的方法

* **`char charAt(index)`**：返回index索引位置的字符。
* **`char[] toCharArray()`**：将此字符串转换为一个新的字符数组返回。
* **`static String valueOf(char[] data)`**：将char数组转换成String字符串，底层使用的是new的方式创建字符串。
* **`static String valueOf(char[] data, int offset, int count)`**：将data数组转换成字符串，从索引offset开始，使用count数目的字符进行创建。（注意这里并不是endIndex，而是count，表示数目）
* **`static String copyValueOf(char[] data)`**：与valueOf()类似。
* **`static String copyValueOf(char[] data, int offset, int count)`**：与valueOf()类似。

案例：

```java
char[] chars = new char[]{'1', '2','3','4','5','6','7','8','9','a','b','c','d'};
String s1 = String.valueOf(chars);
String s2 = String.valueOf(chars, 3, 9);
String s3 = String.copyValueOf(chars);
String s4 = String.copyValueOf(chars, 3, 9);

System.out.println(s1);//123456789abcd
System.out.println(s2);//456789abc
System.out.println(s3);//123456789abcd
System.out.println(s4);//456789abc

System.out.println(s1 == s2);//false
System.out.println(s3 == s4);//false
System.out.println(s1.equals(s2));//false
System.out.println(s3.equals(s4));//false

System.out.println(s1.charAt(6));//7
```

#### 2.5、与开头、结尾有关的方法

* **`boolean startsWith(String prefix)`**：测试此字符串是否以指定的前缀开始。
* **`boolean startsWith(String prefix, int toffset)`**：测试此字符串从指定索引开始的子字符串是否以指定前缀开始。
* **`boolean endsWith(String suffix)`**：测试此字符串是否以指定的后缀结束。

#### 2.6、与替换有关的方法

* **`String replace(char oldChar, char newChar)`**：返回一个新的字符串，它是通过用newChar替换此字符串中出现的所有oldChar得到的。
* **`String replace(CharSequence target, charSequence replacement)`**：使用指定的字面量替换序列replacement替换此字符串中所有匹配字面值目标序列target的子字符串。
* **`String replaceAll(String regex, String replacement)`**：使用给定的replacement替换此字符串所有匹配给定的正则表达式regex的子字符串。
* **`String replaceFirst(String regex, String replacement)`**：使用给定的replacement替换此字符串匹配给定的正则表达式regex的第一个子字符串。

### 3、String与其他结构之间的转换

#### 3.1、基本数据类型/包装类与String之间的转换

由于基本数据类型与对应的包装类之间使用自动装箱/拆箱的方式可以轻易的相互转换，所以我们一般将这两种类型在使用时看成一种。



**基本数据类型、包装类 → String：**

调用String类中的**`valueOf()`**方法可以将基本数据类型/包装类转换成String字符串。



**String → 基本数据类型、包装类：**

使用**`parseXxx(String s)`**可以将数字字符组成的字符串转换为基本数据类型/包装类。

#### 3.2、char数组与字符串之间的转换

**char数组 → 字符串 **：

使用String类的构造器：

**`String(char[] value)`** 

或

 **`String(char[] value, int offset, int length)`**

用char数组中的全部或部分字符创建字符串对象。



**字符串 → char数组**：

使用String类中的`toCharArray()`方法：

**`public char[] toCharArray()`**：将字符串中的全部字符转换成char[]类型的数组。

#### 3.3、byte数组与字符串之间的转换

byte数组与字符串之间的转换，实际上就是编码与解码的过程，byte数组转换成字符串是解码，字符串转换成byte数组是编码。

**字符串 → byte数组**（编码）：

* **`public byte[] getBytes()`**：使用平台的默认字符集将此字符串编码为byte序列，并将结果存储到一个新的byte数组中。
* **`public byte[] getBytes(String charsetName)`**：使用指定的字符集将此String编码到byte序列，并将结果存储到新的byte数组中。



**byte数组 → 字符串**（解码）：

* **`public String(byte[] bytes)`**：使用平台默认的字符集解码指定的byte数组，构建一个新的String。
* **`public String(byte[] bytes, String charsetName)`**：使用指定的字符集解码指定的byte数组。







# StringBuffer与StringBuilder





