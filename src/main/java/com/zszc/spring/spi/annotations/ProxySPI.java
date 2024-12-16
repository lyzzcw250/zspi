package com.zszc.spring.spi.annotations;


import com.zszc.spring.spi.register.AnnotationProxyFactory;

import java.lang.annotation.*;

/**
 * 代理 SPI
 *
 * @author Luiz
 * @date 2024/11/27
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProxySPI {


    Class<? extends AnnotationProxyFactory<?>> value();


}
