package com.zitt.cdx.domain;

/**
 *
 * @param <T>
 */
public abstract class BuilderSupport<T> {

    private T object;

    protected BuilderSupport(T object) {
        this.object = object;
    }

    protected final T getObject() {
        assertObjectNotBuilt();
        return object;
    }

    public T build() {
        assertObjectNotBuilt();
        T data = this.object;
        this.object = null;
        return data;
    }

    private void assertObjectNotBuilt() {
        if (object == null) {
            throw new IllegalStateException("Object already built");
        }
    }
}

