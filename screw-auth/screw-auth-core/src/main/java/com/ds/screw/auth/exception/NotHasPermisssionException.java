package com.ds.screw.auth.exception;

import com.ds.screw.auth.util.AuthFoxUtils;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *      登录状态异常：token无效、过期、被顶下线，都会导致该异常产生
 * </p>
 *
 * @author dongsheng
 */
public class NotHasPermisssionException extends AuthException {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = -4208527742013027678L;


    /** 权限码 */
    private String permission;

    /**
     * @return 获得具体缺少的权限码
     */
    public String getPermission() {
        return permission;
    }


    public NotHasPermisssionException(String permission) {
        super("无此权限：" + permission);
        this.permission = permission;
    }
}
