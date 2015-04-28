package com.example.android.thepomoappandroid.api.services;

import com.example.android.thepomoappandroid.Constants;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by Enric on 22/04/2015.
 */
public abstract class BaseService {

    protected RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(Constants.API_ENDPONT)
            .build();

    protected RequestInterceptor authInterceptor;
    protected String ttl = "31556926";

    public interface OnRetrofitError {
        void onError(RetrofitError error);
    }

    protected void setAuthInterceptor(final String token) {
        authInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addQueryParam("access_token", token);
            }
        };
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.API_ENDPONT)
                .setRequestInterceptor(authInterceptor)
                .build();
    }
}
