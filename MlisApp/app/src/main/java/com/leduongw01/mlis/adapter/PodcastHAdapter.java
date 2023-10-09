package com.leduongw01.mlis.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.utils.Const;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PodcastHAdapter extends RecyclerView.Adapter<PodcastHAdapter.PodcastViewHolder>{

    ArrayList<Podcast> podcastArrayList = new ArrayList<>();
    public PodcastHAdapter(){
        FakeData();
    }
    @NonNull
    @Override
    public PodcastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_audio, parent, false);
        return new PodcastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PodcastViewHolder holder, final int position) {
        holder.getTvTenTruyen().setText(podcastArrayList.get(position).getName());
        holder.getTvBoSung().setText(podcastArrayList.get(position).getAuthor());
        new DownloadImageTask(holder.getIvTruyen()).execute(podcastArrayList.get(position).getUrlImage());
//        holder.getIvTruyen().setImageBitmap();
    }

    @Override
    public int getItemCount() {
        return podcastArrayList.size();
    }

    public static class PodcastViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvTenTruyen;
        private final TextView tvBoSung;
        private final ImageView ivTruyen;
//        private final String idTruyen;

        public TextView getTvTenTruyen() {
            return tvTenTruyen;
        }

        public TextView getTvBoSung() {
            return tvBoSung;
        }

        public ImageView getIvTruyen() {
            return ivTruyen;
        }

        public PodcastViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenTruyen = itemView.findViewById(R.id.tvTenTruyen);
            tvBoSung = itemView.findViewById(R.id.tvBoSung);
            ivTruyen = itemView.findViewById(R.id.ivTruyen);
        }
    }
    private void FakeData(){
        podcastArrayList = new ArrayList<>();
        podcastArrayList.add(new Podcast("1", "M1", "soundhelix", Const.M1, "https://cdn0.fahasa.com/media/catalog/product/m/u/mua-he-khong-ten---bia-mem---qua-tang-kem-1.jpg", ""));
        podcastArrayList.add(new Podcast("1", "M2", "soundhelix", Const.M2, "https://cdn0.fahasa.com/media/catalog/product/m/u/mua-he-khong-ten---bia-cung---qua-tang-kem-1.jpg", ""));
        podcastArrayList.add(new Podcast("1", "M3", "soundhelix", Const.M3, "https://cdn0.fahasa.com/media/catalog/product/m/a/mashle_bia_tap-11-1.jpg", ""));
        podcastArrayList.add(new Podcast("1", "M4", "soundhelix", Const.M4, "", ""));
        podcastArrayList.add(new Podcast("1", "M5", "soundhelix", Const.M5, "https://cdn0.fahasa.com/media/catalog/product/k/a/kaguya-sama-cuoc-chien-to-t_nh_bia_tap-24-1.jpg", ""));
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
