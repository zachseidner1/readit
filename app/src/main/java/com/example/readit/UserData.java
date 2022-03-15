package com.example.readit;

import java.util.ArrayList;

public class UserData {
    private String uid;
    private String highSchool;
    private int thanks;
    private String username;
    private ArrayList<Integer> likedPostIds;
    private ArrayList<String> likedCommentIds;

    public UserData(String uid){
        this.highSchool = null;
        this.thanks = 0;
        this.uid = uid;
        likedPostIds = new ArrayList<>();
        likedCommentIds = new ArrayList<>();
    }

    public UserData() {
        this.highSchool = null;
        this.thanks = 0;
        this.uid = null;
    }

    public ArrayList<String> getLikedCommentIds() {
        return likedCommentIds;
    }

    public void setLikedCommentIds(ArrayList<String> likedCommentIds) {
        this.likedCommentIds = likedCommentIds;
    }

    public ArrayList<Integer> getLikedPostIds() {
        return likedPostIds;
    }

    public void setLikedPostIds(ArrayList<Integer> likedPostIds) {
        this.likedPostIds = likedPostIds;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHighSchool() {
        return highSchool;
    }

    public void setHighSchool(String highSchool) {
        this.highSchool = highSchool;
    }

    public int getThanks() {
        return thanks;
    }

    public void setThanks(int thanks) {
        this.thanks = thanks;
    }
}
