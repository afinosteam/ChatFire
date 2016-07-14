package com.afinos.chatfire.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.afinos.api.key.K;
import com.afinos.chatfire.R;
import com.afinos.chatfire.databinding.ActivityProfileBinding;
import com.afinos.chatfire.model.User;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding mBinding;

    public static void launch(Context context, User user) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(K.NAME, user.getName());
        intent.putExtra(K.EMAIL, user.getEmail());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        mBinding.toolbar.setTitle(getIntent().getStringExtra(K.NAME));
        mBinding.toolbar.setSubtitle(getIntent().getStringExtra(K.EMAIL));

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}
