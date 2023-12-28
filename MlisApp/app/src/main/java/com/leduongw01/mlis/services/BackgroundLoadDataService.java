package com.leduongw01.mlis.services;

import static com.leduongw01.mlis.MainActivity.noImg;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.leduongw01.mlis.MainActivity;
import com.leduongw01.mlis.R;
import com.leduongw01.mlis.models.Favorite;
import com.leduongw01.mlis.models.MapImage;
import com.leduongw01.mlis.models.MlisUser;
import com.leduongw01.mlis.models.Playlist;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.models.StringValue;
import com.leduongw01.mlis.utils.Constant;
import com.leduongw01.mlis.utils.MyComponent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundLoadDataService extends Service {
    static BackgroundLoadDataService instance = new BackgroundLoadDataService();
    public static MlisUser mlisUser;
    public static Favorite mainFavorite;
    public static List<Podcast> allPodcast;
    public static List<Playlist> allPlaylist;
    public static List<Favorite> allFavorite;
    public static List<MapImage> podcastBitmap;
    public static String keyword;
    public static BackgroundLoadDataService getInstance() {
        return instance;
    }
    //getter static
    public static List<Podcast> getAllPodcast() {
        return allPodcast;
    }
    public static List<Favorite> getAllFavorite() {
        return allFavorite;
    }
    public static List<Playlist> getAllPlaylist() {
        return allPlaylist;
    }
    public static List<MapImage> getPodcastBitmap() {
        return podcastBitmap;
    }

    public static List<Podcast> getPodcastInPlaylist(Playlist playlist){
        List<Podcast> t = new ArrayList<Podcast>();
        for (Podcast podcast : getAllPodcast()){
            if (podcast.getPlaylistId().equals(playlist.get_id())){
                t.add(podcast);
            }
        }
        return t;
    }
    public static List<Podcast> getPodcastInPlaylist(String playlistId){
        List<Podcast> t = new ArrayList<Podcast>();
        for (Podcast podcast : getAllPodcast()){
            if (podcast.getPlaylistId().equals(playlistId)){
                t.add(podcast);
            }
        }
        return t;
    }
    public synchronized static Integer getIndexOfPodcastInPlayList(String podcastId, String playlistId){
        List<Podcast> p = getPodcastInPlaylist(playlistId);
        for(int i=0;i<p.size();i++){
            if (p.get(i).get_id().equals(podcastId)){
                return i;
            }
        }
        return -1;
    }
    public static Integer getIndexOfPodcastInFavorite(String podcastId, String favoriteId){
        List<Podcast> p = getPodcastInFavorite(favoriteId);
        for(int i = 0; i< Objects.requireNonNull(p).size(); i++){
            if (p.get(i).get_id().equals(podcastId)){
                return i;
            }
        }
        return -1;
    }
    public static String getAuthorOfPodcast(Podcast podcast){
        Playlist playlist = getPlaylistById(podcast.getPlaylistId());
        if (playlist!= null){
            return playlist.getAuthor();
        }
        return "?";
    }
    public static List<Podcast> getPodcastInFavorite(String favoriteId){
        return null;
    }

    private BackgroundLoadDataService() {
        downloadTask();
    }
    public void searchTask(String word){
        keyword = word;
        downloadTask();
    }
    public void downloadTask() {
        fakeData();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                RealData();
//                for (Podcast podcast : getAllPodcast()){
//                    getPodcastBitmap().add(new MapImage(podcast.get_id()+ Constant.PODCAST, null));
//                    new DownloadTask(podcast.get_id()+Constant.PODCAST).execute(podcast.getUrlImg());
//                }
                handler.postDelayed(this, 30000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }
    public static Playlist getPlaylistById(String id){
        for (Playlist playlist: allPlaylist){
            if (playlist.get_id().equals(id)){
                return playlist;
            }
        }
        return null;
    }
    public static Podcast getPodcastById(String id){
        for (Podcast podcast: allPodcast){
            if (podcast.get_id().equals(id)){
                return podcast;
            }
        }
        return null;
    }
    public static Favorite getFavoriteById(String id){
        for (Favorite favorite: allFavorite){
            if (favorite.get_id().equals(id)){
                return favorite;
            }
        }
        return null;
    }
    public static List<Podcast> getPodcastListByFavorite(Favorite favorite){
        List<String> idList = favorite.getPodListId();
        List<Podcast> podcastList = new ArrayList<>();
        for (String id: idList){
            if (getPodcastById(id)!=null){
                podcastList.add(getPodcastById(id));
            }
        }
        return podcastList;
    }
    public static List<Podcast> getPodcastListByFavorite(String favoriteId){
        Favorite favorite = getFavoriteById(favoriteId);
        assert favorite != null;
        List<String> idList = favorite.getPodListId();
        List<Podcast> podcastList = new ArrayList<>();
        for (String id: idList){
            if (getPodcastById(id)!=null){
                podcastList.add(getPodcastById(id));
            }
        }
        return podcastList;
    }
    public static Bitmap getBitmapById(String id, String model){
        String att = id+model;
        for (int i = 0; i< getPodcastBitmap().size();i++){
            if (getPodcastBitmap().get(i).id.equals(att)){
                return getPodcastBitmap().get(i).bitmap;
            }
        }
        return noImg;
    }
    public MapImage getMapImageById(String id, String model) {
        for (int i = 0; i < getPodcastBitmap().size(); i++) {
            if (getPodcastBitmap().get(i).id.equals(id + model.toLowerCase())) {
                return getPodcastBitmap().get(i);
            }
        }
        return null;
    }
    public boolean checkAuthen(){
        if (mlisUser==null){
            return false;
        }
        else if (mlisUser.get_id()==null || mlisUser.get_id().equals("-1") || mlisUser.get_id().equals("none")){
            return false;
        }
        return true;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }
    public void fakeData() {
        if (getAllPodcast()== null){
            allPodcast = new ArrayList<>();
        }
        if(allPlaylist == null){
            allPlaylist = new ArrayList<>();
        }
        if(podcastBitmap == null){
            podcastBitmap = new ArrayList<>();
        }
        if(allFavorite == null){
            allFavorite = new ArrayList<>();
        }
//        allPodcast.add(
//                new Podcast(
//                        "3",
//                        "Thoát khỏi vòng lặp bận rộn - chương 2",
//                        "Chương 2 của thoát khỏi vòng lặp bận rộn",
//                        "1698395338868",
//                        "localhost",
//                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FS%C3%A1ch%20N%C3%B3i%20Tho%C3%A1t%20Kh%E1%BB%8Fi%20V%C3%B2ng%20B%E1%BA%ADn%20R%E1%BB%99n%20-%20Li%E1%BB%85u%20Thu%E1%BA%ADt%20Qu%C3%A2n%20-%20S%C3%A1ch%20N%C3%B3i%20Online_3.mp3?alt=media&token=143c3d2a-9578-4b74-9082-6f634d004d04",
//                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fthoatkhoivongbanron.jpg?alt=media&token=21321d59-3901-4208-b1d0-197f7ce58805",
//                        "1",
//                        "1"));
//        allPodcast.add(
//                new Podcast(
//                        "1",
//                        "Thoát khỏi vòng bận rộn - Giới thiệu",
//                        "Có phải bạn luôn cảm thấy bản thân cả ngày bận tối mắt tối mũi nhưng đến cuối cùng vẫn không đạt được thành tựu gì?&nbsp;Trong khi đó, lại có rất nhiều người thành công trong cuộc sống, nhìn họ lại chả có vẻ gì là rất bận rộn.&nbsp;Một người muốn công thành danh toại, thì cần phải sống có kế hoạch, nếu khôn",
//                        "1698395117524",
//                        "localhost",
//                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FS%C3%A1ch%20N%C3%B3i%20Tho%C3%A1t%20Kh%E1%BB%8Fi%20V%C3%B2ng%20B%E1%BA%ADn%20R%E1%BB%99n%20-%20Li%E1%BB%85u%20Thu%E1%BA%ADt%20Qu%C3%A2n%20-%20Gi%E1%BB%9Bi%20thi%E1%BB%87u.mp3?alt=media&token=b7066952-864d-49a7-9f16-b144a035edee",
//                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fthoatkhoivongbanron.jpg?alt=media&token=21321d59-3901-4208-b1d0-197f7ce58805",
//                        "1",
//                        "1"
//                ));
//        allPodcast.add(
//                new Podcast(
//                        "7",
//                        "Test",
//                        "Bruh",
//                        "1698395117524",
//                        "localhost",
//                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FS%C3%A1ch%20N%C3%B3i%20Tho%C3%A1t%20Kh%E1%BB%8Fi%20V%C3%B2ng%20B%E1%BA%ADn%20R%E1%BB%99n%20-%20Li%E1%BB%85u%20Thu%E1%BA%ADt%20Qu%C3%A2n%20-%20Gi%E1%BB%9Bi%20thi%E1%BB%87u.mp3?alt=media&token=b7066952-864d-49a7-9f16-b144a035edee",
//                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fthoatkhoivongbanron.jpg?alt=media&token=21321d59-3901-4208-b1d0-197f7ce58805",
//                        "6",
//                        "1"
//                ));
//        allPlaylist.add(new Playlist(
//                "1",
//                "Thoát khỏi vòng lặp bận rộn",
//                "Liễu Thuật Quân",
//                "Đời sống",
//                "0",
//                "1225656",
//                "Mô tả à",
//                "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fthoatkhoivongbanron.jpg?alt=media&token=e8a12b02-9ff1-4ae6-a161-57ff77cc1db7",
//                "1"
//        ));
//        allPlaylist.add(new Playlist(
//                "6",
//                "Test 1 podcast",
//                "local",
//                "Đời sống, truyền thông",
//                "999454",
//                "45545454",
//                "Mô tả à",
//                "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fvov.jpg?alt=media&token=75f2b920-c604-4c3d-9061-fe4c64d99b96",
//                "1"
//        ));

    }
    public synchronized void RealData(){
//        for (Playlist playlist : getAllPlaylist()){
//            getPodcastBitmap().add(new MapImage(playlist.get_id()+Constant.PLAYLIST, null));
//            new DownloadTask(playlist.get_id()+Constant.PLAYLIST).execute(playlist.getUrlImg());
//        }
//        for (Podcast podcast : getAllPodcast()){
//            getPodcastBitmap().add(new MapImage(podcast.get_id()+Constant.PODCAST, null));
//            new DownloadTask(podcast.get_id()+Constant.PODCAST).execute(podcast.getUrlImg());
//        }
        if (keyword==null){
            keyword = "";
        }
        ApiService.apisService.getBySearch(keyword).enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    allPlaylist = (response.body());
                    for (int i=0;i<allPlaylist.size();i++) {
                        if (!existPodcastBitmapById(allPlaylist.get(i).get_id() + Constant.PLAYLIST)) {
                            getPodcastBitmap().add(new MapImage(allPlaylist.get(i).get_id() + Constant.PLAYLIST, null));
                            new DownloadTask(allPlaylist.get(i).get_id() + Constant.PLAYLIST).execute(allPlaylist.get(i).getUrlImg());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
            }
        });
        ApiService.apisService.getAllPodcast().enqueue(new Callback<ArrayList<Podcast>>() {
            @Override
            public void onResponse(Call<ArrayList<Podcast>> call, Response<ArrayList<Podcast>> response) {
                if (response.isSuccessful()){
                    allPodcast = (response.body());
                    for (int i=0;i<allPodcast.size();i++){
                        if (!existPodcastBitmapById(allPodcast.get(i).get_id()+Constant.PODCAST)){
                            getPodcastBitmap().add(new MapImage(allPodcast.get(i).get_id()+Constant.PODCAST, null));
                            new DownloadTask(allPodcast.get(i).get_id()+Constant.PODCAST).execute(allPodcast.get(i).getUrlImg());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Podcast>> call, Throwable t) {

            }
        });

    }
    public boolean existPodcastBitmapById(String id){
        for (MapImage mapImage: podcastBitmap){
            if (mapImage.id.equals(id)){
                return true;
            }
        }
        return false;
    }
    public void loadFavorite(){
        if (!mlisUser.get_id().equals("-1"))
            ApiService.apisService.getAllFavoriteByUserId(mlisUser.get_id()).enqueue(new Callback<List<Favorite>>() {
                @Override
                public void onResponse(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                    if (response.isSuccessful()){
                        assert response.body() != null;
                        allFavorite.clear();
                        allFavorite.addAll(response.body());
                        for (Favorite favorite : response.body()){
                            if (favorite.get_id().equals(mlisUser.get_id())){
                                mainFavorite = favorite;
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Favorite>> call, Throwable t) {
                }
            });
    }
    public void loadMainFavorite(){
        ApiService.apisService.getAllFavoriteByUserId(mlisUser.get_id()).enqueue(new Callback<List<Favorite>>() {
            @Override
            public void onResponse(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                for (Favorite favorite : response.body()){
                    if (favorite.get_id().equals(mlisUser.get_id())){
                        mainFavorite = favorite;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Favorite>> call, Throwable t) {
            }
        });
    }
    public void setMainFavorite(Favorite favorite){
        for (int i=0;i<allFavorite.size();i++){
            if (allFavorite.get(i).get_id().equals(mlisUser.get_id())){
                allFavorite.set(i, favorite);
            }
        }
        mainFavorite = favorite;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class DownloadTask extends AsyncTask<String, Void, MapImage> {
        String id;
        public DownloadTask(String id) {
            this.id = id;
        }

        protected MapImage doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap decoded;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                decoded = BitmapFactory.decodeStream(in);
                decoded.compress(Bitmap.CompressFormat.PNG, 50, out);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            } catch (Exception e) {
                decoded = noImg;
                Log.e("error background service", e.getMessage());
                e.printStackTrace();
                MapImage mapImage = new MapImage();
                mapImage.id = id;
                mapImage.bitmap = decoded;
                for (int i=0;i<getPodcastBitmap().size();i++){
                    if(getPodcastBitmap().get(i).id.equals(id)){
                        getPodcastBitmap().get(i).bitmap = decoded;
                        getPodcastBitmap().get(i).hasRes = true;
                        return mapImage;
                    }
                }
                return mapImage;
            }
            MapImage mapImage = new MapImage();
            mapImage.id = id;
            mapImage.bitmap = decoded;
            for (int i=0;i<getPodcastBitmap().size();i++){
                if(getPodcastBitmap().get(i).id.equals(id)){
                    getPodcastBitmap().get(i).bitmap = decoded;
                    getPodcastBitmap().get(i).hasRes = true;
                    return mapImage;
                }
            }
            return mapImage;
        }

        @Override
        protected void onPostExecute(MapImage mapImage) {
            super.onPostExecute(mapImage);
        }
    }

}
