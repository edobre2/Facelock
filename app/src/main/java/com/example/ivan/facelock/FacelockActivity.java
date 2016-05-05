package com.example.ivan.facelock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FacelockActivity extends AppCompatActivity {

    // facelock state variables
    private boolean mEnabled;          // facelock enabled status
    private boolean mClock;            // should display clock?
    private String mBackground;         // background image
    private boolean mRunOnStartup;      // should run on startup?
    private boolean mPinSet;            // is PIN set?
    private String mPin;                // PIN

    // Log tag
    private final String TAG = "FacelockActivity";

    // title and info text for each menu option
    private List<String> titles = new ArrayList<String>();
    private List<String> infos = new ArrayList<String>();

    // list view
    private ListView mListView;
    private FacelockAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");

        setContentView(R.layout.activity_facelock);

        // initialize titles and infos for each option

        // Enable Facelock
        titles.add(getText(R.string.enable).toString());
        infos.add(getText(R.string.pin_not_set).toString());

        // Set PIN
        titles.add(getText(R.string.set_pin).toString());
        infos.add(getText(R.string.empty).toString());

        // Set background
        titles.add(getText(R.string.set_bg).toString());
        infos.add(getText(R.string.default_bg).toString());

        // Enable clock display
        titles.add(getText(R.string.clock).toString());
        infos.add(getText(R.string.enabled).toString());

        // Enable Run on startup
        titles.add(getText(R.string.run_startup).toString());
        infos.add(getText(R.string.enabled).toString());

        // get list view, assign a facelock adapter to it
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new FacelockAdapter(this, titles, infos);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
