package com.ds.screw.auth.spring.sso.listener;

import com.ds.screw.auth.AuthManager;
import com.ds.screw.auth.context.domain.AuthRequest;
import com.ds.screw.auth.spring.event.LoginEvent;
import com.ds.screw.auth.sso.SsoManager;
import com.ds.screw.auth.sso.SsoUtils;
import com.ds.screw.auth.sso.config.SsoConfig;
import com.ds.screw.auth.sso.domain.SsoLoginSync;
import com.ds.screw.auth.sso.domain.SsoResult;
import com.ds.screw.auth.util.AuthFoxUtils;
import org.springframework.context.ApplicationListener;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 * sso 登录成功事件实现
 *
 * @author dongsheng
 */
public class SsoLoginListener implements ApplicationListener<LoginEvent> {

    private static final Logger log = Logger.getLogger(SsoLoginListener.class.getName());

    @Override
    public void onApplicationEvent(LoginEvent event) {
        SsoConfig cfg = SsoManager.getConfig();
        String loginId = event.getTokenInfo().getLoginId();

        String ssoAccount = SsoManager.getSsoResolver().ssoAccountResolver(loginId);

        AuthRequest authRequest = AuthManager.getAuthRequestContext().getRequest();
        // 如果没有登出的配置项，则根据request获取当前系统根路径
        SsoConfig config = SsoManager.getConfig();
        String ssoLogoutCall = config.getSsoLogoutCall();
        if (AuthFoxUtils.isEmpty(ssoLogoutCall)) {
            HttpServletRequest request = (HttpServletRequest) authRequest.getSource();
            String path = request.getContextPath();
            ssoLogoutCall = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
        }
        SsoLoginSync ssoLogin = new SsoLoginSync();
        ssoLogin.setSsoAccount(ssoAccount);
        ssoLogin.setSsoLogoutCall(ssoLogoutCall);
        ssoLogin.setUniqueMark(authRequest.getAddress());

        log.info(">>>> 单点登录代理：客户端[" + cfg.getSystemCode() + "]：登录成功，发起登录信息同步请求！");

        // 将登录信息同步到认证中心
        SsoResult result = SsoUtils.ssoSyncLogin(ssoLogin.toMap());

        if (result.isSuccess()) {
            log.info(">>>> 单点登录代理：客户端[" + cfg.getSystemCode() + "]：登录信息同步到认证中心成功！");
        } else {
            log.warning(">>>> 单点登录代理：客户端[" + cfg.getSystemCode() + "]：登录信息同步到认证中心失败！");
        }

    }
}
