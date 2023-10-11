package com.leduongw01.mlis.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.services.ForegroundAudioService;

public class PauseResumeReceiverNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ForegroundAudioService.getInstance().pauseOrResumeMediaPlayer();
    }
}
