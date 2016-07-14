package com.afinos.chatfire.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afinos.api.base.BaseFragment;
import com.afinos.api.binder.CompositeItemBinder;
import com.afinos.api.binder.ItemBinder;
import com.afinos.api.config.UserProfile;
import com.afinos.api.helper.FireDBHelper;
import com.afinos.api.key.Action;
import com.afinos.api.listener.ClickHandler;
import com.afinos.chatfire.BR;
import com.afinos.chatfire.R;
import com.afinos.chatfire.binder.UserBinder;
import com.afinos.chatfire.databinding.FragmentFriendRequestBinding;
import com.afinos.chatfire.model.UserRequest;
import com.afinos.chatfire.ui.ChatActivity;
import com.afinos.chatfire.viewmodel.FriendRequestViewModel;
import com.afinos.chatfire.viewmodel.UserViewModel;
import com.afinos.chatfire.viewmodel.UsersViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by phearom on 7/14/16.
 */
public class FriendRequestFragment extends BaseFragment implements ChildEventListener {
    private FragmentFriendRequestBinding mBinding;
    private UsersViewModel mUsersViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_request, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setView(this);
        mBinding.setUsers(mUsersViewModel = new UsersViewModel());
        FireDBHelper.doQuery(UserRequest.class).addChildEventListener(this);
    }

    public ItemBinder<UserViewModel> itemViewBinder() {
        return new CompositeItemBinder<>(
                new UserBinder(BR.user, R.layout.item_friend_request)
        );
    }

    public ClickHandler<UserViewModel> clickHandler() {
        return new ClickHandler<UserViewModel>() {
            @Override
            public void onClick(UserViewModel viewModel, View v) {
                ChatActivity.launch(getContext(), viewModel.getModel());
            }
        };
    }

    @Override
    public String getTitle() {
        return "FriendRequest";
    }

    @Override
    public Integer getIcon() {
        return null;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        UserRequest user = dataSnapshot.getValue(UserRequest.class);
        if (user.getAction().equals(Action.Friend_Request)) {
            if (!user.getUser().getId().equals(UserProfile.init(getContext()).getId()))
                mUsersViewModel.addItem(new FriendRequestViewModel(user.getUser()));
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
