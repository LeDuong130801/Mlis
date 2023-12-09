package com.leduongw01.mlis.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.models.Comment;
import com.leduongw01.mlis.models.ViewComment;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.utils.MyComponent;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    List<ViewComment> viewComments;
    Context context;

    public CommentAdapter(Context context, List<ViewComment> viewComments){
        this.context = context;
        this.viewComments = viewComments;
    }
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_message, parent, false);
        return new CommentViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.mlisName.setText(viewComments.get(position).getUsername());
        holder.cmtOn.setText(MyComponent.toDateTimeStr(viewComments.get(position).getCmtOn()));
        holder.content.setText(viewComments.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return viewComments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder{
        public TextView mlisName;
        public TextView cmtOn;
        public TextView content;
        public CommentViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            mlisName = itemView.findViewById(R.id.tvName);
            cmtOn = itemView.findViewById(R.id.tvCmtOn);
            content = itemView.findViewById(R.id.tvContent);
        }
    }
}
