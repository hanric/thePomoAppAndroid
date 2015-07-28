package com.example.android.thepomoappandroid.db;

import com.example.android.thepomoappandroid.api.dto.SettingDTO;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hanric on 25/7/15.
 */
public class Setting extends RealmObject {
    @PrimaryKey
    private String uuid;
    private int id;
    private String name;
    private int workTime;
    private int restTime;
    private int largeRestTime;
    private int personId;

    public Setting() {

    }

    public Setting(SettingDTO settingDTO) {
        this.uuid = UUID.randomUUID().toString();
        this.id = settingDTO.getId();
        this.name = settingDTO.getName();
        this.workTime = settingDTO.getWorkTime();
        this.restTime = settingDTO.getRestTime();
        this.largeRestTime = settingDTO.getLargeRestTime();
        this.personId = settingDTO.getPersonId();
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public int getLargeRestTime() {
        return largeRestTime;
    }

    public void setLargeRestTime(int largeRestTime) {
        this.largeRestTime = largeRestTime;
    }
}
