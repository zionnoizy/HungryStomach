package com.example.hungrystomach;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungrystomach.Adapter.CartAdapter;
import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.Model.ShoppingCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;

public class Cart_Activity extends AppCompatActivity {

    RecyclerView recycler_view;
    RecyclerView.LayoutManager lm;
    private ArrayList<ShoppingCart> m_listCart;
    TextView total_amount, sub_total;
    Button btn_co, btn_delete;
    CartAdapter adapter;

    private DatabaseReference ref2;
    private DatabaseReference get_uid;
    FirebaseAuth m_auth = FirebaseAuth.getInstance();
    private FirebaseDatabase dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_main);

        recycler_view = (RecyclerView) findViewById(R.id.cart_recycler_view);
        lm = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(lm);

        m_listCart = new ArrayList<ShoppingCart>();
        adapter = new CartAdapter(this, m_listCart);
        recycler_view.setAdapter(adapter);

        //firebase
        //request=database.getReference("tmp_checkout");

        total_amount = findViewById(R.id.total_price);
        btn_co = findViewById(R.id.btn_co);
        btn_delete = findViewById(R.id.delete_item);
        sub_total = findViewById(R.id.sub_total);

        ref2 = FirebaseDatabase.getInstance().getReference().child("shopping_cart");
        get_uid = ref2.child(m_auth.getCurrentUser().getUid());
        get_uid.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ShoppingCart data = dataSnapshot.getValue(ShoppingCart.class);

                m_listCart.add(data);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //checkout- btn_co
        btn_co.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Cart_Activity.this,"CheckOut Button",Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(Cart_Activity.this, Checkout1_Activity.class);
                //startActivity(intent);
            }
        });
    }

}
