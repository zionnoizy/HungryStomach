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


import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.hungrystomach.Cart_Activity.PASS_TOTAL_AMT;
import static com.example.hungrystomach.Cart_Activity.PASS_UPLAODER_UID;

public class Checkout1_Activity extends AppCompatActivity {
    EditText edit_name, edit_email, edit_phone, edit_address, edit_state, edit_city, edit_zip ;
    Button but_next;


    FirebaseAuth m_auth = FirebaseAuth.getInstance();
    String UID = m_auth.getUid();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
    DatabaseReference uid = ref.child(UID);

    //assign string to pass to checkout2
    public static final String PASS_NAME = "NoName";
    public static final String PASS_EMAIL = "NoEmail";
    public static final String PASS_PHONE = "NoPhone";
    public static final String PASS_ADDRESS = "NoAddress";
    public static final String PASS_STATE = "NoState";
    public static final String PASS_CITY = "NoCity";
    public static final String PASS_ZIP = "NoZip";

    //changed info
    String Cname;
    String Cemail;
    String Cphone;
    String Caddress;
    String Cstate;
    String Ccity;
    String Czip;
    String grandT;
    String uploader_uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_info);

        grandT = getIntent().getStringExtra(PASS_TOTAL_AMT);
        uploader_uid = getIntent().getStringExtra(PASS_UPLAODER_UID);
        edit_name = findViewById(R.id.edit_name);
        edit_email = findViewById(R.id.edit_email);
        edit_phone = findViewById(R.id.edit_phone);
        edit_address = findViewById(R.id.edit_address);
        edit_state = findViewById(R.id.edit_state);
        edit_city = findViewById(R.id.edit_city);
        edit_zip = findViewById(R.id.edit_zip);
        but_next = findViewById(R.id.but_next);

        Query q = ref.child(UID);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                edit_name.setText(u.getUsername());
                edit_email.setText(u.getEmail());
                edit_phone.setText(u.getPhone());
                edit_address.setText(u.getAddress());
                edit_state.setText(u.getState());
                edit_city.setText(u.getCity());
                edit_zip.setText(u.getZip());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        but_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Checkout1_Activity.this,"Moving to Payment Method.", Toast.LENGTH_SHORT).show();
                uid.addListenerForSingleValueEvent((new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            Cphone = edit_phone.getText().toString();
                            Caddress = edit_address.getText().toString();
                            Cstate = edit_state.getText().toString();
                            Ccity = edit_city.getText().toString();
                            Czip = edit_zip.getText().toString();

                            uid.child("phone").setValue(Cphone);
                            uid.child("address").setValue(Caddress);
                            uid.child("state").setValue(Cstate);
                            uid.child("city").setValue(Ccity);
                            uid.child("zip").setValue(Czip);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                }));
                Intent intent = new Intent(Checkout1_Activity.this, Checkout2_Activity.class);
                intent.putExtra(PASS_NAME, Cname);
                intent.putExtra(PASS_EMAIL, Cemail);
                intent.putExtra(PASS_PHONE, Cphone);
                intent.putExtra(PASS_ADDRESS, Caddress);
                intent.putExtra(PASS_STATE, Cstate);
                intent.putExtra(PASS_CITY, Ccity);
                intent.putExtra(PASS_TOTAL_AMT, grandT);
                intent.putExtra(PASS_UPLAODER_UID, uploader_uid);
                intent.putExtra(PASS_ZIP, Czip);
                startActivity(intent);
            }
        });
    }




}
