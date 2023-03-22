package com.ds.screw.auth.sso;


import com.ds.screw.auth.resolver.SsoResolver;
import com.ds.screw.auth.sso.config.SsoConfig;
import com.ds.screw.auth.sso.config.SsoFunctionConfig;
import com.ds.screw.auth.sso.resolver.DefaultSsoResolver;

/**
 * <p>
 *      SSO 模块 总控类
 * </p>
 *
 * @author dongsheng
 */
public class SsoManager {
	private SsoManager(){}

	/**
	 * Sso 配置 Bean 
	 */
	private static volatile SsoConfig config;

	public static SsoConfig getConfig() {
		if (config == null) {
			synchronized (SsoManager.class) {
				if (config == null) {
					setConfig(new SsoConfig());
				}
			}
		}
		return config;
	}
	public static void setConfig(SsoConfig config) {
		SsoManager.config = config;
	}

	/**
	 * Sso 回调函数配置类
	 */
	private static volatile SsoFunctionConfig functionConfig;
	public static SsoFunctionConfig getFunctionConfig() {
		if (functionConfig == null) {
			synchronized (SsoManager.class) {
				if (functionConfig == null) {
					setFunctionConfig(new SsoFunctionConfig());
				}
			}
		}
		return functionConfig;
	}
	public static void setFunctionConfig(SsoFunctionConfig functionConfig) {
		SsoManager.functionConfig = functionConfig;
	}

	/**
	 * Sso 配置 Bean
	 */
	private static volatile SsoTemplate ssoTemplate;
	public static SsoTemplate getSsoTemplate() {
		if (ssoTemplate == null) {
			synchronized (SsoManager.class) {
				if (ssoTemplate == null) {
					setSsoTemplate(new SsoTemplate());
				}
			}
		}
		return ssoTemplate;
	}
	public static void setSsoTemplate(SsoTemplate ssoTemplate) {
		SsoManager.ssoTemplate = ssoTemplate;
	}

	/**
	 * 配置账号解析工具
	 */
	private static volatile SsoResolver ssoResolver;
	public static SsoResolver getSsoResolver() {
		if (ssoResolver == null) {
			synchronized (SsoManager.class) {
				if (ssoResolver == null) {
					setSsoResolver(new DefaultSsoResolver());
				}
			}
		}
		return ssoResolver;
	}

	public static void setSsoResolver(SsoResolver ssoResolver) {
		SsoManager.ssoResolver = ssoResolver;
	}


}
