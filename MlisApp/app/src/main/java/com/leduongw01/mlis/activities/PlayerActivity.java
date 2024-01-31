package com.leduongw01.mlis.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SeekBar;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.databasehelper.MlisSqliteDBHelper;
import com.leduongw01.mlis.databinding.ActivityPlayerBinding;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.services.ForegroundAudioService;
import com.leduongw01.mlis.utils.Constant;
import com.leduongw01.mlis.utils.MyComponent;

import java.io.IOException;
import java.util.Objects;

public class PlayerActivity extends AppCompatActivity {
    ActivityPlayerBinding binding;
    Runnable runnable;
    Handler handler;
    boolean playing = false;
    MlisSqliteDBHelper mlisSqliteDBHelper = new MlisSqliteDBHelper(this);
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        Objects.requireNonNull(getSupportActionBar()).hide();
        loadingDialog = new LoadingDialog(PlayerActivity.this);
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
            if (playlistId == null){
                playlistId = "none";
            }
            if (favoriteId == null){
                favoriteId = "none";
            }
            Integer indexPodcast = getIntent().getIntExtra("index", -1);
            if(!playlistId.equals("none")){
                if (indexPodcast == -1){
                    indexPodcast = BackgroundLoadDataService.getIndexOfPodcastInPlayList(podcastId, playlistId);
                }
                Intent intent = new Intent(this, ForegroundAudioService.class);
                ForegroundAudioService.setCurrentPodcast(BackgroundLoadDataService.getPodcastById(podcastId));
                ForegroundAudioService.setCurrentPlaylist(BackgroundLoadDataService.getPlaylistById(playlistId));
                ForegroundAudioService.setCurrentFavorite(null);
                ForegroundAudioService.setCurrentList(BackgroundLoadDataService.getPodcastInPlaylist(playlistId));
                ForegroundAudioService.setCurrentAudio(indexPodcast);
                Log.d("playlist", "onCreate: ");
                if (BackgroundLoadDataService.getInstance().checkAuthen()){
                    mlisSqliteDBHelper.putPodcastToRecent(ForegroundAudioService.getCurrentPodcast(), BackgroundLoadDataService.mlisUser.get_id(), "none", playlistId);
                }
                else mlisSqliteDBHelper.putPodcastToRecent(ForegroundAudioService.getCurrentPodcast(), "none", "none", playlistId);
                startService(intent);
            }
            else if (!favoriteId.equals("none")){
                if (indexPodcast==-1){
                    indexPodcast = BackgroundLoadDataService.getIndexOfPodcastInFavorite(podcastId, favoriteId);
                }
                Intent intent = new Intent(this, ForegroundAudioService.class);
                ForegroundAudioService.setCurrentPodcast(BackgroundLoadDataService.getPodcastById(podcastId));
                ForegroundAudioService.setCurrentFavorite(BackgroundLoadDataService.getFavoriteById(favoriteId));
                ForegroundAudioService.setCurrentPlaylist(null);
                ForegroundAudioService.setCurrentList(BackgroundLoadDataService.getPodcastListByFavorite(favoriteId));
                ForegroundAudioService.setCurrentAudio(indexPodcast);
                Log.d("favorite", "onCreate: ");
                if (BackgroundLoadDataService.getInstance().checkAuthen()){
                    mlisSqliteDBHelper.putPodcastToRecent(ForegroundAudioService.getCurrentPodcast(), BackgroundLoadDataService.mlisUser.get_id(), favoriteId, "none");
                }
                else mlisSqliteDBHelper.putPodcastToRecent(ForegroundAudioService.getCurrentPodcast(), "none", favoriteId, "none");
                startService(intent);
            }
            else{
                onBackPressed();
            }
            initSeekBar();
            ktSuKien();
            fillData();
        }
    }

    void showMenu(View view){
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_player, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.themvaodanhsach:{

            }
        }
        return super.onOptionsItemSelected(item);
    }

    void initSeekBar()  {
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
        final boolean[] f = {true};
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (ForegroundAudioService.getMediaPlayer().isPlaying() || f[0]){
                    binding.seekBarMediaPlayer.setProgress(ForegroundAudioService.getMediaPlayer().getCurrentPosition());
                    binding.tvCurrentSeek.setText(NumberTimeToString(ForegroundAudioService.getMediaPlayer().getCurrentPosition()));
                    binding.seekBarMediaPlayer.setMax(ForegroundAudioService.getMediaPlayer().getDuration());
                    binding.tvMediaPlayerDuration.setText(NumberTimeToString(ForegroundAudioService.getMediaPlayer().getDuration()));
                    f[0] = false;
                }
                if(ForegroundAudioService.getMediaPlayer().isPlaying()!= playing) {
                    if (ForegroundAudioService.getMediaPlayer().isPlaying()) {
                        binding.icPauseResumeMediaPlayer.setImageResource(R.drawable.baseline_pause_24);
                    } else {
                        binding.icPauseResumeMediaPlayer.setImageResource(R.drawable.baseline_play_arrow_24);
                    }
                    playing = ForegroundAudioService.getMediaPlayer().isPlaying();
                }
                if (binding.seekBarMediaPlayer.getProgress() == binding.seekBarMediaPlayer.getMax() && binding.seekBarMediaPlayer.getMax()!=0){
                    if(ForegroundAudioService.getInstance().nextAudio()){
                        startFore();
                        fillData();
                    }
                }
                if (ForegroundAudioService.getCurrentAudio()==0){
                    binding.backSkipMediaPlayer.setClickable(false);
                    binding.backSkipMediaPlayer.setColorFilter(R.color.light_gray);
                }
                else{
                    binding.backSkipMediaPlayer.setClickable(true);
                    binding.backSkipMediaPlayer.setColorFilter(null);
                }
                if(ForegroundAudioService.getCurrentAudio()==ForegroundAudioService.getCurrentList().size()-1){
                    binding.nextSkipMediaPlayer.setClickable(false);
                    binding.nextSkipMediaPlayer.setColorFilter(R.color.light_gray);
                }
                else{
                    binding.nextSkipMediaPlayer.setClickable(true);
                    binding.nextSkipMediaPlayer.setColorFilter(null);
                }
                if (ForegroundAudioService.getMediaPlayer().isPreparing()){
                    loadingDialog.show();
                }
                else{
                    loadingDialog.hide();
                }
                handler.postDelayed(runnable, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);

    }
    void ktSuKien(){
        binding.icPauseResumeMediaPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForegroundAudioService.pauseOrResumeMediaPlayer();
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
        binding.openChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlayerActivity.this, ChatActivity.class);
                startActivity(i);
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
        binding.openMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });
        binding.backSkipMediaPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ForegroundAudioService.getInstance().backAudio()) {
                    startFore();
                    fillData();
                }
            }
        });
        binding.nextSkipMediaPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ForegroundAudioService.getInstance().nextAudio()){
                    startFore();
                    fillData();
                }
            }
        });
    }


    void fillData() {
        binding.tvTenTruyen.setText(ForegroundAudioService.getCurrentPodcast().getName());
        binding.tvBoSung.setText(BackgroundLoadDataService.getAuthorOfPodcast(ForegroundAudioService.getCurrentPodcast()));
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
    private void startFore(){
        Intent i = new Intent(PlayerActivity.this, ForegroundAudioService.class);
        startService(i);
    }
}