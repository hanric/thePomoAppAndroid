package com.example.android.thepomoappandroid.api.request;

import com.google.gson.annotations.Expose;

//@Generated("org.jsonschema2pojo")
public class CreateGroupRequest {

    @Expose
    private String name;
    @Expose
    private String description;
    @Expose
    private int id;

    public CreateGroupRequest(String name, String description) {
        this.name = name;
        this.description = description;
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