package com.example.android.thepomoappandroid.dto;

import com.google.gson.annotations.Expose;

//@Generated("org.jsonschema2pojo")
public class GroupDTO {

    @Expose
    private String name;
    @Expose
    private String description;
    @Expose
    private int adminId;
    @Expose
    private int id;

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
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The adminId
     */
    public int getAdminId() {
        return adminId;
    }

    /**
     * @param adminId The adminId
     */
    public void setAdminId(int adminId) {
        this.adminId = adminId;
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

}