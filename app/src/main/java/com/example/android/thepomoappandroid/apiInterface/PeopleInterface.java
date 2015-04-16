package com.example.android.thepomoappandroid.apiInterface;

import com.example.android.thepomoappandroid.dto.PersonDTO;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Enric on 16/04/2015.
 */
public interface PeopleInterface {
    @GET("/People/{id}")
    void findById(@Path("id") int id, Callback<PersonDTO> callback);
}
