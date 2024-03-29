package com.leduongw01.mlis.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.utils.Constant;
import com.leduongw01.mlis.utils.MyComponent;

import java.util.List;

public class PlaylistDetailAdapter extends RecyclerView.Adapter<PlaylistDetailAdapter.PlaylistDetailViewHolder> {
    private static RecyclerViewClickListener playlistDetailClickListener;
    private static RecyclerViewClickListener favoriteClickListener;
    Context context;
    List<Podcast> podcastList;

    public PlaylistDetailAdapter(Context context, List<Podcast> podcastList, RecyclerViewClickListener clickListener, RecyclerViewClickListener favoriteClickListener) {
        this.context = context;
        this.podcastList = podcastList;
        PlaylistDetailAdapter.playlistDetailClickListener = clickListener;
        PlaylistDetailAdapter.favoriteClickListener = favoriteClickListener;
    }

    @NonNull
    @Override
    public PlaylistDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_chapter, parent, false);
        return new PlaylistDetailViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistDetailViewHolder holder, int position) {
        holder.getTvName().setText(podcastList.get(position).getName());
        holder.getTvLastUpdate().setText(MyComponent.toDateTimeStr(podcastList.get(position).getUpdateOn()));
        if (BackgroundLoadDataService.getInstance().checkAuthen()) {
            holder.ivFavorite.setVisibility(View.VISIBLE);
        } else {
            holder.ivFavorite.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return podcastList.size();
    }

    static class PlaylistDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName;
        TextView tvLastUpdate;
        ImageView ivFavorite;

        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvLastUpdate() {
            return tvLastUpdate;
        }

        public PlaylistDetailViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvName = itemView.findViewById(R.id.chapName);
            tvLastUpdate = itemView.findViewById(R.id.lastUpdate);
            ivFavorite = itemView.findViewById(R.id.isFavorite);
            ivFavorite.setOnClickListener(view -> favoriteClickListener.recyclerViewListClicked(view, getLayoutPosition()));
        }

        @Override
        public void onClick(View view) {
            playlistDetailClickListener.recyclerViewListClicked(view, getLayoutPosition());
        }
    }
}
