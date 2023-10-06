package com.leduongw01.mlis.receivers;

import static com.leduongw01.mlis.services.ForegroundAudioService.mediaPlayer;
import static com.leduongw01.mlis.services.ForegroundAudioService.playing;
import static com.leduongw01.mlis.services.ForegroundAudioService.currentSeek;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.utils.MyComponent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        Integer id = b.getInt("a");
        switch (id) {
            case 4:{
                mediaPlayer.stop();
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            }
            case 5:{
                if (playing){
                    mediaPlayer.stop();
                    currentSeek = mediaPlayer.getCurrentPosition();
                    playing = false;
//                    binding.ivControllNofication.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_arrow_24));
                }
                else{
                    mediaPlayer.seekTo(currentSeek);
                    mediaPlayer.start();
                    playing = true;
//                    binding.ivControllNofication.setImageDrawable(getDrawable(R.drawable.pause_svgrepo_com));
                }
                break;
            }
            case 6:{
                MyComponent.ToastShort(context, "next");
                break;
            }
            default:{
                MyComponent.ToastShort(context, "default");
            }
        }
    }
}