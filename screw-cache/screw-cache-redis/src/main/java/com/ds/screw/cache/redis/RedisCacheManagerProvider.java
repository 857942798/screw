package com.ds.screw.cache.redis;


import com.ds.screw.cache.CacheManagerProvider;
import org.springframework.cache.CacheManager;

public class RedisCacheManagerProvider implements CacheManagerProvider {

    private CacheManager cacheManager;
    private long timeout;

    public RedisCacheManagerProvider(CacheManager cacheManager, long timeout) {
        this.cacheManager = cacheManager;
        this.timeout = timeout;
    }

    @Override
    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
