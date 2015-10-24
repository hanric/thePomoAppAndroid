package com.example.android.thepomoappandroid.api.services;

import com.example.android.thepomoappandroid.api.dto.SessionDTO;
import com.example.android.thepomoappandroid.api.interfaces.SessionsInterface;

import retrofit.Callback;
import retrofit.ResponseCallback;

/**
 * Created by Enric on 22/04/2015.
 */
public class SessionsService extends BaseService{
    private static SessionsService ourInstance = new SessionsService();

    public static SessionsService getInstance() {
        return ourInstance;
    }

    private SessionsService() {
        super();
    }

    public void findById(String token, int id, Callback<SessionDTO> callback) {
        setAuthInterceptor(token);
        SessionsInterface sessionsInterface = restAdapter.create(SessionsInterface.class);
        sessionsInterface.findById(id, callback);
    }

    public void create(String token, SessionDTO sessionDTO, Callback<SessionDTO> callback) {
        setAuthInterceptor(token);
        SessionsInterface sessionsInterface = restAdapter.create(SessionsInterface.class);
        sessionsInterface.create(sessionDTO, callback);
    }

    public void delete(String token, int id, Callback<ResponseCallback> callback) {
        setAuthInterceptor(token);
        SessionsInterface sessionsInterface = restAdapter.create(SessionsInterface.class);
        sessionsInterface.delete(id, callback);
    }
}
