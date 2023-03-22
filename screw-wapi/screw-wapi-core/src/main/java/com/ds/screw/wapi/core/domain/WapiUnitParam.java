package com.ds.screw.wapi.core.domain;


import com.ds.screw.wapi.core.WapiRequest;
import com.ds.screw.wapi.core.xml.bean.WapiUnit;

public class WapiUnitParam {

    private final WapiRequest wapiRequest;

    private final WapiUnit unit;

    private WapiUnitResult currResult;

    public WapiUnitParam(WapiRequest request, WapiUnit unit) {
        this.wapiRequest = request;
        this.unit = unit;
    }

    public WapiRequest getWapiRequest() {
        return wapiRequest;
    }

    public WapiUnit getUnit() {
        return unit;
    }

    public WapiUnitResult getCurrResult() {
        return currResult;
    }

    public void setCurrResult(WapiUnitResult currResult) {
        this.currResult = currResult;
    }
}
