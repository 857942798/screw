package com.ds.screw.wapi.core.xml.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * xml无法直接转换map，需要该类作为中转
 *
 * @author: dongsheng
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Params {
    private List<WapiParam> param = new ArrayList<>();

    @XmlElement(name = "param")
    public List<WapiParam> getParam() {
        return param;
    }

    public void setParam(List<WapiParam> param) {
        this.param = param;
    }
}
