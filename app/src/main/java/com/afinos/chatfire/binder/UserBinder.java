package com.afinos.chatfire.binder;

import com.afinos.api.binder.ConditionalDataBinder;
import com.afinos.chatfire.viewmodel.UserViewModel;

/**
 * Created by phearom on 7/13/16.
 */
public class UserBinder extends ConditionalDataBinder<UserViewModel> {

    public UserBinder(int bindingVariable, int layoutId) {
        super(bindingVariable, layoutId);
    }

    @Override
    public boolean canHandle(UserViewModel model) {
        return true;
    }
}
