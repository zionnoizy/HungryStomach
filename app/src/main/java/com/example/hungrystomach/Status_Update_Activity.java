package com.example.hungrystomach;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungrystomach.Adapter.SellerFoodListAdapter;
import com.example.hungrystomach.Model.BuyerFoodList;
import com.example.hungrystomach.Model.FCMToken;
import com.example.hungrystomach.Model.Receipt;
import com.example.hungrystomach.Model.UnRating;
import com.example.hungrystomach.Model.SellerFoodList;
import com.example.hungrystomach.Model.User;
import com.example.hungrystomach.Notification.APIService;
import com.example.hungrystomach.Notification.Client;
import com.example.hungrystomach.Notification.Data;
import com.example.hungrystomach.Notification.Response;
import com.example.hungrystomach.Notification.Sender;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.hungrystomach.Adapter.RequestAdapter.EXTRA_BUYER_UID;
import static com.example.hungrystomach.Adapter.RequestAdapter.EXTRA_POSITION;
import static com.example.hungrystomach.Adapter.RequestAdapter.EXTRA_RANDOM_KEY;

public class Status_Update_Activity extends AppCompatActivity {

    String request_num;
    String random_key;
    String buyer_uid;
    String cooker_uid;

    RecyclerView recyclerView;
    DatabaseReference get_foodlist;

    RadioGroup radio_group;
    RadioButton radio_button1, radio_button2, radio_button3;
    Button btn_chat_to;

    boolean notify = false;
    APIService api_service;
    private String FCM_SEND_URL = "https://fcm.googleapis.com/";

    String my_name;
    String buyer_name;
    public static final String EXTRA_HISUID = "NoHisUID";

    ArrayList<SellerFoodList> list_request;
    SellerFoodListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_update);

        //getStringExtra
        request_num = getIntent().getStringExtra(EXTRA_POSITION);
        random_key = getIntent().getStringExtra(EXTRA_RANDOM_KEY);
        buyer_uid = getIntent().getStringExtra(EXTRA_BUYER_UID);
        cooker_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = (RecyclerView) findViewById(R.id.ordered_recycler_view);
        get_foodlist = FirebaseDatabase.getInstance().getReference().child("seller_food_list").child(cooker_uid).child(random_key); //need to be query and limit to

        //get my name
        DatabaseReference find = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = find.child(cooker_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                my_name = u.getUsername();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        //get buyer name
        Query q = find.child(buyer_uid);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                buyer_name = u.getUsername();
                Log.d("su_debug", "z+" + buyer_name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        //Log.d("SU_DEBUG", "z"+request_num+"/"+random_key+"/"+buyer_uid+"/"+cooker_uid+"/"+my_name+"/"+buyer_name);
        radio_group = findViewById(R.id.rg_due_date);
        radio_button1 = findViewById(R.id.cb_1_receive);
        radio_button2 = findViewById(R.id.cb_2_preparing);
        radio_button3 = findViewById(R.id.cb_3_finish);
        btn_chat_to = findViewById(R.id.chat_to);


        radio_group.setOnCheckedChangeListener (new RadioGroup.OnCheckedChangeListener(){
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.cb_1_receive) {
                    for(int i = 0; i < group.getChildCount()-2; i++){
                        ((RadioButton)group.getChildAt(i)).setEnabled(false);
                        String status_change = String.valueOf(radio_button1.getText());
                        int radioID = radio_group.getCheckedRadioButtonId();
                        radio_button2 = findViewById(radioID);
                        CheckButton(radioID, status_change);
                    }
                }
                if (checkedId == R.id.cb_2_preparing) {
                    for(int j = 0; j < group.getChildCount()-1; j++){
                        ((RadioButton)group.getChildAt(j)).setEnabled(false);
                        String status_change = String.valueOf(radio_button2.getText());
                        int radioID = radio_group.getCheckedRadioButtonId();
                        radio_button2 = findViewById(radioID);
                        CheckButton(radioID, status_change);
                    }
                }
                if (checkedId == R.id.cb_3_finish) {
                    for(int k = 0; k < group.getChildCount(); k++){
                        ((RadioButton)group.getChildAt(k)).setEnabled(false);
                        String status_change = String.valueOf(radio_button3.getText());
                        int radioID = radio_group.getCheckedRadioButtonId();
                        radio_button3 = findViewById(radioID);
                        CheckButton(radioID, status_change);
                    }
                }
            }
        });
        loadSellerRequiteFoodList();

        //Chat to Buyer; putExtra status_update
        btn_chat_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Status_Update_Activity.this, RoomChat_Activity.class);
                i.putExtra("FROM_ACTIVITY", "status_update");
                i.putExtra(EXTRA_HISUID, buyer_uid);
                startActivity(i);
            }
        });
    }

    public void CheckButton(final int id, final String status){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Updating your status")
                .setMessage("Sending new notification status to " + buyer_name + "?")
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendUpdateNotification(buyer_uid, my_name, id, status);
                        update_firebase(status);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void sendUpdateNotification(final String buyerUID, final String myname,final int radioID, final String status){
        notify = true;
        api_service = Client.getRetrofit(FCM_SEND_URL).create(APIService.class);
        DatabaseReference find_buyer_fcm = FirebaseDatabase.getInstance().getReference("FCMToken"); //.child(buyerUID)
        Query query = find_buyer_fcm.orderByKey().equalTo(buyerUID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    FCMToken FCMToken = ds.getValue(FCMToken.class);
                    switch(radioID) {
                        case R.id.cb_1_receive:
                            Data data = new Data("Order Status Update", "Your Order from " + myname + " receive your order.", cooker_uid, buyerUID, "RequestNotif");
                            //Log.d("su_debug", "receive");
                            Sender sender = new Sender(data, FCMToken.getFcm_token());
                            api_service.sendNotification(sender)
                                    .enqueue(new Callback<Response>() {
                                        @Override
                                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                            Log.d("SEND_RECEIVE_ORDER?", response + " sent? " + call);
                                        }

                                        @Override
                                        public void onFailure(Call<Response> call, Throwable t) {
                                            Log.d("SEND_RECEIVE_ORDER", t + " not sent? " + call);
                                        }
                                    });

                        case R.id.cb_2_preparing:
                            Data data2 = new Data("Order Status Update", "Your Order from " + myname + " preparing your order.", cooker_uid, buyerUID, "RequestNotif");
                            Sender sender2 = new Sender(data2, FCMToken.getFcm_token());
                            //Log.d("su_debug", "preparing");
                            api_service.sendNotification(sender2)
                                    .enqueue(new Callback<Response>() {
                                        @Override
                                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                            Log.d("Status_Update", response + " sent? " + call);
                                        }
                                        @Override
                                        public void onFailure(Call<Response> call, Throwable t) {
                                            Log.d("Status_Update", t + " not sent? " + call);
                                        }
                                    });

                        case R.id.cb_3_finish:
                            Data data3 = new Data("Order Status Update", "Your Order from " + myname + " finish your order. Please go to Rating Session to rate your favorite food", cooker_uid, buyerUID, "RequestNotif");
                            Sender sender3 = new Sender(data3, FCMToken.getFcm_token());
                            //Log.d("su_debug", "finish");
                            api_service.sendNotification(sender3)
                                    .enqueue(new Callback<Response>() {
                                        @Override
                                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                            Log.d("Status_Update", response + " sent? " + call);
                                        }

                                        @Override
                                        public void onFailure(Call<Response> call, Throwable t) {
                                            Log.d("Status_Update", t + " not sent? " + call);
                                        }
                                    });
                            create_unrating_food();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    void update_firebase(final String status){
        DatabaseReference seller = FirebaseDatabase.getInstance().getReference().child("request").child(cooker_uid);
        seller.child(random_key).child("his_status").setValue(status);
        seller.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Receipt u = dataSnapshot.getValue(Receipt.class);
                u.setHis_status(status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        DatabaseReference buyer = FirebaseDatabase.getInstance().getReference().child("receipt").child(buyer_uid);
        buyer.child(random_key).child("his_status").setValue(status);
        buyer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Receipt u = dataSnapshot.getValue(Receipt.class);
                u.setHis_status(status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void create_unrating_food(){
        final DatabaseReference unrate = FirebaseDatabase.getInstance().getReference().child("unrate").child(buyer_uid);

        Query q1 = FirebaseDatabase.getInstance().getReference("buyer_food_list").child(buyer_uid).child(random_key);
        q1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    BuyerFoodList bfl = d.getValue(BuyerFoodList.class);
                    String name = bfl.getName();
                    String price = bfl.getProduct_price();
                    String url = bfl.getUrl();
                    String random_key = bfl.getRandom_key();
                    UnRating one_unrate = new UnRating(name, url, price, random_key);
                    unrate.child(name).setValue(one_unrate);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    /////////////////////////////////////////////////////////////////////////
    private void loadSellerRequiteFoodList(){
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LayoutManager);
        list_request = new ArrayList<>();

        get_foodlist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_request.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    SellerFoodList all_request = snapshot.getValue(SellerFoodList.class);
                    list_request.add(all_request);
                    adapter = new SellerFoodListAdapter(Status_Update_Activity.this, list_request);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
    /////////////////////////////////////////////////////////////////////////

}
