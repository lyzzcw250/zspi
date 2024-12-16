package com.zszc.spring.spi.handler;



import com.zszc.spring.spi.support.SpringBeanContext;
import com.zszc.spring.spi.annotations.ProfilesSPI;
import com.zszc.spring.spi.register.AnnotationProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class ProfilesProxyFactory implements AnnotationProxyFactory<ProfilesSPI> {

    @Autowired
    private Environment environment;

    @Override
    public Object getProxy(Class<?> targetClass, ProfilesSPI spi) {
        String value = environment.resolvePlaceholders(spi.value());
        Object bean = null;
        try {
            bean = SpringBeanContext.getBeanByAllName(value);
        } catch (Exception e) {
            throw new RuntimeException("can not find yml spiBean for value " + value,e);
        }
        return bean;
    }

}
