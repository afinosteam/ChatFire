package com.afinos.chatfire.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

/**
 * Created by phearom on 7/13/16.
 */
public class UsersViewModel extends BaseObservable {
    @Bindable
    public ObservableList<UserViewModel> items;

    public UsersViewModel() {
        items = new ObservableArrayList<>();
    }

    public void addItem(UserViewModel item) {
        if (items.size() > 0) {
            int index = items.indexOf(item);
            if (index > -1)
                items.set(index, item);
            else
                items.add(item);
        } else {
            items.add(item);
        }
    }

    public void updateItem(UserViewModel item) {
        int index = items.indexOf(item);
        if (index > 0) {
            items.set(index, item);
        }
    }

    public UserViewModel getItem(int pos) throws Exception {
        return items.get(pos);
    }

    public void clear() {
        items.clear();
    }

    public void removeItem(UserViewModel item) {
        this.items.remove(item);
    }
}
