package com.ds.screw.auth.exception;

/**
 * <p>
 *      登录状态异常：token无效、过期、被顶下线，都会导致该异常产生
 * </p>
 *
 * @author dongsheng
 */
public class NotHasRoleException extends AuthException {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = -4208527742013027678L;

    /** 角色标识 */
    private String role;

    public String getRole() {
        return role;
    }

    public NotHasRoleException(String role) {
        super("无此角色：" + role);
        this.role = role;
    }
}
