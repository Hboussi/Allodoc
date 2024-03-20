package com.example.allodoc.files;

public class
FileModel {
    private int id;
    private String path;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FileModel(int id, String path, String description) {
        this.id = id;
        this.path = path;
        this.description = description;
    }
}
