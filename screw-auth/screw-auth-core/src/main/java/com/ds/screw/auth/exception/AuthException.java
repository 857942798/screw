package com.ds.screw.auth.exception;

/**
 * <p>
 *      认证模块的顶层异常
 * </p>
 *
 * @author dongsheng
 */
public class AuthException extends RuntimeException {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 5147350318730264928L;

    /**
     * 异常细分状态码
     */
    private String code;

    /**
     * 构建一个异常
     *
     * @param message 异常描述信息
     */
    public AuthException(String message) {
        super(message);
        this.code = AuthExceptionCode.CODE_UNDEFINED;
    }

    /**
     * 构建一个异常
     *
     * @param code    异常细分状态码
     * @param message 异常信息
     */
    public AuthException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构建一个异常
     *
     * @param cause 异常对象
     */
    public AuthException(Throwable cause) {
        super(cause);
        this.code = AuthExceptionCode.CODE_UNDEFINED;
    }

    /**
     * 构建一个异常
     *
     * @param message 异常信息
     * @param cause   异常对象
     */
    public AuthException(String message, Throwable cause) {
        super(message, cause);
        this.code = AuthExceptionCode.CODE_UNDEFINED;
    }

    /**
     * 获取异常细分状态码
     *
     * @return 异常细分状态码
     */
    public String getCode() {
        return code;
    }

    /**
     * 写入异常细分状态码
     * @param code 异常细分状态码
     * @return 对象自身
     */
    public AuthException setCode(String code) {
        this.code = code;
        return this;
    }
}
