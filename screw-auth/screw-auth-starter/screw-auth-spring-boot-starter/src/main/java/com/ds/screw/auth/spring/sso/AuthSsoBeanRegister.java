package com.ds.screw.auth.spring.sso;

import com.ds.screw.auth.spring.sso.ssl.SslClientHttpRequestFactory;
import com.ds.screw.auth.sso.SsoManager;
import com.ds.screw.auth.sso.config.SsoConfig;
import com.ds.screw.auth.sso.config.SsoFunctionConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * <p>
 * 注册 SSO 所需要的Bean
 * </p>
 *
 * @author dongsheng
 */
@ConditionalOnClass(SsoManager.class)
public class AuthSsoBeanRegister {

    /**
     * 获取 SSO 配置Bean
     *
     * @return 配置对象
     */
    @Bean
    @ConditionalOnMissingBean(SsoConfig.class)
    @ConfigurationProperties(prefix = "config.auth.sso")
    public SsoConfig ssoConfig() {
        return new SsoConfig();
    }

    /**
     * 获取 SSO-handle function处理类bean
     *
     * @return 配置对象
     */
    @Bean
    @ConditionalOnMissingBean(SsoFunctionConfig.class)
    public SsoFunctionConfig ssoFunctionConfig() {
        return new SsoFunctionConfig().setSendHttp((url, headers) -> {
            try {
                SslClientHttpRequestFactory factory = new SslClientHttpRequestFactory(); //这里使用刚刚配置的SSL
                factory.setReadTimeout(30000);//单位为ms
                factory.setConnectTimeout(30000);//单位为ms
                RestTemplate restTemplate = new RestTemplate(factory);
                // 将参数都设置到头中
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                for (String key : headers.keySet()) {
                    httpHeaders.set(key, headers.get(key));
                }
                HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
                // 发起 http 请求
                ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                return result.getBody();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

}
