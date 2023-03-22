package com.ds.screw.auth.context.domain;

/**
 * <p>
 *      response对象包装类
 * </p>
 *
 * @author dongsheng
 */
public interface AuthResponse {

    /**
     * 获取底层源对象
     *
     * @return see note
     */
    Object getSource();


    /**
     * 设置响应状态码
     *
     * @param sc 响应状态码
     * @return 对象自身
     */
    AuthResponse setStatus(int sc);

    /**
     * 在响应头里写入一个值
     *
     * @param name  名字
     * @param value 值
     * @return 对象自身
     */
    AuthResponse setHeader(String name, String value);

    /**
     * 在响应头里添加一个值
     *
     * @param name  名字
     * @param value 值
     * @return 对象自身
     */
    AuthResponse addHeader(String name, String value);

    /**
     * 重定向
     *
     * @param url 重定向地址
     */
    void sendRedirect(String url);

}
