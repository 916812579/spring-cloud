# Spring Cloud构建微服务架构服务消费者 （Feign）

- 参考：[http://blog.didispace.com/springcloud2/](http://blog.didispace.com/springcloud2/)

## Feign

`Feign`是一个声明式的`Web Service`客户端，它使得编写`Web Serivce`客户端变得更加简单。我们只需要使用`Feign`来创建一个接口并用注解来配置它既可完成。它具备可插拔的注解支持，包括Feign注解和JAX-RS注解。Feign也支持可插拔的编码器和解码器。Spring Cloud为Feign增加了对Spring MVC注解的支持，还整合了Ribbon和Eureka来提供均衡负载的HTTP客户端实现。


## 配置依赖

- pom.xml

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-feign</artifactId>
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

- 在应用主类中通过`@EnableFeignClients`注解开启`Feign`功能
```java
package org.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
```

- 定义针对`compute-service`服务的接口
```java
package org.demo;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("compute-service")
public interface ComputeClient {
	@RequestMapping(method = RequestMethod.GET, value = "/add")
	Integer add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b);
}
```
> `@FeignClient("compute-service")`指定了服务的名称

- 在web层中调用定义的`ComputeClient`

```java
package org.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {
    @Autowired
    ComputeClient computeClient;
    
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public int add() {
        return computeClient.add(10, 20);
    }
}
```

- `application.yml`中的配置保持不变

- 启动应用

访问：http://localhost:3333/add两次，可以观察服务端控制台的变化


总结：
- `@EnableFeignClients`注解开启`Feign`功能
- `@FeignClient`指定消费的服务以及接口
- `@Autowired`注入，然后就可以直接使用了
 