package lyzzcw.work.zspi.handler;



import lyzzcw.work.zspi.annotations.ProfilesSPI;
import lyzzcw.work.zspi.register.AnnotationProxyFactory;
import lyzzcw.work.zspi.support.SpringBeanContext;
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
