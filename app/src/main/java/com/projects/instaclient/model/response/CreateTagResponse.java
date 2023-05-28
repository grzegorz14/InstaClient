package com.projects.instaclient.model.response;

public class CreateTagResponse {
    private String message;
    private String tag;

    public CreateTagResponse(String message, String tag) {
        this.message = message;
        this.tag = tag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
