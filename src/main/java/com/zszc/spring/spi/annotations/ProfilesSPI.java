package com.zszc.spring.spi.annotations;


import com.zszc.spring.spi.handler.ProfilesProxyFactory;

import java.lang.annotation.*;

/**
 * 配置文件代理 SPI
 * 通过 spring 配置文件来实现spi注入
 * @author Luiz
 * @date 2024/11/22
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ProxySPI(ProfilesProxyFactory.class)
public @interface ProfilesSPI {

    /**
     * spring environment attribute
     */
    String value();

}
