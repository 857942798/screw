package com.ds.screw.auth.sso.exception;


import com.ds.screw.auth.exception.AuthException;
import com.ds.screw.auth.sso.constant.SsoEnumCode;

/**
 * 异常：代表 SSO 认证流程错误
 *
 * @author dongsheng
 */
public class SsoException extends AuthException {

	/**
	 * 序列化版本号
	 */
	private static final long serialVersionUID = 6806121545290130114L;

	/**
	 * 一个异常：代表 SSO 认证流程错误
	 * @param message 异常描述
	 */
	public SsoException(String message) {
		super(message);
	}


	/**
	 * 一个异常：代表 SSO 认证流程错误
	 * @param csmfSsoEnumCode sso异常枚举
	 */
	public SsoException(SsoEnumCode csmfSsoEnumCode) {
		super(csmfSsoEnumCode.getCode(),csmfSsoEnumCode.getDesc());
	}

	/**
	 * 如果flag==true，则抛出message异常 
	 * @param flag 标记
	 * @param message 异常信息 
	 */
	public static void throwBy(boolean flag, String message) {
		if(flag) {
			throw new SsoException(message);
		}
	}
	
}
