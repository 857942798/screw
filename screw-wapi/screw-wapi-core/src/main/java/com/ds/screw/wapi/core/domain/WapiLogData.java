package com.ds.screw.wapi.core.domain;

/**
 * [功能描述]
 *
 * @author dongsheng
 */
public class WapiLogData {

    String opModule;
    String opType;
    String opDate;
    String opData;
    String opUser;
    String opResult;


    public String getOpModule() {
        return opModule;
    }

    public void setOpModule(String opModule) {
        this.opModule = opModule;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getOpDate() {
        return opDate;
    }

    public void setOpDate(String opDate) {
        this.opDate = opDate;
    }

    public String getOpData() {
        return opData;
    }

    public void setOpData(String opData) {
        this.opData = opData;
    }

    public String getOpUser() {
        return opUser;
    }

    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }

    public String getOpResult() {
        return opResult;
    }

    public void setOpResult(String opResult) {
        this.opResult = opResult;
    }
}
