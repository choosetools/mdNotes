# ssh：connnect to host github.com port 22:Connection refused

**问题现象**

本文以Windows系统为例进行说明，在个人电脑上使用Git命令来操作GitHub上的项目，本来都很正常，突然某一天开始，会提示如下错误`ssh: connect to host github.com port 22: Connection refused`。

```
$ git pull
ssh: connect to host github.com port 22:Connection refused
fatal: Could not read from remote repository.

Please make sure you have the correct access rights and the repository exists.
```

**排查思路**

`ssh: connect to host github.com port 22: Connection refused`这个错误提示的是链接`github.com`的22端口被拒绝了。

原本以为github.com挂了，但是浏览器访问github.com一切正常。

往上搜索这个报错，发现很多人遇到这个问题，大概有两个原因和解决方案：

## 方案一：**使用GitHub的443端口**

22端口可能被防火墙拼比了，可以尝试连接GitHub的443端口。

````
$ vim ~/.ssh/config
```
#Add section below to it
Host github.com
	Hostname ssh.github.com
	Port 443
```

$ ssh -T git@github.com
Hi xxxx! You've successfully authenticated, but GitHub dose not provide shell access.
````

这个解决方案的思路是：给~/.ssh/config文件里添加如下内容，这样ssh链接GitHub的时候就会使用443端口。

如果~/.ssh目录下没有config文件，新建一个即可。

修改完`~/.ssh/config`文件后，使用`ssh -T git@github.com`来测试和GitHub的网络通信是否正常，如果提示`Hi xxxxx! You've successfully authenticated, but GitHub does not provide shell access.`就表示一切正常了。

但是，当这个方案行不通时，需要使用其他方案。



## 方案二：**使用https协议，不要使用ssh协议**

在你的GitHub的本地repo目录，执行如下命令：

```
$ git config --local -e
```

然后把里面的url配置项从git格式

```
url = git@github.com:username/repo.git
```

修改为https格式

```
url = https://github.com/username/repo.git
```

这个其实修改的是repo根目录下的./git/config文件。

但是当这个方法也不生效时，使用以下的方法

## 方案三：修改DNS解析

网上的招都没用，只能自力更生了。既然和GitHub建立ssh连接的时候提示`connection refused`，那我们就详细看看建立ssh连接的过程中发生了什么，可以使用`ssh -v`命令，`-v`表示verbose，会打出详细日志。

```
$ ssh -vT git@github.com
OpenSSH_9.0p1, OpenSSL 1.1.1o  3 May 2022
debug1: Reading configuration data /etc/ssh/ssh_config
debug1: Connecting to github.com [::1] port 22.
debug1: connect to address ::1 port 22: Connection refused
debug1: Connecting to github.com [127.0.0.1] port 22.
debug1: connect to address 127.0.0.1 port 22: Connection refused
ssh: connect to host github.com port 22: Connection refused
```

从上面的信息马上就发现了诡异的地方，连接[http://github.com](https://link.zhihu.com/?target=http%3A//github.com)的地址居然是`::1`和`127.0.0.1`。前者是IPV6的localhost地址，后者是IPV4的localhost地址。

到这里问题就很明确了，是DNS解析出问题了，导致[http://github.com](https://link.zhihu.com/?target=http%3A//github.com)域名被解析成了localhost的ip地址，就自然连不上GitHub了。

Windows下执行`ipconfig /flushdns` 清楚DNS缓存后也没用，最后修改hosts文件，增加一条github.com的域名映射搞定。

```bash
140.82.113.4 github.com
```

查找[http://github.com](https://link.zhihu.com/?target=http%3A//github.com)的ip地址可以使用[https://www.ipaddress.com/](https://link.zhihu.com/?target=https%3A//www.ipaddress.com/)来查询，也可以使用`nslookup`命令

```bash
nslookup github.com 8.8.8.8
```

`nslookup`是域名解析工具，`8.8.8.8`是Google的DNS服务器地址。直接使用

```bash
nslookup github.com
```

就会使用本机已经设置好的DNS服务器进行域名解析，`ipconfig /all`可以查看本机DNS服务器地址。

这个问题其实就是DNS解析被污染了，有2种可能：

- DNS解析被运营商劫持了
- 使用了科学上网工具