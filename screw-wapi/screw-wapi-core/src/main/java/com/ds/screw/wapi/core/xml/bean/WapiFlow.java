package com.ds.screw.wapi.core.xml.bean;

import com.ds.screw.wapi.core.xml.bean.flow.DELETE;
import com.ds.screw.wapi.core.xml.bean.flow.GET;
import com.ds.screw.wapi.core.xml.bean.flow.POST;
import com.ds.screw.wapi.core.xml.bean.flow.PUT;
import com.ds.screw.wapi.core.xml.bean.unit.Gateway;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
public abstract class WapiFlow implements Serializable {

    @XmlAttribute
    private String name;
    @XmlAttribute
    private String license;
    @XmlAttribute
    private String desc;
    @XmlAttribute
    private String exception;
    @XmlElements(value = {
            @XmlElement(name = "task", type = Task.class),
            @XmlElement(name = "async", type = Async.class),
            @XmlElement(name = "gateway", type = Gateway.class),
    })
    private List<WapiUnit> units = new ArrayList<>();

    public WapiFlow() {
    }

    public WapiFlow(String name, String license, String desc, List<WapiUnit> units) {
        this.name = name;
        this.license = license;
        this.desc = desc;
        this.units = units;
    }

    public static WapiFlow build(String type, String name, String license, String desc, List<WapiUnit> units) {
        WapiFlow flow;
        switch (type.toLowerCase()) {
            case "post":
                flow = new POST();
                break;
            case "put":
                flow = new PUT();
                break;
            case "delete":
                flow = new DELETE();
                break;
            default:
                flow = new GET();
                break;

        }
        flow.name = name;
        flow.license = license;
        flow.desc = desc;
        flow.units = units;
        return flow;
    }

    public abstract String getType();

    public String getName() {
        return name;
    }

    public String getLicense() {
        return license;
    }

    public String getException() {
        return exception;
    }

    public String getDesc() {
        return desc;
    }

    public List<WapiUnit> getUnits() {
        return units;
    }
}
