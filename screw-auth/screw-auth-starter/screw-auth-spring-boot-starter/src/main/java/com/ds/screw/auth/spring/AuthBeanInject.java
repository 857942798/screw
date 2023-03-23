package com.ds.screw.auth.spring;

import com.ds.screw.auth.AuthManager;
import com.ds.screw.auth.config.AuthConfig;
import com.ds.screw.auth.context.AuthRequestContext;
import com.ds.screw.auth.listener.AuthEventListener;
import com.ds.screw.auth.provider.AuthCheckProvider;
import com.ds.screw.auth.publisher.AuthEventPublisher;
import com.ds.screw.auth.repository.AuthRepository;
import com.ds.screw.auth.spring.json.AuthJsonTemplateForJackson;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 注入screw-auth所需要的Bean
 *
 * @author dongsheng
 */
public class AuthBeanInject {

    /**
     * 注入配置Bean
     *
     * @param saTokenConfig 配置对象
     */
    @Autowired(required = false)
    public void setConfig(AuthConfig saTokenConfig) {
        AuthManager.setConfig(saTokenConfig);
    }

    /**
     * 注入持久化Bean
     *
     * @param authRepository AuthRepository对象
     */
    @Autowired(required = false)
    public void setSaTokenDao(AuthRepository authRepository) {
        AuthManager.setAuthRepository(authRepository);
    }


    /**
     * 注入上下文Bean
     *
     * @param authRequestContext AuthRequestContext对象
     */
    @Autowired(required = false)
    public void setSaTokenContext(AuthRequestContext authRequestContext) {
        AuthManager.setAuthRequestContext(authRequestContext);
    }


    /**
     * 注入侦听器Bean
     *
     * @param authEventListener AuthListener对象
     */
    @Autowired(required = false)
    public void setSaTokenListener(AuthEventListener authEventListener) {
        AuthManager.setAuthListener(authEventListener);
    }

    /**
     * 注入事件发布器Bean
     *
     * @param authEventPublisher AuthEventPublisher对象
     */
    @Autowired(required = false)
    public void setAuthEventPublisher(AuthEventPublisher authEventPublisher) {
        AuthManager.setAuthEventPublisher(authEventPublisher);
    }

    /**
     * 注入自定义的 JSON 转换器 Bean
     *
     * @param authJsonTemplateForJackson JSON 转换器
     */
    @Autowired(required = false)
    public void setAuthJsonTemplateForJackson(AuthJsonTemplateForJackson authJsonTemplateForJackson) {
        AuthManager.setAuthJsonTemplate(authJsonTemplateForJackson);
    }

    /**
     * 注入自定义的 权限认证 Bean
     *
     * @param authCheckProvider 权限认证 Bean
     */
    @Autowired(required = false)
    public void setAuthCheckProvider(AuthCheckProvider authCheckProvider) {
        AuthManager.setAuthCheckProvider(authCheckProvider);
    }
}
