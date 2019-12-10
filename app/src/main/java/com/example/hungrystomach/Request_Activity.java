package com.example.hungrystomach;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungrystomach.Adapter.ReceiptAdapter;
import com.example.hungrystomach.Adapter.RequestAdapter;
import com.example.hungrystomach.Model.Receipt;
import com.example.hungrystomach.Model.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Request_Activity extends AppCompatActivity {

    List<Request> list_request;
    RequestAdapter adapter;
    RecyclerView recyclerView;

    String my_uid;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_main);

        recyclerView = (RecyclerView) findViewById(R.id.request_recycler_view);
        my_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadRequest();
    }

    private void loadRequest(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        list_request = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("request").child(my_uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_request.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Request all_request = snapshot.getValue(Request.class);
                    list_request.add(all_request);
                    adapter = new RequestAdapter(Request_Activity.this, list_request);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
