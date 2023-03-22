package com.ds.screw.auth.context.domain;

/**
 * <p>
 *      [存储器] 包装类,在 Request 作用域里: 存值、取值
 * </p>
 *
 * @author dongsheng
 */
public interface AuthStorage {

    /**
     * 获取底层源对象
     *
     * @return see note
     */
    Object getSource();

    /**
     * 在 [Request作用域] 里写入一个值
     *
     * @param key   键
     * @param value 值
     */
    void set(String key, Object value);

    /**
     * 在 [Request作用域] 里获取一个值
     *
     * @param key 键
     * @return 值
     */
    Object get(String key);

    /**
     * 在 [Request作用域] 里删除一个值
     *
     * @param key 键
     */
    void remove(String key);

}
