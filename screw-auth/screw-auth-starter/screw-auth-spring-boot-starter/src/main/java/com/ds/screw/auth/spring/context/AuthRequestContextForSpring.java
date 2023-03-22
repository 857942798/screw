package com.ds.screw.auth.spring.context;

import com.ds.screw.auth.context.AuthRequestContext;
import com.ds.screw.auth.context.domain.AuthRequest;
import com.ds.screw.auth.context.domain.AuthResponse;
import com.ds.screw.auth.context.domain.AuthStorage;
import com.ds.screw.auth.exception.AuthException;
import com.ds.screw.auth.servlet.domain.AuthRequestForServlet;
import com.ds.screw.auth.servlet.domain.AuthResponseForServlet;
import com.ds.screw.auth.servlet.domain.AuthStorageForServlet;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * RequestContextForSpring
 *
 * @author dongsheng
 */
public class AuthRequestContextForSpring implements AuthRequestContext {


    /**
     * 获取当前请求的Request对象
     */
    @Override
    public AuthRequest getRequest() {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            throw new AuthException("非Web上下文无法获取Request");
        }
        return new AuthRequestForServlet(servletRequestAttributes.getRequest());
    }

    /**
     * 获取当前请求的Response对象
     */
    @Override
    public AuthResponse getResponse() {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            throw new AuthException("非Web上下文无法获取Response");
        }
        return new AuthResponseForServlet(servletRequestAttributes.getResponse());
    }

    /**
     * 获取当前请求的 [存储器] 对象
     */
    @Override
    public AuthStorage getStorage() {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            throw new AuthException("非Web上下文无法获取Request");
        }
        return new AuthStorageForServlet(servletRequestAttributes.getRequest());
    }

}
