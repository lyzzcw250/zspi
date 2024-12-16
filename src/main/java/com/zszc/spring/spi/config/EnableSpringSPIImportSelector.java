package com.zszc.spring.spi.config;

import com.zszc.spring.spi.annotations.EnableSpringSPI;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 启用 Spring SpiImport 选择器
 *
 * @author Luiz
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
            boolean enableDB = attributes.getBoolean("enableDB");
            // 设置环境属性，供 Condition 使用
            System.setProperty("enableDB", String.valueOf(enableDB));
        }
        return new String[]{SpringSpiConfiguration.class.getName()};
    }
}
