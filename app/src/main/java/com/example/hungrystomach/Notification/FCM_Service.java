package com.example.hungrystomach.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.hungrystomach.MainActivity;
import com.example.hungrystomach.Model.FCMToken;
import com.example.hungrystomach.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


//https://www.codervlog.com/2019/10/firebase-push-notification-android.html
public class FCM_Service extends FirebaseMessagingService {
    private static final String TAG = "FCM_Debug";

    @Override
    public void onNewToken(@NonNull String fcm_token){
        super.onNewToken(fcm_token);
        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("FCMToken").child(usr.getUid());
        FCMToken FCM_token = new FCMToken(fcm_token); //
        String new_token = FCM_token.getFcm_token();
        ref.child("fcm_token").setValue(new_token);
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //if (remoteMessage.getData().size() > 0)
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String my_uid = remoteMessage.getData().get("my_uid");
        String his_uid = remoteMessage.getData().get("his_uid");
        String whattype = remoteMessage.getData().get("whattype");
        Log.d("TAG", title + body + my_uid + his_uid + whattype);
        if(whattype.equals("RequestNotif")) {
            Log.d("TAG", "=RequestNotif");
            sendRequestNotification(title, body, my_uid, his_uid);
        }
        //else if(whattype.equals("ChatNotif"))
        //sendChatNotification(title, body, his_uid);
    }


    private void sendRequestNotification(String title, String body, String my_uid, String his_uid){
        Intent click_redirect = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("hisUid", his_uid);
        click_redirect.putExtras(bundle);
        click_redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int i = Integer.parseInt(his_uid.replaceAll("[\\D]",""));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, i, click_redirect, PendingIntent.FLAG_ONE_SHOT);
        Log.d("TAG", "1");
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(getApplicationContext(), getString(R.string.default_notification_channel_id));

        nBuilder.setChannelId(getString(R.string.default_notification_channel_id));
        nBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                //.setSound(sound_uri)
                .setContentIntent(pendingIntent);
        Log.d("TAG", "2");
        //Uri sound_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notifMger = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE); //v
        notifMger.notify(1, nBuilder.build());
    }
}