package com.leduongw01.mlis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.leduongw01.mlis.R;
import com.leduongw01.mlis.listener.RecycleViewLongClickListener;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.Favorite;
import com.leduongw01.mlis.services.BackgroundLoadDataService;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AllFavoriteAdapter extends RecyclerView.Adapter<AllFavoriteAdapter.AllFavoriteViewHolder> {
    RecyclerViewClickListener allFavoriteClickListener;
    RecycleViewLongClickListener allFavoriteLongClickListener;
    Context context;
    List<Favorite> favoriteList;
    public AllFavoriteAdapter(Context context, List<Favorite> favoriteList, RecyclerViewClickListener recyclerViewClickListener, RecycleViewLongClickListener recycleViewLongClickListener){
        this.context = context;
        this.favoriteList = favoriteList;
        this.allFavoriteClickListener = recyclerViewClickListener;
        this.allFavoriteLongClickListener = recycleViewLongClickListener;
    }
    @NonNull
    @Override
    public AllFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_favorite_adapteruse, parent, false);
        return new AllFavoriteAdapter.AllFavoriteViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull AllFavoriteViewHolder holder, int position) {
        holder.favoriteName.setText(favoriteList.get(position).getName());
        holder.favoriteB.setText(BackgroundLoadDataService.mlisUser.getUsername());
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    class AllFavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView favoriteName;
        public TextView favoriteB;
        public ImageView favoriteImage;

        public AllFavoriteViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            favoriteB = itemView.findViewById(R.id.favoriteB);
            favoriteName = itemView.findViewById(R.id.favoriteName);
            favoriteImage = itemView.findViewById(R.id.favoriteImage);
        }

        @Override
        public void onClick(View view) {
            allFavoriteClickListener.recyclerViewListClicked(view, getLayoutPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            allFavoriteLongClickListener.recyclerViewLongClicked(view, getLayoutPosition());
            return true;
        }
    }
}
