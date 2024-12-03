package lyzzcw.work.zspi.annotations;


import java.lang.annotation.*;

/**
 * 自动装配 SPI
 *
 * @author Luiz
 * @date 2024/11/27
 */
@Inherited
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutowiredSPI {


}
