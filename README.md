# 1、spring-spi
spring 动态依赖注入扩展

- @EnableSpringSPI
- @AutowiredProxySPI
- @ProfilesProxySPI
- @DBProxySPI
- @BeanAliasName


# 2、快速开始
引入依赖
```xml
    <dependency>
        <groupId>lyzzcw.work.zspi</groupId>
        <artifactId>zspi-parents</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
```
在spring启动类上标记 @EnableSpringSPI 注解
```java
@SpringBootApplication
@EnableSpringSPI
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```


## 2.1 AutowiredProxySPI注解

`@AutowiredProxySPI` 进行依赖注入 \
`@ProfilesProxySPI` application.yml文件配置 \
`@DBProxySPI` mysql配置，修改后无需重启服务即可生效 

### 2.1.1 ProfilesProxySPI 文件配置

application.yml 文件配置
```yaml
spring:
  spi:
    sms:
      service: txsms
```
```java
@ProfilesProxySPI("${spring.spi.sms.service}")
public interface SmsService {
}

@BeanAliasName("alisms")
@Component
public class SmsAService implements SmsService {
}

@BeanAliasName("txsms")
@Component
public class SmsBService implements SmsService {
}

// 依赖注入
@AutowiredProxySPI
private SmsService smsService;
```

### 2.1.2 DBProxySPI 数据库配置

启动类注解需开启db功能，否则代码中使用`@DBProxySPI`相关注解，项目启动异常：
```
Injection of autowired dependencies failed
```
```java
@SpringBootApplication
@EnableSpringSPI(enableDBProxy = true)
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

db_proxy 数据表配置

| id   | proxy       | service | content  |
| ---- |-------------|---------| -------- |
| 1    | pay.service | wxpay   | 支付服务 |
```java
@DBProxySPI("pay.service")
public interface PayService {
}

@Service
@BeanAliasName("alipay")
public class PayAService implements PayService {
}

@Service
@BeanAliasName("wxpay")
public class PayBService implements PayService {
}

// 依赖注入
@AutowiredProxySPI
private PayService payService;
```