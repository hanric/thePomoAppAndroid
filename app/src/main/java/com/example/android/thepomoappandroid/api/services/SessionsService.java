package com.example.android.thepomoappandroid.api.services;

import com.example.android.thepomoappandroid.api.dto.SessionDTO;
import com.example.android.thepomoappandroid.api.interfaces.SessionsInterface;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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

    public interface OnDeleteSession {
        void onDeleteSession();
    }

    public interface OnCreateSession {
        void onCreateSession();
    }

    public void create(String token, SessionDTO sessionDTO, final OnCreateSession onCreateSession) {
        setAuthInterceptor(token);
        SessionsInterface sessionsInterface = restAdapter.create(SessionsInterface.class);
        sessionsInterface.create(sessionDTO, new Callback<SessionDTO>() {
            @Override
            public void success(SessionDTO sessionDTO, Response response) {
                if (onCreateSession != null) onCreateSession.onCreateSession();
            }

            @Override
            public void failure(RetrofitError error) {
                if (onCreateSession != null) ((OnRetrofitError)onCreateSession).onError(error);
            }
        });
    }

    public void delete(String token, int id, final OnDeleteSession onDeleteSession) {
        setAuthInterceptor(token);
        SessionsInterface sessionsInterface = restAdapter.create(SessionsInterface.class);
        sessionsInterface.delete(id, new Callback<ResponseCallback>() {
            @Override
            public void success(ResponseCallback responseCallback, Response response) {
                if (onDeleteSession != null) onDeleteSession.onDeleteSession();
            }

            @Override
            public void failure(RetrofitError error) {
                if (onDeleteSession != null) ((OnRetrofitError) onDeleteSession).onError(error);
            }
        });
    }
}
