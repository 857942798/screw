package com.ds.screw.auth.sso.config;



import com.ds.screw.auth.util.AuthFoxUtils;

import java.io.Serializable;

/**
 * <p>
 *  SSO 单点登录模块 配置类
 * </p>
 *
 * @author dongsheng
 */
public class SsoConfig implements Serializable {

    private static final long serialVersionUID = -6541280061182004705L;
    /**
     * 是否打开单点登录
     */
    private boolean enable;
    /**
     * 系统编号，请务必保证多个系统间的系统编码唯一
     */
    private String systemCode;

    // ---------------- ticket 相关配置
    /**
     * Ticket有效期 (单位: 秒)
     */
    private long ticketTimeout = 60;

    /**
     * sso登录信息保存有效期 (单位: 秒)
     */
    private long ssoLoginTimeout = 60 * 60 * 6L;


    // ----------------- Server端相关配置


    // ----------------- Client端相关配置

    /**
     * 配置 Server 端单点登录地址
     */
    private String serverUrl;

    /**
     * 配置当前 Client 端的单点注销回调URL （为空时自动获取）
     */
    private String ssoLogoutCall;
    /**
     * 是否打开单点注销功能
     */
    private boolean ssoLogoutEnable = true;

    // ----------------- 其它
    /**
     * 接口调用秘钥
     */
    private String secretKey = "6K9zBcCVv38ZOkY84";

    /**
     * 接口调用时的时间戳允许的差距（单位：ms），-1代表不校验差距
     */
    private long timestampDisparity = 1000 * 60L;



    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    /**
     * @return 接口调用秘钥 (用于SSO模式三单点注销的接口通信身份校验)
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * @param secretKey 接口调用秘钥 (用于SSO模式三单点注销的接口通信身份校验)
     * @return 对象自身
     */
    public SsoConfig setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    /**
     * @return 配置的 Server 端单点登录授权地址
     */
    public String getServerUrl() {
        if (!AuthFoxUtils.isEmpty(serverUrl) && serverUrl.endsWith("/")) {
            return serverUrl.substring(0, serverUrl.length() - 1);
        }
        return serverUrl;
    }

    /**
     * @param serverUrl 配置 Server 端单点登录授权地址
     * @return 对象自身
     */
    public SsoConfig setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        return this;
    }


    /**
     * @return 是否打开单点注销功能
     */
    public boolean isSsoLogoutEnable() {
        return ssoLogoutEnable;
    }

    /**
     * @param ssoLogoutEnable 是否打开单点注销功能
     * @return 对象自身
     */
    public SsoConfig setSsoLogoutEnable(boolean ssoLogoutEnable) {
        this.ssoLogoutEnable = ssoLogoutEnable;
        return this;
    }

    /**
     * @return 配置当前 Client 端的单点注销回调URL （为空时自动获取）
     */
    public String getSsoLogoutCall() {
        if (ssoLogoutCall != null && ssoLogoutCall.endsWith("/")) {
            return ssoLogoutCall.substring(0, ssoLogoutCall.length() - 1);
        }
        return ssoLogoutCall;
    }

    /**
     * @param ssoLogoutCall 配置当前 Client 端的单点注销回调URL （为空时自动获取）
     * @return 对象自身
     */
    public SsoConfig setSsoLogoutCall(String ssoLogoutCall) {
        this.ssoLogoutCall = ssoLogoutCall;
        return this;
    }

    /**
     * @return 接口调用时的时间戳允许的差距（单位：ms），-1代表不校验差距
     */
    public long getTimestampDisparity() {
        return timestampDisparity;
    }

    /**
     * @param timestampDisparity 接口调用时的时间戳允许的差距（单位：ms），-1代表不校验差距
     * @return 对象自身
     */
    public SsoConfig setTimestampDisparity(long timestampDisparity) {
        this.timestampDisparity = timestampDisparity;
        return this;
    }

    public long getTicketTimeout() {
        return ticketTimeout;
    }

    public SsoConfig setTicketTimeout(long ticketTimeout) {
        this.ticketTimeout = ticketTimeout;
        return this;
    }

    public long getSsoLoginTimeout() {
        return ssoLoginTimeout;
    }

    public SsoConfig setSsoLoginTimeout(long ssoLoginTimeout) {
        this.ssoLoginTimeout = ssoLoginTimeout;
        return this;
    }
}
