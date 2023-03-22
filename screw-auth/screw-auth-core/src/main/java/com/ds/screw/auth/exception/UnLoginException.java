package com.ds.screw.auth.exception;

import com.ds.screw.auth.util.AuthFoxUtils;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *      登录状态异常：token无效、过期、被顶下线，都会导致该异常产生
 * </p>
 *
 * @author dongsheng
 */
public class UnLoginException extends AuthException {

    /**
     * 表示未提供token
     */
    public static final String NOT_TOKEN = "-1";


    // ------------------- 异常类型常量  --------------------

    /*
     * 这里简述一下为什么要把常量设计为String类型
     * 因为loginId刚取出的时候类型为String，为了避免两者相比较时不必要的类型转换带来的性能消耗，故在此直接将常量类型设计为String
     */
    public static final String NOT_TOKEN_MESSAGE = "未能读取到有效Token";
    /**
     * 表示token无效
     */
    public static final String INVALID_TOKEN = "-2";
    public static final String INVALID_TOKEN_MESSAGE = "Token无效";
    /**
     * 表示token已过期
     */
    public static final String TOKEN_TIMEOUT = "-3";
    public static final String TOKEN_TIMEOUT_MESSAGE = "Token已过期";
    /**
     * 表示token已被顶下线
     */
    public static final String BE_REPLACED = "-4";
    public static final String BE_REPLACED_MESSAGE = "Token已被顶下线";
    /**
     * 表示token已被踢下线
     */
    public static final String KICK_OUT = "-5";
    public static final String KICK_OUT_MESSAGE = "Token已被踢下线";
    /**
     * 默认的提示语
     */
    public static final String DEFAULT_MESSAGE = "当前会话未登录";
    /**
     * 登录设备异常
     */
    public static final String INVALID_DEVICE_MESSAGE = "登录设备异常";
    /**
     * 代表异常token的标志集合
     */
    private static final List<String> ABNORMAL_LIST = Arrays.asList(NOT_TOKEN, INVALID_TOKEN, TOKEN_TIMEOUT, BE_REPLACED, KICK_OUT);
    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = -4208527742013027678L;
    /**
     * 异常类型
     */
    private final String type;

    /**
     * 构造方法创建一个
     *
     * @param message 异常消息
     * @param type    类型
     */
    public UnLoginException(String message, String type) {
        super(message);
        this.type = type;
    }

    /**
     * 静态方法构建一个NotLoginException
     *
     * @param type 账号类型
     * @return 构建完毕的异常对象
     */
    public static UnLoginException newInstance(String type) {
        return newInstance(type, null);
    }

    /**
     * 静态方法构建一个NotLoginException
     *
     * @param type  账号类型
     * @param token 引起异常的Token值
     * @return 构建完毕的异常对象
     */
    public static UnLoginException newInstance(String type, String token) {
        String message = null;
        if (NOT_TOKEN.equals(type)) {
            message = NOT_TOKEN_MESSAGE;
        } else if (INVALID_TOKEN.equals(type)) {
            message = INVALID_TOKEN_MESSAGE;
        } else if (TOKEN_TIMEOUT.equals(type)) {
            message = TOKEN_TIMEOUT_MESSAGE;
        } else if (BE_REPLACED.equals(type)) {
            message = BE_REPLACED_MESSAGE;
        } else if (KICK_OUT.equals(type)) {
            message = KICK_OUT_MESSAGE;
        } else {
            message = DEFAULT_MESSAGE;
        }
        if (!AuthFoxUtils.isEmpty(token)) {
            message = message + "：" + token;
        }
        return new UnLoginException(message, type);
    }

    public static void checkIsValidLoginId(String loginId, String tokenValue) {
        // 检查loginId是否有效
        if (loginId == null) {
            throw UnLoginException.newInstance(UnLoginException.INVALID_TOKEN, tokenValue);
        }
        // 如果是已经过期，则抛出：已经过期
        if (loginId.equals(UnLoginException.TOKEN_TIMEOUT)) {
            throw UnLoginException.newInstance(UnLoginException.TOKEN_TIMEOUT, tokenValue);
        }
        // 如果是已经被顶替下去了, 则抛出：已被顶下线
        if (loginId.equals(UnLoginException.BE_REPLACED)) {
            throw UnLoginException.newInstance(UnLoginException.BE_REPLACED, tokenValue);
        }
        // 如果是已经被踢下线了, 则抛出：已被踢下线
        if (loginId.equals(UnLoginException.KICK_OUT)) {
            throw UnLoginException.newInstance(UnLoginException.KICK_OUT, tokenValue);
        }
    }

    /**
     * 判断一个 loginId 是否是有效的
     *
     * @param loginId 账号id
     * @return /
     */
    public static boolean isInValidLoginId(String loginId) {
        return loginId == null || UnLoginException.ABNORMAL_LIST.contains(loginId);
    }

    /**
     * 获取异常类型
     *
     * @return 异常类型
     */
    public String getType() {
        return type;
    }

}
