package com.example.hungrystomach;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungrystomach.Adapter.ReceiptAdapter;
import com.example.hungrystomach.Model.Receipt;
import com.example.hungrystomach.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Receipt_Activity extends AppCompatActivity {

    ReceiptAdapter adapter;
    RecyclerView recyclerView;

    List<Receipt> list_receipt;
    FirebaseAuth m_auth;
    DatabaseReference receipt_ref;
    String my_uid;
    String my_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_main);


        my_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //get my name
        DatabaseReference find = FirebaseDatabase.getInstance().getReference().child("users").child(my_uid);
        find.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                my_name = u.getUsername();
                Log.d("recepipt_", "AA" + my_name);
                Toolbar toolbar = findViewById(R.id.receipt_toolbar);
                toolbar.setTitle(my_name + "'s Receipt");
                setSupportActionBar(toolbar);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });







        recyclerView = (RecyclerView) findViewById(R.id.receipt_recycler_view);
        loadReceipt();

    }


    private void loadReceipt(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        list_receipt = new ArrayList<>();


        receipt_ref = FirebaseDatabase.getInstance().getReference("receipt").child(my_uid);
        receipt_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_receipt.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Receipt all_receipt = ds.getValue(Receipt.class);

                    list_receipt.add(all_receipt);
                    Log.d("a_a1", "nothing.." + list_receipt);
                    adapter = new ReceiptAdapter(Receipt_Activity.this, list_receipt);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
