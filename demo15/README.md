# Spring Cloud构建微服务架构：Hystrix监控数据聚合(对集群的监控 Turbine)

- 参考：[http://blog.didispace.com/spring-cloud-starter-dalston-5-2/](http://blog.didispace.com/spring-cloud-starter-dalston-5-2/)


## 两种监控方式

### 通过HTTP收集聚合

- pom.xml中引入如下依赖
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-turbine</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

- 创建应用主类`Application`，并使用`@EnableTurbine`注解开启`Turbine`。
```java
package org.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableTurbine
@EnableDiscoveryClient
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```

- `bootstrap.yml`配置如下
```yml
spring:
  application.name: service-turbine
server:
  port: 8769
security.basic.enabled: false
turbine:
  aggregator:
    clusterConfig: default   # 指定聚合哪些集群，多个使用","分割，默认为default。可使用http://.../turbine.stream?cluster={clusterConfig之一}访问
  appConfig: ribbon-consumer ### 配置Eureka中的serviceId列表，表明监控哪些服务
  clusterNameExpression: new String("default")
  # 1. clusterNameExpression指定集群名称，默认表达式appName；此时：turbine.aggregator.clusterConfig需要配置想要监控的应用名称
  # 2. 当clusterNameExpression: default时，turbine.aggregator.clusterConfig可以不写，因为默认就是default
  # 3. 当clusterNameExpression: metadata['cluster']时，假设想要监控的应用配置了eureka.instance.metadata-map.cluster: ABC，则需要配置，同时turbine.aggregator.clusterConfig: ABC
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:1111/eureka/
```

-  启动

eureka-server ： 参看代码：[https://github.com/916812579/spring-cloud/tree/master/demo1](https://github.com/916812579/spring-cloud/tree/master/demo1)

eureka-consumer-ribbon-hystrix : 参看代码 [https://github.com/916812579/spring-cloud/tree/master/demo5](https://github.com/916812579/spring-cloud/tree/master/demo5)

turbine : 参看代码 [https://github.com/916812579/spring-cloud/tree/master/demo15](https://github.com/916812579/spring-cloud/tree/master/demo15)

hystrix-dashboard : 参看代码 [https://github.com/916812579/spring-cloud/tree/master/demo14](https://github.com/916812579/spring-cloud/tree/master/demo14)



- 访问 http://localhost:1301/hystrix，输入http://localhost:8769/turbine.stream进行查看

![](https://github.com/916812579/spring-cloud/raw/master/demo15/hystrix.stream.png)

显示效果如下

![](https://github.com/916812579/spring-cloud/raw/master/demo15/rs.png)


- 注意访问前需要访问一下eureka-consumer-ribbon-hystrix，不然一直处于采集数据中


###  通过消息代理收集聚合

参看：[https://github.com/916812579/spring-cloud/tree/master/demo16](https://github.com/916812579/spring-cloud/tree/master/demo16)