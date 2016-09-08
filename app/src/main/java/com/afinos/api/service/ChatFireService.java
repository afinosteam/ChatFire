package com.afinos.api.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.afinos.api.config.UserProfile;
import com.afinos.api.key.ChatEvent;
import com.afinos.chatfire.R;
import com.afinos.chatfire.model.Message;
import com.afinos.chatfire.ui.BaseActivity;
import com.afinos.chatfire.ui.MainActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by phearom on 7/13/16.
 */
public class ChatFireService extends Service implements ChildEventListener {
    private static final String TAG = ChatFireService.class.getSimpleName();
    private DatabaseReference mMessageRef;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMessageRef = FirebaseDatabase.getInstance().getReference().child(Message.class.getSimpleName());
        mMessageRef.orderByChild("toId").equalTo(UserProfile.init(getApplicationContext()).getId()).addChildEventListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent)
            return START_STICKY;

        if (null == intent.getAction())
            return START_STICKY;

        String action = intent.getAction();
        if (action.equals(ChatEvent.ACTION_JOIN)) {

        } else if (action.equals(ChatEvent.ACTION_SEND)) {

        } else if (action.equals(ChatEvent.ACTION_UPDATE)) {

        }
        return START_STICKY;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void doNotify(Context context, Message message) {
        if (message == null)
            return;
        if (message.getFromId().equals(UserProfile.init(context).getId()))
            return;
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(ChatEvent.CHAT);
        intent.putExtra("toId", message.getToId());
        intent.putExtra("toUser", message.getToUser());
        intent.putExtra("fromId", message.getFromId());
        intent.putExtra("fromUser", message.getFromUser());
        intent.putExtra("content", message.getContent());
        intent.putExtra("dateTime", message.getDateTime());

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_launcher))
                .setContentTitle(message.getFromUser())
                .setContentText(message.getContent())
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .build();

        int mNotificationId = message.getFromId().hashCode();
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, notification);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Message message = dataSnapshot.getValue(Message.class);
        if (!BaseActivity.isForeground) {
            doNotify(getApplicationContext(), message);
            return;
        }
        Intent intent = new Intent();
        intent.setAction(ChatEvent.ACTION_RECEIVE);
        intent.putExtra("toId", message.getToId());
        intent.putExtra("toUser", message.getToUser());
        intent.putExtra("fromId", message.getFromId());
        intent.putExtra("fromUser", message.getFromUser());
        intent.putExtra("content", message.getContent());
        intent.putExtra("dateTime", message.getDateTime());
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
    public void onCancelled(DatabaseError databaseError) {
    }
}
