package com.example.android.thepomoappandroid.api.dto;

import com.google.gson.annotations.Expose;

//@Generated("org.jsonschema2pojo")
public class SettingDTO {

    @Expose
    private String name;
    @Expose
    private int workTime;
    @Expose
    private int restTime;
    @Expose
    private int largeRestTime;
    @Expose
    private int id;
    @Expose
    private int personId;

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
     * @return The workTime
     */
    public int getWorkTime() {
        return workTime;
    }

    /**
     * @param workTime The workTime
     */
    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    /**
     * @return The restTime
     */
    public int getRestTime() {
        return restTime;
    }

    /**
     * @param restTime The restTime
     */
    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    /**
     * @return The largeRestTime
     */
    public int getLargeRestTime() {
        return largeRestTime;
    }

    /**
     * @param largeRestTime The largeRestTime
     */
    public void setLargeRestTime(int largeRestTime) {
        this.largeRestTime = largeRestTime;
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
     * @return The personId
     */
    public int getPersonId() {
        return personId;
    }

    /**
     * @param personId The personId
     */
    public void setPersonId(int personId) {
        this.personId = personId;
    }

}