package com.ds.screw.wapi.core.xml.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.NONE)
public class WapiResult implements Serializable {
    @XmlAttribute
    private String when;
    @XmlAttribute
    private Integer then;

    public WapiResult() {
    }

    public WapiResult(String when, Integer then) {
        this.when = when;
        this.then = then;
    }

    public String getWhen() {
        return when;
    }

    public Integer getThen() {
        return then;
    }
}
