package com.afinos.chatfire.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.afinos.api.binder.CompositeItemBinder;
import com.afinos.api.binder.ItemBinder;
import com.afinos.api.config.UserProfile;
import com.afinos.api.helper.FireDBHelper;
import com.afinos.api.listener.ClickHandler;
import com.afinos.chatfire.BR;
import com.afinos.chatfire.R;
import com.afinos.chatfire.binder.ChatBinder;
import com.afinos.chatfire.databinding.ActivityChatBinding;
import com.afinos.chatfire.model.Chat;
import com.afinos.chatfire.model.User;
import com.afinos.chatfire.viewmodel.ChatViewModel;
import com.afinos.chatfire.viewmodel.ChatsViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.UUID;

public class ChatActivity extends BaseActivity implements ChildEventListener {
    private ActivityChatBinding mBinding;
    private ChatsViewModel chatsViewModel;

    public static void launch(Context context, User user) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("toId", user.getId());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chatsViewModel = new ChatsViewModel();
        mBinding.setView(this);
        mBinding.setChats(chatsViewModel);

        FireDBHelper.doQuery(Chat.class).addChildEventListener(this);

        mBinding.chatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mBinding.chatText.getText().toString().trim()))
                    return;
                Chat chat = new Chat();
                chat.setId(UUID.randomUUID().toString());
                chat.setToId(toId());
                chat.setAuthorId(UserProfile.init(getApplicationContext()).getId());
                chat.setMessage(mBinding.chatText.getText().toString());
                chat.setAuthor(UserProfile.init(getApplicationContext()).getName());

                FireDBHelper.doQuery(Chat.class).child(chat.getId()).setValue(chat);
                mBinding.chatText.setText("");
            }
        });
    }

    public String toId() {
        return getIntent().getStringExtra("toId");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot.exists()) {
            Chat chat = dataSnapshot.getValue(Chat.class);
            chat.setId(dataSnapshot.getKey());

            ChatViewModel chatViewModel = new ChatViewModel(chat);
            if (chat.getAuthorId().equals(UserProfile.init(getApplicationContext()).getId()))
                chatViewModel.setMe(true);
            else
                chatViewModel.setMe(false);
            chatsViewModel.addItem(chatViewModel);
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

    public ItemBinder<ChatViewModel> itemViewBinder() {
        return new CompositeItemBinder<>(
                new ChatBinder(BR.chat, R.layout.item_chat)
        );
    }

    public ClickHandler<ChatViewModel> clickHandler() {
        return new ClickHandler<ChatViewModel>() {
            @Override
            public void onClick(ChatViewModel viewModel, View v) {

            }
        };
    }
}
