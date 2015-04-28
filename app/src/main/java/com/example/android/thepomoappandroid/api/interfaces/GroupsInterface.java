package com.example.android.thepomoappandroid.api.interfaces;

import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.request.CreateGroupRequest;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Enric on 16/04/2015.
 */
public interface GroupsInterface {
    @GET("/Groups/{id}")
    void findById(@Path("id") int id, Callback<GroupDTO> callback);

    @POST("/Groups")
    void create(@Body CreateGroupRequest createGroupRequest, Callback<GroupDTO> callback);
}
