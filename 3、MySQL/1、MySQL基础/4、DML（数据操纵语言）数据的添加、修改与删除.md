# 一、插入数据 -> INSERT INTO

## 方式1：一条一条地添加数据

使用这种方式一次只能向表中插入一条数据。

**`情况1：为表的所有字段按默认顺序插入数据`**

**格式：**

```sql
INSERT INTO 表名
VALUES (value1, value2, ...);
```

值列表中需要为表的每一个字段指定值，并且**值的顺序必须和数据表中字段定义时的顺序相同**。

举例：

```sql
INSERT INTO	empl
VALUES (1, 'Tom','2000-12-21', 2400);
```



**`情况2：为表的指定字段插入数据`**

**格式：**

```sql
INSERT INTO 表名(column1 [, column2, ..., columnn])
VALUES (value1 [, value2, ..., valuen]);
```

该语句是为表的指定字段插入数据，就是在INSERT语句中只向部分字段插入值，而其他字段的值为表定义时的默认值。

使用上述方式插入数据，不需要管表创建时列的先后顺序，只需要将VALUES中要插入的value1,...,valuen和column1,...,columnn列一一对应。如果类型不同，将无法插入，并且MySQL会产生错误。

举例：

```sql
INSERT INTO empl(name, id)
VALUES ('cheng', 2);
```

如果列约束设置了`NOT NULL`，表明该数据不能为空，则在插入数据的时候必须要给该字段设置值，否则就会报错。



**`情况3：同时插入多条记录`**

INSERT语句可以同时向数据表中插入多条记录，插入时指定多个值列表，每个值列表之间用逗号分隔开，基本语法格式如下：

```sql
INSERT INTO table_name
VALUES
(value1 [, value2,..., valuen]),
(value1 [, value2,..., valuen]),
...
(value1 [, value2,..., valuen]);

或者

INSERT INTO table_name(column1 [, column2, …, columnn])
VALUES
(value1 [,value2, …, valuen]),
(value1 [,value2, …, valuen]),
……
(value1 [,value2, …, valuen])；
```

> 一个同时插入多行记录的INSERT语句等同于多个单行插入的INSERT语句，但是多行的INSERT语句在处理过程中**`效率更高`**。因为MySQL执行单条INSERT语句插入多行数据比使用多条INSERT语句块，所以在插入多条记录时最好选择使用单条INSERT语句的方式插入。
>
> 所以，在开发中，如果要一次性添加多个数据，建议使用这种方式添加数据。

案例：

```sql
INSERT INTO empl(id, `name`, salary)
VALUES
(3, 'java', 12000),
(4, 'jia', 13000),
(5, 'wei', 16000);
```

注意：字符和日期型数据应该包含在单引号中。

## 方式2：将查询结果插入表中

INSERT还可以将SELECT语句查询的结果插入到表中，此时不需要把每一条记录的值一个一个输入，只需要使用一条INSERT语句和一条SELECT语句组成的组合语句即可快速地从一个或多个表中插入多行。

**基本语法：**

```sql
INSERT INTO 目标表名 (tar_column1 [, tar_column2,...,tarcolumnn])
SELECT src_column1 [, src_column2,...,src_columnn]
FROM 源表名
其他子句;
```

在INSERT语句中加入SELECT查询语句，**`不必书写VALUES子句`**

**注意：**

> **查询结果集列表应当与INSERT子句中列的类型一一对应**。

案例：

```sql
INSERT INTO empl (id, `name`, salary, hire_date)
SELECT employee_id, last_name, salary, hire_date
FROM employees
WHERE department_id IN (60,70);
```

此时，就可以将employees表中department_id为60、70的数据添加到empl表中



这里有个问题：当两个表的类型一致，但是长度不一样时，将一个表中的数据添加到另一个表中，此时会不会报错呢？

比如：empl表中的name字段长度是15

<img src=".\images\image-20240309191548887.png" align="left">

employees表中last_name字段的长度是25

<img src=".\images\image-20240309191622581.png" align="left">

我们将employees表中的数据插入到empl表中后，会不会报错？

当employees表中没有数据的last_name长度超过15时，此时往empl表中插入数据不会报错；当employees表中有数据超过15，比如20，并且将该数据插入到empl表，此时就会报错。

**说明：**

如果我们使用这种方式去添加字段，`不仅需要考虑类型一致，还需要保证插入的数据不会超出当前表字段的长度`，如果超过了，需要考虑换种方式或者修改表字段的长度。





# 二、更新数据 -> UPDATE

使用UPDATE语句更新数据。

**语法如下：**

```sql
UPDATE table_name
SET column1=value1, column2=value2,...,columnn=valuen
[WHERE condition];
```

可以一次更新多条数据。

如果需要回滚数据，需要保证在DML前，进行设置：`SET autocommit = FALSE;`

使用WHERE子句指定需要更新的数据。

**如果省略WHERE子句，则表中的所有数据都将被更新。**

案例：将表中姓名中包含字符a的员工提薪20%

```sql
UPDATE empl
SET salary = salary * 1.2
WHERE `name` LIKE '%a%';
```

在修改数据时，可能会存在不成功的情况的，可能是由于约束情况造成的。



# 三、删除数据 -> DELETE

**语法格式：**

```sql
DELETE FROM table_name
[WHERE子句];
```

table_name指定要执行删除操作的表；[where]为可选参数，指定删除条件。

如果没有WHERE子句，DELETE语句将删除表中的所有记录。

案例：

```sql
DELETE FROM empl
WHERE id = 1;
```

在删除数据时，也有可能会因为约束的影响，导致删除失败。这一些在后面约束那一节讲清楚。

**小结：**

DML操作默认情况下，执行完都会自动提交数据，如果希望执行完后不自动提交数据，则需要使用**`SET autocommit = FALSE;`**



# 四、MySQL8新特性：计算列

**什么是计算列？**

简单来说就是某一列的值是通过别的列计算得来的。例如，a列值为1，b列值为2，c列值不需要手动插入，定义a+b的结果为c的值，那么c就是计算列，是通过别的列计算得来的。

在MySQL8.0中，CREATE TABLE和ALTER TABLE中都支持增加计算列。下面以CREATE TABLE为例进行讲解。

举例：定义数据表tb1，然后定义字段id、字段a、字段b和字段c，其中字段c为计算列，用于计算a+b的值。首先创建测试表tb1，语句如下：

```sql
CREATE TABLE tb1(
	id INT,
	a INT,
	b INT,
	c INT GENERATED ALWAYS AS (a + b) VIRTUAL
);
```

此时，向数据表中插入数据：

```sql
INSERT INTO tb1(a, b) VALUES (100, 200);
```

查询数据表tb1中的数据，结果如下：

<img src=".\images\image-20240309233222173.png" align="left">

更新数据表的数据，语句如下：

```sql
UPDATE tb1 SET a = 500;
```

此时查询数据表tb1的结果：

<img src=".\images\image-20240309233825297.png" align="left">

即字段c的值，是随着a与b值的变化而变化的，永远会等于a+b。