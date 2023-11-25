package com.leduongw01.mlis.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.leduongw01.mlis.models.LocalRecentPodcast;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.utils.Constant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MlisMySqlDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MlisDatabase";
    private static final String TABLE_RECENT= "RecentPodcast";
    private static final String COL_ID= "id";
    private static final String COL_LISTEN_ON= "listen_on";
    private static final Integer DATABASE_VERSION = 1;

    public MlisMySqlDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableSQl = String.format("CREATE TABLE IF NOT EXISTS %s(%s TEXT, %s BIGINT)", TABLE_RECENT, COL_ID, COL_LISTEN_ON);
        sqLiteDatabase.execSQL(createTableSQl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void putPodcastToRecent(Podcast podcast){
        SQLiteDatabase db = this.getWritableDatabase();
        Date d = new Date();
        long listenOn = d.getTime();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, podcast.get_id());
        contentValues.put(COL_LISTEN_ON, listenOn);
        db.insert(TABLE_RECENT, null, contentValues);
        db.close();
    }
    public List<LocalRecentPodcast> get3IdRecent(){
        String sql =  String.format("select * from %s order by %s desc", TABLE_RECENT, COL_LISTEN_ON);
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        List<LocalRecentPodcast> output = new ArrayList<LocalRecentPodcast>();
        if(c.moveToFirst()){
            do {
                LocalRecentPodcast a = new LocalRecentPodcast();
                a.id = c.getString(0);
                a.listenOn = c.getLong(1);
                output.add(a);
            }
            while (c.moveToNext() && output.size()!=3);
        }
        c.close();
        return output;
    }

}
