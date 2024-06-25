[TOC]



# 一、JS简介

## 1、JS特点

JS是一种运行于浏览器端上的小脚本语句，可以实现网页如文本内容、数据动态变化和动画特效等。JS有如下特点：

1、脚本语言

* JavaScript是一种解释型的脚本语言。不同于C、C++、Java等语言先编译后执行，JavaScript不会产生编译出来的字节码文件，而是在程序运行过程中对源文件逐行进行解释。

2、基于对象

* JavaScript是一种基于对象的脚本语言，它不仅可以封装对象，也能使用现有的对象。但是面向对象的三大特征：封装、继承、多态中，JavaScript能够实现封装，可以模拟继承，不支持多态，所以它不是一门面向对象的编程语言。

3、弱类型

* JavaScript也有明确的数据类型，但是声明一个变量后它可以接收任何类型的数据，并且会在程序执行过程中根据上下文自动转换类型。

4、事件驱动

* JavaScript是一种采用事件驱动的脚本语言，它不需要经过Web服务器就可以对用户的输入做出响应。

5、跨平台性

* JavaScript脚本语言不依赖于操作系统，仅需要浏览器的支持。因此一个JavaScript脚本在编写后可以带到任意机器上使用，前提是机器上的浏览器支持JavaScript脚本语言。目前JavaScript已被大多数的浏览器所支持。



## 2、JS组成部分

![1681266220955](.\images\1681266220955.png)

ECMA是一种由欧洲计算机制作商协会（ECMA）通过ECMA-262标准化的脚本程序语言，所以JavaScript也叫做ECMAScript。ECMAScript描述了语法、类型、语句、关键字、保留字、运算符和对象。它就是定义了脚本语言的所有属性、方法和对象。

ECMA-262第6版，于2015年6月发布，这一版包含了大概这个规范有史以来最重要的一批增强特性。也叫作ES6.

BOM是Browser Object Model的简写，即浏览器对象模型。

DOM编程就是使用document对象的API完成对网页HTML文档进行动态修改，以实现网页数据和样式动态变化效果的编程。

DOM编程其实就是用window对象的document属性的相关API完成对页面元素的控制的编程。



## 3、JS的引入方式

### 方式一：内部脚本引入

和CSS的内嵌式引入方式类似，JS内部引入是在页面中，通过一对**`script`**标签引入JS代码。

这对script标签放置的位置具有一定的随意性，可以放在head标签中，也可以放在body标签中，但是推荐放在head标签。

代码：

```html
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>小标题</title>
        <style>
            /* 通过选择器确定样式的作用范围 */
            .btn1 {
                display: block;
                width: 150px; 
                height: 40px; 
                background-color: rgb(245, 241, 129); 
                color: rgb(238, 31, 31);
                border: 3px solid rgb(238, 23, 66);
                font-size: 22px;
                font-family: '隶书';
                line-height: 30px;
                border-radius: 5px;
            }
        </style>
        <script>
            function suprise(){
                alert("Hello,我是惊喜")
            }
        </script>
    </head>
    <body>
        <button class="btn1" onclick="suprise()">点我有惊喜</button>
    </body>
</html>
```



### 方式二：外部脚本引入

内部脚本引入的方式只能在当前的页面上使用，其他页面无法使用其他页面上的脚本，代码的复用度不高，造成代码冗余。

此时，我们可以使用外部脚本的方式引入外部的js文件，通过在js文件中声明相关的脚本，就可以在其他文件中通过**`script`**标签引入。

在script标签中，有一个`src`属性，用于指定引入的js文件路径；

`type`属性用于指定引入的js脚本属于什么类型，一般值都是`text/javascript`。

案例：

```html
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>小标题</title>
        <style>
            /* 通过选择器确定样式的作用范围 */
            .btn1 {
                display: block;
                width: 150px; 
                height: 40px; 
                background-color: rgb(245, 241, 129); 
                color: rgb(238, 31, 31);
                border: 3px solid rgb(238, 23, 66);
                font-size: 22px;
                font-family: '隶书';
                line-height: 30px;
                border-radius: 5px;
            }
        </style>
        <script src="js/button.js" type="text/javascript"></script>
    </head>

    <body>
        <button class="btn1" onclick="suprise()">点我有惊喜</button>
    </body>
</html>
```

这里的script标签中，使用了相对路径引入了当前文件路径下的js目录中的button.js文件。





**注意：**

* 在一个html文件中，可以有多个script标签，故可以引入多个JS文件，也可以在html文件中既有内部脚本方式，也可以引入外部脚本文件。

* 一对script标签要么用于定义内部脚本，要么用于移入外部js文件，不能混用。

* **对于`script`标签来说，`必须要写成一个双标签`，不能使用单标签的模式，否则就会失效。**

  例如：

  ```html
  <script src="./js/button.js" type="text/javascript" />
  ```

  这里的引入外部文件的script标签是失效的，必须要写成双标签。

* 在调用函数时，需要像类似于Java中调用方法一样，需要在函数名后面加上一对小括号()，就比如上例中的onlick="surprise()"，给点击事件绑定surprise()函数。







---

# 二、数据类型和运算符

因为JS是弱类型的，不是没有类型，指的是变量在声明时不指定类型，赋值时才确定类型。在JS中，变量的声明全部都使用**`var`**。

比如在Java中，int i = 10 -> 在JS中，则是var i = 10

​	String str = "asdf"-> var str = "asdf"

JS在声明变量时不会指定变量的类型，在赋值时，给变量赋值什么样类型的值，变量就是什么类型的数据。

## 1、数据类型

**JS中常见的数据类型**

1. **数值类型** `number` 数值类型统一为number，不区分整数和浮点数。
2. **字符串类型** `string` 和JAVA中的String相似，JS中不严格区分单双引号，都可以用于标识字符串。
3. **布尔类型** `boolean` 和JAVA中的boolean相似，但是在JS的if语句中，非空字符串会被转换为'真'，非零数字也会被认为是'真'；但是在JAVA中，字符串和数字不能和布尔类型比较。
4. **引用类型** `Object` 引用数据类型对象是Object类型，各种对象和数组在JS中都是Object类型。
5. **function类型** `function` JS中的各种函数属于function数据类型。在JS中，function函数类型的变量声明既可以使用`function fun1(){}`的方式进行声明，也可以使用`var fun1 = function(){}`的方式进行声明。

使用`typeof`关键字来判断数据的类型。

案例：

```js
	   var i = 10;
       console.log(i);
       console.log(typeof i);

       //字符串类型数据赋值给i，i就会自动转换成字符串类型
       i = "asdf";
       console.log(i);
       console.log(typeof i);

       i = i>10;
       console.log(i);
       console.log(typeof i);

       i = new Object();
       console.log(i);
       console.log(typeof i);

       var fun1 = function(){}
       console.log(fun1);
       console.log(typeof fun1)
```

浏览器打印结果：

<img src=".\images\image-20240528080534804.png" alt="image-20240528080534804" style="zoom: 67%;" /> 

这个JS代码，语句结尾可以写分号，也可以不加分号，不写分号换行就表示结束。



6. **如果一个变量仅命名但未赋值时：**

由于JS为弱类型语言，统一使用var声明对象和变量，在赋值时才确定真正的数据类型，如果一个变量只声明但未赋值时，此时的数据以及数据类型都为**`undefined`**。

例如：

```js
var y
console.log(y)
console.log(typeof y)
```

打印结果：

![image-20240528080908916](.\images\image-20240528080908916.png) 



7. **如果一个变量赋值为null：**

在JS中，如果给一个变量赋值为null，则该变量的数据类型是**`Object`**类型。就类似于Java中Person p = null，这个p的值虽然为null，但是p一定是引用数据类型的变量。

例如：

```js
var p = null;
console.log(p);
console.log(typeof p)
```

![image-20240528081426154](.\images\image-20240528081426154.png) 

## 2、变量特征

**JS中的变量具有如下特征：**

1. 弱类型变量，可以统一声明成var；
2. var声明的变量可以再次声明；
3. 变量可以使用不同的数据类型多次赋值；
4. JS的语句可以以分号结尾，也可以不使用分号结尾；
5. 变量标识符严格区分大小写；
6. 标识符的命名规则参考JAVA；
7. 如果使用了一个没有声明的变量，那么会报Uncaught错误。
8. 如果一个变量只声明，未赋值，那么值是undefined。



---

## 3、运算符

1. **算术运算符：`+ - * / %`**

2. **复合算术运算符：`++  --  +=  -=  *=  /=  %=`**
3. **关系运算符：`>  <  >=  <=  ==  ===  !=`**
4. **逻辑运算符：短路或`||`  短路与`&&`**

5. **条件运算符：`条件表达式? 值1 : 值2`**

6. **位运算符：按位或`|` 按位与`&` 异步`^` 左移`<<` 右移`>>` 无符号右移`>>>`**



**算术运算符**和**复合算术运算符**和Java差不多，只不过需要注意的是/、/=、%、%=有一点小小的差别：

* 除/和除等于/=在除0时，结果是`Infinity`，而不是报错。Infinity意思是无穷的。
* 取模%和取模等于%=在模0时，结果是`NaN`，意思为not a number，而不是报错。

例如：

```js
		console.log(10 / 0);  //Infinity
        var i = 10;
        
        console.log(i /= 0);  //Infinity
        

        console.log(10 % 0);  //NaN
        
        var i = 10;
        console.log(i %= 0);  //NaN
```





**关系运算符**需要注意的是 == 和 === 的差别：

* **`==`符号**：如果比较的两端数据类型不一致时，会尝试将两端的是数据转换成number类型，再比较number数据是否相等。

  比如：'123'这种类型的字符串，会转换成数字123。

  true会被转换成1；false会被转换成false.

* **`===`符号**：不仅比较数据是否相等，还要求数据的类型一致。如果数据类型不一致，直接返回false。

案例：

```js
		console.log(1 == 1);      //true
        console.log(1 == "1");    //true
        console.log(1 == true);   //true

        console.log(1 === 1);     //true
        console.log(1 === "1");   //fasle
        console.log(1 === true);  //false
```





---

# 三、流程控制和函数

## 1、分支结构

分支结构分为**if结构**与**switch结构**。

if结构与switch结构均与Java中类似。

> 1. **if结构**

这里的if结构几乎与Java中类似。

注意：在JS中，是弱数据类型，字符串与数字都可以转换成boolean类型：

* `非空字符串会被认作是true，空字符串被认作是false。`
* `非零数字会被认作是true，数字0被认作是false。`

注意与Java区分开，在Java中，布尔类型只能是true或false。

对于对象类型，以及NULL，也都可以转换成布尔类型；对象类型可以转换成true，NULL可以转换成false。

**所以，对于JS来说，表示空或者无这一含义的数据转换成布尔类型时，会转换成false；表示有含义的数据转换成布尔类型时，会被转换成true。**

案例：

```js
if("false"){
	console.log(true);
}else{
    console.log(false);
}

if(""){
    console.log(true);
}else{
    console.log(false);
}

if(1){
    console.log(true);
}else{
    console.log(false);
}

if(0){
    console.log(true);
}else{
    console.log(false);
}
```

对于第一个、第二个if判断来说：虽然字符串是"false"，但是将字符串转换为布尔类型，并不是看它字符串中的字符信息，而是只要有一个字符，就是转换成true；没有字符，就是转换成false。

所以第一个、第二个if判断输出的结果是true、false。

对于第三个、第四个if判断来说：将数字转换成布尔类型，只要数字不是0，转换成布尔类型就是true；数字为0，转换成布尔类型就是false。

所以第三个、第四个if判断输出的结果是true、false。

<img src=".\images\image-20240528093639678.png" alt="image-20240528093639678" style="zoom:67%;" /> 





> 2. **switch结构**

switch结构集合和Java的语法一致。

**注意**：这里的switch结构，进行case判断时，不`仅会去判断数据是否一致，数据类型也要一致`，类似于===判断。即：数值以及数据类型都要一致才会被case捕捉。

案例：

```js
var monthStr=prompt("请输入月份","例如:10 ");
var month= Number.parseInt(monthStr)
switch(month){
    case 3:
    case 4:
    case 5:
        console.log("春季");
        break;
    case 6:
    case 7:
    case 8:
        console.log("夏季");
        break;
    case 9:
    case 10:
    case 11:
        console.log("秋季");
        break;
    case 1:
    case 2:
    case 12:
        console.log("冬季");
        break;
    default :
        console.log("月份有误")
}
```

效果：

<img src=".\images\switchex.gif" alt="switchex" style="zoom:67%;" /> 

在上例中，由于prompt()弹出的窗口中所输入的信息是string类型，由于case后面是数字，我们如果要使用swicth-case结构进行判断，就需要将prompt()函数的返回值转换成number类型。

上例中涉及到了两个函数：`prompt()`与`Number.parseInt()`：

* prompt()函数会在页面中弹出一个窗口，用于给用户输入信息并且获取。该方法的返回值就是用户输入的信息，是string类型数据。

  该函数可以传入两个参数，第一个参数是窗口的提示信息，第二个参数是输入框的提示信息。

* Number.parseInt()类似于Java中的Integer.parseInt()方法，将字符串类型数据转换成数值类型。

因为switch不仅要求数值一致，而且数据类型也要一致。所以，我们需要将prompt()函数的返回值转换成number类型。





---

## 2、循环结构

JS中有三种循环结构，分别是while循环、for循环以及foreach循环。while循环和for循环与Java中一致，foreach循环与Java有所不同。

> 1. **while循环**

几乎和Java中一模一样。

案例：打印9*9乘法表

```js
		var i = 1;
        while(i <= 9){
            var j = 1;
            while(j <= i){
                document.write(j + "*" + i + "=" + i * j + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                j++;
            }
            document.write("<hr />");
            i++;
        }
```

上述的`document.write("")`方法是向浏览器窗口上打印信息的函数，该函数能够将标签一起打印，并且打印出来的标签具有相应的功能。

实际上，**该函数相当于在body标签中，使用html语言写代码**。所以，该函数打印标签时，标签就能够起作用。

当我们需要打印一些特殊含义的字符时，就需要使用到转义字符。

打印结果：

<img src=".\images\image-20240528100722036.png" alt="image-20240528100722036" style="zoom: 50%;" /> 



> 2. **for循环**

案例：打印9*9乘法表

```js
for(var i = 1; i <= 9; i++){
	for(var j = 1; j <= i; j++){
		document.write(j + "*" + i + "=" + i * j + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	}
	document.write("<hr />")
}
```

结果：

<img src=".\images\image-20240528102629496.png" alt="image-20240528102629496" style="zoom:50%;" /> 





> 3. **foreach循环**

Java中的foreach循环：

```java
int[] ints = new int[]{1, 2, 4, 5, 7};
for (int i : ints) {
    System.out.println(i);
}

List<Emp> emps = new ArrayList<>();
emps.forEach(e -> {
    System.out.println(e);
});
```

一般我们所说的foreach循环是第一种，第二种foreach循环是使用了lambda表达式的foreach()方法。

JS中foreach循环与Java中的不同之处：

* 在Java的foreach循环中，定义的临时变量是元素的值；但在JS中，括号中定义的临时变量则是**`元素的索引`**。
* ()中不是使用:分号进行分隔，而是使用**`in`**关键字。

案例：

```js
var cities =["北京","上海","深圳","武汉","西安","成都"]
document.write("<ul>")
for(var index in  cities){
    document.write("<li>"+cities[index]+"</li>")
}
document.write("</ul>")
```

效果：

<img src=".\images\1681287540562.png" alt="1681287540562" style="zoom: 80%;" /> 





---

## 3、函数声明

JS中函数与Java中的方法类似，但是JS中函数的声明语法与Java中有较大的区别。

**JS函数声明有两种语法：**

**方式一：`function 函数名(参数列表){函数体}`**

**方式二：`var 函数名 = function(参数列表){函数体}`**



**函数声明的说明**：

1. 使用`function`关键字声明函数。
2. 函数`没有权限修饰符`；
3. `不用声明函数的返回值类型`，如果需要返回数据在函数体中直接return即可，同时也`无需void关键字`。
4. 参数列表中，`不用声明参数的类型`。因为JS是弱类型，变量是在赋值时才确定数据类型，所以只需要放参数名在参数列表中即可。
5. JS的函数`没有异常列表`。
6. 调用函数时，`实参的个数与形参的数量可以不一致`。比如我们在函数中声明了两个参数，在调用时传入了5个参数，那实际上起作用的还是前两个参数；如果调用时传入1个参数，那么起作用的就是一个参数。

由于在函数中，形参的数量和实参的数量可以不一致，所以我们无法确保在调用函数时传入实参的数量。函数中，有一个**`arguments`**变量，该变量是一个数组类型，变量中存储了实参的值。

案例：

```js
//函数声明方式一
function sum(a, b){
    console.log(arguments);
    return a + b;
}

var result1 = sum(1,2,3,4,5);
console.log(result1); //3

var result2 = sum(10);
console.log(result2); //NaN

//函数声明方式二：
var add = function(a, b){
    return a + b;
}
```

执行结果：

<img src=".\images\image-20240528111059195.png" alt="image-20240528111059195" style="zoom: 67%;" /> 

可以看到，第一次调用函数的arguments数组中有5个实参；第二次调用函数的arguments数组中只有1个实参。

第一次调用，只取前两个实参，结果是3；

第二次调用，由于只有一个实参，所以参数a = 10，参数b是undefined，数字+undefined=NaN，结果就是NaN，表示Not a number。





---

# 四、JS的对象和JSON

## 1、声明对象的语法

声明对象的语法有两种，一种是通过new Object()直接创建对象；另一种是通过{}形式创建对象。

> 1. **通过`new Object()`直接创建对象**
>
> 使用这种方式直接创建对象，然后通过`.属性名`或`.函数名`进行给其中的属性或函数实例化。该方式有些类似于Java中的使用空参构造器创建对象，然后使用set()方法进行赋值。

在Java中，先要去创建一个类，通过这个类的构造器去实例化对象。

在JS中，没有类，也没有什么构造器，直接去实例化对象，而对于其中的属性或函数，在实例化之后直接通过.属性名或.函数名的方式进行声明赋值：

如果对象中原本有该属性或函数，则表示赋值；如果对象中原本没有该属性或函数，则表示声明并赋值。

案例：

```js
var person = new Object();
//声明并赋值
person.name = "张三";
person.age = 24;
person.eat = function(food){
    console.log(this.age + "岁的" + this.name + "正在吃" + food);
}
//访问属性
console.log(person.age);
console.log(person.name);

//调用方法
person.eat("火锅");
```

显示：

![image-20240528112647771](.\images\image-20240528112647771.png) 





> 2. **通过{}形式创建对象**

**该方式的语法：**

**`var person = {属性名: 属性值, 属性名: 属性值, 函数名: 函数}`**

注意，使用这种方式创建对象，属性与属性之间，属性与函数之间，使用逗号分隔，而不是分号。

案例：

```js
var person = {
    name: "cheng",
    age: 24,
    eat: function(food){
        console.log(this.age + "岁的" + this.name + "正在吃" + food);
    },
    run: function(){
        console.log("run..........");
    }
}

person.eat("火锅");
person.run();
```

打印信息：

![image-20240528121602628](.\images\image-20240528121602628.png) 





---

## 2、JSON格式

JSON（JavaScript Object Notation, JS对象简谱）是一种轻量级数据交换格式。它基于ECMAScript，采用完全独立于编程语言的文本格式来存储和表示数据。简洁和清晰的层次结构使得JSON成为理想的数据交换语言。易于人阅读和编写，同时也易于机器解析和生成，并有效地提升网络传输效率。

`简单来说，JSON就是一种字符串格式，这种格式无论是在前端还是在后端，都可以很容易地转换成对象，所以常用于前后端数据传递。`

前端JS中是使用对象类型存储数据的，后端Java也是使用对象存储数据。但是JS和Java中的对象不是通用的，那么当我们前端数据给后端，后端传输数据给前端，就需要使用一个通用的类型来进行传输，这个通用的类型就是JSON格式的字符串。

前后端传递数据：

![1681292306466](.\images\1681292306466.png) 

**JSON格式的语法：**

**`{"属性名":"属性值", "属性名":"属性值", "属性名":"属性值"}`**

JSON格式中，属性名必须要使用""双引号包上；

属性如果是字符串类型，则要使用""双引号包上，如果是数值类型则可以不使用""双引号；

如果属性是对象类型，则该属性的格式与外部对象格式一致：`"对象属性名":{"属性名":属性值, "属性名":属性值...}`

如果属性是数组类型，则格式为`属性名:[属性值,属性值,属性值...]`。



**案例：**

```js
var employee = new Object();
employee.name = "tom";
employee.age = 24;
employee.email = "123@qq.com";
//其中有一个dept对象类型的属性
employee.dept = {
    deptName: "技术部",
    deptId: 1,
};

//有一个字符串数组类型的属性
employee.hobbies = ["打篮球", "爬山", "听音乐", "打游戏"];

//有一个对象类型的数组属性
employee.friends = [{"fname":"小明"},{"fname":"晓东"},{"fname":"小华"},{"fname":"小刚"}];

//work()方法
employee.work = function(){
    console.log("work...");
}

//将对象转换成JSON字符串
var employeeJSON = JSON.stringify(employee);

//打印JSON字符串
console.log(employeeJSON);

console.log(typeof employeeJSON);

//将JSON字符串转换成对象
var emp2 = JSON.parse(employeeJSON);
console.log(emp2);
```

打印结果：

<img src=".\images\image-20240528145143584.png" alt="image-20240528145143584" style="zoom: 67%;" /> 

我们来看看打印出来的JSON字符串：

```json
{"name":"tom","age":24,"email":"123@qq.com","dept":{"deptName":"技术部","deptId":1},"hobbies":["打篮球","爬山","听音乐","打游戏"],"friends":[{"fname":"小明"},{"fname":"晓东"},{"fname":"小华"},{"fname":"小刚"}]}
```

JSON的格式和{}形式创建对象的格式一样，只不过JSON格式是字符串类型的数据。

> **说明：**
>
> * JSON字符串一般是用于传递数据的，所以对象中的函数进行数据的传递是没有意义的，所以对象类型数据转换成JSON格式字符串时，函数就会被去除。
> * 通过**`JSON.parse()`**方法可以将一个JSON字符串转换成一个JS对象。
> * 通过**`JSON.stringify()`**方法可以将一个JS对象转换成一个JSON格式的字符串。
> * 如果对象是Map集合类型，转换成JSON格式与对象转换成JSON格式是一样的，都是{"":""}的格式
> * 如果对象是List或Array类型，并且其元素是普通数据类型的话，转换成JSON格式是["", ""]的格式。如果该集合元素是对象类型，则转换成字符串则是[{}, {}]的格式。





---

## 3、常见对象

### 数组

在JS中，数组属于Object类型，与Java中的数组不同，**`JS中的数组长度可以变化`**，更类似于Java中的集合。并且数组不区分类型，既可以往数组中添加字符串类型，也可以往同一个数组中添加数值。

总之，在JS中的数组更类似于Java中的`List<Object>`。



**创建数组的四种方式**

* `new Array()`：创建一个空数组
* `new Array(5)`：创建一个指定长度的数组
* `new Array(ele1, ele2, ele3, ..., elen)`：创建数组时指定元素值
* `[ele1, ele2, ele3, ..., elen]`：相当于第三种方式的简写。

案例：

```js
//1、数组四种创建方式
var arr1 = new Array();
console.log(arr1);
console.log(arr1.length)
//给数组中添加数据
arr1[0] = 1;
arr1[1] = "张三";
console.log(arr1);
console.log(arr1.length)

//第二种方式
var arr2 = new Array(5);
console.log(arr2);
console.log(arr2.length);

//第三种方式
var arr3 = new Array(1, 2, 3, 4, 5, 6);
console.log(arr3);

//第四种方式
var arr4 = [6, 7, 8, 9, 10];    
console.log(arr4);
```

打印结果：

<img src=".\images\image-20240528161552897.png" alt="image-20240528161552897" style="zoom:50%;" /> 





**数组的常见API**（点击方法可以跳转到演示）

* [concat()](https://www.runoob.com/jsref/jsref-concat-array.html)：连接两个或更多的数组，并返回结果。
* [copyWithin()](https://www.runoob.com/jsref/jsref-copywithin.html)：从数组的指定位置拷贝元素到数组的另一个指定位置。

* [entries()](https://www.runoob.com/jsref/jsref-entries.html)：返回数组的可迭代对象。
* [every()](https://www.runoob.com/jsref/jsref-every.html)：检测数值元素的每个元素是否都符合条件。
* [fill()](https://www.runoob.com/jsref/jsref-fill.html)：使用一个固定值来填充数组。
* [filter()](https://www.runoob.com/jsref/jsref-filter.html)：检测数值元素，并返回符合条件的所有元素的数组。
* [find()](https://www.runoob.com/jsref/jsref-find.html)：返回符合传入测试（函数）条件的数组元素。
* [findIndex()](https://www.runoob.com/jsref/jsref-findindex.html)：返回符合传入测试（函数）条件的数组元素索引。
* [forEach()](https://www.runoob.com/jsref/jsref-foreach.html)：数组每个元素都执行一次回调函数。
* [from()](https://www.runoob.com/jsref/jsref-from.html)：通过给定的对象中创建一个数组。
* [includes()](https://www.runoob.com/jsref/jsref-includes.html)：判断一个数组是否包含一个指定的值。
* [indexOf()](https://www.runoob.com/jsref/jsref-indexof-array.html)：搜索数组中的元素，并返回它所在的位置。
* [isArray()](https://www.runoob.com/jsref/jsref-isarray.html)：判断对象是否为数组。
* [join()](https://www.runoob.com/jsref/jsref-join.html)：把数组的所有元素放入一个字符串。
* [keys()](https://www.runoob.com/jsref/jsref-keys.html)：返回数组的可迭代对象，包含原始数组的键（key）。
* [lastIndexOf()](https://www.runoob.com/jsref/jsref-lastindexof-array.html)：搜索数组中的元素，并返回它最后出现的位置。
* [map()](https://www.runoob.com/jsref/jsref-map.html)：通过指定函数处理数组的每个元素，并返回处理后的数组。
* [pop()](https://www.runoob.com/jsref/jsref-pop.html)：珊瑚粗数组的最后一个元素并且返回删除的元素。
* [push()](https://www.runoob.com/jsref/jsref-push.html)：向数组的末尾添加一个或更多元素，并且返回新的长度。
* [reduce()](https://www.runoob.com/jsref/jsref-reduce.html)：将数组元素计算为一个值（从左到右）。
* [reduceRight()](https://www.runoob.com/jsref/jsref-reduceright.html)：将数组元素计算为一个值（从右到左）。
* [reverse()](https://www.runoob.com/jsref/jsref-reverse.html)：反转数组的元素顺序。
* [shift()](https://www.runoob.com/jsref/jsref-shift.html)：删除并返回数组的第一个元素。
* [slice()](https://www.runoob.com/jsref/jsref-slice-array.html)：选取数组的一部分，并返回一个新数组。
* [some()](https://www.runoob.com/jsref/jsref-some.html)：检测数组元素中是否有元素符合指定条件。
* [sort()](https://www.runoob.com/jsref/jsref-sort.html)：对数组的元素进行排序。
* [splice()](https://www.runoob.com/jsref/jsref-splice.html)：从数组中添加或删除元素。
* [toString()](https://www.runoob.com/jsref/jsref-tostring-array.html)：把数组转换成字符串，并返回结果。
* [unshift()](https://www.runoob.com/jsref/jsref-unshift.html)：向数组的开头添加一个或多个元素，并返回新的长度。
* [valueOf()](https://www.runoob.com/jsref/jsref-valueof-array.html)：返回数组对的原始值。
* [Array.of()](https://www.runoob.com/jsref/jsref-of-array.html)：将一组值转换为数组。
* [Array.at()](https://www.runoob.com/jsref/jsref-at-array.html)：用于接收一个整数值并返回该索引对应的元素，允许正数和负数。负整数从数组中的最后一个元素开始倒数。
* [Array.flat()](https://www.runoob.com/jsref/jsref-flat-array.html)：创建一个新数组，这个新数组由原数组中的每个元素都调用一次提供的函数后的返回值组成。
* [Array.flatMap()](https://www.runoob.com/jsref/jsref-flatmap-array.html)：使用映射函数映射每个元素，然后将结果压缩成一个新数组。





---

### Boolean

Boolean对象的方法比较简单。

* [toString()](https://www.runoob.com/jsref/jsref-tostring-boolean.html)：将布尔值转换为字符串，并返回结果。
* [valueOf()](https://www.runoob.com/jsref/jsref-valueof-boolean.html)：返回Boolean对象的原始值。





---

### Date

和Java中的Date类型比较类似。

| 方法                                                         | 描述                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [getDate()](https://www.runoob.com/jsref/jsref-getdate.html) | 从 Date 对象返回一个月中的某一天 (1 ~ 31)。                  |
| [getDay()](https://www.runoob.com/jsref/jsref-getday.html)   | 从 Date 对象返回一周中的某一天 (0 ~ 6)。                     |
| [getFullYear()](https://www.runoob.com/jsref/jsref-getfullyear.html) | 从 Date 对象以四位数字返回年份。                             |
| [getHours()](https://www.runoob.com/jsref/jsref-gethours.html) | 返回 Date 对象的小时 (0 ~ 23)。                              |
| [getMilliseconds()](https://www.runoob.com/jsref/jsref-getmilliseconds.html) | 返回 Date 对象的毫秒(0 ~ 999)。                              |
| [getMinutes()](https://www.runoob.com/jsref/jsref-getminutes.html) | 返回 Date 对象的分钟 (0 ~ 59)。                              |
| [getMonth()](https://www.runoob.com/jsref/jsref-getmonth.html) | 从 Date 对象返回月份 (0 ~ 11)。                              |
| [getSeconds()](https://www.runoob.com/jsref/jsref-getseconds.html) | 返回 Date 对象的秒数 (0 ~ 59)。                              |
| [getTime()](https://www.runoob.com/jsref/jsref-gettime.html) | 返回 1970 年 1 月 1 日至今的毫秒数。                         |
| [getTimezoneOffset()](https://www.runoob.com/jsref/jsref-gettimezoneoffset.html) | 返回本地时间与格林威治标准时间 (GMT) 的分钟差。              |
| [getUTCDate()](https://www.runoob.com/jsref/jsref-getutcdate.html) | 根据世界时从 Date 对象返回月中的一天 (1 ~ 31)。              |
| [getUTCDay()](https://www.runoob.com/jsref/jsref-getutcday.html) | 根据世界时从 Date 对象返回周中的一天 (0 ~ 6)。               |
| [getUTCFullYear()](https://www.runoob.com/jsref/jsref-getutcfullyear.html) | 根据世界时从 Date 对象返回四位数的年份。                     |
| [getUTCHours()](https://www.runoob.com/jsref/jsref-getutchours.html) | 根据世界时返回 Date 对象的小时 (0 ~ 23)。                    |
| [getUTCMilliseconds()](https://www.runoob.com/jsref/jsref-getutcmilliseconds.html) | 根据世界时返回 Date 对象的毫秒(0 ~ 999)。                    |
| [getUTCMinutes()](https://www.runoob.com/jsref/jsref-getutcminutes.html) | 根据世界时返回 Date 对象的分钟 (0 ~ 59)。                    |
| [getUTCMonth()](https://www.runoob.com/jsref/jsref-getutcmonth.html) | 根据世界时从 Date 对象返回月份 (0 ~ 11)。                    |
| [getUTCSeconds()](https://www.runoob.com/jsref/jsref-getutcseconds.html) | 根据世界时返回 Date 对象的秒钟 (0 ~ 59)。                    |
| getYear()                                                    | 已废弃。 请使用 getFullYear() 方法代替。                     |
| [parse()](https://www.runoob.com/jsref/jsref-parse.html)     | 返回1970年1月1日午夜到指定日期（字符串）的毫秒数。           |
| [setDate()](https://www.runoob.com/jsref/jsref-setdate.html) | 设置 Date 对象中月的某一天 (1 ~ 31)。                        |
| [setFullYear()](https://www.runoob.com/jsref/jsref-setfullyear.html) | 设置 Date 对象中的年份（四位数字）。                         |
| [setHours()](https://www.runoob.com/jsref/jsref-sethours.html) | 设置 Date 对象中的小时 (0 ~ 23)。                            |
| [setMilliseconds()](https://www.runoob.com/jsref/jsref-setmilliseconds.html) | 设置 Date 对象中的毫秒 (0 ~ 999)。                           |
| [setMinutes()](https://www.runoob.com/jsref/jsref-setminutes.html) | 设置 Date 对象中的分钟 (0 ~ 59)。                            |
| [setMonth()](https://www.runoob.com/jsref/jsref-setmonth.html) | 设置 Date 对象中月份 (0 ~ 11)。                              |
| [setSeconds()](https://www.runoob.com/jsref/jsref-setseconds.html) | 设置 Date 对象中的秒钟 (0 ~ 59)。                            |
| [setTime()](https://www.runoob.com/jsref/jsref-settime.html) | setTime() 方法以毫秒设置 Date 对象。                         |
| [setUTCDate()](https://www.runoob.com/jsref/jsref-setutcdate.html) | 根据世界时设置 Date 对象中月份的一天 (1 ~ 31)。              |
| [setUTCFullYear()](https://www.runoob.com/jsref/jsref-setutcfullyear.html) | 根据世界时设置 Date 对象中的年份（四位数字）。               |
| [setUTCHours()](https://www.runoob.com/jsref/jsref-setutchours.html) | 根据世界时设置 Date 对象中的小时 (0 ~ 23)。                  |
| [setUTCMilliseconds()](https://www.runoob.com/jsref/jsref-setutcmilliseconds.html) | 根据世界时设置 Date 对象中的毫秒 (0 ~ 999)。                 |
| [setUTCMinutes()](https://www.runoob.com/jsref/jsref-setutcminutes.html) | 根据世界时设置 Date 对象中的分钟 (0 ~ 59)。                  |
| [setUTCMonth()](https://www.runoob.com/jsref/jsref-setutcmonth.html) | 根据世界时设置 Date 对象中的月份 (0 ~ 11)。                  |
| [setUTCSeconds()](https://www.runoob.com/jsref/jsref-setutcseconds.html) | setUTCSeconds() 方法用于根据世界时 (UTC) 设置指定时间的秒字段。 |
| setYear()                                                    | 已废弃。请使用 setFullYear() 方法代替。                      |
| [toDateString()](https://www.runoob.com/jsref/jsref-todatestring.html) | 把 Date 对象的日期部分转换为字符串。                         |
| toGMTString()                                                | 已废弃。请使用 toUTCString() 方法代替。                      |
| [toISOString()](https://www.runoob.com/jsref/jsref-toisostring.html) | 使用 ISO 标准返回字符串的日期格式。                          |
| [toJSON()](https://www.runoob.com/jsref/jsref-tojson.html)   | 以 JSON 数据格式返回日期字符串。                             |
| [toLocaleDateString()](https://www.runoob.com/jsref/jsref-tolocaledatestring.html) | 根据本地时间格式，把 Date 对象的日期部分转换为字符串。       |
| [toLocaleTimeString()](https://www.runoob.com/jsref/jsref-tolocaletimestring.html) | 根据本地时间格式，把 Date 对象的时间部分转换为字符串。       |
| [toLocaleString()](https://www.runoob.com/jsref/jsref-tolocalestring.html) | 根据本地时间格式，把 Date 对象转换为字符串。                 |
| [toString()](https://www.runoob.com/jsref/jsref-tostring-date.html) | 把 Date 对象转换为字符串。                                   |
| [toTimeString()](https://www.runoob.com/jsref/jsref-totimestring.html) | 把 Date 对象的时间部分转换为字符串。                         |
| [toUTCString()](https://www.runoob.com/jsref/jsref-toutcstring.html) | 根据世界时，把 Date 对象转换为字符串。实例：`var today = new Date(); var UTCstring = today.toUTCString();` |
| [UTC()](https://www.runoob.com/jsref/jsref-utc.html)         | 根据世界时返回 1970 年 1 月 1 日 到指定日期的毫秒数。        |
| [valueOf()](https://www.runoob.com/jsref/jsref-valueof-date.html) | 返回 Date 对象的原始值。                                     |







---

### Math

| 方法                                                         | 描述                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [abs(x)](https://www.runoob.com/jsref/jsref-abs.html)        | 返回 x 的绝对值。                                            |
| [acos(x)](https://www.runoob.com/jsref/jsref-acos.html)      | 返回 x 的反余弦值。                                          |
| [asin(x)](https://www.runoob.com/jsref/jsref-asin.html)      | 返回 x 的反正弦值。                                          |
| [atan(x)](https://www.runoob.com/jsref/jsref-atan.html)      | 以介于 -PI/2 与 PI/2 弧度之间的数值来返回 x 的反正切值。     |
| [atan2(y,x)](https://www.runoob.com/jsref/jsref-atan2.html)  | 返回从 x 轴到点 (x,y) 的角度（介于 -PI/2 与 PI/2 弧度之间）。 |
| [ceil(x)](https://www.runoob.com/jsref/jsref-ceil.html)      | 对数进行上舍入。                                             |
| [cos(x)](https://www.runoob.com/jsref/jsref-cos.html)        | 返回数的余弦。                                               |
| [exp(x)](https://www.runoob.com/jsref/jsref-exp.html)        | 返回 Ex 的指数。                                             |
| [floor(x)](https://www.runoob.com/jsref/jsref-floor.html)    | 对 x 进行下舍入。                                            |
| [log(x)](https://www.runoob.com/jsref/jsref-log.html)        | 返回数的自然对数（底为e）。                                  |
| [max(x,y,z,...,n)](https://www.runoob.com/jsref/jsref-max.html) | 返回 x,y,z,...,n 中的最高值。                                |
| [min(x,y,z,...,n)](https://www.runoob.com/jsref/jsref-min.html) | 返回 x,y,z,...,n中的最低值。                                 |
| [pow(x,y)](https://www.runoob.com/jsref/jsref-pow.html)      | 返回 x 的 y 次幂。                                           |
| [random()](https://www.runoob.com/jsref/jsref-random.html)   | 返回 0 ~ 1 之间的随机数。                                    |
| [round(x)](https://www.runoob.com/jsref/jsref-round.html)    | 四舍五入。                                                   |
| [sin(x)](https://www.runoob.com/jsref/jsref-sin.html)        | 返回数的正弦。                                               |
| [sqrt(x)](https://www.runoob.com/jsref/jsref-sqrt.html)      | 返回数的平方根。                                             |
| [tan(x)](https://www.runoob.com/jsref/jsref-tan.html)        | 返回角的正切。                                               |
| [tanh(x)](https://www.runoob.com/jsref/jsref-tanh.html)      | 返回一个数的双曲正切函数值。                                 |
| [trunc(x)](https://www.runoob.com/jsref/jsref-trunc.html)    | 将数字的小数部分去掉，只保留整数部分。                       |





---

### Number

| 方法                                                         | 描述                                                 |
| :----------------------------------------------------------- | :--------------------------------------------------- |
| [isFinite](https://www.runoob.com/jsref/jsref-isfinite-number.html) | 检测指定参数是否为无穷大。                           |
| [isInteger](https://www.runoob.com/jsref/jsref-isinteger-number.html) | 检测指定参数是否为整数。                             |
| [isNaN](https://www.runoob.com/jsref/jsref-isnan-number.html) | 检测指定参数是否为 NaN。                             |
| [isSafeInteger](https://www.runoob.com/jsref/jsref-issafeInteger-number.html) | 检测指定参数是否为安全整数。                         |
| [toExponential(x)](https://www.runoob.com/jsref/jsref-toexponential.html) | 把对象的值转换为指数计数法。                         |
| [toFixed(x)](https://www.runoob.com/jsref/jsref-tofixed.html) | 把数字转换为字符串，结果的小数点后有指定位数的数字。 |
| [toLocaleString(locales, options)](https://www.runoob.com/jsref/jsref-tolocalestring-number.html) | 返回数字在特定语言环境下的表示字符串。               |
| [toPrecision(x)](https://www.runoob.com/jsref/jsref-toprecision.html) | 把数字格式化为指定的长度。                           |
| [toString()](https://www.runoob.com/jsref/jsref-tostring-number.html) | 把数字转换为字符串，使用指定的基数。                 |
| [valueOf()](https://www.runoob.com/jsref/jsref-valueof-number.html) | 返回一个 Number 对象的基本数字值。                   |







---

### String

| 方法                                                         | 描述                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [charAt()](https://www.runoob.com/jsref/jsref-charat.html)   | 返回在指定位置的字符。                                       |
| [charCodeAt()](https://www.runoob.com/jsref/jsref-charcodeat.html) | 返回在指定的位置的字符的 Unicode 编码。                      |
| [concat()](https://www.runoob.com/jsref/jsref-concat-string.html) | 连接两个或更多字符串，并返回新的字符串。                     |
| [endsWith()](https://www.runoob.com/jsref/jsref-endswith.html) | 判断当前字符串是否是以指定的子字符串结尾的（区分大小写）。   |
| [fromCharCode()](https://www.runoob.com/jsref/jsref-fromcharcode.html) | 将 Unicode 编码转为字符。                                    |
| [indexOf()](https://www.runoob.com/jsref/jsref-indexof.html) | 返回某个指定的字符串值在字符串中首次出现的位置。             |
| [includes()](https://www.runoob.com/jsref/jsref-string-includes.html) | 查找字符串中是否包含指定的子字符串。                         |
| [lastIndexOf()](https://www.runoob.com/jsref/jsref-lastindexof.html) | 从后向前搜索字符串，并从起始位置（0）开始计算返回字符串最后出现的位置。 |
| [match()](https://www.runoob.com/jsref/jsref-match.html)     | 查找找到一个或多个正则表达式的匹配。                         |
| [repeat()](https://www.runoob.com/jsref/jsref-repeat.html)   | 复制字符串指定次数，并将它们连接在一起返回。                 |
| [replace()](https://www.runoob.com/jsref/jsref-replace.html) | 在字符串中查找匹配的子串，并替换与正则表达式匹配的子串。     |
| [replaceAll()](https://www.runoob.com/jsref/jsref-replaceall.html) | 在字符串中查找匹配的子串，并替换与正则表达式匹配的所有子串。 |
| [search()](https://www.runoob.com/jsref/jsref-search.html)   | 查找与正则表达式相匹配的值。                                 |
| [slice()](https://www.runoob.com/jsref/jsref-slice-string.html) | 提取字符串的片断，并在新的字符串中返回被提取的部分。         |
| [split()](https://www.runoob.com/jsref/jsref-split.html)     | 把字符串分割为字符串数组。                                   |
| [startsWith()](https://www.runoob.com/jsref/jsref-startswith.html) | 查看字符串是否以指定的子字符串开头。                         |
| [substr()](https://www.runoob.com/jsref/jsref-substr.html)   | 从起始索引号提取字符串中指定数目的字符。                     |
| [substring()](https://www.runoob.com/jsref/jsref-substring.html) | 提取字符串中两个指定的索引号之间的字符。                     |
| [toLowerCase()](https://www.runoob.com/jsref/jsref-tolowercase.html) | 把字符串转换为小写。                                         |
| [toUpperCase()](https://www.runoob.com/jsref/jsref-touppercase.html) | 把字符串转换为大写。                                         |
| [trim()](https://www.runoob.com/jsref/jsref-trim.html)       | 去除字符串两边的空白。                                       |
| [toLocaleLowerCase()](https://www.runoob.com/jsref/jsref-tolocalelowercase.html) | 根据本地主机的语言环境把字符串转换为小写。                   |
| [toLocaleUpperCase()](https://www.runoob.com/jsref/jsref-tolocaleuppercase.html) | 根据本地主机的语言环境把字符串转换为大写。                   |
| [valueOf()](https://www.runoob.com/jsref/jsref-valueof-string.html) | 返回某个字符串对象的原始值。                                 |
| [toString()](https://www.runoob.com/jsref/jsref-tostring.html) | 返回一个字符串。                                             |











---

# 五、事件的绑定（:star:）

## 1、什么是事件？

事件可以是浏览器行为，也可以是用户行为。当这些行为发生时，可以自动触发对应的JS函数的运行，我们称之为**事件发生**。

JS的**事件驱动**指的就是行为触发代码运行的这种特点。

## 2、常见事件

### 鼠标事件

| 属性                                                         | 描述                                   |
| :----------------------------------------------------------- | :------------------------------------- |
| [onclick](https://www.runoob.com/jsref/event-onclick.html)   | 当用户点击某个对象时调用的事件句柄。   |
| [oncontextmenu](https://www.runoob.com/jsref/event-oncontextmenu.html) | 在用户点击鼠标右键打开上下文菜单时触发 |
| [ondblclick](https://www.runoob.com/jsref/event-ondblclick.html) | 当用户双击某个对象时调用的事件句柄。   |
| [onmousedown](https://www.runoob.com/jsref/event-onmousedown.html) | 鼠标按钮被按下。                       |
| [onmouseenter](https://www.runoob.com/jsref/event-onmouseenter.html) | 当鼠标指针移动到元素上时触发。         |
| [onmouseleave](https://www.runoob.com/jsref/event-onmouseleave.html) | 当鼠标指针移出元素时触发               |
| [onmousemove](https://www.runoob.com/jsref/event-onmousemove.html) | 鼠标被移动。                           |
| [onmouseover](https://www.runoob.com/jsref/event-onmouseover.html) | 鼠标移到某元素之上。                   |
| [onmouseout](https://www.runoob.com/jsref/event-onmouseout.html) | 鼠标从某元素移开。                     |
| [onmouseup](https://www.runoob.com/jsref/event-onmouseup.html) | 鼠标按键被松开。                       |



---

### 键盘事件

| 属性                                                         | 描述                     |
| :----------------------------------------------------------- | :----------------------- |
| [onkeydown](https://www.runoob.com/jsref/event-onkeydown.html) | 键盘的按键被按下。       |
| [onkeypress](https://www.runoob.com/jsref/event-onkeypress.html) | 键盘的按键被按下并松开。 |
| [onkeyup](https://www.runoob.com/jsref/event-onkeyup.html)   | 键盘的按键被松开。       |



---

### 表单事件

| 属性                                                         | 描述                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [onblur](https://www.runoob.com/jsref/event-onblur.html)     | 元素失去焦点时触发                                           |
| [onchange](https://www.runoob.com/jsref/event-onchange.html) | 该事件在表单元素的内容改变时触发( <input>, <keygen>, <select>, 和 <textarea>)，在该事件绑定的函数中可以传入一个值，表示改变的内容 |
| [onfocus](https://www.runoob.com/jsref/event-onfocus.html)   | 元素获取焦点时触发                                           |
| [onfocusin](https://www.runoob.com/jsref/event-onfocusin.html) | 元素即将获取焦点时触发                                       |
| [onfocusout](https://www.runoob.com/jsref/event-onfocusout.html) | 元素即将失去焦点时触发                                       |
| [oninput](https://www.runoob.com/jsref/event-oninput.html)   | 元素获取用户输入时触发                                       |
| [onreset](https://www.runoob.com/jsref/event-onreset.html)   | 表单重置时触发                                               |
| [onsearch](https://www.runoob.com/jsref/event-onsearch.html) | 用户向搜索域输入文本时触发 ( <input="search">)               |
| [onselect](https://www.runoob.com/jsref/event-onselect.html) | 用户选取文本时触发 ( <input> 和 <textarea>)                  |
| [onsubmit](https://www.runoob.com/jsref/event-onsubmit.html) | 表单提交时触发                                               |

**注意：**

* **对于`onchange`事件来说，当表单中表单项的数据发生了改变并且失去了焦点时才会触发**。若仅仅改变数据，但是焦点依然在该表单项上时不会触发。失去焦点，相当于浏览器认为该表单项数据修改完成，才会去触发onchange事件。并且，绑定onchange事件的函数可以传入一个值，用于获取修改后的值。

* **对于`onsubmit`事件来说，该事件是绑定在`form`标签上**，不能绑定在input:submit标签上，即提交实际上是表单提交，而input:submit只是一个按钮用于提交表单。

  我们可以在onsubmit提交事件所绑定的函数中，使用`event.preventDefault()`方法去阻止提交。该方法的作用是阻止组件的默认行为。

  也可以根据onsubmit提交事件绑定的**函数的返回值**来判断是否提交数据，如果返回值为`true`，则表示提交；如果返回值为`false`，则表示不提交。需要在onsubmit事件类型的属性中，**在函数之前添加`return`关键字**。

  例如：

  ```html
  <head>
  <script>
    function testSubmit(){
      var flag = confirm("确定提交表单吗？");
      if(flag){
          return true;
      }
      return false;
    }
  </script>
  </head>
  <body>
  <form action="./js循环结构.html" method="get" onsubmit=" return testSubmit()">
    用户昵称：<input type="text" name="realName"/><br />
    登录密码：<input type="text" name="loginName" id="" /> <br />
    <input type="submit" value="注册" />
  </form>
  </body>
  ```

  根据confirm()方法的返回值来判断是否提交数据，如果返回true，则提交，也就是用户在信息确认框中点击确定时；如果返回false，则不提交，也就是用户在信息确认框中点击取消。

* **`onreset`事件同样也是绑定在form表单上**，而不是绑定在input:reset表单项上，因为重置是给表单进行重置，而不是给该表单项进行重置。



---

## 3、事件的绑定

事件绑定的方式有两种：

### 通过元素的属性绑定

即，通过元素指定的事件类型属性去绑定函数，在该元素发生对应事件时就会去触发函数的执行。

这种方式是使用最多的，通过去声明标签中的事件属性，来绑定创建的事件函数。这些属性一般都是以on开头，表示当发生...事情，例如onclick属性就表示当发生点击时。

案例：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <script>
            function fun1(){
                alert("点击了");
            }
    </script>
</head>
<body>
    <input type="button" value="按钮" onclick="fun1()">
</body>
</html>
```

此时就给按钮绑定了一个单击事件。







### 通过DOM编程动态绑定

在之前学习过通过元素的属性来进行绑定，类似于onclick都可以看作标签的属性，那么，我们可以通过`document.getElementById()`的方式，传入标签的id来获取标签所对应的对象，然后，通过`对象.属性`的方式来给标签元素绑定事件。

我们先来看个例子：

```html
<head>
</head>
<body>
<button id="btn1">按钮</button>

<script>
    //该变量是一个对象，该对象就表示button这个按钮
    var btn = document.getElementById("btn1");
    //给它绑定一个单击事件，我们之前所设置的onclick是标签的属性
    //那么，当我们获取到标签所对应的对象时，可以使用对象.属性的方式，给属性赋值
    //并且，我们之前给事件类型的属性赋值是赋函数名的方式，那么我们同样也可以直接将该属性设置为一个函数

	btn.onclick = function(){
        alert("按钮单击了");
    }
</script>
</body>
```

对于上例来说，我们使用了document.getElementById()根据id去获取了获取到了button标签所对应的对象，也就是说，上例中的btn实际上就是代表了`<button id="btn1">按钮</button>`这个标签。然后，我们使用了对象.属性的方式，去给该标签中的onclick属性赋值，也就是给该标签元素绑定点击事件。

这样一来，该按钮就绑定上了一个点击事件：

![动画](.\images\jqeworjqewior.gif) 



但是，我们知道，script标签一般都是放在head标签中的，上例却是将其放在了body标签中。我们将script标签改成放在head标签中以后，此时我们再来打开浏览器进行测试，发现此时点击按钮并不会触发点击事件了。

为什么会这样？

浏览器在扫描文件时，是一行一行地从上往下进行扫描。

若绑定点击事件的代码放在button标签后面，当浏览器扫描到绑定点击事件的代码时，此时浏览器就能够根据btn1这个id找到对应的button标签，从而绑定上；

若绑定点击事件的代码放在button标签前面，当浏览器扫描到绑定点击时间的代码时，此时浏览器就无法根据btn1这个id找到对应的标签，所以此时无法绑定点击事件。

那该怎么解决？难道要让script标签的代码放在body标签最后吗？

不用，我们只需要让确保浏览器在扫描完整个html文件之后，再去执行绑定事件的代码即可。

这里涉及到了一个事件：

> **页面加载事件 `onload`**
>
> **`onload`页面加载事件，是在浏览器加载完整个页面后触发的。**
>
> 该事件可以作为`body`标签的事件类型属性，在页面加载完毕之后触发；也可以在JS中使用`window.onload`去设置页面加载完毕后触发的函数。

这个事件和之前学习的事件有什么区别？

之前学习的事件，触发的方式是用户触发的，而这个事件是由浏览器触发的。

那么，上面的代码就可以改成：

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <script>
      function ready() {
        var btn = document.getElementById("btn1");
        btn.onclick = function () {
          alert("按钮单击了");
        };
      }
    </script>
  </head>
  <body onload="ready()">
    <button id="btn1">按钮</button>
  </body>
</html>
```

或者

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <script>
      window.onload = function () {
        var btn = document.getElementById("btn1");
        btn.onclick = function () {
          alert("按钮单击了");
        };
      };

    </script>
  </head>
  <body>
    <button id="btn1">按钮</button>
  </body>
</html>
```





**注意事项：**

1. `一个事件可以绑定多个函数`

   例如：onclick="fun1() fun2()"，此时一个点击事件就会触发两个函数。

2. `一个元素可以绑定多个事件`

   例如：<input type="button" onclick="fun1()" ondblclick="fun2()" />该元素就绑定了单击事件与双击事件。

---

## 4、事件的触发

事件触发有两种方式，一种就是行为触发，也就是当行为满足事件时触发；还有一种，是通过DOM编程触发事件。

第一种行为触发方式就不讲了，我们来说说第二种方式。

**DOM编程触发**

通过在事件的函数中，调用其他标签对象的事件函数，从而触发其他事件。这就是DOM编程触发。

例如：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <style>
        div {
            width: 200px;
            height: 200px;
            background-color: pink;
        }
    </style>
    
    <script>
        window.onload = function(){
            //给div创建一个点击事件，点击后让其变红
            var div = document.getElementById("div1");
            div.onclick = function(){
                div.style.backgroundColor = "red";
            }

            //给button创建一个点击事件，点击之后弹框并且调用div的点击事件，同时触发div的点击事件
            var btn = document.getElementById("btn1");
            btn.onclick = function(){
                alert("点击了按钮");
                //触发div的点击事件
                //这里的onclick在前面使用了函数进行赋值，所以该onclick实际上是一个函数
                //并且onclick也是标签的属性
                div.onclick();
            }
        }
    </script>
</head>
<body>
    <div id="div1"></div>
    <button id="btn1">按钮</button>
</body>
</html>
```

显示：

<img src=".\images\dhwihowqhie.gif" alt="动画" style="zoom: 50%;" /> 

可以看到，按钮的点击事件同时也去触发了div的点击事件。





---



# 弹窗的三种方式

**`alert()`：信息提示框**

传入的参数是提示的信息。

案例：

```js
alert("信息提示框");
```

<img src=".\images\image-20240528204941850.png" alt="image-20240528204941850" style="zoom:67%;" /> 



**`prompt()`：信息输入框**

可以传入两个参数，第一个参数是弹框的提示信息，第二个参数是输入框的提示信息。该函数有个返回值，是用户在输入框中输入的值。

案例：

```js
var value = prompt("信息输入框", "输入框内的提示信息");
console.log(value);
```

![动画](.\images\qwurqwreyqw.gif)





**`confirm()`：信息确认框**

传入一个参数，表示提示信息，有一个boolean类型的返回值，如果返回值为true，表示用户点击的是确定；如果返回值是false，表示用户点击的是取消。

案例：

在表单中绑定一个提交事件，当表单提交时，去循环用户是否提交，如果确定，则提交；如果取消，则不提交。

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <script>
      function testSubmit(){
        var flag = confirm("确定提交表单吗？");
        if(!flag){
            event.preventDefault();
        }
      }
    </script>
  </head>
  <body>
    <form action="./js循环结构.html" method="get" onsubmit="testSubmit()">
      用户昵称：<input type="text" name="realName"/><br />
      登录密码：<input type="text" name="loginName" id="" /> <br />
      <input type="submit" value="注册" />
      <input type="reset" value="清空" />
    </form>
  </body>
</html>
```

显示：

![动画](.\images\ejroqweroiwqe.gif)

这里使用到了event.preventDefault()方法，该方法表示取消组件的默认行为，也就取消了表单的提交行为。





---

# 六、BOM编程（:star:）

## 1、什么是BOM？

* BOM是Browser Object Model的简写，即浏览器对象模型。
* BOM由一系列对象组成，能够访问、控制和修改浏览器的属性和方法（通过window对象及属性的一系列方法，控制浏览器行为的一种编程）
* BOM没有统一的标准（每种浏览器都可以自定标准），所以我们学习的都是比较通用的。
* BOM编程是将浏览器窗口的各个组成部分抽象成各个对象，通过各个对象的API操作组件行为的一种编程。
* BOM编程的对象结构如下：
  * window：顶级对象，代表整个浏览器窗口
    * location对象：window对象的属性之一，代表浏览器的地址栏。
    * history对象：window对象的属性之一，代表浏览器的访问历史。
    * screen对象：window对象的属性之一，代表屏幕
    * navigator对象：window对象的属性之一，代表浏览器软件本身
    * document对象：window对象的属性之一，代表浏览器窗口目前解析的html文档
    * console对象：window对象的属性之一，代表浏览器开发者工具的控制台。
    * localStorage对象：window对象的属性之一，代表浏览器的本地数据持久化存储
    * sessionStorage对象：window对象的属性之一，代表浏览器的本地数据会话级存储

<img src=".\images\1681267483366.png" alt="1681267483366" style="zoom:67%;" />



简单来说，BOM编程就是通过window对象，去控制浏览器的一些属性与行为。



## 2、window对象的常见属性与方法

### window对象常见的属性（了解）

| 属性                                                         | 描述                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [closed](https://www.runoob.com/jsref/prop-win-closed.html)  | 返回窗口是否已被关闭。                                       |
| [defaultStatus](https://www.runoob.com/jsref/prop-win-defaultstatus.html) | 设置或返回窗口状态栏中的默认文本。                           |
| [document](https://www.runoob.com/jsref/dom-obj-document.html) | 对 Document 对象的只读引用。(请参阅[对象](https://www.runoob.com/jsref/dom-obj-document.html)) |
| [frames](https://www.runoob.com/jsref/prop-win-frames.html)  | 返回窗口中所有命名的框架。该集合是 Window 对象的数组，每个 Window 对象在窗口中含有一个框架。 |
| [history](https://www.runoob.com/jsref/obj-history.html)     | 对 History 对象的只读引用。请参数 [History 对象](https://www.runoob.com/jsref/obj-history.html)。 |
| [innerHeight](https://www.runoob.com/jsref/prop-win-innerheight.html) | 返回窗口的文档显示区的高度。                                 |
| [innerWidth](https://www.runoob.com/jsref/prop-win-innerheight.html) | 返回窗口的文档显示区的宽度。                                 |
| [localStorage](https://www.runoob.com/jsref/prop-win-localstorage.html) | 在浏览器中存储 key/value 对。没有过期时间。                  |
| [length](https://www.runoob.com/jsref/prop-win-length.html)  | 设置或返回窗口中的框架数量。                                 |
| [location](https://www.runoob.com/jsref/obj-location.html)   | 用于窗口或框架的 Location 对象。请参阅 [Location 对象](https://www.runoob.com/jsref/obj-location.html)。 |
| [name](https://www.runoob.com/jsref/prop-win-name.html)      | 设置或返回窗口的名称。                                       |
| [navigator](https://www.runoob.com/jsref/obj-navigator.html) | 对 Navigator 对象的只读引用。请参数 [Navigator 对象](https://www.runoob.com/jsref/obj-navigator.html)。 |
| [opener](https://www.runoob.com/jsref/prop-win-opener.html)  | 返回对创建此窗口的窗口的引用。                               |
| [outerHeight](https://www.runoob.com/jsref/prop-win-outerheight.html) | 返回窗口的外部高度，包含工具条与滚动条。                     |
| [outerWidth](https://www.runoob.com/jsref/prop-win-outerheight.html) | 返回窗口的外部宽度，包含工具条与滚动条。                     |
| [pageXOffset](https://www.runoob.com/jsref/prop-win-pagexoffset.html) | 设置或返回当前页面相对于窗口显示区左上角的 X 位置。          |
| [pageYOffset](https://www.runoob.com/jsref/prop-win-pagexoffset.html) | 设置或返回当前页面相对于窗口显示区左上角的 Y 位置。          |
| [parent](https://www.runoob.com/jsref/prop-win-parent.html)  | 返回父窗口。                                                 |
| [screen](https://www.runoob.com/jsref/obj-screen.html)       | 对 Screen 对象的只读引用。请参数 [Screen 对象](https://www.runoob.com/jsref/obj-screen.html)。 |
| [screenLeft](https://www.runoob.com/jsref/prop-win-screenleft.html) | 返回相对于屏幕窗口的x坐标                                    |
| [screenTop](https://www.runoob.com/jsref/prop-win-screenleft.html) | 返回相对于屏幕窗口的y坐标                                    |
| [screenX](https://www.runoob.com/jsref/prop-win-screenx.html) | 返回相对于屏幕窗口的x坐标                                    |
| [sessionStorage](https://www.runoob.com/jsref/prop-win-sessionstorage.html) | 在浏览器中存储 key/value 对。 在关闭窗口或标签页之后将会删除这些数据。 |
| [screenY](https://www.runoob.com/jsref/prop-win-screenx.html) | 返回相对于屏幕窗口的y坐标                                    |
| [self](https://www.runoob.com/jsref/prop-win-self.html)      | 返回对当前窗口的引用。等价于 Window 属性。                   |
| [status](https://www.runoob.com/jsref/prop-win-status.html)  | 设置窗口状态栏的文本。                                       |
| [top](https://www.runoob.com/jsref/prop-win-top.html)        | 返回最顶层的父窗口。                                         |





### window对象常见的方法（了解）

| 方法                                                         | 描述                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [alert()](https://www.runoob.com/jsref/met-win-alert.html)   | 显示带有一段消息和一个确认按钮的警告框。                     |
| [atob()](https://www.runoob.com/jsref/met-win-atob.html)     | 解码一个 base-64 编码的字符串。                              |
| [btoa()](https://www.runoob.com/jsref/met-win-btoa.html)     | 创建一个 base-64 编码的字符串。                              |
| [blur()](https://www.runoob.com/jsref/met-win-blur.html)     | 把键盘焦点从顶层窗口移开。                                   |
| [clearInterval()](https://www.runoob.com/jsref/met-win-clearinterval.html) | 取消由 setInterval() 设置的 timeout。                        |
| [clearTimeout()](https://www.runoob.com/jsref/met-win-cleartimeout.html) | 取消由 setTimeout() 方法设置的 timeout。                     |
| [close()](https://www.runoob.com/jsref/met-win-close.html)   | 关闭浏览器窗口。                                             |
| [confirm()](https://www.runoob.com/jsref/met-win-confirm.html) | 显示带有一段消息以及确认按钮和取消按钮的对话框。             |
| [createPopup()](https://www.runoob.com/jsref/met-win-createpopup.html) | 创建一个 pop-up 窗口。                                       |
| [focus()](https://www.runoob.com/jsref/met-win-focus.html)   | 把键盘焦点给予一个窗口。                                     |
| [getSelection](https://www.runoob.com/jsref/met-win-getselection.html)() | 返回一个 Selection 对象，表示用户选择的文本范围或光标的当前位置。 |
| [getComputedStyle()](https://www.runoob.com/jsref/jsref-getcomputedstyle.html) | 获取指定元素的 CSS 样式。                                    |
| [matchMedia()](https://www.runoob.com/jsref/met-win-matchmedia.html) | 该方法用来检查 media query 语句，它返回一个 MediaQueryList对象。 |
| [moveBy()](https://www.runoob.com/jsref/met-win-moveby.html) | 可相对窗口的当前坐标把它移动指定的像素。                     |
| [moveTo()](https://www.runoob.com/jsref/met-win-moveto.html) | 把窗口的左上角移动到一个指定的坐标。                         |
| [open()](https://www.runoob.com/jsref/met-win-open.html)     | 打开一个新的浏览器窗口或查找一个已命名的窗口。               |
| [print()](https://www.runoob.com/jsref/met-win-print.html)   | 打印当前窗口的内容。                                         |
| [prompt()](https://www.runoob.com/jsref/met-win-prompt.html) | 显示可提示用户输入的对话框。                                 |
| [resizeBy()](https://www.runoob.com/jsref/met-win-resizeby.html) | 按照指定的像素调整窗口的大小。                               |
| [resizeTo()](https://www.runoob.com/jsref/met-win-resizeto.html) | 把窗口的大小调整到指定的宽度和高度。                         |
| scroll()                                                     | 已废弃。 该方法已经使用了 [scrollTo()](https://www.runoob.com/jsref/met-win-scrollto.html) 方法来替代。 |
| [scrollBy()](https://www.runoob.com/jsref/met-win-scrollby.html) | 按照指定的像素值来滚动内容。                                 |
| [scrollTo()](https://www.runoob.com/jsref/met-win-scrollto.html) | 把内容滚动到指定的坐标。                                     |
| [setInterval()](https://www.runoob.com/jsref/met-win-setinterval.html) | 按照指定的周期（以毫秒计）来调用函数或计算表达式。           |
| [setTimeout()](https://www.runoob.com/jsref/met-win-settimeout.html) | 在指定的毫秒数后调用函数或计算表达式。                       |
| [stop()](https://www.runoob.com/jsref/met-win-stop.html)     | 停止页面载入。                                               |
| [postMessage()](https://www.runoob.com/jsref/met-win-postmessage.html) | 安全地实现跨源通信。                                         |







## 3、通过BOM编程控制浏览器行为演示

> **1、`window`对象的API演示：三种弹窗方式**

三种弹窗方式分别是：`alert()`、`prompt()`、`confirm()`，这三种方式我们在之前详细地讲过。

实际上这三种方式，都是使用window对象进行调用的。

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <script>
       function fun1(){
        window.alert("hello");
       }

       function fun2(){
        var res = window.prompt("hello，请输入姓名");
        console.log(res);
       }

       function fun3(){
        var res = window.confim("确定要删除吗？");
        console.log(res);
       }
    </script>
</head>
<body>
    <button onclick="fun1()">信息提示框</button>
    <button onclick="fun2()">信息输入框</button>
    <button onclick="fun3()">信息确认框</button>
</body>
</html>
```

如果是window对象在调用属性或方法时，window对象是可以省略的，直接进行访问即可。







> **2、`window`对象的API演示：定时任务**

使用window对象的`setTimeout()`方法执行定时任务，在函数中传入两个参数，第一个参数是超出指定时间后所要执行的函数，第二个参数是定时的时间，单位是毫秒。该定时任务只会执行一次。

例如：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <script>
       function fun4(){
            setTimeout(function(){
                alert("两秒时间到！");
            }, 2000);
       }
    </script>
</head>
<body>
    <button onclick="fun4()">两秒后弹框</button>
</body>
</html>
```





> **3、`history`对象的API演示：上一页与下一页**

history对象表示的页面访问的历史记录，历史记录中包含着上一页访问的地址以及下一页访问的地址。

通过history对象的`back()`与`forward()`方法，表示回到上一页以及回到下一页。就和浏览器中的上一页的箭头与下一页的箭头的作用一致：

![image-20240529011111479](.\images\image-20240529011111479.png) 

案例：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <script>
       function funA(){
        //back()方法表示往回翻一页
        history.back();
       }

       function funB(){
        //forward()表示向前翻一页
        history.forward();
       }
    </script>
</head>
<body>
    <a href="https://www.baidu.com">百度</a>

    <button onclick="funA()">上一页</button>
    <button onclick="funB()">下一页</button>
</body>
</html>
```







> **4、`location`对象的API演示：实现页面跳转**

location对象表示着地址栏，那么一般就是使用location对象来修改地址栏的信息，从而实现地址的改变、页面的跳转。

可以使用location对象的`href`属性，修改地址栏的数据，从而实现页面的跳转。

案例：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <script>
       function funC(){
        location.href = "https://www.baidu.com";
       }
    </script>
</head>
<body>
    <button onclick="funC()">跳转到百度地址</button>
</body>
</html>
```





## 4、通过BOM编程实现会话级和持久级数据存储演示

* **会话级数据**：内存型数据，是浏览器在内存上临时存储的数据，浏览器关闭后，数据就丢失。通过window对象的`sessionStorge`属性对象实现。
* **持久级数据**：磁盘型数据，是浏览器在磁盘上持久存储的数据，浏览器关闭后，数据仍会存在。通过window对象的`localStorge`属性对象实现。

这两个对象中存储的数据，类似于Map集合类型存储数据，使用**key=value**键值对的方式进行存储。

使用**`setItem()`方法存储数据**，传入两个参数，第一个参数表示`key`，第二个参数表示`value`。

使用**`getItem()`方法获取数据**，传入key值，去获取key所对应的value值。

使用**`removeItem()`方法清除数据**，传入key值，去清除key所对应的key=value数据。

**案例：**

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <script>

    function funA(){
        //向sessionStorage中存储数据
        sessionStorage.setItem("key1", "value1");

        //向localStorage中存储数据
        localStorage.setItem("key2", "value2");
    }

    function funB(){
        //使用getItem()方法读取数据
        console.log(sessionStorage.getItem("key1"));
        console.log(localStorage.getItem("key2"));
    }

    function funC(){
        //使用removeItem()方法清除数据
        sessionStorage.removeItem("key1");
        localStorage.removeItem("key2");
    }
    </script>
</head>
<body>
    <button onclick="funA()">存储数据</button>
    <button onclick="funB()">读取数据</button>
    <button onclick="funC()">清空数据</button>
</body>
</html>
```

**实现：**

打开页面，原本浏览器的本地存储与会话存储中，均没有数据（在F12的应用程序中的存储查看）：

<img src=".\images\image-20240529013659030.png" alt="image-20240529013659030" style="zoom: 50%;" /><img src=".\images\image-20240529013715577.png" alt="image-20240529013715577" style="zoom: 50%;" />

然后我们点击存储数据按钮，此时浏览器的本地存储与会话存储中，都会添加一个数据：

<img src=".\images\image-20240529013811895.png" alt="image-20240529013811895" style="zoom:50%;" /><img src=".\images\image-20240529013826257.png" alt="image-20240529013826257" style="zoom:50%;" />

读取数据，控制台则会打印存储数据信息：

![image-20240529013958920](.\images\image-20240529013958920.png) 

点击清空数据按钮，浏览器的存储数据都会被清除：

<img src=".\images\image-20240529014034271.png" alt="image-20240529014034271" style="zoom:50%;" /><img src=".\images\image-20240529014058403.png" alt="image-20240529014058403" style="zoom:50%;" />







---

# 七、DOM编程（:star:）

## 1、什么是DOM编程？

浏览器对html页面解析之后，得到的对象就是document对象。

我们按F12，查看元素所显现出来的html文档，就是document对象：

<img src=".\images\image-20240529095319019.png" alt="image-20240529095319019" style="zoom: 67%;" /> 

* document对象代表整个html文档，可用来访问页面中的所有元素，是最复杂的一个dom对象，可以说是学好dom编程的关键所在。
* 根据HTML代码结构特点，document对象本身是一种**树形结构**的文档形象。这个树形结构，是一个逻辑结构。
* **dom树中节点的类型**
  * `node` 结点，所有结点的父类型
    * `element`	元素节点，node的子类型之一，代表一个完整的标签
    * `attribute`  属性节点，node的子类型之一，代表元素的属性
    * `text`       文本节点，node的子类型之一，代表双标签中间的文本

例如：html文档如下所示

<img src=".\images\1681269953136.png" alt="1681269953136" style="zoom:67%;" />

上面的代码生成的document树如下：

<img src=".\images\1681269970254.png" alt="1681269970254"  />

上面的树整体就是一个document对象，下面有两个元素，分别是head与body，在head和body中，又有很多的element子节点，在element子节点下，就是各个节点中的attribute与text节点，分别表示属性与文本。

只要上面属性结构中，各个结点发生了变化，浏览器会实时地更新内容。DOM编程实际上就是通过改变上面结点的内容，来达成动态地改变页面内容的目的。



* **DOM编程其实就使用window对象的document属性的相关API完成度以页面元素的控制的编程。**

<img src=".\images\1681270260741.png" alt="1681270260741"  />



---

## 2、获取页面元素的几种方式

使用document去操作页面中的元素，分为三个步骤：

1. **获得document，也叫作dom树**

   document是window对象的属性，window对象是由浏览器解析后生成的，就不需要我们自己去创建，直接获取即可。并且window.document中window对象可以省略。

2. **从document中获取要操作的元素**

   1）直接获取

   2）间接获取

3. **对元素进行操作**

   1）操作元素的属性

   2）操作元素的样式

   3）操作元素的文本

   4）增删元素



### 在整个文档范围内查找元素结点

> 1. **`document.getElementById("id值")`**：根据id查询元素结点，返回唯一一个具体的元素节点。

案例：

```html
<head>
<script>
    function fun1(){
        //使用getElementById()获取id为username的元素
        var usernameEle = document.getElementById("username");//根据元素的id值获取页面上唯一的一个元素
        console.log(usernameEle);
    }
</script>
</head>
<body>
<div id="div01">
    <input type="text" id="username" name="aaa"/>
    <input type="text" id="password" name="aaa"/>
    <input type="text" id="email"/>
    <input type="text" id="address"/>
</div>
<input type="text"/><br>

<input type="button" value="根据id获取指定元素" onclick="fun1()">
</body>
```

打印结果：

<img src=".\images\image-20240529110243625.png" alt="image-20240529110243625" style="zoom: 67%;" /> 





> 2. **`document.getElementsByTagName("标签名")`**：根据标签名查询元素结点，返回的是一个`HTMLCollection`对象，是标签的集合，属于Object类型。那么，我们就可以使用循环结构，来对结果进行遍历。

案例：

```html
<head>
<script>

    function fun2(){
        //使用getElementsByTagName()获取input标签名的元素
        var inputEles = document.getElementsByTagName("input");
        console.log(inputEles);

        for(var i = 0; i < inputEles.length; i++){
            console.log(inputEles[i]);
        }
    }

</script>
</head>
<body>
<div id="div01">
    <input type="text" id="username" name="aaa"/>
    <input type="text" id="password" name="aaa"/>
    <input type="text" id="email"/>
    <input type="text" id="address"/>
</div>
<input type="text"/><br>

<input type="button" value="根据标签名获取多个元素" onclick="fun2()">
</body>
```

打印结果：

<img src=".\images\image-20240529110539070.png" alt="image-20240529110539070" style="zoom:50%;" /> 



**注意：**以上的HTMLCollection对象的遍历，不要使用foreach循环，因为该对象是Object类型，不是数组类型，对象中包含属性与方法，foreach循环会遍历所有的元素。使用foreach循环的打印结果：

<img src=".\images\image-20240529110049064.png" alt="image-20240529110049064" style="zoom:50%;" /> 可以看到，其中还包含了length属性以及该对象中的两个方法。

但是如果使用for循环，因为该对象中前length个数据都是集合中的标签元素，所以刚好能够通过i < length这个判断，只将集合中的标签元素打印，而不会打印对象自身的属性与函数。

<img src=".\images\image-20240529105724016.png" alt="image-20240529105724016" style="zoom:50%;" /> 







> 3. **`document.getElementsByName("name值")`**：根据标签的name属性值查询元素结点，返回的也是一个HTMLCollection标签集合。可以使用循环结构对集合进行遍历。

案例：

```html
<head>
<script>

  function fun3() {
    //使用getElementsByName获取多个name值相同的元素
    var aaaEles = document.getElementsByName("aaa");
    for (var i = 0; i < aaaEles.length; i++) {
      console.log(aaaEles[i]);
    }
  }
</script>
</head>
<body>
<div id="div01">
  <input type="text" id="username" name="aaa" />
  <input type="text" id="password" name="aaa" />
  <input type="text" id="email" />
  <input type="text" id="address" />
</div>
<input type="text" /><br />

<input type="button" value="根据name属性值获取多个元素" onclick="fun3()" />
</body>
```

打印结果：

<img src=".\images\image-20240529111558223.png" alt="image-20240529111558223" style="zoom: 67%;" /> 





> 4. **`document.getElementsByClassName("class属性值")`**：根据标签的class属性值查询元素节点，返回的是一个HTMLCollection标签集合。

案例：

```html
<head>
<script>
  function fun4(){
    //使用getElementsByClassName获取多个指定class值的元素
    var aClassEles = document.getElementsByClassName("a");
    for (var i = 0; i < aClassEles.length; i++) {
      console.log(aClassEles[i]);
    }
  }
</script>
</head>
<body>
<div id="div01">
  <input type="text" class="a" id="username" name="aaa" />
  <input type="text" class="b" id="password" name="aaa" />
  <input type="text" class="a" id="email" />
  <input type="text" class="b" id="address" />
</div>
<input type="text" /><br />

<input type="button" value="根据class属性值获取多个元素" onclick="fun4()" />
</body>
</html>
```

打印结果：

<img src=".\images\image-20240529111955873.png" alt="image-20240529111955873" style="zoom:67%;" /> 





---

### 在具体元素节点范围内查找子节点

| 功能               | API                         | 返回值   |
| ------------------ | --------------------------- | -------- |
| 查找子标签         | `element.children`          | 标签数组 |
| 查找第一个子标签   | `element.firstElementChild` | 标签对象 |
| 查找最后一个子标签 | `element.lastElementChild`  | 标签对象 |



### 查找指定子元素节点的父节点

| 功能                     | API                     | 返回值   |
| ------------------------ | ----------------------- | -------- |
| 查找指定元素节点的父标签 | `element.parentElement` | 标签对象 |

### 查找指定元素节点的兄弟节点

| 功能               | API                              | 返回值   |
| ------------------ | -------------------------------- | -------- |
| 查找前一个兄弟元素 | `element.previousElementSibling` | 标签对象 |
| 查找后一个兄弟元素 | `element.nextElementSibling`     | 标签对象 |



案例：

```html
<head>
<script>

function fun5(){
// 先获取父元素
 var div01 = document.getElementById("div01")
 // 获取所有子元素
 var cs=div01.children // 通过父元素获取全部的子元素
 for(var i =0;i< cs.length;i++){
        console.log(cs[i])
 }
 console.log(div01.firstElementChild)  // 通过父元素获取第一个子元素
 console.log(div01.lastElementChild)   // 通过父元素获取最后一个子元素
}

    
function fun6(){
    // 获取子元素
    var pinput =document.getElementById("password")
    console.log(pinput.parentElement) // 通过子元素获取父元素
}

    
function fun7(){
    // 获取子元素
    var pinput =document.getElementById("password")
    console.log(pinput.previousElementSibling) // 获取前面的第一个元素
    console.log(pinput.nextElementSibling) // 获取后面的第一个元素
}
    
</script>
</head>
<body>
<div id="div01">
    <input type="text" class="a" id="username" name="aaa"/>
    <input type="text" class="b" id="password" name="aaa"/>
    <input type="text" class="a" id="email"/>
    <input type="text" class="b" id="address"/>
</div>
<input type="text" class="a"/><br>

<hr>
<input type="button" value="通过父元素获取子元素" onclick="fun5()" id="btn05"/>
<input type="button" value="通过子元素获取父元素" onclick="fun6()" id="btn06"/>
<input type="button" value="通过当前元素获取兄弟元素" onclick="fun7()" id="btn07"/>

</body>
```



---

## 3、元素的操作

### 属性操作

标签的属性，都是使用**`元素名.属性名`**来进行获得与修改。

由于标签的样式也是标签的一种属性，所以我们也可以使用这种方式来修改元素的样式。

修改样式的语法格式为：**`元素名.style.样式名 = 样式值`**。

如果原本样式名带有"-"符号，则在JS中的样式名需要转换成驼峰式，例如在CSS中的样式名为border-radius，则在JS中作为style属性的样式名则为borderRadius。



**案例1：**修改元素的属性值，例如修改输入框的value属性

```html
<head>
<script>
function changeAttribute(){
var in1 = document.getElementById("in1")
console.log(in1.value);
in1.value = "hi";
console.log(in1.value);
}

</script>
</head>
<body>
<!--操作元素的属性值-->
<input type="text" id="in1" value="hello">
<br/>
<button onclick="changeAttribute()">变</button>
</body>
```

在上例中，给变按钮添加了一个点击事件，当我们点击按钮时，会去获取输入框的元素，使用元素.属性名的方式，将输入框的value属性修改成hi。

<img src=".\images\qwrqywerq" alt="动画" style="zoom:67%;" /> 





**案例2**：修改样式，将文字颜色修改为黄色

```html
<head>
<style>
    #in1{
        color: red;
    }
</style>
<script>

function changeColor(){
var in1 = document.getElementById("in1")
in1.style.color = "yellow";
in1.style.borderRadius

}
</script>
</head>
<body>
<input type="text" id="in1" value="hello"><br/>

<button onclick="changeColor()">变颜色</button>
</body>
</html>
```

在上例中，通过去获取输入框的元素，然后使用`元素名.style.样式名`的方式，修改元素的样式。

<img src=".\images\ekebqrqwgirwqe.gif" alt="动画" style="zoom:67%;" /> 





---

### 内部文本操作

**两种操作方式：**

* **`element.innerText`**
* **`element.innerHTML`**

这两种操作都是可以获取或者设置标签体中的文本内容。

但是，这两种方式有些区别：

**`innerText`会将该元素中所有的内容都看作是文本，包括标签也看成是文本；**

**`innerHTML`则会将元素中的标签内容进行解析。**



**案例1：**

```html
<head>
<script>
  window.onload = function () {
    var div1 = document.getElementById("div1");
    console.log(div1.innerHTML);
    console.log(div1.innerText);
  }
</script>
</head>
<body>
<div id="div1">hello</div>
</body>
```

打印结果：

<img src=".\images\image-20240529132133566.png" alt="image-20240529132133566" style="zoom:67%;" /> 

如上图，innerHTML和innerText都能获取标签体中的文本。





**案例2：**

```html
<head>
<script>
  window.onload = function () {
    var div1 = document.getElementById("div1");
    console.log(div1.innerHTML);
    console.log(div1.innerText);
  }
</script>
</head>
<body>
<div id="div1">hello
    <p>你好</p>
</div>
</body>
```

显示：

<img src=".\images\image-20240529132703275.png" alt="image-20240529132703275" style="zoom:67%;" /> 



**并且，如果使用`innerText`修改内容时添加标签，则也会被看作是普通文本；但是如果使用`innerHTML`修改内容添加标签，则会解析成相应的标签元素。**

**案例3：**

```html
<head>
<script>
    function changeText(){
        var divC1 = document.getElementById("div1");
        divC1.innerText = "<h1>hi</h1>";
    }

    function changeHTML(){
        var divC1 = document.getElementById("div1");
        divC1.innerHTML = "<h1>hi</h1>";
    }
</script>
</head>
<body>
<div id="div1">hello</div><br>

<button onclick="changeText()">修改文本</button>
<button onclick="changeHTML()">修改内容</button>
</body>
```

显示：

<img src=".\images\动124213421342" alt="动画" style="zoom:67%;" />  





---

### 增删操作

**创建元素**

* **`document.createElement("标签名")`**：通过标签名，创建一个新的元素，该元素是新创建出来的，所以没有任何的属性与文本，只有标签信息，所以我们创建完元素后，还需要使用元素名.属性名或者innerText等方式进行初始化。



**插入元素**

* **`parentEle.appendChild(ele)`**：将ele元素添加到element子节点中的最后面。
* **`parentEle.insertBefore(newEle, targetEle)`**：将newEle元素插入到父元素parentEle中指定子元素targetEle的前面。
* **`parentEle.replaceChild(newEle, oldEle)`**：在parentEle元素的子元素中，使用newEle元素替换原本的oldEle元素。



**删除元素**

* **`element.remove()`**：哪个元素调用了remove()，该元素就会从dom树中移除。



**案例**

在页面中创建一个无序列表，显示一些城市：

```html
<ul id="city">
  <li id="bj">北京</li>
  <li id="sh">上海</li>
  <li id="sz">深圳</li>
  <li id="gz">广州</li>
</ul>
```



案例1：在城市列表的最后添加一个子标签：`<li id="cs">长沙</li>`

```html
<head>
    <script>
      function addCs() {
        //创建一个新的元素
        var csLi = document.createElement("li"); //该对象是<li></li>

        //设置子元素的属性和文本
        csLi.id = "cs";
        csLi.innerText = "长沙";

        //将子元素放入父元素中
        var cityUl = document.getElementById("city");
        //在父元素中追加子元素
        cityUl.appendChild(csLi);
      }
    </script>
  </head>
  <body>
    <ul id="city">
      <li id="bj">北京</li>
      <li id="sh">上海</li>
      <li id="sz">深圳</li>
      <li id="gz">广州</li>
    </ul>
    <hr />
    <!-- 目标1：在城市列表的最后添加一个子标签： <li id="cs">长沙</li> -->
    <button onclick="addCs()">增加长沙</button>
  </body>
</html>
```

实现：

<img src="C:\Users\14036\Desktop\笔记\2、JavaWeb\images\jweprjjweqrjpqw.gif" alt="动画" style="zoom:67%;" /> 





案例2：在城市列表中的深圳之前，添加一个子标签：`<li id="cs">长沙</li>`

```html
<head>
    <script>
      function addCsBeforeSz() {
        //创建一个新的元素
        var csLi = document.createElement("li"); //该对象是<li></li>

        //设置子元素的属性和文本
        csLi.id = "cs";
        csLi.innerText = "长沙";

        //获取父元素ul
        var cityUl = document.getElementById("city");
        //获取深圳元素szLi
        var szLi = document.getElementById("sz");

        //将长沙元素添加到深圳之前
        cityUl.insertBefore(csLi, szLi);
      }

    </script>
  </head>
  <body>
    <ul id="city">
      <li id="bj">北京</li>
      <li id="sh">上海</li>
      <li id="sz">深圳</li>
      <li id="gz">广州</li>
    </ul>
    <hr />

    <!-- 目标2：在城市列表中的深圳之前，添加一个子标签： <li id="cs">长沙</li> -->
    <button onclick="addCsBeforeSz()">在深圳之前添加长沙</button>

  </body>
```

实现：

<img src="C:\Users\14036\Desktop\笔记\2、JavaWeb\images\13321231312" alt="动画" style="zoom:67%;" /> 





案例3：将城市列表中的深圳替换为：`<li id="cs">长沙</li>`

```html
<head>
<script>
  function replaceSzToCs(){
    //创建一个新的元素
    var csLi = document.createElement("li"); //该对象是<li></li>

    //设置子元素的属性和文本
    csLi.id = "cs";
    csLi.innerText = "长沙";

    //获取父元素ul
    var cityUl = document.getElementById("city");
    //获取深圳元素szLi
    var szLi = document.getElementById("sz");

    //替换深圳为长沙
    cityUl.replaceChild(csLi, szLi);
  }
</script>
</head>
<body>
<ul id="city">
  <li id="bj">北京</li>
  <li id="sh">上海</li>
  <li id="sz">深圳</li>
  <li id="gz">广州</li>
</ul>
<hr />

<!-- 目标3：将城市列表中的深圳替换为 <li id="cs">长沙</li>-->
<button onclick="replaceSzToCs()">替换深圳为长沙</button> 
</body>
```

实现：

<img src="C:\Users\14036\Desktop\笔记\2、JavaWeb\images\123131312312.gif" alt="动画" style="zoom:50%;" /> 





案例4：在城市列表中删除深圳

```html
<head>
<script>

  function removeSz(){
    //获取深圳元素
    var szLi = document.getElementById("sz");
    //移除深圳元素
    szLi.remove();
  }
</script>
</head>
<body>
<ul id="city">
  <li id="bj">北京</li>
  <li id="sh">上海</li>
  <li id="sz">深圳</li>
  <li id="gz">广州</li>
</ul>
<hr />

<!-- 目标4：在城市列表中删除深圳 -->
<button onclick="removeSz()">删除深圳</button>

</body>
```

实现：

<img src="C:\Users\14036\Desktop\笔记\2、JavaWeb\images\动1313123123" alt="动画" style="zoom:67%;" />  



### 总结

> 1. **操作元素的属性**  **`元素名.属性名=""`**
>
> 2. **操作元素的样式**  **`元素名.style.样式名=""`**  样式名"-"要进行驼峰转换
>
> 3. **操作元素的文本  `元素名.innerText`   只识别文本**
>
>    ​			  **`元素名.innerHTML`   同时可以识别html代码**
>
> 4. **增删元素**
>
>    **`document.createElement("标签名")`**   创建元素
>
>    **`父元素.appendChild(子元素)`**    在父元素中追加子元素
>
>    **`父元素.insertBefore(新元素, 参照元素)`** 在参照元素前插入新元素
>
>    **`父元素.replaceChild(新元素, 旧元素)`**  在新元素替换旧元素
>
>    **`元素.remove()`**   清除元素









