package com.example.android.thepomoappandroid.db;

import android.content.Context;

import com.example.android.thepomoappandroid.api.dto.SessionDTO;
import com.example.android.thepomoappandroid.api.dto.SettingDTO;

import java.util.List;
import java.util.UUID;

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

    public void createAloneSession(final String name, final int num, final int state, final String settingUuid) throws RealmException {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AloneSession aloneSession = realm.createObject(AloneSession.class);
                aloneSession.setName(name);
                aloneSession.setNum(num);
                aloneSession.setState(state);
                aloneSession.setSettingUuid(settingUuid);
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

    public void deleteBulkSessions(final List<Integer> ids) {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (Integer id : ids) {
                        Session session = getSession(id);
                        if (session != null) {
                            session.removeFromRealm();
                        }
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

    // Setting

    public void createSetting(final SettingDTO settingDTO) throws RealmException {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Setting setting = realm.createObject(Setting.class);
                    setting.setUuid(UUID.randomUUID().toString());
                    setting.setId(settingDTO.getId());
                    setting.setName(settingDTO.getName());
                    setting.setWorkTime(settingDTO.getWorkTime());
                    setting.setRestTime(settingDTO.getRestTime());
                    setting.setLargeRestTime(settingDTO.getLargeRestTime());
                    setting.setPersonId(settingDTO.getPersonId());
                }
            });
        } catch (RealmException e) {
            throw e;
        }

    }

    public void createSetting(final String name, final int work, final int rest, final int largeRest) throws RealmException {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Setting setting = realm.createObject(Setting.class);
                    setting.setUuid(UUID.randomUUID().toString());
                    setting.setId(-1);
                    setting.setName(name);
                    setting.setWorkTime(work);
                    setting.setRestTime(rest);
                    setting.setLargeRestTime(largeRest);
                }
            });
        } catch (RealmException e) {
            throw e;
        }

    }

    public Setting getSetting(int id) {
        RealmQuery<Setting> query = realm.where(Setting.class);
        query.equalTo("id", id);
        return query.findFirst();
    }

    public Setting getSetting(String uuid) {
        RealmQuery<Setting> query = realm.where(Setting.class);
        query.equalTo("uuid", uuid);
        return query.findFirst();
    }

    public RealmResults<Setting> getSettings() {
        return realm.where(Setting.class).findAll();
    }

    public RealmResults<Setting> getOnlyOnlineSettings() {
        RealmQuery<Setting> query = realm.where(Setting.class);
        query.notEqualTo("id", -1);
        return query.findAll();
    }

    public void deleteSettings() {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.clear(Setting.class);
                }
            });
        } catch (RealmException e) {
            throw e;
        }
    }
}
