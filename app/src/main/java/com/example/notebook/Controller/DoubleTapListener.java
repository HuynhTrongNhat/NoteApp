package com.example.notebook.Controller;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class DoubleTapListener implements View.OnClickListener {

    private boolean isRunning = false;
    private int resetInTime = 500;
    private int counter = 0;

    private DoubleTapCallback listener;

    public DoubleTapListener(Context context) {
        listener = (DoubleTapCallback) context;
    }

    public interface DoubleTapCallback {
        void onDoubleClick(View v);
    }

    @Override
    public void onClick(View v) {
        if (isRunning) {
            if (counter == 1) {
                listener.onDoubleClick(v);
            }
        }
        counter++;
        if (!isRunning) {
            Toast.makeText(v.getContext(), "Nhấn đúp để chỉnh sửa", Toast.LENGTH_SHORT).show();
            isRunning = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(resetInTime);
                        isRunning = false;
                        counter = 0;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}