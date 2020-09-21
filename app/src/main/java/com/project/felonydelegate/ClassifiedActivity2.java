package com.project.felonydelegate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class ClassifiedActivity2 extends AppCompatActivity {

    Intent it;
    SharedPreferences sp;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

    Button go_Back,doneButton;

    LinearLayout getComplaint,updateWantedList,getSuspicion,getClient;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refSuspicion = database.getReference("Suspicion");
    private DatabaseReference refComplaint = database.getReference("Complaint");
    private DatabaseReference refCriminals = database.getReference("Criminals");
    private DatabaseReference refClient = database.getReference("Client");
    private StorageReference mStorage = FirebaseStorage.getInstance().getReference("Files");
    private StorageReference mRef;

    String controller,timestamp;
    String imageUrl="";
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classified2);

        it = getIntent();
        controller = it.getStringExtra("keyVal");

        getSuspicion = findViewById(R.id.getSuspicion);
        getComplaint = findViewById(R.id.getComplaint);
        updateWantedList = findViewById(R.id.updateWantedList);
        getClient = findViewById(R.id.getUser);

        go_Back = findViewById(R.id.goback);
        doneButton = findViewById(R.id.done);

        if(controller.equals("Suspicion")){
            getSuspicion.setVisibility(View.VISIBLE);
            doneButton.setVisibility(View.GONE);
            getSuspicion();
        }
        else if(controller.equals("Complaint")){
            getComplaint.setVisibility(View.VISIBLE);
            doneButton.setVisibility(View.GONE);
            getComplaint();
        }
        else if(controller.equals("Wanted")){
            updateWantedList.setVisibility(View.VISIBLE);
            updateWantedList();
        }
        else if(controller.equals("Client")){
            getClient.setVisibility(View.VISIBLE);
            doneButton.setVisibility(View.GONE);
            getClient();
        }

        go_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                it = new Intent(ClassifiedActivity2.this,ClassifiedActivity.class);
                startActivity(it);
                finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Button upload = findViewById(R.id.uploadCriminalPhoto);
            ImageView imageView = findViewById(R.id.criminalPhoto);
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageView.setVisibility(View.VISIBLE);
            upload.setText(imageUri.toString());
        }
    }

    private void getClient() {
        final ListView client = findViewById(R.id.listUser);
        ArrayList al = new ArrayList();
        final ArrayAdapter ad = new ArrayAdapter(getApplicationContext(),R.layout.info_layout,al);
        client.setAdapter(ad);

        refClient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot i : snapshot.getChildren()){
                    User user = i.getValue(User.class);
                    ad.add("ID : "+user.getId()+"\nName : "+user.getUserName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Can't Load!",Toast.LENGTH_SHORT).show();
            }
        });

        client.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = client.getItemAtPosition(position).toString();
                Intent it = new Intent(ClassifiedActivity2.this,ShowClient.class);
                it.putExtra("clientVal",data);
                startActivity(it);
            }
        });
    }

    private void updateWantedList() {
        final Button upload = findViewById(R.id.uploadCriminalPhoto);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        final EditText fn,mn,ln,age;
        fn = findViewById(R.id.firstName);
        mn = findViewById(R.id.middleName);
        ln = findViewById(R.id.lastName);
        age = findViewById(R.id.age);

        timestamp = sdf.format(new Date());

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!upload.getText().toString().equalsIgnoreCase("Upload Photo")) {
                    if (fn.getText().toString().isEmpty() || age.getText().toString().isEmpty()) {
                        if (fn.getText().toString().isEmpty()) {
                            fn.setError("First Name Required!");
                        }
                        if (age.getText().toString().isEmpty()) {
                            age.setError("Age Range Required!");
                        }
                    }
                    else {
                        final User user = new User();
                        user.setCriminalName(fn.getText().toString(), mn.getText().toString(), ln.getText().toString());
                        user.setAge(age.getText().toString());
                        user.setId(fn.getText().toString() + timestamp);

                        mRef = mStorage.child("Images/" + timestamp + "." + getFileExtension(imageUri));

                        mRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageUrl = uri.toString();
                                        refCriminals.child(user.getId()).setValue(user);
                                        refCriminals.child( user.getId()+ "/imageUrl").setValue(imageUrl);
                                        Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Image Required!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getComplaint() {
        final ListView complaint = findViewById(R.id.listComplaint);
        ArrayList al = new ArrayList();
        final ArrayAdapter ad = new ArrayAdapter(getApplicationContext(),R.layout.info_layout,al);
        complaint.setAdapter(ad);

        refComplaint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot i : snapshot.getChildren()){
                    User user = i.getValue(User.class);
                    ad.add("ID : "+user.getId()+"\nComplaint ID : "+user.getComplaintId()+"\nComplaint : "+user.getUserComplaint()+"\nDate : "+user.getDate()+"\nTime : "+user.getTime());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Can't Load!",Toast.LENGTH_SHORT).show();
            }
        });

        complaint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = complaint.getItemAtPosition(position).toString();
                Intent it = new Intent(ClassifiedActivity2.this,ShowClient.class);
                it.putExtra("clientVal",data);
                startActivity(it);
            }
        });
    }

    private void getSuspicion() {
        final ListView suspicion = findViewById(R.id.listSuspicion);
        ArrayList al = new ArrayList();
        final ArrayAdapter ad = new ArrayAdapter(getApplicationContext(),R.layout.info_layout,al);
        suspicion.setAdapter(ad);

        refSuspicion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot i : snapshot.getChildren()){
                    User user = i.getValue(User.class);
                    ad.add("ID : "+user.getId()+"\nSuspicion ID : "+user.getSuspicionId()+"\nDate : "+user.getDate()+"\nTime : "+user.getTime());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Can't Load!",Toast.LENGTH_SHORT).show();
            }
        });

        suspicion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = suspicion.getItemAtPosition(position).toString();
                Intent it = new Intent(ClassifiedActivity2.this,ShowSuspicion.class);
                it.putExtra("suspicionVal",data);
                startActivity(it);
            }
        });
    }
}