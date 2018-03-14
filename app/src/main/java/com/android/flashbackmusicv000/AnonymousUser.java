package com.android.flashbackmusicv000;

public class AnonymousUser extends User {
    private String email;
    private String username;
    private int ID;

    AnonymousUser(String username, int ID) {
        email = "anonymous@email.com";
        //set username as a random value from the phone
        this.username = "Anonymous " + username;
        this.ID = ID;
    }

    public String getEmail() {
        return this.email;
    }
    public String getUsername() {
        return this.username;
    }
    public int getID() {
        return this.ID;
    }
}
