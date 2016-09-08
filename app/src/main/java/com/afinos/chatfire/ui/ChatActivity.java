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
import com.afinos.api.listener.ClickHandler;
import com.afinos.api.listener.SimpleChildEventListener;
import com.afinos.api.utils.DateFormatUtils;
import com.afinos.chatfire.BR;
import com.afinos.chatfire.R;
import com.afinos.chatfire.binder.ChatBinder;
import com.afinos.chatfire.databinding.ActivityChatBinding;
import com.afinos.chatfire.model.Message;
import com.afinos.chatfire.model.RecentMessage;
import com.afinos.chatfire.model.User;
import com.afinos.chatfire.model.UserMessage;
import com.afinos.chatfire.viewmodel.ChatViewModel;
import com.afinos.chatfire.viewmodel.ChatsViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends BaseActivity implements ValueEventListener {
    private ActivityChatBinding mBinding;
    private ChatsViewModel chatsViewModel;
    private DatabaseReference mUserMessageRef;
    private DatabaseReference mMessageRef;
    private DatabaseReference mRecentMessageRef;
    private Query query;

    private String keyChat;

    private ChildEventListener mListener = new SimpleChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            UserMessage model = dataSnapshot.getValue(UserMessage.class);
            mMessageRef.orderByKey().equalTo(model.getMessageId()).addChildEventListener(new SimpleChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String s) {
                    Message message = snapshot.getValue(Message.class);
                    chatsViewModel.addItem(message);
                }
            });
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    public static void launch(Context context, User user) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("toId", user.getId());
        intent.putExtra("toUser", user.getName());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(toName());

//        keyChat = CharHelper.sort(toId() + UserProfile.init(this).getId());

        chatsViewModel = new ChatsViewModel(this);
        mBinding.setView(this);
        mBinding.setChats(chatsViewModel);

        mUserMessageRef = FirebaseDatabase.getInstance().getReference().child(UserMessage.class.getSimpleName());
        mMessageRef = FirebaseDatabase.getInstance().getReference().child(Message.class.getSimpleName());
        mRecentMessageRef = FirebaseDatabase.getInstance().getReference().child(RecentMessage.class.getSimpleName());

        query = mUserMessageRef.child(UserProfile.init(getApplicationContext()).getId()).child(toId());

        mBinding.chatSend.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(mBinding.chatText.getText().toString().trim()))
                            return;
                        Message message = new Message();
                        message.setToId(toId());
                        message.setToUser(toName());
                        message.setFromId(UserProfile.init(getApplicationContext()).getId());
                        message.setFromUser(UserProfile.init(getApplicationContext()).getName());
                        message.setContent(mBinding.chatText.getText().toString());
                        message.setDateTime(DateFormatUtils.currentTime());

                        mMessageRef.child(message.getUnixId()).setValue(message);

                        UserMessage userMessage = new UserMessage();
                        userMessage.setMessageId(message.getUnixId());

                        mUserMessageRef.child(toId()).child(UserProfile.init(getApplicationContext()).getId()).push().setValue(userMessage);
                        mUserMessageRef.child(UserProfile.init(getApplicationContext()).getId()).child(toId()).push().setValue(userMessage);

                        mRecentMessageRef.child(UserProfile.init(getApplicationContext()).getId()).child(toId()).setValue(message);
                        mRecentMessageRef.child(toId()).child(UserProfile.init(getApplicationContext()).getId()).setValue(message);

                        mBinding.chatText.setText("");
                        mBinding.recycler.smoothScrollToPosition(mBinding.recycler.getAdapter().getItemCount());
                    }
                }

        );
    }

    @Override
    public void onStart() {
        super.onStart();
        if (query == null) return;
        query = query.limitToLast(25);
        query.addChildEventListener(mListener);
        query.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (query == null)
            return;
        query.removeEventListener(mListener);
        query.removeEventListener(this);
    }

    public String toId() {
        return getIntent().getStringExtra("toId");
    }

    public String toName() {
        return getIntent().getStringExtra("toUser");
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

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mBinding.contentProgress.setVisibility(View.GONE);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        mBinding.contentProgress.setVisibility(View.GONE);
    }
}
