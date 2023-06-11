package com.projects.instaclient.model;

import android.os.Build;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Post {
    private String id;
    private SimpleUser simpleUser;
    private Image image;
    private String description;
    private String location;
    private ArrayList<String> tags;
    private int likes;
    private String date;

    // only for sending edit post patch
    public Post(String id, String description, String location, ArrayList<String> tags) {
        this.id = id;
        this.description = description;
        this.location = location;
        this.tags = tags;
    }

    public Post(String id, SimpleUser simpleUser, Image image, String description, String location, ArrayList<String> tags, int likes) {
        this.id = id;
        this.simpleUser = simpleUser;
        this.image = image;
        this.description = description;
        this.location = location;
        this.tags = tags;
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

    public SimpleUser getSimpleUser() {
        return simpleUser;
    }

    public void setSimpleUser(SimpleUser simpleUser) {
        this.simpleUser = simpleUser;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
        //call
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostDescription() {
        if (this.getDescription().isEmpty()) {
            return "";
        }
        return this.getSimpleUser().getFirstName() + " - " + this.getDescription();
    }
}
