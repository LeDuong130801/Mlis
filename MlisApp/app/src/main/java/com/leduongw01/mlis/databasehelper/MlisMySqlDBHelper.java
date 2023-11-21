package com.leduongw01.mlis.databasehelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.leduongw01.mlis.models.LocalRecentPodcast;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.utils.Constant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MlisMySqlDBHelper{
    private static final SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(Constant.databaseName, null);
    private static final MlisMySqlDBHelper instance = new MlisMySqlDBHelper();
    public static MlisMySqlDBHelper getInstance(){
        return instance;
    }
    MlisMySqlDBHelper(){
        generatorBD();
    }
    public static void generatorBD(){
        String sql = "create table if not exists recentPodcast(id text, listenOn number)";
        database.execSQL(sql);
    }
    public void putPodcastToRecent(Podcast podcast){
        Date d = new Date();
        long listenOn = d.getTime();
        String sql = "insert into recentPodcast values(\""+podcast.get_id()+"\", "+listenOn+"\")";
        database.execSQL(sql);
    }
    public List<LocalRecentPodcast> get3IdRecent(){
        String sql = "select * from recentPodcast order by listenOn desc";
        Cursor c = database.rawQuery(sql, null);
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
