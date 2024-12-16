# 1、spring-spi
spring 动态依赖注入扩展

- @EnableSpringSPI
- @AutowiredSPI
- @ProfilesSPI
- @DBSPI
- @SPIClass


# 2、快速开始
引入依赖
```xml
    <dependency>
        <groupId>com.zszc.component</groupId>
        <artifactId>spring.spi</artifactId>
        <version>1.0-SNAPSHOT</version>
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


## 2.1 AutowiredSPI注解

`@AutowiredSPI` 进行依赖注入 \
`@ProfilesSPI` application.yml文件配置 \
`@DBSPI` mysql配置，修改后无需重启服务即可生效 

### 2.1.1 ProfilesSPI 文件配置

application.yml 文件配置
```yaml
spring:
  spi:
    sms:
      service: txsms
```
```java
@ProfilesSPI("${spring.spi.sms.service}")
public interface SmsService {
}

@SPIClass("alisms")
@Component
public class SmsAService implements SmsService {
}

@SPIClass("txsms")
@Component
public class SmsBService implements SmsService {
}

// 依赖注入
@AutowiredSPI
private SmsService smsService;
```

### 2.1.2 DBSPI 数据库配置

启动类注解需开启db功能，否则代码中使用`@DBSPI`相关注解，项目启动异常：
```
Injection of autowired dependencies failed
```
```java
@SpringBootApplication
@EnableSpringSPI(enableDB = true)
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
@DBSPI("pay.service")
public interface PayService {
}

@Service
@SPIClass("alipay")
public class PayAService implements PayService {
}

@Service
@SPIClass("wxpay")
public class PayBService implements PayService {
}

// 依赖注入
@AutowiredSPI
private PayService payService;
```