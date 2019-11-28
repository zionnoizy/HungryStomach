package com.example.hungrystomach;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungrystomach.Model.FCMToken;
import com.example.hungrystomach.Model.Rating;
import com.example.hungrystomach.Model.Receipt;
import com.example.hungrystomach.Model.ShoppingCart;
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

import static com.example.hungrystomach.Adapter.RequestAdapter.EXTRA_POSITION;
import static com.example.hungrystomach.Adapter.RequestAdapter.EXTRA_RANDOM_KEY;

public class Status_Update_Activity extends AppCompatActivity {

    RadioGroup radio_group;
    RadioButton radio_button1, radio_button2, radio_button3;

    boolean notify = false;
    APIService api_service;
    private String FCM_SEND_URL = "https://fcm.googleapis.com/";

    String my_uid;
    String his_uid;
    String buyer_name;
    String my_name;
    String request_num;
    String random_key;
    List<ShoppingCart> food_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_update);

        request_num = getIntent().getStringExtra(EXTRA_POSITION);
        random_key = getIntent().getStringExtra(EXTRA_RANDOM_KEY);
        my_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        my_name = get_myname();
        buyer_name = get_hisname();

        radio_group = findViewById(R.id.selectradiogroup);
        radio_button1 = findViewById(R.id.cb_1_receive);
        radio_button2 = findViewById(R.id.cb_2_preparing);
        radio_button3 = findViewById(R.id.cb_3_finish);

        radio_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckButton1(view);
            }
        });
        radio_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckButton1(view);
            }
        });
        radio_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckButton1(view);
            }
        });
    }

    public void CheckButton1(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Updating your status")
                .setMessage("Sending notification status to " + buyer_name)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int radioID = radio_group.getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton)
                        radio_group.findViewById(radioID);

                        String selectedtext = (String) radioButton.getText();
                        sendUpdateNotification1(his_uid, my_name, radioID, selectedtext);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void sendUpdateNotification1(final String buyerUID, final String myname,final int radioID, final String status){
        Log.d("su_debug", "z-" + status);
        notify = true;
        api_service = Client.getRetrofit(FCM_SEND_URL).create(APIService.class);
        DatabaseReference find_buyer_fcm = FirebaseDatabase.getInstance().getReference("FCMToken"); //.child(buyerUID)
        Query query = find_buyer_fcm.orderByKey().equalTo(buyerUID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    FCMToken FCMToken = ds.getValue(FCMToken.class);
                    if(radioID == 1) {
                        Data data = new Data("Order Status Update", "Your Order from " + myname + " receive your order.", my_uid, buyerUID, "RequestNotif");
                        //updateToken(FCMToken.getFcm_token());
                        Sender sender = new Sender(data, FCMToken.getFcm_token());
                        api_service.sendNotification(sender)
                                .enqueue(new Callback<Response>() {
                                    @Override
                                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                        Log.d("Status_Update", response + " sent? "+ call);
                                    }
                                    @Override
                                    public void onFailure(Call<Response> call, Throwable t) {
                                        Log.d("Status_Update", t + " not sent? "+ call);
                                    }
                                });
                        update_firebase(status);
                    }else if(radioID == 2){
                        Data data = new Data("Order Status Update", "Your Order from " + myname + " preparing your order.", my_uid, buyerUID, "RequestNotif");
                        Sender sender = new Sender(data, FCMToken.getFcm_token());
                        api_service.sendNotification(sender)
                                .enqueue(new Callback<Response>() {
                                    @Override
                                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                        Log.d("Status_Update", response + " sent? "+ call);
                                    }
                                    @Override
                                    public void onFailure(Call<Response> call, Throwable t) {
                                        Log.d("Status_Update", t + " not sent? "+ call);
                                    }
                                });
                        update_firebase(status);
                    }else if(radioID == 3){
                        Data data = new Data("Order Status Update", "Your Order from " + myname + " finish your order.", my_uid, buyerUID, "RequestNotif");
                        Sender sender = new Sender(data, FCMToken.getFcm_token());
                        api_service.sendNotification(sender)
                                .enqueue(new Callback<Response>() {
                                    @Override
                                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                        Log.d("Status_Update", response + " sent? "+ call);
                                    }
                                    @Override
                                    public void onFailure(Call<Response> call, Throwable t) {
                                        Log.d("Status_Update", t + " not sent? "+ call);
                                    }
                                });
                        update_firebase(status);
                        unrating_food();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    void update_firebase(final String status){
        DatabaseReference seller = FirebaseDatabase.getInstance().getReference().child("receipt").child(my_uid);
        seller.child(random_key).child("his_status").setValue(status);

        DatabaseReference buyer = FirebaseDatabase.getInstance().getReference().child("request").child(his_uid);
        buyer.child(random_key).child("my_status").setValue(status);
    }


    public String get_myname(){
        DatabaseReference find = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = find.child(my_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                my_name = u.getUsername();
                Log.d("su_debug", "z=" + my_name);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        return my_name;
    }


    public String get_hisname(){
        DatabaseReference find = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = find.child(his_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                buyer_name = u.getUsername();
                Log.d("su_debug", "z+" + buyer_name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        return buyer_name;
    }

    public void unrating_food(){
        DatabaseReference rate = FirebaseDatabase.getInstance().getReference().child("unrate").child(my_uid);

        Query q1 = FirebaseDatabase.getInstance().getReference("request").child(my_uid).child("foodList");
        q1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    ShoppingCart data = d.getValue(ShoppingCart.class);
                    food_list.add(data);
                    Log.d("su_debug", "z+" + food_list);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        Rating unrate = new Rating(food_list);

        rate.setValue(unrate);
    }
}
