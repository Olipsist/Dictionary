package com.olipsist.dictionary.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Olipsist on 12/28/2015.
 */
public class MyDbHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "lexitron.db";
    private static final int version = 4;
    private static final String TABLE_NAME = "TEST";

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME,null, version);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor findAllWord(SQLiteDatabase db){

        Cursor resulutCursor = db.rawQuery("SELECT eng2thai.id AS _id, eng2thai.* FROM eng2thai GROUP BY eng2thai.esearch",null);
        return resulutCursor;
    }

    public Cursor findWordByString(SQLiteDatabase db,String str){

        str += "%";
        String sql = "SELECT eng2thai.id AS _id, eng2thai.* FROM eng2thai " +
                "WHERE eng2thai.esearch LIKE ? GROUP BY eng2thai.esearch";
        Cursor resultCursor = db.rawQuery(sql,new String[]{str});
        return resultCursor;
    }

    public Cursor findWordsDetailByString(SQLiteDatabase db, String word, String cat){

        String sql = "SELECT eng2thai.id AS _id, eng2thai.* FROM eng2thai " +
                "WHERE eng2thai.esearch LIKE ? AND ecat = ?";
        Cursor resultCursor = db.rawQuery(sql,new String[]{word,cat});
        return resultCursor;
    }

    public Cursor findTypeOfWord( SQLiteDatabase db, String word){
        String sql = "SELECT * FROM eng2thai WHERE esearch = ? GROUP BY ecat";
        Cursor resultCursor  = db.rawQuery(sql,new String[]{word});
        return resultCursor;
    }

    public Cursor findAllFevWord(SQLiteDatabase db){
        String sql = "SELECT eng2thai.id AS _id, eng2thai.* FROM eng2thai WHERE fev = 'A'";
        Cursor resultCursor = db.rawQuery(sql,null);
        return resultCursor;
    }

    public void setFavUpdate(SQLiteDatabase db,String id,String favValue){
        ContentValues data = new ContentValues();
        data.put("fav",favValue);
        db.update("eng2thai",data,"id = ?",new String[]{id});
    }
}
