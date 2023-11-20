package com.leduongw01.mlis.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.adapter.AllPlaylistAdapter;
import com.leduongw01.mlis.adapter.PlaylistDetailAdapter;
import com.leduongw01.mlis.databinding.ActivityPlaylistDetailBinding;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.Playlist;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDetailActivity extends AppCompatActivity {
    ActivityPlaylistDetailBinding binding;
    List<Podcast> listChaper = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.hide();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_playlist_detail);
        getChap();
        ktSukien();
    }
    private void getChap(){
        Intent i = getIntent();
        String playlistId = i.getStringExtra("playlistId")!=null ?
                i.getStringExtra("playlistId") : "-1";
        listChaper.clear();
        if (!playlistId.equals("-1")){
            Playlist playlist = BackgroundLoadDataService.getPlaylistById(playlistId);
            assert playlist != null;
            binding.tvName.setText(playlist.getName());
            binding.tvAuthor.setText(String.format("Tác giả: %s", playlist.getAuthor()));
            binding.tvInf.setText(playlist.getDetail());
            binding.ivPlaylist.setImageBitmap(BackgroundLoadDataService.getBitmapById(playlistId, Constant.PLAYLIST));
            for (Podcast podcast : BackgroundLoadDataService.getAllPodcast()){
                if (podcast.getPlaylistId().equals(playlistId)){
                    listChaper.add(podcast);
                }
            }
        }
    }
    private void ktSukien(){
        binding.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.btListenChap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlaylistDetailActivity.this, PlayerActivity.class);
                i.putExtra("podcastId", listChaper.get(0).get_id());
                i.putExtra("playlistId", getIntent().getStringExtra("playlistId"));
                i.putExtra("index", 0);
                startActivity(i);
            }
        });
        binding.lvPodcast.setAdapter(new PlaylistDetailAdapter(PlaylistDetailActivity.this, listChaper, new RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position) {
                Intent i = new Intent(PlaylistDetailActivity.this, PlayerActivity.class);
                i.putExtra("podcastId", listChaper.get(position).get_id());
                i.putExtra("playlistId", getIntent().getStringExtra("playlistId"));
                i.putExtra("index", position);
                startActivity(i);
            }
        }));
        binding.lvPodcast.setLayoutManager(new LinearLayoutManager(this));
    }
}