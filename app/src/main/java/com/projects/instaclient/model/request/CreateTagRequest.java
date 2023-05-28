package com.projects.instaclient.model.request;

public class CreateTagRequest {
    private String name;

    public CreateTagRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

