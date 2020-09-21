package com.project.felonydelegate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShowSuspicion extends AppCompatActivity {

    Intent it;
    TextView tv;
    ImageView imageShow;
    Button image,video,userInfo;
    WebView webView;
    String id,sid,imageUrl=null,videoUrl=null,suspicion=null;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Suspicion");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_suspicion);

        tv = findViewById(R.id.showSuspicion);
        userInfo = findViewById(R.id.showUser);
        image = findViewById(R.id.showImage);
        video = findViewById(R.id.showVideo);
        webView = findViewById(R.id.webView);
        imageShow = findViewById(R.id.imageShow);

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        it = getIntent();
        id = it.getStringExtra("suspicionVal");
        String[] part = id.split("\n");

        part[1] = part[1].replace("Suspicion ID","");
        part[1] = part[1].replace(" ","");
        sid = part[1].replace(":","");

        part[0] = part[0].replace("ID","");
        part[0] = part[0].replace(" ","");
        id = part[0].replace(":","");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot i : snapshot.getChildren()){
                    User user = i.getValue(User.class);
                    if(user.getSuspicionId().equals(sid)){
                        userInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent it = new Intent(ShowSuspicion.this,ShowClient.class);
                                it.putExtra("clientVal",id);
                                startActivity(it);
                            }
                        });

                        try {
                            suspicion = user.getUserSuspicion();
                            if(!suspicion.isEmpty()){
                                tv.setVisibility(View.VISIBLE);
                                tv.setText(suspicion);
                            }
                        }catch(Exception e){e.printStackTrace();}

                        try {
                            imageUrl = user.getImageUrl();
                            if(!imageUrl.isEmpty()){
                                image.setVisibility(View.VISIBLE);
                                image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imageShow.setVisibility(View.VISIBLE);
                                        webView.setVisibility(View.GONE);
                                        Picasso.get().load(imageUrl).into(imageShow);
                                    }
                                });
                            }
                        }catch(Exception e){e.printStackTrace();}
                        try {
                            videoUrl = user.getVideoUrl();
                            if(!videoUrl.isEmpty()){
                                video.setVisibility(View.VISIBLE);
                                video.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        webView.setVisibility(View.VISIBLE);
                                        imageShow.setVisibility(View.GONE);
                                        webView.loadUrl(videoUrl);
                                    }
                                });
                            }
                        }catch(Exception e){e.printStackTrace();}
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}