package com.ds.screw.queue.action;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 注解指定需要订阅的主题
 *
 * @author dongsheng
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ActionQueueListener {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    int threadSize() default 1;

    int zoneSize() default 1;

    int pollSize() default 1;

    int maxWait() default 30000;

    boolean isDelay() default false;
}
