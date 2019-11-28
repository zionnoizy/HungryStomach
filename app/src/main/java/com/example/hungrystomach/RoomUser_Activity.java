package com.example.hungrystomach;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
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

import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_URL;

public class RoomUser_Activity extends AppCompatActivity {
    RecyclerView rv;
    UserAdapter usrAdapter;
    ArrayList<User> mUsrsList;

    public RoomUser_Activity(){
        //empty
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user);

        rv = (RecyclerView) findViewById(R.id.usr_recyclerview);

        readUsrs();
    }

    private void readUsrs(){
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(LayoutManager);
        mUsrsList = new ArrayList<>();

        final FirebaseUser me = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsrsList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    User all_usr = ds.getValue(User.class);
                    //Log.d("ra_debug", all_usr.getUid() +" "+ me.getUid());
                    if (!all_usr.getUid().equals(me.getUid())) {
                        mUsrsList.add(all_usr);
                    }
                    usrAdapter = new UserAdapter(RoomUser_Activity.this, mUsrsList);
                    rv.setAdapter(usrAdapter);
                    usrAdapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
