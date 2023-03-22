package com.ds.screw.auth.spring.sso.listener;

import com.ds.screw.auth.exception.LogoutFailedException;
import com.ds.screw.auth.spring.event.BeforeLogoutEvent;
import com.ds.screw.auth.sso.SsoManager;
import com.ds.screw.auth.sso.SsoUtils;
import com.ds.screw.auth.sso.config.SsoConfig;
import com.ds.screw.auth.sso.domain.SsoResult;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

import java.util.logging.Logger;

/**
 * sso 登录前事件实现
 *
 * @author dongsheng
 */
public class SsoBeforeLogoutListener implements ApplicationListener<BeforeLogoutEvent> {

    private static final Logger log = Logger.getLogger(SsoBeforeLogoutListener.class.getName());

    @Override
    public void onApplicationEvent(@NonNull BeforeLogoutEvent beforeLogoutEvent) {

        SsoConfig cfg = SsoManager.getConfig();
        try {
            SsoResult result = SsoUtils.ssoLogout();
            if (!result.isSuccess()) {
                log.info(">>>> 单点登录代理：客户端[" + cfg.getSystemCode() + "]：认证中心同步下线失败！登出失败！");
                throw new LogoutFailedException();
            } else {
                log.info(">>>> 单点登录代理：客户端[" + cfg.getSystemCode() + "]：认证中心同步下线成功！登出成功！");
            }
        } catch (Exception e) {
            log.warning(">>>> 单点登录代理：客户端[" + cfg.getSystemCode() + "]：认证中心同步下线失败！登出失败！");
            throw new LogoutFailedException();
        }
    }
}
