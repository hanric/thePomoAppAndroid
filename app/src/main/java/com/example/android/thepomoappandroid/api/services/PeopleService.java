package com.example.android.thepomoappandroid.api.services;

import android.content.Context;

import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.dto.PersonDTO;
import com.example.android.thepomoappandroid.api.dto.SettingDTO;
import com.example.android.thepomoappandroid.api.interfaces.PeopleInterface;
import com.example.android.thepomoappandroid.api.request.LoginRequest;
import com.example.android.thepomoappandroid.api.request.RegisterRequest;
import com.example.android.thepomoappandroid.api.response.LoginResponse;
import com.google.android.gms.plus.model.people.Person;
import com.squareup.okhttp.Call;

import java.util.List;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Enric on 22/04/2015.
 */
public class PeopleService extends BaseService {

    private static PeopleService ourInstance = new PeopleService();

    public static PeopleService getInstance() {
        return ourInstance;
    }

    private PeopleService() {
        super();
    }

    public void login(String email, String password, Callback<LoginResponse> callback) {
        PeopleInterface peopleInterface = restAdapter.create(PeopleInterface.class);
        LoginRequest loginRequest = new LoginRequest(email, password, ttl);
        peopleInterface.login(loginRequest, callback);
    }

    public void register(String email, String username, String password, Callback<PersonDTO> callback) {
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);
        PeopleInterface peopleInterface = restAdapter.create(PeopleInterface.class);
        peopleInterface.register(registerRequest, callback);
    }

    public void logout(Context context, Callback<ResponseCallback> callback) {
//        setAuthInterceptor(context);
        PeopleInterface peopleInterface = restAdapter.create(PeopleInterface.class);
        peopleInterface.logout(callback);
    }

    public void findByFilter(String token, String constraint, Callback<List<PersonDTO>> callback) {
        setAuthInterceptor(token);
        String finalConstraint = constraint + "%";
        PeopleInterface peopleInterface = restAdapter.create(PeopleInterface.class);
        peopleInterface.findByFilter(finalConstraint, finalConstraint, callback);
    }

    public void getGroups(int id, String token, Callback<List<GroupDTO>> callback) {
        setAuthInterceptor(token);
        PeopleInterface peopleInterface = restAdapter.create(PeopleInterface.class);
        peopleInterface.getGroups(id, callback);
    }

    public void getSettings(Context context, Callback<List<SettingDTO>> callback) {
//        setAuthInterceptor(context);
        PeopleInterface peopleInterface = restAdapter.create(PeopleInterface.class);
        peopleInterface.getSettings(Utils.getUserId(context), callback);
    }
}
