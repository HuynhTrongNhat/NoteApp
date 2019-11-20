package com.example.notebook.Controller;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class DoubleTapListener implements View.OnClickListener {

    private static final long DOUBLE_CLICK_TIME_DELTA = 500;//milliseconds

    boolean oneTouch = false;
    boolean doubleTouch = false;
    long lastClickTime = 0;

    public interface DoubleTapCallback {
        void onSingleClick(View v);

        void onDoubleClick(View v);
    }

    private DoubleTapCallback listener;

    public DoubleTapListener(Context context) {
        listener = (DoubleTapCallback) context;
    }

    @Override
    public void onClick(View v) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            doubleTouch = true;
            if (oneTouch == true && doubleTouch == true) {
                Toast.makeText(v.getContext(), "Chỉnh sửa...", Toast.LENGTH_SHORT).show();
                listener.onDoubleClick(v);
                lastClickTime = 0;
            }
        } else {
            oneTouch = true;
            if (oneTouch == true && doubleTouch == false) {
                Toast.makeText(v.getContext(), "Nhấn đúp để chỉnh sửa", Toast.LENGTH_SHORT).show();
            }
            listener.onSingleClick(v);
        }
        lastClickTime = clickTime;
    }
}