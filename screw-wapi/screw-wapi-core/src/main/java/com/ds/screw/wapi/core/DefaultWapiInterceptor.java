package com.ds.screw.wapi.core;


import com.ds.screw.wapi.core.domain.WapiConst;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class DefaultWapiInterceptor implements WapiInterceptor {

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean preHandle(WapiRequest request, WapiResponse<?> wapiResponse) {
        // 获取用户ID
        String userId = getRequestUserId(request);
        request.setUserId(userId);
        Map<String, Object> maps = request.getRequestBody();
        maps.put("login_id", userId);
        return true;
    }

    @Override
    public void postHandle(WapiRequest request, WapiResponse<?> response) {
        Object res = response.getData();
        if (res instanceof WapiResponse) {
            response.of((WapiResponse) res);
        }
    }

    /**
     * 获取用户Id
     *
     * @param wapiRequest WapiRequest
     * @return userId
     */
    private String getRequestUserId(WapiRequest wapiRequest) {
        HttpServletRequest httpServletRequest = wapiRequest.getRequest();
        if (null != httpServletRequest) {
            HttpSession session = httpServletRequest.getSession();
            if (!ObjectUtils.isEmpty(session.getAttribute(WapiConst.LOGIN_ID))) {
                return session.getAttribute(WapiConst.LOGIN_ID).toString();
            }
        }
        return null;
    }

    @Override
    public void afterCompletion(WapiRequest request, WapiResponse<?> response, WapiProcessor processor, Exception ex) {

    }
}
