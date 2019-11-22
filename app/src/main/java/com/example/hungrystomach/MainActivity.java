package com.example.hungrystomach;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.hungrystomach.Model.FCMToken;
import com.example.hungrystomach.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;


public class MainActivity extends AppCompatActivity {

    FirebaseApp m_app;
    FirebaseDatabase m_database;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseAuth m_auth;

    EditText et_email, et_password ;
    Button btn_register, btn_login;
    TextView forget_password;
    EditText enter_rest_email;

    //default info to register usr
    String defaultIcon =  "default_icon";
    String defaultPhone = "blank";
    String defaultAddress = "blank";
    String defaultState = "blank";
    String defaultCity = "blank";
    String defaultZip = "blank";
    String defaultFullName = "blank";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);
        forget_password = findViewById(R.id.forgetpassword);

        init_firebase();
        click_to_register_new_user();
        click_to_login();


        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_pwd();
            }
        });


    }

    private void init_firebase(){
        m_app = FirebaseApp.getInstance();
        m_database = FirebaseDatabase.getInstance(m_app);
        m_auth = FirebaseAuth.getInstance();
    }

    private void click_to_register_new_user(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = et_email.getText().toString().trim();
                final String username = email.split("@")[0];
                final String pwd = et_password.getText().toString().trim();
                if (email.isEmpty() || pwd.isEmpty()) {
                    et_email.setError("Entering Email Password Is required");
                    et_email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    et_email.setError("Entering Valid Email is required");
                }
                if (pwd.length() < 3) {
                    et_password.setError("Password Length Should More Than 3");
                    et_password.requestFocus();
                } else {
                    m_auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //set getDisplayName
                            FirebaseUser user = m_auth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Registration successed, redirect to home", Toast.LENGTH_SHORT).show();
                                        String uid = m_auth.getCurrentUser().getUid();
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
                                        DatabaseReference id = ref.child(m_auth.getCurrentUser().getUid());

                                        User usr = new User(email,username, defaultIcon, defaultPhone, defaultAddress, defaultState, defaultCity, defaultZip, uid, defaultFullName, 0);
                                        id.setValue(usr);
                                    }
                                }
                            });


                            Intent refer_to_home = new Intent(MainActivity.this, Home_Activity.class);
                            GenerateToken();
                            startActivity(refer_to_home);
                            finish();
                        }
                        //else
                            //Toast.makeText(MainActivity.this,"Field Empty!",Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }


    private void click_to_login() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser m_firebase_user = m_auth.getCurrentUser();
                if (m_firebase_user != null) {
                    Intent i = new Intent(MainActivity.this, Home_Activity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String pwd = et_password.getText().toString();
                if (email.isEmpty() && pwd.isEmpty()) {
                    et_email.setError("Entering Email Password Is required");
                    et_email.requestFocus();
                } else {
                    m_auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Check Your Network Connection", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(MainActivity.this, "Welcome " + m_auth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                                Intent refer_to_home = new Intent(MainActivity.this, Home_Activity.class);
                                GenerateToken();
                                fix_null_info();
                                startActivity(refer_to_home);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }


    private void reset_pwd(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View v = getLayoutInflater().inflate(R.layout.alert_reset_pwd, null);
        enter_rest_email = (EditText) v.findViewById(R.id.enter_rest_email);

        mBuilder.setMessage("Reset Password")
                .setView(v)
                .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = enter_rest_email.getText().toString();
                        if (email.isEmpty()) {
                            enter_rest_email.setError("Entering Email Password Is required");
                            enter_rest_email.requestFocus();
                        } else {

                            m_auth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                                Toast.makeText(MainActivity.this, "Reset Instruction Has Been Sent to Your Email", Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(MainActivity.this, "Your account is not associated with the system", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .setCancelable(false);
        AlertDialog alert = mBuilder.create();
        alert.show();
    }

    public void GenerateToken(){
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = m_auth.getCurrentUser().getUid();

        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            String fcm_token = FirebaseInstanceId.getInstance().getToken();
                            String uid = m_auth.getCurrentUser().getUid();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("FCMToken");
                            DatabaseReference id = ref.child(uid);

                            FCMToken FCMToken = new FCMToken(fcm_token);
                            id.setValue(FCMToken);

                        } else {
                            Log.e("Main_Err", "token" , task.getException());
                        }
                    }
                });
    }

    public void fix_null_info(){
        String id = m_auth.getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                if(u.getFull_name() == null) {
                    u.setFull_name("empty");
                    String id = m_auth.getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(id);
                    ref.child("full_name").setValue("empty");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
