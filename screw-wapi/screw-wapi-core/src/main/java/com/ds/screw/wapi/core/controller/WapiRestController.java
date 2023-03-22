package com.ds.screw.wapi.core.controller;

import com.ds.screw.wapi.core.WapiRequest;
import com.ds.screw.wapi.core.WapiResponse;
import com.ds.screw.wapi.core.domain.WapiConst;
import com.ds.screw.wapi.core.domain.WapiEnumCode;
import com.ds.screw.wapi.core.exception.WapiException;
import com.ds.screw.wapi.core.exception.WapiFlowNotFoundException;
import com.ds.screw.wapi.core.exception.WapiUnsupportedActionException;
import com.ds.screw.wapi.core.service.WapiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/***
 *  Wapi 请求控制器
 *
 * @author yotasky
 * @date 2022/8/29 10:46
 */
@RestController
@RequestMapping("/wapi")
public class WapiRestController {

    private final Logger log = LoggerFactory.getLogger(WapiRestController.class);

    private WapiService wapiService;

    @Autowired
    private void setWapiService(WapiService wapiService) {
        this.wapiService = wapiService;
    }

    /**
     * <h1>Wapi GET 请求</h1>
     * <p>
     * case1:/wapi/resourceKey
     * case2:/wapi/resourceKey^get
     * case3:/wapi/resourceKey^get/id
     * case4:/wapi/resourceKey/get
     * case5:/wapi/resourceKey/get/id
     * </p>
     *
     * <h1>Wapi POST 请求</h1>
     * <p>
     * case1:/wapi/resourceKey
     * case2:/wapi/resourceKey^post
     * case3:/wapi/resourceKey/post
     * </p>
     *
     * <h1>Wapi PUT 请求</h1>
     * <p>
     * case1:/wapi/resourceKey^put/id
     * case2:/wapi/resourceKey/put/id
     * </p>
     *
     * <h1>Wapi DELETE 请求</h1>
     * <p>
     * case1:/wapi/resourceKey^delete/id
     * case2:/wapi/resourceKey/delete/id
     * </p>
     */
    @RequestMapping(
            path = {

                    /*get、post*/
                    "/{resourceKey:^[\\w]+$}",

                    /*get*/
                    "/{resourceKey:^[\\w]+\\^(?i)get$}",
                    "/{resourceKey:^[\\w]+\\^(?i)get$}/{id}",
                    "/{resourceKey:^[\\w]+$}/{action:(?i)get$}",
                    "/{resourceKey:^[\\w]+$}/{action:(?i)get$}/{id}",

                    /*post*/
                    "/{resourceKey:^[\\w]+\\^(?i)post$}",
                    "/{resourceKey:^[\\w]+$}/{action:(?i)post$}",

                    /*put*/
                    "/{resourceKey:^[\\w]+\\^(?i)put$}/{id}",
                    "/{resourceKey:^[\\w]+$}/{action:(?i)put$}/{id}",

                    /*delete*/
                    "/{resourceKey:^[\\w]+\\^(?i)delete$}/{id}",
                    "/{resourceKey:^[\\w]+$}/{action:(?i)delete$}/{id}"
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            },
            method = {
                    RequestMethod.GET,
                    RequestMethod.POST
            }
    )
    public WapiResponse<Object> wapi(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam Map<String, Object> requestParam,
                                     @RequestBody(required = false) String requestBody,
                                     @PathVariable(required = true) String resourceKey,
                                     @PathVariable(required = false) String action,
                                     @PathVariable(required = false) String id) {
        String key = resourceKey;
        // 兼容旧版 wapi 1.0
        if (key.contains("^")) {
            key = resourceKey.substring(0, resourceKey.indexOf("^"));
            action = resourceKey.substring(resourceKey.indexOf("^") + 1);
        } else {
            if (ObjectUtils.isEmpty(action)) {
                action = request.getMethod().toLowerCase();
            }
        }

        try {

            // 验证是否有效的请求方法，put/post/delete 操作对应post请求，get操作对应get请求
            if (!isValidMethod(action, request.getMethod())) {
                throw new WapiUnsupportedActionException();
            }

            action = action.toLowerCase();

            Map<String, Object> reqb = new HashMap<>();

            if (null != requestBody) {
                reqb =new ObjectMapper().readValue(requestBody, Map.class);
            }

            requestParam = requestParam == null ? new HashMap<>() : requestParam;

            // 构造请求参数
            WapiRequest wapiRequest = WapiRequest.builder()
                    .request(request)
                    .method(action)
                    .resourceKey(key)
                    .requestParam(requestParam)
                    .requestBody(reqb)
                    .dataId(id)
                    .build();

            if (action.equalsIgnoreCase(WapiConst.METHOD.GET.name())) {
                return wapiService.get(wapiRequest);
            } else if (action.equalsIgnoreCase(WapiConst.METHOD.POST.name())) {
                return wapiService.post(wapiRequest);
            } else if (action.equalsIgnoreCase(WapiConst.METHOD.PUT.name())) {
                return wapiService.put(wapiRequest);
            } else if (action.equalsIgnoreCase(WapiConst.METHOD.DELETE.name())) {
                return wapiService.delete(wapiRequest);
            } else {
                throw new WapiUnsupportedActionException();
            }
        } catch (WapiFlowNotFoundException | WapiUnsupportedActionException e) {
            log.error(">>>>> WapiV2RestController error: {}", e.getMessage());
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return null;
            } catch (IOException e1) {
                return new WapiResponse<>().failure(WapiEnumCode.SYSTEM_RUNTIME_ERROR);
            }
        } catch (WapiException e) {
            log.error(">>>>> WapiV2RestController error: {}", e.getMessage());
            return new WapiResponse<>().failure(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 验证是否有效的wapi请求
     *
     * @param action wapi action
     * @param method http method
     * @return 是否有效
     */
    private boolean isValidMethod(String action, String method) {
        action = action.toLowerCase();
        if (ObjectUtils.isEmpty(action)) {
            return false;
        }
        if (action.equalsIgnoreCase(WapiConst.METHOD.GET.name())) {
            return method.equalsIgnoreCase(action);
        } else if (action.equalsIgnoreCase(WapiConst.METHOD.POST.name())
                || action.equalsIgnoreCase(WapiConst.METHOD.PUT.name())
                || action.equalsIgnoreCase(WapiConst.METHOD.DELETE.name())) {
            return method.equalsIgnoreCase(HttpMethod.POST.name());
        } else {
            return false;
        }
    }
}
