package com.example.hungrystomach;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungrystomach.Adapter.RatingAdapter;
import com.example.hungrystomach.Adapter.RequestAdapter;
import com.example.hungrystomach.Model.Rating;
import com.example.hungrystomach.Model.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


//https://www.codingdemos.com/android-ratingbar-example/
public class Rating_Activity extends AppCompatActivity {

    ArrayList<Rating> list_rating;
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
        list_rating = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("unrate").child(my_uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_rating.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Rating unrate = snapshot.getValue(Rating.class);
                    list_rating.add(unrate);
                    adapter = new RatingAdapter(Rating_Activity.this, list_rating);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
