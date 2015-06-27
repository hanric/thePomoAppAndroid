package com.example.android.thepomoappandroid.api.dto;

import com.google.gson.annotations.Expose;

//@Generated("org.jsonschema2pojo")
public class SessionDTO {

    @Expose
    private String name;
    @Expose
    private int nPomos;
    @Expose
    private String startTime;
    @Expose
    private String endTime;
    @Expose
    private boolean isStopped;
    @Expose
    private boolean isFinished;
    @Expose
    private int id;
    @Expose
    private int groupId;
    @Expose
    private int settingId;

    public SessionDTO(String name, int nPomos, String startTime, String endTime, int groupId, Integer settingId) {
        this.name = name;
        this.nPomos = nPomos;
        this.startTime = startTime;
        this.endTime = endTime;
        this.groupId = groupId;
        if (null != settingId) this.settingId = settingId;
        isStopped = true;
        isFinished = false;
        this.id = 0;
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