package com.atguigu.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @ClassName: SpringIocInit
 * @Package: com.atguigu.config
 * @Author cheng
 * @Create 2024/6/10 22:41
 * @Description: TODO
 */
public class SpringIocInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    //指定root容器对应的配置类
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{MapperJavaConfig.class, ServiceJavaConfig.class, DataSourceJavaConfig.class};
    }

    //指定web容器对应的配置类
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebJavaConfig.class};
    }

    //指定DispatcherServlet的处理路径，一般为/
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
