package com.example.android.thepomoappandroid.db;

import com.example.android.thepomoappandroid.api.dto.SessionDTO;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Enric on 09/05/2015.
 */
public class Session extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;
    private int nPomos;
    private String startTime;
    private String endTime;
    private boolean isStopped;
    private boolean isFinished;
    private int groupId;
    private int settingId;

    public Session() {

    }

    public Session(SessionDTO sessionDTO) {
        this.name = sessionDTO.getName();
        this.nPomos = sessionDTO.getNPomos();
        this.startTime = sessionDTO.getStartTime();
        this.endTime = sessionDTO.getEndTime();
        this.groupId = sessionDTO.getGroupId();
        this.settingId = sessionDTO.getSettingId();
        isStopped = sessionDTO.isIsStopped();
        isFinished = sessionDTO.isIsFinished();
        this.id = sessionDTO.getId();
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The nPomos
     */
    public int getNPomos() {
        return nPomos;
    }

    /**
     * @param nPomos The nPomos
     */
    public void setNPomos(int nPomos) {
        this.nPomos = nPomos;
    }

    /**
     * @return The startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime The startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return The endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime The endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return The isStopped
     */
    public boolean isIsStopped() {
        return isStopped;
    }

    /**
     * @param isStopped The isStopped
     */
    public void setIsStopped(boolean isStopped) {
        this.isStopped = isStopped;
    }

    /**
     * @return The isFinished
     */
    public boolean isIsFinished() {
        return isFinished;
    }

    /**
     * @param isFinished The isFinished
     */
    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The groupId
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * @param groupId The groupId
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    /**
     * @return The settingId
     */
    public int getSettingId() {
        return settingId;
    }

    /**
     * @param settingId The settingId
     */
    public void setSettingId(int settingId) {
        this.settingId = settingId;
    }
}
