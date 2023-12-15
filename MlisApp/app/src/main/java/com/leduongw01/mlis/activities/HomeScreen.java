package com.leduongw01.mlis.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leduongw01.mlis.MainActivity;
import com.leduongw01.mlis.R;
import com.leduongw01.mlis.adapter.AllPlaylistAdapter;
import com.leduongw01.mlis.adapter.PlaylistAdapter;
import com.leduongw01.mlis.adapter.PodcastRecentListenedAdapter;
import com.leduongw01.mlis.databasehelper.MlisSqliteDBHelper;
import com.leduongw01.mlis.databinding.ActivityHomeScreenBinding;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.LocalRecentPodcast;
import com.leduongw01.mlis.models.Playlist;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.services.ForegroundAudioService;
import com.leduongw01.mlis.utils.MyConfig;
import com.leduongw01.mlis.utils.Constant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    ActivityHomeScreenBinding binding;
    List<LocalRecentPodcast> top3Recent;
    List<Playlist> listMa;
    List<Playlist> listKiemHiep;
    List<Playlist> listMoiCapNhat;
    List<Podcast> p;
    String currentPodcastName = "";
    boolean hide = true;
    Handler handler;
    Runnable runnable;
    boolean pause = false;

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
        splitData();
        binding.rcvKhamPha.setAdapter(new AllPlaylistAdapter(getApplicationContext(), (v, position) -> {
            Intent intent = new Intent(this, PlaylistDetailActivity.class);
            intent.putExtra("playlistId", BackgroundLoadDataService.getAllPlaylist().get(position).get_id());
            startActivity(intent);

        }));
        binding.rcvKhamPha.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        p = getRecentListenedPodcast();
        binding.rcvRecent.setAdapter(new PodcastRecentListenedAdapter(HomeScreen.this, p, new RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position) {
                Intent i = new Intent(HomeScreen.this, PlayerActivity.class);
                i.putExtra("podcastId", p.get(position).get_id());
                i.putExtra("playlistId", p.get(position).getPlaylistId());
                startActivity(i);
            }
        }));
        binding.rcvRecent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        binding.rcvCateKiemHiep.setAdapter(new PlaylistAdapter(getApplicationContext(), listKiemHiep, (v, position) -> {
            Intent intent = new Intent(this, PlaylistDetailActivity.class);
            intent.putExtra("playlistId", listKiemHiep.get(position).get_id());
            startActivity(intent);
        }));
        binding.rcvCateKiemHiep.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        binding.rcvCateMa.setAdapter(new PlaylistAdapter(getApplicationContext(),listMa, (v, position) -> {
            Intent intent = new Intent(this, PlaylistDetailActivity.class);
            intent.putExtra("playlistId", listMa.get(position).get_id());
            startActivity(intent);
        }));
        binding.rcvCateMa.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        binding.rcvMoiCapNhat.setAdapter(new PlaylistAdapter(getApplicationContext(),listMa, (v, position) -> {
            Intent intent = new Intent(this, PlaylistDetailActivity.class);
            intent.putExtra("playlistId", listMa.get(position).get_id());
            startActivity(intent);
        }));
        binding.rcvMoiCapNhat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }
    private void splitData(){
        List<Playlist> playlistList = BackgroundLoadDataService.getAllPlaylist();
        listMa = new ArrayList<>();
        listKiemHiep = new ArrayList<>();
        listMoiCapNhat = new ArrayList<>();
        Date date = new Date();
        long t = date.getTime();
        for (Playlist playlist: playlistList){
            if (playlist.isMa()){
                listMa.add(playlist);
            }
            if (playlist.isKiemHiep()){
                listKiemHiep.add(playlist);
            }
            if (Long.parseLong(playlist.getUpdateOn()) - t >Constant.oneday* MyConfig.DAYOFNEW){
                listMoiCapNhat.add(playlist);
            }
        }
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
        ktNavigator();
    }
    void ktNavigator(){
        View headerView = binding.navHomeScreen.getHeaderView(0);
        headerView.findViewById(R.id.goLogin).setOnClickListener(view -> {
            Intent intent = new Intent(HomeScreen.this, LoginActivity.class);
            startActivity(intent);
        });
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFERENCES_NAME, MODE_PRIVATE);

        String name = sharedPreferences.getString("username", "none").equals("none")?"Chưa đăng nhập": sharedPreferences.getString("username", "none");
        TextView tvUsername = headerView.findViewById(R.id.tvUsername);
        tvUsername.setText(name);
        if (BackgroundLoadDataService.getInstance().checkAuthen()){
            headerView.findViewById(R.id.goLogin).setVisibility(View.INVISIBLE);
        }
        else{
            headerView.findViewById(R.id.goLogin).setVisibility(View.VISIBLE);
        }
        Menu menu = binding.navHomeScreen.getMenu();
        menu.findItem(R.id.favoritelist).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(HomeScreen.this, FavoriteActivity.class);
                startActivity(intent);
                return false;
            }
        });
        menu.findItem(R.id.logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
                AlertDialog dialog = builder.setMessage("Bạn có muốn đăng xuất?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFERENCES_NAME, MODE_PRIVATE);
                                sharedPreferences.edit().putString("username", "none").apply();
                                sharedPreferences.edit().putString("token", "none").apply();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("EXIT", true);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                builder.show();
                return false;
            }
        });
        menu.findItem(R.id.exit).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                return false;
            }
        });
    }
    void ktHandler() {
        pause = false;
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
                if (ForegroundAudioService.getMediaPlayer().isPlaying()){
                    binding.icPauseResume.setImageResource(R.drawable.baseline_pause_24);
                }
                else{
                    binding.icPauseResume.setImageResource(R.drawable.baseline_play_arrow_24);
                }
                handler.postDelayed(this, 1000);
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
    List<Podcast> getRecentListenedPodcast(){
        top3Recent = new MlisSqliteDBHelper(getApplicationContext()).get3IdRecent();
        List<Podcast> output = new ArrayList<>();
        for (LocalRecentPodcast localRecentPodcast:top3Recent){
            Podcast tmp = BackgroundLoadDataService.getPodcastById(localRecentPodcast.id);
            if (tmp != null){
                if (!output.contains(tmp))
                output.add(tmp);
            }
        }
        return output;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause = true;
        runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        handler = new Handler();
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