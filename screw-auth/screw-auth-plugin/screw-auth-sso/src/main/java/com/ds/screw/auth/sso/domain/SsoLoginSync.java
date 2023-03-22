package com.ds.screw.auth.sso.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *      同步认证节点的登录信息
 * </p>
 *
 * @author dongsheng
 */
public class SsoLoginSync {
    /** 登录id */
    private String ssoAccount;
    /** 登录来源标识 */
    private String uniqueMark;
    /** 单点注销-回调URL */
    private String ssoLogoutCall;

    public String getLoginId() {
        return ssoAccount;
    }

    public void setSsoAccount(String ssoAccount) {
        this.ssoAccount = ssoAccount;
    }

    public String getUniqueMark() {
        return uniqueMark;
    }

    public void setUniqueMark(String uniqueMark) {
        this.uniqueMark = uniqueMark;
    }

    public String getSsoLogoutCall() {
        return ssoLogoutCall;
    }

    public void setSsoLogoutCall(String ssoLogoutCall) {
        this.ssoLogoutCall = ssoLogoutCall;
    }

    public Map<String,String> toMap() {
        Map<String,String> map = new HashMap<>();
        map.put("ssoAccount",this.ssoAccount);
        map.put("uniqueMark",this.uniqueMark);
        map.put("ssoLogoutCall",this.ssoLogoutCall);
        return map;
    }
}
