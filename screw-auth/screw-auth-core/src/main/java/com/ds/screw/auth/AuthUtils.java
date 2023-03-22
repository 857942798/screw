package com.ds.screw.auth;

import com.ds.screw.auth.domain.LoginModel;
import com.ds.screw.auth.domain.Token;
import com.ds.screw.auth.session.AuthSession;

import java.util.List;

/**
 * 会话管理工具类
 *
 * @author dongsheng
 */
public class AuthUtils {

    private AuthUtils() {
    }
    // ------------------- 会话管理相关-start-------------------

    public static final AuthLogic authenticationLogic = new AuthLogic();

    public static void login(String loginId) {
        authenticationLogic.login(loginId, new LoginModel());
    }

    public static void login(String loginId, LoginModel loginModel) {
        authenticationLogic.login(loginId, loginModel);
    }

    public static boolean isLogin() {
        return authenticationLogic.isLogin();
    }

    public static void checkLogin() {
        authenticationLogic.checkLogin();
    }

    public static Token getToken() {
        return authenticationLogic.getToken();
    }

    public static String getTokenValue() {
        return authenticationLogic.getTokenValue();
    }

    public static AuthSession getSession() {
        return authenticationLogic.getSession();
    }

    public static String getLoginId() {
        return getToken().getLoginId();
    }

    public static void logout() {
        authenticationLogic.logout();
    }

    public static void logout(String loginId) {
        authenticationLogic.logout(loginId,null);
    }

    public static AuthSession getTempSession(String tokenValue) {
        return authenticationLogic.getTempSession(tokenValue);
    }

    public static String createTempTokenValue(String loginId) {
        return authenticationLogic.createTempTokenValue(loginId, new LoginModel());
    }

    // ------------------- 会话管理相关-end-------------------

    // ------------------- 权限认证相关-start-------------------
    /**
     * 获取：当前账号的权限码集合
     * @return /
     */
    public static List<String> getPermissionList() {
        return authenticationLogic.getPermissionList();
    }

    /**
     * 获取：指定账号的权限码集合
     * @param loginId 指定账号id
     * @return /
     */
    public static List<String> getPermissionList(Object loginId) {
        return authenticationLogic.getPermissionList(loginId);
    }

    /**
     * 判断：当前账号是否含有指定权限, 返回true或false
     * @param permission 权限码
     * @return 是否含有指定权限
     */
    public static boolean hasPermission(String permission) {
        return authenticationLogic.hasPermission(permission);
    }
    /**
     * 判断：指定账号id是否含有指定权限, 返回true或false
     * @param loginId 账号id
     * @param permission 权限码
     * @return 是否含有指定权限
     */
    public static boolean hasPermission(Object loginId, String permission) {
        return authenticationLogic.hasPermission(loginId, permission);
    }

    /**
     * 获取：当前账号的角色集合
     * @return /
     */
    public static List<String> getRoleList() {
        return authenticationLogic.getRoleList();
    }

    /**
     * 获取：指定账号的角色集合
     * @param loginId 指定账号id
     * @return /
     */
    public static List<String> getRoleList(Object loginId) {
        return authenticationLogic.getRoleList(loginId);
    }

    /**
     * 判断：当前账号是否拥有指定角色, 返回true或false
     * @param role 角色标识
     * @return 是否含有指定角色标识
     */
    public static boolean hasRole(String role) {
        return authenticationLogic.hasRole(role);
    }

    /**
     * 判断：指定账号是否含有指定角色标识, 返回true或false
     * @param loginId 账号id
     * @param role 角色标识
     * @return 是否含有指定角色标识
     */
    public static boolean hasRole(Object loginId, String role) {
        return authenticationLogic.hasRole(loginId, role);
    }

    // ------------------- 权限认证相关-end-------------------
}
