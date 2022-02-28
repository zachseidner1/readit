package com.example.readit;

import java.util.Comparator;

public class Post {

    private String title;
    private String post;
    private String course;
    private String uid;
    private boolean question;
    private int thanks;
    private int postId;
    private int comments;
    private String highSchool;
    //call it class in the app //add a subtitle to the layout item xml thing //store firebase // on all of the posts make sure to use strings.xml for like the class names and stuff //add some more history courses like honors history and normal history and stuff blah blah blah //tie each post to an account somehow (add 'private String userID' as a field) and then the thanks will just be an int or something



    public Post(String title, String post, String course, String highSchool, boolean question, String uid) {
        this.title = title;
        this.post = post;
        this.course = course;
        this.uid = uid;
        this.question = question;
        this.postId = (int) Math.floor(Math.random() * Integer.MAX_VALUE);
        this.highSchool = highSchool;
        this.comments = 0;
        this.thanks = 0;
    }
    public  Post(){

    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
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

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public static Comparator<Post> PostNameAZComparator = new Comparator<Post>() {
        @Override
        public int compare(Post p1, Post p2) {
            return p1.getTitle().compareTo(p2.getTitle());
        }
    };

    public static Comparator<Post> PostNameZAComparator = new Comparator<Post>() {
        @Override
        public int compare(Post p1, Post p2) {
            return p2.getTitle().compareTo(p1.getTitle());
        }
    };

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", post='" + post + '\'' +
                ", class='" + course + '\'' +
                ", uid='" + uid + '\'' +
                ", question='" + question + '\'' +
                '}';
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) { this.course = course; }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) { this.uid = uid; }

    public boolean getQuestion() { return  question; }

    public void setQuestion(boolean question) { this.question = question; }

}