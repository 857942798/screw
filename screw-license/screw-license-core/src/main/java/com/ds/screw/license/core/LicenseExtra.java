package com.ds.screw.license.core;

import de.schlichtherle.license.LicenseContentException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义需要校验的License参数
 *
 * @author dongsheng
 */
public class LicenseExtra implements Serializable {

    private static final long serialVersionUID = 8600137500316662317L;

    private ServerData serverData = null;

    @SuppressWarnings("all")
    private List<FeatureData> featureDataList = new ArrayList<>();

    public ServerData getServerData() {
        return serverData;
    }

    public void setServerData(ServerData serverInfo) {
        this.serverData = serverInfo;
    }

    public List<FeatureData> getFeatureList() {
        return featureDataList;
    }

    public void setFeatureList(List<FeatureData> featureDataList) {
        this.featureDataList = featureDataList;
    }

    public LicenseExtra addFeatureData(FeatureData featureData) {
        this.featureDataList.add(featureData);
        return this;
    }

    public void validateFeature() throws LicenseContentException {
        if (featureDataList != null) {
            for (FeatureData featureData : featureDataList) {
                for (FeatureData featureData1 : featureDataList) {
                    if (featureData != featureData1 && featureData.getName().equals(featureData1.getName())) {
                        throw new LicenseContentException("禁止使用重复的FeatureInfoName");
                    }
                }
            }
        }
    }
}
