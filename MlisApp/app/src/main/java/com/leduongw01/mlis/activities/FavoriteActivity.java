package com.leduongw01.mlis.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.leduongw01.mlis.MainActivity;
import com.leduongw01.mlis.R;
import com.leduongw01.mlis.adapter.AllFavoriteAdapter;
import com.leduongw01.mlis.databinding.ActivityFavoriteBinding;
import com.leduongw01.mlis.listener.RecycleViewLongClickListener;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.Favorite;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.utils.Constant;
import com.leduongw01.mlis.utils.MyComponent;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteActivity extends AppCompatActivity {
    ActivityFavoriteBinding binding;
    List<Favorite> favoriteList;
    String currentId = "";
    String currentName = "";
    Handler handler;
    CreateFavoriteDialog dialog;
    RenameDialog renameDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);
        Objects.requireNonNull(getSupportActionBar()).hide();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog!= null) {
                    if (!dialog.isShowing()){
                        if (BackgroundLoadDataService.getInstance().checkAuthen()){
                            BackgroundLoadDataService.getInstance().loadFavorite();
                            ktRecycle();
                            dialog = null;
                        }
                        else onBackPressed();
                    }
                }
                if (renameDialog!=null){
                    if (!renameDialog.isShowing()){
                        if (BackgroundLoadDataService.getInstance().checkAuthen()){
                            BackgroundLoadDataService.getInstance().loadFavorite();
                            ktRecycle();
                            renameDialog = null;
                        }
                        else onBackPressed();
                    }
                }
                handler.postDelayed(this, 100);
            }
        }, 100);
        if (BackgroundLoadDataService.getInstance().checkAuthen()){
            binding.text1.setText("Danh sách yêu thích của "+ BackgroundLoadDataService.mlisUser.getUsername());
            BackgroundLoadDataService.getInstance().loadFavorite();
            ktRecycle();
        }
        else onBackPressed();
    }

    synchronized void ktRecycle() {
        favoriteList = BackgroundLoadDataService.getAllFavorite();
        binding.btAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new CreateFavoriteDialog(FavoriteActivity.this);
                dialog.show();
            }
        });
        binding.rcvFavoriteList.setAdapter(new AllFavoriteAdapter(FavoriteActivity.this, favoriteList,
                new RecyclerViewClickListener() {
                    @Override
                    public void recyclerViewListClicked(View v, int position) {
                        Intent intent = new Intent(FavoriteActivity.this, FavoriteDetailActivity.class);
                        intent.putExtra("favoriteId", favoriteList.get(position).get_id());
                        startActivity(intent);
                    }
                },
                new RecycleViewLongClickListener() {
                    @Override
                    public void recyclerViewLongClicked(View v, int position) {
                        showMenu(v, position);
                    }
                }));
        binding.rcvFavoriteList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    void showMenu(View view, int position){
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_favorite, popup.getMenu());
        popup.show();
        popup.getMenu().findItem(R.id.itSuaTen).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                renameDialog = new RenameDialog(FavoriteActivity.this, currentId, currentName);
                renameDialog.show();
                return false;
            }
        });
        popup.getMenu().findItem(R.id.itXoa).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteActivity.this);
                AlertDialog dialog = builder.setTitle("Xóa danh sách yêu thích này?").setMessage("Danh sách yêu thích sau khi xóa sẽ không thể khôi phục").setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                    ApiService.apisService.deleteFavorite(currentId).enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {

                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {

                                        }
                                    });
                                    for (int j=0;j<BackgroundLoadDataService.getAllFavorite().size();j++){
                                        if (BackgroundLoadDataService.getAllFavorite().get(j).get_id().equals(currentId)){
                                            BackgroundLoadDataService.getAllFavorite().remove(j);
                                            break;
                                        }
                                    }
                                    ktRecycle();
                            }
                        })
                        .setNegativeButton("Trở lại", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                builder.show();
                return false;
            }
        });
        currentId = favoriteList.get(position).get_id();
        currentName = favoriteList.get(position).getName();
    }

    @Override
    public void onBackPressed() {
        BackgroundLoadDataService.getInstance().loadFavorite();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}