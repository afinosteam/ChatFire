package com.afinos.chatfire.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import java.util.List;

/**
 * Created by phearom on 7/13/16.
 */
public class UsersViewModel extends BaseObservable {
    @Bindable
    public ObservableList<UserViewModel> items;

    public UsersViewModel() {
        items = new ObservableArrayList<>();
    }

    public void addItem(UserViewModel userViewModel) {
        items.add(userViewModel);
    }

    public void updateItems(List<UserViewModel> userViewModels) {

    }

    public UserViewModel getItem(int pos) throws Exception {
        return items.get(pos);
    }

    public void clear() {
        items.clear();
    }
}
