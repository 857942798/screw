package com.ds.screw.wapi.core.xml.adapter;


import com.ds.screw.wapi.core.xml.bean.Results;
import com.ds.screw.wapi.core.xml.bean.WapiResult;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.Map;

public class ResultMapAdapter extends XmlAdapter<Results, Map<String, WapiResult>> {

    @Override
    public Map<String, WapiResult> unmarshal(Results v) throws Exception {
        Map<String, WapiResult> maps = new HashMap<>();
        for (WapiResult p : v.getResult()) {
            maps.put(p.getWhen(), p);
        }
        return maps;
    }

    @Override
    public Results marshal(Map<String, WapiResult> v) throws Exception {
        if (null != v) {
            Results results = new Results();
            for (Map.Entry<String, WapiResult> entry : v.entrySet()) {
                results.getResult().add(entry.getValue());
            }
            return results;
        }
        return null;
    }

}
