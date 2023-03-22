package com.ds.screw.auth.config;

/**
 * 常量定义
 *
 * @author dongsheng
 */
public class AuthConst {

    private AuthConst() {
    }

    /**
     * 常量key标记: 如果token为本次请求新创建的，则以此字符串为key存储在当前request中
     */
    public static final String JUST_CREATED = "JUST_CREATED_";


    /**
     * 常量key标记: 在登录时，默认使用的设备类型
     */
    public static final String DEFAULT_LOGIN_DEVICE = "default-device";
}
