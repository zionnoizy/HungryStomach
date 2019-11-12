package com.example.hungrystomach;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Random;

//ver 20.0.0
public class MyFirebaseMessagingService extends FirebaseMessagingService {
	private static final String TAG = "MyFirebaseIIDService";
	//https://stackoverflow.com/questions/37787373/firebase-fcm-how-to-get-token
	@Override
	public void onNewToken(@NonNull String Token){
		super.onNewToken(Token);
		String token = FirebaseInstanceId.getInstance().getToken();
		Log.d(TAG, "refreshed token:" + Token + token);
	}

	//Receive
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);
        Log.d("From_", "From: " + remoteMessage.getFrom());
        Log.d("Title_", "Notification Msg Body: " + remoteMessage.getNotification().getBody());
        //ArrayList<String> notificationData = new ArrayList<String>(remoteMessage.getData().values());
        //String fortuneID = notificationData.get(0);

        if (remoteMessage.getData().size() > 0) {
			Log.d("F3", "Message data payload: " + remoteMessage.getData());
			sendNotification(remoteMessage.getData().get("name"),remoteMessage.getNotification().getBody()); //getTitle()
        }
    }


	//Generate; Send; Show
    private void sendNotification(String body, String title){
		NotificationCompat.Builder nBuilder;

        Intent click_redirect = new Intent(this,Home_Activity.class);
		click_redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pI = PendingIntent.getActivity(this,0, click_redirect,
				PendingIntent.FLAG_ONE_SHOT);

        Uri sound_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE); //v

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
			NotificationChannel nChannel = new NotificationChannel(getString(R.string.default_notification_channel_id), "notifications", NotificationManager.IMPORTANCE_DEFAULT);
			Log.e("NC_", "send_Notification: "+ nChannel );

			//if (mChannel == null)
			nChannel.setDescription("Your Invoice");
			nChannel.enableLights(true);
			nChannel.setLightColor(Color.BLUE);
			nChannel.setVibrationPattern(new long[]{1,1000,500,1000});
			nm.createNotificationChannel(nChannel);
		}
		nBuilder = new NotificationCompat.Builder(getApplicationContext(), getString(R.string.default_notification_channel_id));
		nBuilder.setChannelId(getString(R.string.default_notification_channel_id));
		nBuilder.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(title)
				.setContentText(body)
				.setAutoCancel(true)
				.setSound(sound_uri)
				.setContentIntent(pI);

		nm.notify(1000, nBuilder.build()); //new Random().nextInt()
	}










}
