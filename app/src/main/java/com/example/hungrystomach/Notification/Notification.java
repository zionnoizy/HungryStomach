package com.example.hungrystomach.Notification;


import java.util.Date;

public class Notification{
    private String title;
    private String body;
    //private long msgTime;
 
    public Notification(String title, String body) {
        this.title = title;
        this.body = body;
        //msgTime = new Date().getTime();
    }
 
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}