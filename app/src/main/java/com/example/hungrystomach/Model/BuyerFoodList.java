package com.example.hungrystomach.Model;

public class BuyerFoodList {
    String name, product_price, url, cooker_uid;
    int quantity;
    double sub_total;
    String random_key;

    public BuyerFoodList() {
        //empty
    }

    public BuyerFoodList(String name, String product_price, String url, String cooker_uid, int quantity, double sub_total, String random_key) {
        this.name = name;
        this.product_price = product_price;
        this.url = url;
        this.cooker_uid = cooker_uid;
        this.quantity = quantity;
        this.sub_total = sub_total;
        this.random_key = random_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCooker_uid() {
        return cooker_uid;
    }

    public void setCooker_uid(String cooker_uid) {
        this.cooker_uid = cooker_uid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSub_total() {
        return sub_total;
    }

    public void setSub_total(double sub_total) {
        this.sub_total = sub_total;
    }

    public String getRandom_key() {
        return random_key;
    }

    public void setRandom_key(String random_key) {
        this.random_key = random_key;
    }
}
