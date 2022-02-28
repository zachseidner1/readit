package com.example.readit;

public class Comment {

    private String comment;
    private String postID;
    private String username;


    public Comment(){

    }

    public Comment(String comment, String postID, String uID) {
        this.comment = comment;
        this.postID = postID;
        this.username = uID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}