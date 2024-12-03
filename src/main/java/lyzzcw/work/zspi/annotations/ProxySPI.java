package lyzzcw.work.zspi.annotations;


import lyzzcw.work.zspi.register.AnnotationProxyFactory;

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
