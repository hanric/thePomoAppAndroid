package com.example.android.thepomoappandroid.api.interfaces;

import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.dto.PersonDTO;
import com.example.android.thepomoappandroid.api.dto.SettingDTO;
import com.example.android.thepomoappandroid.api.request.LoginRequest;
import com.example.android.thepomoappandroid.api.request.RegisterRequest;
import com.example.android.thepomoappandroid.api.response.LoginResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Enric on 16/04/2015.
 */
public interface PeopleInterface {

    // /People?filter[limit]=10&filter[where][username][like]={constraint%}&filter[where][email][like]={constraint%}
    @GET("/People?filter%5Blimit%5D%3D10")
    void findByFilter(@Query("filter%5Bwhere%5D%5Bor%5D%5B0%5D%5Busername%5D%5Blike%5D") String constraintUsername, @Query("ffilter%5Bwhere%5D%5Bor%5D%5B1%5D%5Bemail%5D%5Blike%5D") String constraintEmail, Callback<List<PersonDTO>> callback);

    @GET("/People/{id}")
    void findById(@Path("id") int id, Callback<PersonDTO> callback);

    @POST("/People")
    void register(@Body RegisterRequest registerRequest, Callback<PersonDTO> callback);

    @POST("/People/login")
    void login(@Body LoginRequest loginRequest, Callback<LoginResponse> callback);

    @POST("/People/logout")
    void logout(Callback<ResponseCallback> callback);

    // /People/{id}/groups?filter={"include":["people"]}
    @GET("/People/{id}/groups?filter=%7B%22include%22:%5B%22people%22%5D%7D")
    void getGroups(@Path("id") int id, Callback<List<GroupDTO>> callback);

    @GET("/People/{id}/settings")
    void getSettings(@Path("id") int id, Callback<List<SettingDTO>> callback);
}
