package com.projects.instaclient.model;

import android.os.Build;

import java.time.LocalDateTime;

public class Post {
    private String id;
    private User user;
    private int image;
    private String description;
    private int likes;
    private String date;

    public Post(User user, int image, String description) {
        this.user = user;
        this.image = image;
        this.description = description;
        this.likes = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.date = LocalDateTime.now().toString();
        }
    }

    public Post(User user, int image, String description, int likes) {
        this.user = user;
        this.image = image;
        this.description = description;
        this.likes = likes;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.date = LocalDateTime.now().toString();
        }
    }

    public Post(String id, User user, int image, String description, int likes) {
        this.id = id;
        this.user = user;
        this.image = image;
        this.description = description;
        this.likes = likes;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.date = LocalDateTime.now().toString();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
