package com.ds.screw.auth.sso.constant;

/**
 * <p>
 *      SSO模块相关常量
 * </p>
 *
 * @author dongsheng
 */
public class SsoConsts {

	private SsoConsts(){

	}

	/**
	 * 所有API接口 
	 * @author kong 
	 */
	public static final class Api {

		private Api(){

		}

		/** SSO-Server端：单点登录，客户端向服务端同步登录信息 */
		public static final String SSO_SYNC_LOGIN = "/auth/sso/server/ssoSyncLogin";
		/** SSO-Server端：校验ticket 获取账号id */
		public static final String SSO_CHECK_TICKET = "/auth/sso/server/checkTicket";
		/** SSO-Server端：ticket下发 */
		public static final String SSO_SERVER_APPLY_TICKET = "/auth/sso/server/applyTicket";

		/** SSO-Server端 (and Client端)：单点注销地址 */
		public static final String SSO_LOGOUT = "/auth/sso/server/logout";


		/** SSO-Client端：单点注销的回调 */
		public static final String SSO_LOGOUT_CALL = "/auth/sso/client/logoutCall";
		/** SSO-Client端：向服务端申请ticket */
		public static final String SSO_CLIENT_APPLY_TICKET = "/auth/sso/client/applyTicket";
		/** SSO-Client端：向服务端校验ticket */
		public static final String SSO_CLIENT_CHECKTICKET = "/auth/sso/client/checkTicket";

	}
	
	/**
	 * 所有参数名称 
	 * @author kong 
	 */
	public static final class ParamName {

		private ParamName(){

		}

		/** ssoAccount参数名称 */
		public static final String SSO_ACCOUNT = "ssoAccount";

		/** secretkey参数名称 */
		public static final String SECRETKEY = "secretkey";

		public static final String NAME = "name";
		public static final String PWD = "pwd";
		
		public static final String TIMESTAMP = "timestamp";
		public static final String NONCE = "nonce";
		public static final String SIGN = "sign";


		/** token绑定的参数名称 */
		public static final String SSO_LOGIN_MODEL = "ssoLoginModel";
		/** 登录来源标识 */
		public static final String UNIQUE_MARK = "uniqueMark";
		/** 允许 */
		public static final String ALLOW_URL = "allowUrl";
		/** 第一次登录时间 */
		public static final String LOGIN_TIME = "loginTime";
		/** 单点注销-回调URL */
		public static final String SSO_LOGOUT_CALL = "ssoLogoutCall";


		/** ticket参数名称 */
		public static final String TICKET = "ticket";
	}

	/** Client端单点注销回调URL的Set集合，存储在Session中使用的key */
	public static final String SLO_CALLBACK_SET_KEY = "SLO_CALLBACK_SET_KEY_";


	/** 表示请求没有得到任何有效处理 {msg: "not handle"} */
	public static final String NOT_HANDLE = "{\"msg\": \"not handle\"}";

}
