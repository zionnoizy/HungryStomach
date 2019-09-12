package com.example.hungrystomach.model;
//

public class food_information {
    public String m_name;
    public String m_description;
    public String m_price;
    public food_information(){
        //empty
    }



    public food_information(String name, String description,String price) {

        this.m_name = name;
        this.m_description= description;
        this.m_price = price;
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

}