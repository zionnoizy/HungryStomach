package com.example.hungrystomach;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.hungrystomach.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.example.hungrystomach.Cart_Activity.PASS_TOTAL_AMT;

public class Checkout1_Activity extends AppCompatActivity {
    EditText edit_name, edit_email, edit_phone, edit_address, edit_state, edit_city, edit_zip ;
    Button but_next;

    //get user info
    FirebaseAuth m_auth = FirebaseAuth.getInstance();
    DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("users");
    DatabaseReference usr_uid = cartListRef.child(m_auth.getCurrentUser().getUid());

    //assign string to pass to checkout2
    public static final String PASS_NAME = "NoName";
    public static final String PASS_EMAIL = "NoEmail";
    public static final String PASS_PHONE = "NoPhone";
    public static final String PASS_ADDRESS = "NoAddress";
    public static final String PASS_STATE = "NoState";
    public static final String PASS_CITY = "NoCity";

    //changed info
    String Cname;
    String Cemail;
    String Cphone;
    String Caddress;
    String Cstate;
    String Ccity;
    String Czip;

    //String AMOUNT = getIntent().getStringExtra(PASS_TOTAL_AMT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_info);

        edit_name = findViewById(R.id.edit_name);
        edit_email = findViewById(R.id.edit_email);
        edit_phone = findViewById(R.id.edit_phone);
        edit_address = findViewById(R.id.edit_address);
        edit_state = findViewById(R.id.edit_state);
        edit_city = findViewById(R.id.edit_city);
        edit_zip = findViewById(R.id.edit_zip);
        but_next = findViewById(R.id.but_next);

        //find default info
        usr_uid.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User data = dataSnapshot.getValue(User.class);
                edit_name.setText(data.getUsername());
                edit_email.setText(data.getEmail());
                edit_phone.setText(data.getPhone());
                edit_address.setText(data.getAddress());
                edit_state.setText(data.getState());
                edit_city.setText(data.getCity());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        but_next.setOnClickListener(new View.OnClickListener() { //button null
            @Override
            public void onClick(View v) {
                //changed information
                usr_uid.addListenerForSingleValueEvent((new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            Cphone = edit_phone.getText().toString();
                            usr_uid.child("phone").setValue(Cphone);
                            Caddress = edit_address.getText().toString();
                            usr_uid.child("address").setValue(Caddress);
                            Cstate = edit_state.getText().toString();
                            usr_uid.child("state").setValue(Cstate);
                            Ccity = edit_city.getText().toString();
                            usr_uid.child("city").setValue(Ccity);
                            Czip = edit_zip.getText().toString();
                            usr_uid.child("zip").setValue(Czip);
                            //usr_uid.child("fullName").setValue(c_fullname);
                        } catch (Exception e) {
                            e.printStackTrace();
                            //Log.e("setting: ","Please Check your Network "+ e.getMessage());
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                }));
                //pass all the tmp usr info to checkout2 activity
                Intent intent = new Intent(Checkout1_Activity.this, Checkout2_Activity.class);
                intent.putExtra(PASS_NAME, Cname);
                intent.putExtra(PASS_EMAIL, Cemail);
                intent.putExtra(PASS_PHONE, Cphone);
                intent.putExtra(PASS_ADDRESS, Caddress);
                intent.putExtra(PASS_STATE, Cstate);
                intent.putExtra(PASS_CITY, Ccity);
                //intent.putExtra(PASS_TOTAL_AMT, AMOUNT);
                startActivity(intent);
            }
        });
    }




}
