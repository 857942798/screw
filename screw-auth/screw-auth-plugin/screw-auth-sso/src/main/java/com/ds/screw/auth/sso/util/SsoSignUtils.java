package com.ds.screw.auth.sso.util;


import com.ds.screw.auth.AuthManager;
import com.ds.screw.auth.context.AuthContextHolder;
import com.ds.screw.auth.context.domain.AuthRequest;
import com.ds.screw.auth.sso.SsoManager;
import com.ds.screw.auth.sso.constant.SsoConsts;
import com.ds.screw.auth.sso.constant.SsoEnumCode;
import com.ds.screw.auth.sso.exception.SsoException;
import com.ds.screw.auth.util.AuthFoxUtils;

import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>
 *      单点登录模块 签名相关工具类
 * </p>
 *
 * @author dongsheng
 */
public class SsoSignUtils {
	private SsoSignUtils(){}
	/**
	 * 根据header请求参数生成sign
	 * @param headers
	 * @return
	 */
	public static String createSign(Map<String, String> headers) {
		Map<String, String> treeMap = new TreeMap<>();
		if(headers !=null){
			treeMap.putAll(headers);
		}
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : treeMap.entrySet()) {
			if(!AuthFoxUtils.isEmpty(entry.getValue())) {
				sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
		}
		String fullStr = sb+ "key=" + getSecretkey();
		return md5(fullStr);
	}

	/**
	 * 校验签名
	 */
	public static void checkSign() {
		AuthRequest req = AuthContextHolder.getRequest();
		// 参数签名、账号id、时间戳、随机字符串
		String sign = req.getHeaderNotNull(SsoConsts.ParamName.SIGN);

		Map<String, String> map = new TreeMap<>();
		if(!AuthFoxUtils.isEmpty(req.getHeader(SsoConsts.ParamName.SSO_LOGOUT_CALL))){
			map.put(SsoConsts.ParamName.SSO_LOGOUT_CALL,req.getHeader(SsoConsts.ParamName.SSO_LOGOUT_CALL));
		}
		if(!AuthFoxUtils.isEmpty(req.getHeader(SsoConsts.ParamName.TICKET))){
			map.put(SsoConsts.ParamName.TICKET,req.getHeader(SsoConsts.ParamName.TICKET));
		}
		if(!AuthFoxUtils.isEmpty(req.getHeader(SsoConsts.ParamName.SSO_ACCOUNT))){
			map.put(SsoConsts.ParamName.SSO_ACCOUNT,req.getHeader(SsoConsts.ParamName.SSO_ACCOUNT));
		}
		if(!AuthFoxUtils.isEmpty(req.getHeader(AuthManager.getConfig().getTokenName()))){
			map.put(AuthManager.getConfig().getTokenName(),req.getHeader(AuthManager.getConfig().getTokenName()));
		}
		// 时间戳、随机字符串、请求来源
		map.put(SsoConsts.ParamName.NONCE,req.getHeaderNotNull(SsoConsts.ParamName.NONCE));
		map.put(SsoConsts.ParamName.TIMESTAMP,req.getHeaderNotNull(SsoConsts.ParamName.TIMESTAMP));
		map.put(SsoConsts.ParamName.UNIQUE_MARK,req.getHeaderNotNull(SsoConsts.ParamName.UNIQUE_MARK));

		// 校验时间戳
		checkTimestamp(Long.valueOf(req.getHeaderNotNull(SsoConsts.ParamName.TIMESTAMP)));
		// 校验签名
		String calcSign = createSign(map);
		if(!calcSign.equals(sign)) {
			throw new SsoException("签名无效：" + calcSign).setCode(SsoEnumCode.CODE_008.getCode());
		}
	}

	/**
	 * 校验时间戳与当前时间的差距是否超出限制
	 * @param timestamp 时间戳
	 */
	public static void checkTimestamp(long timestamp) {
		long disparity = Math.abs(System.currentTimeMillis() - timestamp);
		long allowDisparity = SsoManager.getConfig().getTimestampDisparity();
		if(allowDisparity != -1 && disparity > allowDisparity) {
			throw new SsoException("timestamp 超出允许的范围").setCode(SsoEnumCode.CODE_007.getCode());
		}
	}

	/**
	 * 获取：接口调用秘钥
	 * @return see note
	 */
	public static String getSecretkey() {
		// 默认从配置文件中返回
		String secretkey = SsoManager.getConfig().getSecretKey();
		if(AuthFoxUtils.isEmpty(secretkey)) {
			throw new SsoException("请配置 secretkey 参数").setCode(SsoEnumCode.CODE_009.getCode());
		}
		return secretkey;
	}

	/**
	 * md5加密
	 * @param str 指定字符串
	 * @return 加密后的字符串
	 */
	private static String md5(String str) {
		str = (str == null ? "" : str);
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = str.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char[] strA = new char[j * 2];
			int k = 0;
			for (byte byte0 : md) {
				strA[k++] = hexDigits[byte0 >>> 4 & 0xf];
				strA[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(strA);
		} catch (Exception e) {
			throw new SsoException("加密出现异常");
		}
	}


}
