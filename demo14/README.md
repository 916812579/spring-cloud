# Spring Cloud构建微服务架构：Hystrix监控面板(监控单实例)

- 参考：[http://blog.didispace.com/spring-cloud-starter-dalston-5-1/](http://blog.didispace.com/spring-cloud-starter-dalston-5-1/)


## Hystrix Dashboard(用来监控的应用)

- pom.xml配置如下依赖
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-hystrix</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-hystrix-dashboard</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

- 为应用主类加上`@EnableHystrixDashboard`，启用`Hystrix Dashboard`功能。
```java
package org.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@EnableHystrixDashboard
@SpringCloudApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```

- `bootstrap.yml`配置如下
```yml
server:
    port: 1301
spring:
    application:
        name: hystrix-dashboard
```
- 启动应用，访问http://localhost:1301/hystrix， 显示如下

![](https://github.com/916812579/spring-cloud/raw/master/demo14/hystrix-dashboard.png)


-  Dashboard共支持三种不同的监控方式，依次为：

> - 默认的`集群监控`：通过URL `http://turbine-hostname:port/turbine.stream`开启，实现对默认集群的监控。
>
> - 指定的`集群监控`：通过URL `http://turbine-hostname:port/turbine.stream?cluster=[clusterName]`开启，实现对`clusterName`集群的监控。
> 
> - 单体`应用的监控`：通过URL `http://hystrix-app:port/hystrix.stream`开启，实现对具体某个服务实例的监控。


- 对集群的监控，需要整合`Turbine`才能实现

- 监控单实例需要访问`/hystrix.stream`


## 对单实例的监控

- pom.xml 引入如下依赖
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-hystrix</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

- 确保在服务实例的主类中已经使用`@EnableCircuitBreaker`或`@EnableHystrix`注解，开启了断路器功能。

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

代码参考：[https://github.com/916812579/spring-cloud/tree/master/demo5](https://github.com/916812579/spring-cloud/tree/master/demo5)

- 启动应用

访问：http://localhost:1301/hystrix（监控应用的地址），输入http://localhost:3333/hystrix.stream（被监控的应用）
![](https://github.com/916812579/spring-cloud/raw/master/demo14/hystrix.stream.png)

点击：“Monitor Stream”按钮

显示如下：

![](https://github.com/916812579/spring-cloud/raw/master/demo14/hystrix.stream_db.png)


- 注意，查看数据前应该**访问一下断路由的方法**，不然一直显示加载数据

- `Delay`：该参数用来控制服务器上轮询监控信息的延迟时间，默认为2000毫秒，我们可以通过配置该属性来降低客户端的网络和CPU消耗。
- `Title`：该参数对应了上图头部标题Hystrix Stream之后的内容，默认会使用具体监控实例的URL，我们可以通过配置该信息来展示更合适的标题。


### 监控页面分析

参考：[http://blog.didispace.com/spring-cloud-starter-dalston-5-1/](http://blog.didispace.com/spring-cloud-starter-dalston-5-1/)
