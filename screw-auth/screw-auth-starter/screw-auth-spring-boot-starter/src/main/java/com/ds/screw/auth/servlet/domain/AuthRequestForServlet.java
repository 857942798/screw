package com.ds.screw.auth.servlet.domain;

import com.ds.screw.auth.AuthManager;
import com.ds.screw.auth.context.domain.AuthRequest;
import com.ds.screw.auth.exception.AuthException;
import com.ds.screw.auth.util.AuthFoxUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Request for Servlet
 *
 * @author dongsheng
 */
public class AuthRequestForServlet implements AuthRequest {

    /**
     * 底层Request对象
     */
    protected HttpServletRequest request;

    /**
     * 实例化
     *
     * @param request request对象
     */
    public AuthRequestForServlet(HttpServletRequest request) {
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
     * 在 [请求体] 里获取一个值
     */
    @Override
    public String getParameter(String name) {
        return request.getParameter(name);
    }

    /**
     * 在 [请求头] 里获取一个值
     */
    @Override
    public String getHeader(String name) {
        return request.getHeader(name);
    }

    /**
     * 返回当前请求path (不包括上下文名称)
     */
    @Override
    public String getRequestPath() {
        return request.getServletPath();
    }

    /**
     * 返回当前请求的url，例：http://xxx.com/test
     *
     * @return see note
     */
    @Override
    public String getRequestURL() {
        String currDomain = AuthManager.getConfig().getDomain();
        if (!AuthFoxUtils.isEmpty(currDomain)) {
            return currDomain + this.getRequestPath();
        }
        return request.getRequestURL().toString();
    }

    /**
     * 返回当前请求的类型
     */
    @Override
    public String getMethod() {
        return request.getMethod();
    }

    /**
     * 转发请求
     */
    @Override
    public void forward(String path) {
        try {
            HttpServletResponse response = (HttpServletResponse) AuthManager.getAuthRequestContext().getResponse().getSource();
            request.getRequestDispatcher(path).forward(request, response);
        } catch (ServletException | IOException e) {
            throw new AuthException(e);
        }
    }

    /**
     * 返回当前请求的来源标识,servlet环境使用ip作为标识
     */
    @Override
    public String getAddress() {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String s : ips) {
                if (!("unknown".equalsIgnoreCase(s))) {
                    ip = s;
                    break;
                }
            }
        }
        return ip;
    }
}
