package com.ds.screw.auth.exception;

/**
 * <p>
 *      异常：代表 JSON 转换失败
 * </p>
 *
 * @author dongsheng
 */
public class AuthJsonConvertException extends AuthException {

	/**
	 * 序列化版本号
	 */
	private static final long serialVersionUID = 6806129545290134144L;

	/**
	 * 一个异常：代表 JSON 转换失败
	 * @param cause 异常对象
	 */
	public AuthJsonConvertException(Throwable cause) {
		super(cause);
	}

}
