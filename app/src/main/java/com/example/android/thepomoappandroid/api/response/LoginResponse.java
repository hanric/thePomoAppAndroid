package com.example.android.thepomoappandroid.api.response;

import com.google.gson.annotations.Expose;

//@Generated("org.jsonschema2pojo")
public class LoginResponse {

    @Expose
    private String id;
    @Expose
    private int ttl;
    @Expose
    private String created;
    @Expose
    private int userId;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The ttl
     */
    public int getTtl() {
        return ttl;
    }

    /**
     * @param ttl The ttl
     */
    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    /**
     * @return The created
     */
    public String getCreated() {
        return created;
    }

    /**
     * @param created The created
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * @return The userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId The userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

}