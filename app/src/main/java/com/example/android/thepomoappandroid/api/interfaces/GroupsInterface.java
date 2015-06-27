package com.example.android.thepomoappandroid.api.interfaces;

import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.dto.SessionDTO;
import com.example.android.thepomoappandroid.api.request.CreateGroupRequest;
import com.example.android.thepomoappandroid.api.dto.LinkPeopleDTO;

import java.util.List;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Enric on 16/04/2015.
 */
public interface GroupsInterface {
    // /Groups/findOne?filter[include]=people&filter[where][id]={id}

    @GET("/Groups/findOne?filter%5Binclude%5D%3Dpeople")
    void findById(@Query("filter%5Bwhere%5D%5Bid%5D") int id, Callback<GroupDTO> callback);

    @POST("/Groups")
    void create(@Body CreateGroupRequest createGroupRequest, Callback<GroupDTO> callback);

    @DELETE("/Groups/{id}")
    void delete(@Path("id") int id, Callback<ResponseCallback> callback);

    @PUT("/Groups/{id}")
    void update(@Path("id") int id, @Body CreateGroupRequest createGroupRequest, Callback<GroupDTO> callback);

    @GET("/Groups/{id}/sessions")
    void getSessions(@Path("id") int id, Callback<List<SessionDTO>> callback);

    @PUT("/Groups/{id}/people/rel/{fk}")
    void linkPeople(@Path("id") int id, @Path("fk") int fk, @Body LinkPeopleDTO linkPeopleDTO, Callback<LinkPeopleDTO> callback);
}
