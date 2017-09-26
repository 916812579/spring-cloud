# Spring Cloud构建微服务架构-消息总线(kafka)

- 参考：[http://blog.didispace.com/springcloud7-2/](http://blog.didispace.com/springcloud7-2/)



## 安装kafka

- 参考 [http://kafka.apache.org/quickstart](http://kafka.apache.org/quickstart)


### kafka相关命令

```bash
# 启动zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# 启动kafka
bin/kafka-server-start.sh config/server.properties
```


## 配置kafka

- pom.xml引入
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-bus-kafka</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
- `bootstrap.yml`相关配置
```yml
server:
    port: 7005
spring:
    application:
        name: test

# git管理配置
    cloud:
        config:
            profile: dev
            discovery: 
                enabled: true
                serviceId: config-server
        #kafka 相关配置            
        stream:
           kafka:
               binder:
                   zk-nodes: 192.168.116.10:2181
                   brokers: 192.168.116.10:9092            
eureka:
    client:
        serviceUrl:
            defaultZone: http://localhost:1111/eureka/  
            
management:
    security:
        enabled: false
```

 