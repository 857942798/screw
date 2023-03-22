package com.ds.screw.cache.caffeine;

import com.ds.screw.cache.entity.CacheValue;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Caffeine 缓存配置管理类
 *
 * @author dongsheng
 */
@EnableCaching
public class ScrewCaffeineConfiguration {

    private static final long DEFAULT_TIME_MS = 24 * 60 * 60 * 1000L;

    @Bean("screwCaffeineCacheManager")
    @Primary
    public CaffeineCacheManager cacheManager() {
        Caffeine<Object, Object> caffeine = configuration();
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setAllowNullValues(true);
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }


    private Caffeine<Object, Object> configuration() {
        return Caffeine.newBuilder()
                .maximumSize(500000)
                .expireAfter(new Expiry<Object, Object>() {
                    @Override
                    public long expireAfterCreate(@NonNull Object key, @NonNull Object value, long currentTime) {
                        if (value instanceof CacheValue) {
                            long timeout = ((CacheValue<?, ?>) value).getTtl();
                            if (timeout == -1) {
                                return DEFAULT_TIME_MS * 1000 * 1000L;
                            }
                            return timeout * 1000 * 1000L;
                        }
                        return DEFAULT_TIME_MS * 1000 * 1000L;
                    }

                    @Override
                    public long expireAfterUpdate(@NonNull Object key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
                        return currentDuration;
                    }

                    @Override
                    public long expireAfterRead(@NonNull Object key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
                        return currentDuration;
                    }
                });
    }


    @Bean("screwCaffeineCacheManagerProvider")
    public CaffeineCacheManagerProvider getCaffeineCacheManagerProvider(CaffeineCacheManager caffeineCacheManager) {
        return new CaffeineCacheManagerProvider(caffeineCacheManager, DEFAULT_TIME_MS);
    }
}
