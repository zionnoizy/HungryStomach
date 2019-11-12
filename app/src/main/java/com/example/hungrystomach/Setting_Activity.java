package com.example.hungrystomach;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungrystomach.Model.User;
import com.example.hungrystomach.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Setting_Activity extends AppCompatActivity {
    FirebaseAuth m_auth;
    String myuid = m_auth.getInstance().getCurrentUser().getUid();
    FirebaseUser cur_usrname = m_auth.getInstance().getCurrentUser();

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
    DatabaseReference usr_uid = ref.child(myuid);

    EditText et_phone;
    EditText et_fullname;
    EditText et_address;
    EditText et_state;
    EditText et_city;
    EditText et_zip;
    TextView clickupdate;
    EditText display_usrname;
    EditText display_email;
    TextView uid;
    TextView close;

    String c_phone;
    String c_fullname;
    String c_address;
    String c_state;
    String c_city;
    String c_zip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();

            display_usrname = findViewById(R.id.display_username);
            display_email = findViewById(R.id.display_email);
            et_phone = findViewById(R.id.phone_num);
            et_fullname = findViewById(R.id.full_name);
            et_address = findViewById(R.id.edit_address);
            et_state = findViewById(R.id.edit_state);
            et_city = findViewById(R.id.edit_city);
            et_zip  = findViewById(R.id.edit_zip);
            uid  = findViewById(R.id.id_num);
            close  = findViewById(R.id.close);

            display_usrname.setText(name);
            display_email.setText(email);
            uid.setText(myuid);
        }

        clickupdate = findViewById(R.id.update_info);

        //set info
        Query query = ref.child(myuid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                String usr_phone = u.getPhone();
                if(!u.getPhone().isEmpty())
                    et_phone.setText(usr_phone);
                else
                    et_phone.setText("not_provide,pls_state_bf_checkout");

                if(!u.getAddress().isEmpty())
                    et_address.setText(u.getAddress());
                else
                    et_address.setText("not_provide,pls_state_bf_checkout");

                if(!u.getState().isEmpty())
                    et_state.setText(u.getState());
                else
                    et_state.setText("not_provide,pls_state_bf_checkout");

                if(!u.getCity().isEmpty())
                    et_city.setText(u.getCity());
                else
                    et_city.setText("not_provide,pls_state_bf_checkout");

                if(!u.getZip().isEmpty())
                    et_zip.setText(u.getZip());
                else
                    et_zip.setText("not_provide,pls_state_bf_checkout");

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //https://firebase.google.com/docs/auth/android/manage-users#update_a_users_profile
        clickupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewIn) {
                Toast.makeText(Setting_Activity.this,"Info Updated", Toast.LENGTH_SHORT).show();
                usr_uid.addListenerForSingleValueEvent((new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            c_phone = et_phone.getText().toString();
                            usr_uid.child("phone").setValue(c_phone);
                            c_address = et_address.getText().toString();
                            usr_uid.child("address").setValue(c_address);
                            c_state = et_state.getText().toString();
                            usr_uid.child("state").setValue(c_state);
                            c_city = et_city.getText().toString();
                            usr_uid.child("city").setValue(c_city);
                            c_zip = et_zip.getText().toString();
                            usr_uid.child("zip").setValue(c_zip);

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
            }
        });


        //when click close btn
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect_home();
            }
        });
    }

    private void redirect_home() {
        Intent i = new Intent(Setting_Activity.this,Home_Activity.class);
        startActivity(i);
        finish();
    }
}