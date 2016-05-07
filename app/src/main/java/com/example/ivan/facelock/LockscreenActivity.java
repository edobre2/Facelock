package com.example.ivan.facelock;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Emanuil Dobrev on 5/6/16.
 */

public class LockscreenActivity extends AppCompatActivity {

    private boolean canLock = false;
    private boolean locked = false;
    private HomeKeyLocker locker;
    private String mEnteredPin;
    private String mPin;
    private boolean mClock;
    private String mBackground;
    private Context mContext;

    private static final String TAG = "LockscreenActivity";
    private static final int REQUEST_CODE = 0;
    SharedPreferences mSharedPreferences;

    TextView timeTextView;
    TextView dateTextView;
    TextView pinTextView;
    TextView enterPinTextView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lockscreen);

        Log.i(TAG, "onCreate()");
        // get a reference to each view
        ImageButton one = (ImageButton) findViewById(R.id.buttonOne);
        ImageButton two = (ImageButton) findViewById(R.id.buttonTwo);
        ImageButton three = (ImageButton) findViewById(R.id.buttonThree);
        ImageButton four = (ImageButton) findViewById(R.id.buttonFour);
        ImageButton five = (ImageButton) findViewById(R.id.buttonFive);
        ImageButton six = (ImageButton) findViewById(R.id.buttonSix);
        ImageButton seven = (ImageButton) findViewById(R.id.buttonSeven);
        ImageButton eight = (ImageButton) findViewById(R.id.buttonEight);
        ImageButton nine = (ImageButton) findViewById(R.id.buttonNine);
        ImageButton zero = (ImageButton) findViewById(R.id.buttonZero);
        ImageButton back = (ImageButton) findViewById(R.id.buttonBack);
        ImageButton ok = (ImageButton) findViewById(R.id.buttonOK);

        pinTextView = (TextView) findViewById(R.id.pinTextView);
        enterPinTextView = (TextView) findViewById(R.id.enterPinTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout);

        mContext = this;
        mEnteredPin = "";
        locker = new HomeKeyLocker();
        checkDrawOverlayPermission();

        if(Settings.canDrawOverlays(this)) {
            canLock = true;
            locker.lock(this);
            locked = true;
        }

        // get settings from shared preferences
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (! mSharedPreferences.contains("initialized")) {
            Log.e(TAG, "Shared Preferences were not initialized");
            finish();
        }

        mPin = mSharedPreferences.getString("pin", "0000");
        mBackground = mSharedPreferences.getString("background", "Default");
        mClock = mSharedPreferences.getBoolean("clock", true);


        pinTextView.setText("");
        updateTime();

        if (!mBackground.equals("Default")) {
            Uri imageUri = Uri.parse(mBackground);
            Drawable d;
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                d = Drawable.createFromStream(inputStream, imageUri.toString() );
                layout.setBackground(d);
            } catch (FileNotFoundException e) {
                layout.setBackground(getDrawable(R.drawable.default_bg));
            }
        }
        else
            layout.setBackground(getDrawable(R.drawable.default_bg));

        // set a listener for each button
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChar('1');
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChar('2');
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChar('3');
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChar('4');
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChar('5');
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChar('6');
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChar('7');
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChar('8');
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChar('9');
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChar('0');
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEnteredPin.length() > 0) {
                    // vibrate
                    Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(50);


                    mEnteredPin = mEnteredPin.substring(0, mEnteredPin.length() - 1);
                    pinTextView.setText(pinTextView.getText().toString().substring(0, pinTextView.getText().toString().length() - 1));
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEnteredPin.equals(mPin)) {
                    // unlock phone
                    MediaPlayer mp = MediaPlayer.create(mContext, R.raw.click);
                    mp.start();
                    finish();
                }
                else {
                    // access denied
                    MediaPlayer mp = MediaPlayer.create(mContext, R.raw.beep);
                    mp.start();
                    enterPinTextView.setTextColor(Color.RED);
                    enterPinTextView.setText("Invalid PIN");
                }
            }
        });
    }

    private void updateTime() {
        // check if clock is enabled
        if (mClock) {
            Calendar calendar = Calendar.getInstance();

            // set time
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            timeTextView.setText(sdf.format(calendar.getTime()));

            // set date
            sdf = new SimpleDateFormat("EEE, MMM d");
            dateTextView.setText(sdf.format(calendar.getTime()));
        }
        else {
            timeTextView.setText("");
            dateTextView.setText("");
        }
    }

    private void addChar(char c) {
        // max pin length is 10
        if (mEnteredPin.length() > 10)
            return;

        // vibrate
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(50);

        // append the character
        mEnteredPin = mEnteredPin + c;

        pinTextView.setText(pinTextView.getText().toString() + '*');
    }

    public void checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps */
        if (!Settings.canDrawOverlays(mContext)) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        Log.i("stuff", "Code is " + REQUEST_CODE);
        if (requestCode == REQUEST_CODE) {

            if (Settings.canDrawOverlays(this)) {
                // continue here - permission was granted
                canLock = true;
            }
        }
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if( !hasFocus )
        {
            if (locked) {
                locked = false;
                locker.unlock();
            }
            ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            am.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME );
            sendBroadcast( new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS) );
            locker.lock(this);
            locked = true;
        }
        else {
            if(!locked && canLock) {
                locked = true;
                locker.lock(this);
            }
        }
    }
}