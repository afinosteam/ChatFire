package com.afinos.api.config;

import android.content.Context;

/**
 * Created by phearom on 7/13/16.
 */
public class UserProfile extends SessionManager {
    private static UserProfile instance;

    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String EMAIL = "email";
    private final static String TOKEN = "token";

    protected UserProfile(Context context) {
        super(context);
    }

    public static UserProfile init(Context context) {
        if (null == instance)
            instance = new UserProfile(context);
        return instance;
    }

    public String getId() {
        return getData(ID, null);
    }

    public void setId(String id) {
        this.saveData(ID, id);
    }

    public String getName() {
        return getData(NAME, "Unknown");
    }

    public void setName(String name) {
        this.saveData(NAME, name);
    }

    public String getEmail() {
        return getData(EMAIL, "Unknown");
    }

    public void setEmail(String email) {
        this.saveData(EMAIL, email);
    }

    public String getToken() {
        return getData(TOKEN, null);
    }

    public void setToken(String token) {
        this.saveData(TOKEN, token);
    }
}
