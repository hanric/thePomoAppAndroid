package com.example.android.thepomoappandroid.api.request;

import com.google.gson.annotations.Expose;

public class LoginRequest {

    @Expose
    private String email;
    @Expose
    private String password;
    @Expose
    private String ttl;

    public LoginRequest(String username, String password, String ttl) {
        this.email = username;
        this.password = password;
        this.ttl = ttl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }
}