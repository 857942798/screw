package com.ds.screw.auth.spring.event;

import com.ds.screw.auth.domain.Token;
import com.ds.screw.auth.publisher.AuthEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
/**
 * 所有认证模块事件父类
 *
 * @author dongsheng
 */
public class AuthEventPublisherForSpring implements AuthEventPublisher, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    public AuthEventPublisherForSpring(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publishLogin(Token token) {
        this.applicationEventPublisher.publishEvent(new LoginEvent(token));
    }

    @Override
    public void publishTempLogin(Token token) {
        this.applicationEventPublisher.publishEvent(new TempLoginEvent(token));
    }

    @Override
    public void beforeLogout(Token token) {
        this.applicationEventPublisher.publishEvent(new BeforeLogoutEvent(token));
    }
}
