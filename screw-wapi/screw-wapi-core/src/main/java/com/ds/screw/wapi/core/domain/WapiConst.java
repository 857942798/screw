package com.ds.screw.wapi.core.domain;

/**
 *
 * Wapi枚举类
 *
 * @author dongsheng
 */
public class WapiConst {
    public enum METHOD {
        GET("get"),
        POST("post"),
        PUT("put"),
        DELETE("delete");
        private String name;

        METHOD(String method) {
            this.name = method;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static final String LOGIN_ID = "login_id";

}
