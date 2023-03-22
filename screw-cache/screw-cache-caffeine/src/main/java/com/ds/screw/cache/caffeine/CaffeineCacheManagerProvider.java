package com.ds.screw.cache.caffeine;

import com.ds.screw.cache.CacheManagerProvider;
import org.springframework.cache.CacheManager;
import org.springframework.lang.NonNull;

/**
 * Caffeine 缓存管理类
 *
 * @author dongsheng
 */
public class CaffeineCacheManagerProvider implements CacheManagerProvider {

    private CacheManager cacheManager;
    private long timeout = -1;

    public CaffeineCacheManagerProvider(@NonNull CacheManager cacheManager, long timeout) {
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
