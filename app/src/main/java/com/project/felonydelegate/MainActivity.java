package com.project.felonydelegate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    GetLocation getLocation = new GetLocation();
    Toolbar toolbar;
    SharedPreferences sp;
    ViewPager  vp;
    TabLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("info",MODE_PRIVATE);
        toolbar = findViewById(R.id.mainToolbar);
        vp = findViewById(R.id.mainViewPager);
        tl = findViewById(R.id.mainTabLayout);

        getLocation.userLocation(MainActivity.this);

        tl.addTab(tl.newTab().setCustomView(R.layout.report_icon));
        tl.addTab(tl.newTab().setCustomView(R.layout.map_icon));
        tl.setTabGravity(TabLayout.GRAVITY_FILL);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tl.getTabCount());
        vp.setAdapter(pagerAdapter);
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl));
        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("Logout")){
                    sp = getSharedPreferences("info",MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.remove("Pwd");
                    ed.apply();
                    Intent it = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(it);
                    finish();
                }
                return false;
            }
        });
    }
}