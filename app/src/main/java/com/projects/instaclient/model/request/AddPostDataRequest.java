package com.projects.instaclient.model.request;

import java.util.ArrayList;

public class AddPostDataRequest {
    private String description;
    private String location;
    private ArrayList<String> tags;

    public AddPostDataRequest(String description, String location, ArrayList<String> tags) {
        this.description = description;
        this.location = location;
        this.tags = tags;
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
}
