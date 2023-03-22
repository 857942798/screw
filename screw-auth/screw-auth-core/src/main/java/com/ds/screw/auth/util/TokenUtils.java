package com.ds.screw.auth.util;

import com.ds.screw.auth.AuthManager;
import com.ds.screw.auth.domain.TokenSign;
import com.ds.screw.auth.exception.AuthException;
import com.ds.screw.auth.repository.AuthRepository;
import com.ds.screw.auth.session.AuthSession;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 *       token相关 工具类
 * </p>
 *
 * @author dongsheng
 */
public class TokenUtils {

    private TokenUtils() {
    }

    /**
     * 返回token名称
     *
     * @return 此StpLogic的token名称
     */
    public static String getTokenName() {
        return AuthManager.getConfig().getTokenName();
    }


    /**
     * 创建一个TokenValue
     *
     * @return 生成的tokenValue
     */
    public static String createTokenValue() {
        return UUID.randomUUID().toString();
    }

    /**
     * 删除 Token-Id 映射
     *
     * @param tokenValue token值
     */
    public static void removeTokenValueToLoginIdMap(String tokenValue) {
        AuthManager.getAuthRepository().delete(KeyUtils.getKeyForTokenValue(tokenValue));
    }

    /**
     * 更改 Token 指向的 账号Id 值
     *
     * @param tokenValue token值
     * @param loginId    新的账号Id值
     */
    public static void updateTokenValueToLoginIdMap(String tokenValue, String loginId) {
        if (AuthFoxUtils.isEmpty(loginId)) {
            throw new AuthException("账号id不能为空");
        }
        AuthManager.getAuthRepository().update(KeyUtils.getKeyForTokenValue(tokenValue), loginId);
    }

    /**
     * 存储 Token-Id 映射
     *
     * @param tokenValue token值
     * @param loginId    账号id
     * @param timeout    会话有效期 (单位: 秒)
     */
    public static void saveTokenValueToLoginIdMap(String tokenValue, String loginId, long timeout) {
        if (AuthFoxUtils.isEmpty(loginId)) {
            throw new AuthException("账号id不能为空");
        }
        AuthManager.getAuthRepository().set(KeyUtils.getKeyForTokenValue(tokenValue), loginId, timeout);
    }

    /**
     * 更好用 token 获取账号Id
     *
     * @param tokenValue token值
     * @return 账号Id
     */
    public static String getLoginIdByTokenValue(String tokenValue) {
        Object result = AuthManager.getAuthRepository().get(KeyUtils.getKeyForTokenValue(tokenValue));
        if (result != null) {
            return String.valueOf(result);
        }
        return null;
    }

    /**
     * 获取指定账号id指定设备类型端的tokenValue
     * <p> 在配置为允许并发登录时，此方法只会返回队列的最后一个token，
     * 如果你需要返回此账号id的所有token，请调用 getTokenValueListByLoginId
     *
     * @param loginId 账号id
     * @param device  设备类型，填null代表不限设备类型
     * @return token值
     */
    public static String getTokenValueByLoginId(String loginId, String device) {
        // 如果session为null的话直接返回空集合
        AuthSession session = getSessionByLoginId(loginId, false);
        if (session == null) {
            return null;
        }
        // 遍历解析
        List<TokenSign> tokenSignList = session.getTokenSignListByDevice(device);
        return tokenSignList.isEmpty() ? null : tokenSignList.get(tokenSignList.size() - 1).getValue();
    }

    /**
     * 获取指定账号id指定设备类型端的tokenValue
     * <p> 在配置为允许并发登录时，此方法只会返回队列的最后一个token，
     * 如果你需要返回此账号id的所有token，请调用 getTokenValueListByLoginId
     *
     * @param loginId 账号id
     * @param ip     IP地址，填 null 代表不限IP地址
     * @param device  设备类型，填null代表不限设备类型
     * @return token值
     */
    public static String getTokenValueByLoginId(String loginId, String ip, String device) {
        // 如果session为null的话直接返回空集合
        AuthSession session = getSessionByLoginId(loginId, false);
        if (session == null) {
            return null;
        }
        // 遍历解析
        List<TokenSign> tokenSignList = session.getTokenSignListByIpAndDevice(ip, device);
        return tokenSignList.isEmpty() ? null : tokenSignList.get(tokenSignList.size() - 1).getValue();
    }

    /**
     * 获取指定账号id的User-Session, 如果Session尚未创建，isCreate=是否新建并返回
     *
     * @param loginId  账号id
     * @param isCreate 是否新建
     * @return Session对象
     */
    public static AuthSession getSessionByLoginId(String loginId, boolean isCreate) {
        String sessionId = KeyUtils.getKeyForSession(loginId);
        AuthSession session = (AuthSession) AuthManager.getAuthRepository().get(sessionId);
        if (session == null && isCreate) {
            session = new AuthSession(sessionId);
        }
        return session;
    }

    /**
     * 获取指定token的User-Session
     *
     * @param tokenValue token值
     * @return Session对象
     */
    public static AuthSession getTempSessionByTokenValue(String tokenValue) {
        String sessionId = KeyUtils.getKeyForTempSession(tokenValue);
        return (AuthSession) AuthManager.getAuthRepository().get(sessionId);
    }

    /**
     * 创建指定token的User-Session
     *
     * @param tokenValue token值
     * @return Session对象
     */
    public static AuthSession createTempSessionByTokenValue(String tokenValue, long timeout) {
        String sessionId = KeyUtils.getKeyForTempSession(tokenValue);
        return new AuthSession(sessionId, timeout, true);
    }

    /**
     * 判断该token是否存活
     * @param tokenValue
     * @return
     */
    public static boolean isTokenInActive(String tokenValue) {
        return getLastAccessedTime(tokenValue) == AuthRepository.ALREADY_EXPIRE;
    }

    /**
     * 为token分配一个存活时间
     * @param tokenValue
     */
    public static void createLastAccessedTime(String tokenValue) {
        AuthManager.getAuthRepository().set(KeyUtils.getKeyForActivityTime(tokenValue), String.valueOf(System.currentTimeMillis()), AuthManager.getConfig().getTimeout());
    }

    /**
     * 移除token存活时间
     * @param tokenValue
     */
    public static void removeLastAccessedTime(String tokenValue) {
        AuthManager.getAuthRepository().delete(KeyUtils.getKeyForActivityTime(tokenValue));
    }

    /**
     * 更新token存活时间
     * @param tokenValue
     */
    public static void updateLastAccessedTime(String tokenValue) {
        AuthManager.getAuthRepository().update(KeyUtils.getKeyForActivityTime(tokenValue), String.valueOf(System.currentTimeMillis()));
    }

    /**
     * 获取token存活时间
     * @param tokenValue
     * @return
     */
    public static long getLastAccessedTime(String tokenValue) {
        // 如果token为null , 则返回 -2
        if (tokenValue == null) {
            return AuthRepository.ALREADY_EXPIRE;
        }
        // 如果设置了永不过期, 则返回 -1
        if (AuthManager.getConfig().getActivityTimeout() == AuthRepository.NEVER_EXPIRE) {
            return AuthRepository.NEVER_EXPIRE;
        }
        // ------ 开始查询
        // 获取相关数据
        String lastActivityTimeString = (String) AuthManager.getAuthRepository().get(KeyUtils.getKeyForActivityTime(tokenValue));
        // 查不到，返回-2
        if (lastActivityTimeString == null) {
            return AuthRepository.ALREADY_EXPIRE;
        }
        // 计算相差时间
        long lastActivityTime = Long.parseLong(lastActivityTimeString);
        long apartSecond = (System.currentTimeMillis() - lastActivityTime) / 1000;
        long timeout = AuthManager.getConfig().getActivityTimeout() - apartSecond;
        // 如果 < 0， 代表已经过期 ，返回-2
        if (timeout < 0) {
            return AuthRepository.ALREADY_EXPIRE;
        }
        return timeout;
    }
}
