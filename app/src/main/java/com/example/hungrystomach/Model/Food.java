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
    private float rating;

    public Food() {
        //empty
    }

    public Food(String name, String description, String price, String uri, String dateTime, String uploader_uid, String key, String uploader, float rating) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.uri = uri;
        this.dateTime = dateTime;
        this.uploader_uid = uploader_uid;
        this.key =key;
        this.uploader = uploader;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUploader_uid() {
        return uploader_uid;
    }

    public void setUploader_uid(String uploader_uid) {
        this.uploader_uid = uploader_uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
