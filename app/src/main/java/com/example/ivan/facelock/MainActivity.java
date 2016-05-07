package com.example.ivan.facelock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // facelock state variables
    private boolean mEnabled;          // facelock enabled status
    private boolean mClock;            // should display clock?
    private String mBackground;         // background image
    private boolean mRunOnStartup;      // should run on startup?
    private boolean mPinSet;            // is PIN set?
    private String mPin;                // PIN

    // Log tag
    private final String TAG = "MainActivity";
    private Intent mServiceIntent = null;

    // option ids
    final static int ENABLE_OPTION = 0;
    final static int PIN_OPTION = 1;
    final static int BACKGROUND_OPTION = 2;
    final static int CLOCK_OPTION = 3;
    final static int STARTUP_OPTION = 4;
    final static int DEFAULT_SETTINGS_OPTION = 5;
    final static int GET_PIN_REQUEST = 0;
    final static int SELECT_PICTURE = 1;

    // title and info text for each menu option
    private List<String> titles = new ArrayList<String>();
    private List<String> infos = new ArrayList<String>();

    // facelock uses shared preferences to save data to persistent storage
    private SharedPreferences mPreferences;

    // list view
    private ListView mListView;
    private FacelockAdapter mAdapter = null;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate()");

        setContentView(R.layout.activity_facelock);
        mContext = this;

        // add 6 empty strings to each list
        for(int i = 0; i < 6; i++) {
            titles.add(" ");
            infos.add(" ");
        }

        // get shared preferences file
        mPreferences = getPreferences(MODE_PRIVATE);

        // get settings from shared preferences
        loadSettings();

        // set the strings for each setting
        updateSettings();

        // get list view, assign a facelock adapter to it
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new FacelockAdapter(this, titles, infos);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // function called when one of the options was tapped
            // position is the id of the option
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // select option using position
                switch (position) {

                    // enable/disable facelock
                    case ENABLE_OPTION:
                        if (mEnabled) {
                            mEnabled = false;
                            updateSettings();

                            if (LockscreenService.isRunning)
                                stopService(LockscreenService.mIntent);

                        }
                        // if disabled, launch service
                        else {
                            if (!mPinSet)
                                break;
                            mEnabled = true;
                            updateSettings();
                            mServiceIntent = new Intent(mContext, LockscreenService.class);
                            mServiceIntent.putExtra("pin", mPin);
                            mServiceIntent.putExtra("clock", mClock);
                            mServiceIntent.putExtra("background", mBackground);
                            startService(mServiceIntent);
                        }
                        break;

                    // let user set or change their PIN
                    case PIN_OPTION:
                        if (!mEnabled) {
                            Intent intent = new Intent(mContext, ChangePinActivity.class);
                            startActivityForResult(intent, GET_PIN_REQUEST);
                        }
                        break;

                    // get a background image from gallery
                    case BACKGROUND_OPTION:
                        if (!mEnabled) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, SELECT_PICTURE);
                        }
                        break;

                    // change display clock status
                    case CLOCK_OPTION:
                        if (!mEnabled) {
                            mClock = !mClock;
                            updateSettings();
                        }
                        break;

                    // change runOnStartup status
                    case STARTUP_OPTION:
                        if (!mEnabled) {
                            mRunOnStartup = !mRunOnStartup;
                            updateSettings();
                        }
                        break;

                    // revert to default settings
                    case DEFAULT_SETTINGS_OPTION:
                        if (!mEnabled) {
                            mEnabled = false;
                            mPinSet = false;
                            mBackground = "Default";
                            mClock = true;
                            mRunOnStartup = true;
                            updateSettings();
                        }
                        break;
                    // invalid option - should never get here
                    default:
                        Log.i(TAG, "Unrecognized option");
                        break;
                }
            }
        });
    }

    // called after activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");

        // get the PIN if activity ended with result ok
        if (requestCode == GET_PIN_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                Log.i(TAG, "intent received was null");
                return;
            }
            mPin = data.getStringExtra("pin");
            mPinSet = true;
            updateSettings();
        }

        // get the image path if a picture was selected
        if(requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            if (data == null) {
                Log.i(TAG, "intent received was null");
                return;
            }

            Uri imageUri = data.getData();

            if(imageUri == null) {
                Log.i(TAG, "image uri was null");
                return;
            }


            mBackground = imageUri.toString();
            Log.i(TAG, mBackground);
            updateSettings();
        }

    }

    // load settings from shared preferences
    private void loadSettings() {

        // check if the file is initialized
        if (! mPreferences.contains("initialized")) {

            // initialize file with default values
            SharedPreferences.Editor editor = mPreferences.edit();

            editor.putBoolean("enabled", false);
            editor.putString("pin", "invalid");
            editor.putBoolean("pinSet", false);
            editor.putString("background", "Default");
            editor.putBoolean("clock", true);
            editor.putBoolean("runOnStartup", true);
            editor.putBoolean("initialized", true);
            boolean result = editor.commit();

            // check if commit was successful
            if (!result) {
                Log.e(TAG, "Failed to commit changes to shared preferences");
            }
        }

        // load settings
        mEnabled = mPreferences.getBoolean("enabled", true);
        mPin = mPreferences.getString("pin", "invalid");
        mPinSet = mPreferences.getBoolean("pinSet", true);
        mBackground = mPreferences.getString("background", "invalid");
        mClock = mPreferences.getBoolean("clock", false);
        mRunOnStartup = mPreferences.getBoolean("runOnStartup", false);
    }

    // initialize the text for each setting
    private void updateSettings() {
        if (!mEnabled) {
            // Enable Facelock
            titles.set(ENABLE_OPTION, getText(R.string.disabled).toString());
            if (!mPinSet)
                infos.set(ENABLE_OPTION, getText(R.string.pin_not_set).toString());
            else
                infos.set(ENABLE_OPTION, " ");
        }
        else {
            // Disable Facelock
            titles.set(ENABLE_OPTION, getText(R.string.enabled).toString());
            infos.set(ENABLE_OPTION, getText(R.string.change_settings).toString());
        }

        // Set PIN
        titles.set(PIN_OPTION, getText(R.string.set_pin).toString());
        if(mPinSet)
            infos.set(PIN_OPTION, getText(R.string.pin_isset).toString());
        else
            infos.set(PIN_OPTION, getText(R.string.not_set).toString());

        // Set background
        titles.set(BACKGROUND_OPTION, getText(R.string.set_bg).toString());
        infos.set(BACKGROUND_OPTION, mBackground);

        // Enable clock display
        titles.set(CLOCK_OPTION, getText(R.string.clock).toString());
        if (mClock)
            infos.set(CLOCK_OPTION, getText(R.string.enabled).toString());
        else
            infos.set(CLOCK_OPTION, getText(R.string.disabled).toString());

        // Enable Run on startup
        titles.set(STARTUP_OPTION, getText(R.string.run_startup).toString());
        if(mRunOnStartup)
            infos.set(STARTUP_OPTION, getText(R.string.enabled).toString());
        else
            infos.set(STARTUP_OPTION, getText(R.string.disabled).toString());

        titles.set(DEFAULT_SETTINGS_OPTION, getText(R.string.default_settings).toString());
        infos.set(DEFAULT_SETTINGS_OPTION, " ");

        // update adapter if it is not null
        if (mAdapter != null) {
            mAdapter.update(titles, infos);
        }
    }

    // save settings before destroying
    @Override
    protected  void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy()");
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean("enabled", mEnabled);
        editor.putString("pin", mPin);
        editor.putBoolean("pinSet", mPinSet);
        editor.putString("background", mBackground);
        editor.putBoolean("clock", mClock);
        editor.putBoolean("runOnStartup", mRunOnStartup);
        boolean result = editor.commit();

        if (!result) {
            Log.e(TAG, "failed to commit changes");
        }
    }
}
