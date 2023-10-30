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
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.activities.PlayerActivity;
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
import java.util.Objects;

public class ForegroundAudioService extends Service {
    private static ForegroundAudioService instance = new ForegroundAudioService();
    private static int currentSeek = 0;
    private static boolean playing = false;
    public static boolean waiting = true;
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private static ArrayList<Podcast> podcastQueue = new ArrayList<>();
    private static int currentAudio = 0;
    private static int timer = -1;
    public static Podcast podcastTemp = new Podcast();
    public static Podcast currentPodcast = new Podcast();
    public static Bitmap imagePod = null;
    IBinder localBinder = new LocalBinder();
    public static RemoteViews notificationLayout;
    TimeThread timeThread = new TimeThread();

    public static int getTimer() {
        return timer;
    }

    public static void setTimer(int timer) {
        ForegroundAudioService.timer = timer;
    }

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
    public boolean getPlaying(){ return playing;}

    public int getCurrentSeek() {
        return mediaPlayer.getCurrentPosition();
    }
//    public void setCurrentSeek(int seek) {
//        return currentSeek = currentSeek = seek;
//    }
    public void setCurrentSeek(int seek){
        mediaPlayer.seekTo(seek);
        currentSeek = seek;
    }

    public ArrayList<Podcast> getPodcastQueue(){
        return podcastQueue;
    }
    public MediaPlayer getMediaPlayer() {
        return ForegroundAudioService.mediaPlayer;
    }
    public void stopMediaPlayer(){
//        timeThread = new TimeThread();
        mediaPlayer.release();
        timeThread = null;
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
        currentAudio = position;
        currentPodcast = podcastQueue.get(position);
        new DownloadImageAsyncTask().execute(currentPodcast.getUrlImg());
//        customNotification();
        loadMediaPlayerFromUrl(url);
    }

    public void startPodcast(Podcast podcast){
        podcastQueue.clear();
        podcastQueue.add(podcast);
        loadMediaPlayerByPosition(0);
        mediaPlayer.start();
    }
    public void startPodcastByList(ArrayList<Podcast> podcastArrayList){
        podcastQueue = podcastArrayList;
        loadMediaPlayerByPosition(0);
        mediaPlayer.start();
    }
    public void initQueueWithStartPodcast(Podcast podcast){
        podcastQueue.clear();
        podcastQueue.add(podcast);
        currentPodcast = podcast;
        loadMediaPlayerByPosition(0);
        mediaPlayer.start();
    }
    public void loadNextPodcast(String url){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                try {
                    CustomMediaPlayer mediaPlayer1 = new CustomMediaPlayer();
                    mediaPlayer1.setDataSource(url);
                    waiting = true;
                    mediaPlayer1.prepare();
                    waiting = false;
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
                waiting = true;
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                currentSeek = 0;
                waiting = false;
            } catch (IOException e) {
                Log.e(Constant.ERROR, "Tai nhac that bai");
                e.printStackTrace();
            }
        }
        else{
            mediaPlayer = new CustomMediaPlayer();
            mediaPlayer = CustomMediaPlayer.create(this, R.raw.bgbgbg);
        }
    }

    @SuppressLint({"RemoteViewLayout"})
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        timeThread = new TimeThread();
        timeThread.start();
        customNotification();
        fillData();
        return START_NOT_STICKY;
    }

    private void fillData(){

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
    void customNotification() {
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
        if (mediaPlayer.isPlaying()) {
            notificationLayout = new RemoteViews(getPackageName(), R.layout.nofication_audio);
        } else {
            notificationLayout = new RemoteViews(getPackageName(), R.layout.nofication_audio_stop);
        }
        notificationLayout.setTextViewText(R.id.tvTenTruyen, currentPodcast.getName());
        notificationLayout.setImageViewBitmap(R.id.ivAudio , imagePod);
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
        startForeground(1, notification);
    }

    public void pauseOrResumeMediaPlayer() {
        if (mediaPlayer.isPlaying()) {
            currentSeek = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
//            playing = false;
        } else {
            mediaPlayer.seekTo(currentSeek);
            mediaPlayer.start();
//            playing = true;
        }
    }
    public void pauseMediaPlayer() {
        currentSeek = mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();
    }
    public void skipNext10s() {
        currentSeek = mediaPlayer.getCurrentPosition();
        if(mediaPlayer.getCurrentPosition()+10000< mediaPlayer.getDuration()){
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
            currentSeek = mediaPlayer.getCurrentPosition();
        }
        else{
            mediaPlayer.seekTo(mediaPlayer.getDuration());
            currentSeek = mediaPlayer.getDuration();
        }
    }
    public void forwardprevious10s() {
        currentSeek = mediaPlayer.getCurrentPosition();
        if(mediaPlayer.getCurrentPosition()-10000> 0){
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
            currentSeek = mediaPlayer.getCurrentPosition();
        }
        else{
            mediaPlayer.seekTo(0);
            currentSeek = 0;
        }
    }

    public void resetMediaPlayer() {
        currentSeek = 0;
        mediaPlayer.seekTo(0);
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
                    //hẹn giờ tắt
                    if (timer==0){
                        pauseMediaPlayer();
                        timer = -1;
                    }
                    else if(timer!=-1){
                        timer--;
                    }
                    if (playing) {
                        time = 0;
                    } else {
                        time++;
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
                imagePod = decoded;
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
