package com.leduongw01.mlis.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.leduongw01.mlis.models.LocalRecentPodcast;
import com.leduongw01.mlis.models.Podcast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MlisSqliteDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MlisDatabase";
    private static final String TABLE_RECENT= "RecentPodcast";
    private static final String COL_ID= "id";
    private static final String COL_LISTEN_ON= "listen_on";
    private static final String COL_USERID= "userId";
    private static final String COL_PLAYLISTID= "playlistId";
    private static final String COL_FAVORITEID= "favoriteId";
    private static final Integer DATABASE_VERSION = 1;

    public MlisSqliteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableSQl = String.format("CREATE TABLE IF NOT EXISTS %s(%s TEXT, %s BIGINT, %s TEXT, %s TEXT, %s TEXT)", TABLE_RECENT, COL_ID, COL_LISTEN_ON, COL_USERID, COL_FAVORITEID, COL_PLAYLISTID);
        sqLiteDatabase.execSQL(createTableSQl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void putPodcastToRecent(Podcast podcast, String userId, String favoriteId, String playlistId){
        SQLiteDatabase db = this.getWritableDatabase();
        Date d = new Date();
        long listenOn = d.getTime();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, podcast.get_id());
        contentValues.put(COL_LISTEN_ON, listenOn);
        contentValues.put(COL_USERID, userId);
        contentValues.put(COL_FAVORITEID, favoriteId);
        contentValues.put(COL_PLAYLISTID, playlistId);
        db.insert(TABLE_RECENT, null, contentValues);
        db.close();
    }
    public List<LocalRecentPodcast> get3IdRecent(String userId){
        String sql =  String.format("select * from %s where %s = '%s' or %s = '%s' order by %s desc", TABLE_RECENT, COL_USERID, userId,COL_USERID, "none", COL_LISTEN_ON);
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        List<LocalRecentPodcast> output = new ArrayList<LocalRecentPodcast>();
        if(c.moveToFirst()){
            do {
                LocalRecentPodcast a = new LocalRecentPodcast();
                a.id = c.getString(0);
                a.listenOn = c.getLong(1);
                a.userId = c.getString(2);
                a.favoriteId = c.getString(3);
                a.playlistId = c.getString(4);
                boolean y = true;
                for(LocalRecentPodcast localRecentPodcast : output){
                    if (localRecentPodcast.id.equals(a.id) || (a.playlistId ==null && a.favoriteId==null)){
                        y = false;
                        break;
                    }
                }
                if (y)
                    output.add(a);
            }
            while (c.moveToNext() && output.size()!=3);
        }
        c.close();
        return output;
    }
    public List<LocalRecentPodcast> get3IdRecent(){
        String sql =  String.format("select * from %s where %s = 'none' order by %s desc", TABLE_RECENT, COL_USERID, COL_LISTEN_ON);
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        List<LocalRecentPodcast> output = new ArrayList<LocalRecentPodcast>();
        if(c.moveToFirst()){
            do {
                LocalRecentPodcast a = new LocalRecentPodcast();
                a.id = c.getString(0);
                a.listenOn = c.getLong(1);
                a.userId = c.getString(2);
                a.favoriteId = c.getString(3);
                a.playlistId = c.getString(4);
                boolean y = true;
                for(LocalRecentPodcast localRecentPodcast : output){
                    if (localRecentPodcast.id.equals(a.id)){
                        y = false;
                        break;
                    }
                }
                if (y)
                    output.add(a);
            }
            while (c.moveToNext() && output.size()!=3);
        }
        c.close();
        return output;
    }

}
