package com.example.hungrystomach.Model;

public class Food {
    public String m_name;
    public String m_description;
    public String m_price;
    public String m_img_url;

    public Food() {
        //empty
    }

    public Food(String name, String description, String price) { //v

        this.m_name = name;
        this.m_description = description;
        this.m_price = price;
        //this.m_img_url = img;

    }


    public String get_name() {
        return m_name;
    }


    public String get_description() {
        return m_description;
    }


    public String get_price() {
        return m_price;
    }


    public String get_imgurl() {
        return m_img_url;
    }
    public void setImageUrl(String imageUrl) {
        m_img_url = imageUrl;
    }
}
