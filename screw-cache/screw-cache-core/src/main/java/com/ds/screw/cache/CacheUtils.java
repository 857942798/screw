package com.ds.screw.cache;


import com.ds.screw.cache.adapter.CacheHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存工具类,该工具类仅使用默认的命名空间进行存储
 *
 * @author : dongsheng
 */
public class CacheUtils {

    private static final String DEFAULT_SPACE = "default";

    protected CacheUtils() {
    }

    /**
     * 是否存在key
     */
    public static boolean hasKey(String key) {
        return CacheHolder.getCache(DEFAULT_SPACE).hasKey(key);
    }

    /**
     * 功能描述：设置key-value到redis中,不设置过期时间
     *
     * @param key   键
     * @param value 值
     */
    public static boolean put(String key, Object value) {
        try {
            CacheHolder.getCache(DEFAULT_SPACE).put(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 功能描述：设置key-value到redis中,并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  单位：秒
     */
    public static boolean put(String key, Object value, long time) {
        try {
            CacheHolder.getCache(DEFAULT_SPACE).put(key, value, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 功能描述：通过key获取缓存里面的值
     *
     * @param key 键
     */
    public static String getString(String key) {
        return CacheHolder.getCache(DEFAULT_SPACE).get(key) == null ? "" : CacheHolder.getCache(DEFAULT_SPACE).get(key).toString();
    }

    /**
     * 功能描述：通过key获取缓存里面的值
     *
     * @param key 键
     */
    public static Object getObject(String key) {
        return CacheHolder.getCache(DEFAULT_SPACE).get(key);
    }

    /**
     * 功能描述：设置key-value到redis中,不设置过期时间
     *
     * @param key   键
     * @param value 值
     */
    public static boolean putObject(String key, Object value) {
        try {
            CacheHolder.getCache(DEFAULT_SPACE).put(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 功能描述：设置key-value到redis中,不设置过期时间
     *
     * @param key   键
     * @param value 值
     */
    public static boolean putObject(String key, Object value, long time) {
        try {
            CacheHolder.getCache(DEFAULT_SPACE).put(key, value, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取存储在哈希表中指定字段的值
     */
    @SuppressWarnings("all")
    public static Object hGet(String key, String field) {
        Map<String, Object> map = (Map) CacheHolder.getCache(DEFAULT_SPACE).get(key);
        if (map != null) {
            return map.get(field);
        }
        return null;
    }

    /**
     * 获取存储在哈希表中指定字段的值
     */
    @SuppressWarnings("all")
    public static boolean hPut(String key, String field, Object value) {
        try {
            Map<String, Object> map = (Map) CacheHolder.getCache(DEFAULT_SPACE).get(key);
            if (map == null) {
                map = new HashMap<>();
            }
            map.put(field, value);
            CacheHolder.getCache(DEFAULT_SPACE).put(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将map中的键值添加至指定key中
     */
    public static boolean hPutAll(String key, Map<String, Object> maps) {
        try {
            CacheHolder.getCache(DEFAULT_SPACE).put(key, maps);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将map中的键值添加至指定key中
     */
    public static boolean hPutAll(String key, Map<String, Object> maps, long time) {
        try {
            CacheHolder.getCache(DEFAULT_SPACE).put(key, maps, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 功能描述：设置某个key过期时间
     *
     * @param key  键
     * @param time 时间
     */
    public static boolean expire(String key, long time) {
        try {
            if (time > 0) {
                CacheHolder.getCache(DEFAULT_SPACE).put(key, CacheHolder.getCache(DEFAULT_SPACE).get(key), time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 功能描述：移除key
     *
     * @param key 键
     */
    public static void delete(String key) {
        CacheHolder.getCache(DEFAULT_SPACE).remove(key);
    }

    /**
     * 清空默认命名空间下的所有数据
     */
    public static void cleanAll() {
        CacheHolder.getCache(DEFAULT_SPACE).clear();
    }

}
