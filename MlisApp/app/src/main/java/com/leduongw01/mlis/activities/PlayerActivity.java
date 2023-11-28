package com.leduongw01.mlis.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.DialogCompat;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.databasehelper.MlisMySqlDBHelper;
import com.leduongw01.mlis.databinding.ActivityPlayerBinding;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.services.ForegroundAudioService;
import com.leduongw01.mlis.utils.Constant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Objects;

public class PlayerActivity extends AppCompatActivity {
    ActivityPlayerBinding binding;
    Runnable runnable;
    Handler handler;
    boolean playing = false;
    MlisMySqlDBHelper mlisMySqlDBHelper = new MlisMySqlDBHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        Objects.requireNonNull(getSupportActionBar()).hide();
        boolean b= getIntent().getBooleanExtra("continue", false);
        if (b){
            Log.d("continue", "yessssss");
            initSeekBar();
            ktSuKien();
            fillData();
        }
        else{
            String podcastId = getIntent().getStringExtra("podcastId");
            String playlistId = getIntent().getStringExtra("playlistId");
            String favoriteId = getIntent().getStringExtra("favoriteId");
            Integer indexPodcast = getIntent().getIntExtra("index", -1);
            if (indexPodcast==-1){
                indexPodcast = BackgroundLoadDataService.getIndexOfPodcastInPlayList(podcastId, playlistId);
            }
            if(!Objects.equals(playlistId, "")){
                Intent intent = new Intent(this, ForegroundAudioService.class);
                ForegroundAudioService.setCurrentPodcast(BackgroundLoadDataService.getPodcastById(podcastId));
                ForegroundAudioService.setCurrentPlaylist(BackgroundLoadDataService.getPlaylistById(playlistId));
                ForegroundAudioService.setCurrentList(BackgroundLoadDataService.getPodcastInPlaylist(playlistId));
                ForegroundAudioService.setCurrentAudio(indexPodcast);
                mlisMySqlDBHelper.putPodcastToRecent(ForegroundAudioService.getCurrentPodcast());
                startService(intent);
            }
            else if (!favoriteId.equals("")){
                Intent intent = new Intent(this, ForegroundAudioService.class);
                ForegroundAudioService.setCurrentPodcast(BackgroundLoadDataService.getPodcastById(podcastId));
                ForegroundAudioService.setCurrentPlaylist(BackgroundLoadDataService.getPlaylistById(playlistId));
                ForegroundAudioService.setCurrentList(BackgroundLoadDataService.getPodcastInPlaylist(playlistId));
                ForegroundAudioService.setCurrentAudio(indexPodcast);
                startService(intent);
            }
            initSeekBar();
            ktSuKien();
            fillData();
        }
    }
    void initSeekBar() {
        binding.seekBarMediaPlayer.setProgress(0);
        binding.seekBarMediaPlayer.setMax(ForegroundAudioService.getMediaPlayer().getDuration());
        binding.tvMediaPlayerDuration.setText(NumberTimeToString(ForegroundAudioService.getMediaPlayer().getDuration()));
        binding.seekBarMediaPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    ForegroundAudioService.setCurrentSeek(i);
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
                binding.seekBarMediaPlayer.setProgress(ForegroundAudioService.getMediaPlayer().getCurrentPosition());
                binding.tvCurrentSeek.setText(NumberTimeToString(ForegroundAudioService.getMediaPlayer().getCurrentPosition()));
                binding.seekBarMediaPlayer.setMax(ForegroundAudioService.getMediaPlayer().getDuration());
                binding.tvMediaPlayerDuration.setText(NumberTimeToString(ForegroundAudioService.getMediaPlayer().getDuration()));
                if(ForegroundAudioService.getMediaPlayer().isPlaying()!= playing) {
                    if (ForegroundAudioService.getMediaPlayer().isPlaying()) {
                        binding.icPauseResumeMediaPlayer.setImageResource(R.drawable.baseline_pause_24);
                    } else {
                        binding.icPauseResumeMediaPlayer.setImageResource(R.drawable.baseline_play_arrow_24);
                    }
                    playing = ForegroundAudioService.getMediaPlayer().isPlaying();
                }
                handler.postDelayed(runnable, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
        ForegroundAudioService.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
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
                ForegroundAudioService.pauseOrResumeMediaPlayer();
//                if(ForegroundAudioService.getInstance().getMediaPlayer().isPlaying()){
//                    binding.icPauseResumeMediaPlayer.setImageResource(R.drawable.baseline_pause_24);
//                }
//                else{
//                    binding.icPauseResumeMediaPlayer.setImageResource(R.drawable.baseline_play_arrow_24);
//                }
//                Intent intent = new Intent(getApplicationContext(), ForegroundAudioService.class);
//                startService(intent);
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
        binding.ivPlayerlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlayerActivity.this, PlayerListActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PlayerActivity.this, HomeScreen.class);
        startActivity(i);
    }

    void fillData() {
        binding.tvTenTruyen.setText(ForegroundAudioService.getCurrentPodcast().getName());
        binding.tvBoSung.setText(ForegroundAudioService.getCurrentPlaylist().getAuthor());
        runnable.run();
        binding.ivPlayer.setImageBitmap(BackgroundLoadDataService.getBitmapById(ForegroundAudioService.getCurrentPodcast().get_id(), Constant.PODCAST));
    }
    String NumberTimeToString(int number){
        number/=1000;
        if(number/3600!=0){
            return insert0(number/3600)+":"+insert0((number%3600)/60)+":"+insert0(number%60);
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