package com.ds.screw.wapi.core.configuration;

import com.ds.screw.wapi.core.DefaultWapiInterceptor;
import com.ds.screw.wapi.core.controller.WapiRestController;
import com.ds.screw.wapi.core.logs.WapiLogProcess;
import com.ds.screw.wapi.core.logs.WapiLogQueue;
import com.ds.screw.wapi.core.properties.WapiProperties;
import com.ds.screw.wapi.core.service.WapiResourceService;
import com.ds.screw.wapi.core.service.impl.WapiServiceImpl;
import com.ds.screw.wapi.core.xml.utils.SpringContextUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * bean 注入
 *
 * @author dongsheng
 */
public class ScrewWapiConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "config.wapi")
    public WapiProperties wapiProperties() {
        return new WapiProperties();
    }

    @Bean
    public WapiLogQueue initWapiLogQueue(){
        return new WapiLogQueue();
    }

    @Bean
    public WapiLogProcess initWapiLogProcess(){
        return new WapiLogProcess();
    }

    @Bean
    public WapiResourceService initWapiResourceService(){
        return new WapiResourceService();
    }

    @Bean
    public SpringContextUtil initSpringContextUtil(){
        return new SpringContextUtil();
    }

    @Bean
    public DefaultWapiInterceptor initDefaultWapiInterceptor(){
        return new DefaultWapiInterceptor();
    }

    @Bean
    public WapiServiceImpl initWapiServiceImpl(){
        return new WapiServiceImpl();
    }

    @Bean
    public WapiRestController initWapiRestController(){
        return new WapiRestController();
    }

}
