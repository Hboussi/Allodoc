package com.example.allodoc.patient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Query;

public interface FolderService {
    @GET("medecalfolders")
    Call<ApiFolderResponse> getFolders();

    @POST("medecalfolders")
    Call<Folder> addFolder(@Body Folder folder);


    @POST("FindFolderByPatientId")
    Call<List<Folder>> getFoldersByPatientId(@Body PatientIdRequest patientIdRequest);

}
