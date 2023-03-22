package com.ds.screw.wapi.core;

import com.ds.screw.wapi.core.domain.WapiEnumCode;
import com.ds.screw.wapi.core.exception.WapiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dongsheng
 */
public class WapiResponse<T>{
    
    private boolean success;
    private String desc;
    private String code;
    @SuppressWarnings("all")
    private T data;

    public WapiResponse() {
        this.success = true;
        this.desc = WapiEnumCode.SUCCESS.getDesc();
        this.code = WapiEnumCode.SUCCESS.getCode();
    }

    public WapiResponse(boolean success, String desc, String code, T data) {
        this.success = success;
        this.desc = desc;
        this.code = code;
        this.data = data;
    }

    /**
     * 成功码只有一个
     */
    public WapiResponse<T> success() {
        this.success = true;
        this.desc = WapiEnumCode.SUCCESS.getDesc();
        this.code = WapiEnumCode.SUCCESS.getCode();
        return this;
    }

    public WapiResponse<T> success(String desc, T data) {
        this.success = true;
        this.data = data;
        this.desc = desc;
        this.code = WapiEnumCode.SUCCESS.getCode();
        return this;
    }

    public WapiResponse<T> success(T data) {
        this.success = true;
        this.desc = WapiEnumCode.SUCCESS.getDesc();
        this.code = WapiEnumCode.SUCCESS.getCode();
        this.data = data;
        return this;
    }


    /**
     * 返回失败必须要有具体的错误码
     */
    public WapiResponse<T> failure() {
        this.success = false;
        this.code = WapiEnumCode.SYSTEM_ERROR.getCode();
        this.desc = WapiEnumCode.SYSTEM_ERROR.getDesc();
        return this;
    }

    /**
     * 返回失败必须要有具体的错误码
     */
    public WapiResponse<T> failure(WapiException wapiException) {
        this.success = false;
        this.code = wapiException.getCode();
        this.desc = wapiException.getMessage();
        return this;
    }

    /**
     * 返回失败必须要有具体的错误码
     */
    public WapiResponse<T> failure(String code) {
        this.success = false;
        this.code = code;
        this.desc = "Failed";
        return this;
    }

    /**
     * 返回失败必须要有具体的错误码
     */
    public WapiResponse<T> failure(WapiEnumCode wapiEnumCode) {
        this.success = false;
        this.desc = wapiEnumCode.getDesc();
        this.code = wapiEnumCode.getCode();
        return this;
    }

    public WapiResponse<T> failure(WapiEnumCode wapiEnumCode, T data) {
        this.success = false;
        this.desc = wapiEnumCode.getDesc();
        this.code = wapiEnumCode.getCode();
        this.data = data;
        return this;
    }


    public WapiResponse<T> failure(String code, String desc, T data) {
        this.success = false;
        this.desc = desc;
        this.code = code;
        this.data = data;
        return this;
    }

    public WapiResponse<T> failure(String code, String desc) {
        this.success = false;
        this.desc = desc;
        this.code = code;
        return this;
    }


    public WapiResponse<T> of(WapiResponse<?> wapiResponse) {
        this.success = wapiResponse.isSuccess();
        this.desc = wapiResponse.getDesc();
        this.code = wapiResponse.getCode();
        this.data = (T) wapiResponse.getData();
        return this;
    }

    public WapiResponse<T> of(boolean success, String code, String desc, T data) {
        this.success = success;
        this.code = code;
        this.desc = desc;
        this.data = data;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        Map<String, Object> map = new HashMap<>();
        map.put("success", success);
        map.put("desc", desc);
        map.put("code", code);
        map.put("data", data);
        try {
            return new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
