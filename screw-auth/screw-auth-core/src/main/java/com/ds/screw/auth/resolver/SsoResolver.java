package com.ds.screw.auth.resolver;

/**
 * <p>
 *      sso操作类
 * </p>
 *
 * @author dongsheng
 */
public interface SsoResolver {
    /**
     * 根据登录id获取账号
     * @param loginId
     * @return
     */

    String ssoAccountResolver(String loginId);

    /**
     * 根据账号获取登录id
     * @param ssoAccount
     * @return
     */
    String loginIdResolver(String ssoAccount);

}
