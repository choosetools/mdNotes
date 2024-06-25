# 安装前说明

首先，我们需要去创建一个Linux的环境，请参考笔记：`Linux系统使用的准备工作`

在创建完一个CentOS 7的系统后，需要再对其进行克隆，具体请查看笔记：Linux系统使用的准备工作目录下的`4.Linux虚拟机的克隆（可选）.doxc`笔记

创建两个`CentOS 7`系统，分别命名为centOS7和centOS7 clone，在centOS7中安装MySQL8.0数据库，在centOS7 clone中安装MySQL5.7数据库。

安装好`Xshell`和`Xftp`等访问CentOS系统的工具

CentOS6和CentOS7在MySQL的使用中的区别

```
1、防火墙：6是iptables，7是firewalld

2、启动服务的命令：6是service，7是systemctl
```



## 检查是否安装过MySQL

* 如果使用的是rpm安装，检查一下RPM PACKAGE:

  ```shell
  rpm -qa | grep -i mysql
  ```

* 检查mysql service：

  ```shell
  systemctl status mysqld.service
  ```

* 如果存在mysql-libs的旧版本包，显示如下：

  ![image-20240325165146432](.\images\image-20240325165146432.png)

* 如果不存在mysql-lib的版本，显示如下：

  ![image-20240325165216512](.\images\image-20240325165216512.png)



## MySQL的卸载

1. **`关闭MySQL服务`**

   ```shell
   systemctl stop mysqld.service
   ```

2. **`查看当前MySQL安装状况`**

   ```shell
   rpm -qa | grep -i mysql
   
   #或
   
   yum list installed | grep mysql
   ```

   

3. **`卸载上述命令查询出的已安装程序`**

   ```shell
   yum remove mysql-xxx mysql-xxx mysql-xxx mysqk-xxxx
   ```

   务必删除干净，反复执行`rpm -qa | grep -i mysql`确认是否有卸载残留

4. **`删除mySQL相关文件`**

   * 查找相关文件

     ```shell
     find / -name mysql
     ```

   * 删除上述命令查找出的相关文件

     ```shell
     rm -rf xxx
     ```

5. **`删除my.cnf`**

   ```shell
   rm -rf /etc/my.cnf
   ```

   





# MySQL的Linux版安装

## 1、下载MySQL的指定版本

进入到MySQL官网中，下载MySQL8版本

![image-20240325214449488](.\images\image-20240325214449488.png)

由于是要给Linux系统下载MySQL数据库，所以，需要将下载的版本选择为Linux系统。

需要为CentOS 7下载MySQL数据库，但是这里并没有CentOS 7的选项，这里选择Red Hat Enterprise Linux/Oracle Linux，这是与CentOS 7兼容的。

然后再选择系统的版本

![image-20240325214710471](.\images\image-20240325214710471.png)

这里选择版本7，x86版本。

之后选择下载包的类型，这里选择的是`RPM Bundle`。

![image-20240325214749943](.\images\image-20240325214749943.png)

下载好的tar包，使用压缩工具解压，就可以得到很多的rpm文件。我们只需要其中的一部分安装包即可。

![image-20240325215024086](.\images\image-20240325215024086.png)

**将选中的文件，复制一份，使用Xftp软件，粘贴到CentOS 7系统的`/opt`目录下，这样一来，MySQL文件就复制到了CentOS 7系统中了。**



## 2、CentOS7检查MySQL依赖

### 2.1、检查/tmp临时目录权限（必不可少）

由于mysql安装过程中，会通过mysql用户在/tmp目录下新建tmp_db文件，所以请给/tmp较大的权限。执行：

```shell
chmod -R 777 /tmp
```

### 2.2、安装前，检查依赖

```shell
rpm -qa|grep libaio
```

如果存在libaio包，正常执行结果为：

![image-20240325215644714](.\images\image-20240325215644714.png)

```shell
rpm -qa|grep net-tools
```

如果存在net-tools包，正常执行结果为：

![image-20240325215757587](.\images\image-20240325215757587.png)

一般来说，只要正常安装CentOS 7系统，这些都是安装好的。









## 3、在CentOS 7下安装MySQL过程

#### 3.1、先将安装程序拷贝到/opt目录下

在Linux中，/opt目录就是用来存放安装的软件与程序的，所以需要先将下载的MySQL文件拷贝到/opt目录下进行安装操作。

由于之前，已经使用了Xftp软件，将下载的一部分MySQL文件拷贝到了/opt目录下，这里就不再阐述。

#### 3.2、在MySQL的安装目录下执行：

首先进入到/opt目录下：

```shell
cd /opt
```

然后，在/opt目录下，按照顺序，一步一步地，依次执行下述语句：

```shell
rpm -ivh mysql-community-common-8.0.25-1.el7.x86_64.rpm

rpm -ivh mysql-community-client-plugins-8.0.25-1.el7.x86_64.rpm
 
rpm -ivh mysql-community-libs-8.0.25-1.el7.x86_64.rpm

rpm -ivh mysql-community-client-8.0.25-1.el7.x86_64.rpm

rpm -ivh mysql-community-server-8.0.25-1.el7.x86_64.rpm
```

* `rpm`是Redhat Package Manage缩写，通过RPM的管理，用户可以把源代码包装成以rpm为扩展名的文件形式，易于安装。
* `-i`，--install安装软件包
* `-v`，--verbose提供更多的详细信息输出
* `-h`，--hash软件包安装的似乎列出哈希标志（和-v一起使用效果更好），展示进度条。



**注意：**

1. 必须要按照顺序，一步一步地执行语句，有可能下载的MySQL版本不一样，导致文件名不一致而报错误，上例中使用的是8.0.25版本的MySQL，根据实际使用的MySQL版本进行安装即可。
2. 如在检查工作时，没有检查mysql依赖环境，则在安装mysql-community-server会报错。

3. 实际上按照：**`common -> client-plugins -> libs -> client -> server`**的顺序进行执行。

4. 在安装`mysql-community-libs`时，有可能会报一个错误信息：

   ![image-20240325221522356](.\images\image-20240325221522356.png)

   使用命令：**`yum remove mysql-libs`**解决，清除之前安装过的依赖即可。



#### 3.3、查看MySQL版本

执行如下命令，如果成功表示安装mysql成功。类似java -version打出版本等信息

```shell
mysql --version
#或
mysqladmin --version
```

![image-20240325222525678](.\images\image-20240325222525678.png)

执行如下名，查看是否安装成功。需要增加-i表示不去区分大小写，否则搜索不到。

```shell
rpm -qa|grep -i mysql
```

![image-20240325222611635](.\images\image-20240325222611635.png)



#### 3.4、服务的初始化

为了保证数据库目录与文件的所有者为mysql登录用户，如果你是以root身份运行mysql服务，需要执行下面的命令进行初始化：

```shell
mysqld --initialize --user=mysql
```

说明：--initialize选项默认以"安全"模式来初始化，则会为root用户生成一个密码并`将该密码标记为过期`，登录后你需要去设置一个新的密码。生成的`临时密码`会往日志中记录一份。

查看密码：

```shell
cat /var/log/mysqld.log
```

![image-20240325223446114](.\images\image-20240325223446114.png)

root@localhost后面的就是初始化的密码，也就是#defH51j*rf4



#### 3.5、启动MySQL，查看状态

这个时候，就可以去登录MySQL数据库了，不过在此之前，需要确保MySQL服务已经启动。

**查看MySQL服务器状态：**

```shell
#加不加.service后缀都可以
systemctl status mysqld.service
```

![image-20240325223909087](.\images\image-20240325223909087.png)

`dead`就表示MySQL服务已经关闭停止了，此时我们就需要启动MySQL服务。

```shell
#加不加.service后缀都可以

启动：systemctl start mysqld.service

关闭：systemctl stop mysqld.service

重启：systemctl restart mysqld.service
```

启动后，我们再去查看mysql服务：

![image-20240325224236226](.\images\image-20240325224236226.png)

此时的MySQL服务就是running状态。

我们也可以通过查看系统进程的方式，查看到mysql服务是否启动：

```shell
ps -ef | grep -i mysql
```

![image-20240325224345946](.\images\image-20240325224345946.png)



#### 3.6、查看MySQL服务是否自启动

```shell
systemctl list-unit-files|grep mysqld.service
```

![image-20240325225724251](.\images\image-20240325225724251.png)

默认是enabled，表示MySQL服务自启动。

如果不是enabled，可以运行如下命令设置自启动：

```shell
systemctl enable mysqld.service
```

如果希望不进行自启动，运行如下命令设置：

```shell
systemctl disable mysqld.service
```

![image-20240325225843328](.\images\image-20240325225843328.png)





# MySQL登录

那么，CentOS 7系统的MySQL环境已经配置完毕了，接下来就是进行MySQL登录了。

## 1、首次登录

通过`mysql -hlocalhost -P3306 -uroot -p`进行登录，此时的密码是初始密码，也就是MySQL服务初始化时，生成的临时密码，可以使用`cat /var/log/mysqld.log`进行查看：

![image-20240325234635743](.\images\image-20240325234635743.png)

也就是#defH51j*rf4。

登录MySQL：

![image-20240325234836916](.\images\image-20240325234836916.png)



## 2、修改密码

因为初始化密码默认是过期的，所以查看数据库会报错。

**修改密码：**

```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY '新密码';
```

修改完成后，重新进入到MySQL数据库中，就可以正常地使用数据库了。





# MySQL远程登录

## 存在的问题

在使用SQLyog或者Navicat去远程连接MySQL数据库时，遇到了下面的报错信息，这是由于MySQL配置了不支持远程连接引起的。

<img src=".\images\image-20240326095118565.png" alt="image-20240326095118565" style="zoom: 67%;" />

## 远程登录步骤

### 1、确认网络

1. 在远程机器上使用ping ip地址`保证网络畅通`

2. 确保本地机器`开启telnet命令`：

![image-20240326100232717](.\images\image-20240326100232717.png)

![image-20240326100244902](.\images\image-20240326100244902.png)

![image-20240326100303664](.\images\image-20240326100303664.png)



### 2、关闭防火墙或开放端口

需要把本地（Windows系统)的防火墙关闭，这样才可以去连接远程机器。

然后，可以选择去关闭远程机器的防火墙或者开放远程机器的3306端口。

**`方式一：关闭远程机器防火墙`**

**查看防火墙状态**

```shell
systemctl status firewalld.service
```

**打开防火墙**

```shell
systemctl start firewalld
```

**关闭防火墙**

```shell
systemctl stop firewalld
```

**设置开机启动防火墙**

```shell
systemctl enable firewalld
```

**设置开机禁用防火墙**

```shell
systemctl disable firewalld
```

我们可以使用上面的命令，去设置远程机器关闭防火墙，这样就给了远程连接一个基础。



**`方式二：开放远程机器的端口`**（推荐）

远程连接，要么去关闭防火墙，要么在开启防火墙的基础上，开放MySQL数据库连接的端口3306，只让该端口允许进行访问。

**查看开放的端口号**

```shell
firewall-cmd --list-all
```

**设置开放的端口号**

```shell
firewall-cmd --add-service=http --permanent

firewall-cmd --add-port=3306/tcp --permanent
```

**重启防火墙**

```shell
firewall-cmd --reload
```



### 3、修改Linux配置

在默认的情况下，MySQL数据库是不允许root用户进行远程连接的，root用户在默认的情况下只允许进行本地连接。我们可以通过下面的操作查看：

```sql
use mysql;

select Host, User from user;
```

![image-20240326101152277](.\images\image-20240326101152277.png)

我们可以看到，root用户的当前主机配置信息为localhost，即root用户只允许localhost本地访问，不允许进行远程访问。

那如何进行修改？

* **`修改Host为通配符%`**

Host列指定了允许用户登录所使用的IP，比如user=root Host=192.168.1.1。这里的意思就是说root用户只能通过192.168.1.1的客户端去访问。user=root Host=localhost，表示只能通过本地客户端去访问。而`%`是个`通配符`，如果Host=192.168.138.%，那么就表示只要是IP地址前缀为"192.168.138."的客户端都可以连接。如果`Host=%`，表示所有IP都有连接权限。

注意：在生成环境下不能为了省事将host设置为%，这样做会存在安全问题，具体的可以根据生成环境的IP进行设置。

```sql
update user set host = '%' where user = 'root';
```

Host设置了'%'后便允许进行远程访问。

![image-20240326103216451](.\images\image-20240326103216451.png) 

Host修改完成后记得执行flush privileges使配置立即生效：

```sql
flush privileges;
```

这个时候，去连接远程数据库就连接成功了。

<img src=".\images\image-20240326101403182.png" alt="image-20240326101403182" style="zoom:67%;" /> 





### 可能存在的问题

如果是MySQL8的版本，连接时可能会出现如下的问题：

<img src=".\images\image-20240326103426142.png" alt="image-20240326103426142" style="zoom:80%;" />

配置新连接报错：错误号码2058，分析是mysql密码加密方法变了。

解决方法：Linux下mysql -uroot -p 登录mysql数据库，然后执行这条SQL语句：

```sql
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '密码';
```

然后再重新配置远程连接，即可连接成功。
