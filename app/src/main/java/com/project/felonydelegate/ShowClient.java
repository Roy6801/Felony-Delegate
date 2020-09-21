package com.project.felonydelegate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowClient extends AppCompatActivity {

    Intent it;
    TextView tv;
    String id;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Client");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_client);

        tv = findViewById(R.id.showClient);
        it = getIntent();
        id = it.getStringExtra("clientVal");
        String[] part = id.split("\n");
        part[0] = part[0].replace("ID","");
        part[0] = part[0].replace(" ","");
        id = part[0].replace(":","");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot i : snapshot.getChildren()){
                    User user = i.getValue(User.class);
                    if(user.getId().equals(id)){
                        tv.setText("Name : " + user.getUserName() + "\nPhone : " + user.getUserPhone() + "\n E-mail : " + user.getUserEmail() + "\nLat : " + user.getUserLat() + " Lng : " + user.getUserLng() + "\nAddress : " + user.getUserAddress() + "\nLocality : " + user.getUserLocality() + "\nCountry : " + user.getUserCountryName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}