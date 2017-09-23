# Spring Cloud构建微服务架构-断路器 （Feign）

- 参考：[http://blog.didispace.com/springcloud3/](http://blog.didispace.com/springcloud3/)

 
- 不需要在[Feigh](https://github.com/916812579/spring-cloud/tree/master/demo4)工程中引入Hystix，Feign中已经依赖了Hystrix
- `application.yml`需要增加如下配置,不然断路由不起作用
```yml
feign:
    hystrix:
        enabled: true
```
- 使用@FeignClient注解中的fallback属性指定回调类
```java
package org.demo;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "compute-service", fallback = ComputeClientHystrix.class)
public interface ComputeClient {
	@RequestMapping(method = RequestMethod.GET, value = "/add")
	Integer add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b);
}
```
- `ComputeClientHystrix`类如下，注意该类继承了`ComputeClient`

```java
package org.demo;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class ComputeClientHystrix implements ComputeClient {
	@Override
	public Integer add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b) {
		return -9999;
	}
}
```

经过上面的配置，服务已经支持断路由了
