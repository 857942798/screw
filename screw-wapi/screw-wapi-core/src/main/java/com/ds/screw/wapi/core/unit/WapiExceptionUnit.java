package com.ds.screw.wapi.core.unit;


import com.ds.screw.wapi.core.domain.WapiUnitResult;

public interface WapiExceptionUnit {
    WapiUnitResult execute(Exception e);
}
