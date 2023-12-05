package com.leduongw01.mlis.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.adapter.AllFavoriteAdapter;
import com.leduongw01.mlis.databinding.ActivityFavoriteBinding;
import com.leduongw01.mlis.listener.RecycleViewLongClickListener;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.services.BackgroundLoadDataService;

import java.util.Objects;

public class FavoriteActivity extends AppCompatActivity {
    ActivityFavoriteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);
        Objects.requireNonNull(getSupportActionBar()).hide();
        ktRecycle();
    }

    void ktRecycle() {
        binding.rcvFavoriteList.setAdapter(new AllFavoriteAdapter(FavoriteActivity.this, BackgroundLoadDataService.getAllFavorite(),
                new RecyclerViewClickListener() {
                    @Override
                    public void recyclerViewListClicked(View v, int position) {

                    }
                },
                new RecycleViewLongClickListener() {
                    @Override
                    public void recyclerViewLongClicked(View v, int position) {

                    }
                }));
        binding.rcvFavoriteList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}