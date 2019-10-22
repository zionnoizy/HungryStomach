package com.example.hungrystomach;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hungrystomach.Model.User;
import com.example.hungrystomach.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Setting_Activity extends AppCompatActivity {
    FirebaseAuth m_auth;
    String cur_uid = m_auth.getInstance().getCurrentUser().getUid();
    FirebaseUser cur_usrname = m_auth.getInstance().getCurrentUser();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
    DatabaseReference usr_uid = ref.child(cur_uid);

    EditText et_phone;
    EditText et_fullname;
    EditText et_address;
    EditText et_state;
    EditText et_city;
    EditText et_zip;
    TextView clickupdate;
    EditText display_usrname;
    EditText display_email;
    User usr;

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

        //set usrname n email
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            display_usrname = findViewById(R.id.display_username);
            display_email = findViewById(R.id.display_email);
            display_usrname.setText(name);
            display_email.setText(email);
        }



        clickupdate = findViewById(R.id.update_info);
        usr_uid.addListenerForSingleValueEvent(new ValueEventListener() { //.orderByChild(cur_uid)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    String displayName = cur_usrname.getDisplayName();

                    et_phone = findViewById(R.id.phone_num); //null
                    et_fullname = findViewById(R.id.full_name);
                    et_address = findViewById(R.id.edit_address);
                    et_state = findViewById(R.id.edit_state);
                    et_city = findViewById(R.id.edit_city);
                    et_zip  = findViewById(R.id.edit_zip);


                    String phone =datas.child("phone").getValue().toString(); //null
                    //String full_name =datas.child("fullName").getValue().toString();
                    String address =datas.child("address").getValue().toString();
                    String state =datas.child("state").getValue().toString();
                    String city =datas.child("city").getValue().toString();
                    String zip =datas.child("zip").getValue().toString();

                    et_phone.setText(phone);
                    //full_name.setText(usr.getName());
                    et_address.setText(address);
                    et_state.setText(state);
                    et_city.setText(city);
                    et_zip.setText(zip);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        /*
        c_phone = et_phone.getText().toString(); //null
        c_fullname = et_fullname.getText().toString();
        c_address = et_address.getText().toString();
        c_state = et_state.getText().toString();
        c_city = et_city.getText().toString();
        c_zip = et_zip.getText().toString();
        */

        clickupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewIn) {
                try {
                    usr_uid.child("phone").setValue(c_phone);
                    //usr_uid.child("fullName").setValue(c_fullname);
                    usr_uid.child("address").setValue(c_address);
                    usr_uid.child("state").setValue(c_state);
                    usr_uid.child("city").setValue(c_city);
                    usr_uid.child("zip").setValue(c_zip);
                } catch (Exception except) {
                    Log.e("setting: ","Please Check your Network "+except.getMessage());
                }
            }
        });
    }


}