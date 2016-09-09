package com.afinos.chatfire.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.afinos.chatfire.BR;
import com.afinos.chatfire.model.Message;

/**
 * Created by phearom on 7/13/16.
 */
public class ChatViewModel extends BaseObservable {
    private Message model;
    private boolean me;
    private String profile;

    public ChatViewModel(Message message) {
        this.model = message;
    }

    public Message getModel() {
        return this.model;
    }

    public String getContent() {
        return this.model.getContent();
    }

    public String getFromUser() {
        return this.model.getFromUser();
    }

    @Bindable
    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
        notifyPropertyChanged(BR.me);
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
