package com.android.flashbackmusicv000;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import java.util.UUID;

public class UserBuilder implements IUserBuilder {
    private String username;
    private String email;

    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String createID() {
        return UUID.randomUUID().toString();
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
            HashMap map = new HashMap();


            String deviceId = createID();
            int val = Integer.parseInt(deviceId);
            String username = map.hash(val);
            user = new AnonymousUser(username);

        }
        else {
            //create a not-anonymous user
            user = new SignedInUser(username, email);
        }

        return null;
        //return user;
    }
}
