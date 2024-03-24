package com.example.allodoc;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TokenService {
    @FormUrlEncoded
    @POST("tokens")
    Call<Void> sendTokenRequest(@Field("expiration") String expirationTime, @Field("folder_id") int selectedFolderId);
}
