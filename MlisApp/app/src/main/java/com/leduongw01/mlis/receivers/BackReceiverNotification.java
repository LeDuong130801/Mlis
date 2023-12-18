package com.leduongw01.mlis.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.leduongw01.mlis.services.ForegroundAudioService;

public class BackReceiverNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(ForegroundAudioService.getInstance().backAudio()){
            Intent i = new Intent(context, ForegroundAudioService.class);
            context.startService(i);
        }
    }
}
