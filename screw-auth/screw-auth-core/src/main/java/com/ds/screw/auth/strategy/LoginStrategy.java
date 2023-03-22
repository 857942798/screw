package com.ds.screw.auth.strategy;

import com.ds.screw.auth.AuthManager;
import com.ds.screw.auth.config.AuthConfig;
import com.ds.screw.auth.context.AuthContextHolder;
import com.ds.screw.auth.domain.LoginModel;
import com.ds.screw.auth.domain.TokenSign;
import com.ds.screw.auth.exception.UnLoginException;
import com.ds.screw.auth.session.AuthSession;
import com.ds.screw.auth.util.TokenUtils;


/**
 * <h1>异地登录策略</h1>
 * <p>
 * 0：允许异地登录：不论IP，不论设备类型场景下，允许同一账号同时登录，不执行踢人下线策略
 * <p>>>: 任意IP，任意终端都允许同时登录</p>
 * </p><br>
 * <p>
 * 1：允许异地登录：相同IP，不论设备类型场景下，允许同一账号同时登录，需要将当前账号的其它IP的登录踢下线
 * <p>>>: 同一台主机允许同时登录浏览器、APP等</p>
 * </p><br>
 * <p>
 * 2：允许异地登录：相同IP，相同设备类型场景下，允许同一账号同时登录，需要将当前账号的其它IP或者其它设备类型的登录踢下线
 * <p>>>: 同一台主机允许同时登录不同的浏览器，但不允许同时登录浏览器和APP。</p>
 * </p><br>
 * <p>
 * 3：允许异地登录：相同IP，不同设备类型场景下，允许同一账号同时登录，需要将当前账号的其它IP或者当前设备类型的登录踢下线
 * <p>>>: 同一台主机不允许同时登录两种浏览器，但允许同时在浏览器或APP上执行登录。</p>
 * </p><br>
 * <p>
 * 4：允许异地登录：不同IP，不论设备类型场景下，允许同一账号同时登录，需要将当前账号的当前IP的登录踢下线
 * <p>>>: 同一台主机上不管什么设备都不允许同时登录，但不同的IP可以</p>
 * </p><br>
 * <p>
 * 5：允许异地登录：不同IP，相同设备类型场景下，允许同一账号同时登录，需要将当前账号的当前IP或者其它设备类型的登录踢下线
 * <p>>>: 不同的主机允许同时登录浏览器，但不允许一台主机登录浏览器，一台主机登录APP</p>
 * </p><br>
 * <p>
 * 6：允许异地登录：不同IP，不同设备类型场景下，允许同一账号同时登录，需要将当前账号的当前IP或者当前设备类型的登录踢下线
 * <p>>>: 一台主机登录了app、一台主机登录了浏览器</p>
 * </p><br>
 * <p>
 * 9：禁止异地登录：禁止其它登录，将登录账号关联的所有其它登录全部踢下线
 * </p><br>
 *
 * @author dongsheng
 */
public class LoginStrategy {


    public static String getToken(String loginId, LoginModel loginModel) {

        AuthConfig config = AuthManager.getConfig();

        // 1. 生成一个token
        String tokenValue = getSharedToken(loginId, loginModel, config.getLoginStrategy());

        // 2. 执行异地登录下线策略
        executeReplacedStrategy(loginId, loginModel, config.getLoginStrategy());

        // 如果至此，仍未成功创建tokenValue, 则开始生成一个
        if (tokenValue == null) {
            tokenValue = TokenUtils.createTokenValue();
        }

        return tokenValue;
    }


    private static String getSharedToken(String loginId, LoginModel loginModel, String strategy) {
        // 2. 生成一个token
        String tokenValue = null;
        switch (strategy) {
            case "2":
                tokenValue = TokenUtils.getTokenValueByLoginId(loginId, loginModel.getDeviceOrDefault());
                break;
            case "5":
                tokenValue = TokenUtils.getTokenValueByLoginId(loginId, loginModel.getDeviceOrDefault());
                break;
            default:
                break;
        }
        return tokenValue;
    }


    private static void executeReplacedStrategy(String loginId, LoginModel loginModel, String strategy) {
        AuthSession session = TokenUtils.getSessionByLoginId(loginId, false);
        if (session != null) {
            switch (strategy) {
                case "0":
                    replaced_0(loginId, session, loginModel);
                    break;
                case "1":
                    replaced_1(loginId, session, loginModel);
                    break;
                case "2":
                    replaced_2(loginId, session, loginModel);
                    break;
                case "3":
                    replaced_3(loginId, session, loginModel);
                    break;
                case "4":
                    replaced_4(loginId, session, loginModel);
                    break;
                case "5":
                    replaced_5(loginId, session, loginModel);
                    break;
                case "6":
                    replaced_6(loginId, session, loginModel);
                    break;
                case "9":
                    replaced_9(loginId, session, loginModel);
                    break;
                default:
                    break;
            }
        }
    }


    // 0：允许异地登录：不论IP，不论设备类型场景下，允许同一账号同时登录，不执行踢人下线策略
    // 任意IP，任意终端都允许同时登录
    private static void replaced_0(String loginId, AuthSession session, LoginModel loginModel) {
        // do nothing
    }


    // 1：允许异地登录：相同IP，不论设备类型场景下，允许同一账号同时登录，需要将当前账号的其它IP的登录踢下线
    // 同一台主机允许同时登录浏览器、APP等
    private static void replaced_1(String loginId, AuthSession session, LoginModel loginModel) {
        for (TokenSign tokenSign : session.getTokenSignList()) {
            if (!tokenSign.getAddress().equals(AuthContextHolder.getRequest().getAddress())) {
                replaced(loginId, session, tokenSign);
            }
        }
    }

    // 2：允许异地登录：相同IP，相同设备类型场景下，允许同一账号同时登录，需要将当前账号的其它IP或者其它设备类型的登录踢下线
    // 同一台主机允许同时登录不同的浏览器，但不允许同时登录浏览器和APP。
    private static void replaced_2(String loginId, AuthSession session, LoginModel loginModel) {
        for (TokenSign tokenSign : session.getTokenSignList()) {
            if (!tokenSign.getDevice().equals(loginModel.getDevice()) ||
                    !tokenSign.getAddress().equals(AuthContextHolder.getRequest().getAddress())) {
                replaced(loginId, session, tokenSign);
            }
        }
    }

    // 3：允许异地登录：相同IP，不同设备类型场景下，允许同一账号同时登录，需要将当前账号的其它IP或者当前设备类型的登录踢下线
    // 同一台主机不允许同时登录两种浏览器，但允许同时在浏览器或APP上执行登录。
    private static void replaced_3(String loginId, AuthSession session, LoginModel loginModel) {
        for (TokenSign tokenSign : session.getTokenSignList()) {
            if (tokenSign.getDevice().equals(loginModel.getDevice()) ||
                    !tokenSign.getAddress().equals(AuthContextHolder.getRequest().getAddress())) {
                replaced(loginId, session, tokenSign);
            }
        }
    }

    // 4：允许异地登录：不同IP，不论设备类型场景下，允许同一账号同时登录，需要将当前账号的当前IP的登录踢下线
    // 同一台主机上不管什么设备都不允许同时登录，但不同的IP可以
    private static void replaced_4(String loginId, AuthSession session, LoginModel loginModel) {
        for (TokenSign tokenSign : session.getTokenSignList()) {
            if (tokenSign.getAddress().equals(AuthContextHolder.getRequest().getAddress())) {
                replaced(loginId, session, tokenSign);
            }
        }
    }

    // 5：允许异地登录：不同IP，相同设备类型场景下，允许同一账号同时登录，需要将当前账号的当前IP或者其它设备类型的登录踢下线
    // 不同的主机允许同时登录浏览器，但不允许一台主机登录浏览器，一台主机登录APP
    private static void replaced_5(String loginId, AuthSession session, LoginModel loginModel) {
        for (TokenSign tokenSign : session.getTokenSignList()) {
            if (!tokenSign.getDevice().equals(loginModel.getDevice()) ||
                    tokenSign.getAddress().equals(AuthContextHolder.getRequest().getAddress())) {
                replaced(loginId, session, tokenSign);
            }
        }
    }

    // 6：允许异地登录：不同IP，不同设备类型场景下，允许同一账号同时登录，需要将当前账号的当前IP或者当前设备类型的登录踢下线
    // 一台主机登录了app、一台主机登录了浏览器
    private static void replaced_6(String loginId, AuthSession session, LoginModel loginModel) {
        for (TokenSign tokenSign : session.getTokenSignList()) {
            if (tokenSign.getDevice().equals(loginModel.getDevice()) ||
                    tokenSign.getAddress().equals(AuthContextHolder.getRequest().getAddress())) {
                replaced(loginId, session, tokenSign);
            }
        }
    }


    // 禁止异地登录
    private static void replaced_9(String loginId, AuthSession session, LoginModel loginModel) {
        for (TokenSign tokenSign : session.getTokenSignList()) {
            replaced(loginId, session, tokenSign);
        }
    }


    /**
     * 执行下线策略
     *
     * @param loginId   用户ID
     * @param session   会话对象
     * @param tokenSign token记录
     */
    private static void replaced(String loginId, AuthSession session, TokenSign tokenSign) {
        String tokenValue = tokenSign.getValue();
        // 清理token签名
        session.removeTokenSign(tokenValue);
        // 清理 token最近访问时间
        TokenUtils.removeLastAccessedTime(tokenValue);
        // 将此 token 标记为已被顶替
        TokenUtils.updateTokenValueToLoginIdMap(tokenValue, UnLoginException.BE_REPLACED);
        // $$ 通知监听器，某某Token被顶下线了
        AuthManager.getAuthListener().onReplaced(loginId, tokenValue);
    }


}
