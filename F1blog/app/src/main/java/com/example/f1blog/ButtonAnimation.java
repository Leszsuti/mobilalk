package com.example.f1blog;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

public class ButtonAnimation {
    @SuppressLint("ClickableViewAccessibility")
    public static void pressEffect(View button) {
        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            return false;
        });
    }
}
