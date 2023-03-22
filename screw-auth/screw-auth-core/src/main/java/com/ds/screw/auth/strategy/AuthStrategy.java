package com.ds.screw.auth.strategy;

import com.ds.screw.auth.AuthUtils;
import com.ds.screw.auth.annotation.*;
import com.ds.screw.auth.config.AuthConfig;
import com.ds.screw.auth.context.AuthContextHolder;
import com.ds.screw.auth.domain.LoginModel;
import com.ds.screw.auth.domain.TokenSign;
import com.ds.screw.auth.exception.UnLoginException;
import com.ds.screw.auth.session.AuthSession;
import com.ds.screw.auth.util.AuthFoxUtils;
import com.ds.screw.auth.util.TokenUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 *
 * @author dongsheng
 */
public class AuthStrategy {


    private AuthStrategy() {
    }

    /**
     * 获取 AuthStrategy 对象的单例引用
     */
    public static final AuthStrategy me = new AuthStrategy();

    //
    // 所有策略
    //
    /**
     * 判断：集合中是否包含指定元素（模糊匹配）
     * <p> 参数 [集合, 元素]
     */
    public BiFunction<List<String>, String, Boolean> hasElement = (list, element) -> {

        // 空集合直接返回false
        if(list == null || list.size() == 0) {
            return false;
        }

        // 先尝试一下简单匹配，如果可以匹配成功则无需继续模糊匹配
        if (list.contains(element)) {
            return true;
        }

        // 开始模糊匹配
        for (String patt : list) {
            if(AuthFoxUtils.vagueMatch(patt, element)) {
                return true;
            }
        }

        // 走出for循环说明没有一个元素可以匹配成功
        return false;
    };

    /**
     * 对一个 [Method] 对象进行注解校验 （注解鉴权内部实现）
     * <p> 参数 [Method句柄]
     */
    public Consumer<Method> checkMethodAnnotation = (method) -> {

        // 先校验 Method 所属 Class 上的注解
        me.checkElementAnnotation.accept(method.getDeclaringClass());

        // 再校验 Method 上的注解
        me.checkElementAnnotation.accept(method);
    };

    /**
     * 对一个 [元素] 对象进行注解校验 （注解鉴权内部实现）
     * <p> 参数 [element元素]
     */
    public Consumer<AnnotatedElement> checkElementAnnotation = (target) -> {
        // 校验 @AuthCheckLogin 注解
        AuthCheckLogin checkLogin = (AuthCheckLogin) AuthStrategy.me.getAnnotation.apply(target, AuthCheckLogin.class);
        if(checkLogin != null) {
            AuthUtils.authenticationLogic.checkByAnnotation(checkLogin);
        }

        // 校验 @AuthCheckRole 注解
        AuthCheckRole checkRole = (AuthCheckRole) AuthStrategy.me.getAnnotation.apply(target, AuthCheckRole.class);
        if(checkRole != null) {
            AuthUtils.authenticationLogic.checkByAnnotation(checkRole);
        }

        // 校验 @AuthCheckPermission 注解
        AuthCheckPermission checkPermission = (AuthCheckPermission) AuthStrategy.me.getAnnotation.apply(target, AuthCheckPermission.class);
        if(checkPermission != null) {
            AuthUtils.authenticationLogic.checkByAnnotation(checkPermission);
        }
    };

    /**
     * 从元素上获取注解（注解鉴权内部实现）
     * <p> 参数 [element元素，要获取的注解类型]
     */
    public BiFunction<AnnotatedElement, Class<? extends Annotation> , Annotation> getAnnotation = (element, annotationClass)->{
        // 默认使用jdk的注解处理器
        return element.getAnnotation(annotationClass);
    };

    /**
     * 判断：集合中是否包含指定元素（模糊匹配）
     * <p> 参数 [集合, 元素]
     * @param hasElement /
     * @return 对象自身
     */
    public AuthStrategy setHasElement(BiFunction<List<String>, String, Boolean> hasElement) {
        this.hasElement = hasElement;
        return this;
    }

    /**
     * 对一个 [Method] 对象进行注解校验 （注解鉴权内部实现）
     * <p> 参数 [Method句柄]
     * @param checkMethodAnnotation /
     * @return 对象自身
     */
    public AuthStrategy setCheckMethodAnnotation(Consumer<Method> checkMethodAnnotation) {
        this.checkMethodAnnotation = checkMethodAnnotation;
        return this;
    }

    /**
     * 对一个 [元素] 对象进行注解校验 （注解鉴权内部实现）
     * <p> 参数 [element元素]
     * @param checkElementAnnotation /
     * @return 对象自身
     */
    public AuthStrategy setCheckElementAnnotation(Consumer<AnnotatedElement> checkElementAnnotation) {
        this.checkElementAnnotation = checkElementAnnotation;
        return this;
    }

    /**
     * 从元素上获取注解（注解鉴权内部实现）
     * <p> 参数 [element元素，要获取的注解类型]
     * @param getAnnotation /
     * @return 对象自身
     */
    public AuthStrategy setGetAnnotation(BiFunction<AnnotatedElement, Class<? extends Annotation> , Annotation> getAnnotation) {
        this.getAnnotation = getAnnotation;
        return this;
    }

}
