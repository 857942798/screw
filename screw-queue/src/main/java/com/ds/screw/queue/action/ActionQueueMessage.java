package com.ds.screw.queue.action;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 实现了Delayed接口，可延迟执行
 *
 * @author dongsheng
 */
class ActionQueueMessage<T> implements Delayed {

    String key;
    T body;
    private long time;

    public ActionQueueMessage() {
    }

    public ActionQueueMessage(String key, T body, long time) {
        this.key = key;
        this.body = body;
        this.time = System.nanoTime()+time*1000000000L;
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

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.time-System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (o != null) {
            ActionQueueMessage<T> msg = (ActionQueueMessage<T>) o;
            long diff = this.time - msg.time;
            if (diff < 0) {// 改成>=会造成问题
                return -1;
            } else {
                return 1;
            }
        }
        return 0;
    }
}