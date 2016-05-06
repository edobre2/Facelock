package com.example.ivan.facelock;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Emanuil Dobrev on 5/6/16.
 */

public class LockscreenActivity extends AppCompatActivity {

    private String mEnteredPin;
    private SharedPreferences mPreferences;
    private String mPin;
    private boolean mClock;
    private String mBackground;
    private Context mContext;

    TextView timeTextView;
    TextView dateTextView;
    TextView pinTextView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lockscreen);

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
        TextView enterPinTextView = (TextView) findViewById(R.id.pinTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout);

        mContext = this;
        mEnteredPin = "";

        // get settings from preferences file
        mPreferences = getPreferences(MODE_PRIVATE);

        mPin = mPreferences.getString("pin", "");
        mBackground = mPreferences.getString("background", "");
        mClock = mPreferences.getBoolean("clock", false);

        pinTextView.setText("");
        updateTime();

        if (!mBackground.equals("Default"))
            layout.setBackground(Drawable.createFromPath(mBackground));
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
                    vibrator.vibrate(100);

                    // make a click sound
                    MediaPlayer mp = MediaPlayer.create(mContext, R.raw.click);
                    mp.start();

                    mEnteredPin = mEnteredPin.substring(0, mEnteredPin.length() - 2);
                    pinTextView.setText(pinTextView.getText().toString().substring(0, pinTextView.getText().toString().length() - 2));
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEnteredPin.equals(mPin)) {
                    // unlock phone
                    finish();
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
        v.vibrate(100);

        // make a click sound
        MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
        mp.start();

        // append the character
        mEnteredPin = mEnteredPin + c;

        pinTextView.setText(pinTextView.getText().toString() + '*');
    }
}