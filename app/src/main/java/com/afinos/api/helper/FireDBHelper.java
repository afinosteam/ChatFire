package com.afinos.api.helper;

import com.afinos.chatfire.model.FModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by phearom on 7/13/16.
 */
public class FireDBHelper {
    public static <T extends FModel> void create(T item) {
        FirebaseDatabase.getInstance().getReference()
                .child(item.getClass().getSimpleName())
                .child(item.getUnixId()).setValue(item);
    }

    public static <T extends FModel> void doQuery(Class<T> clazz, ValueEventListener listener) {
        FirebaseDatabase.getInstance().getReference(clazz.getSimpleName()).addValueEventListener(listener);
    }

    public static <T extends FModel> DatabaseReference doQuery(Class<T> clazz) {
        return FirebaseDatabase.getInstance().getReference().child(clazz.getSimpleName());
    }

    public static DatabaseReference doQuery(String key) {
        return FirebaseDatabase.getInstance().getReference().child(key);
    }

    public static <T extends FModel> DatabaseReference doUpdate(Class<T> clazz) {
        return FirebaseDatabase.getInstance().getReference(clazz.getSimpleName());
    }
}
