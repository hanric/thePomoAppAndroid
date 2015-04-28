package com.example.android.thepomoappandroid;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Enric on 22/04/2015.
 */
public class Utils {
    private static Utils ourInstance = new Utils();

    public static Utils getInstance() {
        return ourInstance;
    }

    private Utils() {
    }

    public SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(Constants.PREFS_KEY, Context.MODE_PRIVATE);
    }

    public int getUserId(Context context) {
        return getPrefs(context).getInt(Constants.PREFS_USERID, 0);
    }

    public String getEmail(Context context) {
        return getPrefs(context).getString(Constants.PREFS_EMAIL, "");
    }

    public String getToken(Context context) {
        return getPrefs(context).getString(Constants.PREFS_TOKEN, "");
    }

    public boolean isLoggedIn (Context context) {
        String token = getToken(context);
        return (token != null && !token.isEmpty());
    }
}
