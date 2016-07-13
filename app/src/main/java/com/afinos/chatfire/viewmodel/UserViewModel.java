package com.afinos.chatfire.viewmodel;

import android.databinding.BaseObservable;

import com.afinos.chatfire.model.User;

/**
 * Created by phearom on 7/13/16.
 */
public class UserViewModel extends BaseObservable {
    private User model;

    public UserViewModel(User user) {
        this.model = user;
    }

    public String getName() {
        return this.model.getName();
    }

    public boolean isActive() {
        return this.model.isActive();
    }

    public User getModel() {
        return model;
    }
}
