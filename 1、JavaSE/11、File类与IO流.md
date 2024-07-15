[TOC]



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

* **`public String[] list()`**：返回一个String数组，获取该File目录中的所有子文件或子目录。
* **`public File[] listFiles()`**：返回一个File数组，表示该File目录中的所有子文件或子目录。

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

* `public int read()`：从输入流读取一个字符。虽然读取了一个字符，但是会自动提升为int类型。返回该字符的Unicode编码值。如果已经到达流末尾了，则返回-1。
* **`public int read(char[] cbuf)`**：从输入流读取一些字符，并将它们存储到字符数组cbuf中。返回的结果是实际读取到的字符个数，当未读取到任何字符个数时，返回-1。
* `public int red(char[] cbuf, int off, int len)`：从输入流中读取一些字符，并将它们存储到字符数组cbuf中，从cbuf[off]开始的位置存储，每次最多读取len个字符，返回实际读取的字符个数。如果已经到达流末尾，没有数据可读，则返回-1。
* **`public void close()`**：关闭此流并释放与此流相关联的任何系统资源。

> 注意：当完成流的操作时，必须调用close()方法，释放系统资源，否则会造成内存泄露。







#### 2、字符输出流：Writer

`java.io.Writer`抽象类是表示用于写出字符流的所有类的超类，将指定的字符信息写出到目的地。它定义了字节输出流的基本共性功能方法。

* `public void writer(int c)`：写出单个字符。
* `public void write(char[] cbuf)`：写出字符数组。
* **`public void write(char[] cbuf, int off, int len)`**：写出字符数组的某一部分。off：数组的开始索引；len：写出字符的个数。
* `public void write(String str)`：写出字符串。
* `public void write(String str, int off, int len)`：写出字符串的某一部分。off：字符串的开始索引；len：写出的字符个数。
* **`public void flush()`**：刷新该流的缓冲。
* **`public void close()`**：关闭此流。

> 注意：当完成流的操作时，必须调用close()方法，释放系统资源，否则会造成内存泄露。
>



### 2、FileReader与FileWriter

#### 2.1、FileReader

`java.io.FileReader`类用于读取字符文件，构造时使用系统默认的字符编码和默认字节缓冲区。

* **`FileReader(File file)`**：根据要读取的File对象，创建一个新的FileReader。
* `FileReader(String fileName)`：创建一个新的FileReader，给定要读取的文件的名称。



举例：读取hello.txt文件中的字符数据，并显示在控制台上

方案一：

```java
@Test
public void test() throws IOException {
    //1、创建File对象，对应着hello.txt文件
    File file = new File(".\\src\\main\\java\\org\\example\\hello.txt");
    
    //2、创建输入型的字符流，用于读取数据
    FileReader reader = new FileReader(file);
    
    //3、读取数据，并显示在控制台上
    int c;
    StringBuilder str = new StringBuilder();
    while ((c = reader.read()) != -1){
        //当read()返回值不为-1时，说明未到达流末尾
        //此时将c转换成字符型数据
        str.append((char)c);
    }
    System.out.println(str);
    
    //4、流资源的关闭（必须要关闭，否则会内存泄露）
    reader.close();
}
```



改进方案二：使用`try-catch-finally`代替`throws`，因为程序必须关闭流资源

```java
@Test
public void test(){
    File file = new File(".\\src\\main\\java\\org\\example\\hello.txt");

    FileReader reader = null;
    try {
        reader = new FileReader(file);
        int c;
        StringBuilder str = new StringBuilder();
        while ((c = reader.read()) != -1){
            str.append((char)c);
        }
        System.out.println(str);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }finally {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```



改进方案三：使用`read(char[] cbuf)`代替`read()`，每次从文件中读取多个字符

* **`int read(char[] cbuf)`**：从磁盘文件中读取cbuf数组长度个字符，返回的int类型是实际上读取到的字符个数，当未读取到任何字符时，返回-1。

代码：

```java
/**
 * 对test()进行优化，每次读取多个字符存放到字符数组，减少与磁盘交互的次数，提升效率
 */
@Test
public void test1(){

    File file = new File(".\\src\\main\\java\\org\\example\\hello.txt");
    FileReader reader = null;
    
    //创建字符数组，用于存储读取到的字符
    char[] cBuffer = new char[5];
    StringBuilder str = new StringBuilder();
    try {
        reader = new FileReader(file);
        //循环读取，当read(char[])的返回值为-1时，即表示未读取到任何数据，返回值表示实际读取字符的个数
        while (true){
            int charNum = reader.read(cBuffer);
            if (charNum == -1){
                break;
            }else {
                for (int i = 0; i < charNum; i++) {
                    str.append(cBuffer[i]);
                }
            }
        }
        System.out.println(str);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }finally {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```



> `read()`一次只能获取一个数据
>
> `read(char[] cbuf)`一次性可以获取多个数据，减少了内存与磁盘之间的交互次数。磁盘的处理速度是比内存慢很多很多的，所以，**我们要尽可能地减少与磁盘的交互**。



#### 2.2、FileWriter

`java.io.FileWriter`类用于写出字符到磁盘文件中，构造时使用系统默认的字符编码和默认字节缓冲区。

* **`FileWriter(File file)`**：创建一个新的FileWriter，给定要读取的File对象。
* `FileWriter(String fileName)`：创建一个新的FileWriter，给定要读取的文件的名称。
* `FileWriter(File file, boolean append)`：创建一个新的FileWriter，指明当前的输出流是覆盖还是在末尾追加内容，为false是覆盖原内容，true是在文件末尾追加内容。



举例1：将内存中的数据写出到指定文件中

```java
/**
 * 需求：将内存中的数据写出到指定文件中
 */
@Test
public void test2(){
    //1、创建File类对象，指明要写出的文件的名称
    File file = new File(".\\src\\main\\java\\org\\example\\nihao.txt");
    FileWriter writer = null;
    try {
        //2、创建输出流
        writer = new FileWriter(file);
        //3、写出的具体过程
        //输出的具体方法：write(String str) / write(char[] cdata)
        writer.write("I love U!\n");
        writer.write("You love him!\n");
    } catch (IOException e) {
        throw new RuntimeException(e);
    }finally {
        //4、关闭资源
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

注意点：

1、当File对象所表示的文件不存在时，此时去执行，并不会报错，因为当文件不存在时，输出流会自动帮助我们创建文件：

<img src=".\images\image-20240115204137123.png" align="left">

<img src=".\images\image-20240115204150120.png" align="left">

2、当File对象所表示的文件存在，且文件中包含内容时，此时去执行文件，会对文件进行覆盖。

比如：原文件内容：

<img src=".\images\image-20240115204428547.png" align="left">

当我们去执行程序后：

<img src=".\images\image-20240115204436517.png" align="left">

这实际是去覆盖了。

这其实和创建输出流的构造器有关：

`FileWriter(File file)`类似于`FileWriter(File file, false)`，即不是在文件末尾追加内容，而是**覆盖内容**。

当我们使用`FileWriter(File file, true)`的方式创建FileWriter对象时，实际上是去**文件末尾追加内容**，而不是去覆盖内容。

比如，将FileWriter的创建改成使用FileWriter(File file, true)的方式，此时去执行程序后，多次执行结果：

<img src=".\images\image-20240115204745784.png" align="left">

即：此时就会往文件末尾追加写出的字符。





案例2：复制一份hello.txt文件，命名为hello_copy.txt文件

```java
@Test
public void test5(){
    //1、创建File类的对象
    File srcFile = new File(".\\src\\main\\java\\org\\example\\hello.txt");
    File destFile = new File(".\\src\\main\\java\\org\\example\\hello_copy.txt");

    //2、创建输入流、输出流
    FileReader reader = null;
    FileWriter writer = null;
    try {
        reader = new FileReader(srcFile);
        writer = new FileWriter(destFile);

        //3、数据的读入和写出过程
        char[] cBuffer = new char[5];//用于存储每次读取的字符
        int len;//记录每次读入到cBuffer中的字符个数
        while ((len = reader.read(cBuffer)) != -1){
            //writer给我们提供了一个write(char cbuf[], int off, int len)方法
            //该方法刚好可以用于写出使用char[]数组读入的数据
            writer.write(cBuffer, 0, len);
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }finally {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

#### **总结**

**执行步骤**：

> 第一步：创建读取或写出的File类对象
>
> 第二步：创建输入流或输出流
>
> 第三步：具体的读入或写出的过程。
>
> ​		读入：`read(char[] cbuffer)`
>
> ​		写出：`write(String str)` / `write(char[] cbuffer, 0, len)`
>
> 第四步：关闭流资源，避免内容泄露



**注意点**：

> 1. 因为出现流资源的关闭操作，为了避免因出现异常而造成内存泄露，需要使用`try-catch-finally`处理异常。
> 2. 对于**输入流**来说，File类的对象必须在物理磁盘上**存在**，否则执行就会报`FileNotFoundException`。如果传入的是一个目录，则会报`IOException`。
> 3. 对于**输出流**来说，File类的对象是**可以不存在**的。
>    * 如果File类的对象不存在，则可以在输出的过程中，自动创建File类的对象。
>    * 如果File类的对象存在：
>      * 如果调用`FileWriter(File file)`或`FileWriter(File file, false)`创建输出流对象的话，输出时会新建File文件并**覆盖已有的文件**
>      * 如果调用`FileWriter(File file, true)`构造器创建输出流对象的话，则输出时会在现有的文件**末尾追加写出的内容**。



#### 2.3、关于`flush()`（刷新）

由于内置缓冲区的缘故，如果FileWriter不关闭输出流，即不调用`close()`方法，就无法写出字符到文件中。

例如：

```java
@Test
public void test6(){
    File file = new File(".\\src\\main\\java\\org\\example\\flushTest.txt");
    FileWriter writer = null;
    try {
        writer = new FileWriter(file);
        writer.write("测试刷新方法");
        //休眠
        Thread.sleep(100000L);
        
    } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
    } finally {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

当我们在休眠期间去查看flushTest.txt文件时，发现字符并没有写到文件中：

<img src=".\images\image-20240115213853792.png" align="left">

我们可以得知，write()方法执行完毕后并没有将字符写出到文件中，而是当执行完close()方法时，才写出到文件。



但是，当关闭流对象后，就无法继续写出数据了。**当我们既想写出数据到文件中，又想继续使用流时，就需要`flush()`方法了。**

* **`flush()`**：刷新缓冲区，将缓冲区中的字符写出到文件中，还可以继续使用流。
* **`close()`**：先刷新缓冲区，将缓冲区中的字符写出到文件中，然后通知系统释放资源，流对象不可再被使用。

注意：

即便是使用了flush()方法刷新缓冲了，但是最后还是需要close()方法去释放资源。

案例：

```java
@Test
public void test6(){
    File file = new File(".\\src\\main\\java\\org\\example\\flushTest.txt");
    FileWriter writer = null;
    try {
        writer = new FileWriter(file);
        writer.write("测试刷新方法");
        writer.flush();
        Thread.sleep(100000L);
    } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
    } finally {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

此时，当我们在休眠期间去查看文件时，就可以发现字符写出到了文件中了。因为flush()刷新了缓冲区。该方法可以在不关闭流的情况下将内容输出到文件中。

<img src=".\images\image-20240115214041678.png" align="left">

## 节点流之二：FileInputStream\FileOutputStream

如果我们读取或写出的数据是非文本文件时，比如视频或图片，则Reader、Writer就无能为力了，必须使用字节流。



### 1、InputStream和OutputStream介绍

InputStream、OutputStream与Reader、Writer的区别仅仅只是一个是字节流，一个是字符流，在参数中一个是char[]类型的数组，一个是byte[]类型的数组。

#### 1.1、字节输入流：InputStream

`java.io.InputStream`抽象类是表示字节输入流的所有类的超类，可以读取字节信息到内存中。它定义了字节输入流的基本共性功能方法。

* `public int read()`：从输入流读取一个字节。返回读取的字节值。虽然读取了一个字节，但是会自动提升为int类型。如果已经到达流末尾，没有数据可读，则返回-1。
* **`public int read(byte[] b)`**：从输入流中读取一些字节数，并将他们存储到字节数组b中。每次最多读取b.length个字节。返回实际读取的字节个数。如果已经到达流末尾，没有数据可读，则返回-1。
* `public int read(byte[] b, int off, int len)`：从输入流中读取一些字节数，并将它们存储到字节数组b中，从b[off]开始存储，每次最多读取len个字节。返回实际读取的字节个数。如果已经到达流末尾，没有数据可读，则返回-1。
* **`public void close()`**：关闭此输入流并释放与此流相关联的任何系统资源。

#### 1.2、字节输出流：OutputStream

`java.io.OutputStream`抽象类是表示字节输出流的所有类的超类，将指定的字节信息写出到目的地。它定义了字节输出流的基本共性功能方法。

* `public void write(int b)`：将指定的字节输出流。虽然参数为int类型四个字节，但是只会保留一个字节的信息写出。
* `public void write(byte[] b)`：将b.length字节从指定的字节数组写入此输出流。
* **`public void write(byte[] b, int off, int len)`**：从指定的字节数组写入len字节，从偏移量off开始输出到此输出流。
* `public void flush()`：刷新此输出流并强制任何缓冲的输出字节被写出。
* **`public void close()`**：关闭此输出流并释放与此流相关联的任何系统资源。



### 2、FileInputStream与FileOutputStream

字节流不仅仅可以用于图形、视频，还可以用于文本文件的复制。

#### 2.1、FileInputStream

`java.io.FileInputStream`类是文件输入流，从文件中读取字节。

* **`FileInputStream(File file)`**：通过打开与实际文件的链接来创建一个FileInputStream，该文件由文件系统中的File对象file命名。
* `FileInputStream(String name)`：通过打开与实际文件的链接来创建一个FileInputStream，该文件由文件系统中的路径名name命名。

#### 2.2、FileOutputStream

`java.io.FileOutputStream`类是文件输出流，用于将数据写出到文件。

* **`public FileOutputStream(File file)`**：创建文件输出流，写出由指定的File对象表示的文件。
* `public FileOutputStream(String name)`：创建文件输出流，指定的名称为写出文件。
* `public FileOutputStream(File file, boolean append)`：创建文件输出流，指明是否在现有文件末尾追加内容。

#### 2.3、文件字节流的最常使用方式：复制

代码：

```java
@Test
public void test8(){
    //创建File对象，指明文件复制的源文件和目标文件地址
    File srcFile = new File("./src/main/java/org/example/图片1.jpg");
    File destFile = new File("./src/main/java/org/example/复制后的图片1.jpg");
    
    FileInputStream fis = null;
    FileOutputStream fos = null;
    try {
        //创建输入输出流
        fis = new FileInputStream(srcFile);
        fos = new FileOutputStream(destFile);

        //创建byte[]数组，该数组用于存储从文件中读取的数据
        byte[] buffer = new byte[1024];
        int len;
        
        //当len != -1时，说明buffer中有从文件中读取来的数据
        while ((len = fis.read(buffer)) != -1){
            //将buffer中的数据写出到destFile文件中
            fos.write(buffer, 0, len);
        }
        
    } catch (IOException e) {
        throw new RuntimeException(e);
    }finally {
        try {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

此时就实现了文件的复制

文件的复制基本上都使用上述的方式进行

对于文本文件来说，复制也可以使用上述的方式，但是对于仅仅读取或写出操作，文本文件则不能使用字节流，因为字节流有可能打印到控制台上会出现乱码，但是复制操作可以使用字节流。

#### 总结

字节流与字符流的操作是一样的，只不过read()方法和write()方法的参数不同，一个使用的是byte[]，一个使用的是char[]数组。



#### 注意点

> 在字符流注意点的基础上，字节流的其他注意点：
>
> * 对于**`字符流`**，**只能用于操作文本文件**，不能用来处理非文本文件。
>
> * 对于**`字节流`**，**通常是用来处理非文本文件的，如果涉及到文本文件的`复制`操作，也可以使用字节流**。
>
> 
>
> **说明**
>
> 文本文件：.txt、.java、.c、.cpp、.py等
>
> 非文本文件：.doc、.xls、.ppt、.pdf、.mp3、.mp4、.jpg等

### 使用文件流复制目录：

```java
private static void copyDir(File file, File file1) {
    if (file.isFile()){
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {

            //读这个文件
            fis = new FileInputStream(file);

            //先获取源路径的相对路径
            String strFile = file.getAbsolutePath();

            //目标路径：拷贝目标路径 + 源路径（截取掉盘符）后面的路径。
            String strNew = (file1.getAbsolutePath().endsWith("\\") ? file1.getAbsolutePath():file1.getAbsolutePath() + "\\")+ strFile.substring(3);

            //写到这个文件中
            fos = new FileOutputStream(strNew);

            //一边读一边写
            byte[] bytes = new byte[1024]; //一次复制1MB
            int ioo = 0;
            while ((ioo = fis.read(bytes)) != -1){
                fos.write(bytes,0,ioo);
            }

            //刷新
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //如果是一个文件的话，递归结束
        return;
    }

    //获取源下的子目录
    File[] file2 = file.listFiles();
    if (file2 != null) {
        for(File file3 : file2){
            //获取所有文件的（包括目录和文件）绝对路径
            //System.out.println(file3.getAbsolutePath());
            //如果是一个目录
            if(file3.isDirectory()){
                //新建对应的目录
                String str = file3.getAbsolutePath();//获取源目录

                //获取目标目录
                //判断是否是以斜杠(\)结尾
                String strSub = (file1.getAbsolutePath().endsWith("\\") ? file1.getAbsolutePath():file1.getAbsolutePath() + "\\") + str.substring(3);

                //新建一个File对象
                File newFile = new File(strSub);

                //判断目录是否存在，如果不存在就新建多重目录
                if(!newFile.exists()){
                    newFile.mkdirs();
                }
            }
            //递归调用
            copyDir(file3,file1);
        }
    }
}
```



## `ByteArrayOutputStream`字节数组输出流

`ByteArrayOutputStream`，可以用来解决字节流获取字符文件数据，而出现乱码的现象。

在我们之前学过，如果使用字节流去获取字符型数据，并将其打印到控制台上，可能会出现乱码，而`ByteArrayOutputStream`流可以解决这一问题。

它虽然是**输出流**，但是却不需要指定文件输出地址，而是将数据写入到内存中，其内部维护了一个**`byte`类型的数组**，它会将数据write()输出到自身内部的byte数组中。这样一来，就会将字节输入流的所有字节数据，全部写入到byte数组中，就不会出现因为字符转换为字节后，因字节被拆分而造成的乱码现象。

**使用方式案例代码：**

```java
@Test
public void test(){
    FileInputStream fis = null;
    //创建ByteArrayOutputStream对象，无需指定打印文件地址
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
        fis = new FileInputStream(new File("test.txt"));
        byte[] buffer = new byte[5];
        int len;
        StringBuilder str = new StringBuilder();
        while ((len = fis.read(buffer)) != -1){
            //打印，这里接收的是其内部的byte数组
            baos.write(buffer, 0, len);
        }
        System.out.println(baos.toString());
    } catch (IOException e) {
        throw new RuntimeException(e);
    }finally {
        try {
            if (fis != null) {
                fis.close();
            }
            baos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

test.txt文件内容：

![image-20240122201453371](.\images\image-20240122201453371.png)

**打印结果：**

<img src=".\images\image-20240122201515048.png" align="left">

此时，无论使用多长的byte[]数组去接收输入的数据，都不会出现乱码现象。







# 处理流

## 处理流之一：缓冲流

### 1、概述

* **作用**：**`为了提高数据读写的速度`**。

  Java API提供了带缓冲功能的流类：缓冲流。

* 缓冲流要“套接”在相应的节点流之上，根据数据操作单位可以把缓冲流分为：

  * **字节缓冲流**：**`BufferedInputStream`**、**`BufferedOutputStream`**
  * **字符缓冲流**：**`BufferedReader`**、**`BufferedWriter`**

* **缓冲流的基本原理**：在创建流对象时，内部会创建一个缓冲区数组（缺省使用**`8192个字节（8KB）`**的缓冲区），通过缓冲区读写，减少IO次数，从而提高读写的效率。

![image-20240116093823333](.\images\image-20240116093823333.png)

<img src=".\images\image-20220514183413011.png" alt="image-20220514183413011" style="zoom:80%;" />

调用flush()刷新方法，将缓冲区中的内容读入/写出。







### 2、四种缓冲流以及使用的方法

四个缓冲流										使用的方法

**处理非文本文件的字节流：**		

**`BufferedInputStream`**			`read(byte[] buffer)`

**`BufferedOutputStream`**		`write(byte[] buffer, 0, len)`



**处理文本文件的字节流：**

**`BufferedReader`**				`read(char[] cBuffer)  /  readLine()`

**`BufferedWriter`**					`write(char[] cBuffer, 0, len)`



### 3、构造器

* **`public BufferedInputStream(InputStream in)`**：创建一个新的字节型的缓冲输入流，需要传入一个字节输入流类型对象。
* **`public BufferedOutputStream(OutputStream out)`**：创建一个新的字节型缓冲输出流，需要传入一个字节输出流类型对象。
* **`public BufferedReader(Reader in)`**：创建一个新的字符型缓冲输入流。
* **`public BufferedWriter(Writer out)`**：创建一个新的字符串缓冲输出流。



### 4、实现步骤

第一步：创建File对象、流的对象（包括文件流、缓冲流）



第二步：使用缓冲流实现读取或写出数据的过程

​	读取：int read(char[] cbuf / byte[] buffer)：每次将数据读入到cbuf / buffer数组中，并返回

​	写出：void write(String str) / write(char[] cbuf)：将str或cbuf写出到文件中

​			   void write(byte[] buffer)：将byte[]写出到文件中



第三步：关闭资源



案例：使用缓冲流实现图片的复制



```java
@Test
public void test(){
    //创建相关的File类对象（其中，需要被复制的File必须有文件对应，复制后的File对象可不存在）
    File srcFile = new File("./src/main/java/org/example/图片1.jpg");
    File destFile = new File("src/main/java/org/example/复制的图片.jpg");

    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
    try {
        //创建缓冲流，处理流需要使用节点流或其他处理流作为参数创建，即相当于包裹在其他流上
        bis = new BufferedInputStream(new FileInputStream(srcFile));
        bos = new BufferedOutputStream(new FileOutputStream(destFile));

        //进行读写操作
        byte[] buffer = new byte[1024];
        int len;
        while ((len = bis.read(buffer)) != -1){
            bos.write(buffer, 0, len);
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }finally {
        
        try {
            //关闭流资源
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```



> 说明：
>
> 1. 涉及到嵌套的多个流时，如果都显式关闭的话，需要先关闭外层的流，再关闭内层的流。
> 2. 其实在开发中，只需要关闭最外层的流即可，因为在关闭外层流时，内层的流也会被关闭。





### 5、缓冲流效率测试

测试：对比测试缓冲流是否真的有提高读写速度的功能

使用文件流复制一份300MB目录：

```java
private static void copyDir(File file, File file1) {
    if (file.isFile()){
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {

            //读这个文件
            fis = new FileInputStream(file);

            //先获取源路径的相对路径
            String strFile = file.getAbsolutePath();

            //目标路径：拷贝目标路径 + 源路径（截取掉盘符）后面的路径。
            String strNew = (file1.getAbsolutePath().endsWith("\\") ? file1.getAbsolutePath():file1.getAbsolutePath() + "\\")+ strFile.substring(3);

            //写到这个文件中
            fos = new FileOutputStream(strNew);

            //一边读一边写
            byte[] bytes = new byte[1024]; //一次复制1kb
            int ioo = 0;
            while ((ioo = fis.read(bytes)) != -1){
                fos.write(bytes,0,ioo);
            }

            //刷新
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //如果是一个文件的话，递归结束
        return;
    }

    //获取源下的子目录
    File[] file2 = file.listFiles();
    if (file2 != null) {
        for(File file3 : file2){
            //获取所有文件的（包括目录和文件）绝对路径
            //System.out.println(file3.getAbsolutePath());
            //如果是一个目录
            if(file3.isDirectory()){
                //新建对应的目录
                String str = file3.getAbsolutePath();//获取源目录

                //获取目标目录
                //判断是否是以斜杠(\)结尾
                String strSub = (file1.getAbsolutePath().endsWith("\\") ? file1.getAbsolutePath():file1.getAbsolutePath() + "\\") + str.substring(3);

                //新建一个File对象
                File newFile = new File(strSub);

                //判断目录是否存在，如果不存在就新建多重目录
                if(!newFile.exists()){
                    newFile.mkdirs();
                }
            }
            //递归调用
            copyDir(file3,file1);
        }
    }
}
```

测试代码：

```java
@Test
public void test3(){
    long start = System.currentTimeMillis();
    copyDir(new File("E:\\测试\\jdk7"), new File("E:\\测试\\复制后的地址"));
    long end = System.currentTimeMillis();
    System.out.println("所花时间为：" + (end - start));
}
```

打印结果：

<img src=".\images\image-20240116111556734.png" align="left">





**改成使用缓冲流复制一份相同文件：**

将复制代码进行修改：

```java
private static void copyDir(File file, File file1) {
    if (file.isFile()){
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;


        try {

            //读这个文件
            FileInputStream fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);

            //先获取源路径的相对路径
            String strFile = file.getAbsolutePath();

            //目标路径：拷贝目标路径 + 源路径（截取掉盘符）后面的路径。
            String strNew = (file1.getAbsolutePath().endsWith("\\") ? file1.getAbsolutePath():file1.getAbsolutePath() + "\\")+ strFile.substring(3);

            //写到这个文件中
            FileOutputStream fos = new FileOutputStream(strNew);
            bos = new BufferedOutputStream(fos);

            //一边读一边写
            byte[] bytes = new byte[1024]; //一次复制1kb
            int ioo = 0;
            while ((ioo = bis.read(bytes)) != -1){
                bos.write(bytes,0,ioo);
            }

            //刷新
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //如果是一个文件的话，递归结束
        return;
    }

    //获取源下的子目录
    File[] file2 = file.listFiles();
    if (file2 != null) {
        for(File file3 : file2){
            //获取所有文件的（包括目录和文件）绝对路径
            //System.out.println(file3.getAbsolutePath());
            //如果是一个目录
            if(file3.isDirectory()){
                //新建对应的目录
                String str = file3.getAbsolutePath();//获取源目录

                //获取目标目录
                //判断是否是以斜杠(\)结尾
                String strSub = (file1.getAbsolutePath().endsWith("\\") ? file1.getAbsolutePath():file1.getAbsolutePath() + "\\") + str.substring(3);

                //新建一个File对象
                File newFile = new File(strSub);

                //判断目录是否存在，如果不存在就新建多重目录
                if(!newFile.exists()){
                    newFile.mkdirs();
                }
            }
            //递归调用
            copyDir(file3,file1);
        }
    }
}
```

测试代码：

```java
@Test
public void test3(){
    long start = System.currentTimeMillis();
    copyDir(new File("E:\\测试\\jdk7"), new File("E:\\测试\\复制后的地址"));
    long end = System.currentTimeMillis();
    System.out.println("所花时间为：" + (end - start));
}
```

打印结果：

<img src=".\images\image-20240116111410992.png" align="left">

我们可以发现，缓冲流可以让我们的读写速度更快，读写的效率更高，复制所花的时间更少了。





### 6、字符缓冲流特有方法

字符缓冲流的基本方法与普通字符流调用方式一致，不再赘述，我们来看它们具备的特有方法。

* BufferedReader：**`public String readLine()`**：读取一行文字，返回读取的那一行数据的String类型，返回的字符串不包含换行符。当读取到末尾时，返回null。

* BufferedWriter：`public void newLine()`：表示换行操作。

  newLine()经常与readLine()一起使用。因为readLine()表示读取一行数据，但是不去换行，与newLine()一起使用刚好读取完一行，然后换一行。

使用案例：

使用BufferedReader和BufferedWriter实现文本文件的复制

```java
@Test
public void test12(){
    File file1 = new File(".\\src\\main\\java\\org\\example\\hello.txt");
    File file2 = new File(".\\src\\main\\java\\org\\example\\hello_copy.txt");
    BufferedReader br = null;
    BufferedWriter bw = null;
    try {
        br = new BufferedReader(new FileReader(file1));
        bw = new BufferedWriter(new FileWriter(file2));
        String str;
        while ((str = br.readLine()) != null){
            //readLine()读取一行数据，write()写一行
            bw.write(str);
            //相当于换行符，就是去换一行
            bw.newLine();
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }finally {
        try {
            if (br != null) {
                br.close();
            }
            if (bw != null) {
                bw.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

### 7、何时使用缓冲流

当**`文件比较大`**时，此时我们去读写数据，就需要使用到缓冲流。



## 处理流之二：转换流

### 1、问题引入

如果希望程序在读取文本文件时，不出现乱码，需要注意什么？

**解码时使用的字符集必须与当初编码时使用的字符集相同。**

编码：字符 -> 字节（从我们看得懂的 --> 我们看不懂的）

解码：字节 -> 字符（从我们看不懂的 --> 我们看得懂的）

**问题的引入：**

使用`FileReader`读取项目中的文本文件。由于IDEA设置中针对项目设置了UTF-7编码，当读取Windows系统中创建的文本文件时，如果Windows系统默认的是GBK编码，则读入内存中会出现乱码。

案例：

```java
@Test
public void test(){
    File file = new File("C:\\Users\\14036\\Desktop\\test.txt");
    FileReader fr = null;
    try {
        fr = new FileReader(file);
        char[] cBuffer = new char[20];
        int len;
        while ((len = fr.read(cBuffer)) != -1){
            System.out.println(new String(cBuffer, 0, len));
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }finally {
        try {
            if (fr != null) {
                fr.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

当文件使用GBK编码，打印出来的结果：

<img src=".\images\image-20240116181121925.png" align="left">

使用中文的就会出现乱码。这个时候就需要使用到转换流。



### 2、转换流的理解

转换流也是一种处理流，它提供了字节流和字符流之间的转换。在Java IO流中提供了两个转换流：**`InputStreamReader`** 和 **`OutputStreamWriter`**，这两个类都属于字符流。其中`InputStreamReader`将**字节输入流**转为**字符输入流**，继承自`Reader`。`OutputStreamWriter`是将**字符输出流**转为字节输出流，继承自`Writer`。



众所周知，计算机中存储的数据都是二进制的数字，我们在电脑屏幕上看到的文字信息是将二进制转换之后显示的，两者之间存在**编码**和**解码**的过程，其互相转换必须遵循某种规则，即编码和解码都遵循同一种规则才能将文字信息正常显示，如果编码和解码使用了不同的规则，就会出现乱码的情况。

* 编码：字符、字符串（能看懂的）→ 字节（看不懂的）
* 解码：字节（看不懂的） → 字符、字符串（能看懂的）

上面说的编码和解码过程需要遵循某种规则，这种规则就是不同的字符编码。我们刚刚学习编程的时候最早接触的就是ASCII码，它主要是用来显示英文和一些符号，到最后还有接触到别的编码规则常用的有：gb2312，gbk，utf-8等。它们分别属于不同的编码集。

转换流的特点：是字符流和字节流之间的桥梁。

​	可对读取到的字节数据经过指定编码转换成字符

​	可对读取到的字符数据经过指定编码转换成字节

**注意点：**

> 我们在电脑上看到的文件，呈现给我们的实际上都是已经经过了编码后的，实际上存储在电脑中的就是一些二进制的数据。
>
> 我们使用转换流，实际上去转换的就是存储在电脑中的二进制数据，而不是经过编码后我们看到的数据。
>
> InputStreamReader将电脑中的二进制数据转换成我们指定编码集的编码
>
> OutputStreamWriter将编码后的数据转换成电脑中的二进制数据存储在磁盘中





**那么何时使用转换流？**

> ​	当字节和字符之间有转换动作时
>
> ​	流操作的数据需要编码和解码时



**`作用：实现字节与字符之间的转换。`**

<img src=".\images\2_zhuanhuan.jpg" style="zoom: 67%;" />



具体来说：

<img src=".\images\image-20220412231533768.png" alt="image-20220412231533768" style="zoom:85%;" />



即

InputStreamReader可以将字节转换成指定编码类型的字符

OutputStreamWriter可以将字符转换成指定编码类型的字节



### 3、InputStreamReader

* 转换流`java.io.InputStreamReader`，是Reader的子类，是**字符流**，是从字节流到字符流的桥梁。它读取字节，并使用指定的字符集将其解码为字符。它的字符集可以由名称指定，也可以接受平台的默认字符集。
* **该流的作用**：**`将存储在磁盘上的二进制数据，通过指定的字符编码集，将其转换成字符数据`**。
* **构造器：**
  * `InputStreamReader(InputStream in)`：创建一个使用默认字符集的字符流。
  * `InputStreamReader(InputStream in, String charsetName)`：创建一个指定字符集的字符流。

案例：

```java
@Test
public void test1(){
    File file = new File("C:\\Users\\14036\\Desktop\\test.txt");
    InputStreamReader isr = null;
    try {
        FileInputStream fis = new FileInputStream(file);
        //使用GBK编码集进行解码
        isr = new InputStreamReader(fis,"GBK");
        char[] cBuffer = new char[100];
        int charData;
        //读取文件中的二进制字节数据，读入到内存中，转换成指定编码集的字符
        while ((charData = isr.read(cBuffer))!= -1){
            System.out.println(new String(cBuffer, 0, charData));
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }finally {
        try {
            if (isr != null) {
                isr.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```



打印结果：

<img src=".\images\image-20240117155351553.png" align="left">

为什么会出现乱码？

因为test.txt文件是使用utf-8编码集进行编码（字符转换成二进制字节）的，即将指定的字符根据编码集转换成二进制数据，所以我们去解码（二进制字节转换成字符）时，也需要使用utf-8编码集进行解码，如果使用GBK，就会出现乱码的情况。

对于文件中的英文字符，则不会出现乱码，因为GBK和utf-8都向下兼容了ASCII码。



**注意点：**

> **InputStreamReader`解码集`没得选，`必须与`File对应文件编码时的`编码集一致`，这样才不会出现乱码。**
>
> 因为在字符使用不同的编码集编码成二进制数据，需要使用相同的解码集解码成字符。

### 4、OutputStreamWriter

* 转换流`java.io.OutputStreamWriter`，是Writer的子类，属于字符流，是从字符流到字节流的桥梁。使用指定的字符集将字符编码为字节。它的字符集可以由名称指定，也可以接受平台的默认字符集。
* 构造器：
  * **`OutputStreamWriter(OutputStream in)`**：创建一个使用默认字符集的字符流。
  * **`OutputStreamWriter(OutputStream in, String charsetName)`**：创建一个指定字符集的字符流。



**注意：**

> OutputStreamWriter是将字符编码成二进制数据，可以根据需要使用不同的编码集。

### 5、使用案例

需求：将gbk格式的文件转换成utf-8格式的文件存储、

注意：InputStreamReader设置的编码集，必须是和原文件的解码集一样，否则将文件的二进制数据转换成字符时，会出现乱码。

而且，由于`InputStreamReader`和`OutputStreamWriter`是**字符流**，所以**只能用于`字符类型的文件`进行编码集的转换**。

代码如下：

```java
@Test
public void test2(){
    File srcFile = new File("C:\\Users\\14036\\Desktop\\test.txt");
    File destFile = new File("C:\\Users\\14036\\Desktop\\复制后的文件.txt");

    InputStreamReader isr = null;
    OutputStreamWriter osw = null;
    try {
        //GBK是解码集，InputStreamReader的解码集必须与原文件的编码集一致
        isr = new InputStreamReader(new FileInputStream(srcFile), "GBK");
        //参数2的uft-8是字符流转换成字节流的编码集，是将内存中的字符转换成文件中的字节使用到的编码集
        osw = new OutputStreamWriter(new FileOutputStream(destFile), "utf-8");

        char[] cBuffer = new char[1024];
        int len;
        while ((len = isr.read(cBuffer)) != -1){
            osw.write(cBuffer, 0, len);
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }finally {
        try {
            if (isr != null) {
                isr.close();
            }
            if (osw != null) {
                osw.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

原文件：

![image-20240117162520173](.\images\image-20240117162520173.png)

复制后的文件：

![image-20240117162551137](.\images\image-20240117162551137.png)



### 6、字符编码和字符集详解

#### 6.1、编码与解码

计算机中存储的信息都是用`二进制`数表示的，而我们在屏幕上看到的数字、英文、标点符号、汉字等字符是二进制数转换之后的结果。按照某种规则，将字符存储到计算机中，称为**编码**。反之，将存储在计算机中的二进制数按照某种规则解析出来，称为**解码**。

**字符编码（Character Encoding）**：就是一套自然语言的字符与二进制数之间的对应规则。

**编码表**：生活中文字和计算机中二进制的对应规则。

**乱码的情况**：按照A规则存储，同样按照A规则解析，那么就能显示正确的文本符号。反之，按照A规则存储，再按照B规则解析，就会导致乱码的情况。

```
编码：字符（人能看懂的） → 字节（人看不懂的）

解码：字节（人看不懂的） → 字符（人能看懂的）
```



#### 6.2、字符集

* **字符集Charset**：也叫编码表。是一个系统支持的所有字符的集合，包括各国家文件、标点符号、图形符号、数字等。
* 计算机要准确的存储和识别各种字符集符号，需要进行字符编码，一套字符集必然至少有一套字符编码。常见的字符集有ASCII字符集、GBK字符集、Unicode字符集等。

可见，当指定了**编码**，它所对应的**字符集**自然就指定了，所以编码才是我们最终要关心的。

* **ASCII字符集**：主要用来存储英文字符、阿拉伯数字和常用的标点符号。每个字符占用**`一个字节`**。

  * ASCII码(American Standard Code for Information Interchange，美国信息交换标准代码)：上个世纪60年代，美国制定了一套字符编码，对`英文字符`与二进制位之间的关系，做了统一规定。这被称为ASCII码。
  * ASCII码用于显示现代英语，主要包括控制字符（回车键、退格、换行键等）和可显示字符（英文大小写字符、阿拉伯数字和西文符号）。
  * 基本的ASCII字符集，使用7位（bits）表示一个字符（最前面的1位统一规定为0），使用**单字节**编码，每个字符占用一个字节，共`128个`字符。比如：空格“SPACE"是32（二进制00100000），大写的字母A是65（二进制01000001）。
  * 缺点：不能表示所有字符。

* **ISO-8859-1字符集**：

  * 拉丁码表，别名Latin-1，用于显示欧洲使用的语言，包括荷兰语、德语、意大利语、葡萄牙语等。
  * ISO-8859-1使用**单字节**编码，**`兼容ASCII编码`**。

* **GBxxx字符集**：

  * GB就是国标的意思，是为了`显示中文`而设计的一套字符集。
  * **GB2312**：简体中文码表。一个小于127的字符的意义与原来相同，即向下兼容ASCII码。但两个大于127的字符连在一起时，就表示一个汉字，这样大约可以组合了包含`7000多个简体汉字`，此外数字符号、罗马希腊的字母、日文的假名们都编进去了，这就是常说的“全角”字符，而原来在127号以下的那些符号就叫做“半角”字符了。
  * **GBK**：最常用的中文码表。是在GB2312标准基础上的扩展规范，使用`双字节`编码方案，共收录了`21003个`汉字，完全兼容GB2312标准，同时支持`繁体汉字`以及日韩汉字等。
  * **GB18030**：最新的中文码表。收录汉字`70244个`，采用`多字节`编码，每个字可以由1个、2个或4个字节组成。支持中国国内少数民族的文字，同时支持繁体汉字以及日韩汉字等。
  * **中文字符**是使用**`两个字节`**存储的，但是对于英文字符、**阿拉伯数字以及标点符号**是使用**`一个字节`**存储的，兼容**`ASCII码`**。

* **UTF-8字符集：**

  * UTF-8、UTF-16、UTF-32是三种将数字转换到程序数据的编码方案。顾名思义，UTF-8就是每次8个位传输数据，而UTF-16就是每次16个位。其中，**UTF-8是在互联网上`使用最广`的一种Unicode的实现方式**。utf-8可以用来存储世界范围内主要的语言的所有的字符。

  * 互联网工程工作小组（IETF）要求所有互联网协议都必须支持UTF-8编码。所以，我们开发Web应用，也要使用UTF-8编码。UTF-8是一种`变长的编码方式`。它使用1-4个字节为每个字符编码，编码规则：

    > 1. 128个US-ASCII字符，只需一个字节编码。
    > 2. 拉丁文等字符，需要两个字节编码。
    > 3. 大部分常用字（含中文），使用三个字节编码。
    > 4. 其他极少使用的Unicode辅助字符，使用四字节编码。

    **`UFT-8也向下兼容ASCII码。`**

- 举例

Unicode符号范围  | UTF-8编码方式

```
(十六进制)           | （二进制）

————————————————————|—–—–—–—–—–—–—–—–—–—–—–—–—–—–

0000 0000-0000 007F | 0xxxxxxx（兼容原来的ASCII）

0000 0080-0000 07FF | 110xxxxx 10xxxxxx

0000 0800-0000 FFFF | 1110xxxx 10xxxxxx 10xxxxxx

0001 0000-0010 FFFF | 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx

以上，就是utf-8使用不同字节存储数据时，二进制数据使用不同方式开头来进行区别。
```

![image-20220525164636164](.\images\image-20220525164636164.png)

- 小结

![字符集](.\images\字符集.jpg)

> 注意：在中文操作系统上，ANSI（美国国家标准学会、AMERICAN NATIONAL STANDARDS INSTITUTE: ANSI）编码即为GBK；在英文操作系统上，ANSI编码即为ISO-8859-1。



**问题：**

char占用两个字节，但是对于utf-8来说，我们知道中文的字符占用3个字节。那到底存储的中文字符（使用utf-8字符集编码）到底是占两个字节还是三个字节呢？

**答：**

我们所说的字符集占用的字节，指的是存储在文件中占有的空间，而一个char字符占用两个字节，指的是在内存中占用的空间。

如果我们将char字符存储到文件中，是根据字符集来存储的，如果使用的是GBK，中文字符就是占用两个字节；如果使用的是utf-8，中文字符就是占用三个字节。

比如，要去存储'a'字符，在文件中，无论使用ASCII码，还是GBK或者utf-8，都是使用一个字节存储；但是在内存中，'a'都是使用两个字节存储。

即：

> **在`内存`中，无论是中文字符，还是英文字符，都是使用`两个字节`来存储。**
>
> **在`文件`中，才会因编码集的不同，使用`不同数量的字节`存储字符。**
>
> 
>
> **内存中实际上使用的是`Unicode字符集`存储数据的。**

* **Unicode字符集：**
  * Unicode编码为表达`任意语言的任意字符`而设计，也称为统一码、标准万国码。Unicode将世界上**所有的文字**用**`2个字节`**统一进行编码，为每个字符设定唯一的二进制编码，以满足跨语言、跨平台进行文本处理的要求。所以，**Unicode字符集无法向下兼容`ASCII码`**。
  * Unicode的缺点：这里有三个问题：
    * 第一，英文字母只用一个字节表示就够了，如果用更多的字节存储是`极大的浪费`。
    * 第二，如何才能`区别Unicode和ASCII`？计算机怎么知道两个字节表示一个符号，而不是分别表示两个符号呢？
    * 第三，如果和GBK等双字节编码方式一样，用最高位是1或0表示两个字节和一个字节，就少了很多值无法用于表示字符，`不够表示所有字符`。
  * Unicode在很长一段时间内无法推广，直到互联网的出现，为解决Unicode如何在网络上传输的问题，于是面向传输的众多UTF（USC Transfer Format）标准出现。具体来说，有三种编码方案，UTF-8、UTF-16、UTF-32。

由于以上的各种原因，Unicode字符集只用于内存中字符的存储，无法使用到文件中。

## 处理流之三/四：数据流、对象流

如果需要将内存中定义的变量（包括基本数据类型或引用数据类型）保存在文件中，那怎么办呢？

例如：

```java
int age = 300;
char gender = '男';
int energy = 5000;
double price = 75.5;
boolean relive = true;

String name = "巫师";
Student stu = new Student("张三",23,89);
```

Java提供了数据流和对象流来处理这些类型的数据。

### 数据流（了解即可）

数据流**`只能处理基本数据类型和String类型`**，不能处理一般的引用数据类型。故，其只需要进行了解即可。

* `DataOutputStream`：允许应用程序将基本数据类型、String类型的变量写进输出流中。
* `DataInputStream`：允许应用程序以与机器无关的方式从底层输入流中读取基本数据类型、String类型的变量。

对象流DataInputStream中的方法：

```
byte readByte()  
short readShort()
int readInt()    
long readLong()
float readFloat() 
double readDouble()
char readChar()				 
boolean readBoolean()
String readUTF()               
void readFully(byte[] b)
```

数据流DataOutputStream中的方法就是将上述的方法中的read改为相应的write即可。

**数据流的弊端**：只支持Java基本数据类型和字符串的读写，而不支持其他Java对象的类型。而对象流ObjectOutputStream和ObjectInputStream既支持Java基本数据类型的读写，又支持引用类型的Java对象的读写。所以，重点介绍对象流ObjectOutputStream和ObjectInputStream。



### 对象流

#### 1、对象流的理解

对象流除了可以处理**`基本数据类型`**以外，还可以处理**`一般的引用数据类型对象`**。

* **`ObjectOutputStream`**：将Java基本数据类型和对象写入字节输出流中，实现Java各种基本数据类型以及引用对象的持久存储。
* **`ObjectInputStream`**：对以前使用ObjectOutputStream写出的基本数据类型的数据和对象进行读入操作，保存在内存中。

> 说明：对象流的强大之处就是可以把Java中的对象写入到数据源中，也能把对象从数据源中还原出来。



#### 2、对象流的API

##### 2.1、ObjectOutputStream

**ObjectOutputStream中的构造器：**

* **`public ObjectOutputStream(OutputStream out)`**：创建一个对象输出流。

例如：

```java
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("game.dat"));
```



**ObjectOutputStream中的方法：**

* `public void writeBoolean(boolean val)`：写出一个boolean值
* `public void writeByte(int val)`：写出一个8位字节。
* `public void writeShort(int val)`：写出一个16位的short值
* `public void writeChar(int val)`：写出一个16位的char值
* `public void writeInt(int val)`：写出一个32位的int值
* `public void writeLong(long val)`：写出一个64位的long值
* `public void writeFloat(float val)`：写出一个32位的float值。
* `public void writeDouble(double val)`：写出一个64位的double值。
* `public void writeUTF(String str)`：将表示长度信息的两个字节写入输出流，后跟字符串s中每个字符的UTF-7修改版表示形式。根据字符的值，将字符串s中每个字符转换成一个字节、两个字节或三个字节的字节组。注意，将String作为基本数据写入流中将它作为Object写入流中明显不同。如果str为null，则抛出NullPointerException。
* **`public void writeObject(Object obj)`**：写出一个Object对象。
* `public void close()`：关闭此输出流并释放与此流相关联的任何系统资源。

##### 2.2、ObjectInputStream

**构造器**：

* **`public ObjectInputStream(InputStream in)`**：创建一个ObjectInputStream。

```java
ObjectInputStream ois = new ObjectInputStream(new FileInputStream("game.dat"));
```

**方法：**

- `public boolean readBoolean()`：读取一个 boolean 值
- `public byte readByte()`：读取一个 8 位的字节
- `public short readShort()`：读取一个 16 位的 short 值
- `public char readChar()`：读取一个 16 位的 char 值
- `public int readInt()`：读取一个 32 位的 int 值
- `public long readLong()`：读取一个 64 位的 long 值
- `public float readFloat()`：读取一个 32 位的 float 值
- `public double readDouble()`：读取一个 64 位的 double 值
- `public String readUTF()`：读取 UTF-8 修改版格式的 String
- **`public void readObject(Object obj)`**：读入一个obj对象
- `public void close()` ：关闭此输入流并释放与此流相关联的任何系统资源

#### 3、认识对象序列化机制

##### 3.1、何为对象序列化机制？

`对象序列化机制`，允许把内存中的Java对象转换成平台无关的二进制流，从而允许把这种二进制流持久地保存在磁盘上，或通过网络将这种二进制流传输到另一个网络节点。当其他程序获取到了这种二进制流，就可以恢复到成原来的Java对象。

* **序列化过程**：用一个字节序列可以表示一个对象，该字节序列包含该`对象的类型`和`对象中存储的属性`等信息。字节序列写出到文件之后，相当于文件中`持久保存`了一个对象的信息。
* **反序列化过程**：该字节序列还可以从文件中读取回来，重构对象，对它进行`反序列化`。`对象的数据`、`对象的类型`和`对象中存储的数据`信息，都可以用来在内存中创建对象。

<img src=".\images\image-20220503123328452.png" alt="image-20220503123328452" style="zoom:67%;" />

##### 3.2、序列化机制的重要性

序列化是RMI（Remote Method Invoke、远程方法调用）过程的参数和返回值都必须实现的机制，而RMI是JavaEE的基础。因此序列化机制是JavaEE平台的基础。

序列化的好处，在于可将任何实现了Serializable接口的对象转化为**字节数据**，使其在保存和传输时可被还原。



##### 3.3、实现原理

* 序列化：用ObjectOutputStream类保存基本类型数据或对象的机制。方法为：
  * `public final void writeObject(Object obj)`：将指定的对象写出。
* 反序列化：用ObjectInputStream类读取基本类型数据或对象的机制。方法为：
  * `public final Object readObject()`：读取一个对象。

<img src=".\images\3_xuliehua.jpg" style="zoom:70%;" />





案例：演示序列化与反序列过程

序列化：

```java
/**
 * 序列化过程，使用ObjectOutputStream流实现，将内存中的Java对象保存在文件中或通过网络传播出去
 */
@Test
public void test(){
    File file = new File("object.txt");
    ObjectOutputStream oos = null;
    try {
        oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeUTF("江山如此多娇，引无数英雄竞折腰");
        oos.flush();

        oos.writeObject("轻轻地我走了，正如我轻轻地来");
        oos.flush();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }finally {
        try {
            if (oos != null) {
                oos.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

反序列化：

```java
/**
 * 反序列化过程：将存储在文件中的编码读入到内存中转换成数据
 */
@Test
public void test1() {
    File file = new File("object.txt");
    ObjectInputStream ois = null;
    try {
        ois = new ObjectInputStream(new FileInputStream(file));
        String s1 = ois.readUTF();
        String s2 =(String) ois.readObject();
        System.out.println(s1);
        System.out.println(s2);
    } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException(e);
    }finally {
        try {
            if (ois != null) {
                ois.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

打印结果：

<img src=".\images\image-20240117220917668.png" align="left">

序列化之后使用文件保存，但是这个文件不是给我们看的，而是进行反序列化的，序列化后的文件中的数据肯定都是乱码，序列化保存在文件中或者通过网络传输后，让我们可以再使用反序列化读取到内存中。



以上序列化与反序列化的对象都是String类型的，对于自定义类型的对象进行序列化和反序列化是否有什么要求吗？

有要求，下面就介绍一下有哪些要求。

#### 4、如何实现序列化机制

如果需要让某个对象支持序列化机制，则必须让对象所属的类及其属性是可序列化的，为了让某个类是可序列化的，该类必须实现**`java.io.Serializable`**接口。

`Serializable`是一个标记接口，不实现此接口的类将不会使任何状态序列化或反序列化，会抛出`NotSerializableException`。

* 如果对象的某个属性也是引用属性类型，那么如果该属性也要序列化的话，也要实现`Serializable`接口
* 该类得到所有属性必须都是可序列化的。如果有一个属性不需要可序列化，则该属性必须注明是**瞬态**的，使用`transient`关键字修饰。
* **`静态`（static）变量的值不会序列化**。因为静态变量的值不属于某个对象。

`Serializable`接口给需要序列化的类，提供一个序列版本号：**`serialVersionUID`**。凡是实现Serializable接口的类都应该有一个表示序列化版本标识符的静态变量：

```java
static final long serialVersionUID = 127398132789L;//它的值由程序员随意指定即可。
```

* serivalVersionUID用来表明类的不同版本间的兼容性。简单来说，Java的序列化机制是通过在运行时判断类的serialVersionUID来验证版本一致性的。在进行反序列化时，JVM会把传来的字节流中的serialVersionUID与本地相应实体类的serialVersionUID进行比较，如果相同就认为是一致的，可以进行反序列化，否则就会出现序列化版本不一致的异常（`InvalidCastException`）。
* 如果类没有显示定义这个静态常量，它的值是Java运行时环境根据类的内部细节`自动生成`的。若类的实例变量做了修改，serialVersionUID`可能发生变化`。因此，需要显式声明。
* 如果声明了serialVersionUID，即使在序列化完成之后修改了类导致类重新编译，则原来的数据也能正常反序列化，只是新增的字段值是默认值而已。





**测试：**

定义一个类Person，然后直接去使用对象流将创建的Person类对象写出到文件中：

定义的Person类：

```java
class Person{
    String name;
    int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

	//getter、setter方法以及toString()方法省略
}
```

使用ObjectOutputStream进行输出测试：

```java
@Test
public void test3(){
    File file = new File("object1.dat");
    ObjectOutputStream oos = null;
    try {
        oos = new ObjectOutputStream(new FileOutputStream(file));

        Person p1 = new Person("Tom", 22);
        oos.writeObject(p1);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }finally {
        try {
            if (oos != null) {
                oos.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
```

执行结果：

![image-20240117225611263](.\images\image-20240117225611263.png)

原因就在于Person类未实现`Serializable`接口，该类就不可进行序列化与反序列化操作。

当自定义类Person实现Serializable接口后，就执行成功了。

然后我们进行反序列化，将文件中的数据转换成类对象数据：

```java
@Test
public void test4(){
    File file = new File("object1.dat");
    ObjectInputStream ois = null;
    try {
        ois = new ObjectInputStream(new FileInputStream(file));
        Person p = (Person)ois.readObject();
        System.out.println(p);
    } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException(e);
    } finally {
        try {
            if (ois != null) {
                ois.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

打印结果：

<img src=".\images\image-20240117233427689.png" align="left">

我们注意到，这个Person类中没有设置`serialVersionUID`，但是序列化与反序列化过程依旧成功了，这是为什么呢？

实际上是因为Person类没有发生变化。

让我们思考一下serivalVersionUID这个全局常量的**作用**：

> **`标识传输的对象是否属于同一个类`**。

当我们进行序列化时，比如对Person类对象进行序列化，序列化完成后进行了存储、网络传输等等一些操作，然后我们想要对序列化的文件进行反序列化成内存中的Person类对象数据，那么肯定是需要转换成相同的Person类型对象，所以serivalVersionUID就可以去告诉我们转换前对象所属的类型与转换后的类型是否属于同一个类，是否该类发生了变化（因为类型改变以后，对象无法转变回来）。

案例：

当未设置serialVersionUID后，我们对Person类进行修改，比如增加一个id类型的属性：

```java
class Person implements Serializable{
    String name;
    int age;
    int id;
    
    //省略其他构造器和方法
}
```

此时去测试输入流，打印结果为：

<img src=".\images\image-20240117233627946.png" align="left">

结果报错，实际上就是因为存储到文件中的对象，反序列化接收的类是修改过的，会报错。

此时，我们就可以使用`serialVersionUID`全局常量，用来验证版本的一致性，

当给Person类添加serialVersionUID全局常量：

```java
class Person implements Serializable{
    @Serial
    private static final long serialVersionUID = 12316387126L;

    String name;
    int age;
    int id;
}
```

此时再去调用输入流进行测试：

![image-20240117233757740](.\images\image-20240117233757740.png)

此时就能够让我们得到传入的Person类型对象数据。





**总结：**

> 自定义类要想实现序列化机制，需要满足：
>
> 1. 自定义类需要**实现接口**：`Serializable`
> 2. 要求自定义类声明一个**全局常量**：`static final long serialVersionUID`
> 3. 要求自定义类的**各个属性也必须是可序列化的**。
>    * 对于**基本数据**类型的属性，默认就是可序列化的。
>    * 对于**引用数据**类型的属性，要求实现`Serializable`接口



#### 5、注意点：

1. 如果不声明全局常量serialVersionUID，系统会自动声明生产一个针对于当前类的serialVersionUID。

   如果修改此类的话，会导致serialVersionUID变化，进而导致反序列化时，出现`InvalidClassException`异常。

2. 类中的属性如果声明为`transient`或`static`，不会实现序列化。

在实际的开发中，一般在网络传输中使用最多的都是String类型的数据（JSON数据），String类本身就去实现了Serializable接口，不需要我们另外进行操作就可以进行序列化了。



# 其他流

## 标准输入流、输出流

* **`System.in`**和**`System.out`**分别代表了系统标准的输入和输出设备
* 默认输入设备是：**键盘**；默认输出设备是：**显示器**
* System.in的类型是`InputStream`
* System.out的类型是`PrintStream`，其父类是FilterOutputStream，FilterOutputStream是OutputStream的子类。
* 重定向：通过System类的`setIn()`，`setOut()`方法对默认设备进行改变。
  * **`public static void setIn(InputStream in)`**
  * **`public static void setOut(PrintStream out)`**

举例：

从键盘输入字符串，要求将读取到的整行字符串转成大写输出。然后继续进行输入操作，知道输入"e"或"exit"时，退出程序。

```java
@Test
public void test(){
    System.out.println("请输入信息（退出输入e或exit）：");

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String s = null;
    try {
        while ((s = br.readLine()) != null){
            if ("e".equalsIgnoreCase(s) || "exit".equalsIgnoreCase(s)){
                System.out.println("安全提出！");
                break;
            }
            //将读取到的整行字符转成大写输出
            System.out.println("-->：" +s.toUpperCase());
            System.out.println("继续输入信息：");
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }finally {
        try {
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

**这里为什么要使用InputStreamReader？**

InputStreamReader是转换流，用处是将字节流转换成字符流。System.in默认从键盘输入，是一个字节流，我们需要从键盘输入字符串，将字符串转换成大写输出，那么就需要获取从键盘输入的信息，将其转换成字符串，也就需要转换流。



**这里为什么要使用BufferedReader，为什么不直接使用InputStreamReader呢？**

BufferedReader提供了一个新的方法：`readLine()`，表示读取一行数据。

如果我们只使用InputStreamReader，这个类里面没有读取一行数据的方法，只有读取一个一个的数据，或者将读取的数据放到char[]数组中的方法。所以，我们需要使用到BufferedReader套在InputStreamReader上。





**拓展**：

System类中有三个常量对象：System.out、System.in、System.err

查看System类中这三个常量对象的声明：

![image-20240118153707541](.\images\image-20240118153707541.png)

![image-20240118153733611](.\images\image-20240118153733611.png)

![image-20240118153756823](.\images\image-20240118153756823.png)

对其我们有一些疑问：

* 这三个常量对象有final声明，但是却初始化为null。final声明的常量一旦赋值就不能修改，那么这三个常量声明有什么意义？
* 这三个常量对象为什么要小写？常量按照命名规范不是应该大写吗？
* 这三个常量有set方法，final声明的常量不是不能修改值吗？set方法是如何修改它们的值的？

根据上述的疑问，我们打开setIn()方法查看：

![image-20240118154203725](.\images\image-20240118154203725.png)

打开setIn0()方法：

![image-20240118154255249](.\images\image-20240118154255249.png)

我们可以看到，setIn()方法中实际对in、out和err常量进行操作的都使用了**`native`修改**。使用native就表示这个方法不是使用Java语言来实现的，而是使用C或C++实现。

所以，我们可以得出一个结论：

> final声明的常量，表示在`Java的语法系统`中它们的值**不可被修改**，但如果使用`C/C++语言`则**可以进行修改**。
>
> 这三个常量对象的值是由C/C++等系统函数进行初始化和修改值的，所以它们故意没有用大写，也有set()方法。





## 打印流

* 实现将基本数据类型的数据格式转化为字符串输出。

* 打印流：`PrintStream`和`PrintWriter`

* PrintStream 打印的所有字符都使用平台的默认字符编码集转换为字节。

  在需要读入的是字符，而不是字节的情况下，应该使用 PrintWriter 类去打印字符。

  * 提供了一系列重载的print()和println()方法，用于多种数据类型的输出

  ![image-20220131021502089](.\images\image-20220131021502089.png)

  * PrintStream和PrintWriter的输出不会抛出IOException异常
  * PrintStream和PrintWriter有自动flush()功能
  * System.out返回的是PrintStream的实例

* **构造器**

  * PrintStream(File file) ：创建具有指定文件且不带自动行刷新的新打印流。 
  * PrintStream(File file, String csn)：创建具有指定文件名称和字符集且不带自动行刷新的新打印流。 
  * PrintStream(OutputStream out) ：创建新的打印流。 
  * PrintStream(OutputStream out, boolean autoFlush)：创建新的打印流。 autoFlush如果为 true，则每当写入 byte 数组、调用其中一个 println 方法或写入换行符或字节 ('\n') 时都会刷新输出缓冲区。
  * PrintStream(OutputStream out, boolean autoFlush, String encoding) ：创建新的打印流。 
  * PrintStream(String fileName)：创建具有指定文件名称且不带自动行刷新的新打印流。 
  * PrintStream(String fileName, String csn) ：创建具有指定文件名称和字符集且不带自动行刷新的新打印流。

使用案例：

```java
@Test
public void test() {
    PrintStream ps = null;
    try {
        ps = new PrintStream("io.txt");
        ps.println("hello");
        ps.println(1);
        ps.println("adjpajd");
        ps.println(343);

        //System.out默认是控制台打印出来
        //这里去设置了输出的方式，使用的是指定文件输出，那么就会输出到io.txt中
        System.setOut(ps);
        System.out.println("你好！Java");
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }finally {
        if (ps != null) {
            ps.close();
        }
    }
}
```

io.txt显示结果：

<img src=".\images\image-20240118162506167.png" align="left">





### 打印流的应用：自定义一个打印日志信息工具

日志信息工作类：

```java
public class Logger {
    public static void log(String msg){
        PrintStream out = null;
        try {
            
            //由于日志信息肯定不能够覆盖，所以需要在PrintStream中传入FileOutputStream对象参数
            //FileOutputStream对象的构造器第二个参数需要设置成true，表示在后面加上数据
            out = new PrintStream(new FileOutputStream("log.txt", true));
            
            //改变系统输出方向
            System.setOut(out);
            
            //获取当前的时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
            String time = sdf.format(new Date());
            
            //打印日志信息，打印到指定文件中
            System.out.println(time + "：" + msg);
            
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            if (out != null) {
                //关闭输出流
                out.close();
            }
        }
    }
}
```

打印信息测试：

```java
public class LogTest {
	public static void main(String[] agrs){
        Logger.log("调用了System类的gc()方法，建议启动垃圾回收");
        Logger.log("调用了TeamView的addMember()方法");
        Logger.log("用户尝试进行登录，验证失败");
	}
}
```





## Scanner类

构造方法

* Scanner(File source) ：构造一个新的 Scanner，它生成的值是从指定文件扫描的。 
* Scanner(File source, String charsetName) ：构造一个新的 Scanner，它生成的值是从指定文件扫描的。 
* Scanner(InputStream source) ：构造一个新的 Scanner，它生成的值是从指定的输入流扫描的。 
* Scanner(InputStream source, String charsetName) ：构造一个新的 Scanner，它生成的值是从指定的输入流扫描的。

常用方法：

* boolean hasNextXxx()： 如果通过使用nextXxx()方法，此扫描器输入信息中的下一个标记可以解释为默认基数中的一个 Xxx 值，则返回 true。
* Xxx nextXxx()： 将输入信息的下一个标记扫描为一个Xxx

```java
package com.atguigu.systemio;

import org.junit.Test;

import java.io.*;
import java.util.Scanner;

public class TestScanner {

    @Test
    public void test01() throws IOException {
        Scanner input = new Scanner(System.in);
        PrintStream ps = new PrintStream("1.txt");
        while(true){
            System.out.print("请输入一个单词：");
            String str = input.nextLine();
            if("stop".equals(str)){
                break;
            }
            ps.println(str);
        }
        input.close();
        ps.close();
    }
    
    @Test
    public void test2() throws IOException {
        Scanner input = new Scanner(new FileInputStream("1.txt"));
        while(input.hasNextLine()){
            String str = input.nextLine();
            System.out.println(str);
        }
        input.close();
    }
}
```

## apache-common包的使用

### 介绍

IO技术开发中，代码量很大，而且代码的重复率较高，为此Apache软件基金会，开发了IO技术的工具类`commonsIO`，大大简化了IO开发。

Apahce软件基金会属于第三方，（Oracle公司第一方，我们自己第二方，其他都是第三方）我们要使用第三方开发好的工具，需要添加jar包。

### 导包及举例

- 在导入commons-io-2.5.jar包之后，内部的API都可以使用。


 ![image-20220416004246436](.\images\image-20220416004246436.png)

- IOUtils类的使用

```java
- 静态方法：IOUtils.copy(InputStream in,OutputStream out)传递字节流，实现文件复制。
- 静态方法：IOUtils.closeQuietly(任意流对象)悄悄的释放资源，自动处理close()方法抛出的异常。
```

```java
public class Test01 {
    public static void main(String[] args)throws Exception {
        //- 静态方法：IOUtils.copy(InputStream in,OutputStream out)传递字节流，实现文件复制。
        IOUtils.copy(new FileInputStream("E:\\Idea\\io\\1.jpg"),new FileOutputStream("E:\\Idea\\io\\file\\柳岩.jpg"));
        //- 静态方法：IOUtils.closeQuietly(任意流对象)悄悄的释放资源，自动处理close()方法抛出的异常。
       /* FileWriter fw = null;
        try {
            fw = new FileWriter("day21\\io\\writer.txt");
            fw.write("hahah");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
           IOUtils.closeQuietly(fw);
        }*/
    }
}
```

- FileUtils类的使用

```java
- 静态方法：void copyDirectoryToDirectory(File src,File dest)：整个目录的复制，自动进行递归遍历
          参数:
          src:要复制的文件夹路径
          dest:要将文件夹粘贴到哪里去
             
- 静态方法：void writeStringToFile(File file,String content)：将内容content写入到file中
- 静态方法：String readFileToString(File file)：读取文件内容，并返回一个String
- 静态方法：void copyFile(File srcFile,File destFile)：文件复制
```

```java
public class Test02 {
    public static void main(String[] args) {
        try {
            //- 静态方法：void copyDirectoryToDirectory(File src,File dest);
            FileUtils.copyDirectoryToDirectory(new File("E:\\Idea\\io\\aa"),new File("E:\\Idea\\io\\file"));


            //- 静态方法：writeStringToFile(File file,String str)
            FileUtils.writeStringToFile(new File("day21\\io\\commons.txt"),"柳岩你好");

            //- 静态方法：String readFileToString(File file)
            String s = FileUtils.readFileToString(new File("day21\\io\\commons.txt"));
            System.out.println(s);
            //- 静态方法：void copyFile(File srcFile,File destFile)
            FileUtils.copyFile(new File("io\\yangm.png"),new File("io\\yangm2.png"));
            System.out.println("复制成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```



9af29d872c6a93255ec4725543aeae1847cd1508
