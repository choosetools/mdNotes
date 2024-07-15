## IDEA项目结构

**层级关系**：

```tex
project（工程） - module（模块） - package（包） - class（类）
```

具体的：

> 一个project中可以创建多个module
>
> 一个module中可以创建多个package
>
> 一个package中可以创建多个class

这些结构的划分，是为了方便管理功能代码。

## project和module的概念

在IDEA中，提出了project和module这两个概念。

<img src=".\images\20220523014358169.png" style="zoom:80%;" />

在IntelliJ IDEA中project是最顶级的结构单元，然后是module。目前，主流的大型项目结构基本上都是多module结构，这类项目一般是按照功能划分的，比如user-core-module、user-facade-module和user-hessian-module等等，模块之间彼此可以相互依赖，有着不可分割的业务关系。因此，对于一个project来说：

* 当为单module项目时，这个单独的module实际上就是project。
* 当为多module项目时，多个模块处于一个project中，此时彼此之间具有相互依赖的关联关系。
* 当多个module之间没有建立依赖关系的话，也可以作为单独一个“小项目”运行。

例如：

<img src=".\images\Snipaste_2023-10-17_19-26-34.png" style="zoom:80%;">

在这个项目中，实际上有两个module：

<img src=".\images\Snipaste_2023-10-17_19-28-33.png">

我们可以看到有一个JavaSECode模块以及一个studyCode模块，即项目project也是作为一个模块。

## module和package

在一个module下，可以声明多个包（package），一般命名规则如下：

```
1.不要有中文
2.不要以数字开头
3.给包取名时一般都是公司域名倒着写，而且都是小写
比如：阿里的网址：www.alibaba.com
那么包名应该写成：com.alibaba.子名字
```

## 创建module

建议创建“empty空工程”，然后创建多模块，每一个模块可以独立运行，相当于一个小项目。JavaSE阶段不涉及到模块之间的依赖。

步骤：

（1）选择创建模块

![](.\images\1655167625885.png )

(2)选择模块类型：这里选择创建Java模块，给模块命名，确定存放位置

<img src=".\images\1659191966074.png" style="zoom:50%;">

（3）模块声明在工程下面

<img src=".\images\1659192028623.png">

## 导入模块

（1）将模块teacher_chapter04整个复制到自己IDEA项目路径下

<img src=".\images\1659192514219.png" style="zoom:80%;">

接着打开自己IDEA的项目，会在项目目录下看到拷贝过来的module，但是此时不是以模块的方式呈现。

<img src=".\images\1659192692658.png" style="zoom:80%;">

（2）查看Project Structure，选中import module

<img src=".\images\image-20220615213827271.png" style="zoom:80%;">

<img src=".\images\image-20220615214746952.png" >

（3）选中要导入的module：

<img src=".\images\1659192850055.png" style="zoom:50%;">

<img src=".\images\image-20220615214916374.png" style="zoom:50%;">

（4）接着可以一路next下去，最后选中Overwrite

最后点击ok即可。