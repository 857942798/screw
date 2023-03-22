package com.ds.screw.auth.spring.json;

import com.ds.screw.auth.exception.AuthJsonConvertException;
import com.ds.screw.auth.json.AuthJsonTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * <p>
 *      JSON 转换器， Jackson 版实现
 * </p>
 *
 * @author dongsheng
 */
public class AuthJsonTemplateForJackson implements AuthJsonTemplate {

	/**
	 * 底层 Mapper 对象 
	 */
	public ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 将任意对象转换为 json 字符串 
	 * 
	 * @param obj 对象 
	 * @return 转换后的 json 字符串
	 */
	public String toJsonString(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new AuthJsonConvertException(e);
		}
	}
	
	/**
	 * 将 json 字符串解析为 Map
	 */
	@Override
	public Map<String, Object> parseJsonToMap(String jsonStr) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = objectMapper.readValue(jsonStr, Map.class);
			return map;
		} catch (JsonProcessingException e) {
			throw new AuthJsonConvertException(e);
		}
	}

}
