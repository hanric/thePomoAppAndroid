package com.example.android.thepomoappandroid.api.interfaces;

import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.dto.SessionDTO;
import com.example.android.thepomoappandroid.api.request.CreateGroupRequest;

import java.util.List;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Enric on 16/04/2015.
 */
public interface GroupsInterface {
    @GET("/Groups/{id}")
    void findById(@Path("id") int id, Callback<GroupDTO> callback);

    @POST("/Groups")
    void create(@Body CreateGroupRequest createGroupRequest, Callback<GroupDTO> callback);

    @DELETE("/Groups/{id}")
    void delete(@Path("id") int id, Callback<ResponseCallback> callback);

    @PUT("/Groups/{id}")
    void update(@Path("id") int id, @Body CreateGroupRequest createGroupRequest, Callback<GroupDTO> callback);

    @GET("/Groups/{id}/sessions")
    void getSessions(@Path("id") int id, Callback<List<SessionDTO>> callback);
}
