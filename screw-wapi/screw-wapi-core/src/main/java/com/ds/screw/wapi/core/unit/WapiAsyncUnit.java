package com.ds.screw.wapi.core.unit;

import com.ds.screw.wapi.core.domain.WapiUnitParam;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class WapiAsyncUnit implements Runnable {

    private final Map<String, Object> paramMap = new ConcurrentHashMap<>();

    public Map<String, Object> getParamMap() {
        return this.paramMap;
    }

    public void setParamMap(Map<String, Object> map) {
        paramMap.clear();
        if (!CollectionUtils.isEmpty(map)) {
            paramMap.putAll(map);
        }

        //设置子线程共享 防止子线程中由于获取不到session报错
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(servletRequestAttributes, true);
    }

    @Override
    public void run() {
        this.execute();
    }

    public abstract void setAsyncParam(WapiUnitParam param);


    public abstract void execute();
}
