package com.leduongw01.mlis.adapter;

import android.content.Context;
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
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.services.ForegroundAudioService;
import com.leduongw01.mlis.utils.Constant;

import java.util.List;
import java.util.Objects;

public class FavoriteDetailAdapter  extends RecyclerView.Adapter<FavoriteDetailAdapter.FavoriteDetailViewHolder> {
    private static RecyclerViewClickListener favoriteListDetailClickListener;
    List<Podcast> podcastList;
    Context context;
    public FavoriteDetailAdapter(Context context, List<Podcast> list, RecyclerViewClickListener listener){
        favoriteListDetailClickListener = listener;
        podcastList = list;
        this.context = context.getApplicationContext();
    }
    @NonNull
    @Override
    public FavoriteDetailAdapter.FavoriteDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_audio_recent, parent, false);
        return new FavoriteDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteDetailAdapter.FavoriteDetailViewHolder holder, int position) {
        holder.tvName.setText(podcastList.get(position).getName());
        holder.tvAuthor.setText(Objects.requireNonNull(BackgroundLoadDataService.getPlaylistById(podcastList.get(position).getPlaylistId())).getAuthor());
        holder.ivAudio.setImageBitmap(BackgroundLoadDataService.getBitmapById(podcastList.get(position).get_id(), Constant.PODCAST));
        if (position%2==0){
            holder.layout.setBackgroundColor(context.getColor(R.color.background0));
        }
        else{
            holder.layout.setBackgroundColor(context.getColor(R.color.background1));
        }
    }

    @Override
    public int getItemCount() {
        return podcastList.size();
    }

    static class FavoriteDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvAuthor;
        ImageView ivAudio;
        ImageView icMore;
        LinearLayout layout;

        public FavoriteDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvName = itemView.findViewById(R.id.tvName);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            ivAudio = itemView.findViewById(R.id.ivAudio);
            icMore = itemView.findViewById(R.id.icMore);
            layout = itemView.findViewById(R.id.layout);
        }

        @Override
        public void onClick(View view) {
            favoriteListDetailClickListener.recyclerViewListClicked(view, getLayoutPosition());
        }
    }
}
