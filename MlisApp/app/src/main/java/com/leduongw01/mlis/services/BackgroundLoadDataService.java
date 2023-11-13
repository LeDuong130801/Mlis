package com.leduongw01.mlis.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.models.MapImage;
import com.leduongw01.mlis.models.Playlist;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.models.StringValue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BackgroundLoadDataService extends Service {
    static List<Podcast> allPodcast = new ArrayList<Podcast>();
    static List<Playlist> allPlaylist = new ArrayList<Playlist>();
    static List<MapImage> podcastBitmap = new ArrayList<MapImage>();
    static BackgroundLoadDataService instance = new BackgroundLoadDataService();
    private static final String PODCAST = "podcast";
    private static final String PLAYLIST = "playlist";

    private BackgroundLoadDataService() {

    }

    public static BackgroundLoadDataService getInstance() {
        return instance;
    }

    public void downloadTask() {
        fakeData();
        for (Podcast podcast : allPodcast){
            podcastBitmap.add(new MapImage(podcast.get_id()+PODCAST, null));
            new DownloadTask(podcast.get_id()+PODCAST).execute(podcast.getUrlImg());
        }
        for (Playlist playlist : allPlaylist){
            podcastBitmap.add(new MapImage(playlist.get_id()+PLAYLIST, null));
            new DownloadTask(playlist.get_id()+PLAYLIST).execute(playlist.getUrlImg());
        }
    }
    public Bitmap getBitmapById(String id, String model){
        for (int i = 0; i< podcastBitmap.size();i++){
            if (podcastBitmap.get(i).id.equals(id+model.toLowerCase())){
                return podcastBitmap.get(i).bitmap;
            }
        }
        return BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
    }

    public void fakeData() {
        allPodcast.clear();
        allPodcast.add(
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
        allPodcast.add(
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
        allPodcast.add(
                new Podcast(
                        "7",
                        "Test",
                        "Bruh",
                        "1698395117524",
                        "localhost",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myfiles%2FS%C3%A1ch%20N%C3%B3i%20Tho%C3%A1t%20Kh%E1%BB%8Fi%20V%C3%B2ng%20B%E1%BA%ADn%20R%E1%BB%99n%20-%20Li%E1%BB%85u%20Thu%E1%BA%ADt%20Qu%C3%A2n%20-%20Gi%E1%BB%9Bi%20thi%E1%BB%87u.mp3?alt=media&token=b7066952-864d-49a7-9f16-b144a035edee",
                        "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fthoatkhoivongbanron.jpg?alt=media&token=e8a12b02-9ff1-4ae6-a161-57ff77cc1db7",
                        "6",
                        "1"
                ));
        allPlaylist.clear();
        allPlaylist.add(new Playlist(
                "1",
                "Thoát khỏi vòng lặp bận rộn",
                "Liễu Thuật Quân",
                "Đời sống",
                "0",
                "1225656",
                "Mô tả à",
                "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fthoatkhoivongbanron.jpg?alt=media&token=e8a12b02-9ff1-4ae6-a161-57ff77cc1db7",
                "1"
        ));
        allPlaylist.add(new Playlist(
                "1",
                "Test 1 podcast",
                "local",
                "Đời sống, truyền thông",
                "999454",
                "45545454",
                "Mô tả à",
                "https://firebasestorage.googleapis.com/v0/b/mlis-18b55.appspot.com/o/myimages%2Fvov.jpg?alt=media&token=75f2b920-c604-4c3d-9061-fe4c64d99b96",
                "1"
        ));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class DownloadTask extends AsyncTask<String, Void, MapImage> {
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
                decoded = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
                Log.e("error background service", e.getMessage());
                e.printStackTrace();
                MapImage mapImage = new MapImage();
                mapImage.id = id;
                mapImage.bitmap = decoded;
                for (int i=0;i<podcastBitmap.size();i++){
                    if(podcastBitmap.get(i).id.equals(id)){
                        podcastBitmap.get(i).bitmap = decoded;
                        podcastBitmap.get(i).hasRes = true;
                        return mapImage;
                    }
                }
                return mapImage;
            }
            MapImage mapImage = new MapImage();
            mapImage.id = id;
            mapImage.bitmap = decoded;
            for (int i=0;i<podcastBitmap.size();i++){
                if(podcastBitmap.get(i).id.equals(id)){
                    podcastBitmap.get(i).bitmap = decoded;
                    podcastBitmap.get(i).hasRes = true;
                    return mapImage;
                }
            }
            return mapImage;
        }
    }

}
