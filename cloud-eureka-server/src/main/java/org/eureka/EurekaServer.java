package org.eureka;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

// 参考：http://www.cnblogs.com/skyblog/p/5127690.html
// @SpringBootApplication相当于@Configuration、@EnableAutoConfiguration和@ComponentScan，也可以同时使用这3个注解。其中@Configuration、@ComponentScan是spring框架的语法，在spring 3.x就有了，用于代码方式创建配置信息和扫描包。@EnableAutoConfiguration是spring boot语法，表示将使用自动配置。你如果下载了spring boot源码，就会看到spring boot实现了很多starter应用，这些starter就是一些配置信息（有点类似于docker，一组环境一种应用的概念），spring boot看到引入的starter包，就可以计算如何自动配置你的应用。
@SpringBootApplication
@EnableEurekaServer
public class EurekaServer {
	public static void main(String[] args) {
		// SpringApplication.run(EurekaServer.class, args);
		new SpringApplicationBuilder(EurekaServer.class).web(true).run(args);
		// String errorString = "emergency! eureka may be incorrectly claiming instances
		// are up when they're not. renewals are lesser than threshold and hence the
		// instances are not being expired just to be safe.";
		// errorString = "the self preservation mode is turned off.this may not protect
		// instance expiry in case of network/other problems.";
	}
}
