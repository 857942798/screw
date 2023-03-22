package com.ds.screw.queue.topic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 队列数据生产者，需实例化后使用
 *
 * @author dongsheng
 */
public class TopicQueueManager {

    private static TopicQueueManager instance = null;
    // topic名称为key，队列为value
    private final Map<String, TopicQueue<?>> maps = new ConcurrentHashMap<>();

    private TopicQueueManager() {
    }

    public static TopicQueueManager g() {
        return getInstance();
    }

    public static synchronized TopicQueueManager getInstance() {
        if (instance == null) {
            instance = new TopicQueueManager();
        }
        return instance;
    }

    public <V> TopicQueue<V> getTopic(String topic) {
        if (maps.containsKey(topic)) {
            return (TopicQueue<V>) maps.get(topic);
        }
        return null;
    }

    /**
     * 注册一个主题
     *
     * @param topic
     */
    public void registerTopic(String topic) {
        maps.put(topic, new TopicQueue<>());
    }

    /**
     * 注册一个消费者
     *
     * @param topic
     * @param threadSize
     * @param consumer
     * @param <V>
     */
    public <V> void registerConsumer(String topic, int threadSize, TopicQueueConsumer<V> consumer) {
        for (int i = 0; i < threadSize; i++) {
            TopicQueue<V> queue = getTopic(topic);
            consumer.subscribe(queue);
            TopicQueueScheduler.g().submit(consumer);
        }
    }
}
