package com.ds.screw.test.cache;

import cn.hutool.core.lang.Assert;
import com.ds.screw.cache.CacheUtils;
import com.ds.screw.cache.adapter.CacheHolder;
import com.ds.screw.test.ScrewTestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.cache.RedisCacheManager;

/**
 * 缓存工具测试
 *
 * @author dongsheng
 */
class CacheTest extends ScrewTestApplication {

    @Test
    void testCaffeineCache() {
        // 按照不同的缓存拓展存取数据
        CacheHolder.getCache("default", CaffeineCacheManager.class).put("caffeine", "caffeine123");
        Object caffeineValue = CacheHolder.getCache("default", CaffeineCacheManager.class).get("caffeine");
        Assert.equals("caffeine123", caffeineValue);
    }

    @Test
    void testRedisCache() {

        CacheHolder.getCache("default", RedisCacheManager.class).put("redis", "redis123");
        Object redisValue = CacheHolder.getCache("default", RedisCacheManager.class).get("redis");
        Assert.equals("redis123", redisValue);
    }

    @Test
    void testNamespcaeCache() {


        // 按照namespcae命令空间存取数据
        CacheHolder.getCache("namespcae1", CaffeineCacheManager.class).put("caffeine", "caffeine123");
        Object namespcae1Value = CacheHolder.getCache("namespcae1", CaffeineCacheManager.class).get("caffeine");

        CacheHolder.getCache("namespcae2", CaffeineCacheManager.class).put("caffeine", "caffeine456");
        Object namespcae2Value = CacheHolder.getCache("namespcae2", CaffeineCacheManager.class).get("caffeine");

        Object namespcae1ValueAgain = CacheHolder.getCache("namespcae1", CaffeineCacheManager.class).get("caffeine");
        Assert.notEquals(namespcae1Value, namespcae2Value);
        Assert.equals("caffeine123", namespcae1ValueAgain);

    }
}
