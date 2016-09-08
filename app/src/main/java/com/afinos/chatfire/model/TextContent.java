package com.afinos.chatfire.model;

/**
 * @Copy Right 2012-2016, Afinos, Inc., or its affiliates
 * @Author: Afinos Team
 **/
public class TextContent extends BaseContent<String> {
    private String text;

    @Override
    public String getValue() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
