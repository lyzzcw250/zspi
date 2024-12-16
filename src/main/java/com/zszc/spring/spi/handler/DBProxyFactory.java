package com.zszc.spring.spi.handler;


import com.zszc.spring.spi.handler.dao.DBProxyDao;
import com.zszc.spring.spi.handler.dao.DBProxyDaoImpl;
import com.zszc.spring.spi.support.EnableDBProxyCondition;
import com.zszc.spring.spi.support.SpringBeanContext;
import lombok.Getter;
import com.zszc.spring.spi.annotations.DBSPI;
import com.zszc.spring.spi.register.AnnotationProxyFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Conditional(EnableDBProxyCondition.class)
@Getter
public class DBProxyFactory implements AnnotationProxyFactory<DBSPI> {

    private DBProxyDao dbProxyDao;

    public DBProxyFactory(DataSource dataSource) {
        this.dbProxyDao = new DBProxyDaoImpl(dataSource);
    }

    public SqlSessionFactory getSqlSessionFactory(){
        return this.dbProxyDao.getSqlSessionFactory();
    }

    @Override
    public Object getProxy(Class<?> targetClass, DBSPI spi) {
        String value = dbProxyDao.getService(spi.value());
        Object bean = null;
        try {
            bean = SpringBeanContext.getBeanByAllName(value);
        } catch (Exception e) {
            throw new RuntimeException("can not find yml spiBean for value " + value,e);
        }
        return bean;
    }


}
