package com.afinos.chatfire.viewmodel;

import android.databinding.BaseObservable;

import com.afinos.chatfire.model.Message;

/**
 * @Copy Right 2012-2016, Afinos, Inc., or its affiliates
 * @Author: Afinos Team
 **/
public class RecentMessageViewModel extends BaseObservable {
    private Message model;

    private String userName;
    private String userId;
    private String userProfile;

    public RecentMessageViewModel(Message model) {
        this.model = model;
    }

    public Message getModel() {
        return model;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getDateTime() {
        return model.getDateTime();
    }

    public String getContent() {
        return model.getContent();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            if (obj instanceof RecentMessageViewModel) {
                RecentMessageViewModel r = (RecentMessageViewModel) obj;
                if (r.getUserId().equals(getUserId()))
                    return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }
}
