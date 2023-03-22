package com.ds.screw.wapi.core.properties;

import org.springframework.beans.factory.annotation.Value;

public class WapiProperties {

    @Value("${config.dbconfig.loadorder:}")
    private String dbRouterLoadOrder = "";

    // 指定jar包中的所有流程配置按照模块名指定排序加载
    @Value("${config.wapiconfig.loadorder:}")
    private String wapiConfigLoadOrder = "";

    public String getDbRouterLoadOrder() {
        return "csmf-wapi-db-router-config.xml,csmf-wapi-migrate-db-router-config.xml,mgnt-db-router-config.xml," + dbRouterLoadOrder;
    }

    public void setDbRouterLoadOrder(String dbRouterLoadOrder) {
        this.dbRouterLoadOrder = dbRouterLoadOrder;
    }

    public String getWapiConfigLoadOrder() {
        return "csmf-wapi-core,csmf-wapi-business,csmf-wapi-migrate,csmf-monitor-wapi," + wapiConfigLoadOrder;
    }

    public void setWapiConfigLoadOrder(String wapiConfigLoadOrder) {
        this.wapiConfigLoadOrder = wapiConfigLoadOrder;
    }
}
