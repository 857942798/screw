package com.ds.screw.auth.session;


import com.ds.screw.auth.AuthManager;
import com.ds.screw.auth.domain.TokenSign;
import com.ds.screw.auth.repository.AuthRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *     存储系统交互过程中一些高频读写数据,屏蔽不同系统会话对象的区别
 * </p>
 *
 * @author dongsheng
 */
public class AuthSession implements Serializable {

    private static final long serialVersionUID = 4598398463395360304L;

    /**
     * 此Session的id
     */
    private String id;

    /**
     * 此Session的创建时间
     */
    private long createTime;

    /**
     * 是否为临时session
     */
    private boolean isTemp = false;

    /**
     * 此Session绑定的token签名列表
     */
    private final List<TokenSign> tokenSignList = new ArrayList<>();

    /**
     * 此Session的所有挂载数据
     */
    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    /**
     * 无参构造
     */
    public AuthSession() {
    }

    /**
     * 构建一个Session对象
     *
     * @param id Session的id
     */
    public AuthSession(String id) {
        this.id = id;
        this.createTime = System.currentTimeMillis();
        createRepo(AuthManager.getConfig().getTimeout());
        // $$ 通知监听器
        AuthManager.getAuthListener().onSessionCreated(id);
    }

    /**
     * 构建一个Session对象
     *
     * @param id Session的id
     */
    public AuthSession(String id, long timeout, boolean isTemp) {
        this.id = id;
        this.createTime = System.currentTimeMillis();
        createRepo(timeout);
        this.isTemp = isTemp;
        if (!this.isTemp) {
            // $$ 通知监听器
            AuthManager.getAuthListener().onSessionCreated(id);
        }
    }

    /**
     * 获取此Session的id
     *
     * @return 此会话的id
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 返回当前会话创建时间
     *
     * @return 时间戳
     */
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    /**
     * 是否临时会话
     *
     * @return boolean
     */
    public boolean isTemp() {
        return isTemp;
    }

    public void setTemp(boolean temp) {
        isTemp = temp;
    }

    /**
     * 返回token签名列表的拷贝副本
     *
     * @return token签名列表
     */
    public List<TokenSign> getTokenSignList() {
        return new ArrayList<>(tokenSignList);
    }

    /**
     * 返回token签名列表的拷贝副本，根据 device 筛选
     *
     * @param device 设备类型，填 null 代表不限设备类型
     * @return token签名列表
     */
    public List<TokenSign> getTokenSignListByDevice(String device) {
        List<TokenSign> list = new ArrayList<>();
        for (TokenSign tokenSign : getTokenSignList()) {
            if (device == null || tokenSign.getDevice().equals(device)) {
                list.add(tokenSign);
            }
        }
        return list;
    }

    /**
     * 返回token签名列表的拷贝副本，根据 device 筛选
     *
     * @param ip     IP地址，填 null 代表不限IP地址
     * @param device 设备类型，填 null 代表不限设备类型
     * @return token签名列表
     */
    public List<TokenSign> getTokenSignListByIpAndDevice(String ip, String device) {
        List<TokenSign> list = new ArrayList<>();
        for (TokenSign tokenSign : getTokenSignList()) {
            if (ip == null) {
                if (device == null) {
                    list.add(tokenSign);
                } else {
                    if (tokenSign.getDevice().equals(device)) {
                        list.add(tokenSign);
                    }
                }
            } else {
                if (tokenSign.getAddress().equals(ip)) {
                    if (device == null) {
                        list.add(tokenSign);
                    } else {
                        if (tokenSign.getDevice().equals(device)) {
                            list.add(tokenSign);
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 查找一个token签名
     *
     * @param tokenValue token值
     * @return 查找到的tokenSign
     */
    public TokenSign getTokenSign(String tokenValue) {
        for (TokenSign tokenSign : getTokenSignList()) {
            if (tokenSign.getValue().equals(tokenValue)) {
                return tokenSign;
            }
        }
        return null;
    }

    /**
     * 添加一个token签名
     *
     * @param tokenSign token签名
     */
    public void addTokenSign(TokenSign tokenSign) {
        // 如果已经存在于列表中，则无需再次添加
        for (TokenSign tokenSign2 : getTokenSignList()) {
            if (tokenSign2.getValue().equals(tokenSign.getValue())) {
                return;
            }
        }
        // 添加并更新
        tokenSignList.add(tokenSign);
        updateRepo();
    }

    /**
     * 添加一个token签名
     *
     * @param tokenValue token值
     * @param device     设备类型
     */
    public void addTokenSign(String tokenValue, String loginId, String device) {
        addTokenSign(new TokenSign(tokenValue, loginId, device));
    }

    /**
     * 移除一个token签名
     *
     * @param tokenValue token名称
     */
    public void removeTokenSign(String tokenValue) {
        TokenSign tokenSign = getTokenSign(tokenValue);
        if (tokenSignList.remove(tokenSign)) {
            updateRepo();
        }
        // 注销 Session
        logoutByTokenSignCountToZero();
    }

    /******************/

    /**
     * 注销Session (从持久库删除)
     */
    public void invalidate() {
        deleteRepo();
        if (!this.isTemp) {
            // $$ 通知监听器
            AuthManager.getAuthListener().onSessionInvalidate(id);
        }
    }

    /**
     * 当Session上的tokenSign数量为零时，注销会话
     */
    private void logoutByTokenSignCountToZero() {
        if (tokenSignList.isEmpty()) {
            invalidate();
        }
    }

    /**
     * 获取此Session的剩余存活时间 (单位: 秒)
     *
     * @return 此Session的剩余存活时间 (单位: 秒)
     */
    public long timeout() {
        return AuthManager.getAuthRepository().getExpire(this.id);
    }

    /**
     * 修改此Session的剩余存活时间
     *
     * @param timeout 过期时间 (单位: 秒)
     */
    public void updateTimeout(long timeout) {
        AuthManager.getAuthRepository().expire(this.id, timeout);
    }

    /**
     * 修改此Session的最小剩余存活时间 (只有在Session的过期时间低于指定的minTimeout时才会进行修改)
     *
     * @param minTimeout 过期时间 (单位: 秒)
     */
    public void updateMinTimeout(long minTimeout) {
        long min = trans(minTimeout);
        long curr = trans(timeout());
        if (curr < min) {
            updateTimeout(minTimeout);
        }
    }

    /**
     * value为 -1 时返回 Long.MAX_VALUE，否则原样返回
     *
     * @param value /
     * @return /
     */
    protected long trans(long value) {
        return value == AuthRepository.NEVER_EXPIRE ? Long.MAX_VALUE : value;
    }

    /******************/

    /**
     * 获取所有的session会话属性值价值对象
     *
     * @return map
     */
    public Map<String, Object> getAttributes() {
        return new ConcurrentHashMap<>(attributes);
    }

    /**
     * 取值
     *
     * @param key key
     * @return 值
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * 写值
     *
     * @param key   名称
     * @param value 值
     */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
        updateRepo();
    }

    /**
     * 删值
     *
     * @param key 要删除的key
     */
    public void removeAttribute(String key) {
        attributes.remove(key);
        updateRepo();
    }

    /******************/

    private void deleteRepo() {
        AuthManager.getAuthRepository().delete(this.id);
    }

    private void createRepo(long timeout) {
        AuthManager.getAuthRepository().set(this.id, this, timeout);
    }

    /**
     * 更新Session（从持久库更新刷新一下）
     */
    private void updateRepo() {
        AuthManager.getAuthRepository().update(this.id, this);
    }
}
