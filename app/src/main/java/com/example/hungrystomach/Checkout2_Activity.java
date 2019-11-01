package com.example.hungrystomach;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Checkout2_Activity extends AppCompatActivity {


    //PAYMENT
    final int REQUEST_CODE = 1;
    String token, amount;
    HashMap<String, String> paraHash;
    final String API_GET_TOKEN = "http://10.0.2.2/braintree/main.php";
    final String API_CHECKOUT = "http://10.0.2.2/braintree/checkout.php";

    Button btnPay;
    EditText et_amount;
    LinearLayout ll;
    //CardForm cardForm = (CardForm) findViewById(R.id.card_form);
    Button buy = findViewById(R.id.btnBuy);


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_payment);

        /*
        buy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                OnBraintreeSubmitPayment();
            }
        });
        */
        new Checkout2_Activity.getToekn().execute();

    }

    /*
    public void onBraintreeSubmit(View v) {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(token); //maybe empty?
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
    }


    //submit payment
    public void OnBraintreeSubmitPayment(){
        String payValue; //I don't have it
        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
    }

    //send payment first
    private void sendPaymntDetails() {
        RequestQueue queue = Volley.newRequestQueue(Checkout2_Activity.this);
        StringRequest stringReq = new StringRequest(Request.Method.POST, API_CHECKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("Successful")) {
                            Toast.makeText(Checkout2_Activity.this, "Transaction successful!", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(Checkout2_Activity.this, "Transaction failed!", Toast.LENGTH_LONG).show();
                        Log.d("mylog1", "Final Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("my_log2", "Volle error: " + error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(paraHash == null)
                    return null;
                Map<String, String> params = new HashMap<>();
                for(String key: params.keySet()) {
                    params.put(key, paraHash.get(key));
                    Log.d("my_log3", "Key: " + key + "Value: " + paraHash.get(key));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RetryPolicy mRetryPolicy=new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringReq.setRetryPolicy(mRetryPolicy);
        queue.add(stringReq);
    }
    */


    //get token function
    private class getToekn extends AsyncTask { //HttpRequest=getToken
        @Override
        protected Object doInBackground(Object[] objects){
            HttpClient client = new HttpClient();
            client.get(API_GET_TOKEN, new HttpResponseCallback() {
                @Override
                public void success(final String responseBody) {
                    Log.d("my_log4", responseBody);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Checkout2_Activity.this, "Successfully got token", Toast.LENGTH_SHORT).show();
                            token=responseBody;
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
                    Log.d("Err", exception.toString());

                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }

    }


    /*
    //https://www.thecodecity.com/2017/04/braintree-android-integration-tutorial.html
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (requestCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String paymentMethodNonce = result.getPaymentMethodNonce().getNonce();
                Log.d("mylog5 ", "Result:" + paymentMethodNonce);

                if (!et_amount.getText().toString().isEmpty()) {
                    amount = et_amount.getText().toString();
                    paraHash = new HashMap<>();
                    paraHash.put("amount", amount);
                    paraHash.put("nonce", paymentMethodNonce);
                    sendPaymntDetails();
                } else
                    Toast.makeText(Checkout2_Activity.this, "Please enter a valid amount.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "User canceled", Toast.LENGTH_SHORT).show();
                Log.d("my_log6", "usr cenceled");
            } else {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("my_log7", "Error:: " + error.toString());
            }
        }
    }
    */

    ///////////////////////////////////////////////////////////////////////////////////////////
    //if click finish, create new database "invoice"
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("invoice");

    //notification

}
