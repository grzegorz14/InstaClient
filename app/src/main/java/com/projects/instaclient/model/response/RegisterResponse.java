package com.projects.instaclient.model.response;

public class RegisterResponse {

    private String confirmLink;
    private String token;
    private String timeLimit;

    public RegisterResponse(String confirmLink, String token, String timeLimit) {
        this.confirmLink = confirmLink;
        this.token = token;
        this.timeLimit = timeLimit;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getConfirmLink() {
        return confirmLink;
    }

    public void setConfirmLink(String confirmLink) {
        this.confirmLink = confirmLink;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }
}
