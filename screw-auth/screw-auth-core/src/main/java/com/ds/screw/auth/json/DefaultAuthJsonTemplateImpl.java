package com.ds.screw.auth.json;

import com.ds.screw.auth.exception.ApiDisabledException;

import java.util.Map;

/**
 *
 * <p>
 *      JSON 相关操作接口
 * </p>
 *
 * @author dongsheng
 */
public class DefaultAuthJsonTemplateImpl implements AuthJsonTemplate {

	public static final String ERROR_MESSAGE = "未实现具体的 json 转换器";
	
	/**
	 * 将任意对象转换为 json 字符串 
	 */
	@Override
	public String toJsonString(Object obj) {
		throw new ApiDisabledException(ERROR_MESSAGE);
	}
	
	/**
	 * 将 json 字符串解析为 Map 
	 */
	@Override
	public Map<String, Object> parseJsonToMap(String jsonStr) {
		throw new ApiDisabledException(ERROR_MESSAGE);
	};
	
}
