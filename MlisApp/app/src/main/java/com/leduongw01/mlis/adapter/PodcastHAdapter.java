package com.leduongw01.mlis.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.services.ForegroundAudioService;
import com.leduongw01.mlis.utils.Const;

import java.io.InputStream;
import java.util.ArrayList;

public class PodcastHAdapter extends RecyclerView.Adapter<PodcastHAdapter.PodcastViewHolder>{

    ArrayList<Podcast> podcastArrayList;
    Context context;
    private static RecyclerViewClickListener clickListener;

    public PodcastHAdapter(Context context,ArrayList<Podcast> podcastArrayList,RecyclerViewClickListener clickListener){
        this.context = context;
        PodcastHAdapter.clickListener = clickListener;
        this.podcastArrayList = podcastArrayList;
    }
    @NonNull
    @Override
    public PodcastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_audio, parent, false);
        return new PodcastViewHolder(view, context);
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

    public static class PodcastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView tvTenTruyen;
        private final TextView tvBoSung;
        private final ImageView ivTruyen;
        private final LinearLayout linearLayout;
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

        public PodcastViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTenTruyen = itemView.findViewById(R.id.tvTenTruyen);
            tvBoSung = itemView.findViewById(R.id.tvBoSung);
            ivTruyen = itemView.findViewById(R.id.ivTruyen);
            linearLayout = itemView.findViewById(R.id.layoutAudio);
        }

        @Override
        public void onClick(View view) {
            clickListener.recyclerViewListClicked(view, getLayoutPosition());
        }
    }
    private  class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
