package com.leduongw01.mlis.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.adapter.PodcastRecentListenedAdapter;
import com.leduongw01.mlis.databinding.ActivityPlayerListBinding;
import com.leduongw01.mlis.databinding.ActivityPlaylistDetailBinding;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.Playlist;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.services.ForegroundAudioService;

import java.util.List;

public class PlayerListActivity extends AppCompatActivity {
    ActivityPlayerListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player_list);
        updateRecyclePlayerList();
    }
    void updateRecyclePlayerList(){
        Podcast podcast = ForegroundAudioService.getCurrentPodcast();
        List<Podcast> podcastList = ForegroundAudioService.getCurrentList();
        binding.tvTenTruyen.setText(podcast.getName());
        binding.tvAuthor.setText(BackgroundLoadDataService.getAuthorOfPodcast(podcast));
        binding.lvPlayerList.setAdapter(new PodcastRecentListenedAdapter(podcastList, new RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position) {
                ForegroundAudioService.setCurrentAudio(position);
                updateRecyclePlayerList();
            }
        }));
    }
}