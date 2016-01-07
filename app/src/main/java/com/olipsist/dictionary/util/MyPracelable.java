package com.olipsist.dictionary.util;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Olipsist on 12/30/2015.
 */
public class MyPracelable implements Parcelable {

    private Cursor cursor;

    public MyPracelable(Cursor cursor){
        this.cursor = cursor;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    //    #Pracel Part
    protected MyPracelable(Parcel in) {
//        cursor = in.read
    }

    public static final Creator<MyPracelable> CREATOR = new Creator<MyPracelable>() {
        @Override
        public MyPracelable createFromParcel(Parcel in) {
            return new MyPracelable(in);
        }

        @Override
        public MyPracelable[] newArray(int size) {
            return new MyPracelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
