package com.afinos.chatfire.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

import com.afinos.api.binder.CompositeItemBinder;
import com.afinos.api.binder.ItemBinder;
import com.afinos.api.config.UserProfile;
import com.afinos.api.helper.FireDBHelper;
import com.afinos.api.listener.ClickHandler;
import com.afinos.chatfire.BR;
import com.afinos.chatfire.R;
import com.afinos.chatfire.binder.UserBinder;
import com.afinos.chatfire.databinding.ActivityMainBinding;
import com.afinos.chatfire.model.User;
import com.afinos.chatfire.viewmodel.UserViewModel;
import com.afinos.chatfire.viewmodel.UsersViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends BaseActivity implements ValueEventListener {
    private ActivityMainBinding mBinding;

    private ActionBarDrawerToggle barDrawerToggle;
    private UsersViewModel usersViewModel;

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

        usersViewModel = new UsersViewModel();
        mBinding.setUsers(usersViewModel);
        mBinding.setView(this);

        mBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSignOut();
                finish();
            }
        });

        FireDBHelper.doQuery(User.class, this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (!dataSnapshot.exists()) {

        } else {
            usersViewModel.clear();
            for (DataSnapshot s : dataSnapshot.getChildren()) {
                if (!(UserProfile.init(this).getId().equals(s.getKey()))) {
                    User user = s.getValue(User.class);
                    user.setId(s.getKey());
                    usersViewModel.addItem(new UserViewModel(user));
                }
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public ItemBinder<UserViewModel> itemViewBinder() {
        return new CompositeItemBinder<>(
                new UserBinder(BR.user, R.layout.item_user)
        );
    }

    public ClickHandler<UserViewModel> clickHandler() {
        return new ClickHandler<UserViewModel>() {
            @Override
            public void onClick(UserViewModel viewModel, View v) {
                ChatActivity.launch(MainActivity.this, viewModel.getModel());
            }
        };
    }
}
