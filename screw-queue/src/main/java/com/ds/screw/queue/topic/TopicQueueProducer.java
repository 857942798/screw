package com.ds.screw.queue.topic;

/**
 * 队列数据生产者，需实例化后使用
 *
 * @author dongsheng
 */
public class TopicQueueProducer<V> {
    public void send(TopicQueueMessage<V> message) {
        TopicQueue<V> queue = TopicQueueManager.g().getTopic(message.getTopic());
        if (queue != null) {
            queue.send(message);
        }
    }

}
