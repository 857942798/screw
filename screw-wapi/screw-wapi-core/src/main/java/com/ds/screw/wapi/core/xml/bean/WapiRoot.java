package com.ds.screw.wapi.core.xml.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class WapiRoot implements Serializable {

    @XmlElement(name = "resource")
    private List<WapiResource> wapiResources;

    public WapiRoot() {
    }

    public WapiRoot(List<WapiResource> wapiResources) {
        this.wapiResources = wapiResources;
    }

    public List<WapiResource> getResources() {
        return wapiResources;
    }
}
