# Spring Cloud构建微服务架构：Hystrix监控数据聚合(通过消息代理收集聚合)

- 参考：[http://blog.didispace.com/spring-cloud-starter-dalston-5-2/](http://blog.didispace.com/spring-cloud-starter-dalston-5-2/)


## 通过消息代理收集聚合

### 监控应用的修改
- pom.xml 引入如下依赖
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-turbine-amqp</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

- 在应用主类中使用`@EnableTurbineStream`注解来启用`Turbine Stream`的配置。
```java
package org.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream;

@SpringCloudApplication
@EnableTurbineStream
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	} 

}

```
- 配置`application.yml`文件：
```yml
spring:
    application:
        name: turbine-amqp
    rabbitmq:
        host: 192.168.116.10
        port: 5672
        username: admin
        password: admin
server:
    port: 8989
management:
    port: 8990
    
eureka:
    client:
        serviceUrl:
            defaultZone: http://localhost:1111/eureka/ 
```

### 被监控应用的修改
- pom.xml 引入如下依赖
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-netflix-hystrix-amqp</artifactId>
</dependency>
```

- `application.yml`文件需要添加rabbitmq配置


消息代理应用，参看：[https://github.com/916812579/spring-cloud/tree/master/demo16](https://github.com/916812579/spring-cloud/tree/master/demo16)

被监控应用参看: [https://github.com/916812579/spring-cloud/tree/master/demo17](https://github.com/916812579/spring-cloud/tree/master/demo17)


