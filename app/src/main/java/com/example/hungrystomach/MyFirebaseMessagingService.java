package com.example.hungrystomach;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {




    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //debug
        Log.d("From", "From: " + remoteMessage.getFrom());
        Log.d("Title", "From: " + remoteMessage.getFrom());
        Log.d("Body", "From: " + remoteMessage.getFrom());

        super.onMessageReceived(remoteMessage);
        generateNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        if (remoteMessage.getData().size() > 0) {
            if (true) {
            } else {
            }
        }
        //contain payload
        if (remoteMessage.getNotification() != null) {
        }
    }
    private void generateNotification(String b, String t){
        Intent i = new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pi = PendingIntent.getActivities(this,0,i,PendingIntent.FLAG_ONE_SHOT);

        //notification
        Uri soung_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(t) //title
                .setContentText(b) //body
                .setAutoCancel(true)
                .setSound(soung_uri)
                .setContentIntent(pi);



    }
    public static int NOTIFICATION_ID = 1;
    NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    nm.notify(NOTIFICATION_ID++, nb.build()); //NotificationManager notify
}
