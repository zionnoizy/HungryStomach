package com.example.hungrystomach.Model;

import java.text.SimpleDateFormat;

public class Food {
    private String name;
    private String description;
    private String price;
    private String uri;
    private String dateTime;
    private String uploader_uid;
    private String key;
    private String uploader;

    public Food() {
        //empty
    }

    public Food(String name, String description, String price, String uri, String dateTime, String uploader_uid, String key,String uploader) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.uri = uri;
        this.dateTime = dateTime;
        this.uploader_uid = uploader_uid;
        this.key =key;
        this.uploader = uploader;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getUri() {
        return uri;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getUploader_uid() {
        return uploader_uid;
    }

    public String getKey() {
        return key;
    }

    public String getUploader() {
        return uploader;
    }


}
