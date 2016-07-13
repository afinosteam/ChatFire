package com.afinos.api.listener;

/**
 * Created by phearom on 3/16/16.
 */
public interface OnResponseListener<T,D> {
    void onRecordSuccess(T object, D duration);
}
