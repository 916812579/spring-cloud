# Spring Cloud构建微服务架构 - 分布式配置中心(高可用的使用)

- 参考：[http://blog.didispace.com/springcloud4/](http://blog.didispace.com/springcloud4/)


- pom.xml引入依赖
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-config</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
```

- `bootstrap.yml`配置如下

```yml
server:
    port: 7002
spring:
    application:
        name: test

# git管理配置
    cloud:
        config:
            profile: dev
            discovery: 
                enabled: true
                serviceId: config-server
            
eureka:
    client:
        serviceUrl:
            defaultZone: http://localhost:1111/eureka/    
```

- `eureka.client.serviceUrl.defaultZone`参数指定服务注册中心，用于服务的注册与发现


- `spring.cloud.config.discovery.enabled`参数设置为true，开启通过服务来访问Config Server的功能


- `spring.cloud.config.discovery.serviceId`参数来指定Config Server注册的服务名。

- `spring.application.name`和`spring.cloud.config.profile`用来定位Git中的资源。


- 在应用主类中，增加`@EnableDiscoveryClient`注解，用来发现config-server服务，利用其来加载应用配置

```java
package org.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```
- 沿用之前我们创建的Controller来加载Git中的配置信息,注意添加`RefreshScope`

```java
package org.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
class TestController {
    @Value("${from}")
    private String from;
    @RequestMapping("/from")
    public String from() {
        return this.from;
    }
}
```

- 启动应用，访问http://localhost:7002/from可以返回from的属性内容


## 配置刷新

- 对配置内容做一些实时更新的场景

- pom.xml引入依赖, `spring-boot-starter-actuator`监控模块，其中包含了/refresh刷新API。
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
- 重新启动`config-clinet`，访问一次`http://localhost:7002/from`，可以看到当前的配置值
 
- 修改`Git`仓库`config-repo/didispace-dev.properties`文件中`from`的值
 
- 再次访问一次`http://localhost:7002/from`，可以看到配置值没有改变
 
- 通过POST请求发送到`http://localhost:7002/refresh`，我们可以看到返回内容如下，代表`from`参数的配置内容被更新了

如果提示没有权限，则`bootstrap.yml`需要加入下面的配置
```yml
management:
    security:
        enabled: false
```

- 再次访问一次`http://localhost:7002/from`，可以看到配置值已经是更新后的值了
