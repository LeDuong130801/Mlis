package com.leduongw01.mlis.activities;

import androidx.annotation.NonNull;
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
import com.leduongw01.mlis.models.Favorite;
import com.leduongw01.mlis.models.Playlist;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.utils.Constant;
import com.leduongw01.mlis.utils.MyComponent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        ktRecycle();
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
        binding.ivPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyComponent.ToastShort(PlaylistDetailActivity.this, "s");
            }
        });
        if (listChaper.size()==0){
            binding.btListenChap1.setText("Tập truyện này chưa có chương");
            binding.btListenChap1.setClickable(false);
        }
        else
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
    }
    void ktRecycle(){

        binding.lvPodcast.setAdapter(new PlaylistDetailAdapter(PlaylistDetailActivity.this, listChaper, new RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position) {
                //itemview
                Intent i = new Intent(PlaylistDetailActivity.this, PlayerActivity.class);
                i.putExtra("podcastId", listChaper.get(position).get_id());
                i.putExtra("playlistId", getIntent().getStringExtra("playlistId"));
                i.putExtra("index", position);
                startActivity(i);
            }
        },
                new RecyclerViewClickListener() {
                    @Override
                    public void recyclerViewListClicked(View v, int position) {
                        //favorite
                        ArrayList<String> tmp = new ArrayList<String>();
                        tmp.add(listChaper.get(position).get_id());
                        if (BackgroundLoadDataService.mainFavorite.getPodListId().contains(listChaper.get(position).get_id())){
                            ApiService.apisService.removePodcastToMainFavorite(BackgroundLoadDataService.mlisUser.get_id(), tmp).enqueue(new Callback<Favorite>() {
                                @Override
                                public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                                    if (response.isSuccessful()){
                                        BackgroundLoadDataService.getInstance().setMainFavorite(response.body());
                                        ktRecycle();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Favorite> call, Throwable t) {

                                }
                            });
                        }
                        else
                        ApiService.apisService.addPodcastToMainFavorite(BackgroundLoadDataService.mlisUser.get_id(), tmp).enqueue(new Callback<Favorite>() {
                            @Override
                            public void onResponse(Call<Favorite> call, @NonNull Response<Favorite> response) {
                                if (response.isSuccessful()){
                                    BackgroundLoadDataService.mainFavorite = response.body();
                                    ktRecycle();
                                }
                            }

                            @Override
                            public void onFailure(Call<Favorite> call, Throwable t) {

                            }
                        });
                    }
                }));
        binding.lvPodcast.setLayoutManager(new LinearLayoutManager(this));
    }
}