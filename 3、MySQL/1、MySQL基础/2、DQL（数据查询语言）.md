[TOC]



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
...
ORDER BY <字段名>
LIMIT <限制行数>;
```

# **SELECT查询执行的顺序**：

![SQL查询语句的执行顺序解析](.\images\format,png)

1. FROM
2. ON
3. JOIN
4. WHERE
5. GROUP BY
6. 聚合函数（avg,sum...）
7. HAVING
8. SELECT
9. DISTINCT
10. ORDER BY
11. LIMIT

结合上图，整理出如下伪SQL查询语句：

```sql
(9) SELECT (10) DISTINCT column, (6) AGG_FUNC(column or expression),...
(1) FROM left_table
	(3) JOIN right_table
	(2) ON column1 = column2
(4) WHERE constraint_expression
(5) GROUP BY column
(7) WITH CUBE|ROLLUP
(8) HAVING constraint_expression
(11) ORDER BY column ASC|DESC
(12) LIMIT (PageNum-1)*PageSize, PageSize;
```

从这个顺序中我们可以发现，所有的查询语句都是从FROM开始执行的，在实际执行过程中，每个步骤都会为下一个步骤生成一个**虚拟表**，这个虚拟表将作为下一个执行步骤的输入。接下来，我们详细的介绍下每个步骤的具体执行过程。

## 1、FROM 执行笛卡尔积

FROM才是SQL语句执行的第一步，并非SELECT。对FROM子句中的前两个表执行笛卡尔积（交叉连接），生成虚拟表**VT1**，获取不同数据源的数据集。

这一步实际上已经获取到了所有的表，包括JOIN子句中的表，然后对这些表执行笛卡尔积操作，即：所有字段都进行了匹配。

**FROM子句执行顺序为从后往前、从右往左**，FROM子句中写在最后的表（基础表——将被最先处理，即最后的表为驱动表，当FROM子句中包含多个表的情况下，我们需要选择数据最少的表作为基础表。

## 2、ON 启用ON过滤器

对虚拟表**VT1**启用ON筛选器，ON中的逻辑表达式将应用到虚拟表**VT1**中的各个行，筛选出满足ON逻辑表达式的行，生成虚拟表**VT2**.

## 3、JOIN 添加外部行

保留表如下：

* LEFT JOIN把左表记为保留表
* RIGHT JOIN把右表记为保留表
* FULL JOIN把左右表都作为保留表

在虚拟表**VT2**表的基础上添加保留表中被过滤条件过滤掉的数据，非保留表中的数据被赋予NULL值，最后生成虚拟表**VT3**。

如果有两个以上的表连接，则对上一个连接生成的结果集和下一个表重复执行步骤1~3，直到处理完所有的表为止。

## 4、WHERE 应用WHERE过滤器

对虚拟表**VT3**应用WHERE筛选器。根据指定的条件对数据进行筛选，并把满足的数据插入虚拟表**VT4**。

* 由于此时还未执行聚合函数，所以不能在WHERE过滤器中使用聚合函数进行筛选操作。
* 由于此时还未执行SELECT操作，因此不能在WHERE过滤器中使用列的别名。

## 5、GROUP BY 分组

按GROUP BY子句中的列/列表将虚拟表**VT4**中的行唯一的值组合成为一组，生成虚拟表**VT5**。如果应用了GROUP BY，那么后面只能得到GROUP BY的列（比如GROUP BY department_id，那么也只能查询得到department_id数据）或者聚合函数（比如count、sum、avg等），原因在于最后的结果集中只为每个组包含一行。

同时，从这一步开始，后面的语句中都可以使用SELECT中列的别名。

## 6、AGG_FUNC 计算聚合函数

计算max等聚合函数。SQL Aggregate函数计算从列中取得的值，返回一个单一的值。常用的聚合函数包含以下几种：

* AVG：平均值
* COUNT：行数
* FIRST：返回第一个记录的值
* LAST：返回最后一个记录的值
* MAX：返回最大值
* MIN：返回最小值
* SUM：返回总和

## 7、WITH 应用ROLLUP或CUBE

对虚拟表**VT5**应用ROLLUP或CUBE选项，生成虚拟表**VT6**.

CUBE和ROLLUP区别如下：

* CUBE生成的结果数据集显示了所选列中值的所有组合的聚合。
* ROLLUP生成的结果数据集显示了所选列中值的某一层次结果的聚合。

## 8、HAVING 应用HAVING过滤器

对虚拟表**VT6**应用HAVING筛选器。根据指定的条件对数据进行筛选，并把满足的数据插入虚拟表**VT7**。

HAVING语句在SQL语句中的主要作用是与WHERE语句作用是相同的，但是HAVING用于过滤聚合值，在SQL中添加HAVING子句原因就在于，WHERE无法与聚合函数一起使用，HAVING子句主要和GROUP BY子句配合使用。

注意：如果既能使用WHERE，也能使用HAVING，推荐使用WHERE，HAVING推荐只与聚合函数一起使用。

## 9、SELECT 选出指定列

将虚拟表**VT7**中的在SELECT中出现的列筛选出来，并对字段进行处理，计算SELECT子句中的表达式，产生虚拟表**VT8**.

## 10、DISTINCT 行去重

将重复的行从虚拟表**VT8**中移除，产生虚拟表**VT9**。DISTINCT用来删除重复行，只保留唯一的。

## 11、ORDER BY 排列

将虚拟表**VT9**中的行按ORDER BY子句中的列/列表排序，生成游标**VC10**，注意不是虚拟表。因此，使用ORDER BY子句查询不能应用于表达式。同时，ORDER BY子句的执行顺序为从左到右排序，是非常消耗资源的。

## 12、LIMIT/OFFSET 指定返回行

从**VC10**的开始处选择指定数量行，生成虚拟表**VT11**，并返回调用者。



## 执行实例

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



# 一、基本的SELECT语句

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



# 二、运算符

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



## 4、位运算符（使用的较少）

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



# 三、排序与分页

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



# 四、多表查询

多表查询，也称为关联查询，指两个或多个表一起完成查询操作。

前提条件：这些一起查询的表之间是有关系的（一对一、一对多），它们之间一定是有关联字段，这个关联字段可能建立了外键，也可能没有建立外键。比如：员工表和部门表，这两个表依靠“部门编号”进行关联。

## 1、笛卡尔积的说明

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

## 2、多表查询的分类

* **角度一：等值连接 vs 非等值连接**
* **角度二：自连接 vs 非自连接**
* **角度三：内连接 vs 外连接**

### 2.1、等值连接 vs 非等值连接

等值连接，意思就是表之间的连接关系是使用=进行连接的。

非等值连接就是与之相反，不是使用=进行连接。

之前的例子都是使用等值连接进行的，这里举一个非等值连接的例子：

有一个表job_grades，这个表是工作等级表，根据等级有一个工资的范围：

```sql
SELECT *
FROM job_grades;
```

![image-20240302131122922](.\images\image-20240302131122922.png)

可以看到，这里每一个等级都有一个范围。现在有一个需求，根据员工的工资，查询出员工所属的等级，这里就会使用到非等值连接的多表查询：

```sql
SELECT last_name, salary, grade_level
FROM employees e, job_grades j
WHERE e.salary BETWEEN j.lowest_sal AND j.highest_sal;
```

查询结果：

![image-20240302131759756](.\images\image-20240302131759756.png)

### 2.2、自连接 vs 非自连接

什么是自连接？什么是非自连接？

自连接，意思就是自己连接自己。

非自连接，意思就是自己表连接其他表。

之前的案例全都是非自连接，即不同的表之间进行连接查询的。

但是，如果是需要将一张表上的信息，分别列出两套不同的信息时，就需要使用到自连接。

案例：查询员工id，员工姓名及其管理者的id和姓名

在employees表中，既有员工id信息employee_id，员工姓名last_name,也有其管理者的id信息manager_id，那么，我们就可以根据manager_id查询出其管理者的姓名，也是在同一张表上的last_name。可以将employees作为两张表，一个表的manager_id连接上另一张表的employee_id，就可以查询出管理者的last_name了。

```sql
SELECT emp.employee_id, emp.last_name, emp.manager_id,man.last_name as manager_name
FROM employees as emp, employees as man
WHERE emp.manager_id = man.employee_id;
```

查询结果：

![image-20240302133024560](.\images\image-20240302133024560.png)



### 2.3、内连接与外连接

（这种很重要，请查看后面的笔记）



## 3、内连接与外连接

### 什么是内连接，什么是外连接？

* **内连接**：把两个表中满足的数据查询出来，合并具有同一列的两个以上的表的行，**`结果集中不包含一个表与另一个表不匹配的行`**

* **外连接**：两个表在连接过程中除了返回满足条件的行以外，**`还返回左（或右）表中不满足条件的行，这种连接成为左（或右）外连接`**。没有匹配的行时，结果表中相应的列为空（NULL）。

* **外连接分类**：`左外连接`、`右外连接`、`满外连接`

* 如果是左外连接，则连接条件中左边的表也称为`主表`，右边的表称为`从表`。

  如果是右外连接，则连接条件中右边的表也称为`主表`，左边的表称为`从表`。

  > 内连接与外连接的**区别**就在于：
  >
  > 内连接只会查询出多个表中满足匹配条件（ON条件）的数据；
  >
  > 外连接会将主表中不满足匹配条件（ON条件）的数据都返回。

  虽然外连接会返回主表中不匹配条件的数据，但是如果还有其他的筛选条件，且这些条件会将不匹配条件的数据去除，那么实际结果集中还是不会包含这些数据。

我们之前所学习的：

```sql
SELECT employee_id, department_name
FROM employees, departments
WHERE employees.department_id = departments.department_id
```

都是内连接。

即，当employees表或depatments表中有不满足条件的数据时（employees.department_id = departments.department_id），此时就会把信息筛选掉，结果集中不会包含。

比如，当employees表中有一个数据的department_id为NULL：

![image-20240302142923799](.\images\image-20240302142923799.png)

此时，由于是内连接，且WHERE子句的筛选条件是返回结果为1，但由于=等于运算符左右两边有一个为NULL，所以返回结果也为NULL，此时不满足条件，就会在结果集中筛选掉该数据，所以该条数据不会查询到。

上述的案例使用的是`SQL92`的语法。

但，如果我们想在结果集中显示这条数据该怎么办？

这个时候就需要使用到外连接。

将employees表作为主表，让主表的数据都显示，若从表中没有匹配的数据，该条数据对应的从表的查询结果为NULL。



### SQL99语法实现多表查询（不推荐SQL92语法）

#### 1、基本语法

* 使用**`[INNER/LEFT/RIGHT] JOIN...ON`**子句创建连接的语法结构：

  ```sql
  SELECT table1.column, table2.column, table3.column
  FROM table1
  	[INNER/LEFT/RIGHT] JOIN table2 ON tabl1和table的连接条件
  		[INNER/LEFT/RIGHT] JOIN table3 ON table2和table3的连接条件
  ```

  SQL99采用的这种嵌套结构非常清爽、层次性更强、可读性更强，即使再多的表进行连接也都清晰可见。

* **语法说明：**

  * 可以使用ON子句指定额外的连接条件。
  * 这个连接条件是与其他条件分开的。
  * ON子句使语句具有更高的可读性。
  * 关键字JOIN、INNER JOIN、CROSS JOIN的含义是一样的，都表示内连接。

#### 2、内连接（INNER JOIN）的实现

> **`内连接的结果 = 左右表匹配的数据`**

* **语法：**

  ```sql
  SELECT 字段条件
  FROM A表 
  INNER JOIN B表
  ON 关联条件
  WHERE等其他子句;
  ```

* 案例：查询员工的姓名，部门名和城市名

  ```sql
  SELECT e.last_name, d.department_name, l.city
  FROM employees as e
  INNER JOIN departments as d
  ON e.department_id = d.department_id
  INNER JOIN locations as l
  ON d.location_id = l.location_id
  ```

  查询结果：

  ![image-20240302152532042](.\images\image-20240302152532042.png)



#### `3、外连接（OUTER JOIN）的实现`

OUTER可以省略。

对于外连接而言，主表若有数据不满足匹配条件，在结果集中依然可以得到一条数据结果；但是主表中只要这一条数据满足了匹配条件，已经查询得到一次了，就不会因为不满足匹配条件而出现。

即，**`主表中的数据至少会出现一次`**。也可能会因为满足匹配条件而重复出现。

如部门表与员工表，部门表作为主表，此时部门表中的部门信息可能会因为该部门有多个员工而多次出现，因为员工表中的信息较多，所以查询的结果的数据量肯定是与员工表信息数量相差无几。而外连接仅仅只是保证了，主表中的所有数据都会查询得到，不能保证数据量与主表相差无几。即只能保证部门表中的所有部门信息都可以查出来，就算不满足匹配条件。

##### 3.1、左外连接（LEFT OUTER JOIN）

此时，左表是主表，右表是从表。

> **`左外连接的结果 = 左右表匹配的数据 + 左表没有匹配到的数据`**

* **语法：**

  ```sql
  SELECT 字段列表
  FROM A表
  LEFT JOIN B表
  ON 关联条件
  WHERE等其他子句
  ```

* 案例：查询所有员工的last_name，department_name的信息

  ```sql
  SELECT last_name, department_name
  FROM employees e 
  LEFT JOIN departments d
  ON e.department_id = d.department_id
  ```

  查询结果：

  ![image-20240302153237574](.\images\image-20240302153237574.png)

这里使用的是左外连接。employees表中的Grant信息的department_id是空NULL，实际上是不满足两表匹配条件e.department_id = d.department_id的，但是因为employees表是主表，departments表是从表，所以虽然Grant信息不满足匹配条件，但是依然会保留下来，这就是外连接。



##### 3.2、右外连接（RIGHT OUTER JOIN）

此时，右表是主表，左表是从表。

> **`右外连接的结果 = 左右表匹配的数据 + 右表没有匹配到的数据`**

* **语法**：

  ```sql
  SELECT 字段列表
  FROM A表
  RIGHT JOIN B表
  ON 关联条件
  WHERE等其他子句;
  ```

* 举例：

  ```sql
  SELECT e.last_name, d.department_id, d.department_name
  FROM employees e
  RIGHT JOIN departments d
  ON e.department_id = d.department_id;
  ```

  执行结果：

  ![image-20240302154722314](.\images\image-20240302154722314.png)

  这里查询的结果，会将employees表与departments表中满足匹配条件e.department_id = d.department_id的数据查询出来，并且若在departments表中包含数据不匹配条件，也在结果集中被返回。比如上例结果中返回的Treasury，其就是在employees表中没有department_id匹配。



##### 3.3、满外连接（FULL OUTER JOIN）

* 满外连接的结果 = 左右表匹配的数据 + 左表没有匹配到的数据 + 右表没有匹配得到的数据。
* SQL99是支持满外连接的，使用FULL JOIN或FULL OUTER JOIN来实现。
* 需要注意的是，MySQL不支持FULL JOIN，但是可以使用LEFT JOIN `UNION` RIGHT JOIN代替。



#### 4、案例分析

**案例1**：查询90号部门员工的job_id和location_id

这涉及到两个表，分别是employees员工表和departments部门表，两个表使用department_id关联。其中，job_id是属于employees表的，location_id是属于departments表的。

那么，显而易见，实现上述需求的SQL代码如下所示：

```sql
SELECT e.job_id, d.location_id
FROM employees e INNER JOIN departments d
ON e.department_id = d.department_id
WHERE e.department_id = 90;
```

查询结果：

![image-20240303224415767](.\images\image-20240303224415767.png)

有一个问题：为什么这里使用INNER JOIN，使用LEFT JOIN或RIGHT JOIN可以吗？

答案其实也是可以的，我们来看看使用左连接的查询结果：

```sql
SELECT e.job_id, d.location_id
FROM employees e LEFT JOIN departments d
ON e.department_id = d.department_id
WHERE e.department_id = 90;
```

![image-20240303225301327](.\images\image-20240303225301327.png)

为什么呢？为什么这里既可以使用INNER JOIN，也能使用LEFT JOIN呢？

原因在于这里的WHERE e.department_id = 90条件。

我们可以知道，外连接的作用，是去将主表中不满足匹配条件的数据也放入结果集中返回。而在这例中，不满足匹配条件的数据是employees表或departments表中department_id为NULL的数据。

但是，在该例中，还有一个筛选条件是WHERE e.department_id = 90，而这个筛选条件，则会帮助我们将两个表中department_id为NULL的数据也去除掉，所以此时是使用内连接还是外连接则不那么重要了，只要两个表使用department_id连接了起来，并且筛选出了department_id为90的数据，就一定得到的是两个表信息都存在的数据。



**案例2**：选择city在Toronto工作的员工的last_name，job_id，department_id，department_name。

这里涉及到三个表：employees表、departments表和locations表。其中，employees表与departments表之间使用department_id进行关联，departments表与locations表之间使用location_id关联。

首先看条件，要求city在Toronto，即locations表中的city字段要求为Toronto。

我们来考虑一下使用内连接还是外连接。首先，因为city为Toronto，则locations表中的city字段一定不为NULL，此时若选择外连接，也会将employees表中因不匹配数据而致使连接的其他表字段为NULL的数据也筛选出去，所以，其实这里使用外连接和内连接的效果一样。

```sql
SELECT e.last_name, e.job_id, d.department_id, d.department_name
FROM employees e
[INNER / LEFT] JOIN departments d
ON e.department_id = d.department_id
[INNER / LEFT] JOIN locations l
ON d.location_id = l.location_id
WHERE l.city = 'Toronto'
```



#### 5、UNION的使用

**`合并查询结果`** 利用UNION关键字，可以给出多条SELECT语句，并将它们的结果组合成单个结果集。

**合并时，两个表对应的列数和数据类型必须相同，并且相互对应**。各个SELECT语句之间使用`UNION`或`UNION ALL`关键字分隔。

**语法格式：**

```sql
SELECT column,...FROM table1
UNION [ALL]
SELECT column,...FROM table2
```

**`UNION操作符`**

![image-20240302165420654](.\images\image-20240302165420654.png)

UNION操作符返回两个查询的结果集的并集，去除重复记录。

**`UNION ALL操作符`**

![image-20240302165511297](.\images\image-20240302165511297.png)

UNION ALL操作符返回两个查询的结果集的并集。对于两个结果集的重复部分，不去重。

> 注意：执行UNION ALL语句时所需要的资源比UNION语句少。如果明确知道合并数据后的结果数据不存在重复数据，或者不需要去除重复的数据，就尽量使用UNION ALL语句，以提高数据查询的效率。

举例：查询部门编号>90或邮箱包含a的员工信息

```sql
SEELCT * FROM employees WHERE email LIKE '%a%'
UNION
SELECT * FROM employees WHERE department_id > 90;
```





#### 5、7种SQL JOIN的实现

![image-20240302171715612](.\images\image-20240302171715612.png)

**代码实现**

```sql
#中图：内连接 A ∩ B
SELECT employee_id,last_name,department_name
FROM employees e INNER JOIN departments d
ON e.department_id = d.department_id

#左上图：左外连接
SELECT employee_id, last_name, department_name
FROM employees e LEFT JOIN departments d
ON e.department_id = d.department_id

#右上图：右外连接
SELECT employee_id, last_name, department_name
FROM employees e RIGHT JOIN departments d
ON e.department_id = d.department_id

#左中图：A - A ∩ B
SELECT employee_id, last_name, department_name
FROM employees e LEFT JOIN departments d
ON e.department_id = d.department_id
WHERE d.department_id IS NULL;

#右中图：B - A ∩ B
SELECT employee_id, last_name, department_name
FROM employees e RIGHT JOIN departments d
ON e.department_id = d.department_id
WHERE e.department_id IS NULL;

#左下图：满外连接
#左中图 + 右上图 A ∪ B
SELECT employee_id, last_name, department_name
FROM employees e LEFT JOIN departments d
ON e.department_id = d.department_id
WHERE d.department_id IS NULL
UNION ALL #没有去重操作，效率更高
SELECT employee_id, last_name, department_name
FROM employees e RIGHT JOIN departments d
ON e.department_id = d.department_id

#右下图
#左中图 + 右中图 A ∪ B - A ∩ B 或者 (A - A∩B) ∪ (B - A∩B)
SELECT employee_id, last_name, department_name
FROM employees e LEFT JOIN departments d
ON e.department_id = d.department_id
WHERE d.department_id IS NULL
UNION ALL
SELECT employee_id, last_name, department_name
FROM employees e RIGHT JOIN departments d
ON e.department_id = d.department_id
WHERE e.department_id IS NULL;
```



##### 语法格式小结

* 左中图/右中图，查询表中与其他表不关联的字段

  ![image-20240304112456330](.\images\image-20240304112456330.png)![image-20240304112514528](.\images\image-20240304112514528.png)

  

  ```sql
  select 字段列表
  from 查询的表 left join 关联表
  on 关联条件
  where 从表关联字段 is null
  其他子句;
  ```

* 左下图，查询两个表中的所有信息，包含两个表中不关联的信息

  ![image-20240304112635478](.\images\image-20240304112635478.png)

  ```sql
  #查询的结果是并集
  #上部分查询的是A表中与B表不关联的数据，即 A - A ∩ B
  select 字段列表
  from A表 left join B表
  on 关联条件
  where B表的关联条件 IS NULL
  其他子句
  
  union all #union all不会去重，相较于union效率更高
  
  select 字段列表
  from A表 right join B表
  on 关联条件
  其他子句
  #下部分查询的是所有B表的数据，即 B
  #两部分加起来就是 A ∪ B
  ```

* 右下图，这部分就是将两个表的关联部分去除，只剩下两个表中不关联的部分

  ![image-20240304113315556](.\images\image-20240304113315556.png)

  ```sql
  #A表非关联字段 ∪ B表非关联字段
  select 字段列表
  from A表 left join B表
  on 关联条件
  where 从表关联字段 is null
  其他子句
  
  union
  
  select 字段列表
  from A表 right join B表
  on 关联条件
  where 从表关联字段 is null
  其他子句;
  ```



#### 6、SQL99语法的新特性

##### 6.1、自然连接

SQL99在SQL92的基础上提供了一些特殊语法，比如`NATURAL JOIN`用来表示自然连接。我们可以把自然连接理解为SQL92中的等值连接。它会帮你自动查询两张连接表中`所有相同的字段`，然后进行**`等值连接`**。

比如：对于employees表和departments表而言，它们两张表中均有department_id和manager_id，此时我对它们两张表使用自然连接时：

```sql
SELECT employee_id, last_name, department_name
FROM employees e NATURAL [INNER/LEFT/RIGHT] JOIN departments d;
```

相当于如下SQL代码：

```sql
SELECT employee_id, last_name, department_name
FROM employees e [INNER/LEFT/RIGHT] JOIN departments d
ON e.department_id = d.department_id
AND e.manager_id = d.manager_id
```

即，它会去自动查询两张表中所有相等名称的字段，然后使用等值连接两张表。



##### 6.2、USING连接

当我们进行连接的时候，SQL99还支持使用USING指定数据表里的`同名字段`进行等值连接。但是只能配合JOIN一起使用。比如

```sql
SELECT employee_id, last_name, department_name
FROM employees e [INNER/LEFT/RIGHT] JOIN departments d
USING (department_id);
```

你能看出与自然连接NATURAL JOIN不同的是，USING指定了具体的相同的字段名称，你需要在USING的括号()中填入要指定的同名字段。同时使用`JOIN...USING`可以简化JOIN ON的等值连接。它相当于下面的SQL查询：

```sql
SELECT employee_id, last_name, department_name
FROM employees e [INNER/LEFT/RIGHT] JOIN departments d
ON e.department_id = d.department_id
```



**`注意！`**

我们要**控制连接表的数量**。多表连接就相当于嵌套for循环一样，非常消耗资源，会让SQL查询性能下降得很厉害，因此不要连接不必要的表。



# 五、单行函数

## 1、函数的理解

从函数定义的角度出发，我们可以把函数分为`内置函数`和`自定义函数`。在SQL语言中，同样也包括了内置函数和自定义函数。内置函数是系统内置的通用函数，而自定义函数四我们根据自己的需要编写的。

### 1.1、不同DBMS函数的差异

我们在使用SQL语言的似乎，不是直接和这门语言打交道，而是通过它使用不同的数据库软件，即DBMS。**`DBMS之间的差异性很大，远大于同一个语言不通版本之间的差异`**。实际上，只有很少的函数是被DBMS同时支持的。比如，大多数DBMS使用（||）或者（+）来做拼接符，而在MySQL中的字符串拼接函数concat()。大多数DBMS会有自己特定的函数，这就意味着**采用SQL函数的代码可移植性是很差的**，因此在使用函数的时候需要特别注意。

### 1.2、MySQL的内置函数及分类

MySQL提供了丰富的内置函数，这些函数使得数据的维护与管理更加方便，能够更好地提供数据的分析与统计功能，在一定程度上提高了开发人员进行数据分析与统计的效率。

MySQL提供的内置函数从`实现的功能`角度可以分为数值函数、字符串函数、日期和时间函数、流程控制函数、加密与解密函数、获取MySQL信息函数、聚合函数等。这里，我将这些丰富的内置函数再分为两类：`单行函数`、`聚合函数（或多行函数、分组函数）`

**`MySQL函数`**

![image-20240304121456417](.\images\image-20240304121456417.png)

**单行函数**

* 操作数据对象
* 接受参数返回一个结果
* **只对一行进行变换**
* **每行返回一个结果**
* 可以嵌套
* 参数可以是一列或一个值



## 2、数值函数

### 2.1、基本的数值函数

| 函数                  | 用法                                                         |
| --------------------- | ------------------------------------------------------------ |
| ABS(x)                | 返回x的绝对值                                                |
| SIGN(x)               | 返回x的符号。正数返回1，负数返回-1,0返回0                    |
| PI()                  | 返回圆周率的值                                               |
| CEIL(x),CEILING(x)    | 返回大于或等于某个值的最小整数                               |
| FLOOR(x)              | 返回小于或等于某个值的最大整数                               |
| LEAST(e1,e2,e3...)    | 返回列表中的最小值                                           |
| GREATEST(e1,e2,e3...) | 返回列表中的最大值                                           |
| MOD(x, y)             | 返回x除以y后的余数                                           |
| RAND()                | 返回0~1的随机数                                              |
| RAND(x)               | 返回0~1的随机数，其中x的值用作种子值，相同的x值会产生相同的随机数 |
| ROUND(x)              | 返回一个对x的值进行四舍五入后，最接近于x的整数               |
| ROUND(x, y)           | 返回一个对x的值进行四舍五入后最接近X的值，并保留到小数点后面y位 |
| TRUNCATE(x,y)         | 返回数字x截断为y位小数的结果                                 |
| SQRT(x)               | 返回x的平方根。当x的值为负数时，返回NULL                     |

举例：

```sql
SELECT ABS(-123), ABS(32),SIGN(-23),SIGN(43),PI(),CEIL(32.32),CEILING(-43.23),FLOOR(32.32),FLOOR(-43.34),MOD(12,5);
```

![image-20240304124632065](.\images\image-20240304124632065.png)



```sql
SELECT RAND(), RAND(),RAND(10), RAND(10), RAND(-1), RAND(-1);
```

![image-20240304124822017](.\images\image-20240304124822017.png)



**单行函数可以嵌套**：

需求：生成一个0到100之间的数，要求保留两位小数

```sql
SELECT TRUNCATE(RAND() * 100,2);
```

执行结果：

<img src=".\images\image-20240304130853455.png" align="left">

### 2.2、角度与弧度互换函数

| 函数       | 用法                                  |
| ---------- | ------------------------------------- |
| RADIANS(x) | 将角度转化为弧度，其中，参数x为角度值 |
| DEGREES(x) | 将弧度转化为角度，其中，参数x为弧度值 |

```sql
select RADIANS(30),RADIANS(45),RADIANS(60),RADIANS(90),DEGREES(2 * PI()),DEGREES(RADIANS(60));
```

查询结果：

![image-20240304131700256](.\images\image-20240304131700256.png)



### 2.3、三角函数

| 函数        | 用法                                                         |
| ----------- | ------------------------------------------------------------ |
| SIN(x)      | 返回x的正弦值，其中，参数x为弧度制                           |
| ASIN(x)     | 返回x的反正弦值，即获取正弦为x的值。如果x的值不在-1到1之间，则返回NULL |
| COS(x)      | 返回x的余弦值，其中，参数x为弧度值                           |
| ACOS(x)     | 返回x的反余弦值，即获取余弦为x的值。如果x的值不在-1到1之间，则返回NULL |
| TAN(x)      | 返回x的正切值，其中，参数x为弧度值                           |
| ATAN(x)     | 返回x的反正切值，即返回正切值为x的值                         |
| ATAN2(m, n) | 返回两个参数的反正切值                                       |
| COT(x)      | 返回x的余切值，其中，x为弧度值                               |

ATAN2(M, N)函数返回两个参数的反正切值。与ATAN(x)函数相比，ATAN2(M, N)需要两个参数，例如有两个点point(x1, y1)和point(x2, y2)，使用ATAN(x)函数计算反正切值为ATAN((y2-y1),(x2-x1))，使用ATAN2(M, N)计算反正切值则为ATAN2(y2-y1, x2-x1)。由使用方式可以看出，当x2-x1等于0时，ATAN(x)函数会报错，而ATAN2(M, N)仍然可以计算。

```sql
SELECT sin(RADIANS(30)), DEGREES(ASIN(1)),TAN(RADIANS(45)), DEGREES(ATAN(1)),DEGREES(ATAN2(1,1));
```

查询结果：

![image-20240304141556308](.\images\image-20240304141556308.png)



### 2.4、指数与对数

| 函数                   | 用法                                                |
| ---------------------- | --------------------------------------------------- |
| POW(x, y), POWER(x, y) | 返回x的y次方                                        |
| EXP(x)                 | 返回e的x次方，其中e是一个常数                       |
| LN(x),LOG(x)           | 返回以e为底的x的对数，当x <= 0时，返回结果为NULL    |
| LOG10(x)               | 返回以10为底的x的对数，当x <= 0时，返回的结果为NULL |
| LOG2(x)                | 返回以2为底的x的对数，当x <= 0时，返回NULL          |

```sql
select POW(2,5), POWER(2,4), EXP(2),LN(10), LOG10(10), LOG2(4)
```

![image-20240304142128126](.\images\image-20240304142128126.png)



### 2.5、进制间的转换

| 函数            | 用法                     |
| --------------- | ------------------------ |
| BIN(x)          | 返回x的二进制编码        |
| HEX(x)          | 返回x的十六进制编码      |
| OCT(x)          | 返回x的八进制编码        |
| CONV(x, f1, f2) | 返回f1进制数变成f2进制数 |

```sql
select POW(2,5), POWER(2,4), EXP(2),LN(10), LOG10(10), LOG2(4);
```

查询结果：

![image-20240304142439138](.\images\image-20240304142439138.png)





## 3、字符串函数

| 函数                           | 用法                                                         |
| ------------------------------ | ------------------------------------------------------------ |
| ASCII(s)                       | 返回字符串s中的第一个字符的ASCII码值                         |
| CHAR_LENGTH(s)                 | 返回字符串s的字符数。作用与CHARACTER_LENGTH(s)相同           |
| LENGTH(s)                      | 返回字符串s的字节数，和字符集有关                            |
| CONCAT(s1, s2,...,sn)          | 连接s1，s2,...,sn为一个字符串                                |
| CONCAT_WS(x,s1,s2,...,sn)      | 同CONCAT()函数，但是每个字符串之间要加上x                    |
| INSERT(str,idx,len,replacestr) | 将字符串str从idx位置开始，len个字符长的子串替换为字符串replacestr |
| REPLACE(str,a,b)               | 用字符串b替换字符串str中所有出现的字符串a                    |
| UPPER(s)或UCASE(s)             | 将字符串s的所有字母转成为大写字母                            |
| LOWER(s)或LCASE(s)             | 将字符串s的所有字母转成小写字母                              |
| LEFT(str,n)                    | 返回字符串str最左边的n个字符                                 |
| RIGHT(str,n)                   | 返回字符串str最右边的n个字符                                 |
| LPAD(str,len,pad)              | 用字符串pad对str最左边进行填充，直到str的长度为len           |
| RPAD(str,len,pad)              | 用字符串pad对str最右边进行填充，直到str的长度为len           |
| LTRIM(s)                       | 去掉字符串s左侧的空格                                        |
| RTRIM(s)                       | 去掉字符串s右侧的空格                                        |
| TRIM(s)                        | 去掉字符串s开始与结尾的空格                                  |
| TRIM(s1 FROM s)                | 去掉字符串s开始与结尾的s1                                    |
| TRIM(LEADING s1 FROM s)        | 去掉字符串s开始处的s1                                        |
| TRIM(TRAILING s1 FROM s)       | 去掉字符串s结尾处的s1                                        |
| REPEAT(str, n)                 | 返回str重复n次的结果                                         |
| SPACE(n)                       | 返回n个空格                                                  |
| STRCMP(s1, s2)                 | 比较字符串s1,s2的ASCII码值的大小                             |
| SUBSTR(s, index, len)          | 返回从字符串s的index位置其len个字符，作用与SUBSTRING(s,n,len)、MID(s,n,len)相同 |
| LOCATE(substr, str)            | 返回字符串substr在字符串str中首次出现的位置，作用与POSITION(substr IN str)、INSTR(str,substr)相同。未找到，返回0. |
| ELT(m,s1,s2,...,sn)            | 返回指定位置的字符串，如果m=1，则返回s1；如果m=2，则返回s2... |
| FIELD(s,s1,s2,...,sn)          | 返回字符串s在字符串列表中第一次出现的位置                    |
| FIND_IN_SET(s1,s2)             | 返回字符串s1在字符串s2中出现的位置。其中，字符串s2是一个以逗号分隔的字符串。 |
| REVERSE(s)                     | 返回s反转后的字符串                                          |
| NULLIF(value1,value2)          | 比较两个字符串，如果value1和value2相等，则返回NULL，否则返回value1. |

> 注意：MySQL中，字符串的位置是从**`1`**开始的。

```sql
SELECT FIELD('mm','hello','msm','amma'), FIND_IN_SET('mm','hello,mm,amma');
```

![image-20240304152416152](.\images\image-20240304152416152.png)



```sql
SELECT NULLIF('mysql','mysql'),NULLIF('mysql','');
```

![image-20240304152513373](.\images\image-20240304152513373.png)

## 4、日期和时间函数

### 4.1、获取日期、时间

| 函数                                                         | 用法                           |
| ------------------------------------------------------------ | ------------------------------ |
| **`CURDATE()`**，CURRENT_DATE()                              | 返回当前日期，只包含年、月、日 |
| **`CURTIME()`**，CURRENT_TIME()                              | 返回当前时间，只包含时、分、秒 |
| **`NOW()`** / SYSDATE() / CURRENT_TIMESTAMP() / LOCALTIME() / LOCALTIMESTAMP() | 返回当前系统日期和时间         |
| UTC_DATE()                                                   | 返回UTC（世界标准时间）日期    |
| UTC_TIME()                                                   | 返回UTC（世界标准时间）时间    |

```sql
SELECT CURDATE(), CURTIME(), NOW(), SYSDATE() + 0, UTC_DATE(), UTC_DATE() + 0, UTC_TIME(), UTC_TIME() + 0;
```

查询结果：

![image-20240304153934185](.\images\image-20240304153934185.png)



### 4.2、日期和时间戳的转换

| 函数                     | 用法                                     |
| ------------------------ | ---------------------------------------- |
| UNIX_TIMESTAMP()         | 以UNIX时间戳的形式返回当前时间。         |
| UNIX_TIMESTAMP(date)     | 将时间date以UNIX时间戳的形式返回。       |
| FROM_UNIXTIME(timestamp) | 将UNIX时间戳的时间转换为普通格式的时间。 |

```sql
SELECT UNIX_TIMESTAMP(), UNIX_TIMESTAMP(NOW()),UNIX_TIMESTAMP(CURDATE()),UNIX_TIMESTAMP(CURRENT_TIME), UNIX_TIMESTAMP('2024-3-4 15:57:57'),FROM_UNIXTIME(1709539049);
```

查询结果：

![image-20240304155845023](.\images\image-20240304155845023.png)



### 4.3、获取月份、星期、星期数、天数等函数

| 函数                                     | 用法                                           |
| ---------------------------------------- | ---------------------------------------------- |
| YEAR(date) / MONTH(date) / DAY(date)     | 返回具体的日期值                               |
| HOUR(time) / MINUTE(time) / SECOND(time) | 返回具体的时间值                               |
| MONTHNAME(date)                          | 返回月份：January,...                          |
| DAYNAME(date)                            | 返回星期几：MONDAY, TUESDAY,...SUNDAY          |
| WEEKDAY(date)                            | 返回周几，注意，周一是0，周二是1，...，周日是6 |
| QUARTER(date)                            | 返回日期对应的季度，范围是1~4                  |
| WEEK(date),WEEKOFYEAR(date)              | 返回一年中的第几周                             |
| DAYOFYEAR(date)                          | 返回日期是一年中的第几天                       |
| DAYEOFMONTH(date)                        | 返回日期位于所在月份的第几天                   |
| DAYOFWEEK(date)                          | 返回周几，注意：周日是1，周一是2，...，周六是7 |

举例：

```sql
SELECT YEAR(CURDATE()),MONTH(CURDATE()),DAY(CURDATE()),
HOUR(CURTIME()),MINUTE(NOW()),SECOND(SYSDATE())
FROM DUAL;
```

查询结果：

![image-20240304162357519](.\images\image-20240304162357519.png)



```sql
SELECT MONTHNAME('2021-10-26'),DAYNAME('2021-10-26'),WEEKDAY('2021-10-26'),
QUARTER(CURDATE()),WEEK(CURDATE()),DAYOFYEAR(NOW()),
DAYOFMONTH(NOW()),DAYOFWEEK(NOW())
FROM DUAL;
```

查询结果：

![image-20240304162420205](.\images\image-20240304162420205.png)



### 4.4、日期的操作函数

| 函数                    | 用法                                       |
| ----------------------- | ------------------------------------------ |
| EXTRACT(type FROM date) | 返回指定日期中特定的部分，type指定返回的值 |

EXTRACT(type FROM date)函数中type的取值与含义：

![image-20240304163024007](.\images\image-20240304163024007.png)

```sql
SELECT EXTRACT(MINUTE FROM NOW()), EXTRACT(WEEK FROM NOW()), EXTRACT(QUARTER FROM NOW()), EXTRACT(MINUTE_SECOND FROM NOW());
```

查询结果：

![image-20240304163227380](.\images\image-20240304163227380.png)



### 4.5、时间和秒钟转换的函数

| 函数                 | 用法                                                         |
| -------------------- | ------------------------------------------------------------ |
| TIME_TO_SEC(time)    | 将time转化为秒并返回结果值。转化的公式为：`小时*3600+分钟*60+秒` |
| SEC_TO_TIME(seconds) | 将seconds描述转化为包含小时、分钟和秒的时间                  |

举例：

```sql
SELECT TIME_TO_SEC(NOW()), SEC_TO_TIME(78288);
```

执行结果：

![image-20240304163620923](.\images\image-20240304163620923.png)



### 4.6、计算日期和时间的函数

**`第一组`**:

| 函数                                                         | 用法                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| DATE_ADD(datetime,INTERVAL expr type),<br>ADDDATE(date,INTERVAL expr type) | 返回与给定日期时间相差expr时间段的日期时间（这里使用的是加法） |
| DATE_SUB(date,INTERVAL expr type),<br>SUBDATE(date,INTERVAL expr type) | 返回与date相差expr时间间隔的日期（这里使用的是减法）         |

上述函数中type的取值：

![image-20240304225439918](.\images\image-20240304225439918.png)

举例：

```sql
SELECT DATE_ADD(NOW(), INTERVAL 1 DAY) AS col1,DATE_ADD('2021-10-21 23:32:12',INTERVAL
1 SECOND) AS col2,
ADDDATE('2021-10-21 23:32:12',INTERVAL 1 SECOND) AS col3,
DATE_ADD('2021-10-21 23:32:12',INTERVAL '1_1' MINUTE_SECOND) AS col4,
DATE_ADD(NOW(), INTERVAL -1 YEAR) AS col5, #可以是负数
DATE_ADD(NOW(), INTERVAL '1_1' YEAR_MONTH) AS col6 #需要单引号
FROM DUAL;
```

执行结果：

![image-20240304225124018](.\images\image-20240304225124018.png)

```sql
SELECT DATE_SUB('2021-01-21',INTERVAL 31 DAY) AS col1,
SUBDATE('2021-01-21',INTERVAL 31 DAY) AS col2,
DATE_SUB('2021-01-21 02:01:01',INTERVAL '1 1' DAY_HOUR) AS col3
FROM DUAL;
```

执行结果：

![image-20240304225622019](.\images\image-20240304225622019.png)



**`第二组：`**

| 函数                         | 用法                                                         |
| ---------------------------- | ------------------------------------------------------------ |
| ADDTIME(time1,time2)         | 返回time1加上time2的时间。当time2为一个数字时，代表的是秒，可以为负数 |
| SUBTIME(time1,time2)         | 返回time1减去time2后的时间。当time2为一个数字时，代表的是秒，可以为负数 |
| **`DATEDIFF(date1,date2)`**  | 返回date1-date2的日期间隔天数，这里是使用前面的-后面的       |
| TIMEDIFF(time1,time2)        | 返回time1-time2的时间间隔                                    |
| FROM_DAYS(N)                 | 返回从0000年1月1日起，N天以后的日期                          |
| TO_DAYS(date)                | 返回日期date距离0000年1月1日的天数                           |
| LAST_DAY(date)               | 返回date所在月份的最后一天的日期                             |
| MAKEDATE(year,n)             | 针对给定年份与所在年份中的天数返回一个日期                   |
| MAKETIME(hour,minute,second) | 将给定的小时、分钟和秒组合成时间并返回                       |
| PERIOD_ADD(time,n)           | 返回time加上n后的时间                                        |

```sql
SELECT
ADDTIME(NOW(),20),SUBTIME(NOW(),30),SUBTIME(NOW(),'1:1:3'),DATEDIFF(NOW(),'2021-10-
01'),
TIMEDIFF(NOW(),'2021-10-25 22:10:10'),FROM_DAYS(366),TO_DAYS('0000-12-25'),
LAST_DAY(NOW()),MAKEDATE(YEAR(NOW()),12),MAKETIME(10,21,23),PERIOD_ADD(20200101010101,
10)
FROM DUAL;
```

查询结果：

![image-20240304235212922](.\images\image-20240304235212922.png)



### 4.7、日期的格式化与解析

* **`格式化`**：日期 --> 字符串
* **`解析`**：字符串 --> 日期

之前，我们接触过的都是隐式的格式化或解析，比如：

```sql
SELECT *
FROM employees
WHERE hire_date = '1993-01-13';
```

查询结果：

![image-20240304235747613](.\images\image-20240304235747613.png)

这里实际上就是将字符串转换成了日期类型，进行了隐式的解析。

但是，这种隐式的解析往往非常的死板，字符串的格式与日期的格式需要保持一致才能进行隐式地格式化或解析。当我们想将月日放在前面，而年份放在后面的时候，这个时候去格式化或者解析就无法进行隐式地转换。所以，我们需要进行显式地对字符串或者日期进行格式化或者解析。

| 函数                              | 用法                                       |
| --------------------------------- | ------------------------------------------ |
| `DATE_FROMAT(date,fmt)`           | 按照字符串fmt格式化日期date值              |
| `TIME_FORMAT(time,fmt)`           | 按照字符串fmt格式化时间time值              |
| GET_FORMAT(date_type,format_type) | 返回日期字符串的显示格式                   |
| `STR_TO_DATE(str,fmt)`            | 按照字符串fmt对str进行解析，解析为一个日期 |

上述函数中fmt参数常用的格式符：

![image-20240305005838097](.\images\image-20240305005838097.png)

![image-20240305005857304](.\images\image-20240305005857304.png)

举例：

```sql
SELECT DATE_FORMAT(CURDATE(),'%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i:%s'),
TIME_FORMAT(CURTIME(),'%Y-%m-%d %H:%i:%s'),
TIME_FORMAT(NOW(),'%Y-%m-%d %H:%i:%s'),
STR_TO_DATE('2014-04-22 15:47:06','%Y-%m-%d %H:%i:%s');
```

查询结果：

![](.\images\image-20240305005515622.png)

由查询结果可知，当我们进行格式化时：

* 只想获取日期信息，使用DATE_FORMAT()函数并传入CURDATE()函数获取当前的日期。
* 只是获取时间信息，使用TIME_FORMAT()函数
* 既想获取日期，也想获取时间信息，使用DATE_FORMAT()函数并传入NOW()函数获取当前的日期与时间。



当我们需要一种格式，去进行格式化或者解析时，这个时候我们可以使用现成的，**`GET_FORMAT()`**方法就是给我们返回日期进行格式化或解析格式的方法，它有如下类型的参数：

![image-20240305003950576](.\images\image-20240305003950576.png)

这样，我们就可以使用获取指定格式的日期类型字符串。

例如：

```sql
SELECT DATE_FORMAT(NOW(),GET_FORMAT(DATE,'USA')) FROM DUAL;
```

我们就可以将当前时间转换成现有的格式

执行结果：

<img src=".\images\image-20240305005728891.png" align="left">



## `5、流程控制函数（重要）`

流程处理函数可以根据不同的条件，执行不同的处理流程，可以在SQL语句中实现不同的条件选择。MySQL中的流程处理函数主要包括`IF()`、`IFNULL()`和`CASE()`函数。

| 函数                                                         | 用法                                            |
| ------------------------------------------------------------ | ----------------------------------------------- |
| **`IF(value,value1,value2)`**                                | 如果value的值为TRUE，返回value1，否则返回value2 |
| IFNULL(value1,value2)                                        | 如果value1不为NULL，返回value1，否则返回value2  |
| **`CASE WHEN 条件1 THEN 结果1 WHEN 条件2 THEN 结果2 ... [ELSE 结果n] END`** | 相当于Java的if...else if...else...              |
| **`CASE expr WHEN 常量值1 THEN 值1 WHEN 常量值2 THEN 值2...[ELSE 值n] END`** | 相当于Java的switch...case...                    |

案例1：将employees表中的commission_pct字段进行筛选，如果是NULL就返回0，如果不是NULL就返回自身：

```sql
SELECT commission_pct,IF(commission_pct IS NOT NULL, commission_pct, 0) as "details"
FROM employees;

#或

SELECT commission_pct, IFNULL(commission_pct, 0) as "details"
FROM employees;
```

查询结果：

<img src=".\images\image-20240305102720295.png" align="left">

案例2：查询部门号为10,20,30的员工信息，若部门号为10，则打印其工资的1.1倍；20号部门，则打印其工资的1.2倍；30号部门打印其工资的1.3倍。

```sql
SELECT employee_id, last_name, department_id, salary, 
CASE WHEN department_id = 10 THEN salary * 1.1
	 WHEN department_id = 20 THEN salary * 1.2
	 ELSE salary * 1.3 END "details"
FROM employees
WHERE department_id IN (10, 20, 30);

#或者也可以使用switch...case语句

SELECT employee_id, last_name, department_id, salary,
CASE department_id WHEN 10 THEN salary * 1.1
				   WHEN 20 THEN salary * 1.2
				   ELSE salary * 1.3 END "details"
FROM employees
WHERE department_id IN (10, 20, 30);
```

查询结果：

![image-20240305104622179](.\images\image-20240305104622179.png)



## 6、加密与解密函数

加密与解密函数主要用于对数据库中的数据进行加密和解密处理，以防止数据被他人窃取。这些函数在保证数据库安全时非常有用。

| 函数                        | 用法                                                         |
| --------------------------- | ------------------------------------------------------------ |
| PASSWORD(str)               | 返回字符串str的加密版本，41位长的字符串。加密结果`不可逆`，常用于用户的密码加密 |
| MD5(str)                    | 返回字符串str的md5加密后的值，也是一种加密方式。若参数为NULL，则返回NULL。 |
| SHA(str)                    | 从原明文密码str计算并返回加密后的密码字符串，当参数为NULL时，返回NULL。`SHA加密算法比MD5更加安全。` |
| ENCODE(value,password_seed) | 返回使用password_seed作为加密密码加密value                   |
| DECODE(value,password_seed) | 返回使用password_seed作为加密密码解密value                   |

可以看到，ENCODE(value,password_seed)函数与DECODE(value,password_seed)函数互为反函数。

**注意：**

* PASSWORD()、ENCODE()以及DECODE()函数在mysql8.0中已被弃用。

* MD5()和SHA()也是`不可逆的`，即转换成密文后不可再转回明文。

  那当我们想要去获取这个明文信息时该怎么办呢？MD5()和SHA()均是用在加密上，当我们往前端输入一段密码后，MySQL也会使用同样的加密算法进行加密，然后跟原密文进行比较equals，若为TRUE，则表示输入的密码正确，而不会将密文转回明文。

案例：

```sql
SELECT MD5('hellojava'), SHA('hellojava');
```

执行结果：

![image-20240305111120032](.\images\image-20240305111120032.png)



## 7、MySQL信息函数和其他函数

### MySQL信息函数

MySQL中内置了一些可以查询MySQL信息的函数，这些函数主要用于帮助数据库开发或运维人员更好地对数据库进行维护工作。

| 函数                                                  | 用法                                                     |
| ----------------------------------------------------- | -------------------------------------------------------- |
| VERSION()                                             | 返回当前MySQL的版本号                                    |
| CONNECTION_ID()                                       | 返回当前MySQL服务器的连接数量                            |
| DATABASE(),SCHEMA()                                   | 返回MySQL命令行当前所在的数据库                          |
| USER()，CURRENT_USER()，SYSTEM_USER()，SESSION_USER() | 返回当前连接MySQL的用户名，返回结果格式为"主机名@用户名" |
| CHARSET(value)                                        | 返回字符串value自变量的字符集                            |
| COLLATION(value)                                      | 返回字符串value的比较规则                                |

```java
SELECT VERSION(), CONNECTION_ID(), DATABASE(), SCHEMA(), USER(), CURRENT_USER(), CHARSET('hellojava'), COLLATION('hellojava');
```

查询结果：

![image-20240305113257811](.\images\image-20240305113257811.png)



### 其他函数

MySQL中有些函数无法对其进行具体的分类，但是这些函数在MySQL的开发和运维过程中也是不容忽视的。

| 函数                           | 用法                                                         |
| ------------------------------ | ------------------------------------------------------------ |
| FORMAT(value, n)               | 返回对数字value进行格式化后的结果数据。n表示四舍五入后保留到小数点后n位。 |
| CONV(value,from,to)            | 将value的值进行不同进制之间的转换                            |
| INET_ATON(ipvalue)             | 将以点分割的IP地址转化为一个数字                             |
| INET_NTOA(value)               | 将数字形式IDIp地址转化为以点分隔的IP地址                     |
| BENCHMARK(n,expr)              | 将表达式expr重复执行n次。用于测试MySQL处理expr表达式所耗费的时间 |
| CONVERT(value USING char_code) | 将value所使用的字符编码修改为char_code                       |

![image-20240305115828079](.\images\image-20240305115828079.png)

![image-20240305115853155](.\images\image-20240305115853155.png)

# 六、多行函数（聚合函数）

聚合函数作用于一组数据，并对一组数据返回一个值。

![image-20240305154140139](.\images\image-20240305154140139.png)

**聚合函数类型**

* **`AVG()`**
* **`SUM()`**
* **`MAX()`**
* **`MIN()`**
* **`COUNT()`**

**聚合函数语法**

![image-20240305154525369](.\images\image-20240305154525369.png)

* 聚合函数不能嵌套调用。比如不能出现类似“AVG(SUM(字段名称))”形式的调用



## AVG和SUM函数

只适用于**`数值类型`**的求平均值和求总和。

**会自动过滤掉空值NULL**，将空值为NULL的数据不算在其中，比如：公司中有100个人，只有20个人有奖金，其他人的奖金为NULL，那么计算奖金的平均值只计算那20个人的，即：

SUM(salary) / 20，而不是去除以100.

```sql
SELECT AVG(salary), SUM(salary)
FROM employees;
```

<img src=".\images\image-20240305155158358.png" align="left">



## MAX和MIN函数

可以对**`任意数据类型`**(包括数值类型、字符串类型以及日期类型)使用MAX和MIN函数。

**会自动过滤掉空值NULL**。

```sql
SELECT MIN(hire_date),MAX(hire_date),MAX(salary),MIN(salary),MAX(last_name),MIN(last_name)
FROM employees;
```

![image-20240305155724400](.\images\image-20240305155724400.png)



## COUNT函数

**作用**：计算指定字段在查询结果中出现的个数。

**COUNT函数的使用一般有两种方式：**

1. **`COUNT(1)`**或**`COUNT(*)`**：用于返回结果集中的记录总数。
2. **`COUNT(expr)`**：返回<font style="color:red;">**expr字段不为空**</font>的记录总数，会将为空的字段筛选掉。

案例：

```sql
SELECT COUNT(1), COUNT(*), COUNT(commission_pct)
FROM employees;
```

执行结果：

<img src=".\images\image-20240305160417531.png" align="left">

这两种方式查询得到的结果不一样的原因在于，COUNT(commission_pct)不会统计此列为NULL值的行数，而COUNT(*)或COUNT(1)仅仅查询结果集的行数。



* **问题1：用COUNT(*)，COUNT(1)，COUNT(列名)谁比较好？**

  在效率上来说，对于MyISAM引擎的表是没有区别的，这种引擎内部有一个计数器在维护行数。Innodb引擎的表用count(*)，count(1)直接读行数，时间复杂度是O(n)，innodb真的要去数一遍，但是要优于COUNT(列名)。

  但是在业务的角度上考虑，如果是去获取结果集所有的行数，那么就使用COUNT(*)或COUNT(1)；如果要去筛选掉NULL空字段的信息，则使用COUNT(列名)。

* **问题2：能不能使用COUNT(列名)替换COUNT(*)？**

  不要使用COUNT(列名)来替代count(\*)，count(*)是SQL92定义的标准统计行数的语法，跟数据库无关，跟NULL与非NULL无关。

  说明：count(*)会统计值为NULL的行，而count(列名)不会统计此列为NULL值的行。

**总结：**

> 如果要统计所有记录，使用`COUNT(1)`或者`COUNT(*)`；如果要统计表中某一个字段所有非空的记录，使用`COUNT(字段名)`。

## 案例（易错点）

需求：查询公司中平均奖金率

此时，若我们使用：

```sql
SELECT AVG(commission_pct)
FROM employees;
```

以上的写法是正确的吗？

这种写法是错误的。

原因在于公司中没有奖金率的员工，commission_pct字段的值为NULL。

而AVG()方法会自动过滤掉空值NULL，此时我们如果使用AVG(commission_pct)方式去计算公司的平均奖金率，则是只在有奖金率的员工之间进行计算的，比如公司中总人数为100人，有奖金率的员工只有30人，则是将总和 / 30，而不是总和 / 100。但是，要求实际上是去计算公司所有人的平均奖金率，所以应该要将没有奖金率的员工也计算在内。此时，就不能使用AVG()进行计算，应该改成：

```sql
SELECT SUM(commission_pct) / COUNT(*)
FROM employees;

#或者：
SELECT AVG(IFNULL(commission_pct,0))
FROM employees;
```

这样得出的结果，才是去计算公司所有人的平均奖金率。





# 七、GROUP BY与HAVING的使用

## GROUP BY

### 1、基本使用

GROUP BY用于给查询的结果进行分组，将原本的结果集分成一组一组的形式。

![image-20240305183349048](.\images\image-20240305183349048.png)

**`可以使用GROUP BY子句将表中的数据分为若干组`**

**格式：**

```sql
SELECT column, group_fuction(cloumn)
FROM table
[WHERE condition]
[GROUP BY group_by_expression]
[ORDER BY column];
```

案例：查询各个部门的平均工资，最高工资

```sql
SELECT department_id, AVG(salary), SUM(salary)
FROM employees
GROUP BY department_id;
```

查询结果：

![image-20240305183804737](.\images\image-20240305183804737.png)

> **注意：**
> **`在SELECT列表中所有未包含在组函数中的列都应该包含在GROUP BY子句中。`**

这是什么意思呢？

意思是若要进行分组操作，则在SELECT中查询的字段必须放在GROUP BY子句中进行分组。

为什么要这样？

因为若不将查询的字段放入GROUP BY分组操作中的话，那么SELECT查询的字段表示的含义实际上是未进行分组的字段，而分组就不知道该如何进行计算。比如：

```sql
select salary, department_id
from employees
group by department_id;
```

上述的SQL语句是根据department_id进行分组的，每一组中的department_id都是相等的，所以可以直接查询出来。但是对于salary该如何查询呢？每一组中的每一个用户的salary都是不一样的，而且没有对salary进行分组操作，这个时候就无法保证结果集中的每一条数据上的salary保持一致，此时就会报错。

所以，对于SELECT子句中的非组函数列，都应该包含在GROUP BY子句中。



**但是有这种情况：**

比如有一个需求：查询每个部门的部门名和最低工资。

此时，使用department_id进行分组比使用department_name分组会更好，因为有可能会有两个不同的部门使用相同的部门名，当有两个不同的部门使用了相同的部门名时，如果对部门名进行分组，就会将这两个部门合并在一起，看作是同一组，这是不符合业务逻辑的。此时，就不能使用department_name进行分组，而应该对department_id进行分组，即：

```sql
SELECT department_name, MIN(salary)
FROM departments d LEFT JOIN employees e
ON e.department_id = d.department_id
GROUP BY department_id;
```

那这里为什么又可以使用SELECT子句中没有的字段进行分组呢？

原因就在于，在departments表中，department_id与department_name字段是一一对应关系的，我们使用department_id进行分组，不会将department_name进行合并，依旧是一一对应的。

而在前面的例子中，department_id与salary之间并不是一一对应关系，当SELECT子句中包含department_id与salary，但是GROUP BY子句中不含有salary字段时，我们就不知道salary该如何进行合并从而报错。

但是这里就不需要考虑合并的问题。



### 2、使用多个列进行分组

使用多个列进行分组时，就会将每一组更加详细地进行划分。

比如，使用department_id和job_id进行分组，那么就会在department_id分一组的情况下，再对其中的job_id一致的数据分为一组，使得数据分组更加地详细。

![image-20240305184738054](.\images\image-20240305184738054.png)

案例：查询各个部门中各个工种员工的平均工资

```sql
SELECT department_id, job_id, AVG(salary)
FROM employees
GROUP BY department_id, job_id;
```

查询结果：

<img src=".\images\image-20240305184914603.png" align="left">

这里有一个疑问：如果我们分组时将department_id和job_id换一下顺序，先根据job_id进行分组，再按照department_id进行分组，这样结果有没有什么不同？

实际上，结果还是一样的。

因为不论是先按照department_id进行分组，还是先按照job_id进行分组，最后都是要求将同一个department_id和job_id的数据放在一个组里面，所以顺序并不会影响分组后的数据的。



### 3、WITH ROLLUP的使用

使用**`WITH ROLLUP`**关键字之后，在所有查询出的分组记录之后增加一条记录，该记录记录者对所有记录整体进行的操作所得到的结果。

案例：

```sql
SELECT department_id, AVG(salary)
FROM employees
GROUP BY department_id WITH ROLLUP;
```

查询结果：

<img src=".\images\image-20240305190439204.png" align="left">

我们可以看到，在最后一行，新增了一条数据，这条数据实际上是对所有的salary工资数据进行了求平均值AVG的操作，即WITH ROLLUP会在记录的最后添加一条记录，用于记录着对所有记录进行的操作所得到的数据。

> **注意：**
>
> 当使用`ROLLUP`时，不能同时使用`ORDER BY`子句进行结果排序，即ROLLUP和ORDER BY是相互排斥的。



## HAVING

### 基本使用

HAVING的作用是去**过滤分组**。

**案例**：查询各个部门中最高工资比10000高的部门信息

当我们需要对分组后的数据进行过滤，比如上述案例中要求过滤出MAX(salary) > 10000的数据时，此时就不能再使用WHERE了，因为WHERE是对整个表中的整体进行过滤。此时就需要使用到`HAVING`。

![image-20240305191510974](.\images\image-20240305191510974.png)



**`过滤分组：HAVING子句`**

1. 行已经被分组。
2. 使用了聚合函数。
3. 满足HAVING子句中条件的分组将被显示。
4. **HAVING不能单独使用，必须跟GROUP BY一起使用。**

**格式：**

![image-20240305191701124](.\images\image-20240305191701124.png)

使用HAVING实现上述的案例：

```sql
SELECT department_id, MAX(salary)
FROM employees
GROUP BY department_id
HAVING MAX(salary) > 10000;
```

执行结果：

<img src=".\images\image-20240305192203134.png" align="left">

这样就对分组后的数据进行了筛选。



**要求：**

> 1. HAVING必须声明在GROUP BY的后面。
> 2. HAVING必须要跟GROUP一起使用。
> 3. **`如果在过滤条件中使用了聚合函数，比如SUM()，则必须使用HAVING来替换WHERE。否则，报错。`**

比如：

```sql
SELECT department_id, AVG(salary)
FROM employees 
WHERE AVG(salary) > 8000
GROUP BY department_id;
```

这种写法是错误的，为什么错？

原因就在于SQL的执行顺序。AVG()聚合函数的执行顺序在WHERE子句之后，当执行到WHERE子句时，其中的AVG()函数还未执行，那也就无法去进行筛选操作了。

所以，遇到要使用聚合函数作为筛选条件时，需要在HAVING子句中使用，因为一般要使用聚合函数进行筛选时，都要进行分组操作。



### WHERE和HAVING的对比

**`区别一：WHERE可以直接使用表中的字段作为筛选条件，但不能使用分组中的计算函数作为筛选条件；HAVING必须要和GROUP BY配合使用，可以把分组计算的函数和分组字段作为筛选条件。`**

这决定了，在需要对数据进行分组统计的时候，HAVING可以完成WHERE不能完成的任务。这是因为，在查询语法结构中，WHERE在GROUP BY之前，所以无法对分组结果进行筛选。HAVING在GROUP BY之后，可以使用分组字段和分组中的计算函数，对分组的结果集进行筛选。这个功能是WHERE无法完成的。另外，WHERE排除的记录不再包括在分组中。

**`区别二：如果需要连接从关联表中获取需要的数据，WHERE是先筛选后连接，而HAVING是先连接后筛选。`**这一点，就决定了在关联查询中，WHERE比HAVING更高效。因为WHERE可以先筛选，用一个筛选后的较小的数据集和关联表进行连接，这样占用的资源比较少，执行的效率也比较高。HAVING则需要先把结果集准备好，也就是用未被筛选的数据集进行关联，然后对这个大的数据集进行筛选，这样占用的资源就比较多，执行效率也较低。



**`开发中选择：`**

WHERE和HAVING也不是互相排斥的，我们可以在一个查询里面同时使用WHERE和HAVING。包含分组统计函数的条件用HAVING，普通条件用WHERE。这样，我们就既利用了WHERE条件的高效快速，又发挥了HAVING可以使用分组统计函数的查询条件的优点。当数据量特别大的时候，运行效率会有很大的差别。



**结论：**

> 当过滤条件中有聚合函数，要声明在HAVING中，不能声明在WHERE中。
>
> 当过滤条件是对分组后的数据进行过滤的，要声明在HAVING中。
>
> 其他过滤条件都建议声明在WHERE中。
>
> **`一般情况下，没有聚合函数都声明在WHERE里面，有聚合函数就声明在HAVING中。`**



# 八、子查询

子查询指一个查询语句嵌套在另一个查询语句内部的查询，这个特性从MySQL4.1开始引入。

SQL中子查询的使用大大地增强了SELECT查询的能力，因为很多时候查询需要从结果集获取数据，或者需要从同一个表中先计算得出一个数据结果，然后与这个数据结果（可能是某个标量，也可能是某个集合）进行比较。

## 1、子查询的基本使用

### 案例

现在有一个需求：谁的工资比Abel的高？

![image-20240306110701069](.\images\image-20240306110701069.png)

解决方式：

```sql
#方式一：分两步进行查询，先查出Abel的salary，再使用查出来的salary进行查询
SELECT salary
FROM employees
WHERE last_name = 'Abel';

SELECT last_name,salary
FROM employees 
WHERE salary > 11000;

#方式二：自连接
SELECT e2.last_name, e2.salary
FROM employees e1,employees e2
WHERE e1.last_name = 'Abel'
AND e1.salary < e2.salary

#方式三：子查询
SELECT last_name, salary
FROM employees
WHERE salary > (
	SELECT salary
    FROM employees
    WHERE last_name = 'Abel'
);
```



### 子查询的基本使用

* 子查询的基本语法结构：

  ![image-20240306112242521](.\images\image-20240306112242521.png)

* 子查询（内查询）在主查询之前一次执行完成。即**先去执行子查询，再去执行主查询**。
* 子查询的结果被主查询（外查询）使用。
* **注意事项**
  * 子查询要包括在括号内
  * 将子查询放在比较条件的右侧
  * 单行操作符对应单行子查询，多行操作符对应多行子查询



### 子查询的分类

**`分类方式一：`**

我们按内查询的结果返回一条还是多条记录，将子查询分为`单行子查询`、`多行子查询`。

* 单行子查询

  ![image-20240306113100012](.\images\image-20240306113100012.png)

* 多行子查询

  ![image-20240306113127335](.\images\image-20240306113127335.png)

**`分类方式二：`**

我们按内查询是否被执行多次，将子查询划分为`相关（或关联）子查询`和`不相关（或非关联）子查询`

子查询从数据表中查询了数据结果，如果这个数据结果只执行一次，然后这个数据结果作为主查询的条件进行执行，那么这样的子查询叫做不相关子查询。

同样，如果子查询需要执行多次，即采用循环的方式，先从外部查询开始，每次都传入子查询进行查询，然后再将结果反馈给外部，这种嵌套的执行方式就称为相关子查询。

**相关子查询案例**：查询工资大于本部门平均工资的员工信息

在这个案例中，不仅仅需要将salary工资与AVG(salary)平均工资进行对比，还需要向子查询中传入当前员工的部门id，将部门id传入到子查询中进行查询，然后才能将AVG(salary)结果反馈给外部，进行嵌套执行。这种子查询就是相关子查询。

**不相关子查询案例**：查询工资大于本公司平均工资的员工信息

这个时候，就不需要将department_id传入到子查询中，直接使用salary与AVG(salary)进行比较即可。

## 2、单行子查询

单行子查询，就是子查询的结果只有一个数据，供主查询使用。

### 2.1、单行操作符和案例

**单行操作符**：= > >= < <= <>

> **`单行子查询使用单行操作符`**。

案例：查询与141号或174号员工的manager_id和department_id相同的其他员工的employee_id,mangaer_id,department_id

实现方式1：不成对比较

```sql
SELECT employee_id, manager_id, department_id
FROM employees 
WHERE manager_id IN (
	SELECT manager_id
    FROM employees 
    WHERE employee_id IN (174,141)
)
AND department_id IN (
    SELECT department_id
    FROM employees
    WHERE employee_id IN (174,141)
)
AND employee_id NOT IN (174,141);
```

实现方式2：成对比较（了解）

```sql
SELECT employee_id, department_id, manager_id
FROM employees
WHERE (manager_id, department_id) IN (
	SELECT manager_id, department_id
    FROM employees
    WHERE employee_id IN (174,141)
)
AND employee_id NOT IN (141,174);
```



### 2.2、HAVING中的子查询

* 首先执行子查询
* 向主查询中的HAVING子句返回结果

案例：查询最低工资大于50号部门最低工资的部门id和最低工资

由于条件里面出现了聚合函数，所以这个条件应该放在HAVING中

```sql
SELECT department_id, MIN(salary)
FROM employees
WHERE department_id IS NOT NULL
GROUP BY department_id
HAVING MIN(salary) > (
	SELECT MIN(salary)
	FROM employees
	WHERE department_id = 50
);
```



### 2.3、CASE中的子查询

**案例**：显示员工的employee_id,last_name和location。其中，若员工的department_id与location_id为1800的部门的department_id相同，则location为'Canada'，其余为'USA'.

```sql
SELECT employee_id, last_name, 
CASE department_id WHEN (SELECT department_id FROM departments WHERE location_id = 1800) THEN 'Canada' ELSE 'USA' END as location
FROM employees;
```

查询结果：

<img src=".\images\image-20240306132020641.png" align="left">

### 2.4、子查询中的空值情况

案例：

```sql
SELECT last_name, job_id
FROM employees
WHERE job_id = (
	SELECT job_id
	FROM employees
	WHERE last_name = 'Haas'
);
```

当employees表中不存在last_name为Haas的数据时，此时查询的结果为：

<img src=".\images\image-20240306132504180.png" align="left">

此时，不会报错，但是查询不到任何结果。

### 2.5、非法使用子查询

当多行子查询，但是使用的是单行比较符时，就会出现非法使用子查询的情况。

例如：

```sql
SELECT employee_id, last_name
FROM employees
WHERE salary = (
	SELECT MIN(salary)
	FROM employees
	GROUP BY department_id
);
```

报错信息：Subquery returns more than 1 row

在上例中，子查询查询的结果肯定是不止一个的，因为在employees表中有不止一种department_id，那么这里就不能使用=等于连接符进行连接，因为等于是只能连接一个值的。这里可以改成IN()函数。

对于单行操作符：=、>、>=、<、<=、<>，这几种操作符只能应用于返回结果只有一个的子查询，有多个结果的子查询需要使用多行操作符。



## 3、多行子查询

* 也称为集合比较子查询
* 内查询（子查询）返回多行
* **`使用多行比较操作符`**

### 3.1、多行比较操作符和案例

**多行比较操作符**

| 操作符    | 含义                                                     |
| --------- | -------------------------------------------------------- |
| **`IN`**  | 等于列表中的**任意一个**，满足一个即可                   |
| **`ANY`** | 需要和单行比较符一起使用，和子查询返回的**某一个值**比较 |
| **`ALL`** | 需要和单行操作符一起使用，和子查询返回的**所有值**比较   |
| SOME      | 实际上是ANY的别名，作用相同，一般常使用ANY               |

**代码示例**

**题目1**：返回其他job_id中比job_id为'IT_PROG'部门任意工资低的员工的员工号、姓名、job_id以及salary

```sql
SELECT employee_id, last_name, job_id, salary
FROM employees
WHERE salary < ANY (
	SELECT salary
    FROM employees
    WHERE job_id = 'IT_PROG'
)
AND job_id != 'IT_PROG';
```

这里要求比'IT_PROG'部门中任意员工的工资低，那么实际上就是去和最高工资的员工进行比较，只要比其低即可。



**题目2**：返回其他job_id中比job_id为'IT_PROG'部门所有工资都低的员工的员工号、姓名、job_id以及salary

```sql
SELECT employee_id, last_name, job_id, salary
FROM employees
WHERE salary < ALL (
	SELECT salary
    FROM employees
    WHERE job_id = 'IT_PROG'
)
AND job_id != 'IT_PROG';
```

这里要求比'IT_PROG'部门中所有员工的工资都低，实际上就是去与最低工资的做比较，只要比最低工资低，那么肯定比IT_PROG部门中所有员工的工资都低。



题目3：查询平均工资最低的部门

```sql
#方式一：
SELECT department_id
FROM employees
GROUP BY department_id
HAVING AVG(salary) = (
	SELECT MIN(avg_sal)
	FROM (
		SELECT AVG(salary) avg_sal
		FROM employees
		GROUP BY department_id
	) dept_avg_sal
);

#方式二：
SELECT department_id
FROM employees
```



### 3.2、空值问题

当内查询存在NULL值时，此时如果使用操作符去判断时，可能会由于操作数因为有一个NULL值，从而使得结果出现问题，如下例：

```sql
SELECT last_name
FROM employees
WHERE employee_id NOT IN (
	SELECT manager_id
	FROM employees
)
```

查询结果：

<img src=".\images\image-20240306143029550.png" align="left">

为什么会出现这种情况，让我们来单独运行一下子查询：

```sql
SELECT manager_id
FROM employees;
```

查询结果：

<img src=".\images\image-20240306143141871.png" align="left">

发现实际上是有数据的，但是为什么查询不出来呢？

这跟manager_id的第一个数据是NULL有关。

让我们来排除一下NULL的干扰：

```sql
SELECT last_name
FROM employees
WHERE employee_id NOT IN (
	SELECT manager_id
	FROM employees
	WHERE manager_id IS NOT NULL
);
```

查询结果：

<img src=".\images\image-20240306143323722.png" align="left">

所以，当我们使用多行子查询时，一定要注意：

> **`要排除因NULL可能造成的查询结果异常`**。



## 4、关联子查询（相关子查询）

之前学习的案例都是不相关子查询，现在我们来看一看相关子查询。

### 4.1、相关子查询的介绍

**基本特点**：**`执行依赖于外部查询`**。多数情况下是子查询的WHERE子句中引用了外部查询的表。

**语法：**

```sql
SELECT column1, column2
FROM table1 AS outer
WHERE column1 operator (
	SELECT column1, column2
	FROM table2
	WHERE expr1 = outer.expr2
);
```

**执行过程：**

1. 从外查询中取出一个元组，将元组相关列的值传给内查询。
2. 执行内查询，得到子查询操作的值。
3. 外查询根据子查询返回的结果或结果集得到满足条件的行。
4. 然后外查询取出下一个元组重复做步骤1~3，直到外层的元组全部处理完毕。

这个执行过程，有些类似于Java中的循环操作，每次都是从外部取出数据传入到内层中，然后将内层中的数据返回外部进行筛选，重复这个步骤直到外部的数据处理完毕。

在子查询中，使用到了外部的表employees，那么此时的子查询就是关联子查询。

下面对关联子查询**举例**说明：

查询Books表中大于该类图书价格平均值的图书信息

```sql
SELECT 图书信息
FROM Books AS a
WHERE 价格 >
(
	SELECT AVG(价格)
	FROM Books AS b
	WHERE a.类编号 = b.类编号
);
```

与前面介绍的子查询不同，相关子查询无法独立于外部查询而得到解决。该子查询需要一个“类编号”的值，而这个值是一个变量，随着SQLServer检索Books表中的不同行而改变。下面详细说明该查询执行过程：

先将Books表中的第一条记录的“类编号”的值，例如"2"代入子查询中，子查询变为：

```sql
SELECT AVG(价格)
FROM Books AS b
WHERE 2 = b.类编号
```

子查询的结果为该类图书的平均价格，然后再将得到的结果返回给外部查询，外部查询变为：

```sql
SELECT 图书信息
FROM Books AS a
WHERE 价格 > 34;
```

如果WHERE结果为TRUE，则第一条结果包含在最后的结果集中，否则不包含。

对Books表中的所有行运行相同的过程，最后获得结果。



**`关联子查询`和`非关联子查询`的区别在于：**

> 1. **`关联子查询`**引用了外部查询的列；
> 2. 执行的顺序不同。对于非关联子查询，先执行内查询，再执行外查询；对于关联子查询来说，**先执行外层查询，然后对所有通过过滤条件的记录执行内层查询**。



**题目1**：查询员工的id，salary，按照department_name排序

其中，员工id、salary是在employees表中的，department_name是在departments表中的，二者使用department_id进行关联。

```sql
#方式一：使用外连接
SELECT employee_id, salary
FROM employees e
LEFT JOIN departments d
ON e.department_id = d.department_id
ORDER BY d.department_name;

#方式二：使用子查询
SELECT employee_id, salary
FROM employees e
ORDER BY (
	SELECT department_name
	FROM departments d
	WHERE e.department_id = d.department_id
);
```



**结论：**

> **`在查询当中，除了GROUP BY和LIMIT之外，其他位置都可以声明子查询。`**



**题目2**：若employees表中的employee_id与job_history表中的employee_id相同的数目不小于2，则输出这些相同id员工的employee_id，last_name,和其job_id。

在job_history表中，有employee_id字段，不同的数据可能有相同的employee_id信息。

```sql
SELECT e.employee_id, e.last_name, e.job_id
FROM employees e
WHERE 2 <= (
	SELECT COUNT(*)
	FROM job_history j
	WHERE e.employee_id = j.employee_id
)
```

查询结果：

<img src=".\images\image-20240306232525717.png" align="left">





### 4.2、EXISTS和NOT EXISTS关键字

* 关联子查询通常也会和`EXISTS`操作符一起来使用，用来检查在子查询中是否存在满足条件的行。
* **`如果在子查询中不存在满足条件的行：`**
  * 条件返回FALSE
  * 继续在子查询中查找
* **`如果在子查询中存在满足条件的行：`**
  * 不在子查询中继续查找
  * 条件返回TRUE
* `NOT EXISTS`关键字表示如果不存在某种条件，在返回TRUE，否则返回FALSE。

**案例1：**查询公司管理者的employee_id, last_name, job_id和department_id信息

```sql
#方式一：内连接
SELECT DISTINCT m.employee_id,last_name,m.job_id,m.department_id
FROM employees e
INNER JOIN employees m
ON e.manager_id = m.employee_id

#方式二：子查询
SELECT e.employee_id, e.last_name, e.job_id, e.department_id
FROM employee e
WHERE e.employee_id IN (
	SELECT DISTINCT m.manager_id
    FROM employees m
    WHERE m.manager_id IS NOT NULL;
)

#方式三：使用EXISTS
SELECT m.employee_id, m.last_name, m.job_id, m.department_id
FROM employees m
WHERE EXISTS (
	SELECT *
	FROM employees e
    WHERE m.employee_id = e.manager_id
);
```

上述案例使用EXISTS方式的大致过程：

先去执行外层查询，将外层查询结果集中的第一条数据的employee_id传入到子查询中，开始对employees e2表进行一个一个地筛选，比较e2表中第一个数据的manager_id与传入的e1.employee_id是否相等，如果相等，说明数据存在EXISTS，返回TRUE给外层查询，表明外层查询的第一条数据符合条件，将第一条数据放入到最终的结果集中；如果不相等，说明数据不匹配，继续对e2表中的数据往后匹配，若将e2表中的数据全部进行筛选后都不匹配，则返回FALSE给外层查询，然后，继续将外层查询结果集的第二条数据的employee_id传入子查询中，继续执行，直到外层查询结果集所有数据都执行完毕，获得最终结果集。





**案例2：**查询departments表中，不存在于employees表中的部门的department_id和department_name，即没有员工的部门信息

```sql
#方式一：使用外连接
SELECT d.department_id, department_name
FROM departments d
LEFT JOIN employees e
ON d.department_id = e.department_id
WHERE e.department_id IS NULL;


#方式二：使用子查询+NOT EXISTS
SELECT department_id, department_name
FROM departments d
WHERE NOT EXISTS (
	SELECT *
	FROM employees e
	WHERE e.department_id = d.department_id
)
```





### 4.3、更新操作使用到关联子查询

使用相关子查询依据一个表中的数据更新另一个表的数据。

```sql
UPDATE table1 alias1
SET column = (
	SELECT expression
	FROM table2 alias2
	WHERE alias1.column = alias2.column
);
```

案例：在employees表中增加一个department_name字段，数据为员工对应的部门名称

```sql
#1
ALTER TABLE employees
ADD (department_name VARCHAR(14));

#2
UPDATE employees e
SET department_name = (
	SELECT department_name
	FROM departments d
    WHERE d.department_id = e.department_id
);
```



### 4.4、删除操作使用到关联子查询

使用相关子查询依据一个表中的数据删除另一个表的数据

```sql
DELETE FROM table1 alias1
WHERE column operator (
	SELECT expression
	FROM table2 alias2
	WHERE alias1.column = alias2.column
);
```

题目：删除表employees中，其与emp_history表皆有的数据

```sql
DELETE FROM employees e
WHERE employee_id IN (
	SELECT employee_id
	FROM emp_history
	WHERE employee_id = e.employee_id
);
```



## 5、子查询效率高还是自连接效率高？

当既可以使用子查询，也可以使用自连接时，一般情况**`建议你使用自连接`**。因为在许多DBMS的处理过程中，对于自连接的处理速度要比子查询快得多。

可以这样理解：子查询实际上是通过未知表进行查询后的条件判断，而自连接是通过已知的自身数据表进行条件判断，因此在大部分DBMS中都对自连接处理进行了优化。

