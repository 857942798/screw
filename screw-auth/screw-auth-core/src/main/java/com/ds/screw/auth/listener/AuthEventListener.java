package com.ds.screw.auth.listener;

import com.ds.screw.auth.domain.LoginModel;

/**
 *
 * <p>
 *      事件监听器，对登录、登出等事件进行一些aop操作
 * </p>
 *
 * @author dongsheng
 */
public interface AuthEventListener {

    /**
     * 每次登录时触发
     *
     * @param loginId    账号id
     * @param tokenValue 本次登录产生的 token 值
     * @param loginModel 登录参数
     */
    void onLogin(String loginId, String tokenValue, LoginModel loginModel);

    /**
     * 每次注销时触发
     *
     * @param loginId    账号id
     * @param tokenValue token值
     */
    void onLogout(String loginId, String tokenValue);

    /**
     * 每次被顶下线时触发
     *
     * @param loginId    账号id
     * @param tokenValue token值
     */
    void onReplaced(String loginId, String tokenValue);

    /**
     * 每次被封禁时触发
     *
     * @param loginId     账号id
     * @param disableTime 封禁时长，单位: 秒
     */
    void onDisable(String loginId, long disableTime);

    /**
     * 每次被解封时触发
     *
     * @param loginId 账号id
     */
    void onEnable(String loginId);

    /**
     * 每次创建Session时触发
     *
     * @param id SessionId
     */
    void onSessionCreated(String id);

    /**
     * 每次注销Session时触发
     *
     * @param id SessionId
     */
    void onSessionInvalidate(String id);
}
