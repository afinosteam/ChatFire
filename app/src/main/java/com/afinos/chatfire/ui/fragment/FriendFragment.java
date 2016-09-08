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
import com.afinos.api.listener.ClickHandler;
import com.afinos.chatfire.BR;
import com.afinos.chatfire.R;
import com.afinos.chatfire.binder.FriendBinder;
import com.afinos.chatfire.databinding.FragmentFriendBinding;
import com.afinos.chatfire.model.User;
import com.afinos.chatfire.ui.ChatActivity;
import com.afinos.chatfire.viewmodel.FriendViewModel;
import com.afinos.chatfire.viewmodel.UserViewModel;
import com.afinos.chatfire.viewmodel.UsersViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by phearom on 7/14/16.
 */
public class FriendFragment extends BaseFragment implements ValueEventListener {
    private FragmentFriendBinding mBinding;
    private UsersViewModel mUsersViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setView(this);
        mBinding.setUsers(mUsersViewModel = new UsersViewModel());
        FireDBHelper.doQuery(User.class, this);
    }

    public ItemBinder<FriendViewModel> itemViewBinder() {
        return new CompositeItemBinder<>(
                new FriendBinder(BR.user, R.layout.item_friend)
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
    public void onDataChange(DataSnapshot dataSnapshot) {
        try {
            if (!dataSnapshot.exists()) {
            } else {
                mUsersViewModel.clear();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    User user = s.getValue(User.class);
                    if (!(UserProfile.init(getContext()).getId().equals(user.getId()))) {
                            mUsersViewModel.addItem(new FriendViewModel(user));
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

    @Override
    public String getTitle() {
        return "Friends";
    }

    @Override
    public Integer getIcon() {
        return null;
    }
}
