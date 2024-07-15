# 注意！Mapper接口使用@Repository注解没有作用

> **`注意！注意！注意！`**
>
> 我原本以为，需要给Mapper接口添加一个@Repository注解，将其放入到IOC容器中就会生效。
>
> 但是，实际上这个注解去修饰接口是不会起作用的！
>
> 因为@Component、@Repository、@Service、@Controller这四个注解的作用是给类创建Bean对象，并放入到IOC容器中，给接口是创建不了对象的，所以给接口设置Bean的注解没有用。
>
> 真正起作用的，实际上是SSM中配置的Mapper扫描路径：
>
> ```java
> @Bean
> public MapperScannerConfigurer mapperScannerConfigurer(){
>     MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
>     mapperScannerConfigurer.setBasePackage("com.atguigu.mapper");
>     return mapperScannerConfigurer;
> }
> ```
>
> 所以，我们才要求Mapper接口与Mapper映射文件放在同一个目录下，这样才能将Mapper接口与Mapper映射文件都进行扫描。
>
> 
>
> 在实际开发中，我们使用SpringBoot项目，在SpringBoot项目中，Mapper接口与Mapper映射文件不会放在同一个目录结构下。此时，我们可以使用两种方式让Mapper接口起作用：
>
> 1. **在启动类中扫描Mapper包**
>
>    在启动类中，使用`@MapperScan`注解去扫描指定的mapper包，该包下的mapper接口就能够生成代理类对象，放入到IOC容器中，并能与mapper映射文件产生映射关系
>
>    ```java
>    @SpringBootApplication
>    @MapperScan("com.atguigu.mapper")
>    public class MybatisApplication {
>        public static void main(String[] args) {
>            SpringApplication.run(MybatisApplication.class, args);
>        }
>    }
>    ```
>
> 2. 在Mapper接口中，使用`@Mapper`注解表示当前接口是一个Mapper接口，此时也能够起作用：
>
>    ```java
>    @Mapper
>    public interface UserMapper {
>        List<User> queryAll();
>    }
>    ```
>
>    以上这两种方式都可以使用，也可以一起使用。
>
> 那么此时，我们可以将Mapper接口与Mapper映射文件放在不同的包下，在application.yml中配置的是Mapper映射文件所存放的位置：
>
> ![image-20240613110545200](.\images\image-20240613110545200.png) 
>
> 该位置是在resources/mapper/下。
>
> 此时，mapper接口与mapper映射文件分开指定，就不需要放在同一个结构下了，这样比较灵活。