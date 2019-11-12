package com.example.hungrystomach.Model;

import java.util.ArrayList;
import java.util.List;

public class Invoice {
    String buyer;
    String billdate;
    double grand_total;
    int invoice_num;
    int discount;
    //List<Food> foodList = new ArrayList<>();


    public Invoice(String buyer, String billdate, double grand_total, int discount) { //String typeOfPymt, int tax, List<Food> foodList, boolean isPickup, , int total
        this.buyer = buyer;
        this.billdate = billdate;
        this.grand_total = grand_total;
        this.discount = discount;
    }

    public String getBuyer() {
        return buyer;
    }

    public String getBillDate() {
        return billdate;
    }


    public double getDiscount() {
        return discount;
    }

    public double getGrandTotal() {
        return grand_total;
    }
}
