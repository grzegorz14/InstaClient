package com.projects.instaclient.model.response;

import com.projects.instaclient.model.Post;
import com.projects.instaclient.model.PostFINAL;

public class AddPostResponse {
    private boolean success;
    private PostFINAL newPost;

    public AddPostResponse(boolean success, PostFINAL newPost) {
        this.success = success;
        this.newPost = newPost;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public PostFINAL getNewPost() {
        return newPost;
    }

    public void setNewPost(PostFINAL newPost) {
        this.newPost = newPost;
    }
}
