package com.leduongw01.mlis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.leduongw01.mlis.R;
import com.leduongw01.mlis.listener.RecycleViewLongClickListener;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.Favorite;
import com.leduongw01.mlis.models.Podcast;

import java.util.List;

public class AoRtoPlaylistAdapter extends RecyclerView.Adapter<AoRtoPlaylistAdapter.AoRtoPlaylistViewHolder> {
    RecyclerViewClickListener addToFavoriteClickListener;
    RecyclerViewClickListener removeToFavoriteClickListener;
    Context context;
    List<Favorite> favoriteList;
    Podcast podcast;
    public AoRtoPlaylistAdapter(Context context, List<Favorite> favoriteList, Podcast podcast, RecyclerViewClickListener add, RecyclerViewClickListener remove){
        this.context = context;
        this.favoriteList = favoriteList;
        this.podcast = podcast;
        addToFavoriteClickListener = add;
        removeToFavoriteClickListener = remove;
    }
    @NonNull
    @Override
    public AoRtoPlaylistAdapter.AoRtoPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_add_or_remove_tofavotite, parent, false);
        return new AoRtoPlaylistAdapter.AoRtoPlaylistViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull AoRtoPlaylistAdapter.AoRtoPlaylistViewHolder holder, int position) {
        holder.favoriteName.setText(favoriteList.get(position).getName());
        if (favoriteList.get(position).getPodListId().contains(podcast.get_id())){
            holder.addButton.setVisibility(View.GONE);
            holder.removeButton.setVisibility(View.VISIBLE);
        }
        else{
            holder.addButton.setVisibility(View.VISIBLE);
            holder.removeButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    class AoRtoPlaylistViewHolder extends RecyclerView.ViewHolder {
        public TextView favoriteName;
        public MaterialButton addButton;
        public MaterialButton removeButton;

        public AoRtoPlaylistViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            favoriteName = itemView.findViewById(R.id.favoriteName);
            addButton = itemView.findViewById(R.id.btAdd);
            removeButton = itemView.findViewById(R.id.btRemove);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeToFavoriteClickListener.recyclerViewListClicked(view, getLayoutPosition());
                }
            });
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToFavoriteClickListener.recyclerViewListClicked(view, getLayoutPosition());
                }
            });
        }
    }
}