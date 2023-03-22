package com.ds.screw.wapi.core.exception;

/**
 * @author: dongsheng
 */
public class WapiNotOverwriteResourceKeyException extends WapiException {
    public WapiNotOverwriteResourceKeyException() {
        super();
    }

    public WapiNotOverwriteResourceKeyException(String message) {
        super(message);
    }

    public WapiNotOverwriteResourceKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public WapiNotOverwriteResourceKeyException(Throwable cause) {
        super(cause);
    }
}
