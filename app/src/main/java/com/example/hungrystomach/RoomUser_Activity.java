package com.example.hungrystomach;

import android.os.Bundle;

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
    private List<User> mUsrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_main);

        rv = (RecyclerView) findViewById(R.id.list_usrRV);
        rv.setHasFixedSize((true));
        rv.setLayoutManager(new LinearLayoutManager(this));

        mUsrs = new ArrayList<>();
        //readUsrs();
    }

    /*
    private void readUsrs(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsrs.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    User usr = snapshot.getValue(User.class);

                    assert usr != null;
                    assert firebaseUser != null;
                    if (!usr.getId().equals(firebaseUser.getUid())){ //getID .equals?
                        mUsrs.add(usr);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
    }
    */
}
