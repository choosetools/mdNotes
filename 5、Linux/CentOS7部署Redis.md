### 安装Redis依赖

Redis是基于C语言编写的，因此我们首先需要安装Redis所需的gcc依赖：

```shell
yum install -y gcc tcl
```

不过在使用yum命令之前，首先需要去配置yum的远程仓库（如果之间已经配置过则不需要再配置）：

```shell
yum -y install epel-release
```





### 上传安装包并解压

在安装好Redis的依赖后，我们就可以去安装Redis了，这里我们选择的Redis版本是6.2.6。

安装包已经下载到了lib目录下了，可以直接使用： [redis-6.2.6.tar.gz](lib\redis-6.2.6.tar.gz) 

我们将这个安装包，使用Xftp软件，将这个安装包复制到`/usr/local/src/`目录下，这个目录是用来存放用户编译软件的：

![image-20240618230307216](C:\Users\14036\Desktop\mdNotes\5、Linux\images\image-20240618230307216.png) 

 

然后，我们进入到这个目录，进行解压缩：

```shell
tar -xzf redis-6.2.6.tar.gz
```

解压完成后：

![image-20240618230602254](C:/Users/14036/Desktop/mdNotes/8、Redis/images/image-20240618230602254.png) 

可以看到，已经解压成功了。



然后，我们需要进入Redis的解压目录：

```shell
cd redis-6.2.6
```



之后，运行编译命令：

```shell
make && make install
```

执行结束后，此时redis就已经安装成功了。

如果出现了致命错误问题，则可以先去运行：

```shell
make MALLOC=libc
```

命令，执行完毕之后再来运行该命令。



默认的安装路径是在`/usr/local/bin`的目录下，让我们查看一下：

![image-20240618230931428](C:\Users\14036\Desktop\mdNotes\5、Linux\images\image-20240618230931428.png) 

该目录下，存在着很多以redis开头的命令，并且该目录已经默认配置到path环境变量中，所以可以在任意的目录下运行这些命令。其中：

* `redis-cli`：是redis提供的命令行客户端
* `redis-server`：是redis服务端启动脚本
* `redis-sentinel`：是redis的哨兵启动脚本



### 启动Redis

#### 默认启动方式

安装完成后，我们可以在任意目录输入redis-server命令启动Redis：

```shell
redis-server
```

如图：

![image-20211211081716167](C:\Users\14036\Desktop\mdNotes\5、Linux\images\image-20211211081716167.png)

但是，这种启动属于前台启动，会阻塞整个会话窗口，当前的会话就无法执行命令了，只能执行Redis，直到Redis停止，所以不推荐使用这种方式。（与部署MinIO时类似）





#### 部署Systemd

与MinIO一样，Redis也是未部署Systemd的，无法让Systemd帮助我们管理Redis服务，所以，我们需要去给Redis部署Systemd。

**编写Redis服务配置文件**

在/etc/systemd/system/目录下，创建一个redis.service文件：

```shell
vim /etc/systemd/system/redis.service
```

内容如下：

```ini
[Unit]
Description=redis-server
After=network.target

[Service]
Type=forking
ExecStart=/usr/local/bin/redis-server /usr/local/src/redis-6.2.6/redis.conf
PrivateTmp=true

[Install]
WantedBy=multi-user.target
```

保存并退出。



**修改Redis配置文件**

在编写Redis服务配置文件中，我们配置了Redis的启动服务是redis-server，Redis的配置文件是/usr/local/src/redis-6.2.6/redis.conf，那么我们首先去配置一下该配置文件的信息。

首先，进入到redis软件目录下：

```shell
cd /usr/local/src/redis-6.2.6/
```

然后，修改redis.conf文件的一些配置：

```shell
vim redis.conf
```

修改配置（可以根据实际情况进行修改）：

```ini
# 允许访问的地址，默认是127.0.0.1，会导致只能在本地访问。修改为0.0.0.0则可以在任意IP访问，生产环境不要设置为0.0.0.0
bind 0.0.0.0
# 守护进程，修改为yes后redis服务即可后台运行
daemonize yes 
# 密码，设置后访问Redis必须输入密码
requirepass 061535asd
# 监听的端口
port 6379
# 工作目录，默认是当前目录，也就是运行redis-server时的命令，日志、持久化等文件会保存在这个目录
dir .
# 数据库数量，设置为1，代表只使用1个库，默认有16个库，编号0~15
databases 1
# 设置redis能够使用的最大内存
maxmemory 512mb
# 日志文件，默认为空，不记录日志，可以指定日志文件名
logfile "redis.log"
```

那么，此时redis就配置完毕了。



然后重载系统服务：

```shell
systemctl daemon-reload
```



此时，我们就可以使用systemctl命令，来控制Redis了：

```shell
# 启动
systemctl start redis
# 停止
systemctl stop redis
# 重启
systemctl restart redis
# 查看状态
systemctl status redis

# 设置Redis自启动
systemctl enable redis
# 查看Redis是否开机启动
systemctl is-enabled redis
# 设置redis开机不启动
systemctl disabled redis
```

那么，我们将Redis设置为自启动。

