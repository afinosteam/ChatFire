package com.afinos.chatfire.viewmodel;

import android.databinding.Bindable;
import android.view.View;

import com.afinos.api.config.UserProfile;
import com.afinos.api.helper.FireDBHelper;
import com.afinos.api.key.Action;
import com.afinos.chatfire.BR;
import com.afinos.chatfire.model.User;
import com.afinos.chatfire.model.UserRequest;

import java.util.Arrays;

/**
 * Created by phearom on 7/13/16.
 */
public class FriendRequestViewModel extends UserViewModel {

    private boolean confirmed;

    public FriendRequestViewModel(User user) {
        super(user);
    }

    public View.OnClickListener onConfirm() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = UserProfile.init(view.getContext()).getId();
                FireDBHelper.doQuery(User.class).child(id).child("chatIds").setValue(Arrays.asList(id));
                FireDBHelper.doQuery(User.class).child(id).child("action").setValue(Action.Friend);
                FireDBHelper.doQuery(User.class).child(getModel().getId()).child("chatIds").setValue(Arrays.asList(id));
                FireDBHelper.doQuery(User.class).child(getModel().getId()).child("action").setValue(Action.Friend);

                UserProfile.init(view.getContext()).saveData("chatId", id);

                FireDBHelper.doQuery(UserRequest.class).removeValue();
            }
        };
    }

    @Bindable
    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
        notifyPropertyChanged(BR.confirmed);
    }
}
