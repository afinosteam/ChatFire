package com.afinos.chatfire.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.afinos.api.adapter.FragmentPager;
import com.afinos.api.base.BaseFragment;
import com.afinos.chatfire.R;
import com.afinos.chatfire.databinding.ActivityMainBinding;
import com.afinos.chatfire.ui.fragment.FriendFragment;
import com.afinos.chatfire.ui.fragment.RecentFragment;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding mBinding;
    private ActionBarDrawerToggle barDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(mBinding.toolbar);
        barDrawerToggle = new ActionBarDrawerToggle(this, mBinding.drawer, 0, 0);
        mBinding.drawer.addDrawerListener(barDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        barDrawerToggle.syncState();

        mBinding.tab.setupWithViewPager(mBinding.viewPager);

        FragmentPager<BaseFragment> fragmentPager = new FragmentPager<>(getSupportFragmentManager());
        mBinding.viewPager.setAdapter(fragmentPager);

        fragmentPager.addItem(new FriendFragment());
        fragmentPager.addItem(new RecentFragment());

        mBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSignOut();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mBinding.drawer.openDrawer(Gravity.LEFT);
                barDrawerToggle.syncState();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
