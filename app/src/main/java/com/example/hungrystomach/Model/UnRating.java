package com.example.hungrystomach.Model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class UnRating {
    String name;
    String url;
    String price;
    String random_key;
    //List<ShoppingCart> foodList = new ArrayList<>();

    public UnRating(){
        //Empty
    }

    public UnRating(String name, String url, String price,String random_key) {
        this.name = name;
        this.url = url;
        this.price = price;
        this.random_key = random_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRandom_key() {
        return random_key;
    }

    public void setRandom_key(String random_key) {
        this.random_key = random_key;
    }
}
