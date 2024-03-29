
package com.leduongw01.mlis;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;

import com.leduongw01.mlis.activities.HomeScreen;
import com.leduongw01.mlis.models.MlisUser;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
import com.leduongw01.mlis.utils.Constant;
import com.leduongw01.mlis.utils.MyComponent;
import com.leduongw01.mlis.utils.MyConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static Bitmap noImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String servername = MyComponent.getStringRef(getApplicationContext(), "servername");
        if (servername.equals("none")){
            MyConfig.serverAddress = "http://192.168.1.35:8080/";
        }
        else MyConfig.serverAddress = servername;
        noImg = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
        startService(new Intent(MainActivity.this, BackgroundLoadDataService.class));
        int permission_write_storage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission_write_storage != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        int permission_read_storage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission_read_storage != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFERENCES_NAME, MODE_PRIVATE);
        if (!sharedPreferences.getString("username", "none").equals("none")){
            ApiService.apisService.loginwithtoken(
                    sharedPreferences.getString("username", "none"),
                    sharedPreferences.getString("token", "none")
            ).enqueue(new Callback<MlisUser>() {
                @Override
                public void onResponse(Call<MlisUser> call, Response<MlisUser> response) {
                    BackgroundLoadDataService.mlisUser = response.body();
                    if(BackgroundLoadDataService.getInstance().checkAuthen()){
                        MyComponent.ToastShort(MainActivity.this, "Đã đăng nhập bằng tài khoản "+BackgroundLoadDataService.mlisUser.getUsername());
                        BackgroundLoadDataService.getInstance().loadFavorite();
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
                    BackgroundLoadDataService.mlisUser = new MlisUser("-1");
                    MyComponent.ToastShort(MainActivity.this, "Mất kết nối với máy chủ");
                    Intent home = new Intent(MainActivity.this, HomeScreen.class);
                    startActivity(home);
                }
            });
        }
        else{
            BackgroundLoadDataService.mlisUser = null;
            Intent home = new Intent(this, HomeScreen.class);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(home);
        }
    }
}