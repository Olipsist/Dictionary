package com.olipsist.dictionary.util;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.olipsist.dictionary.R;

/**
 * Created by Olipsist on 12/30/2015.
 */
public class MyCursorAdapter extends CursorAdapter {

    public MyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_listview,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView resultText = (TextView)view.findViewById(R.id.showTextView);
        String strResult = cursor.getString(cursor.getColumnIndex("esearch"));
        resultText.setText(strResult);
    }
}
