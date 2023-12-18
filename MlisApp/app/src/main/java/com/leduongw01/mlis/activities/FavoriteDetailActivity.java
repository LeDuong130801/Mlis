package com.leduongw01.mlis.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.adapter.FavoriteDetailAdapter;
import com.leduongw01.mlis.adapter.PodcastListAdapter;
import com.leduongw01.mlis.databinding.ActivityFavoriteDetailBinding;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.Favorite;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.services.BackgroundLoadDataService;

import java.util.List;
import java.util.Objects;

public class FavoriteDetailActivity extends AppCompatActivity {
    ActivityFavoriteDetailBinding binding;
    Favorite favorite = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite_detail);
        Objects.requireNonNull(getSupportActionBar()).hide();
        String favoriteId = getIntent().getStringExtra("favoriteId");
        if (!favoriteId.equals("")){
            favorite = BackgroundLoadDataService.getFavoriteById(favoriteId);
        }
        ktRecycle();
    }
    void ktRecycle(){
        if (favorite==null){
            binding.tvTrong.setVisibility(View.VISIBLE);
            return;
        }
        else if (favorite.getPodListId().size()!=0){
            binding.tvTrong.setVisibility(View.GONE);
        }
        binding.favoriteName.setText(favorite.getName());
        List<Podcast> podcastList = BackgroundLoadDataService.getPodcastListByFavorite(favorite);
        binding.rcvPodcastList.setAdapter(new FavoriteDetailAdapter(FavoriteDetailActivity.this, podcastList, new RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position) {
                Intent i = new Intent(FavoriteDetailActivity.this, PlayerActivity.class);
                i.putExtra("podcastId", podcastList.get(position).get_id());
                i.putExtra("favoriteId", favorite.get_id());
                i.putExtra("playlistId", "none");
                i.putExtra("index", position);
                startActivity(i);
            }
        }));
        binding.rcvPodcastList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}