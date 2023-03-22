package com.ds.screw.wapi.core.service;


import com.ds.screw.wapi.core.WapiRequest;
import com.ds.screw.wapi.core.WapiResponse;


/***
 *  Wapi请求专用接口类
 *
 * @author dongsheng
 */
public interface WapiService {


    /**
     * get类型请求，查询对应数据
     *
     * @param wapiRequest request请求参数
     * @return WapiResult
     */
    WapiResponse<Object> get(WapiRequest wapiRequest);

    /**
     * post 类型接口请求，新增对应数据
     *
     * @param wapiRequest request请求参数
     * @return WapiResult
     */
    WapiResponse<Object> post(WapiRequest wapiRequest);

    /**
     * put类型接口请求，更新对应数据
     *
     * @param wapiRequest request请求参数
     * @return WapiResult
     */
    WapiResponse<Object> put(WapiRequest wapiRequest);


    /**
     * delete类型接口请求，删除对应的数据
     *
     * @param wapiRequest request请求参数
     * @return WapiResult
     */
    WapiResponse<Object> delete(WapiRequest wapiRequest);

}
