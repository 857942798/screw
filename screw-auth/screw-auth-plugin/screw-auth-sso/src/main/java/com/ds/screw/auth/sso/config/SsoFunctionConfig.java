package com.ds.screw.auth.sso.config;

import com.ds.screw.auth.sso.exception.SsoException;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * <p>
 *      SSO 单点登录模块 回调函数配置类
 * </p>
 *
 * @author dongsheng
 */
public class SsoFunctionConfig {

	// -------------------- SaSsoHandle 所有回调函数 --------------------
	/**
	 * SSO-Client/server端：发送Http 请求的处理函数
	 */
	private BiFunction<String, Map<String,String>, String> sendHttp = (url,headers) -> {
		throw new SsoException("请配置 Http 请求处理器");
	};

	public BiFunction<String, Map<String, String>, String> getSendHttp() {
		return sendHttp;
	}

	public SsoFunctionConfig setSendHttp(BiFunction<String, Map<String, String>, String> sendHttp) {
		this.sendHttp = sendHttp;
		return this;
	}
}
