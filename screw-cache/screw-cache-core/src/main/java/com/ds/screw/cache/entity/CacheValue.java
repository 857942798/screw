package com.ds.screw.cache.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 缓存封装对象
 *
 * @author dongsheng
 */
public class CacheValue<K, V> implements Serializable {
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("all")
    protected K key;
    @SuppressWarnings("all")
    protected V obj;
    protected long ttl;
    protected volatile long lastAccess;
    @JsonIgnore
    protected AtomicLong accessCount = new AtomicLong();


    public CacheValue() {
    }

    public CacheValue(K key, V obj) {
        this.key = key;
        this.obj = obj;
        this.ttl = -1;
        this.lastAccess = System.currentTimeMillis();
    }

    public CacheValue(K key, V obj, long ttl) {
        this.key = key;
        this.obj = obj;
        this.ttl = ttl;
        this.lastAccess = System.currentTimeMillis();
    }

    public K getKey() {
        return this.key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getObj() {
        return obj;
    }

    public void setObj(V obj) {
        this.obj = obj;
    }

    public long getTtl() {
        return this.ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    @JsonIgnore
    public V getValue() {
        this.lastAccess = System.currentTimeMillis();
        this.accessCount.getAndIncrement();
        return this.obj;
    }

    @JsonIgnore
    public Date getExpiredTime() {
        return this.ttl > 0L ? new Date(this.lastAccess + this.ttl) : null;
    }

    @JsonIgnore
    public long getLastAccess() {
        return this.lastAccess;
    }

    @JsonIgnore
    public boolean isExpired() {
        if (this.ttl > 0L) {
            return System.currentTimeMillis() - this.lastAccess > this.ttl;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "CacheValue{" +
                "key=" + key +
                ", obj=" + obj +
                ", ttl=" + ttl +
                ", lastAccess=" + lastAccess +
                ", accessCount=" + accessCount +
                '}';
    }
}
