package com.ds.screw.cache.configuration;

import com.ds.screw.cache.adapter.CacheHolder;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 *      注入所需要的bean
 * </p>
 *
 * @author dongsheng
 */
public class ScrewCacheBeanRegister {

    @Bean
    public CacheHolder getCacheHolder() {
        return CacheHolder.getInstance();
    }

}
