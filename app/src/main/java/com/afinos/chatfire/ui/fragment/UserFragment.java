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
import com.afinos.chatfire.binder.FriendRequestBinder;
import com.afinos.chatfire.binder.UserBinder;
import com.afinos.chatfire.databinding.FragmentUserBinding;
import com.afinos.chatfire.model.User;
import com.afinos.chatfire.ui.ProfileActivity;
import com.afinos.chatfire.viewmodel.UserViewModel;
import com.afinos.chatfire.viewmodel.UsersViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by phearom on 7/14/16.
 */
public class UserFragment extends BaseFragment implements ValueEventListener {
    private FragmentUserBinding mBinding;
    private UsersViewModel mUsersViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setView(this);
        mBinding.setUsers(mUsersViewModel = new UsersViewModel());
        FireDBHelper.doQuery(User.class, this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        try {
            if (!dataSnapshot.exists()) {

            } else {
                mUsersViewModel.clear();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    if (!(UserProfile.init(getContext()).getId().equals(s.getKey()))) {
                        User user = s.getValue(User.class);
                        UserViewModel userViewModel = new UserViewModel(user);
                        userViewModel.setRequesting(user.getAction().equals(Action.Friend_Request));
                        boolean friend = user.getAction().equals(Action.Friend);
                        userViewModel.setFriend(friend);
                        mUsersViewModel.addItem(userViewModel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public ItemBinder<UserViewModel> itemViewBinder() {
        return new CompositeItemBinder<>(
                new UserBinder(BR.user, R.layout.item_user),
                new FriendRequestBinder(BR.user, R.layout.item_friend_request)
        );
    }

    public ClickHandler<UserViewModel> clickHandler() {
        return new ClickHandler<UserViewModel>() {
            @Override
            public void onClick(UserViewModel viewModel, View v) {
                ProfileActivity.launch(getContext(), viewModel.getModel());
            }
        };
    }

    @Override
    public String getTitle() {
        return "All";
    }

    @Override
    public Integer getIcon() {
        return null;
    }
}
