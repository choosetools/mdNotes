# 面试常考

* Cookie可以存储在浏览器或者本地，Session只能存在服务器。
* Session可以存储任意的Java对象，Cookie只能存储String类型的对象。
* Session比Cookie更具有安全性（Cookie有安全隐患，通过拦截或本地文件找到你的Cookie后可以进行攻击）
* Session占用服务器性能，Session过多，增加服务器的压力。
* 单个Cookie保存的数据不能超过4K，很多浏览器都限制一个站点最多保存20个Cookie，Session是没有大小限制的，和服务器的内存大小有关。





# 一、Cookie详解

> 1. **Cookie是什么？**

Cookie，有时也用其复数形式Cookies。类型为“小型文本文件”，`是网站为了识别用户身份`，进行Session跟踪而存储在用户本地终端上的数据（通常经过加密），`由用户客户端计算机暂时或永久保存的信息`。



> 2. **为什么要使用Cookie？解决了什么问题？**

web程序是使用Http协议传输的，而Http协议是无状态的协议，对于事务处理没有记忆能力。缺少状态意味着如果后续需要处理前面的信息，它必须重传，这样可能导致每次连接传送的数据量增大。另一个方面，在服务器不需要先前信息时它的应答就较快。

**cookie的出现就是为了解决这一问题的。**

`在用户登录后，服务器会使用Session保存用户的信息，然后将Session的Id放在Cookie中返回给浏览器，浏览器保存在本地。当用户再次发送请求时，会自动把浏览器中存储的Cookie自动携带给服务器，服务器通过Cookie中的SessionId就能判断用户是哪个了。`

**特点**：Cookie存储的数据量有限，不同的浏览器有不同的存储大小，但一般不超过4KB。因此使用Cookie只能存储一些小量的数据。



> 3. **Cookie什么时候产生？**

Cookie的使用先看需求，因为浏览器可以禁用Cookie，同时服务端也可以不Set-Cookie。

`客户端向服务器端发送一个请求时，服务端向客户端发送一个Cookie然后浏览器将Cookie保存。`

Cookie有两种保存方式，一种是浏览器会将Cookie保存在内存中，还有一种保存在客户端的硬盘中，之后每次Http请求浏览器都会将Cookie发送给服务器端。

![在这里插入图片描述](.\images\31241234.png)

> 4. **Cookie的生存周期？**

Cookie在生成时，就会指定一个Expire值，这就是Cookie的生成周期，在这个周期内Cookie有效，超出周期Cookie就会被清除。有些页面将Cookie的生存周期设置为"0"或负值，这样在关闭浏览器时，就会马上清除Cookie，不会记录用户信息，更加安全。



> 5. **Cookie有哪些缺陷？**

* ①**`数量受到限制`**。一个浏览器能创建的Cookie数量最多为300个，并且每个超过4KB，每个Web站点能设置的Cookie总数不能超过20个。
* ②**`安全性无法得到保证`**。通常跨站点脚本攻击往往利用网站漏洞在网站页面中植入脚本代码或网站页面利用第三方脚本代码，均存在跨站点脚本攻击的可能，在受到跨站点脚本攻击时，脚本指令将会读取当前站点的所有Cookie内容（已不存在Cookie作用域限制）。
* ③**`浏览器可以禁用Cookie`**，禁用Cookie后，就无法享有Cookie带来的便利。



> 6. **Cookie的应用场景**

![在这里插入图片描述](.\images\1237818927398.png)









# 二、Session详解

> 1. **Web中什么是会话？**

用户开一个浏览器，点击多个超链接，访问服务器多个Web资源，然后关闭浏览器，整个过程称之为会话。



> 2. **什么是Session？**

Session：在计算机中，尤其是网络应用中，称之为"`会话控制`"。Session对象存储特定用户会话所需的属性及配置信息。



> 3. **Session什么时候产生？**

* 当用户请求来自于应用程序的Web页时，如果该用户还没有会话，则Web服务器将自动创建一个Session对象。
* 这样，当用户在应用程序的Web页之间跳转时，存储在Session对象中的对象将不会丢失，而是在整个用户会话中一直存在下去。

![在这里插入图片描述](.\images\watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NTM5MzA5NA==,size_16,color_FFFFFF,t_70)

* 服务器会向客户端浏览器发送一个每个用户特有的会话编号SessionID，让它进入到Cookie中。

![在这里插入图片描述](.\images\7129398172387.png)

* 服务器同时也会把SessionID和对应的用户信息、用户操作记录在服务器上，这些记录就是Session，再次访问时会将浏览器中保存的Cookie发送给服务器，其中就包含了SessionID。

![在这里插入图片描述](.\images\7198328917238972.png)

* 然后服务器，就会根据Cookie中包含的SessionId，找到服务器中对应的Session，Session中包含了用户的信息，这样一来，就实现了登录验证的功能。有些请求是必须在登录之后，才能完成，所以实现了登录验证，就可以去处理这些请求并返回数据给客户端。

![在这里插入图片描述](.\images\123789127398.png)



> 4. **Session的生命周期**

根据需求设定，默认是30分钟过期。例如，你登录了一个服务器，服务器返回给你一个SessionId，登录成功之后半小时内没有对该服务器进行任何Http请求，半小时后再进行一次Http请求，会提示重新登陆。



**`服务器会为每一个会话，都维护一个Session。`**

**`Session是线程不安全的，浏览器或客户端发送的每一个请求，都是一个线程，多个请求都对同一个Session进行修改，有可能会出现线程的安全问题。`**

所以，我们会将从Session中取到的数据，保存到ThreadLocal中。



**小结**：

Session是另一种记录客户状态的机制，`不同的是Cookie保存在客户端浏览器中，而Session保存在服务器上`。客户端浏览器访问服务器的时候，服务器把客户端信息以某种形式记录在服务器上。这就是Session。客户端浏览器再次访问时只需要从该Session中查找该客户的状态就可以了。





# 三、Cookie和Session结合使用实现登录验证功能

**利用Cookie和Session登录的原理**

客户输入账号和密码进行登录，服务器端进行验证，验证成功则生成SessionId，并且在Session对象中存储当前用户信息。服务器端将SessionId写入客户端Cookie中，当客户端下次访问服务器端时Cookie会被自动发送给服务器端，服务器端在Cookie中拿到SessionId然后在服务器端的Session对象中查找SessionId进行验证，验证成功说明用户是登录状态，则可以为其响应只有在登录状态才能响应的数据。

<img src=".\images\12738918279.png" alt="在这里插入图片描述" style="zoom:67%;" /> 

**具体实现流程：**

1. **在登录业务代码中，当用户登录成功时，生成一个登录凭证存储到Redis，将凭证中的字符串保存在Cookie中返回给客户端。**
2. **创建拦截器，在客户端向服务器发送请求后，服务器会去拦截每一次请求，从Cookie中获取凭证字符串与Redis中的凭证进行匹配，获取用户信息，并将用户信息保存在ThreadLocal中，在本次请求中持有用户信息，即可在后续操作中使用到用户信息。**
3. **当请求结束，我们就会把保存的用户信息清除掉，防止内存泄漏。**

那么，下面来介绍这个拦截器的代码：

```java
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    // 保存登录信息
    // 调用时间：Controller方法处理之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");

        if (ticket != null){
            //查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())){
                //根据凭证查询用户
                User user = userService.findByUserId(loginTicket.getUserId());
                //在本次请求中持有用户
                hostHolder.setUser(user);
                //构建用户认证的结果，并存入SecurityContext，以便于Security进行授权
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), userService.getAuthorities(user.getId()));
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            }
        }

        return true;
    }

    // 调用时间：Controller方法处理完之后
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //得到当前线程持有的user
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null){
            modelAndView.addObject("loginUser", user);
        }
    }

    // 调用时间：DispatcherServlet进行视图的渲染之后
    // 请求结束，把保存的用户信息清除掉
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
        SecurityContextHolder.clearContext();
    }
}
```



**为什么要使用ThreadLocal？**

因为ThreadLocal是**`线程隔离`**的。一个线程只能访问当前线程中ThreadLocal中的数据，而不能访问其他线程的ThreadLocal中所保存的数据。

在项目运行时，每次客户端向服务器发送的一个请求，都可以看作是一个线程，那么，多次访问就是多个线程，如果多次请求中都去访问了同一个数据，就会存在线程的安全问题。

所以，我们就想解决线程的安全问题。

那么，我们就可以使用ThreadLocal实现。

ThreadLocal是线程隔离的，意味着线程只能去访问当前线程内的ThreadLocal中存储的数据。那么，我们就可以把用户信息从Session中取出，保存在ThreadLocal中，这样既方便我们在后续操作中获取用户的登录信息，同时也能够将用户与当前线程（请求）绑定，防止被其他的请求访问而造成线程的安全问题。
