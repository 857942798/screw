package com.ds.screw.cache;

import org.springframework.cache.CacheManager;

public interface CacheManagerProvider {

    public CacheManager getCacheManager();

    public long getTimeout();

}
