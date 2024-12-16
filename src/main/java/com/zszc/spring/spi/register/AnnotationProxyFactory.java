package com.zszc.spring.spi.register;

import java.lang.annotation.Annotation;


public interface AnnotationProxyFactory<T extends Annotation> {

    /**
     * @param targetClass        Object type obtained
     * @param spi                spi anno
     * @return
     */
    Object getProxy(Class<?> targetClass, T spi);

}
