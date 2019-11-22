package com.example.hungrystomach.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Authorization: key=AAAAHI3mI9Y:APA91bGnuTKUcP5s_BQwwsLgim1xnmVIjPRpJwdvwXTSBQaIV3PsfHBuJPC1ZVknrk4tJbjWpizLUZY3dBuaKGScElnjRPfJ-qYoLuK84IxAFdw4Ennpl3wupYxkMKLVKFgujJj5Eiub",
            "Content-Type:application/json"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);

}
