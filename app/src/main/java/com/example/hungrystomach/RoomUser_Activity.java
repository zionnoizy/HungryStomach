package com.example.hungrystomach;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungrystomach.Adapter.UserAdapter;
import com.example.hungrystomach.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomUser_Activity extends AppCompatActivity {
    private RecyclerView rv;
    private UserAdapter usrAdapter;
    private ArrayList<User> mUsrsList;

    public RoomUser_Activity(){
        //empty
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user);

        rv = (RecyclerView) findViewById(R.id.usr_recyclerview);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        mUsrsList = new ArrayList<>();

        readUsrs();
    }

    private void readUsrs(){
        final FirebaseUser me = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsrsList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    User all_usr = snapshot.getValue(User.class);
                    if (!all_usr.getUId().equals(me.getUid())) {
                        mUsrsList.add(all_usr);
                        break;
                    }
                }
                usrAdapter = new UserAdapter(getApplicationContext(), mUsrsList);
                rv.setAdapter(usrAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
