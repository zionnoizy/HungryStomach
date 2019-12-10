package com.example.hungrystomach;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungrystomach.Adapter.BuyerFoodListAdapter;
import com.example.hungrystomach.Adapter.ReceiptAdapter;
import com.example.hungrystomach.Adapter.RequestAdapter;
import com.example.hungrystomach.Model.BuyerFoodList;
import com.example.hungrystomach.Model.Receipt;
import com.example.hungrystomach.Model.Request;
import com.example.hungrystomach.Model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.hungrystomach.Adapter.ReceiptAdapter.EXTRA_BILLDATE;
import static com.example.hungrystomach.Adapter.ReceiptAdapter.EXTRA_COOKER_UID;
import static com.example.hungrystomach.Adapter.ReceiptAdapter.EXTRA_RECEIPT_KEY;
import static com.example.hungrystomach.Adapter.ReceiptAdapter.EXTRA_STATUS;

public class Track_Order_Activity extends AppCompatActivity {

    String cooker_uid;
    String cur_status;
    String billdate;
    String transaction_key;

    TextView cookername;
    TextView cookerplace;
    TextView orderstatus;
    TextView orderdate;

    String cooker_fulladdr_text;
    String my_uid;
    public static final String EXTRA_COOKER_UID2 = "NoCookerUID";
    ArrayList<BuyerFoodList> list_request;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    DatabaseReference ref;
    BuyerFoodListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_order);

        cooker_uid = getIntent().getStringExtra(EXTRA_COOKER_UID);
        cur_status = getIntent().getStringExtra(EXTRA_STATUS);
        billdate = getIntent().getStringExtra(EXTRA_BILLDATE);
        transaction_key = getIntent().getStringExtra(EXTRA_RECEIPT_KEY);

        cookername = findViewById(R.id.order_name);
        cookername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Track_Order_Activity.this, RoomChat_Activity.class);
                i.putExtra("FROM_ACTIVITY", "track_order");
                i.putExtra(EXTRA_COOKER_UID2, cooker_uid);
                startActivity(i);
            }
        });
        cookerplace = findViewById(R.id.order_place);
        orderstatus = findViewById(R.id.order_status);
        orderdate = findViewById(R.id.order_date);

        my_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        get_cooker_info();

        recyclerView = (RecyclerView) findViewById(R.id.receipt_recycler_view);

        loadBuyerReceiptFoodList();
    }

    void get_cooker_info(){
        DatabaseReference cooker_info = FirebaseDatabase.getInstance().getReference("users").child(cooker_uid);
        cooker_info.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User u = dataSnapshot.getValue(User.class);
                    String cooker_name = u.getUsername();
                    String cooker_phone = u.getPhone();
                    String cooker_address = u.getAddress();
                    String cooker_city = u.getCity();
                    String cooker_state = u.getState();
                    String cooker_zip = u.getZip();
                    cooker_fulladdr_text = getString(R.string.transaction_address, cooker_address, cooker_city, cooker_state, cooker_zip);
                    Log.d("to_debug", cooker_fulladdr_text);

                    SpannableString content = new SpannableString(cooker_name);
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    cookername.setText(content);
                    cookerplace.setText(cooker_fulladdr_text);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        DatabaseReference rest_info = FirebaseDatabase.getInstance().getReference("receipt").child(my_uid).child(transaction_key); //
        rest_info.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //String name = ds.getKey();
                    //Log.d("TO_Debug", name); //billDate
                    Receipt r = dataSnapshot.getValue(Receipt.class); //string to receipt
                    String his_status = r.getHis_status();
                    String bill_date = r.getBillDate();
                    orderstatus.setText(his_status);
                    orderdate.setText(bill_date);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void loadBuyerReceiptFoodList(){
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LayoutManager);
        list_request = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("buyer_food_list").child(my_uid).child(transaction_key);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_request.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    BuyerFoodList all_request = snapshot.getValue(BuyerFoodList.class);
                    list_request.add(all_request);
                    adapter = new BuyerFoodListAdapter(Track_Order_Activity.this, list_request);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

}
