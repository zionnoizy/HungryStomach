package com.example.hungrystomach.Model;


public class ShoppingCart {
    private String product_name;
    private String product_price;
    private int quantity;
    public String img_url;
    private String modify_date;
    private double subtotal;
    private String usr_uid;

    public ShoppingCart() {
        //empty
    }

    public ShoppingCart(String product_name, String product_price, int quantity , String img_url, String modify_date, double subtotal, String usr_uid){
        this.product_name = product_name;
        this.product_price = product_price;
        this.quantity = quantity;
        this.img_url = img_url;
        this.modify_date = modify_date;
        this.subtotal = subtotal;
        this.usr_uid = usr_uid;

    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getModify_date() {
        return modify_date;
    }

    public double getSubtotal() {
        return subtotal;
    }


    public String getUsr_uid(){
        return usr_uid;
    }

}
