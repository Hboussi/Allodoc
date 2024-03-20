package com.example.allodoc;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @GET("com/example/allodoc/files")
    Call<FilesResponse> getFiles();

    @Multipart
    @POST("com/example/allodoc/files")
    Call<FileResponse> uploadFile(
            @Part MultipartBody.Part file,
            @Part("description") RequestBody description,
            @Part("folder_id") RequestBody folderId
    );
}
