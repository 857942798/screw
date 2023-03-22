package com.ds.screw.queue.action;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 消费者
 *
 * @author dongsheng
 */
class ActionQueueConsumer<T> implements Runnable {
    // 阻塞分区队列
    private final ActionQueue.DataZone<ActionQueueMessage<T>> zone;
    // 实际处理单元
    private final ActionQueueProcessor<T> processor;
    // 设置队列处理时间
    private final long timeout;
    // 设置队列处理长度
    private final int batchSize;
    // 运行状态
    private boolean flag = true;
    // 用来存放从队列拿出的数据
    private List<T> dataList;

    ActionQueueConsumer(ActionQueue.DataZone<ActionQueueMessage<T>> zone, ActionQueueProcessor<T> processor, long timeout, int batchSize) {
        this.zone = zone;
        this.processor = processor;
        this.dataList = new ArrayList<>(batchSize);
        this.batchSize = batchSize;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        long endTime;
        while (isRunning()) {
            try {
                // 从队列拿出队列头部的元素，如果没有就阻塞
                ActionQueueMessage<T> t = this.getZone().fetch(1, TimeUnit.SECONDS);
                if (t != null) {
                    dataList.add(t.getBody());
                }
                if (dataList.size() >= batchSize) {
                    callBack(dataList);
                    startTime = System.currentTimeMillis();
                } else {
                    endTime = System.currentTimeMillis();
                    long time = endTime - startTime;
                    if (time >= timeout) {
                        if (dataList.size() > 0) {
                            callBack(dataList);
                        }
                        startTime = System.currentTimeMillis();
                    }
                }
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void callBack(List<T> dataList) {
        // 处理队列
        try {
            this.getProcessor().processData(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 清理掉dataList中的元素
            clear();
        }
    }

    /**
     * 清理生成的list
     */
    private void clear() {
        dataList = null;
        dataList = new ArrayList<>();
    }


    boolean isRunning() {
        return this.flag;
    }

    ActionQueue.DataZone<ActionQueueMessage<T>> getZone() {
        return this.zone;
    }

    void stop() {
        this.flag = false;
    }

    public ActionQueueProcessor<T> getProcessor() {
        return processor;
    }
}
