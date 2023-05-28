package com.projects.instaclient.model.response;

import com.projects.instaclient.model.Post;

public class AddPostResponse {
    private boolean success;
    private Post newPost;

    public AddPostResponse(boolean success, Post newPost) {
        this.success = success;
        this.newPost = newPost;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Post getNewPost() {
        return newPost;
    }

    public void setNewPost(Post newPost) {
        this.newPost = newPost;
    }
}
