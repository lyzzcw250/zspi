package lyzzcw.work.zspi.support;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 启用 dbproxy 条件
 *
 * @author lzy
 * @date 2024/11/27
 */
public class EnableDBProxyCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 从环境中读取 enableDBProxy 属性
        Boolean enableDBProxy = context.getEnvironment().getProperty("enableDBProxy", Boolean.class, false);
        return Boolean.TRUE.equals(enableDBProxy);
    }
}
