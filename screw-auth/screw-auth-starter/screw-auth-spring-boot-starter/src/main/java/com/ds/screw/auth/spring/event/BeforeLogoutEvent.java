package com.ds.screw.auth.spring.event;

import com.ds.screw.auth.domain.Token;

/**
 * 登出前事件
 *
 * @author dongsheng
 */
public class BeforeLogoutEvent extends AuthEvent {

    private final Token token;

    public BeforeLogoutEvent(Token token) {
        super(token);
        this.token = token;
    }

    public Token getTokenInfo() {
        return token;
    }
}
