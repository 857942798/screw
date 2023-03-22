package com.ds.screw.auth.annotation;

/**
 * 注解鉴权的验证模式 
 * @author dongsheng
 *
 */
public enum AuthMode {

	/**
	 * 必须具有所有的元素 
	 */
	AND,

	/**
	 * 只需具有其中一个元素
	 */
	OR
	
}
