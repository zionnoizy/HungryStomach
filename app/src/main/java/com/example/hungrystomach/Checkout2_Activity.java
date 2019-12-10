package com.example.hungrystomach;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.dropin.utils.PaymentMethodType;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.hungrystomach.Model.BuyerFoodList;
import com.example.hungrystomach.Model.FCMToken;
import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.Model.Receipt;
import com.example.hungrystomach.Model.Request;
import com.example.hungrystomach.Model.SellerFoodList;
import com.example.hungrystomach.Model.ShoppingCart;
import com.example.hungrystomach.Notification.APIService;
import com.example.hungrystomach.Notification.Client;
import com.example.hungrystomach.Notification.Data;
import com.example.hungrystomach.Notification.Notification;
import com.example.hungrystomach.Notification.Response;
import com.example.hungrystomach.Notification.Sender;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.hungrystomach.Cart_Activity.PASS_TOTAL_AMT;
import static com.example.hungrystomach.Cart_Activity.PASS_UPLAODER_UID;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;


public class Checkout2_Activity extends AppCompatActivity {

    final int BRAINTREE_REQUEST_CODE = 1; //4949
    final String TAG = "Braintree_Debug";
    final String TAG2 = "Notiifcaiton_Debug";
    final String API_GET_TOKEN = " http://192.168.0.15/braintree/main.php";
    final String API_CHECKOUT = "http://192.168.0.15/braintree/checkout.php";

    Button buy_button;
    TextView total_amount;

    String grandT;
    String format_gT;
    String uploader_uid;

    int invoice_entry_no;
    long request_entry_no=0;
    String invoice_notif;
    String my_uid = FirebaseAuth.getInstance().getUid();

    private final String CHANNEL_ID = "Invoice Notification";
    private final int NOTIFICATION_ID = 001;
    private String FCM_SEND_URL = "https://fcm.googleapis.com/";
    private RequestQueue mRequestQue;
    boolean notify = false;

    public static final String EXTRA_UPLOADERUID = "NoUploaderUid";
    public static final String EXTRA_AMOUNT_TO_PAY = "NoTotalAmount";
    String random_key;
    String first_status = "not response";
    APIService api_service;
    List<ShoppingCart> save_food_list = new ArrayList<>();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_payment);

        buy_button = findViewById(R.id.btnBuy);
        total_amount = findViewById(R.id.TTprice);

        get_foodlist();
        grandT = getIntent().getStringExtra(PASS_TOTAL_AMT);
        DecimalFormat df = new DecimalFormat("#.##");
        format_gT = df.format(Double.parseDouble(grandT));
        total_amount.setText("Your total is: $" + format_gT);
        uploader_uid = getIntent().getStringExtra(PASS_UPLAODER_UID);

        /*
        //choose utnesil and pick up choose
        choose_utensil = findViewById(R.id.choose_utensil);
        choose_pickup = findViewById(R.id.choose_pickup);
        */


        buy_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startPayment();
            }
        });

        ///////////////////////////////////////////////////////send notification
        //https://www.androidauthority.com/android-push-notifications-with-firebase-cloud-messaging-925075/
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( Checkout2_Activity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                Log.e(TAG, "what token?" + mToken);
            }
        });
        mRequestQue = Volley.newRequestQueue(this);

    }

    void startPayment(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(API_GET_TOKEN, new TextHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, "Client Failure: " +  responseString);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseToken) {
                Log.d(TAG, "Client token: " + responseToken);
                onBraintreeSubmit(responseToken);
            }
        });
    }

    public void onBraintreeSubmit(String responseToken){
        DropInRequest dropInRequest = new DropInRequest().clientToken(responseToken);
        Log.d(TAG, "Submiting to BrainTree.. " + responseToken);

        startActivityForResult(dropInRequest.getIntent(this), BRAINTREE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BRAINTREE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String paymentMethodNonce = result.getPaymentMethodNonce().getNonce();
                //Log.d(TAG, "Nonce.. " + paymentMethodNonce);


                if(!format_gT.isEmpty())
                    submitNonce(paymentMethodNonce);
                else
                    Toast.makeText(Checkout2_Activity.this, "Please Check Your Total Amount", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "User return");
            } else {
                Exception exception = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d(TAG, "Error: " + exception.toString());
            }
        }
    }

    private void submitNonce(String paymentMethodNonce){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("amount", format_gT);
        params.put("nonce", paymentMethodNonce);

        client.post(API_CHECKOUT, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //Log.e(TAG, "Payment Made Successuffly= " + statusCode + "headers" + headers + "response body" + responseBody);
                buyer_receipt();
                create_bc_foodlist();
                cooker_request();
                Intent finish = new Intent(Checkout2_Activity.this, Checkout3_Activity.class);
                finish.putExtra(EXTRA_UPLOADERUID, uploader_uid);
                finish.putExtra(EXTRA_AMOUNT_TO_PAY, format_gT);

                startActivity(finish);
                finish();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, "CheckOut Failure. Please Check Your Network " + error);
            }
        });

    }


    ///////////////////////////////////////////////////////////////////////////////////////////


    void buyer_receipt(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mmaa", Locale.getDefault());
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
        random_key = ref.push().getKey();
        Receipt one_receipt = new Receipt (my_uid, username, uploader_uid, Double.parseDouble(grandT), currentDateandTime, 0, save_food_list, first_status, invoice_entry_no+1, random_key);
        ref.child(random_key).setValue(one_receipt);

        invoice_notif = getString(R.string.invoice_text, username, grandT, currentDateandTime);
    }


    public void create_bc_foodlist(){
        Query q1 = FirebaseDatabase.getInstance().getReference("shopping_cart").child(my_uid);
        final DatabaseReference buyer_fl = FirebaseDatabase.getInstance().getReference().child("buyer_food_list").child(my_uid).child(random_key);
        final DatabaseReference seller_fl = FirebaseDatabase.getInstance().getReference().child("seller_food_list").child(uploader_uid).child(random_key);
        q1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    ShoppingCart data = d.getValue(ShoppingCart.class);
                    String name = data.getProduct_name();
                    String product_price = data.getProduct_price();
                    String url = data.getImg_url();
                    int uantity = data.getQuantity();
                    double sub_total = data.getSubtotal();
                    String cooker_uid = data.getUsr_uid();
                    String random_key = data.getProduct_key();
                    BuyerFoodList b = new BuyerFoodList(name, product_price, url, cooker_uid, uantity, sub_total, random_key);
                    SellerFoodList s = new SellerFoodList(name, product_price, url, cooker_uid, uantity, sub_total, random_key);
                    buyer_fl.child(name).setValue(b);
                    seller_fl.child(name).setValue(s);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
    /*
    //send notification
    void sendNotification(){
        createChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);

        builder.setSmallIcon(R.drawable.bt_ic_sms_code);
        builder.setContentTitle("Invoie Notificaiton");
        builder.setContentText(invoice_notif);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

        String buyer_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        sendRequestNotification(uploader_uid, buyer_name);
    }

    private void createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Order Notifications";
            String description = "Invoice";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    //////////////////////////////////////////////////////////////
    private void sendRequestNotification(final String hisUID, final String name){//, final String chat_message){
        notify = true;
        Log.d(TAG, hisUID+ " "+ name);
        api_service = Client.getRetrofit(FCM_SEND_URL).create(APIService.class);

        DatabaseReference find_fcm = FirebaseDatabase.getInstance().getReference("FCMToken");

        Query query = find_fcm.orderByKey().equalTo(uploader_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    FCMToken FCMToken = ds.getValue(FCMToken.class);

                    Data data = new Data("New Request", "You have a new request from: "+name+". Please check you invoice page", my_uid, hisUID, "RequestNotif");
                    updateToken(FCMToken.getFcm_token());
                    Log.d(TAG, "ready to send notif to: " + hisUID+ " with msg");
                    Sender sender = new Sender(data, FCMToken.getFcm_token());

                    api_service.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Log.d(TAG, response + " sent? "+ call);
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {
                                    Log.d(TAG, t + " not sent? "+ call);
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void updateToken(String token){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FCMToken").child(uploader_uid);
        FCMToken mFCMToken = new FCMToken(token);
        String new_token = mFCMToken.getFcm_token();
        ref.child("fcm_token").setValue(new_token);
        Log.d(TAG, "renew uploader uid: " + uploader_uid + " to " + new_token);
    }
    */
    //https://stackoverflow.com/questions/47854504/how-to-add-and-retrieve-data-into-firebase-using-lists-arraylists
    public void cooker_request(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date();
        String dateTime = dateFormat.format(date);

        DatabaseReference new_request_db = FirebaseDatabase.getInstance().getReference("request").child(uploader_uid);
        new_request_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    request_entry_no=(dataSnapshot.getChildrenCount());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        Request rq = new Request (save_food_list, my_uid, dateTime, Double.parseDouble(grandT), first_status, uploader_uid, request_entry_no+1, random_key); //image_url
        new_request_db.child(random_key).setValue(rq);
    }



    public void get_foodlist(){
        Query q1 = FirebaseDatabase.getInstance().getReference("shopping_cart").child(my_uid);
        q1.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot d : dataSnapshot.getChildren()){
                     ShoppingCart data = d.getValue(ShoppingCart.class);
                    String name = data.getProduct_name();
                    String sub_ = data.getProduct_price();
                    save_food_list.add(data);
                    Log.d("generate_foodList", "z" + save_food_list);
                }
             }
             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) { }
         });
    }


}
