package com.ds.screw.auth.context;

import com.ds.screw.auth.context.domain.AuthRequest;
import com.ds.screw.auth.context.domain.AuthResponse;
import com.ds.screw.auth.context.domain.AuthStorage;

/**
 * <p>
 *      请求的上下文对象
 * </p>
 *
 * @author dongsheng
 */
public interface AuthRequestContext {
    /**
     * 获取当前请求的 [Request] 对象
     *
     * @return see note
     */
    AuthRequest getRequest();

    /**
     * 获取当前请求的 [Response] 对象
     *
     * @return see note
     */
    AuthResponse getResponse();

    /**
     * 获取当前请求的 [存储器] 对象
     *
     * @return see note
     */
    AuthStorage getStorage();
}
