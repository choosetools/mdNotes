package com.atguigu.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * @ClassName: DataSourceJavaConfig
 * @Package: com.atguigu.config
 * @Author cheng
 * @Create 2024/6/10 22:15
 * @Description: TODO
 */
@Configuration
@PropertySource("classpath:jdbc.properties") //导入外部配置文件
public class DataSourceJavaConfig {
    @Value("${jdbc.user}")
    private String user;

    @Value("${jdbc.password}")
    private String password;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.driver}")
    private String driver;

    //配置数据源对象
    @Bean
    public DataSource dataSource() {
        //创建的是Druid数据库连接池
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        return dataSource;
    }
}
