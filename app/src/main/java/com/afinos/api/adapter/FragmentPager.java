package com.afinos.api.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.afinos.api.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phearom on 7/14/16.
 */
public class FragmentPager<T extends BaseFragment> extends FragmentPagerAdapter {
    private List<T> items;

    public FragmentPager(FragmentManager fm) {
        super(fm);
        items = new ArrayList<>();
    }

    public void addItem(T item) {
        items.add(item);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return items.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }
}
