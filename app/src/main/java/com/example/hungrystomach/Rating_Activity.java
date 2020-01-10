package com.example.hungrystomach;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungrystomach.Adapter.RatingAdapter;
import com.example.hungrystomach.Model.UnRating;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


//https://www.codingdemos.com/android-ratingbar-example/
public class Rating_Activity extends AppCompatActivity {

    ArrayList<UnRating> list_Un_rating;
    RatingAdapter adapter;
    RecyclerView recyclerView;

    String my_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_main);

        recyclerView = (RecyclerView) findViewById(R.id.rating_recycler_view);
        my_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadUnrate();
    }

    private void loadUnrate(){
        LinearLayoutManager lm = new LinearLayoutManager(Rating_Activity.this);
        recyclerView.setLayoutManager(lm);
        list_Un_rating = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("unrate").child(my_uid);
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_Un_rating.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    UnRating unrate = snapshot.getValue(UnRating.class);
                    list_Un_rating.add(unrate);
                    adapter = new RatingAdapter(Rating_Activity.this, list_Un_rating);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
