package com.ds.screw.cache.entity;

import java.io.Serializable;

/**
 * 缓存键封装对象
 *
 * @author dongsheng
 */
public class CacheKey<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T value;

    public CacheKey() {
    }

    public CacheKey(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (this.getClass() == obj.getClass()) {
            CacheKey<?> that = (CacheKey<?>) obj;
            return this.value.equals(that.value);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.value == null ? 0 : this.value.hashCode();
    }

    public String toString() {
        return this.value == null ? "null" : this.value.toString();
    }


}