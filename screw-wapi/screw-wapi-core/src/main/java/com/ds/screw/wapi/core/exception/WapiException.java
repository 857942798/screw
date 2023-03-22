package com.ds.screw.wapi.core.exception;


import com.ds.screw.wapi.core.domain.WapiEnumCode;
/**
 * @author: dongsheng
 */
public class WapiException extends RuntimeException{
    private final String code;

    public WapiException() {
        super(WapiEnumCode.SYSTEM_RUNTIME_ERROR.getDesc());
        this.code = WapiEnumCode.SYSTEM_RUNTIME_ERROR.getCode();
    }

    public WapiException(String message) {
        this(WapiEnumCode.SYSTEM_RUNTIME_ERROR.getCode(), message);
    }

    public WapiException(String code, String message) {
        super(message);
        this.code = code;
    }

    public WapiException(WapiEnumCode wapiEnumCode) {
        super(wapiEnumCode.getDesc());
        this.code = wapiEnumCode.getCode();
    }

    public WapiException(Throwable cause) {
        super(cause);
        if (cause instanceof RuntimeException) {
            this.code = WapiEnumCode.SYSTEM_RUNTIME_ERROR.getCode();
        } else {
            this.code = WapiEnumCode.SYSTEM_ERROR.getCode();
        }
    }

    public WapiException(String message, Throwable cause) {
        super(message, cause);
        this.code = WapiEnumCode.SYSTEM_RUNTIME_ERROR.getCode();
    }

    public String getCode() {
        return code;
    }
}
