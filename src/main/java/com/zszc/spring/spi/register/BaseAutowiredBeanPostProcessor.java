package com.zszc.spring.spi.register;


import com.zszc.spring.spi.support.AutowiredInvocation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import com.zszc.spring.spi.support.AnnotationMeta;
import com.zszc.spring.spi.utils.AnnotationPlusUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * 基本自动装配 Bean 后处理器
 *
 * @author Luiz
 * @date 2024/11/27
 */
@Getter
@Slf4j
public abstract class BaseAutowiredBeanPostProcessor extends BaseSpringAware implements
        Ordered,
        InstantiationAwareBeanPostProcessor,
        BeanFactoryPostProcessor,
        DisposableBean,
        AutowiredBeanProcessor {

    private final List<Class<? extends Annotation>> annotationTypes = new ArrayList<>();

    private final ConcurrentMap<String,AnnotatedInjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<>(32);

    protected BaseAutowiredBeanPostProcessor() {
        this.annotationTypes.addAll(interceptAnnotation());
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 2;
    }
    /**
     *  自定义注入BeanDefinition
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Class<?> beanType = beanFactory.getType(beanName);
            if (beanType == null){
                continue;
            }

            AnnotatedInjectionMetadata injectionMetadata = findInjectionMetadata(beanName, beanType, null);
            if (injectionMetadata != null){
                beforeInjection(injectionMetadata);
            }
        }
    }

    /**
     * 自定义依赖注入
     */
    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        AnnotatedInjectionMetadata metadata = findInjectionMetadata(beanName, bean.getClass(), pvs);
        try {
            if(metadata != null && !metadata.getFieldElements().isEmpty()){
                metadata.inject(bean, beanName, pvs);
            }
        }
        catch (BeanCreationException ex) {
            throw ex;
        }
        catch (Throwable ex) {
            throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
        }
        return pvs;
    }

    protected void beforeInjection(AnnotatedInjectionMetadata metadata) {

    }

    protected AnnotatedInjectionMetadata findInjectionMetadata(String beanName, Class<?> clazz, PropertyValues pvs){
        String cacheKey = (StringUtils.hasLength(beanName) ? beanName : clazz.getName());
        AnnotatedInjectionMetadata metadata = this.injectionMetadataCache.get(cacheKey);
        if (needsRefreshInjectionMetadata(metadata, clazz)) {
            synchronized (this.injectionMetadataCache) {
                metadata = this.injectionMetadataCache.get(cacheKey);
                if (needsRefreshInjectionMetadata(metadata, clazz)) {
                    if (metadata != null) {
                        metadata.clear(pvs);
                    }
                    try {
                        metadata = buildAnnotatedMetadata(clazz);
                        this.injectionMetadataCache.put(cacheKey, metadata);
                    } catch (NoClassDefFoundError err) {
                        throw new IllegalStateException("Failed to introspect object class [" + clazz.getName() +
                                "] for annotation metadata: could not find class that it depends on", err);
                    }
                }
            }
        }
        return metadata;
    }

    private AnnotatedInjectionMetadata buildAnnotatedMetadata(Class<?> beanClass) {
        Collection<AnnotatedFieldElement> elements = findFieldAnnotationMetadata(beanClass);
        return  new AnnotatedInjectionMetadata(beanClass,elements);
    }


    protected List<AnnotatedFieldElement> findFieldAnnotationMetadata(final Class<?> beanClass){
        List<AnnotatedFieldElement> elements = new ArrayList<>();
        ReflectionUtils.doWithFields(beanClass,(field -> {
            for (Class<? extends Annotation> annotationType : annotationTypes) {
                //判断是否包含指定注解元信息
                AnnotationMeta annotationMeta = AnnotationPlusUtils.getAnnotationMeta(field, annotationType, getEnvironment(), false, true);
                if (annotationMeta != null) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        if (log.isWarnEnabled()) {
                            log.warn("@" + annotationType.getName() + " is not supported on static fields: " + field);
                        }
                        return;
                    }
                    elements.add(new AnnotatedFieldElement(field, annotationMeta));
                }
            }
        }));
        return elements;
    }

    private boolean needsRefreshInjectionMetadata(AnnotatedInjectionMetadata metadata, Class<?> clazz) {
        return (metadata == null || metadata.needsRefresh(clazz));
    }


    protected static class AnnotatedInjectionMetadata extends InjectionMetadata {

        private Class<?> targetClass;

        private final Collection<AnnotatedFieldElement> fieldElements;

        public AnnotatedInjectionMetadata(Class<?> targetClass, Collection<AnnotatedFieldElement> fieldElements) {
            super(targetClass, combine(fieldElements));
            this.targetClass = targetClass;
            this.fieldElements = fieldElements;
        }

        protected boolean needsRefresh(Class<?> clazz) {
            if (this.targetClass == clazz) {
                return false;
            }
            //IGNORE Spring CGLIB enhanced class
            if (targetClass.isAssignableFrom(clazz) &&  clazz.getName().contains("$$EnhancerBySpringCGLIB$$")) {
                return false;
            }
            return true;
        }

        public Collection<AnnotatedFieldElement> getFieldElements() {
            return fieldElements;
        }
    }

    private static <T> Collection<T> combine(Collection<? extends T>... elements) {
        List<T> allElements = new ArrayList<T>();
        for (Collection<? extends T> e : elements) {
            allElements.addAll(e);
        }
        return allElements;
    }


    protected class AnnotatedInjectElement extends InjectionMetadata.InjectedElement {

        protected final AnnotationMeta annotationMeta;

        private Class<?> injectedType;

        protected AnnotatedInjectElement(Member member, PropertyDescriptor pd,AnnotationMeta annotationMeta) {
            super(member, pd);
            this.annotationMeta = annotationMeta;
            try {
                this.injectedType = getInjectedTypeFormMember();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {

            AutowiredInvocation invocation = new AutowiredInvocation();
            invocation.setTargetBean(bean);
            invocation.setTargetBeanName(beanName);
            invocation.setAnnotationMeta(annotationMeta);
            invocation.setInjectedType(injectedType);
            invocation.setInjectedMember(member);

            Object injectedObject = getInjectedBean(invocation);
            if (member instanceof Field) {
                Field field = (Field) member;
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field,bean,injectedObject);
            } else if (member instanceof Method) {
                Method method = (Method) member;
                ReflectionUtils.makeAccessible(method);
                method.invoke(bean, injectedObject);
            }
        }

        public Class<?> getInjectedTypeFormMember() throws ClassNotFoundException {
            if (injectedType == null) {
                if (this.isField) {
                    injectedType = ((Field) this.member).getType();
                }
                else if (this.pd != null) {
                    return this.pd.getPropertyType();
                }
                else {
                    Method method = (Method) this.member;
                    if (method.getParameterTypes().length > 0) {
                        injectedType = method.getParameterTypes()[0];
                    } else {
                        throw new IllegalStateException("get injected type failed");
                    }
                }
            }
            return injectedType;
        }

        public AnnotationMeta getAnnotationMeta() {
            return annotationMeta;
        }
    }

    public class AnnotatedFieldElement extends AnnotatedInjectElement {

        protected final Field field;

        public AnnotatedFieldElement(Field field, AnnotationMeta annotationMeta) {
            super(field,null,annotationMeta);
            this.field = field;
        }

        public Field getField() {
            return field;
        }
    }

    public final Class<? extends Annotation> getAnnotationType() {
        return annotationTypes.get(0);
    }

    @Override
    public void destroy() throws Exception {
        injectionMetadataCache.clear();
    }
}
