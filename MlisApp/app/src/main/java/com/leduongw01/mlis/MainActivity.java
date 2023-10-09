
package com.leduongw01.mlis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;

import com.leduongw01.mlis.activities.HomeScreen;
import com.leduongw01.mlis.services.ForegroundAudioService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent serviceIntent = new Intent(this, ForegroundAudioService.class);
//        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
//        ContextCompat.startForegroundService(this, serviceIntent);
        Intent home = new Intent(this, HomeScreen.class);
        startActivity(home);
    }
}