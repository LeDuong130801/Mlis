package com.leduongw01.mlis.receivers;

import static com.leduongw01.mlis.services.ForegroundAudioService.currentSeek;
import static com.leduongw01.mlis.services.ForegroundAudioService.mediaPlayer;
import static com.leduongw01.mlis.services.ForegroundAudioService.playing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.leduongw01.mlis.services.ForegroundAudioService;

public class BackReceiverNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ForegroundAudioService.resetOrBackMediaPlayer();
    }
}
