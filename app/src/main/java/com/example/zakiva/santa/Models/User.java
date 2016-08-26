package com.example.zakiva.santa.Models;

/**
 * Created by zakiva on 8/26/16.
 */

public class User {

    public String email;

    public User() {}

    public User (String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
