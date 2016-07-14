package com.afinos.chatfire.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.afinos.api.config.UserProfile;
import com.afinos.api.key.ChatEvent;
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

    private IntentFilter mIntentFilter;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ChatEvent.ACTION_PRIVATE)) {

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(ChatEvent.ACTION_JOIN);
        mIntentFilter.addAction(ChatEvent.ACTION_UPDATE);
        mIntentFilter.addAction(ChatEvent.ACTION_SEND);
        mIntentFilter.addAction(ChatEvent.ACTION_PUBLIC);
        mIntentFilter.addAction(ChatEvent.ACTION_PRIVATE);
    }

    @Override
    public void onStart() {
        registerReceiver(receiver, mIntentFilter);
        mAuth.addAuthStateListener(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        unregisterReceiver(receiver);
        mAuth.removeAuthStateListener(this);
        super.onStop();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        try {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                UserProfile.init(this).setId(firebaseUser.getUid());
                UserProfile.init(this).setName(firebaseUser.getDisplayName());
                UserProfile.init(this).setEmail(firebaseUser.getEmail());

                FireDBHelper.doUpdate(User.class).child(UserProfile.init(this).getId()).child("active").setValue(true);
                onSignIn(firebaseUser);
            } else {
                onSignOut();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
