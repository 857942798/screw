# 消息队列
## 设计思路
screw 提供了一个消息队列工具，可实现快速切换消息队列中间件实现，并且默认提供了本地消息队列实现

试想这样一个场景，初期业务系统数据量较小，于是先是在本地实现了一个消息队列进行消息的处理，但业务系统存在扩张的可能性，后期可能需要引入kafka、RabbitMq等消息队列中间件时，
原本向本地消息队列发送的消息这部分代码都需要进行变更，甚至极端情况下，业务系统为适应不同的业务场景需要采用不同的消息队列实现时，其发送消息和接受消息的代码又需要改变。

screw 提供的消息队列工具就是解决这样的场景出现的，统一发送消息和接受消息的方式，即使换了消息队列中间件的实现，只需要引入相应的插件即可，原本的代码几乎不用改变

发送消息和订阅消息十分简单。首先你需要引入screw的消息队列组件：
```xml
<!-- 消息队列工具 -->
<dependency>
    <groupId>com.ds.screw</groupId>
    <artifactId>screw-queue</artifactId>
    <version>${screw.version}</version>
</dependency>
```

## 发送消息
示例代码：
```java
// 发送消息到指定主题中
Map<String, Object> params=new HashMap<>();
params.put("msg", "消息发送时间："+ DateUtil.now());
QueueUtils.sendMessage("testTopic", "testKey", params);

// 发送一条延迟处理的消息，1分钟后再处理，单位：秒
QueueUtils.sendMessage("delayTopic", "delayKey", params, 60L);
```
* topic : 消息的主题topic名称
* key : 消息的唯一key
* body : 消息内容
* delayTime : 消息延迟时间，需要将`@ActionQueueListener`注解的`isDelay`属性设置为true才会生效

## 订阅消息
示例代码：
```java
@ActionQueueListener(name = "testTopic", zoneSize = 1, threadSize = 1, pollSize = 50, maxWait = 30000, isDelay=false)
public void consumer(List<Map<String,Object>> messages) {
    for (Map<String, Object> message : messages) {
        // TODO 消息处理
    }
}
```
* name: 订阅的主题topic名称
* zoneSize : topic的分区数量，同一个key会发送到相同的分区中
* threadSize : 处理的线程数
* pollSize : 消息批量处理长度，如设置50，表示消息积攒到50后才会将消息批量发送到消费者进行处理
* maxWait : 最长等待时间，即使消息量没有达到pollSize的指定数量，超过一定时间，还是会将消息批量发送到消费者进行处理
* isDelay : 是否设置为延迟处理队列，当为true时，底层队列将改为`DelayQueue`实现

## 分区及扩容
* 分区机制

这里参考了`kafka`的分区理念，一个topic可分为多个分区，默认一个分区会被一个线程所消费，且相同的key会根据hash路由发送同一个分区中

* 扩容机制

当队列大小超过限制时，为了防止无限制扩容，当jvm内存使用率小于80%时才会对消费线程进行自动扩容

