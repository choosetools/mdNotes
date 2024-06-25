CentOS 7完成配置之后，通过root登录，进入到的是命令行界面

![image-20240325122221150](.\images\image-20240325122221150.png)

查看centOS 7的默认启动模式，命令行输入：`systemctl get-default`

查看默认启动模式的结果显示

```shell
#命令行启动模式
multi-user.target
#图形化界面启动模式
graphical.target
```

查看centOS 7系统支持的启动模式：`cat /etc/inittab`

![image-20240325152242751](.\images\image-20240325152242751.png)

可设置的centOS 7系统默认启动模式

```shell
#设置为图形化界面模式
systemctl set-default graphical.target
#设置为命令行模式
systemctl set-default multi-user.target
```

设置centOS 7默认为图形化界面启动

![image-20240325152352867](.\images\image-20240325152352867.png)

配置网卡信息，使得虚拟机能够连接外网

```shell
#进入系统网卡配置文件
cd /etc/sysconfig/network-scripts/
#找到ifcfg-ens33文件，进行编辑
vi ifcfg-ens33
#修改启动设备参数为yes
ONBOOT=yes
#增加DNS配置信息
DNS1=8.8.8.8
DNS2=4.2.2.2
#编辑后保存退出
:wq
```

![image-20240325152727707](.\images\image-20240325152727707.png)

重启网卡服务，ping外网能够连通

注意：首先确保虚拟机网络能够访问外网

```shell
#重启网卡服务
systemctl restart network
#ping外网测试连通性
ping www.baidu.com
```

![image-20240325152959900](.\images\image-20240325152959900.png)

检查yum命令是否支持

```shell
#检查yum命令
yum -h
```

![image-20240325153035166](.\images\image-20240325153035166.png)

通过yum命令获取并且安装图形化界面GNOME的程序包

```shell
#通过yum命令获取资源并安装图形化界面包，直到complete
yum groupinstall "GNOME Desktop" "Graphical Administration Tools"
```

遇到需要输入的，输入y表示确定

![image-20240325153306128](.\images\image-20240325153306128.png)

检查默认启动方式，重启centOS 7

```shell
#查看默认启动方式是否是图形化界面
systemctl get-default
#重启centOS 7
reboot
```

![image-20240325153419526](.\images\image-20240325153419526.png)

重启完毕后进入桌面模式，设定账号密码直接登录

![image-20240325153448646](.\images\image-20240325153448646.png)
