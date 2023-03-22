package com.ds.screw.auth.sso.domain;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 *      存入认证节点的登录信息
 * </p>
 *
 * @author dongsheng
 */
public class SsoLoginInfo {
    /** 登录来源标识 */
    private String uniqueMark;
    /** 第一次登录时间 */
    private LocalDateTime loginTime;
    /** 单点注销-回调URL */
    private Set<String> ssoLogoutCalls;

    public String getUniqueMark() {
        return uniqueMark;
    }

    public SsoLoginInfo setUniqueMark(String uniqueMark) {
        this.uniqueMark = uniqueMark;
        return this;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public SsoLoginInfo setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
        return this;
    }

    public Set<String> getSsoLogoutCalls() {
        return ssoLogoutCalls;
    }

    public SsoLoginInfo setSsoLogoutCalls(Set<String> ssoLogoutCalls) {
        this.ssoLogoutCalls = ssoLogoutCalls;
        return this;
    }
}
