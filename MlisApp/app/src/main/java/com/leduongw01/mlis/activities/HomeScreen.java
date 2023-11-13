package com.leduongw01.mlis.activities;


import static com.leduongw01.mlis.services.ForegroundAudioService.currentPodcast;
import static com.leduongw01.mlis.services.ForegroundAudioService.podcastTemp;

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
import com.leduongw01.mlis.adapter.PodcastHAdapter;
import com.leduongw01.mlis.databinding.ActivityHomeScreenBinding;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.services.ForegroundAudioService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreen extends AppCompatActivity {

    ActivityHomeScreenBinding binding;
    List<Podcast> mostPopularPodcastList;
    List<Podcast> allPodcastList;
    String currentPodcastName = "";
    boolean hide = true;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen);
        ktDrawer();
        ktHandler();
        ktSuKien();
        FakeData();
        capNhatRecycleView();
    }

    private void capNhatRecycleView() {
        binding.rcvMostPopular.setAdapter(new PodcastHAdapter(getApplicationContext(), mostPopularPodcastList, (v, position) -> {
            Intent playerIntent = new Intent(this, PlayerActivity.class);
            playerIntent.putExtra("startNow", 1);
            podcastTemp = mostPopularPodcastList.get(position);
            if (ForegroundAudioService.getInstance().getMediaPlayer().isPlaying()) {
                ForegroundAudioService.getInstance().stopMediaPlayer();
            }
            startActivity(playerIntent);
        }));
        binding.rcvMostPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ApiService.apisService.getPodcastWithSl().enqueue(new Callback<ArrayList<Podcast>>() {
            @Override
            public void onResponse(Call<ArrayList<Podcast>> call, Response<ArrayList<Podcast>> response) {
                allPodcastList = response.body();
                binding.rcvPodcast.setAdapter(new PodcastHAdapter(getApplicationContext(), allPodcastList, ((v, position) -> {
                    Intent playerIntent = new Intent(HomeScreen.this, PlayerActivity.class);
                    playerIntent.putExtra("startNow", 1);
                    podcastTemp = mostPopularPodcastList.get(position);
                    if (ForegroundAudioService.getInstance().getMediaPlayer().isPlaying()) {
                        ForegroundAudioService.getInstance().stopMediaPlayer();
                    }
                    startActivity(playerIntent);
                })));
                binding.rcvPodcast.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            }

            @Override
            public void onFailure(Call<ArrayList<Podcast>> call, Throwable t) {
                Log.d("eq", "onFailure: ");
            }
        });
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
        binding.llplaying.setVisibility(View.GONE);
        hide = true;
        if (currentPodcast != null) {
            currentPodcastName = currentPodcast.getName();
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
                } else if (!currentPodcastName.equals("")) {
                    if (ForegroundAudioService.getInstance().getPlaying()) {
                        binding.icPauseResume.setImageResource(R.drawable.baseline_pause_24);
                    } else {
                        binding.icPauseResume.setImageResource(R.drawable.baseline_play_arrow_24);
                    }
                }
//                if (!(currentPodcastName.equals(""))) {
//                    if (!Objects.equals(ForegroundAudioService.currentPodcast.getName(), currentPodcastName)) {
//                        binding.tvTenTruyen.setText(ForegroundAudioService.currentPodcast.getName());
//                        currentPodcastName = ForegroundAudioService.currentPodcast.getName();
//                        hide = false;
//                        binding.llplaying.setVisibility(View.VISIBLE);
//                    }
//                } else if (!hide) {
//                    hide = true;
//                    binding.llplaying.setVisibility(View.GONE);
//                }
//                if (ForegroundAudioService.getInstance().getPlaying()) {
//                    binding.icPauseResume.setImageResource(R.drawable.baseline_pause_24);
//                } else {
//                    binding.icPauseResume.setImageResource(R.drawable.baseline_play_arrow_24);
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
                ForegroundAudioService.getInstance().pauseOrResumeMediaPlayer();
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

    private void FakeData() {
        mostPopularPodcastList = new ArrayList<>();
        mostPopularPodcastList.add(
                new Podcast(
                        "1",
                        "Thoát khỏi vòng bận rộn - Giới thiệu",
                        "Có phải bạn luôn cảm thấy bản thân cả ngày bận tối mắt tối mũi nhưng đến cuối cùng vẫn không đạt được thành tựu gì?&nbsp;Trong khi đó, lại có rất nhiều người thành công trong cuộc sống, nhìn họ lại chả có vẻ gì là rất bận rộn.&nbsp;Một người muốn công thành danh toại, thì cần phải sống có kế hoạch, nếu khôn",
                        "1698395117524",
                        "localhost",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FS%C3%A1ch%20N%C3%B3i%20Tho%C3%A1t%20Kh%E1%BB%8Fi%20V%C3%B2ng%20B%E1%BA%ADn%20R%E1%BB%99n%20-%20Li%E1%BB%85u%20Thu%E1%BA%ADt%20Qu%C3%A2n%20-%20Gi%E1%BB%9Bi%20thi%E1%BB%87u.mp3?alt=media&token=b7066952-864d-49a7-9f16-b144a035edee",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fthoatkhoivongbanron.jpg?alt=media&token=e8a12b02-9ff1-4ae6-a161-57ff77cc1db7",
                        "1",
                        "1"
                ));
        mostPopularPodcastList.add(
                new Podcast(
                        "3",
                        "Thoát khỏi vòng lặp bận rộn - chương 2",
                        "Chương 2 của thoát khỏi vòng lặp bận rộn",
                        "1698395338868",
                        "localhost",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FS%C3%A1ch%20N%C3%B3i%20Tho%C3%A1t%20Kh%E1%BB%8Fi%20V%C3%B2ng%20B%E1%BA%ADn%20R%E1%BB%99n%20-%20Li%E1%BB%85u%20Thu%E1%BA%ADt%20Qu%C3%A2n%20-%20S%C3%A1ch%20N%C3%B3i%20Online_3.mp3?alt=media&token=143c3d2a-9578-4b74-9082-6f634d004d04",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fthoatkhoivongbanron.jpg?alt=media&token=e8a12b02-9ff1-4ae6-a161-57ff77cc1db7",
                        "1",
                        "1"));
    }
}