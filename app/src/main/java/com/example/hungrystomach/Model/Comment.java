package com.example.hungrystomach.Model;

public class Comment {
    private float rating;
    private String comment;

    public Comment() {
        //empty
    }
    public Comment(float rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating_star(float rating_star) {
        this.rating = rating_star;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
