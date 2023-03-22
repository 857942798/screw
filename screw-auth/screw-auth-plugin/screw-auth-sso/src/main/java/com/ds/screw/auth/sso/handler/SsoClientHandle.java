package com.ds.screw.auth.sso.handler;


import com.ds.screw.auth.AuthManager;
import com.ds.screw.auth.AuthUtils;
import com.ds.screw.auth.context.AuthContextHolder;
import com.ds.screw.auth.context.domain.AuthRequest;
import com.ds.screw.auth.sso.SsoManager;
import com.ds.screw.auth.sso.SsoUtils;
import com.ds.screw.auth.sso.config.SsoConfig;
import com.ds.screw.auth.sso.constant.SsoConsts;
import com.ds.screw.auth.sso.constant.SsoEnumCode;
import com.ds.screw.auth.sso.domain.SsoResult;
import com.ds.screw.auth.sso.util.SsoSignUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


/**
 * <p>
 *      sso 客户端处理器类
 * </p>
 *
 * @author dongsheng
 */
public class SsoClientHandle {

    private static final Logger log = Logger.getLogger(SsoClientHandle.class.getName());

    private SsoClientHandle(){

    }

    // ----------- 路由分发 ----------------------
    public static Object ssoClientRequest() {
        // 安全策略
        // 获取对象
        AuthRequest req = AuthContextHolder.getRequest();
        // ------------------ 路由分发 ------------------
        // ---------- SSO-Client端：登录地址
        // SSO-Client端：向服务端申请ticket
        if(req.isPath(SsoConsts.Api.SSO_CLIENT_APPLY_TICKET)) {
            return ssoApplyTicket();
        }

        // SSO-Client端：向服务端校验ticket是否有效
        if(req.isPath(SsoConsts.Api.SSO_CLIENT_CHECKTICKET)) {
            return ssoCheckTicket();
        }

        // SSO-Client端：服务端调用客户端的单点注销回调
        if(req.isPath(SsoConsts.Api.SSO_LOGOUT_CALL)) {
            return ssoLogoutCall();
        }

        // 默认返回
        return SsoResult.error(SsoEnumCode.CODE_003);
    }


    // ----------- SSO-Client 端路由分发
    /**
     * SSO-Client端：单点注销的回调
     * @return 处理结果
     */
    public static Object ssoLogoutCall() {
        SsoConfig cfg = SsoManager.getConfig();
        try {
            log.info(">>>> 单点登录代理：客户端[" + cfg.getSystemCode() + "]：接收到单点登出请求！");
            // 校验签名
            SsoSignUtils.checkSign();
            AuthRequest req = AuthContextHolder.getRequest();
            // 注销当前应用端会话
            String ssoAccount = req.getHeaderNotNull(SsoConsts.ParamName.SSO_ACCOUNT);
            String loginId = SsoManager.getSsoResolver().loginIdResolver(ssoAccount);
            if(loginId == null) {
                return SsoResult.error(SsoEnumCode.CODE_006);
            }
            AuthUtils.logout(loginId);
            return SsoResult.success();
        } catch (Exception e) {
            return SsoResult.error(SsoEnumCode.CODE_006);
        }
    }

    /**
     * SSO-Client端：向服务端申请ticket
     * 入参：token
     * 1. 如果当前客户端未登录，返回失败
     * 2. 如果当前客户端已登录，根据token获取当前客户端的loginId，服务端根据loginId生成ticket返回
     * @return 处理结果
     */
    public static Object ssoApplyTicket() {
        if(!AuthUtils.isLogin()){
            return SsoResult.error(SsoEnumCode.CODE_005);
        }
        SsoConfig cfg = SsoManager.getConfig();
        log.info(">>>> 单点登录代理：客户端[" + cfg.getSystemCode() + "]：向服务端发起票据申请，并返回给前端！");
        // 构建请求，将token放入请求头中
        return SsoUtils.applySsoTicket();
    }

    /**
     * SSO-Client端：从请求头中获取ticket后，调用服务端接口校验该ticket的有效性<br/>
     * 入参：ticket<br/>
     * @return 处理结果
     */
    public static Object ssoCheckTicket() {
        SsoConfig cfg = SsoManager.getConfig();
        AuthRequest req = AuthContextHolder.getRequest();
        log.info(">>>> 单点登录代理：客户端[" + cfg.getSystemCode() + "]：拿到服务端返回的票据信息，向服务端发起验证请求！");
        SsoResult ssoResult = SsoUtils.ssoCheckTicket(req.getParameterNotNull(SsoConsts.ParamName.TICKET));
        if(ssoResult.isSuccess()){
            Map<String, String> result = (HashMap<String, String>) ssoResult.getData();

            String ssoAccount = result.get(SsoConsts.ParamName.SSO_ACCOUNT);
            String loginId = SsoManager.getSsoResolver().loginIdResolver(ssoAccount);

            if(loginId == null) {
                return  SsoResult.error(SsoEnumCode.CODE_011);
            }


            log.info(">>>> 单点登录代理：客户端[" + cfg.getSystemCode() + "]：服务端票据验证通过，执行本地登录！");
            AuthUtils.login(loginId);
            String tokenValue = AuthUtils.getTokenValue();
            Map<String, String> data = new HashMap<>();
            data.put(AuthManager.getConfig().getTokenName(), tokenValue);
            return SsoResult.data(data);
        }else{
            log.warning(">>>> 单点登录代理：客户端[" + cfg.getSystemCode() + "]：服务端票据验证失败！");
            return  SsoResult.error(SsoEnumCode.CODE_004);
        }
    }
}
