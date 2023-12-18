package com.leduongw01.mlis.adapter;

import static com.leduongw01.mlis.MainActivity.noImg;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.Playlist;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.utils.Constant;

import java.util.List;

public class MoiCapNhatPlaylistAdapter extends RecyclerView.Adapter<MoiCapNhatPlaylistAdapter.MoiCapNhatPlaylistViewHolder> {

    Context context;
    List<Playlist> playlists;
    private static RecyclerViewClickListener recyclerViewMoiPlaylistClickListener;
    public MoiCapNhatPlaylistAdapter(){}
    public MoiCapNhatPlaylistAdapter(Context context, List<Playlist> playlistList, RecyclerViewClickListener recyclerViewPlaylistClickListener){
        this.context = context;
        playlists = playlistList;
        recyclerViewMoiPlaylistClickListener = recyclerViewPlaylistClickListener;
    }

    @NonNull
    @Override
    public MoiCapNhatPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_playlist_adapteruse, parent, false);
        return new MoiCapNhatPlaylistViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MoiCapNhatPlaylistViewHolder holder, int position) {
        holder.getPlaylistNameTextView().setText(playlists.get(position).getName());
        holder.getPlaylistAuthorTextView().setText(playlists.get(position).getAuthor());
        Handler a = new Handler();
        try{
            while (!BackgroundLoadDataService.getInstance().getMapImageById(playlists.get(position).get_id(), Constant.PLAYLIST).hasRes){
                a.postDelayed(null, 100);
                Log.d("while", "onBindViewHolder: loop inf");
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

    static class MoiCapNhatPlaylistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        public MoiCapNhatPlaylistViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            playlistImageView = itemView.findViewById(R.id.ivPlaylist);
            playlistNameTextView = itemView.findViewById(R.id.tvName);
            playlistAuthorTextView = itemView.findViewById(R.id.tvAuthor);
        }

        @Override
        public void onClick(View view) {
            recyclerViewMoiPlaylistClickListener.recyclerViewListClicked(view, getLayoutPosition());
        }
    }
}


