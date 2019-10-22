package com.example.hungrystomach.Model;

public class Food {
    public String name;
    public String description;
    public String price;
    public String img_uri;
    public String uploader;

    public Food() {
        //empty
    }

    public Food(String name, String description, String price, String img_uri) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.img_uri = img_uri;
    }


    public String get_name() {
        return name;
    }


    public String get_description() {
        return description;
    }


    public String get_price() {
        return price;
    }


    public String get_uri() {
        return img_uri;
    }


}
