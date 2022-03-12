package com.example.readit;

public class Comment {

    private String comment;
    private int postID;
    private String username;
    private int thanks;


    public Comment(){

    }

    public Comment(String comment, int postID, String uID, int thanks) {
        this.comment = comment;
        this.postID = postID;
        this.username = uID;
        this.thanks = thanks;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getThanks() { return thanks; }

    public void setThanks(int thanks) { this.thanks = thanks; }

}