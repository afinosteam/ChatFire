package com.afinos.chatfire.model;

import java.util.List;

/**
 * Created by phearom on 7/13/16.
 */
public class User extends FModel {
    private String id;
    private String name;
    private String email;
    private boolean active;
    private String action;
    private List<String> chatIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getChatIds() {
        return chatIds;
    }

    public void setChatIds(List<String> chatIds) {
        this.chatIds = chatIds;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        setUnixId(id);
    }
}
