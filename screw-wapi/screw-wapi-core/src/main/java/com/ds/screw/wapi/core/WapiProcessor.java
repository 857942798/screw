package com.ds.screw.wapi.core;

import com.ds.screw.wapi.core.domain.WapiUnitConst;
import com.ds.screw.wapi.core.domain.WapiUnitParam;
import com.ds.screw.wapi.core.domain.WapiUnitResult;
import com.ds.screw.wapi.core.exception.WapiException;
import com.ds.screw.wapi.core.logs.WapiLog;
import com.ds.screw.wapi.core.logs.WapiLogQueue;
import com.ds.screw.wapi.core.service.WapiResourceService;
import com.ds.screw.wapi.core.unit.WapiAsyncUnit;
import com.ds.screw.wapi.core.unit.WapiExceptionUnit;
import com.ds.screw.wapi.core.unit.WapiGatewayUnit;
import com.ds.screw.wapi.core.unit.WapiTaskUnit;
import com.ds.screw.wapi.core.xml.bean.WapiFlow;
import com.ds.screw.wapi.core.xml.bean.WapiResource;
import com.ds.screw.wapi.core.xml.bean.WapiResult;
import com.ds.screw.wapi.core.xml.bean.WapiUnit;
import com.ds.screw.wapi.core.xml.bean.unit.Gateway;
import com.ds.screw.wapi.core.xml.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * [功能描述]
 *
 * @author dongsheng
 */
public class WapiProcessor {

    private final Logger log = LoggerFactory.getLogger(WapiProcessor.class);

    private final WapiRequest request;

    private final WapiResponse<Object> response;

    private WapiResource resource;

    private List<WapiInterceptor> wapiInterceptors;

    private int interceptorIndex = -1;

    public WapiProcessor(WapiRequest request, WapiResponse<Object> response) {
        this.request = request;
        this.response = response;
        this.resource = prepareWapiResource();
        if (this.resource == null || this.resource.getFlow(this.request.getMethod()) == null) {
            throw new RuntimeException("no wapi flow found!");
        }
    }

    public List<WapiInterceptor> getWapiInterceptors() {
        return wapiInterceptors;
    }

    public void setWapiInterceptors(List<WapiInterceptor> wapiInterceptors) {
        this.wapiInterceptors = wapiInterceptors;
    }

    public WapiResponse<Object> process() {
        processWapi();
        saveWapiLog(this.request, this.response, this.resource);
        return this.response;
    }


    private void processWapi() {
        Exception dispatchException = null;
        // 判断单元执行列表是否为空
        try {
            // 前置处理
            if (!applyPreHandle(this.request, this.response)) {
                return;
            }

            WapiFlow flow = this.resource.getFlow(this.request.getMethod());
            // 执行
            WapiUnitResult wapiUnitResult = this.executeUnit(this.request, flow);
            this.response.of(wapiUnitResult.isSuccess(), wapiUnitResult.getCode(), wapiUnitResult.getDesc(), wapiUnitResult.getData());
            // 后置处理
            applyPostHandle(this.request, this.response);
        } catch (Exception e) {
            dispatchException = e;
            // 异常处理
            this.response.of(handleWapiException(this.request, this.resource, dispatchException));
        }
        triggerAfterCompletion(this.request, this.response, dispatchException);
    }

    /**
     * 获取wapiResource
     *
     * @return wapiResource
     */
    private WapiResource prepareWapiResource() {
        WapiFlow wapiFlow;
        // 查询流程配置
        WapiResourceService wapiResourceService = SpringContextUtil.getBean("wapiResourceService", WapiResourceService.class);
        WapiResource wapiResource = wapiResourceService.getResource(this.request.getResourceKey());
        // 判断单元执行列表是否为空
        if (wapiResource != null) {
            wapiFlow = wapiResource.getFlow(this.request.getMethod());
            if (wapiFlow == null) {
                wapiResource = wapiResourceService.getResource("default_flow");
            }
        } else {
            wapiResource = wapiResourceService.getResource("default_flow");
        }
        return wapiResource;
    }

    /**
     * 根据xml配置的WapiFlow执行Wapi流程
     */
    private WapiUnitResult executeUnit(WapiRequest request, WapiFlow flow) {
        WapiUnit currUnit = getFirstUnit(flow);
        WapiUnitResult wapiUnitResult = new WapiUnitResult();
        WapiUnitParam currUnitParam;
        boolean valid = true;
        while (null != currUnit && valid) {

            // 构建单元参数，仅当前执行单元生效
            currUnitParam = new WapiUnitParam(request, currUnit);
            currUnitParam.setCurrResult(wapiUnitResult);

            // 获取下一步操作步骤序号
            Integer nextOrder = currUnit.getNext();

            // 获取当前单元的单元类型
            String unitType = currUnit.getType().trim();

            if (ObjectUtils.isEmpty(unitType)) {
                continue;
            }

            // 一般节点
            switch (unitType) {
                case WapiUnitConst.UNITTYPE.TASK: {
                    WapiTaskUnit wapiUnit = (WapiTaskUnit) getBean(currUnit.getImpl());
                    wapiUnitResult = wapiUnit.execute(currUnitParam);
                    if (!wapiUnitResult.isSuccess()) {
                        valid = false;
                        break;
                    }
                    break;
                }

                // 异步节点
                case WapiUnitConst.UNITTYPE.ASYNC: {
                    WapiAsyncUnit wapiUnit = (WapiAsyncUnit) getBean(currUnit.getImpl());
                    wapiUnit.setAsyncParam(currUnitParam);
                    WapiAsyncExecutor.getInstance().execute(wapiUnit);
                    break;
                }

                // 分支节点
                case WapiUnitConst.UNITTYPE.GATEWAY:
                    Object bean = getBean(currUnit.getImpl());
                    // 兼容旧版分支节点返回值为序号的问题
                    WapiGatewayUnit wapiUnit = (WapiGatewayUnit) bean;
                    String wapiUnitOrder = wapiUnit.execute(currUnitParam);

                    // 判断结果走向
                    Gateway gateway = (Gateway) currUnit;
                    Map<String, WapiResult> results = gateway.getResults();

                    WapiResult result = results.get(wapiUnitOrder);
                    if (null != result) {
                        nextOrder = result.getThen();
                    }
                    break;
                default:
                    break;
            }

            // 获取下一个执行单元
            if (ObjectUtils.isEmpty(nextOrder)) {
                nextOrder = -1;
            }
            currUnit = getNextUnit(flow, nextOrder);
        }
        return wapiUnitResult;
    }

    /**
     * 获取第一个执行单元
     *
     * @param flow 流程配置
     * @return 第一个执行单元
     */
    private WapiUnit getFirstUnit(WapiFlow flow) {
        List<WapiUnit> list = flow.getUnits();
        return Collections.min(list, (o1, o2) -> {
            int diff = o1.getOrder() - o2.getOrder();
            if (diff > 0) {
                return 1;
            } else if (diff < 0) {
                return -1;
            }
            return 0;
        });
    }

    /**
     * 获取下一个执行单元
     *
     * @param flow      流程配置
     * @param nextOrder 下一个执行单元的order
     * @return 下一个执行单元
     */
    private WapiUnit getNextUnit(WapiFlow flow, Integer nextOrder) {
        List<WapiUnit> list = flow.getUnits();
        if (ObjectUtils.isEmpty(nextOrder)) {
            nextOrder = -1;
        }
        Integer finalNextOrder = nextOrder;
        for (WapiUnit unit : list) {
            if (unit.getOrder().equals(finalNextOrder)) {
                return unit;
            }
        }
        return null;
    }

    /**
     * 获取实现类
     */
    private Object getBean(String bean) {
        try {
            Class<?> impl = Class.forName(bean);
            return SpringContextUtil.getBean(impl);
        } catch (ClassNotFoundException e) {
            return SpringContextUtil.getBean(bean);
        }
    }

    /**
     * Apply preHandle methods of registered interceptors.
     */
    private boolean applyPreHandle(WapiRequest request, WapiResponse<?> response) {
        List<WapiInterceptor> interceptors = getWapiInterceptors();
        if (!CollectionUtils.isEmpty(interceptors)) {
            for (int i = 0; i < interceptors.size(); i++) {
                WapiInterceptor interceptor = interceptors.get(i);
                if (!interceptor.preHandle(request, response)) {
                    triggerAfterCompletion(request, response, null);
                    return false;
                }
                this.interceptorIndex = i;
            }
        }
        return true;
    }

    /**
     * Apply postHandle methods of registered interceptors.
     */
    private void applyPostHandle(WapiRequest request, WapiResponse<?> response) {
        List<WapiInterceptor> interceptors = getWapiInterceptors();
        if (!ObjectUtils.isEmpty(interceptors)) {
            for (int i = interceptors.size() - 1; i >= 0; i--) {
                WapiInterceptor interceptor = interceptors.get(i);
                interceptor.postHandle(request, response);
            }
        }
    }

    private void triggerAfterCompletion(WapiRequest request, WapiResponse<?> response, Exception e) {
        List<WapiInterceptor> interceptors = getWapiInterceptors();
        if (!ObjectUtils.isEmpty(interceptors)) {
            for (int i = this.interceptorIndex; i >= 0; i--) {
                WapiInterceptor interceptor = interceptors.get(i);
                try {
                    interceptor.afterCompletion(request, response, this, e);
                } catch (Throwable ex2) {
                    ex2.printStackTrace();
                }
            }
        }
    }

    private WapiResponse<?> handleWapiException(WapiRequest request, WapiResource resource, Exception exception) {
        // 主动抛出的异常
        WapiFlow flow = resource.getFlow(request.getMethod());
        String exc = flow.getException();
        if (!ObjectUtils.isEmpty(exc)) {
            Object bean = getBean(flow.getException());
            WapiExceptionUnit handler = (WapiExceptionUnit) bean;
            WapiUnitResult wapiUnitResult = handler.execute(exception);
            return new WapiResponse<>().failure(wapiUnitResult.getCode(), wapiUnitResult.getDesc(), wapiUnitResult.getData());
        } else {
            if (exception instanceof WapiException) {
                log.warn(">>>>> Wapi流程处理：{}", exception.getMessage());
                return new WapiResponse<>().failure((WapiException) exception);
            } else {
                log.error(">>>>> Wapi流程处理异常：{}", exception.getMessage(), exception);
                // 禁止输出数据库执行异常到前端
                return new WapiResponse<>().failure();
            }
        }
    }


    private void saveWapiLog(WapiRequest request, WapiResponse<?> response, WapiResource resource) {
        // 记录日志
        try {
            WapiFlow flow = resource.getFlow(request.getMethod());
            if (flow == null) {
                return;
            }
            // 日志加入wapi日志队列
            // 创建日志数据对象
            WapiLog wapiLog = new WapiLog();
            wapiLog.setUserId((request.getUserId() == null || request.getUserId().length() == 0) ? "system" : request.getUserId());
            wapiLog.setResourceName(resource.getName());
            wapiLog.setResourceKey(resource.getKey());
            wapiLog.setFlowName(flow.getName());
            wapiLog.setFlowType(flow.getType());
            wapiLog.setRequestBody(request.getSrcRequestBody());
            wapiLog.setRequestParam(request.getSrcRequestParam());
            wapiLog.setResponse(response);
            wapiLog.setLocalIp(request.getRequest() == null ? "127.0.0.1" : request.getRequest().getLocalAddr());
            wapiLog.setOpDate(new Date());
            // 加入日志队列
            WapiLogQueue wapiLogQueue = (WapiLogQueue) SpringContextUtil.getBean("wapiLogQueue");
            boolean result = wapiLogQueue.offer(wapiLog);
            if (!result) {
                log.error(">>>>> Wapi流程日志队列已满！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}