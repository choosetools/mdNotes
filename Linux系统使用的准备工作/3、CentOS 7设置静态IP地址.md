centOS 7默认使用的是动态的IP地址，即IP地址不是固定的，每次启动的时候都会动态地生成一个，我们可以将IP地址设置成静态的，方便我们使用。

## 第一步 查看虚拟机的网络设置

在VMware中，依次点击"编辑"-->"虚拟网络编辑器"，如下图：

![image-20240325154917900](.\images\image-20240325154917900.png)

![image-20240325155049014](.\images\image-20240325155049014.png)

点击更改设置，进入到网络的设置中。

![image-20240325155226377](.\images\image-20240325155226377.png)

选择NAT模式。

为了能够使用静态IP，这里不要勾选"使用本地DHCP服务将IP分配给虚拟机"这个选项。

我们需要去在CentOS 7系统中配置IP地址。注意这里的`子网IP`和`子网掩码`，然后进入到NAT设置中，查看`网关IP`：

![image-20240325155553946](.\images\image-20240325155553946.png)

**注意：**

> * 虚拟机中配置的IP地址`IPADDR`，需要处于子网IP的范围内，例如上述的子网IP为192.168.138.0，则IPADDR应该在192.168.138.xxx这个范围。
> * 虚拟机中的子网掩码`NETMASK`，与虚拟网络编辑器中的子网掩码一致。
> * 在虚拟机中设置的网关地址`GATEWAY`，就是NAT设置中的网关IP。
> * DNS服务器可以随意填写，我这里填写与GATEWAY网关一样。



## 第二步 修改CentOS 7中网卡配置文件

centOS 7中，网卡的配置文件路径一般为：/etc/sysconfig/network-scripts/ifcfg-ens33，所以可以使用下面语句去修改：

```shell
vim /etc/sysconfig/network-scripts/ifcfg-ens33
```

默认设置如下，一般使用动态分配的方式，采用DHCP动态主机配置协议

```shell
BOOTPROTO=dhcp  # 动态分配ip地址
ONBOOT=no  # 开机自启用
```

修改为静态IP地址的方式如下：

```shell
BOOTPROTO=static
ONBOOT=yes
```

静态IP地址还需要在配置文件中添加指定的IP地址、子网掩码、网关和DNS服务器，整体修改后如下所示：

```shell
TYPE=Ethernet
BOOTPROTO=static  #设置静态Ip
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
NAME=ens33
UUID=4f40dedc-031b-4b72-ad4d-ef4721947439
DEVICE=ens33
ONBOOT=yes  #这里如果为no的话就改为yes，表示网卡设备自动启动

IPADDR=192.168.138.160  #配置ip，在第一步已经设置ip处于192.168.138.xxx这个范围，我就随便设为160了，只要不和网关相同均可
GATEWAY=192.168.138.2  #这里的网关地址就是第一步获取到的那个网关地址
NETMASK=255.255.255.0#子网掩码
DNS1=192.168.138.2#dns服务器1，填写你所在的网络可用的dns服务器地址即可，我这里用了网关地址
```

关键的参数我都使用了注解进行解释，其他的参数使用默认即可。



## 第三步 重启网卡服务

```shell
systemctl restart network.service
```

重启网络使配置生效，这个时候，就将CentOS 7设置成了静态的IP地址

我们去查看ip地址信息：

```shell
ifconfig
```

![image-20240325161028156](.\images\image-20240325161028156.png)

IP地址就为192.168.138.160



## 静态IP和动态IP的比较

* 静态IP主要是使用准确定位的
* 动态IP是方便管理，IP由服务器自动分配和回收，但性能会比静态稍微差一点。