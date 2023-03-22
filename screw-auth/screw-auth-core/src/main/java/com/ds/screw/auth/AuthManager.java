package com.ds.screw.auth;


import com.ds.screw.auth.config.AuthConfig;
import com.ds.screw.auth.context.AuthRequestContext;
import com.ds.screw.auth.json.AuthJsonTemplate;
import com.ds.screw.auth.json.DefaultAuthJsonTemplateImpl;
import com.ds.screw.auth.listener.AuthEventListener;
import com.ds.screw.auth.listener.DefaultAuthEventListener;
import com.ds.screw.auth.provider.AuthCheckProvider;
import com.ds.screw.auth.provider.DefaultAuthCheckProvider;
import com.ds.screw.auth.publisher.AuthEventPublisher;
import com.ds.screw.auth.publisher.DefaultAuthEventPublisher;
import com.ds.screw.auth.repository.AuthRepository;
import com.ds.screw.auth.repository.DefaultAuthRepository;

/**
 * 管理 auth模块的 所有全局组件
 *
 * @author dongsheng
 */
public class AuthManager {

    private AuthManager() {
    }

    /**
     * 配置文件 Bean
     */
    private static volatile AuthConfig config;
    /**
     * 持久化 Bean
     */
    private static volatile AuthRepository authRepository;
    /**
     * 上下文Context Bean
     */
    private static volatile AuthRequestContext authRequestContext;
    /**
     * 侦听器 Bean
     */
    private static volatile AuthEventListener authEventListener;
    /**
     * 事件发布 Bean
     */
    private static volatile AuthEventPublisher authEventPublisher;

    /**
     * JSON 转换器 Bean
     */
    private volatile static AuthJsonTemplate authJsonTemplate;
    /**
     * 权限认证 Bean
     */
    private volatile static AuthCheckProvider authCheckProvider;

    public static AuthConfig getConfig() {
        if (config == null) {
            synchronized (AuthManager.class) {
                if (config == null) {
                    setConfig(new AuthConfig());
                }
            }
        }
        return config;
    }

    public static void setConfig(AuthConfig config) {
        AuthManager.config = config;
    }

    public static AuthRepository getAuthRepository() {
        if (authRepository == null) {
            synchronized (AuthManager.class) {
                if (authRepository == null) {
                    setAuthRepository(new DefaultAuthRepository());
                }
            }
        }
        return authRepository;
    }

    public static void setAuthRepository(AuthRepository authRepository) {
        AuthManager.authRepository = authRepository;
    }

    public static AuthRequestContext getAuthRequestContext() {
        return authRequestContext;
    }

    public static void setAuthRequestContext(AuthRequestContext authRequestContext) {
        AuthManager.authRequestContext = authRequestContext;
    }

    public static AuthEventListener getAuthListener() {
        if (authEventListener == null) {
            synchronized (AuthManager.class) {
                if (authEventListener == null) {
                    setAuthListener(new DefaultAuthEventListener());
                }
            }
        }
        return authEventListener;
    }

    public static void setAuthListener(AuthEventListener authEventListener) {
        AuthManager.authEventListener = authEventListener;
    }

    public static AuthEventPublisher getAuthEventPublisher() {
        if (authEventPublisher == null) {
            synchronized (AuthManager.class) {
                if (authEventPublisher == null) {
                    setAuthEventPublisher(new DefaultAuthEventPublisher());
                }
            }
        }
        return authEventPublisher;
    }

    public static void setAuthEventPublisher(AuthEventPublisher authEventPublisher) {
        AuthManager.authEventPublisher = authEventPublisher;
    }


    public static void setAuthJsonTemplate(AuthJsonTemplate authJsonTemplate) {
        AuthManager.authJsonTemplate = authJsonTemplate;
    }
    public static AuthJsonTemplate getAuthJsonTemplate() {
        if (authJsonTemplate == null) {
            synchronized (AuthManager.class) {
                if (authJsonTemplate == null) {
                    setAuthJsonTemplate(new DefaultAuthJsonTemplateImpl());
                }
            }
        }
        return authJsonTemplate;
    }

    public static void setAuthCheckProvider(AuthCheckProvider authCheckProvider) {
        AuthManager.authCheckProvider = authCheckProvider;
    }
    public static AuthCheckProvider getAuthCheckProvider() {
        if (authCheckProvider == null) {
            synchronized (AuthManager.class) {
                if (authCheckProvider == null) {
                    setAuthCheckProvider(new DefaultAuthCheckProvider());
                }
            }
        }
        return authCheckProvider;
    }
}
