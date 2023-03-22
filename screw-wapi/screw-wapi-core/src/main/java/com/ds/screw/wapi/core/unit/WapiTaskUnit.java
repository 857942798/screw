package com.ds.screw.wapi.core.unit;


import com.ds.screw.wapi.core.domain.WapiUnitParam;
import com.ds.screw.wapi.core.domain.WapiUnitResult;

public interface WapiTaskUnit {

    WapiUnitResult execute(WapiUnitParam param);
}
