package com.example.readit;

public class UserData {
    private String uid;
    private String highSchool;
    private int thanks;

    public UserData(String uid, String highSchool, int thanks) {
        this.uid = uid;
        this.highSchool = highSchool;
        this.thanks = thanks;
    }
    public UserData(String uid){
        this.highSchool = null;
        this.thanks = 0;
        this.uid = uid;
    }

    public UserData() {
        this.highSchool = null;
        this.thanks = 0;
        this.uid = null;
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
