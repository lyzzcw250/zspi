package lyzzcw.work.zspi.handler;


import lombok.Getter;
import lyzzcw.work.zspi.annotations.DBProxySPI;
import lyzzcw.work.zspi.handler.dao.DBProxyDao;
import lyzzcw.work.zspi.handler.dao.DBProxyDaoImpl;
import lyzzcw.work.zspi.register.AnnotationProxyFactory;
import lyzzcw.work.zspi.support.EnableDBProxyCondition;
import lyzzcw.work.zspi.support.SpringBeanContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Conditional(EnableDBProxyCondition.class)
@Getter
public class DBProxyFactory implements AnnotationProxyFactory<DBProxySPI> {

    private DBProxyDao dbProxyDao;

    public DBProxyFactory(DataSource dataSource) {
        this.dbProxyDao = new DBProxyDaoImpl(dataSource);
    }

    public SqlSessionFactory getSqlSessionFactory(){
        return this.dbProxyDao.getSqlSessionFactory();
    }

    @Override
    public Object getProxy(Class<?> targetClass,DBProxySPI spi) {
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
