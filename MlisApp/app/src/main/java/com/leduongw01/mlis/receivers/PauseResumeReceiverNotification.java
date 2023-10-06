package com.leduongw01.mlis.receivers;

import static com.leduongw01.mlis.services.ForegroundAudioService.currentSeek;
import static com.leduongw01.mlis.services.ForegroundAudioService.mediaPlayer;
import static com.leduongw01.mlis.services.ForegroundAudioService.playing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PauseResumeReceiverNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (playing){
            mediaPlayer.pause();
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

    }
}
