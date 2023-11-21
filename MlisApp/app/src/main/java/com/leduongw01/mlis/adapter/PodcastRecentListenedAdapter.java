package com.leduongw01.mlis.adapter;

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
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.utils.Constant;

import java.util.List;
import java.util.Objects;

public class PodcastRecentListenedAdapter extends RecyclerView.Adapter<PodcastRecentListenedAdapter.PodcastRecentListenedViewHolder> {
    private static RecyclerViewClickListener podcastRecentListenedClickListener;
    List<Podcast> podcastList;

    public PodcastRecentListenedAdapter(List<Podcast> list, RecyclerViewClickListener listener){
        podcastRecentListenedClickListener = listener;
        podcastList = list;
    }
    @NonNull
    @Override
    public PodcastRecentListenedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_audio_recent, parent, false);
        return new PodcastRecentListenedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PodcastRecentListenedViewHolder holder, int position) {
        holder.tvName.setText(podcastList.get(position).getName());
        holder.tvAuthor.setText(Objects.requireNonNull(BackgroundLoadDataService.getPlaylistById(podcastList.get(position).getPlaylistId())).getAuthor());
        holder.ivAudio.setImageBitmap(BackgroundLoadDataService.getBitmapById(podcastList.get(position).get_id(), Constant.PODCAST));
    }

    @Override
    public int getItemCount() {
        return podcastList.size();
    }

    static class PodcastRecentListenedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvAuthor;
        ImageView ivAudio;

        public PodcastRecentListenedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            ivAudio = itemView.findViewById(R.id.ivAudio);
        }

        @Override
        public void onClick(View view) {
            podcastRecentListenedClickListener.recyclerViewListClicked(view, getLayoutPosition());
        }
    }
}
