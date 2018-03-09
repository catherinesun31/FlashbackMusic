package com.android.flashbackmusicv000;

import java.util.ArrayList;

/**
 * Created by cailintreseder on 3/5/18.
 */

public class SignedInUser extends User {
    private String email;
    private String username;
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

    public void setEmail(String em) {
        this.email = em;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addFriend() {

    }
}
