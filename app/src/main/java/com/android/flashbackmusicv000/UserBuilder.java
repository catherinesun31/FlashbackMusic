package com.android.flashbackmusicv000;

/**
 * Created by cailintreseder on 3/5/18.
 */

public class UserBuilder extends User {
    private String username;
    private String email;

    private void setUsername(String username) {
        this.username = username;
    }
    private void setEmail(String email) {
        this.email = email;
    }

    public User build() {
        /*
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
        */
        User user;
        if (email.equals("")) {
            //create an anonymous user
            user = new AnonymousUser();
        }
        else {
            //create a not-anonymous user
            user = new SignedInUser(username, email);
        }

        return user;
    }
}
