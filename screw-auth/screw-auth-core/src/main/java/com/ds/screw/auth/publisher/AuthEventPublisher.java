package com.ds.screw.auth.publisher;

import com.ds.screw.auth.domain.Token;

/**
 *
 * <p>
 *      事件发布
 * </p>
 *
 * @author dongsheng
 */
public interface AuthEventPublisher {

    /**
     * 正式登录，会记录日志
     * @param token
     */
    public void publishLogin(Token token);

    /**
     * 临时登录，不会修改当前登录状态
     * @param token
     */
    public void publishTempLogin(Token token);

    /**
     * 登出前事件
     * @param token
     */
    public void beforeLogout(Token token);

}
