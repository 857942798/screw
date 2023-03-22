package com.ds.screw.license.core;

import java.util.HashMap;
import java.util.Map;

public class FeatureData {

    private String name;
    private Map<String, String> param;

    public FeatureData() {
    }

    public FeatureData(String name, Map<String, String> param) {
        this.name = name;
        this.param = param;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }

    public static FeatureDataBuilder builder() {
        return new FeatureDataBuilder();
    }

    public static final class FeatureDataBuilder {
        private String name;
        private Map<String, String> param = new HashMap<>();

        private FeatureDataBuilder() {
        }

        public FeatureDataBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public FeatureDataBuilder withParam(Map<String, String> param) {
            this.param = param;
            return this;
        }

        public FeatureDataBuilder addParam(String key, String value) {
            this.param.put(key, value);
            return this;
        }

        public FeatureData build() {
            return new FeatureData(name, param);
        }
    }

    @Override
    public String toString() {
        return "FeatureData{" +
                "name='" + name + '\'' +
                ", param=" + param +
                '}';
    }
}
