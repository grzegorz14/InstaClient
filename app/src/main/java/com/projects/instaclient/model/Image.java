package com.projects.instaclient.model;

public class Image {
    private String id;
    private String album;
    private String originalName;
    private String url;
    private String lastChange;
    private ImageHistory[] history;

    public Image(String id, String album, String originalName, String url, String lastChange, ImageHistory[] history) {
        this.id = id;
        this.album = album;
        this.originalName = originalName;
        this.url = url;
        this.lastChange = lastChange;
        this.history = history;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getUrl() {
        return url.replace("\\", "/");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLastChange() {
        return lastChange;
    }

    public void setLastChange(String lastChange) {
        this.lastChange = lastChange;
    }

    public ImageHistory[] getHistory() {
        return history;
    }

    public void setHistory(ImageHistory[] history) {
        this.history = history;
    }
}
