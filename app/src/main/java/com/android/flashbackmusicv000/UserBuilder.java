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

    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    private int createID() {
        int id = UUID.randomUUID().hashCode();
        SignInActivity activity = new SignInActivity();
        SharedPreferences preferences = activity.getSharedPrefs();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Id", id);
        editor.apply();
        return id;
    }

    public User build() {
        User user;
        if (email == null) {
            //create an anonymous user
            HashMap map = new HashMap();
            int val = createID();
            String username = map.hash(val);
            user = new AnonymousUser(username);

        }
        else {
            //create a not-anonymous user
            user = new SignedInUser(username, email);
        }
        return user;
    }
}
