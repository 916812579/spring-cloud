# Spring Cloud构建微服务架构 - 分布式配置中心

- 参考：[http://blog.didispace.com/springcloud4/](http://blog.didispace.com/springcloud4/)

 
`Spring Cloud Config`为服务端和客户端提供了分布式系统的外部化配置支持。配置服务器为各应用的所有环境提供了一个中心化的外部配置。它实现了对服务端和客户端对`Spring Environment`和`PropertySource`抽象的映射，所以它除了适用于Spring构建的应用程序，也可以在任何其他语言运行的应用程序中使用。作为一个应用可以通过部署管道来进行测试或者投入生产，我们可以分别为这些环境创建配置，并且在需要迁移环境的时候获取对应环境的配置来运行。

配置服务器默认采用`git`来存储配置信息，这样就有助于对环境配置进行版本管理，并且可以通过`git`客户端工具来方便的管理和访问配置内容。当然他也提供本地化文件系统的存储方式，下面从这两方面介绍如何使用分布式配置来存储微服务应用多环境的配置内容。

## 构建Config Server

- pom.xml中引入`spring-cloud-config-server`依赖，完整依赖配置如下：

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>spring-cloud</groupId>
	<artifactId>demo1</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.5.7.RELEASE</version>
		<relativePath /> <!-- lookup parent from repository -->
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
		<spring-cloud.version>Dalston.SR3</spring-cloud.version>
	</properties>

	<dependencies>

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-config-server</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>${spring-cloud.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>
```

- 创建Spring Boot的程序主类，并添加`@EnableConfigServer`注解，开启Config Server
```java
package org.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```

- `application.yml`中配置服务信息以及`git`信息，例如：
```yml
server:
    port: 7001
spring:
    application:
        name: config-server

# git管理配置
spring:
    cloud:
        config:
            server:
                git:
                    uri: https://github.com/916812579/spring-cloud
                    searchPaths: config/service-config
                    username: 916812579
                    password: ******
```

- `spring.cloud.config.server.git.uri`：配置git仓库位置
- `spring.cloud.config.server.git.searchPaths`：配置仓库路径下的相对搜索位置，可以配置多个
- `spring.cloud.config.server.git.username`：访问git仓库的用户名
- `spring.cloud.config.server.git.password`：访问git仓库的用户密码


`Spring Cloud Config`也提供`本地存储配置`的方式。我们只需要设置属性`spring.profiles.active=native`，`Config Server`会默认从应用的`src/main/resource`目录下检索配置文件。也可以通过`spring.cloud.config.server.native.searchLocations=file:F:/properties/`属性来指定配置文件的位置。虽然`Spring Cloud Config`提供了这样的功能，但是为了支持更好的管理内容和版本控制的功能，还是推荐使用`git`的方式。


## 配置服务的测试

git仓库配置文件下创建四个文件如下：
- test.yml
- test-dev.yml
- test-prod.yml
- test-test.yml

内容分别如下：
- from: default
- from: dev
- from: prod
- from: test


URL与配置文件的映射关系如下：

- /{application}/{profile}[/{label}]
- /{application}-{profile}.yml
- /{label}/{application}-{profile}.yml
- /{application}-{profile}.properties
- /{label}/{application}-{profile}.properties

上面的`url`会映射`{application}-{profile}.properties`对应的配置文件，`{label}`对应`git`上不同的`分支`，默认为`master`。

- 可以通过url访问配置文件

```
http://localhost:7001/filename/active/branch
```

- filename 文件名(-前面的部分)
- active 文件名(-后面的部分)
- branch git分支

例如：
```bash
http://localhost:7001/test/dev
http://localhost:7001/test/dev/master
```
