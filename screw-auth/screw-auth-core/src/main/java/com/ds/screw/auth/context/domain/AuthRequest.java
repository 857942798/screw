package com.ds.screw.auth.context.domain;

import com.ds.screw.auth.exception.AuthException;
import com.ds.screw.auth.util.AuthFoxUtils;

/**
 * <p>
 *      request对象包装类
 * </p>
 *
 * @author dongsheng
 */
public interface AuthRequest {
    /**
     * 获取底层源对象
     */
    Object getSource();

    /**
     * 在 [请求体] 里获取一个值
     *
     * @param name 键
     * @return 值
     */
    String getParameter(String name);


    /**
     * 在 [请求头] 里获取一个值
     *
     * @param name 键
     * @return 值
     */
    String getHeader(String name);

    /**
     * 返回当前请求path (不包括上下文名称)
     *
     * @return see note
     */
    String getRequestPath();

    /**
     * 返回当前请求的url，不带query参数，例：http://xxx.com/test
     *
     * @return see note
     */
    String getRequestURL();

    /**
     * 返回当前请求的类型
     *
     * @return see note
     */
    String getMethod();

    /**
     * 此请求是否为Ajax请求
     *
     * @return see note
     */
    default boolean isAjax() {
        return getHeader("X-Requested-With") != null;
    }

    /**
     * 转发请求
     *
     * @param path 转发地址
     */
    default void forward(String path) {
        throw new AuthException("No implementation");
    }

    /**
     * 返回当前请求path是否为指定值
     * @param path path
     * @return see note
     */
    default boolean isPath(String path) {
        return getRequestPath().equals(path);
    }

    /**
     * 在 [请求体] 里获取一个值 （此值必须存在，否则抛出异常 ）
     * @param name 键
     * @return 参数值
     */
    default String getParameterNotNull(String name) {
        String paramValue = getParameter(name);
        if(AuthFoxUtils.isEmpty(paramValue)) {
            throw new AuthException("缺少参数：" + name);
        }
        return paramValue;
    }

    /**
     * 在 [请求头] 里获取一个值 （此值必须存在，否则抛出异常 ）
     * @param name 键
     * @return 参数值
     */
    default String getHeaderNotNull(String name) {
        String paramValue = getHeader(name);
        if(AuthFoxUtils.isEmpty(paramValue)) {
            throw new AuthException("缺少参数：" + name);
        }
        return paramValue;
    }

    /**
     * 检测请求是否提供了指定参数
     * @param name 参数名称
     * @return 是否提供
     */
    default boolean hasParameter(String name) {
        return AuthFoxUtils.isNotEmpty(getParameter(name));
    }

    /**
     * 在 [请求体] 里获取一个值，值为空时返回默认值
     * @param name 键
     * @param defaultValue 值为空时的默认值
     * @return 值
     */
    default String getParameter(String name, String defaultValue) {
        String value = getParameter(name);
        if(AuthFoxUtils.isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 返回当前请求来源的唯一标识，servlet环境可以使用ip作为标识
     */
    default String getAddress(){
        return "";
    }

}
