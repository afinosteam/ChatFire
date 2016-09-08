package com.afinos.api.binding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.afinos.chatfire.R;
import com.bumptech.glide.Glide;

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

    @BindingAdapter("bindImage")
    public static void setImage(AppCompatImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) return;
        Glide.with(imageView.getContext()).load(url).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imageView);
    }
}
