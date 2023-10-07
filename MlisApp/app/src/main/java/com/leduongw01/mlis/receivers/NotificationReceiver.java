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
import com.leduongw01.mlis.services.ForegroundAudioService;
import com.leduongw01.mlis.utils.MyComponent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        Integer id = b.getInt("a");
        switch (id) {
            case 4:{
                ForegroundAudioService.resetOrBackMediaPlayer();
                break;
            }
            case 5:{
                ForegroundAudioService.pauseOrResumeMediaPlayer();
                break;
            }
            case 6:{
                ForegroundAudioService.nextMediaPlayer();
                break;
            }
            default:{
                MyComponent.ToastShort(context, "default");
            }
        }
    }
}