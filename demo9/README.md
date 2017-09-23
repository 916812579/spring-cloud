# Spring Cloud构建微服务架构 - 服务网关

- 参考：[http://blog.didispace.com/springcloud5/](http://blog.didispace.com/springcloud5/)


 Spring Cloud Netflix中的`Zuul`担任了服务网关的功能


## 准备工作


在使用`Zuul`之前，我们先构建一个服务注册中心、以及两个简单的服务，比如：我构建了一个`service-A`，一个`service-B`。然后启动`eureka-server`和这两个服务。通过访问`eureka-server`，我们可以看到`service-A`和`service-B`已经注册到了服务中心。

## 开始使用Zuul

- pom.xml中引入依赖, 如果不是通过指定`serviceId`的方式，`eureka`依赖不需要，但是为了对服务集群细节的透明性，还是用`serviceId`来避免直接引用`url`的方式吧。
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zuul</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
```
- 应用主类使用`@EnableZuulProxy`注解开启`Zuul`
```java
package org.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringCloudApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```
> - `@SpringCloudApplication`注解整合了`@SpringBootApplication`、`@EnableDiscoveryClient`、`@EnableCircuitBreaker`

- `application.yml`中配置`Zuul`应用的基础信息，如：`应用名`、`服务端口`等。


## Zuul配置

### 服务路由

> 两种映射方式: `通过url直接映射`  `通过serviceId映射`

- `application.yml`通过url直接映射

```yml
zuul:
    routes:
        api-a-url:
            path: /api-a-url/**
            url: http://localhost:2222/
```

该配置定义的`Zuul`规则为：`/api-a-url/**`的访问都映射到`http://localhost:2222/`上。

配置属性`zuul.routes.api-a-url.path`中的`api-a-url`部分为路由的名字，`可以任意定义`，但是一组映射关系的path和url要相同。

例如通过如下路径访问：
```
http://localhost:5555/api-a-url/add?a=19&b=20
```

- `application.yml`通过`serviceId`映射

```yml
zuul:
    routes:
        api-a:
            path: /api-a/**
            serviceId: service-A
        api-b:
            path: /api-b/**
            serviceId: service-B
eureka:
    client:
        serviceUrl:
            defaultZone:
                http://localhost:1111/eureka/
```


- `application.yml`配置文件还需要添加`eureka`的地址
```yml
eureka:
    client:
        serviceUrl:
            defaultZone:
                http://localhost:1111/eureka/
```

### 服务过滤

- 过滤器需要继承`ZuulFilter`

- `filterType`：返回一个字符串代表过滤器的类型，在`zuul`中定义了四种不同生命周期的过滤器类型，具体如下：
> - `pre`：可以在请求被路由之前调用
> - `routing`：在路由请求时候被调用
> - `post`：在`routing`和`error`过滤器之后被调用
> - `erro`r：处理请求时发生错误时被调用

- `filterOrder`：通过`int`值来定义过滤器的执行顺序
- `shouldFilter`：返回一个`boolean`类型来判断该过滤器是否要执行，所以通过此函数可实现过滤器的开关。在上例中，我们直接返回true，所以该过滤器总是生效。
- `run`：过滤器的具体逻辑。需要注意，这里我们通过`ctx.setSendZuulResponse(false)`令zuul过滤该请求，不对其进行路由，然后通过- - `ctx.setResponseStatusCode(401)`设置了其返回的错误码，当然我们也可以进一步优化我们的返回，比如，通过`ctx.setResponseBody(body)`对返回`body`内容进行编辑等。



- 实例化该过滤器

```java
package org.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@SpringCloudApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public AccessFilter accessFilter() {
		return new AccessFilter();
	}

}
```

- 启动

测试
```
http://localhost:5555/api-a-url/add?a=19&b=20&accessToken=11
http://localhost:5555/api-a-url/add?a=19&b=20
```

服务网关作用：

- 不仅仅实现了路由功能来屏蔽诸多服务细节，更实现了服务级别、均衡负载的路由。
- 
- 实现了接口权限校验与微服务业务逻辑的解耦。通过服务网关中的过滤器，在各生命周期中去校验请求的内容，将原本在对外服务层做的校验前移，保证了微服务的无状态性，同时降低了微服务的测试难度，让服务本身更集中关注业务逻辑的处理。

- 实现了断路器，不会因为具体微服务的故障而导致服务网关的阻塞，依然可以对外服务。