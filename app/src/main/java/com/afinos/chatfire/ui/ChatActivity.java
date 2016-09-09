package com.afinos.chatfire.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.afinos.api.binder.CompositeItemBinder;
import com.afinos.api.binder.ItemBinder;
import com.afinos.api.config.UserProfile;
import com.afinos.api.helper.CharHelper;
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
import com.afinos.chatfire.viewmodel.ChatViewModel;
import com.afinos.chatfire.viewmodel.ChatsViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends BaseActivity {
    private ActivityChatBinding mBinding;
    private ChatsViewModel chatsViewModel;
    private DatabaseReference mUserRef;
    private DatabaseReference mMessageRef;
    private DatabaseReference mRecentMessageRef;
    private Query query;

    private String keyChat;

    private int loaded = 0;

    private ChildEventListener mListener = new SimpleChildEventListener() {
        @Override
        public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
            loaded++;
            final Message message = dataSnapshot.getValue(Message.class);
            String id = message.getToId();
            if (message.getFromId().equals(UserProfile.init(getApplicationContext()).getId())) {
                ChatViewModel model = new ChatViewModel(message);
                chatsViewModel.addItem(model);
            } else {
                mUserRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        ChatViewModel model = new ChatViewModel(message);
                        model.setProfile(user.getProfile());
                        chatsViewModel.addItem(model);
                        Log.i("item", "item : " + snapshot.toString());
                        loaded--;
                        if (loaded == 0) {
                            Log.i("item", "done");
                            mBinding.contentProgress.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            Log.i("item", "item : " + s);
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

        keyChat = CharHelper.sort(toId() + UserProfile.init(this).getId());
        mUserRef = FirebaseDatabase.getInstance().getReference().child(User.class.getSimpleName());

        chatsViewModel = new ChatsViewModel(this);
        mBinding.setView(this);
        mBinding.setChats(chatsViewModel);

        mMessageRef = FirebaseDatabase.getInstance().getReference().child(Message.class.getSimpleName());
        mRecentMessageRef = FirebaseDatabase.getInstance().getReference().child(RecentMessage.class.getSimpleName());

        query = mMessageRef.orderByChild("keyChat").equalTo(keyChat);

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
                        message.setKeyChat(keyChat);
                        message.setContent(mBinding.chatText.getText().toString());
                        message.setDateTime(DateFormatUtils.currentTime());

                        mMessageRef.push().setValue(message);

                        mRecentMessageRef.child(UserProfile.init(getApplicationContext()).getId()).child(toId()).setValue(message);
                        mRecentMessageRef.child(toId()).child(UserProfile.init(getApplicationContext()).getId()).setValue(message);

                        mBinding.chatText.setText("");
                        if (mBinding.recycler.getAdapter().getItemCount() > 0)
                            mBinding.recycler.smoothScrollToPosition(mBinding.recycler.getAdapter().getItemCount() - 1);
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
    }

    @Override
    public void onStop() {
        super.onStop();
        if (query == null)
            return;
        query.removeEventListener(mListener);
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
}
