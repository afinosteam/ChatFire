package com.afinos.chatfire.viewmodel;

import com.afinos.chatfire.model.User;

/**
 * Created by phearom on 7/13/16.
 */
public class FriendViewModel extends UserViewModel {

    public FriendViewModel(User user) {
        super(user);
    }

    public boolean isActive() {
        return getModel().isActive();
    }
}
