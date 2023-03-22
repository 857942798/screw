package com.ds.screw.wapi.core.logs;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * wapi日志处理
 *
 * @author dongsheng
 */
public class WapiLogHandle implements Runnable {
    private final Logger log = LogManager.getLogger(WapiLogHandle.class);

    private final WapiLogQueue wapiLogQueue;
    private final List<WapiLog> saveRecords = new ArrayList<>();
    private boolean working = true;

    public WapiLogHandle(WapiLogQueue wapiLogQueue) {
        this.wapiLogQueue = wapiLogQueue;
    }

    @Override
    public void run() {
        while (working) {
            try {
                WapiLog wapiLog = wapiLogQueue.poll(30, TimeUnit.SECONDS);
                if (wapiLog == null) {
                    // 轮空，说明队里里面没有值，这时候直接入库
                    if (!saveRecords.isEmpty()) {
                        // 入库
                        insertRecords(saveRecords);
                    }
                } else {
                    saveRecords.add(wapiLog);
                    if (saveRecords.size() >= 50) {
                        insertRecords(saveRecords);
                    }
                }

                // 冻结一下，让位cpu
                Thread.sleep(0);
            } catch (Exception e) {
                log.error(this, e);
            }
        }
    }

    public void stopWork() {
        this.working = false;
    }

    //批量插入
    private void insertRecords(List<WapiLog> saveRecords) {
        if (CollectionUtils.isEmpty(saveRecords)) {
            return;
        }
        try {
            // TODO
        } catch (Exception e) {
            log.error(this, e);
        }
        //清空待保存数据
        saveRecords.clear();

    }
}
