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





# 处理流

## 处理流之一：缓冲流

### 概述

* **作用**：**`为了提高数据读写的速度`**。

  Java API提供了带缓冲功能的流类：缓冲流。

* 缓冲流要“套接”在相应的节点流之上，根据数据操作单位可以把缓冲流分为：

  * **字节缓冲流**：**`BufferedInputStream`**、**`BufferedOutputStream`**
  * **字符缓冲流**：**`BufferedReader`**、**`BufferedWriter`**

* **缓冲流的基本原理**：在创建流对象时，内部会创建一个缓冲区数组（缺省使用**`8192个字节（8KB）`**的缓冲区），通过缓冲区读写，减少IO次数，从而提高读写的效率。

![image-20240116093823333](.\images\image-20240116093823333.png)

<img src=".\images\image-20220514183413011.png" alt="image-20220514183413011" style="zoom:80%;" />

调用flush()刷新方法，将缓冲区中的内容读入/写出。







### 四种缓冲流以及使用的方法

四个缓冲流										使用的方法

**处理非文本文件的字节流：**		

**`BufferedInputStream`**			`read(byte[] buffer)`

**`BufferedOutputStream`**		`write(byte[] buffer, 0, len)`



**处理文本文件的字节流：**

**`BufferedReader`**				`read(char[] cBuffer)  /  readLine()`

**`BufferedWriter`**					`write(char[] cBuffer, 0, len)`



### 构造器

* **`public BufferedInputStream(InputStream in)`**：创建一个新的字节型的缓冲输入流，需要传入一个字节输入流类型对象。
* **`public BufferedOutputStream(OutputStream out)`**：创建一个新的字节型缓冲输出流，需要传入一个字节输出流类型对象。
* **`public BufferedReader(Reader in)`**：创建一个新的字符型缓冲输入流。
* **`public BufferedWriter(Writer out)`**：创建一个新的字符串缓冲输出流。



### 实现步骤

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





### 缓冲流效率测试

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





### 字符缓冲流特有方法

字符缓冲流的基本方法与普通字符流调用方式一致，不再赘述，我们来看它们具备的特有方法。

* BufferedReader：`public String readLine()`：读一行文字。
* BufferedWriter：`public void newLine()`：写一行行分隔符，由系统属性定义符号。







## 处理流之二：转换流



## 处理流之三/四：数据流、对象流





# 其他流



