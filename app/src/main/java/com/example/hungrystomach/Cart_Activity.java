package com.example.hungrystomach;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Cart_Activity extends AppCompatActivity {

    RecyclerView recycler_view;
    RecyclerView.LayoutManager lm;
    private ArrayList<ShoppingCart> m_listCart;
    TextView total_amount, sub_total;
    Button btn_chceckout, btn_delete;
    CartAdapter adapter;

    private DatabaseReference ref2;
    private DatabaseReference get_uid;
    FirebaseAuth m_auth = FirebaseAuth.getInstance();
    private double grandT = 0.0;

    String FIND_UPLOADER_UID;
    public static final String PASS_TOTAL_AMT = "NoPrice";
    public static final String PASS_UPLAODER_UID = "NoUploaderUid";

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


        btn_chceckout = findViewById(R.id.btn_co);
        btn_delete = findViewById(R.id.delete_item);
        sub_total = findViewById(R.id.sub_total);
        total_amount = findViewById(R.id.total_price);

        ref2 = FirebaseDatabase.getInstance().getReference().child("shopping_cart");
        get_uid = ref2.child(m_auth.getCurrentUser().getUid());
        get_uid.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ShoppingCart data = dataSnapshot.getValue(ShoppingCart.class);
                m_listCart.add(data);
                adapter.notifyDataSetChanged();
                grandT += Double.parseDouble(data.getProduct_price())*data.getQuantity();
                total_amount.setText(new DecimalFormat("#.##").format(grandT));
                FIND_UPLOADER_UID = data.getUploader_uid();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //change quantity
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //deleted item
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        btn_chceckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(grandT == 0.0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Cart_Activity.this);
                    builder.setTitle("Shopping Cart")
                            .setMessage("You cart is empty!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    Intent intent = new Intent(Cart_Activity.this, Checkout1_Activity.class);
                    intent.putExtra(PASS_TOTAL_AMT, String.valueOf(grandT));
                    intent.putExtra(PASS_UPLAODER_UID, FIND_UPLOADER_UID);
                    startActivity(intent);
                }
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode) {



    }

}
