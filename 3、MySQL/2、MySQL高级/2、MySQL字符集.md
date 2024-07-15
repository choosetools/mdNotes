**在MySQL8.0版本之前**，默认字符集为**`latin1`**，uft8字符集指向的是`utf8mb3`.网站开发人员在数据库设计中往往会将编码修改为utf8字符集。如果忘记修改默认的编码，在往数据库中添加中文字符时，就会出现乱码的问题。

**从MySQL8.0开始**，数据库的默认编码修改为**`utf8mb4`**，从而避免出现乱码的问题。



# 一、字符集的操作

## 查看默认使用的字符集

```sql
show variables like 'character%';

或者

show variables like '%char%';
```

**在mysql8.0中执行：**

![image-20240326111051013](.\images\image-20240326111051013.png)

我们去查看一下创建的数据表使用的是什么字符集：

![image-20240326112115421](.\images\image-20240326112115421.png)



**在mysql5.7中执行：**

![image-20240326111205994](.\images\image-20240326111205994.png)

上述框起来的，就表示在创建数据库以及创建数据表时，默认使用的字符集就是latin1。

那么，在MySQL5.7中添加中文数据时，就会报错：

![image-20240326112149708](.\images\image-20240326112149708.png)

因为默认情况下，在MySQL5.7中，创建表使用的是**`latin1`**。如下：

![image-20240326112232049](.\images\image-20240326112232049.png)



## 修改默认字符集

该操作，可以去解决MySQL5.7版本中，因字符集latin1而造成了数据库中不能添加中文字符的问题。同时，也能够统一使用utf8字符集。

在Linux终端上，去修改Mysql的配置文件：`my.cnf`

```shell
vim /etc/my.cnf
```

在MySQL5.7或之前的版本中，在文件最后添加上中文字符集的配置：

```shell
character_set_server=utf8
```

![image-20240326112720757](.\images\image-20240326112720757.png)

修改好后，重启MySQL服务

```shell
systemctl restart mysqld
```

之后，查看MySQL5.7版本使用的字符集：

![image-20240326113143663](.\images\image-20240326113143663.png)

character_set_server表示的是MySQL服务器字符集。

character_set_database表示的是MySQL数据库使用的字符集。

即：**以上的修改，修改的是MySQL`服务器`和`数据库`默认使用的字符集。**

**注意：**

> 已经创建出来的数据库和数据表的字符集不会发生变化，修改只对新建的数据库生效，即`修改的是创建时默认使用的字符集`。
>
> 新建数据表时，若没有显示地声明字符集，默认使用的是数据库的字符集。

比如之前创建好的数据库test1：

![image-20240326115905027](.\images\image-20240326115905027.png)

依然使用的是latin1字符集，并且就算我修改字符集后再去test1中创建一个数据库test_emp，test_emp使用的字符集依旧是latin1：

![image-20240326120134472](.\images\image-20240326120134472.png)

为什么会出现这种情况？

原因在于：当我们去创建数据表时没有显示地给数据表设置字符集，数据表默认使用的是其所在的数据库的字符集。

创建test_emp表时，没有显示地去设置字符集，那么就默认使用test数据库的字符集latin1去创建。

那么，我们就有一个需求，需要对已有的数据库和数据表的字符集进行修改。



## 已有库与表的字符集的修改

上面的操作，仅仅只是去修改默认的字符集，已创建出来的库与表的字符集并不会发生改变。

MySQL5.7版本中，以前创建的库，创建的表字符集还是latin1。

![image-20240326113546644](.\images\image-20240326113546644.png)

* **修改已创建数据库的字符集**

  ```sql
  alter database 数据库名 character set 'utf8';
  ```

* **修改已创建数据表的字符集**

  ```sql
  alter TABLE 数据表名 convert to character set 'utf8';
  ```

![image-20240326120630807](.\images\image-20240326120630807.png)

此时，就可以往mysql5.7版本中添加中文字符的数据了。

> 注意：如果原有的数据使用的是非'utf8'编码的话，数据本身编码不会发生改变，已有数据需要导出或者删除，然后重新插入。





# 二、各级别的字符集

MySQL有4个级别的字符集和比较规则，分别是：

* 服务器级别
* 数据库级别
* 表级别
* 列级别

执行如下SQL语句：

```sql
show variables like 'character%';
```

![image-20240326124604243](.\images\image-20240326124604243.png)

* `character_set_server`：服务器级别的字符集
* `character_set_database`：当前数据库的字符集
* character_set_client：服务器解码请求时使用的字符集
* character_set_connection：服务器处理请求时会把请求字符串从character_set_client转为character_set_connection
* character_set_results：服务器向客户端返回数据时使用的字符集



### 1、服务器级别

* `character_set_server`：服务器级别的字符集

我们可以在启动服务器程序时通过启动项或者在服务器程序运行过程中使用`SET`语句修改这两个变量的值。比如我们可以在配置文件中这样写：

```shell
[server]
character_set_server=gbk #默认字符集
collation_server=gbk_chinese_ci #对应的默认比较规则
```

当服务器启动的时候读取这个配置文件后这两个系统变量的值便修改了。



### 2、数据库级别

* `character_set_database`：当前数据库的字符集

我们在创建和修改数据库的时候可以指定该数据库的字符集和比较规则，具体语法如下：

```sql
CREATE DATABASE 数据库名
	[[DEFAULT] CHARACTER SET 字符集名称]
 	[[DEFAULT] COLLATE 比较规则名称];
 	
ALTER DATABASE 数据库名
	[[DEFAULT] CHARACTER SET 字符集名称
```



### 3、表级别

我们也可以在创建和修改表的时候指定表的字符集和比较规则，语法如下：

```sql
CREATE TABLE 表名 (列的信息)
 	[[DEFAULT] CHARACTER SET 字符集名称]
 	[COLLATE 比较规则名称]]

ALTER TABLE 表名
	[[DEFAULT] CHARACTER SET 字符集名称]
     [COLLATE 比较规则名称]
```

**如果创建和修改表的语句中没有指明字符集和比较规则，将使用该表所在数据库的字符集和比较规则作为该表的字符集和比较规则。**



### 4、列级别

对于存储字符串的列，同一个表中不同的列也可以有不同的字符集和比较规则。我们在创建和修改列定义的时候可以指定该列的字符集和比较规则，语法如下：

```sql
CREATE TABLE 表名(
	列名 字符串类型 [CHARACTER SET 字符集名称] [COLLATE 比较规则名称],
	其他列...
);

ALTER TABLE 表名 MODIFY 列名 字符串类型 [CHARACTER SET 字符集名称] [COLLATE 比较规则名称];
```

**对于某个列来说，如果在创建和修改的语句中没有指明字符集和比较规则，将使用该列所在表的字符集和比较规则作为该列的字符集和比较规则。**

注意：

在转换列的字符集时需要注意，如果转换前列中存储的数据不能用转换后的字符集进行表示会发生错误。比如说原先列使用的字符集是utf8，列中存储了一些汉字，现在把列的字符集转换为ascii的话就会出错，因为ascii字符集并不能表示汉字字符。



### 5、小结

我们介绍的这4个级别字符集和比较规则的联系如下：

* 如果`创建或修改列`时没有显式的指定字符集和比较规则，则该列`默认用表的`字符集和比较规则。
* 如果`创建表时`没有显式的指定字符集和比较规则，则该表`默认用数据库的`字符集和比较规则。
* 如果`创建数据库时`没有显式地指定字符集和比较规则，则该数据库`默认用服务器的`字符集和比较规则。



### 开发中的经验

> **`实际上在开发中，使用的就是utf8字符集，不会使用其他的字符集。`**
>
> 所以：
>
> 如果在MySQL5.7之前，会先去修改服务器和数据库默认的字符集，改为utf8之后，就不会再去显示地声明字符集。
>
> 在MySQL8.0之后，直接使用数据库本身的字符集(utf8mb4)即可，不去显示地声明。





# 三、字符集与比较规则（了解）

## 1、utf8与utfmb4

`utf8`字符集表示一个字符需要使用1~4个字节，但是我们常用的一些字符使用1~3个字节就可以表示了。而字符集表示一个字符所用的最大字节长度，在某些方面会影响系统的存储和性能，所以设计MySQL的设计师偷偷定义了两个概念：

* **`utf8mb3`**：阉割过的utf8字符集，只使用1~3个字节表示字符。
* **`utf8mb4`**：正宗的utf8字符集，使用1~4个字节表示字符。

在MySQL中，`utf8`是`utf8mb3`的别称，所以实际上在MySQL5.7版本中，所谓的utf8字符集就是utf8mb3。

![image-20240326141218109](.\images\image-20240326141218109.png)

之后，在MySQL中提到utf8就意味着使用1~3个字节来表示一个字符，如果大家有使用4字节编码一个字符的情况，比如存储一些emoji标签，那请使用utf8mb4。

此外，通过如下指令可以查看MySQL支持的字符集：

```mysql
SHOW CHARSET;
#或
SHOW CHARACTER SET;
```

![image-20240326142116543](.\images\image-20240326142116543.png)

## 2、比较规则

上表中，MySQL版本一共支持41种字符集。

这些字符集中的字符是如何比较大小的呢？当我们进行排序的时候就需要用到这个比较规则。

上表中的**`Default collation`**列表示这一种字符集中一种默认的比较规则。

里面包含了该比较规则主要用于哪种语言，比如`utf8_polish_ci`表示以波兰语的规则比较，`utf8_general_ci`是一种通用的比较规则。

后缀表示该比较规则是否区分语言中的重音、大小写。具体如下：

| 后缀   | 英文释义           | 描述             |
| ------ | ------------------ | ---------------- |
| `_ai`  | accent insensitive | 不区分重音       |
| `_as`  | accent sensitive   | 区分重音         |
| `_ci`  | case insensitive   | 不区分大小写     |
| `_cs`  | case sensitive     | 区分大小写       |
| `_bin` | binary             | 以二进制方式比较 |

最后一列的**`Maxlen`**，它代表该种字符集表示一个字符最多需要几个字节。

**`常用操作1：`**

```sql
#查看GBK字符集的比较规则
SHOW COLLATION LIKE 'gbk%';

#查看UTF-8字符集的比较规则
SHOW COLLATION LIKE 'utf8%';
```

**`常用操作2：`**

```mysql
#查看服务器的字符集和比较规则
SHOW variables like '%_server';

#查看数据库的字符集和比较规则
SHOW VARIABLES LIKE '%_database';

#查看具体数据库的字符集
SHOW CREATE DATABASE 数据库名;

#修改具体数据库的字符集
ALTER DATABASE 数据库名 DEFAULT CHARSET SET 'utf8' COLLATE 
'比较规则';
```

**`常用操作3：`**

```sql
#查看表的字符集
SHOW CREATE TABLE 表名;

#查看表的比较规则
SHOW TABLE STATUS FROM 数据库名 LIKE '数据表名';

#修改表的字符集和比较规则
ALTER TABLE 表名 DEFAULT CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';
```





# 四、请求到响应过程中字符集的变化（了解）

| 系统变量                   | 描述                                                         |
| -------------------------- | ------------------------------------------------------------ |
| `character_set_client`     | 服务器解码请求时使用的字符集                                 |
| `character_set_connection` | 服务器处理请求时，会把请求字符串从character_set_client转为character_set_connection |
| `character_set_results`    | 服务器向客户端返回数据时使用的字符集                         |

这几个系统变量在MySQL8.0中的默认值如下：

![image-20240326144713236](.\images\image-20240326144713236.png)

![image-20240326162702828](.\images\image-20240326162702828.png)

![image-20240326162721672](.\images\image-20240326162721672.png)

![image-20240326162744681](.\images\image-20240326162744681.png)

![image-20240326162800633](.\images\image-20240326162800633.png)