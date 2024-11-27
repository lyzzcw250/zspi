package lyzzcw.work.zspi.annotations;


import java.lang.annotation.*;


/**
 * Bean 别名
 *
 * @author 84428
 * @date 2024/11/27
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanAliasName {

    String value();

}
