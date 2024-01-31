package com.leduongw01.mlis.services;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.IOException;

public class CustomMediaPlayer extends MediaPlayer {
    String dataSource;
    boolean preparing;
    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        // TODO Auto-generated method stub
        super.setDataSource(path);
        dataSource = path;
        preparing = true;
    }

    @Override
    public void start() throws IllegalStateException {
        super.start();
        preparing = false;
    }

    @Override
    public void prepare() throws IOException, IllegalStateException {
        super.prepare();
        preparing = true;
    }

    @Override
    public void setDataSource(@NonNull Context context, @NonNull Uri uri) throws IOException {
        super.setDataSource(context, uri);

    }

    public boolean isPreparing() {
        return preparing;
    }

    public String getDataSource() {
        return dataSource;
    }
}
