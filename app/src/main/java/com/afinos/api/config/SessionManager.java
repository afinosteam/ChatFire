package com.afinos.api.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by phearom on 7/13/16.
 */
public abstract class SessionManager {
    private SharedPreferences mShare;
    private SharedPreferences.Editor mEditor;

    protected SessionManager(Context context) {
        mShare = context.getSharedPreferences("chat_fire.xml", Context.MODE_PRIVATE);
        mEditor = mShare.edit();
    }

    public void saveData(String key, int val) {
        mEditor.putInt(key, val);
        mEditor.apply();
    }

    public void saveData(String key, boolean val) {
        mEditor.putBoolean(key, val);
        mEditor.apply();
    }

    public void saveData(String key, String val) {
        mEditor.putString(key, val);
        mEditor.apply();
    }

    public String getData(String key, String def) {
        return mShare.getString(key, def);
    }

    public int getData(String key, int def) {
        return mShare.getInt(key, def);
    }

    public boolean getData(String key, boolean def) {
        return mShare.getBoolean(key, def);
    }

    public void clear() {
        mEditor.clear();
        mEditor.apply();
    }
}
