package com.afinos.api.listener;

import android.os.Handler;
import android.support.v7.widget.SearchView;

/**
 * Created by phearom on 2/24/16.
 */
public abstract class ZeTextSearch implements SearchView.OnQueryTextListener, ZeSearchListener {
    private final Handler timeoutHandler = new Handler();
    private int TYPING_TIMEOUT = 5000; // 5 seconds timeout
    private boolean isTyping = false;
    private String newText;
    private final Runnable typingTimeout = new Runnable() {
        public void run() {
            isTyping = false;
            onSearch(newText, isTyping);
        }
    };

    public ZeTextSearch(int timeout) {
        if (timeout > 0)
            this.TYPING_TIMEOUT = timeout;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.newText = newText;
        timeoutHandler.removeCallbacks(typingTimeout);
        if (newText.length() > 0) {
            // schedule the timeout
            timeoutHandler.postDelayed(typingTimeout, TYPING_TIMEOUT);
            if (!isTyping) {
                isTyping = true;
                onSearch(newText, isTyping);
            }
        } else {
            isTyping = false;
            onSearch(newText, isTyping);
        }
        return false;
    }
}
