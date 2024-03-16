package com.example.allodoc;

import java.util.List;

public class FilesResponse {
    private List<FileModel> data;

    public List<FileModel> getData() {
        return data;
    }

    public void setData(List<FileModel> data) {
        this.data = data;
    }
}