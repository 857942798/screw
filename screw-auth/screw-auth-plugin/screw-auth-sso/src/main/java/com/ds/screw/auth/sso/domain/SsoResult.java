package com.ds.screw.auth.sso.domain;


import com.ds.screw.auth.sso.constant.SsoEnumCode;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 对Ajax请求返回Json格式数据的简易封装 <br>
 * 所有预留字段：<br>
 * code=状态码 <br>
 * msg=描述信息 <br>
 * data=携带对象 <br>
 */
public class SsoResult extends LinkedHashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 1L;    // 序列化版本号

    public static final String CODE_SUCCESS = "100000000";
    /**
     * sso 模块通用异常码
     */
    public static final String CODE_ERROR = "100001000";

    /**
     * 构建
     */
    public SsoResult() {
    }

    /**
     * 构建
     *
     * @param code 状态码
     * @param msg  信息
     * @param data 数据
     */
    public SsoResult(boolean success, String code, String msg, Object data) {
        this.setSuccess(success);
        this.setCode(code);
        this.setDesc(msg);
        this.setData(data);
    }

    /**
     * 根据 Map 快速构建
     *
     * @param map /
     */
    public SsoResult(Map<String, Object> map) {
        for (String key : map.keySet()) {
            this.set(key, map.get(key));
        }
    }

    /**
     * 获取code
     *
     * @return code
     */
    public String getCode() {
        return (String) this.get("code");
    }

    /**
     * 获取msg
     *
     * @return msg
     */
    public String getMsg() {
        return (String) this.get("desc");
    }

    /**
     * 获取data
     *
     * @return data
     */
    public Object getData() {
        return this.get("data");
    }

    /**
     * 获取success
     *
     * @return success
     */
    public boolean getSuccess() {
        return (boolean) this.get("success");
    }

    /**
     * 给code赋值，连缀风格
     *
     * @param code code
     * @return 对象自身
     */
    public SsoResult setCode(String code) {
        this.put("code", code);
        return this;
    }

    /**
     * 给msg赋值，连缀风格
     *
     * @param msg msg
     * @return 对象自身
     */
    public SsoResult setDesc(String msg) {
        this.put("desc", msg);
        return this;
    }

    /**
     * 给data赋值，连缀风格
     *
     * @param data data
     * @return 对象自身
     */
    public SsoResult setData(Object data) {
        this.put("data", data);
        return this;
    }

    /**
     * 给success赋值，连缀风格
     *
     * @param success success
     * @return 对象自身
     */
    public SsoResult setSuccess(boolean success) {
        this.put("success", success);
        return this;
    }

    /**
     * 写入一个值 自定义key, 连缀风格
     *
     * @param key  key
     * @param data data
     * @return 对象自身
     */
    public SsoResult set(String key, Object data) {
        this.put(key, data);
        return this;
    }

    /**
     * 写入一个Map, 连缀风格
     *
     * @param map map
     * @return 对象自身
     */
    public SsoResult setMap(Map<String, ?> map) {
        for (String key : map.keySet()) {
            this.put(key, map.get(key));
        }
        return this;
    }

    /**
     * 判断请求是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        if (this.getCode().equals(CODE_SUCCESS)) {
            return true;
        }
        return false;
    }


    // ============================  构建  ==================================

    // 构建成功
    public static SsoResult success() {
        return new SsoResult(true, CODE_SUCCESS, "ok", null);
    }

    public static SsoResult success(String msg) {
        return new SsoResult(true, CODE_SUCCESS, msg, null);
    }

    public static SsoResult data(Object data) {
        return new SsoResult(true, CODE_SUCCESS, "ok", data);
    }

    public static SsoResult error(SsoEnumCode enumCode) {
        return new SsoResult(false, enumCode.getCode(), enumCode.getDesc(), null);
    }

    // 构建指定状态码
    public static SsoResult get(boolean success, String code, String msg, Object data) {
        return new SsoResult(success, code, msg, data);
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{"
                + "\"success\": " + this.getSuccess()
                + "\"code\": " + this.getCode()
                + ", \"msg\": " + transValue(this.getMsg())
                + ", \"data\": " + transValue(this.getData())
                + "}";
    }

    private String transValue(Object value) {
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        return String.valueOf(value);
    }

}
