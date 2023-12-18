package com.leduongw01.mlis.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.adapter.AoRtoPlaylistAdapter;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.Favorite;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.services.BackgroundLoadDataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AoRtoPlaylistDialog extends Dialog {
    Context context;
    Podcast podcast;
    public AoRtoPlaylistDialog(@NonNull Context context, Podcast podcast) {
        super(context);
        this.context = context;
        this.podcast = podcast;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_favorite);
        ktSuKien();
    }
    private void ktSuKien(){
        RecyclerView recyclerView = findViewById(R.id.rcvFavoriteList);
        recyclerView.setAdapter(new AoRtoPlaylistAdapter(context, BackgroundLoadDataService.getAllFavorite(), podcast,
                new RecyclerViewClickListener() {
                    @Override
                    public void recyclerViewListClicked(View v, int position) {
                        BackgroundLoadDataService.getAllFavorite().get(position).getPodListId().add(podcast.get_id());
                        ApiService.apisService.addPodcastToFavorite(BackgroundLoadDataService.mlisUser.get_id(), podcast.get_id(), BackgroundLoadDataService.getAllFavorite().get(position)).enqueue(new Callback<Favorite>() {
                            @Override
                            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                                if (response.isSuccessful()){
                                    BackgroundLoadDataService.getInstance().loadFavorite();
                                    ktSuKien();
                                }
                            }

                            @Override
                            public void onFailure(Call<Favorite> call, Throwable t) {

                            }
                        });
                    }
                },
                new RecyclerViewClickListener() {
                    @Override
                    public void recyclerViewListClicked(View v, int position) {
                        BackgroundLoadDataService.getAllFavorite().get(position).getPodListId().remove(podcast.get_id());
                        ApiService.apisService.removePodcastToFavorite(BackgroundLoadDataService.mlisUser.get_id(), podcast.get_id(), BackgroundLoadDataService.getAllFavorite().get(position)).enqueue(new Callback<Favorite>() {
                            @Override
                            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                                if (response.isSuccessful()){
                                    BackgroundLoadDataService.getInstance().loadFavorite();
                                    ktSuKien();
                                }
                            }

                            @Override
                            public void onFailure(Call<Favorite> call, Throwable t) {

                            }
                        });
                    }
                }));
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }
}
