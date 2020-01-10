package com.example.hungrystomach;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

    EditText display_usrname;
    EditText display_email;
    EditText et_phone;
    EditText et_fullname;
    EditText et_address;
    EditText et_state;
    EditText et_city;
    EditText et_zip;
    TextView uid;
    TextView close;
    ImageView image_icon;

    FirebaseAuth m_auth;
    String myuid = m_auth.getInstance().getCurrentUser().getUid();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
    DatabaseReference usr_uid = ref.child(myuid);

    TextView clickupdate;
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
            image_icon  = findViewById(R.id.setting_icon);

            display_usrname.setText(name);
            display_email.setText(email);
            display_usrname.setKeyListener(null);
            display_email.setKeyListener(null);
            uid.setText(myuid);
        }

        clickupdate = findViewById(R.id.update_info);

        //setText
        Query query = ref.child(myuid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                String icon = u.getIcon();
                if(icon.equals("default_icon"))
                    image_icon.setImageResource(R.drawable.default_icon);

                if(!u.getPhone().isEmpty())
                    et_phone.setText(u.getPhone());
                else
                    et_phone.setText("blank");

                if(!u.getFull_name().isEmpty())
                    et_fullname.setText(u.getFull_name());
                else
                    et_fullname.setText("blank");

                if(!u.getAddress().isEmpty())
                    et_address.setText(u.getAddress());
                else
                    et_address.setText("blank");

                if(!u.getState().isEmpty())
                    et_state.setText(u.getState());
                else
                    et_state.setText("blank");

                if(!u.getCity().isEmpty())
                    et_city.setText(u.getCity());
                else
                    et_city.setText("blank");

                if(!u.getZip().isEmpty())
                    et_zip.setText(u.getZip());
                else
                    et_zip.setText("blank");

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
                            c_state = et_state.getText().toString();
                            c_city = et_city.getText().toString();
                            c_zip = et_zip.getText().toString();
                            c_address = et_address.getText().toString();

                            usr_uid.child("phone").setValue(c_phone);
                            usr_uid.child("address").setValue(c_address);
                            usr_uid.child("state").setValue(c_state);
                            usr_uid.child("city").setValue(c_city);
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