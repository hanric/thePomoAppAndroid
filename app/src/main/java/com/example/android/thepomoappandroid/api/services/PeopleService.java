package com.example.android.thepomoappandroid.api.services;

import android.content.Context;

import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.dto.SettingDTO;
import com.example.android.thepomoappandroid.api.interfaces.PeopleInterface;
import com.example.android.thepomoappandroid.api.request.LoginRequest;
import com.example.android.thepomoappandroid.api.response.LoginResponse;

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

    public interface OnLogin {
        void onLogin(LoginResponse loginResponse);
    }

    public interface OnGetGroups {
        void onGetGroups(List<GroupDTO> groupDTOs);
    }

    public void login(String email, String password, final OnLogin onLogin) {
        PeopleInterface peopleInterface = restAdapter.create(PeopleInterface.class);
        LoginRequest loginRequest = new LoginRequest(email, password, ttl);
        peopleInterface.login(loginRequest, new Callback<LoginResponse>() {
            @Override
            public void success(LoginResponse loginResponse, Response response) {
                if (onLogin != null) onLogin.onLogin(loginResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                if (onLogin != null) ((OnRetrofitError) onLogin).onError(error);
            }
        });
    }

    public void logout(Context context, ResponseCallback responseCallback) {
//        setAuthInterceptor(context);
        PeopleInterface peopleInterface = restAdapter.create(PeopleInterface.class);
        peopleInterface.logout(responseCallback);
    }

    public void getGroups(int id, String token, final OnGetGroups onGetGroups) {
        setAuthInterceptor(token);
        PeopleInterface peopleInterface = restAdapter.create(PeopleInterface.class);
        peopleInterface.getGroups(id, new Callback<List<GroupDTO>>() {
            @Override
            public void success(List<GroupDTO> groupDTOs, Response response) {
                if (onGetGroups != null) onGetGroups.onGetGroups(groupDTOs);
            }

            @Override
            public void failure(RetrofitError error) {
                if (onGetGroups != null) ((OnRetrofitError) onGetGroups).onError(error);
            }
        });
    }

    public void getSettings(Context context, Callback<List<SettingDTO>> callback) {
//        setAuthInterceptor(context);
        PeopleInterface peopleInterface = restAdapter.create(PeopleInterface.class);
        peopleInterface.getSettings(Utils.getInstance().getUserId(context), callback);
    }
}