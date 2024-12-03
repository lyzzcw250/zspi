package lyzzcw.work.zspi.support;

import lombok.Data;

/**
 * proxy spifactory bean 参数
 *
 * @author Luiz
 * @date 2024/11/27
 */
@Data
public class ProxySPIFactoryBeanParam {

    private Class<?> targetClass;
    private AnnotationMeta annotationMeta;

    public ProxySPIFactoryBeanParam(Class<?> targetClass, AnnotationMeta annotationMeta) {
        this.targetClass = targetClass;
        this.annotationMeta = annotationMeta;
    }
}
