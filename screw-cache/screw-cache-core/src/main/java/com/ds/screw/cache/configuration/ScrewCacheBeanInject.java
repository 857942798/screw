package com.ds.screw.cache.configuration;

import com.ds.screw.cache.CacheManagerProvider;
import com.ds.screw.cache.adapter.CacheHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.List;

/**
 * <p>
 *     为bean注入属性
 * </p>
 *
 * @author dongsheng
 */
public class ScrewCacheBeanInject {

    @Autowired
    public void injectCacheManagers(List<CacheManagerProvider> cacheManagerProviders) {
        CacheHolder.getInstance().setCacheManagerProviders(cacheManagerProviders);
    }

}
