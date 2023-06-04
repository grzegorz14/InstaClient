package com.projects.instaclient.model.request;

public class EditImageRequest {
    private String id;
    private String operation;
    private int rotate;

    public EditImageRequest(String id, String operation, int rotate) {
        this.id = id;
        this.operation = operation;
        this.rotate = rotate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }
}
