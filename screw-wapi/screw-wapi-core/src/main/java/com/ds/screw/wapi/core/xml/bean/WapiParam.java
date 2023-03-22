package com.ds.screw.wapi.core.xml.bean;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import java.io.Serializable;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.NONE)
public class WapiParam implements Serializable {
    @XmlAttribute
    private String name;
    @XmlValue
    private String value;

    public WapiParam() {
    }

    public WapiParam(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WapiParam wapiParam = (WapiParam) o;
        if (Objects.equals(name, wapiParam.name) && Objects.equals(StringUtils.replaceBlank(value), StringUtils.replaceBlank(wapiParam.value))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
