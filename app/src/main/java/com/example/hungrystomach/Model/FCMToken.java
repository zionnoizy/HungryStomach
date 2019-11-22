package com.example.hungrystomach.Model;

public class FCMToken {

    String fcm_token;

    public FCMToken(){
        //empty
    }

    public FCMToken(String fcm_token){
        this.fcm_token = fcm_token;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }
}
