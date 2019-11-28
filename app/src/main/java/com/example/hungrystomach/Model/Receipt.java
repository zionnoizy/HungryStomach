package com.example.hungrystomach.Model;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private String my_uid;
    private String my_name;
    private String uuid;
    private double grand_total;
    private String billDate;
    private int discount;
    List<ShoppingCart> foodList = new ArrayList<>();
    private String his_status;
    private int receipt_number;
    //private String utensil;
    //private String pickup;
    private String random_key;


    public Receipt(){
        //Empty
    }

    public Receipt(String my_uid, String my_name, String uuid, double grand_total, String billDate, int discount, List<ShoppingCart> foodList, String his_status, int receipt_number, String random_key) {
        this.my_uid = my_uid;
        this.my_name = my_name;
        this.uuid = uuid;
        this.grand_total = grand_total;
        this.billDate = billDate;
        this.discount = discount;
        this.foodList = foodList;
        this.his_status = his_status;
        this.receipt_number = receipt_number;
        this.random_key = random_key;
    }

    public String getMy_uid() {
        return my_uid;
    }

    public void setMy_uid(String my_uid) {
        this.my_uid = my_uid;
    }

    public String getMy_name() {
        return my_name;
    }

    public void setMy_name(String my_name) {
        this.my_name = my_name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public double getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(double grand_total) {
        this.grand_total = grand_total;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public List<ShoppingCart> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<ShoppingCart> foodList) {
        this.foodList = foodList;
    }

    public String getHis_status() {
        return his_status;
    }

    public void setHis_status(String his_status) {
        this.his_status = his_status;
    }

    public int getReceipt_number() {
        return receipt_number;
    }

    public void setReceipt_number(int receipt_number) {
        this.receipt_number = receipt_number;
    }

    public String getRandom_key() {
        return random_key;
    }

    public void setRandom_key(String random_key) {
        this.random_key = random_key;
    }
}
