package com.ds.screw.wapi.core.xml.bean.unit;


import com.ds.screw.wapi.core.xml.bean.WapiUnit;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "task")
public class Task extends WapiUnit {

    public Task() {
    }

    public String getType() {
        return "task";
    }
}
