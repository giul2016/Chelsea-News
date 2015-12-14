package com.example.lukaszwieczorek.chelseanews;

public class Portal {
    private String url;
    private String filename;
    private String buttonId;

    public Portal(String url, String filename, String buttonId) {
        this.url = url;
        this.filename = filename;
        this.buttonId = buttonId;
    }

    public String getUrl() {
        return url;
    }

    public String getFilename() {
        return filename;
    }

    public String getButtonId() {
        return buttonId;
    }
}
