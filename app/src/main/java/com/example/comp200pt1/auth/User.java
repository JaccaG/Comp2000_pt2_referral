package com.example.comp200pt1.auth;

public class User {
    public final String username;
    public final String email;
    public final String role;

    // Set once user logs in - pulls from DB/shared prefs
    public User(String username, String email, String role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // simple getters

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
    public String getRole() {
        return role;
    }
}
