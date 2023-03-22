package com.ds.screw.cache;

import com.ds.screw.cache.entity.CacheKey;
import com.ds.screw.cache.entity.CacheValue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * 一个通用的缓存抽象委托类
 *
 * @author dongsheng
 */
public abstract class GeneralCache<K, V> implements CacheContainer<K, V> {

    protected final Map<K, Lock> keyLockMap = new ConcurrentHashMap<>();

    protected long timeout = -1;

    protected GeneralCache(long defaultTimeout) {
        this.timeout = defaultTimeout;
    }

    @Override
    public void put(K key, V value) {
        this.put(new CacheValue<>(new CacheKey<>(key), value, timeout));
    }

    @Override
    public void put(K key, V value, long timeout) {
        this.put(new CacheValue<>(new CacheKey<>(key), value, timeout));
    }

    @Override
    public V get(K key) {
        CacheValue<CacheKey<K>, V> cacheObj = this.get(new CacheKey<>(key));
        if (cacheObj != null) {
            return cacheObj.getValue();
        }
        return null;
    }

    @Override
    public V get(K key, Function<K, V> supplier) {
        V v = this.get(key);
        if (null == v && null != supplier) {
            Lock keyLock = this.keyLockMap.computeIfAbsent(key, (k) -> new ReentrantLock());
            keyLock.lock();

            try {
                CacheValue<CacheKey<K>, V> co = this.get(new CacheKey<>(key));
                if (null != co && !co.isExpired()) {
                    v = co.getValue();
                } else {
                    try {
                        v = supplier.apply(key);
                    } catch (Exception var11) {
                        throw new RuntimeException(var11);
                    }

                    this.put(key, v, this.timeout);
                }
            } finally {
                keyLock.unlock();
                this.keyLockMap.remove(key);
            }
        }
        return v;
    }

    @Override
    public void remove(K key) {
        remove(new CacheKey<>(key));
    }

    @Override
    public boolean hasKey(K key) {
        return hasKey(new CacheKey<>(key));
    }

    protected abstract void put(CacheValue<CacheKey<K>, V> obj);

    protected abstract CacheValue<CacheKey<K>, V> get(CacheKey<K> key);

    protected abstract void remove(CacheKey<K> key);

    protected abstract boolean hasKey(CacheKey<K> key);
}
