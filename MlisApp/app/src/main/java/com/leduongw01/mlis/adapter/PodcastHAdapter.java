package com.leduongw01.mlis.adapter;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.Podcast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class PodcastHAdapter extends RecyclerView.Adapter<PodcastHAdapter.PodcastViewHolder>{

    ArrayList<Podcast> podcastArrayList;
    ArrayList<Bitmap> bitmapArrayList;
    Context context;
    private static RecyclerViewClickListener clickListener;
    static boolean canceled = false;

    public PodcastHAdapter(Context context,ArrayList<Podcast> podcastArrayList,RecyclerViewClickListener clickListener){
        this.context = context;
        PodcastHAdapter.clickListener = clickListener;
        this.podcastArrayList = podcastArrayList;
        bitmapArrayList = new ArrayList<>();
        for(int i=0;i<podcastArrayList.size();i++){
            bitmapArrayList.add(null);
            new DownloadImageTask(null, i).execute(podcastArrayList.get(i).getUrlImg());
        }
        canceled = false;
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
        if(!canceled) {
            holder.getTvTenTruyen().setText(podcastArrayList.get(position).getName());
            holder.getTvBoSung().setText(podcastArrayList.get(position).getAuthor());
            if (bitmapArrayList.get(position) == null)
                holder.downloadImageTask = new DownloadImageTask(holder.getIvTruyen(), position).execute(podcastArrayList.get(position).getUrlImg());
            else{
                holder.ivTruyen.setImageBitmap(bitmapArrayList.get(position));
            }
        }
//        new DownloadImageTask(holder.getIvTruyen()).execute(podcastArrayList.get(position).getUrlImage());
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
        AsyncTask downloadImageTask;
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
            linearLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View view) {
            clickListener.recyclerViewListClicked(view, getLayoutPosition());
            canceled = true;
        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        int position;
        public DownloadImageTask(ImageView bmImage, int position) {
            this.bmImage = bmImage;
            this.position = position;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            Bitmap decoded = null;
            if(!canceled)
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                mIcon11.compress(Bitmap.CompressFormat.PNG, 25, out);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            } catch (Exception e) {
                Log.e("error background service", e.getMessage());
                e.printStackTrace();
            }
            return decoded;
        }

        protected void onPostExecute(Bitmap result) {
            try {
                if (bmImage!=null)
                bmImage.setImageBitmap(result);
                if(bitmapArrayList.get(position)==null)
                bitmapArrayList.set(position, result);
            }
            catch (Exception e){
                Log.e("error: ", e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled(Bitmap bitmap) {
            super.onCancelled(bitmap);
        }
    }
}
