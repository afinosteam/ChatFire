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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by phearom on 7/14/16.
 */
public class FriendFragment extends BaseFragment implements ValueEventListener, ChildEventListener {
    private FragmentFriendBinding mBinding;
    private UsersViewModel mUsersViewModel;
    private DatabaseReference mUserRef;

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
        mUserRef = FirebaseDatabase.getInstance().getReference().child(User.class.getSimpleName());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mUserRef == null)
            return;
        mUserRef.addChildEventListener(this);
        mUserRef.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onStop() {
        super.onStop();
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
        mBinding.contentProgress.setVisibility(View.GONE);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        User user = dataSnapshot.getValue(User.class);
        if (!(UserProfile.init(getContext()).getId().equals(user.getId()))) {
            mUsersViewModel.addItem(new FriendViewModel(user));
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        User user = dataSnapshot.getValue(User.class);
        if (!(UserProfile.init(getContext()).getId().equals(user.getId()))) {
            mUsersViewModel.updateItem(new FriendViewModel(user));
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        if (!(UserProfile.init(getContext()).getId().equals(user.getId()))) {
            mUsersViewModel.removeItem(new FriendViewModel(user));
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
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
