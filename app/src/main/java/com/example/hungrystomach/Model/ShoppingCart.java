package com.example.hungrystomach.Model;

import java.text.NumberFormat;

public class ShoppingCart {
    private String product_name, description, productID;
    private String product_price;
    private int quantity;
    public String img_url;

    public ShoppingCart() {
        //empty
    }

    public ShoppingCart(String product_name, String product_price, int quantity , String img_url){
        //this.productID = productId_position;
        this.product_name = product_name;
        this.product_price = product_price;
        this.quantity = quantity;
        this.img_url = img_url;

    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_price() {
        return product_price; //NumberFormat.getCurrencyInstance().format(price)
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getProductID() {
        return productID;
    }

    public int setQuantity(){
        return quantity;
    }
}
