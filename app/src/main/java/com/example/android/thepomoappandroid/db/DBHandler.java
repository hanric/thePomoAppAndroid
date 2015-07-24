package com.example.android.thepomoappandroid.db;

import android.content.Context;

import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.dto.SessionDTO;

import java.util.Calendar;
import java.util.GregorianCalendar;

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

    public void createAloneSession(final String name, final int num, final int state) throws RealmException {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AloneSession aloneSession = realm.createObject(AloneSession.class);
                aloneSession.setName(name);
                aloneSession.setNum(num);
                aloneSession.setState(state);
            }
        });
    }

    public AloneSession getAloneSession(String name) {
        RealmQuery<AloneSession> query = realm.where(AloneSession.class);
        query.equalTo("name", name);
        return query.findFirst();
    }

    public void updateAloneSession(final String name, final String newName, final int newNum, final int newState) {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    AloneSession aloneSession = getAloneSession(name);
                    aloneSession.setName(newName);
                    aloneSession.setNum(newNum);
                    aloneSession.setState(newState);
                }
            });
        } catch (RealmException e) {
            throw e;
        }
    }

    public void deleteAloneSession(final String name) throws RealmException {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    AloneSession aloneSession = getAloneSession(name);
                    aloneSession.removeFromRealm();
                }
            });
        } catch (RealmException e) {
            throw e;
        }
    }

    public RealmResults<AloneSession> getAloneSessions() {
        return realm.where(AloneSession.class).findAll();
    }


    //Session

    public void createSession(final SessionDTO sessionDTO) throws RealmException {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Session session = realm.createObject(Session.class);
                    session.setId(sessionDTO.getId());
                    session.setName(sessionDTO.getName());
                    session.setNPomos(sessionDTO.getNPomos());
                    session.setStartTime(sessionDTO.getStartTime());
                    session.setEndTime(sessionDTO.getEndTime());
                    session.setGroupId(sessionDTO.getGroupId());
                    session.setSettingId(sessionDTO.getSettingId());
                }
            });
        } catch (RealmException e) {
            throw e;
        }

    }

    public Session getSession(int id) {
        RealmQuery<Session> query = realm.where(Session.class);
        query.equalTo("id", id);
        return query.findFirst();
    }
//
//    public void updateAloneSession(final String name, final String newName, final int newNum) {
//        try {
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    AloneSession aloneSession = getAloneSession(name);
//                    aloneSession.setName(newName);
//                    aloneSession.setNum(newNum);
//                }
//            });
//        } catch (RealmException e) {
//            throw e;
//        }
//    }

    public void deleteSession(final int id) throws RealmException {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Session session = getSession(id);
                    if (session != null) {
                        session.removeFromRealm();
                    }
                }
            });
        } catch (RealmException e) {
            throw e;
        }
    }

    public RealmResults<Session> getSessions() {
        return realm.where(Session.class).findAll();
    }

    public RealmResults<Session> getSessionsByGroup(int groupId) {
        return realm.where(Session.class)
                .equalTo("groupId", groupId)
                .findAll();
    }


}
