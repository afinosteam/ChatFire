package com.afinos.chatfire.model;

/**
 * @Copy Right 2012-2016, Afinos, Inc., or its affiliates
 * @Author: Afinos Team
 **/
public abstract class BaseContent<T> {
    public int getType() {
        return 0;
    }

    public abstract T getValue();
}
