package com.afinos.chatfire.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afinos.api.base.BaseFragment;
import com.afinos.api.binder.CompositeItemBinder;
import com.afinos.api.binder.ItemBinder;
import com.afinos.api.config.UserProfile;
import com.afinos.api.listener.ClickHandler;
import com.afinos.api.listener.SimpleChildEventListener;
import com.afinos.chatfire.BR;
import com.afinos.chatfire.R;
import com.afinos.chatfire.binder.RecentBinder;
import com.afinos.chatfire.databinding.FragmentRecentMessageBinding;
import com.afinos.chatfire.model.Message;
import com.afinos.chatfire.model.RecentMessage;
import com.afinos.chatfire.model.User;
import com.afinos.chatfire.ui.ChatActivity;
import com.afinos.chatfire.viewmodel.RecentMessageViewModel;
import com.afinos.chatfire.viewmodel.RecentMessagesViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by phearom on 7/14/16.
 */
public class RecentFragment extends BaseFragment implements ChildEventListener {
    private FragmentRecentMessageBinding mBinding;
    private RecentMessagesViewModel mRecentMessagesViewModel;

    private DatabaseReference mRecentMessageRef;
    private DatabaseReference mUserRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recent_message, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setView(this);
        mBinding.setRecents(mRecentMessagesViewModel = new RecentMessagesViewModel());

        mRecentMessageRef = FirebaseDatabase.getInstance().getReference().child(RecentMessage.class.getSimpleName());
        mRecentMessageRef.child(UserProfile.init(getContext()).getId()).addChildEventListener(this);
        mUserRef = FirebaseDatabase.getInstance().getReference().child(User.class.getSimpleName());
    }


    public ItemBinder<RecentMessageViewModel> itemViewBinder() {
        return new CompositeItemBinder<>(
                new RecentBinder(BR.recent, R.layout.item_recent_message)
        );
    }

    public ClickHandler<RecentMessageViewModel> clickHandler() {
        return new ClickHandler<RecentMessageViewModel>() {
            @Override
            public void onClick(RecentMessageViewModel viewModel, View v) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("toId", viewModel.getUserId());
                intent.putExtra("toUser", viewModel.getUserName());
                startActivity(intent);
            }
        };
    }

    @Override
    public String getTitle() {
        return "Message";
    }

    @Override
    public Integer getIcon() {
        return null;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        final Message message = dataSnapshot.getValue(Message.class);
        Log.i("KEY", "User KEY : " + dataSnapshot.getKey());
        mUserRef.orderByKey().equalTo(dataSnapshot.getKey()).addChildEventListener(new SimpleChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                Log.i("KEY", "User : " + snapshot);
                User user = snapshot.getValue(User.class);
                RecentMessageViewModel recentMessageViewModel = new RecentMessageViewModel(message);
                recentMessageViewModel.setUserId(user.getId());
                recentMessageViewModel.setUserName(user.getName());
                mRecentMessagesViewModel.addItem(recentMessageViewModel);
            }
        });
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        final Message message = dataSnapshot.getValue(Message.class);
        Log.i("KEY", "User KEY : " + dataSnapshot.getKey());
        mUserRef.orderByKey().equalTo(dataSnapshot.getKey()).addChildEventListener(new SimpleChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                Log.i("KEY", "User : " + snapshot);
                User user = snapshot.getValue(User.class);
                RecentMessageViewModel recentMessageViewModel = new RecentMessageViewModel(message);
                recentMessageViewModel.setUserId(user.getId());
                recentMessageViewModel.setUserName(user.getName());
                mRecentMessagesViewModel.swapItem(recentMessageViewModel);
            }
        });
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