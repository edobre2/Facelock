package com.example.ivan.facelock;

import android.content.Context;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Emanuil Dobrev on 5/4/16.
 */
// Custom adapter for a list view containing 2 text fields
// The first text field contains the title of the menu option
// The second text field contains information about the current status of that option
public class FacelockAdapter extends BaseAdapter {

    // lists of strings for titles and info strings
    private List<String> titles;
    private List<String> infos;

    // app context
    private Context mContext;

    // contructor - initialize titles and infos and save context
    public FacelockAdapter(Context c, List<String>titles_, List<String> infos_) {
        mContext = c;
        titles = titles_;
        infos = infos_;
    }

    // get number of elements in list view
    @Override
    public int getCount() {
        return titles.size();
    }

    // get data item at position - returns the title at that position
    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    // get row id at position
    @Override
    public long getItemId(int position) {
        return position;
    }

    // set the layout for that list item
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);

        // get text views
        TextView title = (TextView) view.findViewById(R.id.titleText);
        TextView info = (TextView) view.findViewById(R.id.infoText);

        // set text views
        title.setText(titles.get(position));
        info.setText(infos.get(position));
        return view;
    }
}
