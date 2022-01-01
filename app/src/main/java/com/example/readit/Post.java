package com.example.readit;

public class Post {

    private String title;
    private String post;
    private String imageURL;

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", post='" + post + '\'' +
                ", imageURL='" + imageURL + '\'' +
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

    public Post(String title, String post, String imageURL) {
        this.title = title;
        this.post = post;
        this.imageURL = imageURL;
    }
}
