package com.ds.screw.queue.action;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理注册的ActionQueue
 *
 * @author dongsheng
 */
public class ActionQueueRegister {

    private static Map<String, ActionQueue<?>> queueMap = new ConcurrentHashMap<>();


    public static void registerActionQueue(String name, ActionQueue<?> queue) {
        queueMap.put(name, queue);
    }

    public static Map<String, Object> printActionQueue() {
        Map<String, Object> map = new HashMap<>();
        queueMap.forEach((key, item)-> {
            Map<String, Map<String, Object>> info = item.getZoneInfo();
            map.put(key, info);
        });
        return map;
    }
}
