package com.ds.screw.auth.util;

import com.ds.screw.auth.AuthManager;

/**
 * <p>
 *       key生成器 工具类
 * </p>
 *
 * @author dongsheng
 */
public class KeyUtils {

    private KeyUtils() {
    }

    /**
     * 拼接key：客户端 tokenName
     *
     * @return key
     */
    public static String getKeyForTokenName() {
        return AuthManager.getConfig().getTokenName();
    }

    /**
     * 拼接key： tokenValue 持久化 token-id
     *
     * @param tokenValue token值
     * @return key
     */
    public static String getKeyForTokenValue(String tokenValue) {
        return AuthManager.getConfig().getTokenName() + ":token:" + tokenValue;
    }

    /**
     * 指定账号是否已被封禁 (true=已被封禁, false=未被封禁)
     *
     * @param loginId 账号id
     * @return see note
     */
    public static String getKeyForDisable(String loginId) {
        return AuthManager.getConfig().getTokenName() + ":disable:" + loginId;
    }

    /**
     * 拼接key： Session 持久化
     *
     * @param loginId 账号id
     * @return key
     */
    public static String getKeyForSession(String loginId) {
        return AuthManager.getConfig().getTokenName() + ":session:" + loginId;
    }

    /**
     * 拼接key：临时 Session 持久化
     *
     * @param tokeValue token值
     * @return key
     */
    public static String getKeyForTempSession(String tokeValue) {
        return AuthManager.getConfig().getTokenName() + ":temp-session:" + tokeValue;
    }

    /**
     * 拼接key： 指定token的最后操作时间 持久化
     *
     * @param tokenValue token值
     * @return key
     */
    public static String getKeyForActivityTime(String tokenValue) {
        return AuthManager.getConfig().getTokenName() + ":activity:" + tokenValue;
    }
}
