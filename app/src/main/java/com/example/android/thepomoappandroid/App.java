package com.example.android.thepomoappandroid;

import android.app.Application;

import com.strongloop.android.loopback.RestAdapter;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by Enric on 09/05/2015.
 */
public class App extends Application {
    RestAdapter adapter;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }

    public RestAdapter getLoopBackAdapter() {
        if (adapter == null) {
            // Instantiate the shared RestAdapter. In most circumstances,
            // you'll do this only once; putting that reference in a singleton
            // is recommended for the sake of simplicity.
            // However, some applications will need to talk to more than one
            // server - create as many Adapters as you need.
            adapter = new RestAdapter(
                    getApplicationContext(),
                    "http://localhost:3000/api/");
        }
        return adapter;
    }
}
