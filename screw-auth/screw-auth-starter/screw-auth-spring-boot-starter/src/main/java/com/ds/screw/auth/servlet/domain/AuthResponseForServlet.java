package com.ds.screw.auth.servlet.domain;

import com.ds.screw.auth.context.domain.AuthResponse;
import com.ds.screw.auth.exception.AuthException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Response for Servlet
 *
 * @author dongsheng
 */
public class AuthResponseForServlet implements AuthResponse {

    /**
     * 底层Request对象
     */
    protected HttpServletResponse response;

    /**
     * 实例化
     *
     * @param response response对象
     */
    public AuthResponseForServlet(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * 获取底层源对象
     */
    @Override
    public Object getSource() {
        return response;
    }

    /**
     * 设置响应状态码
     */
    @Override
    public AuthResponse setStatus(int sc) {
        response.setStatus(sc);
        return this;
    }

    /**
     * 在响应头里写入一个值
     */
    @Override
    public AuthResponse setHeader(String name, String value) {
        response.setHeader(name, value);
        return this;
    }

    /**
     * 在响应头里添加一个值
     *
     * @param name  名字
     * @param value 值
     * @return 对象自身
     */
    @Override
    public AuthResponse addHeader(String name, String value) {
        response.addHeader(name, value);
        return this;
    }

    /**
     * 重定向
     */
    @Override
    public void sendRedirect(String url) {
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            throw new AuthException(e);
        }
    }
}
