package com.ds.screw.queue.topic;


import com.ds.screw.queue.monitor.ThreadPoolMonitor;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 队列数据生产者，需实例化后使用
 *
 * @author dongsheng
 */
class TopicQueueScheduler {

    private final Logger log = LoggerFactory.getLogger(TopicQueueScheduler.class);

    //每次扩容自动增加线程数
    private static final int PER_ADD_THREAD = 5;
    //监控积压时间频率
    private static final int MONITOR_DELAY_TIME = 1;
    //对象实例
    private static TopicQueueScheduler instance = null;
    // 调度线程池执行器
    private ThreadPoolExecutor executor;

    private TopicQueueScheduler() {
        start();
    }

    public static TopicQueueScheduler g() {
        return getInstance();
    }

    public static synchronized TopicQueueScheduler getInstance() {
        if (instance == null) {
            instance = new TopicQueueScheduler();
        }
        return instance;
    }

    /**
     * 获取jvm内存使用率
     */
    public static double getMemoryUsage() {
        return (double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Runtime.getRuntime().maxMemory();
    }

    public void start() {
        executor = new ThreadPoolExecutor(
                1,
                1000,
                60L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),

                new BasicThreadFactory.Builder().namingPattern("monitor-topic-queue-handle-pool-%d").daemon(true).build());

        ThreadPoolMonitor.g().scheduleWithFixedDelay(() -> {
            //当队列大小超过限制，且jvm内存使用率小于80%时扩容，防止无限制扩容
            if (!executor.getQueue().isEmpty() && executor.getPoolSize() < executor.getMaximumPoolSize() && getMemoryUsage() < 0.8) {
                log.info("线程池扩容！{}", executor);
                executor.setCorePoolSize(executor.getPoolSize() + PER_ADD_THREAD);
            }
        }, MONITOR_DELAY_TIME, MONITOR_DELAY_TIME, TimeUnit.SECONDS);
    }

    public void stop() throws InterruptedException {
        executor.shutdown();
        while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
            //等待线程池中任务执行完毕
        }
    }


    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

    public Future<?> submit(Runnable task) {
        return executor.submit(task);
    }
}
