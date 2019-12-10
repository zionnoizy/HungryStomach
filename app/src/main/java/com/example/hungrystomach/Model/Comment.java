package com.example.hungrystomach.Model;

public class Comment {
    private float rating_star;
    private String comment;

    public Comment(float rating_star, String comment) {
        this.rating_star = rating_star;
        this.comment = comment;
    }

    public float getRating_star() {
        return rating_star;
    }

    public void setRating_star(float rating_star) {
        this.rating_star = rating_star;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
