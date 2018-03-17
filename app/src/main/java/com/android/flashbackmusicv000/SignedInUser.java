package com.android.flashbackmusicv000;

import java.util.ArrayList;

public class SignedInUser extends User {
    private String email;
    private String username;
    private int ID = -1;
    //private ArrayList<User> friends;

    SignedInUser(String username, String email) {
        this.username = username;
        this.email = email;
        //friends = new ArrayList<User>();
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public void addFriend() {

    }
}
