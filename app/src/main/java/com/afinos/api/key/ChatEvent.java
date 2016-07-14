package com.afinos.api.key;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by phearom on 7/14/16.
 */
public class ChatEvent {
    public final static String ACTION_JOIN = "action_join";
    public final static String ACTION_SEND = "action_send";
    public final static String ACTION_UPDATE = "action_update";
    public final static String ACTION_PUBLIC = "action_public";
    public final static String ACTION_PRIVATE = "action_private";

    public final static String CHAT = "Chat";

    public ChatEvent() {
    }

    public static DatabaseReference listen(String key) {
        return FirebaseDatabase.getInstance().getReference().child(key);
    }

    public static DatabaseReference createGroup(String id) {
        return FirebaseDatabase.getInstance().getReference().child(CHAT).child(id);
    }

    public static DatabaseReference createSingle(String id) {
        return FirebaseDatabase.getInstance().getReference().child(CHAT).child(id);
    }

    public static DatabaseReference join(String id) {
        return FirebaseDatabase.getInstance().getReference().child(CHAT).child(id);
    }
}
