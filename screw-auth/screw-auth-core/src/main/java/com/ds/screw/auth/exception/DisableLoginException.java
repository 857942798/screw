package com.ds.screw.auth.exception;

/**
 * <p>
 *      异常：此账号已被封禁
 * </p>
 *
 * @author dongsheng
 */
public class DisableLoginException extends AuthException {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 2633811739108211470L;

    /**
     * 异常标记值
     */
    public static final String BE_VALUE = "disable";
    /**
     * 异常提示语
     */
    public static final String BE_MESSAGE = "此账号已被封禁";
    /**
     * 被封禁的账号id
     */
    private final String loginId;

    /**
     * 封禁剩余时间，单位：秒
     */
    private final long disableTime;


    /**
     * 一个异常：代表账号已被封禁
     *
     * @param loginId     被封禁的账号id
     * @param disableTime 封禁剩余时间，单位：秒
     */
    public DisableLoginException(String loginId, long disableTime) {
        super(BE_MESSAGE);
        this.loginId = loginId;
        this.disableTime = disableTime;
    }

    /**
     * 获取: 被封禁的账号id
     *
     * @return See above
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * 获取: 封禁剩余时间，单位：秒
     *
     * @return See above
     */
    public long getDisableTime() {
        return disableTime;
    }
}
