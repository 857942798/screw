# 介绍
## Screw是什么？
Screw是一个框架组件集，主要提供了Token校验、权限管理、WAPI标准流程服务、统一认证、消息队列、缓存等项目中常需要引入的组件功能。

Screw目的是为了以简单、优雅的方式来打造一个易用的、可拓展的JAVA后端项目。

Screw对外大多是以工具类的方式提供组件能力，以登录认证为例，只需要一行代码便可完成登录：
```java
// 会话登录：参数填写要登录的账号id, 该id可以是任何的唯一标识
AuthUtil.login(String loginId);
``` 
你无需编写大量的代码管理登录状态，Screw已经在背后做了大量的工作。

对外的大多数功能都只需一行代码便可实现，充分体现了API设计的简单与优雅！

## Screw功能一览
Screw 目前提供了五个组件：会话管理、WAPI标准流程服务、缓存、消息队列、License认证授权。提供了以下部分能力：

* 登录认证 —— 单端登录、多端登录、同端互斥登录、七天内免登录
* 权限认证 —— 权限认证、角色认证
* Session会话 —— 全端共享Session、单端独享Session、自定义Session
* 踢人下线 —— 根据账号id踢人下线、根据Token值踢人下线
* 账号封禁 —— 根据账号进行封禁
* 持久层扩展 —— 可集成Redis、Memcached等专业缓存中间件，重启数据不丢失
* 单点登录 —— 提供类CAS的单点登录解决方案，可与已使用CAS单点登录方案的系统无缝集成
* 临时Token认证 —— 解决短时间的Token授权问题
* 注解式鉴权 —— 优雅的将鉴权与业务代码分离
* 路由拦截式鉴权 —— 根据路由拦截鉴权，可适配Restful模式
* 全局侦听器 —— 在用户登陆、注销、被踢下线等关键性操作时进行一些AOP操作
* 本地消息队列 —— 延迟消息、消息分区、自动扩容、批量消息处理
* 消息中间件切换 —— 不想将数据放入本地消息队列时，可无缝切换至kafka、RabbitMq等缓存中间件中
* 本地缓存组件 —— 默认使用了Caffeine本地缓存，还提供了按指定命名空间的方式进行存储的方式，实现了业务数据隔离
* 缓存中间件切换 —— 不想将数据保存在本地缓存时，可无缝切换至Redis等缓存中间件中
* License授权 —— 基于License文件对java类软件实现授权控制，保护好自己的软件产品


## 技术栈

| 第三方组件                | 版本               | 官方地址                                                                  | 说明                                     |
|----------------------| ------------------ |-----------------------------------------------------------------------| ---------------------------------------- |
| Java(jdk/jre)        | 8u191,8u202        | [Java](https://www.oracle.com/cn/java/technologies/)                  | JAVA 编译及运行环境                      |
| Spring-boot          | 2.7.3              | [Spring-boot](https://spring.io/projects/spring-boot)                 | 简化新*Spring*应用的初始搭建以及开发过程 |
| Spring-framework     | 5.3.22             | [Spring-framework ](https://spring.io/projects/spring-framework)      | spring-framework全家桶                   |
| MyBatis              | 3.5.9              | [MyBatis](https://github.com/mybatis/mybatis-3)                       | 数据层管理工具                           |
| Atomikos             | 4.0.6              | [Atomikos](https://www.atomikos.com/Main/WebHome)                     | 分布式事务管理工具                       |
| Log4j2               | 2.17.2             | [Log4j2](https://logging.apache.org/log4j/2.x/index.html)             | 开源日志框架                             |
| Fastjson             | 1.2.83             | [Fastjson](https://github.com/alibaba/fastjson)                       | 开源的json处理器                         |
| FasterXML/Jackson    | 2.13.3             | [Jackson](https://github.com/FasterXML/jackson)                       | 开源的json处理器                         |
| Caffeine             | 2.9.3              | [Caffeine](https://github.com/ben-manes/caffeine/wiki)                | 基于内存的缓存组件                       |
| Hutool               | 5.8.3              | [Hutool](https://hutool.cn/)                                          | 国产工具集合                             |
| Mysql-connector-java | 5.1.34             | [Mysql-connector-java](https://dev.mysql.com/doc/connector-j/8.0/en/) | MySQL数据库驱动-JAVA                     |
| Tomcat               | 9.0.60,9.0.67      | [Tomcat](https://tomcat.apache.org/)                                  | Servlet运行容器                          |
| Redis                | 2.7.2              | [Redis](https://redis.io/)                                            | Redis缓存数据库                          |
| MySQL                | 5.6.* 、5.7.*、8.0 | [MySQL](https://www.mysql.com/)                                       | MySQL数据库                              |






