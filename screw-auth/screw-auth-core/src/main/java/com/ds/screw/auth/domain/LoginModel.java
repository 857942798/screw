package com.ds.screw.auth.domain;

import com.ds.screw.auth.config.AuthConfig;
import com.ds.screw.auth.config.AuthConst;

/**
 * <p>
 *      登录信息封装类
 * </p>
 *
 * @author dongsheng
 */
public class LoginModel {
    /**
     * 此次登录的客户端设备类型
     */
    private String device;

    /**
     * 指定此次登录token的有效期, 单位:秒 （如未指定，自动取全局配置的timeout值）
     */
    private Long timeout;

    /**
     * 静态方法获取一个 LoginModel 对象
     *
     * @return LoginModel 对象
     */
    public static LoginModel create() {
        return new LoginModel();
    }

    /**
     * @return 此次登录的客户端设备类型
     */
    public String getDevice() {
        return device;
    }

    /**
     * @param device 此次登录的客户端设备类型
     * @return 对象自身
     */
    public LoginModel setDevice(String device) {
        this.device = device;
        return this;
    }

    /**
     * @return 指定此次登录token的有效期, 单位:秒 （如未指定，自动取全局配置的timeout值）
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * @param timeout 指定此次登录token的有效期, 单位:秒 （如未指定，自动取全局配置的timeout值）
     * @return 对象自身
     */
    public LoginModel setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * @return 获取device参数，如果为null，则返回默认值
     */
    public String getDeviceOrDefault() {
        if (device == null) {
            device = AuthConst.DEFAULT_LOGIN_DEVICE;
        }
        return device;
    }

    /**
     * 构建对象，初始化默认值
     *
     * @param config 配置对象
     * @return 对象自身
     */
    public LoginModel build(AuthConfig config) {
        if (timeout == null) {
            timeout = config.getTimeout();
        }
        return this;
    }

    /*
     * toString
     */
    @Override
    public String toString() {
        return "LoginModel [device=" + device + ", timeout=" + timeout + "]";
    }


}
