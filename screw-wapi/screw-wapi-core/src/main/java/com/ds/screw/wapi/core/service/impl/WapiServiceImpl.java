package com.ds.screw.wapi.core.service.impl;

import com.ds.screw.wapi.core.WapiInterceptor;
import com.ds.screw.wapi.core.WapiProcessor;
import com.ds.screw.wapi.core.WapiRequest;
import com.ds.screw.wapi.core.WapiResponse;
import com.ds.screw.wapi.core.domain.WapiConst;
import com.ds.screw.wapi.core.domain.WapiEnumCode;
import com.ds.screw.wapi.core.service.WapiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class WapiServiceImpl implements WapiService {

    private final Logger logger = LoggerFactory.getLogger(WapiServiceImpl.class);

    List<WapiInterceptor> wapiInterceptors;

    /**
     * 获取拦截器并执行排序后注入
     *
     * @param wapiInterceptors 拦截器
     */
    @Autowired
    public void setWapiInterceptors(List<WapiInterceptor> wapiInterceptors) {
        wapiInterceptors.sort((u1, u2) -> {
            int diff = u1.order() - u2.order();
            if (diff > 0) {
                return 1;
            } else if (diff < 0) {
                return -1;
            }
            return 0;
        });
        this.wapiInterceptors = wapiInterceptors;
    }

    @Override
    public WapiResponse<Object> get(WapiRequest wapiRequest) {
        wapiRequest.setMethod(WapiConst.METHOD.GET.getName());
        return process(wapiRequest);
    }

    @Override
    public WapiResponse<Object> post(WapiRequest wapiRequest) {
        wapiRequest.setMethod(WapiConst.METHOD.POST.getName());
        return process(wapiRequest);
    }

    @Override
    public WapiResponse<Object> put(WapiRequest wapiRequest) {
        wapiRequest.setMethod(WapiConst.METHOD.PUT.getName());
        return process(wapiRequest);
    }

    @Override
    public WapiResponse<Object> delete(WapiRequest wapiRequest) {
        wapiRequest.setMethod(WapiConst.METHOD.DELETE.getName());
        return process(wapiRequest);
    }


    /**
     * wapi流程单元执行
     *
     * @param wapiRequest request请求参数 构造 request method requestParam requestBody
     * @return WapiResponse 构造 success code desc data
     */
    private WapiResponse<Object> process(WapiRequest wapiRequest) {
        WapiProcessor processor;
        try {
            processor = new WapiProcessor(wapiRequest, new WapiResponse<>());
        } catch (Exception e) {
            logger.error(">>>>> WapiService工作流程，执行异常", e);
            return new WapiResponse<>().failure(WapiEnumCode.SYSTEM_RUNTIME_ERROR);
        }
        processor.setWapiInterceptors(wapiInterceptors);
        return processor.process();
    }

}
