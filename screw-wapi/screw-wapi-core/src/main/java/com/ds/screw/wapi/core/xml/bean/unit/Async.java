package com.ds.screw.wapi.core.xml.bean.unit;


import com.ds.screw.wapi.core.xml.bean.WapiUnit;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "async")
public class Async extends WapiUnit {

    public Async() {
    }

    public String getType() {
        return "async";
    }
}
