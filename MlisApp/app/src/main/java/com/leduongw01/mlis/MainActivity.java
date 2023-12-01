
package com.leduongw01.mlis;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.leduongw01.mlis.activities.HomeScreen;
import com.leduongw01.mlis.activities.LoadingDialog;
import com.leduongw01.mlis.models.MlisUser;
import com.leduongw01.mlis.models.StringValue;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.utils.Constant;
import com.leduongw01.mlis.utils.DefaultConfig;
import com.leduongw01.mlis.utils.MyComponent;

import java.net.MalformedURLException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static Bitmap noImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noImg = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
        startService(new Intent(MainActivity.this, BackgroundLoadDataService.class));
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFERENCES_NAME, MODE_PRIVATE);
        LoadingDialog dialog = new LoadingDialog(MainActivity.this);
        dialog.show();
        if (!sharedPreferences.getString("username", "none").equals("none")){
            ApiService.apisService.loginwithtoken(
                    sharedPreferences.getString("username", "none"),
                    sharedPreferences.getString("token", "none")
            ).enqueue(new Callback<MlisUser>() {
                @Override
                public void onResponse(Call<MlisUser> call, Response<MlisUser> response) {
                    dialog.dismiss();
                    BackgroundLoadDataService.mlisUser = response.body();
                    if(BackgroundLoadDataService.getInstance().checkAuthen()){
                        MyComponent.ToastShort(MainActivity.this, "Đã đăng nhập bằng tài khoản "+BackgroundLoadDataService.mlisUser.getUsername());
                        BackgroundLoadDataService.getInstance().loadMainFavorite();
                    }
                    else{
                        MyComponent.ToastShort(MainActivity.this, "Phiên đăng nhập đã hết hạn");
                        sharedPreferences.edit().putString("username", "none").apply();
                    }
                    Intent home = new Intent(MainActivity.this, HomeScreen.class);
                    startActivity(home);
                }
                @Override
                public void onFailure(Call<MlisUser> call, Throwable t) {
                    dialog.dismiss();
                    BackgroundLoadDataService.mlisUser = null;
                    MyComponent.ToastShort(MainActivity.this, "Phiên đăng nhập đã hết hạn");
                    Intent home = new Intent(MainActivity.this, HomeScreen.class);
                    startActivity(home);
                }
            });
        }
        else{
            dialog.dismiss();
            BackgroundLoadDataService.mlisUser = null;
            Intent home = new Intent(this, HomeScreen.class);
            startActivity(home);
        }
    }
}