package com.afinos.chatfire.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.afinos.api.config.UserProfile;
import com.afinos.api.helper.FireDBHelper;
import com.afinos.api.key.Action;
import com.afinos.chatfire.BR;
import com.afinos.chatfire.model.User;
import com.afinos.chatfire.model.UserRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by phearom on 7/13/16.
 */
public class UserViewModel extends BaseObservable {
    private User model;
    private boolean requesting;
    private boolean friend;

    public UserViewModel(User user) {
        this.model = user;
    }

    public String getName() {
        return this.model.getName();
    }

    public User getModel() {
        return model;
    }

    public View.OnClickListener onAddFriend() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRequest userRequest = new UserRequest();
                userRequest.setAction(Action.Friend_Request);
                userRequest.setUnixId(getModel().getId());
                userRequest.setRequestId(UserProfile.init(view.getContext()).getId());

                User user = new User();
                user.setId(UserProfile.init(view.getContext()).getId());
                user.setName(UserProfile.init(view.getContext()).getName());
                user.setEmail(UserProfile.init(view.getContext()).getEmail());

                userRequest.setUser(user);
                FireDBHelper.create(userRequest);
                FireDBHelper.doQuery(UserRequest.class).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        FireDBHelper.doQuery(User.class).child(getModel().getId()).child("action").setValue(Action.Friend_Request);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
    }

    @Bindable
    public boolean isRequesting() {
        return requesting;
    }

    public void setRequesting(boolean requesting) {
        this.requesting = requesting;
        notifyPropertyChanged(BR.requesting);
    }

    @Bindable
    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
        notifyPropertyChanged(BR.friend);
    }
}
