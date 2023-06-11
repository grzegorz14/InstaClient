package com.projects.instaclient.model.request;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class AddPostDataRequest {
    private String description;
    private String location;
    private ArrayList<String> tags;
    private String date;

    public AddPostDataRequest(String description, String location, ArrayList<String> tags, String date) {
        this.description = description;
        this.location = location;
        this.tags = tags;
        this.date = date;
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

    public String getDateTime() {
        return date;
    }

    public void setDateTime(String date) {
        this.date = date;
    }
}
