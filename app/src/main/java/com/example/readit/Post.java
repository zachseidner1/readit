package com.example.readit;

import java.util.Comparator;

public class Post {

    private String title;
    private String post;
    private String imageURL;
    private String course;
    private String userID;
    private boolean question;
    //call it class in the app //add a subtitle to the layout item xml thing //store firebase // on all of the posts make sure to use strings.xml for like the class names and stuff //add some more history courses like honors history and normal history and stuff blah blah blah //tie each post to an account somehow (add 'private String userID' as a field) and then the thanks will just be an int or something

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
                ", imageURL='" + imageURL + '\'' +
                ", class='" + course + '\'' +
                ", class='" + userID + '\'' +
                ", class='" + question + '\'' +
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) { this.course = course; }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) { this.userID = userID; }

    public boolean getQuestion() { return  question; }

    public void setQuestion(boolean question) { this.question = question; }

    public Post(String title, String post, String imageURL, String course, String userID, boolean question) {
        this.title = title;
        this.post = post;
        this.imageURL = imageURL;
        this.course = course;
        this.userID = userID;
        this.question = question;
    }
}
