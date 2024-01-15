# java.io.File类的使用

## 1、概述

* File类及本章下的各种流，都定义在`java.io`包下。
* 文件或者目录（即文件夹）在Java看来，都可以看成是一个File类。
* **File能新建、删除、重命名文件和目录**，即File可以操作文件和目录本身的属性，**但是File不能访问文件内容本身**。如果需要访问文件内容本身，则需要使用`输入/输出流`。
  * File对象可以作为参数传递给流的构造器。
* 想要在Java程序中表示一个真实存在的文件或目录，那么必须有一个File对象。但是Java程序中的一个File对象，可能没有文件或文件夹与之对应。



## 2、构造器

* **`public File(String pathname)`**：以pathname为路径创建File对象，可以是绝对路径，也可以是相对路径。如果pathname是相对路径，则默认的当前路径在系统属性`user.dir`中存储。
* **`public File(String parent, String child)`**：以parent为父路径，child为子路径创建File对象。这种方式实际上就是将路径与文件/目录名称分开书写。
* **`public File(File parent, String child)`**：根据一个父File对象和子文件路径创建File对象。

**关于路径：**

* `绝对路径`：从盘符开始的路径，这是一个完整的路径。
* `相对路径`：相对于项目目录的路径，这是一个便捷的路径，开发中经常使用。
  * IDEA中，**main方法**中的文件的相对路径，是相对于“**`当前工程`**”。
  * IDEA中，**单元测试方法**中文件的相对路径，是相对于"**`当前module`**"。

我们可以通过`System`的属性`user.dir`来获取当前的执行的位置，从而方便我们设置相对路径。

代码：

```java
public static void main(String[] args) {
    String property = System.getProperty("user.dir");
    System.out.println(property);
}


@Test
public void test(){
    String property = System.getProperty("user.dir");
    System.out.println(property);
}
```

**main方法的执行结果：**

![image-20240114204108217](.\images\image-20240114204108217.png)

**单元测试的执行结果：**

![image-20240114204137965](.\images\image-20240114204137965.png)

我们可以知道，main方法的相对路径是在当前的项目中。

单元测试方法的相对路径是在执行位置的module中。

所以，当我们通过不同的方式执行时，使用到相对路径，需要考虑当前执行的位置。

**文件创建的案例：**

当路径下有如下文件时：

<img src=".\images\image-20240114210130103.png" align="left">

此时我们可以去创建相应的File对象：

```java
public static void main(String[] args) {
    String property = System.getProperty("user.dir");
    System.out.println(property);


    File file1 = new File(".\\collectionsourcecode\\src\\main\\java\\org\\example\\FileTest.txt");
    File file2 = new File(".\\collectionsourcecode\\src\\main\\java\\org\\example","FileTest.txt");

    File file3 = new File(".\\collectionsourcecode\\src\\main\\java\\org\\example");
    File file4 = new File(file3,"FileTest.txt");

    System.out.println(file1.getAbsolutePath());
    System.out.println(file2.getAbsolutePath());
    System.out.println(file4.getAbsolutePath());
}
```

打印结果：

![image-20240114211148801](.\images\image-20240114211148801.png)

以上三种构造器均创建了同样的File对象，均表示同一个。

**注意：**

> 1. 无论该路径下是否存在文件或目录，都不影响File对象的创建。当路径下不存在文件或目录时，创建出来的File对象相当于仅是内存上的Java数据。
>
>    但是，如果我们要去操作这个File对象，并且没有相应的文件或目录与之对应时，比如去读取数据，则会报错。
>
> 2. windows的路径分隔符使用"`\`"，而在Java程序中的"`\`"表示转义字符，所以在Java中表示路径，需要使用到"`\\`"。或者也可以使用"`/`"。Java程序支持将"`/`"当成平台无关的路径分隔符。或者直接使用`File.separator`常量值表示。比如：
>
>    ```java
>    File file = new File("d:" + File.separator + "atguigu" + File.separator + "info.txt")
>    ```
>
> 3. `getPath()`：获取File的构造路径
>
>    `getAbsolutePath()`：获取File的绝对路径
>
>    getPath()是获取创建该File类时，使用的路径。getAbsolutePath()是获取该File类的绝对路径。
>
>    当File的构造路径是**绝对路径**时，那么getPath()和getAbsolutePath()结果一样。
>
>    当File的构造路径是**相对路径**时。那么getAbsolutePath的路径 = user.dir的路径 + getPath的路径。
>
>    案例：
>
>    ```java
>    @Test
>    public void test3(){
>        File file = new File("d://abc//123//java.txt");
>        System.out.println(file.getPath());
>        System.out.println(file.getAbsolutePath());
>          
>        File file1 = new File("abc//123//java.txt");
>        System.out.println(file1.getPath());
>        System.out.println(file1.getAbsolutePath());
>    }
>    ```
>
>    打印结果：
>
>    ![image-20240114213054082](.\images\image-20240114213054082.png)





## 3、常用方法

### 3.1、获取文件和目录基本信息

* `public String getName()`：获取名称
* `public String getPath()`：获取构造路径
* **`public String getAbsolutePath()`**：获取绝对路径
* `public File getAbsoluteFile()`：获取绝对路径表示的文件
* **`public String getParent()`**：获取上层文件目录路径。若无，返回null。
* `public long length()`：获取文件长度（即字节数）。不能获取目录的长度。
* `public long lastModified()`：获取最后一次修改的时间，返回的是毫秒值。

> 如果File对象代表的文件或目录存在，则File对象实例初始化时，就会用硬盘中对应文件或目录的属性信息（例如：时间、类型等）为File对象的属性赋值，否则除了路径和名称，File对象的其他属性将会保留默认值。

<img src=".\images\image-20220412215446368.png" alt="image-20220412215446368" style="zoom:80%;" />



举例：

```java
@Test
public void test4(){
    File file = new File(".\\src\\main\\java\\org\\example\\FileTest.txt");
    System.out.println(file.getName());
    System.out.println(file.getPath());
    System.out.println(file.getAbsolutePath());
    System.out.println(file.getParent());
    System.out.println(file.length());
    System.out.println(file.lastModified());

    System.out.println("*******************************");
    File absoluteFile = file.getAbsoluteFile();
    System.out.println(absoluteFile.getPath());
    System.out.println(absoluteFile.getAbsolutePath());

}
```

打印结果：

![image-20240114215307202](.\images\image-20240114215307202.png)

`getAbsoluteFile()`方法：

实际上就是根据当前的File对象的绝对路径，重新创建一个根据绝对路径来创建的File对象，实际上还是同一个对象，只不过getPath()方法时，获取到的是绝对路径。

`length()`方法：

只能用于文件，而不能用于文件夹的大小获取。对于目录的大小获取，需要去获取其中每一个文件的大小，将所有文件的大小累加起来，才可获得。使用**递归操作**进行实现。

### 3.2、列出目录的下一级

* **`public String[] list()`**：返回一个String数组，获取该File目录中的所有子文件或目录。
* **`public File[] listFiles()`**：返回一个File数组，表示该File目录中的所有子文件或目录。

案例：

```java
@Test
public void test5(){
   File file = new File(".\\src\\main\\java\\org\\example");
    for (String s : file.list()) {
        System.out.println(s);
    }

    for (File file1 : file.listFiles()) {
        System.out.println(file1);
    }
}
```

打印结果：

<img src=".\images\image-20240114222426749.png" align="left">



File类还提供了两个重载的包含**`文件过滤器参数`**的方法：

> * **`public String[] list(FilenameFilter filter)`**
>
> * **`public File[] listFiles(FileFilter filter)`**

这两种方法，可以让我们去**获取符合指定条件**文件/目录的名称数组和File数组。

案例：

判断指定目录下是否有后缀名为.jpg的文件，有，就输出该文件名称。

```java
//方式一：
@Test
public void test2(){
	File srcFile = new File("D:\\壁纸");
	File[] listFiles = srcFile.listFiles();
	for(File file : listFiles){
		if(file.getName().endsWith(".jpg")){
            		System.out.println(file.getName());
		}
	}
}


//方式二：使用文件过滤器（推荐）
@Test
public void test11(){
    File file = new File("D:\\壁纸");
    //根据accept()方法的返回值来判断是否满足条件
    String[] nameList = file.list(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            if (name.endsWith(".jpg")){
                return true;
            }else {
                return false;
            }
        }
    });
    for (String s : nameList) {
        System.out.println(s);
    }
}
```


### 3.3、重命名

* **`public boolean renameTo(File dest)`**：把文件重命名为制定的文件路径。

file1.renameTo(file2)：要想此方法执行成功，并返回true。要求：

`file1必须存在，且file2必须不存在，file2所在的上层目录必须存在。`



案例：

```java
File file1 = new File(".\\src\\main\\java\\org\\example\\FileTest.txt");
File file2 = new File(".\\src\\main\\java\\org\\example\\FileTest2.txt");
boolean b = file1.renameTo(file2);
System.out.println(b ? "重命名成功" : "重命名失败");
```

### 3.4、判断功能的方法

* **`public boolean exists()`**：此File表示的文件或目录是否实际存在。
* **`public boolean isDirectory()`**：此File表示的是否是目录。
* **`public boolean isFile()`**：此File表示的是否是文件。
* `public boolean canRead()`：是否可读。
* `public boolean canWrite()`：是否可写。
* `public boolean isHidden()`：是否隐藏。



案例：去获取某一个目录的大小

我们知道，length()方法只能去获取文件的大小，当我们想要去获取目录的大小，即获取目录下所有文件（包括所有子目录中的文件）的大小之和。

```java
//需求：获取一个目录的大小
public long getDirectorySize(File file){
    long size = 0L;
    if (file.exists()){
        if (file.isFile()){
            size = file.length();
        }else {
            for (File f : file.listFiles()) {
                //使用递归的方式去获取所有子目录中的文件大小之和
                size += getDirectorySize(f);
            }
        }
    }
    return size;
}
```

> 如果文件或目录不存在，那么exists()、isFile()和isDirectory()都是返回false。
>
> 所以，当isFile()或isDirectory()返回false时，需要先判断文件或目录存不存在。



### 3.5、创建与删除功能

* **`public boolean createNewFile()`**：创建文件。如果文件存在，则不创建，返回false。
* **`public boolean mkdir()`**：创建文件目录。如果此文件目录存在，则不创建。如果此文件目录的上层目录不存在，也不创建。
* **`public boolean mkdirs()`**：创建文件目录。如果上层文件目录不存在，一并创建。
* **`public boolean delete()`**：删除文件或文件夹。删除注意事项：要删除一个文件夹，需要确保该文件夹中不存在文件或子目录。

文件的创建：

```java
@Test
public void test9() throws IOException {
    File file1 = new File(".\\src\\main\\java\\org\\example\\123.txt");
    //如果文件不存在，就去创建文件
    if (!file1.exists()){
        boolean isSuccessed = file1.createNewFile();
        if (isSuccessed){
            System.out.println("创建成功");
        }
    }else {
        System.out.println("文件已存在");
    }
    boolean isDel = file1.delete();
    System.out.println(isDel ? "文件删除成功" : "文件删除失败");
}
```

目录的创建：

```java
@Test
public void test8(){
    //前提：d:\\io文件目录存在，io2或io3目录是不存在的
	File file1 = new File("d:\\io\\io2\\io4");
    System.out.println(file1.mkdir());//false
    
    File file2 = new File("d:\\io\\io3\\io5");
    System.out.println(file2.mkdirs());//true
}
```

> mkdir()方法要求上层目录一定需要存在。
>
> mkdirs()方法若上层目录不存在，也一并创建。
>
> `delete()`方法若是删除的是目录，则要求目录下不存在文件或目录，即**目录是空的，才可以通过delete()删除。**





# IO流原理及流的分类

## Java IO原理

* Java程序中，对于数据的输入/输出操作以“`流（Stream）`”的方式进行，可以看做是一种数据的流动。

  <img src=".\images\image-20220503123117300.png" alt="image-20220503123117300" style="zoom: 80%;" />

* I/O流中的I/O是`Input/Output`的缩写，I/O技术是非常实用的技术，用于处理设备之间的数据传输。如读/写文件，网络通讯等。

  * `输入input`：读取外部数据（磁盘、光盘等存储设备的数据）到程序（内存）中。
  * `输出Output`：将程序（内存）数据输出到磁盘、光盘等存储设备中。

![image-20220412224700133](.\images\image-20220412224700133.png)



## 流的分类

`java.io`包下提供了各种“流”类和接口，用以获取不同种类的数据，并通过`标准的方法`输入或输出数据。

* 按数据的流向不同分为：**输入流**和**输出流**

  * **输入流**：把数据从`其他设备`上读取到`内存`中的流
    * 以InputStream、Reader结尾
  * **输出流**：把数据从`内存`中写出到`其他设备`上的流
    * 以OutputStream、Writer结尾

* 按操作数据单位的不同分为：**字节流（8bit）**和**字符流（16bit）**

  * **字节流**：以字节为单位，读写数据的流
    * 以`InputStream`、`OutputStream`结尾
  * **字符流**：以字符为单位，读写数据的流
    * 以`Reader`、`Writer`结尾

* 根据IO流的角色不同分为：**节点流**和**处理流**

  * **节点流**：直接从数据源或目的地读写数据

    ![image-20220412230745170](.\images\image-20220412230745170.png)

  * **处理流**：不直接连接到数据源或目的地，而是连接在已存在的流（节点流或处理流）之上，通过对数据的处理为程序提供更为强大的读写功能。

    ![image-20220412230751461](.\images\image-20220412230751461.png)

    大概来说，其实就是节点流直接去读写数据，处理流相当于在外面包上了一层，可以包在节点流上，也可以包在处理流之上。

小结：图解

<img src=".\images\image-20220412225253349.png" alt="image-20220412225253349" style="zoom:67%;" />



## 流的API

* Java的IO流共涉及40多个类，实际上非常规则，都是从如下4个抽象基类派生来的。

| （抽象基类） | 输入流      | 输出流       |
| :----------: | ----------- | ------------ |
|    字节流    | InputStream | OutputStream |
|    字符流    | Reader      | Writer       |

* 由这四个类派生出来的子类名称都是以其父类名作为子类名后缀。

![image-20220412230501953](.\images\image-20220412230501953.png)

**常用的节点流：**

* 文件流：FileInputStream、FileOutputStream、FileReader、FileWriter
* 字节/字符数组流：ByteArrayInputStream、ByteArrayOutputStream、CharArrayReader、CharArrayWriter
  * 对数组进行处理的节点流（对应的不再是文件，而是内存中的一个数组）。

**常用处理流：**

* 缓冲流：BufferedInputStream、BufferedOutputStream、BufferedReader、BufferedWriter
  * 作用：增加缓冲功能，避免频繁读写硬盘，进而提升读写效率。
* 转换流：InputStreamReader、OutputStreamReader
  * 作用：实现字节流和字符流之间的转换
* 对象流：ObjectInputStream、ObjectOutputStream
  * 作用：提供直接读写Java对象功能



# 节点流

## 节点流之一：FileReader\FileWriter

### 1、Reader与Writer的介绍

Java提供一些字符流类，以**字符为单位**读写数据，专门用于`处理文本文件`。不能操作图片，视频等非文本文件。

> 常见的文本文件有如下格式：.txt、.java、.c、.cpp、.py等
>
> 注意：.doc、.xls、.ppt都不是文本文件

#### 1.1、字符输入流：Reader

`java.io.Reader`抽象类是表示用于读取字符流的所有类的父类，可以读取字符信息到内存中。它顶级了字符输入流的基本共性功能方法。

* **`public int read()`**：从输入流读取一个字符。虽然读取了一个字符，但是会自动提升为int类型。返回该字符的Unicode编码值。如果已经到达流末尾了，则返回-1。
* **`public int read(char[] cbuf)`**：从输入流读取一些字符，并将它们存储到字符数组cbuf中。每次最多读取cbuf.length个字符。返回实际读取的字符个数。如果已经到达流末尾，没有数据可读，则返回-1。
* **`public int red(char[] cbuf, int off, int len)`**：从输入流中读取一些字符，并将它们存储到字符数组cbuf中，从cbuf[off]开始的位置存储，每次最多读取len个字符，返回实际读取的字符个数。如果已经到达流末尾，没有数据可读，则返回-1。
* **`public void close()`**：关闭此流并释放与此流相关联的任何系统资源。

> 注意：当完成流的操作时，必须调用close()方法，释放系统资源，否则会造成内存泄露。







#### 2、字符输出流：Writer

`java.io.Writer`抽象类是表示用于写出字符流的所有类的超类，将指定的字符信息写出到目的地。它定义了字节输出流的基本共性功能方法。

* **`public void writer(int c)`**：写出单个字符。
* **`public void write(char[] cbuf)`**：写出字符数组。
* **`public void write(char[] cbuf, int off, int len)`**：写出字符数组的某一部分。off：数组的开始索引；len：写出字符的个数。
* **`public void write(String str)`**：写出字符串。
* **`public void write(String str, int off, int len)`**：写出字符串的某一部分。off：字符串的开始索引；len：写出的字符个数。
* **`public void flush()`**：刷新该流的缓冲。
* **`public void close()`**：关闭此流。









## 节点流之二：FileInputStream\FileOutputStream







# 处理流

## 处理流之一：缓冲流



## 处理流之二：转换流



## 处理流之三/四：数据流、对象流





# 其他流



