package com.example.ivan.facelock;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    TextView timeTextView;
    TextView dateTextView;


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

        TextView pinTextView = (TextView) findViewById(R.id.pinTextView);
        TextView enterPinTextView = (TextView) findViewById(R.id.pinTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout);


        mEnteredPin = "";

        // get settings from preferences file
        mPreferences = getPreferences(MODE_PRIVATE);

        mPin = mPreferences.getString("pin", "");
        mBackground = mPreferences.getString("background", "");
        mClock = mPreferences.getBoolean("clock", false);

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
}