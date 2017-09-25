# Spring Cloud构建微服务架构-分布式配置中心（高可用）

- 参考：[http://blog.didispace.com/springcloud4-2/](http://blog.didispace.com/springcloud4-2/)


## 高可用问题

### 传统作法

将所有的Config Server都指向同一个Git仓库，这样所有的配置内容就通过统一的共享文件系统来维护，而客户端在指定Config Server位置时，只要配置Config Server外的均衡负载即可。

### 注册为服务

把配置服务当做微服务进行注册


##  注册为服务的配置

- pom.xml引入如下配置,比普通的配置服务多了`spring-cloud-starter-eureka`的配置
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-config-server</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
```

- 在`application.yml`中配置参数`eureka.client.serviceUrl.defaultZone`以指定服务注册中心的位置，详细内容如下：

```yml
eureka:
    client:
        serviceUrl:
            defaultZone: http://localhost:1111/eureka/
```

- 在`application.yml`添加配置库相关配置
```yml
server:
    port: 7001
spring:
    application:
        name: config-server

# git管理配置
    cloud:
        config:
            server:
                git:
                    uri: https://github.com/916812579/spring-cloud
                    searchPaths: config/service-config
                    username: 916812579
                    password: ******
```

- 在应用主类中，新增`@EnableDiscoveryClient`注解，用来将`config-server`注册到上面配置的服务注册中心上去
```java
package org.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```

- 启动该应用，并访问`http://localhost:1111/`(Eureka Server地址)，可以在`Eureka Server`的信息面板中看到`config-server`已经被注册了。

![](https://github.com/916812579/spring-cloud/raw/master/demo10/config.png)





 
 
