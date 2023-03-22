package com.ds.screw.queue.monitor;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池运行监控：当队列大小超过限制，且jvm内存使用率小于80%时扩容，防止无限制扩容
 *
 * @author dongsheng
 */
public class ThreadPoolMonitor {
    // 对象实例
    private static ThreadPoolMonitor instance = null;
    // 监控线程执行器
    private ScheduledExecutorService scheduledExecutorService;

    private ThreadPoolMonitor() {
        start();
    }

    public static ThreadPoolMonitor g() {
        return getInstance();
    }

    public static synchronized ThreadPoolMonitor getInstance() {
        if (instance == null) {
            instance = new ThreadPoolMonitor();
        }
        return instance;
    }

    private void start() {
        scheduledExecutorService = new ScheduledThreadPoolExecutor(10,
                new BasicThreadFactory.Builder().namingPattern("monitor-thread-monitor-pool-%d").daemon(true).build());
    }


    public void submit(Runnable runnable) {
        scheduledExecutorService.submit(runnable);
    }

    public void scheduleWithFixedDelay(Runnable runnable,long initialDelay,
                                       long delay,
                                       TimeUnit unit) {
        scheduledExecutorService.scheduleWithFixedDelay(runnable, initialDelay, delay, unit);
    }

    public void stop() throws InterruptedException {
        scheduledExecutorService.shutdown();
    }
}
