package com.ds.screw.auth.spring.event;

import com.ds.screw.auth.domain.Token;

/**
 * 登录成功事件
 *
 * @author dongsheng
 */
public class LoginEvent extends AuthEvent {

    private final Token token;

    public LoginEvent(Token token) {
        super(token);
        this.token = token;
    }

    public Token getTokenInfo() {
        return token;
    }
}
