package com.ds.screw.wapi.core.xml.adapter;


import com.ds.screw.wapi.core.xml.bean.Params;
import com.ds.screw.wapi.core.xml.bean.WapiParam;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.Map;

public class ParamMapAdapter extends XmlAdapter<Params, Map<String, WapiParam>> {

    @Override
    public Map<String, WapiParam> unmarshal(Params v) throws Exception {
        Map<String, WapiParam> maps = new HashMap<>();
        for (WapiParam p : v.getParam()) {
            maps.put(p.getName(), p);
        }
        return maps;
    }

    @Override
    public Params marshal(Map<String, WapiParam> v) throws Exception {
        if (null != v) {
            Params params = new Params();
            for (Map.Entry<String, WapiParam> entry : v.entrySet()) {
                params.getParam().add(entry.getValue());
            }
            return params;
        }
        return null;
    }


}
