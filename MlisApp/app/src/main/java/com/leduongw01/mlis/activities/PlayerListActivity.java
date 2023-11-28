package com.leduongw01.mlis.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.adapter.PodcastListAdapter;
import com.leduongw01.mlis.adapter.PodcastRecentListenedAdapter;
import com.leduongw01.mlis.databasehelper.MlisMySqlDBHelper;
import com.leduongw01.mlis.databinding.ActivityPlayerListBinding;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.Playlist;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.services.ForegroundAudioService;
import com.leduongw01.mlis.utils.Constant;

import java.util.List;
import java.util.Objects;

public class PlayerListActivity extends AppCompatActivity {
    ActivityPlayerListBinding binding;
    MlisMySqlDBHelper mlisMySqlDBHelper = new MlisMySqlDBHelper(this);
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
                ForegroundAudioService.getInstance().startMediaWithPosition(position);
                mlisMySqlDBHelper.putPodcastToRecent(ForegroundAudioService.getCurrentPodcast());
                updateRecyclePlayerList();
            }
        }));
        binding.lvPlayerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}