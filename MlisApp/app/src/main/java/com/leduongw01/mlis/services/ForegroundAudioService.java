package com.leduongw01.mlis.services;

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
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.activities.LoginActivity;
import com.leduongw01.mlis.receivers.BackReceiverNotification;
import com.leduongw01.mlis.receivers.NextReceiverNotification;
import com.leduongw01.mlis.receivers.NotificationReceiver;
import com.leduongw01.mlis.receivers.PauseResumeReceiverNotification;
import com.leduongw01.mlis.utils.Const;

import java.util.Objects;

public class ForegroundAudioService extends Service {
    public static int currentSeek = 0;
    public static boolean playing = false;
    public static MediaPlayer mediaPlayer;
    String ACTION_CLICK = "actionclick";
    String EXTRA_CLICK = "a";
    IBinder localBinder = new LocalBinder();
    RemoteViews notificationLayout;
    TimeThread timeThread = new TimeThread();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @SuppressLint("RemoteViewLayout")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, LoginActivity.class);
        NotificationChannel serviceChannel = new NotificationChannel(
                "hoho",
                "Thanh dieu huong truyen",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
        notificationLayout = new RemoteViews(getPackageName(), R.layout.nofication_audio);
        notificationLayout.setOnClickPendingIntent(R.id.ivbackNofication, onButtonNotificationClick(getApplicationContext(), Const.BACK));
//        notificationLayout.setOnClickPendingIntent(R.id.myNofication, onButtonNotificationClick(getApplicationContext(),R.id.myNofication));
        notificationLayout.setOnClickPendingIntent(R.id.ivControllNofication, onButtonNotificationClick(getApplicationContext(), Const.PAUSE_RESUME));
        notificationLayout.setOnClickPendingIntent(R.id.ivNextNofication, onButtonNotificationClick(getApplicationContext(), Const.NEXT));
//        notificationLayout.setOnClickPendingIntent(R.id.ivAudio, onButtonNotificationClick(getApplicationContext(), Const.IMAGE));
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(this, R.raw.bgbgbg);
//        try {
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.setDataSource(Consts.M1);
//            mediaPlayer.prepare();
            mediaPlayer.start();
            Log.d("dddd", mediaPlayer.getDuration()+"");
            notificationLayout.setTextViewText(R.id.timeNotification, mediaPlayer.getDuration()/3600000+":"+ mediaPlayer.getDuration()%3600000/60000+":"+mediaPlayer.getDuration()/1000%60);
            playing = true;
//            timeThread.start();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, "hoho")
                .setSmallIcon(R.drawable.ic_baseline_play_arrow_24)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayout)
//                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.qiqi))
//                .addAction(R.drawable.back_svgrepo_com, "", onButtonNotificationClick(getApplicationContext(), Const.BACK))
//                .addAction(R.drawable.pause_svgrepo_com, "", onButtonNotificationClick(getApplicationContext(), Const.PAUSE_RESUME))
//                .addAction(R.drawable.next_svgrepo_com, "", onButtonNotificationClick(getApplicationContext(), Const.NEXT))
//                .setContentIntent(pendingIntent)
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();
//        notificationLayout.setTextViewText(R.id.timeNotification, "00:00:01");
        NotificationManager notificationManager =
                (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
        startForeground(1, notification);
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
        Bundle b = new Bundle();
        b.putInt("a", id);
        intent.putExtras(b);
        return PendingIntent.getBroadcast(context, 200, intent, 0);
    }

//
//    public class NotificationReceiver extends BroadcastReceiver{
//        public NotificationReceiver(){}
//        @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables"})
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int id = intent.getIntExtra(EXTRA_CLICK, -1);
//            switch (id) {
//                case R.id.myNofication:{
//                    //go layout
//                }
//                case R.id.ivbackNofication:{
//                    mediaPlayer.stop();
//                    mediaPlayer.seekTo(0);
//                    mediaPlayer.start();
//                    break;
//                }
//                case  R.id.ivControllNofication:{
//                    if (playing){
//                        mediaPlayer.stop();
//                        currentSeek = mediaPlayer.getCurrentPosition();
//                        playing = false;
//                        binding.ivControllNofication.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_arrow_24));
//                    }
//                    else{
//                        mediaPlayer.seekTo(currentSeek);
//                        mediaPlayer.start();
//                        playing = true;
//                        binding.ivControllNofication.setImageDrawable(getDrawable(R.drawable.pause_svgrepo_com));
//                    }
//                    break;
//                }
//                case R.id.ivNextNofication:{
//                    MyComponent.ToastShort(context, "next");
//                    break;
//                }
//                default:{
//                    MyComponent.ToastShort(context, "hdoa");
//                }
//            }
//        }
//    }
    public class LocalBinder extends Binder {
        ForegroundAudioService getService() {
            return ForegroundAudioService.this;
        }
    }
    int time = 0;
    public class TimeThread implements Runnable{
        Thread thread;
        @Override
        public void run() {
            try {
                while (time>-1){
                    Thread.sleep(1000);
                    if(playing){
                        String t = time/3600+":"+time%3600/60+":"+time%60;
                        time++;
                        notificationLayout.setTextViewText(R.id.timeNotification, t);
                        Log.d("a",""+t);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
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
