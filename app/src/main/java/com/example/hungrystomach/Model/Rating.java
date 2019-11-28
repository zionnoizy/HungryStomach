package com.example.hungrystomach.Model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class Rating {
    String product_name;
    String price;
    String uploader_uid;
    String uri;
    String description;
    String uploader;
    List<ShoppingCart> foodList = new ArrayList<>();

    public Rating(){
        //Empty
    }

    public Rating(List<ShoppingCart> foodList) {
        this.foodList = foodList;
    }

    /*
    @Exclude
    public List<ShoppingCart> toList() {
        List<ShoppingCart> result = new ArrayList<>();
        result.put("product_name", product_name);
        result.put("price", author);
        result.put("uploader_uid", title);
        result.put("uri", body);
        result.put("description", starCount);
        result.put("uploader", stars);

        return result;
    }
    */
}
