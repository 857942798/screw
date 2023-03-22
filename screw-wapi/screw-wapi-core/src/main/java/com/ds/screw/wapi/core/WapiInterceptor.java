package com.ds.screw.wapi.core;

public interface WapiInterceptor {
    int order();

    boolean preHandle(WapiRequest request, WapiResponse<?> response);

    void postHandle(WapiRequest request, WapiResponse<?> response);

    void afterCompletion(WapiRequest request, WapiResponse<?> wapiResponse, WapiProcessor processor, Exception ex);
}
