package com.afinos.chatfire.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.afinos.api.utils.DateFormatUtils;

import java.util.Collections;
import java.util.Comparator;

/**
 * @Copy Right 2012-2016, Afinos, Inc., or its affiliates
 * @Author: Afinos Team
 **/
public class RecentMessagesViewModel extends BaseObservable {
    public ObservableList<RecentMessageViewModel> items;

    public RecentMessagesViewModel() {
        items = new ObservableArrayList<>();
    }

    public void addItem(RecentMessageViewModel item) {
        if (items.size() > 0) {
            int index = items.indexOf(item);
            if (index > -1)
                items.set(index, item);
            else
                items.add(item);
        } else {
            items.add(item);
        }
        Collections.sort(items, new Comparator<RecentMessageViewModel>() {
            @Override
            public int compare(RecentMessageViewModel o1, RecentMessageViewModel o2) {
                return DateFormatUtils.compare(o2.getDateTime(), o1.getDateTime()) ? 1 : -1;
            }
        });
    }

    public void swapItem(RecentMessageViewModel item) {
        int index = items.indexOf(item);
        if (index > -1) {
            items.set(index, item);
            Collections.swap(items, index, 0);
        }
    }

    public void clear() {
        items.clear();
    }
}
