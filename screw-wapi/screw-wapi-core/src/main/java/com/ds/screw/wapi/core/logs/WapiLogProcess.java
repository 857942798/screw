package com.ds.screw.wapi.core.logs;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * wapi 日志处理
 *
 * @author dongsheng
 */
public class WapiLogProcess implements InitializingBean, DisposableBean {
    private static final int POOL_SIZE = 1;
    private final Logger log = LoggerFactory.getLogger(WapiLogProcess.class);
    private final List<WapiLogHandle> handleList = new ArrayList<>();

    @Autowired
    public WapiLogQueue wapiLogQueue;

    private ExecutorService executorService;


    /**
     * 启动
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug(">>>>> WapiLogProcess日志记录队列：start init wapi logs process ...");
        executorService = new ScheduledThreadPoolExecutor(POOL_SIZE,
                new BasicThreadFactory.Builder().namingPattern("wapi-log-handle-%d").daemon(true).build());
        for (int i = 0; i < POOL_SIZE; i++) {
            WapiLogHandle handle = new WapiLogHandle(wapiLogQueue);
            handleList.add(handle);
            executorService.execute(handle);
        }
    }

    /**
     * 停止
     */
    @Override
    public void destroy() throws Exception {
        handleList.forEach(WapiLogHandle::stopWork);
        executorService.shutdown();
    }
}
