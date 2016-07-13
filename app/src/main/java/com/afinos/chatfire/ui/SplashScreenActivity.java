package com.afinos.chatfire.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.afinos.chatfire.R;
import com.afinos.chatfire.databinding.ActivitySplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends BaseActivity {
    private ActivitySplashScreenBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);

        mBinding.imvLogo.postDelayed(new Runnable() {
            @Override
            public void run() {
                launch();
            }
        }, 1000);
    }

    private void launch() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
