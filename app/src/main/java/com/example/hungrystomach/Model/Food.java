package com.example.hungrystomach.Model;

import java.text.SimpleDateFormat;

public class Food {
    public String name;
    public String description;
    public String price;
    public String img_uri;
    public String uploader;
    public String DatenTime;

    public Food() {
        //empty
    }

    public Food(String name, String description, String price, String img_uri, String sdf) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.img_uri = img_uri;
        this.DatenTime = sdf;
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
        return img_uri;
    }

    public String getDateTime() {
        return DatenTime;
    }


}
