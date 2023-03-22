package com.ds.screw.auth.provider;

import java.util.ArrayList;
import java.util.List;

/**
 * 对 {@link AuthCheckProvider} 接口默认的实现类
 *
 * @author dongsheng
 */
public class DefaultAuthCheckProvider implements AuthCheckProvider {

	@Override
	public List<String> getPermissionList(Object loginId) {
		return new ArrayList<String>();
	}

	@Override
	public List<String> getRoleList(Object loginId) {
		return new ArrayList<String>();
	}

}
