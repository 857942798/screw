package com.ds.screw.auth.spring.sso;

import com.ds.screw.auth.resolver.SsoResolver;
import com.ds.screw.auth.spring.sso.listener.SsoBeforeLogoutListener;
import com.ds.screw.auth.spring.sso.listener.SsoLoginListener;
import com.ds.screw.auth.sso.SsoManager;
import com.ds.screw.auth.sso.SsoTemplate;
import com.ds.screw.auth.sso.config.SsoConfig;
import com.ds.screw.auth.sso.config.SsoFunctionConfig;
import com.ds.screw.auth.util.AuthFoxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * 注入 screw-auth-sso 所需要的Bean
 * </p>
 *
 * @author dongsheng
 */
@ConditionalOnClass(SsoManager.class)
public class AuthSsoBeanInject {

    /**
     * 注入 SSO 配置Bean
     *
     * @param ssoConfig 配置对象
     */
    @Autowired(required = false)
    @SuppressWarnings("all")
    public void setSsoConfig(SsoConfig ssoConfig) throws Exception {
        if (AuthFoxUtils.isEmpty(ssoConfig.getSystemCode())) {
            throw new Exception("无有效的系统标号配置！！！，请添加参数配置【\"config.auth.sso.systemCode\"】");
        }
        SsoManager.setConfig(ssoConfig);
    }

    /**
     * 注入 SSO-handle function处理类bean
     *
     * @param ssoFunctionConfig 配置对象
     */
    @Autowired(required = false)
    public void setSsoFunctionConfig(SsoFunctionConfig ssoFunctionConfig) {
        SsoManager.setFunctionConfig(ssoFunctionConfig);
    }

    /**
     * 注入sso账号解析工具
     *
     * @param ssoResolver sso账号解析工具
     */
    @Autowired(required = false)
    public void setSsoResolver(SsoResolver ssoResolver) {
        SsoManager.setSsoResolver(ssoResolver);
    }


    /**
     * 注入 SSO 单点登录模块 Bean
     *
     * @param ssoTemplate ssoTemplate对象
     */
    @Autowired(required = false)
    public void setSsoTemplate(SsoTemplate ssoTemplate) {
        SsoManager.setSsoTemplate(ssoTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(name = "ssoLoginListener")
    @ConditionalOnProperty(prefix = "config.auth.sso", name = "enable", havingValue = "true")
    public SsoLoginListener ssoLoginListener() {
        return new SsoLoginListener();
    }

    @Bean
    @ConditionalOnMissingBean(name = "ssoBeforeLogoutListener")
    @ConditionalOnProperty(prefix = "config.auth.sso", name = "enable", havingValue = "true")
    public SsoBeforeLogoutListener ssoBeforeLogoutListener() {
        return new SsoBeforeLogoutListener();
    }

}
