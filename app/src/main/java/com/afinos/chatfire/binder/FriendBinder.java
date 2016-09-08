package com.afinos.chatfire.binder;

import com.afinos.api.binder.ConditionalDataBinder;
import com.afinos.chatfire.viewmodel.FriendViewModel;

/**
 * Created by phearom on 7/13/16.
 */
public class FriendBinder extends ConditionalDataBinder<FriendViewModel> {

    public FriendBinder(int bindingVariable, int layoutId) {
        super(bindingVariable, layoutId);
    }

    @Override
    public boolean canHandle(FriendViewModel model) {
        return true;
    }
}
