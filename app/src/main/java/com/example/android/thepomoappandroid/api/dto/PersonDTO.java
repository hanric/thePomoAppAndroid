package com.example.android.thepomoappandroid.api.dto;

import com.google.gson.annotations.Expose;

//@Generated("org.jsonschema2pojo")
public class PersonDTO {

    @Expose
    private Object realm;
    @Expose
    private String username;
    @Expose
    private Object credentials;
    @Expose
    private Object challenges;
    @Expose
    private String email;
    @Expose
    private Object emailVerified;
    @Expose
    private Object verificationToken;
    @Expose
    private Object status;
    @Expose
    private Object created;
    @Expose
    private Object lastUpdated;
    @Expose
    private int id;

    /**
     * @return The realm
     */
    public Object getRealm() {
        return realm;
    }

    /**
     * @param realm The realm
     */
    public void setRealm(Object realm) {
        this.realm = realm;
    }

    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The credentials
     */
    public Object getCredentials() {
        return credentials;
    }

    /**
     * @param credentials The credentials
     */
    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }

    /**
     * @return The challenges
     */
    public Object getChallenges() {
        return challenges;
    }

    /**
     * @param challenges The challenges
     */
    public void setChallenges(Object challenges) {
        this.challenges = challenges;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The emailVerified
     */
    public Object getEmailVerified() {
        return emailVerified;
    }

    /**
     * @param emailVerified The emailVerified
     */
    public void setEmailVerified(Object emailVerified) {
        this.emailVerified = emailVerified;
    }

    /**
     * @return The verificationToken
     */
    public Object getVerificationToken() {
        return verificationToken;
    }

    /**
     * @param verificationToken The verificationToken
     */
    public void setVerificationToken(Object verificationToken) {
        this.verificationToken = verificationToken;
    }

    /**
     * @return The status
     */
    public Object getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Object status) {
        this.status = status;
    }

    /**
     * @return The created
     */
    public Object getCreated() {
        return created;
    }

    /**
     * @param created The created
     */
    public void setCreated(Object created) {
        this.created = created;
    }

    /**
     * @return The lastUpdated
     */
    public Object getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated The lastUpdated
     */
    public void setLastUpdated(Object lastUpdated) {
        this.lastUpdated = lastUpdated;
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