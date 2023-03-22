package com.ds.screw.cache.adapter;

import com.ds.screw.cache.GeneralCache;
import com.ds.screw.cache.entity.CacheKey;
import com.ds.screw.cache.entity.CacheValue;
import org.springframework.cache.Cache;

/**
 * 缓存适配，适配spring的公共缓存操作的接口
 *
 * @author dongsheng
 */
public class CacheAdapter extends GeneralCache<Object, Object> {

    private Cache cache;

    public CacheAdapter(Cache cache) {
        this(cache, -1);
    }

    public CacheAdapter(Cache cache, long defaultTimeout) {
        super(defaultTimeout);
        this.cache = cache;
    }


    @Override
    public void clear() {
        this.cache.clear();
    }

    @Override
    protected void put(CacheValue<CacheKey<Object>, Object> obj) {
        this.cache.put(obj.getKey(), obj);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected CacheValue<CacheKey<Object>, Object> get(CacheKey<Object> key) {
        Cache.ValueWrapper valueWrapper = this.cache.get(key);
        if (valueWrapper != null) {
            Object realValue = valueWrapper.get();
            if (realValue instanceof CacheValue) {
                return (CacheValue<CacheKey<Object>, Object>) realValue;
            } else {
                // todo 这里的过期时间只是为了适配，并不准确，当数据存入缓存的那一刻，过期时间已在流逝。
                return new CacheValue<>(key, realValue, timeout);
            }
        }
        return null;
    }

    @Override
    protected void remove(CacheKey<Object> key) {
        this.cache.evictIfPresent(key);
    }

    @Override
    protected boolean hasKey(CacheKey<Object> key) {
        return this.cache.get(key) != null;
    }
}
