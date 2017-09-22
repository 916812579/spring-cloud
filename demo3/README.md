# Spring Cloud构建微服务架构服务消费者 （Ribbon）

- 参考：[http://blog.didispace.com/springcloud2/](http://blog.didispace.com/springcloud2/)

## Ribbon

- `Ribbon`是一个基于`HTTP`和`TCP`客户端的`负载均衡器`。`Feign`中也使用`Ribbon`。

`Ribbon`可以在通过客户端中配置的`ribbonServerList`服务端列表去`轮询访问`以达到均衡负载的作用。

当`Ribbon`与`Eureka`联合使用时，`ribbonServerList`会被`DiscoveryEnabledNIWSServerList`重写，扩展成从`Eureka`注册中心中获取服务端列表。同时它也会用`NIWSDiscoveryPing`来取代`IPing`，它将职责委托给`Eureka`来确定服务端是否已经启动。

## 使用Ribbon实现客户端负载均衡的消费者

- pom.xml中引入依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-ribbon</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

- 通过`@EnableDiscoveryClient`注解来添加发现服务能力, 创建`RestTemplate`实例，并通过`@LoadBalanced`注解开启均衡负载能力。

```java
package org.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class Application {
	
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
```
- 创建`ConsumerController`对`COMPUTE-SERVICE`服务进行消费
```java
package org.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {
    @Autowired
    RestTemplate restTemplate;
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
    	// 这里通过服务的名称进行服务的消费
        return restTemplate.getForEntity("http://COMPUTE-SERVICE/add?a=10&b=20", String.class).getBody();
    }
}
```
- `application.yml`中配置注册中心

```yml
spring:
    application:
        name: ribbon-consumer
server:
    port: 3333
eureka:
    client:
        serviceUrl:
            defaultZone: http://localhost:1111/eureka/
```

- maven 打jar包

pom.xml需要有如下配置
```xml
<build>  
    <finalName>cyc</finalName>  
    <plugins>  
        <plugin>  
            <groupId>org.springframework.boot</groupId>  
            <artifactId>spring-boot-maven-plugin</artifactId>  
        </plugin>  
    </plugins>  
</build> 
```

![](https://github.com/916812579/spring-cloud/raw/master/demo3/jar.png)

启动服务时，使用类似如下方式启动
```bash
java -jar C:\Users\hhly-pc\Desktop\jars\demo1-0.0.1-SNAPSHOT.jar
java -jar C:\Users\hhly-pc\Desktop\jars\demo2-0.0.1-SNAPSHOT.jar --server.port=2222
java -jar C:\Users\hhly-pc\Desktop\jars\demo2-0.0.1-SNAPSHOT.jar --server.port=2223
```

访问http://localhost:1111/，应该显示如下
![](https://github.com/916812579/spring-cloud/raw/master/demo3/eureka.png)

- 启动应用

访问http://localhost:3333/add两次
![](https://github.com/916812579/spring-cloud/raw/master/demo3/Ribbon.png)

