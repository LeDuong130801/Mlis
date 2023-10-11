package com.leduongw01.mlis.services;

import static com.leduongw01.mlis.utils.DefaultConfig.WaitTime;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.activities.LoginActivity;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.receivers.BackReceiverNotification;
import com.leduongw01.mlis.receivers.NextReceiverNotification;
import com.leduongw01.mlis.receivers.PauseResumeReceiverNotification;
import com.leduongw01.mlis.utils.Const;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ForegroundAudioService extends Service {
    private static ForegroundAudioService instance = new ForegroundAudioService();
    private static int currentSeek = 0;
    private static boolean playing = false;
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private static final ArrayList<Podcast> podcastQueue = new ArrayList<>();
    private static int currentAudio = 0;
    public static Podcast podcastTemp = new Podcast();
    IBinder localBinder = new LocalBinder();
    public static RemoteViews notificationLayout;
    TimeThread timeThread = new TimeThread();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    public static ForegroundAudioService getInstance() {
        return instance;
    }

    public Podcast getCurrentAudio() {
        return podcastQueue.get(currentAudio);
    }

    public Integer getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentSeek() {
        return currentSeek;
    }

    public ArrayList<Podcast> getPodcastQueue(){
        return podcastQueue;
    }
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void nextAudio() {
        if (currentAudio<podcastQueue.size()){
            currentAudio++;
            currentSeek = 0;
            loadMediaPlayerFromUrl(podcastQueue.get(currentAudio).getUrl());
        }
    }
    public void backAudio() {
        if (currentAudio>0){
            currentAudio--;
            currentSeek = 0;
            loadMediaPlayerFromUrl(podcastQueue.get(currentAudio).getUrl());
        }
    }

    public void loadMediaPlayerByPosition(Integer position) {
        String url = podcastQueue.get(position).getUrl();
//        customNotification();
        loadMediaPlayerFromUrl(url);
    }

    public void startPodcast(Podcast podcast){
        podcastQueue.clear();
        podcastQueue.add(podcast);
        loadMediaPlayerByPosition(0);
        mediaPlayer.start();
    }
    public void addPodcastToQueue(Podcast podcast){
        podcastQueue.add(podcast);
    }
    public void loadNextPodcast(String url){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                try {
                    CustomMediaPlayer mediaPlayer1 = new CustomMediaPlayer();
                    mediaPlayer1.setDataSource(url);
                    mediaPlayer1.prepare();
                    mediaPlayer.setNextMediaPlayer(mediaPlayer1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadMediaPlayerFromUrl(String url) {
        if (url != null && !url.equals("")) {
            mediaPlayer = new CustomMediaPlayer();
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
            } catch (IOException e) {
                Log.e(Const.ERROR, "Tai nhac that bai");
                e.printStackTrace();
            }
        }
        else{
            mediaPlayer = new CustomMediaPlayer();
            mediaPlayer = CustomMediaPlayer.create(this, R.raw.bgbgbg);
        }
    }

    @SuppressLint("RemoteViewLayout")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentAudio = intent.getIntExtra("position", 0);
        int startNow = intent.getIntExtra("startNow", 0);
        if(startNow==1){
            String url = intent.getStringExtra("url");
            loadMediaPlayerFromUrl(url);
        }
        podcastQueue.add(podcastTemp);
        loadMediaPlayerByPosition(currentAudio);
        mediaPlayer.start();
        timeThread.start();
        return START_NOT_STICKY;
    }

    private PendingIntent onButtonNotificationClick(Context context, Integer id) {
        Intent intent = new Intent();
        if (Objects.equals(id, Const.NEXT)) {
            intent = new Intent(this, NextReceiverNotification.class);
        } else if (Objects.equals(id, Const.BACK)) {
            intent = new Intent(this, BackReceiverNotification.class);
        } else if (Objects.equals(id, Const.PAUSE_RESUME)) {
            intent = new Intent(this, PauseResumeReceiverNotification.class);
        } else if (Objects.equals(id, Const.IMAGE)) {
            return PendingIntent.getActivity(this, 0, new Intent(this, LoginActivity.class), 0);
        }
        return PendingIntent.getBroadcast(context, 200, intent, 0);
    }

    @SuppressLint("RemoteViewLayout")
    void customNotification() {
//        Intent notificationIntent = new Intent(this, LoginActivity.class);
        Context context = getApplicationContext();
        if (context!= null){
            int a = 0;
        }
        NotificationChannel serviceChannel = new NotificationChannel(
                "MlisMediaPlayerF",
                "Mlis MediaPlayer",
                NotificationManager.IMPORTANCE_NONE
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
        if (mediaPlayer.isPlaying()) {
            notificationLayout = new RemoteViews(getPackageName(), R.layout.nofication_audio);
        } else {
            notificationLayout = new RemoteViews(getPackageName(), R.layout.nofication_audio_stop);
        }
        notificationLayout.setOnClickPendingIntent(R.id.ivbackNofication, onButtonNotificationClick(getApplicationContext(), Const.BACK));
        notificationLayout.setOnClickPendingIntent(R.id.ivControllNofication, onButtonNotificationClick(getApplicationContext(), Const.PAUSE_RESUME));
        notificationLayout.setOnClickPendingIntent(R.id.ivNextNofication, onButtonNotificationClick(getApplicationContext(), Const.NEXT));
        notificationLayout.setOnClickPendingIntent(R.id.ivAudio, onButtonNotificationClick(getApplicationContext(), Const.IMAGE));
        Notification notification = new NotificationCompat.Builder(this, "MlisMediaPlayerF")
                .setSmallIcon(R.drawable.outline_music_note_24)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayout)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();
        startForeground(1, notification);
    }

    public void pauseOrResumeMediaPlayer() {
        if (mediaPlayer.isPlaying()) {
            currentSeek = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        } else {
            mediaPlayer.seekTo(currentSeek);
            mediaPlayer.start();
        }
    }

    public void resetMediaPlayer() {
        if (mediaPlayer.isPlaying()) {
            currentSeek = 0;
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        } else {
            currentSeek = 0;
        }
    }

    public class LocalBinder extends Binder {
        ForegroundAudioService getService() {
            return ForegroundAudioService.this;
        }
    }

    @Override
    public void onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE);
        super.onDestroy();
    }

    int time = 0;

    public class TimeThread implements Runnable {
        Thread thread;

        @Override
        public void run() {
            try {
                while (time < WaitTime) {
                    Thread.sleep(1000);
                    if (playing != mediaPlayer.isPlaying()) {
                        customNotification();
                        playing = mediaPlayer.isPlaying();
                    }
                    if (!playing) {
                        time++;
                    } else {
                        time = 0;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (time >= WaitTime) {
                time = 0;
                stopForeground(STOP_FOREGROUND_REMOVE);
                thread = null;
            }
        }

        void start() {
            if (thread == null) {
                thread = new Thread(this);
                thread.start();
            }
        }
    }

    static class CustomMediaPlayer extends MediaPlayer {
        String dataSource;

        @Override
        public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
            // TODO Auto-generated method stub
            super.setDataSource(path);
            dataSource = path;
        }

        public String getDataSource() {
            return dataSource;
        }
    }
}
