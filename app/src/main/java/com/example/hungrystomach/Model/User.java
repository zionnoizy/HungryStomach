package com.example.hungrystomach.Model;

public class User {

    private String email;
    private String username;
    private static int incrementor = 0;
    private long usr_id=0;
    private String icon;
    private String phone;
    private String address;
    private String state;
    private String city;
    private String zip;

    public User(String email, String username) { //, long usr_id, String icon, String phone, String address, String state, String city, String zip
        this.email = email;
        this.username = username;
        this.usr_id = incrementor++;
        /*
        this.icon = "default_icon";
        this.phone = "blank";
        this.address = "blank";
        this.state = "blank";
        this.city = "blank";
        this.zip = "blank";
        */
    }



    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return usr_id;
    }

    public void setId(int id) {
        this.usr_id = id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setPhone(String icon) {
        this.phone = icon;
    }

    public String getPhone() {
        return phone;
    }

    public void setAddress(String icon) {
        this.address = icon;
    }

    public String getAddress() {
        return address;
    }

    public void setState(String icon) {
        this.state = icon;
    }

    public String getState() {
        return state;
    }

    public void setCity(String icon) {
        this.city = icon;
    }

    public String getCity() {
        return city;
    }

    public void setZip(String icon) {
        this.zip = icon;
    }

    public String getZip() {
        return zip;
    }

}
