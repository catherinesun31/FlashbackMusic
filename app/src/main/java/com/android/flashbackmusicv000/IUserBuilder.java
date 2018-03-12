package com.android.flashbackmusicv000;

public interface IUserBuilder {
    String username = "Unknown username";
    String email = "Unknown email";

    void setUsername(String username);
    void setEmail(String email);
    User build();
}
