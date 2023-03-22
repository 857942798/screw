package com.ds.screw.cache;

import java.util.function.Function;

/**
 * 通用缓存容器接口
 *
 * @author dongsheng
 */
public interface CacheContainer<K, V> {

    void put(K key, V value);

    void put(K key, V value, long timeout);

    V get(K key);

    V get(K key, Function<K, V> func);

    void remove(K key);

    void clear();

    boolean hasKey(K key);

}