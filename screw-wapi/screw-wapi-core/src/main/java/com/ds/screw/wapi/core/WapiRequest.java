package com.ds.screw.wapi.core;

import com.ds.screw.wapi.core.exception.WapiException;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Wapi请求参数封装类
 *
 * @author: dongsheng
 */
public class WapiRequest {

    private HttpServletRequest request;

    private Map<String, Object> requestParam;

    private Map<String, Object> requestBody;

    private Map<String, Object> srcRequestBody;

    private Map<String, Object> srcRequestParam;

    private String resourceKey;

    private String method;

    private String dataId;

    private String userId;

    public WapiRequest() {
    }

    public WapiRequest(HttpServletRequest request, Map<String, Object> param, Map<String, Object> body, String resourceKey, String dataId) {
        this.request = request;
        body = body == null ? new HashMap<>() : body;
        param = param == null ? new HashMap<>() : param;

        this.srcRequestBody = new HashMap<>(body);
        this.srcRequestParam = new HashMap<>(param);
        this.requestParam = param;
        this.requestBody = body;
        this.resourceKey = resourceKey;
        this.dataId = dataId;
    }

    public WapiRequest(HttpServletRequest request, Map<String, Object> param, Map<String, Object> body, String resourceKey, String method, String dataId, String userId) {
        this(request, param, body, resourceKey, dataId);
        this.method = method;
        this.userId = userId;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    @Deprecated
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public Map<String, Object> getRequestParam() {
        return requestParam;
    }

    @Deprecated
    public void setRequestParam(Map<String, Object> requestParam) {
        this.requestParam = requestParam;
    }

    public Map<String, Object> getRequestBody() {
        return requestBody;
    }

    @Deprecated
    public void setRequestBody(Map<String, Object> requestBody) {
        this.requestBody = requestBody;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    @Deprecated
    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getMethod() {
        return method;
    }

    @Deprecated
    public void setMethod(String method) {
        this.method = method;
    }

    public String getDataId() {
        return dataId;
    }

    @Deprecated
    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getUserId() {
        return userId;
    }

    @Deprecated
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Object> getSrcRequestBody() {
        return srcRequestBody;
    }

    public Map<String, Object> getSrcRequestParam() {
        return srcRequestParam;
    }

    public static WapiRequestBuilder builder() {
        return new WapiRequestBuilder();
    }

    public static class WapiRequestBuilder {
        private HttpServletRequest request;
        private Map<String, Object> requestParam = new HashMap<>();
        private Map<String, Object> requestBody = new HashMap<>();
        private String resourceKey;
        private String method;
        private String dataId;
        private String userId;

        private WapiRequestBuilder() {
        }

        public WapiRequestBuilder request(HttpServletRequest request) {
            this.request = request;
            return this;
        }

        public WapiRequestBuilder requestParam(Map<String, Object> requestParam) {
            this.requestParam = requestParam;
            return this;
        }


        public WapiRequestBuilder requestBody(Map<String, Object> requestBody) {
            this.requestBody = requestBody;
            return this;
        }


        public WapiRequestBuilder resourceKey(String resourceKey) {
            this.resourceKey = resourceKey;
            return this;
        }


        public WapiRequestBuilder method(String method) {
            this.method = method;
            return this;
        }

        public WapiRequestBuilder dataId(String dataId) {
            this.dataId = dataId;
            return this;
        }

        public WapiRequestBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public WapiRequest build() {
            if (ObjectUtils.isEmpty(resourceKey)) {
                throw new WapiException("无效的资源键！");
            }
            return new WapiRequest(request, requestParam, requestBody, resourceKey, method, dataId, userId);
        }
    }
}
