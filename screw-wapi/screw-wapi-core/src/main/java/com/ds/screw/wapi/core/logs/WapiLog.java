package com.ds.screw.wapi.core.logs;

import com.ds.screw.wapi.core.WapiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dongsheng
 */
public class WapiLog {

    private String userId;
    private String resourceName;
    private String resourceKey;

    private String flowName;
    private String flowType;

    private Map<String, Object> requestBody;
    private Map<String, Object> requestParam;

    private WapiResponse<?> response;

    private String remoteIp;
    private String localIp;
    private Date opDate;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public Map<String, Object> getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Map<String, Object> requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, Object> getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(Map<String, Object> requestParam) {
        this.requestParam = requestParam;
    }

    public WapiResponse<?> getResponse() {
        return response;
    }

    public void setResponse(WapiResponse<?> response) {
        this.response = response;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public String getModName() {
        return !ObjectUtils.isEmpty(this.getResourceName()) ? this.getResourceName() : this.getResourceKey();
    }

    public String getOpName() {
        return !ObjectUtils.isEmpty(this.getFlowName()) ? this.getFlowName() : this.getFlowType();
    }

    public Date getOpDate() {
        return opDate;
    }

    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }

    public String getParamData() {
        // 处理请求参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("requestBody", this.getRequestBody());
        paramMap.put("requestParam", this.getRequestParam());
        try {
            return  new ObjectMapper().writeValueAsString(paramMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getResultData() {
        try {
            return  new ObjectMapper().writeValueAsString(this.getResponse());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
