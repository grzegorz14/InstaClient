package com.projects.instaclient.model.response;

import com.projects.instaclient.model.User;

public class ConfirmResponse {
    private User user;
    private String message;

    public ConfirmResponse(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
