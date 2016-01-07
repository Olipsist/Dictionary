package com.olipsist.dictionary.util;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.olipsist.dictionary.R;

import org.w3c.dom.Text;

/**
 * Created by Olipsist on 1/5/2016.
 */
public class DetailCursorAdapter extends CursorAdapter {

    public DetailCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_detail_listview,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tEntryTextView = (TextView) view.findViewById(R.id.entryTextView_detail);
        TextView eCatTextView = (TextView) view.findViewById(R.id.catTextView_detail);


        String tEntry = cursor.getString(cursor.getColumnIndex("tentry"));
        String cat = cursor.getString(cursor.getColumnIndex("ecat"));


        tEntryTextView.setText(tEntry);
        eCatTextView.setText(cat);
    }
}
