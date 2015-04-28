package com.example.android.thepomoappandroid.api.services;

/**
 * Created by Enric on 22/04/2015.
 */
public class SessionsService {
    private static SessionsService ourInstance = new SessionsService();

    public static SessionsService getInstance() {
        return ourInstance;
    }

    private SessionsService() {
    }
}
