package com.ds.screw.wapi.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * [功能描述]
 *
 * @author dongsheng
 */
public class WapiAsyncExecutor {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static ExecutorService getInstance() {
        return executorService;
    }
}
