package com.leduongw01.mlis.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.databinding.ActivityPlaylistDetailBinding;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.services.BackgroundLoadDataService;

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

            }
        });
        ListAdapter adapter = new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEnabled(int i) {
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public int getCount() {
                return listChaper.size();
            }

            @Override
            public Object getItem(int i) {
                return listChaper.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null){
                    view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_chapter, viewGroup, false);
                }
                TextView textView = view.findViewById(R.id.chapName);
                textView.setText(listChaper.get(i).getName());
                TextView timeView = view.findViewById(R.id.lastUpdate);
                timeView.setText(listChaper.get(i).getUpdateOn());
                return view;
            }

            @Override
            public int getItemViewType(int i) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };
        binding.lvPodcast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PlaylistDetailActivity.this, PlayerActivity.class);
                intent.putExtra("podcastId", listChaper.get(i).get_id());
                startActivity(intent);
            }
        });
        binding.lvPodcast.setAdapter(adapter);
    }
}