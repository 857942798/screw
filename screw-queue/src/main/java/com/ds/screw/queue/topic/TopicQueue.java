package com.ds.screw.queue.topic;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 由BlockingQueue实现的本地队列
 *
 * 设计目的：用于快速切换消息队列中间件实现，其本地消费由ActionQueue实现
 *
 * @author dongsheng
 */
class TopicQueue<V> {
    private final Topic<V> topic = new Topic<V>();

    public void send(TopicQueueMessage<V> record) {
        topic.offer(record);
    }

    public TopicQueueMessage<V> poll(Long timeout, TimeUnit seconds) {
        try {
            return topic.poll(timeout, seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    static class Topic<V> extends LinkedBlockingQueue<TopicQueueMessage<V>> {
        public Topic() {
            super(200000);
        }
    }
}
