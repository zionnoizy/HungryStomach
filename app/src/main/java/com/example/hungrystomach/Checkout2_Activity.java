package com.example.hungrystomach;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.GooglePaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.hungrystomach.Model.Invoice;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.hungrystomach.Cart_Activity.PASS_TOTAL_AMT;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;


public class Checkout2_Activity extends AppCompatActivity {

    final int BRAINTREE_REQUEST_CODE = 4949;
    final String TAG = "BrainTree_Debug";
    final String API_GET_TOKEN = " http://192.168.0.15/braintree/main.php";
    final String API_CHECKOUT = "http://192.168.0.15/braintree/checkout.php";

    EditText et_amount;
    LinearLayout ll;
    Button buy;
    TextView TT;
    String grandT;
    long entry_no;
    String clientToken;
    String invoice_format;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String name = user.getDisplayName();
    private final String CHANNEL_ID = "Invoice Notification";
    private final int NOTIFICATION_ID = 001;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_payment);
        buy = findViewById(R.id.btnBuy);
        TT = findViewById(R.id.TTprice);
        grandT = getIntent().getStringExtra(PASS_TOTAL_AMT);
        TT.setText("Your total is: $" + grandT); //DecimalFormat("#.##").format
        buy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startPayment();
                //onBraintreeSubmit(view);
            }
        });

        //https://www.androidauthority.com/android-push-notifications-with-firebase-cloud-messaging-925075/
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( Checkout2_Activity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                Log.e(TAG, mToken);
            }
        });
    }

    //get token from server
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
                //clientToken = responseToken;
                onBraintreeSubmit(responseToken);
            }
        });
    }

    public void onBraintreeSubmit(String responseToken){
        DropInRequest dropInRequest = new DropInRequest().clientToken(responseToken);
        Log.d(TAG, "Submiting to BrainTree.. " + responseToken);
        startActivityForResult(dropInRequest.getIntent(this), BRAINTREE_REQUEST_CODE);
    }

    //https://github.com/mchodyn/braintree_android_test/blob/master/app/src/main/java/navneet/com/braintreepaypalsample/MainActivity.java
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BRAINTREE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String paymentMethodNonce = result.getPaymentMethodNonce().getNonce();
                Log.d(TAG, "Nonce.. " + paymentMethodNonce);
                if(!grandT.isEmpty())
                    submitNonce(paymentMethodNonce);
                else
                    Toast.makeText(Checkout2_Activity.this, "Please Check Your Total Amount", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "User return");
            } else {
                Exception exception = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d(TAG, "Error:: " + exception.toString());
            }
        }
    }

    private void submitNonce(String paymentMethodNonce){
        RequestParams params = new RequestParams();
        params.put("amount", grandT);
        params.put("nonce", paymentMethodNonce);
        Log.d(TAG, "Params.. " + params);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(API_CHECKOUT, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("zionnoizy");
                createDatabase();
                sendNotification();
                Intent finish = new Intent(Checkout2_Activity.this, Checkout3_Activity.class);
                startActivity(finish);
                finish();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, "CheckOut Failure.. " + error);
            }
        });

    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    void createDatabase(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("invoice");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    entry_no=(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Invoice one_invoice = new Invoice (username, currentDateandTime, Double.parseDouble(grandT), 0); //seller
        ref.child(String.valueOf(entry_no+1)).setValue(one_invoice);
        invoice_format = getString(R.string.invoice_format, username, grandT, currentDateandTime);
    }

    //send notification
    void sendNotification(){
        createChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);

        builder.setSmallIcon(R.drawable.bt_ic_sms_code);
        builder.setContentTitle("Invoie Notificaiton");
        builder.setContentText(invoice_format);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());


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

}
