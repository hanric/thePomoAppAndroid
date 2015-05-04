package com.example.android.thepomoappandroid.api.interfaces;

import com.example.android.thepomoappandroid.api.dto.SessionDTO;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Enric on 16/04/2015.
 */
public interface SessionsInterface {

    @POST("/Sessions")
    void create(@Body SessionDTO sessionDTO, Callback<SessionDTO> callback);

    @DELETE("/Sessions/{id}")
    void delete(@Path("id") int id, Callback<ResponseCallback> responseCallbackCallback);
}
