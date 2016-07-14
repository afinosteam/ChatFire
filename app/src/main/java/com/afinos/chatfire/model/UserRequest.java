package com.afinos.chatfire.model;

/**
 * Created by phearom on 7/14/16.
 */
public class UserRequest extends FModel {
    private String requestId;
    private String action;
    private User user;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
