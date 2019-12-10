package com.example.hungrystomach;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hungrystomach.Adapter.ChatAdapter;
import com.example.hungrystomach.Model.Chat;
import com.example.hungrystomach.Model.FCMToken;
import com.example.hungrystomach.Notification.Data;
import com.example.hungrystomach.Model.User;
import com.example.hungrystomach.Notification.Sender;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.hungrystomach.Status_Update_Activity.EXTRA_HISUID;
import static com.example.hungrystomach.Track_Order_Activity.EXTRA_COOKER_UID2;


public class RoomChat_Activity extends AppCompatActivity {

    String my_uid;
    String his_uid;
    Boolean notify = true;
    ImageButton send_btn;
    EditText input_message;
    TextView tv_recevier;
    String receiver_name;
    private RequestQueue requestQueue;

    String whattype = "ChatNotif";
    List<Chat> chat_list;

    ChatAdapter adapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_chat);

        //check pervious activity
        Intent mIntent = getIntent();
        String previousActivity= mIntent.getStringExtra("FROM_ACTIVITY");
        if (previousActivity.equals("status_update")){
            his_uid = getIntent().getStringExtra(EXTRA_HISUID);
        }
        if (previousActivity.equals("track_order")){
            his_uid = getIntent().getStringExtra(EXTRA_COOKER_UID2);
        }
        send_btn = findViewById(R.id.room_send);
        input_message = findViewById(R.id.room_message);
        tv_recevier = findViewById(R.id.sr_receivername);

        my_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        requestQueue = Volley.newRequestQueue(getApplicationContext());


        //get receiver name
        DatabaseReference find = FirebaseDatabase.getInstance().getReference().child("users").child(his_uid);
        find.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                receiver_name = u.getUsername();
                tv_recevier.setText(receiver_name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        recyclerView = (RecyclerView) findViewById(R.id.chatroom_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        read_message();
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_msg = input_message.getText().toString().trim();
                if (TextUtils.isEmpty(input_msg)) {
                    Toast.makeText(RoomChat_Activity.this, "Your message is empty", Toast.LENGTH_SHORT).show();
                } else {
                    send_message(input_msg);
                }
                input_message.setText("");

            }
        });
    }

    private void send_message(final String input_msg) {
        DatabaseReference create_chat = FirebaseDatabase.getInstance().getReference().child("chat_list");
        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(yourmilliseconds);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", input_msg);
        hashMap.put("sender", my_uid);
        hashMap.put("receiver", his_uid);
        hashMap.put("times", sdf.format(resultdate));
        create_chat.push().setValue(hashMap);

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(my_uid);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify)
                    sendNotify(his_uid, user.getUsername(), input_msg);
                notify = false;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void read_message(){
        chat_list = new ArrayList<>();
        DatabaseReference red_chat = FirebaseDatabase.getInstance().getReference("chat_list");
        red_chat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chat_list.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Chat chat = ds.getValue(Chat.class);
                    if(chat.getReceiver().equals(my_uid) && chat.getSender().equals(his_uid) ||
                            chat.getReceiver().equals(his_uid) && chat.getSender().equals(my_uid)){
                        chat_list.add(chat);
                    }
                }
                adapter = new ChatAdapter(RoomChat_Activity.this, chat_list);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    private void sendNotify(final String his_uid, final String name, final String message){
        DatabaseReference fcm_tokens = FirebaseDatabase.getInstance().getReference("FCMToken");
        Query query = fcm_tokens.orderByKey().equalTo(his_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    FCMToken token = ds.getValue(FCMToken.class);
                    Data data = new Data("New Message from HungryStomach", name + ": " + message,  my_uid, his_uid, whattype);
                    Sender sender = new Sender(data, token.getFcm_token());
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

    //https://www.youtube.com/watch?v=MblszhqIWI8

}
