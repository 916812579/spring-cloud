# 该配置库主要是针对自己对spring cloud进行学习的整理
- 参考： [http://blog.didispace.com/categories/Spring-Cloud/page/5/](http://blog.didispace.com/categories/Spring-Cloud/page/5/)



`Spring Cloud`是一个基于`Spring Boot`实现的云应用开发工具，它为基于`JVM`的云应用开发中的`配置管理`、`服务发现`、`断路器`、`智能路由`、`微代理`、`控制总线`、`全局锁`、`决策竞选`、`分布式会话`和`集群状态管理`等操作提供了一种简单的开发方式。

`Spring Cloud`包含了多个子项目（针对分布式系统中涉及的多个不同开源产品），比如：`Spring Cloud Config`、`Spring Cloud Netflix`、`Spring Cloud CloudFoundry`、`Spring Cloud AWS`、`Spring Cloud Security`、`Spring Cloud Commons`、`Spring Cloud Zookeeper`、`Spring Cloud CLI`等项目。

简单的说，微服务架构就是**将一个完整的应用从数据存储开始`垂直拆分`成多个不同的服务，每个服务都能`独立部署`、`独立维护`、`独立扩展`，服务与服务间通过诸如RESTful API的方式互相调用**。


通过[http://start.spring.io/](http://start.spring.io/)创建项目

![创建项目](https://github.com/916812579/spring-cloud/raw/master/project_new.png)



## 目录

[服务注册与发现（服务注册中心)](https://github.com/916812579/spring-cloud/tree/master/demo1)

[服务注册与发现（创建“服务提供方”）](https://github.com/916812579/spring-cloud/tree/master/demo2)

[Spring Cloud构建微服务架构服务消费者 （Ribbon）](https://github.com/916812579/spring-cloud/tree/master/demo3)

[Spring Cloud构建微服务架构服务消费者 （Feign）](https://github.com/916812579/spring-cloud/tree/master/demo4)

[Spring Cloud构建微服务架构-断路器(Ribbon)](https://github.com/916812579/spring-cloud/tree/master/demo5)

[Spring Cloud构建微服务架构-断路器 （Feign）](https://github.com/916812579/spring-cloud/tree/master/demo6)

[Spring Cloud构建微服务架构 - 分布式配置中心](https://github.com/916812579/spring-cloud/tree/master/demo7)

[Spring Cloud构建微服务架构 - 分布式配置中心的使用](https://github.com/916812579/spring-cloud/tree/master/demo8)

[Spring Cloud构建微服务架构 - 服务网关](https://github.com/916812579/spring-cloud/tree/master/demo9)

[Spring Cloud构建微服务架构-分布式配置中心（高可用）](https://github.com/916812579/spring-cloud/tree/master/demo10)

[Spring Cloud构建微服务架构 - 分布式配置中心(高可用的使用)](https://github.com/916812579/spring-cloud/tree/master/demo11)

[Spring Cloud构建微服务架构-消息总线(RabbitMQ)](https://github.com/916812579/spring-cloud/tree/master/demo12)

[Spring Cloud构建微服务架构-消息总线(kafka)](https://github.com/916812579/spring-cloud/tree/master/demo13)


## 注册中心高可用解决方案

- 参考： [http://blog.didispace.com/springcloud6/](http://blog.didispace.com/springcloud6/)

`Eureka Server`除了单点运行之外，还可以通过运行多个实例，并`进行互相注册`的方式来实现高可用的部署，所以我们只需要将Eureke Server配置其他可用的serviceUrl就能实现高可用部署。


## Spring Cloud版本

- 参考： [http://blog.didispace.com/springcloud-version/](http://blog.didispace.com/springcloud-version/)

### 版本命名
之前提到过，Spring Cloud是一个拥有诸多子项目的大型综合项目，原则上其子项目也都维护着自己的发布版本号。那么每一个Spring Cloud的版本都会包含不同的子项目版本，为了要管理每个版本的子项目清单，避免版本名与子项目的发布号混淆，所以没有采用版本号的方式，而是通过命名的方式。

这些版本名字采用了伦敦地铁站的名字，根据字母表的顺序来对应版本时间顺序，比如：最早的Release版本：Angel，第二个Release版本：Brixton，以此类推……

### 版本号

经过上面的解释，不难猜出，之前所提到的Angel.SR6，Brixton.SR5中的SR6、SR5就是版本号了。

当一个版本的Spring Cloud项目的发布内容积累到临界点或者一个严重bug解决可用后，就会发布一个“service releases”版本，简称SRX版本，其中X是一个递增数字。





