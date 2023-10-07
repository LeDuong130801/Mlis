package com.leduongw01.mlis.databasehelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.leduongw01.mlis.utils.Const;

public class MlisMySqlDBHelper{
    public static Cursor getAllSavedPodcastByStatus(String status){
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(Const.databaseName, null);
        String sql = "select * from podcast where status = \""+status+"\"";
        return database.rawQuery(sql, null);
    }
    public static void generatorBD(){
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(Const.databaseName, null);
        String sql = "create table if not exists podcast(id text, podcastName text, uri text, status text)";
        database.execSQL(sql);
    }
}
