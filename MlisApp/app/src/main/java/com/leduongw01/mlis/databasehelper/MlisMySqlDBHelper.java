package com.leduongw01.mlis.databasehelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.leduongw01.mlis.utils.Constant;

public class MlisMySqlDBHelper{
    private static SQLiteDatabase instance = SQLiteDatabase.openOrCreateDatabase(Constant.databaseName, null);
    public static SQLiteDatabase getInstance(){
        return instance;
    }
    public static Cursor getAllSavedPodcastByStatus(String status){
        String sql = "select * from podcast where status = \""+status+"\"";
        return getInstance().rawQuery(sql, null);
    }
    public static void generatorBD(){
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(Constant.databaseName, null);
        String sql = "create table if not exists podcast(id text, podcastName text, uri text, status text)";
        database.execSQL(sql);
    }
}
