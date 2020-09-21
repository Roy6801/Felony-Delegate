package com.project.felonydelegate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ClassifiedActivity extends AppCompatActivity {

    Intent it;
    Button registerSuspicion,registerComplaint,registerWantedList,registerClient;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classified);

            toolbar = findViewById(R.id.classifiedToolbar);
            registerSuspicion = findViewById(R.id.registerSuspicion);
            registerWantedList = findViewById(R.id.registerWantedList);
            registerComplaint = findViewById(R.id.registerComplaint);
            registerClient = findViewById(R.id.registerClient);

            registerSuspicion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    it = new Intent(ClassifiedActivity.this,ClassifiedActivity2.class);
                    it.putExtra("keyVal","Suspicion");
                    startActivity(it);
                    finish();
                }
            });

            registerWantedList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    it = new Intent(ClassifiedActivity.this,ClassifiedActivity2.class);
                    it.putExtra("keyVal","Wanted");
                    startActivity(it);
                    finish();
                }
            });

            registerComplaint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    it = new Intent(ClassifiedActivity.this,ClassifiedActivity2.class);
                    it.putExtra("keyVal","Complaint");
                    startActivity(it);
                    finish();
                }
            });

            registerClient.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    it = new Intent(ClassifiedActivity.this,ClassifiedActivity2.class);
                    it.putExtra("keyVal","Client");
                    startActivity(it);
                    finish();
            }
            });

            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getTitle().equals("Logout")){
                        SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
                        SharedPreferences.Editor ed = sp.edit();
                        ed.remove("Pwd");
                        ed.apply();
                        Intent it = new Intent(ClassifiedActivity.this,LoginActivity.class);
                        startActivity(it);
                        finish();
                    }
                    return false;
                }
            });
    }
}