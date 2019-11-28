package com.example.hungrystomach;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.hungrystomach.Model.FCMToken;
import com.example.hungrystomach.Model.Receipt;
import com.example.hungrystomach.Model.Request;
import com.example.hungrystomach.Model.ShoppingCart;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.hungrystomach.Cart_Activity.PASS_UPLAODER_UID;
import static com.example.hungrystomach.Checkout2_Activity.EXTRA_AMOUNT_TO_PAY;
import static com.example.hungrystomach.Checkout2_Activity.EXTRA_UPLOADERUID;

public class Checkout3_Activity extends AppCompatActivity {

    Button back;

    String my_uid;
    String his_uid;
    String grant_total;
    int invoice_entry_no=0;
    int request_entry_no=0;
    String first_status = "not response";
    String random_key;

    //notification
    private final String CHANNEL_ID = "Receipt Notification";
    private final int NOTIFICATION_ID = 1;
    String notif_text;
    boolean notify = false;
    private String FCM_SEND_URL = "https://fcm.googleapis.com/";
    APIService api_service;

    List<ShoppingCart> food_list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_success);

        back = findViewById(R.id.bt_back);
        my_uid =FirebaseAuth.getInstance().getCurrentUser().getUid();
        his_uid = getIntent().getStringExtra(EXTRA_UPLOADERUID);
        grant_total = getIntent().getStringExtra(EXTRA_AMOUNT_TO_PAY);


        //food_list = save_foodlist();
        //Log.d("CA_Debug","z+" +food_list);
        //create_receipt_buyer();
        //create_request_seller();
        send_buyer_notif();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(Checkout3_Activity.this, Home_Activity.class);
                startActivity(home);
                delete_foodlist();
                finish();
            }
        });

        //show Google Map if available

    }

    /*
    void create_receipt_buyer(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mmaa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("receipt").child(my_uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                invoice_entry_no = (int) dataSnapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        int x = invoice_entry_no + 1;
        random_key = ref.push().getKey();

        Receipt one_receipt = new Receipt (my_uid, username, his_uid, Double.parseDouble(grant_total), currentDateandTime, 0, food_list, first_status, x, random_key);
        ref.child(random_key).setValue(one_receipt);
        notif_text = getString(R.string.invoice_text, username, grant_total, currentDateandTime);
    }

    void create_request_seller(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date();
        String dateTime = dateFormat.format(date);

        DatabaseReference new_request_db = FirebaseDatabase.getInstance().getReference("request").child(his_uid);
        new_request_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    request_entry_no=(int)dataSnapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        int y = request_entry_no + 1;
        Log.d("CA_Debug", "a_a" + food_list);

        Request rq = new Request (food_list, my_uid, dateTime, Double.parseDouble(grant_total), first_status, his_uid, y, random_key);
        new_request_db.child(random_key).setValue(rq);

    }
    */

    void send_buyer_notif(){
        createChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);

        builder.setSmallIcon(R.drawable.bt_ic_sms_code);
        builder.setContentTitle("Invoie Notificaiton");
        builder.setContentText(notif_text);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(notif_text));

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

        String buyer_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        send_seller_notif(his_uid, buyer_name);
    }

    private void createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Order Notification";
            String description = "Receipt";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel("CHANNEL_ID", name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    void send_seller_notif(final String hisUID, final String name){
        notify = true;

        api_service = Client.getRetrofit(FCM_SEND_URL).create(APIService.class);

        DatabaseReference find_fcm = FirebaseDatabase.getInstance().getReference("FCMToken");

        Query query = find_fcm.orderByKey().equalTo(his_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    FCMToken FCMToken = ds.getValue(FCMToken.class);

                    Data data = new Data("New Request", "You have a new request from: "+name+". Please check you invoice page", my_uid, hisUID, "RequestNotif");
                    updateToken(FCMToken.getFcm_token());
                    Sender sender = new Sender(data, FCMToken.getFcm_token());

                    api_service.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Log.d("C3_D", response + " sent "+ call);
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {
                                    Log.d("C3_D", t + " not send "+ call);
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void updateToken(String token){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FCMToken").child(his_uid);
        FCMToken mFCMToken = new FCMToken(token);
        String new_token = mFCMToken.getFcm_token();
        ref.child("fcm_token").setValue(new_token);
        Log.d("C3_D", "renew uploader uid: " + his_uid + " to " + new_token);
    }

    /*
    public List<ShoppingCart> save_foodlist(){
        food_list = new ArrayList<>();
        Query q1 = FirebaseDatabase.getInstance().getReference("shopping_cart").child(my_uid);
        q1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                food_list.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    ShoppingCart data = d.getValue(ShoppingCart.class);
                    String name = data.getProduct_name();
                    String price = data.getProduct_price();
                    String uuid = data.getUploader_uid();
                    String uri = data.getImg_url();
                    food_list.add(data);
                }
                Log.d("CA_Debug", "zz" + food_list.size());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        return food_list;
    }
    */

    public void delete_foodlist(){
        final DatabaseReference delete_allitem = FirebaseDatabase.getInstance().getReference("shopping_cart");
        delete_allitem.child(my_uid).removeValue();
    }




}
