package com.example.hungrystomach.Model;

import java.text.NumberFormat;

public class ShoppingCart {
    private String name, description, productID;
    private int price, quantity;

    public ShoppingCart(String productId, int price, String name, String description, int quantity){
        this.productID = productId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public String getProductID() {
        return productID;
    }

    public String getTitle() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return NumberFormat.getCurrencyInstance().format(price);
    }
}
