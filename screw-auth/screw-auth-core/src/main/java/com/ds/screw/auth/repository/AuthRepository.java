package com.ds.screw.auth.repository;

/**
 * <p>
 *      定义数据存储行为
 * </p>
 *
 * @author dongsheng
 */
public interface AuthRepository {


    /**
     * 常量，表示一个key永不过期 (在一个key被标注为永远不过期时返回此值)
     */
    long NEVER_EXPIRE = -1;

    /**
     * 常量，表示系统中不存在这个缓存 (在对不存在的key获取剩余存活时间时返回此值)
     */
    long ALREADY_EXPIRE = -2;


    /**
     * 获取Object，如无返空
     *
     * @param key 键名称
     * @return object
     */
    Object get(String key);

    /**
     * 写入Value，并设定存活时间 (单位: 秒)
     *
     * @param key     键名称
     * @param value   值
     * @param timeout 过期时间(值大于0时限时存储，值=-1时永久存储，值=0或小于-2时不存储)
     */
    void set(String key, Object value, long timeout);

    /**
     * 更新Value (过期时间不变)
     *
     * @param key   键名称
     * @param value 值
     */
    void update(String key, Object value);

    /**
     * 删除Value
     *
     * @param key 键名称
     */
    void delete(String key);

    /**
     * 获取Value的剩余存活时间 (单位: 秒)
     *
     * @param key 指定key
     * @return 这个key的剩余存活时间
     */
    long getExpire(String key);

    /**
     * 修改Value的剩余存活时间 (单位: 秒)
     *
     * @param key     指定key
     * @param timeout 过期时间
     */
    void expire(String key, long timeout);
}
