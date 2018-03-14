package com.android.flashbackmusicv000;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import java.util.UUID;

public class UserBuilder implements IUserBuilder  {
    private String username;
    private String email;
    private int ID = -1;

    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setID(int id) {
        this.ID = id;
    }

    public User build() {
        User user;
        if (ID != -1) {
            //create an anonymous username
            HashMap map = new HashMap();
            int val = this.ID;
            String username = map.hash(val);
            user = new AnonymousUser(username, val);
        }
        else {
            //create a not-anonymous user
            user = new SignedInUser(username, email);
        }
        return user;
    }
}
