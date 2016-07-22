package com.example.ivan.facelock;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Emanuil Dobrev on 5/6/16.
 */

public class LockscreenActivity extends AppCompatActivity implements GestureDetector.OnGestureListener
{
    private WindowManager.LayoutParams layoutParams;
    private String mEnteredPin;
    private String mPin;
    private boolean mClock;
    private String mBackground;
    private Context mContext;

    private GestureDetectorCompat mDetector;

    private static final String TAG = "LockscreenActivity";
    private static final int FD_REQUEST = 1;
    SharedPreferences mSharedPreferences;

    TextView timeTextView;
    TextView dateTextView;
    TextView pinTextView;
    TextView enterPinTextView;
    ImageButton one, two, three, four, five, six, seven, eight, nine, zero, back, ok;
    LinearLayout layout;

    public WindowManager winManager;
    public RelativeLayout wrapperView;

    private void unlockDevice() {
        Log.i(TAG, "unlockDevice()");
        winManager.removeView(wrapperView);
        wrapperView.removeAllViews();
    }

    private boolean lockDevice(){
        Log.i(TAG, "lockDevice()");
        if(Settings.canDrawOverlays(this)) {
            View.inflate(this, R.layout.lockscreen, this.wrapperView);
            winManager.addView(wrapperView, layoutParams);
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lockscreen);

        Log.i(TAG, "onCreate()");
        mDetector = new GestureDetectorCompat(this, this);

        layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        this.winManager = ((WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE));
        this.wrapperView = new RelativeLayout(getBaseContext());
        getWindow().setAttributes(layoutParams);
        View.inflate(this, R.layout.lockscreen, this.wrapperView);
        this.winManager.addView(this.wrapperView, layoutParams);

        mContext = this;
        mEnteredPin = "";


        wrapperView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });
        // get settings from shared preferences
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (! mSharedPreferences.contains("initialized")) {
            Log.e(TAG, "Shared Preferences were not initialized");
            finish();
        }

        mPin = mSharedPreferences.getString("pin", "0000");
        mBackground = mSharedPreferences.getString("background", "Default");
        mClock = mSharedPreferences.getBoolean("clock", true);

        // get a reference to each view
        one = (ImageButton) wrapperView.findViewById(R.id.buttonOne);
        two = (ImageButton) wrapperView.findViewById(R.id.buttonTwo);
        three = (ImageButton) wrapperView.findViewById(R.id.buttonThree);
        four = (ImageButton) wrapperView.findViewById(R.id.buttonFour);
        five = (ImageButton) wrapperView.findViewById(R.id.buttonFive);
        six = (ImageButton) wrapperView.findViewById(R.id.buttonSix);
        seven = (ImageButton) wrapperView.findViewById(R.id.buttonSeven);
        eight = (ImageButton) wrapperView.findViewById(R.id.buttonEight);
        nine = (ImageButton) wrapperView.findViewById(R.id.buttonNine);
        zero = (ImageButton) wrapperView.findViewById(R.id.buttonZero);
        back = (ImageButton) wrapperView.findViewById(R.id.buttonBack);
        ok = (ImageButton) wrapperView.findViewById(R.id.buttonOK);

        pinTextView = (TextView) wrapperView.findViewById(R.id.pinTextView);
        enterPinTextView = (TextView) wrapperView.findViewById(R.id.enterPinTextView);
        timeTextView = (TextView) wrapperView.findViewById(R.id.timeTextView);
        dateTextView = (TextView) wrapperView.findViewById(R.id.dateTextView);

        layout = (LinearLayout) wrapperView.findViewById(R.id.linear_layout);

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
                    mEnteredPin = mEnteredPin.substring(0, mEnteredPin.length() - 1);
                    pinTextView.setText(pinTextView.getText().toString().substring(0, pinTextView.getText().toString().length() - 1));
                }
                else {
                    // vibrate
                    Vibrator vb = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                    vb.vibrate(50);
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEnteredPin.equals(mPin)) {
                    MediaPlayer mp = MediaPlayer.create(mContext, R.raw.click);
                    mp.start();
                    unlockDevice();
                    // unlock here
                    finish();
                }
                else {
                    // access denied
                    // vibrate
                    Vibrator vb = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                    vb.vibrate(50);
                    enterPinTextView.setTextColor(Color.RED);
                    enterPinTextView.setText("Invalid PIN");
                }
            }
        });

        pinTextView.setText("");
        updateTime();




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

        // append the character
        mEnteredPin = mEnteredPin + c;

        pinTextView.setText(pinTextView.getText().toString() + '*');
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if (requestCode == FD_REQUEST) {
            if (resultCode == RESULT_OK) {
                MediaPlayer mp = MediaPlayer.create(mContext, R.raw.click);
                mp.start();
                unlockDevice();
                finish();
            }
            else {
                lockDevice();
            }
        }
    }

    // gesture detector events
    @Override
    public boolean onDown(MotionEvent e) {
        Log.i(TAG, "onDown()");

        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.i(TAG, "onShowPress()");

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(TAG, "onSingleTapUp()");

        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i(TAG, "onScroll()");

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.i(TAG, "onLongPress()");
    }

    // open face detect activity on fling
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i(TAG, "onFling()");
        unlockDevice();
        Intent intent = new Intent(this, FaceDetectActivity.class);
        startActivityForResult(intent, FD_REQUEST) ;
        return true;
    }
}