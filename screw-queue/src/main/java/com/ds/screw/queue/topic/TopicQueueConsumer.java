package com.ds.screw.queue.topic;

import java.util.concurrent.TimeUnit;

/**
 * 队列数据抽象消费者，由ActionQueue注册实现本地消费
 *
 * @author dongsheng
 */
public abstract class TopicQueueConsumer<V> implements Runnable {

    private final Long timeout;
    private TopicQueue<V> topic;

    public TopicQueueConsumer() {
        this.timeout = 1L;
    }

    public void subscribe(TopicQueue<V> queue) {
        this.topic = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                TopicQueueMessage<V> msg = topic.poll(this.timeout, TimeUnit.SECONDS);
                if (msg != null) {
                    this.consume(msg);
                }
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void consume(TopicQueueMessage<V> list);
}
