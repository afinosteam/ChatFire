package com.afinos.api.listener;

import android.view.View;

public interface ClickHandler<T>
{
    void onClick(T viewModel, View v);
}