package com.leduongw01.mlis.activities;


import static com.leduongw01.mlis.services.ForegroundAudioService.podcastTemp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.adapter.PodcastHAdapter;
import com.leduongw01.mlis.databinding.ActivityHomeScreenBinding;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.services.ForegroundAudioService;
import com.leduongw01.mlis.utils.Const;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    ActivityHomeScreenBinding binding;
    ArrayList<Podcast> mostPopularPodcastList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen);
        ktDrawer();
        FakeData();
        capNhatRecycleView();
    }

    private void capNhatRecycleView() {
        binding.rcvMostPopular.setAdapter(new PodcastHAdapter(getApplicationContext(), mostPopularPodcastList, (v, position) -> {
            Intent serviceIntent = new Intent(this, ForegroundAudioService.class);
            serviceIntent.putExtra("startNow", 1);
            serviceIntent.putExtra("url", mostPopularPodcastList.get(position).getUrl());
            podcastTemp = mostPopularPodcastList.get(position);
            ContextCompat.startForegroundService(this, serviceIntent);
        }));
        binding.rcvMostPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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
        mostPopularPodcastList.add(new Podcast("1", "M1", "soundhelix", Const.M1, "https://cdn0.fahasa.com/media/catalog/product/m/u/mua-he-khong-ten---bia-mem---qua-tang-kem-1.jpg", ""));
        mostPopularPodcastList.add(new Podcast("1", "M2", "soundhelix", Const.M2, "https://cdn0.fahasa.com/media/catalog/product/m/u/mua-he-khong-ten---bia-cung---qua-tang-kem-1.jpg", ""));
        mostPopularPodcastList.add(new Podcast("1", "M3", "soundhelix", Const.M3, "https://cdn0.fahasa.com/media/catalog/product/m/a/mashle_bia_tap-11-1.jpg", ""));
        mostPopularPodcastList.add(new Podcast("1", "M4", "soundhelix", Const.M4, "", ""));
        mostPopularPodcastList.add(new Podcast("1", "M5", "soundhelix", Const.M5, "https://cdn0.fahasa.com/media/catalog/product/k/a/kaguya-sama-cuoc-chien-to-t_nh_bia_tap-24-1.jpg", ""));
    }
}