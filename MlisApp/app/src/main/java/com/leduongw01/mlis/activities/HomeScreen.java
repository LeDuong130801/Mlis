package com.leduongw01.mlis.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.adapter.AllPlaylistAdapter;
import com.leduongw01.mlis.adapter.PodcastHAdapter;
import com.leduongw01.mlis.databinding.ActivityHomeScreenBinding;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.services.ForegroundAudioService;
import com.leduongw01.mlis.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreen extends AppCompatActivity {

    ActivityHomeScreenBinding binding;
    String currentPodcastName = "";
    boolean hide = true;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen);
        Intent i = new Intent(HomeScreen.this, BackgroundLoadDataService.class);
        startService(i);
        ktDrawer();
        ktHandler();
        ktSuKien();
        capNhatRecycleView();
    }

    private void capNhatRecycleView() {
        binding.rcvMostPopular.setAdapter(new AllPlaylistAdapter(getApplicationContext(), (v, position) -> {
            Intent intent = new Intent(this, PlaylistDetailActivity.class);
            intent.putExtra("playlistId", BackgroundLoadDataService.getAllPlaylist().get(position).get_id());
            startActivity(intent);

        }));
        binding.rcvMostPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        ApiService.apisService.getPodcastWithSl().enqueue(new Callback<ArrayList<Podcast>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Podcast>> call, Response<ArrayList<Podcast>> response) {
//                allPodcastList = response.body();
//                binding.rcvPodcast.setAdapter(new AllPlaylistAdapter(getApplicationContext(), BackgroundLoadDataService.getAllPlaylist(), ((v, position) -> {
//                    Intent playerIntent = new Intent(HomeScreen.this, PlaylistDetailActivity.class);
//                    playerIntent.putExtra("playlistId", BackgroundLoadDataService.getAllPlaylist().get(position).get_id());
//                    if (ForegroundAudioService.getMediaPlayer().isPlaying()) {
//                        ForegroundAudioService.getInstance().stopMediaPlayer();
//                    }
//                    startActivity(playerIntent);
//                })));
//                binding.rcvPodcast.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Podcast>> call, Throwable t) {
//                Log.d("eq", "onFailure: ");
//            }
//        });
    }

    private void ktDrawer() {
        binding.drawerHomeScreen.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        View headerView = binding.navHomeScreen.getHeaderView(0);
        headerView.findViewById(R.id.goLogin).setOnClickListener(view -> {
            Intent intent = new Intent(HomeScreen.this, LoginActivity.class);
            startActivity(intent);
        });
        Menu menu = binding.navHomeScreen.getMenu();
    }

    void ktHandler() {
        if (ForegroundAudioService.getCurrentPodcast() != null) {
            currentPodcastName = ForegroundAudioService.getCurrentPodcast().getName();
        } else {
            currentPodcastName = "";
        }
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPodcastName.equals("") && !hide) {
                    hide = true;
                    binding.llplaying.setVisibility(View.GONE);
                } else if (!currentPodcastName.equals("") && hide) {
                    hide = false;
                    binding.llplaying.setVisibility(View.VISIBLE);
                    binding.tvTenTruyen.setText(ForegroundAudioService.getCurrentPodcast().getName());
                    binding.imageAudio.setImageBitmap(BackgroundLoadDataService.getBitmapById(ForegroundAudioService.getCurrentPodcast().get_id(), Constant.PODCAST));
                }
//                else if (!currentPodcastName.equals("")) {
//                    if (ForegroundAudioService.getInstance().) {
//                        binding.icPauseResume.setImageResource(R.drawable.baseline_pause_24);
//                    } else {
//                        binding.icPauseResume.setImageResource(R.drawable.baseline_play_arrow_24);
//                    }
//                }
                handler.postDelayed(runnable, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    void ktSuKien() {
        binding.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForegroundAudioService.getInstance().forwardprevious10s();
            }
        });
        binding.icPauseResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForegroundAudioService.pauseOrResumeMediaPlayer();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (binding.drawerHomeScreen.isDrawerOpen(GravityCompat.START)) {
                binding.drawerHomeScreen.closeDrawers();
            } else {
                binding.drawerHomeScreen.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_header, menu);
        menu.findItem(R.id.searchbar_navigation).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                return true;
            }
        });
        SearchView searchView = (SearchView) menu.findItem(R.id.searchbar_navigation).getActionView();
        searchView.setQueryHint("Nhập kênh cần tìm....");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
}