package com.example.hungrystomach;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hungrystomach.Model.FCMToken;
import com.example.hungrystomach.Notification.Data;
import com.example.hungrystomach.Model.User;
import com.example.hungrystomach.Notification.Sender;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RoomChat_Activity extends AppCompatActivity {

    String my_uid;
    String his_uid;
    Boolean notify = true;
    private RequestQueue requestQueue;
    //create new database chat_room with sender(myuid) and receiver uid(hisuid)

    //create notification to receiver


    //send_msg()
    //attach hash in firebase


    //show_prev_msg()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_chat);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    private void send_message(final String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", my_uid);
        hashMap.put("receiver", his_uid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        //hashMap.put("isSeen", false);
        hashMap.put("type", "text");
        databaseReference.child("chats").push().setValue(hashMap);

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(my_uid);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (notify) {
                    sendNotify(his_uid, user.getUsername(), message);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //save sender/receiver
        final DatabaseReference mychatRef = FirebaseDatabase.getInstance().getReference("chat_list")
                .child(my_uid)
                .child(his_uid);
        mychatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    mychatRef.child("aite").setValue(his_uid);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        final DatabaseReference hischatRef = FirebaseDatabase.getInstance().getReference("chat_list")
                .child(his_uid)
                .child(my_uid);
        hischatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    hischatRef.child("aite").setValue(my_uid);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    private void sendNotify(final String his_uid, final String name, final String message){
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(his_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    FCMToken token = ds.getValue(FCMToken.class);
                    Data data = new Data("New Message from HungryStomach", name + ": " + message,  my_uid, his_uid, "ChatNotif");

                    Sender sender = new Sender(data, token.getFcm_token());

                    //fcm json object request
                    try {
                        JSONObject senderJsonObj = new JSONObject(new Gson().toJson(sender));
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", senderJsonObj,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("JSON_RESPONSE", "onResponse: "+response.toString());

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("JSON_RESPONSE", "onResponse: "+error.toString());
                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                //put params
                                Map<String, String> headers = new HashMap<>();
                                headers.put("Content-Type", "application/json");
                                headers.put("Authorization", "key=AAAAHI3mI9Y:APA91bGnuTKUcP5s_BQwwsLgim1xnmVIjPRpJwdvwXTSBQaIV3PsfHBuJPC1ZVknrk4tJbjWpizLUZY3dBuaKGScElnjRPfJ-qYoLuK84IxAFdw4Ennpl3wupYxkMKLVKFgujJj5Eiub");

                                return headers;
                            }
                        };
                        requestQueue.add(jsonObjectRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

}
