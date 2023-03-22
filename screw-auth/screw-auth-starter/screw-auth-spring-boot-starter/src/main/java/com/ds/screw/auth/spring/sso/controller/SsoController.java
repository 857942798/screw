package com.ds.screw.auth.spring.sso.controller;

import com.ds.screw.auth.sso.handler.SsoClientHandle;
import com.ds.screw.auth.sso.handler.SsoServerHandle;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  SSO 对外接口，当开启sso配置时才会生效
 * </p>
 *
 * @author dongsheng
 */
@ConditionalOnProperty(prefix = "config.auth.sso", name = "enable", havingValue = "true")
@RestController
public class SsoController {

    @RequestMapping("/auth/sso/client/*")
    public Object ssoClientRequest() {
        return SsoClientHandle.ssoClientRequest();
    }

    @RequestMapping("/auth/sso/server/*")
    public Object ssoServerRequest() {
        return SsoServerHandle.ssoServerRequest();
    }

}
