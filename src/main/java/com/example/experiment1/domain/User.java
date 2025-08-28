package com.example.experiment1.domain;

public class User {
    private String userId;
    private String username;
    private String email;

    public User() {}

    public String getUserId() {
        return this.userId;
    }
    public void setUserId( String userId ) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername( String username ) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail( String email ) {
        this.email = email;
    }

}
