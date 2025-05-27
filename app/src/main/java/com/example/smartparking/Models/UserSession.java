package com.example.smartparking.Models;

public class UserSession {
    private static UserSession instance;

    private String name;
    private String email;
    private String token;// nếu có

    private UserSession() {
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    // Optional: Clear session
    public void clearSession() {
        name = null;
        email = null;
        token = null;
    }
}
