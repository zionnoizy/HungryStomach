package com.example.hungrystomach.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Request {
    List<ShoppingCart> foodList = new ArrayList<>();
    String uuid;
    String RequestDate;

    public Request(List<ShoppingCart> foodList, String uuid, String RequestDate) {
        this.foodList = foodList;
        this.uuid = uuid;
        this.RequestDate = RequestDate;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<ShoppingCart> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<ShoppingCart> foodList) {
        this.foodList = foodList;
    }

    public String getRequestDate() {
        return RequestDate;
    }

    public void setRequestDate(String requestDate) {
        RequestDate = requestDate;
    }
}
