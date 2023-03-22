package com.ds.screw.wapi.core.domain;

/**
 * @author: dongsheng
 */
public class WapiEnumCode {
    /*通用成功码*/
    public static WapiEnumCode SUCCESS = new WapiEnumCode("100000000", "OK");    // 00000 系统级错误 开始
    public static WapiEnumCode SYSTEM_ERROR = new WapiEnumCode("100000001", "系统异常");
    public static WapiEnumCode SYSTEM_RUNTIME_ERROR = new WapiEnumCode("100000002", "运行异常");
    public static WapiEnumCode PARAMETER_ERROR = new WapiEnumCode("100000012", "参数错误");
    public static WapiEnumCode FILE_TOO_LARGE_ERROR = new WapiEnumCode("100000013", "上传的文件过大");

    private String desc;
    private String code;

    public WapiEnumCode(String code, String desc) {
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
