package com.ds.screw.auth.domain;

import java.io.Serializable;

/**
 * <p>
 *      令牌：用来描述一个Token的常用参数
 * </p>
 *
 * @author dongsheng
 */
public class Token implements Serializable {

    private static final long serialVersionUID = 6682086489890811502L;

    /**
     * token名称
     */
    private String tokenName;

    /**
     * token值
     */
    private String tokenValue;

    /**
     * 此token是否已经登录
     */
    private Boolean isLogin;

    /**
     * 此token对应的LoginId，未登录时为null
     */
    private String loginId;

    /**
     * token剩余有效期 (单位: 秒)
     */
    private long tokenTimeout;

    /**
     * User-Session剩余有效时间 (单位: 秒)
     */
    private long sessionTimeout;

    /**
     * token剩余无操作有效时间 (单位: 秒)
     */
    private long tokenActivityTimeout;

    /**
     * 登录设备类型
     */
    private String loginDevice;

    public Token() {
        // this constructor is empty
    }

    /**
     * @return token名称
     */
    public String getTokenName() {
        return tokenName;
    }

    /**
     * @param tokenName token名称
     */
    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    /**
     * @return token值
     */
    public String getTokenValue() {
        return tokenValue;
    }

    /**
     * @param tokenValue token值
     */
    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    /**
     * @return 此token是否已经登录
     */
    public Boolean getIsLogin() {
        return isLogin;
    }

    /**
     * @param isLogin 此token是否已经登录
     */
    public void setIsLogin(Boolean isLogin) {
        this.isLogin = isLogin;
    }

    /**
     * @return 此token对应的LoginId，未登录时为null
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * @param loginId 此token对应的LoginId，未登录时为null
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }


    /**
     * @return token剩余有效期 (单位: 秒)
     */
    public long getTokenTimeout() {
        return tokenTimeout;
    }

    /**
     * @param tokenTimeout token剩余有效期 (单位: 秒)
     */
    public void setTokenTimeout(long tokenTimeout) {
        this.tokenTimeout = tokenTimeout;
    }

    /**
     * @return User-Session剩余有效时间 (单位: 秒)
     */
    public long getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * @param sessionTimeout User-Session剩余有效时间 (单位: 秒)
     */
    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }


    /**
     * @return token剩余无操作有效时间 (单位: 秒)
     */
    public long getTokenActivityTimeout() {
        return tokenActivityTimeout;
    }

    /**
     * @param tokenActivityTimeout token剩余无操作有效时间 (单位: 秒)
     */
    public void setTokenActivityTimeout(long tokenActivityTimeout) {
        this.tokenActivityTimeout = tokenActivityTimeout;
    }

    /**
     * @return 登录设备类型
     */
    public String getLoginDevice() {
        return loginDevice;
    }

    /**
     * @param loginDevice 登录设备类型
     */
    public void setLoginDevice(String loginDevice) {
        this.loginDevice = loginDevice;
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                "tokenName='" + tokenName + '\'' +
                ", tokenValue='" + tokenValue + '\'' +
                ", isLogin=" + isLogin +
                ", loginId='" + loginId + '\'' +
                ", tokenTimeout=" + tokenTimeout +
                ", sessionTimeout=" + sessionTimeout +
                ", tokenActivityTimeout=" + tokenActivityTimeout +
                ", loginDevice='" + loginDevice + '\'' +
                '}';
    }
}
