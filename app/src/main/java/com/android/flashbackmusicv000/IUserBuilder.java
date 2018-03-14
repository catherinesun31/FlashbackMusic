package com.android.flashbackmusicv000;

import android.content.SharedPreferences;

public interface IUserBuilder {
    void setUsername(String username);
    void setEmail(String email);
    void setID(int id);
    User build();
}
