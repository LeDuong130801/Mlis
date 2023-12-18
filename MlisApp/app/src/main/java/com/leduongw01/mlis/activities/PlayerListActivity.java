package com.leduongw01.mlis.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.adapter.PodcastListAdapter;
import com.leduongw01.mlis.databasehelper.MlisSqliteDBHelper;
import com.leduongw01.mlis.databinding.ActivityPlayerListBinding;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.services.ForegroundAudioService;
import com.leduongw01.mlis.utils.Constant;

import java.util.List;
import java.util.Objects;

public class PlayerListActivity extends AppCompatActivity {
    ActivityPlayerListBinding binding;
    MlisSqliteDBHelper mlisSqliteDBHelper = new MlisSqliteDBHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player_list);
        Objects.requireNonNull(getSupportActionBar()).hide();
        updateRecyclePlayerList();
    }
    void updateRecyclePlayerList(){
        Podcast podcast = ForegroundAudioService.getCurrentPodcast();
        List<Podcast> podcastList = ForegroundAudioService.getCurrentList();
        binding.tvTenTruyen.setText(podcast.getName());
        binding.tvAuthor.setText(BackgroundLoadDataService.getAuthorOfPodcast(podcast));
        binding.ivTruyen.setImageBitmap(BackgroundLoadDataService.getBitmapById(podcast.get_id(), Constant.PODCAST));
        binding.lvPlayerList.setAdapter(new PodcastListAdapter(PlayerListActivity.this, podcastList, new RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position) {
                ForegroundAudioService.setCurrentAudio(position);
                ForegroundAudioService.setCurrentPodcast(ForegroundAudioService.getCurrentList().get(position));
                String userId = BackgroundLoadDataService.getInstance().checkAuthen()? BackgroundLoadDataService.mlisUser.get_id() : "none";
                if (ForegroundAudioService.getCurrentFavorite() == null){
                    mlisSqliteDBHelper.putPodcastToRecent(ForegroundAudioService.getCurrentPodcast(), userId, "none", ForegroundAudioService.getCurrentPlaylist().get_id());
                }
                else{
                    mlisSqliteDBHelper.putPodcastToRecent(ForegroundAudioService.getCurrentPodcast(), userId, ForegroundAudioService.getCurrentFavorite().get_id(), "none");
                }
                updateRecyclePlayerList();
            }
        }));
        binding.lvPlayerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}