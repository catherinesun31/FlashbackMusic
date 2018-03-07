package com.android.flashbackmusicv000;

/**
 * Created by cailintreseder on 3/5/18.
 */

public interface IUserBuilder {
    String username = "Unknown username";
    String email = "Unknown email";

    abstract void setUsername(String username);
    abstract void setEmail(String email);
    abstract IUser build();
}
