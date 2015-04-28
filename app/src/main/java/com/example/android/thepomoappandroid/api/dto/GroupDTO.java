package com.example.android.thepomoappandroid.api.dto;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

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
    @Expose
    private List<PersonDTO> people = new ArrayList<PersonDTO>();

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

    /**
     *
     * @return
     * The people
     */
    public List<PersonDTO> getPeople() {
        return people;
    }

    /**
     *
     * @param people
     * The people
     */
    public void setPeople(List<PersonDTO> people) {
        this.people = people;
    }

}