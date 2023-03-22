# 内存队列 PartitionQueue

### 功能描述

分区队列，抽取数据分区标识（key）将数据发往不同的队列，消费线程确保仅消费指定分区，确保数据的有序性，同时达到多线程消费分散压力的目的。

### 结构说明

```shell
// Partition extends LinkedBlockingQueue
private final Map<String, Partition<K, V>> sets = new ConcurrentHashMap<>();
```

其结构类似于Kafka中的Topic，其对象实例即为一个Topic。内部包含多个分区实例，每个分区为一个界阻塞队列。

### 执行逻辑

系统启动时根据配置的分区数量为指定Topic创建若干分区。

#### 生产数据

* 向队列发送数据时，自动根据传入的数据标识，计算分区号（hashCode取余），取得对应的分区队列后置入队列。

#### 消费数据

根据消费线程数量配置为每个分区创建一个或多个消费线程（分区均分），不间断地从分区队列中拉取数据消费。

- 注意：在需要严格保证数据消费有序性的场景下，消费线程数量应小于等于分区数量，确保一个分区不会被多个线程消费。

# 内存异步数据队列 ActionQueue

### 功能描述

为执行过程中需要进行模块解耦、异步操作、聚合批量操作的数据提供队列支持。通过为指定方法添加`@ActionQueueListener`注解实现消费。

### 结构说明

其结构类似PartitionQueue

```shell
private final Map<String, BlockingQueue<ActionQueueRecord<T>>> sets = new ConcurrentHashMap<>();
```

### 消费者注解 ActionQueueListener

该注解需添加在方法上，由Spring容器加载，根据配置自动创建对应的队列容器`ActionQueue`，其包含如下参数：

- name: String; 名称，消费者名称，生产者根据该名称指定消费者消费。
- zoneSize: Integer; 分区数量。
- pollSize: Integer; 批量执行数量，当batch值大于1时，队列中的数据仅当达到一定的数量时才执行消费。
- maxWait: Long; 队列拉取超时时长，当长时间无法获取数据时，执行一次本地批量消费。
- threadSize: Integer; 消费线程数量。
- isDelay: Boolean; 是否延时消费数据。

实现逻辑：将注解方法注册为消费者执行单元，内部根据参数配置，实现多种数据监听对象，从`ActionQueue`中拉取数据。

- pollSize: DataBatchListener、DataSingleListener; 聚合消费。
- isDelay：DataDelayedListener; 延时消费。 当监听对象满足消费条件时，回调消费者执行单元，记当前注解方法。

```shell
/**
 * 队列方式批量更新
 *
 * @param list 批量更新数据
 * pollSize=50 50条更新一次
 * maxWait=60000 60秒超时也执行一次消费
 */
@ActionQueueListener(name = "queueName", pollSize = 50, maxWait = 60000)
public void onBatchUpdateMaNoticeCount(List<Map<String, Object>> list) {
  // todo
}
```

### 生产者

生产者则需要指定`消费者名称`生产数局，将数据发往ActionQueue内容队列，当然，ActionQueues实现

```shell
MonitorAction<Map<String, Object>> action = new MonitorAction<>();
// 此处 ma_notice_count表中 machine_id+index_id+sub_code锁定一行数据，因此以此作为key
action.setKey(info.getMachineId() + "_" + info.getIndexId() + "_" + info.getSubCode());
action.setName("batchUpdateMaNoticeCount");
action.setParam(params);
monitorActionService.buildQueueAction(action);
```