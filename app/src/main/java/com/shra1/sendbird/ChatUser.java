package com.shra1.sendbird;

/**
 * Created by Shrawan WABLE on 3/20/2018.
 */

public class ChatUser {
    String email;
    String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatUser() {

    }

    public ChatUser(String email, String name) {
        this.email = email;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
