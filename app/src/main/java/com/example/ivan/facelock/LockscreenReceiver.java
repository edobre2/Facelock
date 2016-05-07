package com.example.ivan.facelock;

/**
 * Created by Emanuil Dobrev on 5/6/16.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

public class LockscreenReceiver extends BroadcastReceiver {
    private static String TAG = "LockscreenReceiver";

    // lockscreen settings
    private boolean mClock;
    private String mPin;
    private String mBackground;

    public LockscreenReceiver() {
        Log.i(TAG, "LockscreenReceiver() default");
    }
    // constructor takes settings as parameters
    public LockscreenReceiver(boolean clock, String pin, String background) {
        Log.i(TAG, "LockscreenReceiver()");
        mClock = clock;
        mPin = pin;
        mBackground = background;
    }

    // on receive - create an intent that launches lockscreen activity, passing settings as extras
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "onReceive()");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

            Intent localIntent = new Intent(context, LockscreenActivity.class);
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            localIntent.addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
            localIntent.putExtra("clock", mClock);
            localIntent.putExtra("pin", mPin);
            localIntent.putExtra("background", mBackground);

            context.startActivity(localIntent);
        }
    }
}