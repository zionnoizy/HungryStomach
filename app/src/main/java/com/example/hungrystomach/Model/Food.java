package com.example.hungrystomach.Model;

public class Food {
    public String name;
    public String description;
    public String price;
    public String img_url;
    public String icon;

    public Food() {
        //empty
    }


    public Food(String name, String description, String price, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        this.name = name;
        this.description = description;
        this.price = price;
        this.img_url = imageUrl;
    }


    public String get_name() {
        return name;
    }


    public String get_description() {
        return description;
    }


    public String get_price() {
        return price;
    }
    public String get_icon() {
        return icon;
    }

    public String get_url() {
        return img_url;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setImageUrl(String imageUrl) {
        img_url = imageUrl;
    }

    public void set_icon(String icon) {
        this.icon = icon;
    }



}
