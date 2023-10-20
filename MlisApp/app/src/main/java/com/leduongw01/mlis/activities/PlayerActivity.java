package com.leduongw01.mlis.activities;

import static com.leduongw01.mlis.services.ForegroundAudioService.podcastTemp;
import static com.leduongw01.mlis.services.ForegroundAudioService.waiting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.DialogCompat;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.databinding.ActivityPlayerBinding;
import com.leduongw01.mlis.services.ForegroundAudioService;

import java.util.Objects;

public class PlayerActivity extends AppCompatActivity {
    ActivityPlayerBinding binding;
    Runnable runnable;
    Handler handler;
    boolean playing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        Objects.requireNonNull(getSupportActionBar()).hide();
        if(getIntent().getIntExtra("startNow", 0) == 1){
            ForegroundAudioService.getInstance().initQueueWithStartPodcast(podcastTemp);
            Intent intent = new Intent(this, ForegroundAudioService.class);
            intent.putExtra("startNow", 1);
            startService(intent);
        }
        initSeekBar();
        ktSuKien();
        fillData();
    }
    void initSeekBar(){
        binding.seekBarMediaPlayer.setProgress(0);
        binding.seekBarMediaPlayer.setMax(ForegroundAudioService.getInstance().getMediaPlayer().getDuration());
        binding.tvMediaPlayerDuration.setText(NumberTimeToString(ForegroundAudioService.getInstance().getMediaPlayer().getDuration()));
        binding.seekBarMediaPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    ForegroundAudioService.getInstance().setCurrentSeek(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
//        binding.seekBarMediaPlayer.setP(getResources().getColor(R.color.emerald));
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                binding.seekBarMediaPlayer.setProgress(ForegroundAudioService.getInstance().getMediaPlayer().getCurrentPosition());
                binding.tvCurrentSeek.setText(NumberTimeToString(ForegroundAudioService.getInstance().getMediaPlayer().getCurrentPosition()));
                if(ForegroundAudioService.getInstance().getPlaying()!= playing) {
                    if (ForegroundAudioService.getInstance().getPlaying()) {
                        binding.icPauseResumeMediaPlayer.setImageResource(R.drawable.baseline_pause_24);
                    } else {
                        binding.icPauseResumeMediaPlayer.setImageResource(R.drawable.baseline_play_arrow_24);
                    }
                    playing = ForegroundAudioService.getInstance().getPlaying();
                }
                handler.postDelayed(runnable, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
        ForegroundAudioService.getInstance().getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.pause();
                binding.icPauseResumeMediaPlayer.setImageResource(R.drawable.baseline_play_arrow_24);
            }
        });

    }
    void ktSuKien(){
        binding.icPauseResumeMediaPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForegroundAudioService.getInstance().pauseOrResumeMediaPlayer();
//                if(ForegroundAudioService.getInstance().getMediaPlayer().isPlaying()){
//                    binding.icPauseResumeMediaPlayer.setImageResource(R.drawable.baseline_pause_24);
//                }
//                else{
//                    binding.icPauseResumeMediaPlayer.setImageResource(R.drawable.baseline_play_arrow_24);
//                }
                Intent intent = new Intent(getApplicationContext(), ForegroundAudioService.class);
                startService(intent);
            }
        });
        binding.next10sMediaPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForegroundAudioService.getInstance().skipNext10s();
            }
        });
        binding.back10sMediaPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForegroundAudioService.getInstance().forwardprevious10s();
            }
        });
        binding.systemSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundControllDialog t = new SoundControllDialog(PlayerActivity.this);
                t.show();
            }
        });
        binding.timerPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimerDialog t = new TimerDialog(PlayerActivity.this);
                t.show();
            }
        });
    }
    void fillData(){
        binding.tvTenTruyen.setText(podcastTemp.getName());
        binding.tvBoSung.setText(podcastTemp.getAuthor());
        runnable.run();
    }
    String NumberTimeToString(int number){
        number/=1000;
        if(number/3600!=0){
            return insert0(number/3600)+":"+insert0(number/60)+":"+insert0(number%60);
        }
        else{
            return insert0(number/60)+":"+insert0(number%60);
        }
    }
    String insert0(int number){
        if (number<10){
            return "0"+number;
        }
        return ""+number;
    }
}