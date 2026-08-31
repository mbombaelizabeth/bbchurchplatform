package com.church.model;

import java.io.Serializable;


public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ROLE_MEMBER = "Member";
    public static final String ROLE_LEADER = "Church Leader";

    private String username;
    private String password;
    private String role;

    public User() {
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', role='" + role + "'}";
    }
}
