package com.example.hungrystomach.Model;

public class Ordered {

    String food_name;
    String food_key;
    String food_url;
    String food_description;
    String food_price;
    String food_uuid;

    public Ordered(){
        //Empty
    }

    public Ordered(String food_name, String food_key, String food_url, String food_description, String food_price, String food_uuid) {
        this.food_name = food_name;
        this.food_key = food_key;
        this.food_url = food_url;
        this.food_description = food_description;
        this.food_price = food_price;
        this.food_uuid = food_uuid;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_key() {
        return food_key;
    }

    public void setFood_key(String food_key) {
        this.food_key = food_key;
    }

    public String getFood_url() {
        return food_url;
    }

    public void setFood_url(String food_url) {
        this.food_url = food_url;
    }

    public String getFood_description() {
        return food_description;
    }

    public void setFood_description(String food_description) {
        this.food_description = food_description;
    }

    public String getFood_price() {
        return food_price;
    }

    public void setFood_price(String food_price) {
        this.food_price = food_price;
    }

    public String getFood_uuid() {
        return food_uuid;
    }

    public void setFood_uuid(String food_uuid) {
        this.food_uuid = food_uuid;
    }
}
