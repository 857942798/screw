package com.ds.screw.wapi.core.domain;


/**
 * 和WapiResponse结构体相同，只是为了区分不同的业务场景，方便以后有针对性的改动
 */
public class WapiUnitResult {

    /**
     * 处理单元执行是否成功
     */
    private boolean success;
    /**
     * 描述
     */
    private String desc;
    /**
     * 状态码
     */
    private String code;
    /**
     * 处理单元返回的数据
     */
    private Object data;

    /**
     * 成功码只有一个
     */
    public WapiUnitResult() {
        this.success = true;
        this.desc = WapiEnumCode.SUCCESS.getDesc();
        this.code = WapiEnumCode.SUCCESS.getCode();
    }

    /**
     * 成功码只有一个
     */
    public WapiUnitResult success() {
        this.success = true;
        this.desc = WapiEnumCode.SUCCESS.getDesc();
        this.code = WapiEnumCode.SUCCESS.getCode();
        return this;
    }

    /**
     * 成功码只有一个
     */
    public <T> WapiUnitResult success(T t) {
        this.success = true;
        this.desc = WapiEnumCode.SUCCESS.getDesc();
        this.code = WapiEnumCode.SUCCESS.getCode();
        this.data = t;
        return this;
    }

    /**
     * 返回失败必须要有具体的错误码
     */
    public WapiUnitResult failure(WapiEnumCode wapiEnumCode) {
        this.success = false;
        this.desc = wapiEnumCode.getDesc();
        this.code = wapiEnumCode.getCode();
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
