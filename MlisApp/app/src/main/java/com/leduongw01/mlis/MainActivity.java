
package com.leduongw01.mlis;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.leduongw01.mlis.activities.HomeScreen;
import com.leduongw01.mlis.models.StringValue;
import com.leduongw01.mlis.services.ApiService;
import com.leduongw01.mlis.services.BackgroundLoadDataService;
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
        Intent home = new Intent(this, HomeScreen.class);
        startActivity(home);
    }
}