package com.ds.screw.auth.config;

import java.io.Serializable;
/**
 * 全局配置类
 *
 * @author dongsheng
 */
public class AuthConfig implements Serializable {

    private static final long serialVersionUID = -241637313237949395L;

    /**
     * token名称 (同时也是cookie名称)
     */
    private String tokenName = "token";

    /**
     * token的长久有效期(单位:秒) 默认7天, -1代表永久
     */
    private long timeout = 60 * 60 * 24 * 7L;

    /**
     * token临时有效期 [指定时间内无操作就视为token过期] (单位: 秒), 默认-1 代表不限制
     * (例如可以设置为1800代表30分钟内无操作就过期)
     */
    private long activityTimeout = -1;

    /**
     * 异地登录策略，控制不同的踢人下线策略
     *
     */
    private String loginStrategy = "9";

    /**
     * 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)
     */
    private Boolean isShare = true;

    /**
     * 同一账号最大登录数量，-1代表不限 （只有在 isConcurrent=true, isShare=false 时此配置才有效）
     */
    private int maxLoginCount = 12;

    /**
     * 是否尝试从请求体里读取token
     */
    private Boolean isReadBody = true;

    /**
     * 是否尝试从header里读取token
     */
    private Boolean isReadHead = true;

    /**
     * 配置当前项目的网络访问地址
     */
    private String domain;

    /**
     * @return token名称 (同时也是cookie名称)
     */
    public String getTokenName() {
        return tokenName;
    }

    /**
     * @param tokenName token名称 (同时也是cookie名称)
     * @return 对象自身
     */
    public AuthConfig setTokenName(String tokenName) {
        this.tokenName = tokenName;
        return this;
    }

    /**
     * @return token的长久有效期(单位 : 秒) 默认30天, -1代表永久
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * @param timeout token的长久有效期(单位:秒) 默认30天, -1代表永久
     * @return 对象自身
     */
    public AuthConfig setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * @return token临时有效期 [指定时间内无操作就视为token过期] (单位: 秒), 默认-1 代表不限制
     * (例如可以设置为1800代表30分钟内无操作就过期)
     */
    public long getActivityTimeout() {
        return activityTimeout;
    }

    /**
     * @param activityTimeout token临时有效期 [指定时间内无操作就视为token过期] (单位: 秒), 默认-1 代表不限制
     *                        (例如可以设置为1800代表30分钟内无操作就过期)
     * @return 对象自身
     */
    public AuthConfig setActivityTimeout(long activityTimeout) {
        this.activityTimeout = activityTimeout;
        return this;
    }

    /**
     * @return 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)
     */
    public Boolean getIsShare() {
        return isShare;
    }

    /**
     * @param isShare 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)
     * @return 对象自身
     */
    public AuthConfig setIsShare(Boolean isShare) {
        this.isShare = isShare;
        return this;
    }

    /**
     * @return 同一账号最大登录数量，-1代表不限 （只有在 isConcurrent=true, isShare=false 时此配置才有效）
     */
    public int getMaxLoginCount() {
        return maxLoginCount;
    }

    /**
     * @param maxLoginCount 同一账号最大登录数量，-1代表不限 （只有在 isConcurrent=true, isShare=false 时此配置才有效）
     * @return 对象自身
     */
    public AuthConfig setMaxLoginCount(int maxLoginCount) {
        this.maxLoginCount = maxLoginCount;
        return this;
    }

    /**
     * @return 是否尝试从请求体里读取token
     */
    public Boolean getIsReadBody() {
        return isReadBody;
    }

    /**
     * @param isReadBody 是否尝试从请求体里读取token
     * @return 对象自身
     */
    public AuthConfig setIsReadBody(Boolean isReadBody) {
        this.isReadBody = isReadBody;
        return this;
    }

    /**
     * @return 是否尝试从header里读取token
     */
    public Boolean getIsReadHead() {
        return isReadHead;
    }

    /**
     * @param isReadHead 是否尝试从header里读取token
     * @return 对象自身
     */
    public AuthConfig setIsReadHead(Boolean isReadHead) {
        this.isReadHead = isReadHead;
        return this;
    }

    /**
     * @return 配置当前项目的网络访问地址
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain 配置当前项目的网络访问地址
     * @return 对象自身
     */
    public AuthConfig setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public String getLoginStrategy() {
        return loginStrategy;
    }

    public void setLoginStrategy(String loginStrategy) {
        this.loginStrategy = loginStrategy;
    }

    @Override
    public String toString() {
        return "AuthConfig{" +
                "tokenName='" + tokenName + '\'' +
                ", timeout=" + timeout +
                ", activityTimeout=" + activityTimeout +
                ", loginStrategy=" + loginStrategy +
                ", isShare=" + isShare +
                ", maxLoginCount=" + maxLoginCount +
                ", isReadBody=" + isReadBody +
                ", isReadHead=" + isReadHead +
                ", domain='" + domain + '\'' +
                '}';
    }
}