
package com.leduongw01.mlis;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.leduongw01.mlis.activities.HomeScreen;
import com.leduongw01.mlis.models.StringValue;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.services.ForegroundAudioService;
import com.leduongw01.mlis.utils.Const;
import com.leduongw01.mlis.utils.DefaultConfig;
import com.leduongw01.mlis.utils.MyComponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyComponent.ToastShort(this, "main");
//        Intent serviceIntent = new Intent(this, ForegroundAudioService.class);
//        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
//        ContextCompat.startForegroundService(this, serviceIntent);
        Intent home = new Intent(this, HomeScreen.class);
//        try {
//            getKeyApi();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
        startActivity(home);
    }

    public void getKeyApi() throws MalformedURLException {
        ApiService.apisService.getApiKey().enqueue(new Callback<StringValue>() {
            @Override
            public void onResponse(Call<StringValue> call, Response<StringValue> response) {
                MyComponent.ToastShort(MainActivity.this, "ahah");
                if (response.body().equals("MlisServer")) {
//                            DefaultConfig.serverAddress = "192.168.1." + i;
                    MyComponent.ToastShort(MainActivity.this, DefaultConfig.serverAddress);
                    return;
                }
            }

            @Override
            public void onFailure(Call<StringValue> call, Throwable t) {
                Log.d("TAG", "onFailure: ");
                MyComponent.ToastShort(MainActivity.this, "huhu");
            }
        });

    }
}