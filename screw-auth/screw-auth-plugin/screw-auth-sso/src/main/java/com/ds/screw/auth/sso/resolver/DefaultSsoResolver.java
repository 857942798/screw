package com.ds.screw.auth.sso.resolver;

import com.ds.screw.auth.resolver.SsoResolver;

/**
 * <p>
 *      sso账户相关的默认实现类
 * </p>
 *
 * @author dongsheng
 */
public class DefaultSsoResolver implements SsoResolver {
    @Override
    public String ssoAccountResolver(String loginId) {
        return loginId;
    }

    @Override
    public String loginIdResolver(String ssoAccount) {
        return ssoAccount;
    }
}
