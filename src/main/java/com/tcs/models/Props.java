package com.tcs.models;

public class Props {
    private String filename;
    public Props(){}

    public Props(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
