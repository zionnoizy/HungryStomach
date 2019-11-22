package com.example.hungrystomach.Notification;


public class Data {

    private String title;
    private String body;
    private String my_uid;
    private String his_uid;
    private String whattype;

    public Data() {
        //
    }

    public Data(String title, String body, String my_uid, String his_uid, String whattype) {
        this.title = title;
        this.body = body;
        this.my_uid = my_uid;
        this.his_uid = his_uid;
        this.whattype = whattype;
    }

    public String getMy_uid() {
        return my_uid;
    }

    public void setMy_uid(String my_uid) {
        this.my_uid = my_uid;
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

    public String getHis_uid() {
        return his_uid;
    }

    public void setHis_uid(String his_uid) {
        this.his_uid = his_uid;
    }

    public String getWhattype() {
        return whattype;
    }

    public void setWhattype(String whattype) {
        this.whattype = whattype;
    }
}
