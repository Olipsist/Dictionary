package com.olipsist.dictionary.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Olipsist on 12/28/2015.
 */
public class MyDbHelper extends SQLiteAssetHelper {

 private static final String DATABASE_NAME = "lexitron.db";
 private static final int DATABASE_VERSION = 4;
 private static final String TABLE_NAME = "thai2eng";
 private String testtoo = "i don't want to getup";

 public MyDbHelper(Context context) {
  super(context, DATABASE_NAME,null, DATABASE_VERSION);
 }

 @Override
 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        super.onUpgrade(db, oldVersion, newVersion);
 }

 public Cursor initCursor(SQLiteDatabase db){
  checkDbOpen(db);
  Cursor resulutCursor = db.rawQuery("SELECT "+TABLE_NAME+".id AS _id, "+TABLE_NAME+".* FROM "+TABLE_NAME+" WHERE id = 0 GROUP BY "+TABLE_NAME+".esearch",null);
  return resulutCursor;
 }

 public Cursor findAllWord(SQLiteDatabase db){
  checkDbOpen(db);
  Cursor resulutCursor = db.rawQuery("SELECT "+ TABLE_NAME + ".id AS _id, "+ TABLE_NAME +".* FROM "+ TABLE_NAME +" GROUP BY esearch",null);
  return resulutCursor;
 }

 public Cursor findWordByString(SQLiteDatabase db, String str){
  checkDbOpen(db);
  str += "%";
  String sql = "SELECT "+TABLE_NAME+".id AS _id, "+TABLE_NAME+".* FROM "+TABLE_NAME +
          " WHERE "+TABLE_NAME +".esearch LIKE ? GROUP BY "+TABLE_NAME+".esearch ORDER BY id";
  Cursor resultCursor = db.rawQuery(sql,new String[]{str});
  return resultCursor;
 }

 public Cursor findWordsDetailByString(SQLiteDatabase db, String word, String cat){
  checkDbOpen(db);
  String sql = "SELECT "+TABLE_NAME+".id AS _id, "+TABLE_NAME+".* FROM "+TABLE_NAME+" " +
          "WHERE "+TABLE_NAME+".esearch LIKE ? AND cat = ?";
  Cursor resultCursor = db.rawQuery(sql,new String[]{word,cat});
  return resultCursor;
 }

 public Cursor findTypeOfWord( SQLiteDatabase db, String word){
  checkDbOpen(db);
  String sql = "SELECT * FROM "+TABLE_NAME+" WHERE esearch = ? GROUP BY cat";
  Cursor resultCursor  = db.rawQuery(sql,new String[]{word});
  return resultCursor;
 }

 public Cursor findAllFevWord(SQLiteDatabase db){
  checkDbOpen(db);
  String sql = "SELECT "+TABLE_NAME+".id AS _id, "+TABLE_NAME+".* FROM "+TABLE_NAME+" WHERE fav = 'A'";
  Cursor resultCursor = db.rawQuery(sql,null);
  return resultCursor;
 }

 public void setFavUpdate(SQLiteDatabase db,String id,String favValue){
  checkDbOpen(db);
  ContentValues data = new ContentValues();
  data.put("fav", favValue);
  db.update(TABLE_NAME, data, "id = ?", new String[]{id});
 }

 private void checkDbOpen(SQLiteDatabase db){
  if(!db.isOpen()){
   db = this.getWritableDatabase();
  }
 }

 public void update(SQLiteDatabase db){
  Log.i("SQL", "UPGRAD");
  String[] queries = {"UPDATE thai2eng SET esearch = REPLACE(CAST(esearch as varchar(500)),?,?);"
          , "UPDATE thai2eng SET entry1 = REPLACE(CAST(entry1 as varchar(500)),?,?);"
          , "UPDATE thai2eng SET entry2 = REPLACE(CAST(entry2 as varchar(500)),?,?);"};
  for(String query:queries){
   Cursor c = db.rawQuery(query,new String[]{"\"","'"});
   c.moveToFirst();
   c.close();
  }
 }
}
