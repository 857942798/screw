package com.ds.screw.auth.sso.handler;

import com.ds.screw.auth.AuthManager;
import com.ds.screw.auth.context.AuthContextHolder;
import com.ds.screw.auth.context.domain.AuthRequest;
import com.ds.screw.auth.repository.AuthRepository;
import com.ds.screw.auth.sso.SsoManager;
import com.ds.screw.auth.sso.SsoUtils;
import com.ds.screw.auth.sso.config.SsoConfig;
import com.ds.screw.auth.sso.constant.SsoConsts;
import com.ds.screw.auth.sso.constant.SsoEnumCode;
import com.ds.screw.auth.sso.domain.SsoLoginInfo;
import com.ds.screw.auth.sso.domain.SsoResult;
import com.ds.screw.auth.sso.util.SsoSignUtils;
import com.ds.screw.auth.sso.util.SsoTicketUtils;
import com.ds.screw.auth.util.AuthFoxUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;


/**
 * <p>
 *      sso 服务端处理器类
 * </p>
 *
 * @author dongsheng
 */
public class SsoServerHandle {

    private static final Logger log = Logger.getLogger(SsoServerHandle.class.getName());

    private SsoServerHandle() {

    }

    // ----------- 路由分发 ----------------------
    public static Object ssoServerRequest() {

        // 安全策略
        // 获取对象
        AuthRequest req = AuthContextHolder.getRequest();
        SsoConfig cfg = SsoManager.getConfig();

        // ------------------ 路由分发 ------------------
        // 以下接口由客户端调用，需要校验签名
        SsoSignUtils.checkSign();

        // SSO-Server端：认证中心根据loginId登录
        if (req.isPath(SsoConsts.Api.SSO_SYNC_LOGIN)) {
            log.info(">>>> 单点登录代理：服务端 [" + cfg.getSystemCode() + "]：接收到登录信息同步请求！");
            return ssoSyncDoLogin();
        }

        // SSO-Server端：ticket下发
        if (req.isPath(SsoConsts.Api.SSO_SERVER_APPLY_TICKET)) {
            log.info(">>>> 单点登录代理：服务端[" + cfg.getSystemCode() + "]：接收到单点登录申请票据请求！");
            return ssoApplyTicket();
        }

        // SSO-Server端：ticket校验
        if (req.isPath(SsoConsts.Api.SSO_CHECK_TICKET)) {
            log.info(">>>> 单点登录代理：服务端[" + cfg.getSystemCode() + "]：接收到单点登录验证票据请求！");
            return ssoCheckTicket();
        }

        // SSO-Server端：单点注销
        if (req.isPath(SsoConsts.Api.SSO_LOGOUT) && cfg.isSsoLogoutEnable()) {
            log.info(">>>> 单点登录代理：服务端[" + cfg.getSystemCode() + "]：接收到单点登出请求！");
            return ssoLogout();
        }

        // 默认返回
        return SsoResult.error(SsoEnumCode.CODE_003);
    }


    // ----------- SSO-Server 端路由分发

    /**
     * SSO-Server端：存储loginId与登录信息
     *
     * @return 处理结果
     */
    public static Object ssoSyncDoLogin() {
        // 获取对象
        AuthRequest req = AuthContextHolder.getRequest();

        AuthRepository authRepository = AuthManager.getAuthRepository();
        SsoConfig ssoConfig = SsoManager.getConfig();
        String loginModelKey = splicingSsoAccountKey(req.getHeaderNotNull(SsoConsts.ParamName.SSO_ACCOUNT));
        long ssoTimeout = ssoConfig.getSsoLoginTimeout();

        SsoLoginInfo ssoLoginModel = (SsoLoginInfo) authRepository.get(loginModelKey);
        if (ssoLoginModel == null) {
            Set<String> urls = new HashSet<>();
            urls.add(req.getHeaderNotNull(SsoConsts.ParamName.SSO_LOGOUT_CALL));
            // 以hash结构保存登录信息
            ssoLoginModel = new SsoLoginInfo()
                    .setUniqueMark(req.getHeaderNotNull(SsoConsts.ParamName.UNIQUE_MARK))
                    .setLoginTime(LocalDateTime.now())
                    .setSsoLogoutCalls(urls);
            authRepository.set(loginModelKey, ssoLoginModel, ssoTimeout);
        } else {
            Set<String> urls = ssoLoginModel.getSsoLogoutCalls();
            urls.add(req.getHeaderNotNull(SsoConsts.ParamName.SSO_LOGOUT_CALL));
            ssoLoginModel.setSsoLogoutCalls(urls);
            authRepository.set(loginModelKey, ssoLoginModel, ssoTimeout);
        }
        return SsoResult.success();
    }


    /**
     * SSO-Server端：申请ticket
     * 入参：token
     * 1. 如果当前客户端未登录，返回失败
     * 2. 如果当前客户端已登录，根据token获取当前客户端的loginId，服务端根据loginId生成ticket返回
     *
     * @return 处理结果
     */
    public static Object ssoApplyTicket() {
        SsoConfig ssoConfig = SsoManager.getConfig();
        long ssoTimeout = ssoConfig.getSsoLoginTimeout();
        AuthRequest req = AuthContextHolder.getRequest();
        String ssoAccount = req.getHeaderNotNull(SsoConsts.ParamName.SSO_ACCOUNT);
        String loginModelKey = splicingSsoAccountKey(ssoAccount);
        AuthRepository authRepository = AuthManager.getAuthRepository();
        SsoLoginInfo ssoLoginModel = (SsoLoginInfo) authRepository.get(loginModelKey);
        // 如果该loginId未关联任何登录信息,则应该是登录事件触发的登录信息同步请求还未得到执行，此时则直接先创建一个
        if (ssoLoginModel == null) {
            // 以hash结构保存登录信息
            ssoLoginModel = new SsoLoginInfo()
                    .setUniqueMark(AuthContextHolder.getRequest().getAddress())
                    .setLoginTime(LocalDateTime.now())
                    .setSsoLogoutCalls(new HashSet<>());
            authRepository.set(loginModelKey, ssoLoginModel, ssoTimeout);
        }
        // 如果存在登录信息，新建一个ticket与当前loginId进行关联
        String ticketValue = SsoTicketUtils.createTicket(ssoAccount);
        Map<String, Object> data = new HashMap<>();
        data.put(SsoConsts.ParamName.TICKET, ticketValue);
        return SsoResult.data(data);
    }


    /**
     * SSO-Server端：校验ticket
     *
     * @return 处理结果 返回loginId
     */
    public static SsoResult ssoCheckTicket() {
        AuthRequest req = AuthContextHolder.getRequest();
        String ticket = req.getHeaderNotNull(SsoConsts.ParamName.TICKET);
        String ssoAccount = SsoTicketUtils.checkTicket(ticket);
        if (!AuthFoxUtils.isEmpty(ssoAccount)) {
            Map<String, String> data = new HashMap<>();
            data.put(SsoConsts.ParamName.SSO_ACCOUNT, ssoAccount);
            return SsoResult.data(data);
        }
        return SsoResult.error(SsoEnumCode.CODE_004);
    }

    /**
     * SSO-Server端：单点注销
     *
     * @return 处理结果
     */
    public static Object ssoLogout() {
        AuthRequest req = AuthContextHolder.getRequest();
        AuthRepository authRepository = AuthManager.getAuthRepository();
        String ssoAccount = req.getHeaderNotNull(SsoConsts.ParamName.SSO_ACCOUNT);
        String loginModelKey = splicingSsoAccountKey(ssoAccount);
        SsoLoginInfo ssoLoginModel = (SsoLoginInfo) authRepository.get(loginModelKey);

        if (ssoLoginModel != null) {
            // 说明是认证节点，需要向其他子系统发送登出请求
            SsoConfig cfg = SsoManager.getConfig();
            // 取到值后立马移除，防止单点登出时触发再登出死循环
            authRepository.delete(loginModelKey);
            // 是否打开单点注销
            if (cfg.isSsoLogoutEnable()) {
                Set<String> urls = ssoLoginModel.getSsoLogoutCalls();
                for (String url : urls) {
                    SsoUtils.callClientLogout(url, ssoAccount);
                }
            }
        }
        return SsoResult.success();
    }

    /**
     * 拼接key：该账号Id 对应的登录信息
     *
     * @param ssoAccount 账号id
     * @return key
     */
    private static String splicingSsoAccountKey(String ssoAccount) {
        return AuthManager.getConfig().getTokenName() + ":sso-ssoAccount:" + ssoAccount;
    }

}
