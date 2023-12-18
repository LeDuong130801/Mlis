package com.leduongw01.mlis.adapter;


import static com.leduongw01.mlis.MainActivity.noImg;

import android.content.Context;
import android.os.Handler;
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
import com.leduongw01.mlis.models.Playlist;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.utils.Constant;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    Context context;
    List<Playlist> playlists;
    private static RecyclerViewClickListener recyclerViewPlaylistClickListener;
    public PlaylistAdapter(){}
    public PlaylistAdapter(Context context, List<Playlist> playlistList, RecyclerViewClickListener recyclerViewClickListener){
        this.context = context;
        playlists = playlistList;
        recyclerViewPlaylistClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_playlist_adapteruse, parent, false);
        return new PlaylistViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        holder.getPlaylistNameTextView().setText(playlists.get(position).getName());
        holder.getPlaylistAuthorTextView().setText(playlists.get(position).getAuthor());
        Handler a = new Handler();
        try{
            while (!BackgroundLoadDataService.getInstance().getMapImageById(playlists.get(position).get_id(), Constant.PLAYLIST).hasRes){
                a.postDelayed(null, 100);
            }
            holder.getPlaylistImageView().setImageBitmap(BackgroundLoadDataService.getBitmapById(playlists.get(position).get_id(), Constant.PLAYLIST));
        }
        catch (NullPointerException e){
            holder.getPlaylistImageView().setImageBitmap(noImg);
        }

    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    static class PlaylistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView playlistImageView;
        TextView playlistNameTextView;
        TextView playlistAuthorTextView;

        public ImageView getPlaylistImageView() {
            return playlistImageView;
        }

        public TextView getPlaylistNameTextView() {
            return playlistNameTextView;
        }

        public TextView getPlaylistAuthorTextView() {
            return playlistAuthorTextView;
        }

        public PlaylistViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            playlistImageView = itemView.findViewById(R.id.ivPlaylist);
            playlistNameTextView = itemView.findViewById(R.id.tvName);
            playlistAuthorTextView = itemView.findViewById(R.id.tvAuthor);
        }

        @Override
        public void onClick(View view) {
            recyclerViewPlaylistClickListener.recyclerViewListClicked(view, getLayoutPosition());
        }
    }
}

