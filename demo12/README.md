# Spring Cloud构建微服务架构-消息总线(RabbitMQ)

- 参考：[http://blog.didispace.com/springcloud7/](http://blog.didispace.com/springcloud7/)



## CentOS安装RabbitMQ

- 参考：[http://blog.csdn.net/allsharps/article/details/52062416](http://blog.csdn.net/allsharps/article/details/52062416)


- 命令
```bash
# 启动
./sbin/rabbitmq-server 

# 停止
./sbin/rabbitmqctl stop

# 启动插件， 执行一次以后不用再次执行
 ./sbin/rabbitmq-plugins enable rabbitmq_management

# 添加用户
./sbin/rabbitmqctl add_user admin 111111

# 设置权限
./rabbitmqctl set_user_tags admin administrator

```


浏览器访问地址：http://localhost:15672/


## 集成RabbitMQ

- pom.xml引入如下依赖(针对配置服务的客户端)

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

- 在配置文件中增加关于`RabbitMQ`的连接和用户信息
```yml
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
    rabbitmq:
        host: 192.168.116.10
        port: 5672
        username: admin
        password: admin
```
- 启动多个应用
- 通过`http://localhost:7002/from`查看返回值
- 修改git仓库的配置信息
- 向其中一个客户端应用程序`/bus/refresh`发送`POST`请求


## 指定刷新范围


- `/bus/refresh`接口还提供了`destination`参数，用来定位具体要刷新的应用程序。

- 刷新指定实例

```
http://localhost:7002/bus/refresh?destination=test:7002
```

- 刷新指定服务
```
http://localhost:7002/bus/refresh?destination=test:**
```


## 架构优化


- 在Config Server中也引入Spring Cloud Bus，将配置服务端也加入到消息总线中来。
- /bus/refresh请求不在发送到具体服务实例上，而是发送给Config Server，并通过destination参数来指定需要更新配置的服务或实例。

pom.xml加入如下配置
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```



刷新时报如下错误
```json
{
  "timestamp": 1506416561914,
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource.",
  "path": "/bus/refresh"
}
```
需要加入下面的配置
```yml
management:
    security:
        enabled: false
```

application.yml需要配置rabbitmq相关配置