package com.example.allodoc.patient;

import java.util.List;

public class ApiFolderResponse<T> {
    private List<Folder> data;

    public List<Folder> getData() {
        return data;
    }

    public void setData(List<Folder> data) {
        this.data = data;
    }
}


