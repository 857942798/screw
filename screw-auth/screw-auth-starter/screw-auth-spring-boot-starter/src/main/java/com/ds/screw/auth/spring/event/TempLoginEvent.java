package com.ds.screw.auth.spring.event;

import com.ds.screw.auth.domain.Token;

/**
 * 临时登录成功事件
 *
 * @author dongsheng
 */
public class TempLoginEvent extends AuthEvent {

    private final Token token;

    public TempLoginEvent(Token token) {
        super(token);
        this.token = token;
    }

    public Token getTokenInfo() {
        return token;
    }
}
