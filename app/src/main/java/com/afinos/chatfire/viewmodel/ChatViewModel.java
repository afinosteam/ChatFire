package com.afinos.chatfire.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.afinos.chatfire.BR;
import com.afinos.chatfire.model.Chat;

/**
 * Created by phearom on 7/13/16.
 */
public class ChatViewModel extends BaseObservable {
    private Chat model;
    private boolean me;

    public ChatViewModel(Chat user) {
        this.model = user;
    }

    public String getMessage() {
        return this.model.getMessage();
    }

    public String getAuthor() {
        return this.model.getAuthor();
    }

    @Bindable
    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
        notifyPropertyChanged(BR.me);
    }
}
