package com.ds.screw.queue.action;

import com.ds.screw.queue.topic.TopicQueueConsumer;
import com.ds.screw.queue.topic.TopicQueueManager;
import com.ds.screw.queue.topic.TopicQueueMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 本地消息队列实现
 *
 * @author dongsheng
 */
class ActionQueue<T> {

    private final Logger log = LoggerFactory.getLogger(ActionQueue.class);

    private final static String ZONE_PREFIX = "zone_";
    // 设置队列处理名字
    private final String name;
    // 设置队列处理长度
    private final int pollSize;
    // 设置是否使用延时队列处理
    private final boolean isDelay;
    // 设置队列处理时间
    private final long maxWait;
    // 设置处理线程数量
    private final int threadSize;
    // 设置分区zone数量
    private final int zoneSize;
    // 分区队列
    private final Map<String, DataZone<ActionQueueMessage<T>>> sets = new ConcurrentHashMap<>();
    // 消费者
    private final Map<String, ActionQueueConsumer<T>> maps = new ConcurrentHashMap<>();
    // 回调接口
    private final ActionQueueProcessor<T> processor;

    /**
     * 可以设置队列的处理时间和处理长度
     *
     * @param name       处理队列名称
     * @param pollSize   一次拉取数量
     * @param threadSize 处理线程数量
     * @param maxWait    超时读取时间
     * @param isDelay    是否延时处理
     * @param processor  批量处理方法
     */
    public ActionQueue(String name, int zoneSize, int pollSize, int threadSize, int maxWait, boolean isDelay, ActionQueueProcessor<T> processor) {
        this.name = name;
        this.zoneSize = zoneSize;
        this.pollSize = pollSize;
        this.threadSize = threadSize;
        this.maxWait = maxWait;
        this.isDelay = isDelay;
        this.processor = processor;
        ActionQueueRegister.registerActionQueue(name, this);
        start();
    }

    /**
     * 启动处理线程
     */
    private void start() {
        log.info("创建Action队列For：{}", this.name);


        if (this.isDelay) {
            for (int i = 0; i < this.zoneSize; i++) {
                sets.put(ZONE_PREFIX + i, new DataDelayedZone<>(name + "_" + i));
            }
        } else {
            for (int i = 0; i < this.zoneSize; i++) {
                sets.put(ZONE_PREFIX + i, new DataLinkedZone<>(name + "_" + i));
            }
        }

        // case 1:
        // zone:0,1,2,3,4,5,6,7
        // thread: A,B,C
        // A:0,3,6
        // B:1,4,7
        // C:2,5
        // -----------------------------
        // case 2:
        // zone:0,1,2
        // thread:A,B,C,D,E,F,G,H
        // A:0
        // B:1
        // C;2
        // D;0
        // E;1
        // F;2
        // G;0
        // H;1

        // 线程数量不得小于分区数量
        // 原因：暂未实现单个线程消费多个分区，但默认一个分区可被多个线程消费
        int threadSize = Math.max(this.threadSize, this.zoneSize);
        for (int i = 0; i < threadSize; i++) {
            int zoneIndex = i % this.zoneSize;
            ActionQueueConsumer<T> consumer = new ActionQueueConsumer<>(sets.get(ZONE_PREFIX + zoneIndex), processor, maxWait, pollSize);
            maps.put(ZONE_PREFIX + zoneIndex, consumer);
            ActionQueueScheduler.g().submit(consumer);
        }

        TopicQueueManager.g().registerTopic(name);

        TopicQueueManager.g().registerConsumer(name, 1, new TopicQueueConsumer<T>() {
            @Override
            public void consume(TopicQueueMessage<T> message) {
                send(new ActionQueueMessage<>(message.getKey(), message.getBody(), message.getTime()));
            }
        });
    }

    private int getZoneIndex(String key) {
        if (key == null || key.length() == 0) {
            key = "0";
        }
        return Math.floorMod(key.hashCode(), this.zoneSize);
    }

    /**
     * 往队列添加数据
     *
     * @param t 队列元素
     */
    public void send(ActionQueueMessage<T> t) {
        DataZone<ActionQueueMessage<T>> queue = sets.get(ZONE_PREFIX + getZoneIndex(t.getKey()));
        if (queue != null) {
            queue.send(t);
        }
    }

    interface DataZone<V> {
        String getZoneName();

        void send(V t);

        V fetch(long wait, TimeUnit timeUnit);

        int getZoneSize();
    }


    static class DataLinkedZone<T> extends LinkedBlockingQueue<ActionQueueMessage<T>> implements DataZone<ActionQueueMessage<T>> {
        private final String zoneName;

        public DataLinkedZone(String zoneName) {
            super(200000);
            this.zoneName = zoneName;
        }

        @Override
        public String getZoneName() {
            return zoneName;
        }

        @Override
        public void send(ActionQueueMessage<T> t) {
            super.offer(t);
        }

        @Override
        public ActionQueueMessage<T> fetch(long wait, TimeUnit timeUnit) {
            try {
                return super.poll(wait, timeUnit);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int getZoneSize() {
            return this.size();
        }
    }


    static class DataDelayedZone<T> extends DelayQueue<ActionQueueMessage<T>> implements DataZone<ActionQueueMessage<T>> {
        private final String zoneName;

        public DataDelayedZone(String zoneName) {
            super();
            this.zoneName = zoneName;
        }

        @Override
        public String getZoneName() {
            return zoneName;
        }

        @Override
        public void send(ActionQueueMessage<T> t) {
            super.offer(t);
        }

        @Override
        public ActionQueueMessage<T> fetch(long wait, TimeUnit timeUnit) {
            try {
                return super.poll(wait, timeUnit);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int getZoneSize() {
            return this.size();
        }
    }


    public Map<String, Map<String, Object>> getZoneInfo() {
        Map<String, Map<String, Object>> map = new HashMap<>();
        sets.forEach((key, zone) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("size", zone.getZoneSize());
            map.put(key, item);
        });
        return map;
    }
}
