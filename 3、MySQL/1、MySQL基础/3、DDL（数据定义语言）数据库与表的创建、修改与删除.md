

凡是对表中字段的操作，都是使用**`ALTER TABLE 表名`**开头的。

比如：ALTER TBALE 表名 MODIFY是去修改表中的字段；ALTER TABLE 表名 ADD是去增加表的字段。

对数据库以及表、表中字段的删除都是使用`DROP`删除的；

对数据表的数据，使用的是`DELETE`删除。





# 一、基础知识

## 1、一条数据存储的过程

`存储数据是处理数据的第一步`。只有正确地把数据存储起来，我们才能进行有效的处理和分析。否则，只能是一团乱麻，无从下手。

那么，怎样才能把用户各种经营相关的、纷繁复杂的数据，有序、高效地存储起来呢？在MySQL中，一个完整的数据存储过程总共有4步，分别是创建数据库、确认字段、创建数据表、插入数据。

![image-20240309004117678](.\images\image-20240309004117678.png)

我们要先创建一个数据库，而不是直接创建数据表呢？

因为从系统架构的层次上看，MySQL数据库系统从大到小依次是`数据库服务器`、`数据库`、`数据表`、数据表的`行与列`。

MySQL数据库服务器之前已经安装。所以，我们就从创建数据库开始。

## 2、标识符命名规则

* 数据库名、表名不得超过30字符，变量名限制为29个。
* 必须只能包含A-Z，a-z，0-9，_共63个字符
* 数据库名、表名、字段名等对象名中间不要包含空格
* 同一个MySQL软件中，数据库不能同名；同一个库中，表不能重名；同一个表中，字段不能重名
* 必须保证你的字段和保留字、数据库系统或常用方法冲突。如果坚持使用，请在SQL语句中使用`（着重号）引起来
* 保持字段名和类型的一致性：在命名字段并为其制定数据类型的时候，一定要保证一致性，假如数据类型在一个表是整数，在另一个表里可就别变成字符型了。



## 3、MySQL中数据类型的简单介绍（后面的笔记中有详细介绍）

常用的几种数据类型介绍如下：

| 数据类型      | 描述                                                         |
| ------------- | ------------------------------------------------------------ |
| INT           | 从-2^31到2^31-1个整形数据。存储大小为4个字节                 |
| CHAR(size)    | 定长字符数据。若未指定，默认为1个字符，最大长度255           |
| VARCHAR(size) | 可变长字符数据，根据字符串实际长度保存，**`必须指定长度`**   |
| FLOAT(M, D)   | 单精度，占用4个字节，M=整数位+小数位，D=小数位。D<=M<=255，0<=D<=30，默认M+D<6 |
| DOUBLE(M, D)  | 双精度，占用8个字节，D<=M<=255,0<=D<=30，默认M+D<=15         |
| DECIMAL(M,D)  | 高精度小数，占用M+2个字节，D<=M<=65,0<=D<=30，最大取值范围与DOUBLE相同 |
| DATE          | 日期类型数据，格式'YYYY-MM-DD'                               |
| BLOB          | 二进制形式的长文本数据，最大可达4G                           |
| TEXT          | 长文本数据，最大可达4G                                       |





# 二、创建和管理数据库

## 1、创建数据库 -> CREATE

* **方式一**：创建数据库

  ```sql
  CREATE DATABASE 数据库名；
  ```

* **方式二**：创建数据库并指定数据库的字符集

  ```sql
  CREATE DATABASE 数据库名 CHARACTER SET 字符集;
  ```

* **方式三**：判断数据库是否已经存在，不存在则创建数据库`（推荐）`

  ```sql
  CREATE DATABASE IF NOT EXISTS 数据库名;
  ```

注意：

> * **创建数据库时，未指定字符集，使用的就是数据库默认的字符集（`utf8mb3`）。**
>
> * **DATABASE不能改名**。一些可视化工具可以改名，它是重新建库，把所有表复制到新库，再删除旧库完成的。

我们去查看一下创建数据库的信息：

```sql
SHOW CREATE DATABASE atguigudb;
```

![image-20240309110313075](.\images\image-20240309110313075.png)

可以看到，在默认的情况下，创建数据库使用的是utf8mb3字符集创建的。

由于不能存在相同名称数据库，所以，当未使用第三种方式创建一个同名的数据库时，会报错。所以，推荐使用第三种方式去创建数据库，当已经存在同名的数据库时，忽略该语句，不再创建数据库。

当然，在创建新库的时候，可以既去判断库是否已存在，同时去设定字符集，例如：

```sql
CREATE DATABASE IF NOT EXISTS mytest CHARACTER SET 'utf8';
```



## 2、查看数据库的信息

* **查看当前所有的数据库**

  ```sql
  SHOW DATABASES;#有一个s，代表多个数据库
  ```

* **查看当前正在使用的数据库**

  ```sql
  SELECT DATABASE();
  ```

* **查看当前库下的所有表**

  ```sql
  SHOW TABLES;
  #后面跟上`FROM 数据库名`可以查看指定库下的所有表
  ```

* **使用/切换数据库**

  ```sql
  USE 数据库名;
  ```

* **查看数据库的创建信息**

  ```sql
  SHOW CREATE DATABASE 数据库名;
  ```



## 3、修改数据库 -> ALTER

一般来说，不会对数据库进行修改，因为修改数据库会涉及到数据库下几百个数据表的修改，一般也就是刚刚创建完数据库的时候去修改一下字符集。

* **修改数据库字符集**

  ```sql
  ALTER DATABASE 数据库名 CHARACTER SET 字符集;#比如：gdk、utf8等
  ```



## 4、删除数据库 -> DROP

* **方式一**：删除指定的数据库

  ```sql
  DROP DATABASE 数据库名
  ```

* **方式二**：删除指定的数据库（`推荐`）

  ```sql
  DROP DATABASE IF EXISTS 数据库名;
  ```

当数据库不存在时，使用第一种方式会报错，所以推荐使用第二种方式删除数据库，第二种方式有一个判断，只有当数据库实际存在时才会去删除，数据库不存在时忽略该语句。





# 三、创建和管理表

## 1、创建表 -> CREATE TABLE

创建表时，需要当前用户具备创建表的权限。我们在后续学习的时候，会去学习如何给权限，以及可以去创建一些角色。

### 1.1、创建方式1（手动创建）

* **`必须具备：`**

  * 创建表的权限
  * 存储空间

* **`语法格式：`**

  ```sql
  CREATE TABLE [IF NOT EXISTS] 表名(
  	字段1, 数据类型 [约束条件] [默认值],
      字段2, 数据类型 [约束条件] [默认值],
  	字段3, 数据类型 [约束条件] [默认值],
  	...
  	[表约束条件]
  )[CHARACTER SET 字符集];
  ```

* **`必须指定：`**
  * 表名
  * 列名（或字段名），数据类型，长度
* **`可选指定：`**
  * 约束条件
  * 默认值

> * 加上了`IF NOT EXISTS`关键字，则表示：如果当前数据库中不存在要创建的数据表，则创建数据表；如果当前数据库中已经存在要创建的数据表，则忽略建表语句，不再创建数据表。
>
> * 使用VARCHAR来定义字符串时，`必须在使用VARCHAR时指明其长度`。

案例：

```sql
CREATE TABLE IF NOT EXISTS myempl (
	id INT,
	emp_name VARCHAR(15),
	hire_date DATE
);
```

使用DESC查看表结构

```sql
DESC myempl;
```

![image-20240309110620630](.\images\image-20240309110620630.png)

使用SHOW查看创建表语句：

```sql
SHOW CREATE TABLE myempl;
```

![image-20240309110851048](.\images\image-20240309110851048.png)

从上述查询结果可以看出，数据库的存储引擎是InnoDB，字符集是utf8mb3.

**为什么表的字符集使用的是utf8mb3？**

原因在于，当我们在创建表时，`如果没有显示地去声明其字符集，默认使用的就是数据库的字符集`，数据库自身字符集是utf8mb3，那么表的字符集也就是utf8mb3.

可以在创建表的时候给表设置字符集，这样的话就可以不会去使用数据库的字符集：

```sql
CREATE TABLE my_empl(
	id INT,
	`name` VARCHAR(15)
)CHARACTER SET 'gbk';
```



甚至，在创建表时，还可以显示地指明字段的字符集：

```sql
CREATE TABLE templ(
	id INT,
	`name` VARCHAR(15) CHARACTER SET 'utf8'
);
```

所以，我们对上述字符集进行**总结**：

> 1. 可以对数据库、数据表以及表中的字段都设置指定的字符集。
> 2. 当未给表字段显示地设置字符集时，默认使用的是表中的字符集；当未给表显示地设置字符集时，默认使用的是数据库的字符集；当未给数据库显示地设置字符集时，默认使用的是`utf8mb3`。

### 1.2、创建方式2（基于现有表）

这种方式是基于现有的表去创建新的表。

* **格式：**

  ```sql
  CREATE TABLE 表名
  AS
  查询语句;
  ```

* 这种方式是从现有的表中，通过查询的方式获取表的字段信息，放入到新表中。

* **`这种方式将创建表和插入数据结合起来`**，即，不仅仅是将查询结果的列放入新表中，结果集也会放入到新表中。

* 这种方式不会把现有表的约束（NOT NULL除外）复制，只会复制字段和数据。

* 查询语句中字段的别名，作为新创建表的字段的名称。

说明：

> 1. 查询语句中字段的别名，会作为新创建表的字段名。
> 2. 此时的查询语句，可以结构比较丰富，使用前面章节讲过的各种SELECT语句。
> 3. 当我们只想复制表的结构，不想要表中数据时，可以使用不成立的WHERE子句进行筛选，将所有数据都筛选出去，比如WHERE 1 = 2;



**案例1：**创建一个表myemp3，包含employees中的员工信息和对应的部门名称，列名分别为emp_id，lname和department_name

```sql
CREATE TABLE myemp3
AS
SELECT e.employee_id emp_id, e.last_name lname, d.department_name
FROM employees e INNER JOIN departments d
ON e.department_id = d.department_id;
```

查询语句的结果是：

![image-20240309112713096](.\images\image-20240309112713096.png)

执行创建表的语句，然后去查看表信息：

```sql
DESC myemp3;
```

![image-20240309112840482](.\images\image-20240309112840482.png)

查询结果集中，列的别名会作为创建表的列名。

该表中的数据：

<img src=".\images\image-20240309113208254.png" align="left">

会将查询结果中的数据一起复制到创建的新表中。



**案例2：**创建一个表employees_blank，实现对employees表中复制，不包括表数据

```sql
CREATE TABLE employees_blank
AS
SELECT *
FROM employees
WHERE 1 = 2;
```

使用WHERE 1 = 2这样不成立的WHERE子句，将所有查询到的结果全部筛选掉，这样就只去复制了表的结构，没有复制表的数据，得到一个空的新表。

![image-20240309114905802](.\images\image-20240309114905802.png)

查看表的结构：

```sql
DESC employees_blank;
```

![image-20240309115004472](.\images\image-20240309115004472.png)



## 2、查看表的结构

在MySQL中创建好数据表之后，可以查看数据表的结构。MySQL支持使用**`DESCRIBE/DESC`**语句查看数据表结构，也支持使用**`SHOW CREATE TABLE`**语句查看数据表结构。

语法格式如下：

```sql
DESCRIBE|DESC 表名;

SHOW CREATE TABLE 表名;
```

使用SHOW CREATE TABLE 表名的执行案例：

![image-20240309115741297](.\images\image-20240309115741297.png)

使用SHOW CREATE TABLE语句不仅可以查看表创建时的详细语句，还可以查看存储引擎和字符编码。

## 3、修改表 -> ALTER TABLE

修改表指的是修改数据库中已经存在的数据表的结构。

**`使用ALTER TABLE语句可以实现：`**

* 向已有的表中添加列
* 修改现有表中的列
* 删除现有表中的列
* 重命名现有表中的列

### 3.1、追加一个列

**语法格式如下**：

```sql
ALTER TABLE 表名 
ADD [COLUMN] 字段名 字段类型 [FIRST|AFTER 字段名]
```

> **默认添加到表中的最后一个字段的位置。**
>
> **当后面跟上`FIRST`，可以用于表示放在表中的第一个字段；或跟上`AFTER + 字段名`时，表示往该字段后的位置添加。**

案例：

往表myempl表中添加字段，该表中已有字段如下所示：

<img src=".\images\image-20240309134845686.png" align="left">

往其中添加一个phone_number字段，添加到emp_name之后，可以写成：

```sql
ALTER TABLE myempl
ADD phone_number VARCHAR(20) AFTER emp_name;
```

执行成功后，myempl表的结构就变成了：

<img src=".\images\image-20240309135126404.png" align="left">

### 3.2、修改一个列

* 可以修改列的**`数据类型`，`长度`、`默认值`和`位置`**。一般不会去修改字段的数据类型。

* **语法格式**如下：

  ```sql
  ALTER TABLE 表名
  MODIFY [COLUMN] 字段名1 字段类型 [DEFAULT 默认值] [FIRST | AFTER 字段名2]
  ```

举例：

```sql
ALTER TABLE myempl
MODIFY emp_name VARHCAR(25) DEFAULT 'aaa';
```

**注意：**

> 对默认值的修改不会影响到之前的数据，只会影响今后表中的数据。



### 3.3、重命名一个列

使用`CHANGE 旧列名 新列名子句 新数据类型`重命名列，**语法格式如下**：

```sql
ALTER TABLE 表名
CHANGE [COLUMN] 列名 新列名 新数据类型;
```

重命名列的时候，也可以对列的数据类型进行修改。

案例：

```sql
ALTER TABLE myempl
CHANGE email my_email VARCHAR(50); 
```

执行后，数据表中的email列变成了my_email，同时数据类型也变为了VARCHAR(50)。

### 3.4、删除一个列

删除表中某个字段的语法格式如下：

```sql
ALTER TABLE 表名
DROP [COLUMN] 字段名;
```

案例：

```sql
ALTER TABLE myempl
DROP my_email;
```

这就会去myempl表中删除my_email字段；如果不存在该字段，则报错。

## 4、重命名表 -> RENAME TABLE

* **方式一`（推荐使用）`：**

  ```sql
  RENAME TABLE 旧表名 TO 新表名;
  ```

* **方式二：**

  ```sql
  ALTER TABLE 旧表名
  RENAME [TO] 新表名; --[TO]可以省略
  ```

## 5、删除表 -> DROP TABLE

删除表不光将表结构删除，同时表中给定数据也删除掉，释放表空间。

**语法：**

```sql
DROP TABLE [IF EXISTS] 数据表1 [, 数据表2,...,数据表n];
```

`IF EXISTS`的含义为：如果当前数据库中存在相应的数据表，则删除数据表；如果当前数据库中不存在相应的数据表，则忽略删除语句，不再执行删除数据表的操作。

> * 在MySQL中，当一张数据表`没有与其他任何数据表形成关联关系`时，可以将当前数据表直接删除。
> * 数据和结构都被删除
> * 所有正在运行的相关事务被提交
> * 所有相关索引被删除
> * DROP TABLE语句无法回滚

案例：

```sql
DROP TABLE IF EXISTS myemp4;
```



## 6、清空表 -> TRUNCATE

清空表，表示清空表中的所有数据，但是表结构保留。

* **语法：**

  ```sql
  TRUNCATE TABLE 表名;
  ```

* TRUNCATE语句**`不能回滚`**，而使用DELETE语句删除数据，可以回滚。

> TRUNCATE TABLE比DELETE速度快，且使用的系统和事务日志资源少，但TRUNCATE无事务且不触发TRIGGER，有可能造成事故，故**不建议在开发代码中使用此语句**。
>
> **说明：**TRUNCATE TABLE在功能上与不带WHERE子句的DELETE语句相同。



使用案例：

首先我们去查询employees_copy表：

```sql
SELECT *
FROM employees_copy;
```

执行结果：

![image-20240309143024470](.\images\image-20240309143024470.png)

然后去清空这张表：

```sql
TRUNCATE TABLE employees_copy;
```

之后再去查询，得到的结果为：

![image-20240309143107919](.\images\image-20240309143107919.png)

去查看这个表的结构：

```sql
DESC employees_copy;
```

![image-20240309143152781](.\images\image-20240309143152781.png)

即表的结构没有被删除，只是数据被清空了。





# 四、COMMIT和ROLLBACK的理解

* **`COMMIT`**：**提交数据**。一旦执行COMMIT，则数据就被永久的保存在数据库中，意味着数据不可以回滚。
* **`ROLLBACK`**：**回滚数据**。一旦执行ROLLBACK，则可以实现数据的回滚，回滚到最近的一次COMMIT之后。

比如说，我提交了一次数据，得到C1；之后又提交了一次数据，得到C2；之后进行了一系列操作，结果为C3。此时，我去回滚数据，那么最终只能回滚到C2，因为提交数据后，数据已经被永久保存了，不可再进行回滚。

由此，我们可以根据这两个的理解，对比一下TRUNCATE和DELETE的区别。



### DDL 和 DML 的说明

* `DDL`（对数据库和表结构的操作）：一旦执行，就不可以回滚。原因在于**DDL在执行完操作后，一定会去提交数据**，并且这个commit操作不会受到autocommit数据的影响，所以无法进行回滚。

* `DML`（对表中数据的操作）：默认情况下，一旦执行，也是不可以回滚的。但是，如果在执行DML之前，执行了**`SET autocommit = FALSE`**，则执行的DML操作（增删改）就可以实现回滚了。

系统默认情况下是自动提交的，即当我们去对数据库中的数据实现增删改操作时，自动提交数据，此时，我们就不可以回滚；当我们去设置autocommit为false时，此时不会去自动提交数据，就可以对数据的修改进行回滚操作。

但是，对于DDL来说，数据库以及表结构的修改一旦执行，就不可回滚，与autocommit无关。



### 对比TRUNCATE TABLE和DELETE FROM

> * **相同点：**
>   * 都可以实现对表中数据的删除，同时保留表结构。
>
> * **不同点：**
>
>   * `TRUNCATE TABLE`：一旦执行此操作，表数据全部清除。同时，数据是不可以回滚的。
>
>   * `DELETE FROM`：一旦执行此操作，表数据可以全部清除（不带WHERE）。数据是可以实现回滚的（设置autocommit = false）。

**演示**：分别对DELETE FROM 和TRUNCATE TABLE进行演示

DELETE FROM

首先先去COMMIT提交数据

```sql
COMMIT;
```

然后设置autocommit = false，防止其自动提交数据

```sql
SET autocommit = false;
```

之后，使用DELETE FROM删除数据表中的所有数据

```sql
DELETE FROM employees_copy;
```

此时，去查询employees_copy表中的数据，查询结果为：

![image-20240309164114112](.\images\image-20240309164114112.png)

可以发现，数据已经全部被删除了。然后此时使用ROLLBACK进行回滚

```sql
ROLLBACK;
```

此时，再去查询，得到的结果为：

![image-20240309164223000](.\images\image-20240309164223000.png)

即DELETE FROM操作被回滚了，回滚到删除之前。



TRUNCATE

假设使用TRUNCATE TABLE进行删除操作，也依旧按照上述步骤进行：

```sql
COMMIT;

SET autocommit = false;

TRUNCATE TABLE employees_copy;

ROLLBACK;
```

此时，再去查询employees_copy表中的数据，查询的结果为：

![image-20240309164524959](.\images\image-20240309164524959.png)

即此时，并没有回滚到清空数据表之前，所以得出结论：TRUNCATE TABLE不支持回滚操作。

为什么会出现这样的情况呢？

> **`因为在执行完DDL操作后，一定会去提交数据，而且此操作不受到autocommit的影响，所以无法进行回滚。`**





# 五、内容扩展

## 扩展1：阿里巴巴《Java开发手册》之MySQL字段名

* 【`强制`】表名、字段名必须使用小写字母或数字，禁止出现数字开头，禁止两个下划线中间只出现数字。数据库字段名的修改代价很大，因为无法进行预发布，所以字段名称需要慎重考虑。
  * 正例：aliyun_admin, rdc_config, level3_name
  * 反例：AliyunAdmin, rdcConfig, level_3_name
* 【`强制`】禁用保留字，如desc、range、match、delayed等。
* 【`强制`】：表必备三字段：id、gmt_create、gmt_modified。
  * 说明：其中id必为主键，类型为BIGINT UNSIGNED、单表时自增、步长为1。gmt_create，gmt_modified的类型均为DATETIME类型，前者现在时表示主动式创建，后者过去分词表示被动式更新
* 【`强制`】表的命名最好是遵循“业务名称_表的作用”
  * 正例：alipay_task、force_project、trade_config
* 【`推荐`】库名与应用名称尽量一致。
* 【参考】合适的字符存储长度，不但节约数据库表空间、节约索引存储，更重要的是提升检索速度。





## 扩展2：如何理解清空表、删除表等操作需谨慎？

`表删除`操作将把表的定义和表中的数据一起删除，并且MySQL在执行删除操作时，不会有任何的确认信息提示，因此执行删除操作时应当慎重。在删除表前，最好对表中的数据进行`备份`，这样当操作失误时可以对数据进行恢复，以免造成无法挽回的后果。

同样地，在使用`ALTER TABLE`进行表的基本修改操作时，在执行操作过程之前，也应该确保对数据进行完整的`备份`，因为数据库的改变是`无法撤销`的，如果添加了一个不需要的字段，可以将其删除；相同的，如果删除一个需要的列，该列下的所有数据都将会丢失。

## 扩展3：MySQL8新特性——DDL的原子化

在MySQL8.0版本中，InnoDB表的DDL支持事务完整性，即`DDL操作要么成功要么回滚`。DDL操作回滚日志写入到data dictionary数据字典白哦mysql.innodb_ddl_log（该表是隐藏的表，通过show tables无法看到）中，用于回滚操作，通过设置参数，可将DDL操作日志打印输出到MySQL错误日志中。

事务是一个或多个操作构成的，要么都成功执行，要么都失败回滚，这就是事务的特性。

在MySQL8之后，DDL操作本身也是一个事务，要么全部成功执行，要么失败回滚。

案例：

分别在MySQL5.7和MySQL8.0版本中创建数据表：

```sql
CREATE TABLE book1(
	book_id INT,
	book_name VARCHAR(255)
);
```

此时分别去执行DDL操作的删除表操作：

```sql
DROP TABLE book1, book2;
```

由于此时数据库中只有book1表，没有book2表，所以执行该操作会报错。

但是对于MySQL5.7来说，报错后查看数据库，发现数据库中book1表还是被删除了。

对于MySQL8.0来说，报错后查看数据库，数据库中的book1表没有被删除。

这就和事务有关了，MySQL5.7的删除表操作整体并不是一个事务，前面执行成功的操作会被直接提交。

由上述两个结果可知，MySQL8之前，DDL操作不是事务，当执行失败时，前面成功执行的操作也会被COMMIT提交；在MySQL8之后，当DDL操作失败时，之前的执行成功的操作也会被回滚。
