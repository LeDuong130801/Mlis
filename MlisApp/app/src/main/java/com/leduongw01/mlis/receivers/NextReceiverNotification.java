package com.leduongw01.mlis.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.leduongw01.mlis.services.ForegroundAudioService;
import com.leduongw01.mlis.utils.MyComponent;

public class NextReceiverNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ForegroundAudioService.getInstance().nextAudio();
        Intent i = new Intent(context, ForegroundAudioService.class);
        context.startService(i);
    }
}
