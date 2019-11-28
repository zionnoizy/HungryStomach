package com.example.hungrystomach.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//uploader; seller
public class Request {
    List<ShoppingCart> foodList = new ArrayList<>();
    String buyer_uid;
    String RequestDate;
    double grand_total;
    String my_status;
    String my_uid;
    //private String utensil;
    //private String pickup;
    long request_entry_no;
    String randomkey;



    public Request(){
        //Empty
    }
    public Request(List<ShoppingCart> foodList, String buyer_uid, String requestDate, double grand_total, String my_status, String my_uid, long request_entry_no, String randomkey) {
        this.foodList = foodList;
        this.buyer_uid = buyer_uid;
        RequestDate = requestDate;
        this.grand_total = grand_total;
        this.my_status = my_status;
        this.my_uid = my_uid;
        this.request_entry_no  = request_entry_no;
        this.randomkey = randomkey;
    }

    public List<ShoppingCart> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<ShoppingCart> foodList) {
        this.foodList = foodList;
    }

    public String getBuyer_uid() {
        return buyer_uid;
    }

    public void setBuyer_uid(String buyer_uid) {
        this.buyer_uid = buyer_uid;
    }

    public String getRequestDate() {
        return RequestDate;
    }

    public void setRequestDate(String requestDate) {
        RequestDate = requestDate;
    }

    public double getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(double grand_total) {
        this.grand_total = grand_total;
    }

    public String getMy_status() {
        return my_status;
    }

    public void setMy_status(String my_status) {
        this.my_status = my_status;
    }

    public String getMy_uid() {
        return my_uid;
    }

    public void setMy_uid(String my_uid) {
        this.my_uid = my_uid;
    }

    public long getRequest_entry_no() {
        return request_entry_no;
    }

    public void setRequest_entry_no(long request_entry_no) {
        this.request_entry_no = request_entry_no;
    }

    public String getRandomkey() {
        return randomkey;
    }

    public void setRandomkey(String randomkey) {
        this.randomkey = randomkey;
    }
}
