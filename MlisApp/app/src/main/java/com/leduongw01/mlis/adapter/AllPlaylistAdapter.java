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

public class AllPlaylistAdapter extends RecyclerView.Adapter<AllPlaylistAdapter.AllPlaylistViewHolder> {

    Context context;
    private static RecyclerViewClickListener recyclerViewAllPlaylistClickListener;
    public AllPlaylistAdapter(){}
    public AllPlaylistAdapter(Context context, RecyclerViewClickListener recyclerViewAllPlaylistClickListener){
        this.context = context;
        AllPlaylistAdapter.recyclerViewAllPlaylistClickListener = recyclerViewAllPlaylistClickListener;
    }

    @NonNull
    @Override
    public AllPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_playlist_adapteruse, parent, false);
        return new AllPlaylistViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull AllPlaylistViewHolder holder, int position) {
        holder.getPlaylistNameTextView().setText(BackgroundLoadDataService.getAllPlaylist().get(position).getName());
        holder.getPlaylistAuthorTextView().setText(BackgroundLoadDataService.getAllPlaylist().get(position).getAuthor());
        Handler a = new Handler();
        try{
            while (!BackgroundLoadDataService.getInstance().getMapImageById(BackgroundLoadDataService.getAllPlaylist().get(position).get_id(), Constant.PLAYLIST).hasRes){
                a.postDelayed(null, 100);
                Log.d("while", "onBindViewHolder: loop inf");
            }
            holder.getPlaylistImageView().setImageBitmap(BackgroundLoadDataService.getInstance().getBitmapById(BackgroundLoadDataService.getAllPlaylist().get(position).get_id(), Constant.PLAYLIST));
        }
        catch (NullPointerException e){
            holder.getPlaylistImageView().setImageBitmap(noImg);
        }

    }

    @Override
    public int getItemCount() {
        return BackgroundLoadDataService.allPlaylist.size();
    }

    static class AllPlaylistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView playlistImageView;
        TextView playlistNameTextView;
        TextView playlistAuthorTextView;
        LinearLayout layout;

        public ImageView getPlaylistImageView() {
            return playlistImageView;
        }

        public TextView getPlaylistNameTextView() {
            return playlistNameTextView;
        }

        public TextView getPlaylistAuthorTextView() {
            return playlistAuthorTextView;
        }

        public LinearLayout getLayout() {
            return layout;
        }

        public AllPlaylistViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            playlistImageView = itemView.findViewById(R.id.ivPlaylist);
            playlistNameTextView = itemView.findViewById(R.id.tvName);
            playlistAuthorTextView = itemView.findViewById(R.id.tvAuthor);
            layout = itemView.findViewById(R.id.layoutCell);
            layout.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View view) {
            recyclerViewAllPlaylistClickListener.recyclerViewListClicked(view, getLayoutPosition());
        }
    }
}
