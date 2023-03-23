package com.ds.screw.wapi.core.xml.bean;

import com.ds.screw.wapi.core.xml.adapter.ParamMapAdapter;
import com.ds.screw.wapi.core.xml.bean.unit.Async;
import com.ds.screw.wapi.core.xml.bean.unit.Gateway;
import com.ds.screw.wapi.core.xml.bean.unit.Task;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@XmlAccessorType(value = XmlAccessType.NONE)
public abstract class WapiUnit implements Serializable {
    @XmlAttribute
    private Integer order;
    @XmlAttribute
    private Integer next;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String impl;
    @XmlJavaTypeAdapter(ParamMapAdapter.class)
    @XmlElement(name = "params")
    private Map<String, WapiParam> params = new HashMap<>();

    public WapiUnit() {
    }

    public WapiUnit(Integer order, Integer next, String name, String impl, Map<String, WapiParam> params) {
        this.order = order;
        this.next = next;
        this.name = name;
        this.impl = impl;
        this.params = params;
    }

    public static WapiUnit build(String type, Integer order, Integer next, String name, String impl, Map<String, WapiParam> params) {
        WapiUnit unit;
        switch (type) {
            case "async":
            case "asyncTask":
                unit = new Async();
                break;
            case "gateway":
            case "branch":
                unit = new Gateway();
                break;
            default:
                unit = new Task();
                break;
        }
        unit.impl = impl;
        unit.order = order;
        unit.next = next;
        unit.name = name;
        unit.params = params;
        return unit;
    }

    public abstract String getType();

    public Integer getOrder() {
        return order;
    }

    public Integer getNext() {
        return next;
    }

    public String getName() {
        return name;
    }

    public String getImpl() {
        return impl;
    }

    public Map<String, WapiParam> getParams() {
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WapiUnit wapiUnit = (WapiUnit) o;
        if (order.equals(wapiUnit.order) && next.equals(wapiUnit.next) && impl.equals(wapiUnit.impl)) {
            // 继续比较
            if (params.size() != ((WapiUnit) o).getParams().size()) {
                return false;
            }
            for (String key : params.keySet()) {
                if (wapiUnit.getParams().get(key) == null || !wapiUnit.getParams().get(key).equals(params.get(key))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, next, impl, params);
    }
}
