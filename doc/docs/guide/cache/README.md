# 系统缓存扩展
系统缓存默认使用的是基于Caffeine的本地缓存，除此之外，还提供了基于LocalCache和基于Redis的缓存实现，可根据不同的需求场景使用

特性：
1. 多种缓存实现方式
2. 支持不同的数据存到不同的缓存当中
3. 支持按照namespcae命令空间存取数据

## 基于Caffeine的本地缓存扩展
::: tip 适用场景
缓存的数据量较小，但性能要求高
:::
Caffeine为springboot官方推荐使用的本地缓存组件，相对于LocalCache，性能更加优秀，故框架本身已默认引入了Caffeine的缓存扩展，提供的CacheUtil便是基于Caffeine存取数据
```xml
<dependency>
    <groupId>com.ds.screw</groupId>
    <artifactId>screw-cache-caffeine</artifactId>
    <version>1.0</version>
</dependency>
```

## 基于Redis的服务器缓存扩展
::: tip 适用场景
缓存数据量大、有持久化需求、性能要求较高
:::
### 步骤一、依赖引入
框架本身没有默认引入该扩展，需要手动引入
```xml
<dependency>
    <groupId>com.ds.screw</groupId>
    <artifactId>screw-cache-redis</artifactId>
    <version>1.0</version>
</dependency>
```
### 步骤二、配置Redis

#### 单机环境配置
```properties
# Redis数据库索引（默认为0）
spring.redis.database=0
# Redis服务器连接超时时间（毫秒）
spring.redis.timeout=60000ms
spring.redis.client-type=lettuce
# redis单机配置
spring.redis.host=127.0.0.1
spring.redis.port=3379
spring.redis.username=
spring.redis.password=
#连接池最大连接（使用负值表示没有限制）默认-1
spring.redis.lettuce.pool.max-active=200
#连接池最大阻塞等待时间（使用负值表示没有限制）默认-1
spring.redis.lettuce.pool.max-wait=-1
#连接池中的最大空闲连接 默认8
spring.redis.lettuce.pool.max-idle=10
#连接池中的最小空闲时间 默认0
spring.redis.lettuce.pool.min-idle=0
```

#### 集群环境配置
```properties
#集群配置
spring.redis.cluster.nodes=127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381
## 连接超时时间（毫秒）
spring.redis.timeout=60000
## Redis数据库索引(默认为0)
spring.redis.database=0
#连接池的最大连接数，使用负数没有限制
spring.redis.jedis.pool.max-active=100
#连接池的最大空闲连接
spring.redis.jedis.pool.max-idle=10
#连接池的最大阻塞等待时间
spring.redis.jedis.pool.max-wait=100000
# 连接池中的最小空闲连接
spring.redis.jedis.pool.min-idle=0
# 连接超时时间（毫秒）
spring.redis.timeout=10000
```
## 按照namespcae命令空间存取数据
::: tip 引入namespcae命名空间的意义
当我们需要清空缓存刷新数据时，最简单粗暴的方式是将缓存中的数据全部清空掉，但这种方式弊端十分明显，有些不需要刷新的数据也会被迫刷新，当然我们也可以通过删除指定的key进行缓存的刷新，但这种方式效率并不高
因此我们引入了namespcae命令空间的概念，由此可以针对某一业务域进行刷新

典型的例子是：
* 用户权限数据我们存储到namespcae="user"的命名空间中
* 验证码、token、密钥等基础信息我们放到namespcae="basic"的命名空间中

这样我们在分配用户权限时，只需要刷新namespcae="user"的命名空间而不会影响到其它的缓存数据

:::

框架提供的CacheUtil缓存工具类，默认使用了Caffeine进行存取，命名空间统一为"default"，当你需要将数据存在不同的命名空间时，可通过以下的方式实现
```java
CacheHolder.getCache("namespcae1",CaffeineCacheManager.class).put("caffeine","caffeine123");
Object namespcae1Value = CacheHolder.getCache("default", CaffeineCacheManager.class).get("caffeine");

CacheHolder.getCache("namespcae2",CaffeineCacheManager.class).put("caffeine","caffeine123");
Object namespcae2Value = CacheHolder.getCache("default", CaffeineCacheManager.class).get("caffeine");
```
::: tip 方法说明
```java
/**
* 获取具体的缓存实现
* 
* @param name 命名空间
* @param clazz 缓存实现类，可选值有 CaffeineCacheManager.class、RedisCacheManager.class
* @return
*/
public static CacheAdapter getCache(String name, Class<? extends CacheManager> clazz)
```
:::

## 按照不同的缓存拓展存取数据
当同时引入`screw-cache-caffeine`和`screw-cache-redis`时，可按照需求将一部分数据存在本地内存中，一部分数据存在redis中

代码示例：
```java
CacheHolder.getCache("default",CaffeineCacheManager.class).put("caffeine","caffeine123");
Object caffeineValue = CacheHolder.getCache("default", CaffeineCacheManager.class).get("caffeine");

CacheHolder.getCache("default", RedisCacheManager.class).put("redis","redis123");
Object redisValue = CacheHolder.getCache("default", RedisCacheManager.class).get("redis");
```

