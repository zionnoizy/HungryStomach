package com.example.hungrystomach.Model;

import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private String buyer;
    private String billDate;
    private double grand_total;
    private int discount;
    private String uuid;
    //List<Food> foodList = new ArrayList<>();


    public Invoice(String buyer, String billDate, double grand_total, int discount, String uuid) { //String typeOfPymt, int tax, List<Food> foodList, boolean isPickup, , int total
        this.buyer = buyer;
        this.billDate = billDate;
        this.grand_total = grand_total;
        this.discount = discount;
        this.uuid = uuid;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getBillDate() {
        return billDate;
    }


    public void setBillDate(String billdate) {
        this.billDate = billdate;
    }

    public double getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(double grand_total) {
        this.grand_total = grand_total;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
