package com.ds.screw.cache.adapter;

import com.ds.screw.cache.CacheManagerProvider;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
/**
 * 缓存实现持有类，用以选择不同的缓存实现
 *
 * @author dongsheng
 */
public class CacheHolder {

    private static final Map<String, CacheAdapter> cacheMap = new ConcurrentHashMap<>(16);

    private static List<CacheManagerProvider> cacheManagerProviders = new ArrayList<>();

    private static CacheHolder instance;

    CacheHolder() {
        instance=this;
    }

    // 使用单例模式实例化
    public static CacheHolder getInstance() {
        if (instance == null) {
            synchronized (CacheHolder.class) {
                if (instance == null) {
                    instance = new CacheHolder();
                }
            }
        }
        return instance;
    }

    /**
     * 注入不同的缓存实现
     * @param cacheManagerProviders
     */
    public static void setCacheManagerProviders(List<CacheManagerProvider> cacheManagerProviders) {
        synchronized (CacheHolder.class) {
            CacheHolder.cacheManagerProviders = cacheManagerProviders;
        }
    }

    private static List<CacheManagerProvider> getCacheManagerProviders() {
        synchronized (CacheHolder.class) {
            if (cacheManagerProviders == null || cacheManagerProviders.isEmpty()) {
                throw new UnsupportedOperationException("no cache manager found");
            }
            return cacheManagerProviders;
        }
    }

    public static CacheAdapter getCache(String name) {
        return getCache(name, cacheManagerProviders.get(0).getCacheManager().getClass());
    }

    /**
     * 获取具体的缓存实现
     *
     * @param name  命名空间
     * @param clazz 缓存实现类，可选值有 CaffeineCacheManager.class、RedisCacheManager.class
     * @return
     */
    public static CacheAdapter getCache(String name, Class<? extends CacheManager> clazz) {
        List<CacheManagerProvider> cacheManagerFilterResult = getCacheManagerProviders().stream().filter(item -> (item.getCacheManager().getClass().isAssignableFrom(clazz))).collect(Collectors.toList());
        if (cacheManagerFilterResult.isEmpty()) {
            throw new UnsupportedOperationException("no cache manager found");
        }
        return cacheMap.computeIfAbsent(name, s -> {
            Cache cache = cacheManagerFilterResult.get(0).getCacheManager().getCache(name);
            return new CacheAdapter(cache);
        });
    }
}
