#### 安装Redis

执行以下命令安装Redis：

```
yum --enablerepo=remi -y install redis
```

注：--enablerepo选项的作用为启用一个仓库

<img src=".\images\image-20240617130057868.png" alt="image-20240617130057868" style="zoom:67%;" /> 



#### 配置Redis允许远程访问

Redis服务默认只允许本地访问，若需要进行远程访问，需要进行配置。

修改Redis配置文件

```shell
vim /etc/redis/redis.conf
```

需要去修改bind参数，我们在命令行模式下，搜索bind参数：/bind：

<img src=".\images\image-20240617130409124.png" alt="image-20240617130409124" style="zoom: 67%;" /> 

默认只允许本地访问，我们将其修改为：

```ini
#监听所有网络接口，默认只监听localhost
bind 0.0.0.0
```

然后，我们要去关闭保护模式。Redis默认是开启保护模式的，开启保护模式后，远程访问必须进行认证才能访问。

在开发环境下，一般将远程保护关闭，我们只需要配置IP地址和端口号就可以访问远程的Redis了，但是在生产环境下，必须将远程保护打开，我们不仅需要配置IP地址和端口号，还需要配置验证信息。

也是在命令行模式下，输入/protected：

<img src=".\images\image-20240617130606057.png" alt="image-20240617130606057" style="zoom:67%;" /> 

将其值修改为no即可：

```ini
protected-mode no
```

 

#### 启动Redis

执行下面的命令启动Redis

```shell
systemctl start redis
```

执行以下的命令查看Redis的运行状态

```shell
systemctl status redis
```

执行以下的命令设置Redis开机启动

```shell
systemctl enable redis
```

查看Redis服务是否开机启动

```shell
systemctl list-unit-files|grep redis.service
```

