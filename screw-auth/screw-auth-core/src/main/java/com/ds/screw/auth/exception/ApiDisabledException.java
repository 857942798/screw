package com.ds.screw.auth.exception;

/**
 * <p>
 *      异常：缺少 api 的配置
 * </p>
 *
 * @author dongsheng
 */
public class ApiDisabledException extends AuthException {

	/**
	 * 序列化版本号
	 */
	private static final long serialVersionUID = 6805129545290134144L;

	public ApiDisabledException(Throwable cause) {
		super(cause);
	}

	public ApiDisabledException(String msg) {
		super(msg);
	}

}
