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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.activities.PlayerActivity;
import com.leduongw01.mlis.databasehelper.MlisMySqlDBHelper;
import com.leduongw01.mlis.models.Favorite;
import com.leduongw01.mlis.models.Playlist;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.receivers.BackReceiverNotification;
import com.leduongw01.mlis.receivers.NextReceiverNotification;
import com.leduongw01.mlis.receivers.PauseResumeReceiverNotification;
import com.leduongw01.mlis.utils.Constant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ForegroundAudioService extends Service {
    private static final ForegroundAudioService instance = new ForegroundAudioService();
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private static int timer = -1;
    private static int currentSeek = 0;
    private static int currentAudio = 0;
    private static Podcast currentPodcast = new Podcast();
    private static Playlist currentPlaylist = new Playlist();
    private static Favorite currentFavorite = new Favorite();
    private static List<Podcast> currentList = new ArrayList<>();
    IBinder localBinder = new LocalBinder();
    private static Handler handler = new Handler();
    private static Runnable runnable = null;
    public static RemoteViews notificationLayout;
    public static ForegroundAudioService getInstance() {
        return instance;
    }

    public static int getTimer() {
        return timer;
    }
    public static void setTimer(int newValue) {
        timer = newValue;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    public static Podcast getCurrentPodcast() {
        return currentPodcast;
    }
    public static void setCurrentPodcast(Podcast podcast){
        currentPodcast = podcast;
    }

    public static Integer getDuration() {
        return mediaPlayer.getDuration();
    }
    public static MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }
    public static void setMediaPlayer(MediaPlayer newmediaPlayer){
        mediaPlayer.release();
        mediaPlayer = newmediaPlayer;
    }

    public static Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }
    public static void setCurrentPlaylist(Playlist playlist){
        currentPlaylist = playlist;
    }

    public static Favorite getCurrentFavorite() {
        return currentFavorite;
    }
    public static void setCurrentFavorite(Favorite favorite){
        currentFavorite= favorite;
    }

    public static List<Podcast> getCurrentList() {
        return currentList;
    }
    public static void setCurrentList(List<Podcast> newList) {
        currentList = newList;
    }

    public static int getCurrentSeek() {
        return currentSeek;
    }
    public static void setCurrentSeek(int seek){
        getMediaPlayer().seekTo(seek);
        currentSeek = seek;
    }
    public static int getCurrentAudio(){
        return currentAudio;
    }
    public static void setCurrentAudio(int position){
        currentAudio = position;
    }
    public static Handler getHandler(){
        return handler;
    }
    public static Runnable getRunnable(){
        return runnable;
    }
    public static void setRunable(Runnable newrunnable){
        runnable = newrunnable;
    }

    public void stopMediaPlayer(){
        getMediaPlayer().release();
    }
    public void startMediaPlayerInFavorite(Favorite favorite, int position){
//        BackgroundLoadDataService.getAllPlaylist().
    }
    public void startMediaPlayerInPlaylist(Playlist playlist, int position){
        setCurrentPlaylist(playlist);
        setCurrentList(BackgroundLoadDataService.getPodcastInPlaylist(playlist));
        setCurrentAudio(position);
        setCurrentPodcast(getCurrentList().get(getCurrentAudio()));
        setCurrentSeek(0);
        startPodcast(getCurrentPodcast());
    }
    public void startPodcast(Context context, Podcast podcast){
        loadMediaPlayerFromUrl(podcast.getUrl());
        new MlisMySqlDBHelper(context).putPodcastToRecent(podcast);
    }
    public void startPodcast(Podcast podcast){
        loadMediaPlayerFromUrl(podcast.getUrl());
        new MlisMySqlDBHelper(this).putPodcastToRecent(podcast);
    }

    public void nextAudio() {
        if (getCurrentAudio()<getCurrentList().size()){
            setCurrentAudio(getCurrentAudio()+1);
            setCurrentPodcast(getCurrentList().get(getCurrentAudio()));
            setCurrentSeek(0);
            startPodcast(getCurrentPodcast());
        }
    }
    public void backAudio() {
        if (getCurrentAudio()>0){
            setCurrentAudio(getCurrentAudio()-1);
            setCurrentPodcast(getCurrentList().get(getCurrentAudio()));
            setCurrentSeek(0);
            startPodcast(getCurrentPodcast());
        }
    }

    private void loadMediaPlayerFromUrl(String url) {
        if (url != null && !url.equals("")) {
            setMediaPlayer(new CustomMediaPlayer());
            try {
                getMediaPlayer().setDataSource(url);
                getMediaPlayer().prepare();
                getMediaPlayer().start();
                getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        nextAudio();
                    }
                });
            } catch (IOException e) {
                Log.e(Constant.ERROR, "Tai nhac that bai");
                e.printStackTrace();
            }
        }
        else{
            setMediaPlayer(CustomMediaPlayer.create(this, R.raw.bgbgbg));
        }
    }

    @SuppressLint({"RemoteViewLayout"})
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startMediaPlayerInPlaylist(currentPlaylist, currentAudio);
        startNotification();
        startTimerAndUpdateNotify();
        return START_NOT_STICKY;
    }

    private PendingIntent onButtonNotificationClick(Context context, Integer id) {
        Intent intent = new Intent();
        if (Objects.equals(id, Constant.NEXT)) {
            intent = new Intent(this, NextReceiverNotification.class);
        } else if (Objects.equals(id, Constant.BACK)) {
            intent = new Intent(this, BackReceiverNotification.class);
        } else if (Objects.equals(id, Constant.PAUSE_RESUME)) {
            intent = new Intent(this, PauseResumeReceiverNotification.class);
        } else if (Objects.equals(id, Constant.IMAGE)) {
            Intent i = new Intent(this, PlayerActivity.class);
            i.putExtra("continue", true);
            return PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return PendingIntent.getBroadcast(context, 200, intent, 0);
    }

    @SuppressLint("RemoteViewLayout")
    Notification customNotification() {
//        Intent notificationIntent = new Intent(this, LoginActivity.class);
//        Context context = getApplicationContext();
//        if (context!= null){
//            int a = 0;
//        }
        NotificationChannel serviceChannel = new NotificationChannel(
                "MlisMediaPlayerF",
                "Mlis MediaPlayer",
                NotificationManager.IMPORTANCE_NONE
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
        if (getMediaPlayer().isPlaying()) {
            notificationLayout = new RemoteViews(getPackageName(), R.layout.nofication_audio);
        } else {
            notificationLayout = new RemoteViews(getPackageName(), R.layout.nofication_audio_stop);
        }
        notificationLayout.setTextViewText(R.id.tvTenTruyen, getCurrentPodcast().getName());
        notificationLayout.setImageViewBitmap(R.id.ivAudio , BackgroundLoadDataService.getBitmapById(getCurrentPodcast().get_id(), Constant.PODCAST));
        notificationLayout.setOnClickPendingIntent(R.id.ivbackNofication, onButtonNotificationClick(getApplicationContext(), Constant.BACK));
        notificationLayout.setOnClickPendingIntent(R.id.ivControllNofication, onButtonNotificationClick(getApplicationContext(), Constant.PAUSE_RESUME));
        notificationLayout.setOnClickPendingIntent(R.id.ivNextNofication, onButtonNotificationClick(getApplicationContext(), Constant.NEXT));
        notificationLayout.setOnClickPendingIntent(R.id.ivAudio, onButtonNotificationClick(getApplicationContext(), Constant.IMAGE));
        return new NotificationCompat.Builder(this, "MlisMediaPlayerF")
                .setSmallIcon(R.drawable.outline_music_note_24)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayout)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSilent(true)
                .build();
    }
    private void startNotification(){
        startForeground(1, customNotification());
    }
    private void updateNotification() {
//        Notification notification = customNotification();
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(1, notification);
        startForeground(1, customNotification());
    }

    public static void pauseOrResumeMediaPlayer() {
        if (getMediaPlayer().isPlaying()) {
            currentSeek = getMediaPlayer().getCurrentPosition();
            getMediaPlayer().pause();
        } else {
            getMediaPlayer().seekTo(getCurrentSeek());
            getMediaPlayer().start();
        }
    }
    public void pauseMediaPlayer() {
        setCurrentSeek(getMediaPlayer().getCurrentPosition());
        getMediaPlayer().pause();
    }
    public void skipNext10s() {
        setCurrentSeek(getMediaPlayer().getCurrentPosition());
        if(getMediaPlayer().getCurrentPosition()+10000< getMediaPlayer().getDuration()){
            getMediaPlayer().seekTo(getMediaPlayer().getCurrentPosition()+10000);
            setCurrentSeek(getMediaPlayer().getCurrentPosition());
        }
        else{
            getMediaPlayer().seekTo(getMediaPlayer().getDuration());
            setCurrentSeek(getMediaPlayer().getDuration());
        }
    }
    public void forwardprevious10s() {
        setCurrentSeek(getMediaPlayer().getCurrentPosition());
        if(getMediaPlayer().getCurrentPosition()-10000> 0){
            getMediaPlayer().seekTo(getMediaPlayer().getCurrentPosition()-10000);
            setCurrentSeek(getMediaPlayer().getCurrentPosition());
        }
        else{
            getMediaPlayer().seekTo(0);
            setCurrentSeek(0);
        }
    }
    public void startTimerAndUpdateNotify(){
        setRunable(new Runnable() {
            @Override
            public void run() {
                if (timer==0){
                    pauseMediaPlayer();
                    timer = -1;
                }
                else if(timer!=-1){
                    timer--;
                }
                updateNotification();
                getHandler().postDelayed(this, 1000);
            }
        });
        //thêm cái gì đó để tắt lặp?
        getHandler().postDelayed(getRunnable(), 1000);
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
