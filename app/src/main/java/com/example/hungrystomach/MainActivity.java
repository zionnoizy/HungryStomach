package com.example.hungrystomach;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.hungrystomach.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    private User user;

    FirebaseApp m_app;
    FirebaseDatabase m_database;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseAuth m_auth;

    EditText et_email, et_password;
    Button btn_register, btn_login;


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


        //user = new User();
        init_firebase();


        click_to_register_new_user();

        click_to_login();
        //reset_pwd();


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
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
                        DatabaseReference usr_uid = ref.child(m_auth.getCurrentUser().getUid());
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Registration Successed", Toast.LENGTH_SHORT).show();
                            //User usr = new User(email,username,pwd);
                            usr_uid.child("email").setValue(email);
                            usr_uid.child("username").setValue(username);
                            usr_uid.child("password").setValue(pwd);
                            usr_uid.child("icon").setValue("default_icon");

                        }
                        else
                            Log.e("user_reg_fail", "Field Empty!");
                        }
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
                                Toast.makeText(MainActivity.this, "Login Errror", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Login SUCCESS GOING TO HOME_ACTIVITY", Toast.LENGTH_SHORT).show();
                                Intent refer_to_home = new Intent(MainActivity.this, Home_Activity.class);
                                startActivity(refer_to_home);
                            }
                        }
                    });
                }
            }
        });
    }

    /*
    private void reset_pwd(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.acct_reset_pwd, null); //Error

        String email = et_email.getText().toString().trim();
        String tmp = new String(email);
        String username = tmp.split("@")[0];
        String pwd = et_password.getText().toString().trim();

        dialogBuilder.setView(dialogView);
        final EditText enter_rest_email = (EditText) dialogView.findViewById(R.id.enter_rest_email);
        final Button btn_click_toreset = (Button) dialogView.findViewById(R.id.btn_click_toreset);
        final ProgressBar progressBar1 = (ProgressBar) dialogView.findViewById(R.id.progressBar);


        btn_click_toreset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = enter_rest_email.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter email to reset", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar1.setVisibility(View.VISIBLE);
                m_auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Reset Instruction has been sent", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Failed to reset the email, check your network!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
    */


}
