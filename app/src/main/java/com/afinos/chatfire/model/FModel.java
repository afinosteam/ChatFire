package com.afinos.chatfire.model;

import java.util.UUID;

/**
 * Created by phearom on 7/13/16.
 */
public class FModel {
    private String unixId;

    public FModel() {
        setUnixId(UUID.randomUUID().toString());
    }

    public String getUnixId() {
        return unixId;
    }

    public void setUnixId(String unixId) {
        this.unixId = unixId;
    }
}
