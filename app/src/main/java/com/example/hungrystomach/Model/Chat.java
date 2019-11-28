package com.example.hungrystomach.Model;

public class Chat {
    String message;
    String sender;
    String receiver;
    String times;

    public Chat() {
        //empty
    }

    public Chat(String message, String sender, String receiver, String times) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.times = times;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
