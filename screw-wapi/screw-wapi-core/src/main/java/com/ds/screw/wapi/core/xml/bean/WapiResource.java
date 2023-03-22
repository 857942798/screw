package com.ds.screw.wapi.core.xml.bean;


import com.ds.screw.wapi.core.xml.bean.flow.DELETE;
import com.ds.screw.wapi.core.xml.bean.flow.GET;
import com.ds.screw.wapi.core.xml.bean.flow.POST;
import com.ds.screw.wapi.core.xml.bean.flow.PUT;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
public class WapiResource implements Serializable {

    @XmlAttribute
    private String key;

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String desc;

    @XmlAttribute
    private Boolean protect;

    @XmlAttribute
    private Boolean overwrite;


    @XmlElements(value = {
            @XmlElement(name = "get", type = GET.class),
            @XmlElement(name = "post", type = POST.class),
            @XmlElement(name = "put", type = PUT.class),
            @XmlElement(name = "delete", type = DELETE.class)
    })
    private List<WapiFlow> flows = new ArrayList<>();

    public WapiResource() {
    }

    public WapiResource(String key, String name, String desc, List<WapiFlow> flows) {
        this.key = key;
        this.name = name;
        this.desc = desc;
        this.flows = flows;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Boolean getProtect() {
        return protect;
    }

    public Boolean getOverwrite() {
        return overwrite;
    }


    public List<WapiFlow> getFlows() {
        return flows;
    }


    public WapiFlow getFlow(String type) {
        return flows.stream().filter(item -> item.getType().equals(type)).findFirst().orElse(null);
    }
}
