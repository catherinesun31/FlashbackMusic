package com.android.flashbackmusicv000;

/**
 * Created by cailintreseder on 3/7/18.
 */

public class AnonymousUser implements IUser {
    private String email;
    private String username;

    AnonymousUser() {

    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addFriend() {

    }
}
