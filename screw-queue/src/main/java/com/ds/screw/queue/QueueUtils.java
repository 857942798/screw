package com.ds.screw.queue;

import com.ds.screw.queue.topic.TopicQueueMessage;
import com.ds.screw.queue.topic.TopicQueueProducer;

/**
 * 消息队列工具类,默认由本地消息队列ActionQueue实现，当引用其它消息队列中间件时，可无缝切换其它消息队列中间件实现
 *
 * @author dongsheng
 */
public class QueueUtils {
    private QueueUtils() {
    }

    static TopicQueueProducer<Object> producer = new TopicQueueProducer<>();

    /**
     * 发送消息到指定的主题中
     *
     * @param topic 消息主题
     * @param key 消息的唯一key
     * @param body  消息内容
     * @param expire  消息过期时间
     * @param <T> 返回值
     */
    public static <T> void sendMessage(String topic,String key, T body, long expire) {
        try {
            producer.send(new TopicQueueMessage<>(topic, key, body, expire));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void sendMessage(String topic,String key, T body) {
        sendMessage(topic, key,body, 0);
    }

    public static <T> void sendMessage(String name, T body) {
        sendMessage("0", name, body, 0);
    }

    public static <T> void sendMessage(String name, T body, long expire) {
        sendMessage("0", name, body, expire);
    }
}
