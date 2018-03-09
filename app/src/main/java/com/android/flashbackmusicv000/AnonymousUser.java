package com.android.flashbackmusicv000;

public class AnonymousUser extends User {
    private String email;
    private String username;

    AnonymousUser() {
        email = "anonymous@email.com";
        //set username as a random value from the phone
    }

    public String getEmail() {
        return this.email;
    }
    public String getUsername() {
        return this.username;
    }

}
