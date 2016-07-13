package com.afinos.api.binding;

import android.databinding.BindingAdapter;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by pisey on 14-Jun-16.
 */
public class ViewBinding {
    @BindingAdapter("evenClick")
    public static void setOnClickEven(View view, View.OnClickListener onClickListener) {
        view.setOnClickListener(onClickListener);
    }

    @BindingAdapter("isGravityRight")
    public static void setGravityRight(LinearLayout viewGroup, boolean right) {
        viewGroup.setGravity(right ? Gravity.RIGHT : Gravity.LEFT);
    }
}
