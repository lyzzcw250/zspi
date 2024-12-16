package com.zszc.spring.spi.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.zszc.spring.spi.support.EnableDBProxyCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Luiz
 * @date 2024/11/22
 */
@ComponentScan("com.zszc.spring.spi")
public class SpringSpiConfiguration{
    @Bean
    @Conditional(EnableDBProxyCondition.class)
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource initDatabase(Environment env) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.resolvePlaceholders("${spring.datasource.url}"));
        dataSource.setUsername(env.resolvePlaceholders("${spring.datasource.username}"));
        dataSource.setPassword(env.resolvePlaceholders("${spring.datasource.password}"));
        dataSource.setDriverClassName(env.resolvePlaceholders("${spring.datasource.driver-class-name}"));
        dataSource.init();
        return dataSource;
    }

}
