package lyzzcw.work.zspi.annotations;


import lyzzcw.work.zspi.config.EnableSpringSPIImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


/**
 * 在Spring配置类上标记 @EnableSpringSPI 注解 启用功能
 *
 * @author 84428
 * @date 2024/11/22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({EnableSpringSPIImportSelector.class})
@Documented
public @interface EnableSpringSPI {

    boolean enableDBProxy() default false;

}
