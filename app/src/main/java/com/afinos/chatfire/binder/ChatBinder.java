package com.afinos.chatfire.binder;

import com.afinos.api.binder.ConditionalDataBinder;
import com.afinos.chatfire.viewmodel.ChatViewModel;

/**
 * Created by phearom on 7/13/16.
 */
public class ChatBinder extends ConditionalDataBinder<ChatViewModel> {

    public ChatBinder(int bindingVariable, int layoutId) {
        super(bindingVariable, layoutId);
    }

    @Override
    public boolean canHandle(ChatViewModel model) {
        return true;
    }
}
