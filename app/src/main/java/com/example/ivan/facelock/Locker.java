package com.example.ivan.facelock;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by Emanuil Dobrev on 6/20/2016.
 */
public class Locker {
// test
    Activity mActivity;
    WindowManager mWindowManager;
    RelativeLayout mWrapperView;

    public Locker(Activity activity) {
        mActivity = activity;
        mWindowManager = ((WindowManager) activity.getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
    }

    public RelativeLayout lock(){
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        mWrapperView = new RelativeLayout(mActivity.getBaseContext());
        mActivity.getWindow().setAttributes(localLayoutParams);
        View.inflate(mActivity, R.layout.lockscreen, mWrapperView);
        mWindowManager.addView(mWrapperView, localLayoutParams);
        return mWrapperView;
    }

    public void unlock() {
        if (mWindowManager != null && mWrapperView != null) {
            mWindowManager.removeView(mWrapperView);
            mWrapperView.removeAllViews();
        }
    }
}
