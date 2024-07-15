[TOC]



# 一、HTML

## 1、HTML入门

### 什么是HTML？

HTML是Hyper Text Markup Lanauage的缩写，意思是**`超文本标记语言`**。它的作用是搭建网页结构，在网页上展示内容。

**什么是超文本？**

HTML文件本质上是文本文件，而普通的文本文件只能显示字符。但是HTML技术则通过HTML标签把其他网页、图片、音频、视频等各种多媒体资源引入到当前网页中，让网页有了非常丰富的呈现方式，这就是超文本的含义——本身是文本，但是呈现出来的效果最终超越了文本。

**什么是标记语言？**

说HTML是一种标记语言是因为它不像Java这样的编程语言，它是由一系列标签组成的，没有常量、变量、流程控制、异常处理、IO等这些功能。HTML很简单，每个标签都有它固定的含义和确定的页面显示效果。





### HTML基础结构

> 1、**文档声明** `<!DOCTYPE html>`

HTML文件第一行的内容，用来告诉浏览器当前HTML文档的基本信息，其中最重要的就是当前HTML文档遵循的语法规则。这里我们只需要知道HTML有4和5这两个大的版本。

HTML4版本的文档声明是：

```HTML
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
```

HTML5版本的文档声明是：

```HTML
<!DOCTYPE html>
```



> **2、根标签** `<html></html>`

html标签是整个文档的根标签，所有标签都必须放在html标签里面。在html标签下，有两个一级子标签，分别是head标签与body标签。



> **3、头标签** `<head></head>`

头标签中定义那些不直接展示只在页面主体上，但是又很重要的内容。例如字符集、CSS的引入、JS的引入等等。

head标签用于定义文档的头部，其他头部元素都放在head标签里。头部元素包括title标签、script标签、style标签、link标签、meta标签等等。



> **4、体标签** `<body></body>`

体标签中定义那些直接展示在页面上的内容，在浏览器窗口内显示的内容都定义到body标签内。

![1681180699132](.\images\1681180699132.png)



### HTML概念解释

* **标签**（`tag`）：代码中的一个<>叫做一个标签，有些标签成对出现，称之为双标签，有些标签单独出现，称之为单标签。
* **属性**（`attribute`）：对标签特征进行设置的一种方式，属性一般在开始标签中定义。
* **文本**（`text`）：双标签中间的文字，包含空格、换行等结构。
* **元素**（`element`）：经过浏览器解析后，每一个完整的标签（标签+属性+文本）可以称之为一个元素。



### HTML语法规则

1. 根标签有且只能有一个
2. 无论是双标签还是单标签都需要正确关闭
3. 标签可以嵌套但不能交叉嵌套
4. 注释语法为<!-- -->，注意不能嵌套
5. 属性必须有值，值必须加引号，H5中属性名和值相同时可以省略属性值
6. HTML中不严格区分字符串使用单双引号
7. HTML标签不严格区分大小写，但是不能大小写混写
8. HTML中不允许自定义标签名，强行定义无效






### VSCode的使用

VsCode的下载安装都是比较简单的，一直next就可以了。我们主要来讲一讲该如何使用。

**准备工作空间**

使用VsCode打开某个目录，将该目录作为项目存放的根目录，这就是VsCode的工作空间workspace。类似于IDEA中的根项目创建，作为最顶级的项目被创建出来。

![image-20240525232057917](.\images\image-20240525232057917.png) 

选择文件，作为VsCode的工作空间。

**在工作空间下创建目录和文件**

在工作空间下，创建目录和文件。在Java中，还有module以及包等概念；但是，对于前端来说，只有目录和文件的概念，目录就是用来存放文件的。

通过点击带有+号的按钮即可创建文件或目录：

<img src=".\images\1687330312538.png" alt="1687330312538" style="zoom:80%;" /> 

之后，就可以编辑相关代码，然后运行项目了。

这里，我们去通过live Server插件，来运行一个文件。

要求：在VsCode中，需要已经安装live Server插件：

![image-20240525232835970](.\images\image-20240525232835970.png) 

**创建文件进行测试**

在workspace中创建目录demo1，在该目录下，创建一个文件，叫做01hello.html，然后，在该html文件中，输入!并回车，可以快速地构建html的基本结构：

<img src=".\images\newhtml.gif" alt="newhtml" style="zoom: 50%;" /> 

然后，我们可以在body标签中，输入一些代码来进行测试，例如：

![image-20240525233815478](.\images\image-20240525233815478.png) 

然后，我们可以去查看一下内容的效果。





**通过live Server小型服务器运行项目**

点击右下角的Go Live，或者在html编辑视图上右击open with live Server，会去启动一个小型的服务器，并且打开浏览器访问当前的资源

![image-20240525234055422](.\images\image-20240525234055422.png) 

<img src=".\images\image-20240525234117103.png" alt="image-20240525234117103" style="zoom:50%;" /> 

点击，弹出浏览器：

<img src=".\images\image-20240525234229950.png" alt="image-20240525234229950" style="zoom: 67%;" /> 

可以看到，该插件使用了5500端口号，在本机上启动了一个小型的服务器，并且，浏览器的url就是该文件所在的路径，这样我们就可以访问该资源了。



并且，live Server具有**实时加载功能**，当我们去修改文件后，不需要重启该服务器，只需要Ctrl + s保存一下，生成的页面上的数据也会实时地更新：

<img src=".\images\GIF.gif" alt="GIF" style="zoom: 80%;" /> 



当我们关闭浏览器后，该服务器并不会关闭，我们需要手动地去关闭该服务器：

<img src=".\images\image-20240525234726215.png" alt="image-20240525234726215" style="zoom: 50%;" /> 







### VsCode比较重要的设置

+ 设置字体: 齿轮 > search > 搜索    "字体大小"

+ 设置字体大小可以用滚轮控制:齿轮 > 设置 > 搜索 "Mouse Wheel Zoom"

+ 设置左侧树缩进: 齿轮 > 设置 > 搜索  "树缩进"，这里的树缩进，指的是文件系统中，父子层文件之间缩进的距离：

  ![image-20240525235813432](.\images\image-20240525235813432.png) 

  当文件很多的时候，层级就很容易搞混，这个时候就需要将树缩进增大，方便查看

+ 设置文件夹折叠:  齿轮 > 设置 > 搜索 "compact" 

  文件夹折叠指的是当文件夹下还有一层文件夹，并且子文件夹中没有文件时，子文件夹是否与父文件夹折叠在一起：

  ![image-20240526000029399](.\images\image-20240526000029399.png) 

  就类似于Java中的包结构，包下没有文件时，就是折叠在一起的。

+ 设置编码自动保存: 齿轮> 设置> 搜索 "Auto Save" ,选择为"afterDelay"，表示自动保存，然后再去修改自动保存的时间：

  <img src=".\images\image-20240526000124597.png" alt="image-20240526000124597" style="zoom: 67%;" /> 

  这里将Auto Save Delay的值设置为1000，表示当未进行修改操作的1000毫秒后，自动保存。





<hr>

## 2、HTML常用标签

### 标题标签

> 标题标签一般用于在页面上定义一些标题性的内容，如新闻标题、文章标题等。
>
> 有`<h1>`到`<h6>`六级标题。

代码

```html
<body>
    <h1>一级标题</h1>
    <h2>二级标题</h2>
    <h3>三级标题</h3>
    <h4>四级标题</h4>
    <h5>五级标题</h5>
    <h6>六级标题</h6>
</body>
```

效果

![1681179289089](.\images\1681179289089.png) 



---

### 段落标签

> 段落标签`<p>`一般用于定义一些在页面上要显示的大段文字，多个段落标签之间实现自动分段的效果。

代码

```html
<body>
    <p>
        记者从工信部了解到，近年来我国算力产业规模快速增长，年增长率近30%，算力规模排名全球第二。
    </p>
    <p>
        工信部统计显示，截至去年底，我国算力总规模达到180百亿亿次浮点运算/秒，存力总规模超过1000EB（1万亿GB）。
        国家枢纽节点间的网络单向时延降低到20毫秒以内，算力核心产业规模达到1.8万亿元。中国信息通信研究院测算，
        算力每投入1元，将带动3至4元的GDP经济增长。
    </p>
    <p> 
        近年来，我国算力基础设施发展成效显著，梯次优化的算力供给体系初步构建，算力基础设施的综合能力显著提升。
        当前，算力正朝智能敏捷、绿色低碳、安全可靠方向发展。
    </p>
</body>
```

效果

<img src=".\images\1681180017304.png" alt="1681180017304" style="zoom: 67%;" /> 



---

### 换行标签

> 换行标签可以通过两个标签来实现：`<br>`与`<hr>`标签
>
> br是简单的实现换行功能，hr添加分隔线

代码

```html
<body>
        工信部统计显示，截至去年底，我国算力总规模达到180百亿亿次浮点运算/秒，存力总规模超过1000EB（1万亿GB）。
    <br>
        国家枢纽节点间的网络单向时延降低到20毫秒以内，算力核心产业规模达到1.8万亿元。
    <hr>
        中国信息通信研究院测算，算力每投入1元，将带动3至4元的GDP经济增长。
</body>
```

效果

![1681180239241](.\images\1681180239241.png)



---

### 列表标签

> **有序列表**：分条列项展示数据的标签，其每一项前面的符号都带有顺序特征
>
> 列表标签：`<ol>`
>
> 列表项标签：`<li>`

代码

```html
<ol>
    <li>Java</li>
    <li>前端</li>
    <li>大数据</li>
</ol>
```

效果

![1681194349015](.\images\1681194349015.png) 



> **无序列表**：分条列项展示数据的标签，其每一项前面的符号不带有顺序特征
>
> 列表标签：`<ul>`
>
> 列表项标签：`<li>`

代码

```html
<ul>
    <li>JAVASE</li>
    <li>JAVAEE</li>
    <li>数据库</li>
</ul>
```

效果

![ ](.\images\1681194434091.png) 





> **嵌套列表**：列表与列表之间可以互相嵌套，用于多层级别的展示

代码

```html
<ol>
    <li>
        JAVA
        <ul>
            <li>JAVASE</li>
            <li>JAVAEE</li>
            <li>数据库</li>
        </ul>
    </li>
    <li>前端</li>
    <li>大数据</li>
</ol>
```

效果

![1681194504371](.\images\1681194504371.png) 



当我们要去使用列表标签时，不能因为它们的样式而去使用，而是要根据它们所代表的含义去使用。因为所有的样式都可以使用CSS去更改，但是，有序列表就是当数据具有有序性时使用；无序列表就是当数据是无序性的，才去使用。



---

### 超链接标签

**超链接标签**：点击后带有链接跳转的标签，也叫作**`a标签`**。

使用a标签实现指定路径的跳转。其中有两个属性：

* `href`：用于定义要跳转的目标资源的地址。地址有三种类型：
  * `完整url`，例如：https://www.baidu.com
  * `相对路径`，以当前资源所在路径为出发点去寻找目标资源，./表示当前目录下（可以省略）；../表示上一层级目录下
  * `绝对路径`，以/开头，表示以当前工作空间为出发点去寻找目标资源。
* `target`：用于定义目标资源的打开方式，其中有两个类型的值：
  * `_blank`：在新窗口中打开目标资源
  * `_self`：在当前窗口中打开目标资源（默认值）

href标签中，可以填写绝对路径，也可以填写相对路径，也可以定义完整的URL。

案例代码：

```html
<body>
    <a href="http://www.atguigu.com/" target="_blank">新窗口打开尚硅谷</a>
    <a href="http://www.atguigu.com/" target="_self">当前窗口打开尚硅谷</a>
</body>
```

效果：

![动画](.\images\112121244.gif)



#### 对于路径的说明

href属性可以使用三种类型的路径：

1. **完整的url**，例如：https://www.baidu.com
2. **相对路径**，以当前资源的所在路径为出发点去找目标资源
3. **绝对路径**，无论当前资源在哪里，使用以固定的位置作为出发点去找目标资源。注意，绝对路径要以**`/`**作为开头。



##### 相对路径

完整的url就不必再阐述了。

相对路径是依据当前资源所在的路径为出发点去寻找目标资源。

比如在标题段落换行.html中的相对路径，就是相对于该文件所在路径去寻找目标资源，该文件下有一个helloworld.html，所以就可以直接在href中使用该文件：

![image-20240526131352318](.\images\image-20240526131352318.png) 

```html
<a href="helloworld.html" target="_self">打开helloworld.html</a>
```

那么，当我们点击该超链接时就会跳转到该页面：

<img src=".\images\123123123.gif" alt="动画" style="zoom: 67%;" /> 

再比如，在标题段落行.html文件中使用超链接跳转到上一层的demo2-css目录下的test.html文件：

![image-20240526133009487](.\images\image-20240526133009487.png) 

此时的超链接应该为：

```html
<a href="../demo2-css/test.html">打开上一层demo2-css目录下的test.html文件</a>
```

此时的跳转：

<img src=".\images\1231231231223423.gif" alt="动画" style="zoom:67%;" /> 



> **相对路径**
>
> * ./表示当前资源的目录下（可以省略）
>
> * ../表示当前资源的上一层级目录下
>
> * 一个/斜杆就表示一层目录下





##### 绝对路径

无论当前资源在哪里，使用以固定的位置作为出发点去找目标资源，以/开头。

注意：这里的绝对路径，不是以盘符开头的绝对路径，而是使用/开头。

我们来看看以/开头的绝对路径，是什么含义。

案例：

在工作空间下的demo1-html目录下的标题段落换行.html中，使用下面的超链接，访问同层级中的helloworld.html：

![image-20240526134034240](.\images\image-20240526134034240.png) 

```html
<a href="/demo1-html/helloworld.html">helloworld</a>
```

我们来看看能不能实现访问：

![动画](.\images\123123124213345453.gif) 

可以看到，成功实现访问了。

那么，也就是说，绝对路径的/表示的含义是：**`当前所在的工作空间workspace。`**

那么，也就是说，我们使用绝对路径，是以当前工作空间为基础，访问资源。相较于盘符的绝对路径，使用当前工作空间为开头的绝对路径能够保证在其他主机上也能够实现访问（工作空间会一起部署）

绝对路径的好处，就在于，当前文件进行移动时，原本在当前文件中所设定的相对目标资源路径就会失效，此时使用绝对路径不会因当前文件的移动而失效。





---

### 多媒体标签

多媒体标签分为`图片标签img`、`音频标签audio`以及`视频标签video`。

**图片标签img**

图片标签img中有三个属性：

* `src`：用于定义图片的路径
* `title`：用于定义鼠标悬停时显示的文字
* `alt`：用于定义图片加载失败时显示的提示文字

案例：

在图片.html文件中，引入当前路径下的img目录中的壁纸.jpg图片：

![image-20240526140231360](.\images\image-20240526140231360.png) 

那么，src的写法有两种：

1、使用相对路径：./img/壁纸.jpg

2、使用绝对路径：/demo1-html/img/壁纸.jpg

使用哪种都可以，建议最好使用绝对路径。

代码实现：

```html
<img src="./img/壁纸.jpg" width="300px" title="使用相对路径引入的壁纸" alt="这是一个图片" />

<img src="/demo1-html/img/壁纸.jpg" width="300px" title="使用绝对路径引入的壁纸" alt="这是一个图片" />
```

实现：

![动画](.\images\hfdoihoiqwheio.gif) 



**音频标签audio**

该标签用于在页面上引入一段声音。

其中有四个比较常用的属性：

* `src`：用于引入目标声音资源
* `autoplay`：用于控制打开网页时是否自动播放
* `controls`：用于控制是否展示控制面板
* `loop`：用于控制是否进行循环播放

例如：

```html
<audio src="img/music.mp3" autoplay="autoplay" controls="controls" loop="loop" />
```

效果

<img src=".\images\1681196276582.png" alt="1681196276582" style="zoom: 67%;" /> 



**视频标签video**

该标签用于在页面上引入一段视频。

常用的属性和音频标签一样：

* `src`：用于引入目标视频资源
* `autoplay`：用于控制打开网页时是否自动播放
* `controls`：用于控制是否展示控制面板
* `loop`：用于控制是否进行循环播放

```html
<video src="img/movie.mp4" autoplay="autoplay" controls="controls" loop="loop" width="400px" />
</body>
```

效果

<img src=".\images\1681196233304.png" alt="1681196233304" style="zoom: 67%;" /> 





---

### 表格标签（:star:）

> `table`标签，代表整个表格
>
> * `thead`标签，代表表头（可以省略）
> * `tbody`标签，代表表体（可以省略）
> * `tfoot`标签，代表表尾（可以省略）
>   * `tr`标签，代表一行
>     * `td`标签，代表行内的一格
>     * `th`标签，自带加粗和居中效果的td

以上就是表格的常规标签。

这里的thead、tbody以及tfoot都是可以省略的，如果省略了，就表示在该表格中不区分表头、表体或表尾。

当在表格中，没有去声明表头、表体、表尾，那么浏览器会自动在table标签下，声明一个tbody标签，将整体都看作是一个表的表体。

案例：

```html
<h3 style="text-align: center;">员工技能竞赛评分表</h3>
    <table border="1px" width="400px" align="center">
        <thead>
            <tr>
                <th>排名</th>
                <th>姓名</th>
                <th>分数</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>1</td>
                <td>张小明</td>
                <td>100</td>
            </tr>
            <tr>
                <td>2</td>
                <td>李小东</td>
                <td>99</td>
            </tr>
            <tr>
                <td>3</td>
                <td>王小虎</td>
                <td>98</td>
            </tr>
        </tbody>
        <tfoot>
            <tr>
                <td>总计</td>
                <td></td>
                <td>297</td>
            </tr>
        </tfoot>
    </table>
```

显示效果

<img src=".\images\image-20240526145452393.png" alt="image-20240526145452393" style="zoom:67%;" /> 





#### 表格的跨行与跨列

**单元格跨行**

通过td的`rowspan`属性实现上下跨行，一共占用多少行，rowspan的值就是多少。



我们来看一个例子：

想要实现的效果如下

<img src=".\images\1681197062594.png" alt="1681197062594" style="zoom:67%;" /> 

这里的备注那一栏，下面一个单元格将下面的两个单元格都侵占了，也就实现了三个单元格合并成一个单元格的效果。

那么该如何实现让一个单元格，多占用两行呢？

就需要使用到td的属性`rowspan`，row是行的意思，span是跨越的意思。

如果要实现上面的一个单元格，占用3行的效果，那么该单元格的rowspan属性就应该是3，表示跨越了3行的含义。

并且，因为第三行和第四行中，备注列的单元格会被第二行单元格占用，所以该位置的单元格省略。

实现：

```html
<h3 style="text-align: center;">员工技能竞赛评分表</h3>
    <table border="1px" style="margin: 0px auto; width: 400px;">
        <thead>
            <tr>
                <th>排名</th>
                <th>姓名</th>
                <th>分数</th>
                <th>备注</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>1</td>
                <td>张小明</td>
                <td>100</td>
                <td rowspan="3">前三名升职加薪</td>
            </tr>
            <tr>
                <td>2</td>
                <td>李小黑</td>
                <td>99</td>
            </tr>
            <tr>
                <td>3</td>
                <td>王小东</td>
                <td>98</td>
            </tr>
        </tbody>
    </table>
```







**单元格跨列**

通过td的`colspan`属性实现左右跨列。

案例：

在单元格跨行案例的基础上，我们需要来实现下面的表格：

<img src=".\images\1681197299564.png" alt="1681197299564" style="zoom:67%;" /> 

在原本的基础上，在表格的后面加上总人数、平均分以及及格率行。并且在这三行中，第二个单元格会占用两列。备注列下的单元格最终占用6行。

那么，我们可以使用colspan属性，来给最后三行中的第二个单元格实现占用两列的效果。

实现：

```html
<body>
    <h3 style="text-align: center;">员工技能竞赛评分表</h3>
    <table border="1px" style="margin: 0px auto; width: 400px;">
        <thead>
            <tr>
                <th>排名</th>
                <th>姓名</th>
                <th>分数</th>
                <th>备注</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>1</td>
                <td>张小明</td>
                <td>100</td>
                <td rowspan="6">前三名升职加薪</td>
            </tr>
            <tr>
                <td>2</td>
                <td>李小黑</td>
                <td>99</td>
                
            </tr>
            <tr>
                <td>3</td>
                <td>王小东</td>
                <td>98</td>
            </tr>
            <tr>
                <td>总人数</td>
                <td colspan="2">2000</td>
            </tr>
            <tr>
                <td>平均分</td>
                <td colspan="2">90</td>
            </tr>
            <tr>
                <td>及格率</td>
                <td colspan="2">80%</td>
            </tr>
        </tbody>       
    </table>
</body>
```



注意：

这里需要注意一个问题，这里的跨行rowspan，并不能实现不同表格类型的跨行，thead不能跨到tbody中，tbody不能跨行到tfoot中。

那么，也就是说，当我们在tbody中的单元格进行跨行时，不会去侵占tfoot中的单元格，只能侵占tbody中自身的单元格。

例如：

```html
	<table border="1px" style="margin: 0px auto; width: 200px;">
      <tbody>
        <tr>
          <td>单元格测试</td>
          <td rowspan="2">测试1</td>
        </tr>
      </tbody>
      <tfoot>
        <tr>
          <td>测试2</td>
        </tr>
      </tfoot>
    </table>
```

显示结果：

![image-20240526154416955](.\images\image-20240526154416955.png) 

可以发现，此时第一行的第二个单元格并没有与第二行的第二个单元格进行合并。

那么，也就是说，合并只会合并相同表格类型的单元格（同一个表头、表体、表尾），不会跨表格类型合并。



---

### 表单标签（:star:）

表单标签，可以实现让用户在界面上输入各种信息并提交的一种标签，是向服务端发送数据的主要方式之一。

**表单标签：`form`**

表单标签内衣用于定义可以让用户输入的表单项标签。

**其中有两个主要的属性：**

* `action`：定义数据的提交地址，既然是地址，那就有三种类型的路径。

* `method`：定义数据的提交方式。有两种方式：

  * `GET`：默认值，提交的数据会以**键值对**（key=value）的形式，放在url地址后面，以?作为参数开始的标识，多个参数之间使用&隔开。

    例如：url?key1=value2&key2=value2...

  * `POST`：数据会通过**请求体**发送，不会放在url之后。

  

在表单项标签中，使用的最多的是`input`标签，其中也有几个重要的属性：

* id：标签的唯一标识
* value：表单项的值

* name：用于定义提交的参数名
* type：用于定义input表单项类型
  * text		文本框
  * password    密码框
  * submit      提交按钮
  * reset       重置按钮



案例：

```html
<form action="welcome.html" method="get">
    用户名：<input type="text"><br/>
	密码：<input type="password"><br/>
	<input type="submit" value="登录" />
	<input type="reset" value="重置">
</form>
```

显示：

![image-20240526163444852](.\images\image-20240526163444852.png) 

此时，我们去输入数据并且点击登录：

<img src=".\images\动画.gif" alt="动画" style="zoom: 67%;" /> 

在该表单中，我们使用的是get方式提交的，按道理来说，跳转到页面后，url上的?后面应该跟着传输过去的参数才对，但是这里跳转后的url的?后面并没有参数，这是为什么？

原因在于，没有给input标签声明name属性。

> 表单项标签必须要声明**`name`**属性才能作为参数传输数据。该属性用于明确提交的参数名。
>
> 表单项要需要明确**`value`**属性，该属性用于定义提交的参数值。
>
> 以name作为key，以value作为值，以**`键值对`**的形式提交参数。

name属性，就相当于键值对中的key，而input标签的值，就相当于键值对中的value。所以，我们必须要给表单项标签设置name属性，才能提交参数，value值则是我们输入的数据。

那么，我们将原本的表单进行修改：

```html
<form action="welcome.html" method="get">
        用户名：<input type="text" name="username"><br/>
        密码：<input type="password" name="pwd"><br/>
        <input type="submit" value="登录" />
        <input type="reset" value="重置">
</form>
```

此时我们再去提交数据：

<img src=".\images\123131fasdf23123.gif" alt="动画" style="zoom: 80%;" /> 

发现，此时在url中就有相关的参数信息，通过了get提交的方式提交了参数。



以上都是使用get的方式进行提交的，对于post方式，会通过请求体进行提交，实际传输的方式就是JSON格式，这个在后面学。





---

#### GET、POST提交的区别

**这两种提交方式的特点：**

**`GET`**

​	1.参数会以键值对形式放在url后提交：url?key1=value1&key2=value2...

​	2.数据直接暴露在地址栏上，相对不安全

​	3.地址栏长度有限制，所以提交的数据量不大

​	4.地址栏上，只能是字符，不能提交文件

​	5.相比于POST，效率高一些

**`POST`**

​	1.参数默认不放到url后

​	2.数据不会直接暴露在地址栏上，相对安全

​	3.数据是单独打包通过请求体发送，post提交的数据量比较大

​	4.请求体中，可以是字符，也可以是字节数据，可以提交文件

​	5.相比于GET，效率略低（可以忽略不计）



所以，一般来说GET方式适合提交一些简单的数据；对于复杂的、要求安全的数据，应当使用POST方式提交。







---

### 常见表单项标签（:star:）

常见的表单项标签有：

1. 单行文本框
2. 密码框
3. 单选框
4. 复选框
5. 下拉框
6. 按钮
7. 隐藏域
8. 多行文本框
9. 文件标签

接下来，我们来一一介绍。



> **单行文本框**

`input`标签，type类型为`text`

案例代码：

```html
个性签名：<input type="text" name="signal" />
```

效果：

<img src="/images/1681198354039.png" alt="1681198354039" style="zoom: 50%;" /> 



---

> **密码框**

`input`标签，type类型为`password`

案例代码：

```html
密码：<input type="password"  name="secret" />
```

效果：

<img src=".\images\1681198393831.png" alt="1681198393831" style="zoom:50%;" /> 





---

> **单选框**

`input`标签，type类型为`radio`

说明：

* 在单选框中，根据`name`属性进行分组，name属性相同的radio为一组，同一组内进行单选，组内互斥，即：**控制同一组的单选框，设置相同name属性值，这些单选框只能选择一个，具有了互斥效果。**
* 可以声明checked属性，被设置`checked="checked"`属性的单选框默认被选中。
* 该标签是一个选择标签，用户无法输入文本，所以我们需要去定义value属性值。
* 当用户选择了一个radio并提交表单，这个radio的name属性和value属性组成一个键值对发送给服务器。
* 如果属性名和属性值一样的话，可以省略属性值，直接使用属性名。例如下例中的checked，由于原属性是：checked="checked"，所以只写checked即可。

案例：

```html
<form action="welcome.html" method="get">
	性别：<input type="radio" name="sex" value="man">男
	<input type="radio" name="sex" value="woman" checked>女<br>
	<input type="submit" value="登录" />
</form>
```

显示：

![动画](.\images\qiwehroiweuqi.gif)

通过这个演示的动图，我们可以看到：

当我们打开页面时，默认选中的是使用了checked="checked"属性的单选框标签。当我们提交之后，会将name=value键值对形式作为参数提交。



---

> **复选框**

`input`标签，type类型是`checkbox`

说明：

* 复选框与单选框一样，都是**通过name属性来判断属于是否同一个组**。只不过，复选框可以选择多个数据，即同组的复选框不会互斥。
* 在提交时，会使用键值对key=value的形式，将**所有被选中的复选框内容都提交**。
* 同样地，在复选框中，也可以使用`checked`属性，来默认选中复选框。

案例：

```html
<form action="welcome.html" method="get">
      你喜欢的球队是：
      <input type="checkbox" name="team" value="Brazil" />巴西
      <input type="checkbox" name="team" value="German" checked />德国
      <input type="checkbox" name="team" value="France" />法国
      <input type="checkbox" name="team" value="China" checked="checked" />中国
      <input type="checkbox" name="team" value="Italian" />意大利<br />
      <input type="submit" value="登录" />
</form>
```

演示效果：

![动画](.\images\qwehriohwqer.gif)

从上述的过程可以看出，复选框默认会选中使用了checked属性的元素，并且在提交时，会将所有选中的复选框，以name=value的形式都将参数进行提交。



---

> **下拉框**

`select`标签定义下拉框，其中的`option`标签设置列表项。

说明：

* 下拉列表用到了两种标签，其中select标签用来定义下拉列表，option标签设置列表项。

* **在下拉框中，name属性在select标签中设置，value属性在option标签中设置。**
* option标签的标签体是显示出来给用户看的，提交到服务器的是value属性的值。
* 通过在option标签中设置selected="selected"（可以省略为selected）属性，实现默认选中的效果。

案例：

```html
<form action="welcome.html" method="get">
	你喜欢的运动是：
	<select name="interesting">
		<option value="swimming">游泳</option>
		<option value="running">跑步</option>
		<option value="shooting" selected="selected">射击</option>
		<option value="skating">溜冰</option>
	</select><br/>
	<input type="submit" value="提交" />
</form>
```

效果：

此时，进入到页面中，下拉框默认选中的是射击这一选项：

![image-20240526190124033](.\images\image-20240526190124033.png) 

当我们选择其中的跑步，并且提交数据后，url为：

![image-20240526190145512](.\images\image-20240526190145512.png) 

由结果我们可以得知，下拉框在提交时，会将select中设置的name属性，以及选中的列表项所对应的value属性，组成name=value键值对的形式提交参数。







---

> **隐藏域**

`input`标签，type属性是`hidden`

通过表单隐藏域设置的表单项不会显示到页面上，用户看不到。但是提交表单时会一起被提交。用来设置一些需要和表单一起提交但是不希望用户看到的数据。比如：用户的id等等。

案例：

```html
<form action="welcome.html" method="get">
   <input type="hidden" name="userId" value="123456" />
   <input type="submit" value="提交" />
</form>
```

显示：

![动画](.\images\iowqerhowiqeh.gif)

如上图所示，在页面中并没有隐藏域标签的表单项显示，但是在提交时，会将隐藏域中的表单信息进行提交。



我们也可以通过使用`hidden="hidden"`属性来实现隐藏域的效果：

使用这种属性进行修饰的表单项标签也会在页面上进行隐藏，同时，在提交时，也会将被隐藏的表单项内容提交。例如：

```html
<input type="text" name="username" value="34567" hidden>
```

该文本表单项就不会显示在页面上，同时，在提交时，也会按照key=value键值对的形式进行提交，效果与隐藏域是一样的。



还有另外两种修饰表单项的属性：一种是`readOnly`；一种是`disabled`。

readOnly，表示表单只读，可以提交。

disabled，表示表单被禁用了，此时也是只读的，但是提交时不进行提交。

我们来看看这两个标签的案例：

```html
<form action="welcome.html" method="get">
	<input type="text" name="readOnly" value="123" readonly /><br/>
	<input type="text" name="disabled" value="456" disabled> <br/>
	<input type="submit" value="提交" />
</form>
```

显示为：

<img src=".\images\image-20240526185406660.png" alt="image-20240526185406660" style="zoom: 80%;" /> 

发现，被readOnly属性与disabled属性修饰的标签都不可修改，并且disabled修饰的标签不可被光标选中，颜色置灰。

当我们提交后：

<img src=".\images\image-20240526185532538.png" alt="image-20240526185532538" style="zoom: 80%;" /> 

发现readOnly修饰的标签可以提交数据，但是disabled修饰的标签不可提交数据。

所以，我们由此可以得知：

> 1. hidden表示隐藏，修饰的表单项不可见，可以提交数据。
> 2. readOnly表示只读，修饰的表单项不可被修改，可以提交数据。
> 3. disabled表示不可用，修饰的表单项不可被修改，不可提交数据。





---

> **多行文本框**

`textarea`标签

多行文本框，是为了输入一些较多文字内容而设置的标签。

为了能够让文本框进行提交，我们仍然需要给改标签**设置`name`属性**。

textarea标签**不能设置`value`属性**，如果需要设置默认值需要写在标签体中。

该标签是一个双标签，标签体中的内容实际上就是该标签的value属性值。

同时，我们可以在该标签中，使用style属性，去设置多行文本框的样式，比如设置该文本框长度为200px，高度为100px：`style="width: 200px; height: 100px;"`



案例：

```html
<form action="welcome.html" method="get">
      自我介绍：<textarea name="desc" style="width: 200px; height: 100px">请写自己介绍的内容</textarea><br />
      <input type="submit" value="提交" />
</form>
```

打开页面显示：

<img src=".\images\image-20240526193022904.png" alt="image-20240526193022904" style="zoom:80%;" /> 

在文本框中，输入内容，比如hello HTML，提交后跳转的url为：

![image-20240526193128238](.\images\image-20240526193128238.png) 

多行文本框将标签的name与value，也是使用name=value键值对的形式传递参数。







---

> **文件标签**

`input`标签，type类型为`file`

文件类型，只能使用POST方式进行提交，因为GET方式只能提交文本类型的数据，并且长度有限；而POST方式可以提交字节类型的数据。

文件标签的提交，name属性在标签中声明，value属性就是选择要提交的文件数据。

案例代码：

```html
<form action="welcome.html" method="post">
      头像：<input type="file" name="file"><br>
      <input type="submit" value="提交" />
</form>
```

显示：

![image-20240526194129338](.\images\image-20240526194129338.png) 

当选择一个文件后，比如选择图片.gif文件：

![image-20240526194202214](.\images\image-20240526194202214.png) 

此时，后面就会显示选择文件的名称。

这里我们暂时无法进行提交演示，因为我们使用的是live Server插件来生成的小型服务器，不支持POST提交方式提交数据。



---

### 布局相关标签

布局涉及到两个标签：`<div>`与`<span>`

div标签，俗称"块"，主要拥有划分页面结构，做页面布局。

span标签，俗称"层"，主要用于划分元素范围，配合CSS做页面元素样式的修饰。

div是块级元素，占用一整行；span是行级元素，有多大占多大。

案例：

```html
    <div style="width: 500px; height: 400px;background-color: cadetblue;">
        <div style="width: 400px; height: 100px;background-color: beige;margin: 10px auto;">
            <span style="color: blueviolet;">页面开头部分</span>
        </div> 
        <div style="width: 400px; height: 100px;background-color: blanchedalmond;margin: 10px auto;">
            <span style="color: blueviolet;">页面中间部分</span>
        </div> 
        <div style="width: 400px; height: 100px;background-color: burlywood;margin: 10px auto;">
            <span style="color: blueviolet;">页面结尾部分</span>
        </div> 
    </div>
```

显示结果：

<img src=".\images\1681200198741.png" alt="1681200198741" style="zoom:67%;" /> 







---

### 特殊字符

> 对于有特殊含义的字符，需要通过转义字符来表示

<img src=".\images\1681200435834.png" alt="1681200435834"  />



<img src=".\images\1681200467767.png" alt="1681200467767"  />



<img src=".\images\1681200487125.png" alt="1681200487125"  />

<img src=".\images\1681200503798.png" alt="1681200503798"  />

案例代码：

```html
&lt;span&gt; <br/>
&lt;a href="https://www.baidu.com"&gt;尚&nbsp;硅&nbsp;谷&lt;/a&gt;<br/>
 &amp;amp;
```

效果：

![image-20240526215948797](.\images\image-20240526215948797.png) 







---

# 二、CSS

CSS：层叠样式表，能够对网页中元素位置的排版进行像素级精确控制，支持几乎所有的字体字号样式，拥有对网页对象和模型样式编辑的能力。简单来说，CSS就是用于美化页面的。

## 1、CSS引入方式

CSS有三种引入的方式，分别是`行内式`、`内嵌式`以及`外部式`。

我们来设置一个按钮的样式，分别使用三种不同的引入方式来实现：

按钮的宽度设置为60px、高度为40px，背景色设置为黄绿色，字体颜色设置为白色，字体大小设置为20px，字体类型设置为隶书，添加2px、直线、绿色的边框，边框角使用5px的弧度。

#### 方式一：行内式

行内式，通过标签元素本身的**`style`**属性，在标签上设置样式来引入。

style属性中样式的语法格式为：`style="样式名: 样式值;样式名: 样式值;"`



使用行内式实现按钮的样式：

```html
<input type="button" value="按钮" 
    style="
    width: 60px;
    height: 40px;
    background-color: yellowgreen;
    color: white;
    font-size: 20px;
    font-family: '隶书';
    border: 2px solid green;
    border-radius: 5px;
    ">
```

显示：

![image-20240526224100706](.\images\image-20240526224100706.png) 

行内式的优点就在于能够直接在标签上设置样式，比较直观。

但是，行内式的**缺点**更多：

1. 代码的复用度低，不利于维护，并且会造成样式代码过多，文件过大的情况，从而影响性能。

2. CSS样式代码和html结构代码交织在一起，影响阅读。

为了解决这些缺点，就可以使用方式二来引入样式。



#### 方式二：内嵌式

内嵌式，是通过在head标签中的`style`标签，定义本页面的公共样式。

**在style标签中，使用`选择器`来确定当前样式作用的范围。**具体的选择器请看CSS选择器内容。

通过选择器，选定当前样式所修饰的标签，被选中的标签就会使用这些样式信息。

使用内嵌式实现：

```html
<head>
    <style>
      input {
        width: 60px;
        height: 40px;
        background-color: yellowgreen;
        color: white;
        font-size: 20px;
        font-family: "隶书";
        border: 2px solid green;
        border-radius: 5px;
      }
    </style>
  </head>
  <body>
    <input type="button" value="按钮"/>
    <input type="button" value="按钮"/>
    <input type="button" value="按钮"/>
  </body>
```

显示结果：

![image-20240526225851217](.\images\image-20240526225851217.png) 

说明：

* 内嵌样式需要声明在head标签中，通过一对style标签定义CSS样式。
* CSS样式的作用范围控制要依赖于选择器。
* CSS的样式代码中注释的方式为/* */
* 内嵌样式虽然对样式代码做了抽取，但是该方式只能作用于当前文件中，代码的复用性还不够。

如果我们想要在多个页面中，重用某些样式，可以使用外部样式表的方式引入样式。





#### 方式三：外部样式表

外部样式表，指的是去单独创建CSS样式文件，专门用来存放CSS样式代码，当html中需要这些样式代码，可以在head标签中，通过**`link`**标签引入外部的样式文件。

案例：

我们在demo2-css目录下，创建css目录，该目录表示专门用于存放CSS样式文件的目录。在该目录下，创建button.css样式文件：

![image-20240526230839885](.\images\image-20240526230839885.png) 

在该文件中，使用元素选择器，来给input标签设置样式：

```css
input {
        width: 60px;
        height: 40px;
        background-color: yellowgreen;
        color: white;
        font-size: 20px;
        font-family: "隶书";
        border: 2px solid green;
        border-radius: 5px;
      }
```

当我们需要使用这些标签时，在html文件的head标签中，通过link标签引入。

在link标签中，有两个属性需要填写：

* `href属性`，就是要引入文件的地址。可以使用相对路径，也可以使用绝对路径。

* `rel属性`，用于标识引入的是什么类型文件，对于CSS类型的文件，需要填写固定值`stylesheet`，表明引入的是样式文件。

```html
  <head>
    <link rel="stylesheet" href="./css/button.css">
  </head>
  <body>
    <input type="button" value="按钮"/>
    <input type="button" value="按钮"/>
    <input type="button" value="按钮"/>
  </body>
```

显示结果：

![image-20240526231353227](.\images\image-20240526231353227.png) 

说明：

* CSS样式文件代码从html文件中剥离，有利于代码的维护
* CSS样式文件可以被多个不同的html引入，有利于项目风格的统一

* 在开发中，我们一般会去声明一个CSS的目录，专门用来存放样式文件。







---

## 2、三大CSS选择器

CSS中，选择器有很多，包括一些基本选择器以及复合选择器，还有伪类选择器等等。这里我们就只介绍三种最基本的选择器：元素选择器、id选择器以及class选择器。

### 元素选择器

元素选择器，根据标签的名字确定样式的作用元素。

**语法：**

**`标签名 {}`**

元素选择器，就是根据标签名进行选择，会将所有该标签的元素都使用上对应的样式。

例如：

```html
<head>
    <style>
        input {
            width: 80px;
            height: 40px;
            background-color: chartreuse;
            color: white;
            border: 3px solid green;
            font-size: 22px;
            font-family: '隶书';
            line-height: 30px;
            border-radius: 5px;
        }
    </style>
  </head>
  <body>
    <input type="button" value="按钮"/>
    <input type="button" value="按钮"/>
    <input type="button" value="按钮"/>
    <input type="button" value="按钮"/>
  </body>
```

以上就是一个input的元素选择器，会去将所有的input标签元素都使用该选择器所设置的样式修饰。

显示结果：

![image-20240526232541923](.\images\image-20240526232541923.png) 

元素选择器作用的范围过大，当我们希望其中一些同名标签的元素不使用某些样式时，就无法实现。同时，我们希望某些不同名标签的元素也使用某些样式时，也无法实现。





### id选择器

id选择器，是根据标签的id值确定样式的作用元素。

一般每个标签都有id属性，但是在同一个页面中，id的值不应该相同，即标签的id属性值具有唯一性。

**语法格式：**

**`#id值 {}`**

案例：给第四个按钮设置样式

```html
<head>
    <style>
        #btn4 {
            width: 80px;
            height: 40px;
            background-color: chartreuse;
            color: white;
            border: 3px solid green;
            font-size: 22px;
            font-family: '隶书';
            line-height: 30px;
            border-radius: 5px;
        }
    </style>
  </head>
  <body>
    <input id="btn1" type="button" value="按钮"/>
    <input id="btn2" type="button" value="按钮"/>
    <input id="btn3" type="button" value="按钮"/>
    <input id="btn4" type="button" value="按钮"/>
  </body>
```

显示结果：

![image-20240526233814197](.\images\image-20240526233814197.png) 

由于id值具有唯一性，所以，一个选择器所设置的样式只能给一个标签设置样式，这样的话，如果想要给多个标签都设置某一样式，就需要一个一个地进行设置，这样过于麻烦。

那么，有没有一种选择器，能够选中多个不同标签的元素，给它们设置样式呢？

有，这就是class选择器。





### class选择器

class选择器，根据元素的class属性值确定样式的作用元素。

因为标签的class属性值可以重复，而且一个元素的class属性可以有多个值，所以class选择器，能够给多个元素设置样式，并且一个标签，也能够被多个选择器设置样式。所以，class选择器是使用的比较多的。

**语法格式：**

**`.class属性值 {}`**

案例：

首先，页面中的标签元素如下所示：

```html
<input id="btn1" type="button" value="按钮"/>
<input id="btn2" type="button" value="按钮"/>
<input id="btn3" type="button" value="按钮"/>
<input id="btn4" type="button" value="按钮"/>
```

去定义四种class选择器，分别用于定义形状、颜色、字体相关的样式：

```CSS
		.shapeClass{
            width: 80px;
            height: 40px;
            border-radius: 5px;
        }

        .colorClass{
            background-color: rgb(179, 241, 85);
            color: white;
            border: 3px solid green;
        }

        .fontClass{
            font-size: 20px;
            font-family: '隶书';
            line-height: 30px;
        }
```

如果我们想让btn1的按钮，使用其中的形状与颜色的样式，那么我们就可以在该标签中定义class属性：

```html
<input id="btn1" class="shapeClass colorClass" type="button" value="按钮"/>
<input id="btn2" type="button" value="按钮"/>
<input id="btn3" type="button" value="按钮"/>
<input id="btn4" type="button" value="按钮"/>
```

显示的效果：

![image-20240526235146276](.\images\image-20240526235146276.png) 



class选择器十分灵活，在实际开发中使用的也比较多。





---

## 3、CSS浮动

### 问题引入

我们先来为CSS浮动创建页面样式来讲解：

创建一个大的div盒子，在这个大盒子中，分别创建三个div小盒子

```html
<head>
    <style>
        .outerDiv{
            width: 500px;
            height: 300px;
            border: 1px solid gray;
            background-color: beige;
        }
        .innerDiv{
            width: 100px;
            height: 100px;
            border: 1px solid blue;
        }
        .d1{
            background-color: greenyellow;
        }
        .d2{
            background-color: red;
        }
        .d3{
            background-color: green;
        }
    </style>
</head>
<body>
    <div class="outerDiv">
        <div class="innerDiv d1">diva</div>
        <div class="innerDiv d2">divb</div>
        <div class="innerDiv d3">divc</div>
    </div>
</body>
```

显示的结果为：

<img src=".\images\image-20240527003755670.png" alt="image-20240527003755670" style="zoom:67%;" /> 

这其中的三个盒子元素都是div元素，div是块元素，独占一行。

如果我们想让其中的三个盒子元素，在同一行，我们就要想办法把这些块元素独占一行的特征进行修改，就比如把这些块元素修改成行内元素。

我们可以使用`display`属性进行修改：div元素的display属性默认是`block`，也就是块元素的意思；我们可以将其修改成`line`，也就是行内元素的意思。

那么，我们在.innerDiv选择器中进行修改，将盒内的三个小盒子修改成行内元素：

```css
.innerDiv{
	width: 100px;
	height: 100px;
	border: 1px solid blue;
	display: inline;
}
```

此时显示的结果为：

<img src=".\images\image-20240527004632724.png" alt="image-20240527004632724" style="zoom:50%;" /> 

可以看到，此时这三个盒子是在同一行中了，但是现在存在一个问题：

div元素是具有宽高特征的，但是将这三个div标签设置为行内元素之后，宽高的特征就消失了。这是因为行内元素宽高的设置是不生效的，其宽高的依据是内部的文字，其内部文字占用多少宽高，行内元素就占用多少宽高，也就是说，行内元素的宽高是内部文字撑出来的。

那有没有一个方法，能够让div元素在同一行，并且其宽高设置也能够生效呢？

这就是CSS的浮动。



### CSS浮动

**什么是CSS浮动？**

CSS的浮动（Float）是元素脱离文档流，按照指定的方向（左或右）发送移动，直到它的外边缘碰到包含框或另一个浮动框的边框位置。

浮动设计之初是为了解决文字环绕图片问题，浮动后的图片一定不会将文字挡住。

文档流是文档中可显示对象在排列时所占用的位置/空间，而脱离文档流就是在页面中不占用位置了。

**设置浮动的属性**：**`float`**

该属性有三种取值：

| 值      | 描述               |
| ------- | ------------------ |
| `left`  | 元素向左浮动       |
| `right` | 元素向右浮动       |
| `none`  | 默认值，元素不浮动 |



**浮动实现效果**

假设现在在一个大的框内，包含三个小的div框：

<img src=".\images\image-20240527005948683.png" alt="image-20240527005948683" style="zoom:80%;" /> 

* 当把框 1 向右浮动时，它脱离文档流并且向右移动，直到它的右边缘碰到包含框的右边缘。

<img src=".\images\image-20240527010007449.png" alt="image-20240527010007449" style="zoom:80%;" /> 

* 当框 1 向左浮动时，它脱离文档流并且向左移动，直到它的左边缘碰到包含框的左边缘。因为它不再处于文档流中，所以它不占据空间，实际上覆盖了框 2，使框 2从视图中消失。

<img src=".\images\image-20240527010140088.png" alt="image-20240527010140088" style="zoom:80%;" /> 

* 如果三个框都向左移动，那么框 1 向左浮动直到碰到包含框，另外两个框向左浮动直到碰到前一个浮动框。

<img src=".\images\image-20240527010234839.png" alt="image-20240527010234839" style="zoom:80%;" /> 

* 如果包含框太窄，无法容纳水平排列的三个浮动元素，那么其他浮动块向下移动，直到有足够的空间。如果浮动元素的高度不同，那么当它们向下移动时可能被其他浮动元素"卡住"。

<img src=".\images\1681260887708.png" alt="1681260887708" style="zoom: 80%;" /> 





**浮动的实现案例**

案例1：将第一个盒子向左浮动

将第一个盒子的选择器.di的CSS样式中使用float属性修饰，将其设置为left，表示向左浮动：

```html
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <style>
        .outerDiv{
            width: 500px;
            height: 300px;
            border: 1px solid gray;
            background-color: beige;
        }
        .innerDiv{
            width: 100px;
            height: 100px;
            border: 1px solid blue;

        }
        .d1{
            background-color: greenyellow;
            float: left;
        }
        .d2{
            background-color: red;
        }
        .d3{
            background-color: green;
        }
    </style>
</head>
<body>
    <div class="outerDiv">
        <div class="innerDiv d1">diva</div>
        <div class="innerDiv d2">divb</div>
        <div class="innerDiv d3">divc</div>
    </div>
</body>
```

显示结果：

<img src=".\images\image-20240527011412250.png" alt="image-20240527011412250" style="zoom:67%;" /> 

此时，第一个div块元素就脱离了文档流，由于其不在文档流中，所以不占用空间，此时第二个div块元素就会被该元素覆盖。

但是，我们之前说过，浮动是为了解决文字环绕图片的问题，所以浮动肯定不会将文字给覆盖，所以第二个div中的divb文字不会被覆盖掉，而是被挤入到第三个div中，我们可以详细看看上图：

![image-20240527011759122](.\images\image-20240527011759122.png) 

可以看到，这里的divb与divc进行了重叠。





案例2：将三个盒子都向右浮动

将三个盒子的类选择器.innerDiv中添加float:right属性，表示向右浮动：

```html
<head>
    <style>
        .outerDiv{
            width: 500px;
            height: 300px;
            border: 1px solid gray;
            background-color: beige;
        }
        .innerDiv{
            width: 100px;
            height: 100px;
            border: 1px solid blue;
            float: right;

        }
        .d1{
            background-color: greenyellow;
        }
        .d2{
            background-color: red;
        }
        .d3{
            background-color: green;
        }
    </style>
</head>
<body>
    <div class="outerDiv">
        <div class="innerDiv d1">diva</div>
        <div class="innerDiv d2">divb</div>
        <div class="innerDiv d3">divc</div>
    </div>
</body>
```

显示效果：

<img src=".\images\image-20240527012248102.png" alt="image-20240527012248102" style="zoom:67%;" /> 

由于三个盒子元素都向右浮动，均脱离文档流，所以它们之间不会互相覆盖。

元素向右浮动，直到元素的外边框碰到包含框或者另一个浮动框的边框为止。





**总结：**

通过浮动，我们既可以让块元素保持原本的特征，同时让它们拥有行内元素的处于同一行特征的效果。



---

## 4、CSS定位

虽然我们通过CSS浮动，能够让div块元素处于同一行，但是，我们却不能控制在这一行中，div块元素所处的位置，比如我想让这些块元素处于中间的位置，或者设置这些块元素之间的间隔等。那么，这个时候就需要使用到CSS定位与盒子模型。

通过CSS定位，能够让元素定位到指定的位置。

**`position`属性指定了元素的定位类型**

这个属性顶级建立元素布局所用的定位机制。任何元素都可以定位，不过绝对或固定元素会生成一个块级框，而不论该元素本身是什么类型。

position属性所有的取值：

| 值         | 描述                                                         |
| ---------- | ------------------------------------------------------------ |
| `static`   | 默认值。表示没有定位，元素出现在正常的流中，将会忽略top、bottom、left以及right属性。 |
| `absolute` | 生成绝对定位的元素，相对于static定位以外的第一个父元素进行定位。<br/>元素的位置通过left、top、right以及bottom属性进行规定。 |
| `fixed`    | 生成绝对定位的元素，该绝对定位是相对于浏览器窗口进行定位。<br>元素的位置通过left、top、right以及bottom属性进行规定 |
| `relative` | 生成相对定位的元素，相对于其原本正常位置进行定位。<br>因此：left:20会向原本元素的位置的LEFT位置添加20像素。 |

在使用position属性后，可以使用`left`、`top`、`right`以及`bottom`属性，去设置该元素与左、上、右、下之间的距离，从而实现定位的功能。

**注意**：left与right只会使用其中一个，同理top与bottom也只会使用其中的一个。因为设置了元素距离左边的长度，就没有必要设置距离右边的长度，否则会产生冲突；同理同时设置上下的距离也会产生冲突。



**实现案例1：static静态定位**

说明：

不设置的时候默认值就是static，静态定位，也是没有定位，元素出现在该出现的位置，块级元素垂直排列，行内元素水平排列。

代码：

```html
<head>
    <meta charset="UTF-8">
    <style>
        .innerDiv{
                width: 100px;
                height: 100px;
        }
        .d1{
            background-color: rgb(166, 247, 46);
            position: static;
        }
        .d2{
            background-color: rgb(79, 230, 124);
        }
        .d3{
            background-color: rgb(26, 165, 208);
        }
    </style>
</head>
<body>
        <div class="innerDiv d1">框1</div>
        <div class="innerDiv d2">框2</div>
        <div class="innerDiv d3">框3</div>
</body>
```

效果：

<img src=".\images\image-20240527020939319.png" alt="image-20240527020939319" style="zoom: 67%;" /> 





**实现案例2：absolute绝对定位**

说明：

绝对定位是相对于上一层边框的边缘是固定的。

定位后元素会让出原本的位置（也就是说使用了absolute的元素，脱离了文档流），其他元素可以占用。

代码：

```html
<head>
    <meta charset="UTF-8" />
    <style>
      .innerDiv {
        width: 100px;
        height: 100px;
      }
      .d1 {
        background-color: rgb(166, 247, 46);
        position: absolute;
        right: 30px;
        top: 30px;
      }
      .d2 {
        background-color: rgb(79, 230, 124);
      }
      .d3 {
        background-color: rgb(26, 165, 208);
      }
    </style>
  </head>
  <body>
    <div class="innerDiv d1">框1</div>
    <div class="innerDiv d2">框2</div>
    <div class="innerDiv d3">框3</div>
  </body>
```

显示结果：

<img src=".\images\qofiwheoiwhef.gif" alt="动画" style="zoom: 67%;" />

可以看到，绝对定位，是相对于上一级别边框的长度是固定的，并且元素会脱离文档流，让出原本的位置，其他元素可以占用。





**实现案例3：relative相对定位**

说明：

相对定位是相对于元素原本的位置来进行定位的。

定位后保留原来的位置（即元素没有脱离文档流），其他元素不会移动到该位置

代码：

```html
<head>
    <meta charset="UTF-8">
    <style>
        .innerDiv{
                width: 100px;
                height: 100px;
        }
        .d1{
            background-color: rgb(166, 247, 46);
            position: relative;
            left: 30px;
            top: 30px;
        }
        .d2{
            background-color: rgb(79, 230, 124);
        }
        .d3{
            background-color: rgb(26, 165, 208);
        }
    </style>
</head>
<body>
        <div class="innerDiv d1">框1</div>
        <div class="innerDiv d2">框2</div>
        <div class="innerDiv d3">框3</div>
</body>
```

效果：

<img src=".\images\1681261993904.png" alt="1681261993904" style="zoom: 67%;" /> 

即相对定位，相对于原本所处的位置进行定位，原本位置不会被其他元素占用。







**实现案例4：fixed固定定位**

说明：

fixed固定定位是相对于浏览器窗口来说，其元素是固定的，不会随着页面的上下移动而移动。

元素定位后会让出原本的位置（脱离文档流），其他元素可以占用。

代码：

```html
<head>
    <meta charset="UTF-8">
    <style>
        .innerDiv{
                width: 100px;
                height: 100px;
        }
        .d1{
            background-color: rgb(166, 247, 46);
            position: fixed;
            right: 30px;
            top: 30px;
        }
        .d2{
            background-color: rgb(79, 230, 124);
        }
        .d3{
            background-color: rgb(26, 165, 208);
        }
    </style>
</head>
<body>
        <div class="innerDiv d1">框1</div>
        <div class="innerDiv d2">框2</div>
        <div class="innerDiv d3">框3</div>
        br*100+tab
</body>
```

效果：

<img src=".\images\fixeddingwei.gif" alt="fixeddingwei" style="zoom: 67%;" /> 







---

## 5、CSS盒子模型

### 盒子模型的说明

所有HTML元素都可以看作是一个盒子，在CSS中，box model这一术语是用来设计和布局时使用。

CSS盒模型本质上是一个盒子，封装周围的HTML元素，它包括：边距（`margin`）、边框（`border`）、填充（`padding`）以及内容（`content`）

<img src=".\images\1681262535006.png" alt="1681262535006" style="zoom:67%;" />

我们所设置的`width`与`height`两个参数，实际上设置的是盒子的内容content的宽度与高度。

设置的`border`，也就是盒子的`边框`的大小。

容量和边框之间是互不侵占的，即设置边框的大小与设置盒子的大小互不影响。

比如，我们给一个盒子设置width为100px，height为100px，border为1px，那么实际上这个盒子的高度应该为102px，宽度也应该为102px。

<img src=".\images\image-20240527111902348.png" alt="image-20240527111902348" style="zoom:80%;" /> 



盒子模型中还涉及到外边距与内边距：

``外边距`，也就是该盒子的外边框与其他盒子之间的距离；

`内边距`，也就是盒子的内边框与content内容之间的距离。

<img src=".\images\1681262585852.png" alt="1681262585852" style="zoom:67%;" />

（上图中的左右内边距属性写错了，应该是padding-left与padding-right）



> 盒子模型各部分内容属性的控制：
>
> 1. **`width、height`控制盒子内容。**
> 2. **`broder`控制盒子边框**
> 3. **`padding`控制盒子内边距**
> 4. **`margin`控制盒子外边距。**





**举例1：给盒子模型添加外边距**

我们来在一个大盒子中，创建三个小盒子，将这三个小盒子设置成向左浮动：

```html
<head>
    <meta charset="UTF-8" />
    <style>
        .outerDiv{
            height: 300px;
            background-color: rgb(246, 243, 219);
        }
      .innerDiv {
        width: 100px;
        height: 100px;
        border: 1px solid black;
        float: left;
      }
      .d1 {
        background-color: rgb(166, 247, 46);
      }
      .d2 {
        background-color: rgb(79, 230, 124);
      }
      .d3 {
        background-color: rgb(26, 165, 208);
      }
    </style>
  </head>
  <body>
    <div class="outerDiv">
      <div class="innerDiv d1">框1</div>
      <div class="innerDiv d2">框2</div>
      <div class="innerDiv d3">框3</div>
    </div>
  </body>
```

显示：

<img src=".\images\image-20240527113951248.png" alt="image-20240527113951248" style="zoom:67%;" /> 

此时，假如我们想让框1与框2之间相隔20px，我们可以去设置外边距，可以分别让框1右外边距设置为10px，框2的左外边距设置为10px：

```html
<head>
    <style>
        .outerDiv{
            height: 300px;
            background-color: rgb(246, 243, 219);
        }
      .innerDiv {
        width: 100px;
        height: 100px;
        border: 1px solid black;
        float: left;
      }
      .d1 {
        background-color: rgb(166, 247, 46);
        margin-right: 10px;
      }
      .d2 {
        background-color: rgb(79, 230, 124);
        margin-left: 10px;
      }
      .d3 {
        background-color: rgb(26, 165, 208);
      }
    </style>
  </head>
  <body>
    <div class="outerDiv">
      <div class="innerDiv d1">框1</div>
      <div class="innerDiv d2">框2</div>
      <div class="innerDiv d3">框3</div>
    </div>
  </body>
```

此时的显示效果：

<img src=".\images\image-20240527114321030.png" alt="image-20240527114321030" style="zoom:67%;" /> 

此时，框1以及右边距到红线过，都是属于框1的盒子模型；

从红线到框2过，都是属于框2的盒子模型。

由于设置了框1的右外边距是10px，所以后面的盒子模型会被框1的右外边距往右撑走；同理，由于设置了框2的左外边距为10px，所以框2的边框会有一个距离框1盒子10px的外边距。



我们可以通过F12，在浏览器中来查看盒子模型的信息。

点击F12，选择当前要进行查看的盒子模型元素，比如我这里选择框1的元素：

![image-20240527115031291](.\images\image-20240527115031291.png) 

可以清晰地看到这个元素的盒子模型的各部分内容的大小。

其中content是100 x 100，也就是我们所设置的weight与height的大小都是100；

padding都是-，也就是没有设置；

border的大小都是1px；（如果border边框的大小与自己设置的不一致，是因为显示的缩放问题，更改一下缩放即可）

有一个右外边距margin-right，大小是10px。





**案例2：给盒子模型添加内边距**

在原本添加了外边距的基础上，给框1添加内边距：

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <style>
        .outerDiv{
            height: 300px;
            background-color: rgb(246, 243, 219);
        }
      .innerDiv {
        width: 100px;
        height: 100px;
        border: 1px solid black;
        float: left;
      }
      .d1 {
        background-color: rgb(166, 247, 46);
        margin-right: 10px;
        padding-left: 10px;
        padding-right: 20px;
        padding-top: 30px;
        padding-bottom: 40px;
      }
      .d2 {
        background-color: rgb(79, 230, 124);
        margin-left: 10px;
      }
      .d3 {
        background-color: rgb(26, 165, 208);
      }
    </style>
  </head>
  <body>
    <div class="outerDiv">
      <div class="innerDiv d1">框1</div>
      <div class="innerDiv d2">框2</div>
      <div class="innerDiv d3">框3</div>
    </div>
  </body>
</html>
```

给框1分别添加了左内边距10px、右内边距20px、上内边距30px以及下内边距40px，我们来看看显示的效果：

<img src=".\images\image-20240527115952699.png" alt="image-20240527115952699" style="zoom:67%;" /> 

可以看到，框1的大小明显变大了，但是距离框2的长度依旧没变。

因为内边距默认不会去侵占content容量的位置，所以在我们设置内边距后，既需要去保证contnt的大小不会改变，还需要去设置内边距的大小，那么就会将原本的盒子模型放大，增大边框到内容的距离。

我们来通过F12看看该盒子的信息：

![image-20240527120311095](.\images\image-20240527120311095.png) 

当鼠标放在盒子模型上的某个属性上时，该属性所对应的页面位置就会使用阴影，我们来看看内边距：

![image-20240527120429531](.\images\image-20240527120429531.png) 

可以看到，图中的阴影部分就是内边距，阴影所围起来的部分就是内容部分，内容部分依旧是100 x 100的，没有被内边距占用。







### padding与margin属性值的写法

我们既可以使用padding-left、padding-rigt、padding-top以及padding-bottom属性，来分别表示盒子模型的四种方向的内边距；

同时也可以直接使用一个padding属性，直接指定四种方向的内边距。

在padding与magin属性中，可以设置一个值、两个值、四个值。设置不同数量的值，实际的效果是什么呢？这里以margin为例。

> * `设置一个值`，表示四个方位都使用相同的长度值；
>
> * `设置两个值`，第一个值表示上下方位，第二个值表示左右方位；
>
> * `设置四个值`，以顺时针方向设值，分别表示上、右、下、左这四个方位。

案例1：设置一个值

```css
.d1 {
        background-color: rgb(166, 247, 46);
        margin: 10px;
      }
```

<img src=".\images\image-20240527131606120.png" alt="image-20240527131606120" style="zoom: 80%;" /> 

使用一个值，那么四个方向都被设置了同样的值。



案例2：设置两个值

```css
.d1 {
        background-color: rgb(166, 247, 46);
        margin: 10px 20px;
      }
```

<img src=".\images\image-20240527131727009.png" alt="image-20240527131727009" style="zoom: 80%;" /> 

在margin或padding属性中设置了两个值，则第一个值是上下的长度，第二个值是左右的长度。





案例3：设置四个值

```css
.d1 {
        background-color: rgb(166, 247, 46);
        margin: 10px 20px 30px 40px;
      }
```

![image-20240527132242665](.\images\image-20240527132242665.png) 

设置四个值，分别表示上、右、下、左四个方向的外边距距离。





> 除了设置具体的长度值以外，**`margin`可以使用`auto`这个关键字实现水平居中效果。**
>
> **注意：auto只适用于magin外边距，并且只能实现水平边距的居中效果，不适用于浮动、内联以及绝对定位和固定定位的元素，即auto不适用于脱离文档流的元素。**

案例：

```html
<head>
    <style>
        .outerDiv{
            height: 300px;
            width: 400px;
            background-color: gray;
        }
      .innerDiv {
        width: 100px;
        height: 100px;
        border: 1px solid black;
        float: left;
      }
      .d1 {
        background-color: rgb(166, 247, 46);
        margin: 0px auto
      }
      .d2 {
        background-color: rgb(79, 230, 124);
      }
      .d3 {
        background-color: rgb(26, 165, 208);
      }
    </style>
  </head>
  <body>
    <div class="outerDiv">
      <div class="innerDiv d1">框1</div>
      <div class="innerDiv d2">框2</div>
      <div class="innerDiv d3">框3</div>
    </div>
  </body>
</html>
```

我们这里给向左浮动的盒子的水平方向外边距设置成了auto，来看看是否可以实现水平居中的效果：

<img src=".\images\image-20240527132752155.png" alt="image-20240527132752155" style="zoom:67%;" /> 

结果是不行，原因在于auto不适用于浮动的元素。

那么我们去给最外层的盒子，没有向左浮动的盒子设置水平方向外边距：

```html
<head>
    <style>
        .outerDiv{
            height: 300px;
            width: 400px;
            background-color: gray;
            margin: 0px auto;
        }
      .innerDiv {
        width: 100px;
        height: 100px;
        border: 1px solid black;
      }
      .d1 {
        background-color: rgb(166, 247, 46);
      }
      .d2 {
        background-color: rgb(79, 230, 124);
      }
      .d3 {
        background-color: rgb(26, 165, 208);
      }
    </style>
  </head>
  <body>
    <div class="outerDiv">
      <div class="innerDiv d1">框1</div>
      <div class="innerDiv d2">框2</div>
      <div class="innerDiv d3">框3</div>
    </div>
  </body>
```

![image-20240527132913667](.\images\image-20240527132913667.png)

可以看到，该盒子实现了水平居中的效果。
