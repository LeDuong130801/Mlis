package com.leduongw01.mlis.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.adapter.CommentAdapter;
import com.leduongw01.mlis.databinding.ActivityChatBinding;
import com.leduongw01.mlis.models.Comment;
import com.leduongw01.mlis.models.ViewComment;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.services.ForegroundAudioService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    int REQLogin = 4219;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        Objects.requireNonNull(getSupportActionBar()).hide();
        ktSuKien();
        loadComment();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadComment();
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    void ktSuKien() {
        binding.btSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = binding.etComment.getText().toString();
                if (!text.trim().equals("")) {
                    Comment comment = new Comment(
                            ForegroundAudioService.getCurrentPodcast().get_id(),
                            BackgroundLoadDataService.mlisUser.get_id(),
                            text);
                    ApiService.apisService.sendComment(BackgroundLoadDataService.mlisUser.get_id(), comment).enqueue(new Callback<Comment>() {
                        @Override
                        public void onResponse(Call<Comment> call, Response<Comment> response) {
                            loadComment();
                        }

                        @Override
                        public void onFailure(Call<Comment> call, Throwable t) {

                        }
                    });
                }
                binding.etComment.setText("");
            }
        });
        binding.goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChatActivity.this, LoginActivity.class);
                startActivityForResult(i, REQLogin);
            }
        });
    }
    void loadComment(){
        if (BackgroundLoadDataService.getInstance().checkAuthen()) {
            binding.noCommentBox.setVisibility(View.GONE);
            binding.commentBox.setVisibility(View.VISIBLE);
        } else {
            binding.commentBox.setVisibility(View.GONE);
            binding.noCommentBox.setVisibility(View.VISIBLE);
        }
        ApiService.apisService.viewComment(ForegroundAudioService.getCurrentPodcast().get_id(), "1").enqueue(new Callback<List<ViewComment>>() {
            @Override
            public void onResponse(Call<List<ViewComment>> call, Response<List<ViewComment>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null ? response.body().isEmpty() : false){
                        binding.noCommentText.setVisibility(View.VISIBLE);
                        binding.rcvComment.setVisibility(View.GONE);
                    }
                    else{
                        binding.noCommentText.setVisibility(View.GONE);
                        binding.rcvComment.setVisibility(View.VISIBLE);
                        List<ViewComment> viewComments = new ArrayList<>();
                        for (int i=response.body().size()-1; i>=0; i--){
                            viewComments.add(response.body().get(i));
                        }
                        binding.rcvComment.setAdapter(new CommentAdapter(ChatActivity.this, viewComments));
                        binding.rcvComment.setLayoutManager(new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ViewComment>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQLogin){
            ktSuKien();
            loadComment();
        }
    }
}