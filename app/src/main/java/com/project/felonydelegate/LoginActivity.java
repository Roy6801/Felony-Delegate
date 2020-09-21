package com.project.felonydelegate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor ed ;
    Button getLocation,userRegister;
    EditText fName,mName=null,lName=null,userEmail,phoneNo,phoneCode,userPwd,re_enterPwd;
    TextView userWarning;
    ScrollView scrollScreen;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference authRef = firebaseDatabase.getReference("Classified");
    private DatabaseReference ref = firebaseDatabase.getReference("Client");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Intent it;

    PhoneCode pCode = new PhoneCode();
    User user = new User();
    GetLocation userLocation = new GetLocation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp  = getSharedPreferences("info",MODE_PRIVATE);
        ed = sp.edit();

        getLocation = findViewById(R.id.getLocation);
        userRegister = findViewById(R.id.userRegister);

        fName = findViewById(R.id.fName);
        mName = findViewById(R.id.mName);
        lName = findViewById(R.id.lName);
        userEmail = findViewById(R.id.userEmail);
        userPwd = findViewById(R.id.userPwd);
        re_enterPwd = findViewById(R.id.re_enterPwd);
        phoneCode = findViewById(R.id.phoneCode);
        phoneNo = findViewById(R.id.phoneNo);

        userWarning = findViewById(R.id.userWarning);
        scrollScreen = findViewById(R.id.scrollScreen);

        userLocation.userLocation(this);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userWarning.setVisibility(View.GONE);
                phoneCode.setText("+"+pCode.getCode(sp.getString("CountryCode","")));

                if(phoneCode.getText().toString().equals("+null")){
                    Intent it = new Intent(LoginActivity.this,LoginActivity.class);
                    startActivity(it);
                }
                else{
                    ed.putString("PhoneCode",phoneCode.getText().toString());
                    ed.apply();
                    getLocation.setVisibility(View.GONE);
                    userRegister.setVisibility(View.VISIBLE);
                    scrollScreen.setVisibility(View.VISIBLE);
                }
            }
        });

        userRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fName.getText().toString().isEmpty() || phoneNo.getText().toString().isEmpty() || phoneCode.getText().toString().isEmpty()
                || userEmail.getText().toString().isEmpty() || userPwd.getText().toString().isEmpty()){
                    if(fName.getText().toString().isEmpty()){
                        fName.setError("First Name Required!!");
                    }
                    if(phoneNo.getText().toString().isEmpty()){
                        phoneNo.setError("Phone Number Required!!");
                    }
                    if(phoneCode.getText().toString().isEmpty()){
                        phoneCode.setError("Phone Code Required!!");
                    }
                    if(userEmail.getText().toString().isEmpty()){
                        userEmail.setError("Email cannot be Empty!!");
                    }
                    if(userPwd.getText().toString().isEmpty()){
                        userPwd.setError("Password cannot be Empty!!");
                    }
                }
                else{
                    if(userPwd.getText().toString().equals(re_enterPwd.getText().toString())) {
                        mAuth.createUserWithEmailAndPassword(userEmail.getText().toString(),userPwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                user.setUserName(fName.getText().toString(),mName.getText().toString(),lName.getText().toString());
                                user.setUserEmail(userEmail.getText().toString());
                                user.setUserPhone(phoneCode.getText().toString(),phoneNo.getText().toString());
                                user.setId(userEmail.getText().toString());
                                user.setUserLat(sp.getString("Lat",""));
                                user.setUserLng(sp.getString("Lng",""));
                                user.setUserAddress(sp.getString("Address",""));
                                user.setUserLocality(sp.getString("Locality",""));
                                user.setUserCountryName(sp.getString("CountryName",""));

                                if(task.isSuccessful()){
                                    ref.child(user.getId()).setValue(user);
                                    Toast.makeText(getApplicationContext(),"Registration Success",Toast.LENGTH_SHORT).show();
                                }
                                userLogin();
                            }
                        });
                    }
                    else{
                        userPwd.setError("Password doesn't match");
                        re_enterPwd.setError("Password doesn't match");
                    }
                    }
            }
        });
    }

    private void userLogin(){
        mAuth.signInWithEmailAndPassword(userEmail.getText().toString(),userPwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    ed.putString("Name", fName.getText().toString() + " " + mName.getText().toString() + " " + lName.getText().toString());
                    ed.putString("Email", userEmail.getText().toString());
                    ed.putString("Pwd", userPwd.getText().toString());
                    ed.putString("PhoneNo", phoneNo.getText().toString());
                    ed.putString("Type","Client");
                    ed.apply();

                    final String uid = user.getId();
                    it = new Intent(LoginActivity.this,MainActivity.class);

                    authRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot i : dataSnapshot.getChildren()) {
                                User user = i.getValue(User.class);
                                    if (user.getId().equals(uid)) {
                                        ed.putString("Type","Classified");
                                        ed.apply();
                                        it = new Intent(LoginActivity.this, ClassifiedActivity.class);
                                    }
                            }
                            startActivity(it);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}