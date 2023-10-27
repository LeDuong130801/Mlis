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
import android.os.Handler;
import android.util.JsonReader;
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

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Objects;

public class HomeScreen extends AppCompatActivity {

    ActivityHomeScreenBinding binding;
    ArrayList<Podcast> mostPopularPodcastList;
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
//            Intent serviceIntent = new Intent(this, ForegroundAudioService.class);
//            serviceIntent.putExtra("startNow", 1);
//            serviceIntent.putExtra("url", mostPopularPodcastList.get(position).getUrl());
//            podcastTemp = mostPopularPodcastList.get(position);
//            ContextCompat.startForegroundService(this, serviceIntent);
            Intent playerIntent = new Intent(this, PlayerActivity.class);
            playerIntent.putExtra("startNow", 1);
            podcastTemp = mostPopularPodcastList.get(position);
            if (ForegroundAudioService.getInstance().getMediaPlayer().isPlaying()) {
                ForegroundAudioService.getInstance().stopMediaPlayer();
            }
            startActivity(playerIntent);
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

    void ktHandler() {
        binding.llplaying.setVisibility(View.GONE);
        hide = true;
        currentPodcastName = "";
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!ForegroundAudioService.currentPodcast.getName().isEmpty()
                ) {
                    if (!Objects.equals(ForegroundAudioService.currentPodcast.getName(), currentPodcastName)) {
                        binding.tvTenTruyen.setText(ForegroundAudioService.currentPodcast.getName());
                        currentPodcastName = ForegroundAudioService.currentPodcast.getName();
                        hide = false;
                        binding.llplaying.setVisibility(View.VISIBLE);
                    }
                } else if (!hide) {
                    hide = true;
                    binding.llplaying.setVisibility(View.GONE);
                }
                if (ForegroundAudioService.getInstance().getPlaying()) {
                    binding.icPauseResume.setImageResource(R.drawable.baseline_pause_24);
                } else {
                    binding.icPauseResume.setImageResource(R.drawable.baseline_play_arrow_24);
                }
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
                        "Liễu thuật quân",
                        "Kỹ năng sống",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FS%C3%A1ch%20N%C3%B3i%20Tho%C3%A1t%20Kh%E1%BB%8Fi%20V%C3%B2ng%20B%E1%BA%ADn%20R%E1%BB%99n%20-%20Li%E1%BB%85u%20Thu%E1%BA%ADt%20Qu%C3%A2n%20-%20Gi%E1%BB%9Bi%20thi%E1%BB%87u.mp3?alt=media&token=b7066952-864d-49a7-9f16-b144a035edee",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fthoatkhoivongbanron.jpg?alt=media&token=e8a12b02-9ff1-4ae6-a161-57ff77cc1db7",
                        "1"
                ));
        mostPopularPodcastList.add(
                new Podcast(
                        "1",
                        "Thoát khỏi vòng lặp bận rộn - chương 2",
                        "Chương 2 của thoát khỏi vòng lặp bận rộn",
                        "1698395338868",
                        "localhost",
                        "Liễu Thuật Quân",
                        "Kỹ năng sống",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FS%C3%A1ch%20N%C3%B3i%20Tho%C3%A1t%20Kh%E1%BB%8Fi%20V%C3%B2ng%20B%E1%BA%ADn%20R%E1%BB%99n%20-%20Li%E1%BB%85u%20Thu%E1%BA%ADt%20Qu%C3%A2n%20-%20S%C3%A1ch%20N%C3%B3i%20Online_3.mp3?alt=media&token=143c3d2a-9578-4b74-9082-6f634d004d04",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fthoatkhoivongbanron.jpg?alt=media&token=e8a12b02-9ff1-4ae6-a161-57ff77cc1db7",
                        "1"));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Thoát khỏi vòng lặp bận rộn - chương 3",
                        "Chương 3 của thoát khỏi vòng lặp bận rộn",
                        "1698395359714",
                        "localhost",
                        "Liễu Thuật Quân",
                        "Kỹ năng sống",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FS%C3%A1ch%20N%C3%B3i%20Tho%C3%A1t%20Kh%E1%BB%8Fi%20V%C3%B2ng%20B%E1%BA%ADn%20R%E1%BB%99n%20-%20Li%E1%BB%85u%20Thu%E1%BA%ADt%20Qu%C3%A2n%20-%20S%C3%A1ch%20N%C3%B3i%20Online_4.mp3?alt=media&token=1b0fcc99-1017-4657-a0ef-b8b3aa375117",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fthoatkhoivongbanron.jpg?alt=media&token=e8a12b02-9ff1-4ae6-a161-57ff77cc1db7",
                        "1"));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Thoát khỏi vòng lặp bận rộn - chương 4",
                        "Chương 4 của thoát khỏi vòng lặp bận rộn",
                        "1698395373827",
                        "localhost",
                        "Liễu Thuật Quân",
                        "Kỹ năng sống",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FS%C3%A1ch%20N%C3%B3i%20Tho%C3%A1t%20Kh%E1%BB%8Fi%20V%C3%B2ng%20B%E1%BA%ADn%20R%E1%BB%99n%20-%20Li%E1%BB%85u%20Thu%E1%BA%ADt%20Qu%C3%A2n%20-%20S%C3%A1ch%20N%C3%B3i%20Online_5.mp3?alt=media&token=f8cb80e2-1307-4cff-8c97-d4824018a18e",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fthoatkhoivongbanron.jpg?alt=media&token=e8a12b02-9ff1-4ae6-a161-57ff77cc1db7",
                        "1"));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Thoát khỏi vòng lặp bận rộn - chương 5",
                        "Chương 5 của thoát khỏi vòng lặp bận rộn",
                        "1698395387636",
                        "localhost",
                        "Liễu Thuật Quân",
                        "Kỹ năng sống",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FS%C3%A1ch%20N%C3%B3i%20Tho%C3%A1t%20Kh%E1%BB%8Fi%20V%C3%B2ng%20B%E1%BA%ADn%20R%E1%BB%99n%20-%20Li%E1%BB%85u%20Thu%E1%BA%ADt%20Qu%C3%A2n%20-%20S%C3%A1ch%20N%C3%B3i%20Online_6.mp3?alt=media&token=bc8d9ffe-a58f-459c-9491-4b0ad791fa6a",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fthoatkhoivongbanron.jpg?alt=media&token=e8a12b02-9ff1-4ae6-a161-57ff77cc1db7",
                        "1"));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Cây cam ngọt của tôi",
                        "Hãy làm quen với Zezé, cậu bé tinh nghịch siêu hạng đồng thời cũng đáng yêu bậc nhất, với ước mơ lớn lên trở thành nhà thơ cổ thắt nơ bướm. Chẳng phải ai cũng công nhận khoản “đáng yêu” kia đâu nhé. Bởi vì, ở cái xóm ngoại ô nghèo ấy, nỗi khắc khổ bủa vây đã che mờ mắt người ta trước trái",
                        "1698395632207",
                        "localhost",
                        "José Mauro de Vasconcelos",
                        "Tiểu thuyết, Văn học nước ngoài",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fvov.jpg?alt=media&token=75f2b920-c604-4c3d-9061-fe4c64d99b96",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fcaycamngotcuatoi.jpg?alt=media&token=15f7801f-3669-4a20-89b9-a205723d58f6",
                        "1"));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Cây cam ngọt của tôi chương 2",
                        "chương 2 của tiểu thuyết cây cam ngọt của tôi",
                        "1698395704909",
                        "localhost",
                        "José Mauro de Vasconcelos",
                        "Tiểu thuyết, Văn học nước ngoài",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fvov.jpg?alt=media&token=75f2b920-c604-4c3d-9061-fe4c64d99b96",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fcaycamngotcuatoi.jpg?alt=media&token=15f7801f-3669-4a20-89b9-a205723d58f6",
                        "1"));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Cây cam ngọt của tôi chương 3",
                        "chương 3 của tiểu thuyết cây cam ngọt của tôi",
                        "1698395715689",
                        "localhost",
                        "José Mauro de Vasconcelos",
                        "Tiểu thuyết, Văn học nước ngoài",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fvov.jpg?alt=media&token=75f2b920-c604-4c3d-9061-fe4c64d99b96",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fcaycamngotcuatoi.jpg?alt=media&token=15f7801f-3669-4a20-89b9-a205723d58f6",
                        "1"));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Cây cam ngọt của tôi chương 4",
                        "chương 4 của tiểu thuyết cây cam ngọt của tôi",
                        "1698395731059",
                        "localhost",
                        "José Mauro de Vasconcelos",
                        "Tiểu thuyết, Văn học nước ngoài",
                        "storage\\file\\4265456842369597312Cây cam ngọt của tôi chương 4localhost1698395731059file.mp3",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fcaycamngotcuatoi.jpg?alt=media&token=15f7801f-3669-4a20-89b9-a205723d58f6",
                        "1"));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Cây cam ngọt của tôi chương 5",
                        "chương 5 của tiểu thuyết cây cam ngọt của tôi",
                        "1698395744151",
                        "localhost",
                        "José Mauro de Vasconcelos",
                        "Tiểu thuyết, Văn học nước ngoài",
                        "storage\\file\\3035802820651884376Cây cam ngọt của tôi chương 5localhost1698395744151file.mp3",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fcaycamngotcuatoi.jpg?alt=media&token=15f7801f-3669-4a20-89b9-a205723d58f6",
                        "1"));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Đọc truyện đêm khuya VOV 1",
                        "Nghe truyện cùng VOV",
                        "1698395885031",
                        "localhost",
                        "VOV",
                        "Truyện",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FAudio%20%C4%90%E1%BB%8Dc%20Truy%E1%BB%87n%20%C4%90%C3%AAm%20Khuya%20VOV%20Ch%E1%BB%8Dn%20L%E1%BB%8Dc%20-%20S%C3%A1ch%20N%C3%B3i%20Online.mp3?alt=media&token=f9f28d6d-1e20-4db5-ac20-8a8ad3377fa2",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fvov.jpg?alt=media&token=75f2b920-c604-4c3d-9061-fe4c64d99b96",
                        "1"));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Đọc truyện đêm khuya VOV 2",
                        "Nghe truyện cùng VOV",
                        "1698395897165",
                        "localhost",
                        "VOV",
                        "Truyện",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FAudio%20%C4%90%E1%BB%8Dc%20Truy%E1%BB%87n%20%C4%90%C3%AAm%20Khuya%20VOV%20Ch%E1%BB%8Dn%20L%E1%BB%8Dc%20-%20S%C3%A1ch%20N%C3%B3i%20Online_2.mp3?alt=media&token=9714b645-32af-4ee8-b0f4-781f47721675",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fvov.jpg?alt=media&token=75f2b920-c604-4c3d-9061-fe4c64d99b96",
                        "1"
                        ));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Đọc truyện đêm khuya VOV 3",
                        "Nghe truyện cùng VOV",
                        "1698395905429",
                        "localhost",
                        "VOV",
                        "Truyện",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FAudio%20%C4%90%E1%BB%8Dc%20Truy%E1%BB%87n%20%C4%90%C3%AAm%20Khuya%20VOV%20Ch%E1%BB%8Dn%20L%E1%BB%8Dc%20-%20S%C3%A1ch%20N%C3%B3i%20Online_3.mp3?alt=media&token=d5d254e6-aa00-4508-bf7b-a550f741e2ac",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fvov.jpg?alt=media&token=75f2b920-c604-4c3d-9061-fe4c64d99b96",
                        "1"
                        ));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Đọc truyện đêm khuya VOV 4",
                        "Nghe truyện cùng VOV",
                        "1698395914210",
                        "localhost",
                        "VOV",
                        "Truyện",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FAudio%20%C4%90%E1%BB%8Dc%20Truy%E1%BB%87n%20%C4%90%C3%AAm%20Khuya%20VOV%20Ch%E1%BB%8Dn%20L%E1%BB%8Dc%20-%20S%C3%A1ch%20N%C3%B3i%20Online_4.mp3?alt=media&token=f0d71783-12f0-4f47-8de2-efbf5e2a7b3a",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fvov.jpg?alt=media&token=75f2b920-c604-4c3d-9061-fe4c64d99b96",
                        "1"
                        ));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Địa mộ phần 1",
                        "Không có thông tin",
                        "1698397502581",
                        "localhost",
                        "Trường Lê",
                        "Truyện ma",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FTruy%E1%BB%87n%20N%C3%B3i%20%C4%90%E1%BB%8Ba%20M%E1%BB%99%20-%20Tr%C6%B0%E1%BB%9Dng%20L%C3%AA%20-%20S%C3%A1ch%20N%C3%B3i%20Online.mp3?alt=media&token=b1b9331b-e2a8-4821-9841-7a90559b5b57",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fdiamo.jpg?alt=media&token=956c1fcd-434c-439a-8c91-bc9b413dd351",
                        "1"
                        ));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Miếu hoang phần 1",
                        "Không có thông tin",
                        "1698397565904",
                        "localhost",
                        "Trường Lê",
                        "Truyện ma",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FTruy%E1%BB%87n%20N%C3%B3i%20Mi%E1%BA%BFu%20Hoang%20-%20Tr%C6%B0%E1%BB%9Dng%20L%C3%AA%20-%20S%C3%A1ch%20N%C3%B3i%20Online.mp3?alt=media&token=c00efdef-c48b-4b1b-b7b1-262624df3ce1",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fmieu-hoang.jpg?alt=media&token=ee55206c-375b-4414-9c67-2206d9b0c50f",
                        "1"
                        ));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Trong quan tài buồn 1",
                        "Không có thông tin",
                        "1698397666525",
                        "localhost",
                        "Nguyễn Ngọc Ngạn",
                        "Truyện ma",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FTruy%E1%BB%87n%20N%C3%B3i%20Trong%20Quan%20T%C3%A0i%20Bu%E1%BB%93n%20-%20Nguy%E1%BB%85n%20Ng%E1%BB%8Dc%20Ng%E1%BA%A1n%20-%20S%C3%A1ch%20N%C3%B3i%20Online.mp3?alt=media&token=5bc0d4c8-a84e-4016-ba9e-14840ca9bfb2",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2FTrong-quan-tai-buon.jpg?alt=media&token=c512ccc6-da34-4592-ab9d-84cbf8699c90",
                        "1"
                        ));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Anh hùng xạ điêu 1",
                        "Không có thông tin",
                        "1698397848021",
                        "localhost",
                        "Kim Dung",
                        "Truyện kiếm hiệp",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FTruy%E1%BB%87n%20N%C3%B3i%20Anh%20H%C3%B9ng%20X%E1%BA%A1%20%C4%90i%C3%AAu%20-%20Kim%20Dung%20-%20S%C3%A1ch%20N%C3%B3i%20Online.mp3?alt=media&token=1449238c-881b-480c-9105-62d5ac4dbdcc",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fanhhungxadieu.jpg?alt=media&token=d073b06b-5a42-4e5a-a3e3-db298036c197",
                        "1"
                        ));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Mắt biếc 1",
                        "Không có thông tin",
                        "1698398057936",
                        "localhost",
                        "Nguyễn Nhật Ánh",
                        "trống",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FTruy%E1%BB%87n%20N%C3%B3i%20M%E1%BA%AFt%20Bi%E1%BA%BFc%20-%20Nguy%E1%BB%85n%20Nh%E1%BA%ADt%20%C3%81nh%20-%20S%C3%A1ch%20N%C3%B3i%20Online.mp3?alt=media&token=74031e6e-21bd-4e5f-8e79-7347a5470535",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fmatbiec.webp?alt=media&token=009573cb-175c-4956-8d5b-550b481df456",
                        "1"
                        ));
        mostPopularPodcastList.add(
                new Podcast(
                        "1","Thoát khỏi vòng lặp bận rộn - chương 1",
                        "Chương 1 của thoát khỏi vòng lặp bận rộn",
                        "1698395315766",
                        "localhost",
                        "Liễu Thuật Quân",
                        "Kỹ năng sống",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FS%C3%A1ch%20N%C3%B3i%20Tho%C3%A1t%20Kh%E1%BB%8Fi%20V%C3%B2ng%20B%E1%BA%ADn%20R%E1%BB%99n%20-%20Li%E1%BB%85u%20Thu%E1%BA%ADt%20Qu%C3%A2n%20-%20S%C3%A1ch%20N%C3%B3i%20Online_2.mp3?alt=media&token=924d693b-dad5-4fa9-8fa5-7793b5d67413",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fthoatkhoivongbanron.jpg?alt=media&token=e8a12b02-9ff1-4ae6-a161-57ff77cc1db7",
                        "1"
                        ));
    }
}