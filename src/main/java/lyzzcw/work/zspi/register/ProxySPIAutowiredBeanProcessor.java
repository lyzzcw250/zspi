package lyzzcw.work.zspi.register;


import lyzzcw.work.zspi.annotations.AutowiredSPI;
import lyzzcw.work.zspi.support.AnnotationMeta;
import lyzzcw.work.zspi.support.AutowiredInvocation;
import lyzzcw.work.zspi.support.ProxySPIFactoryBeanParam;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;


@Component
public class ProxySPIAutowiredBeanProcessor extends BaseAutowiredBeanProcessor {

    @Override
    public List<Class<? extends Annotation>> interceptAnnotation() {
        return Collections.singletonList(AutowiredSPI.class);
    }

    @Override
    public Object getInjectedBean(AutowiredInvocation invocation) {
        Class<?> injectedType = invocation.getInjectedType();
        return springContext.getBean(getBeanName(injectedType));
    }

    @Override
    protected void beforeInjection(AnnotatedInjectionMetadata metadata) {
        for (AnnotatedFieldElement fieldElement : metadata.getFieldElements()) {
            Class<?> injectedType = fieldElement.getField().getType();
            AnnotationMeta annotationMeta = fieldElement.getAnnotationMeta();
            RootBeanDefinition beanDefinition = getRootBeanDefinition(injectedType, annotationMeta);
            String beanName = getBeanName(injectedType);
            if (!beanDefinitionRegistry.containsBeanDefinition(beanName)){
                beanDefinitionRegistry.registerBeanDefinition(beanName,beanDefinition);
            }
        }
    }


    public RootBeanDefinition getRootBeanDefinition(Class<?> injectedType, AnnotationMeta annotationMeta) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(ProxySPIFactoryBean.class);
        ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
        ProxySPIFactoryBeanParam param = new ProxySPIFactoryBeanParam(injectedType,annotationMeta);
        constructorArgumentValues.addGenericArgumentValue(param);
        //标记 Bean 定义为主要候选者（@Primary 的效果）
        beanDefinition.setPrimary(true);
        //根据类型匹配注入依赖
        beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        return beanDefinition;
    }

    public String getBeanName(Class<?> injectedType){
        return "SPI$$" + Introspector.decapitalize(injectedType.getSimpleName());
    }

}
