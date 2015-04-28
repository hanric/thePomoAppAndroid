package com.example.android.thepomoappandroid.api.interfaces;

import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.dto.PersonDTO;
import com.example.android.thepomoappandroid.api.dto.SettingDTO;
import com.example.android.thepomoappandroid.api.request.LoginRequest;
import com.example.android.thepomoappandroid.api.response.LoginResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Enric on 16/04/2015.
 */
public interface PeopleInterface {
    @GET("/People/{id}")
    void findById(@Path("id") int id, Callback<PersonDTO> callback);

    @POST("/People/login")
    void login(@Body LoginRequest loginRequest, Callback<LoginResponse> callback);

    @POST("/People/logout")
    void logout(ResponseCallback responseCallback);

    // /People/{id}/groups?filter={"include":["people"]}"
    @GET("/People/{id}/groups?filter=%7B%22include%22:%5B%22people%22%5D%7D")
    void getGroups(@Path("id") int id, Callback<List<GroupDTO>> callback);

    @GET("/People/{id}/settings")
    void getSettings(@Path("id") int id, Callback<List<SettingDTO>> callback);
}
