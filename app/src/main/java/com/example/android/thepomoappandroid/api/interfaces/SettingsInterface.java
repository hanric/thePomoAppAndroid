package com.example.android.thepomoappandroid.api.interfaces;

import com.example.android.thepomoappandroid.api.dto.SettingDTO;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Enric on 16/04/2015.
 */
public interface SettingsInterface {

    @GET("/Settings")
    void create(@Body SettingDTO settingDTO, Callback<SettingDTO> callback);

    @GET("/Settings/{id}")
    void findById(@Path("id") int id, Callback<SettingDTO> callback);

    @DELETE("/Settings/{id}")
    void delete(@Path("id") int id, Callback<ResponseCallback> callback);

    @PUT("/Settings/{id}")
    void update(@Path("id") int id, @Body SettingDTO settingDTO, Callback<SettingDTO> callback);
}
