package com.ds.screw.auth.exception;

/**
 * <p>
 *      登录失败异常
 * </p>
 *
 * @author dongsheng
 */
public class LogoutFailedException extends AuthException {

    public LogoutFailedException() {
        super("logout failed");
    }
}

