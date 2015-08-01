package com.example.android.thepomoappandroid.api.interfaces;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.http.DELETE;
import retrofit.http.Path;

/**
 * Created by Enric on 01/08/2015.
 */
public interface InstallationsInterface {

    @DELETE("/installations/{id}")
    void delete(@Path("id") int id, Callback<ResponseCallback> callback);
}
