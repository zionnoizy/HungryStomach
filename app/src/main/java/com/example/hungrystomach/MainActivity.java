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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
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

    TextView m_displaytext;
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

        m_displaytext = (TextView)findViewById(R.id.tv_already_register);
        m_displaytext.setText("Unknown Auth State.");

        user = new User();
        init_firebase();

        click_to_register_new_user();
        //click_to_login();
        click_to_logout();
    }

    private void init_firebase(){
        m_app = FirebaseApp.getInstance();
        m_database = FirebaseDatabase.getInstance(m_app);
        m_auth = FirebaseAuth.getInstance();
    }

    private void click_to_register_new_user(){

        btn_register.setOnClickListener(new View.OnClickListener() { //ref


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");

            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String pwd = et_password.getText().toString().trim();
                User usr = new User(pwd,email);
                ref.child("usersss").setValue(usr);

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
                    return;
                } else {
                    m_auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Registration Successed", Toast.LENGTH_SHORT).show();
                                Log.e("user_reg", "User Registration Successful");

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


    private void click_to_logout(){
        m_auth.signOut();
    }


}
