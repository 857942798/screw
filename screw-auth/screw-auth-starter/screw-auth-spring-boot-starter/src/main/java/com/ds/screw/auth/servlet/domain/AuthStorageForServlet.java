package com.ds.screw.auth.servlet.domain;

import com.ds.screw.auth.context.domain.AuthStorage;

import javax.servlet.http.HttpServletRequest;

/**
 * Storage for Servlet
 *
 * @author dongsheng
 */
public class AuthStorageForServlet implements AuthStorage {

    /**
     * 底层Request对象
     */
    protected HttpServletRequest request;

    /**
     * 实例化
     *
     * @param request request对象
     */
    public AuthStorageForServlet(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 获取底层源对象
     */
    @Override
    public Object getSource() {
        return request;
    }

    /**
     * 在 [Request作用域] 里写入一个值
     */
    @Override
    public void set(String key, Object value) {
        request.setAttribute(key, value);
    }

    /**
     * 在 [Request作用域] 里获取一个值
     */
    @Override
    public Object get(String key) {
        return request.getAttribute(key);
    }

    /**
     * 在 [Request作用域] 里删除一个值
     */
    @Override
    public void remove(String key) {
        request.removeAttribute(key);
    }

}
