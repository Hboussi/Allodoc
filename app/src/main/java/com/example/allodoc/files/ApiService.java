package com.example.allodoc.files;

import com.example.allodoc.files.FileResponse;
import com.example.allodoc.files.FilesResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("files")
    Call<FilesResponse> getFiles();

    @GET("files/{id}")
    Call<FileResponse> getFileDetails(@Path("id") int fileId);

    @Multipart
    @POST("files")
    Call<FileResponse> uploadFile(
            @Part MultipartBody.Part file,
            @Part("description") RequestBody description,
            @Part("folder_id") RequestBody folderId
    );

    @FormUrlEncoded
    @POST("files/folder")
    Call<FilesResponse> getFilesByFolderId(@Field("folder_id") String folderId);

    @DELETE("files/{id}")
    Call<Void> deleteFile(@Path("id") int fileId);


}
