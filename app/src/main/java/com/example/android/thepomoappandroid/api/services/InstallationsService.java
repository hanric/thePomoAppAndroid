package com.example.android.thepomoappandroid.api.services;

import com.example.android.thepomoappandroid.api.interfaces.InstallationsInterface;

import retrofit.Callback;
import retrofit.ResponseCallback;

/**
 * Created by Enric on 01/08/2015.
 */
public class InstallationsService extends BaseService {

    private static InstallationsService ourInstance = new InstallationsService();

    public static InstallationsService getInstance() {
        return ourInstance;
    }

    private InstallationsService() {
        super();
    }

    public void delete(int id, Callback<ResponseCallback> callback) {
        InstallationsInterface installationsInterface = restAdapter.create(InstallationsInterface.class);
        installationsInterface.delete(id, callback);
    }
}
