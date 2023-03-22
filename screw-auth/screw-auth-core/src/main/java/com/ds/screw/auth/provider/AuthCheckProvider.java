package com.ds.screw.auth.provider;

import java.util.List;

/**
 * <p>
 *      权限认证接口，定义获取权限码集合和角色标识集合方式
 * </p>
 *
 * @author dongsheng
 */
public interface AuthCheckProvider {
    /**
     * 返回指定账号id所拥有的权限码集合
     *
     * @param loginId  账号id
     * @return 该账号id具有的权限码集合
     */
    List<String> getPermissionList(Object loginId);

    /**
     * 返回指定账号id所拥有的角色标识集合
     *
     * @param loginId  账号id
     * @return 该账号id具有的角色标识集合
     */
    List<String> getRoleList(Object loginId);

}
