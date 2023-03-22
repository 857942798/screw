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
public class DefaultAuthEventPublisher implements AuthEventPublisher {

    @Override
    public void publishLogin(Token token) {
        //do nothing
    }

    @Override
    public void publishTempLogin(Token token) {
        //do nothing
    }

    @Override
    public void beforeLogout(Token token) {
        //do nothing
    }
}
