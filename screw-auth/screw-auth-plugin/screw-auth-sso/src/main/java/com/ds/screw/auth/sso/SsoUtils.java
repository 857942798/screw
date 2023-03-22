package com.ds.screw.auth.sso;

import com.ds.screw.auth.sso.domain.SsoResult;

import java.util.Map;

/**
 * <p>
 *      单点登录模块 工具类
 * </p>
 *
 * @author dongsheng
 */
public class SsoUtils {
	private SsoUtils(){}

	/**
	 * 底层 SsoTemplate 对象
	 */
	public static final SsoTemplate ssoTemplate = SsoManager.getSsoTemplate();

	// -------------------客户端 请求相关 -------------------
	/**
	 * 单点登录，向服务端同步单点登录信息
	 */
	public static SsoResult ssoSyncLogin(Map<String, String> headers) {
		return ssoTemplate.ssoSyncLogin(headers);
	}

	/**
	 * 根据向服务端申请一个ticket
	 */
	public static SsoResult applySsoTicket() {
		return ssoTemplate.applySsoTicket();
	}

	/**
	 * SSO-Client端：从请求头中获取ticket后，调用服务端接口校验该ticket的有效性<br/>
	 */
	public static SsoResult ssoCheckTicket(String ticket) {
		return ssoTemplate.ssoCheckTicket(ticket);
	}

	/**
	 * 向服务端发送单点登出请求
	 * @return 构建完毕的URL
	 */
	public static SsoResult ssoLogout() {
		return ssoTemplate.ssoLogout();
	}

	// -------------------服务端 请求相关 -------------------

	/**
	 * 服务端调用客户端的登出接口
	 * @return 构建完毕的URL
	 */
	public static SsoResult callClientLogout(String url, String ssoAccount) {
		return ssoTemplate.callClientLogout(url,ssoAccount);
	}
}
