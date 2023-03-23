package com.ds.screw.wapi.core.xml.bean.flow;


import com.ds.screw.wapi.core.xml.bean.WapiFlow;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "put")
public class PUT extends WapiFlow {

    public String getType() {
        return "put";
    }
}
