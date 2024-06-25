package com.atguigu.config;

import com.github.pagehelper.PageInterceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @ClassName: MapperJavaConfig
 * @Package: com.atguigu.config
 * @Author cheng
 * @Create 2024/6/10 21:35
 * @Description: TODO
 */
@Configuration
public class MapperJavaConfig {

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        //指定数据库连接池对象
        sqlSessionFactoryBean.setDataSource(dataSource);
//<settings>
//        <!--将表中字段的下划线自动转换为驼峰-->
//        <setting name="mapUnderscoreToCamelCase" value="true"/>
//        <!--开启延迟加载-->
//        <setting name="lazyLoadingEnabled" value="true"/>
//    </settings>
//
//    <typeAliases>
//        <!--给类取别名-->
//        <package name="com.atguigu.pojo"/>
//    </typeAliases>
//
//
//    <!--分页插件-->
//    <plugins>
//        <plugin interceptor="com.github.pagehelper.PageInterceptor">
//            <!--
//                helperDialect：分页插件会自动检测当前的数据库链接，自动选择合适的分页方式。
//        你可以配置helperDialect属性来指定分页插件使用哪种方言。配置时，可以使用下面的缩写值：
//        oracle,mysql,mariadb,sqlite,hsqldb,postgresql,db2,sqlserver,informix,h2,sqlserver2012,derby
//                -->
//            <property name="helperDialect" value="mysql"/>
//        </plugin>
//    </plugins>
        //settings
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLazyLoadingEnabled(true);
        sqlSessionFactoryBean.setConfiguration(configuration);

        //typeAliases
        sqlSessionFactoryBean.setTypeAliasesPackage("com.atguigu.pojo");

        //分页插件配置
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        pageInterceptor.setProperties(properties);
        sqlSessionFactoryBean.addPlugins(pageInterceptor);

        return sqlSessionFactoryBean;
    }


    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.atguigu.mapper");
        return mapperScannerConfigurer;
    }

}
