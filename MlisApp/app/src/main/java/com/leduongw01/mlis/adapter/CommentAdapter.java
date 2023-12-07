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
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.utils.MyComponent;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    List<Comment> commentList;
    Context context;

    public CommentAdapter(Context context, List<Comment> commentList){
        this.context = context;
        this.commentList = commentList;
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
        if(MyComponent.getStringTempRef(context, commentList.get(position).getMlisUserId()).equals("none")){
            int index = position;
            ApiService.apisService.getUsername(commentList.get(position).getMlisUserId(), index).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d("yayayaya", "onResponse: "+response.body());
                    if (response.body().equals("none")){
                        MyComponent.setStringTempRef(context, commentList.get(index).getMlisUserId() ,"anonymous");
                        holder.mlisName.setText("anonymous");
                    }
                    else{
                        MyComponent.setStringTempRef(context, commentList.get(index).getMlisUserId() ,response.body());
                        holder.mlisName.setText(response.body());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
        else{
            holder.mlisName.setText(MyComponent.getStringTempRef(context, commentList.get(position).getMlisUserId()));
        }
        holder.cmtOn.setText(MyComponent.toDateTimeStr(commentList.get(position).getCmtOn()));
        holder.content.setText(commentList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
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
