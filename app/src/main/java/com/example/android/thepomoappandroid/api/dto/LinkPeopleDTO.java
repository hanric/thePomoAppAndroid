package com.example.android.thepomoappandroid.api.dto;

import com.google.gson.annotations.Expose;

//@Generated("org.jsonschema2pojo")
public class LinkPeopleDTO {

    @Expose
    private int id;
    @Expose
    private int personId;
    @Expose
    private int groupId;

    public LinkPeopleDTO (int personId, int groupId) {
        this.personId = personId;
        this.groupId = groupId;
        id = 0;
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

}