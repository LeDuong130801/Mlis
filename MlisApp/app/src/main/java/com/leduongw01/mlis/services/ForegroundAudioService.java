package com.leduongw01.mlis.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.activities.LoginActivity;
import com.leduongw01.mlis.receivers.BackReceiverNotification;
import com.leduongw01.mlis.receivers.NextReceiverNotification;
import com.leduongw01.mlis.receivers.NotificationReceiver;
import com.leduongw01.mlis.receivers.PauseResumeReceiverNotification;
import com.leduongw01.mlis.utils.Const;

import java.util.ArrayList;
import java.util.Objects;

public class ForegroundAudioService extends Service {
    public static int currentSeek = 0;
    public static boolean playing = false;
    public static MediaPlayer mediaPlayer;
    private static ArrayList listAudio;
    private static int currentAudio;
    String ACTION_CLICK = "actionclick";
    String EXTRA_CLICK = "a";
    IBinder localBinder = new LocalBinder();
    public static RemoteViews notificationLayout;
    TimeThread timeThread = new TimeThread();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @SuppressLint("RemoteViewLayout")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        NotificationChannel serviceChannel = new NotificationChannel(
//                "hoho",
//                "Thanh dieu huong truyen",
//                NotificationManager.IMPORTANCE_DEFAULT
//        );
//        NotificationManager manager = getSystemService(NotificationManager.class);
//        manager.createNotificationChannel(serviceChannel);
//        customNotification();
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(this, R.raw.bgbgbg);
        mediaPlayer.start();
        timeThread.start();
        return START_NOT_STICKY;
    }
    private PendingIntent onButtonNotificationClick(Context context, Integer id){
        Intent intent = new Intent();
        if (Objects.equals(id, Const.NEXT)){
            intent = new Intent(this, NextReceiverNotification.class);
        }
        else if(Objects.equals(id, Const.BACK)){
            intent = new Intent(this, BackReceiverNotification.class);
        }
        else if(Objects.equals(id, Const.PAUSE_RESUME)){
            intent = new Intent(this, PauseResumeReceiverNotification.class);
        }
        else if(Objects.equals(id, Const.IMAGE)){
            return PendingIntent.getActivity(this, 0, new Intent(this, LoginActivity.class), 0);
        }
        return PendingIntent.getBroadcast(context, 200, intent, 0);
    }
    @SuppressLint("RemoteViewLayout")
    void customNotification(){
        Intent notificationIntent = new Intent(this, LoginActivity.class);
        NotificationChannel serviceChannel = new NotificationChannel(
                "hoho",
                "Mlis MediaPlayer",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
        if(mediaPlayer.isPlaying()){
            notificationLayout = new RemoteViews(getPackageName(), R.layout.nofication_audio);
        }
        else {
            notificationLayout = new RemoteViews(getPackageName(), R.layout.nofication_audio_stop);
        }
        notificationLayout.setOnClickPendingIntent(R.id.ivbackNofication, onButtonNotificationClick(getApplicationContext(), Const.BACK));
        notificationLayout.setOnClickPendingIntent(R.id.ivControllNofication, onButtonNotificationClick(getApplicationContext(), Const.PAUSE_RESUME));
        notificationLayout.setOnClickPendingIntent(R.id.ivNextNofication, onButtonNotificationClick(getApplicationContext(), Const.NEXT));
        notificationLayout.setOnClickPendingIntent(R.id.ivAudio, onButtonNotificationClick(getApplicationContext(), Const.IMAGE));
        Log.d("dddd", mediaPlayer.getDuration()+"");
        Notification notification = new NotificationCompat.Builder(this, "hoho")
                .setSmallIcon(R.drawable.outline_music_note_24)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayout)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();
        startForeground(1, notification);
    }
    public static void pauseOrResumeMediaPlayer(){
        if(mediaPlayer.isPlaying()){
            currentSeek = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
        else {
            mediaPlayer.seekTo(currentSeek);
            mediaPlayer.start();
        }
    }
    public static void resetOrBackMediaPlayer(){
        if (mediaPlayer.isPlaying()){
            currentSeek = 0;
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }
        else{
//            mediaPlayer.setNextMediaPlayer();
            currentSeek = 0;

        }
    }
    public static void nextMediaPlayer(){
//        mediaPlayer.setNextMediaPlayer();
    }


    public class LocalBinder extends Binder {
        ForegroundAudioService getService() {
            return ForegroundAudioService.this;
        }
        public void reloadNotification(){

        }
    }

    @Override
    public void onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE);
        super.onDestroy();
    }

    int time = 0;
    public class TimeThread implements Runnable{
        Thread thread;
        @Override
        public void run() {
            try {
                while (time<6000){
                    Thread.sleep(1);
                    if(playing!=mediaPlayer.isPlaying()){
                        customNotification();
                        playing = mediaPlayer.isPlaying();
                    }
                    if (!playing){
                        time++;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(time>=6000){
                time = 0;
                stopForeground(STOP_FOREGROUND_REMOVE);
                thread = null;
            }
        }
        void start(){
            if(thread == null){
                thread = new Thread(this);
                thread.start();
            }
        }
    }
}
