package com.ds.screw.auth.context;


import com.ds.screw.auth.context.domain.AuthRequest;
import com.ds.screw.auth.context.domain.AuthResponse;
import com.ds.screw.auth.context.domain.AuthStorage;
import com.ds.screw.auth.AuthManager;

/**
 * <p>
 *      上下文对象持有类
 * </p>
 *
 * @author dongsheng
 */
public class AuthContextHolder {

    private AuthContextHolder(){
    }

    /**
     * 获取当前请求的 上下文对象
     *
     * @return see note
     */
    public static AuthRequestContext getContext() {
        return AuthManager.getAuthRequestContext();
    }

    /**
     * 获取当前请求的 [Request] 对象
     *
     * @return see note
     */
    public static AuthRequest getRequest() {
        return AuthManager.getAuthRequestContext().getRequest();
    }

    /**
     * 获取当前请求的 [Response] 对象
     *
     * @return see note
     */
    public static AuthResponse getResponse() {
        return AuthManager.getAuthRequestContext().getResponse();
    }

    /**
     * 获取当前请求的 [存储器] 对象
     *
     * @return see note
     */
    public static AuthStorage getStorage() {
        return AuthManager.getAuthRequestContext().getStorage();
    }
}
