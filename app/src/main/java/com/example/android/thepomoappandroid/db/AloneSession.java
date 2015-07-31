package com.example.android.thepomoappandroid.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Enric on 11/04/2015.
 */
public class AloneSession extends RealmObject {

    @PrimaryKey
    private String name;
    private int num;
    private int state;
    private String settingUuid;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSettingUuid() {
        return settingUuid;
    }

    public void setSettingUuid(String settingUuid) {
        this.settingUuid = settingUuid;
    }
}
