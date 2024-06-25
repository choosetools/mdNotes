package com.atguigu.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @ClassName: WebJavaConfig
 * @Package: com.atguigu.config
 * @Author cheng
 * @Create 2024/6/10 18:05
 * @Description: 控制层的配置类
 */
@Configuration
@ComponentScan(value = {"com.atguigu.controller", "com.atguigu.exception"})
@EnableWebMvc
public class WebJavaConfig implements WebMvcConfigurer {
    //jsp视图解析器，配置跳转路径的前缀与后缀
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/",".jsp");
    }

    //开启静态资源扫描
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    //拦截器配置
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new 拦截器类()).addPathPatterns(指定拦截的请求路径);
    }

}
