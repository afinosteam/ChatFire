package com.afinos.chatfire.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.afinos.api.config.UserProfile;
import com.afinos.chatfire.model.Message;

import java.util.List;

/**
 * Created by phearom on 7/13/16.
 */
public class ChatsViewModel extends BaseObservable {
    private Context mContext;
    @Bindable
    public ObservableList<ChatViewModel> items;

    public ChatsViewModel(Context context) {
        mContext = context;
        items = new ObservableArrayList<>();
    }

    public void addItem(Message model) {
        ChatViewModel chatViewModel = new ChatViewModel(model);
        chatViewModel.setMe(model.getFromId().equals(UserProfile.init(mContext).getId()));
        items.add(chatViewModel);
    }

    public void addItem(ChatViewModel chatViewModel) {
        items.add(chatViewModel);
    }

    public void updateItems(List<ChatViewModel> chatsViewModel) {

    }

    public ChatViewModel getItem(int pos) throws Exception {
        return items.get(pos);
    }

    public void cleanup() {
        items.clear();
    }
}
