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
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private int timer = -1;
    private int currentSeek = 0;
    private int currentAudio = 0;
    private Podcast currentPodcast = new Podcast();
    private Playlist currentPlaylist = new Playlist();
    private Favorite currentFavorite = new Favorite();
    private List<Podcast> currentList = new ArrayList<>();
    IBinder localBinder = new LocalBinder();
    private Handler handler = new Handler();
    private Runnable runnable = null;
    public static RemoteViews notificationLayout;
    TimeThread timeThread = new TimeThread();
    public static ForegroundAudioService getInstance() {
        return instance;
    }

    public static int getTimer() {
        return getInstance().timer;
    }
    public static void setTimer(int timer) {
        getInstance().timer = timer;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    public static Podcast getCurrentPodcast() {
        return getInstance().currentPodcast;
    }
    public static void setCurrentPodcast(Podcast podcast){
        getInstance().currentPodcast = podcast;
    }

    public static Integer getDuration() {
        return getInstance().mediaPlayer.getDuration();
    }
    public static MediaPlayer getMediaPlayer(){
        return getInstance().mediaPlayer;
    }
    public static void setMediaPlayer(MediaPlayer mediaPlayer){
        getInstance().mediaPlayer = mediaPlayer;
    }

    public static Playlist getCurrentPlaylist() {
        return getInstance().currentPlaylist;
    }
    public static void setCurrentPlaylist(Playlist playlist){
        getInstance().currentPlaylist = playlist;
    }

    public static Favorite getCurrentFavorite() {
        return getInstance().currentFavorite;
    }
    public static void setCurrentFavorite(Favorite favorite){
        getInstance().currentFavorite= favorite;
    }

    public static List<Podcast> getCurrentList() {
        return getInstance().currentList;
    }
    public static void setCurrentList(List<Podcast> newList) {
        getInstance().currentList = newList;
    }

    public static int getCurrentSeek() {
        return getInstance().currentSeek;
    }
    public void setCurrentSeek(int seek){
        getMediaPlayer().seekTo(seek);
        getInstance().currentSeek = seek;
    }
    public static int getCurrentAudio(){
        return getInstance().currentAudio;
    }
    public static void setCurrentAudio(int position){
        getInstance().currentAudio = position;
    }
    public static Handler getHandler(){
        return getInstance().handler;
    }
    public static Runnable getRunnable(){
        return getInstance().runnable;
    }
    public static void setRunable(Runnable runnable){
        getInstance().runnable = runnable;
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
    public static void startPodcast(Podcast podcast){
        getInstance().loadMediaPlayerFromUrl(podcast.getUrl());
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
            return PendingIntent.getActivity(this, 0, new Intent(this, PlayerActivity.class), 0);
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
        notificationLayout.setImageViewBitmap(R.id.ivAudio , BackgroundLoadDataService.getInstance().getBitmapById(getCurrentPodcast().get_id(), Constant.PODCAST));
        notificationLayout.setOnClickPendingIntent(R.id.ivbackNofication, onButtonNotificationClick(getApplicationContext(), Constant.BACK));
        notificationLayout.setOnClickPendingIntent(R.id.ivControllNofication, onButtonNotificationClick(getApplicationContext(), Constant.PAUSE_RESUME));
        notificationLayout.setOnClickPendingIntent(R.id.ivNextNofication, onButtonNotificationClick(getApplicationContext(), Constant.NEXT));
        notificationLayout.setOnClickPendingIntent(R.id.ivAudio, onButtonNotificationClick(getApplicationContext(), Constant.IMAGE));
        Notification notification = new NotificationCompat.Builder(this, "MlisMediaPlayerF")
                .setSmallIcon(R.drawable.outline_music_note_24)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayout)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSilent(true)
                .build();
        return notification;
    }
    private void startNotification(){
        startForeground(1, customNotification());
    }
    private void updateNotification() {
        Notification notification = customNotification();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notification);
    }

    public void pauseOrResumeMediaPlayer() {
        if (getMediaPlayer().isPlaying()) {
            setCurrentSeek(getMediaPlayer().getCurrentPosition());
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

    int time = 0;
    public class TimeThread implements Runnable {
        Thread thread;
        @Override
        public void run() {
            try {
                while (time < WaitTime) {
                    Thread.sleep(1000);
                    updateNotification();
                    //hẹn giờ tắt
                    if (timer==0){
                        pauseMediaPlayer();
                        timer = -1;
                    }
                    else if(timer!=-1){
                        timer--;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (time >= WaitTime) {
                time = 0;
                currentPodcast = new Podcast();
                currentSeek = 0;
                currentAudio = 0;
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
    class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        public DownloadImageAsyncTask(){}
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            Bitmap decoded = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                mIcon11.compress(Bitmap.CompressFormat.PNG, 25, out);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            } catch (Exception e) {
                Log.e("error background service", e.getMessage());
                e.printStackTrace();
            }
            return decoded;
        }

        protected void onPostExecute(Bitmap result) {
        }

        @Override
        protected void onCancelled(Bitmap bitmap) {
            super.onCancelled(bitmap);
        }
    }
}
