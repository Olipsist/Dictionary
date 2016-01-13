package com.olipsist.dictionary.util;

import android.content.Context;
import android.nfc.tech.NfcBarcode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.olipsist.dictionary.R;

import java.util.ArrayList;

/**
 * Created by Olipsist on 1/10/2016.
 */
public class DetailArrayAdapter extends ArrayAdapter<DetailWord> {
    public DetailArrayAdapter(Context context, ArrayList<DetailWord> detail) {
        super(context, R.layout.view_detail_listview ,detail);
    }

    private static class ViewHolder{
        TextView cat;
        TextView entry;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        DetailWord detailWord =  getItem(position);
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.view_detail_listview,parent,false);
            viewHolder.cat = (TextView) convertView.findViewById(R.id.catTextView_detail);
            viewHolder.entry = (TextView) convertView.findViewById(R.id.entryTextView_detail);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.entry.setText(detailWord.entry);
        viewHolder.cat.setText(detailWord.cat);
        return convertView;
    }
}
