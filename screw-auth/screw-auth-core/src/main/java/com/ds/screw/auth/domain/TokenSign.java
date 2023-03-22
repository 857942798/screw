package com.ds.screw.auth.domain;


import com.ds.screw.auth.context.AuthContextHolder;

import java.io.Serializable;
/**
 * <p>
 *      Token签名：用以标识用户的来源, token签名与token值、登录id、设备类型、请求来源标识(Servlet环境默认使用ip作为标识)四个因素有关.
 * </p>
 *      根据token签名,系统可以判断出用户是否发生了异地登录、同一个用户的会话数量是否已经达到上限等等.
 *
 * @author dongsheng
 */
public class TokenSign implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8153274193836394030L;

    /**
     * token值
     */
    private String value;

    /**
     * 账号Id
     */
    private String loginId;

    /**
     * 所属设备类型
     */
    private String device;

    /**
     * 所属网络请求来源地址
     */
    private String address;

    /**
     * 无参构造
     */
    public TokenSign() {
    }

    /**
     * 构建一个
     *
     * @param value  token值
     * @param device 所属设备类型
     */
    public TokenSign(String value, String loginId, String device) {
        this.value = value;
        this.loginId = loginId;
        this.device = device;
        this.address = AuthContextHolder.getRequest().getAddress();
    }

    /**
     * @return token值
     */
    public String getValue() {
        return value;
    }

    /**
     * @return 所属设备类型
     */
    public String getDevice() {
        return device;
    }

    /**
     * @return 关联账号Id
     */
    public String getLoginId() {
        return loginId;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}