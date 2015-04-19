package com.example.android.thepomoappandroid.db;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

/**
 * Created by Enric on 19/04/2015.
 */
public class DBHandler {

    private Realm realm;

    public DBHandler() {

    }

    public static DBHandler newInstance(Context context) {
        return new DBHandler(context);
    }

    public DBHandler(Context context) {
        realm = Realm.getInstance(context);
    }

    public void createAloneSession(final String name, final int num) throws RealmException {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    AloneSession aloneSession = realm.createObject(AloneSession.class);
                    aloneSession.setName(name);
                    aloneSession.setNum(num);
                }
            });
        } catch (RealmException e) {
            throw e;
        }

    }

    public AloneSession getAloneSession(String name) {
        RealmQuery<AloneSession> query = realm.where(AloneSession.class);
        query.equalTo("name", name);
        return query.findFirst();
    }

    public void updateAloneSession(final String name, final String newName, final int newNum) {
        AloneSession aloneSession = getAloneSession(name);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AloneSession aloneSession = realm.createObject(AloneSession.class);
                aloneSession.setName(newName);
                aloneSession.setNum(newNum);
            }
        });
    }

    public void deleteAloneSession(String name) {
        AloneSession aloneSession = getAloneSession(name);
        aloneSession.removeFromRealm();
    }

    public RealmResults<AloneSession> getAloneSessions() {
        return realm.where(AloneSession.class).findAll();
    }
}
