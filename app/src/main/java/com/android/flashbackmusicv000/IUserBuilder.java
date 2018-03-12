package com.android.flashbackmusicv000;

/**
 * Created by cailintreseder on 3/5/18.
 */

public interface IUserBuilder {
    String username = "Unknown username";
    String email = "Unknown email";

    void setUsername(String username);
    void setEmail(String email);
    User build();
}
