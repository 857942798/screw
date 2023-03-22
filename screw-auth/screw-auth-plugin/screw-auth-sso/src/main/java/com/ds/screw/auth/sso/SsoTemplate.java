package com.ds.screw.auth.sso;


import com.ds.screw.auth.AuthManager;
import com.ds.screw.auth.AuthUtils;
import com.ds.screw.auth.context.AuthContextHolder;
import com.ds.screw.auth.sso.constant.SsoConsts;
import com.ds.screw.auth.sso.constant.SsoConsts.ParamName;
import com.ds.screw.auth.sso.constant.SsoEnumCode;
import com.ds.screw.auth.sso.domain.SsoResult;
import com.ds.screw.auth.sso.util.SsoSignUtils;
import com.ds.screw.auth.util.AuthFoxUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * SSO 模版类
 * </p>
 *
 * @author dongsheng
 */
public class SsoTemplate {
    // ------------------- 请求相关 -------------------

    /**
     * 发出请求，并返回 body 结果
     *
     * @param url 请求地址
     * @return 返回的结果
     */
    public SsoResult request(String url, Map<String, String> headers) {
        try {
            // 增加默认的参数timestmp,noce,uniqueMark
            headers.put(ParamName.NONCE, AuthFoxUtils.getRandomString(20));
            headers.put(ParamName.UNIQUE_MARK, AuthContextHolder.getRequest().getAddress());
            headers.put(ParamName.TIMESTAMP, String.valueOf(System.currentTimeMillis()));
            // 增加签名
            headers.put(ParamName.SIGN, SsoSignUtils.createSign(headers));

            String body = SsoManager.getFunctionConfig().getSendHttp().apply(url, headers);
            if (AuthFoxUtils.isEmpty(body)) {
                return SsoResult.error(SsoEnumCode.CODE_002);
            }
            Map<String, Object> map = AuthManager.getAuthJsonTemplate().parseJsonToMap(body);
            return new SsoResult(map);
        } catch (Exception e) {
            return SsoResult.error(SsoEnumCode.CODE_002);
        }
    }

    /**
     * 单点登录，向服务端同步单点登录信息
     */
    public SsoResult ssoSyncLogin(Map<String, String> headers) {
        String ssoSyncLoginUrl = SsoManager.getConfig().getServerUrl() + SsoConsts.Api.SSO_SYNC_LOGIN;
        return request(ssoSyncLoginUrl, headers);
    }

    public SsoResult applySsoTicket() {
        String ssoServerGetTicketUrl = SsoManager.getConfig().getServerUrl() + SsoConsts.Api.SSO_SERVER_APPLY_TICKET;
        Map<String, String> headers = new HashMap<>();
        headers.put(AuthManager.getConfig().getTokenName(), AuthUtils.getTokenValue());
        String ssoAccount = SsoManager.getSsoResolver().ssoAccountResolver(AuthUtils.getLoginId());
        headers.put(ParamName.SSO_ACCOUNT, ssoAccount);
        return request(ssoServerGetTicketUrl, headers);
    }

    public SsoResult ssoLogout() {
        String ssoLogoutUrl = SsoManager.getConfig().getServerUrl() + SsoConsts.Api.SSO_LOGOUT;
        Map<String, String> headers = new HashMap<>();
        String ssoAccount = SsoManager.getSsoResolver().ssoAccountResolver(AuthUtils.getLoginId());
        headers.put(ParamName.SSO_ACCOUNT, ssoAccount);
        return request(ssoLogoutUrl, headers);
    }

    public SsoResult callClientLogout(String url, String ssoAccount) {
        String ssoLogoutCallUrl = url + SsoConsts.Api.SSO_LOGOUT_CALL;
        Map<String, String> headers = new HashMap<>();
        headers.put(ParamName.SSO_ACCOUNT, ssoAccount);
        return request(ssoLogoutCallUrl, headers);
    }

    public SsoResult ssoCheckTicket(String ticket) {
        String ssoCheckTicketUrl = SsoManager.getConfig().getServerUrl() + SsoConsts.Api.SSO_CHECK_TICKET;
        Map<String, String> headers = new HashMap<>();
        headers.put(ParamName.TICKET, ticket);
        return request(ssoCheckTicketUrl, headers);
    }

}
