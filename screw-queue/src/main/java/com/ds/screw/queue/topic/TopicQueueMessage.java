package com.ds.screw.queue.topic;

/**
 *
 * @author dongsheng
 */
public class TopicQueueMessage<T> {
    String topic;
    String key;
    T body;
    long time = 0;

    public TopicQueueMessage() {
    }

    public TopicQueueMessage(String topic, String key, T body) {
        this.topic = topic;
        this.key = key;
        this.body = body;
    }

    public TopicQueueMessage(String topic, String key, T body, long time) {
        this.topic = topic;
        this.key = key;
        this.body = body;
        this.time = time;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
