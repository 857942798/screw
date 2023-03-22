package com.ds.screw.auth.sso.constant;

/**
 * <p>
 *      单点登录异常枚举类
 * </p>
 *
 * @author dongsheng
 */
public class SsoEnumCode {
    /** 位置错误 */
    public static final SsoEnumCode CODE_001 = new SsoEnumCode("100010001","未知错误");
    /** 接口调用请求异常 */
    public static final SsoEnumCode CODE_002 = new SsoEnumCode("100010002","接口调用请求异常");
    /** 请求没有得到有效的处理 */
    public static final SsoEnumCode CODE_003 = new SsoEnumCode("100010003","请求没有得到有效的处理");
    /** 提供的 ticket 是无效的 */
    public static final SsoEnumCode CODE_004 = new SsoEnumCode("100010004","无效的ticket");
    /** 请求携带的token 是无效的 */
    public static final SsoEnumCode CODE_005 = new SsoEnumCode("100010005","无效的登录状态");
    /** sso-client 调用 sso-server 端 单点注销接口 时，得到的响应是注销失败 */
    public static final SsoEnumCode CODE_006 = new SsoEnumCode("100010006","单点注销失败");
    /** http 请求调用 提供的 timestamp 与当前时间的差距超出允许的范围 */
    public static final SsoEnumCode CODE_007 = new SsoEnumCode("100010007","timestamp 超出允许的范围");
    /** http 请求调用 提供的 sign 无效 */
    public static final SsoEnumCode CODE_008 = new SsoEnumCode("100010008","sign无效");
    /** 本地系统没有配置 secretkey 字段 */
    public static final SsoEnumCode CODE_009 = new SsoEnumCode("100010009","未配置secretkey字段");
    /** 接口调用返回值为空 */
    public static final SsoEnumCode CODE_011 = new SsoEnumCode("100010011","本地账号不存在！");


    private String desc;
    private String code;

    public SsoEnumCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
