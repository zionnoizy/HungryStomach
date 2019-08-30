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
import android.widget.EditText;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Log.e(TAG,"debuging msg.");

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        m_displaytext = (TextView)findViewById(R.id.tv_already_register);
        m_displaytext.setText("Unknown Auth State.");

        init_firebase();

        //read_database_data();
        //writeDatabaseData();
        //readObjects();
        //authentication();

        register_new_user();
        logout();
    }

    private void init_firebase(){
        m_app = FirebaseApp.getInstance();
        m_database = FirebaseDatabase.getInstance(m_app);
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

    private void register_new_user(){

        /*
        OnCompleteListener<AuthResult> success = new OnCompleteListener <AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful())
                    Log.e(TAG,"User Registration Successful");
                else
                    Log.e(TAG, "User Register Failed");
            }
        }
        */

        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if(email.isEmpty()){
            et_email.setError("Email is required");
            et_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Entering Valid Email is required");
        }
        if(password.isEmpty()){
            et_password.setError("Password is required");
            et_password.requestFocus();
            return;
        }
        if(password.length()<3){
            et_password.setError("Password Length Should More Than 3");
            et_password.requestFocus();
            return;
        }

        m_auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    Log.e(TAG,"User Registration Successful");
                else
                    Log.e(TAG, "User Register Failed");

            }
        });
    }

    private void logout(){
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
