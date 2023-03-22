package com.ds.screw.auth.spring.event;

import org.springframework.context.ApplicationEvent;
/**
 * 所有认证模块事件父类
 *
 * @author dongsheng
 */
public abstract class AuthEvent extends ApplicationEvent {

    protected AuthEvent(Object source) {
        super(source);
    }
}
