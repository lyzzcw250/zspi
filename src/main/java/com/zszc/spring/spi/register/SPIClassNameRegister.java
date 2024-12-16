package com.zszc.spring.spi.register;



import com.zszc.spring.spi.support.SpringBeanContext;
import com.zszc.spring.spi.annotations.SPIClass;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @SPIClass spi class 信息维护
 *
 * @author Luiz
 * @date 2024/11/22
 */
@Component
public class SPIClassNameRegister implements BeanDefinitionRegistryPostProcessor {

    /**
     * 1.根据@BeanAliasName注解配置，维护bean+alise对应关系
     *
     * @param registry 注册表
     * @throws BeansException bean 异常
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (StringUtils.isEmpty(beanClassName)){
                continue;
            }
            try {
                Class<?> beanClass = Class.forName(beanClassName);
                SPIClass spiClass = beanClass.getAnnotation(SPIClass.class);
                if (spiClass != null){
                    SpringBeanContext.registerBeanAlias(beanDefinitionName,spiClass.value());
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("can not find class for beanDefinitionName " + beanDefinitionName,e);
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {


    }
}
