package com.afinos.api.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.afinos.api.config.UserProfile;
import com.afinos.api.helper.FireDBHelper;
import com.afinos.api.key.ChatEvent;
import com.afinos.api.key.K;
import com.afinos.chatfire.R;
import com.afinos.chatfire.model.Chat;
import com.afinos.chatfire.ui.ChatActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by phearom on 7/13/16.
 */
public class ChatFireService extends Service implements ChildEventListener, ValueEventListener {
    private static final String TAG = ChatFireService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            ChatEvent.listen(ChatEvent.CHAT).child(UserProfile.init(getApplicationContext()).getData("chatId", null)).addChildEventListener(this);
        } catch (Exception e) {
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent)
            return START_STICKY;

        if (null == intent.getAction())
            return START_STICKY;

        String action = intent.getAction();
        if (action.equals(ChatEvent.ACTION_JOIN)) {
            join(intent.getExtras());
        } else if (action.equals(ChatEvent.ACTION_SEND)) {

        } else if (action.equals(ChatEvent.ACTION_UPDATE)) {

        }

        return START_STICKY;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void doNotify(String fromId, String id, String author, String message) {
        if (fromId.equals(UserProfile.init(getApplicationContext()).getId()))
            return;
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("chatId", id);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(author)
                .setContentText(message)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .build();

        int mNotificationId = 101;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, notification);
    }

    private void join(Bundle bundle) {
        Chat chat = new Chat();
        chat.setAuthorId(bundle.getString(K.FROM_ID));
        chat.setToId(bundle.getString(K.TO_ID));

        FireDBHelper.doQuery(ChatEvent.CHAT).child(bundle.getString(K.TO_ID)).child(chat.getUnixId()).setValue(chat);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (!dataSnapshot.exists())
            return;

        Chat chat = dataSnapshot.getValue(Chat.class);

        if (!ChatActivity.isForeground)
            doNotify(chat.getAuthorId(), chat.getToId(), chat.getAuthor(), chat.getMessage());
        Intent intent = new Intent();
        intent.setAction(ChatEvent.ACTION_PRIVATE);
        intent.putExtra(K.EXTRA_DATA, dataSnapshot.toString());
        sendBroadcast(intent);

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.i(TAG, dataSnapshot.toString());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }
}
