package com.afinos.chatfire.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import java.util.List;

/**
 * Created by phearom on 7/13/16.
 */
public class ChatsViewModel extends BaseObservable {
    @Bindable
    public ObservableList<ChatViewModel> items;

    public ChatsViewModel() {
        items = new ObservableArrayList<>();
    }

    public void addItem(ChatViewModel chatViewModel) {
        items.add(chatViewModel);
    }

    public void updateItems(List<ChatViewModel> chatsViewModel) {

    }

    public ChatViewModel getItem(int pos) throws Exception {
        return items.get(pos);
    }

    public void clear() {
        items.clear();
    }
}
