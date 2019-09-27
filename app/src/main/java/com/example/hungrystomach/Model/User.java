package com.example.hungrystomach.Model;

public class User {

    private String email;
    private String username;
    private String id;
    private String icon;

    public User(String email, String username, String id) {
        this.email = username;
        this.username = username;
        this.id = id;

        //this.imageURl = imageURl;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
