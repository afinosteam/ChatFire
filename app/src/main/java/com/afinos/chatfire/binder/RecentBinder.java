package com.afinos.chatfire.binder;

import com.afinos.api.binder.ConditionalDataBinder;
import com.afinos.chatfire.viewmodel.RecentMessageViewModel;

/**
 * Created by phearom on 7/13/16.
 */
public class RecentBinder extends ConditionalDataBinder<RecentMessageViewModel> {

    public RecentBinder(int bindingVariable, int layoutId) {
        super(bindingVariable, layoutId);
    }

    @Override
    public boolean canHandle(RecentMessageViewModel model) {
        return true;
    }
}
