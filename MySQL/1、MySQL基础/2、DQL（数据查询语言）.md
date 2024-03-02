# **SELECT语句的完整结构**：

```sql
SELECT <字段名>
FROM <表名>
JOIN <表名>
ON <连接条件>
WHERE <筛选条件>
GROUP BY <字段名>
HAVING <筛选条件>
UNION
ORDER BY <字段名>
LIMIT <限制行数>;
```

# **SELECT查询执行的顺序**：

![SQL查询语句的执行顺序解析](.\images\format,png)

# todo!







我们来看一个案例来具体查看一下执行顺序：

```sql
SELECT 班级, avg(数学成绩) as 数学平均成绩

FROM 学生信息表

WHERE 数学成绩 IS NOT NULL 

GROUP BY 班级

HAVING 数学平均成绩 > 75

ORDER BY 数学平均成绩 DESC

LIMIT 3;
```

上例的SQL执行顺序，如下：

1. 首先执行FROM子句，从学生成绩表中组装数据源的数据。
2. 执行WHERE子句，筛选学生成绩表中所有学生的数学成绩不为NULL的数据。
3. 执行GROUP BY子句，把学生成绩表按“班级”字段进行分组。
4. 计算avg聚合函数，按照每个班级分组求出数学平均成绩。
5. 执行HAVING子句，筛选出班级数学平均成绩大于75分的。
6. 执行SELECT语句，返回指定数据。
7. 执行ORDER BY子句，把最后的结果按“数学平均成绩”进行排序。
8. 执行LIMIT，限制仅返回3条数据，结合ORDER BY子句，即返回所有班级中数学平均成绩的前三名的班级及其数学平均成绩。



若将语句改成如下所示，会怎样？

```sql
SELECT 班级, avg(数学成绩) as 数学平均成绩

FROM 学生信息表

WHERE 数学成绩 IS NOT NULL AND avg(数学成绩) > 75

GROUP BY 班级

ORDER BY 数学平均成绩 DESC

LIMIT 3;
```

我们发现，若将avg(数学成绩) > 75放到WHERE子句中，此时GROUP BY语句还未执行，因此此时聚合值avg(数学成绩)还是未知的，所以会报错。



# 基本的SELECT语句

## 1、SELECT...

```sql
SELECT 1;#没有任何子句
SELECT 9/2;#没有任何子句
```

**SELECT...FROM...**

* 语法：

  ```sql
  SELECT 标识选择哪些列
  FROM 标识从哪个表中选择;
  ```

* 选择全部列：

  ```sql
  SELECT *
  FROM departments;
  ```

  > 一般情况下，除非需要使用表中所有的字段数据，最好不要使用通配符*。使用通配符虽然可以节省输入查询语句的时间，但是获取不需要的列数据通常会降低查询和所使用的应用程序的效率。通配符的优势是，当不知道所需要的列的名称时，可以通过它获取它们。
  >
  > 在生成环境下，不推荐直接使用SELECT *进行查询。

* 选择特定的列：

  ```sql
  SELECT department_id, location_id
  FROM departments;
  ```

  > MySQL中的SQL语句是不区分大小写的，因此SELECT和select的作用是相同的，但是推荐关键字使用大写，数据表和列名使用小写。

## 2、列的别名

* 重命名一个列。

* 便于计算。

* 紧跟列名，**`在列名和别名之间加入关键字AS，列的别名使用""双引号引起来，不要使用''单引号`**，以便在别名中包含空格或特殊字符并区分大小写。

* AS可以省略。

* 建议别名简短，见名知意。

* 举例：

  ```sql
  SELECT last_name AS "name", commission_pat "comm"
  FROM employess;
  
  SELECT last_name "Name", salary*12 AS "Annual Salary"
  FROM employees;
  ```

但是注意！！！

给表起别名时，不能使用""双引号！

例如：

```sql
SELECT e.employee_id, d.department_name, e.department_id
FROM employees AS "e", departments AS "d"
WHERE e.department_id = d.department_id;
```

此时执行就会报错：

1064 - You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near '"e", departments AS "d"
WHERE e.department_id = d.department_id' at line 2

表取别名直接使用，不要使用双引号""引起来。

## 3、去除重复行

默认情况下，查询会返回全部行，包括重复行。

例：

```sql
SELECT department_id
FROM employees;
```

查询结果：

<img src=".\images\image-20240228195934441.png" align="left">

> **`在SELECT语句中使用关键字DISTINCT去除重复行。`**

```sql
SELECT DISTINCT department_id
FROM employees;
```

查询结果：

<img src=".\images\image-20240228200111169.png" align="left">

**注意！**

1. `DISTINCT`关键字实际上的作用是对原查询后的信息进行去重操作，而不是将原有表中的信息去重之后再查询。

   例如：

   ```sql
   SELECT DISTINCT salary, department_id
   FROM employees;
   ```

   在上述的代码中，是先去employees表中查询所有的salary和department_id信息，然后对查询到的salary和department_id信息进行去重。

2. 当要查询多个字段时，要使用去重操作，必须将`DISTINCT`关键字放在SELECT后面，而不能放在某一个字段前面。

   例如：

   ```sql
   #错误的写法：
   SELECT salary, DISTINCT department_id
   FROM employees;
   
   #正确写法，这是对查询到的salary和department_id字段整体进行去重
   #去重了两个字段都相同的情况
   SELECT DISTINCT salary, department_id
   FROM employees;
   ```

   即`DISTINCT`去重操作只会对所有查询到的字段进行去重，而不会根据其中一个字段进行去重。

3. 当我们要根据某一字段去重时，使用DISTINCT关键字进行查询，此时SELECT只能去查询该字段，而不能将其他字段也加上，否则就会根据所有查询的字段进行去重。



## 4、空值参与运算

空值：`null`

> **`空值参与运算，无论使用什么运算符，结果一定是空。`**

所以，我们在开发中，需要去考虑空值给运算带来的影响。

例如：

```sql
SELECT employee_id, salary AS "月工资", salary * (1 + commission_pct) * 12 AS "年工资", commission_pct
FROM employees;
```

查询结果：

![image-20240228202435053](.\images\image-20240228202435053.png)

可以发现，只要commission_pct为空时，运算得到的年工资也一定为空。

> **那么在实际的开发中，该如何避免空值null给运算带来的影响呢？**
>
> **引入`IFNULL(expr1, expr2)`函数**
>
> 这个函数的作用是判断某字段或某表达式expr1是否为null，如果为null，则返回指定的值expr2；否则返回原来的值

那么，我们就可以对上述的SQL语句进行修改：

```sql
SELECT employee_id, salary AS "月工资", salary * (1 + IFNULL(commission_pct, 0)) * 12 AS "年工资", commission_pct
FROM employees;
```

修改后的SQL语句就表示，如果commission_pct字段为null，则使用0表示；否则使用原数据。

查询结果：

![image-20240228203328070](.\images\image-20240228203328070.png)

在MySQL中，空值不等于空字符串。空字符串的长度是0，而空值的长度是空，而且，在MySQL里面，空值是占用空间的。

## 5、着重号

我们在给表名、字段名取名字的时候，要避免和保留字、关键字以及函数名冲突。如果真的有相同，那么需要在SQL语句中使用一对**``**（着重号）引起来，表示这个名字不是关键字或保留字，否则执行的时候编译器就会将表名或字段名看做是关键字来使用从而报错。

例如：

```sql
SELECT * FROM ORDER;
```

ORDER是SQL中的关键字，所以会报错。

此时我们要修改成：

```sql
SELECT * FROM `ORDER`;
```

着重号的使用与单引号、双引号有区别，虽然MySQL对这三个符号的使用不会那么严格，可以互相代替，但是，根据上述所学内容：

> 1. 别名使用双引号""
> 2. 名称与关键字、保留字有冲突时，使用着重号``，表明该名称不是关键字或保留字
> 3. 当我们要表示字符串时，使用单引号''

## 6、查询常数

SELECT查询还可以对常数进行查询。对的，**就是在SELECT查询结果中增加一列固定的常数列**。这列的取值是我们指定的，而不是从数据表中动态取出的。

你可能会问为什么我们还要对常数进行查询呢？

SQL中的SELECT语法的确为我们提供了这个功能，一般来说我们只从一个表中查询数据，通常不需要增加一个固定的常数列，但如果我们想整合不同的数据源，用常数列作为这个表的标记，就需要查询常数。

比如，我们想对employees数据表中的员工姓名进程查询，同时增加一个字段`corporation`，这个字段固定为"璞华"，可以这样写：

```sql
SELECT '璞华' as corporation, last_name FROM employees;
```

![image-20240228204725174](.\images\image-20240228204725174.png)

## 7、显示表结构

使用`DESCRIBE`或`DESC`命令，显示表字段的详细信息。

例如：

```sql
DESC employees;
```

查询结果：

![image-20240228205141459](.\images\image-20240228205141459.png)

其中，各个字段的含义分别解释如下：

* Field：表示字段名称。
* Type：表示字段类型。
* Null：表示该列是否可以存储Null值。
* Key：表示该列是否已编制索引。PRI表示该列是表主键的一部分；UNI表示该列是UNIQUE索引的一部分；MUL表示在列中某个给定值允许出现多次。
* Default：表示该列是否有默认值，如果有，值是多少。
* Extra：表示可以获取的与给定列有关的附加信息，例如AUTO_INCREMENT等。

## 8、使用WHERE过滤数据

* **语法：**

  ```sql
  SELECT 字段1，字段2
  FROM 表名
  WHERE 过滤条件;
  ```

  使用WHERE子句，只查询满足某些条件的数据，将不满足条件的行过滤掉。

  **`WHERE子句紧跟着FROM子句。`**

举例：查询员工表中90号部门工作的员工信息

```sql
SELECT *
FROM employees
WHERE department_id = 90;
```

查询结果：

![image-20240228212326855](.\images\image-20240228212326855.png)



* **WHERE子句执行的过程**：

  例如：

  ```sql
  SELECT last_name
  FROM employees
  WHERE salary = 6000;	
  ```

  过程大概是：

  指针循环地去获取所有记录（表中所有字段的信息），然后取出记录中的salary字段，与条件6000进行比较。

  如果返回的是0，则表示不满足条件，去除该记录；如果返回的是1，表示满足条件，保留该记录。



注意！

> **WHERE子句中不能使用列别名**

比如：

```sql
SELECT salary as s

FROM employees

WHERE s > 10000;
```

这是错误的！会报错

原因：先去执行FROM子句，获取表信息；然后去执行WHERE子句，进行筛选；最后才去执行SELECT子句。所以，当我们去执行WHERE子句时，还没有执行SELECT子句，此时还没有给列起别名，所以在WHERE子句中不知道s是什么，所以会报错。



# 运算符

> **字符串存在`隐式转换`，如果用来与数值进行运算，则会将其转换成数值，若转换不成功，则转换成数值0。**
>
> **如果字符串与字符串进行运算，则不会进行隐式转换，而是比较每个字符串中字符的ANSI编码是否相等。**

## 1、算术运算符

算术运算符主要用于数学运算，其可以连接运算符前后的两个数值或者表达式，对数值或者表达式进行加（+）、减（-）、乘（*）、除（/）和取模（%）运算。

| 运算符       | 名称               | 作用                     | 示例                                   |
| ------------ | ------------------ | ------------------------ | -------------------------------------- |
| **`+`**      | 加法运算符         | 计算两个值或表达式的和   | SELECT A + B                           |
| **`-`**      | 减法运算符         | 计算两个值或表达式的差   | SELECT A - B                           |
| **`*`**      | 乘法运算符         | 计算两个值或表达式的乘积 | SELECT A * B                           |
| **`/或DIV`** | 除法运算符         | 计算两个值或表达式的商   | SELECT A / B<br>或者<br>SELECT A DIV B |
| **`%或MOD`** | 求模（求余）运算符 | 计算两个值或表达式的余数 | SELECT A % B<br>或者<br>SELECT A MOD B |

### 1.1、加法与减法运算符

```sql
SELECT 100, 100 + 0.0, 100 - 0.0
FROM DUAL;
```

![image-20240228230754597](.\images\image-20240228230754597.png)

由查询结果，可以得出如下结论：

> * 一个整数类型的值对浮点数进行加法和减法操作，结果是浮点数。
>
> * 在Java中，+的左右两边如果有字符串，那么表示的是字符串的拼接。但是在**`MySQL中+只表示数值相加`**。**如果遇到非数值类型，先尝试转换成数值（隐式转换），如果转换失败，就按0计算。（MySQL中字符串拼接要使用字符串函数`CONCAT()`实现）**

例：

```sql
SELECT 100 + '1'
FROM DUAL;
```

查询结果：

![image-20240228225806944](.\images\image-20240228225806944.png)

我们可以发现：

在Java中，如果我们使用了100 + '1'，得到的结果实际上是一个字符串：'1001'

但是在MySQL中，+运算符只做数值的相加，会将两边都转换成数值类型，即将'1'字符串转换成了数值1，进行了数值的相加。



在上例中，**`DUAL`**在SQL中表示的是**虚表**，不是表示实际的表格，只是用来输出一条记录的。

在Oracle中，SELECT必须有表名，此时就不能省略FROM xxx，当我们不是从表格中查询的，就需要使用到DUAL来表示一个虚表，表明当前不是从表格中获取到的数据；

但是在MySQL中，查询数据不一定需要表名，可以省略FROM xxx，那么此时就可以不使用DUAL。

### 1.2、乘法与除法运算符

```SQL
SELECT 100, 100 * 1, 100 * 1.0, 100 / 1.0, 100 / 2, 100 / 3, 100 + 2 * 5 / 2, 100 DIV 0
FROM DUAL;
```

查询结果：

![image-20240228231304772](.\images\image-20240228231304772.png)

在上例的100 + 2 * 5 / 2运算中，先计算2 * 5 = 10，再计算10 / 2 = 5.0000（保留四位小数），然后再计算100 + 5.0000 = 105.0000，结果为浮点数，并且保留4位小数。

由查询结果可以得出如下结论：

> * 一个数乘以浮点数和除以浮点数后都会变成浮点数。
> * 一个数除以一个数后，**不管是否能除尽，都会变为一个浮点数**，并保留到小数点后`4位`。
> * 乘法和除法的优先级相同。
> * 在数学运算中，0不能作为除数，在MySQL中，**`一个数除以0得到NULL`**。
> * 浮点数进行运算时，会将浮点数保留的几位小数不变。



### 1.3、求模（求余）运算符

```sql
SELECT 12 % 3, 12 MOD 5, 12 % -5, -12 % 5, -12 % -5
FROM DUAL;
```

查询结果：

![image-20240228232235340](.\images\image-20240228232235340.png)

我们根据查询结果可以得出结论：

> * **`模数（余数）的符号与被模数的符号一致。`**



## 2、比较运算符

比较运算符用来对表达式左边的操作数和右边的操作数进行比较，在MySQL中，**`比较的结果为真则返回1，比较的结果为假则返回0，其他情况则返回null`。**

当我们使用WHERE判断

比较运算符经常被用来作为SELECT查询语句的条件来使用，返回符合条件的结果记录。

### 符号运算符

* `=`：等于运算符
* `< = >`：安全等于运算符
* `<>(!=)`：不等于运算符
* `<`：小于运算符
* `<=`：小于等于运算符
* `>`：大于运算符
* `>=`：大于等于运算符

#### 1、等于=运算符

让我们来看几个例子：

**例1：**

```sql
SELECT 1 = 2, 1 != 2, 1 = '1', 1 = 'a', 0 = 'a','a' = 'a', 'ab' = 'ab', 'a' = 'b'
FROM DUAL;
```

查询结果：

![image-20240229101859737](.\images\image-20240229101859737.png)

为什么出现上面的结果？

> **字符串存在`隐式转换`，如果用来与数值进行运算，则会将其转换成数值，若转换不成功，则转换成数值0。**
>
> **如果字符串与字符串进行运算，则不会进行隐式转换，而是比较每个字符串中字符的`ANSI`编码是否相等。**



**例2：**

```sql
SELECT 1 = NULL, NULL = NULL
FROM DUAL;
```

查询结果：

![image-20240229102222858](.\images\image-20240229102222858.png)

由此可知：

> **`只要是NULL参与运算，结果一定为NULL。`**（当我们需要对NULL进行判断时，使用安全等于运算符<=>进行）



例3：

```sql
SELECT last_name, salary,commission_pct
FROM employees
WHERE commission_pct = NULL;
```

查询结果：

![image-20240229103202780](.\images\image-20240229103202780.png)

employees表中的许多记录的commission_pct字段为NULL，那为什么查询的结果为空呢？

原因实际上和WHERE查询的过程有关：

WHERE查询会去将表中所有记录与条件进行比较，当返回1时表示满足条件，保留数据；当返回0时表示不满足条件，不保留数据。

而在该例中，使用的是NULL进行等于=比较，我们可以知道NULL与任何数据进行运算后得到的结果都为NULL，所以没有查询表中的数据进行比较运算后返回1，返回的结果都为NULL，而WHERE查询只有返回1才会保留数据，所以查询的结果为空。

如果我们需要对NULL进行等于判断时，可以使用安全等于运算符<=>。



#### 2、安全等于\< = \>运算符

安全等于运算符与等于运算符的作用是相似的，**`唯一的区别`**是安全等于< = >可以用来对NULL进行判断：

> * 在两个操作数均为NULL时，其返回值为1，而不为NULL；
> * 当一个操作数为NULL，另一个操作数不为NULL，其返回值为0.

此时，我们就可以使用安全等于运算符来筛选出表中的字段为NULL的数据了：

```sql
SELECT last_name, salary,commission_pct
FROM employees
WHERE commission_pct <=> NULL;
```

查询结果：

![image-20240229104149145](.\images\image-20240229104149145.png)



#### 3、不等于运算符!=

不等于运算符（<>和!=）用于判断两边的数字、字符串或表达式的值是否不相等，如果不相等则返回1，相等则返回0。

不等于运算符不能判断NULL值。

如果两边的值有任意一个为NULL，或两边都为NULL，则结果为NULL。

SQL语句示例代码如下：

```sql
SELECT 1 <> 1, 1 != 2, 'a' != 'b', 'a' != NULL, NULL <> NULL
FROM DUAL;
```

查询结果：

![image-20240229105240446](.\images\image-20240229105240446.png)



### 非符号运算符

* **`IS NULL`**：为空运算符，判断值、字符串或表达式是否为空。
* **`IS NOT NULL`**：不为空运算符，判断值、字符串或表达式是否不为空
* **`LEAST`**：最小值运算符，在多个值中返回最小值
* **`GREATEST`**：最大值运算符，在多个值中返回最大值
* **`BETWEEN AND`**：两值之间的运算符，判断一个值是否在两个值之间
* **`ISNULL`**：为空运算符，判断一个值、字符串或表达式是否为空
* **`IN`**：属于运算符，判断一个值是否为列表中的任意一个值
* **`NOT IN`**：不属于运算符，判断一个值是否不是一个列表中的任意一个值
* **`LIKE`**：模糊匹配运算符，判断一个值是否符合模糊匹配规则
* **`REGEXP`**：正则表达式运算符，判断一个值是否符合正则表达式的规则
* **`RLIKE`**：正则表达式运算符，判断一个值是否符合正则表达式的规则

#### 1、空运算符（IS NULL或ISNULL()）与非空运算符（IS NOT NULL）

**空运算符（`IS NULL`或者`ISNULL()`）**：判断一个值是否为NULL，如果为NULL则返回；否则返回0。

对于ISNULL(expr)来说，其是一个函数，需要将数据或字段传入到函数中进行使用。

**非空运算符（`IS NOT NULL`）**：判断一个值是否不为NULL，如果不为NULL则返回1，否则返回0。

使用示例：

空运算符的使用：

```sql
#查询employees表中commission_pct为null的数据有哪些
#方式一：
SELECT last_name, salary,commission_pct
FROM employees
WHERE commission_pct IS NULL;

#方式二：
SELECT last_name, salary, commission_pct
FROM employees
WHERE ISNULL(commission_pct);
```

非空运算符的使用：

```sql
#查询employees表中commission_pct为null的数据有哪些
SELECT last_name, salary, commission_pct
FROM employees
WHERE commission_pct IS NOT NULL;
```

我们也可以使用NOT运算符，将表示肯定的比较转变为否定，例如：

```sql
SELECT employee_id,commission_pct 
FROM employees 
WHERE NOT ISNULL(commission_pct);
```

这里的WHERE子句，则转变成了判断是否不为空了。



#### 2、最小值运算符（LEAST()）与最大值运算符（GREATEST())

**最小值运算符语法格式为：** **`LEAST(值1, 值2, ..., 值n)`**。其中，值n表示参数列表中有n个值，在有两个或多个参数的情况下，返回最小值。

**最大值运算符语法格式为：`GREATEST(值1, 值2, ..., 值n)`**。其中，n表示参数列表中有n个值。当有两个或多个参数时，返回值为最大值。假如任意一个自变量为NULL，则GREATEST()的返回值为NULL。

LEAST()与GREATEST()用来比较数值时，比较的大小。

LEAST()与GREATEST()运算符用来比较字符串时，会去逐个地比较字符串中字符的ANSI值，遇到小或大的就保留。

案例：

```sql
#查询employees表中名与字中较小的信息，并查询名与字中长度较小的字段的长度
SELECT LEAST(first_name, last_name), LEAST(LENGTH(first_name),LENGTH(last_name))
FROM employees;
```

打印结果：

![image-20240229122633863](.\images\image-20240229122633863.png)



#### 3、BETWEEN AND运算符

BETWEEN AND运算符使用的格式通常为

```sql
SELECT D FROM TABLE WHERE C BETWEEN A ADN B
```

此时，当C**大于等于**A，并且C**小于等于**B时，结果为1；否则结果为0。

即，当C的集合范围为[A, B]时，返回为1，A与B都是一个具体的值，而不是值的集合。

案例：

```sql
#查询工资在10000到20000之间的员工信息
SELECT last_name, salary
FROM employees
WHERE salary BETWEEN 10000 AND 20000;

#查询工资不在10000到20000之间的员工信息
SELECT last_name, salary
FROM employees
WHERE salary NOT BETWEEN 10000 AND 20000;
```

查询结果：

![image-20240229123641326](.\images\image-20240229123641326.png)

![image-20240229123704564](.\images\image-20240229123704564.png)



#### 4、IN运算符与NOT IN运算符

**`IN()`运算符**用于判断给定的值是否是IN列表中的一个值，如果是则返回1，否则返回0.如果给定的值为NULL，或者IN列表中存在NULL，则结果为NULL。

**`NOT IN()`运算**符用于判断给定的值是否不是IN列表中的一个值，如果不是IN列表中的一个值，则返回1，否则返回0.

案例：

查询部门为10，20，30部门的员工信息

```sql
SELECT *
FROM employees
WHERE depart_id IN(10, 20, 30);
```

查询工资不是6000,7000,8000的员工信息

```sql
SELECT *
FROM employees
WHERE salary NOT IN(6000, 7000, 8000);
```



#### 5、LIKE运算符

LIKE运算符主要用来匹配字符串，通常用于**模糊匹配**。

如果满足条件则返回1，否则返回0。如果给定给定值或者匹配条件为NULL，则返回结果为NULL。

LIKE运算符通常使用如下**通配符**：

```
%：匹配0或多个字符。
_：只匹配一个字符。
```

**案例**：

查询last_name中包含字符'a'的员工信息

```sql
SELECT *
FROM employees
WHERE last_name LIKE '%a%';
```

查询结果：

![image-20240229125135048](.\images\image-20240229125135048.png)



##### **`ESCAPE：转义符`**

使用转义符，**回避特殊符号**。

比如在LIKE运算中，%与_是通配符，但当我们要去查询字符串中包含%或\_字符时，而不是将其作为通配符使用时，就需要使用到转义符，类似于Java中的\符号。

**转义符有两种方式：**

> 1. 使用"`\`"表示转义符，类似于Java，直接在字符串中加上即可，表示后续的第一个字符没有其他含义。
> 2. 使用`ESCAPE`来指定转义符，一般使用$。表示在字符串中，指定的转义符后续的第一个字符没有特殊含义。

案例：

查询表中first_name字段的第二个字符为_的员工信息。

```sql
#方式一：使用\转义符，使用\转义符不能使用ESCAPE
SELECT *
FROM employees
WHERE first_name LIKE '_\_%';

#方式二：使用ESCAPE指定转义符
SELECT *
FROM employees
WHERE first_name LIKE '_$_%' ESCAPE '$';
```

查询结果：

![image-20240229130822921](.\images\image-20240229130822921.png)



#### 6、REGEXP运算符

REGEXP运算符用来匹配字符，**语法格式为：`expr REGEXP 匹配条件`**。

如果expr满足匹配条件，返回1；如果不满足，则返回0。若expr或匹配条件任意一个为NULL，则结果为NULL。

REGEXP运算符在进行匹配时，常用的有下面几种通配符：

> 1. '^'匹配以该字符后面的字符开头的字符串。
> 2. '$'匹配以该字符前面的字符结尾的字符串。
> 3. '.'匹配任何一个单字符。
> 4. "[...]"匹配在方括号内的任何字符。例如："[abc]"匹配'a'或'b'或'c'。为了命名字符的范围，使用一个'-'。比如"[a-z]"匹配任何字母，而'[0-9]'匹配任何数字。
> 5. '*'匹配零个或多个在它面前的字符。例如，"x\*"匹配任何数量的'x'字符，"[0-9]\*"匹配任何数量的数字，而"\*"匹配任何数量的任何字符。

案例：

```sql
# ^s表示以s开头的字符串；t$表示以t结尾的字符串；hk表示字符串中包含hk字符
SELECT 'shkstart' REGEXP '^s', 'shkstart' REGEXP 't$', 'shkstart' REGEXP 'hk';
```

![image-20240229140114750](.\images\image-20240229140114750.png)

```sql
# gu.gu表示字符串包含gu.gu，其中.表示任意一个字符；
# [ab]表示字符串中包含a或者b
SELECT 'atguigu' REGEXP 'gu.gu','atguigu' REGEXP '[ab]';
```

![image-20240229140823337](.\images\image-20240229140823337.png)



## 3、逻辑运算符

逻辑运算符主要用来判断表达式的真假，在MySQL中，逻辑运算符的返回结果为1,0或者NULL。

MySQL支持4种逻辑运算符：

| 运算符          | 作用     | 示例                             |
| --------------- | -------- | -------------------------------- |
| **`NOT 或 !`**  | 逻辑非   | SELECT NOT A                     |
| **`AND 或 &&`** | 逻辑与   | SELECT A AND B<br>SELECT A && B  |
| **`OR 或 ||`**  | 逻辑或   | SELECT A OR B<br>SELECT A \|\| B |
| **`XOR`**       | 逻辑异或 | SELECT A XOR B                   |

#### XOR逻辑异或运算符

**`逻辑异或（XOR）`**运算符是当给定的值中任意一个值为NULL，则返回NULL；如果两个非NULL的值都是0或者都不等于0时，则返回0；如果一个值为0，另一个值不为0时，则返回1。

这个运算符是去筛选只满足一个条件的信息。
例如：SELECT * FROM employees WHERE A XOR B。

相当于：SELECT * FROM employees WHERE (A AND -B) OR (-A AND B)。

是去employees表中筛选出满足A不满足B或者满足B不满足A条件的信息。

```sql
SELECT 1 XOR -1, 1 XOR 0, 0 XOR 0, 1 XOR NULL, 1 XOR 1
FROM DUAL;
```

打印结果：

![image-20240229154217063](.\images\image-20240229154217063.png)



### 4、位运算符（使用的较少）

位运算符是在二进制数上进行计算的运算符。位运算符会现将操作数变成二进制数，然后进行位运算，最后将计算结果从二进制变为十进制。

实际上位运算符，是将数值的二进制补码之间进行位运算，**`正数的补码是其本身，负数的补码是其绝对值按位取反后末位加1所得`**。

MySQL支持的位运算符如下：

* **`&`**：按位与。
* **`|`**：按位或
* **`^`**：按位异或
* **`~`**：按位取反
* **`>>`**:按位右移
* **`<<`**：按位左移

#### 4.1、按位与运算符

按位与（&）运算符将给定值对应的二进制数逐位进行逻辑与运算。当给定值对应的二进制位的数值都为1时，则该位返回1，否则返回0.

```sql
SELECT 10 & -1;
```

查询结果：

![image-20240229160604973](.\images\image-20240229160604973.png)

为什么有这样的结果：

10的补码与-1的补码分别为：

0000 1010

1111 1111

两个按位与运算，得到：

0000 1010，即结果为10.



#### 4.2、按位或运算

按位或（|）运算符将给定的值对应的二进制数逐位进行逻辑或运算。

**当给定值对应的二进制位的数值有一个或两个为1时，则该位返回1，否则返回0。**

```sql
SELECT 1 | 10, 20 | 30;
```

查询结果：

![image-20240229164328647](.\images\image-20240229164328647.png)

1的补码是0000 0001,10的补码是0000 1010，所以按位或运算的结果为0000 1011，即为11.

20的补码是0001 0100,30的补码为0001 1110，所以按位或运算的结果为0001 1110，即为30。

#### 4.3、按位异或运算符

按位异或（^）运算读将给定的值对应的二进制数逐位进行逻辑异或运算。

**当给定值对应的二进制位的数值不同时，则该位返回1，否则返回0。**

```sql
SELECT 1 ^ 10, 20 ^ 30;
```

查询结果为：

![image-20240229164936023](.\images\image-20240229164936023.png)

1的二进制数为0000 0001,10的二进制数补码为0000 1010，所以1 

#### 4.4、按位取反运算符

按位取反（~）运算符将给定的值的二进制数逐位进行取反操作，即将1变为0，将0变为1.

例如：

```sql
SELECT 10 & ~1;
```

![image-20240229180325103](.\images\image-20240229180325103.png)

由于按位取反（~）运算符的优先级高于按位与（&）运算符的优先级，所以10 & ~1，首先，对数字1进行按位取反操作，即0000 0001进行取反得到1111 1110，然后与10进行按位与操作，即与0000 1010进行按位与，得到0000 1010，即结果为10。

#### 4.5、按位左移、右移运算符

**按位右移（>>）**

将给定的值的二进制的所有位右移指定的位数。

右移指定的位数后，右边低位数的数值被移出并丢弃，**左边高位空出的位置用0补齐**。

例如：

```sql
SELECT 1 >> 2, 4 >> 2;
```

查询结果：

![image-20240229180913485](.\images\image-20240229180913485.png)

1的补码是0000 0001，向右移动两位，空出的位置使用0补齐，得到的结果是0000 0000，即为0.

4的补码为0000 0100，向右移动两位，得到0000 0001，即即为1。



**按位左移（<<）**

按位左移（<<）运算符将给定的值的二进制的所有位左移指定的位数。

左移指定的位数后，左边高位的数值被移出并丢弃，**右边低位空出的位置用0补齐**。

例如：

```sql
SELECT 1 << 2, 4 << 2;
```

查询结果：

![image-20240229181306842](.\images\image-20240229181306842.png)

1补码0000 0001，左移两位得到0000 0100，即结果为4

4补码0000 0100，左移两位得到0001 0000，即结果为16



但是注意，如果移动如果超出了范围，比如0000 1000左移了5位，超出了数据所占用的长度，此时得到的结果是0000 0000，即为0。

即：

> **`在一定范围内`，满足：每向左移动一位，相当于乘以2；每向右移动一位，相当于除以2。**



# 排序与分页

## 1、排序

如果不进行排序，使用默认的顺序，那么在默认情况下，查询到的结果就是实际上往数据库中添加的顺序。

### 1.1、排序规则

* 使用**ORDER BY**子句进行排序

  * **`ASC（ascend）`：升序，由小到大**
  * **`DESC（descend）`：降序，由大到小**

  **当我们未指明升序还是降序时，默认按照`ASC`升序排列。**

案例1：按照salary从高到低的顺序显示员工信息

```sql
SELECT employee_id, last_name, salary
FROM employees
ORDER BY salary DESC;
```

查询结果：

![image-20240229210131597](.\images\image-20240229210131597.png)



案例2：我们可以使用列的别名，进行排序。

比如：按照年工资升序进行排序

```sql
SELECT employee_id, salary, salary * 12 AS "annual_sal"
FROM employees
ORDER BY annual_sal;
```

上面的ORDER BY子句中，使用了列的别名。

但是注意！

> **列的别名不能在`WHERE`子句中使用**

原因：先去执行FROM子句，获取表信息；然后去执行WHERE子句，进行筛选；最后才去执行SELECT子句。所以，当我们去执行WHERE子句时，还没有执行SELECT子句，此时还没有给列起别名，所以在WHERE子句中不知道s是什么，所以会报错。



查询都会将所有的字段全部查出来，然后再进行排序，最后才筛选出要查询的字段。即：SELECT子句选择查询的信息，不会影响排序的结果，无论是否要查询该字段，都可以使用该字段进行排序。

比如:SELECT first_name FROM employees ORDER BY last_name;

上述虽然查询的结果中不包含last_name字段，但是依然可以使用last_name进行查询，因为是先去查询所有字段信息，然后进行排序，最后才会筛选出要查询的字段。



### 1.2、单列排序与多列排序

单列排序，就是只根据某一个字段的信息进行排序；

多列排序，是根据两个或两个以上的字段信息进行排序的。即，当第一个字段的信息相等时，就会根据后面字段的信息，逐级进行排序。

案例：显示员工信息，按照department_id的降序排列，salary的升序排列

```sql
SELECT employee_id, salary, department_id
FROM employees
ORDER BY department_id DESC, salary ASC
```

查询结果：

![image-20240229214708233](.\images\image-20240229214708233.png)

**注意点：**

* 可以使用不在SELECT列表中的列排序。（与SQL的执行顺序有关）
* 在对多列进行排序的时候，首先排序的第一列必须有相同的列值，才会对第二列进行排序。如果第一列数据中所有值都是唯一的，将不再对第二列进行排序。



## 2、分页

### 2.1、背景

背景1：查询返回的记录太多了，查看起来很不方便，怎么样能够实现分页查询呢？

背景2：表里有4条数据，我们只想要显示第2、3条数据怎么办？

### 2.2、实现规则

* **分页原理**

  所谓分页显示，就是将数据库中的结果集，一段一段显示出来需要的条件。

* MySQL中使用**`LIMIT`**实现分页。

* **格式：**

  ```sql
  LIMIT [位置偏移量,] 行数
  ```

  第一个“位置偏移量”参数指示MySQL从哪一行开始显示，是一个可选参数，如果不指定“位置偏移量”，将会从表中的第一条记录开始（第一条记录的位置偏移量是0，第二条记录的位置偏移量是1，以此类推）；第二个参数“行数”指示返回的记录条数。

* 举例：

  ```sql
  -- 前10条记录：
  SELECT * FROM 表名 LIMIT 0,10;
  #或者
  SELECT * FROM 表名 LIMIT 10;
  
  -- 第11到第20条记录：
  SELECT * FROM 表名 LIMIT 10,10;
  
  -- 第21到第30条记录：
  SELECT * FROM 表名 LIMIT 20,10;
  ```

  > 在MySQL8以后，可以使用"`LIMIT 3 OFFSET 4`"，和"LIMIT 4,3"返回的结果相同，意思是从第5条记录开始后面的3条记录。

  在实际的开发中，分页可以用来显示指定数量的信息；更多地，是由于数据过多，从而用来对数据进行分页显示。此时就会使用到分页显示的公式：

* **分页显示公式**：**`LIMIT (当前页数-1)*每页条数, 每页条数`**

  这个公式在实际开发中，应用频繁，当我们需要对数据进行分页显示，从而减少一次性获取的数据，从而提高查询效率时，就会使用到分页显示公式。

  例：查询员工信息，要求对查询到的信息进行分页显示，每页15条数据，当前页为第三页

  ```sql
  SELECT *
  FROM employees
  #LIMIT (PageNo - 1) * PageSize, PageSize
  LIMIT 30, 15
  ```

* **使用LIMIT分页的好处：**

  约束返回结果的数量可以`减少数据表的网络传输量`，也可以`提升查询效率`。如果我们知道返回结果只有一条，就可以使用`LIMIT 1`，告诉SELECT语句只需要返回一条记录即可。这样的好处就是SELECT不需要扫描完整的表，只需要检索到一条符合条件的记录即可返回。



### 2.3、扩展

在不同的DBMS中使用的关键字可能不同。在MySQL、PostgreSQL、MariaDB和SQLite中使用LIMIT关键字，而且需要放到SELECT语句的最后面。

下例中，都是各个DBMS取出前5条数据的SQL语句。

* 如果是SQL Server和Access，需要使用`TOP`关键字，比如：

  ```sql
  SELECT TOP 5 name, hp_max 
  FROM heros 
  ORDER BY hp_max DESC
  ```

* 如果是DB2，使用`FETCH FIRST 5 ROWS ONLY`这样的关键字：

  ```sql
  SELECT name, hp_max 
  FROM heros 
  ORDER BY hp_max DESC
  FETCH FIRST 5 ROWS ONLY;
  ```

* 对于Oracle来说，需要使用到Oracle数据库中特有的字段：`rownum`。在Oracle中，会给每个表中都添加一个隐藏的字段rownum，这个字段记录着当前行信息属于第几个。

  ```sql
  SELECT rownum, last_name, salary
  FROM employees
  WHERE rownum < 5
  ORDER BY salary DESC;
  ```

  需要说明的是，根据SQL的执行顺序，这段SQL语句是先取出前5条数据行，然后再按照hp_max从高到低的顺序进行排序。这样产生的结果与上述两种不一样，这里可以使用子查询的方式：

  ```sql
  SELECT rownum, last_name, salary
  FROM (
  	SELECT last_name, salary
  	FROM employees
  	ORDER BY salary DESC
  )
  WHERE rownum < 5;
  ```

  得到与上述两种数据库一致的结果。



# 多表查询

多表查询，也称为关联查询，指两个或多个表一起完成查询操作。

前提条件：这些一起查询的表之间是有关系的（一对一、一对多），它们之间一定是有关联字段，这个关联字段可能建立了外键，也可能没有建立外键。比如：员工表和部门表，这两个表依靠“部门编号”进行关联。

## 笛卡尔积的说明

案例：

```sql
SELECT employee_id, department_name
FROM employees, departments;
```

查询结果：

![image-20240301105242689](.\images\image-20240301105242689.png)

在上例中，employees表一共有107条数据，departments表中一共有27条数据，此时一共可以查询到2889条数据

为什么会出现这种情况？

原因是不清楚连接方式，employees表中的每一条数据都与departments表中的每一条数据连接了一遍。此时两个表employees与departments之间会使用**笛卡尔积**的方式进行连接，从而造成表A的每一条数据与表B的每一条数据都进行连接，造成了一共会有A的信息数107 * B的信息数27 = 查询到表的信息数，也就是2889条数据。

### 什么是笛卡尔积？

笛卡尔积是一个数学运算。假设我有两个集合X和Y，那么X和Y的笛卡尔就是X和Y的所有可能组合，也就是第一个对象来自于X，第二个对象来自于Y的所有可能。组合的个数即为两个集合中元素个数的乘积数。

![image-20240301143314452](.\images\image-20240301143314452.png)

SQL92中，笛卡尔积也称为交叉连接，英文是CROSS JOIN。在SQL99中也是使用CROSS JOIN表示交叉连接。它的作用就是可以把任意表进行连接，即使这两张表不相关。在MySQL中如下情况会出现笛卡尔积：

```sql
#查询员工姓名和所在部门名称
SELECT last_name, department_name FROM employees, departments;
SELECT last_name, department_name FROM employees CROSS JOIN departments;
SELECT last_name, department_name FROM employees INNER JOIN departments;
SELECT last_name, department_name FROM employees JOIN departments;
```

错误的原因：缺少了多表的连接条件

当我们没有连接条件，而去同时查询两个表时，此时就会将表A的所有信息与表B的所有信息进行结合，出现笛卡尔积错误。

### 笛卡尔积错误解决

* **`笛卡尔积的错误会在下面条件下产生：`**

  * 省略多个表的连接条件（或关联条件）
  * 连接条件（或关联条件）无效
  * 所有表中的所有行互相连接

* 为了避免笛卡尔积，可以**`在WHERE加入有效的连接条件`**。

* 加入连接条件后，查询语法：

  ```sql
  SELECT table1.column, table2.column
  FROM table1, table2
  WHERE table1.column1 = table2.column2;#连接条件
  ```

* 案例：查询员工的姓名及其所属的部门名称

  ```sql
  SELECT last_name, department_name
  FROM employees, departments
  WHERE employees.department_id = departments.department_id;
  ```

查询结果：

![image-20240302101649455](.\images\image-20240302101649455.png)

为什么只查出了106条数据，而在employees表中明明有107条数据？

原因在与employees表中有一条数据的department_id为NULL，=运算符左右两边有一个为NULL，返回的结果就是NULL，而WHERE子句筛选的条件是返回结果为1，所以employees表中那一条department_id为NULL的数据是不满足条件的，也就会被筛选掉了，所以查询不到。

那假如，我想要把employees表中的所有数据都显示出来，该如何操作呢？这里就会用到后面的**外连接**。



### 扩展1：区分重复的列名

* **`多个表中有相同的列时，必须在列名之前加上表名前缀。`**
* 在不同表中具有相同列名的列可以用`表名`加以区分。

```sql
SELECT employees.last_name, departments.department_name, employees.department_id
FROM employees, departments
WHERE employees.department_id = departments.department_id;
```



### 扩展2：表的别名

* 使用别名可以简化查询。
* 列名前使用表名前缀可以提高查询效率。

```sql
SELECT e.employee_id, e.last_name, e.department_id, d.department_id, d.location_id
FROM employee e, department d
WHERE e.department_id = d.department_id;
```

> 需要注意的是，如果我们使用了表的别名，在查询字段中、过滤条件中就只能使用别名进行代替，不能使用原有的表名，否则就会报错。

> `阿里开发规范：`
>
> 【`强制`】对于数据库中表记录的查询和变更，只要涉及多个表，都需要在列名前加表的别名（或表名）进行限定。
>
> `说明`：对多表进行查询记录、更新记录、删除记录时，如果对操作列没有限定表的别名（或表名），并且操作列在多个表中存在时，就会抛异常。
>
> 正例：SELECT t1.name FROM table_first as t1, table_second as t2 WHERE t1.id = t2.id;
>
> 反例：在某业务中，由于多表关联查询语句没有加表的别名（或表名）限制，后续在表中增加一个同名字段，在颁布环境做数据库变更后，线上查询语句出现1052异常：Column 'name' in field list is ambiguous;



### 扩展3：连接多个表

当我们要去查询多个表中的数据时，可以连接多个表，连接多个表的条件使用`AND`连接。

**`连接n个表，至少需要n-1个连接条件。`**

案例：查询出公司员工的last_name，department_name,city信息

```sql
SELECT emp.employee_id, emp.last_name, dep.department_name, locat.city
FROM employees emp, departments dep, locations locat
WHERE emp.department_id = dep.department_id AND dep.location_id = locat.location_id
```

![image-20240302111150828](.\images\image-20240302111150828.png)

## 对等值连接与非等值连接的说明





## 对自连接与非自连接的说明





## 内连接与外连接













# 单行函数









# 聚合函数











# 子查询



