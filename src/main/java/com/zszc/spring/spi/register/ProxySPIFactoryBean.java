package com.zszc.spring.spi.register;


import com.zszc.spring.spi.support.ProxySPIFactoryBeanParam;
import com.zszc.spring.spi.support.SpringBeanContext;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import com.zszc.spring.spi.annotations.ProxySPI;
import com.zszc.spring.spi.exceptions.ProxySPIRuntimeException;
import com.zszc.spring.spi.support.AnnotationMeta;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.AbstractLazyCreationTargetSource;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Slf4j
@Setter
public class ProxySPIFactoryBean<T> extends BaseSpringAware implements FactoryBean<Object> {

    private Object lazyProxy;

    private Class<?> targetClass;

    private AnnotationMeta annotationMeta;

    private ProxySPI proxySPI ;


    public ProxySPIFactoryBean() {
    }

    public ProxySPIFactoryBean(ProxySPIFactoryBeanParam param) {
        this.targetClass = param.getTargetClass();
        this.annotationMeta = param.getAnnotationMeta();
        proxySPI = AnnotatedElementUtils.getMergedAnnotation(targetClass, ProxySPI.class);
    }

    @Override
    public Object getObject() throws Exception {
        if (lazyProxy == null) {
            createLazyProxy();
        }
        return (T) lazyProxy;
    }

    private class ProxySPILazyInitTargetSource extends AbstractLazyCreationTargetSource {

        @Override
        protected Object createObject() throws Exception {
            return getCallProxy();
        }

        @Override
        public synchronized Class<?> getTargetClass() {
            return targetClass;
        }
    }

    private Object getCallProxy() throws Exception {
        return ProxySpiLoader.load(targetClass);
    }

    private void createLazyProxy() {
        ProxyFactory proxyFactory = new ProxyFactory();
        //延迟加载目标对象
        proxyFactory.setTargetSource(new ProxySPILazyInitTargetSource());
        proxyFactory.addInterface(targetClass);

        Annotation obj = ProxySpiLoader.getSPIObject(targetClass);
        if (obj == null){
            throw new BeanCreationException(targetClass.getName() + "请标记ProxySPI的注解或者ProxySPI复合注解");
        }
        AnnotationProxyFactory springBean = SpringBeanContext.getBean(proxySPI.value());

        proxyFactory.addAdvice(new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                Object bean = springBean.getProxy(targetClass,obj);
                Method method = methodInvocation.getMethod();
                if (bean != null){
                    return method.invoke(bean, methodInvocation.getArguments());
                }
                throw new ProxySPIRuntimeException("can not find Specific execution SPI bean for class " + targetClass + ", by use factory " + springBean.getClass().getSimpleName());
            }
        });

        this.lazyProxy = proxyFactory.getProxy(this.beanClassLoader);
    }

    @Override
    public Class<?> getObjectType() {
        return targetClass;
    }
}
