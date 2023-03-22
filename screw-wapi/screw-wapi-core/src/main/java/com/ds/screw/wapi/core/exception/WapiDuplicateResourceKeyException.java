package com.ds.screw.wapi.core.exception;

/**
 * @author: dongsheng
 */
public class WapiDuplicateResourceKeyException extends WapiException {
    public WapiDuplicateResourceKeyException() {
        super();
    }

    public WapiDuplicateResourceKeyException(String message) {
        super(message);
    }

    public WapiDuplicateResourceKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public WapiDuplicateResourceKeyException(Throwable cause) {
        super(cause);
    }
}
