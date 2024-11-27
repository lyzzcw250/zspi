package lyzzcw.work.zspi.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Spring Bean 上下文
 *
 * @author 84428
 * @date 2024/11/22
 */
public abstract class SpringBeanContext implements ApplicationContextAware {


    protected static ApplicationContext springContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (springContext == null){
            springContext = applicationContext;
        }
    }
    /**
     * Bean SPI 名称
     */
    private static final Map<String,String> BEAN_SPI_NAME = new ConcurrentHashMap<>();


    /**
     * 获取 Bean
     *
     * @param name 名字
     * @return {@link T }
     */
    public static <T> T getBean(String name){
        try {
            return (T) springContext.getBean(name);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    /**
     * 获取 Bean
     *
     * @param clz
     * @return {@link T }
     */
    public static <T> T getBean(Class<T> clz) {
        try {
            return springContext.getBean(clz);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    /**
     * 注册 Bean 别名
     *
     * @param beanName  Bean 名称
     * @param aliasName 别名
     */
    public static void registerBeanAlias(String beanName,String aliasName){
        if (BEAN_SPI_NAME.containsKey(aliasName)){
            throw new RuntimeException("bean name ["+aliasName+"] already exist");
        }
        BEAN_SPI_NAME.put(aliasName,beanName);
    }


    /**
     * 按别名获取 Bean
     *
     * @param aliasName 别名
     * @return {@link T }
     */
    public static <T> T getBeanByAliasName(String aliasName){
        String originBeanName = BEAN_SPI_NAME.get(aliasName);
        if (StringUtils.isEmpty(originBeanName)){
            throw new RuntimeException("can not find bean for alias name ["+aliasName+"] , because it not exist");
        }

        return getBean(originBeanName);
    }


    /**
     * 按所有名称获取 Bean
     *
     * @param name 名字
     * @return {@link T }
     */
    public static <T> T getBeanByAllName(String name){
        try {
            return (T) springContext.getBean(name);
        } catch (NoSuchBeanDefinitionException e) {
            return getBeanByAliasName(name);
        }
    }

}
