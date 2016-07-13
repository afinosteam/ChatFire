package com.afinos.chatfire.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.afinos.api.config.UserProfile;
import com.afinos.api.helper.FireDBHelper;
import com.afinos.chatfire.model.User;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by phearom on 7/13/16.
 */
public abstract class BaseActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    private static final String TAG = BaseActivity.class.getSimpleName();
    protected FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(this);
    }

    @Override
    public void onStart() {
        mAuth.addAuthStateListener(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        mAuth.removeAuthStateListener(this);
        super.onStop();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            UserProfile.init(this).setId(firebaseUser.getUid());
            UserProfile.init(this).setName(firebaseUser.getDisplayName());
            UserProfile.init(this).setEmail(firebaseUser.getEmail());

            User user = new User();
            user.setId(firebaseUser.getUid());
            user.setActive(true);
            user.setEmail(firebaseUser.getEmail());
            user.setName(firebaseUser.getDisplayName());

            FireDBHelper.create(user);
            onSignIn(firebaseUser);
        } else {
            onSignOut();
        }
    }

    protected void onSignIn(FirebaseUser user) {
        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
    }

    protected void onSignOut() {
        Log.d(TAG, "onAuthStateChanged:signed_out:");
    }

    protected void doSignOut() {
        FireDBHelper.doUpdate(User.class).child(UserProfile.init(this).getId()).child("active").setValue(false);

        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
