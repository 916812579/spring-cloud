# Spring Cloud构建微服务架构 - 分布式配置中心的使用

- 参考：[http://blog.didispace.com/springcloud4/](http://blog.didispace.com/springcloud4/)


- pom.xml引入依赖

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```
- 主类还是普通的主类，如下
```java
package org.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```

- 通过`bootstrap.properties`配置，来指定`config server`
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
            label: master
            uri: http://localhost:7001/
```

**上面这些属性必须配置在`bootstrap.properties`中，config部分内容才能被正确加载。因为config的相关配置会先于`application.properties`，而`bootstrap.properties`的加载也是先于`application.properties`**。

注意：应用的名称需要和配置文件名`-`前面的名称一致，不然会获取不到配置文件的内容。

- 创建一个Rest Api来返回配置中心的from属性，具体如下：

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

访问路径:localhost:7002/from