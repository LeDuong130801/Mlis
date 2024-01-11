package com.leduongw01.mlis.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.leduongw01.mlis.adapter.CateKiemHiepPlaylistAdapter;
import com.leduongw01.mlis.adapter.CateMaPlaylistAdapter;
import com.leduongw01.mlis.adapter.MoiCapNhatPlaylistAdapter;
import com.leduongw01.mlis.adapter.PodcastRecentListenedAdapter;
import com.leduongw01.mlis.databasehelper.MlisSqliteDBHelper;
import com.leduongw01.mlis.databinding.ActivityHomeScreenBinding;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.LocalRecentPodcast;
import com.leduongw01.mlis.models.Playlist;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.services.ForegroundAudioService;
import com.leduongw01.mlis.utils.MyConfig;
import com.leduongw01.mlis.utils.Constant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreen extends AppCompatActivity {

    ActivityHomeScreenBinding binding;
    List<LocalRecentPodcast> top3Recent;
    List<Playlist> listMa;
    List<Playlist> listKiemHiep;
    List<Playlist> listMoiCapNhat;
    String currentPodcastName = "";
    boolean hide = true;
    Handler handler;
    Runnable runnable;
    Handler handlerView;
    Runnable runnableView;
    boolean pause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen);
//        Intent i = new Intent(HomeScreen.this, BackgroundLoadDataService.class);
//        startService(i);
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

        binding.rcvCateKiemHiep.setAdapter(new CateKiemHiepPlaylistAdapter(getApplicationContext(), listKiemHiep, (v, position) -> {
            Intent intent = new Intent(this, PlaylistDetailActivity.class);
            intent.putExtra("playlistId", listKiemHiep.get(position).get_id());
            startActivity(intent);
        }));
        binding.rcvCateKiemHiep.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        binding.rcvCateMa.setAdapter(new CateMaPlaylistAdapter(getApplicationContext(),listMa, (v, position) -> {
            Intent intent = new Intent(this, PlaylistDetailActivity.class);
            intent.putExtra("playlistId", listMa.get(position).get_id());
            startActivity(intent);
        }));
        binding.rcvCateMa.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        binding.rcvMoiCapNhat.setAdapter(new MoiCapNhatPlaylistAdapter(getApplicationContext(),listMoiCapNhat, (v, position) -> {
            Intent intent = new Intent(this, PlaylistDetailActivity.class);
            intent.putExtra("playlistId", listMoiCapNhat.get(position).get_id());
            startActivity(intent);
        }));
        binding.rcvMoiCapNhat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.tvMoiCapNhat.setVisibility(listMoiCapNhat.isEmpty()? View.GONE : View.VISIBLE);
        binding.tvCateKiemHiep.setVisibility(listKiemHiep.isEmpty()? View.GONE : View.VISIBLE);
        binding.tvCateMa.setVisibility(listMa.isEmpty()? View.GONE : View.VISIBLE);
        if (BackgroundLoadDataService.keyword==null){
            BackgroundLoadDataService.keyword="";
        }
        if (!top3Recent.isEmpty() && BackgroundLoadDataService.keyword.equals("")){
            binding.tvRecent.setVisibility(View.VISIBLE);
            binding.rcvRecent.setAdapter(new PodcastRecentListenedAdapter(HomeScreen.this, top3Recent, new RecyclerViewClickListener() {
                @Override
                public void recyclerViewListClicked(View v, int position) {
                    Intent i = new Intent(HomeScreen.this, PlayerActivity.class);
                    i.putExtra("podcastId", top3Recent.get(position).id);
                    i.putExtra("playlistId", top3Recent.get(position).playlistId);
                    i.putExtra("favoriteId", top3Recent.get(position).favoriteId);
                    i.putExtra("continue", false);
                    startActivity(i);
                }
            }));
            binding.rcvRecent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
        else{
            binding.tvRecent.setVisibility(View.GONE);
            binding.rcvRecent.setAdapter(null);
        }
        if (BackgroundLoadDataService.keyword.equals("")){
            binding.tvKqTimKiem.setVisibility(View.GONE);
            binding.tvReload.setVisibility(View.GONE);
        }
        else{
            binding.tvKqTimKiem.setText(String.format("Kết quả tìm kiếm cho: \" %s\"", BackgroundLoadDataService.keyword));
            binding.tvKqTimKiem.setVisibility(View.VISIBLE);
            binding.tvReload.setVisibility(View.VISIBLE);
        }
    }
    private void splitData(){
        getRecentListenedPodcast();
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
            if ((t-Long.parseLong(playlist.getUpdateOn())) < (Constant.oneday* MyConfig.DAYOFNEW)){
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
        Menu menu = binding.navHomeScreen.getMenu();
        if(BackgroundLoadDataService.getInstance().checkAuthen()){
            TextView tvUsername = headerView.findViewById(R.id.tvUsername);
            tvUsername.setText(BackgroundLoadDataService.mlisUser.getUsername());
            menu.findItem(R.id.itlogin).setVisible(false);
            menu.findItem(R.id.myaccount).setVisible(true);
            menu.findItem(R.id.logout).setVisible(true);
            menu.findItem(R.id.myfavoritelist).setVisible(true);
        }
        else{
            TextView tvUsername = headerView.findViewById(R.id.tvUsername);
            tvUsername.setText("Người dùng khách");
            menu.findItem(R.id.itlogin).setVisible(true);
            menu.findItem(R.id.myaccount).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.myfavoritelist).setVisible(false);
        }

        menu.findItem(R.id.myfavoritelist).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(HomeScreen.this, FavoriteActivity.class);
                startActivity(intent);
                return false;
            }
        });
        menu.findItem(R.id.itlogin).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(HomeScreen.this, LoginActivity.class);
                startActivity(i);
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
        menu.findItem(R.id.itSetting).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                IpSettingDialog ipSettingDialog = new IpSettingDialog(HomeScreen.this);
                ipSettingDialog.show();
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
        handlerView = new Handler();
        runnableView = new Runnable() {
            @Override
            public void run() {
                capNhatRecycleView();
            }
        };
        handlerView.postDelayed(runnableView, 3000);
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
        binding.tvReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundLoadDataService.keyword = "";
                reload();
            }
        });
    }
    void getRecentListenedPodcast(){
        top3Recent = new ArrayList<>();
        if (BackgroundLoadDataService.getInstance().checkAuthen()){
            top3Recent = new MlisSqliteDBHelper(getApplicationContext()).get3IdRecent(BackgroundLoadDataService.mlisUser.get_id());
        }
        else {
            top3Recent = new MlisSqliteDBHelper(getApplicationContext()).get3IdRecent();
        }
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
        searchView.setQueryHint("Nhập tên truyện, tên tác giả hoặc thể loại cần tìm....");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                BackgroundLoadDataService.getInstance().searchTask(query);
                reload();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ktDrawer();
        ktSuKien();
        ktHandler();
        capNhatRecycleView();
        if (binding.drawerHomeScreen.isDrawerOpen(GravityCompat.START)) {
            binding.drawerHomeScreen.closeDrawers();
        }
    }
    void reload(){
        ApiService.apisService.getBySearch(BackgroundLoadDataService.keyword).enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    BackgroundLoadDataService.allPlaylist = (response.body());
                    ktDrawer();
                    ktSuKien();
                    ktHandler();
                    capNhatRecycleView();
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
            }
        });
    }
}