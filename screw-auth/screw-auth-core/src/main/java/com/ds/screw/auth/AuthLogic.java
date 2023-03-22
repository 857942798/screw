package com.ds.screw.auth;

import com.ds.screw.auth.annotation.*;
import com.ds.screw.auth.config.AuthConfig;
import com.ds.screw.auth.config.AuthConst;
import com.ds.screw.auth.context.AuthContextHolder;
import com.ds.screw.auth.context.domain.AuthRequest;
import com.ds.screw.auth.context.domain.AuthStorage;
import com.ds.screw.auth.domain.LoginModel;
import com.ds.screw.auth.domain.Token;
import com.ds.screw.auth.domain.TokenSign;
import com.ds.screw.auth.exception.*;
import com.ds.screw.auth.session.AuthSession;
import com.ds.screw.auth.strategy.AuthStrategy;
import com.ds.screw.auth.strategy.LoginStrategy;
import com.ds.screw.auth.util.AuthFoxUtils;
import com.ds.screw.auth.util.KeyUtils;
import com.ds.screw.auth.util.TokenUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 登录逻辑实现类
 * 
 * @author dongsheng
 */
public class AuthLogic {

    /**
     * 获取当前TokenValue
     *
     * @return 当前tokenValue
     */
    public String getTokenValue() {
        // 0. 获取相应对象
        AuthStorage storage = AuthContextHolder.getStorage();
        AuthRequest request = AuthContextHolder.getRequest();
        AuthConfig config = AuthManager.getConfig();
        String keyTokenName = AuthManager.getConfig().getTokenName();
        String tokenValue = null;

        // 1. 尝试从Storage里读取
        if (storage.get(AuthConst.JUST_CREATED) != null) {
            tokenValue = String.valueOf(storage.get(AuthConst.JUST_CREATED));
        }
        // 2. 尝试从请求体里面读取
        if (tokenValue == null && config.getIsReadBody()) {
            tokenValue = request.getParameter(keyTokenName);
        }
        // 3. 尝试从header里读取
        if (tokenValue == null && config.getIsReadHead()) {
            tokenValue = request.getHeader(keyTokenName);
        }
        // 4. 返回
        return tokenValue;
    }

    /**
     * 会话登录，并指定所有登录参数Model
     *
     * @param id         登录id，建议的类型：（long | int | String）
     * @param loginModel 此次登录的参数Model
     */
    public void login(String id, LoginModel loginModel) {
        if (AuthFoxUtils.isEmpty(id)) {
            throw new AuthException("账号id不能为空");
        }
        // 0. 前置检查：如果此账号已被封禁.
        if (isDisable(id)) {
            throw new DisableLoginException(id, getDisableTime(id));
        }

        // 1. 初始化 loginModel
        AuthConfig config = AuthManager.getConfig();
        loginModel.build(config);

        // 2. 生成一个token
        String tokenValue = LoginStrategy.getToken(id, loginModel);

        // 3. 将token保存到[存储器]里
        AuthContextHolder.getStorage().set(AuthConst.JUST_CREATED, tokenValue);

        // 4. 获取 User-Session
        AuthSession session = TokenUtils.getSessionByLoginId(id, true);
        // 续期 User-Session
        session.updateMinTimeout(loginModel.getTimeout());

        // 在 User-Session 上记录token签名
        session.addTokenSign(tokenValue, id, loginModel.getDeviceOrDefault());

        // 5. 持久化其它数据
        // 保存token-id索引
        TokenUtils.saveTokenValueToLoginIdMap(tokenValue, id, loginModel.getTimeout());

        // 记录Token的最近访问时间
        TokenUtils.createLastAccessedTime(tokenValue);

        // $$ 通知监听器，账号xxx 登录成功
        AuthManager.getAuthListener().onLogin(id, tokenValue, loginModel);

        // $$ 发布登录事件
        AuthManager.getAuthEventPublisher().publishLogin(getToken());

        // 6. 检查此账号会话数量是否超出最大值
        if (config.getMaxLoginCount() > 0) {
            logoutByMaxLoginCount(id, session, config.getMaxLoginCount());
        }
    }

    /**
     * 临时会话登录，并指定所有登录参数Model
     *
     * @param id         登录id，建议的类型：（long | int | String）
     * @param loginModel 此次登录的参数Model
     */
    public String createTempTokenValue(String id, LoginModel loginModel) {
        if (AuthFoxUtils.isEmpty(id)) {
            throw new AuthException("账号id不能为空");
        }

        // 1. 初始化 loginModel
        AuthConfig config = AuthManager.getConfig();
        loginModel.build(config);

        // 2. 生成一个token
        String tokenValue = TokenUtils.createTokenValue();

        // 4. 获取 User-Session
        AuthSession session = TokenUtils.createTempSessionByTokenValue(tokenValue, 60);

        // 在 User-Session 上记录token签名
        session.addTokenSign(tokenValue, id, loginModel.getDeviceOrDefault());

        // 5. 持久化其它数据
        // 保存token-id索引
        TokenUtils.saveTokenValueToLoginIdMap(tokenValue, "temp:" + id, 60);

        // $$ 发布登录事件
        Token info = new Token();
        info.setTokenName(TokenUtils.getTokenName());
        info.setTokenValue(tokenValue);
        info.setIsLogin(true);
        info.setLoginId(id);
        AuthManager.getAuthEventPublisher().publishTempLogin(info);

        return tokenValue;
    }

    /**
     * 会话注销
     */
    public void logout() {
        // 如果连token都没有，那么无需执行任何操作 
        String tokenValue = getTokenValue();
        if (AuthFoxUtils.isEmpty(tokenValue) || !isLogin()) {
            return;
        }
        // $$ 发布登出前事件，某某Token准备下线
        try {
            AuthManager.getAuthEventPublisher().beforeLogout(getToken());
        } catch (Exception e) {
            throw new LogoutFailedException();
        }
        // 从当前 [storage存储器] 里删除
        AuthContextHolder.getStorage().remove(AuthConst.JUST_CREATED);
        // 清除这个token的相关信息
        String loginId = TokenUtils.getLoginIdByTokenValue(tokenValue);
        // 1. 清理 token最近访问时间
        TokenUtils.removeLastAccessedTime(tokenValue);
        // 2. 清理 token-id索引
        TokenUtils.removeTokenValueToLoginIdMap(tokenValue);
        // 3. 无效 loginId 立即返回
        if (UnLoginException.isInValidLoginId(loginId)) {
            return;
        }
        // 4. 清理User-Session上的token签名 & 尝试注销User-Session
        AuthSession session = TokenUtils.getSessionByLoginId(loginId, false);
        if (session != null) {
            session.removeTokenSign(tokenValue);
        }
        // $$ 通知监听器，某某Token注销下线了
        AuthManager.getAuthListener().onLogout(loginId, tokenValue);
    }

    /**
     * 会话注销，根据账号id 和 设备类型
     *
     * @param loginId 账号id
     * @param device  设备类型 (填null代表注销所有设备类型)
     */
    public void logout(String loginId, String device) {
        AuthSession session = TokenUtils.getSessionByLoginId(loginId, false);
        if (session != null) {
            List<TokenSign> list = session.getTokenSignListByDevice(device);
            for (TokenSign tokenSign : list) {
                String tokenValue = tokenSign.getValue();
                if (isLogin()) {
                    // $$ 发布登出前事件，某某Token准备下线
                    AuthManager.getAuthEventPublisher().beforeLogout(getToken());
                }
                // 清理token签名
                session.removeTokenSign(tokenValue);
                // 清理 token最近访问时间
                TokenUtils.removeLastAccessedTime(tokenValue);
                // 删除token-id映射
                TokenUtils.removeTokenValueToLoginIdMap(tokenValue);
                // $$ 监听事件
                AuthManager.getAuthListener().onLogout(loginId, tokenValue);
            }
        }
    }

    /**
     * 会话注销，根据账号id 和 设备类型 和 最大同时在线数量
     *
     * @param loginId       账号id
     * @param session       此账号的 Session 对象，可填写null，框架将自动获取
     * @param maxLoginCount 保留最近的几次登录
     */
    private void logoutByMaxLoginCount(String loginId, AuthSession session, int maxLoginCount) {
        List<TokenSign> list = session.getTokenSignList();
        // 遍历操作
        for (int i = 0; i < list.size(); i++) {
            // 只操作前n条
            if (i >= list.size() - maxLoginCount) {
                continue;
            }
            String tokenValue = list.get(i).getValue();
            // 清理token签名
            session.removeTokenSign(tokenValue);
            // 清理 token最近访问时间
            TokenUtils.removeLastAccessedTime(tokenValue);
            // 删除token-id映射
            TokenUtils.removeTokenValueToLoginIdMap(tokenValue);
            // $$ 监听事件
            AuthManager.getAuthListener().onLogout(loginId, tokenValue);
        }
    }

    /**
     * 顶人下线，根据账号id 和 设备类型
     * <p> 当对方再次访问系统时，会抛出NotLoginException异常，场景值=-4 </p>
     *
     * @param loginId 账号id
     * @param device  设备类型 (填null代表顶替所有设备类型)
     */
    public void replaced(String loginId, String device) {
        AuthSession session = TokenUtils.getSessionByLoginId(loginId, false);
        if (session != null) {
            for (TokenSign tokenSign : session.getTokenSignListByDevice(device)) {
                String tokenValue = tokenSign.getValue();
                // 清理token签名
                session.removeTokenSign(tokenValue);
                // 清理 token最近访问时间
                TokenUtils.removeLastAccessedTime(tokenValue);
                // 将此 token 标记为已被顶替 
                TokenUtils.updateTokenValueToLoginIdMap(tokenValue, UnLoginException.BE_REPLACED);
                // $$ 通知监听器，某某Token被顶下线了
                AuthManager.getAuthListener().onReplaced(loginId, tokenValue);
            }
        }
    }

    /**
     * 当前会话是否已经登录
     *
     * @return 是否已登录
     */
    public boolean isLogin() {
        // 判断条件：不为null，并且不在异常项集合里 
        return getLoginId() != null;
    }

    /**
     * 检验当前会话是否已经登录，如未登录，则抛出异常
     * <p>
     * throws UnLoginException 未登录异常
     */
    public void checkLogin() throws UnLoginException {
        // 如果获取不到token，则抛出: 无token
        String tokenValue = getTokenValue();
        if (tokenValue == null) {
            throw UnLoginException.newInstance(UnLoginException.NOT_TOKEN);
        }
        // 查找此token对应loginId, 如果找不到则抛出：无效token
        String loginId = TokenUtils.getLoginIdByTokenValue(tokenValue);
        // 检查是否有效的账号Id
        UnLoginException.checkIsValidLoginId(loginId, tokenValue);


        // 检查token签名
        AuthSession session;
        // 临时token不做如下校验
        if (!loginId.startsWith("temp:")) {
            // 检查是否已经 [临时过期]
            if (TokenUtils.isTokenInActive(tokenValue)) {
                throw UnLoginException.newInstance(UnLoginException.TOKEN_TIMEOUT, tokenValue);
            }
            session = TokenUtils.getSessionByLoginId(loginId, false);

            if (session != null) {
                TokenSign sign = session.getTokenSign(tokenValue);
                if (null == sign) {
                    throw UnLoginException.newInstance(UnLoginException.INVALID_DEVICE_MESSAGE);
                } else {
                    String signAddress = sign.getAddress();
                    String reqAddress = AuthContextHolder.getRequest().getAddress();
                    List<String> localAddress = Arrays.asList("127.0.0.1", "0:0:0:0:0:0:0:1");
                    if (!signAddress.equals(reqAddress) && !(localAddress.contains(signAddress) && localAddress.contains(reqAddress))) {
                        throw UnLoginException.newInstance(UnLoginException.INVALID_DEVICE_MESSAGE);
                    }
                }
            }
            TokenUtils.updateLastAccessedTime(tokenValue);
        }
    }

    /**
     * 获取当前会话账号id, 如果未登录，则返回null
     *
     * @return 账号id
     */
    public String getLoginId() {
        String tokenValue = getTokenValue();
        return getLoginIdByTokenValue(tokenValue);
    }

    /**
     * 获取指定Token对应的账号id，如果未登录，则返回 null
     *
     * @param tokenValue token
     * @return 账号id
     */
    public String getLoginIdByTokenValue(String tokenValue) {
        // token为空时，直接返回null
        if (AuthFoxUtils.isEmpty(tokenValue)) {
            return null;
        }
        // loginId为无效值时，直接返回null
        String loginId = TokenUtils.getLoginIdByTokenValue(tokenValue);
        if (UnLoginException.isInValidLoginId(loginId)) {
            return null;
        }
        if (!loginId.startsWith("temp:")) {
            // 如果已经[临时过期]
            if (TokenUtils.isTokenInActive(tokenValue)) {
                return null;
            }
            // 执行到此，证明loginId已经是个正常的账号id了
            return loginId;
        } else {
            return loginId.substring(5);
        }
    }

    /**
     * 获取当前会话的Token信息
     *
     * @return token信息
     */
    public Token getToken() {
        Token info = new Token();
        info.setTokenName(TokenUtils.getTokenName());
        info.setTokenValue(getTokenValue());
        info.setIsLogin(isLogin());
        info.setLoginId(getLoginId());
        return info;
    }

    /**
     * 获取当前会话的AuthSession对象
     *
     * @return AuthSession
     */
    public AuthSession getSession() {
        String tokenValue = getTokenValue();
        if (isLogin()) {
            String loginId = getLoginId();
            if (loginId.startsWith("temp:")) {
                return TokenUtils.getTempSessionByTokenValue(tokenValue);
            } else {
                return TokenUtils.getSessionByLoginId(loginId, false);
            }
        }
        return null;
    }

    /**
     * 获取临时会话的AuthSession对象
     *
     * @return AuthSession
     */
    public AuthSession getTempSession(String tokenValue) {
        return TokenUtils.getTempSessionByTokenValue(tokenValue);
    }

    // ------------------- 账号封禁 -------------------

    /**
     * 封禁指定账号
     * <p> 此方法不会直接将此账号id踢下线，而是在对方再次登录时抛出`DisableLoginException`异常
     *
     * @param loginId     指定账号id
     * @param disableTime 封禁时间, 单位: 秒 （-1=永久封禁）
     */
    public void disable(String loginId, long disableTime) {
        // 标注为已被封禁 
        AuthManager.getAuthRepository().set(KeyUtils.getKeyForDisable(loginId), DisableLoginException.BE_VALUE, disableTime);
        // $$ 通知监听器 
        AuthManager.getAuthListener().onDisable(loginId, disableTime);
    }

    /**
     * 指定账号是否已被封禁 (true=已被封禁, false=未被封禁)
     *
     * @param loginId 账号id
     * @return see note
     */
    public boolean isDisable(String loginId) {
        return AuthManager.getAuthRepository().get(KeyUtils.getKeyForDisable(loginId)) != null;
    }

    /**
     * 获取指定账号剩余封禁时间，单位：秒（-1=永久封禁，-2=未被封禁）
     *
     * @param loginId 账号id
     * @return see note
     */
    public long getDisableTime(String loginId) {
        return AuthManager.getAuthRepository().getExpire(KeyUtils.getKeyForDisable(loginId));
    }

    /**
     * 解封指定账号
     *
     * @param loginId 账号id
     */
    public void enable(String loginId) {
        AuthManager.getAuthRepository().delete(KeyUtils.getKeyForDisable(loginId));
        // $$ 通知监听器 
        AuthManager.getAuthListener().onEnable(loginId);
    }

    // ------------------- User-Session 相关 -------------------

    /**
     * 获取指定账号id的User-Session，如果Session尚未创建，则新建并返回
     *
     * @param loginId 账号id
     * @return Session对象
     */
    public AuthSession getSessionByLoginId(String loginId) {
        return TokenUtils.getSessionByLoginId(loginId, true);
    }

    /**
     * 获取指定账号id的User-Session, 如果Session尚未创建，isCreate=是否新建并返回
     *
     * @param loginId  账号id
     * @param isCreate 是否新建
     * @return Session对象
     */
    public AuthSession getSessionByLoginId(String loginId, boolean isCreate) {
        return TokenUtils.getSessionByLoginId(loginId, isCreate);
    }

    // ------------------- 权限认证相关 -------------------

    /**
     * 获取：当前账号的权限码集合
     * @return /
     */
    public List<String> getPermissionList() {
        try {
            return getPermissionList(getLoginId());
        } catch (UnLoginException e) {
            return new ArrayList<>();
        }
    }
    /**
     * 获取：指定账号的权限码集合
     * @param loginId 指定账号id
     * @return /
     */
    public List<String> getPermissionList(Object loginId) {
        return AuthManager.getAuthCheckProvider().getPermissionList(loginId);
    }

    /**
     * 判断：当前账号是否含有指定权限, 返回true或false
     * @param permission 权限码
     * @return 是否含有指定权限
     */
    public boolean hasPermission(String permission) {
        return hasElement(getPermissionList(), permission);
    }

    /**
     * 判断：指定账号id是否含有指定权限, 返回true或false
     * @param loginId 账号id
     * @param permission 权限码
     * @return 是否含有指定权限
     */
    public boolean hasPermission(Object loginId, String permission) {
        return hasElement(getPermissionList(loginId), permission);
    }

    /**
     * 判断：集合中是否包含指定元素（模糊匹配）
     * @param list 集合
     * @param element 元素
     * @return /
     */
    public boolean hasElement(List<String> list, String element) {
        return AuthStrategy.me.hasElement.apply(list, element);
    }

    /**
     * 判断：当前账号是否含有指定角色标识 [指定多个，必须全部验证通过]
     * @param roleArray 角色标识数组
     * @return true或false
     */
    public boolean hasRoleAnd(String... roleArray){
        try {
            checkRoleAnd(roleArray);
            return true;
        } catch (UnLoginException | NotHasRoleException e) {
            return false;
        }
    }

    /**
     * 校验：当前账号是否含有指定角色标识 [指定多个，必须全部验证通过]
     * @param roleArray 角色标识数组
     */
    public void checkRoleAnd(String... roleArray){
        Object loginId = getLoginId();
        List<String> roleList = getRoleList(loginId);
        for (String role : roleArray) {
            if(!hasElement(roleList, role)) {
                throw new NotHasRoleException(role);
            }
        }
    }

    /**
     * 获取：当前账号的角色集合
     * @return /
     */
    public List<String> getRoleList() {
        try {
            return getRoleList(getLoginId());
        } catch (UnLoginException e) {
            return new ArrayList<>();
        }
    }

    /**
     * 获取：指定账号的角色集合
     * @param loginId 指定账号id
     * @return /
     */
    public List<String> getRoleList(Object loginId) {
        return AuthManager.getAuthCheckProvider().getRoleList(loginId);
    }

    /**
     * 判断：当前账号是否拥有指定角色, 返回true或false
     * @param role 角色标识
     * @return 是否含有指定角色标识
     */
    public boolean hasRole(String role) {
        return hasElement(getRoleList(), role);
    }

    /**
     * 判断：指定账号是否含有指定角色标识, 返回true或false
     * @param loginId 账号id
     * @param role 角色标识
     * @return 是否含有指定角色标识
     */
    public boolean hasRole(Object loginId, String role) {
        return hasElement(getRoleList(loginId), role);
    }

    // ------------------- 注解鉴权 -------------------

    /**
     * 根据注解(@AuthCheckLogin)鉴权
     * @param at 注解对象
     */
    public void checkByAnnotation(AuthCheckLogin at) {
        this.checkLogin();
    }

    /**
     * 根据注解(@AuthCheckRole)鉴权
     * @param at 注解对象
     */
    public void checkByAnnotation(AuthCheckRole at) {
        String[] roleArray = at.value();
        if(at.mode() == AuthMode.AND) {
            this.checkRoleAnd(roleArray);
        } else {
            this.checkRoleOr(roleArray);
        }
    }

    /**
     * 校验：当前账号是否含有指定角色标识 [指定多个，只要其一验证通过即可]
     * @param roleArray 角色标识数组
     */
    public void checkRoleOr(String... roleArray){
        Object loginId = getLoginId();
        List<String> roleList = getRoleList(loginId);
        for (String role : roleArray) {
            if(hasElement(roleList, role)) {
                // 有的话提前退出
                return;
            }
        }
        if(roleArray.length > 0) {
            throw new NotHasRoleException(roleArray[0]);
        }
    }

    /**
     * 根据注解(@AuthCheckPermission)鉴权
     * @param at 注解对象
     */
    public void checkByAnnotation(AuthCheckPermission at) {
        String[] permissionArray = at.value();
        try {
            if(at.mode() == AuthMode.AND) {
                this.checkPermissionAnd(permissionArray);
            } else {
                this.checkPermissionOr(permissionArray);
            }
        } catch (NotHasPermisssionException e) {
            // 权限认证未通过，再开始角色认证
            if(at.orRole().length > 0) {
                for (String role : at.orRole()) {
                    String[] rArr = AuthFoxUtils.convertStringToArray(role);
                    // 某一项role认证通过，则可以提前退出了，代表通过
                    if(hasRoleAnd(rArr)) {
                        return;
                    }
                }
            }
            throw e;
        }
    }

    /**
     * 校验：当前账号是否含有指定权限 [指定多个，必须全部验证通过]
     * @param permissionArray 权限码数组
     */
    public void checkPermissionAnd(String... permissionArray){
        Object loginId = getLoginId();
        List<String> permissionList = getPermissionList(loginId);
        for (String permission : permissionArray) {
            if(!hasElement(permissionList, permission)) {
                throw new NotHasPermisssionException(permission);
            }
        }
    }

    /**
     * 校验：当前账号是否含有指定权限 [指定多个，只要其一验证通过即可]
     * @param permissionArray 权限码数组
     */
    public void checkPermissionOr(String... permissionArray){
        Object loginId = getLoginId();
        List<String> permissionList = getPermissionList(loginId);
        for (String permission : permissionArray) {
            if(hasElement(permissionList, permission)) {
                // 有的话提前退出
                return;
            }
        }
        if(permissionArray.length > 0) {
            throw new NotHasPermisssionException(permissionArray[0]);
        }
    }


}
