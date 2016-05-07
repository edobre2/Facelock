package com.example.ivan.facelock;

/**
 * Created by Emanuil Dobrev on 5/6/16.
 */
import android.content.BroadcastReceiver;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class LockscreenService extends Service {
    BroadcastReceiver mReceiver;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        KeyguardManager.KeyguardLock kl;
        KeyguardManager km =(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        kl= km.newKeyguardLock("IN");
        kl.disableKeyguard();

        boolean clock = intent.getBooleanExtra("clock", false);
        String pin = intent.getStringExtra("pin");
        String background = intent.getStringExtra("background");

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        mReceiver = new LockscreenReceiver(clock, pin, background);
        registerReceiver(mReceiver, filter);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
