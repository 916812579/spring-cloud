# Spring Cloud构建微服务架构-断路器(Ribbon)

- 参考：[http://blog.didispace.com/springcloud3/](http://blog.didispace.com/springcloud3/)


## 断路器

“断路器”本身是一种开关装置，用于在电路上保护线路过载，当线路中有电器发生短路时，“断路器”能够及时的切断故障电路，防止发生过载、发热、甚至起火等严重后果。

在分布式架构中，断路器模式的作用也是类似的，当某个服务单元发生故障（类似用电器发生短路）之后，通过断路器的故障监控（类似熔断保险丝），向调用方返回一个错误响应，而不是长时间的等待。这样就不会使得线程因调用故障服务被长时间占用不释放，避免了故障在分布式系统中的蔓延。

## Netflix Hystrix

在Spring Cloud中使用了`Hystrix` 来实现断路器的功能。Hystrix是Netflix开源的微服务框架套件之一，该框架目标在于通过控制那些访问远程系统、服务和第三方库的节点，从而对延迟和故障提供更强大的容错能力。Hystrix具备拥有回退机制和断路器功能的线程和信号隔离，请求缓存和请求打包，以及监控和配置等功能。


## [Ribbon](https://github.com/916812579/spring-cloud/tree/master/demo3)中引入Hystrix

- `pom.xml`中引入依赖`hystrix`依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-hystrix</artifactId>
</dependency>
```

- 应用程序主类使用`@EnableCircuitBreaker`注解开启断路器功能

```java
package org.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
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

- 改造服务消费方式，新增`ComputeService`类，在使用`ribbon`消费服务的函数上增加`@HystrixCommand`注解来指定回调方法。

```java
package org.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class ComputeService {
	@Autowired
	RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "addServiceFallback")
	public String addService() {
		return restTemplate.getForEntity("http://COMPUTE-SERVICE/add?a=10&b=20", String.class).getBody();
	}

	public String addServiceFallback() {
		return "error";
	}
}
```
- 提供`rest`接口的`Controller`改为调用`ComputeServic`e的`addService`
```java
package org.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {
	
	@Autowired
	private ComputeService computeService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() {
		return computeService.addService();
	}
}
```
- 验证断路器的回调

> - 依次启动eureka-server、compute-service、eureka-ribbon工程
> - 访问http://localhost:1111/可以看到注册中心的状态
> - 访问http://localhost:3333/add，页面显示：30
> - 关闭compute-service服务后再访问http://localhost:3333/add，页面显示：error

总结：
- 主类添加`@EnableCircuitBreaker`注解开启断路由功能
- 使用`@HystrixCommand`标注具有断路由的方法