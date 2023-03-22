package com.ds.screw.wapi.core.xml.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * xml无法直接转换map，需要该类作为中转
 *
 * @author: dongsheng
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Results {
    private List<WapiResult> result = new ArrayList<>();

    @XmlElement(name = "result")
    public List<WapiResult> getResult() {
        return result;
    }

    public void setResult(List<WapiResult> result) {
        this.result = result;
    }

}
