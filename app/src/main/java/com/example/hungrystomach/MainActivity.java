package com.example.hungrystomach;

import android.media.MediaPlayer;
import android.os.Bundle;

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
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    String TAG = "fb_debug";

    FirebaseApp m_app;
    FirebaseDatabase m_database;
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

        //Log.e(TAG,"debuging msg.");

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);

        m_displaytext = (TextView)findViewById(R.id.tv_already_register);
        m_displaytext.setText("Unknown Auth State.");

        init_firebase();
        //read_database_data();
        //writeDatabaseData();
        //readObjects();
        //authentication();

        click_to_register_new_user();
        //click_to_login();
        click_to_logout();
    }

    private void init_firebase(){
        m_app = FirebaseApp.getInstance();
        m_database = FirebaseDatabase.getInstance(m_app);
        m_auth = FirebaseAuth.getInstance();
    }

    /*
    private void read_database_data(){
        DatabaseReference ref = m_database.getReference("chat_msg");

        ValueEventListener eventListener = new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                Log.e(TAG,"Snapshot received" + dataSnapshot.getChildrenCount()
                      + "key :" + dataSnapshot.getKey().toString() + "value :" + dataSnapshot.getValue().toString());
            }

            @Override
            public void OnCancelled(DatabaseError databaseError){
            }
        };
        ref.addValueEventListener(eventListener);
    }
    */

    /*
    private void write_object(){
        ChatMessage msg = new CharMessage();
        msg.sender = "Sophy";
        msg.sentTime = "19:18:21 08-29-2019";
        msg.CharMessage = "Now is the time";

        DatabaseReference ref = m_database.getReference("chat_msg").child("Sophy 19:18:21 08-29-2019");
        ref.setValue(msg);
    }
    */

    private void click_to_register_new_user(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String email = et_email.getText().toString().trim();
                String pwd = et_password.getText().toString().trim();
                if(email.isEmpty() || pwd.isEmpty()){
                    et_email.setError("Entering Email Password Is required");
                    et_email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    et_email.setError("Entering Valid Email is required");
                }
                if(pwd.length()<3) {
                    et_password.setError("Password Length Should More Than 3");
                    et_password.requestFocus();
                    return;
                }
                else{
                    m_auth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete (@NonNull Task < AuthResult > task) {
                            if (task.isSuccessful())
                                Log.e(TAG, "User Registration Successful");
                            else
                                Log.e(TAG, "User Register Failed");
                        }
                    });
                }
            }
            /*
            OnFailureListener fail = new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e){
                    Log.e(TAG,"Registration Call Failed")
                }
            }
            */
        });
    }

    /*
    private void click_to_login(){
        AuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            protected void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser m_firebase_user = m_auth.getCurrentUser();
                if(m_firebase_user != null){
                    Log.e(TAG,"You Are Logged in");
                    Intent i = new Intent(MainActivity.this, Home_Activity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(TAG,"Login First");
                }
            }
        };

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String email = et_email.getText().toString();
                String pwd = et_password.getText().toString();
                if(email.isEmpty() && pwd.isEmpty()){
                    et_email.setError("Entering Email Password Is required");
                    et_email.requestFocus();
                }
                else if (!(email.isEmpty() && pwd.isEmpty())){
                    m_auth.signInWithEmailAndPassword((email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Home_Activity.this,"Login Errror", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent refer_to_home = new Intent(MainActivity.this, Home_Activity.class);
                                startActivity(refer_to_home);
                        }
                    }
                }))
            }
        });
    }
    */

    private void click_to_logout(){
        m_auth.signOut();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
