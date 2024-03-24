package com.example.allodoc.files;

import com.google.gson.annotations.SerializedName;

public class FolderIdRequest {
    @SerializedName("folder_id")
    private int folderId;

    public FolderIdRequest(int folderId) {
        this.folderId = folderId;
    }
}

