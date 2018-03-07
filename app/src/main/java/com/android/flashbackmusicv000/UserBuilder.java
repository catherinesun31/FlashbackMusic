package com.android.flashbackmusicv000;

/**
 * Created by cailintreseder on 3/5/18.
 */

public class UserBuilder implements IUserBuilder {
    private String username;
    private String email;

    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    String getUsername() { return this.username; }
    String getEmail() { return this.email; }

    public IUser build() {
        String un = "";
        if (username != null) un = username;
        else {
            //Set username to be anonymous fruit
        }
        String em = "";
        if (email != null) em = email;
        else {
            //set anonymous email
        }
        User user = new User(em, un);
        return user;
    }
}
