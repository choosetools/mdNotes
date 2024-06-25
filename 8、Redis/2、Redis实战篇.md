[TOC]



# 导入黑马点评项目

### 导入SQL

首先，导入课前资料提供的SQL文件： [hmdp.sql](lib\hmdp.sql) 

其中的表有：

* tb_user：用户表
* tb_user_info：用户详情表
* tb_shop：商户信息表
* tb_shop_type：商户类型表
* tb_blog：用户日记表（达人探店日记）
* tb_follow：用户关注表
* tb_voucher：优惠券表
* tb_voucher_order：优惠券的订单表



### 当前的模型说明

手机或app端发送请求，请求到我们的nginx服务器，nginx可以实现基于Lua直接绕开Tomcat访问Redis，也可以作为静态资源服务器，轻松抗下上万并发，负载均衡到下游Tomcat服务器，打散流量。

一台四核8G的tomcat，在优化和处理简单业务的加持下，大不了就处理1000左右的并发，经过nginx负载均衡分流后，利用集群支撑起整个项目，同时nginx在部署了前端项目后，更是可以做到动静分离，进一步降低Tomcat服务的压力，这些功能都得靠nginx起作用，所以nginx是整个项目中重要的一环。

在Tomcat支撑起并发流量后，我们如果让Tomcat直接去访问Mysql，根据经验MySQL企业级服务器只要上点并发，一般是16或32核心CPU，32或64G内存，企业级MySQL加上固态硬盘能够支撑的并发，大概就是4000~7000左右，上万的并发瞬间就会让MySQL服务器的CPU和硬盘全部打满，容易崩溃，所以我们在高并发场景下，会选择使用mysql集群，同时为了进一步降低mysql的压力，增加访问的性能，我们也会加入Redis，同时使用Redis集群使得Redis对外提供更好的服务。

![1653059409865](.\images\1653059409865.png)





### 导入后端项目

后端项目已经放在了笔记中： [hm-dianping.zip](lib\hm-dianping.zip) 

我们将其解压之后，使用IDEA打开即可。

我已经将原本项目中，老的依赖修改成新的了，我们直接打开启动即可。

**修改编码方式**

当我们创建SpringBoot项目时，都需要去指定编码方式为UTF-8，否则执行Application时会报错。

<img src=".\images\image-20240620122059237.png" alt="image-20240620122059237" style="zoom: 50%;" /> 

这样一来，后端项目的导入就完成了。

我们可以访问：[localhost:8081/shop-type/list](http://localhost:8081/shop-type/list)来测试一下数据运行是否有问题：

<img src=".\images\image-20240620122320448.png" alt="image-20240620122320448" style="zoom:67%;" /> 

出现上述的结果，就说明后端导入完成了。







### 导入前端项目

在我们的笔记中，提供了一个压缩包：[nginx-1.18.0.zip](lib\nginx-1.18.0.zip) 

在这个压缩包里面，提供了nginx服务器，并且已经部署了黑马点评的前端项目，我们只需要将其解压之后，然后在nginx所在目录下打开cmd窗口，输入命令：

```shell
start nginx.exe
```

这样一来，我们的项目就启动了。

nginx监听的端口号是8080，那么，我们只需要去访问http://localhost:8080，就可以看到页面：

<img src=".\images\image-20240620123249300.png" alt="image-20240620123249300" style="zoom:50%;" /> 

此时，前端项目就是运行成功了。

**注意！nginx这个目录不能放在包含中文的路径下，否则启动不了！**





---

# 一、短信登录

## 基于Session实现登录

#### 使用Session实现登录的流程

1. **发送验证码**

用户在提交手机号后，会去校验手机号是否合法，如果不合法，要求用户重新输入。

如果手机号合法，后台生成对应的验证码，并将验证码先保存到Session中，然后通过短信的方式将验证码发送给用户。

<img src=".\images\image-20240620132855113.png" alt="image-20240620132855113" style="zoom:67%;" /> 

2. **短信验证码登录、注册**

用户收到验证码后，将验证码和手机号输入，后台从session中拿到当前的验证码，然后对用户输入的验证码进行校验，如果不一致，则无法通过校验；如果一直，则后台根据手机号查询用户，如果用户不存在，则为用户创建账号信息，保存到数据库。

无论用户是否存在，都会将用户的信息保存到session中，方便后续获得当前登录信息。

<img src=".\images\image-20240620133104311.png" alt="image-20240620133104311" style="zoom:67%;" /> 



3. **校验登录信息**

用户每次去访问信息，都需要去校验登录的信息，用户的信息都保存在session中。而session的sessionId会保存在cookie中。

当用户访问时，一定会携带上自己的cookie，cookie中就会包含sessionId，我们就可以根据cookie中的sessionId，去获取对应的session，从而就可以获取用户信息。

我们只需要判断是否有用户存在，如果没有，说明之前该用户没有登录过，则拦截该请求；如果有，说明用户曾经登录过，则放行请求，在放行之前，先将用户缓存起来放到本地的ThreadLocal中，方便后续直接从ThreadLocal中获取用户信息。

ThreadLocal是一个线程域对象，每个请求到达后台，都是一个单独的线程，如果我们将数据保存在本地变量中，则多个线程去修改可能会出现线程安全的问题，而ThreadLocal则会将数据存放到每个线程内部，在线程内部创建一个Map进行存储，线程之间不会彼此干扰，就没有线程安全问题了。

<img src=".\images\image-20240620134155464.png" alt="image-20240620134155464" style="zoom:67%;" /> 



**Cookie和Session是如何实现验证登录功能的？**

客户输入账号和密码进行登录，服务器端进行验证，验证成功则生成SessionId，并且在Session对象中存储当前用户信息。服务器端将SessionId写入客户端Cookie中，当客户端下次访问服务器端时Cookie会被自动发送给服务器端，服务器端在Cookie中拿到SessionId然后在服务器端的Session对象中查找SessionId进行验证，验证成功说明用户是登录状态，则可以为其响应只有在登录状态才能响应的数据。

具体内容请查看笔记： [Cookie与Session.md](..\2、JavaWeb\Cookie与Session.md) 



**为什么要使用ThreadLocal？**

因为ThreadLocal是**`线程隔离`**的。一个线程只能访问当前线程中ThreadLocal中的数据，而不能访问其他线程的ThreadLocal中所保存的数据。

在项目运行时，每次客户端向服务器发送的一个请求，都可以看作是一个线程，那么，多次访问就是多个线程，如果多次请求中都去访问了同一个数据，就会存在线程的安全问题。

所以，我们就想解决线程的安全问题。

那么，我们就可以使用ThreadLocal实现。

ThreadLocal是线程隔离的，意味着线程只能去访问当前线程内的ThreadLocal中存储的数据。那么，我们就可以把用户信息从Session中取出，保存在ThreadLocal中，这样既方便我们在后续操作中获取用户的登录信息，同时也能够将用户与当前线程（请求）绑定，防止被其他的请求访问而造成线程的安全问题。





#### 1、发送短信验证码功能

`注意！以下的代码是非常简短的代码，功能是不全的，重要的是理解功能的流程！`

**业务流程**

用户要进入`我的`页面时：

<img src=".\images\image-20240621131114798.png" alt="image-20240621131114798" style="zoom:50%;" /> 

我的页面里面保存都是用户的信息，所以用户肯定是需要登录的。

<img src=".\images\image-20240621131226742.png" alt="image-20240621131226742" style="zoom:50%;" /> 

在我的页面里面输入手机号，点击发送验证码时，一个请求就会发送到服务端，服务端就会处理这个请求了。

发送验证码的请求：

<img src=".\images\image-20240621131508981.png" alt="image-20240621131508981" style="zoom:50%;" /> 

请求方式是POST，请求路径是/user/code，请求参数是phone。



**代码编写**

我们来看看之前写的发送验证码流程：

<img src=".\images\image-20240620132855113.png" alt="image-20240620132855113" style="zoom:67%;" /> 

服务端接收到手机号之后，先去校验手机号是否符合格式，如果不符合就直接响应提示。符合，就去生成验证码，将验证码保存在Session中，然后将验证码发给用户的手机。

这就是发送短信验证码的流程。

那么，我们使用代码来实现一下：

```java
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

   	//发送验证码功能
    @Override
    public Result sendCode(String phone, HttpSession session) {
        //1、校验手机号，isPhoneInvalid()方法是去校验是否是无效的手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
            //2、如果不符合，返回错误信息
            return Result.fail("手机号格式错误！");
        }

        //3、符合，生成验证码
        String code = RandomUtil.randomNumbers(6);

        //4、保存验证码到Session
        session.setAttribute("code", code);

        //5、发送验证码
        //这里发送验证码的功能就不实现了，比较麻烦，而且不是学习的重点
        log.debug("发送验证码成功，验证码：{}", code);

        //6、结束，返回成功的信息
        return Result.ok();
    }
}
```

这是service层发送验证码的逻辑。

对其中代码的解释：

* `RegexUtils`是项目中已经创建好的工具类，该工具类是使用正则表达式来校验字符串是否符合格式的工具类。其中的`isPhoneInvalid()`方法，是校验字符串是否不符合手机号格式，如果返回值为true，表示不符合手机号格式。
* `RandomUtil`类，是我们所引入的hutool包中带的工具类，该类可以用于生成随机值，其中的`randomNumbers()`可以生成指定数量的随机数。hutool包是一个强大的工具类包，在其中给我们提供了很多的工具类。

* `Result`类，是项目中创建好的，用于响应给前端的结果类。其中，有四个属性：

  <img src=".\images\image-20240621133103031.png" alt="image-20240621133103031" style="zoom:67%;" /> 

  success：表示是否成功。

  errorMsg：表示错误信息。

  data：表示返回的具体信息。

  total：data若是不止一个（集合类型），则total表示其数量。



此时，去重启后端项目，然后再去输入手机号发送验证码，此时请求信息为：

<img src=".\images\image-20240621133730607.png" alt="image-20240621133730607" style="zoom:67%;" /> 

可以看到，此时该请求的响应就是success:true，表示发送验证码成功了。

我们来看看后台程序的打印信息：

<img src=".\images\image-20240621133832987.png" alt="image-20240621133832987" style="zoom:67%;" /> 

那么，至此发送验证码短信的功能就完成了。







#### 2、短信验证码登录功能

`注意！以下的代码是非常简短的代码，功能是不全的，重要的是理解功能的流程！`

**业务流程**

<img src=".\images\image-20240621134709590.png" alt="image-20240621134709590" style="zoom: 50%;" /> 

那么此时，就会发送一个请求给服务端：

<img src=".\images\image-20240621134924781.png" alt="image-20240621134924781" style="zoom:67%;" />

请求地址是/user/login

请求方法是POST

请求参数是手机号以及验证码：

<img src="C:\Users\14036\AppData\Roaming\Typora\typora-user-images\image-20240621135003937.png" alt="image-20240621135003937" style="zoom:50%;" /> 

参数是JSON风格的字符串。





**代码实现**

<img src=".\images\image-20240620133104311.png" alt="image-20240620133104311" style="zoom: 50%;" /> 

1、验证手机号是否符合格式（永远不要相信前端给我们的数据）

2、验证码是否正确：如果与Session中保存的验证码不一致，就报一个异常信息给前端。

3、根据手机号去数据库中查询是否有对应的用户信息：

* 如果有，则将用户保存到Session中；
* 如果没有，就去创建一个新的用户，然后将用户保存到Session中。

结束。

使用代码实现：

```java
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        // 1.校验手机号
        String phone = loginForm.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 2.如果不符合，返回错误信息
            return Result.fail("手机号格式错误！");
        }

        //3、验证用户输入的验证是否正确
        String loginCode = loginForm.getCode();
        if (loginCode.isBlank()){
            return Result.fail("请输入验证码！");
        }
        Object sessionCode = session.getAttribute("code");
        if (sessionCode == null || !sessionCode.toString().equals(loginCode)){
            //4、如果不正确，就报异常信息
            return Result.fail("验证码有误！");
        }
        //5、根据手机号查询是否有对应的用户信息，手机号是唯一的
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getPhone, phone);
        User user = getOne(lambdaQueryWrapper);

        //6、如果用户不存在，则去创建用户
        if (user == null){
             user = createUserWithPhone(phone);
        }

        //7、将用户信息保存在Session中（实际开发中保存的格式肯定不是这样的，会更加复杂）
        session.setAttribute("user", user);

        return Result.ok();
    }
    
    
    //创建用户的方法
	private User createUserWithPhone(String phone) {
        User user = new User();
        //手机号
        user.setPhone(phone);
        //昵称，随机生成
        user.setNickName("user_" + RandomUtil.randomString(10));
        //保存用户
        save(user);
        return user;
    }
}
```

完成上述功能后，我们进行登录，登录完成后会进入到首页页面：

<img src=".\images\image-20240621144233279.png" alt="image-20240621144233279" style="zoom:33%;" /> 

此时，我们再去点击我的页面：

<img src=".\images\image-20240621144258102.png" alt="image-20240621144258102" style="zoom:33%;" /> 

发现我们还是进入的登录页面。

为什么会这样呢？

因为我们还没有完成登录校验的功能，只有在请求之前，校验我们已经完成了登录，那么此时才不会进入到登录页面。



#### 3、登录校验功能

我们已经完成了发送短信验证码，以及登录的功能。

但是，在进入我的页面时，依然进入的是登录页面，服务器依然认为我们没有登录，原因就在于登录校验的功能没有完成。

**说明**

登录验证，会在每一次去处理需要登录的请求之前，都会进行。

用户的请求会携带Cookie，而Cookie中保存着服务器的SessionId：

<img src=".\images\image-20240621145823263.png" alt="image-20240621145823263" style="zoom:67%;" /> 

我们可以看到，请求头中包含了Cookie，在Cookie中，有JSESSIONID数据，这就是Session的ID，通过它可以获取服务器端的Session数据

我们可以获取浏览器中的Cookie，根据Cookie中的SessionId，获取我们在服务器端保存的Session，查看Session中是否保存用户信息，如果保存了，说明用户曾经登录过，也就通过了登录校验，可以对请求进行处理。

如果没保存，则需要用户登录。



**这样会有一个问题：**

在整个项目中，会有很多controller，就比如UserController，我们在UserController中，编写了去获取SessionID的代码，获取Cookie中的SessionId，然后根据SessionId获取user，完成业务逻辑。

但是不仅仅是一个controller中需要去校验用户的登录，在整个项目的开发中，会有越来越多的功能都需要验证用户是否登录，难道在每个controller中都编写登录验证的逻辑吗？那么有没有一个好的办法，能够让我们在指定的请求前都进行登录校验的工作呢？

有，那就是SpringMVC中所学习的**`拦截器`**。

有了拦截器之后，用户的请求就不会直接发送给controller了，而是先被我们的拦截器接收，在拦截器中去判断请求是被拦截还是放行，之后才会去执行handler方法。

那么，我们可以去`定义一个拦截器，在拦截器中对用户进行登录校验，只有满足了校验的要求后，才会被放行，从而去执行相关的业务`。



**还有一个问题：**

在拦截器校验完成后，我们是需要在业务中使用用户的信息，所以我们还需要在拦截器中，将用户的信息传递到handler方法中，并且需要注意线程的安全问题，此时就可以使用到**`ThreadLocal`**。

**服务器会为同一个会话中的所有请求，都会共享同一个Session。而每个请求，都是一个线程，所以，在同一个会话中对Session进行的操作，可能会出现线程安全问题。**此时，我们可以将数据保存到ThreadLocal中，ThreadLocal是线程隔离的，线程只能访问到该线程内部的ThreadLocal，ThreadLocal不是共享的。所以，我们为了防止出现线程安全问题，可以将数据保存在ThreadLocal中，这样其他线程就无法访问到。

这样一来，同一个用户访问不同的请求，或者不同用户访问同一个请求，都会有自己独立的ThreadLocal，ThreadLocal会将数据保存在内存中。ThreadLocal中保存的用户信息互相不干扰，不会有线程安全问题。当要使用到用户信息时，直接从ThreadLocal中取，而不是去Session中取。

**校验登录的流程**

<img src=".\images\image-20240620134155464.png" alt="image-20240620134155464" style="zoom: 50%;" /> 





**代码实现**

1. **创建拦截器包**

创建一个包，叫做com.hmdp.interceptor，在该包下，去定义拦截器类：

<img src=".\images\image-20240621163804498.png" alt="image-20240621163804498" style="zoom: 67%;" /> 

2. **创建拦截器类**

创建一个拦截器类，就叫做LoginInterceptor，使用该类去实现HandlerInterceptor接口，并且实现其中的preHandle()与afterCompletion()方法：

```java
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
```

在preHandle()方法中，我们需要去验证登录信息，然后将用户的信息放入到ThreadLocal中。

在afterCompletion()方法中，我们需要去销毁ThreadLocal中的用户信息，避免内存泄漏。

那么，具体实现：

```java
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1、获取Session
        HttpSession session = request.getSession();

        //2、获取Session中的用户
        Object user = session.getAttribute("user");

        //3、判断用户是否存在
        if (user == null){
            //4、不存在，拦截
            response.setStatus(401);
            return false;
        }

        //5、存在，保存用户信息到ThreadLocal
        UserHolder.saveUser((User) user);

        //6、放行
        return true;
    }

    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //移出用户，避免内存泄露
        UserHolder.removeUser();
    }

}
```

其中的UserHolder类，是项目中自定义类，我们来看看这个类的信息：

<img src=".\images\image-20240621170316563.png" alt="image-20240621170316563" style="zoom:67%;" /> 

在这个类中，定义了一个`ThreadLocal<User>`类型的对象，我们可以将用户的信息，保存在该对象中。由于ThreadLocal是属于线程的数据，所以，我们可以在同一个线程中，随时获取到ThreadLocal中包含的数据，并且不需要担心其他线程造成影响。



3. **配置拦截器**

在config包下，创建一个Mvc的配置类，就叫做MvcConfig，在该类中配置MVC中的配置信息，其中就包括拦截器。

让该类去实现WebMvcConfigurer接口，重写该接口的addInterceptors()，该方法就是用来配置拦截器的。

```java
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns(
                        "/user/code",
                        "/user/login",
                        "/blog/hot",
                        "/shop/**",
                        "/shop-type/**",
                        "/upload/**",
                        "/voucher/**"
                );
    }
}
```

其中的excludePathPatterns()方法，是去指定哪些路径不被拦截器所拦截。

这样一来，登录验证的功能就完成了。

那么，我们要想在登录后，对某些请求验证是否登录，只需要保证该请求在拦截器的拦截路径下即可。



4. **配置me()方法**

最后，如果我们想要去访问我的页面，那么就需要将获取当前用户信息的接口完善：

```java
/**
 * 获取当前登录的用户
 * @return
 */
@Override
public Result me() {
    return Result.ok(UserHolder.getUser());
}
```

获取用户信息，直接从ThreadLocal中获取即可，因为我们在前置拦截方法中，将用户信息已经保存到了ThreadLocal中了，ThreadLocal是线程中的数据，在同一个线程的任意位置都可以直接修改或者获取ThreadLocal中保存的数据。



那么，此时，我的页面登录功能就算完成了，我们可以去我的页面查看了。 





#### 4、隐藏用户敏感信息

我们来看看，我的页面请求获取到的信息：

<img src=".\images\image-20240621172322524.png" alt="image-20240621172322524" style="zoom:50%;" />  

我们发现，有些信息是比较敏感的，不应该让用户看到。

比如说，id、password密码、还有createTime创建时间以及updateTime修改时间。

去显示用户的信息，其实只需要手机号phone、昵称nickName、以及图片位置icon即可，其他的数据我们是不希望响应给前端的。

那么，我们可以去出创建一个类，叫做`UserDTO`，该类中封装了可以返回给前端的数据：

```java
@Data
public class UserDTO {
    private Long id;
    private String nickName;
    private String icon;
}
```

然后，我们去修改代码，只讲UserDTO返回。

**在登录方法处修改**

```java
//7.保存用户信息到session中
session.setAttribute("user", BeanUtil.copyProperties(user, UserDTO.class));
```

**在拦截器处：**

```java
UserHolder.saveUser((UserDTO) user);
```

**在UserHolder处：将user对象换成UserDTO**

```java
public class UserHolder {
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO user){
        tl.set(user);
    }

    public static UserDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
```

修改完成后，我们再看看/user/me接口返回给前端的数据：

<img src=".\images\image-20240621194714155.png" alt="image-20240621194714155" style="zoom:80%;" /> 

那么，此时返回给前端的数据，就是需要的数据了，不会有多余敏感的数据信息。





---

## 存在的问题以及解决方案

**Session共享问题**

一台服务器处理并发的能力有限，对于高并发的场景，我们需要使用服务器集群的方式，经过nginx负载均衡分流并发请求后，减少每台服务器处理的请求，从而降低服务器的压力，使得整个项目能够使用在高并发的场景下。

但是，这样就有了一个问题：

每个Tomcat服务器都有一份属于自己的session，假如用户第一次访问第一台Tomcat，并把自己的信息存放在第一台服务器的session中，但是第二次nginx将该用户的请求分流到第二台Tomcat，那么在第二台服务器上，肯定没有第一台服务器存放的session，那么此时的登录验证就会不过关，需要重新登录。此时，使用Session实现的登录验证功能是有问题的。



**解决方案**

**方案一：**session拷贝

虽然每台服务器都有不同的session，但是每当任意一台服务器session修改时，都会同步给其他的Tomcat服务器的session，这样一来，也实现了session的共享。

但是这种方案有两个大问题：

1、每台服务器中都有完整的一份session数据，服务器压力过大。

2、session拷贝数据时，可能会出现延迟。

所以，这种方案没有得到认可，也就没有得到广泛的应用。

![1653069893050](.\images\1653069893050.png)

我们想要的Session的替代方案应该满足的是：

* 数据共享
* 内存存储
* key、value结构

满足上述三个要求的，不就是Redis吗。



**方案二：使用Redis实现**



---

## 基于Redis实现短信登录（:star:）

### 流程说明

1. **发送验证码功能**

原本是将验证码保存在session中，现在我们将验证码保存到Redis中：

<img src=".\images\image-20240621202219143.png" alt="image-20240621202219143" style="zoom: 50%;" />  

那么，将验证码保存在Redis中，value应该采用什么样的格式呢？

验证码就是String类型，所以value应该使用String类型的数据。

那key应该采用什么样的格式？

session有个特点，每个不同的浏览器，都有会自己独立的session，不同浏览器携带手机号来请求时，不同浏览器所访问到的session中的code验证码都是独立的，不会互相干扰。但是，如果把验证码放在Redis中，Redis中的数据是共享的，我们必须要区分不同的手机号，所对应的验证码。那么，验证码可以使用手机号作为key，这样一来，每个不同的手机号，所保存的验证码key都是不同的。

**所以，Redis中验证码格式为：**

| key            | value        |
| -------------- | ------------ |
| `phone:手机号` | `验证码code` |

之前使用Session时，不需要考虑取数据的问题。因为Tomcat会自动帮助我们维护Session。当我们创建一个Session时，Tomcat会自动将创建的SessionId，写到用户浏览器的Cookie中，这样用户访问时会自动带着Cookie，服务器就可以根据Cookie中的SessionId，找到Session，我们可以直接使用。

但是，现在使用的是Redis，在存数据时，我们还需要与其他数据进行区分，将来要取数据时，才能取出我们想要的数据。





2. **短信验证码登录、注册流程**

使用Redis实现短信验证码登录、注册时，进行验证码校验，是去Redis中，使用手机号寻找Redis中已保存的数据，然后使用验证码进行比对。并且在最后，原本是将用户保存到Session中，也要修改成将用户保存到Redis中。

<img src=".\images\image-20240621205109890.png" alt="image-20240621205109890" style="zoom:50%;" />  

那么，保存到Redis中，用户的信息是转换成JSON，使用String类型保存呢；还是使用Hash类型，对用户的每一个属性都进行单独保存？

我们先来看看这两种类型的区别：

<img src=".\images\image-20240621203703730.png" alt="image-20240621203703730" style="zoom:67%;" /> 

String结构，以JSON字符串来保存，比较直观；

Hash结构，将对象中每个字段单独存储，可以对单个字段进行修改，并且占用内存更少。

我们这里选择使用JSON字符串来保存（实际还是因为在公司用的比较多的还是JSON）



然后，我们来考虑一下key的格式：

key我们推荐使用**随机`token`**来存储用户数据。（随机token也就是一个随机的字符串）

所以，Redis中存储用户数据格式为：

| key                | value                                  |
| ------------------ | -------------------------------------- |
| `token:随机字符串` | `{"name":"tom", "age":21}（JSON格式）` |





3. **登录校验流程**

使用Session实现登录校验比较简单，因为服务器会自动将SessionId写到浏览器的Cookie中，浏览器访问服务器时，就会将Cookie发送给服务器，就可以获取到Session的信息。而服务器就可以从Session中获取到用户的信息了。

但是使用Redis，那么此时登录凭证就不再是Cookie了。

此时的登录凭证，则是保存在Redis中作为key的**`token`**了。

也就是说，用户发送请求给服务器，就需要带上token了，将其作为登录的凭证。但是服务器并不会自动将token写到浏览器中，那么，**我们只能手动地将token返回给前端**。

那么，也就是说，在使用短信登录、注册的流程中，将用户保存到Redis以后，我们还需要将保存用户的key，也就是token返回给浏览器，浏览器将token信息保存下来，之后每次请求，都要携带这个token发送给服务器。

服务器在接收到token以后，去Redis中获取数据，就可以拿到用户信息了。

<img src=".\images\image-20240621205154526.png" alt="image-20240621205154526" style="zoom:50%;" /> 

因为我们是将token保存在浏览器中的，所以token最好使用随机字符串了，而不要使用手机号作为token，这样可能会有泄漏风险。



我们来看看前端是如何去保存token的：

<img src="C:\Users\14036\AppData\Roaming\Typora\typora-user-images\image-20240621205735614.png" alt="image-20240621205735614" style="zoom:67%;" /> 

在访问/user/login，就是登录的请求后，前端将data是响应的数据，也就是token，保存在sessionStorage中，sessionStorage是浏览器的保存方式。

<img src=".\images\image-20240621205956152.png" alt="image-20240621205956152" style="zoom: 80%;" /> 

前端定义了一个request的拦截器，在每次发送请求之前，都将sessionStorage中的token数据拿出来，放到请求头的authorization中，发送给服务器，这样服务器就能够得到token的数据，验证是否登录成功了。



### 代码实现

**发送验证码功能**

```java
@Override
public Result sendCode(String phone, HttpSession session) {
    //1、校验手机号，isPhoneInvalid()方法是去校验是否是无效的手机号
    if (RegexUtils.isPhoneInvalid(phone)) {
        //2、如果不符合，返回错误信息
        return Result.fail("手机号格式错误！");
    }

    //3、符合，生成验证码
    String code = RandomUtil.randomNumbers(6);

    //4、保存验证码到Redis，设置验证码过期时间为2分钟
    stringRedisTemplate.opsForValue().set("login:code:" + phone, code, Duration.ofMinutes(2));
    

    //5、发送验证码
    //这里发送验证码的功能就不实现了，比较麻烦，而且不是学习的重点
    log.debug("发送验证码成功，验证码：{}", code);

    //6、结束，返回成功的信息
    return Result.ok();
}
```

**用户登录功能**

```java
@Override
public Result login(LoginFormDTO loginForm, HttpSession session) {
    // 1.校验手机号
    String phone = loginForm.getPhone();
    if (RegexUtils.isPhoneInvalid(phone)) {
        // 2.如果不符合，返回错误信息
        return Result.fail("手机号格式错误！");
    }

    //3、验证用户输入的验证码是否正确
    String loginCode = loginForm.getCode();
    if (loginCode.isBlank()){
        return Result.fail("请输入验证码！");
    }
    //获取Redis中的验证码
    String code = stringRedisTemplate.opsForValue().get("login:code:" + phone);
    
    
    if (code == null || !code.equals(loginCode)){
        //4、如果不正确，就报异常信息
        return Result.fail("验证码有误！");
    }
    
    
    //5、根据手机号查询是否有对应的用户信息，手机号是唯一的
    LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.eq(User::getPhone, phone);
    User user = getOne(lambdaQueryWrapper);

    
    //6、如果用户不存在，则去创建用户
    if (user == null){
         user = createUserWithPhone(phone);
    }

    
    //7、将用户信息保存在Redis中
    //生成一个随机token，作为登录令牌
    String token = UUID.randomUUID().toString();
    //将对象转换成JSON存在Redis中，使用json存储
    UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
    ObjectMapper objectMapper = new ObjectMapper();
    String userJson = null;
    try {
        userJson = objectMapper.writeValueAsString(userDTO);
    } catch (JsonProcessingException e) {
        return Result.fail("数据有误！");
    }
    //保存用户数据，并设置有效期为30分钟
    stringRedisTemplate.opsForValue().set("login:token:" + token, userJson, Duration.ofMinutes(30));

    
    //返回token给前端
    return Result.ok(token);
}
```



**拦截器类中的登录校验功能**

```java
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //1、获取请求头中保存的token数据
        String token = request.getHeader("authorization");

        //2、获取Redis中的用户信息
        String tokenKey = "login:token:" + token;
        String userJson = stringRedisTemplate.opsForValue().get(tokenKey);
        UserDTO user = null;
        if (userJson != null && !userJson.isBlank()){
            //将其转换成User对象
            ObjectMapper objectMapper = new ObjectMapper();
            user = objectMapper.readValue(userJson, UserDTO.class);
        }
        

        //3、判断用户是否存在
        if (user == null){
            //不存在，拦截
            response.setStatus(401);
            return false;
        }

        //4、存在，刷新用户的有效期
        //用户在访问服务器，说明用户是活跃的，那么就让用户有效期的时间重置
        //只有超过30分钟无操作才会将用户信息清除
        stringRedisTemplate.expire(tokenKey, Duration.ofMinutes(30));

        //5、并保存用户信息到ThreadLocal
        UserHolder.saveUser(user);

        //6、放行
        return true;
    }

    
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //移出用户，避免内存泄露
        UserHolder.removeUser();
    }

}
```





### 解决状态登录刷新问题

**存在的问题**

在上面使用Redis实现登录的功能中，有一个问题存在：

在登录校验的拦截器中，确实可以去拦截需要进行登录校验的请求，在拦截的方法中完成对用户信息的校验，并且刷新token登录令牌的存活时间。但是，这个拦截器只是拦截需要被拦截的路径，假设当前用户访问了一些不需要拦截的路径，那么这个拦截器就不会生效。则此时，令牌的刷新实际上就不会执行。

但是，应该实现的业务是，只要用户发送请求到服务器，服务器就需要去刷新用户的token令牌存活时间。

所以，现在这个方案存在问题。

![1653320822964](.\images\1653320822964.png)



**优化方案**

我们可以添加一个拦截器，在登录校验的拦截器之前，去拦截所有的路径。

在第一个拦截器中，去获取请求中的token，然后根据token获取用户保存到ThreadLocal，再去刷新token。则第二个拦截器（也就是登录校验拦截器）只需要去判断ThreadLocal中是否有user对象即可，有则放行，无则拦截。

这样，整体的拦截器功能就完成。

![1653320764547](.\images\1653320764547.png) 



**创建拦截器RefreshTokenInterceptor，拦截所有请求**

```java
@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取请求头中保存的token数据
        String token = request.getHeader("authorization");

        //token不存在，直接放行
        if (StrUtil.isBlank(token)){
            return true;
        }

        //获取Redis中的用户信息
        String tokenKey = "login:token:" + token;
        String userJson = stringRedisTemplate.opsForValue().get(tokenKey);
        if (userJson == null || userJson.isBlank()){
            return true;
        }

        //将其转换成User对象
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO user = objectMapper.readValue(userJson, UserDTO.class);

        //判断用户是否存在
        if (user == null){
            return true;
        }

        //存在，则刷新用户的有效期
        stringRedisTemplate.expire(tokenKey, Duration.ofMinutes(30));

        //保存用户信息到ThreadLocal
        UserHolder.saveUser(user);

        //放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //释放资源
        UserHolder.removeUser();
    }
}
```





**对登录校验LoginInterceptor拦截器的修改**

```java
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断用户是否存在
        if (UserHolder.getUser() == null){
            //不存在，拦截
            response.setStatus(401);
            return false;
        }

        //放行
        return true;
    }
}
```





**拦截器配置的修改**

```java
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private RefreshTokenInterceptor refreshTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器
        //多个拦截器按照order属性顺序执行，值越小越先执行，默认都是0，则按照添加顺序执行
        registry.addInterceptor(refreshTokenInterceptor).order(0);//拦截所有请求

        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns(
                        "/user/code",
                        "/user/login",
                        "/blog/hot",
                        "/shop/**",
                        "/shop-type/**",
                        "/upload/**",
                        "/voucher/**"
                ).order(1);
    }
}
```

那么，此时无论是访问需要登录验证的页面，还是访问不需要登录验证的页面，都会去触发刷新Redis中token登录令牌的存活时间。













---

# 二、缓存（:star:）

## 1、什么是缓存？

**什么是缓存？**

缓存（Cache），就是数据交换的缓冲区，俗称的缓存就是缓冲区内的数据，一般从数据库中获取，存储于本地代码。

例如：

```java
例1:Static final ConcurrentHashMap<K,V> map = new ConcurrentHashMap<>(); 本地用于高并发

例2:static final Cache<K,V> USER_CACHE = CacheBuilder.newBuilder().build(); 用于redis等缓存

例3:Static final Map<K,V> map =  new HashMap(); 本地缓存
```

由于其被static修饰，所以随着类的加载而被加载到内存中，作为本地缓存。由于其又被final修饰，所以不用担心赋值导致缓存失效。



**为什么使用缓存？**

速度快，好用。

缓存数据存储于代码中，而代码运行在内存中，内存的读写性能远高于磁盘，缓存可以大大降低用户访问并发量带来的服务器读写压力。

实际开发过程中，企业的数据量少则几十万，多则几千万，这么大的数据量，如果没有缓存作为避震器，系统是几乎撑不住的，所以企业会大量运用到缓存技术。

但是缓存也会增加代码复杂度和运营成本：
![](.\images\image-20220523214414123.png) 





**如何使用缓存？**

实际开发中，会构筑多级缓存来使系统运行速度进一步提升，比如：本地缓存与redis中的缓冲并发使用

**浏览器缓存**：主要是存在于浏览器端的缓存

**应用层缓存**：可以分为Tomcat本地缓存，比如使用map集合存储数据，还有mybatis缓存，或者是使用redis作为缓存

**数据库缓存**：在数据库中有一片空间是buffer pool数据库缓冲池，增改差数据会先加载到mysql的缓冲池中

**CPU缓存**：当代计算机最大的问题是CPU性能提升了，但内存读写速度没有跟上，所以为了适应当下的情况，增加了cpu的L1，L2，L3级的缓存

![](.\images\image-20220523212915666.png)







## 2、添加商户查询缓存

**缓存模型**

原本，客户端要去访问数据，会发送一个请求到数据库，在数据库中查询数据，然后将查询到的数据返回给客户端：

<img src=".\images\image-20240622004438006.png" alt="image-20240622004438006" style="zoom:67%;" /> 

在添加了缓存之后，在客户端与数据库之间添加了一个中间层Redis，客户端的请求会先到达Redis，查询要想的数据。如果Redis中保存了要查询的数据，表示命中了，则直接将数据返回的客户端；如果在Redis中没有保存要查询的数据，则再去数据库中查询，然后将查询到的数据返回给客户端。

<img src=".\images\image-20240622004823841.png" alt="image-20240622004823841" style="zoom: 50%;" /> 

去数据库中查询到的数据，应该要保存到Redis中，这样当我们下一次去查询数据时，就可以直接在Redis中获取，不需要从数据库中去获取。

<img src=".\images\image-20240622005000945.png" alt="image-20240622005000945" style="zoom:50%;" /> 



**商户查询使用缓存思路**

根据商户的id查询商户的详细信息，原本我们是直接去数据库中查询。

在使用Redis缓冲后，我们先去Redis中查询，如果Redis中保存了商户的信息，则我们直接返回；

如果Redis中没有对应的商户信息，此时再去mysql数据库中查询，将查询的数据保存到Redis中，方便下次查询，然后再返回。

<img src=".\images\image-20240622005240497.png" alt="image-20240622005240497" style="zoom: 50%;" /> 





**代码实现**

```java
@Autowired
private StringRedisTemplate stringRedisTemplate;

/**
 * 根据id查询商户信息，使用缓存
 * @param id
 * @return
 */
@Override
public Result queryById(Long id) {
    //获取Redis中的数据
    String shopKey = "cache:shop:" + id;
    String shopJsonStr = stringRedisTemplate.opsForValue().get(shopKey);

    //如果数据存在，则直接返回
    if (StrUtil.isNotBlank(shopJsonStr)){
        return Result.ok(JSONUtil.toBean(shopJsonStr, Shop.class));
    }

    //如果不存在，则去数据库中查询
    Shop shop = getById(id);
    if (shop == null){
        return Result.fail("店铺不存在！");
    }

    //存在则将数据保存到Redis中
    stringRedisTemplate.opsForValue().set(shopKey, JSONUtil.toJsonStr(shop));
    return Result.ok(shop);
}
```

这里Redis中对象value使用的是String类型，也就是使用JSON字符串保存对象数据。

此时，第一次去查询商户需要走数据库，第二次查询相同的商户，就不会再去数据库查询，直接查询Redis缓存获取。





## 3、缓存更新策略

### 三种缓存更新策略说明

我们把数据保存在缓存中，当用户更新了mysql数据库中的数据时，缓存中的数据并不会一起更新，那么此时，用户去查询到的数据就是缓存中的旧数据。这种情况在大多数业务场景下都是是不被允许的，那么，我们就需要去更新缓存中的数据。

在企业中，常见的缓存更新策略有三种：

1. **内存淘汰**：这种机制，原本是Redis用来解决内存不足的问题，Redis是基于内存来存储的，内存不像磁盘，它是有限的。当内存不足时，会触发这种策略，Redis会去自动淘汰部分数据，然后在下次查询时再去更新缓存。这种机制，让数据库与缓存之间保持一致性的能力较差。这种机制是Redis自带的机制，所以不需要我们进行维护，维护成本低。
2. **超时剔除**：给缓存数据添加TTL（超时）时间，到期后自动删除缓存。下次查询时更新缓存。如果设置TTL时间较短，比如30分钟，缓存更新的频率就会高一点，这样一来，数据一致性就会高一点；如果设置TTL的时间较长，比如1天，那么更新频率较低，数据一致性也会较差。这种策略所需的维护成本较低。
3. **主动更新**：主动更新，就是我们自己去编写业务逻辑，在修改数据库的同时，更新缓存。这样数据的一致性会比较好的，但是这种策略的维护成本会比较高。

那么，我们选择哪一种机制来实现缓存更新呢？

* **低一致性需求：**`使用内存淘汰机制`，例如店铺类型的查询缓存。
* **高一致性需求**：`主动更新，并以超时剔除作为兜底方案`。例如店铺详情查询的缓存。

![1653322506393](.\images\1653322506393.png)



### 主动更新策略

主动更新策略有三种类型：

* `Cache Aside Pattern`：人工编码方式，缓存调用者在更新完数据库后再去更新缓存，也称之为双写方案。
* `Read/Write Through Pattern`：由系统本身完成，数据库与缓存的问题交由系统本身去处理。
* `Write Behind Caching Pattern`：调用者只操作缓存，其他线程去异步处理数据库，实现最终一致。

![1653322857620](.\images\1653322857620.png)



后面两种方案，实现起来比较麻烦，并且也没有比较好的第三方组件，企业中一般使用的都是第一种方案：人工编码的方式去主动更新缓存。

使用人工编码方式主动更新缓存有三个问题需要考虑：

* **删除缓存还是更新缓存？**

  * 更新缓存：每次更新数据库都更新缓存，无效写操作较多
  * 删除缓存：更新数据库时让缓存失效，查询时再更新缓存

  如果数据库写操作比较多，比如更新了上百次，但是却不去查询，那么我们每次在更新数据库时都去更新缓存，更新缓存操作都是无效的。

  但是，如果在更新数据库时删除缓存，这样每次更新都不需要再去同步缓存，只在查询时让缓存生效，在大量写操作时就不会产生多次更新缓存的无效操作。

  所以，我们选择在更新sql数据库时，**`删除缓存`**。

* **如何保证缓存与数据库的操作的同时成功或失败？**

  我们需要保证删除缓存与更新数据库这两个操作同时成功，同时失败，即要保证这里两个操作合起来作为一个原子操作。

  * 单体系统，更新数据库与删除缓存在同一个项目中，甚至在同一个方法里，我们可以使用事务本身的特性就能够保证同时成功与同时失败；

  * 分布式系统，更新数据库的操作以及删除缓存的操作，有可能是不同的服务，此时就不得不用到TCC这样的分布式事务方案。

* **先操作缓存还是先操作数据库？**

  * 先删除缓存，再操作数据库
  * 先操作数据库，再删除缓存

  我们来看看不同的顺序可能会出现的问题：

![1653323595206](.\images\1653323595206.png)

如果是先删除缓存，再更新数据库的话：

* 假设有两个线程，一个线程是去修改数据库，一个线程是去查询数据。修改数据库的线程，先去删除缓存，假设其删除了缓存之后，另一个线程去查询数据，那么此时，查询缓存未命中，再去查询数据库，并将数据库中的数据写入到缓存中，查询线程结束。修改数据库的线程开始执行，去更新数据库，则此时，数据库中的数据与缓存中的数据不一致。由于查询缓存、查询数据库以及写缓存的速度是很快的，但是更新数据库的操作比较慢，所以，在删除缓存与将数据更新到数据库之间这段时间，有其他线程去查询缓存、查询数据库并将查询到的数据写入缓存发生的几率是很高的。

如果是先更新数据库，再删除缓存的话：

* 假设有两个线程，一个线程去查询数据v，一个线程去更新数据v。查询数据的线程，先去查询缓存，缓存未命中，此时再去查询数据库，此时查询到的数据v结果为10。在查询线程将数据10结果写入缓存之前，另一个线程去更新数据库，将数据库v的结果修改为20，更新数据库之后，删除了缓存。此时，查询数据库的线程去更新缓存，将v的结果修改为10。那么，此时缓存与数据库的数据不一致。出现这种问题，是在缓存失效，并且在查询数据库与写入缓存之间，恰好有一个线程去修改数据库，由于写入缓存的速度是非常快的，所以这种问题发生的概率很低。

以上是不同顺序可能出现的问题。由于我们先更新数据库，再删除缓存出现问题的概率，比先删除缓存，再更新数据库出现问题的概率低很多，所以，应该**`先操作数据库，再删除缓存`**。





### 小结：更新缓存的最佳实践

> * 对于**高一致性需求**的场景（经常修改），我们要**主动更新缓存**，并同时给缓存设置超时时间，使用**超时剔除**兜底。
>
> * 对于**低一致性需求**的场景（不经常修改），我们可以使用Redis自带的**内存淘汰**方案更新缓存。
>
> 
>
> * **读操作：**
>
>   * **`缓存命中则直接返回`**
>
>   * **`缓存未命中，则查询数据库，并写入缓存，设定超时时间`**
>
> * **写操作：**
>   * **`先写数据库，然后再删除缓存`**
>   * **`要确保数据库与缓存操作的原子性`**







### 代码实现商铺缓存与数据库的一致性

依据更新缓存的最佳实践，现去修改ShopController中的业务逻辑，满足下面的需求：

1. 根据id查询店铺时，如果缓存未命中，则查询数据库，将数据库结果写入缓存，并设置超时时间
2. 根据id修改店铺时，先修改数据库，再删除缓存。

**商铺更新方法**

```java
@Override
@Transactional
public Result updateShopById(Shop shop) {
    Long shopId = shop.getId();
    if (shopId == null){
        Result.fail("商铺id不能为空！");
    }

    //更新数据库
    updateById(shop);

    //删除缓存
    String shopKey = "cache:shop:" + shopId;
    stringRedisTemplate.delete(shopKey);
    return Result.ok();
}
```



**商铺查询方法的修改**

```java
@Override
public Result queryById(Long id) {
    //获取Redis中的数据
    String shopKey = "cache:shop:" + id;
    ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
    String shopJsonStr = operations.get(shopKey);

    //如果数据存在，则直接返回
    if (StrUtil.isNotBlank(shopJsonStr)){
       //返回数据
        return Result.ok(JSONUtil.toBean(shopJsonStr, Shop.class));
    }

    //如果不存在，则去数据库中查询
    Shop shop = getById(id);
    if (shop == null){
        return Result.fail("店铺不存在！");
    }

    //存在则将数据保存到Redis中，并设置超时时间
    operations.set(shopKey, JSONUtil.toJsonStr(shop), Duration.ofMinutes(30));
    return Result.ok(shop);
}
```







## 4、缓存的三大问题及解决方案（:star:）

在日常开发工作中，缓存技术被广泛利用以增强系统性能和减轻数据库的访问压力。但是，在使用缓存的过程中，我们可能会碰到几个典型问题，比如缓存穿透、缓存击穿和缓存雪崩。

### 1）缓存穿透

#### 什么是缓存穿透？

> **`缓存穿透`是指客户端请求的数据在缓存和数据库中都不存在，导致缓存永远不会命中，用户每次请求该数据都要去数据库中查询一遍。**

<img src=".\images\image-20240622135457080.png" alt="image-20240622135457080" style="zoom: 50%;" /> 

如果有恶意攻击者不断请求系统中不存在的数据，会导致短时间大量请求落在数据上，造成数据库压力过大，甚至导致数据库承受不住而宕机崩溃。



#### 问题分析

缓存穿透现象的核心问题在于请求中使用的key在Redis缓存中无法找到相应的值。在缓存穿透的情况下，传入的key在Redis中不存在，如果有黑客故意发送大量不存在的key的请求，这会导致数据库遭受巨大的查询压力，可能会严重威胁到系统的正常运行。因此，在日常开发实践中，对请求参数进行严格的校验是至关重要的。对于那些非法或明显不可能存在的key，系统应该立即返回一个错误提示，而不是让这些请求到达数据层面，这样不仅可以提升系统的安全性，还能够维护数据库的稳定性和性能。





#### 方案一：缓存空对象

##### 方案说明

当出现Redis查不到数据，数据库也查不到数据的情况，我们就把这个key保存到Redis中，设置value="null"，并设置其一个较短的过期时间，后面再出现查询这个key的请求的时候，直接返回null，就不需要再去查询数据库了。

<img src="https://img-blog.csdnimg.cn/direct/24f551579bc14b9e9e8e7bf2fbebdbee.png" alt="img" style="zoom:67%;" />

**优点**：实现简单。

**缺点**：

* 1）**耗费内存并且会有失效的情况**。在Redis中缓存大量空值不仅会消耗宝贵的内存资源，而且如果攻击者持续使用随机key进行攻击，这种防御策略就会失效。在这种情况下，不仅数据库可能因过载而崩溃，Redis服务也可能由于内存耗尽而出现拒绝写操作的现象。这样，你的正常需要写入Redis的业务也会跟着收到影响。
* 2）**数据不一致问题**。虽然在缓存空值时我们设定了较短的过期时间，但仍存在一种情况：在缓存的空值尚未过期的这段时间内，数据库中的实际数据可能已经更新，而该键值在数据库中存在了数据。这导致在缓存中的空值仍然被返回，而没有返回真实的数据，从而造成缓存与数据库之间的数据不一致现象。



##### 具体实现

查询商品信息原本的过程：

根据id查询商铺信息，去Redis中查询，没有查询到则再去数据库中查询，若依旧没有，则返回404给前端。这样是存在着缓存穿透问题的。

现在的逻辑：

现在去数据库中查询，数据不存在，不会返回404，还是会把这个空值写入到Redis中，并把空值返回；当再次发起查询时，去Redis缓存中查询，如果value是null，则证明是缓存穿透数据，直接返回。

![1653327124561](.\images\1653327124561.png)

**代码实现**

```java
@Override
public Result queryById(Long id) {
    //获取Redis中的数据
    String shopKey = "cache:shop:" + id;
    ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
    String shopJsonStr = operations.get(shopKey);

    //如果数据存在，则直接返回
    if (StrUtil.isNotBlank(shopJsonStr)){
       //返回数据
        return Result.ok(JSONUtil.toBean(shopJsonStr, Shop.class));
    }

    //如果数据是否是空字符串
    if (shopJsonStr != null){
        //返回一个错误信息
        return Result.fail("店铺不存在！");
    }

    //如果不存在，则去数据库中查询
    Shop shop = getById(id);
    if (shop == null){
        //如果店铺不存在，将空值保存到Redis中，并返回错误信息
        operations.set(shopKey, "", Duration.ofMinutes(2));
        return Result.fail("店铺不存在！");
    }

    //存在则将数据保存到Redis中，并设置超时时间
    operations.set(shopKey, JSONUtil.toJsonStr(shop), Duration.ofMinutes(30));
    return Result.ok(shop);
}
```

查询数据，如果Redis中有数据，则返回缓存数据，如果缓存的数据是一个空值""，那么报错误信息；如果Redis中没有数据，去Mysql数据库中查询，Mysql中也没有数据时，则保存一个空值到Redis中，当下次又要访问该数据时，会直接从Redis中获取到。

比如，我们访问一下id为0的数据，第一次查询会去数据库中获取：

![image-20240623111003798](.\images\image-20240623111003798.png)

当我们再去查询时，就不会走数据库，也就没有这个日志信息，而是直接从Redis中获取，返回错误信息给前端。





---

#### 方案二：布隆过滤器

为了缓解缓存穿透的问题，我们可以在Redis缓存层之前部署一道布隆过滤器防线。将数据库中的所有键导入布隆过滤器中，布隆过滤器提供一种高效的概率检测机制，用于判断一个元素是否可能在一个集合内。这样，在任何查询到达Redis之前，系统会首先检查该查询的键是否在布隆过滤器中。如果布隆过滤器中不存在查询的键，则直接返回，不去查询缓存以及数据库，以此避免对数据库的不必要访问和潜在查询压力。通过这样的布隆过滤器前置筛选，我们不仅保护了数据库免受不存在的键的查询压力，还确保了整体系统的性能稳定，即使在高并发查询的环境下也能有效地运作。

**当布隆过滤器断言数据库中不存在某个键时，这个结论是绝对可靠的；但它认为某个键存在时，只表示存在的可能性很高**。由于其内部使用的是哈希思想，使用哈希算法去计算数据库键所对应的值，保存到布隆过滤器中，所以其可能存在着哈希冲突，由于哈希冲突，所以其**存在误判**的可能。

<img src=".\images\b5f4ed1168b24434b4dbec379886e7ed.png" alt="img" style="zoom:67%;" />

但是，布隆过滤器如果认为数据库没有这个数据，那就一定是没有的，此时就可以直接返回，不去Redis或数据库中查询，所以布隆过滤器也能够过滤掉一些存在问题的请求。

**优点**：**占用内存小。**布隆过滤器不需要存储具体的数据项，只需要存储数据的哈希值，因此相对存储实际数据，它占用的内存更少。

**缺点**：

* 1）**存在一定误判的可能。**布隆过滤器存在一定的误判率，即它可能会错误的认为某个不存在的元素存在，尽管可以通过调整参数来降低误判率，但无法完全消除。

* 2）**数据不一致。**

  为了维护数据的准确性和一致性，理想情况下，当数据库中的数据发生更新时，布隆过滤器也应当进行相应的更新以反映这些变更。然而，布隆过滤器与数据库是两个独立的数据管理实体。所以可能出现的一种情形是，在数据库成功执行了数据更新之后，当尝试更新布隆过滤器时，网络异常发生，导致新增的数据未能及时写入布隆过滤器中。在这种状况下，后续针对这个新加入数据的查询请求将会被布隆过滤器拒绝，因为该数据的键尚未存在于过滤器中。尽管这是一个合理的查询请求，它却被“错误地”拦截了。






在这里，其实并不存在一个完美无缺的方案解决缓存穿透问题。具体选择哪种方案必须依据业务场景来定。那么，我们到底该如何选择呢？

考虑并发度的高地，**`并发度低`的我们可以简单的使用`缓存空值`的方案来解决缓存穿透问题，`并发度高`的情况下我们最好使用`布隆过滤器`的方案解决缓存穿透问题**。

当然，除了上面提到的布隆过滤器和缓存空数据的方案之外，我们还可以通过:

* **增强id的复杂度，避免被猜测id规律**

* **数据参数格式校验**

* **拉黑恶意攻击者的IP**

* **加强用户权限校验**

* **做好热点参数的限流**

这些方法来增强对缓存穿透问题的防护。







---

### 2）缓存雪崩

#### 什么是缓存雪崩？

> **`缓存雪崩`是指由于缓存系统的整体失效，导致大量请求直接到达后端数据库，进而可能造成数据库崩溃和整个系统的崩溃**。

这种现象通常发生在缓存服务器重启或宕机时，或者是大规模的key同时失效导致的结果。

<img src=".\images\1653327884526.png" alt="653327884526" style="zoom:67%;" />

#### 问题分析

造成缓存雪崩的关键在于同一时间大规模访问的key无法从缓存中获取数据，都要去数据库中请求。出现这一问题主要有两种可能：第一种是Redis宕机，第二种可能是key采用了相同的过期时间。



#### 解决方案

* **设置随机的过期时间**

在设置失效时间的时候加上一个随机值，比如1-5分钟随机。这样就可以避免由于使用相同的过期时间导致某一时刻大量的key过期引发的雪崩问题。

* **redis集群**

保证Redis缓存的高可用，防止Redis因宕机导致雪崩问题。可以使用主从 + 哨兵，Redis集群来避免单个Redis服务器宕机导致整个缓存直接失效。

* **多级缓存**

通过实施多级缓存策略，我们可以优化系统的性能并降低因缓存失效导致的风险。在这种策略中，本地进程内的缓存充当第一级缓存，而Redis作为第二级远程缓存。每一级缓存都设定有独立且差异化的超时时间，这样的设计确保了即使一级缓存的数据或被清除，仍能有耳机或其他级别的缓存来提供数据支持。这种层级化的缓存机制为系统提供了额外的弹性层，当一层缓存遇到问题时，其他层级能够起到“安全网”的作用，从而可以有效的避免雪崩现象。

* **使用熔断机制**

当系统流量达到预定的极限时，为避免对数据库造成过大压力，我们将自动显示“系统繁忙”提示。这样做可以确保至少有一部分用户能够顺畅地使用我们的服务。对于未能即时访问的用户，只要多刷新几次，也是可以获得正常访问的。

* **缓存预热**

缓存预热是一种关键技术，它在系统启动前预先加载关键数据到缓存中，以减少系统上线时对后端数据库的冲击。由于新上线的系统缓存是空的，如果没有预热过程，大量并发请求将直接访问数据库，极有可能在系统上线初期导致服务崩溃。因此，通过在系统上线之前将高频率访问数据从数据库加载到Redis等缓存系统中，可以确保用户请求首先由缓存服务处理，从而减轻数据库的压力。实施缓存预热通常涉及编写批处理任务，这些任务可以在系统启动期间执行，或者通过定时任务定期去执行。定期执行更能保证数据的实时性，但是，同样会耗费系统的部分性能，尤其是在数据量大的时候。所以，具体选择如何进行预热数据，还是需要综合考虑预热数据量的大小以及预热数据更新是否频繁等因素。

* **互斥锁**

通过互斥锁来控制数据写缓存的线程数量，这样就可以避免大量请求同时击中数据库。同样，这样虽然可以避免大量key同时失效导致的缓存雪崩问题，但是，同样性能也会因为加锁的原因受到影响。如果系统对吞吐量要求不高的情况下，这种方式其实还是不错的。因为它既解决了缓存击穿问题，也解决了缓存雪崩的问题。









---

### 3）缓存击穿

#### 什么是缓存击穿？

> **`缓存击穿问题`也叫作热点key问题。当某一个被高并发访问的数据（热点key）在缓存中过期，此时有大量的并发请求需要访问这个数据，由于缓存中的数据已经失效，这些并发请求就会直接转发到数据库上，如果数据库的处理能力不足以应对这种突然增加的压力，就可能导致系统响应缓慢甚至崩溃。**

如果缓存雪崩是大量的key同时过期而导致的问题，那么缓存击穿就是部分key到期而导致的后果。

如下图所示：当某个热点缓存失效时，请求会去数据库查询出来，并重建缓存数据保存到缓存中，在这个期间，假如有很多的请求都来请求该数据，都在缓存中查询不到，那么都会去数据库中查询，就会造成数据库的压力过大，导致运行缓慢甚至崩溃。

<img src=".\images\image-20240623121637003.png" alt="image-20240623121637003" style="zoom:50%;" /> 

#### 问题分析

核心问题在于，一旦某个热门的key失效，便会导致密集的并发请求直接涌向数据库。因此，解决方案需从两个方向着手：首先，考虑对热点key不设置过期时间以保持其持久有效；其次，探索降低数据库所承受的请求量的方法，以减轻其压力。





#### 方案一：互斥锁

##### 方案说明

互斥锁，也叫作排它锁。

当缓存失效后，会有大量的请求去访问数据库，并重新创建缓存。

那么我们可以使用互斥锁，控制访问数据库并创建缓存的线程数量，比如某个key只允许一个线程去查询数据库并创建缓存，而其他的请求线程被阻塞，这样，就只有一个请求会到达数据库，而其他的请求会被阻塞直到缓存重新被创建，其他请求则是去查询这个新创建的缓存信息。

<img src=".\images\image-20240623123047290.png" alt="image-20240623123047290" style="zoom:67%;" /> 

**优点：**

1. **强一致性。**互斥锁能够确保在缓存重建的过程中，只有一个线程可以访问数据库并更新缓存，这样可以避免多个线程同时读取到过期的缓存数据，从而保证了数据的强一致性。
2. **实现相对简单**。互斥锁的实现相对简单，不需要复杂的逻辑处理，只需在缓存失效时加锁，更新完毕后释放锁即可。



**缺点：**

1. **吞吐量低。**在高并发的场景下，互斥锁可能会导致系统的可用性降低，因为大量的请求可能会因为等待锁而无法及时得到处理。
2. **可能有死锁风险**。比如在一个业务中需要获取多把锁，而在另一个业务中也需要去获取把锁，当前业务需要获取另一个业务的锁，而另一个业务中也需要获取当前业务的锁，那么就会出现死锁问题。





##### 具体实现

**需求**：修改根据id查询商铺的业务，基于互斥锁方式来解决缓存击穿问题。

<img src=".\images\image-20240623132430287.png" alt="image-20240623132430287" style="zoom: 50%;" /> 

**注意：**这里的锁并不是我们之前所学习的synchronized或者Lock。对于synchronized与Lock来说，我们拿到锁可以执行，未拿到锁就会被阻塞，并且就算将数据已经保存到了缓存中，原本被阻塞的线程中，也只能是其中一个线程去执行，其他线程还是会被阻塞。

所以，我们不能使用synchronized或者Lock来加锁。

我们可以**使用Redis中的命令`SETNX`来实现互斥锁的功能。**

SETNX命令，用于添加原本不存在的key=value，如果添加成功，返回1，如果添加失败，返回0：

<img src=".\images\image-20240623134517236.png" alt="image-20240623134517236" style="zoom:67%;" /> 

这个命令可以用来实现互斥锁的功能：

当一个线程在Redis缓存中未查询到数据时，则其先去Redis中使用SETNX命令创建一个数据：

* 如果SETNX结果返回是1，表示之前没有线程创建该数据，也就是当前线程是第一个去创建的，则当前线程可以去查询mysql数据库数据并重构Redis缓存；

* 如SETNX结果返回0，表示之前已经有线程去创建了该数据，说明已经有线程在查询数据库数据并重构Redis缓存了，则当前线程就休眠一下，等下再去查询缓存。

加完锁重构Redis缓存完毕之后，我们就需要去释放锁：**使用`DEL`删除命令通过删除数据的方式去释放锁。**

并且，为了防止因为系统出现异常，导致锁未能够释放，我们需要**给锁添加`超时时长`兜底**，比如10秒钟，就算系统出现了异常锁未释放，在10秒之后，也能够将锁释放，程序能够得到正常运行。

以上就是使用互斥锁的思路，这跟真正的分布式锁还是有些差距的。



**代码实现**

获取锁的方法

```java
private Boolean tryLock(String lockKey){
    ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
    //setNX命令，在Java中对应的是setIfAbsent()
    //返回的结果是Boolean类型，即如果创建成功，返回的是true；如果创建失败，返回的是false
    return operations.setIfAbsent(lockKey, "1", Duration.ofSeconds(10));
}
```

释放锁的方法

```java
private void unlock(String lockKey){
    //释放锁，也就是去删除使用setNX创建的数据
    stringRedisTemplate.delete(lockKey);
}
```



那么，商铺查询的方法中，添加使用互斥锁方案解决缓存击穿问题的代码：

```java
@Override
public Result queryById(Long id) {
    //获取Redis中的数据
    String shopKey = "cache:shop:" + id;
    ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
    String shopJsonStr = operations.get(shopKey);

    //如果数据存在，则直接返回
    if (StrUtil.isNotBlank(shopJsonStr)){
       //返回数据
        return Result.ok(JSONUtil.toBean(shopJsonStr, Shop.class));
    }

    //如果数据是空字符串
    if (shopJsonStr != null){
        //返回一个错误信息
        return Result.fail("店铺不存在！");
    }

    //当缓存中没有数据时，使用互斥锁方案实现数据重构
    String lockKey = "lock:shop:" + id;
    Shop shop = null;
    try {
        //去获取锁
        if (!tryLock(lockKey)){
            //如果获取锁失败，则说明已经有线程在重构缓存，当前线程进入休眠，然后再去重新查询Redis数据库
            Thread.sleep(50);
            return queryById(id);
        }

        //获取锁成功，则去重构缓存
        shop = getById(id);
        if (shop == null){
            //如果店铺不存在，将空值保存到Redis中，并返回错误信息
            operations.set(shopKey, "", Duration.ofMinutes(2));
            return Result.fail("店铺不存在！");
        }

        //存在则将数据保存到Redis中，并设置超时时间
        operations.set(shopKey, JSONUtil.toJsonStr(shop), Duration.ofMinutes(30));
    } catch (Exception e) {
        throw new RuntimeException(e);
    }finally {
        //释放锁，将释放锁的操作放在finally中执行
        unlock(lockKey);
    }

    return Result.ok(shop);
}
```





#### 方案二：逻辑过期

##### 方案说明

逻辑过期，相当于永不过期。

在存储缓存数据时，不去设置数据过期时间，而是给value中添加一个逻辑过期的字段，比如叫做expire，在当前时间的基础上增加上逻辑过期时间给字段赋值。由于该数据没有设置过期时间，意味着该数据存储在Redis中，数据永远都存在，永不过期，数据永远都可以查到，不会出现缓存击穿的问题。如：

![image-20240623125110979](.\images\image-20240623125110979.png) 



当一个线程去Redis中查询该数据，如果获取到的数据发现已经逻辑过期了，则表示需要去更新该数据，该线程会去获取互斥锁，然后开辟一个新的线程去更新缓存数据，当前线程则直接将已经过期的数据返回。

而其他线程在缓存还未更新时查询数据，发现获取到的缓存中的数据也已过期，去获取互斥锁失败，则直接将过期的数据返回。

当第一个线程开辟的更新缓存的线程，更新数据以及其逻辑过期时间完毕后，会释放锁，其他线程再去查询缓存数据，就可以获取锁并且获取到的数据也没有逻辑过期了，可以直接将新数据返回。

<img src=".\images\image-20240623130332938.png" alt="image-20240623130332938" style="zoom: 67%;" /> 



**优点：吞吐量高，性能较好。**在逻辑过期方案中，即使数据已过期，系统仍会返回过期数据给客户端，而不是等待锁后再去数据库拉取最新数据。

**缺点：**

1. **牺牲数据的一致性。**由于在数据更新过程中，系统可能会返回过期数据，这在一定程度上牺牲了数据的一致性。
2. **实现复杂。**逻辑过期方案需要维护额外的字段来记录每个缓存项的逻辑过期状态，这增加了系统的复杂性。
3. **耗费更多的内存。**因为增加了一个字段来维护逻辑过期时间，这必定要造成额外的空间占用。





**那么，什么时候使用互斥锁，什么时候使用逻辑过期呢？**

根据上面两种方式的优缺点，可以知道：当**数据要求强一致性**时，使用**互斥锁**方案；当要求**数据吞吐量高**，并且对**数据一致性要求不高**的时候，就使用**逻辑过期**方案。



##### 具体实现

**需求**：修改根据id查询商铺的业务，基于逻辑过期方式来解决缓存击穿问题。

**整体流程**



<img src=".\images\image-20240623154102247.png" alt="image-20240623154102247" style="zoom:67%;" /> 

创建一个逻辑过期的类，在该类中声明一个逻辑过期时间属性，并且将要存储到Redis中实际的数据放在该类中。

```java
@Data
public class RedisData<T> {
    private LocalDateTime expireTime;
    private T data;
}
```

对于热点的数据，我们一般需要在后台，提前添加到缓存中。由于我们这里现在没有后台管理系统，所以我们先通过一个单元测试方法，将某个店铺数据添加到缓存中，提前做一个缓存的预热：

```java
public void saveShopToRedis(Long id, Long expireSeconds){
    //查询店铺数据
    Shop shop = getById(id);

    //封装逻辑过期时间
    RedisData<Shop> redisData = new RedisData<>();
    redisData.setData(shop);
    redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
    //写入Redis
    stringRedisTemplate.opsForValue().set("cache:shop:" + id, JSONUtil.toJsonStr(redisData));
}
```

该方法用于保存一些逻辑过期的商铺数据到Redis中，可以指定逻辑过期时间。并且注意，我们在写入Redis时，没有给该用户设定存活时间，而是使用逻辑来控制其真正的过期时间。

此时，我们去执行测试一下，传入id为1，expireSeconds为10，那么执行的结果为：

![image-20240623161248858](.\images\image-20240623161248858.png) 

**使用逻辑过期解决缓存击穿问题的主要代码**

```java
//线程池
private final static ExecutorService executorService = Executors.newFixedThreadPool(10);

/**
 * 使用逻辑过期解决缓存击穿问题
 * @param id
 * @return
 */
@Override
public Result queryById(Long id) {
    //获取Redis中的数据
    String shopKey = "cache:shop:" + id;
    ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
    String shopJsonStr = operations.get(shopKey);

    //如果未命中，则直接返回空
    if (shopJsonStr == null || shopJsonStr.isEmpty()) {
        return Result.ok();
    }

    //如果命中，判断热点数据是否已过期
    RedisData redisData = JSONUtil.toBean(shopJsonStr, RedisData.class);
    if (redisData.getExpireTime().isBefore(LocalDateTime.now())){
        //如果已过期，需要缓存重建
        //获取互斥锁
        String lockKey = "lock:shop:" + id;
        if (tryLock(lockKey)){
            //获取互斥锁成功，则去开启一个新线程，用于重建缓存
            executorService.execute(() -> {
                try {
                    //重建缓存，设置过期时间为20s
                    this.saveShopToRedis(id, 20L);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    //释放锁
                    unlock(lockKey);
                }
            });
        }
        //获取互斥锁失败，直接返回已过期的数据
    }

    return Result.ok(redisData.getData());
}
```



当热点数据过期时，当前线程会先去判断能否获取到互斥锁，如果不能，则直接返回过期的数据；如果可以，则去开启一个新的线程，用于对热点数据进行缓存重建工作，并将过期的数据返回，则当该新线程重建缓存完毕之后，就会释放锁，其他线程查询到的数据就是未逻辑过期的了。

这里采用**线程池**的方式，开启一个新的线程，在线程中对过期的热点数据进行缓存重建工作。给该线程池使用`final static`关键字进行修饰，则表示在程序运行期间，该线程池都会存在，并用于重建缓存。

上述的互斥锁，也是使用`SETNX`命令来实现的，具体可以查看互斥锁的方案内容。





---

## 5、缓存工具封装

基于StringRedisTemplate封装一个缓存工具类，满足下列需求：

* 方法1：将任意Java对象序列化为json存储在string类型的key中，并且可以设置TTL过期时间。
* 方法2：将任意Java对象序列化为JSON并存储在String类型的key中，并且可以设置逻辑过期时间，用于处理缓存击穿问题。
* 方法3：根据指定的key查询缓存，并反序列化为指定类型，利用缓存空值的方式解决缓存穿透问题。
* 方法4：根据指定的key查询缓存，并反序列化为指定类型，需要利用逻辑过期解决缓存击穿问题。

```java
@Component
public class CacheClient {

    private final StringRedisTemplate stringRedisTemplate;

    //线程池
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public CacheClient(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 方法1：将任意Java对象序列化为json存储在string类型的key中，并且可以设置TTL过期时间。
     */
    public void set(String key, Object value, Long time, TimeUnit timeUnit){
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, timeUnit);
    }

    /**
     * 方法2：将任意Java对象序列化为JSON并存储在String类型的key中，并且可以设置逻辑过期时间，用于处理缓存击穿问题。
     */
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit timeUnit){
        RedisData redisData = new RedisData();
        redisData.setExpireTime(LocalDateTime.now().plus(time, timeUnit.toChronoUnit()));
        redisData.setData(value);
        //写入redis中
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }


    /**
     * 方法3：根据指定的key查询缓存，并反序列化为指定类型，利用缓存空值的方式解决缓存穿透问题。
     */
    public <ID, R> R queryWithThrough(String keyPrefix, ID id, Function<ID, R> dbFallback, Class<R> type, Long time, TimeUnit timeUnit){
        String key = keyPrefix + id;
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();

        //从Redis中查询信息
        String jsonValue = operations.get(key);
        //判断数据是否存在
        if (StrUtil.isNotBlank(jsonValue)) {
            //若存在直接返回
            return JSONUtil.toBean(jsonValue, type);
        }
        //若查询出来为空字符串，则返回空字符
        if (jsonValue != null){
            return null;
        }

        //如果为空，则去数据库中查询
        //具体该怎么查，由传进来的dbFallback决定，执行其apply()方法
        R r = dbFallback.apply(id);
        //如果数据不存在，保存空字符串到Redis中
        if (r == null){
            operations.set(key, "", time, timeUnit);
        }
        //将数据保存到Redis中，并设置超时时间
        this.set(key, JSONUtil.toJsonStr(r), time, timeUnit);
        return r;
    }


    /**
     * 方法4：根据指定的key查询缓存，并反序列化为指定类型，需要利用逻辑过期解决缓存击穿问题。
     */
    public <R, ID> R queryWithLogicalExpire(String keyPrefix, ID id, Function<ID, R> dbFallback, Class<R> type, Long time, TimeUnit timeUnit){
        String key = keyPrefix + id;
        //查询缓存
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String jsonStr = operations.get(key);
        //如果缓存未命中，直接返回空
        if (jsonStr == null || jsonStr.isBlank()){
            return null;
        }
        //如果缓存命中，判断是否过期
        RedisData redisData = JSONUtil.toBean(jsonStr, RedisData.class);
        if (redisData.getExpireTime().isBefore(LocalDateTime.now())){
            //如果过期，则要去重新构建缓存数据
            //获取互斥锁
                String lockKey = "lock:shop:" + id;
                if (tryLock(lockKey)) {
                    //如果获得了互斥锁，则开启一个新的线程去重构数据
                    executorService.execute(() -> {
                        try {
                            //查询数据
                            R r = dbFallback.apply(id);
                            //重新保存数据
                            this.setWithLogicalExpire(key, r, time, timeUnit);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        } finally {
                            //释放锁
                            unlock(lockKey);
                        }
                    });
                }
            //如果没有获得互斥锁，直接返回
        }
        //如果未过期，直接返回数据
        return JSONUtil.toBean((JSONObject) redisData.getData(), type);
    }

    /**
     * 方法5：根据指定的key查询缓存，并反序列化为指定类型，可以设置过期时间，并且利用互斥锁来解决缓存击穿问题
     */
    public <ID, R> R queryWithMutex(String keyPrefix, ID id, Function<ID, R> dbFallback, Class<R> type, Long time, TimeUnit timeUnit){
        String key = keyPrefix + id;
        //去查询数据是否存在，如果数据存在直接返回
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String jsonStr = operations.get(key);
        if (StrUtil.isNotBlank(jsonStr)){
            //如果数据存在，直接返回
            return JSONUtil.toBean(jsonStr, type);
        }
        //如果数据为Null，返回空值
        if (jsonStr != null){
            return null;
        }
        R r = null;
        String lockKey = null;
        try {
            //如果数据不存在，则去获取互斥锁
            lockKey = "lock:shop:" + id;
            if (!tryLock(lockKey)){
                //如果获取不到互斥锁，说明已经有线程再重构redis缓存了，则休息一下重新获取
                Thread.sleep(50);
                return queryWithThrough(keyPrefix, id, dbFallback, type, time, timeUnit);
            }

            //如果获取了互斥锁，则去重构redis
            r = dbFallback.apply(id);
            //如果不存在数据，将空值放入redis中
            if (r == null) {
                operations.set(key, "", 2, TimeUnit.MINUTES);
                return null;
            }
            //存在写入redis
            this.set(key, r, time, timeUnit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //释放锁
            unlock(lockKey);
        }


        return r;
    }
    
    

    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }
}
```

这里重点介绍一下这里的`Function`参数，Function接口是java.util.function包下的，用于根据一个类型的数据，得到另一个类型的数据。Function<T, R>第一个泛型类型，表示根据哪种类型的参数，第二个泛型类型，表示根据第一个参数得到的类型。

当我们要在方法中，根据不同的数据，执行不同的语句时，就可以传入一个Function接口类型的参数，在方法调用时指定具体该如何操作。

比如上例方法中，当Redis中缓存没有数据时，我们需要去查询数据表中的数据并写入Redis中，那么这个数据表是无法确定的，我们要根据实际业务场景，选择不同的数据表。那么，此时，我们就可以传入Function接口类型参数，让**实际查询数据表的操作，在方法的调用者中声明**，我们只需要在当前方法中，执行Function接口的`apply()`方法执行即可。

调用案例：查询shop表中指定id的数据，并使用互斥锁的方式解决缓存击穿问题

```java
@Override
public Result queryById(Long id) {
    Shop shop = cacheClient.queryWithMutex("cache:shop:", 1L, new Function<Long, Shop>() {
        @Override
        public Shop apply(Long shopId) {
            return getById(shopId);
        }
    }, Shop.class, 20L, TimeUnit.MINUTES);
    return Result.ok(shop);
}
```

传入的Function接口参数，就表示了在queryWithMutex()方法中，该如何去查询，以及查询哪一张表中的数据。在queryWithMutex()方法中，调用了apply()方法，实际上就是执行当前该方法中Function的匿名实现类中的的apply()方法。







---

# 三、秒杀问题

## 1、全局唯一ID

### 说明

在本项目中，用户购买优惠券时，会生成订单信息保存到tb_voucher_order这张表中，而订单表如果使用数据库默认的自增ID就会存在一些问题：

* id的规律性太明显
* 受单表数据量的限制

场景分析1：如果我们的id具有太明显的规则，用户或者商业对手很容易猜测出来我们的敏感信息，比如商城在一天时间内，卖出了多少单，这明显不合适。

场景分析2：随着我们商城规律越来越大，mysql的单表的容量不宜超过500W，数据量过大后，我们要进行拆库拆表，但拆分表之后，他们从逻辑上实际属于同一张表，所以它们的id不能一样，我们需要保证id的唯一性。



全局ID生成器，是一种在分布式系统下用来生成全局唯一ID的工具，一般要满足下面的特性：

![1653363100502](.\images\1653363100502.png)

在Redis中，刚好有一个命令，可以实现id的自增功能，那就是**`INCR`自增命令**。

由于使用的是Redis自增命令是实现全局唯一ID，所以不论mysql数据库被拆分成多少表，使用的依旧是同一个Redis中的数据，能够确保拆分的所有表之间的ID也都是唯一的。

使用Redis的INCR命令进行自增，能够实现数据的唯一性以及递增性，由于使用的是Redis命令的方式，所以也满足高可用性与高性能的特性。但是，其不满足安全性，因为如果仅仅使用INCR命令对ID进行自增，还是很容易被用户看出ID的规律的。

所以为了增加ID的安全性，我们不直接使用Redis自增的数值，而是拼接一些其它信息，并且为了提高数据的性能，**ID需要设置成数值类型`bigint`**，也就是Java中的`Long`类型来作为ID的类型插入数据库，因为在数据库中，相比于字符串类型，数值类型占用的空间更小，并且更加适合索引，能够提高索引的查询效率。



**ID的组成：**

![image-20240623234309763](.\images\image-20240623234309763.png)

Long类型的数据占用的内存大小是8个字节，也就是`64个bit`。在这64位bit中：

* 第`1`位是**符号位**，永远是0，代表id永远是整数。符号位不需要设定，正数符号位就是0。

* 之后`31`位bit，表示的**时间戳**，以秒为单位。31位bit，可以支持大约69年的秒数。我们会定义一个初始的时间，比如2000年1月1日 0时0分0秒，然后会计算当前时间与设定时间的时间差是多少秒，将秒数记录下来。

* 最后`32`位bit，表示的是**序列号**，相当于秒内计数器，使用的是Redis的自增长来实现。由于其一共占用32位bit，也就是一共有2^32种数值。再加上前面的时间戳，也就是说，在同一秒内，可以有2^32不同的序列号值。



Redis并不是生成全局唯一ID的唯一方案。





### 使用Redis实现全局唯一ID

创建一个类RedisIdWorker，用于使用Redis实现全局ID的生成。使用@Component修饰该类，将该类放入到IOC容器中，方便我们调用。

```java
@Component
public class RedisIdWorker {
}
```

然后，就是去创建一个方法`nextId()`，用于获取全局ID。传入`keyPrefix`参数，表示key的前缀，不同的业务，都会有不同的自增长key用于生成全局ID，所以我们需要一个key的前缀用于获取当前业务所对应的自增长key。

```java
@Component
public class RedisIdWorker {
    public Long nextId(String keyPrefix){
        
    }
}
```

之后，我们就是来编写获取全局ID的方法了。

我们来看看全局ID的组成：

![image-20240623234309763](.\images\image-20240623234309763.png)

第一位符号位我们不需要管，只要ID是正数，符号位就是0.

那么，生成全局ID的过程就是：

1. `生成时间戳`

2. `生成序列号`

3. `拼接时间戳与序列号`

**生成时间戳**

生成时间戳时，首先我们得去设定一个开始的时间，获取当前时间与开始时间之间的时间差（秒数），作为全局ID的第2位-第32位bit的值。

由于时间戳占用31位bit，转换成数值大约可以存储69年的时间，所以，我们设定的开始时间不宜离目前太远，也不宜离当前太近，因为离得太近的话对于某些业务需求可能不能满足，并且不得超过当前时间。所以，我设定了选择2000-01-01 00:00:00作为开始时间：

```java
/**
 * 开始的时间，设置为2000-1-1 00:00:00，以秒为单位
 */
private static final long BEGIN_TIMESTAMP = LocalDateTime.of(2000, 1, 1, 0, 0, 0).toEpochSecond(ZoneOffset.UTC);
```

`toEpochSecond()`表示计算从1970-01-01T00:00:00Z开始到指定日期时间的秒数，也就是2000-01-01 00:00:00的秒数。

那么，全局ID中时间戳的计算就是：

```java
public Long nextId(String keyPrefix){
    //生成时间戳
    long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    long timestamp = now - BEGIN_TIMESTAMP;
}
```



**生成序列号**

序列号使用的是Redis的自增长的方式，在Redis中记录了一个key，用于保存自增长的数据。每次要去创建一个新数据时，就使用`INCR`命令，让该key自增长1，并且返回，那么返回得到的数据就可以作为我们的序列号数据。

这里有一个问题：Redis中，对于自增长的key会有一个上限，上限是2^64次方，并且我们获取到的序列号值（也就是自增长值）也有一个上限，上限是2^32次方，如果在实际的生产中，我们都是使用这一个自增长key，那么，随着时间的推移，自增长key的值会越来越大，有可能会超出序列号值的上限以及Redis对自增长key的上限。

所以，我们需要让这个自增长的key有一个限制，不能超过2^32次方。该怎么做？其实很简单，我们可以设置每天使用一个新的自增长key，这样一来，每天的key数值都不会很大，并且由于前面时间戳的限制，肯定不会出现ID相同的情况。

那么，具体实现的代码是：

```java
//生成序列号
//使用Redis的自增长实现，每天使用一个新的key
String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
Long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);
```

获取到的count，就是序列号的值。

这里的`increment()`方法，就是Redis命令INCR，当Redis中有该数据时，increment()方法会去将该数据自增1，并且返回值；当Redis中没有数据时，则increment()方法会去创建数据，并将数据自增1，并返回值。所以，我们不需要担心Redis中没有对应的序列号key，它会自动帮我们创建。



**拼接并返回**

我们得到时间戳以及序列号，但是如果直接加起来，并返回值的话，是无法实现在全局ID中，2-32位使用时间戳，33-64位使用序列号这种格式的：

![image-20240624013747779](.\images\image-20240624013747779.png) 

那如果使用字符串拼接呢？

将时间戳转换成字符串，将序列号也转换成字符串，然后拼接起来再转换回Long类型。这种方式也不行，如果序列号的数值比较小，比如就是1，那么和时间戳拼接起来，序列号也占不了32位bit，时间戳所占用的位数也不对。

所以，不能使用字符串拼接。

那该怎么办？

这里就需要使用到**`位运算`**了。

可以这样思考：我们需要让时间戳数据占用的位置是在第2到第32位，让序列号占用的位置是在第33-64位。那么，我们可以将时间戳转换成二进制类型，让序列号也转换成二进制类型，时间戳与序列号的值是不够大的，致使这两个Long类型的数据，转换成二进制数据后，前面的bit位都是0，所以将数据向左移动时，真正存储数据的bit位并不会移动出去。

那么，我们可以**将时间戳转换成的二进制数据，向左移动32位（空出来的位置会使用0填充），空出32位bit，让这空出的32位bit（全是0），再与序列号的二进制数进行或运算，这样就能够将序列号的二进制数转移到这空出来的32位bit上，这样就实现了时间戳与序列号的拼接。**

所以，拼接操作对应的代码是：

```java
//拼接时间戳与序列号并返回
//让时间戳左移32位并与序列号进行与运算
return timestamp << 32 | count;
```

左移32位，这32位为了不写死，使用一个常量来存储：

```java
/**
 * 序列号的位数
 */
private static final int COUNT_BITS = 32;
```



**那么，最终基于Redis实现全局ID生成器的代码如下所示：**

```java
@Component
public class RedisIdWorker {
    /**
     * 开始的时间，设置为2000-1-1 00:00:00，以秒为单位
     */
    private static final long BEGIN_TIMESTAMP = LocalDateTime.of(2000, 1, 1, 0, 0, 0).toEpochSecond(ZoneOffset.UTC);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    /**
     * 序列号的位数
     */
    private static final int COUNT_BITS = 32;

    public Long nextId(String keyPrefix){
        //生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;

        //生成序列号
        //使用Redis的自增长实现，每天使用一个新的key
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        Long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);

        //拼接时间戳与序列号并返回
        //让时间戳左移32位并与序列号进行与运算
        return timestamp << COUNT_BITS | count;
    }
}
```







## 2、实现秒杀下单

对于下单一些限定时间限定数量的商品，就是实现秒杀下单业务。比如在本项目中的限定优惠券，需要进行限时的抢购，就是秒杀下单业务：

<img src=".\images\image-20240624142757025.png" alt="image-20240624142757025" style="zoom:50%;" /> 

秒杀下单应该考虑的内容有两点：

* 1、秒杀是否开始或结束，如果尚未开始或已经结束则无法下单；
* 2、库存是否充足，不足则无法下单。

下单核心逻辑分析：

当用户开始下单，我们应当去查询优惠券信息，查询到优惠券信息，判断是否满足条件，比如时间是否充足，如果时间充足，则进一步判断库存是否足够，如果两者都满足，则扣除库存，创建订单，然后返回订单id，如果有一个条件不满足则直接结束。

<img src=".\images\1653366238564.png" alt="1653366238564" style="zoom:80%;" /> 









### 超卖问题













### 一人一单功能











# 四、分布式锁（:star2:）















## Redis优化秒杀







# 五、消息队列















---





# 四、达人探店





# 五、好友关注



# 六、附近商户





# 七、用户签到





# 八、UV统计



