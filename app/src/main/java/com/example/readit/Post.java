package com.example.readit;

import java.util.Comparator;

public class Post {

    private String title;
    private String post;
    private String imageURL;

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
