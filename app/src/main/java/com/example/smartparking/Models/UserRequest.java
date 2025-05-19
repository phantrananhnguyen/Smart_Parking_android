package com.example.smartparking.Models;

public class UserRequest {
    private String Name;
    private String email;
    private String password;

    public UserRequest(String Name, String email, String password) {
        this.Name = Name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
