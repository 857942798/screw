package com.ds.screw.wapi.core.xml.bean.unit;

import com.ds.screw.wapi.core.xml.adapter.ResultMapAdapter;
import com.ds.screw.wapi.core.xml.bean.WapiResult;
import com.ds.screw.wapi.core.xml.bean.WapiUnit;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;
import java.util.Map;

@XmlType(name = "gateway")
public class Gateway extends WapiUnit {

    @XmlJavaTypeAdapter(ResultMapAdapter.class)
    @XmlElement(name = "results")
    private Map<String, WapiResult> results = new HashMap<>();

    public Gateway() {
    }

    public Gateway(Integer order, Integer next, String name, String impl, Map<String, WapiParam> params, Map<String, WapiResult> results) {
        super(order, next, name, impl, params);
        this.results = results;
    }

    public String getType() {
        return "gateway";
    }

    public Map<String, WapiResult> getResults() {
        return results;
    }
}
