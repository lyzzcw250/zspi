package lyzzcw.work.zspi.support;

import lombok.Data;

import java.lang.reflect.Member;


/**
 * Autowired 调用
 *
 * @author Luiz
 * @date 2024/11/27
 */
@Data
public class AutowiredInvocation {

    /**
     *  target beans that require injection operations
     */
    private Object targetBean;

    /**
     *  target beans name that require injection operations
     */
    private String targetBeanName;

    /**
     *  The member fields that need to be injected into the targetBean
     */
    private Member injectedMember;

    /**
     *  need injected class type
     */
    private Class<?> injectedType;

    /**
     *  the meta of mark annotation
     */
    private AnnotationMeta annotationMeta;

}
