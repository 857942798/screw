package com.ds.screw.auth.spring;

import com.ds.screw.auth.config.AuthConfig;
import com.ds.screw.auth.context.AuthRequestContext;
import com.ds.screw.auth.publisher.AuthEventPublisher;
import com.ds.screw.auth.spring.context.AuthRequestContextForSpring;
import com.ds.screw.auth.spring.event.AuthEventPublisherForSpring;
import com.ds.screw.auth.spring.json.AuthJsonTemplateForJackson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

/**
 * 注册screw-auth所需要的Bean
 * <p> Bean 的注册与注入应该分开在两个文件中，否则在某些场景下会造成循环依赖
 *
 * @author dongsheng
 */
public class AuthBeanRegister {

    @Bean
    @ConditionalOnMissingBean(AuthConfig.class)
    @ConfigurationProperties(prefix = "csmf.auth")
    public AuthConfig getAuthConfig() {
        return new AuthConfig();
    }

    /**
     * 获取上下文Bean (Spring版)
     *
     * @return 容器交互Bean (Spring版)
     */
    @Bean
    public AuthRequestContext getAuthRequestContext() {
        return new AuthRequestContextForSpring();
    }

    /**
     * 获取事件发布器 (Spring版)
     *
     * @return 容器交互Bean (Spring版)
     */
    @Bean
    public AuthEventPublisher getAuthEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new AuthEventPublisherForSpring(applicationEventPublisher);
    }

    /**
     * 获取 json 转换器 Bean (Jackson版)
     *
     * @return json 转换器 Bean (Jackson版)
     */
    @Bean
    public AuthJsonTemplateForJackson authJsonTemplateForJackson() {
        return new AuthJsonTemplateForJackson();
    }
}
