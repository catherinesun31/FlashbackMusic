package com.android.flashbackmusicv000;

/**
 * Created by cailintreseder on 3/5/18.
 */

public interface IUser {
    String getEmail();
    String getUsername();
    void setEmail(String email);
    void setUsername(String username);
    void addFriend();
}
