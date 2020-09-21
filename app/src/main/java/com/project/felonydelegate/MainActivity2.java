package com.project.felonydelegate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {

    Intent it;
    SharedPreferences sp;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
    SimpleDateFormat rTime = new SimpleDateFormat("hh-mm-ss");
    SimpleDateFormat rDate = new SimpleDateFormat("yyyy-MM-dd");

    Button goBack,reportButton;

    LinearLayout activityComplaint,activityWantedList;
    ScrollView activitySuspicion;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refSuspicion = database.getReference("Suspicion");
    private DatabaseReference refComplaint = database.getReference("Complaint");
    private DatabaseReference refCriminals = database.getReference("Criminals");
    private StorageReference mStorage = FirebaseStorage.getInstance().getReference("Files");

    String controller,timestamp;
    String imageUrl="",videoUrl="";
    Uri imageUri,videoUri;

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        activityComplaint = findViewById(R.id.activityComplaint);
        activitySuspicion = findViewById(R.id.activitySuspicion);
        activityWantedList = findViewById(R.id.activityWantedList);

        goBack = findViewById(R.id.go_back);
        reportButton = findViewById(R.id.report_button);

        it = getIntent();
        controller = it.getStringExtra("keyVal");

        if(controller.equals("Suspicion")){
            activitySuspicion.setVisibility(View.VISIBLE);

            Button uploadPhoto,uploadVideo;
            uploadPhoto = findViewById(R.id.uploadPhoto);
            uploadVideo = findViewById(R.id.uploadVideo);

            uploadPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickImage();
                }
            });

            uploadVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickVideo();
                }
            });
        }
        else if(controller.equals("Wanted")){
            activityWantedList.setVisibility(View.VISIBLE);
            reportButton.setVisibility(View.GONE);
            activityWantedList();
        }
        else if(controller.equals("Complaint")){
            activityComplaint.setVisibility(View.VISIBLE);
        }

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                it = new Intent(MainActivity2.this,MainActivity.class);
                startActivity(it);
                finish();
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controller.equals("Suspicion")){
                    activitySuspicion();
                }
                else if(controller.equals("Complaint")){
                    activityComplaint();
                }
            }
        });
    }

    protected String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void pickImage() {
        it = new Intent(Intent.ACTION_GET_CONTENT);
        it.setType("image/*");
        startActivityForResult(it,100);
    }

    private void pickVideo() {
        it = new Intent(Intent.ACTION_GET_CONTENT);
        it.setType("video/*");
        startActivityForResult(it,200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK && data!=null){
            ImageView setPhoto = findViewById(R.id.setPhoto);
            Button uploadPhoto = findViewById(R.id.uploadPhoto);

            imageUri = data.getData();
            setPhoto.setImageURI(imageUri);
            setPhoto.setVisibility(View.VISIBLE);
            uploadPhoto.setText(imageUri.toString());
        }

        if(requestCode==200 && resultCode==RESULT_OK && data!=null){
            Button uploadVideo = findViewById(R.id.uploadVideo);

            videoUri = data.getData();
            uploadVideo.setText(videoUri.toString());
        }
    }

    private void activityComplaint() {
        StringBuilder complaint = new StringBuilder();

        CheckBox reportTheft,reportViolence,reportHarass;
        EditText reportDescriptionCrime;

        reportTheft = findViewById(R.id.reportTheft);
        reportViolence = findViewById(R.id.reportViolence);
        reportHarass = findViewById(R.id.reportHarass);
        reportDescriptionCrime = findViewById(R.id.reportDescriptionCrime);

        timestamp = sdf.format(new Date());
        sp = getSharedPreferences("info",MODE_PRIVATE);

        if(reportTheft.isChecked()){
            complaint = complaint.append("Theft ");
        }
        if(reportViolence.isChecked()){
            complaint = complaint.append("Violence ");
        }
        if(reportHarass.isChecked()) {
            complaint = complaint.append("Harassment ");
        }

        user.setId(sp.getString("Email",""));
        user.setUserLat(sp.getString("Lat",""));
        user.setUserLng(sp.getString("Lng",""));
        user.setUserPhone(sp.getString("PhoneCode",""),sp.getString("PhoneNo",""));
        user.setDate(rDate.format(new Date()));
        user.setTime(rTime.format(new Date()));
        user.setUserComplaint(complaint.toString()+reportDescriptionCrime.getText().toString());

        if(user.getUserComplaint().equals(null)){
            Toast.makeText(getApplicationContext(),"Nothing to Report!",Toast.LENGTH_SHORT).show();
        }
        else {
            user.setComplaintId(user.getId() + timestamp);
            refComplaint.child(user.getComplaintId()).setValue(user);
            Toast.makeText(getApplicationContext(),"Reported",Toast.LENGTH_SHORT).show();
        }
    }

    private void activityWantedList() {

        final RecyclerView WantedList = findViewById(R.id.WantedList);
        WantedList.setLayoutManager(new LinearLayoutManager(this));
        refCriminals.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList al = new ArrayList<>();
                ArrayList il = new ArrayList();

                for(DataSnapshot i : dataSnapshot.getChildren())
                {
                    User user = i.getValue(User.class);
                    al.add("Name : "+user.getCriminalName()+"\nAge : "+user.getAge());
                    il.add(user.getImageUrl());
                }
                WantedList.setAdapter(new wantedRecyclerAdapter(al,il));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error Loading!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void activitySuspicion() {
        final StorageReference mRef;
        final StorageReference vRef;

        sp = getSharedPreferences("info",MODE_PRIVATE);
        timestamp = sdf.format(new Date());

        Button uploadPhoto,uploadVideo;
        final EditText reportDescriptionSuspicion = findViewById(R.id.reportDescriptionSuspicion);

        uploadPhoto  = findViewById(R.id.uploadPhoto);
        uploadVideo  = findViewById(R.id.uploadVideo);

        user.setId(sp.getString("Email", ""));
        user.setUserLat(sp.getString("Lat", ""));
        user.setUserLng(sp.getString("Lng", ""));
        user.setDate(rDate.format(new Date()));
        user.setTime(rTime.format(new Date()));
        user.setUserPhone(sp.getString("PhoneCode", ""), sp.getString("PhoneNo", ""));

        user.setSuspicionId(user.getId()+timestamp);

        if(!uploadPhoto.getText().toString().equalsIgnoreCase("Upload Photo") ||
                !uploadVideo.getText().toString().equalsIgnoreCase("Upload Video")) {
            if(!uploadPhoto.getText().toString().equalsIgnoreCase("Upload Photo")){
                mRef = mStorage.child("Images/"+timestamp+"."+getFileExtension(imageUri));
                mRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUrl = uri.toString();
                                refSuspicion.child(user.getSuspicionId()+"/imageUrl").setValue(imageUrl);
                                Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            if(!uploadVideo.getText().toString().equalsIgnoreCase("Upload Video")){
                vRef = mStorage.child("Videos/"+timestamp+"."+getFileExtension(videoUri));
                vRef.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        vRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                videoUrl = uri.toString();
                                refSuspicion.child(user.getSuspicionId()+"/videoUrl").setValue(videoUrl);
                                Toast.makeText(getApplicationContext(), "Video Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
            Toast.makeText(getApplicationContext(), "Reporting....Please don't go back or turn off screen!", Toast.LENGTH_SHORT).show();
            user.setUserSuspicion(reportDescriptionSuspicion.getText().toString());
            refSuspicion.child(user.getSuspicionId()).setValue(user);
        }
        else {
            Toast.makeText(getApplicationContext(), "Nothing to Report!", Toast.LENGTH_SHORT).show();
        }
    }
}