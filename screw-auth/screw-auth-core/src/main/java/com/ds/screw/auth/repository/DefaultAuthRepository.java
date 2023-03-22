package com.ds.screw.auth.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *      默认的数据存储实现类
 * </p>
 *
 * @author dongsheng
 */
public class DefaultAuthRepository implements AuthRepository {

    /**
     * 数据集合
     */
    private final Map<String, Object> dataMap = new ConcurrentHashMap<>();

    /**
     * 过期时间集合 (单位: 毫秒) , 记录所有key的到期时间 [注意不是剩余存活时间]
     */
    private final Map<String, Long> expireMap = new ConcurrentHashMap<>();

    /**
     * 构造函数
     */
    public DefaultAuthRepository() {
        // do nothing
    }


    public String getString(String key) {
        return (String) get(key);
    }

    @Override
    public Object get(String key) {
        clearKeyByTimeout(key);
        return dataMap.get(key);
    }

    @Override
    public void set(String key, Object value, long timeout) {
        if (timeout == 0 || timeout <= ALREADY_EXPIRE) {
            return;
        }
        dataMap.put(key, value);
        expireMap.put(key, (timeout == NEVER_EXPIRE) ? (NEVER_EXPIRE) : (System.currentTimeMillis() + timeout * 1000));
    }

    @Override
    public void update(String key, Object value) {
        if (getExpire(key) == ALREADY_EXPIRE) {
            return;
        }
        dataMap.put(key, value);
    }

    @Override
    public void delete(String key) {
        dataMap.remove(key);
        expireMap.remove(key);
    }

    /**
     * 获取指定key的剩余存活时间 (单位：秒)
     */
    @Override
    public long getExpire(String key) {
        // 先检查是否已经过期
        clearKeyByTimeout(key);
        // 获取过期时间
        Long expire = expireMap.get(key);
        // 如果根本没有这个值
        if (expire == null) {
            return ALREADY_EXPIRE;
        }
        // 如果被标注为永不过期
        if (expire == NEVER_EXPIRE) {
            return NEVER_EXPIRE;
        }
        // ---- 计算剩余时间并返回
        long timeout = (expire - System.currentTimeMillis()) / 1000;
        // 小于零时，视为不存在
        if (timeout < 0) {
            dataMap.remove(key);
            expireMap.remove(key);
            return ALREADY_EXPIRE;
        }
        return timeout;
    }

    @Override
    public void expire(String key, long timeout) {
        expireMap.put(key, System.currentTimeMillis() + timeout * 1000);
    }


    /**
     * 如果指定key已经过期，则立即清除它
     * 清除条件：如果不为空 && 不是 永不过期 && 已经超过过期时间
     *
     * @param key 指定key
     */
    void clearKeyByTimeout(String key) {
        Long expirationTime = expireMap.get(key);
        if (expirationTime != null && expirationTime != NEVER_EXPIRE && expirationTime < System.currentTimeMillis()) {
            dataMap.remove(key);
            expireMap.remove(key);
        }
    }

}
