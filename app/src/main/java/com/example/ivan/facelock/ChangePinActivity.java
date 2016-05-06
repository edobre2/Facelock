package com.example.ivan.facelock;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.AccessibleObject;

/**
 * Created by Emanuil Dobrev on 5/5/16.
 */

// Activity allowing user to change their PIN
// User must enter PIN in both numeric fields to successfully change it
public class ChangePinActivity extends AppCompatActivity {

    private static String TAG = "ChangePinActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate()");
        setContentView(R.layout.change_pin_layout);

        // get intent used to launch this activity
        // will be used later to return PIN to parent
        final Intent intent = getIntent();


        final EditText pin1 = (EditText) findViewById(R.id.editText);
        final EditText pin2 = (EditText) findViewById(R.id.editText2);
        final TextView outputTextView = (TextView) findViewById(R.id.textView3);
        final Button changePinButton = (Button) findViewById(R.id.button);

        // onClick, check if the 2 pins match and return to parent
        changePinButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (pin1.getText().toString().equals(pin2.getText().toString())) {
                    if (pin1.getText().toString().length() < 4 || pin1.getText().toString().length() > 10 ) {
                        outputTextView.setText(getText(R.string.pin_too_short));
                        outputTextView.setTextColor(Color.RED);
                        return;
                    }
                    intent.putExtra("pin", pin1.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    // inform user that pins do not match
                    outputTextView.setText(getText(R.string.pins_not_matching));
                    outputTextView.setTextColor(Color.RED);
                }
            }
        });
    }
}
