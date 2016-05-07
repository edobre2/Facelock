package com.example.ivan.facelock;

/**
 * Created by Emanuil Dobrev on 5/6/16.
 */
import android.content.BroadcastReceiver;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LockscreenService extends Service {
    BroadcastReceiver mReceiver;
    private static String TAG = "LockscreenService";
    public static boolean isRunning = false;
    public static Intent mIntent = null;
    public static boolean clock;
    public static String pin;
    public static String background;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;
        Log.i(TAG, "onStartCommand()");

        mIntent = intent;

        KeyguardManager.KeyguardLock kl;
        KeyguardManager km =(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        kl= km.newKeyguardLock("IN");
        kl.disableKeyguard();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        mReceiver = new LockscreenReceiver(clock, pin, background);
        registerReceiver(mReceiver, filter);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        unregisterReceiver(mReceiver);
        isRunning = false;
        super.onDestroy();
    }
}
