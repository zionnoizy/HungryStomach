package com.example.hungrystomach.Model;

public class User {

    private String id;
    private String username;
    private String icon;

    public User(String id, String username) {
        this.id = id;
        this.username = username;
        //this.imageURl = imageURl;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
