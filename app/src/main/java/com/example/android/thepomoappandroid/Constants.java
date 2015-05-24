package com.example.android.thepomoappandroid;

/**
 * Created by Enric on 22/04/2015.
 */
public interface Constants {

    String PREFS_KEY = "prefs.key";
    String PREFS_EMAIL = "prefs.email";
    String PREFS_USERID = "prefs.userId";
    String PREFS_TOKEN = "prefs.token";

    String API_ENDPONT = "http://pomoapp.herokuapp.com/api";

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "737148429708";

    /**
     * Substitute your own application ID here. This is the id of the
     * application you registered in your LoopBack server by calling
     * Application.register().
     */
    String LOOPBACK_APP_ID = "thePomoAppId";

    int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    int NOTIFICATION_SERVER_SESSION_ID = 1;
    int NOTIFICATION_START_ID = 2;

}
