package com.project.felonydelegate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class SplashActivity extends AppCompatActivity {

    Handler handler = new Handler();
    GetLocation userLocation = new GetLocation();
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp = getSharedPreferences("info",MODE_PRIVATE);

        userLocation.userLocation(this);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent it = new Intent();
                if (sp.contains("Pwd")) {
                    if(sp.getString("Type","").equals("Client")) {
                        it = new Intent(SplashActivity.this, MainActivity.class);
                    }
                    else if(sp.getString("Type","").equals("Classified")){
                        it = new Intent(SplashActivity.this, ClassifiedActivity.class);
                    }
                }
                else {
                    it = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(it);
                finish();
            }
        },3000);
    }
}