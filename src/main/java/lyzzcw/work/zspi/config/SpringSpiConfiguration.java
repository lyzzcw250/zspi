package lyzzcw.work.zspi.config;


import com.alibaba.druid.pool.DruidDataSource;
import lyzzcw.work.zspi.support.EnableDBProxyCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;

@ComponentScan("lyzzcw.work.zspi")
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
