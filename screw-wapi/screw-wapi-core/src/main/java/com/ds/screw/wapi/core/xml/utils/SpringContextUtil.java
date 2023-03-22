package com.ds.screw.wapi.core.xml.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


/**
 * 获取spring容器，以访问容器中定义的其他bean
 *
 * @author dongsheng
 */
public class SpringContextUtil implements ApplicationContextAware {

    // Spring应用上下文环境
    private static ApplicationContext applicationContext;

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param applicationContext ApplicationContext
     */
    @Override
    @SuppressWarnings("all")
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 获取对象
     * 这里重写了bean方法，起主要作用
     *
     * @param name Bean Name
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 获取对象
     * 这里重写了bean方法，起主要作用
     *
     * @param name         Bean Name
     * @param requiredType Bean Class
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException BeansException
     */
    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(name, requiredType);
    }


    /**
     * 获取对象
     * 这里重写了bean方法，起主要作用
     *
     * @param name Bean Name
     * @param args Bean args
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException BeansException
     */
    public static Object getBean(String name, Object... args) throws BeansException {
        return applicationContext.getBean(name, args);
    }

    /**
     * 获取对象
     * 这里重写了bean方法，起主要作用
     *
     * @param requireType Bean Class
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException BeansException
     */
    public static <T> T getBean(Class<T> requireType) throws BeansException {
        return applicationContext.getBean(requireType);
    }

    /**
     * 获取对象
     * 这里重写了bean方法，起主要作用
     *
     * @param requireType Bean Class
     * @param args        Bean args
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException BeansException
     */
    public static <T> T getBean(Class<T> requireType, Object... args) throws BeansException {
        return applicationContext.getBean(requireType, args);
    }


    /**
     * 获取对象
     * 这里重写了bean方法，起主要作用
     *
     * @param requiredType Bean Class
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException BeansException
     * @see #getBean(Class)
     * @deprecated
     */
    @Deprecated
    public static <T> T getService(Class<T> requiredType) {
        return getBean(requiredType);
    }


    /**
     * @param clazz Bean Class
     * @param <T>   class
     * @return Object 一个以所给名字注册的bean的实例
     * @see #getBean(Class)
     * @deprecated
     */
    @Deprecated
    public static <T> T getBeanbyclass(Class<T> clazz) {
        return getBean(clazz);
    }

}
