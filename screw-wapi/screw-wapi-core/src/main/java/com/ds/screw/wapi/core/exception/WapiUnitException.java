package com.ds.screw.wapi.core.exception;


import com.ds.screw.wapi.core.domain.WapiUnitResult;

/**
 * @author: dongsheng
 */
public class WapiUnitException extends WapiException {

    private WapiUnitResult result;

    public WapiUnitException(String message) {
        super(message);
    }

    public WapiUnitException(WapiUnitResult result) {
        super(result.getDesc());
        this.result = result;
    }

    public WapiUnitResult getResult() {
        return result;
    }

    public void setResult(WapiUnitResult result) {
        this.result = result;
    }
}
