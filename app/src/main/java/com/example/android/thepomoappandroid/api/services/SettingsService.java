package com.example.android.thepomoappandroid.api.services;

import com.example.android.thepomoappandroid.api.dto.SettingDTO;
import com.example.android.thepomoappandroid.api.interfaces.SettingsInterface;

import retrofit.Callback;
import retrofit.ResponseCallback;

/**
 * Created by Enric on 22/04/2015.
 */
public class SettingsService extends BaseService {
    private static SettingsService ourInstance = new SettingsService();

    public static SettingsService getInstance() {
        return ourInstance;
    }

    private SettingsService() {
        super();
    }

    public void create(String token, SettingDTO settingDTO, Callback<SettingDTO> callback) {
        setAuthInterceptor(token);
        SettingsInterface settingsInterface = restAdapter.create(SettingsInterface.class);
        settingsInterface.create(settingDTO, callback);
    }

    public void findById(String token, int id, Callback<SettingDTO> callback) {
        setAuthInterceptor(token);
        SettingsInterface settingsInterface = restAdapter.create(SettingsInterface.class);
        settingsInterface.findById(id, callback);
    }

    public void update(String token, int id, SettingDTO settingDTO, Callback<SettingDTO> callback) {
        setAuthInterceptor(token);
        SettingsInterface settingsInterface = restAdapter.create(SettingsInterface.class);
        settingsInterface.update(id, settingDTO, callback);
    }

    public void delete(String token, int id, Callback<ResponseCallback> callback) {
        setAuthInterceptor(token);
        SettingsInterface sessionsInterface = restAdapter.create(SettingsInterface.class);
        sessionsInterface.delete(id, callback);
    }

}
