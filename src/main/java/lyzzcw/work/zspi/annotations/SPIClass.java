package lyzzcw.work.zspi.annotations;


import java.lang.annotation.*;


/**
 * spi 注入类
 *
 * @author Luiz
 * @date 2024/11/27
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SPIClass {

    String value();

}
