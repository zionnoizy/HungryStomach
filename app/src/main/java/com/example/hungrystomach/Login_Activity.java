package com.example.hungrystomach;

import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
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

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Activity extends AppCompatActivity {
    FirebaseApp m_app;
    FirebaseDatabase m_database;
    FirebaseAuth m_auth;
    private FirebaseAuth.AuthStateListener m_auth_state_listener;

    EditText et_email, et_password;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_auth = FirebaseAuth.getInstance();

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_register);

        /*
        AuthStateListener = new FirebaseAuth.AuthStateListener(){
            FirebaseUser m_firebaseuser = m_auth_state_listener.getCurrentUser();
            @Override
            protected void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                if(m_firebaseuser != null){
                    Log.d("You Are Logged in");
                    Intent i = new Intent(Login_Activity.this, Home_Activity.class)
                            startActivity(i);
                }
                else{
                    Toast.makeText()
                }
            }

        }
        */

        /*
        m_auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login_Activity.this,)
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                }
                }
        }
        */
    }
}
