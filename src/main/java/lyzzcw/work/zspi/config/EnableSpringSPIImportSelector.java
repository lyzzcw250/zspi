package lyzzcw.work.zspi.config;

import lyzzcw.work.zspi.annotations.EnableSpringSPI;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 启用 Spring SpiImport 选择器
 *
 * @author 84428
 * @date 2024/11/27
 */
public class EnableSpringSPIImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // 读取 @EnableSpringSPI 的属性值
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(EnableSpringSPI.class.getName(), false)
        );
        if (attributes != null) {
            boolean enableDBProxy = attributes.getBoolean("enableDBProxy");
            // 设置环境属性，供 Condition 使用
            System.setProperty("enableDBProxy", String.valueOf(enableDBProxy));
        }
        return new String[]{SpringSpiConfiguration.class.getName()};
    }
}
